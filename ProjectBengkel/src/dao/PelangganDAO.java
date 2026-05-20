package dao;

import database.DatabaseConnection;
import model.Pelanggan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PelangganDAO - Operasi database untuk tabel pelanggan
 * Kolom: id | nama | telepon | alamat
 */
public class PelangganDAO {

    public boolean tambah(Pelanggan p) {
        String sql = "INSERT INTO pelanggan (nama, telepon, alamat) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, p.getNama());
            ps.setString(2, p.getTelepon());
            ps.setString(3, p.getAlamat());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[DAO] tambah pelanggan: " + e.getMessage());
            return false;
        }
    }

    public List<Pelanggan> getAll() {
        List<Pelanggan> list = new ArrayList<>();
        String sql = "SELECT * FROM pelanggan ORDER BY nama ASC";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.err.println("[DAO] getAll pelanggan: " + e.getMessage());
        }
        return list;
    }

    public Pelanggan getById(int id) {
        String sql = "SELECT * FROM pelanggan WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.err.println("[DAO] getById pelanggan: " + e.getMessage());
        }
        return null;
    }

    public boolean update(Pelanggan p) {
        String sql = "UPDATE pelanggan SET nama=?, telepon=?, alamat=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, p.getNama());
            ps.setString(2, p.getTelepon());
            ps.setString(3, p.getAlamat());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[DAO] update pelanggan: " + e.getMessage());
            return false;
        }
    }

    public boolean hapus(int id) {
        String sql = "DELETE FROM pelanggan WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[DAO] hapus pelanggan: " + e.getMessage());
            return false;
        }
    }

    private Pelanggan map(ResultSet rs) throws SQLException {
        return new Pelanggan(
            rs.getInt("id"),
            rs.getString("nama"),
            rs.getString("telepon"),
            rs.getString("alamat")
        );
    }
}
