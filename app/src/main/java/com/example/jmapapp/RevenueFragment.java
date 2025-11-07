package com.example.jmapapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jmapapp.adapter.ProductRevenueAdapter;
import com.example.jmapapp.datadao.baocaoDAO;
import com.example.jmapapp.model.ProductRevenue;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RevenueFragment extends Fragment {

    private EditText edFrom, edTo;
    private Button btnFilter;
    private TextView tvTotal;
    private RecyclerView rv;
    private ProductRevenueAdapter adapter;
    private baocaoDAO dao;
    private final NumberFormat nf = NumberFormat.getInstance(new Locale("vi","VN"));

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_revenue, container, false);

        edFrom = v.findViewById(R.id.edFrom);
        edTo   = v.findViewById(R.id.edTo);
        btnFilter = v.findViewById(R.id.btnFilter);
        tvTotal = v.findViewById(R.id.tvTotal);
        rv = v.findViewById(R.id.rvRevenue);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        dao = new baocaoDAO(requireContext());

        // mặc định: toàn thời gian
        ArrayList<ProductRevenue> data = dao.getRevenueAll();
        adapter = new ProductRevenueAdapter(data);
        rv.setAdapter(adapter);
        tvTotal.setText("Tổng doanh thu: " + nf.format(dao.getTotalRevenueAll()) + " đ");

        btnFilter.setOnClickListener(view -> applyFilter());

        return v;
    }

    private void applyFilter() {
        String from = edFrom.getText().toString().trim();
        String to   = edTo.getText().toString().trim();

        boolean hasFrom = !TextUtils.isEmpty(from);
        boolean hasTo   = !TextUtils.isEmpty(to);

        if (!hasFrom && !hasTo) {
            // Không nhập gì -> toàn thời gian
            adapter.setData(dao.getRevenueAll());
            tvTotal.setText("Tổng doanh thu: " + nf.format(dao.getTotalRevenueAll()) + " đ");
            return;
        }

        // nếu chỉ nhập 1 đầu, mình vẫn chấp nhận: from (to = 9999...), to (from = 0000...)
        if (!hasFrom) from = "0000-01-01";
        if (!hasTo)   to   = "9999-12-31";

        ArrayList<ProductRevenue> data = dao.getRevenueByDate(from, to);
        adapter.setData(data);

        double total = dao.getTotalRevenueByDate(from, to);
        tvTotal.setText("Tổng doanh thu: " + nf.format(total) + " đ");
    }
}
