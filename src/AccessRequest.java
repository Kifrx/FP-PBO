import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccessRequest {
    private static int counterID = 1;
    
    private int id;
    private String username;
    private String password;
    private String namaLengkap;
    private String alasan;
    private LocalDateTime tanggalRequest;
    private String status; // "PENDING", "APPROVED", "DECLINED"

    public AccessRequest(String username, String password, String namaLengkap, String alasan) {
        this.id = counterID++;
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.alasan = alasan;
        this.tanggalRequest = LocalDateTime.now();
        this.status = "PENDING";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getAlasan() {
        return alasan;
    }

    public LocalDateTime getTanggalRequest() {
        return tanggalRequest;
    }

    public void setTanggalRequest(LocalDateTime tanggalRequest) {
        this.tanggalRequest = tanggalRequest;
    }

    public String getTanggalRequestFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return tanggalRequest.format(formatter);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
