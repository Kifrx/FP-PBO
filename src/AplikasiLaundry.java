import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AplikasiLaundry extends JFrame {

    private JPanel mainContainer;
    private CardLayout cardLayout;

    private DashboardPanel dashboardPanel;
    private LoginPanel loginPanel;
    private RequestAccessPanel requestAccessPanel;
    private ManageUserPanel manageUserPanel;
    private EditLayananPanel editLayananPanel;
    private LaporanPanel laporanPanel;

    private TransaksiDAO transaksiDAO = new TransaksiDAO();
    private AccessRequestDAO accessRequestDAO = new AccessRequestDAO();
    private UserDAO userDAO = new UserDAO();

    private final String PAGE_LOGIN = "LOGIN";
    private final String PAGE_REQUEST = "REQUEST";
    private final String PAGE_MENU = "MENU";
    private final String PAGE_DASHBOARD = "DASHBOARD";
    private final String PAGE_LAPORAN = "LAPORAN";
    private final String PAGE_MANAGE_USER = "MANAGE_USER";
    private final String PAGE_EDIT_LAYANAN = "EDIT_LAYANAN";

    public static final Color COLOR_PRIMARY = new Color(74, 109, 140);
    public static final Color COLOR_BG_LIGHT = new Color(225, 235, 240);
    public static final Color COLOR_INPUT = new Color(238, 242, 245);
    public static final Color COLOR_TEXT = new Color(60, 60, 60);

    public static final Color COLOR_RED = new Color(220, 53, 69);
    public static final Color COLOR_YELLOW = new Color(255, 193, 7);
    public static final Color COLOR_GREEN = new Color(40, 167, 69);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new AplikasiLaundry());
    }

    public AplikasiLaundry() {
        setTitle("Sistem Laundry");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        dashboardPanel = new DashboardPanel();
        loginPanel = new LoginPanel();
        requestAccessPanel = new RequestAccessPanel();
        manageUserPanel = new ManageUserPanel();
        editLayananPanel = new EditLayananPanel();
        laporanPanel = new LaporanPanel();

        mainContainer.add(loginPanel, PAGE_LOGIN);
        mainContainer.add(requestAccessPanel, PAGE_REQUEST);
        mainContainer.add(new MenuOperasiPanel(), PAGE_MENU);
        mainContainer.add(dashboardPanel, PAGE_DASHBOARD);
        mainContainer.add(laporanPanel, PAGE_LAPORAN);
        mainContainer.add(manageUserPanel, PAGE_MANAGE_USER);
        mainContainer.add(editLayananPanel, PAGE_EDIT_LAYANAN);

        add(mainContainer);
        setVisible(true);
    }

    private void switchPage(String pageName) {
        if (pageName.equals(PAGE_DASHBOARD))
            dashboardPanel.refreshData();
        if (pageName.equals(PAGE_MANAGE_USER))
            manageUserPanel.refreshData();
        if (pageName.equals(PAGE_EDIT_LAYANAN))
            editLayananPanel.refreshData();
        if (pageName.equals(PAGE_LAPORAN))
            laporanPanel.refreshData();

        cardLayout.show(mainContainer, pageName);
    }

    private void openDashboard(String mode) {
        dashboardPanel.setMode(mode);
        switchPage(PAGE_DASHBOARD);
    }

    // --- PANEL CLASSES ---

    class LoginPanel extends BackgroundPanel {
        private JTextField txtUser;
        private JPasswordField txtPass;

        public LoginPanel() {
            super("image/bg_login.png");
            setLayout(null);

            int centerX = 950 / 2 - 150;
            int startY = 180;

            JLabel lblTitle = new JLabel("NamaLaundry");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 42));
            lblTitle.setForeground(COLOR_PRIMARY);
            lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
            lblTitle.setBounds(0, 80, 950, 60);
            add(lblTitle);

            add(createLabel("Username", centerX, startY));
            txtUser = createRoundedField();
            txtUser.setBounds(centerX, startY + 25, 300, 40);
            add(txtUser);

            add(createLabel("Password", centerX, startY + 80));
            txtPass = new JPasswordField();
            styleField(txtPass);
            txtPass.setBounds(centerX, startY + 105, 300, 40);
            add(txtPass);

            JLabel lReq = new JLabel("User baru? Request Access");
            lReq.setBounds(centerX, startY + 160, 200, 20);
            lReq.setForeground(Color.GRAY);
            lReq.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lReq.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lReq.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    switchPage(PAGE_REQUEST);
                }
            });
            add(lReq);

            RoundedButton btnLogin = new RoundedButton("Login");
            btnLogin.setBounds(centerX + 180, startY + 155, 120, 35);
            btnLogin.setColor(COLOR_PRIMARY);
            btnLogin.setForeground(Color.WHITE);
            add(btnLogin);

            btnLogin.addActionListener(e -> {
                if (UserSession.cekLogin(txtUser.getText(), new String(txtPass.getPassword()))) {
                    txtUser.setText("");
                    txtPass.setText("");
                    switchPage(PAGE_MENU);
                } else {
                    JOptionPane.showMessageDialog(null, "Username atau Password salah!", "Login Gagal",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    class RequestAccessPanel extends BackgroundPanel {
        public RequestAccessPanel() {
            super("image/bg_login.png");
            setLayout(null);

            JLabel lblTitle = new JLabel("REQUEST ACCESS");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblTitle.setForeground(COLOR_PRIMARY);
            lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
            lblTitle.setBounds(0, 60, 950, 40);
            add(lblTitle);

            int centerX = 950 / 2 - 175;
            int startY = 120;

            add(createLabel("Username:", centerX, startY));
            JTextField txtUser = createRoundedField();
            txtUser.setBounds(centerX + 120, startY - 5, 230, 35);
            add(txtUser);

            add(createLabel("Password:", centerX, startY + 45));
            JPasswordField txtPass = new JPasswordField();
            styleField(txtPass);
            txtPass.setBounds(centerX + 120, startY + 40, 230, 35);
            add(txtPass);

            add(createLabel("Nama Lengkap:", centerX, startY + 90));
            JTextField txtNama = createRoundedField();
            txtNama.setBounds(centerX + 120, startY + 85, 230, 35);
            add(txtNama);

            add(createLabel("Alasan:", centerX, startY + 135));
            JTextArea txtAlasan = new JTextArea();
            txtAlasan.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(15, new Color(200, 200, 200)), new EmptyBorder(5, 5, 5, 5)));
            txtAlasan.setBackground(COLOR_INPUT);
            txtAlasan.setLineWrap(true);
            JScrollPane scroll = new JScrollPane(txtAlasan);
            scroll.setBounds(centerX + 120, startY + 135, 230, 80);
            add(scroll);

            RoundedButton btnKirim = new RoundedButton("Kirim Request");
            btnKirim.setColor(COLOR_PRIMARY);
            btnKirim.setForeground(Color.WHITE);
            btnKirim.setBounds(centerX + 120, startY + 230, 230, 40);
            add(btnKirim);

            RoundedButton btnBatal = new RoundedButton("Batal");
            btnBatal.setColor(COLOR_RED);
            btnBatal.setForeground(Color.WHITE);
            btnBatal.setBounds(centerX + 120, startY + 280, 230, 35);
            add(btnBatal);

            btnKirim.addActionListener(e -> {
                if (txtUser.getText().isEmpty() || new String(txtPass.getPassword()).isEmpty()
                        || txtNama.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Lengkapi data!");
                    return;
                }
                if (userDAO.getUserByUsername(txtUser.getText()) != null) {
                    JOptionPane.showMessageDialog(null, "Username sudah terpakai!");
                    return;
                }
                if (accessRequestDAO.isPendingRequestExists(txtUser.getText())) {
                    JOptionPane.showMessageDialog(null, "Request username ini masih pending!");
                    return;
                }

                AccessRequest req = new AccessRequest(txtUser.getText(), new String(txtPass.getPassword()),
                        txtNama.getText(), txtAlasan.getText());
                if (accessRequestDAO.insertAccessRequest(req)) {
                    JOptionPane.showMessageDialog(null, "Request terkirim! Tunggu approval admin.");
                    switchPage(PAGE_LOGIN);
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal mengirim request.");
                }
            });

            btnBatal.addActionListener(e -> switchPage(PAGE_LOGIN));
        }
    }

    class MenuOperasiPanel extends BackgroundPanel {
        public MenuOperasiPanel() {
            super("image/bg_menu.png");
            setLayout(null);

            JLabel lblTitle = new JLabel("Pilih Operasi");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
            lblTitle.setForeground(COLOR_PRIMARY);
            lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
            lblTitle.setBounds(0, 80, 950, 50);
            add(lblTitle);

            JPanel gridPanel = new JPanel(new GridLayout(2, 3, 20, 20));
            gridPanel.setBounds(150, 180, 650, 280);
            gridPanel.setOpaque(false);

            gridPanel.add(createMenuButton("Input Transaksi Baru", e -> openDashboard("INPUT")));
            gridPanel.add(createMenuButton("Update Transaksi", e -> openDashboard("UPDATE")));
            gridPanel.add(createMenuButton("Hapus Transaksi", e -> openDashboard("HAPUS")));

            gridPanel.add(createMenuButton("Laporan", e -> checkOwnerAndSwitch(PAGE_LAPORAN)));
            gridPanel.add(createMenuButton("Kelola User", e -> checkOwnerAndSwitch(PAGE_MANAGE_USER)));
            gridPanel.add(createMenuButton("Edit Layanan", e -> checkOwnerAndSwitch(PAGE_EDIT_LAYANAN)));

            add(gridPanel);

            RoundedButton btnLogout = new RoundedButton("Logout");
            btnLogout.setBounds(30, 30, 100, 35);
            btnLogout.setColor(COLOR_PRIMARY);
            btnLogout.setForeground(Color.WHITE);
            add(btnLogout);

            btnLogout.addActionListener(e -> {
                UserSession.logout();
                switchPage(PAGE_LOGIN);
            });
        }

        private void checkOwnerAndSwitch(String page) {
            if (UserSession.isOwner()) {
                switchPage(page);
            } else {
                JOptionPane.showMessageDialog(null, "Akses ditolak! Menu ini hanya untuk Owner.");
            }
        }

        private JButton createMenuButton(String text, ActionListener action) {
            JButton btn = new JButton("<html><center>" + text + "</center></html>");
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setBackground(new Color(209, 232, 240));
            btn.setFocusPainted(false);
            btn.setBorder(new RoundedBorder(15, new Color(180, 200, 210)));
            btn.addActionListener(action);
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(new Color(190, 220, 235));
                }

                public void mouseExited(MouseEvent e) {
                    btn.setBackground(new Color(209, 232, 240));
                }
            });
            return btn;
        }
    }

    class DashboardPanel extends BackgroundPanel {

        private JTextField textNama, textNoHP, textBerat, txtTotal;
        private JComboBox<LayananItem> comboLayanan;
        private RoundedButton btnStatusBelum, btnStatusProses, btnStatusDone;
        private RoundedButton btnSimpan, btnUpdate, btnHapus, btnHitung;
        private JTable table;
        private DefaultTableModel modelTabel;
        private String selectedStatus = "BELUM";
        private JLabel labelEstimasi;
        private String currentMode = ""; // Simpan mode saat ini

        public DashboardPanel() {
            super("image/bg_dashboard.png");
            setLayout(null);

            RoundedButton btnBack = new RoundedButton("Kembali");
            btnBack.setBounds(820, 20, 100, 35);
            btnBack.setColor(COLOR_PRIMARY);
            btnBack.setForeground(Color.WHITE);
            add(btnBack);
            btnBack.addActionListener(e -> switchPage(PAGE_MENU));

            // Tabel
            String[] col = { "ID", "Nama", "HP", "Layanan", "Berat", "Status", "Total", "Estimasi" };
            modelTabel = new DefaultTableModel(col, 0) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            table = createStyledTable(modelTabel);
            JScrollPane scroll = new JScrollPane(table);
            scroll.setBounds(30, 80, 580, 500);
            add(scroll);

            // Form
            int xForm = 630;
            int wForm = 280;
            int y = 80;
            int gap = 60;

            add(createLabel("Nama", xForm, y));
            textNama = createRoundedInput(xForm, y + 20, wForm);
            add(textNama);

            add(createLabel("Nomor Telepon", xForm, y + gap));
            textNoHP = createRoundedInput(xForm, y + gap + 20, wForm);
            add(textNoHP);

            add(createLabel("Layanan", xForm, y + gap * 2));
            comboLayanan = new JComboBox<>();
            comboLayanan.setBounds(xForm, y + gap * 2 + 20, wForm, 40);
            comboLayanan.setBackground(COLOR_INPUT);
            add(comboLayanan);

            add(createLabel("Berat", xForm, y + gap * 3));
            textBerat = createRoundedField();
            textBerat.setBounds(xForm, y + gap * 3 + 20, 240, 40);
            add(textBerat);
            JLabel lblKg = new JLabel("kg");
            lblKg.setBounds(xForm + 250, y + gap * 3 + 20, 30, 40);
            add(lblKg);

            // Status Buttons
            add(createLabel("Status", xForm, y + gap * 4));
            int yBtn = y + gap * 4 + 20;
            btnStatusBelum = new RoundedButton("Belum");
            btnStatusBelum.setBounds(xForm, yBtn, 85, 35);
            btnStatusProses = new RoundedButton("Proses");
            btnStatusProses.setBounds(xForm + 95, yBtn, 85, 35);
            btnStatusDone = new RoundedButton("Done");
            btnStatusDone.setBounds(xForm + 190, yBtn, 85, 35);
            add(btnStatusBelum);
            add(btnStatusProses);
            add(btnStatusDone);
            updateStatusStyle("BELUM");

            // Biaya
            add(createLabel("Biaya", xForm, y + gap * 5 + 10));
            btnHitung = new RoundedButton("Hitung");
            btnHitung.setColor(COLOR_PRIMARY);
            btnHitung.setForeground(Color.WHITE);
            btnHitung.setBounds(xForm, y + gap * 5 + 30, 90, 40);
            add(btnHitung);

            txtTotal = createRoundedField();
            txtTotal.setText("0");
            txtTotal.setFont(new Font("SansSerif", Font.BOLD, 14));
            txtTotal.setHorizontalAlignment(SwingConstants.RIGHT);
            txtTotal.setBounds(xForm + 100, y + gap * 5 + 30, 180, 40);
            add(txtTotal);
            txtTotal.putClientProperty("nilai", 0);

            labelEstimasi = new JLabel();

            // Action Buttons
            int yAction = 540;
            btnSimpan = new RoundedButton("Simpan");
            btnSimpan.setColor(COLOR_PRIMARY);
            btnSimpan.setForeground(Color.WHITE);
            btnSimpan.setBounds(xForm + 130, yAction, 150, 45);
            add(btnSimpan);
            btnUpdate = new RoundedButton("Update");
            btnUpdate.setColor(COLOR_PRIMARY);
            btnUpdate.setForeground(Color.WHITE);
            btnUpdate.setBounds(xForm + 130, yAction, 150, 45);
            add(btnUpdate);
            btnHapus = new RoundedButton("Hapus");
            btnHapus.setColor(COLOR_RED);
            btnHapus.setForeground(Color.WHITE);
            btnHapus.setBounds(xForm + 130, yAction, 150, 45);
            add(btnHapus);

            // --- EVENTS ---
            btnStatusBelum.addActionListener(e -> updateStatusStyle("BELUM"));
            btnStatusProses.addActionListener(e -> updateStatusStyle("PROSES"));
            btnStatusDone.addActionListener(e -> updateStatusStyle("DONE"));

            btnHitung.addActionListener(e -> hitungBiaya());

            btnSimpan.addActionListener(e -> {
                if (validasi()) {
                    showCustomDialog("Apakah anda yakin ingin\nmenyimpan transaksi?", "Simpan", () -> {
                        simpanTransaksi();
                    });
                }
            });

            btnUpdate.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0 && validasi()) {
                    showCustomDialog("Apakah anda yakin ingin\nmengupdate transaksi?", "Update", () -> {
                        updateTransaksi(Integer.parseInt(table.getValueAt(row, 0).toString()));
                    });
                } else
                    JOptionPane.showMessageDialog(null, "Pilih data dulu!");
            });

            btnHapus.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    showCustomDialog("Apakah anda yakin ingin\nmenghapus transaksi?", "Hapus", () -> {
                        transaksiDAO.deleteTransaksi(Integer.parseInt(table.getValueAt(row, 0).toString()));
                        refreshData();
                        resetForm();
                        JOptionPane.showMessageDialog(null, "Data Dihapus!");
                    });
                } else
                    JOptionPane.showMessageDialog(null, "Pilih data dulu!");
            });

            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    // KLIK 2x: Deselect & Reset Form
                    if (e.getClickCount() == 2) {
                        table.clearSelection();
                        resetForm();
                        setFormEditable(true); // Unlock
                        return;
                    }

                    // KLIK 1x: Load Data
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                        Transaksi t = transaksiDAO.getTransaksiById(id);
                        if (t != null) {
                            textNama.setText(t.getNamaPelanggan());
                            textNoHP.setText(t.getNoHP());
                            textBerat.setText(String.valueOf(t.getBerat()));
                            updateStatusStyle(t.getStatus());
                            txtTotal.setText(String.valueOf(t.getTotalHarga()));
                            txtTotal.putClientProperty("nilai", t.getTotalHarga());

                            for (int i = 0; i < comboLayanan.getItemCount(); i++) {
                                if (comboLayanan.getItemAt(i).getNama().equals(t.getLayanan())) {
                                    comboLayanan.setSelectedIndex(i);
                                    break;
                                }
                            }
                            labelEstimasi.putClientProperty("estimasi", t.getEstimasiSelesai());

                            // LOGIC KUNCI FORM BERDASARKAN MODE
                            if (currentMode.equals("INPUT") || currentMode.equals("HAPUS")) {
                                setFormEditable(false); // Mode Input/Hapus: Lihat saja
                            } else if (currentMode.equals("UPDATE")) {
                                setFormEditable(true); // Mode Update: Boleh edit
                            }
                        }
                    }
                }
            });
        }

        // --- HELPER UNTUK MENGUNCI FORM ---
        private void setFormEditable(boolean editable) {
            textNama.setEditable(editable);
            textNoHP.setEditable(editable);
            textBerat.setEditable(editable);
            comboLayanan.setEnabled(editable);
            btnStatusBelum.setEnabled(editable);
            btnStatusProses.setEnabled(editable);
            btnStatusDone.setEnabled(editable);
            btnHitung.setEnabled(editable);
        }

        // --- BUSINESS LOGIC METHODS ---
        public void refreshData() {
            comboLayanan.removeAllItems();
            for (LayananItem l : LayananManager.getAllLayanan())
                comboLayanan.addItem(l);

            modelTabel.setRowCount(0);
            for (Transaksi t : transaksiDAO.getAllTransaksi()) {
                modelTabel.addRow(new Object[] { t.getId(), t.getNamaPelanggan(), t.getNoHP(), t.getLayanan(),
                        t.getBerat(), t.getStatus(), t.getTotalHarga(), t.getEstimasiSelesaiFormatted() });
            }
        }

        private void hitungBiaya() {
            try {
                int berat = Integer.parseInt(textBerat.getText());
                LayananItem item = (LayananItem) comboLayanan.getSelectedItem();
                if (item == null)
                    return;
                int harga = LayananManager.hitungHarga(item.getNama(), berat);
                txtTotal.setText(String.valueOf(harga));
                txtTotal.putClientProperty("nilai", harga);
                LocalDate estimasi = LocalDate.now().plusDays(item.getEstimasiHari());
                labelEstimasi.putClientProperty("estimasi", estimasi);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Berat harus angka!");
            }
        }

        private void simpanTransaksi() {
            int berat = Integer.parseInt(textBerat.getText());
            int harga = (Integer) txtTotal.getClientProperty("nilai");
            LayananItem item = (LayananItem) comboLayanan.getSelectedItem();

            Transaksi t = new Transaksi(textNama.getText(), textNoHP.getText(), item.getNama(), berat, harga);
            t.setStatus(selectedStatus);
            LocalDate est = (LocalDate) labelEstimasi.getClientProperty("estimasi");
            if (est != null)
                t.setEstimasiSelesai(est);

            transaksiDAO.insertTransaksi(t, UserSession.getUsername());
            refreshData();
            resetForm();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan!");
        }

        private void updateTransaksi(int id) {
            int berat = Integer.parseInt(textBerat.getText());
            int harga = (Integer) txtTotal.getClientProperty("nilai");
            LayananItem item = (LayananItem) comboLayanan.getSelectedItem();

            Transaksi t = transaksiDAO.getTransaksiById(id);
            t.setNamaPelanggan(textNama.getText());
            t.setNoHP(textNoHP.getText());
            t.setLayanan(item.getNama());
            t.setBerat(berat);
            t.setTotalHarga(harga);
            t.setStatus(selectedStatus);
            LocalDate est = (LocalDate) labelEstimasi.getClientProperty("estimasi");
            if (est != null)
                t.setEstimasiSelesai(est);

            transaksiDAO.updateTransaksi(t, UserSession.getUsername());
            refreshData();
            resetForm();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diupdate!");
        }

        private void resetForm() {
            textNama.setText("");
            textNoHP.setText("");
            textBerat.setText("");
            txtTotal.setText("0");
            updateStatusStyle("BELUM");
            table.clearSelection();
        }

        private boolean validasi() {
            if (textNama.getText().isEmpty() || textBerat.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Data tidak lengkap!");
                return false;
            }
            if (txtTotal.getClientProperty("nilai") == null || (int) txtTotal.getClientProperty("nilai") == 0) {
                hitungBiaya();
            }
            return true;
        }

        public void setMode(String mode) {
            this.currentMode = mode;
            btnSimpan.setVisible(false);
            btnUpdate.setVisible(false);
            btnHapus.setVisible(false);

            // Reset form setiap kali ganti mode agar bersih
            resetForm();
            if (table != null)
                table.clearSelection();

            if (mode.equals("INPUT")) {
                btnSimpan.setVisible(true);
                setFormEditable(true); // Default input harus aktif
            } else if (mode.equals("UPDATE"))
                btnUpdate.setVisible(true);
            else if (mode.equals("HAPUS"))
                btnHapus.setVisible(true);
        }

        private void updateStatusStyle(String status) {
            selectedStatus = status;
            resetStatusBtn(btnStatusBelum);
            resetStatusBtn(btnStatusProses);
            resetStatusBtn(btnStatusDone);
            if (status.equals("BELUM")) {
                btnStatusBelum.setColor(COLOR_RED);
                btnStatusBelum.setForeground(Color.WHITE);
            } else if (status.equals("PROSES")) {
                btnStatusProses.setColor(COLOR_YELLOW);
                btnStatusProses.setForeground(Color.BLACK);
            } else if (status.equals("DONE")) {
                btnStatusDone.setColor(COLOR_GREEN);
                btnStatusDone.setForeground(Color.WHITE);
            }
        }

        private void resetStatusBtn(RoundedButton btn) {
            btn.setColor(new Color(220, 230, 235));
            btn.setForeground(Color.GRAY);
        }
    }

    class LaporanPanel extends BackgroundPanel {
        private JTable table;
        private DefaultTableModel model;
        private JLabel lblTotal, lblCount;
        private JComboBox<String> comboPeriode;

        public LaporanPanel() {
            super("image/bg_dashboard.png");
            setLayout(null);

            JLabel lblTitle = new JLabel("LAPORAN KEUANGAN");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblTitle.setForeground(COLOR_PRIMARY);
            lblTitle.setBounds(30, 30, 400, 40);
            add(lblTitle);

            RoundedButton btnBack = new RoundedButton("Kembali");
            btnBack.setBounds(820, 30, 100, 35);
            btnBack.setColor(COLOR_PRIMARY);
            btnBack.setForeground(Color.WHITE);
            add(btnBack);
            btnBack.addActionListener(e -> switchPage(PAGE_MENU));

            add(createLabel("Pilih Periode:", 30, 90));
            comboPeriode = new JComboBox<>(new String[] { "Hari Ini", "Bulan Ini", "Semua" });
            comboPeriode.setBounds(130, 90, 150, 30);
            comboPeriode.setBackground(COLOR_INPUT);
            add(comboPeriode);

            RoundedButton btnShow = new RoundedButton("Tampilkan");
            btnShow.setColor(COLOR_PRIMARY);
            btnShow.setForeground(Color.WHITE);
            btnShow.setBounds(300, 90, 120, 30);
            add(btnShow);

            String[] col = { "ID", "Tanggal", "Nama Pelanggan", "Layanan", "Status", "Total" };
            model = new DefaultTableModel(col, 0);
            table = createStyledTable(model);
            JScrollPane scroll = new JScrollPane(table);
            scroll.setBounds(30, 140, 890, 350);
            add(scroll);

            lblTotal = new JLabel("Total Pendapatan: Rp 0");
            lblTotal.setFont(new Font("SansSerif", Font.BOLD, 18));
            lblTotal.setForeground(COLOR_TEXT);
            lblTotal.setBounds(30, 510, 400, 30);
            add(lblTotal);

            lblCount = new JLabel("Jumlah Transaksi: 0");
            lblCount.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lblCount.setForeground(Color.GRAY);
            lblCount.setBounds(30, 540, 400, 30);
            add(lblCount);

            btnShow.addActionListener(e -> refreshData());
        }

        public void refreshData() {
            model.setRowCount(0);
            String periode = comboPeriode.getSelectedItem().toString();
            LocalDate today = LocalDate.now();
            List<Transaksi> list;

            if (periode.equals("Hari Ini"))
                list = transaksiDAO.getTransaksiByDate(today);
            else if (periode.equals("Bulan Ini"))
                list = transaksiDAO.getTransaksiByMonth(today.getYear(), today.getMonthValue());
            else
                list = transaksiDAO.getAllTransaksi();

            int total = 0;
            for (Transaksi t : list) {
                total += t.getTotalHarga();
                model.addRow(new Object[] { t.getId(), t.getTanggalMasukFormatted(), t.getNamaPelanggan(),
                        t.getLayanan(), t.getStatus(), t.getTotalHarga() });
            }
            lblTotal.setText("Total Pendapatan: Rp " + total);
            lblCount.setText("Jumlah Transaksi: " + list.size());
        }
    }

    class ManageUserPanel extends BackgroundPanel {
        private DefaultTableModel modelUser, modelReq;
        private JTextField tUser, tName;
        private JPasswordField tPass;
        private JComboBox<String> cRole;
        private JTextArea txtDetail;
        private JTable tableUser, tableReq;

        public ManageUserPanel() {
            super("image/bg_dashboard.png");
            setLayout(null);

            JLabel lblTitle = new JLabel("KELOLA USER");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblTitle.setForeground(COLOR_PRIMARY);
            lblTitle.setBounds(30, 30, 400, 40);
            add(lblTitle);

            RoundedButton btnBack = new RoundedButton("Kembali");
            btnBack.setBounds(820, 30, 100, 35);
            btnBack.setColor(COLOR_PRIMARY);
            btnBack.setForeground(Color.WHITE);
            add(btnBack);
            btnBack.addActionListener(e -> switchPage(PAGE_MENU));

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setBounds(30, 80, 890, 500);
            tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));

            JPanel panelUser = new JPanel(null);
            panelUser.setBackground(new Color(245, 250, 255));
            JLabel lUser = new JLabel("Username:");
            lUser.setBounds(20, 20, 100, 25);
            panelUser.add(lUser);
            tUser = createRoundedField();
            tUser.setBounds(100, 20, 150, 30);
            panelUser.add(tUser);

            JLabel lPass = new JLabel("Password:");
            lPass.setBounds(20, 60, 100, 25);
            panelUser.add(lPass);
            tPass = new JPasswordField();
            styleField(tPass);
            tPass.setBounds(100, 60, 150, 30);
            panelUser.add(tPass);

            JLabel lName = new JLabel("Nama:");
            lName.setBounds(270, 20, 100, 25);
            panelUser.add(lName);
            tName = createRoundedField();
            tName.setBounds(330, 20, 150, 30);
            panelUser.add(tName);

            JLabel lRole = new JLabel("Role:");
            lRole.setBounds(270, 60, 100, 25);
            panelUser.add(lRole);
            cRole = new JComboBox<>(new String[] { "KARYAWAN", "OWNER" });
            cRole.setBounds(330, 60, 150, 30);
            panelUser.add(cRole);

            RoundedButton btnAdd = new RoundedButton("Tambah");
            btnAdd.setColor(COLOR_GREEN);
            btnAdd.setForeground(Color.WHITE);
            btnAdd.setBounds(520, 20, 100, 30);
            panelUser.add(btnAdd);
            btnAdd.addActionListener(e -> {
                UserSession.daftarBaru(tUser.getText(), new String(tPass.getPassword()),
                        cRole.getSelectedItem().toString(), tName.getText());
                refreshData();
                JOptionPane.showMessageDialog(null, "User ditambah!");
            });

            RoundedButton btnDel = new RoundedButton("Hapus");
            btnDel.setColor(COLOR_RED);
            btnDel.setForeground(Color.WHITE);
            btnDel.setBounds(520, 60, 100, 30);
            panelUser.add(btnDel);
            btnDel.addActionListener(e -> {
                int r = tableUser.getSelectedRow();
                if (r >= 0) {
                    UserSession.deleteUser(tableUser.getValueAt(r, 0).toString());
                    refreshData();
                    JOptionPane.showMessageDialog(null, "User dihapus!");
                }
            });

            String[] colU = { "Username", "Nama Lengkap", "Role" };
            modelUser = new DefaultTableModel(colU, 0);
            tableUser = createStyledTable(modelUser);
            JScrollPane scrollU = new JScrollPane(tableUser);
            scrollU.setBounds(20, 110, 840, 330);
            panelUser.add(scrollU);

            JPanel panelReq = new JPanel(null);
            panelReq.setBackground(new Color(245, 250, 255));
            String[] colR = { "ID", "Username", "Nama", "Alasan", "Status" };
            modelReq = new DefaultTableModel(colR, 0);
            tableReq = createStyledTable(modelReq);
            JScrollPane scrollR = new JScrollPane(tableReq);
            scrollR.setBounds(20, 20, 840, 250);
            panelReq.add(scrollR);

            txtDetail = new JTextArea();
            txtDetail.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            txtDetail.setBounds(20, 290, 500, 100);
            panelReq.add(txtDetail);

            RoundedButton btnApp = new RoundedButton("Approve");
            btnApp.setColor(COLOR_GREEN);
            btnApp.setForeground(Color.WHITE);
            btnApp.setBounds(540, 290, 120, 40);
            panelReq.add(btnApp);
            btnApp.addActionListener(e -> processRequest("APPROVED"));

            RoundedButton btnDec = new RoundedButton("Decline");
            btnDec.setColor(COLOR_RED);
            btnDec.setForeground(Color.WHITE);
            btnDec.setBounds(680, 290, 120, 40);
            panelReq.add(btnDec);
            btnDec.addActionListener(e -> processRequest("DECLINED"));

            tableReq.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int r = tableReq.getSelectedRow();
                    if (r >= 0)
                        txtDetail.setText("Alasan: " + tableReq.getValueAt(r, 3));
                }
            });

            tabbedPane.addTab("Daftar User", panelUser);
            tabbedPane.addTab("Request Access", panelReq);
            add(tabbedPane);
        }

        public void refreshData() {
            modelUser.setRowCount(0);
            for (User u : UserSession.getAllUsers())
                modelUser.addRow(new Object[] { u.getUsername(), u.getNamaLengkap(), u.getRole() });

            modelReq.setRowCount(0);
            for (AccessRequest ar : accessRequestDAO.getAccessRequestsByStatus("PENDING"))
                modelReq.addRow(new Object[] { ar.getId(), ar.getUsername(), ar.getNamaLengkap(), ar.getAlasan(),
                        ar.getStatus() });
        }

        private void processRequest(String status) {
            int r = tableReq.getSelectedRow();
            if (r >= 0) {
                int id = Integer.parseInt(tableReq.getValueAt(r, 0).toString());
                int adminId = userDAO.getUserId(UserSession.getUsername());

                if (status.equals("APPROVED")) {
                    String username = tableReq.getValueAt(r, 1).toString();
                    String nama = tableReq.getValueAt(r, 2).toString();
                    AccessRequest fullReq = accessRequestDAO.getAccessRequestById(id);
                    UserSession.daftarBaru(username, fullReq.getPassword(), "KARYAWAN", nama);
                }

                accessRequestDAO.updateAccessRequestStatus(id, status, adminId);
                refreshData();
                JOptionPane.showMessageDialog(null, "Request " + status);
            }
        }
    }

    class EditLayananPanel extends BackgroundPanel {
        private JTextField tNama, tHarga, tEst;
        private DefaultTableModel model;
        private JTable table;

        public EditLayananPanel() {
            super("image/bg_dashboard.png");
            setLayout(null);

            JLabel lblTitle = new JLabel("EDIT LAYANAN");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblTitle.setForeground(COLOR_PRIMARY);
            lblTitle.setBounds(30, 30, 400, 40);
            add(lblTitle);

            RoundedButton btnBack = new RoundedButton("Kembali");
            btnBack.setBounds(820, 30, 100, 35);
            btnBack.setColor(COLOR_PRIMARY);
            btnBack.setForeground(Color.WHITE);
            add(btnBack);
            btnBack.addActionListener(e -> switchPage(PAGE_MENU));

            int x = 30;
            int y = 90;
            add(createLabel("Nama Layanan:", x, y));
            tNama = createRoundedField();
            tNama.setBounds(150, y, 200, 35);
            add(tNama);
            add(createLabel("Harga / Kg:", x, y + 45));
            tHarga = createRoundedField();
            tHarga.setBounds(150, y + 45, 200, 35);
            add(tHarga);
            add(createLabel("Estimasi (Hari):", x, y + 90));
            tEst = createRoundedField();
            tEst.setBounds(150, y + 90, 200, 35);
            add(tEst);

            int bx = 380;
            RoundedButton btnAdd = new RoundedButton("Tambah");
            btnAdd.setColor(COLOR_GREEN);
            btnAdd.setForeground(Color.WHITE);
            btnAdd.setBounds(bx, y, 100, 35);
            add(btnAdd);
            btnAdd.addActionListener(e -> {
                LayananManager.addLayanan(tNama.getText(), Integer.parseInt(tHarga.getText()),
                        Integer.parseInt(tEst.getText()));
                refreshData();
                JOptionPane.showMessageDialog(null, "Layanan Ditambah");
            });

            RoundedButton btnUpd = new RoundedButton("Update");
            btnUpd.setColor(COLOR_YELLOW);
            btnUpd.setForeground(Color.BLACK);
            btnUpd.setBounds(bx, y + 45, 100, 35);
            add(btnUpd);
            btnUpd.addActionListener(e -> {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    LayananManager.updateLayanan(table.getValueAt(r, 0).toString(), tNama.getText(),
                            Integer.parseInt(tHarga.getText()), Integer.parseInt(tEst.getText()));
                    refreshData();
                    JOptionPane.showMessageDialog(null, "Layanan Diupdate");
                }
            });

            RoundedButton btnDel = new RoundedButton("Hapus");
            btnDel.setColor(COLOR_RED);
            btnDel.setForeground(Color.WHITE);
            btnDel.setBounds(bx, y + 90, 100, 35);
            add(btnDel);
            btnDel.addActionListener(e -> {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    LayananManager.deleteLayanan(table.getValueAt(r, 0).toString());
                    refreshData();
                    JOptionPane.showMessageDialog(null, "Layanan Dihapus");
                }
            });

            String[] col = { "Nama Layanan", "Harga", "Estimasi" };
            model = new DefaultTableModel(col, 0);
            table = createStyledTable(model);
            JScrollPane scroll = new JScrollPane(table);
            scroll.setBounds(30, 250, 890, 330);
            add(scroll);

            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int r = table.getSelectedRow();
                    if (r >= 0) {
                        tNama.setText(table.getValueAt(r, 0).toString());
                        tHarga.setText(table.getValueAt(r, 1).toString());
                        tEst.setText(table.getValueAt(r, 2).toString());
                    }
                }
            });
        }

        public void refreshData() {
            model.setRowCount(0);
            for (LayananItem l : LayananManager.getAllLayanan()) {
                model.addRow(new Object[] { l.getNama(), l.getHargaPerKg(), l.getEstimasiHari() });
            }
        }
    }

    // --- UTILITIES ---

    private JLabel createLabel(String t, int x, int y) {
        JLabel l = new JLabel(t);
        l.setBounds(x, y, 200, 20);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(COLOR_TEXT);
        return l;
    }

    private JTextField createRoundedField() {
        JTextField tf = new JTextField();
        styleField(tf);
        return tf;
    }

    private JTextField createRoundedInput(int x, int y, int w) {
        JTextField t = createRoundedField();
        t.setBounds(x, y, w, 40);
        return t;
    }

    private void styleField(JTextField tf) {
        tf.setBackground(COLOR_INPUT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, new Color(200, 200, 200)), new EmptyBorder(0, 10, 0, 10)));
        tf.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(200, 220, 230));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(210, 225, 235));
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(0, 40));
        return table;
    }

    private void showCustomDialog(String message, String actionText, Runnable action) {
        JDialog dialog = new JDialog(AplikasiLaundry.this, true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(null);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(200, 200, 200));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        panel.setLayout(null);

        JLabel lblMsg = new JLabel("<html><center>" + message.replace("\n", "<br>") + "</center></html>");
        lblMsg.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblMsg.setHorizontalAlignment(SwingConstants.CENTER);
        lblMsg.setBounds(20, 30, 360, 100);
        panel.add(lblMsg);

        RoundedButton btnCancel = new RoundedButton("Batalkan");
        btnCancel.setColor(COLOR_PRIMARY);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setBounds(40, 160, 150, 45);
        btnCancel.addActionListener(e -> dialog.dispose());
        panel.add(btnCancel);

        RoundedButton btnAction = new RoundedButton(actionText);
        btnAction.setColor(COLOR_PRIMARY);
        btnAction.setForeground(Color.WHITE);
        btnAction.setBounds(210, 160, 150, 45);
        btnAction.addActionListener(e -> {
            dialog.dispose();
            action.run();
        });
        panel.add(btnAction);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    class RoundedButton extends JButton {
        private Color color = new Color(220, 220, 220);
        private int radius = 15;
        private boolean isHovered = false;
        private boolean isPressed = false;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("SansSerif", Font.BOLD, 13));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }

                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    repaint();
                }

                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
                    repaint();
                }
            });
        }

        public void setColor(Color c) {
            this.color = c;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (!isEnabled()) {
                g2.setColor(new Color(230, 230, 230));
            } else {
                Color baseColor = isPressed ? color.darker() : color;
                g2.setColor(baseColor);
            }

            // Gambar Background Bulat
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            // Effect Hover
            if (isHovered && isEnabled()) {
                g2.setColor(new Color(255, 255, 255, 150));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
            }

            super.paintComponent(g);
        }
    }

    class BackgroundPanel extends JPanel {
        private Image bgImage;

        public BackgroundPanel(String fileName) {
            try {
                bgImage = new ImageIcon(fileName).getImage();
            } catch (Exception e) {
            }
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null && bgImage.getWidth(null) > 0)
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            else {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(200, 230, 250)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    class RoundedBorder implements Border {
        private int r;
        private Color c;

        RoundedBorder(int r, Color c) {
            this.r = r;
            this.c = c;
        }

        public Insets getBorderInsets(Component cmp) {
            return new Insets(r / 2, r / 2, r / 2, r / 2);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component cmp, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.drawRoundRect(x, y, w - 1, h - 1, r, r);
        }
    }
}
