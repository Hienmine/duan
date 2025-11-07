package com.example.jmapapp.datadao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jmapapp.database.mydatabase;
import com.example.jmapapp.model.ProductRevenue;
import com.example.jmapapp.model.TopProduct;

import java.util.ArrayList;

public class baocaoDAO {
    private final SQLiteDatabase db;

    public baocaoDAO(Context context) {
        mydatabase dbHelper = new mydatabase(context);
        db = dbHelper.getReadableDatabase();
    }

    // Doanh thu theo sản phẩm - toàn thời gian
    public ArrayList<ProductRevenue> getRevenueAll() {
        String sql =
                "SELECT sp.idsanpham, sp.namesp, " +
                        "       COALESCE(SUM(hdct.soluong),0) AS tongsl, " +
                        "       COALESCE(SUM(hdct.soluong * hdct.dongia),0) AS doanhthu " +
                        "FROM sanpham sp " +
                        "LEFT JOIN hoadonchitiet hdct ON hdct.idsanpham = sp.idsanpham " +
                        "LEFT JOIN hoadon hd ON hd.idhoadon = hdct.idhoadon " +
                        "GROUP BY sp.idsanpham, sp.namesp " +
                        "ORDER BY doanhthu DESC";

        ArrayList<ProductRevenue> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            list.add(new ProductRevenue(
                    c.getInt(0),
                    c.getString(1),
                    c.getInt(2),
                    c.getDouble(3)
            ));
        }
        c.close();
        return list;
    }

    // Doanh thu theo sản phẩm - lọc khoảng ngày [from, to] (bao gồm cả 2 đầu)
    // from/to dạng "YYYY-MM-DD HH:MM:SS" hoặc "YYYY-MM-DD" (sẽ tự bổ sung giờ)
    public ArrayList<ProductRevenue> getRevenueByDate(String from, String to) {
        String fromFixed = normalizeStart(from);
        String toFixed   = normalizeEnd(to);

        String sql =
                "SELECT sp.idsanpham, sp.namesp, " +
                        "       COALESCE(SUM(hdct.soluong),0) AS tongsl, " +
                        "       COALESCE(SUM(hdct.soluong * hdct.dongia),0) AS doanhthu " +
                        "FROM sanpham sp " +
                        "LEFT JOIN hoadonchitiet hdct ON hdct.idsanpham = sp.idsanpham " +
                        "LEFT JOIN hoadon hd ON hd.idhoadon = hdct.idhoadon " +
                        "WHERE hd.trangthai = 'đã thanh toán' AND hd.ngaylap BETWEEN ? AND ? " +
                        "GROUP BY sp.idsanpham, sp.namesp " +
                        "ORDER BY doanhthu DESC";

        ArrayList<ProductRevenue> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql, new String[]{fromFixed, toFixed});
        while (c.moveToNext()) {
            list.add(new ProductRevenue(
                    c.getInt(0),
                    c.getString(1),
                    c.getInt(2),
                    c.getDouble(3)
            ));
        }
        c.close();
        return list;
    }

    // Tổng doanh thu toàn thời gian
    public double getTotalRevenueAll() {
        String sql = "SELECT COALESCE(SUM(hdct.soluong * hdct.dongia),0) " +
                "FROM hoadonchitiet hdct";
        Cursor c = db.rawQuery(sql, null);
        double total = 0;
        if (c.moveToFirst()) total = c.getDouble(0);
        c.close();
        return total;
    }

    // Tổng doanh thu theo khoảng ngày
    public double getTotalRevenueByDate(String from, String to) {
        String fromFixed = normalizeStart(from);
        String toFixed   = normalizeEnd(to);
        String sql = "SELECT COALESCE(SUM(hdct.soluong * hdct.dongia),0) " +
                "FROM hoadonchitiet hdct " +
                "JOIN hoadon hd ON hd.idhoadon = hdct.idhoadon " +
                "WHERE hd.ngaylap BETWEEN ? AND ?";
        Cursor c = db.rawQuery(sql, new String[]{fromFixed, toFixed});
        double total = 0;
        if (c.moveToFirst()) total = c.getDouble(0);
        c.close();
        return total;
    }

    // --- helpers ---
    private String normalizeStart(String s) {
        if (s == null) return "0000-01-01 00:00:00";
        s = s.trim();
        if (s.length() == 10) return s + " 00:00:00"; // YYYY-MM-DD
        if (s.length() == 16) return s + ":00";       // YYYY-MM-DD HH:MM
        return s;
    }
    private String normalizeEnd(String s) {
        if (s == null) return "9999-12-31 23:59:59";
        s = s.trim();
        if (s.length() == 10) return s + " 23:59:59"; // YYYY-MM-DD
        if (s.length() == 16) return s + ":59";       // YYYY-MM-DD HH:MM
        return s;
    }
    // === TOP bán chạy: toàn thời gian ===
    public ArrayList<TopProduct> getTopByQtyAll(int limit) {
        String sql =
                "SELECT sp.idsanpham, sp.namesp, " +
                        "       COALESCE(SUM(hdct.soluong),0) AS sl, " +
                        "       COALESCE(SUM(hdct.soluong * hdct.dongia),0) AS dt " +
                        "FROM sanpham sp " +
                        "LEFT JOIN hoadonchitiet hdct ON hdct.idsanpham = sp.idsanpham " +
                        "LEFT JOIN hoadon hd ON hd.idhoadon = hdct.idhoadon " +
                        "GROUP BY sp.idsanpham, sp.namesp " +
                        "ORDER BY sl DESC, dt DESC " +
                        "LIMIT " + Math.max(1, limit);

        ArrayList<TopProduct> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            list.add(new TopProduct(
                    c.getInt(0), c.getString(1),
                    c.getInt(2), c.getDouble(3)
            ));
        }
        c.close();
        return list;
    }

    // === TOP bán chạy: theo khoảng ngày & trạng thái ===
// statusFilter: null hoặc "Tất cả" -> không lọc, còn lại dùng đúng giá trị trong bảng (vd "đã thanh toán")
    public ArrayList<TopProduct> getTopByQty(String from, String to, String statusFilter, int limit) {
        String fromFixed = normalizeStart(from);
        String toFixed   = normalizeEnd(to);

        StringBuilder sb = new StringBuilder();
        ArrayList<String> args = new ArrayList<>();
        sb.append(
                "SELECT sp.idsanpham, sp.namesp, " +
                        "       COALESCE(SUM(hdct.soluong),0) AS sl, " +
                        "       COALESCE(SUM(hdct.soluong * hdct.dongia),0) AS dt " +
                        "FROM sanpham sp " +
                        "LEFT JOIN hoadonchitiet hdct ON hdct.idsanpham = sp.idsanpham " +
                        "LEFT JOIN hoadon hd ON hd.idhoadon = hdct.idhoadon " +
                        "WHERE hd.ngaylap BETWEEN ? AND ? "
        );
        args.add(fromFixed); args.add(toFixed);

        if (statusFilter != null && statusFilter.trim().length() > 0 && !"Tất cả".equals(statusFilter)) {
            sb.append("AND hd.trangthai = ? ");
            args.add(statusFilter);
        }

        sb.append("GROUP BY sp.idsanpham, sp.namesp ");
        sb.append("ORDER BY sl DESC, dt DESC ");
        sb.append("LIMIT ").append(Math.max(1, limit));

        Cursor c = db.rawQuery(sb.toString(), args.toArray(new String[0]));

        ArrayList<TopProduct> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(new TopProduct(
                    c.getInt(0), c.getString(1),
                    c.getInt(2), c.getDouble(3)
            ));
        }
        c.close();
        return list;
    }

}
