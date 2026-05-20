package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection - Singleton pattern untuk koneksi ke MySQL
 * DB: db_bengkel | User: bengkel_user | Pass: bengkel123
 */
public class DatabaseConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/db_bengkel?useSSL=false&serverTimezone=Asia/Jakarta";
    private static final String USER     = "bengkel_user";
    private static final String PASSWORD = "bengkel123";

    private static Connection connection = null;

    // Singleton: hanya buat koneksi baru kalau belum ada / sudah tertutup
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); // load driver MySQL
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Koneksi berhasil ke db_bengkel");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL tidak ditemukan! Pastikan mysql-connector.jar sudah ada di classpath.", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("[DB] Koneksi ditutup.");
            } catch (SQLException e) {
                System.err.println("Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }
}
