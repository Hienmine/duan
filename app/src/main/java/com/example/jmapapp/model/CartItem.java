package com.example.jmapapp.model;

public class CartItem {
    public int idsanpham;
    public String tensp;
    public byte[] img;
    public int soluong;
    public double dongia;
    public double tongtien;

    public CartItem(int idsanpham, String tensp, byte[] img, int soluong, double dongia, double tongtien) {
        this.idsanpham = idsanpham;
        this.tensp = tensp;
        this.img = img;
        this.soluong = soluong;
        this.dongia = dongia;
        this.tongtien = tongtien;
    }
}
