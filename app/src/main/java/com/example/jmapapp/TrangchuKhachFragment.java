package com.example.jmapapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.widget.Button;

import com.example.jmapapp.adapter.SanPhamKhachAdapter;
import com.example.jmapapp.adapter.adapterpager;
import com.example.jmapapp.datadao.itemdao;
import com.example.jmapapp.model.modeiviewpager;
import com.example.jmapapp.model.modelsanpham;

import java.util.ArrayList;
import java.util.List;

public class TrangchuKhachFragment extends Fragment {

    private ViewPager2 bannerViewPager;
    private adapterpager bannerAdapter;
    private final List<modeiviewpager> bannerList = new ArrayList<>();
    private final Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;

    private RecyclerView rc;
    private SanPhamKhachAdapter adapter;
    private itemdao itemDao;
    private final ArrayList<modelsanpham> ds = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trangchu_khach, container, false);

        // Banner
        bannerViewPager = v.findViewById(R.id.page2k);
        bannerList.clear();
        bannerList.add(new modeiviewpager(R.drawable.fastship));
        bannerList.add(new modeiviewpager(R.drawable.ride));
        bannerList.add(new modeiviewpager(R.drawable.banner1));
        bannerList.add(new modeiviewpager(R.drawable.banner2));
        bannerList.add(new modeiviewpager(R.drawable.banner3));

        bannerAdapter = new adapterpager(bannerList);
        bannerViewPager.setAdapter(bannerAdapter);

        sliderRunnable = () -> {
            int current = bannerViewPager.getCurrentItem();
            int next = (current + 1) % bannerList.size();
            bannerViewPager.setCurrentItem(next, true);
            sliderHandler.postDelayed(sliderRunnable, 7000);
        };
        sliderHandler.postDelayed(sliderRunnable, 7000);

        // RecyclerView
        rc = v.findViewById(R.id.recyK);
        rc.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        adapter = new SanPhamKhachAdapter(requireContext(), ds, product -> {
            Fragment f = ProductDetailFragment.newInstance(product.getIdsanpham());

            // Lấy id container của chính parent đang chứa fragment này
            int containerId;
            View root = requireView(); // sau onCreateView mới có
            View parent = (View) root.getParent();
            if (parent != null && parent.getId() != View.NO_ID) {
                containerId = parent.getId();
            } else if (requireActivity().findViewById(R.id.flContent3) != null) {
                containerId = R.id.flContent3;
            } else if (requireActivity().findViewById(R.id.flContent2) != null) {
                containerId = R.id.flContent2;
            } else {
                throw new IllegalStateException("Không tìm thấy container để mở chi tiết sản phẩm");
            }

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(containerId, f)
                    .addToBackStack(null)
                    .commit();
        });
        rc.setAdapter(adapter);

        itemDao = new itemdao(requireContext());
        loadData();

        // Nếu bạn muốn hiện nút “Thêm” thì ẩn hoặc bỏ với người dùng
        Button btnAdd = v.findViewById(R.id.btnAddK);
        if (btnAdd != null) btnAdd.setVisibility(View.GONE);

        return v;
    }

    private void loadData() {
        ds.clear();
        ds.addAll(itemDao.getAll());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }
}
