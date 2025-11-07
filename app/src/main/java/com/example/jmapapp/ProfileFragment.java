package com.example.jmapapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jmapapp.datadao.userdao;
import com.example.jmapapp.model.modeluser;

public class ProfileFragment extends Fragment {

    private static final String PREFS = "app_prefs";
    private static final String KEY_USER_ID = "logged_user_id";

    private EditText edUsername, edName, edGender, edAddress, edAge, edRole;
    private Button btnSave, btnChangePass;

    private userdao dao;
    private modeluser current;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        edUsername = v.findViewById(R.id.edUsername);
        edName     = v.findViewById(R.id.edName);
        edGender   = v.findViewById(R.id.edGender);
        edAddress  = v.findViewById(R.id.edAddress);
        edAge      = v.findViewById(R.id.edAge);
        edRole     = v.findViewById(R.id.edRole);
        btnSave    = v.findViewById(R.id.btnSave);
        btnChangePass = v.findViewById(R.id.btnChangePass);

        dao = new userdao(requireContext());

        int uid = getLoggedUserId();
        if (uid == -1) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            // điều hướng về màn đăng nhập nếu cần
            startActivity(new Intent(requireContext(), MainActivity.class));
            requireActivity().finish();
            return v;
        }

        current = dao.getById(uid);
        if (current == null) {
            Toast.makeText(requireContext(), "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
            return v;
        }

        bindUser(current);

        btnSave.setOnClickListener(view -> saveProfile());
        btnChangePass.setOnClickListener(view -> showChangePasswordDialog(uid));

        return v;
    }

    private int getLoggedUserId() {
        SharedPreferences sp = requireContext().getSharedPreferences(PREFS, 0);
        return sp.getInt(KEY_USER_ID, -1);
    }

    private void bindUser(modeluser u) {
        edUsername.setText(u.getUsername());
        edName.setText(u.getName());
        edGender.setText(u.getGioitinh());
        edAddress.setText(u.getDiachi());
        edAge.setText(String.valueOf(u.getTuoi()));
        edRole.setText(u.getRole());
    }

    private void saveProfile() {
        String name   = edName.getText().toString().trim();
        String gender = edGender.getText().toString().trim();
        String addr   = edAddress.getText().toString().trim();
        String ageStr = edAge.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(requireContext(), "Họ tên không được trống", Toast.LENGTH_SHORT).show();
            return;
        }
        int age = 0;
        try { age = Integer.parseInt(ageStr); } catch (Exception ignored) {}

        current.setName(name);
        current.setGioitinh(gender);
        current.setDiachi(addr);
        current.setTuoi(age);

        int n = dao.updateProfile(current);
        if (n > 0) {
            Toast.makeText(requireContext(), "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Lưu thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void showChangePasswordDialog(int uid) {
        View dialog = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_change_password, null);
        EditText edCur = dialog.findViewById(R.id.edCurrentPass);
        EditText edNew = dialog.findViewById(R.id.edNewPass);
        EditText edCf  = dialog.findViewById(R.id.edConfirmPass);

        new AlertDialog.Builder(requireContext())
                .setTitle("Đổi mật khẩu")
                .setView(dialog)
                .setPositiveButton("Lưu", (d, w) -> {
                    String cur = edCur.getText().toString().trim();
                    String nw  = edNew.getText().toString().trim();
                    String cf  = edCf.getText().toString().trim();

                    if (TextUtils.isEmpty(nw) || TextUtils.isEmpty(cf)) {
                        Toast.makeText(requireContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!nw.equals(cf)) {
                        Toast.makeText(requireContext(), "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Nếu muốn kiểm tra mật khẩu cũ:
                    if (current != null && !TextUtils.isEmpty(cur) && !cur.equals(current.getPass())) {
                        Toast.makeText(requireContext(), "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int n = dao.updatePassword(uid, nw);
                    if (n > 0) {
                        Toast.makeText(requireContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
