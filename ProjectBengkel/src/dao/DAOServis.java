package dao;

import database.DatabaseConnection;
import model.DetailServis;
import model.Servis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOServis {

    private final DAOSparepart sparepartDAO = new DAOSparepart();

    // ─── SIMPAN SERVIS + DETAIL (dengan Transaction) ──────────────
    public boolean simpanServis(Servis servis) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // === MULAI TRANSAKSI ===

            // STEP 1: Validasi stok semua sparepart sebelum proses apapun
            for (DetailServis detail : servis.getDetailList()) {
                var sp = sparepartDAO.getById(detail.getIdSparepart());
                if (sp == null || sp.getStok() < detail.getJumlah()) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    System.err.println("[DAO] Stok tidak cukup: " + detail.getNamaSparepart());
                    return false;
                }
            }

            // STEP 2: Insert ke tabel servis
            String sqlServis = """
                INSERT INTO servis
                  (id_pelanggan, plat_kendaraan, merk_kendaraan, tipe_kendaraan,
                   jenis_servis, biaya_jasa, total_biaya, tanggal_servis, keterangan)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            PreparedStatement psServis = conn.prepareStatement(sqlServis, Statement.RETURN_GENERATED_KEYS);
            psServis.setInt(1, servis.getIdPelanggan());
            psServis.setString(2, servis.getPlatKendaraan());
            psServis.setString(3, servis.getMerkKendaraan());
            psServis.setString(4, servis.getTipeKendaraan());
            psServis.setString(5, servis.getJenisServis());
            psServis.setInt(6, servis.getBiayaJasa());
            psServis.setInt(7, servis.getTotalBiaya());
            psServis.setString(8, servis.getTanggalServis());
            psServis.setString(9, servis.getKeterangan());
            psServis.executeUpdate();

            // Ambil id servis yang baru dibuat (AUTO_INCREMENT)
            ResultSet keys = psServis.getGeneratedKeys();
            int idServisBaru = 0;
            if (keys.next()) idServisBaru = keys.getInt(1);

            // STEP 3: Insert detail_servis & kurangi stok untuk setiap sparepart
            String sqlDetail = "INSERT INTO detail_servis (id_servis, id_sparepart, jumlah, harga_satuan) VALUES (?, ?, ?, ?)";
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);

            for (DetailServis detail : servis.getDetailList()) {
                psDetail.setInt(1, idServisBaru);
                psDetail.setInt(2, detail.getIdSparepart());
                psDetail.setInt(3, detail.getJumlah());
                psDetail.setInt(4, detail.getHargaSatuan());
                psDetail.executeUpdate();

                // Kurangi stok di tabel sparepart
                boolean ok = sparepartDAO.kurangiStok(detail.getIdSparepart(), detail.getJumlah());
                if (!ok) {
                    conn.rollback(); // batalkan semua perubahan!
                    conn.setAutoCommit(true);
                    return false;
                }
            }

            conn.commit(); // === COMMIT: semua berhasil ===
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) { conn.rollback(); conn.setAutoCommit(true); }
            } catch (SQLException ex) { System.err.println("Rollback error: " + ex.getMessage()); }
            System.err.println("[DAO] simpanServis: " + e.getMessage());
            return false;
        }
    }

    // ─── RIWAYAT SERVIS (JOIN ke pelanggan) ───────────────────────
    public List<Servis> getRiwayat() {
        List<Servis> list = new ArrayList<>();
        String sql = """
            SELECT s.*, p.nama AS nama_pelanggan
            FROM   servis s
            JOIN   pelanggan p ON s.id_pelanggan = p.id
            ORDER  BY s.tanggal_servis DESC
        """;
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapServis(rs));
        } catch (SQLException e) {
            System.err.println("[DAO] getRiwayat: " + e.getMessage());
        }
        return list;
    }

    // ─── DETAIL SPAREPART PER SERVIS (JOIN ke sparepart) ──────────
    public List<DetailServis> getDetailByServisId(int idServis) {
        List<DetailServis> list = new ArrayList<>();
        String sql = """
            SELECT ds.*, sp.nama AS nama_sparepart
            FROM   detail_servis ds
            JOIN   sparepart sp ON ds.id_sparepart = sp.id
            WHERE  ds.id_servis = ?
        """;
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idServis);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DetailServis d = new DetailServis();
                d.setId(rs.getInt("id"));
                d.setIdServis(idServis);
                d.setIdSparepart(rs.getInt("id_sparepart"));
                d.setNamaSparepart(rs.getString("nama_sparepart"));
                d.setJumlah(rs.getInt("jumlah"));
                d.setHargaSatuan(rs.getInt("harga_satuan"));
                list.add(d);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] getDetail: " + e.getMessage());
        }
        return list;
    }

    // ─── HELPER: mapping ResultSet → Servis ──────────────────────
    private Servis mapServis(ResultSet rs) throws SQLException {
        Servis s = new Servis();
        s.setId(rs.getInt("id"));
        s.setIdPelanggan(rs.getInt("id_pelanggan"));
        s.setNamaPelanggan(rs.getString("nama_pelanggan"));
        s.setPlatKendaraan(rs.getString("plat_kendaraan"));
        s.setMerkKendaraan(rs.getString("merk_kendaraan"));
        s.setTipeKendaraan(rs.getString("tipe_kendaraan"));
        s.setJenisServis(rs.getString("jenis_servis"));
        s.setBiayaJasa(rs.getInt("biaya_jasa"));
        s.setTotalBiaya(rs.getInt("total_biaya"));
        s.setTanggalServis(rs.getString("tanggal_servis"));
        s.setKeterangan(rs.getString("keterangan"));
        return s;
    }
}
