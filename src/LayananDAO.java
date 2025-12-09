import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) untuk tabel layanan
 */
public class LayananDAO {
    
    private Connection connection;
    
    public LayananDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Tambah layanan baru
     */
    public boolean insertLayanan(LayananItem layanan) {
        String sql = "INSERT INTO layanan (nama_layanan, harga_per_kg, estimasi_hari, is_active) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, layanan.getNama());
            stmt.setInt(2, layanan.getHargaPerKg());
            stmt.setInt(3, layanan.getEstimasiHari());
            stmt.setBoolean(4, true);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error insert layanan: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get layanan by nama
     */
    public LayananItem getLayananByNama(String nama) {
        String sql = "SELECT * FROM layanan WHERE nama_layanan = ? AND is_active = TRUE";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nama);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new LayananItem(
                    rs.getString("nama_layanan"),
                    rs.getInt("harga_per_kg"),
                    rs.getInt("estimasi_hari")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error get layanan: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get layanan by ID
     */
    public LayananItem getLayananById(int id) {
        String sql = "SELECT * FROM layanan WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new LayananItem(
                    rs.getString("nama_layanan"),
                    rs.getInt("harga_per_kg"),
                    rs.getInt("estimasi_hari")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error get layanan by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get semua layanan aktif
     */
    public List<LayananItem> getAllLayanan() {
        List<LayananItem> layananList = new ArrayList<>();
        String sql = "SELECT * FROM layanan WHERE is_active = TRUE ORDER BY nama_layanan";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                LayananItem layanan = new LayananItem(
                    rs.getString("nama_layanan"),
                    rs.getInt("harga_per_kg"),
                    rs.getInt("estimasi_hari")
                );
                layananList.add(layanan);
            }
        } catch (SQLException e) {
            System.err.println("Error get all layanan: " + e.getMessage());
        }
        return layananList;
    }
    
    /**
     * Update layanan
     */
    public boolean updateLayanan(String namaLama, String namaBaru, int hargaPerKg, int estimasiHari) {
        String sql = "UPDATE layanan SET nama_layanan = ?, harga_per_kg = ?, estimasi_hari = ? WHERE nama_layanan = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, namaBaru);
            stmt.setInt(2, hargaPerKg);
            stmt.setInt(3, estimasiHari);
            stmt.setString(4, namaLama);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error update layanan: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete layanan (soft delete)
     */
    public boolean deleteLayanan(String nama) {
        String sql = "UPDATE layanan SET is_active = FALSE WHERE nama_layanan = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nama);
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error delete layanan: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get ID layanan by nama
     */
    public int getLayananId(String nama) {
        String sql = "SELECT id FROM layanan WHERE nama_layanan = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nama);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error get layanan ID: " + e.getMessage());
        }
        return -1;
    }
}
