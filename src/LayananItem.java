public class LayananItem {
    private String nama;
    private int hargaPerKg;
    private int estimasiHari;

    public LayananItem(String nama, int hargaPerKg, int estimasiHari) {
        this.nama = nama;
        this.hargaPerKg = hargaPerKg;
        this.estimasiHari = estimasiHari;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHargaPerKg() {
        return hargaPerKg;
    }

    public void setHargaPerKg(int hargaPerKg) {
        this.hargaPerKg = hargaPerKg;
    }

    public int getEstimasiHari() {
        return estimasiHari;
    }

    public void setEstimasiHari(int estimasiHari) {
        this.estimasiHari = estimasiHari;
    }

    @Override
    public String toString() {
        return nama;
    }
}
