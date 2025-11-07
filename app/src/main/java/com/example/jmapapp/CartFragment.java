package com.example.jmapapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.example.jmapapp.datadao.hoadondao;
import com.example.jmapapp.model.CartItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartFragment extends Fragment {

    private static final String PREFS = "app_prefs";
    private static final String KEY_USER_ID = "logged_user_id";

    private RecyclerView rv;
    private TextView tvTotal, tvEmpty;
    private Button btnCheckout;

    private hoadondao dao;
    private CartAdapter adapter;
    private Integer idhoadon;
    private final NumberFormat nf = NumberFormat.getInstance(new Locale("vi","VN"));

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        rv = v.findViewById(R.id.rvCart);
        tvTotal = v.findViewById(R.id.tvTotal);
        btnCheckout = v.findViewById(R.id.btnCheckout);
        tvEmpty = v.findViewById(R.id.tvEmpty);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        dao = new hoadondao(requireContext());

        int uid = requireContext().getSharedPreferences(PREFS, 0).getInt(KEY_USER_ID, -1);
        if (uid == -1) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return v;
        }
        idhoadon = dao.getOpenCartId(uid);

        loadData();

        btnCheckout.setOnClickListener(view -> {
            if (idhoadon == null) {
                Toast.makeText(requireContext(), "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean ok = dao.checkout(idhoadon);
            if (ok) {
                Toast.makeText(requireContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                // reload để thấy giỏ trống
                idhoadon = dao.getOpenCartId(uid); // có thể null vì đơn vừa checkout
                loadData();
            } else {
                Toast.makeText(requireContext(), "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void loadData() {
        if (idhoadon == null) {
            rv.setAdapter(null);
            tvTotal.setText("Tổng: 0 đ");
            tvEmpty.setVisibility(View.VISIBLE);
            return;
        }
        ArrayList<CartItem> items = dao.getCartItems(idhoadon);
        adapter = new CartAdapter(items);
        rv.setAdapter(adapter);

        tvTotal.setText("Tổng: " + nf.format(dao.getCartTotal(idhoadon)) + " đ");
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    // Adapter nhỏ gọn
    static class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {
        private final ArrayList<CartItem> data;
        private final NumberFormat nf = NumberFormat.getInstance(new Locale("vi","VN"));
        CartAdapter(ArrayList<CartItem> data) { this.data = data; }

        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
            View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_cart, p, false);
            return new VH(view);
        }
        @Override public void onBindViewHolder(@NonNull VH h, int pos) {
            CartItem it = data.get(pos);
            h.tvName.setText(it.tensp);
            h.tvQtyPrice.setText("SL: " + it.soluong + "  |  Giá: " + nf.format(it.dongia) + " đ");
            h.tvSub.setText("Thành tiền: " + nf.format(it.tongtien) + " đ");
            if (it.img != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(it.img, 0, it.img.length);
                h.img.setImageBitmap(bm);
            } else h.img.setImageResource(R.drawable.ic_launcher_background);
        }
        @Override public int getItemCount() { return data == null ? 0 : data.size(); }

        static class VH extends RecyclerView.ViewHolder {
            ImageView img; TextView tvName, tvQtyPrice, tvSub;
            VH(@NonNull View v) {
                super(v);
                img = v.findViewById(R.id.img);
                tvName = v.findViewById(R.id.tvName);
                tvQtyPrice = v.findViewById(R.id.tvQtyPrice);
                tvSub = v.findViewById(R.id.tvSub);
            }
        }
    }
}
