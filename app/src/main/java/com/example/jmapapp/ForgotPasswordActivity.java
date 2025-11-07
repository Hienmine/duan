package com.example.jmapapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jmapapp.datadao.userdao;
import com.example.jmapapp.model.modeluser;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edUsername;
    private TextView tvResult;
    private Button btnShow, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edUsername = findViewById(R.id.edUsernameFP);
        tvResult   = findViewById(R.id.tvResultFP);
        btnShow    = findViewById(R.id.btnShowFP);
        btnBack    = findViewById(R.id.btnBackFP);

        userdao dao = new userdao(this);

        btnShow.setOnClickListener(v -> {
            String u = edUsername.getText().toString().trim();
            if (TextUtils.isEmpty(u)) {
                Toast.makeText(this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }
            modeluser user = dao.getByUsername(u);
            if (user == null) {
                tvResult.setText("");
                Toast.makeText(this, "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
            } else {
                // Hiển thị mật khẩu hiện tại (plain-text theo DB của bạn)
                tvResult.setText("Mật khẩu: " + user.getPass());
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}
