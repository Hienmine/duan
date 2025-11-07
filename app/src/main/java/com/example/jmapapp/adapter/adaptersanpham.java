package com.example.jmapapp.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jmapapp.R;
import com.example.jmapapp.database.mydatabase;
import com.example.jmapapp.datadao.itemdao;
import com.example.jmapapp.model.modelsanpham;

import java.util.ArrayList;
import android.app.Dialog;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;


public class adaptersanpham extends RecyclerView.Adapter<adaptersanpham.ViewHolder> {
    private Context context;
    private ArrayList<modelsanpham> list;
    private static final int REQUEST_CODE_PICK_IMAGE = 123;
    public static ImageView dialogImageView = null;
    public static byte[] selectedImageBytesStatic = null;
    private final ActivityResultLauncher<String> pickImageLauncher;
//    private byte[] selectedImageBytes = null;
public interface OnProductClick {
    void onClick(modelsanpham sp);
}
    private final OnProductClick listener;


    public adaptersanpham(Context context, ArrayList<modelsanpham> list,
                          ActivityResultLauncher<String> pickImageLauncher, OnProductClick listener) {
        this.context = context;
        this.list = list;
        this.pickImageLauncher = pickImageLauncher; // lưu launcher

        this.listener = listener;
    }
    @NonNull
    @Override
    public adaptersanpham.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemsp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adaptersanpham.ViewHolder holder, int position) {
        modelsanpham sp = list.get(position);
        holder.tvName.setText(sp.getNamesp());
        holder.tvPrice.setText(String.format("%.0f đ", sp.getGia()));
        holder.txtMo.setText(sp.getMota());
        if (sp.getImg() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(sp.getImg(), 0, sp.getImg().length);
            holder.imgSP.setImageBitmap(bitmap);
        } else {
            holder.imgSP.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(sp);
        });
        holder.btnDelete.setOnClickListener(v -> {
            itemdao dao = new itemdao(context);
            dao.deleteSanPham(sp.getIdsanpham());
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        });
        holder.btnEdit.setOnClickListener(v -> {
            selectedImageBytesStatic = null;
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_edit_sanpham);
            dialog.setTitle("Sửa sản phẩm");

            EditText edtName = dialog.findViewById(R.id.edtName);
            EditText edtPrice = dialog.findViewById(R.id.edtPrice);
            EditText edtMota = dialog.findViewById(R.id.edtMota);
            EditText edtTonkho = dialog.findViewById(R.id.edtTonkho);
            ImageView imgPreview = dialog.findViewById(R.id.imgPreview);
            Button btnSave = dialog.findViewById(R.id.btnSave);
            dialogImageView = imgPreview;
            edtName.setText(sp.getNamesp());
            edtPrice.setText(String.valueOf(sp.getGia()));
            edtMota.setText(sp.getMota());
            edtTonkho.setText(String.valueOf(sp.getTonkho()));

            // Hiển thị ảnh cũ
//            if (sp.getImg() != null) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(sp.getImg(), 0, sp.getImg().length);
//                imgPreview.setImageBitmap(bitmap);
//            }
            if (selectedImageBytesStatic != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(selectedImageBytesStatic, 0, selectedImageBytesStatic.length);
                imgPreview.setImageBitmap(bm);
            } else if (sp.getImg() != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(sp.getImg(), 0, sp.getImg().length);
                imgPreview.setImageBitmap(bm);
            }
            imgPreview.setOnClickListener(view -> {
                dialogImageView = imgPreview;
                pickImageLauncher.launch("image/*");
            });

            btnSave.setOnClickListener(view -> {
                String name = edtName.getText().toString();
                double price = Double.parseDouble(edtPrice.getText().toString());
                String mota = edtMota.getText().toString();
                int tonkho = Integer.parseInt(edtTonkho.getText().toString());

                sp.setNamesp(name);
                sp.setGia(price);
                sp.setMota(mota);
                sp.setTonkho(tonkho);
                byte[] imgToSave = (selectedImageBytesStatic != null) ? selectedImageBytesStatic : sp.getImg();
                sp.setImg(imgToSave);
//                if (selectedImageBytes != null) {
//                    sp.setImg(selectedImageBytes);
//                }

                itemdao dao = new itemdao(context);
                if (dao.updateSanPham(sp) > 0) {
                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    list.set(position, sp);
                    notifyItemChanged(position);
//                    selectedImageBytes = null; // reset
                    dialogImageView = null;
                    selectedImageBytesStatic = null;
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSP;
        TextView tvName, tvPrice,txtMo;
        ImageView btnEdit, btnDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSP = itemView.findViewById(R.id.imgSP);
            tvName = itemView.findViewById(R.id.txtnamesp);
            tvPrice = itemView.findViewById(R.id.txtgiasp);
            txtMo=itemView.findViewById(R.id.txtmotasp);
            btnEdit=itemView.findViewById(R.id.btnup);
            btnDelete=itemView.findViewById(R.id.btnde);
        }
    }
//    public void setSelectedImage(byte[] imageBytes) {
//        this.selectedImageBytes = imageBytes;
//        notifyDataSetChanged(); // hoặc chỉ notifyItemChanged nếu bạn có thể truyền position
//    }
}