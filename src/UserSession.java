public class UserSession {
    
    private static String username = "admin";
    private static String password = "123";


    public static boolean cekLogin(String user, String pass) {
        return user.equals(username) && pass.equals(password);
    }
   
    public static void daftarBaru(String u, String p) {
        username = u;
        password = p;
    }
    public static String getUsername() {
        return username;
    }
}
