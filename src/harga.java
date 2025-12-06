public class LaundryLogic {
    
    public int hitungHarga(String layanan, int berat) {
        int hargaPerKg = 0;

        if (layanan.equals("Cuci + Setrika")) {
            hargaPerKg = 6000;
        } else if (layanan.equals("Setrika Saja")) {
            hargaPerKg = 4000;
        } else if (layanan.equals("Cuci Kering")) {
            hargaPerKg = 3000;
        }

        return berat * hargaPerKg;
    }
}
