package com.example.jmapapp.datadao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.jmapapp.database.mydatabase;
import java.util.ArrayList;

public class HoadonchitietDao {
    private final SQLiteDatabase db;
    public HoadonchitietDao(Context ctx){ db = new mydatabase(ctx).getReadableDatabase(); }

    public static class CartItem {
        public int idCT;
        public int idSP;
        public String tenSP;
        public int soLuong;
        public double donGia;
        public double thanhTien;
        public CartItem(int idCT,int idSP,String tenSP,int soLuong,double donGia){
            this.idCT=idCT; this.idSP=idSP; this.tenSP=tenSP; this.soLuong=soLuong; this.donGia=donGia;
            this.thanhTien = soLuong * donGia;
        }
        @Override public String toString(){ return tenSP + " x" + soLuong; }
    }

    public ArrayList<CartItem> listItems(int idHoaDon){
        ArrayList<CartItem> list = new ArrayList<>();
        Cursor c = db.rawQuery(
                "SELECT hdc.idhoadonchitiet, sp.idsanpham, sp.namesp, hdc.soluong, hdc.dongia " +
                        "FROM hoadonchitiet hdc JOIN sanpham sp ON hdc.idsanpham = sp.idsanpham " +
                        "WHERE hdc.idhoadon=?", new String[]{String.valueOf(idHoaDon)}
        );
        try {
            while(c.moveToNext()){
                list.add(new CartItem(
                        c.getInt(0), c.getInt(1), c.getString(2),
                        c.getInt(3), c.getDouble(4)
                ));
            }
        } finally { c.close(); }
        return list;
    }
}
