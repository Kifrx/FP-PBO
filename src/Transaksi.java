import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaksi {
    private static int counterID = 1;
    
    private int id;
    private String namaPelanggan;
    private String noHP;
    private String layanan;
    private int berat;
    private int totalHarga;
    private String status; // "BELUM", "PROSES", "DONE"
    private LocalDateTime tanggalMasuk;
    private LocalDate estimasiSelesai;
    private String namaPetugas;

    public Transaksi(String namaPelanggan, String noHP, String layanan, int berat, int totalHarga) {
        this.id = counterID++;
        this.namaPelanggan = namaPelanggan;
        this.noHP = noHP;
        this.layanan = layanan;
        this.berat = berat;
        this.totalHarga = totalHarga;
        this.status = "BELUM";
        this.tanggalMasuk = LocalDateTime.now();
        // Estimasi selesai akan di-set dari database menggunakan setter
        this.estimasiSelesai = LocalDate.now(); // Default value
        this.namaPetugas = ""; // Will be set from database or manually
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getNoHP() {
        return noHP;
    }

    public void setNoHP(String noHP) {
        this.noHP = noHP;
    }

    public String getLayanan() {
        return layanan;
    }

    public void setLayanan(String layanan) {
        this.layanan = layanan;
        // Estimasi akan di-update berdasarkan data layanan dari database
    }

    public int getBerat() {
        return berat;
    }

    public void setBerat(int berat) {
        this.berat = berat;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(LocalDateTime tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }

    public String getTanggalMasukFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return tanggalMasuk.format(formatter);
    }

    public LocalDate getEstimasiSelesai() {
        return estimasiSelesai;
    }

    public void setEstimasiSelesai(LocalDate estimasiSelesai) {
        this.estimasiSelesai = estimasiSelesai;
    }

    public String getEstimasiSelesaiFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return estimasiSelesai.format(formatter);
    }

    public String getNamaPetugas() {
        return namaPetugas;
    }

    public void setNamaPetugas(String namaPetugas) {
        this.namaPetugas = namaPetugas;
    }
    
    public boolean isTerlambat() {
        return LocalDate.now().isAfter(estimasiSelesai) && !"DONE".equals(status);
    }
}
