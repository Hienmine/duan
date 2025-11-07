package com.example.jmapapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jmapapp.R;
import com.example.jmapapp.datadao.danhmucdao;
import com.example.jmapapp.model.modeldanhmuc;

import java.util.ArrayList;
import java.util.List;

public class danhmucadapter extends RecyclerView.Adapter<danhmucadapter.ViewHolder> {
    private Context context;
    private ArrayList<modeldanhmuc> list;
    private danhmucdao dao;
    public danhmucadapter(Context context, ArrayList<modeldanhmuc> list) {
        this.context = context;
        this.list = list;
    }
    public void setData(ArrayList<modeldanhmuc> newList) {
        this.list.clear();
        if (newList != null) this.list.addAll(newList);
        notifyDataSetChanged();
    }
    public void setDao(danhmucdao dao) {
        this.dao = dao;
    }
    @NonNull
    @Override
    public danhmucadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemdanhmuc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull danhmucadapter.ViewHolder holder, int position) {
        modeldanhmuc dm = list.get(position);
        holder.nameDm.setText(dm.getName());


        if (dm.getDmimg() != null && dm.getDmimg().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(dm.getDmimg(), 0, dm.getDmimg().length);
            holder.imgDm.setImageBitmap(bitmap);
        } else {
            holder.imgDm.setImageResource(R.drawable.ic_launcher_foreground);
        }
        holder.imgu.setOnClickListener(v -> showUpdateDialog(dm, position));
        holder.imgd.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa danh mục")
                    .setMessage("Bạn có chắc muốn xóa \"" + dm.getName() + "\" không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        int rows = dao.delete(dm.getIddanhmuc());
                        if (rows > 0) {
                            // remove item khỏi list và thông báo Adapter
                            list.remove(position);
                            notifyItemRemoved(position);
                            // Nếu muốn an toàn index: notifyItemRangeChanged(position, list.size() - position);
                            Toast.makeText(context, "Đã xóa!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private void showUpdateDialog(modeldanhmuc dm, int position) {
        // Tạo ô nhập tên mới
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input.setText(dm.getName());
        int padding = (int) (16 * context.getResources().getDisplayMetrics().density);
        input.setPadding(padding, padding, padding, padding);

        new AlertDialog.Builder(context)
                .setTitle("Cập nhật danh mục")
                .setView(input)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (newName.isEmpty()) {
                        Toast.makeText(context, "Tên không được rỗng!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Cập nhật đối tượng và DB (giữ ảnh cũ)
                    dm.setName(newName);
                    int rows = dao.update(dm);
                    if (rows > 0) {
                        list.set(position, dm);
                        notifyItemChanged(position);
                        Toast.makeText(context, "Đã cập nhật!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDm;
        TextView nameDm;
        ImageView imgu,imgd,imga;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDm = itemView.findViewById(R.id.imgdm);
            nameDm = itemView.findViewById(R.id.nameDm);
            imgd=itemView.findViewById(R.id.imgde);
            imgu=itemView.findViewById(R.id.imgup);
            imga=itemView.findViewById(R.id.fabAdd);
        }
    }
}
