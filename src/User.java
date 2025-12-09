public class User {
    private String username;
    private String password;
    private String role; // "OWNER" atau "KARYAWAN"
    private String namaLengkap;

    public User(String username, String password, String role, String namaLengkap) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.namaLengkap = namaLengkap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public boolean isOwner() {
        return "OWNER".equals(role);
    }

    public boolean isKaryawan() {
        return "KARYAWAN".equals(role);
    }
}
