package controller;

import dao.DAOSparepart;
import model.Sparepart;

import java.util.List;

/**
 * ControllerSparepart - Validasi input & koordinasi antara View ↔ DAO
 */
public class ControllerSparepart {

    private final DAOSparepart dao = new DAOSparepart();

    public String tambah(String nama, String merk, String hargaStr, String stokStr) {
        if (nama.isBlank()) return "Nama sparepart wajib diisi!";
        if (hargaStr.isBlank() || stokStr.isBlank()) return "Harga dan stok wajib diisi!";

        int harga; int stok;
        try {
            harga = Integer.parseInt(hargaStr);
            stok  = Integer.parseInt(stokStr);
        } catch (NumberFormatException e) {
            return "Harga dan stok harus berupa angka!";
        }
        if (harga < 0 || stok < 0) return "Harga dan stok tidak boleh negatif!";

        return dao.tambah(new Sparepart(nama, merk, harga, stok)) ? "SUCCESS" : "Gagal menyimpan ke database.";
    }

    public String update(int id, String nama, String merk, String hargaStr, String stokStr) {
        if (nama.isBlank()) return "Nama sparepart wajib diisi!";

        int harga; int stok;
        try {
            harga = Integer.parseInt(hargaStr);
            stok  = Integer.parseInt(stokStr);
        } catch (NumberFormatException e) {
            return "Harga dan stok harus berupa angka!";
        }

        return dao.update(new Sparepart(id, nama, merk, harga, stok)) ? "SUCCESS" : "Gagal update data.";
    }

    public String hapus(int id) {
        return dao.hapus(id) ? "SUCCESS" : "Gagal menghapus. Sparepart mungkin sedang digunakan di data servis.";
    }

    public List<Sparepart> getAll()       { return dao.getAll(); }
    public Sparepart       getById(int id){ return dao.getById(id); }
}
