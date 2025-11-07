package com.example.jmapapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jmapapp.R;
import com.example.jmapapp.model.modelsanpham;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SanPhamKhachAdapter extends RecyclerView.Adapter<SanPhamKhachAdapter.VH> {

    public interface OnProductClick {
        void onClick(modelsanpham sp);
    }

    private final Context context;
    private final ArrayList<modelsanpham> data;
    private final OnProductClick listener;
    private final NumberFormat currency = NumberFormat.getInstance(new Locale("vi", "VN"));

    public SanPhamKhachAdapter(Context context, ArrayList<modelsanpham> data, OnProductClick listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    public void replaceAll(ArrayList<modelsanpham> newData) {
        data.clear();
        if (newData != null) data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemsp_khach, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        modelsanpham sp = data.get(position);
        h.txtName.setText(sp.getNamesp());
        h.txtMoTa.setText(sp.getMota() == null ? "" : sp.getMota());
        h.txtGia.setText(currency.format(sp.getGia()) + " đ");

        if (sp.getImg() != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(sp.getImg(), 0, sp.getImg().length);
            h.img.setImageBitmap(bm);
        } else {
            h.img.setImageResource(R.drawable.ic_launcher_background);
        }

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(sp);
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName, txtMoTa, txtGia;
        VH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgSPk);
            txtName = itemView.findViewById(R.id.txtnamespk);
            txtMoTa = itemView.findViewById(R.id.txtmotaspk);
            txtGia  = itemView.findViewById(R.id.txtgiaspk);
        }
    }
}
