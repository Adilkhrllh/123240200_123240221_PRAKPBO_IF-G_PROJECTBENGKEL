package view;

import controller.PelangganController;
import controller.SparepartController;
import controller.ServisController;
import model.DetailServis;
import model.Pelanggan;
import model.Sparepart;
import util.FormatUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ServisPanel - View Modul Transaksi Servis
 *
 * Alur:
 * 1. Pilih pelanggan  → isi data kendaraan
 * 2. Pilih jenis servis + isi biaya jasa
 * 3. Tambah sparepart yang digunakan (bisa lebih dari 1)
 * 4. Klik "Proses Servis" → sistem cek stok → simpan → stok berkurang otomatis
 */
public class ServisPanel extends JPanel {

    private final PelangganController pelangganCtrl = new PelangganController();
    private final SparepartController sparepartCtrl = new SparepartController();
    private final ServisController    servisCtrl    = new ServisController();

    // Form pelanggan & kendaraan
    private JComboBox<Pelanggan> cbPelanggan;
    private JTextField txtPlat, txtMerk, txtTipe, txtBiayaJasa, txtKeterangan;
    private JComboBox<String> cbJenisServis;

    // Form tambah sparepart
    private JComboBox<Sparepart> cbSparepart;
    private JTextField txtJumlah;
    private JButton btnTambahSparepart, btnHapusSparepart;

    // Tabel detail sparepart (sementara, belum disimpan)
    private JTable detailTable;
    private DefaultTableModel detailModel;
    private List<DetailServis> detailList = new ArrayList<>();

    // Label total & tombol proses
    private JLabel lblTotal;
    private JButton btnProses, btnReset;

    public ServisPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel kiri: form input
        JPanel kiri = new JPanel(new BorderLayout(6, 6));
        kiri.add(buatFormPelangganKendaraan(), BorderLayout.NORTH);
        kiri.add(buatFormSparepart(),          BorderLayout.CENTER);
        kiri.add(buatPanelProses(),            BorderLayout.SOUTH);

        add(kiri, BorderLayout.CENTER);
        muatKomboBox();
    }

    // ── FORM PELANGGAN & KENDARAAN ────────────────────────────────
    private JPanel buatFormPelangganKendaraan() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("🔧  Data Servis"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;
        g.fill   = GridBagConstraints.HORIZONTAL;

        // Baris 1: Pelanggan | Jenis Servis
        g.gridx = 0; g.gridy = 0; p.add(new JLabel("Pelanggan *"), g);
        g.gridx = 1; cbPelanggan = new JComboBox<>(); cbPelanggan.setPreferredSize(new Dimension(200, 28)); p.add(cbPelanggan, g);
        g.gridx = 2; p.add(new JLabel("Jenis Servis *"), g);
        g.gridx = 3; cbJenisServis = new JComboBox<>(FormatUtil.JENIS_SERVIS); p.add(cbJenisServis, g);

        // Baris 2: Plat | Merk
        g.gridx = 0; g.gridy = 1; p.add(new JLabel("Plat Kendaraan *"), g);
        g.gridx = 1; txtPlat = new JTextField(); p.add(txtPlat, g);
        g.gridx = 2; p.add(new JLabel("Merk Kendaraan *"), g);
        g.gridx = 3; txtMerk = new JTextField(); p.add(txtMerk, g);

        // Baris 3: Tipe | Biaya Jasa
        g.gridx = 0; g.gridy = 2; p.add(new JLabel("Tipe Kendaraan"), g);
        g.gridx = 1; txtTipe = new JTextField(); p.add(txtTipe, g);
        g.gridx = 2; p.add(new JLabel("Biaya Jasa (Rp) *"), g);
        g.gridx = 3; txtBiayaJasa = new JTextField("0"); p.add(txtBiayaJasa, g);

        // Baris 4: Keterangan
        g.gridx = 0; g.gridy = 3; p.add(new JLabel("Keterangan"), g);
        g.gridx = 1; g.gridwidth = 3;
        txtKeterangan = new JTextField();
        p.add(txtKeterangan, g);
        g.gridwidth = 1;

        return p;
    }

    // ── FORM TAMBAH SPAREPART ─────────────────────────────────────
    private JPanel buatFormSparepart() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBorder(BorderFactory.createTitledBorder("📦  Sparepart Digunakan"));

        // Sub-form: pilih sparepart + jumlah
        JPanel subForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        subForm.add(new JLabel("Sparepart:"));
        cbSparepart = new JComboBox<>(); cbSparepart.setPreferredSize(new Dimension(220, 28));
        subForm.add(cbSparepart);
        subForm.add(new JLabel("Jumlah:"));
        txtJumlah = new JTextField("1", 5); subForm.add(txtJumlah);
        btnTambahSparepart = tombol("+ Tambah", new Color(52, 152, 219)); subForm.add(btnTambahSparepart);
        btnHapusSparepart  = tombol("- Hapus",  new Color(231, 76, 60));  subForm.add(btnHapusSparepart);

        // Tabel detail
        detailModel = new DefaultTableModel(
            new String[]{"ID SP", "Nama Sparepart", "Jumlah", "Harga Satuan", "Subtotal"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        detailTable = new JTable(detailModel);
        detailTable.setRowHeight(24);
        detailTable.getColumnModel().getColumn(0).setMaxWidth(55);
        detailTable.getColumnModel().getColumn(2).setMaxWidth(60);

        p.add(subForm,                    BorderLayout.NORTH);
        p.add(new JScrollPane(detailTable), BorderLayout.CENTER);

        btnTambahSparepart.addActionListener(e -> aksiTambahSparepart());
        btnHapusSparepart.addActionListener(e  -> aksiHapusSparepart());
        return p;
    }

    // ── PANEL TOTAL & TOMBOL PROSES ───────────────────────────────
    private JPanel buatPanelProses() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        lblTotal = new JLabel("Total Biaya: Rp 0", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotal.setForeground(new Color(39, 174, 96));

        btnProses = tombol("✔  PROSES SERVIS", new Color(39, 174, 96));
        btnProses.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnReset  = tombol("↺  Reset",         new Color(127, 140, 141));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        btnPanel.add(btnReset); btnPanel.add(btnProses);

        p.add(lblTotal, BorderLayout.CENTER);
        p.add(btnPanel, BorderLayout.EAST);

        btnProses.addActionListener(e -> aksiProses());
        btnReset.addActionListener(e  -> resetForm());
        return p;
    }

    // ── AKSI TAMBAH SPAREPART KE TABEL ───────────────────────────
    private void aksiTambahSparepart() {
        Sparepart sp = (Sparepart) cbSparepart.getSelectedItem();
        if (sp == null) { error("Pilih sparepart terlebih dahulu!"); return; }

        int jumlah;
        try { jumlah = Integer.parseInt(txtJumlah.getText().trim()); }
        catch (NumberFormatException e) { error("Jumlah harus berupa angka!"); return; }
        if (jumlah <= 0) { error("Jumlah minimal 1!"); return; }
        if (jumlah > sp.getStok()) {
            error("Stok tidak cukup! Stok tersedia: " + sp.getStok()); return;
        }

        // Cek apakah sparepart ini sudah ada di list → update jumlah
        for (DetailServis d : detailList) {
            if (d.getIdSparepart() == sp.getId()) {
                error("Sparepart sudah ada di daftar. Hapus dulu jika ingin mengubah jumlah."); return;
            }
        }

        DetailServis detail = new DetailServis(sp.getId(), sp.getNama(), jumlah, sp.getHarga());
        detailList.add(detail);

        detailModel.addRow(new Object[]{
            sp.getId(), sp.getNama(), jumlah,
            FormatUtil.formatRupiah(sp.getHarga()),
            FormatUtil.formatRupiah(detail.getSubtotal())
        });

        updateTotal();
        txtJumlah.setText("1");
    }

    private void aksiHapusSparepart() {
        int row = detailTable.getSelectedRow();
        if (row < 0) { error("Pilih baris sparepart yang ingin dihapus!"); return; }
        detailList.remove(row);
        detailModel.removeRow(row);
        updateTotal();
    }

    // ── AKSI PROSES SERVIS ────────────────────────────────────────
    private void aksiProses() {
        Pelanggan plg = (Pelanggan) cbPelanggan.getSelectedItem();
        if (plg == null) { error("Pilih pelanggan terlebih dahulu!"); return; }

        String hasil = servisCtrl.prosesServis(
            plg.getId(),
            txtPlat.getText().trim(),
            txtMerk.getText().trim(),
            txtTipe.getText().trim(),
            (String) cbJenisServis.getSelectedItem(),
            txtBiayaJasa.getText().trim(),
            txtKeterangan.getText().trim(),
            detailList
        );

        if ("SUCCESS".equals(hasil)) {
            info("✅ Servis berhasil diproses!\n" + lblTotal.getText());
            resetForm();
            muatKomboBox(); // refresh stok sparepart di combobox
        } else {
            error(hasil);
        }
    }

    // ── HELPER ───────────────────────────────────────────────────
    private void updateTotal() {
        double biayaJasa = 0;
        try { biayaJasa = Double.parseDouble(txtBiayaJasa.getText().trim()); } catch (Exception ignored) {}
        double totalSp = detailList.stream().mapToDouble(DetailServis::getSubtotal).sum();
        lblTotal.setText("Total Biaya: " + FormatUtil.formatRupiah(biayaJasa + totalSp));
    }

    public void muatKomboBox() {
        // Load pelanggan
        cbPelanggan.removeAllItems();
        pelangganCtrl.getAll().forEach(cbPelanggan::addItem);
        // Load sparepart
        cbSparepart.removeAllItems();
        sparepartCtrl.getAll().forEach(cbSparepart::addItem);
    }

    private void resetForm() {
        txtPlat.setText(""); txtMerk.setText(""); txtTipe.setText("");
        txtBiayaJasa.setText("0"); txtKeterangan.setText("");
        detailList.clear(); detailModel.setRowCount(0);
        lblTotal.setText("Total Biaya: Rp 0");
        cbJenisServis.setSelectedIndex(0);
        muatKomboBox();
    }

    private JButton tombol(String label, Color bg) {
        JButton b = new JButton(label);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
    private void info(String msg)  { JOptionPane.showMessageDialog(this, msg, "Sukses", JOptionPane.INFORMATION_MESSAGE); }
    private void error(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
}
