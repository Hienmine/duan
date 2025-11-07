package com.example.jmapapp.model;

public class modeluser {
    private int iduser;
    private String username;
    private String pass;
    private String name;
    private String gioitinh;
    private String diachi;
    private int tuoi;
    private String role;

    public modeluser(int iduser, String username, String pass, String name, String gioitinh, String diachi, int tuoi, String role) {
        this.iduser=iduser;
        this.username = username;
        this.pass = pass;
        this.name = name;
        this.gioitinh = gioitinh;
        this.diachi = diachi;
        this.tuoi = tuoi;
        this.role = role;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public modeluser(String username, String pass, String name, String gioitinh, String diachi, int tuoi, String role) {
        this.username = username;
        this.pass = pass;
        this.name = name;
        this.gioitinh = gioitinh;
        this.diachi = diachi;
        this.tuoi = tuoi;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public int getTuoi() {
        return tuoi;
    }

    public void setTuoi(int tuoi) {
        this.tuoi = tuoi;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
