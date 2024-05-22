package com.example.praktikum10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.praktikum10.api.ApiConfig;
import com.example.praktikum10.model.UpdateMahasiswaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateMahasiswaActivity extends AppCompatActivity {

    private EditText edtNrp, edtNama, edtEmail, edtJurusan;
    private Button btnSave;

    private String mahasiswaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mahasiswa);

        edtNrp = findViewById(R.id.edtNrp);
        edtNama = findViewById(R.id.edtNama);
        edtEmail = findViewById(R.id.edtEmail);
        edtJurusan = findViewById(R.id.edtJurusan);
        btnSave = findViewById(R.id.btUpdate);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mahasiswaId = extras.getString("mahasiswa_id");
            String nrp = extras.getString("nrp");
            String nama = extras.getString("nama");
            String email = extras.getString("email");
            String jurusan = extras.getString("jurusan");

            edtNrp.setText(nrp);
            edtNama.setText(nama);
            edtEmail.setText(email);
            edtJurusan.setText(jurusan);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMahasiswa();
            }
        });
    }

    private void updateMahasiswa() {
        String nrp = edtNrp.getText().toString().trim();
        String nama = edtNama.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String jurusan = edtJurusan.getText().toString().trim();

        if (nrp.isEmpty() || nama.isEmpty() || email.isEmpty() || jurusan.isEmpty()) {
            Toast.makeText(this, "Harap isi semua bidang", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<UpdateMahasiswaResponse> call = ApiConfig.getApiService().updateMahasiswa(mahasiswaId, nrp, nama, email, jurusan);
        call.enqueue(new Callback<UpdateMahasiswaResponse>() {
            @Override
            public void onResponse(Call<UpdateMahasiswaResponse> call, Response<UpdateMahasiswaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(UpdateMahasiswaActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(UpdateMahasiswaActivity.this, "Gagal memperbarui mahasiswa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateMahasiswaResponse> call, Throwable t) {
                Toast.makeText(UpdateMahasiswaActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
