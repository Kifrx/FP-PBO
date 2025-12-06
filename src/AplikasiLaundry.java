import javax.swing.*;

public class AplikasiLaundry {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginController controller = new LoginController();
            controller.showLoginView();
        });
    }
}
