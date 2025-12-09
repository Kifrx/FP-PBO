import java.util.List;

public class UserSession {
    
    private static User currentUser = null;
    private static UserDAO userDAO = new UserDAO();

    public static boolean cekLogin(String username, String password) {
        User user = userDAO.authenticateUser(username, password);
        if (user != null) {
            currentUser = user;
            return true;
        }
        return false;
    }
   
    public static void daftarBaru(String username, String password, String role, String namaLengkap) {
        User newUser = new User(username, password, role, namaLengkap);
        userDAO.insertUser(newUser);
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static String getUsername() {
        return currentUser != null ? currentUser.getUsername() : "";
    }
    
    public static String getRole() {
        return currentUser != null ? currentUser.getRole() : "";
    }
    
    public static String getNamaLengkap() {
        return currentUser != null ? currentUser.getNamaLengkap() : "";
    }
    
    public static boolean isOwner() {
        return currentUser != null && currentUser.isOwner();
    }
    
    public static boolean isKaryawan() {
        return currentUser != null && currentUser.isKaryawan();
    }
    
    public static List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    public static void updateUser(String username, String newPassword, String newRole, String newNamaLengkap) {
        userDAO.updateUser(username, newPassword, newRole, newNamaLengkap);
    }
    
    public static void deleteUser(String username) {
        userDAO.deleteUser(username);
    }
    
    public static void logout() {
        currentUser = null;
    }
}
