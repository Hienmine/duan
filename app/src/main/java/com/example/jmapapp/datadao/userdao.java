package com.example.jmapapp.datadao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jmapapp.database.mydatabase;
import com.example.jmapapp.model.modeluser;

import java.util.ArrayList;

public class userdao {
    private SQLiteDatabase db;

    public userdao(Context context) {
        mydatabase dbHelper = new mydatabase(context);
        db = dbHelper.getWritableDatabase();
    }
    public long add(modeluser user) {
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("pass", user.getPass());
        values.put("name", user.getName());
        values.put("gioitinh", user.getGioitinh());
        values.put("diachi", user.getDiachi());
        values.put("tuoi", user.getTuoi());
        values.put("role", user.getRole());
        return db.insert("USER", null, values);
    }
    public ArrayList<modeluser> getAll() {
        ArrayList<modeluser> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM USER", null);
        if (cursor.moveToFirst()) {
            do {
                modeluser user = new modeluser(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getString(7)
                );
                list.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    public int deleteUser(int id) {
        return db.delete("USER", "iduser=?", new String[]{String.valueOf(id)});
    }
    public int updateUser(modeluser user) {
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("pass", user.getPass());
        values.put("name", user.getName());
        values.put("gioitinh", user.getGioitinh());
        values.put("diachi", user.getDiachi());
        values.put("tuoi", user.getTuoi());
        values.put("role", user.getRole());
        return db.update("USER", values, "iduser=?", new String[]{String.valueOf(user.getIduser())});
    }
    public ArrayList<modeluser> getEmployees() {
        ArrayList<modeluser> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "SELECT iduser, username, pass, name, gioitinh, diachi, tuoi, role " +
                        "FROM USER WHERE role IN ('staff') ORDER BY name", null);
        if (cursor.moveToFirst()) {
            do {
                modeluser u = new modeluser(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getString(7)
                );
                list.add(u);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    // userdao.java
    public ArrayList<modeluser> getCustomers() {
        ArrayList<modeluser> list = new ArrayList<>();
        Cursor c = db.rawQuery(
                "SELECT iduser, username, pass, name, gioitinh, diachi, tuoi, role " +
                        "FROM USER WHERE role='customer' ORDER BY name", null);
        if (c.moveToFirst()) {
            do {
                list.add(new modeluser(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getInt(6),
                        c.getString(7)
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public int updatePassword(int iduser, String newPass) {
        ContentValues v = new ContentValues();
        v.put("pass", newPass);
        return db.update("USER", v, "iduser=?", new String[]{String.valueOf(iduser)});
    }
    public ArrayList<UserMin> getAllMin() {
        ArrayList<UserMin> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT iduser, name FROM USER", null);
        while (c.moveToNext()) {
            list.add(new UserMin(c.getInt(0), c.getString(1)));
        }
        c.close();
        return list;
    }

    public static class UserMin {
        public final int id;
        public final String name;
        public UserMin(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return id + " - " + name; } // hiển thị Spinner
    }
    public modeluser getById(int id) {
        modeluser u = null;
        Cursor c = db.rawQuery(
                "SELECT iduser, username, pass, name, gioitinh, diachi, tuoi, role FROM USER WHERE iduser=?",
                new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            u = new modeluser(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getInt(6),
                    c.getString(7)
            );
        }
        c.close();
        return u;
    }

    // Chỉ cập nhật thông tin cơ bản (không đổi username/role/pass ở đây)
    public int updateProfile(modeluser u) {
        ContentValues v = new ContentValues();
        v.put("name", u.getName());
        v.put("gioitinh", u.getGioitinh());
        v.put("diachi", u.getDiachi());
        v.put("tuoi", u.getTuoi());
        return db.update("USER", v, "iduser=?", new String[]{String.valueOf(u.getIduser())});
    }
    public modeluser getByUsername(String username) {
        modeluser u = null;
        Cursor c = db.rawQuery(
                "SELECT iduser, username, pass, name, gioitinh, diachi, tuoi, role " +
                        "FROM USER WHERE username=? LIMIT 1",
                new String[]{username});
        if (c.moveToFirst()) {
            u = new modeluser(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getInt(6),
                    c.getString(7)
            );
        }
        c.close();
        return u;
    }
}
