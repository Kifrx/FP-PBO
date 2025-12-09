import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) untuk tabel access_requests
 */
public class AccessRequestDAO {
    
    private Connection connection;
    
    public AccessRequestDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Tambah access request baru
     */
    public boolean insertAccessRequest(AccessRequest request) {
        String sql = "INSERT INTO access_requests (username, password, nama_lengkap, alasan, tanggal_request, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, request.getUsername());
            stmt.setString(2, request.getPassword());
            stmt.setString(3, request.getNamaLengkap());
            stmt.setString(4, request.getAlasan());
            stmt.setTimestamp(5, Timestamp.valueOf(request.getTanggalRequest()));
            stmt.setString(6, request.getStatus());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error insert access request: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get access request by ID
     */
    public AccessRequest getAccessRequestById(int id) {
        String sql = "SELECT * FROM access_requests WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAccessRequest(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error get access request: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get semua access request
     */
    public List<AccessRequest> getAllAccessRequests() {
        List<AccessRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM access_requests ORDER BY tanggal_request DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                AccessRequest request = mapResultSetToAccessRequest(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            System.err.println("Error get all access requests: " + e.getMessage());
        }
        return requests;
    }
    
    /**
     * Get access request by status
     */
    public List<AccessRequest> getAccessRequestsByStatus(String status) {
        List<AccessRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM access_requests WHERE status = ? ORDER BY tanggal_request DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                AccessRequest request = mapResultSetToAccessRequest(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            System.err.println("Error get access requests by status: " + e.getMessage());
        }
        return requests;
    }
    
    /**
     * Update status access request (untuk approve/decline)
     */
    public boolean updateAccessRequestStatus(int id, String newStatus, int approvedBy) {
        String sql = "UPDATE access_requests SET status = ?, approved_by = ?, approved_at = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, approvedBy);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(4, id);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error update access request status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check apakah username sudah pernah request dan masih pending
     */
    public boolean isPendingRequestExists(String username) {
        String sql = "SELECT COUNT(*) as count FROM access_requests WHERE username = ? AND status = 'PENDING'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error check pending request: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get jumlah pending request
     */
    public int getPendingRequestCount() {
        String sql = "SELECT COUNT(*) as count FROM access_requests WHERE status = 'PENDING'";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error get pending count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Helper method: Map ResultSet to AccessRequest object
     */
    private AccessRequest mapResultSetToAccessRequest(ResultSet rs) throws SQLException {
        AccessRequest request = new AccessRequest(
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("nama_lengkap"),
            rs.getString("alasan")
        );
        
        // Override dengan data dari database
        request.setId(rs.getInt("id"));
        request.setStatus(rs.getString("status"));
        request.setTanggalRequest(rs.getTimestamp("tanggal_request").toLocalDateTime());
        
        return request;
    }
}
