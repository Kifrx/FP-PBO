import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) untuk tabel transaksi
 */
public class TransaksiDAO {
    
    private Connection connection;
    private LayananDAO layananDAO;
    private UserDAO userDAO;
    
    public TransaksiDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.layananDAO = new LayananDAO();
        this.userDAO = new UserDAO();
    }
    
    /**
     * Tambah transaksi baru
     */
    public boolean insertTransaksi(Transaksi transaksi, String username) {
        String sql = "INSERT INTO transaksi (nama_pelanggan, no_hp, layanan_id, nama_layanan, berat, " +
                     "total_harga, status, tanggal_masuk, estimasi_selesai, user_id, nama_petugas) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int layananId = layananDAO.getLayananId(transaksi.getLayanan());
            int userId = userDAO.getUserId(username);
            String namaPetugas = userDAO.getUserByUsername(username).getNamaLengkap();
            
            if (layananId == -1 || userId == -1) {
                System.err.println("Error: Layanan atau User tidak ditemukan");
                return false;
            }
            
            stmt.setString(1, transaksi.getNamaPelanggan());
            stmt.setString(2, transaksi.getNoHP());
            stmt.setInt(3, layananId);
            stmt.setString(4, transaksi.getLayanan());
            stmt.setInt(5, transaksi.getBerat());
            stmt.setInt(6, transaksi.getTotalHarga());
            stmt.setString(7, transaksi.getStatus());
            stmt.setTimestamp(8, Timestamp.valueOf(transaksi.getTanggalMasuk()));
            stmt.setDate(9, Date.valueOf(transaksi.getEstimasiSelesai()));
            stmt.setInt(10, userId);
            stmt.setString(11, namaPetugas);
            
            int rows = stmt.executeUpdate();
            
            // Get generated ID
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    // ID sudah di-generate oleh database
                    return true;
                }
            }
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error insert transaksi: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get transaksi by ID
     */
    public Transaksi getTransaksiById(int id) {
        String sql = "SELECT * FROM transaksi WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTransaksi(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error get transaksi: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get semua transaksi
     */
    public List<Transaksi> getAllTransaksi() {
        List<Transaksi> transaksiList = new ArrayList<>();
        String sql = "SELECT * FROM transaksi ORDER BY tanggal_masuk DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Transaksi transaksi = mapResultSetToTransaksi(rs);
                transaksiList.add(transaksi);
            }
        } catch (SQLException e) {
            System.err.println("Error get all transaksi: " + e.getMessage());
        }
        return transaksiList;
    }
    
    /**
     * Get transaksi by status
     */
    public List<Transaksi> getTransaksiByStatus(String status) {
        List<Transaksi> transaksiList = new ArrayList<>();
        String sql = "SELECT * FROM transaksi WHERE status = ? ORDER BY tanggal_masuk DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaksi transaksi = mapResultSetToTransaksi(rs);
                transaksiList.add(transaksi);
            }
        } catch (SQLException e) {
            System.err.println("Error get transaksi by status: " + e.getMessage());
        }
        return transaksiList;
    }
    
    /**
     * Get transaksi by tanggal
     */
    public List<Transaksi> getTransaksiByDate(LocalDate tanggal) {
        List<Transaksi> transaksiList = new ArrayList<>();
        String sql = "SELECT * FROM transaksi WHERE DATE(tanggal_masuk) = ? ORDER BY tanggal_masuk DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(tanggal));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaksi transaksi = mapResultSetToTransaksi(rs);
                transaksiList.add(transaksi);
            }
        } catch (SQLException e) {
            System.err.println("Error get transaksi by date: " + e.getMessage());
        }
        return transaksiList;
    }
    
    /**
     * Get transaksi by bulan dan tahun
     */
    public List<Transaksi> getTransaksiByMonth(int month, int year) {
        List<Transaksi> transaksiList = new ArrayList<>();
        String sql = "SELECT * FROM transaksi WHERE MONTH(tanggal_masuk) = ? AND YEAR(tanggal_masuk) = ? ORDER BY tanggal_masuk DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaksi transaksi = mapResultSetToTransaksi(rs);
                transaksiList.add(transaksi);
            }
        } catch (SQLException e) {
            System.err.println("Error get transaksi by month: " + e.getMessage());
        }
        return transaksiList;
    }
    
    /**
     * Update transaksi
     */
    public boolean updateTransaksi(Transaksi transaksi, String username) {
        String sql = "UPDATE transaksi SET nama_pelanggan = ?, no_hp = ?, nama_layanan = ?, " +
                     "berat = ?, total_harga = ?, status = ?, estimasi_selesai = ?, user_id = ?, nama_petugas = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int userId = userDAO.getUserId(username);
            String namaPetugas = userDAO.getUserByUsername(username).getNamaLengkap();
            
            stmt.setString(1, transaksi.getNamaPelanggan());
            stmt.setString(2, transaksi.getNoHP());
            stmt.setString(3, transaksi.getLayanan());
            stmt.setInt(4, transaksi.getBerat());
            stmt.setInt(5, transaksi.getTotalHarga());
            stmt.setString(6, transaksi.getStatus());
            stmt.setDate(7, Date.valueOf(transaksi.getEstimasiSelesai()));
            stmt.setInt(8, userId);
            stmt.setString(9, namaPetugas);
            stmt.setInt(10, transaksi.getId());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error update transaksi: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete transaksi
     */
    public boolean deleteTransaksi(int id) {
        String sql = "DELETE FROM transaksi WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error delete transaksi: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get total pendapatan
     */
    public int getTotalPendapatan() {
        String sql = "SELECT SUM(total_harga) as total FROM transaksi";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error get total pendapatan: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Helper method: Map ResultSet to Transaksi object
     */
    private Transaksi mapResultSetToTransaksi(ResultSet rs) throws SQLException {
        Transaksi transaksi = new Transaksi(
            rs.getString("nama_pelanggan"),
            rs.getString("no_hp"),
            rs.getString("nama_layanan"),
            rs.getInt("berat"),
            rs.getInt("total_harga")
        );
        
        // Override dengan data dari database
        transaksi.setId(rs.getInt("id"));
        transaksi.setStatus(rs.getString("status"));
        transaksi.setTanggalMasuk(rs.getTimestamp("tanggal_masuk").toLocalDateTime());
        transaksi.setEstimasiSelesai(rs.getDate("estimasi_selesai").toLocalDate());
        transaksi.setNamaPetugas(rs.getString("nama_petugas"));
        
        return transaksi;
    }
}
