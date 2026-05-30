package view;

import controller.ControllerPelanggan;
import controller.ControllerSparepart;
import controller.ControllerServis;
import model.DetailServis;
import model.Pelanggan;
import model.Sparepart;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.TitledBorder;

public class ViewServis extends JPanel {
    
    private static String[] JENIS_SERVIS = {
        "Ganti Oli",
        "Tune Up",
        "Servis Ringan",
        "Servis Berat",
        "Ganti Ban",
        "Servis Rem",
        "Ganti Aki",
        "Lainnya"
    };

    private final ControllerPelanggan pelangganCtrl = new ControllerPelanggan();
    private final ControllerSparepart sparepartCtrl = new ControllerSparepart();
    private final ControllerServis    servisCtrl    = new ControllerServis();

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

    public ViewServis() {
        setLayout(new BorderLayout(8, 20));
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
        JPanel formServis = new JPanel(new GridBagLayout());
        TitledBorder border = BorderFactory.createTitledBorder("  Data Servis  ");
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 24));
        border.setTitleColor(new Color(44, 62, 80));
        border.setTitleJustification(TitledBorder.CENTER);
        formServis.setBorder(border);
        
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 12, 10, 12);
        g.anchor = GridBagConstraints.WEST;
        g.fill   = GridBagConstraints.HORIZONTAL;

        // Baris 1: Pelanggan | Jenis Servis
        g.gridx = 0; g.gridy = 0; formServis.add(new JLabel("Pelanggan *"), g);
        g.gridx = 1; cbPelanggan = new JComboBox<>(); cbPelanggan.setPreferredSize(new Dimension(200, 28)); formServis.add(cbPelanggan, g);
        g.gridx = 2; formServis.add(new JLabel("Jenis Servis *"), g);
        g.gridx = 3; cbJenisServis = new JComboBox<>(JENIS_SERVIS); formServis.add(cbJenisServis, g);

        // Baris 2: Plat | Merk
        g.gridx = 0; g.gridy = 1; formServis.add(new JLabel("Plat Kendaraan *"), g);
        g.gridx = 1; txtPlat = new JTextField(); formServis.add(txtPlat, g);
        g.gridx = 2; formServis.add(new JLabel("Merk Kendaraan *"), g);
        g.gridx = 3; txtMerk = new JTextField(); formServis.add(txtMerk, g);

        // Baris 3: Tipe | Biaya Jasa
        g.gridx = 0; g.gridy = 2; formServis.add(new JLabel("Tipe Kendaraan"), g);
        g.gridx = 1; txtTipe = new JTextField(); formServis.add(txtTipe, g);
        g.gridx = 2; formServis.add(new JLabel("Biaya Jasa (Rp) *"), g);
        g.gridx = 3; txtBiayaJasa = new JTextField("0"); formServis.add(txtBiayaJasa, g);

        // Baris 4: Keterangan
        g.gridx = 0; g.gridy = 3; formServis.add(new JLabel("Keterangan"), g);
        g.gridx = 1; g.gridwidth = 3;
        txtKeterangan = new JTextField();
        formServis.add(txtKeterangan, g);
        g.gridwidth = 1;

        return formServis;
    }

    // ── FORM TAMBAH SPAREPART ─────────────────────────────────────
    private JPanel buatFormSparepart() {
        JPanel tabelSparepart = new JPanel(new BorderLayout(6, 6));
        TitledBorder border = BorderFactory.createTitledBorder("  Sparepart Digunakan  ");
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 24));
        border.setTitleColor(new Color(44, 62, 80));
        border.setTitleJustification(TitledBorder.CENTER);
        tabelSparepart.setBorder(border);

        // Sub-form: pilih sparepart + jumlah
        JPanel subForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        subForm.add(new JLabel("Sparepart:"));
        cbSparepart = new JComboBox<>(); cbSparepart.setPreferredSize(new Dimension(220, 28));
        subForm.add(cbSparepart);
        subForm.add(new JLabel("Jumlah:"));
        txtJumlah = new JTextField("1", 5); subForm.add(txtJumlah);
        btnTambahSparepart = new JButton("Tambah");
        btnHapusSparepart = new JButton("Hapus");
        subForm.add(btnTambahSparepart);
        subForm.add(btnHapusSparepart);

        // Tabel detail
        detailModel = new DefaultTableModel(
            new String[]{"ID SP", "Nama Sparepart", "Jumlah", "Harga Satuan", "Subtotal"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        detailTable = new JTable(detailModel);
        detailTable.setRowHeight(24);
        detailTable.getColumnModel().getColumn(0).setMaxWidth(55);
        detailTable.getColumnModel().getColumn(2).setMaxWidth(60);

        tabelSparepart.add(subForm,                    BorderLayout.NORTH);
        tabelSparepart.add(new JScrollPane(detailTable), BorderLayout.CENTER);

        btnTambahSparepart.addActionListener(e -> aksiTambahSparepart());
        btnHapusSparepart.addActionListener(e  -> aksiHapusSparepart());
        return tabelSparepart;
    }

    // ── PANEL TOTAL & TOMBOL PROSES ───────────────────────────────
    private JPanel buatPanelProses() {
        JPanel tombol = new JPanel(new BorderLayout());
        tombol.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        lblTotal = new JLabel("Total Biaya: 0", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotal.setForeground(new Color(39, 174, 96));

        btnProses = tombol("PROSES SERVIS", new Color(39, 174, 96));
        btnProses.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnReset  = tombol("Reset",         new Color(127, 140, 141));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        btnPanel.add(btnReset); btnPanel.add(btnProses);

        tombol.add(lblTotal, BorderLayout.CENTER);
        tombol.add(btnPanel, BorderLayout.EAST);

        btnProses.addActionListener(e -> aksiProses());
        btnReset.addActionListener(e  -> resetForm());
        return tombol;
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
            sp.getId(), sp.getNama(), jumlah, sp.getHarga(), detail.getSubtotal()
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
            info("Servis berhasil diproses!\n" + lblTotal.getText());
            resetForm();
            muatKomboBox(); // refresh stok sparepart di combobox
        } else {
            error(hasil);
        }
    }

    // ── HELPER ───────────────────────────────────────────────────
    private void updateTotal() {
        int biayaJasa = 0;
        try {
        biayaJasa = Integer.parseInt(txtBiayaJasa.getText().trim()); } catch (NumberFormatException ignored) {}
        int totalSp = detailList.stream() .mapToInt(d -> (int) d.getSubtotal()) .sum();
        int total = biayaJasa + totalSp;
        lblTotal.setText("Total Biaya: " + (biayaJasa + totalSp));
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
        lblTotal.setText("Total Biaya: 0");
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
