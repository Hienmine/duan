package com.example.jmapapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class mydatabase extends SQLiteOpenHelper {

    public mydatabase(Context context){
        super(context,"QlJmark",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
        String tuse="CREATE TABLE USER(iduser Integer primary key autoincrement ,username text not null UNIQUE,pass text not null,name text not null,gioitinh text not null,diachi text,tuoi integer,role text check(role in('admin', 'staff', 'customer')) NOT NULL DEFAULT 'customer')";
        db.execSQL(tuse);
        String thoadon="CREATE TABLE hoadon(idhoadon integer primary key autoincrement,ngaylap text,trangthai text check(trangthai in('Chờ xác nhận','đã thanh toán','chưa thanh toán'))DEFAULT 'Chờ xác nhận',iduser integer not null,foreign key(iduser) references USER(iduser))";
        db.execSQL(thoadon);
        String tdanhmuc="CREATE TABLE danhmuc(iddanhmuc integer primary key autoincrement,name text not null,imgdm BLOB)";
        db.execSQL(tdanhmuc);
        String tsanpham="CREATE TABLE sanpham(idsanpham integer primary key autoincrement,namesp text not null,gia real not null,mota text,tonkho integer default 0,img BLOB,iddanhmuc integer,foreign key (iddanhmuc) references danhmuc(iddanhmuc))";
        db.execSQL(tsanpham);
        String thoadonchitiet="CREATE TABLE hoadonchitiet(idhoadonchitiet INTEGER PRIMARY KEY autoincrement,idhoadon iNTEGER not null,idsanpham INTEGER not null,soluong INTEGER not null check(soluong>=0),dongia decimal(12,2) not null,foreign key (idhoadon) references hoadon(idhoadon) ON DELETE CASCADE ON UPDATE CASCADE,foreign key (idsanpham) references sanpham(idsanpham)ON DELETE CASCADE ON UPDATE CASCADE,unique(idsanpham,idhoadon))";
        db.execSQL(thoadonchitiet);
        String dataItem="INSERT INTO USER (username,name, pass, gioitinh, diachi, tuoi, role) VALUES" +
                "('admin01','hien', '123456', 'Nam', 'Hà Nội', 30, 'admin'),\n" +
                "('staff01','dung', '123456', 'Nữ', 'Đà Nẵng', 25, 'staff'),\n" +
                "('khach01','khoa', '123456', 'Nam', 'TP.HCM', 22, 'customer');\n";
        String datadanhmuc="INSERT INTO danhmuc (name) VALUES" +
                "('Laptop')," +
                "('Điện thoại')," +
                "('Phụ kiện');";
        String datasanpham="INSERT INTO sanpham (namesp, gia,mota, tonkho, iddanhmuc) VALUES" +
                "('MacBook Air M2', 25990000, 'Mỏng nhẹ, chip M2, pin 18 giờ', 10, 1)," +
                "('iPhone 14 Pro', 29990000, '128GB, camera 48MP', 15, 2)," +
                "('Tai nghe Bluetooth', 499000, 'Chống ồn, kết nối nhanh', 50, 3);";
        String datahoadon="INSERT INTO hoadon (ngaylap, trangthai, iduser) VALUES" +
                "('2025-07-30 10:00:00', 'đã thanh toán', 3)," +
                "('2025-07-302025-07-30 14:30:00', 'Chờ xác nhận', 3);";
        String datahoadonchitiet="INSERT INTO hoadonchitiet (idhoadon, idsanpham, soluong, dongia) VALUES" +
                "(1, 1, 1, 25990000)," +
                "(1, 3, 2, 499000)," +
                "(2, 2, 1, 29990000);";
        db.execSQL(dataItem);
        db.execSQL(datadanhmuc);
        db.execSQL(datasanpham);
        db.execSQL(datahoadon);
        db.execSQL(datahoadonchitiet);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USER");
        db.execSQL("DROP TABLE IF EXISTS hoadon");
        db.execSQL("DROP TABLE IF EXISTS danhmuc");
        db.execSQL("DROP TABLE IF EXISTS sanpham");
        db.execSQL("DROP TABLE IF EXISTS hoadonchitiet");
        onCreate(db);
    }
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
