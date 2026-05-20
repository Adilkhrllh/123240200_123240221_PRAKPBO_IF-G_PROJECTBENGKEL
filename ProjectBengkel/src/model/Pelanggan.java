package model;

/**
 * Model Pelanggan
 * Tabel: pelanggan (id, nama, telepon, alamat)
 */
public class Pelanggan {

    private int    id;
    private String nama;
    private String telepon;
    private String alamat;

    public Pelanggan() {}

    public Pelanggan(String nama, String telepon, String alamat) {
        this.nama    = nama;
        this.telepon = telepon;
        this.alamat  = alamat;
    }

    public Pelanggan(int id, String nama, String telepon, String alamat) {
        this.id      = id;
        this.nama    = nama;
        this.telepon = telepon;
        this.alamat  = alamat;
    }

    public int    getId()      { return id; }
    public String getNama()    { return nama; }
    public String getTelepon() { return telepon; }
    public String getAlamat()  { return alamat; }

    public void setId(int id)         { this.id      = id; }
    public void setNama(String n)     { this.nama    = n; }
    public void setTelepon(String t)  { this.telepon = t; }
    public void setAlamat(String a)   { this.alamat  = a; }

    @Override
    public String toString() { return nama; }
}
