package com.example.jmapapp;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.jmapapp.adapter.HoadonAdapter;
import com.example.jmapapp.datadao.hoadondao;
import com.example.jmapapp.datadao.userdao;
import com.example.jmapapp.model.modelhoadon;
import com.example.jmapapp.datadao.userdao.UserMin;

import java.util.ArrayList;
import java.util.Arrays;

public class InvoicesFragment extends Fragment {

    private RecyclerView rv;
    private FloatingActionButton fabAdd;
    private HoadonAdapter adapter;
    private hoadondao hdDao;
    private userdao uDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_invoices, container, false);

        rv = v.findViewById(R.id.rvInvoices);
        fabAdd = v.findViewById(R.id.fabAddInvoice);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        hdDao = new hoadondao(requireContext());
        uDao  = new userdao(requireContext());

        adapter = new HoadonAdapter(loadData(), new HoadonAdapter.OnHoaDonAction() {
            @Override
            public void onEdit(modelhoadon hd) {
                showAddOrEditDialog(hd);
            }

            @Override
            public void onDelete(modelhoadon hd) {
                confirmDelete(hd);
            }
        });
        rv.setAdapter(adapter);

        fabAdd.setOnClickListener(view -> showAddOrEditDialog(null));

        return v;
    }

    private ArrayList<modelhoadon> loadData() {
        return hdDao.getAll();
    }

    private void refresh() {
        adapter.setData(loadData());
    }

    private void confirmDelete(modelhoadon hd) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa hóa đơn")
                .setMessage("Bạn có chắc muốn xóa HĐ #" + hd.getIdhoadon() + "?\n"
                        + "Lưu ý: chi tiết hóa đơn liên quan sẽ bị xóa theo (ON DELETE CASCADE).")
                .setPositiveButton("Xóa", (d, w) -> {
                    int n = hdDao.delete(hd.getIdhoadon());
                    if (n > 0) {
                        Toast.makeText(requireContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                        refresh();
                    } else {
                        Toast.makeText(requireContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showAddOrEditDialog(@Nullable modelhoadon editing) {
        View dialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_hoa_don, null);
        EditText edNgayLap   = dialog.findViewById(R.id.edNgayLap);
        Spinner spTrangThai  = dialog.findViewById(R.id.spTrangThai);
        Spinner spUser       = dialog.findViewById(R.id.spUser);

        // Trạng thái
        String[] statuses = getResources().getStringArray(R.array.invoice_status);
        ArrayAdapter<String> stAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, Arrays.asList(statuses));
        spTrangThai.setAdapter(stAdapter);

        // User
        ArrayList<UserMin> users = uDao.getAllMin();
        ArrayAdapter<UserMin> uAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, users);
        spUser.setAdapter(uAdapter);

        // Nếu SỬA: đổ dữ liệu
        if (editing != null) {
            edNgayLap.setText(editing.getNgaylap());
            // chọn trạng thái
            int pos = Arrays.asList(statuses).indexOf(editing.getTrangthai());
            if (pos >= 0) spTrangThai.setSelection(pos);
            // chọn user
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).id == editing.getIduser()) {
                    spUser.setSelection(i);
                    break;
                }
            }
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(editing == null ? "Thêm hóa đơn" : "Sửa hóa đơn")
                .setView(dialog)
                .setPositiveButton("Lưu", (d, w) -> {
                    String ngaylap = edNgayLap.getText().toString().trim();
                    if (TextUtils.isEmpty(ngaylap)) {
                        Toast.makeText(requireContext(), "Vui lòng nhập ngày lập", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String trangthai = (String) spTrangThai.getSelectedItem();
                    UserMin u = (UserMin) spUser.getSelectedItem();
                    if (u == null) {
                        Toast.makeText(requireContext(), "Chưa có user để gán", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (editing == null) {
                        // THÊM
                        modelhoadon hd = new modelhoadon(0, ngaylap, trangthai, u.id);
                        long row = hdDao.insert(hd);
                        if (row > 0) {
                            Toast.makeText(requireContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                            refresh();
                        } else {
                            Toast.makeText(requireContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // SỬA
                        modelhoadon hd = new modelhoadon(editing.getIdhoadon(), ngaylap, trangthai, u.id);
                        int n = hdDao.update(hd);
                        if (n > 0) {
                            Toast.makeText(requireContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            refresh();
                        } else {
                            Toast.makeText(requireContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
