package main;

import view.MainFrame;

import javax.swing.*;

/**
 * Main - Entry Point Aplikasi Bengkel
 *
 * Cara menjalankan:
 *   javac -cp lib/mysql-connector-j-*.jar -d bin src/**\/*.java
 *   java  -cp bin:lib/mysql-connector-j-*.jar Main
 *
 * (Windows pakai ; sebagai separator, bukan :)
 */
public class Main {
    public static void main(String[] args) {
        // Jalankan di Event Dispatch Thread (EDT) - best practice Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Gunakan tampilan native OS agar terlihat lebih bersih
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Tidak masalah jika gagal, gunakan default Swing
            }
            new MainFrame();
        });
    }
}
