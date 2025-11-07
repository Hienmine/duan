package com.example.jmapapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Menu extends AppCompatActivity {
    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nav=findViewById(R.id.trangchu1);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.duoi) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flContent2, new TrangchuFragment())
                            .commit();
                    return true;
                } else if (id == R.id.duoi2) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flContent2, new DanhMuc())
                            .commit();
                    return true;
                } else if (id == R.id.duoi3) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flContent2, new NhanVienFragment())
                            .commit();
                    return true;
                }else if (id==R.id.duoi4){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flContent2, new MenuFragment())
                            .commit();
                    return true;
                }
                return false;
            }
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flContent2, new TrangchuFragment())
                    .commit();
            nav.getMenu().findItem(R.id.duoi).setChecked(true); // đánh dấu item đang chọn
        }
    }
}