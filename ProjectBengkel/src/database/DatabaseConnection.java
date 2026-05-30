package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static String URL      = "jdbc:mysql://localhost:3306/db_bengkel";
    private static String USER     = "bengkel_user";
    private static String PASSWORD = "";

    private static Connection koneksi = null;

    // Hanya buat koneksi baru kalau belum ada / sudah tertutup
    public static Connection getConnection() throws SQLException {
        if (koneksi == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                koneksi = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Koneksi berhasil");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL tidak ditemukan!", e);
            }
        }
        return koneksi;
    }
}
