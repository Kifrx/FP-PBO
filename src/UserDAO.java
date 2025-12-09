import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) untuk tabel users
 */
public class UserDAO {
    
    private Connection connection;
    
    public UserDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Tambah user baru ke database
     */
    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (username, password, role, nama_lengkap) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getNamaLengkap());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error insert user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("nama_lengkap")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error get user: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get user by username and password (untuk login)
     */
    public User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("nama_lengkap")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error authenticate user: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get semua user
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("nama_lengkap")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error get all users: " + e.getMessage());
        }
        return users;
    }
    
    /**
     * Update user
     */
    public boolean updateUser(String username, String newPassword, String newRole, String newNamaLengkap) {
        StringBuilder sql = new StringBuilder("UPDATE users SET ");
        List<String> updates = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        
        if (newPassword != null && !newPassword.isEmpty()) {
            updates.add("password = ?");
            params.add(newPassword);
        }
        if (newRole != null && !newRole.isEmpty()) {
            updates.add("role = ?");
            params.add(newRole);
        }
        if (newNamaLengkap != null && !newNamaLengkap.isEmpty()) {
            updates.add("nama_lengkap = ?");
            params.add(newNamaLengkap);
        }
        
        if (updates.isEmpty()) return false;
        
        sql.append(String.join(", ", updates));
        sql.append(" WHERE username = ?");
        params.add(username);
        
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error update user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete user by username
     */
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error delete user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get user ID by username
     */
    public int getUserId(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error get user ID: " + e.getMessage());
        }
        return -1;
    }
}
