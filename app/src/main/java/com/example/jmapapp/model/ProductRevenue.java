package com.example.jmapapp.model;

public class ProductRevenue {
    private int idSanPham;
    private String tenSanPham;
    private int tongSoLuong;
    private double doanhThu;

    public ProductRevenue(int idSanPham, String tenSanPham, int tongSoLuong, double doanhThu) {
        this.idSanPham = idSanPham;
        this.tenSanPham = tenSanPham;
        this.tongSoLuong = tongSoLuong;
        this.doanhThu = doanhThu;
    }

    public int getIdSanPham() { return idSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public int getTongSoLuong() { return tongSoLuong; }
    public double getDoanhThu() { return doanhThu; }
}
