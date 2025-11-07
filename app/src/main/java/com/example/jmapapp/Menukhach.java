package com.example.jmapapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Menukhach extends AppCompatActivity {
    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menukhach);

        nav = findViewById(R.id.trangchu3);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.duohome) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent4, new TrangchuKhachFragment())
                        .commit();
                return true;
            } else if (id == R.id.duo2dm) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent4, new DanhMuc())
                        .commit();
                return true;
            } else if (id == R.id.duo3_cart) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent4, new CartFragment())
                        .commit();
                return true;
            } else if (id == R.id.duo4_info) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent4, new ProfileFragment()) // thông tin khách hàng
                        .commit();
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            nav.setSelectedItemId(R.id.duohome); // chọn Trang chủ mặc định (an toàn, không NPE)
        }
    }
}