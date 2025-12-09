import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class untuk koneksi database MySQL
 */
public class DatabaseConnection {
    
    // Konfigurasi database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/laundry_system?" +
                                         "useSSL=false&" +
                                         "serverTimezone=Asia/Jakarta&" +
                                         "allowPublicKeyRetrieval=true&" +
                                         "useUnicode=true&" +
                                         "characterEncoding=UTF-8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    // Singleton instance
    private static DatabaseConnection instance;
    private Connection connection;
    
    // Private constructor untuk Singleton pattern
    private DatabaseConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Buat koneksi
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✓ Koneksi database berhasil!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("✗ MySQL JDBC Driver tidak ditemukan!");
            System.err.println("  Download dari: https://dev.mysql.com/downloads/connector/j/");
            System.err.println("  Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("✗ Gagal koneksi ke database!");
            System.err.println("  Pastikan MySQL sudah running di XAMPP");
            System.err.println("  Error: " + e.getMessage());
        }
    }
    
    /**
     * Get singleton instance
     */
    public static DatabaseConnection getInstance() {
        if (instance == null || !isConnectionValid()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Get connection object
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error reconnecting to database: " + e.getMessage());
        }
        return connection;
    }
    
    /**
     * Check apakah koneksi masih valid
     */
    private static boolean isConnectionValid() {
        try {
            return instance != null 
                && instance.connection != null 
                && !instance.connection.isClosed() 
                && instance.connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Close connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Koneksi database ditutup");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
    
    /**
     * Test koneksi database
     */
    public static boolean testConnection() {
        try {
            Connection conn = getInstance().getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Test connection failed: " + e.getMessage());
            return false;
        }
    }
}
