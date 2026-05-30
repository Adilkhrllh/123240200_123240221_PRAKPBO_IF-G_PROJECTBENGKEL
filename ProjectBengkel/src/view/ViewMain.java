package view;

import database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ViewMain extends JFrame {

    private ViewSparepart sparepartPanel;
    private ViewPelanggan pelangganPanel;
    private ViewServis    servisPanel;
    private ViewRiwayat   riwayatPanel;

    public ViewMain() {
        setTitle(" Aplikasi Manajemen Bengkel");
        setSize(700, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buatUI();
        setVisible(true);
    }

    private void buatUI() {
        // ── Header ─────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setPreferredSize(new Dimension(0, 55));

        JLabel labelJudul = new JLabel("   Sistem Manajemen Bengkel");
        labelJudul.setFont(new Font("SansSerif", Font.BOLD, 20));
        labelJudul.setForeground(Color.WHITE);

        header.add(labelJudul, BorderLayout.WEST);

        // ── Tab Panel ──────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 13));

        sparepartPanel = new ViewSparepart();
        pelangganPanel = new ViewPelanggan();
        servisPanel    = new ViewServis();
        riwayatPanel   = new ViewRiwayat();

        tabs.addTab("📦  Sparepart",  sparepartPanel);
        tabs.addTab("👤  Pelanggan",  pelangganPanel);
        tabs.addTab("🔧  Servis",     servisPanel);
        tabs.addTab("📋  Riwayat",    riwayatPanel);

        // Refresh data tab tertentu saat berpindah tab
        tabs.addChangeListener(e -> {
            int index = tabs.getSelectedIndex();
            if (index == 2) servisPanel.muatKomboBox();
            if (index == 3) riwayatPanel.muatData();
        });

        // ── Layout Utama ───────────────────────────────────────────
        setLayout(new BorderLayout());
        add(header,    BorderLayout.NORTH);
        add(tabs,      BorderLayout.CENTER);
    }

    // Cek koneksi DB → tampilkan dialog error jika gagal
//    private boolean cekKoneksiDatabase() {
//        try {
//            DatabaseConnection.getConnection();
//            return true;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null,
//                "❌ Gagal terhubung ke database!\n\n" +
//                "Pesan: " + e.getMessage() + "\n\n" +
//                "Pastikan:\n" +
//                "  1. MySQL server sudah berjalan\n" +
//                "  2. Database 'db_bengkel' sudah dibuat (jalankan bengkel_db.sql)\n" +
//                "  3. User 'bengkel_user' sudah ada\n" +
//                "  4. File mysql-connector-j-*.jar ada di folder lib/",
//                "Koneksi Database Gagal",
//                JOptionPane.ERROR_MESSAGE
//            );
//            return false;
//        }
//    }
}
