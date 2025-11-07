package com.example.jmapapp.datadao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jmapapp.database.mydatabase;
import com.example.jmapapp.model.modelsanpham;

import java.util.ArrayList;

public class itemdao {
    private SQLiteDatabase db;
    public itemdao(Context context) {
        mydatabase dbHelper = new mydatabase(context);
        db = dbHelper.getWritableDatabase();
    }
    public long insertSanPham(modelsanpham sp) {
        ContentValues values = new ContentValues();
        values.put("namesp", sp.getNamesp());
        values.put("gia", sp.getGia());
        values.put("mota", sp.getMota());
        values.put("tonkho", sp.getTonkho());
        values.put("img", sp.getImg());
        values.put("iddanhmuc", sp.getIddanhmuc());
        return db.insert("sanpham", null, values);
    }
    public ArrayList<modelsanpham> getAll() {
        ArrayList<modelsanpham> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM sanpham", null);
        if (cursor.moveToFirst()) {
            do {
                modelsanpham sp = new modelsanpham(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getBlob(5),
                        cursor.getInt(6)
                );
                list.add(sp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    public int deleteSanPham(int id) {
        return db.delete("sanpham", "idsanpham=?", new String[]{String.valueOf(id)});
    }
    public int updateSanPham(modelsanpham sp) {
        ContentValues values = new ContentValues();
        values.put("namesp", sp.getNamesp());
        values.put("gia", sp.getGia());
        values.put("mota", sp.getMota());
        values.put("tonkho", sp.getTonkho());
        values.put("img", sp.getImg());
        values.put("iddanhmuc", sp.getIddanhmuc());
        return db.update("sanpham", values, "idsanpham=?", new String[]{String.valueOf(sp.getIdsanpham())});
    }
    public modelsanpham getById(int id) {
        Cursor c = db.rawQuery(
                "SELECT idsanpham, namesp, gia, mota, tonkho, img, iddanhmuc " +
                        "FROM sanpham WHERE idsanpham=? LIMIT 1",
                new String[]{String.valueOf(id)});
        try {
            if (c.moveToFirst()) {
                return new modelsanpham(
                        c.getInt(0),
                        c.getString(1),
                        c.getDouble(2),
                        c.getString(3),
                        c.getInt(4),
                        c.getBlob(5),
                        c.getInt(6)
                );
            }
            return null; // không tìm thấy
        } finally {
            c.close();
        }
    }
}
