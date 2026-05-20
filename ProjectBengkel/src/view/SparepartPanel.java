package view;

import controller.SparepartController;
import model.Sparepart;
import util.FormatUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * SparepartPanel - View Modul Inventaris Sparepart
 * Fitur: Tambah | Update | Hapus | Lihat Daftar
 */
public class SparepartPanel extends JPanel {

    private final SparepartController ctrl = new SparepartController();

    private JTable            table;
    private DefaultTableModel tableModel;
    private JTextField        txtNama, txtMerk, txtHarga, txtStok;
    private JButton           btnTambah, btnUpdate, btnHapus, btnBersih;
    private int               selectedId = -1;

    public SparepartPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buatForm(),  BorderLayout.NORTH);
        add(buatTabel(), BorderLayout.CENTER);
        muatData();
    }

    // ── FORM ────────────────────────────────────────────────────────
    private JPanel buatForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("✏️  Form Sparepart"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;

        // Nama
        g.gridx = 0; g.gridy = 0; p.add(new JLabel("Nama Sparepart *"), g);
        g.gridx = 1; txtNama = new JTextField(22); p.add(txtNama, g);
        // Merk
        g.gridx = 2; p.add(new JLabel("Merk"), g);
        g.gridx = 3; txtMerk = new JTextField(14); p.add(txtMerk, g);
        // Harga
        g.gridx = 0; g.gridy = 1; p.add(new JLabel("Harga (Rp) *"), g);
        g.gridx = 1; txtHarga = new JTextField(22); p.add(txtHarga, g);
        // Stok
        g.gridx = 2; p.add(new JLabel("Stok *"), g);
        g.gridx = 3; txtStok = new JTextField(14); p.add(txtStok, g);

        // Tombol
        btnTambah = tombol("Tambah",    new Color(52, 152, 219));
        btnUpdate = tombol("Update",    new Color(39, 174, 96));
        btnHapus  = tombol("Hapus",     new Color(231, 76, 60));
        btnBersih = tombol("Bersihkan", new Color(127, 140, 141));
        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);

        JPanel pb = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pb.add(btnTambah); pb.add(btnUpdate); pb.add(btnHapus); pb.add(btnBersih);
        g.gridx = 0; g.gridy = 2; g.gridwidth = 4; p.add(pb, g);

        btnTambah.addActionListener(e -> aksiTambah());
        btnUpdate.addActionListener(e -> aksiUpdate());
        btnHapus.addActionListener(e  -> aksiHapus());
        btnBersih.addActionListener(e -> bersihkan());
        return p;
    }

    // ── TABEL ───────────────────────────────────────────────────────
    private JPanel buatTabel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("📦  Daftar Sparepart"));

        tableModel = new DefaultTableModel(
            new String[]{"ID", "Nama Sparepart", "Merk", "Harga", "Stok"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(4).setMaxWidth(70);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0)
                isiForm(table.getSelectedRow());
        });
        p.add(new JScrollPane(table));
        return p;
    }

    // ── AKSI TOMBOL ─────────────────────────────────────────────────
    private void aksiTambah() {
        String r = ctrl.tambah(txtNama.getText().trim(), txtMerk.getText().trim(),
                               txtHarga.getText().trim(), txtStok.getText().trim());
        if ("SUCCESS".equals(r)) { info("Sparepart berhasil ditambahkan!"); bersihkan(); muatData(); }
        else error(r);
    }

    private void aksiUpdate() {
        if (selectedId < 0) return;
        String r = ctrl.update(selectedId, txtNama.getText().trim(), txtMerk.getText().trim(),
                               txtHarga.getText().trim(), txtStok.getText().trim());
        if ("SUCCESS".equals(r)) { info("Data berhasil diperbarui!"); bersihkan(); muatData(); }
        else error(r);
    }

    private void aksiHapus() {
        if (selectedId < 0) return;
        if (JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus sparepart ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String r = ctrl.hapus(selectedId);
            if ("SUCCESS".equals(r)) { info("Sparepart dihapus."); bersihkan(); muatData(); }
            else error(r);
        }
    }

    // ── HELPER ──────────────────────────────────────────────────────
    private void isiForm(int row) {
        selectedId = (int) tableModel.getValueAt(row, 0);
        txtNama.setText((String) tableModel.getValueAt(row, 1));
        txtMerk.setText((String) tableModel.getValueAt(row, 2));
        // Ambil nilai numerik dari format rupiah
        String hargaRaw = tableModel.getValueAt(row, 3).toString().replaceAll("[^0-9]", "");
        txtHarga.setText(hargaRaw);
        txtStok.setText(tableModel.getValueAt(row, 4).toString());
        btnUpdate.setEnabled(true); btnHapus.setEnabled(true); btnTambah.setEnabled(false);
    }

    private void bersihkan() {
        txtNama.setText(""); txtMerk.setText(""); txtHarga.setText(""); txtStok.setText("");
        selectedId = -1; table.clearSelection();
        btnTambah.setEnabled(true); btnUpdate.setEnabled(false); btnHapus.setEnabled(false);
    }

    public void muatData() {
        tableModel.setRowCount(0);
        List<Sparepart> list = ctrl.getAll();
        for (Sparepart sp : list) {
            tableModel.addRow(new Object[]{
                sp.getId(), sp.getNama(), sp.getMerk(),
                FormatUtil.formatRupiah(sp.getHarga()), sp.getStok()
            });
        }
    }

    private JButton tombol(String label, Color bg) {
        JButton b = new JButton(label);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
    private void info(String msg)  { JOptionPane.showMessageDialog(this, msg, "Info",  JOptionPane.INFORMATION_MESSAGE); }
    private void error(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
}
