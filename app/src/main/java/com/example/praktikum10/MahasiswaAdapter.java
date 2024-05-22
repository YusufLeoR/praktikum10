package com.example.praktikum10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikum10.model.Mahasiswa;

import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder> {

    private List<Mahasiswa> mahasiswaList;

    private OnDeleteClickListener onDeleteClickListener;
    private OnUpdateClickListener onUpdateClickListener;

    public MahasiswaAdapter(List<Mahasiswa> mahasiswaList, OnDeleteClickListener onDeleteClickListener, OnUpdateClickListener onUpdateClickListener) {
        this.mahasiswaList = mahasiswaList;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onUpdateClickListener = onUpdateClickListener;
    }
    @NonNull
    @Override
    public MahasiswaAdapter.MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_layout, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaAdapter.MahasiswaViewHolder holder, int position) {
        Mahasiswa mahasiswa = mahasiswaList.get(position);
        holder.tvNrp.setText(mahasiswa.getNrp());
        holder.tvNama.setText(mahasiswa.getNama());
        holder.tvEmail.setText(mahasiswa.getEmail());
        holder.tvJurusan.setText(mahasiswa.getJurusan());

        holder.ivDelete.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(mahasiswa, position));
        holder.ivUpdate.setOnClickListener(v -> onUpdateClickListener.onUpdateClick(mahasiswa));
    }

    @Override
    public int getItemCount() {
        return mahasiswaList.size();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Mahasiswa mahasiswa, int position);
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(Mahasiswa mahasiswa);
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder {

        TextView tvNrp, tvNama, tvEmail, tvJurusan;
        ImageView ivDelete, ivUpdate;
        public MahasiswaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNrp = itemView.findViewById(R.id.tvValNrp);
            tvNama = itemView.findViewById(R.id.tvValNama);
            tvEmail = itemView.findViewById(R.id.tvValEmail);
            tvJurusan = itemView.findViewById(R.id.tvValJurusan);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivUpdate = itemView.findViewById(R.id.ivUpdate);
        }
    }
}
