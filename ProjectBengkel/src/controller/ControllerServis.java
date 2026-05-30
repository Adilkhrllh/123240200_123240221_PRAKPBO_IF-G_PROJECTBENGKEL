package controller;

import dao.DAOServis;
import model.DetailServis;
import model.Servis;

import java.time.LocalDate;
import java.util.List;

public class ControllerServis {

    private final DAOServis dao = new DAOServis();

    public String prosesServis(int idPelanggan, String plat, String merk, String tipe,
                               String jenisServis, String biayaJasaStr,
                               String keterangan, List<DetailServis> detailList) {

        // Validasi input wajib
        if (idPelanggan <= 0)      return "Pilih pelanggan terlebih dahulu!";
        if (plat.isBlank())        return "Plat nomor kendaraan wajib diisi!";
        if (merk.isBlank())        return "Merk kendaraan wajib diisi!";
        if (jenisServis.isBlank()) return "Jenis servis wajib dipilih!";

        int biayaJasa;   
        try {
            biayaJasa = Integer.parseInt(biayaJasaStr.replace(",", "."));
        } catch (NumberFormatException e) {
            return "Biaya jasa harus berupa angka!";
        }
        if (biayaJasa < 0) return "Biaya jasa tidak boleh negatif!";

        // Hitung total = biaya jasa + subtotal semua sparepart
        int totalSparepart = detailList.stream()
            .mapToInt(d -> (int) d.getSubtotal()) .sum();
        int totalBiaya = biayaJasa + totalSparepart;

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
