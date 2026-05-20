package view;

import database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * MainFrame - Jendela utama aplikasi bengkel
 * Menggunakan JTabbedPane sebagai navigasi antar modul:
 *   Tab 1: Sparepart  (Inventaris)
 *   Tab 2: Pelanggan  (Data Pelanggan)
 *   Tab 3: Servis     (Transaksi Servis)
 *   Tab 4: Riwayat    (History Servis)
 */
public class MainFrame extends JFrame {

    private SparepartPanel sparepartPanel;
    private PelangganPanel pelangganPanel;
    private ServisPanel    servisPanel;
    private RiwayatPanel   riwayatPanel;

    public MainFrame() {
        setTitle("🔧 Aplikasi Manajemen Bengkel");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // tengah layar

        // Cek koneksi DB sebelum tampilkan UI
        if (!cekKoneksiDatabase()) return;

        buatUI();
        setVisible(true);
    }

    private void buatUI() {
        // ── Header ─────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setPreferredSize(new Dimension(0, 55));

        JLabel lblJudul = new JLabel("  🔧  Sistem Manajemen Bengkel", SwingConstants.LEFT);
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblJudul.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("MVC + DAO Architecture  |  MySQL   ", SwingConstants.RIGHT);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSub.setForeground(new Color(149, 165, 166));

        header.add(lblJudul, BorderLayout.WEST);
        header.add(lblSub,   BorderLayout.EAST);

        // ── Tab Panel ──────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 13));

        sparepartPanel = new SparepartPanel();
        pelangganPanel = new PelangganPanel();
        servisPanel    = new ServisPanel();
        riwayatPanel   = new RiwayatPanel();

        tabs.addTab("📦  Sparepart",  sparepartPanel);
        tabs.addTab("👤  Pelanggan",  pelangganPanel);
        tabs.addTab("🔧  Servis",     servisPanel);
        tabs.addTab("📋  Riwayat",    riwayatPanel);

        // Refresh data tab tertentu saat berpindah tab
        tabs.addChangeListener(e -> {
            int idx = tabs.getSelectedIndex();
            if (idx == 2) servisPanel.muatKomboBox();   // refresh stok sparepart & pelanggan
            if (idx == 3) riwayatPanel.muatData();      // refresh riwayat servis
        });

        // ── Status Bar ─────────────────────────────────────────────
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 3));
        statusBar.setBackground(new Color(236, 240, 241));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        JLabel lblStatus = new JLabel("✅  Terhubung ke database: db_bengkel  |  bengkel_user@localhost");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(39, 174, 96));
        statusBar.add(lblStatus);

        // ── Layout Utama ───────────────────────────────────────────
        setLayout(new BorderLayout());
        add(header,    BorderLayout.NORTH);
        add(tabs,      BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    // Cek koneksi DB → tampilkan dialog error jika gagal
    private boolean cekKoneksiDatabase() {
        try {
            DatabaseConnection.getConnection();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "❌ Gagal terhubung ke database!\n\n" +
                "Pesan: " + e.getMessage() + "\n\n" +
                "Pastikan:\n" +
                "  1. MySQL server sudah berjalan\n" +
                "  2. Database 'db_bengkel' sudah dibuat (jalankan bengkel_db.sql)\n" +
                "  3. User 'bengkel_user' sudah ada\n" +
                "  4. File mysql-connector-j-*.jar ada di folder lib/",
                "Koneksi Database Gagal",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }
}
