package com.example.jmapapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jmapapp.datadao.userdao;
import com.example.jmapapp.model.modeluser;

public class MainActivity extends AppCompatActivity {
    String role = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText edtL=findViewById(R.id.edtnameL);
        EditText edtpassL=findViewById(R.id.edtpassL);
        Button btnL=findViewById(R.id.btnL);
        TextView txt=findViewById(R.id.btntravel);
        TextView txttr=findViewById(R.id.quen);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Dangky.class);
                startActivity(intent);
            }
        });
        txttr.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
        });
        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtL.getText().toString().trim();
                String pass = edtpassL.getText().toString().trim();
                userdao dao = new userdao(MainActivity.this);

                boolean ok = false;
                modeluser currentUser = null; // ★ giữ lại user khớp

                for (modeluser acc : dao.getAll()) {
                    if (acc.getUsername().equals(user) && acc.getPass().equals(pass)) {
                        ok = true;
                        currentUser = acc;      // ★ lấy luôn đối tượng user
                        role = acc.getRole();
                        break;
                    }
                }

                if (ok && currentUser != null) {
                    // ★ LƯU phiên đăng nhập vào đúng file + key mà ProfileFragment đang đọc
                    getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("logged_user_id", currentUser.getIduser()) // ★ quan trọng
                            .putString("logged_user_role", role)
                            .apply();

                    Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                    if ("admin".equals(role)) {
                        startActivity(new Intent(MainActivity.this, Menu.class));
                        Toast.makeText(MainActivity.this, "Đã đăng nhập với quyền admin", Toast.LENGTH_SHORT).show();
                    } else if ("staff".equals(role)) {
                        startActivity(new Intent(MainActivity.this, MenuNv.class));
                        Toast.makeText(MainActivity.this, "Đã đăng nhập với quyền staff", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(MainActivity.this, Menukhach.class));
                    }

                    finish(); // ★ tránh quay lại màn login khi back
                } else {
                    Toast.makeText(MainActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}