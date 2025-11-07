package com.example.jmapapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jmapapp.R;
import com.example.jmapapp.model.modeluser;

import java.util.ArrayList;

public class nhanvienadapter extends RecyclerView.Adapter<nhanvienadapter.NVHolder> {

    public interface OnAction {
        void onEdit(modeluser u, int position);
        void onDelete(modeluser u, int position);
    }

    private final Context context;
    private final ArrayList<modeluser> list;
    private final OnAction callback;

    public nhanvienadapter(Context context, ArrayList<modeluser> list, OnAction cb) {
        this.context = context;
        this.list = list;
        this.callback = cb;
    }

    @NonNull
    @Override
    public NVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_nhanvien, parent, false);
        return new NVHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NVHolder h, int pos) {
        modeluser u = list.get(pos);
        h.tvName.setText(u.getName());
        h.tvUsername.setText("user: " + u.getUsername());
        h.tvRole.setText("Vai trò: " + u.getRole());

        h.btnEdit.setOnClickListener(v -> {
            if (callback != null) callback.onEdit(u, h.getAdapterPosition());
        });
        h.btnDelete.setOnClickListener(v -> {
            if (callback != null) callback.onDelete(u, h.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public void replaceAll(ArrayList<modeluser> newData) {
        list.clear();
        list.addAll(newData);
        notifyDataSetChanged();
    }

    public void addOne(modeluser u) {
        list.add(0, u);
        notifyItemInserted(0);
    }

    public void updateOne(int pos, modeluser u) {
        list.set(pos, u);
        notifyItemChanged(pos);
    }

    public void removeAt(int pos) {
        list.remove(pos);
        notifyItemRemoved(pos);
    }

    static class NVHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUsername, tvRole, btnEdit, btnDelete;
        NVHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvRole = itemView.findViewById(R.id.tvRole);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
