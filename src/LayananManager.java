import java.util.List;

public class LayananManager {
    private static LayananDAO layananDAO = new LayananDAO();

    public static List<LayananItem> getAllLayanan() {
        return layananDAO.getAllLayanan();
    }

    public static LayananItem getLayananByNama(String nama) {
        return layananDAO.getLayananByNama(nama);
    }

    public static void addLayanan(String nama, int hargaPerKg, int estimasiHari) {
        LayananItem item = new LayananItem(nama, hargaPerKg, estimasiHari);
        layananDAO.insertLayanan(item);
    }

    public static void updateLayanan(String namaLama, String namaBaru, int hargaPerKg, int estimasiHari) {
        layananDAO.updateLayanan(namaLama, namaBaru, hargaPerKg, estimasiHari);
    }

    public static void deleteLayanan(String nama) {
        layananDAO.deleteLayanan(nama);
    }

    public static int hitungHarga(String namaLayanan, int berat) {
        LayananItem item = getLayananByNama(namaLayanan);
        if (item != null) {
            return item.getHargaPerKg() * berat;
        }
        return 0;
    }
}
