package com.example.glmotoparts;

public class HomeModel {
    private String nama;
    private String harga;  // Pastikan harga adalah tipe String
    private String gambarurl;
    private String deskripsi;

    // Default constructor diperlukan untuk Firebase
    public HomeModel() {
    }

    public HomeModel(String nama, String harga, String gambarurl, String deskripsi) {
        this.nama = nama;
        this.harga = harga;
        this.gambarurl = gambarurl;
        this.deskripsi = deskripsi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    // Tambahkan metode setter yang mendukung tipe Long
    public void setHarga(Object harga) {
        if (harga instanceof Long) {
            this.harga = String.valueOf(harga);
        } else if (harga instanceof String) {
            this.harga = (String) harga;
        } else {
            throw new IllegalArgumentException("Unsupported type for harga");
        }
    }

    public String getGambarurl() {
        return gambarurl;
    }

    public void setGambarurl(String gambarurl) {
        this.gambarurl = gambarurl;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}