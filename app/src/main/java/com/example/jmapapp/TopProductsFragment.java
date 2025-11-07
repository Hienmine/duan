package com.example.jmapapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jmapapp.adapter.TopProductsAdapter;
import com.example.jmapapp.datadao.baocaoDAO;
import com.example.jmapapp.model.TopProduct;

import java.util.ArrayList;

public class TopProductsFragment extends Fragment {

    private EditText edFrom, edTo, edLimit;
    private Spinner spStatus;
    private Button btnFilter;
    private RecyclerView rv;
    private TopProductsAdapter adapter;
    private baocaoDAO dao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_top_products, container, false);

        edFrom = v.findViewById(R.id.edFromTop);
        edTo   = v.findViewById(R.id.edToTop);
        edLimit= v.findViewById(R.id.edLimit);
        spStatus = v.findViewById(R.id.spStatus);
        btnFilter= v.findViewById(R.id.btnFilterTop);
        rv = v.findViewById(R.id.rvTopProducts);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        // Spinner trạng thái
        ArrayAdapter<CharSequence> stAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.invoice_status_filter, android.R.layout.simple_spinner_item);
        stAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(stAdapter);

        dao = new baocaoDAO(requireContext());

        // Mặc định: Top 10 toàn thời gian
        ArrayList<TopProduct> data = dao.getTopByQtyAll(10);
        adapter = new TopProductsAdapter(data);
        rv.setAdapter(adapter);

        btnFilter.setOnClickListener(view -> applyFilter());

        return v;
    }

    private void applyFilter() {
        String from = edFrom.getText().toString().trim();
        String to   = edTo.getText().toString().trim();
        String status = (String) spStatus.getSelectedItem();
        String limitStr = edLimit.getText().toString().trim();
        int limit = 10;
        if (!TextUtils.isEmpty(limitStr)) {
            try { limit = Math.max(1, Integer.parseInt(limitStr)); } catch (Exception ignored) {}
        }

        boolean hasFrom = !TextUtils.isEmpty(from);
        boolean hasTo   = !TextUtils.isEmpty(to);

        ArrayList<TopProduct> data;
        if (!hasFrom && !hasTo && ("Tất cả".equals(status) || TextUtils.isEmpty(status))) {
            data = dao.getTopByQtyAll(limit);
        } else {
            if (!hasFrom) from = "0000-01-01";
            if (!hasTo)   to   = "9999-12-31";
            data = dao.getTopByQty(from, to, status, limit);
        }
        adapter.setData(data);
    }
}
