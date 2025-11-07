package com.example.jmapapp.model;

public class modeldanhmuc {
    private int iddanhmuc;
    private String name;
    private byte[] dmimg;
    public modeldanhmuc() {}
    public modeldanhmuc(int iddanhmuc, String name, byte[] dmimg) {
        this.iddanhmuc = iddanhmuc;
        this.name = name;
        this.dmimg = dmimg;
    }

    public int getIddanhmuc() {
        return iddanhmuc;
    }

    public void setIddanhmuc(int iddanhmuc) {
        this.iddanhmuc = iddanhmuc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getDmimg() {
        return dmimg;
    }

    public void setDmimg(byte[] dmimg) {
        this.dmimg = dmimg;
    }
}


