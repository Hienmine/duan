package com.example.jmapapp;

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

public class MenuNv extends AppCompatActivity {

    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_nv);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nav=findViewById(R.id.trangchu2);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.duo) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flContent3, new TrangchuFragment())
                            .commit();
                    return true;
                } else if (id == R.id.duo2) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flContent3, new DanhMuc())
                            .commit();
                    return true;
                } else if (id == R.id.duo3) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flContent3, new MenuFragment2())
                            .commit();
                    return true;
                }
                return false;
            }
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flContent3, new TrangchuFragment())
                    .commit();
            nav.getMenu().findItem(R.id.duo).setChecked(true); // đánh dấu item đang chọn
        }
    }
}