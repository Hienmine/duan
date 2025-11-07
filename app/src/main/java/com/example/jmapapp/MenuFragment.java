package com.example.jmapapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jmapapp.datadao.userdao;
import com.example.jmapapp.MainActivity; // nếu bạn có màn login

// import com.example.jmapapp.ui.UsersFragment; // nếu bạn có fragment riêng
// import ... các fragment khác ...

public class MenuFragment extends Fragment {

    private static final String PREFS = "app_prefs";
    private static final String KEY_USER_ID = "logged_user_id"; // iduser của người đăng nhập

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        v.findViewById(R.id.btnUsers).setOnClickListener(view -> {
            // ví dụ điều hướng tới danh sách người dùng
            // Nếu bạn muốn danh sách "nhân viên": dùng NhanVienFragment
            open(new KhachHangFragment());

            // Nếu bạn có UsersFragment hiển thị cả customer:
            // open(new UsersFragment());
        });

        v.findViewById(R.id.btnInvoices).setOnClickListener(view -> {
            open(new InvoicesFragment());
        });

        v.findViewById(R.id.btnRevenue).setOnClickListener(view -> {
            open(new RevenueFragment());
        });
        v.findViewById(R.id.btnTopProducts).setOnClickListener(view -> {
            open(new TopProductsFragment());
        });

        v.findViewById(R.id.btnThongtin).setOnClickListener(view -> {
            open(new ProfileFragment()); // TODO: tạo TopProductsFragment (có thể là list top)
        });

        v.findViewById(R.id.btnChangePass).setOnClickListener(view -> {
            showChangePasswordDialog();
        });

        v.findViewById(R.id.btnLogout).setOnClickListener(view -> {
            confirmLogout();
        });
        v.findViewById(R.id.btnGio /*hoặc 1 nút riêng */).setOnClickListener(view -> {
            open(new CartFragment());
        });

        return v;
    }

    private void open(Fragment f) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContent2, f) // đổi thành id container thực tế của bạn
                .addToBackStack(null)
                .commit();
    }

    // ========== Đổi mật khẩu ==========
    private void showChangePasswordDialog() {
        View dialog = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_change_password, null);
        EditText edCurrent = dialog.findViewById(R.id.edCurrentPass);
        EditText edNew     = dialog.findViewById(R.id.edNewPass);
        EditText edConfirm = dialog.findViewById(R.id.edConfirmPass);

        new AlertDialog.Builder(requireContext())
                .setTitle("Đổi mật khẩu")
                .setView(dialog)
                .setPositiveButton("Lưu", (d, w) -> {
                    String cur = edCurrent.getText().toString().trim();
                    String nw  = edNew.getText().toString().trim();
                    String cf  = edConfirm.getText().toString().trim();
                    if (TextUtils.isEmpty(cur) || TextUtils.isEmpty(nw) || TextUtils.isEmpty(cf)) {
                        Toast.makeText(requireContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!nw.equals(cf)) {
                        Toast.makeText(requireContext(), "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Lấy id user đang đăng nhập
                    SharedPreferences sp = requireContext().getSharedPreferences(PREFS, 0);
                    int uid = sp.getInt(KEY_USER_ID, -1);
                    if (uid == -1) {
                        Toast.makeText(requireContext(), "Không xác định người dùng", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    userdao dao = new userdao(requireContext());
                    // Optional: kiểm tra mật khẩu hiện tại khớp DB (nếu có lưu)
                    // Ở đây mình minh hoạ đơn giản: cập nhật thẳng nếu bạn không cần check.
                    int n = dao.updatePassword(uid, nw);
                    if (n > 0) {
                        Toast.makeText(requireContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    // ========== Đăng xuất ==========
    private void confirmLogout() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (d, w) -> {
                    SharedPreferences sp = requireContext().getSharedPreferences(PREFS, 0);
                    sp.edit().remove(KEY_USER_ID).apply();

                    // Chuyển về màn đăng nhập
                    Intent i = new Intent(requireContext(), MainActivity.class);
                    startActivity(i);
                    requireActivity().finish(); // đóng activity hiện tại
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}
