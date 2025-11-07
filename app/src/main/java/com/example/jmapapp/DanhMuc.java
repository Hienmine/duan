package com.example.jmapapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jmapapp.adapter.danhmucadapter;
import com.example.jmapapp.datadao.danhmucdao;
import com.example.jmapapp.model.modeldanhmuc;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DanhMuc extends Fragment {
    private danhmucadapter adapter;
    private ArrayList<modeldanhmuc> list;
    private danhmucdao dao;

    private byte[] selectedImgBytes = null;
    private ImageView currentImgPreview;
    private ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    selectedImgBytes = readBytesFromUri(requireContext(), uri);
                    if (currentImgPreview != null && selectedImgBytes != null) {
                        Bitmap bm = BitmapFactory.decodeByteArray(selectedImgBytes, 0, selectedImgBytes.length);
                        currentImgPreview.setImageBitmap(bm);
                    }
                }
            });

    // Giữ tham chiếu ImageView trong dialog để set preview sau khi chọn ảnh
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.listdanhmuc, container, false);

        RecyclerView recy = view.findViewById(R.id.recyD);
        recy.setLayoutManager(new LinearLayoutManager(requireContext()));
        recy.setHasFixedSize(true);

        dao  = new danhmucdao(requireContext());
        list = dao.getAll();                      // <-- trả về ArrayList<modeldanhmuc>

        adapter = new danhmucadapter(requireContext(), list);
        adapter.setDao(dao);
        recy.setAdapter(adapter);
        FloatingActionButton fab = view.findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> showAddDialog());
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        currentImgPreview = null; // tránh giữ tham chiếu view cũ
    }
    private void showAddDialog() {
        selectedImgBytes = null;           // reset mỗi lần mở dialog
        currentImgPreview = null;

        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_add_danhmuc, null, false);
        EditText edtName = dialogView.findViewById(R.id.edtName);
        ImageView imgPick = dialogView.findViewById(R.id.imgPick);
        currentImgPreview = imgPick;

        imgPick.setOnClickListener(v -> openGallery());

        new AlertDialog.Builder(requireContext())
                .setTitle("Thêm danh mục")
                .setView(dialogView)
                .setPositiveButton("Lưu", (d, w) -> {
                    String name = edtName.getText().toString().trim();
                    if (name.isEmpty()) {
                        Toast.makeText(requireContext(), "Tên không được rỗng!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    modeldanhmuc dm = new modeldanhmuc();
                    dm.setName(name);
                    dm.setDmimg(selectedImgBytes); // có thể null -> sẽ dùng ảnh mặc định khi hiển thị

                    long row = dao.insert(dm);
                    if (row > 0) {
                        // reload list
                        adapter.setData(dao.getAll());
                        Toast.makeText(requireContext(), "Đã thêm!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickImageLauncher.launch(Intent.createChooser(intent, "Chọn hình danh mục"));
    }
    private byte[] readBytesFromUri(Context ctx, Uri uri) {
        try (InputStream is = ctx.getContentResolver().openInputStream(uri);
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] data = new byte[4096];
            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            // nén nhẹ để giảm kích thước BLOB (tuỳ chọn)
            Bitmap bm = BitmapFactory.decodeByteArray(buffer.toByteArray(), 0, buffer.size());
            if (bm == null) return buffer.toByteArray();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 85, out);
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
