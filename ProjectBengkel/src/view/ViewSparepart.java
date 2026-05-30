package view;

import controller.ControllerSparepart;
import model.Sparepart;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.border.TitledBorder;

public class ViewSparepart extends JPanel {

    private ControllerSparepart controller = new ControllerSparepart();

    private JTable            table;
    private DefaultTableModel tableModel;
    private JTextField        txtNama, txtMerk, txtHarga, txtStok;
    private JButton           btnTambah, btnUpdate, btnHapus, btnBersih;
    private int               selectedId = -1;

    public ViewSparepart() {
        setLayout(new BorderLayout(8, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buatForm(),  BorderLayout.NORTH);
        add(buatTabel(), BorderLayout.CENTER);
        muatData();
    }

    // ── FORM ────────────────────────────────────────────────────────
    private JPanel buatForm() {
        JPanel formSparepart = new JPanel(new GridBagLayout());
        TitledBorder border = BorderFactory.createTitledBorder("  Form Sparepart  ");
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 24));
        border.setTitleColor(new Color(44, 62, 80));
        border.setTitleJustification(TitledBorder.CENTER);
        formSparepart.setBorder(border);
        
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(14, 16, 14, 16);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        // Nama
        g.gridx = 0; g.gridy = 0; formSparepart.add(new JLabel("Nama Sparepart"), g);
        g.gridx = 1; txtNama = new JTextField(22); formSparepart.add(txtNama, g);
        // Merk
        g.gridx = 2; formSparepart.add(new JLabel("Merk"), g);
        g.gridx = 3; txtMerk = new JTextField(14); formSparepart.add(txtMerk, g);
        // Harga
        g.gridx = 0; g.gridy = 1; formSparepart.add(new JLabel("Harga"), g);
        g.gridx = 1; txtHarga = new JTextField(22); formSparepart.add(txtHarga, g);
        // Stok
        g.gridx = 2; formSparepart.add(new JLabel("Stok"), g);
        g.gridx = 3; txtStok = new JTextField(14); formSparepart.add(txtStok, g);

        // Tombol
        btnTambah = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnHapus  = new JButton("Hapus");
        btnBersih = new JButton("Bersihkan");
        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        panelButton.add(btnTambah); panelButton.add(btnUpdate); panelButton.add(btnHapus); panelButton.add(btnBersih);
        g.gridx = 0; g.gridy = 2; g.gridwidth = 4; formSparepart.add(panelButton, g);

        btnTambah.addActionListener(e -> aksiTambah());
        btnUpdate.addActionListener(e -> aksiUpdate());
        btnHapus.addActionListener(e  -> aksiHapus());
        btnBersih.addActionListener(e -> bersihkan());
        return formSparepart;
    }

    // ── TABEL ───────────────────────────────────────────────────────
    private JPanel buatTabel() {
        JPanel tabelSparepart = new JPanel(new BorderLayout());
        TitledBorder border = BorderFactory.createTitledBorder("  Daftar Sparepart  ");
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 24));
        border.setTitleColor(new Color(44, 62, 80));
        border.setTitleJustification(TitledBorder.CENTER);
        tabelSparepart.setBorder(border);

        tableModel = new DefaultTableModel(
            new String[]{"ID", "Nama Sparepart", "Merk", "Harga", "Stok"}, 0) {
            @Override
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
        tabelSparepart.add(new JScrollPane(table));
        return tabelSparepart;
    }

    // ── AKSI TOMBOL ─────────────────────────────────────────────────
    private void aksiTambah() {
        String r = controller.tambah(txtNama.getText().trim(), txtMerk.getText().trim(),
                               txtHarga.getText().trim(), txtStok.getText().trim());
        if ("SUCCESS".equals(r)) { info("Sparepart berhasil ditambahkan!"); bersihkan(); muatData(); }
        else error(r);
    }

    private void aksiUpdate() {
        if (selectedId < 0) return;
        String r = controller.update(selectedId, txtNama.getText().trim(), txtMerk.getText().trim(),
                               txtHarga.getText().trim(), txtStok.getText().trim());
        if ("SUCCESS".equals(r)) { info("Data berhasil diperbarui!"); bersihkan(); muatData(); }
        else error(r);
    }

    private void aksiHapus() {
        if (selectedId < 0) return;
        if (JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus sparepart ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String r = controller.hapus(selectedId);
            if ("SUCCESS".equals(r)) { info("Sparepart dihapus."); bersihkan(); muatData(); }
            else error(r);
        }
    }

    // ── HELPER ──────────────────────────────────────────────────────
    private void isiForm(int row) {
        selectedId = (int) tableModel.getValueAt(row, 0);

        txtNama.setText(tableModel.getValueAt(row, 1).toString());
        txtMerk.setText(tableModel.getValueAt(row, 2).toString());
        txtHarga.setText(tableModel.getValueAt(row, 3).toString());
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
        List<Sparepart> list = controller.getAll();
        for (Sparepart sp : list) {
            tableModel.addRow(new Object[]{
                sp.getId(), sp.getNama(), sp.getMerk(), sp.getHarga(), sp.getStok()
            });
        }
    }

    private void info(String msg)  { JOptionPane.showMessageDialog(this, msg, "Info",  JOptionPane.INFORMATION_MESSAGE); }
    private void error(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
}