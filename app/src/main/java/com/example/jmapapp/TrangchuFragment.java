    package com.example.jmapapp;

    import android.app.Activity;
    import android.app.Dialog;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.net.Uri;
    import android.os.Bundle;
    import android.os.Handler;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.Toast;

    import androidx.activity.result.ActivityResultLauncher;
    import androidx.activity.result.contract.ActivityResultContracts;
    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.viewpager2.widget.ViewPager2;

    import com.example.jmapapp.adapter.adapterpager;
    import com.example.jmapapp.adapter.adaptersanpham;
    import com.example.jmapapp.datadao.itemdao;
    import com.example.jmapapp.model.modeiviewpager;
    import com.example.jmapapp.model.modelsanpham;

    import java.io.ByteArrayOutputStream;
    import java.io.InputStream;
    import java.util.ArrayList;
    import java.util.List;

    public class TrangchuFragment extends Fragment {
        private ViewPager2 bannerViewPager;
        private adapterpager bannerAdapter;
        private List<modeiviewpager> bannerList;
        private Handler sliderHandler = new Handler();
        private Runnable sliderRunnable;

        private ArrayList<modelsanpham> sp = new ArrayList<>();
        private adaptersanpham adaptersanpham;
        private itemdao itemdao;
        private ActivityResultLauncher<String> pickImageLauncher;
        private RecyclerView rc;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            pickImageLauncher = registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri == null) return;
                        try {
                            InputStream in = requireActivity().getContentResolver().openInputStream(uri);
                            Bitmap bm = BitmapFactory.decodeStream(in);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] imageBytes = stream.toByteArray();

                            // Đưa về adapter để lưu
                            adaptersanpham.selectedImageBytesStatic = imageBytes;

                            // Cập nhật ngay ảnh trong dialog (nếu đang mở)
                            if (adaptersanpham.dialogImageView != null) {
                                adaptersanpham.dialogImageView.setImageBitmap(bm);
                            }
                        } catch (Exception e) { e.printStackTrace(); }
                    }
            );
            View view = inflater.inflate(R.layout.fragment_trangchu, container, false);
            bannerViewPager = view.findViewById(R.id.page2);
            rc = view.findViewById(R.id.recy);               // 👈 GÁN VÀO BIẾN LỚP, KHÔNG khai báo lại
            rc.setLayoutManager(new GridLayoutManager(requireContext(), 2));

            adaptersanpham = new adaptersanpham(

                    getContext(),
                    sp,
                    pickImageLauncher,
                    product -> {
                        // Điều hướng sang trang chi tiết
                        Fragment f = ProductDetailFragment.newInstance(product.getIdsanpham());
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(hostContainerId(), f)// đổi sang container thực tế của bạn
                                .addToBackStack(null)
                                .commit();
                    }
            );
            rc.setAdapter(adaptersanpham);

            bannerViewPager = view.findViewById(R.id.page2);
            bannerList = new ArrayList<>();
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

            RecyclerView rc = view.findViewById(R.id.recy);
            rc.setLayoutManager(new GridLayoutManager(getContext(), 2));
            rc.setAdapter(adaptersanpham);
            itemdao = new itemdao(getContext());
            loadDataFromDb();
            Button btnAdd = view.findViewById(R.id.btnAdd);
            btnAdd.setOnClickListener(v -> showAddDialog());
            return view;

    }
        private void loadDataFromDb() {
            sp.clear();
            sp.addAll(itemdao.getAll());
            adaptersanpham.notifyDataSetChanged();
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
//        @Override
//        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == 123 && resultCode == Activity.RESULT_OK && data != null) {
//                Uri imageUri = data.getData();
//                try {
//                    InputStream inputStream = requireActivity().getContentResolver().openInputStream(imageUri);
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byte[] imageBytes = stream.toByteArray();
//
//                    // Gửi ảnh đã chọn về adapter
//                    adaptersanpham.setSelectedImage(imageBytes);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 123 && resultCode == Activity.RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                try {
                    InputStream inputStream = requireActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] imageBytes = stream.toByteArray();

                    // Gửi về adapter
                    adaptersanpham.selectedImageBytesStatic = imageBytes;
                    if (adaptersanpham.dialogImageView != null) {
                        adaptersanpham.dialogImageView.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        private void showAddDialog() {
            // reset vùng nhớ ảnh tạm dùng chung với Adapter
            adaptersanpham.dialogImageView = null;
            adaptersanpham.selectedImageBytesStatic = null;

            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.dialog_add_sanpham);
            dialog.setTitle("Thêm sản phẩm");

            ImageView imgPreview = dialog.findViewById(R.id.imgPreviewAdd);
            EditText edtName    = dialog.findViewById(R.id.edtNameAdd);
            EditText edtPrice   = dialog.findViewById(R.id.edtPriceAdd);
            EditText edtMota    = dialog.findViewById(R.id.edtMotaAdd);
            EditText edtTonkho  = dialog.findViewById(R.id.edtTonkhoAdd);
            EditText edtIddm    = dialog.findViewById(R.id.edtIddmAdd);
            View btnSave        = dialog.findViewById(R.id.btnSaveAdd);

            // Cho phép bấm vào ảnh để chọn ảnh
            imgPreview.setOnClickListener(v -> {
                adaptersanpham.dialogImageView = imgPreview; // để pickImageLauncher set trực tiếp preview
                pickImageLauncher.launch("image/*");
            });

            btnSave.setOnClickListener(v -> {
                // Validate đơn giản
                String name = edtName.getText().toString().trim();
                String priceStr = edtPrice.getText().toString().trim();
                String mota = edtMota.getText().toString().trim();
                String tonkhoStr = edtTonkho.getText().toString().trim();
                String iddmStr = edtIddm.getText().toString().trim();

                if (name.isEmpty() || priceStr.isEmpty() || tonkhoStr.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập Tên, Giá và Tồn kho", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price;
                int tonkho;
                try {
                    price = Double.parseDouble(priceStr);
                    tonkho = Integer.parseInt(tonkhoStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Giá/Tồn kho không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Ảnh (có thể null nếu chưa chọn)
                byte[] imgBytes = adaptersanpham.selectedImageBytesStatic;

                // ID danh mục (tùy chọn): nếu trống để = 0 (bạn có thể đổi logic tùy ràng buộc FK)
                int iddm = 0;
                if (!iddmStr.isEmpty()) {
                    try { iddm = Integer.parseInt(iddmStr); } catch (NumberFormatException ignored) {}
                }

                // Tạo đối tượng sản phẩm mới
                modelsanpham spNew = new modelsanpham(
                        0,          // idsanpham sẽ gán sau khi insert
                        name,
                        price,
                        mota,
                        tonkho,
                        imgBytes,
                        iddm
                );

                long newId = itemdao.insertSanPham(spNew);
                if (newId > 0) {
                    spNew.setIdsanpham((int) newId); // nếu model có setter id
                    sp.add(0, spNew);                // thêm lên đầu danh sách (tùy bạn)
                    adaptersanpham.notifyItemInserted(0);

                    // Reset vùng nhớ tạm
                    adaptersanpham.dialogImageView = null;
                    adaptersanpham.selectedImageBytesStatic = null;

                    Toast.makeText(getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            });

            dialog.show();
        }
        private int hostContainerId() {
            if (requireActivity().findViewById(R.id.flContent4) != null) return R.id.flContent4; // MenuNv/Menukhach
            if (requireActivity().findViewById(R.id.flContent2) != null) return R.id.flContent2; // Menu (admin cũ)
            if (requireActivity().findViewById(R.id.flContent3) != null) return R.id.flContent3; // Menu (admin cũ)
            throw new IllegalStateException("No fragment container (flContent4/flContent2/flContent3) found in host activity");
        }

    }
