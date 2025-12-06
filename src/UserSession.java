public class UserSession {
    
    private static String username = "admin";
    private static String password = "123";


    public static boolean cekLogin(String user, String pass) {
        return user.equals(username) && pass.equals(password);
    }
   
    public static void daftarBaru(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public static String getUsername() {
        return username;
    }
}
