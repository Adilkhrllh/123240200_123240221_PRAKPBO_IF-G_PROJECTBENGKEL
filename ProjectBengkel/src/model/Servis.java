package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model Servis
 * Tabel: servis (id, id_pelanggan, plat_kendaraan, merk_kendaraan,
 *                tipe_kendaraan, jenis_servis, biaya_jasa, total_biaya,
 *                tanggal_servis, keterangan)
 */
public class Servis {

    private int    id;
    private int    idPelanggan;
    private String namaPelanggan;    // dari JOIN pelanggan
    private String platKendaraan;
    private String merkKendaraan;
    private String tipeKendaraan;
    private String jenisServis;
    private double biayaJasa;
    private double totalBiaya;
    private String tanggalServis;    // format: YYYY-MM-DD
    private String keterangan;

    private List<DetailServis> detailList = new ArrayList<>();

    public Servis() {}

    // Getter
    public int    getId()            { return id; }
    public int    getIdPelanggan()   { return idPelanggan; }
    public String getNamaPelanggan() { return namaPelanggan; }
    public String getPlatKendaraan() { return platKendaraan; }
    public String getMerkKendaraan() { return merkKendaraan; }
    public String getTipeKendaraan() { return tipeKendaraan; }
    public String getJenisServis()   { return jenisServis; }
    public double getBiayaJasa()     { return biayaJasa; }
    public double getTotalBiaya()    { return totalBiaya; }
    public String getTanggalServis() { return tanggalServis; }
    public String getKeterangan()    { return keterangan; }
    public List<DetailServis> getDetailList() { return detailList; }

    // Setter
    public void setId(int id)                           { this.id            = id; }
    public void setIdPelanggan(int idPelanggan)         { this.idPelanggan   = idPelanggan; }
    public void setNamaPelanggan(String namaPelanggan)  { this.namaPelanggan = namaPelanggan; }
    public void setPlatKendaraan(String platKendaraan)  { this.platKendaraan = platKendaraan; }
    public void setMerkKendaraan(String merkKendaraan)  { this.merkKendaraan = merkKendaraan; }
    public void setTipeKendaraan(String tipeKendaraan)  { this.tipeKendaraan = tipeKendaraan; }
    public void setJenisServis(String jenisServis)      { this.jenisServis   = jenisServis; }
    public void setBiayaJasa(double biayaJasa)          { this.biayaJasa     = biayaJasa; }
    public void setTotalBiaya(double totalBiaya)        { this.totalBiaya    = totalBiaya; }
    public void setTanggalServis(String tanggalServis)  { this.tanggalServis = tanggalServis; }
    public void setKeterangan(String keterangan)        { this.keterangan    = keterangan; }
    public void setDetailList(List<DetailServis> list)  { this.detailList    = list; }

    public void addDetail(DetailServis d) { this.detailList.add(d); }
}
