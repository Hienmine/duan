package com.example.jmapapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jmapapp.R;
import com.example.jmapapp.model.TopProduct;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TopProductsAdapter extends RecyclerView.Adapter<TopProductsAdapter.VH> {
    private ArrayList<TopProduct> data;
    private final NumberFormat nf = NumberFormat.getInstance(new Locale("vi","VN"));

    public TopProductsAdapter(ArrayList<TopProduct> data) {
        this.data = data;
    }

    public void setData(ArrayList<TopProduct> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_product, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        TopProduct it = data.get(position);
        int rank = position + 1;
        h.tvRank.setText("#" + rank);
        h.tvName.setText(it.getTenSanPham());
        h.tvQty.setText("Đã bán: " + it.getTongSoLuong());
        h.tvRevenue.setText("Doanh thu: " + nf.format(it.getDoanhThu()) + " đ");
    }

    @Override
    public int getItemCount() { return data == null ? 0 : data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvRank, tvName, tvQty, tvRevenue;
        VH(@NonNull View v) {
            super(v);
            tvRank    = v.findViewById(R.id.tvRank);
            tvName    = v.findViewById(R.id.tvName);
            tvQty     = v.findViewById(R.id.tvQty);
            tvRevenue = v.findViewById(R.id.tvRevenue);
        }
    }
}
