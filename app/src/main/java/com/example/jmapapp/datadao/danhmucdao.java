package com.example.jmapapp.datadao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jmapapp.database.mydatabase;
import com.example.jmapapp.model.modeldanhmuc;

import java.util.ArrayList;

public class danhmucdao {
    private SQLiteDatabase db;

    public danhmucdao(Context context) {
        mydatabase dbHelper = new mydatabase(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(modeldanhmuc dm) {
        ContentValues v = new ContentValues();
        v.put("name", dm.getName());
        v.put("imgdm",dm.getDmimg());
        return db.insert("danhmuc", null, v);
    }
    public ArrayList<modeldanhmuc> getAll() {
        ArrayList<modeldanhmuc> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT iddanhmuc, name, imgdm FROM danhmuc", null);
        int idxId   = c.getColumnIndexOrThrow("iddanhmuc");
        int idxName = c.getColumnIndexOrThrow("name");
        int idxImg  = c.getColumnIndex("imgdm");
        while (c.moveToNext()) {
//            list.add(new modeldanhmuc(c.getInt(0), c.getString(1),c.getBlob(2)));
            int id = c.getInt(idxId);
            String name = c.getString(idxName);

            byte[] img = null;
            if (idxImg != -1 && !c.isNull(idxImg)) {
                try { img = c.getBlob(idxImg); } catch (Exception ignore) { img = null; }
            }

            list.add(new modeldanhmuc(id, name, img));
        }
        c.close();
        return list;
    }
    public int update(modeldanhmuc dm) {
        ContentValues v = new ContentValues();
        v.put("name", dm.getName());
        v.put("imgdm",dm.getDmimg());
        return db.update("danhmuc", v, "iddanhmuc=?", new String[]{String.valueOf(dm.getIddanhmuc())});
    }

    public int delete(int id) {
        return db.delete("danhmuc", "iddanhmuc=?", new String[]{String.valueOf(id)});
    }
}
