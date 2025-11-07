package com.example.jmapapp;

import androidx.fragment.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jmapapp.datadao.hoadondao;
import com.example.jmapapp.datadao.HoadonchitietDao;
import java.util.ArrayList;

public class CartFragmentKh  extends Fragment {
    private static final String PREFS = "app_prefs";
    private static final String KEY_USER_ID = "logged_user_id";

    private hoadondao hdDao;
    private HoadonchitietDao ctDao;

    private Integer currentCartId = null;
    private ArrayList<HoadonchitietDao.CartItem> data = new ArrayList<>();
    private RecyclerView rv;
    private TextView tvEmpty, tvTotal;
    private Button btnPay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cartkh, container, false);

        rv = v.findViewById(R.id.rvCart);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        tvTotal = v.findViewById(R.id.tvTotal);
        btnPay  = v.findViewById(R.id.btnPay);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(new RecyclerView.Adapter<CartVH>() {
            @NonNull @Override public CartVH onCreateViewHolder(@NonNull ViewGroup p, int vt) {
                View row = LayoutInflater.from(p.getContext())
                        .inflate(android.R.layout.simple_list_item_2, p, false);
                return new CartVH(row);
            }
            @Override public void onBindViewHolder(@NonNull CartVH h, int i) {
                var it = data.get(i);
                h.title.setText(it.tenSP + " x" + it.soLuong);
                h.sub.setText(String.format("đơn giá: %.0f  |  thành tiền: %.0f", it.donGia, it.thanhTien));
            }
            @Override public int getItemCount(){ return data.size(); }
        });

        hdDao = new hoadondao(requireContext());
        ctDao = new HoadonchitietDao(requireContext());

        loadCart();

        btnPay.setOnClickListener(vw -> {
            if (currentCartId == null) return;
            int n = hdDao.updateStatus(currentCartId, "đã thanh toán");
            if (n > 0) {
                Toast.makeText(requireContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                loadCart(); // reload -> trống giỏ
            } else {
                Toast.makeText(requireContext(), "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void loadCart() {
        int uid = requireContext().getSharedPreferences(PREFS, 0).getInt(KEY_USER_ID, -1);
        if (uid == -1) {
            showEmpty("Bạn chưa đăng nhập");
            return;
        }
        currentCartId = hdDao.getOpenCartId(uid);
        if (currentCartId == null) {
            showEmpty("Giỏ hàng trống");
            return;
        }
        data.clear();
        data.addAll(ctDao.listItems(currentCartId));
        rv.getAdapter().notifyDataSetChanged();

        double total = 0;
        for (var it : data) total += it.thanhTien;
        tvTotal.setText(String.format("Tổng: %.0f", total));

        rv.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        btnPay.setEnabled(!data.isEmpty());
    }

    private void showEmpty(String msg) {
        rv.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
        tvEmpty.setText(msg);
        tvTotal.setText("Tổng: 0");
        btnPay.setEnabled(false);
    }

    static class CartVH extends RecyclerView.ViewHolder {
        TextView title, sub;
        CartVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(android.R.id.text1);
            sub   = itemView.findViewById(android.R.id.text2);
        }
    }
}
