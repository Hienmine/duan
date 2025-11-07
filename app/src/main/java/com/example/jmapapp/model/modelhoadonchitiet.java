package com.example.jmapapp.model;

public class modelhoadonchitiet {
    private int idhoadonchitiet;
    private int idhoadon;
    private int idsanpham;
    private int soluong;
    private double dongia;

    public modelhoadonchitiet(int idhoadonchitiet, int idhoadon, int idsanpham, int soluong, double dongia) {
        this.idhoadonchitiet = idhoadonchitiet;
        this.idhoadon = idhoadon;
        this.idsanpham = idsanpham;
        this.soluong = soluong;
        this.dongia = dongia;
    }

    public int getIdhoadonchitiet() {
        return idhoadonchitiet;
    }

    public void setIdhoadonchitiet(int idhoadonchitiet) {
        this.idhoadonchitiet = idhoadonchitiet;
    }

    public int getIdhoadon() {
        return idhoadon;
    }

    public void setIdhoadon(int idhoadon) {
        this.idhoadon = idhoadon;
    }

    public int getIdsanpham() {
        return idsanpham;
    }

    public void setIdsanpham(int idsanpham) {
        this.idsanpham = idsanpham;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public double getDongia() {
        return dongia;
    }

    public void setDongia(double dongia) {
        this.dongia = dongia;
    }
}
