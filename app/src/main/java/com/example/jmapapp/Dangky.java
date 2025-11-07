package com.example.jmapapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jmapapp.datadao.userdao;
import com.example.jmapapp.model.modeluser;

public class Dangky extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dangky);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText edtname=findViewById(R.id.edttenS);
        EditText edtte=findViewById(R.id.edtnmeS);
        EditText edtPass=findViewById(R.id.passS);
        EditText edtCPass=findViewById(R.id.Cpass);
        EditText edtDia=findViewById(R.id.edtdiachi);
        EditText edttuoi=findViewById(R.id.edtdateS);
        Button btnS=findViewById(R.id.btnS);
        TextView btnt=findViewById(R.id.btntrave);
        EditText srole=findViewById(R.id.role);
        RadioGroup rdgGioiTinh = findViewById(R.id.radio);
        int selectedId = rdgGioiTinh.getCheckedRadioButtonId(); // lấy ID radio đang chọn
        userdao dao=new userdao(this);
        btnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtte.getText().toString().trim();
                String name = edtname.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                String confirm = edtCPass.getText().toString().trim();
                String diachi=edtDia.getText().toString().trim();
                int tuoi=Integer.parseInt(edttuoi.getText().toString().trim());
                String role=srole.getText().toString().trim();
                int selectedId = rdgGioiTinh.getCheckedRadioButtonId(); // nhớ khai báo rdgGioiTinh trước đó
                String gioiTinh = "";
                if (selectedId == R.id.rnam) {
                    gioiTinh = "Nam";
                } else if (selectedId == R.id.rnu) {
                    gioiTinh = "Nữ";
                }
                String gioi=gioiTinh.trim();
                if (user.isEmpty()||pass.isEmpty()||confirm.isEmpty()||name.isEmpty()||gioiTinh.isEmpty()||diachi.isEmpty()||role.isEmpty()){
                    Toast.makeText(Dangky.this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass.equals(confirm)) {
                    Toast.makeText(Dangky.this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                String r;
                if (role.equals("NV")){
                     r = "staff";
                }else if (role.equals("ad")){
                     r="admin";
                }else{
                    r="";
                }
                modeluser u = new modeluser(user, pass, name, gioi, diachi, tuoi,r);
                boolean s=dao.add(u)>0;
                if (s) {
                    Toast.makeText(Dangky.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Dangky.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}