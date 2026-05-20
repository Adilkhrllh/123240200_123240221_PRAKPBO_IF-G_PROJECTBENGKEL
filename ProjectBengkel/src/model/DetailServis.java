package model;

/**
 * Model DetailServis
 * Tabel: detail_servis (id, id_servis, id_sparepart, jumlah, harga_satuan)
 */
public class DetailServis {

    private int    id;
    private int    idServis;
    private int    idSparepart;
    private String namaSparepart;   // dari JOIN sparepart
    private int    jumlah;
    private double hargaSatuan;

    public DetailServis() {}

    public DetailServis(int idSparepart, String namaSparepart, int jumlah, double hargaSatuan) {
        this.idSparepart   = idSparepart;
        this.namaSparepart = namaSparepart;
        this.jumlah        = jumlah;
        this.hargaSatuan   = hargaSatuan;
    }

    public double getSubtotal() { return jumlah * hargaSatuan; }

    // Getter
    public int    getId()            { return id; }
    public int    getIdServis()      { return idServis; }
    public int    getIdSparepart()   { return idSparepart; }
    public String getNamaSparepart() { return namaSparepart; }
    public int    getJumlah()        { return jumlah; }
    public double getHargaSatuan()   { return hargaSatuan; }

    // Setter
    public void setId(int id)                         { this.id            = id; }
    public void setIdServis(int idServis)             { this.idServis      = idServis; }
    public void setIdSparepart(int idSparepart)       { this.idSparepart   = idSparepart; }
    public void setNamaSparepart(String n)            { this.namaSparepart = n; }
    public void setJumlah(int jumlah)                 { this.jumlah        = jumlah; }
    public void setHargaSatuan(double hargaSatuan)    { this.hargaSatuan   = hargaSatuan; }
}
