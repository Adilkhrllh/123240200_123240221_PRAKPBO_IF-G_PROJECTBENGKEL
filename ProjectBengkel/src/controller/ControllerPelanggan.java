package controller;

import dao.DAOPelanggan;
import model.Pelanggan;

import java.util.List;

/**
 * ControllerPelanggan - Validasi & koordinasi View ↔ DAO untuk pelanggan
 */
public class ControllerPelanggan {

    private final DAOPelanggan dao = new DAOPelanggan();

    public String tambah(String nama, String telepon, String alamat) {
        if (nama.isBlank()) return "Nama pelanggan wajib diisi!";
        return dao.tambah(new Pelanggan(nama, telepon, alamat)) ? "SUCCESS" : "Gagal menyimpan pelanggan.";
    }

    public String update(int id, String nama, String telepon, String alamat) {
        if (nama.isBlank()) return "Nama pelanggan wajib diisi!";
        return dao.update(new Pelanggan(id, nama, telepon, alamat)) ? "SUCCESS" : "Gagal update pelanggan.";
    }

    public String hapus(int id) {
        return dao.hapus(id) ? "SUCCESS" : "Gagal hapus. Pelanggan mungkin memiliki data servis.";
    }

    public List<Pelanggan> getAll()        { return dao.getAll(); }
    public Pelanggan       getById(int id) { return dao.getById(id); }
}
