package view;

import controller.ServisController;
import model.DetailServis;
import model.Servis;
import util.FormatUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * RiwayatPanel - View Modul Riwayat Servis
 * Klik baris servis → tampilkan detail sparepart yang digunakan
 */
public class RiwayatPanel extends JPanel {

    private final ServisController ctrl = new ServisController();

    private JTable            tblRiwayat, tblDetail;
    private DefaultTableModel modelRiwayat, modelDetail;
    private JLabel            lblInfoServis;

    public RiwayatPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel atas: tabel riwayat
        JPanel atas = new JPanel(new BorderLayout());
        atas.setBorder(BorderFactory.createTitledBorder("📋  Riwayat Servis (Klik baris untuk lihat detail)"));
        modelRiwayat = new DefaultTableModel(
            new String[]{"ID", "Tanggal", "Pelanggan", "Plat", "Merk/Tipe", "Jenis Servis", "Biaya Jasa", "Total Biaya"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblRiwayat = new JTable(modelRiwayat);
        tblRiwayat.setRowHeight(24);
        tblRiwayat.getColumnModel().getColumn(0).setMaxWidth(45);
        tblRiwayat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblRiwayat.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblRiwayat.getSelectedRow() >= 0)
                muatDetail(tblRiwayat.getSelectedRow());
        });
        atas.add(new JScrollPane(tblRiwayat));

        // Panel bawah: detail sparepart per servis
        JPanel bawah = new JPanel(new BorderLayout(4, 4));
        bawah.setBorder(BorderFactory.createTitledBorder("🔩  Detail Sparepart Servis Terpilih"));
        lblInfoServis = new JLabel("— Pilih baris servis di atas —");
        lblInfoServis.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        modelDetail = new DefaultTableModel(
            new String[]{"No.", "Nama Sparepart", "Jumlah", "Harga Satuan", "Subtotal"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblDetail = new JTable(modelDetail);
        tblDetail.setRowHeight(24);
        tblDetail.getColumnModel().getColumn(0).setMaxWidth(45);
        bawah.add(lblInfoServis,           BorderLayout.NORTH);
        bawah.add(new JScrollPane(tblDetail), BorderLayout.CENTER);

        // Tombol refresh
        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.addActionListener(e -> muatData());
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtn.add(btnRefresh);

        // Split: riwayat 60% | detail 40%
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, atas, bawah);
        split.setResizeWeight(0.6);

        add(panelBtn, BorderLayout.NORTH);
        add(split,    BorderLayout.CENTER);

        muatData();
    }

    public void muatData() {
        modelRiwayat.setRowCount(0);
        modelDetail.setRowCount(0);
        lblInfoServis.setText("— Pilih baris servis di atas —");

        List<Servis> list = ctrl.getRiwayat();
        for (Servis s : list) {
            modelRiwayat.addRow(new Object[]{
                s.getId(),
                s.getTanggalServis(),
                s.getNamaPelanggan(),
                s.getPlatKendaraan(),
                s.getMerkKendaraan() + " " + s.getTipeKendaraan(),
                s.getJenisServis(),
                FormatUtil.formatRupiah(s.getBiayaJasa()),
                FormatUtil.formatRupiah(s.getTotalBiaya())
            });
        }
    }

    private void muatDetail(int row) {
        modelDetail.setRowCount(0);
        int idServis = (int) modelRiwayat.getValueAt(row, 0);
        String info  = modelRiwayat.getValueAt(row, 1) + " | "
                     + modelRiwayat.getValueAt(row, 2) + " | "
                     + modelRiwayat.getValueAt(row, 5);
        lblInfoServis.setText("Detail untuk: " + info);

        List<DetailServis> details = ctrl.getDetail(idServis);
        int no = 1;
        for (DetailServis d : details) {
            modelDetail.addRow(new Object[]{
                no++, d.getNamaSparepart(), d.getJumlah(),
                FormatUtil.formatRupiah(d.getHargaSatuan()),
                FormatUtil.formatRupiah(d.getSubtotal())
            });
        }
        if (details.isEmpty()) {
            modelDetail.addRow(new Object[]{"—", "Tidak ada sparepart", "", "", ""});
        }
    }
}
