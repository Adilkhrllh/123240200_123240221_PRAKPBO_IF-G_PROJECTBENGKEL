package model;

/**
 * Model Sparepart
 * Tabel: sparepart (id, nama, merk, harga, stok)
 */
public class Sparepart {

    private int    id;
    private String nama;
    private String merk;
    private double harga;
    private int    stok;

    public Sparepart() {}

    public Sparepart(String nama, String merk, double harga, int stok) {
        this.nama  = nama;
        this.merk  = merk;
        this.harga = harga;
        this.stok  = stok;
    }

    public Sparepart(int id, String nama, String merk, double harga, int stok) {
        this.id    = id;
        this.nama  = nama;
        this.merk  = merk;
        this.harga = harga;
        this.stok  = stok;
    }

    public int    getId()    { return id; }
    public String getNama()  { return nama; }
    public String getMerk()  { return merk; }
    public double getHarga() { return harga; }
    public int    getStok()  { return stok; }

    public void setId(int id)       { this.id    = id; }
    public void setNama(String n)   { this.nama  = n; }
    public void setMerk(String m)   { this.merk  = m; }
    public void setHarga(double h)  { this.harga = h; }
    public void setStok(int s)      { this.stok  = s; }

    @Override
    public String toString() { return nama + " (" + merk + ")"; }
}
