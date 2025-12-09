public class Layanan {
    private String nama;
    private int hargaPerKg;
    private int estimasiHari;

    public Layanan(String nama, int hargaPerKg, int estimasiHari) {
        this.nama = nama;
        this.hargaPerKg = hargaPerKg;
        this.estimasiHari = estimasiHari;
    }
    
    public int hitungHarga(int berat) {
        return hargaPerKg * berat;
    }
    
    public String getNama() {
        return nama;
    }
    
    public int getHargaPerKg() {
        return hargaPerKg;
    }
    
    public int getEstimasiHari() {
        return estimasiHari;
    }
    
    public String getInfo() {
        return "Layanan: " + nama + " - Rp" + hargaPerKg + "/kg (" + estimasiHari + " hari)";
    }
}
