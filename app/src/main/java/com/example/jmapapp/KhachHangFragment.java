package com.example.jmapapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jmapapp.adapter.nhanvienadapter; // tái dùng adapter
import com.example.jmapapp.datadao.userdao;
import com.example.jmapapp.model.modeluser;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class KhachHangFragment extends Fragment {

    private RecyclerView rcv;
    private FloatingActionButton fab;
    private nhanvienadapter adapter; // dùng lại vì hiển thị name/username/role giống nhau
    private userdao dao;
    private ArrayList<modeluser> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Nếu dùng layout riêng:
        View v = inflater.inflate(R.layout.fragment_khachhang, container, false);
        // Nếu dùng lại fragment_nhanvien.xml thì thay dòng trên bằng:
        // View v = inflater.inflate(R.layout.fragment_nhanvien, container, false);

        dao = new userdao(requireContext());
        rcv = v.findViewById(R.id.rcvKhachHang); // nếu tái dùng layout cũ: R.id.rcvNhanVien
        fab = v.findViewById(R.id.fabAddKH);     // nếu tái dùng layout cũ: R.id.fabAddNV

        rcv.setLayoutManager(new LinearLayoutManager(requireContext()));
        data = dao.getCustomers();
        adapter = new nhanvienadapter(requireContext(), data, new nhanvienadapter.OnAction() {
            @Override
            public void onEdit(modeluser u, int position) {
                showAddEditDialog(u, position);
            }

            @Override
            public void onDelete(modeluser u, int position) {
                confirmDelete(u, position);
            }
        });
        rcv.setAdapter(adapter);

        fab.setOnClickListener(view -> showAddEditDialog(null, -1));
        return v;
    }

    private void confirmDelete(modeluser u, int pos) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xoá khách hàng")
                .setMessage("Bạn có chắc muốn xoá " + u.getName() + " ?")
                .setPositiveButton("Xoá", (d, which) -> {
                    int n = dao.deleteUser(u.getIduser());
                    if (n > 0) {
                        adapter.removeAt(pos);
                        Toast.makeText(requireContext(), "Đã xoá", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Xoá thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    private void showAddEditDialog(@Nullable modeluser editing, int editPos) {
        // tái dùng dialog_nhanvien.xml (không có ô role)
        View dialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_nhanvien, null);
        EditText edName = dialog.findViewById(R.id.edName);
        EditText edUser = dialog.findViewById(R.id.edUsername);
        EditText edPass = dialog.findViewById(R.id.edPass);
        EditText edGT   = dialog.findViewById(R.id.edGioiTinh);
        EditText edDC   = dialog.findViewById(R.id.edDiaChi);
        EditText edTuoi = dialog.findViewById(R.id.edTuoi);

        boolean isEdit = (editing != null);
        if (isEdit) {
            edName.setText(editing.getName());
            edUser.setText(editing.getUsername());
            edPass.setText(editing.getPass());
            edGT.setText(editing.getGioitinh());
            edDC.setText(editing.getDiachi());
            edTuoi.setText(String.valueOf(editing.getTuoi()));
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(isEdit ? "Sửa khách hàng" : "Thêm khách hàng")
                .setView(dialog)
                .setPositiveButton(isEdit ? "Lưu" : "Thêm", (d, which) -> {
                    // validate
                    String name = edName.getText().toString().trim();
                    String user = edUser.getText().toString().trim();
                    String pass = edPass.getText().toString().trim();
                    String gt   = edGT.getText().toString().trim();
                    String dc   = edDC.getText().toString().trim();
                    String tuoiStr = edTuoi.getText().toString().trim();

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                        Toast.makeText(requireContext(), "Vui lòng nhập đủ Họ tên, Username, Mật khẩu", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int tuoi = 0;
                    try { tuoi = TextUtils.isEmpty(tuoiStr) ? 0 : Integer.parseInt(tuoiStr); }
                    catch (NumberFormatException ignored) {}

                    if (isEdit) {
                        // luôn giữ role = customer khi sửa
                        modeluser u = new modeluser(editing.getIduser(), user, pass, name, gt, dc, tuoi, "customer");
                        int n = dao.updateUser(u);
                        if (n > 0) {
                            // có thể requery lại để đảm bảo thứ tự
                            adapter.updateOne(editPos, u);
                            Toast.makeText(requireContext(), "Đã cập nhật", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // thêm mới: role mặc định customer
                        modeluser u = new modeluser(user, pass, name, gt, dc, tuoi, "customer");
                        long id = dao.add(u);
                        if (id > 0) {
                            adapter.replaceAll(dao.getCustomers());
                            Toast.makeText(requireContext(), "Đã thêm khách hàng", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}
