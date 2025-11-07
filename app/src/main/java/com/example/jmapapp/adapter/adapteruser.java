package com.example.jmapapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jmapapp.R;
import com.example.jmapapp.model.modeluser;

import java.util.ArrayList;

public class adapteruser extends ArrayAdapter<modeluser> {
    private Context context;
    private ArrayList<modeluser> list;

    public adapteruser(Context context, ArrayList<modeluser> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.listuser, parent, false);

        modeluser user = list.get(position);
        TextView tvName = convertView.findViewById(R.id.tvUserName);
        TextView tvRole = convertView.findViewById(R.id.tvUserRole);

        tvName.setText(user.getName() + " (" + user.getUsername() + ")");
        tvRole.setText("Vai trò: " + user.getRole());

        return convertView;
    }

}
