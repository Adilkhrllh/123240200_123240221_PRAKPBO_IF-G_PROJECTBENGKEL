package view;

import controller.PelangganController;
import model.Pelanggan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * PelangganPanel - View Modul Data Pelanggan
 * Fitur: Tambah | Update | Hapus | Lihat Daftar
 */
public class PelangganPanel extends JPanel {

    private final PelangganController ctrl = new PelangganController();

    private JTable            table;
    private DefaultTableModel tableModel;
    private JTextField        txtNama, txtTelepon;
    private JTextArea         txtAlamat;
    private JButton           btnTambah, btnUpdate, btnHapus, btnBersih;
    private int               selectedId = -1;

    public PelangganPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buatForm(),  BorderLayout.NORTH);
        add(buatTabel(), BorderLayout.CENTER);
        muatData();
    }

    private JPanel buatForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("✏️  Form Pelanggan"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; p.add(new JLabel("Nama Pelanggan *"), g);
        g.gridx = 1; txtNama = new JTextField(25); p.add(txtNama, g);

        g.gridx = 2; p.add(new JLabel("Telepon"), g);
        g.gridx = 3; txtTelepon = new JTextField(15); p.add(txtTelepon, g);

        g.gridx = 0; g.gridy = 1; p.add(new JLabel("Alamat"), g);
        g.gridx = 1; g.gridwidth = 3;
        txtAlamat = new JTextArea(2, 40);
        txtAlamat.setLineWrap(true);
        txtAlamat.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        p.add(new JScrollPane(txtAlamat), g);
        g.gridwidth = 1;

        btnTambah = tombol("Tambah",    new Color(52, 152, 219));
        btnUpdate = tombol("Update",    new Color(39, 174, 96));
        btnHapus  = tombol("Hapus",     new Color(231, 76, 60));
        btnBersih = tombol("Bersihkan", new Color(127, 140, 141));
        btnUpdate.setEnabled(false); btnHapus.setEnabled(false);

        JPanel pb = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pb.add(btnTambah); pb.add(btnUpdate); pb.add(btnHapus); pb.add(btnBersih);
        g.gridx = 0; g.gridy = 2; g.gridwidth = 4; p.add(pb, g);

        btnTambah.addActionListener(e -> aksiTambah());
        btnUpdate.addActionListener(e -> aksiUpdate());
        btnHapus.addActionListener(e  -> aksiHapus());
        btnBersih.addActionListener(e -> bersihkan());
        return p;
    }

    private JPanel buatTabel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("👤  Daftar Pelanggan"));
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Nama Pelanggan", "Telepon", "Alamat"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0)
                isiForm(table.getSelectedRow());
        });
        p.add(new JScrollPane(table));
        return p;
    }

    private void aksiTambah() {
        String r = ctrl.tambah(txtNama.getText().trim(), txtTelepon.getText().trim(), txtAlamat.getText().trim());
        if ("SUCCESS".equals(r)) { info("Pelanggan berhasil ditambahkan!"); bersihkan(); muatData(); }
        else error(r);
    }

    private void aksiUpdate() {
        if (selectedId < 0) return;
        String r = ctrl.update(selectedId, txtNama.getText().trim(), txtTelepon.getText().trim(), txtAlamat.getText().trim());
        if ("SUCCESS".equals(r)) { info("Data berhasil diperbarui!"); bersihkan(); muatData(); }
        else error(r);
    }

    private void aksiHapus() {
        if (selectedId < 0) return;
        if (JOptionPane.showConfirmDialog(this, "Yakin hapus pelanggan ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String r = ctrl.hapus(selectedId);
            if ("SUCCESS".equals(r)) { info("Pelanggan dihapus."); bersihkan(); muatData(); }
            else error(r);
        }
    }

    private void isiForm(int row) {
        selectedId = (int) tableModel.getValueAt(row, 0);
        txtNama.setText((String) tableModel.getValueAt(row, 1));
        txtTelepon.setText((String) tableModel.getValueAt(row, 2));
        txtAlamat.setText((String) tableModel.getValueAt(row, 3));
        btnUpdate.setEnabled(true); btnHapus.setEnabled(true); btnTambah.setEnabled(false);
    }

    private void bersihkan() {
        txtNama.setText(""); txtTelepon.setText(""); txtAlamat.setText("");
        selectedId = -1; table.clearSelection();
        btnTambah.setEnabled(true); btnUpdate.setEnabled(false); btnHapus.setEnabled(false);
    }

    public void muatData() {
        tableModel.setRowCount(0);
        List<Pelanggan> list = ctrl.getAll();
        for (Pelanggan p : list)
            tableModel.addRow(new Object[]{p.getId(), p.getNama(), p.getTelepon(), p.getAlamat()});
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
