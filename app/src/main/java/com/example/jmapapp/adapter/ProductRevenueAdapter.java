package com.example.jmapapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jmapapp.R;
import com.example.jmapapp.model.ProductRevenue;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductRevenueAdapter extends RecyclerView.Adapter<ProductRevenueAdapter.VH> {
    private ArrayList<ProductRevenue> data;
    private final NumberFormat nf = NumberFormat.getInstance(new Locale("vi","VN"));

    public ProductRevenueAdapter(ArrayList<ProductRevenue> data) {
        this.data = data;
    }

    public void setData(ArrayList<ProductRevenue> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_revenue, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        ProductRevenue pr = data.get(position);
        h.tvName.setText(pr.getTenSanPham());
        h.tvQty.setText("SL: " + pr.getTongSoLuong());
        h.tvMoney.setText("Doanh thu: " + nf.format(pr.getDoanhThu()) + " đ");
    }

    @Override
    public int getItemCount() { return data == null ? 0 : data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvMoney;
        VH(@NonNull View v) {
            super(v);
            tvName  = v.findViewById(R.id.tvName);
            tvQty   = v.findViewById(R.id.tvQty);
            tvMoney = v.findViewById(R.id.tvMoney);
        }
    }
}
