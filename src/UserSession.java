public class UserSession {
   
    private static String username = "admin";
    private static String password = "123";

    //Cek Login
    public static boolean cekLogin(String user, String pass) {
        if (user.equals(username) && pass.equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    public static void registrasiBaru(String userBaru, String passBaru) {
        username = userBaru;
        password = passBaru;
    }
    
    public static String getUsername() {
        return username;
    }
}
