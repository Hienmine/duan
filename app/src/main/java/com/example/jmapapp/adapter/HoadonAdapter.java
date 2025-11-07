package com.example.jmapapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jmapapp.R;
import com.example.jmapapp.model.modelhoadon;

import java.util.ArrayList;

public class HoadonAdapter extends RecyclerView.Adapter<HoadonAdapter.HoaDonVH> {

    public interface OnHoaDonAction {
        void onEdit(modelhoadon hd);
        void onDelete(modelhoadon hd);
    }

    private ArrayList<modelhoadon> list;
    private final OnHoaDonAction listener;

    public HoadonAdapter(ArrayList<modelhoadon> list, OnHoaDonAction listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HoaDonVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hoa_don, parent, false);
        return new HoaDonVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HoaDonVH h, int position) {
        modelhoadon hd = list.get(position);
        h.tvMaHD.setText("Mã HD: " + hd.getIdhoadon());
        h.tvNgayLap.setText("Ngày lập: " + hd.getNgaylap());
        h.tvTrangThai.setText("Trạng thái: " + hd.getTrangthai());
        h.tvUser.setText("ID user: " + hd.getIduser());

        h.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(hd);
        });
        h.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(hd);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setData(ArrayList<modelhoadon> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    static class HoaDonVH extends RecyclerView.ViewHolder {
        TextView tvMaHD, tvNgayLap, tvTrangThai, tvUser;
        Button btnEdit, btnDelete;

        public HoaDonVH(@NonNull View itemView) {
            super(itemView);
            tvMaHD     = itemView.findViewById(R.id.tvMaHD);
            tvNgayLap  = itemView.findViewById(R.id.tvNgayLap);
            tvTrangThai= itemView.findViewById(R.id.tvTrangThai);
            tvUser     = itemView.findViewById(R.id.tvUser);
            btnEdit    = itemView.findViewById(R.id.btnEdit);
            btnDelete  = itemView.findViewById(R.id.btnDelete);
        }
    }
}
