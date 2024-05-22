package com.example.praktikum10;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.praktikum10.api.ApiConfig;
import com.example.praktikum10.model.DeleteMahasiswaResponse;
import com.example.praktikum10.model.Mahasiswa;
import com.example.praktikum10.model.MahasiswaResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMahasiswaActivity extends AppCompatActivity implements MahasiswaAdapter.OnDeleteClickListener, MahasiswaAdapter.OnUpdateClickListener {
    private EditText edtChecNrp;
    private Button btnSearch;
    private ProgressBar progressBar;
    private TextView tvNrp;
    private TextView tvNama;
    private TextView tvEmail;
    private TextView tvJurusan;
    private RecyclerView recyclerView;
    private MahasiswaAdapter adapter;
    private List<Mahasiswa> mahasiswaList;
    private static final int UPDATE_MAHASISWA_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_mahasiswa);

        edtChecNrp = findViewById(R.id.edtChckNrp);
        btnSearch = findViewById(R.id.btnSearch);
        progressBar = findViewById(R.id.progressBar);
        tvNrp = findViewById(R.id.tvValNrp);
        tvNama = findViewById(R.id.tvValNama);
        tvEmail = findViewById(R.id.tvValEmail);
        tvJurusan = findViewById(R.id.tvValJurusan);
        recyclerView = findViewById(R.id.recycleView);
        mahasiswaList = new ArrayList<>();
        adapter = new MahasiswaAdapter(mahasiswaList, this, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadAllMahasiswa();

        btnSearch.setOnClickListener(view -> {
            showLoading(true);
            String nrp = edtChecNrp.getText().toString();
            if (nrp.isEmpty()){
                edtChecNrp.setError("Silahakan Isi nrp terlebih dahulu");
                showLoading(false);
            } else {
                searchMahasiswa(nrp);
            }
        });
    }

    private void loadAllMahasiswa() {
        Call<MahasiswaResponse> client = ApiConfig.getApiService().getAllMahasiswa();
        client.enqueue(new Callback<MahasiswaResponse>() {
            @Override
            public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    mahasiswaList.clear();
                    mahasiswaList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("Error", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
                Log.e("Error Retrofit","onFailure: " + t.getMessage());
            }
        });
    }

    private void searchMahasiswa(String nrp) {
        Call<MahasiswaResponse> client = ApiConfig.getApiService().getMahasiswa(nrp);
        client.enqueue(new Callback<MahasiswaResponse>() {
            @Override
            public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    showLoading(false);
                    Mahasiswa mahasiswa = response.body().getData().get(0);
                    tvNrp.setText(mahasiswa.getNrp());
                    tvNama.setText(mahasiswa.getNama());
                    tvEmail.setText(mahasiswa.getEmail());
                    tvJurusan.setText(mahasiswa.getJurusan());
                } else {
                    showLoading(false);
                    Log.e("Error", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
                showLoading(false);
                Log.e("Error Retrofit","onFailure: " + t.getMessage());
            }
        });
    }

    private void deleteMahasiswa(Mahasiswa mahasiswa, int position) {
        Log.d("DeleteMahasiswa", "Deleting ID: " + mahasiswa.getId());
        Call<DeleteMahasiswaResponse> client = ApiConfig.getApiService().deleteMahasiswa(mahasiswa.getId());
        client.enqueue(new Callback<DeleteMahasiswaResponse>() {
            @Override
            public void onResponse(Call<DeleteMahasiswaResponse> call, Response<DeleteMahasiswaResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    mahasiswaList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(SearchMahasiswaActivity.this, "Mahasiswa deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Error", "onFailure: " + response.message());
                    Toast.makeText(SearchMahasiswaActivity.this, "Failed to delete mahasiswa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteMahasiswaResponse> call, Throwable t) {
                Log.e("Error Retrofit","onFailure: " + t.getMessage());
                Toast.makeText(SearchMahasiswaActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onDeleteClick(Mahasiswa mahasiswa, int position) {
        deleteMahasiswa(mahasiswa, position);
    }

    @Override
    public void onUpdateClick(Mahasiswa mahasiswa) {
        Intent intent = new Intent(this, UpdateMahasiswaActivity.class);
        intent.putExtra("mahasiswa_id", mahasiswa.getId());
        intent.putExtra("nrp", mahasiswa.getNrp());
        intent.putExtra("nama", mahasiswa.getNama());
        intent.putExtra("email", mahasiswa.getEmail());
        intent.putExtra("jurusan", mahasiswa.getJurusan());
        startActivityForResult(intent, UPDATE_MAHASISWA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_MAHASISWA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                loadAllMahasiswa();
            }
        }
    }

    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
