package com.example.jmapapp.model;

public class modelhoadon {
    private int idhoadon;
    private String ngaylap;
    private String trangthai;
    private int iduser;

    public modelhoadon(int idhoadon, String ngaylap, String trangthai, int iduser) {
        this.idhoadon = idhoadon;
        this.ngaylap = ngaylap;
        this.trangthai = trangthai;
        this.iduser = iduser;
    }

    public int getIdhoadon() {
        return idhoadon;
    }

    public void setIdhoadon(int idhoadon) {
        this.idhoadon = idhoadon;
    }

    public String getNgaylap() {
        return ngaylap;
    }

    public void setNgaylap(String ngaylap) {
        this.ngaylap = ngaylap;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }
}
