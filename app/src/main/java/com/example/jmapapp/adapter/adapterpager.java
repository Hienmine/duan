package com.example.jmapapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jmapapp.R;
import com.example.jmapapp.model.modeiviewpager;

import java.util.List;

public class adapterpager extends RecyclerView.Adapter<adapterpager.BannerViewHolder> {
    private List<modeiviewpager> bannerList;

    public adapterpager(List<modeiviewpager> bannerList) {
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.qcpage2, parent, false);
        return new BannerViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterpager.BannerViewHolder holder, int position) {
        modeiviewpager banner = bannerList.get(position);
        holder.imageView.setImageResource(banner.getImageResId());
    }

    @Override
    public int getItemCount() {
        return  bannerList.size();
    }
    public class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgqc2);
        }
    }
}
