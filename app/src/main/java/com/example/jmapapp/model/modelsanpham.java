package com.example.jmapapp.model;

public class modelsanpham {
    private int idsanpham;
    private String namesp;
    private double gia;
    private String mota;
    private int tonkho;
    private byte[] img; // lưu hình dưới dạng byte[]
    private int iddanhmuc;

    public modelsanpham(int idsanpham, String namesp, double gia, String mota, int tonkho, byte[] img, int iddanhmuc) {
        this.idsanpham = idsanpham;
        this.namesp = namesp;
        this.gia = gia;
        this.mota = mota;
        this.tonkho = tonkho;
        this.img = img;
        this.iddanhmuc = iddanhmuc;
    }

    public int getIdsanpham() {
        return idsanpham;
    }

    public void setIdsanpham(int idsanpham) {
        this.idsanpham = idsanpham;
    }

    public String getNamesp() {
        return namesp;
    }

    public void setNamesp(String namesp) {
        this.namesp = namesp;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getTonkho() {
        return tonkho;
    }

    public void setTonkho(int tonkho) {
        this.tonkho = tonkho;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public int getIddanhmuc() {
        return iddanhmuc;
    }

    public void setIddanhmuc(int iddanhmuc) {
        this.iddanhmuc = iddanhmuc;
    }
}
