package controller;

import dao.ServisDAO;
import model.DetailServis;
import model.Servis;

import java.time.LocalDate;
import java.util.List;

/**
 * ServisController - Logika bisnis transaksi servis
 * Flow: validasi input → hitung total → kirim ke DAO (DAO yang handle cek stok & transaction)
 */
public class ServisController {

    private final ServisDAO dao = new ServisDAO();

    /**
     * Proses servis baru
     * @param idPelanggan   ID pelanggan yang dipilih
     * @param plat          Plat nomor kendaraan
     * @param merk          Merk kendaraan
     * @param tipe          Tipe kendaraan
     * @param jenisServis   Jenis servis yang dilakukan
     * @param biayaJasaStr  Biaya jasa montir (dari TextField)
     * @param keterangan    Catatan tambahan
     * @param detailList    Daftar sparepart yang digunakan
     * @return "SUCCESS" atau pesan error
     */
    public String prosesServis(int idPelanggan, String plat, String merk, String tipe,
                               String jenisServis, String biayaJasaStr,
                               String keterangan, List<DetailServis> detailList) {

        // Validasi input wajib
        if (idPelanggan <= 0)      return "Pilih pelanggan terlebih dahulu!";
        if (plat.isBlank())        return "Plat nomor kendaraan wajib diisi!";
        if (merk.isBlank())        return "Merk kendaraan wajib diisi!";
        if (jenisServis.isBlank()) return "Jenis servis wajib dipilih!";

        double biayaJasa;
        try {
            biayaJasa = Double.parseDouble(biayaJasaStr.replace(",", "."));
        } catch (NumberFormatException e) {
            return "Biaya jasa harus berupa angka!";
        }
        if (biayaJasa < 0) return "Biaya jasa tidak boleh negatif!";

        // Hitung total = biaya jasa + subtotal semua sparepart
        double totalSparepart = detailList.stream()
            .mapToDouble(DetailServis::getSubtotal)
            .sum();
        double totalBiaya = biayaJasa + totalSparepart;

        // Buat objek Servis
        Servis servis = new Servis();
        servis.setIdPelanggan(idPelanggan);
        servis.setPlatKendaraan(plat.toUpperCase());
        servis.setMerkKendaraan(merk);
        servis.setTipeKendaraan(tipe);
        servis.setJenisServis(jenisServis);
        servis.setBiayaJasa(biayaJasa);
        servis.setTotalBiaya(totalBiaya);
        servis.setTanggalServis(LocalDate.now().toString()); // YYYY-MM-DD
        servis.setKeterangan(keterangan);
        servis.setDetailList(detailList);

        boolean ok = dao.simpanServis(servis);
        return ok ? "SUCCESS" : "Stok sparepart tidak mencukupi atau terjadi kesalahan!";
    }

    public List<Servis>       getRiwayat()                   { return dao.getRiwayat(); }
    public List<DetailServis> getDetail(int idServis)        { return dao.getDetailByServisId(idServis); }
}
