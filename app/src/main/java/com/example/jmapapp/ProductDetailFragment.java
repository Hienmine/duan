package com.example.jmapapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jmapapp.datadao.hoadondao;
import com.example.jmapapp.datadao.itemdao;
import com.example.jmapapp.model.modelhoadon;
import com.example.jmapapp.model.modelsanpham;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailFragment extends Fragment {

    private static final String ARG_ID = "arg_idsanpham";
    private static final String PREFS = "app_prefs";
    private static final String KEY_USER_ID = "logged_user_id";

    public static ProductDetailFragment newInstance(int idsanpham) {
        Bundle b = new Bundle();
        b.putInt(ARG_ID, idsanpham);
        ProductDetailFragment f = new ProductDetailFragment();
        f.setArguments(b);
        return f;
    }

    private ImageView img;
    private TextView tvName, tvPrice, tvDesc;
    private EditText edQty;
    private Button btnAdd, btnGoCart;

    private itemdao spDao;
    private hoadondao hdDao;
    private modelsanpham sp;
    private final NumberFormat nf = NumberFormat.getInstance(new Locale("vi","VN"));

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_detail, container, false);

        img = v.findViewById(R.id.img);
        tvName = v.findViewById(R.id.tvName);
        tvPrice = v.findViewById(R.id.tvPrice);
        tvDesc = v.findViewById(R.id.tvDesc);
        edQty = v.findViewById(R.id.edQty);
        btnAdd = v.findViewById(R.id.btnAddToCart);
        btnGoCart = v.findViewById(R.id.btnGoCart);

        spDao = new itemdao(requireContext());
        hdDao = new hoadondao(requireContext());

        int id = getArguments() != null ? getArguments().getInt(ARG_ID, -1) : -1;
        sp = spDao.getById(id);
        if (sp == null) {
            Toast.makeText(requireContext(), "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return v;
        }

        bind(sp);

        btnAdd.setOnClickListener(v1 -> addToCart());
        btnGoCart.setOnClickListener(v12 -> openCart());

        return v;
    }

    private void bind(modelsanpham sp) {
        tvName.setText(sp.getNamesp());
        tvPrice.setText(nf.format(sp.getGia()) + " đ");
        tvDesc.setText(TextUtils.isEmpty(sp.getMota()) ? "Không có mô tả" : sp.getMota());
        if (sp.getImg() != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(sp.getImg(), 0, sp.getImg().length);
            img.setImageBitmap(bm);
        } else {
            img.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private void addToCart() {
        int qty = 1;
        try { qty = Integer.parseInt(edQty.getText().toString().trim()); } catch (Exception ignored) {}
        if (qty <= 0) {
            Toast.makeText(requireContext(), "Số lượng phải > 0", Toast.LENGTH_SHORT).show();
            return;
        }

        int uid = requireContext().getSharedPreferences(PREFS, 0).getInt(KEY_USER_ID, -1);
        if (uid == -1) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        // lấy/ tạo giỏ hàng mở
        modelhoadon cart = hdDao.getOrCreateOpenCart(uid);
        // thêm/cộng dồn item
        long r = hdDao.addOrUpdateCartItem(cart.getIdhoadon(), sp.getIdsanpham(), qty, sp.getGia());
        if (r >= 0) {
            Toast.makeText(requireContext(), "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Thêm vào giỏ thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCart() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContent2, new CartFragment())
                .addToBackStack(null)
                .commit();
    }
}
