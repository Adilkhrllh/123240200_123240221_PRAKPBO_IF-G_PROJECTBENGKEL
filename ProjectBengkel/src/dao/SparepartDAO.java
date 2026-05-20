package dao;

import database.DatabaseConnection;
import model.Sparepart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SparepartDAO - Operasi database untuk tabel sparepart
 * Kolom: id | nama | merk | harga | stok
 */
public class SparepartDAO {

    // ─── CREATE ──────────────────────────────────────────────────
    public boolean tambah(Sparepart sp) {
        String sql = "INSERT INTO sparepart (nama, merk, harga, stok) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, sp.getNama());
            ps.setString(2, sp.getMerk());
            ps.setDouble(3, sp.getHarga());
            ps.setInt(4, sp.getStok());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[DAO] tambah sparepart: " + e.getMessage());
            return false;
        }
    }

    // ─── READ ALL ─────────────────────────────────────────────────
    public List<Sparepart> getAll() {
        List<Sparepart> list = new ArrayList<>();
        String sql = "SELECT * FROM sparepart ORDER BY nama ASC";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] getAll sparepart: " + e.getMessage());
        }
        return list;
    }

    // ─── READ BY ID ───────────────────────────────────────────────
    public Sparepart getById(int id) {
        String sql = "SELECT * FROM sparepart WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.err.println("[DAO] getById sparepart: " + e.getMessage());
        }
        return null;
    }

    // ─── UPDATE ───────────────────────────────────────────────────
    public boolean update(Sparepart sp) {
        String sql = "UPDATE sparepart SET nama=?, merk=?, harga=?, stok=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, sp.getNama());
            ps.setString(2, sp.getMerk());
            ps.setDouble(3, sp.getHarga());
            ps.setInt(4, sp.getStok());
            ps.setInt(5, sp.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[DAO] update sparepart: " + e.getMessage());
            return false;
        }
    }

    // ─── KURANGI STOK (dipakai saat servis diproses) ─────────────
    // Pakai atomic SQL: stok dikurangi hanya kalau stok >= jumlah
    public boolean kurangiStok(int id, int jumlah) {
        String sql = "UPDATE sparepart SET stok = stok - ? WHERE id = ? AND stok >= ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, jumlah);
            ps.setInt(2, id);
            ps.setInt(3, jumlah);
            return ps.executeUpdate() > 0; // 0 = stok tidak cukup
        } catch (SQLException e) {
            System.err.println("[DAO] kurangiStok: " + e.getMessage());
            return false;
        }
    }

    // ─── DELETE ───────────────────────────────────────────────────
    public boolean hapus(int id) {
        String sql = "DELETE FROM sparepart WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[DAO] hapus sparepart: " + e.getMessage());
            return false;
        }
    }

    // ─── HELPER: mapping ResultSet → Sparepart ───────────────────
    private Sparepart map(ResultSet rs) throws SQLException {
        return new Sparepart(
            rs.getInt("id"),
            rs.getString("nama"),
            rs.getString("merk"),
            rs.getDouble("harga"),
            rs.getInt("stok")
        );
    }
}
