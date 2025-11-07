package com.example.jmapapp.mmm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jmapapp.database.mydatabase;
import com.example.jmapapp.model.modelhoadonchitiet;

import java.util.ArrayList;

public class hoadonchitietdao {
    private SQLiteDatabase db;

    public hoadonchitietdao(Context context) {
        mydatabase dbHelper = new mydatabase(context);
        db = dbHelper.getWritableDatabase();
    }
    public long insert(modelhoadonchitiet ct) {
        ContentValues v = new ContentValues();
        v.put("idhoadon", ct.getIdhoadon());
        v.put("idsanpham", ct.getIdsanpham());
        v.put("soluong", ct.getSoluong());
        v.put("dongia", ct.getDongia());
        return db.insert("hoadonchitiet", null, v);
    }
    public ArrayList<modelhoadonchitiet> getAll() {
        ArrayList<modelhoadonchitiet> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM hoadonchitiet", null);
        while (c.moveToNext()) {
            list.add(new modelhoadonchitiet(
                    c.getInt(0), c.getInt(1), c.getInt(2),
                    c.getInt(3), c.getDouble(4)
            ));
        }
        c.close();
        return list;
    }
    public int delete(int id) {
        return db.delete("hoadonchitiet", "idhoadonchitiet=?", new String[]{String.valueOf(id)});
    }
    public int update(modelhoadonchitiet ct) {
        ContentValues v = new ContentValues();
        v.put("idhoadon", ct.getIdhoadon());
        v.put("idsanpham", ct.getIdsanpham());
        v.put("soluong", ct.getSoluong());
        v.put("dongia", ct.getDongia());
        return db.update("hoadonchitiet", v, "idhoadonchitiet=?", new String[]{String.valueOf(ct.getIdhoadonchitiet())});
    }
}
