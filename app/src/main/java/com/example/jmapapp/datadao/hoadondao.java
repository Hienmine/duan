package com.example.jmapapp.datadao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.jmapapp.database.mydatabase;
import com.example.jmapapp.model.CartItem;
import com.example.jmapapp.model.modelhoadon;

import java.util.ArrayList;

public class hoadondao {
    private SQLiteDatabase db;

    public hoadondao(Context context) {
        mydatabase dbHelper = new mydatabase(context);
        db = dbHelper.getWritableDatabase();
    }
    public long insert(modelhoadon hd) {
        ContentValues v = new ContentValues();
        v.put("ngaylap", hd.getNgaylap());
        v.put("trangthai", hd.getTrangthai());
        v.put("iduser", hd.getIduser());
        return db.insert("hoadon", null, v);
    }
    public ArrayList<modelhoadon> getAll() {
        ArrayList<modelhoadon> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM hoadon", null);
        while (c.moveToNext()) {
            list.add(new modelhoadon(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3)));
        }
        c.close();
        return list;
    }
    public int update(modelhoadon hd) {
        ContentValues v = new ContentValues();
        v.put("ngaylap", hd.getNgaylap());
        v.put("trangthai", hd.getTrangthai());
        v.put("iduser", hd.getIduser());
        return db.update("hoadon", v, "idhoadon=?", new String[]{String.valueOf(hd.getIdhoadon())});
    }
    public int delete(int id) {
        return db.delete("hoadon", "idhoadon=?", new String[]{String.valueOf(id)});
    }
    // LẤY/ TẠO hóa đơn mở (giỏ hàng) của 1 user
    public modelhoadon getOrCreateOpenCart(int userId) {
        // ưu tiên trạng thái 'chưa thanh toán' (bạn có sẵn các trạng thái)
        Cursor c = db.rawQuery(
                "SELECT idhoadon, ngaylap, trangthai, iduser FROM hoadon " +
                        "WHERE iduser=? AND trangthai='chưa thanh toán' LIMIT 1",
                new String[]{String.valueOf(userId)}
        );
        if (c.moveToFirst()) {
            modelhoadon hd = new modelhoadon(
                    c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
            c.close();
            return hd;
        }
        c.close();

        // không có → tạo mới
        ContentValues v = new ContentValues();
        v.put("ngaylap", now()); // hoặc để trống
        v.put("trangthai", "chưa thanh toán");
        v.put("iduser", userId);
        long id = db.insert("hoadon", null, v);
        return new modelhoadon((int) id, now(), "chưa thanh toán", userId);
    }

    // thêm/cộng dồn item vào giỏ
    public long addOrUpdateCartItem(int idhoadon, int idsanpham, int soLuong, double donGia) {
        // đã unique(idsanpham,idhoadon) → thử update trước
        Cursor c = db.rawQuery(
                "SELECT idhoadonchitiet, soluong FROM hoadonchitiet WHERE idhoadon=? AND idsanpham=?",
                new String[]{String.valueOf(idhoadon), String.valueOf(idsanpham)});
        if (c.moveToFirst()) {
            int idct = c.getInt(0);
            int slCu  = c.getInt(1);
            c.close();
            ContentValues v = new ContentValues();
            v.put("soluong", slCu + soLuong);
            v.put("dongia", donGia); // giữ đơn giá hiện hành
            return db.update("hoadonchitiet", v, "idhoadonchitiet=?",
                    new String[]{String.valueOf(idct)});
        }
        c.close();

        // chưa có → insert
        ContentValues v = new ContentValues();
        v.put("idhoadon", idhoadon);
        v.put("idsanpham", idsanpham);
        v.put("soluong", soLuong);
        v.put("dongia", donGia);
        return db.insert("hoadonchitiet", null, v);
    }

    // lấy giỏ hiện tại của user (idhoadon mở)
    public Integer getOpenCartId(int userId) {
        Cursor c = db.rawQuery(
                "SELECT idhoadon FROM hoadon WHERE iduser=? AND trangthai='chưa thanh toán' LIMIT 1",
                new String[]{String.valueOf(userId)});
        Integer id = null;
        if (c.moveToFirst()) id = c.getInt(0);
        c.close();
        return id;
    }

    // danh sách item trong giỏ (join để có tên, ảnh SP)
    public ArrayList<CartItem> getCartItems(int idhoadon) {
        ArrayList<CartItem> list = new ArrayList<>();
        Cursor c = db.rawQuery(
                "SELECT sp.idsanpham, sp.namesp, sp.img, hdct.soluong, hdct.dongia, " +
                        "       (hdct.soluong*hdct.dongia) AS tongtien " +
                        "FROM hoadonchitiet hdct " +
                        "JOIN sanpham sp ON sp.idsanpham = hdct.idsanpham " +
                        "WHERE hdct.idhoadon = ?",
                new String[]{String.valueOf(idhoadon)});
        while (c.moveToNext()) {
            list.add(new CartItem(
                    c.getInt(0), c.getString(1), c.getBlob(2),
                    c.getInt(3), c.getDouble(4), c.getDouble(5)
            ));
        }
        c.close();
        return list;
    }

    public double getCartTotal(int idhoadon) {
        Cursor c = db.rawQuery(
                "SELECT COALESCE(SUM(soluong*dongia),0) FROM hoadonchitiet WHERE idhoadon=?",
                new String[]{String.valueOf(idhoadon)});
        double t = 0;
        if (c.moveToFirst()) t = c.getDouble(0);
        c.close();
        return t;
    }

    // Thanh toán: set trạng thái, (tùy chọn) cập nhật ngày lập, trừ tồn kho
    public boolean checkout(int idhoadon) {
        db.beginTransaction();
        try {
            // trừ tồn kho theo từng item
            Cursor c = db.rawQuery(
                    "SELECT idsanpham, soluong FROM hoadonchitiet WHERE idhoadon=?",
                    new String[]{String.valueOf(idhoadon)});
            while (c.moveToNext()) {
                int pid = c.getInt(0);
                int sl  = c.getInt(1);
                db.execSQL("UPDATE sanpham SET tonkho = CASE WHEN tonkho>=? THEN tonkho-? ELSE 0 END " +
                        "WHERE idsanpham=?", new Object[]{sl, sl, pid});
            }
            c.close();

            ContentValues v = new ContentValues();
            v.put("trangthai", "đã thanh toán");
            v.put("ngaylap", now());
            int n = db.update("hoadon", v, "idhoadon=?", new String[]{String.valueOf(idhoadon)});

            db.setTransactionSuccessful();
            return n > 0;
        } finally {
            db.endTransaction();
        }
    }

    // helper
    private String now() {
        // đơn giản: yyyy-MM-dd HH:mm:ss
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                .format(new java.util.Date());
    }
    @Nullable
    public Integer getOpenCartIds(int userId) {
        Cursor c = db.rawQuery(
                "SELECT idhoadon FROM hoadon WHERE iduser=? AND trangthai='chưa thanh toán' " +
                        "ORDER BY idhoadon DESC LIMIT 1", new String[]{String.valueOf(userId)});
        try {
            if (c.moveToFirst()) return c.getInt(0);
            return null;
        } finally { c.close(); }
    }

    // cập nhật trạng thái nhanh
    public int updateStatus(int idHoaDon, String trangThai) {
        ContentValues v = new ContentValues();
        v.put("trangthai", trangThai);
        return db.update("hoadon", v, "idhoadon=?", new String[]{String.valueOf(idHoaDon)});
    }

}
