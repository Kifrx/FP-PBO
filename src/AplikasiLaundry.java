import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AplikasiLaundry extends JFrame {

    JTextField textNama, textNoHP, textBerat;
    JComboBox<LayananItem> comboLayanan;
    JComboBox<String> comboStatus;
    JLabel labelTotalHarga, labelTotalPendapatan, labelHeaderGambar, labelRoleInfo, labelEstimasi;
    JTable tabelData;
    DefaultTableModel modelTabel;
    TransaksiDAO transaksiDAO = new TransaksiDAO();
    AccessRequestDAO accessRequestDAO = new AccessRequestDAO();
    UserDAO userDAO = new UserDAO();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AplikasiLaundry());
    }


    public AplikasiLaundry() {
        tampilkanLayarLogin();
    }

    //Login view
    private void tampilkanLayarLogin() {
        JFrame frameLogin = new JFrame("Login Sistem Laundry");
        frameLogin.setSize(600, 400); 
        frameLogin.setLayout(null);
        frameLogin.setLocationRelativeTo(null);
        frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameLogin.getContentPane().setBackground(new Color(230, 240, 255));

        JLabel lblJudul = new JLabel("LOGIN APLIKASI");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 24));
        lblJudul.setBounds(0, 50, 600, 40);
        lblJudul.setHorizontalAlignment(SwingConstants.CENTER);
        frameLogin.add(lblJudul);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(150, 120, 100, 30);
        frameLogin.add(lblUser);
        JTextField txtUser = new JTextField();
        txtUser.setBounds(250, 120, 200, 30);
        frameLogin.add(txtUser);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(150, 170, 100, 30);
        frameLogin.add(lblPass);
        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(250, 170, 200, 30);
        frameLogin.add(txtPass);

        JButton btnLogin = new JButton("MASUK");
        btnLogin.setBounds(250, 220, 200, 40);
        btnLogin.setBackground(Color.BLUE);
        btnLogin.setForeground(Color.WHITE);
        frameLogin.add(btnLogin);

        JLabel lblReg = new JLabel("Belum punya akun? Request Access disini");
        lblReg.setBounds(0, 280, 600, 30);
        lblReg.setHorizontalAlignment(SwingConstants.CENTER);
        lblReg.setForeground(Color.BLUE);
        lblReg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        frameLogin.add(lblReg);

        // LOGIC LOGIN
        btnLogin.addActionListener(e -> {
            if (UserSession.cekLogin(txtUser.getText(), new String(txtPass.getPassword()))) {
                frameLogin.dispose();
                tampilkanDashboard();
            } else {
                JOptionPane.showMessageDialog(null, "Salah woy!");
            }
        });

        lblReg.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frameLogin.dispose();
                tampilkanLayarRequestAccess();
            }
        });

        frameLogin.setVisible(true);
    }

    //Request Access view
    private void tampilkanLayarRequestAccess() {
        JFrame frameReg = new JFrame("Request Access");
        frameReg.setSize(600, 500);
        frameReg.setLayout(null);
        frameReg.setLocationRelativeTo(null);
        frameReg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblJudul = new JLabel("REQUEST ACCESS");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 24));
        lblJudul.setBounds(0, 40, 600, 40);
        lblJudul.setHorizontalAlignment(SwingConstants.CENTER);
        frameReg.add(lblJudul);

        JLabel lblInfo = new JLabel("Request Anda akan diverifikasi oleh Owner");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setBounds(0, 80, 600, 20);
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfo.setForeground(Color.GRAY);
        frameReg.add(lblInfo);

        JLabel lblUser = new JLabel("Username :");
        lblUser.setBounds(80, 110, 120, 30);
        frameReg.add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(200, 110, 200, 30);
        frameReg.add(txtUser);

        JLabel lblPass = new JLabel("Password :");
        lblPass.setBounds(80, 160, 120, 30);
        frameReg.add(lblPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(200, 160, 200, 30);
        frameReg.add(txtPass);

        JLabel lblNama = new JLabel("Nama Lengkap :");
        lblNama.setBounds(80, 210, 120, 30);
        frameReg.add(lblNama);

        JTextField txtNama = new JTextField();
        txtNama.setBounds(200, 210, 200, 30);
        frameReg.add(txtNama);

        JLabel lblAlasan = new JLabel("Alasan Request :");
        lblAlasan.setBounds(80, 250, 120, 30);
        frameReg.add(lblAlasan);

        JTextArea txtAlasan = new JTextArea();
        txtAlasan.setLineWrap(true);
        txtAlasan.setWrapStyleWord(true);
        JScrollPane scrollAlasan = new JScrollPane(txtAlasan);
        scrollAlasan.setBounds(200, 250, 200, 60);
        frameReg.add(scrollAlasan);

        JButton btnRequest = new JButton("KIRIM REQUEST");
        btnRequest.setBounds(200, 330, 200, 40);
        btnRequest.setBackground(new Color(0, 123, 255));
        btnRequest.setForeground(Color.WHITE);
        frameReg.add(btnRequest);

        JButton btnKembali = new JButton("Batal");
        btnKembali.setBounds(200, 380, 200, 30);
        frameReg.add(btnKembali);

        btnRequest.addActionListener(e -> {
            if (txtUser.getText().isEmpty() || new String(txtPass.getPassword()).isEmpty() || 
                txtNama.getText().isEmpty() || txtAlasan.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Isi semua field!");
                return;
            }
            
            // Cek apakah username sudah ada
            for (User u : UserSession.getAllUsers()) {
                if (u.getUsername().equals(txtUser.getText())) {
                    JOptionPane.showMessageDialog(null, "Username sudah digunakan!");
                    return;
                }
            }
            
            // Cek apakah sudah pernah request dengan username yang sama
            if (accessRequestDAO.isPendingRequestExists(txtUser.getText())) {
                JOptionPane.showMessageDialog(null, "Request dengan username ini sudah ada dan menunggu approval!");
                return;
            }
            
            AccessRequest request = new AccessRequest(
                txtUser.getText(),
                new String(txtPass.getPassword()),
                txtNama.getText(),
                txtAlasan.getText()
            );
            accessRequestDAO.insertAccessRequest(request);
            
            JOptionPane.showMessageDialog(null, 
                "Request berhasil dikirim!\nSilakan tunggu approval dari Owner.\n",
                "Request Terkirim",
                JOptionPane.INFORMATION_MESSAGE);
            frameReg.dispose();
            tampilkanLayarLogin();
        });

        btnKembali.addActionListener(e -> {
            frameReg.dispose();
            tampilkanLayarLogin();
        });

        frameReg.setVisible(true);
    }

    //Dashboard view
    private void tampilkanDashboard() {
        // Bersihkan semua komponen lama
        getContentPane().removeAll();
        
        setTitle("Sistem Laundry - " + UserSession.getNamaLengkap() + " (" + UserSession.getRole() + ")");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Header Gambar
        labelHeaderGambar = new JLabel("[ AREA GAMBAR LOGO ]");
        labelHeaderGambar.setOpaque(true);
        labelHeaderGambar.setBackground(Color.DARK_GRAY);
        labelHeaderGambar.setForeground(Color.WHITE);
        labelHeaderGambar.setHorizontalAlignment(SwingConstants.CENTER);
        labelHeaderGambar.setBounds(0, 0, 1100, 120);
        add(labelHeaderGambar);

        // Info Role & User
        labelRoleInfo = new JLabel("Login: " + UserSession.getNamaLengkap() + " | Role: " + UserSession.getRole());
        labelRoleInfo.setBounds(20, 130, 500, 25);
        labelRoleInfo.setFont(new Font("Arial", Font.BOLD, 12));
        add(labelRoleInfo);

        int y = 165;

        // Input Form
        addLabel("Nama:", 30, y);
        textNama = addInput(130, y, 200);
        
        addLabel("No HP:", 30, y+35);
        textNoHP = addInput(130, y+35, 200);
        
        addLabel("Layanan:", 360, y);
        comboLayanan = new JComboBox<>();
        updateComboLayanan();
        comboLayanan.setBounds(460, y, 180, 25);
        add(comboLayanan);
        
        addLabel("Berat (kg):", 360, y+35);
        textBerat = addInput(460, y+35, 100);

        addLabel("Status:", 670, y);
        String[] statusMenu = {"BELUM", "PROSES", "DONE"};
        comboStatus = new JComboBox<>(statusMenu);
        comboStatus.setBounds(770, y, 120, 25);
        add(comboStatus);

        labelEstimasi = new JLabel("Estimasi: -");
        labelEstimasi.setBounds(670, y+35, 220, 25);
        labelEstimasi.setFont(new Font("Arial", Font.ITALIC, 11));
        add(labelEstimasi);

        // Tombol Hitung
        JButton btnHitung = new JButton("Hitung");
        btnHitung.setBounds(460, y+75, 100, 25);
        add(btnHitung);

        labelTotalHarga = new JLabel("Rp 0");
        labelTotalHarga.setBounds(570, y+75, 200, 25);
        labelTotalHarga.setForeground(Color.BLUE);
        labelTotalHarga.setFont(new Font("Arial", Font.BOLD, 14));
        add(labelTotalHarga);

        // Tombol CRUD
        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(30, y+120, 100, 35);
        btnSimpan.setBackground(Color.GREEN);
        add(btnSimpan);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(140, y+120, 100, 35);
        btnUpdate.setBackground(Color.YELLOW);
        add(btnUpdate);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setBounds(250, y+120, 100, 35);
        btnHapus.setBackground(Color.RED);
        btnHapus.setForeground(Color.WHITE);
        add(btnHapus);

        // Tombol Owner Only
        JButton btnLaporan = new JButton("Laporan");
        btnLaporan.setBounds(400, y+120, 100, 35);
        btnLaporan.setBackground(new Color(0, 150, 200));
        btnLaporan.setForeground(Color.WHITE);
        btnLaporan.setEnabled(UserSession.isOwner());
        add(btnLaporan);

        JButton btnManageUser = new JButton("Kelola User");
        btnManageUser.setBounds(510, y+120, 120, 35);
        btnManageUser.setBackground(new Color(100, 50, 150));
        btnManageUser.setForeground(Color.WHITE);
        btnManageUser.setEnabled(UserSession.isOwner());
        add(btnManageUser);
        
        // Update button text jika ada pending request
        if (UserSession.isOwner()) {
            int pendingCount = accessRequestDAO.getPendingRequestCount();
            if (pendingCount > 0) {
                btnManageUser.setText("Kelola User (" + pendingCount + ")");
            }
        }

        JButton btnEditLayanan = new JButton("Edit Layanan");
        btnEditLayanan.setBounds(640, y+120, 120, 35);
        btnEditLayanan.setBackground(new Color(255, 140, 0));
        btnEditLayanan.setForeground(Color.WHITE);
        btnEditLayanan.setEnabled(UserSession.isOwner());
        add(btnEditLayanan);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(970, y+120, 100, 35);
        add(btnLogout);

        // Tabel
        String[] col = {"ID", "Nama", "HP", "Layanan", "Berat", "Total", "Status", "Tgl Masuk", "Estimasi", "Petugas"};
        modelTabel = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Semua cell tidak bisa di-edit langsung
            }
        };
        tabelData = new JTable(modelTabel);
        tabelData.getColumnModel().getColumn(0).setPreferredWidth(30);
        tabelData.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabelData.getColumnModel().getColumn(2).setPreferredWidth(90);
        JScrollPane scroll = new JScrollPane(tabelData);
        scroll.setBounds(30, y+170, 1040, 250);
        add(scroll);

        // Total Pendapatan
        labelTotalPendapatan = new JLabel("Total Pendapatan: Rp 0");
        labelTotalPendapatan.setBounds(30, y+430, 500, 30);
        labelTotalPendapatan.setFont(new Font("Arial", Font.BOLD, 18));
        add(labelTotalPendapatan);

        btnHitung.addActionListener(e -> {
            try {
                int berat = Integer.parseInt(textBerat.getText());
                LayananItem selectedLayanan = (LayananItem) comboLayanan.getSelectedItem();
                
                if (selectedLayanan == null) {
                    JOptionPane.showMessageDialog(null, "Pilih layanan terlebih dahulu!");
                    return;
                }

                int hasil = LayananManager.hitungHarga(selectedLayanan.getNama(), berat);
                int estimasiHari = selectedLayanan.getEstimasiHari();
                LocalDate estimasi = LocalDate.now().plusDays(estimasiHari);
                
                labelTotalHarga.setText("Rp " + hasil);
                labelTotalHarga.putClientProperty("nilai", hasil);
                labelEstimasi.setText("Estimasi: " + estimasi.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " (" + estimasiHari + " hari)");
                labelEstimasi.putClientProperty("estimasi", estimasi);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Berat harus angka!");
            }
        });

        // Logika Simpan
        btnSimpan.addActionListener(e -> {
            if (validasi()) {
                int berat = Integer.parseInt(textBerat.getText());
                int harga = (Integer) labelTotalHarga.getClientProperty("nilai");
                LayananItem selectedLayanan = (LayananItem) comboLayanan.getSelectedItem();
                
                Transaksi transaksi = new Transaksi(
                    textNama.getText(),
                    textNoHP.getText(),
                    selectedLayanan.getNama(),
                    berat,
                    harga
                );
                transaksi.setStatus(comboStatus.getSelectedItem().toString());
                
                // Set estimasi selesai dari data layanan database
                LocalDate estimasi = (LocalDate) labelEstimasi.getClientProperty("estimasi");
                if (estimasi != null) {
                    transaksi.setEstimasiSelesai(estimasi);
                }
                
                transaksiDAO.insertTransaksi(transaksi, UserSession.getUsername());
                loadTransaksiData(); // Reload dari database
                reset();
                JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
            }
        });
        
        // Logika Update
        btnUpdate.addActionListener(e -> {
            int row = tabelData.getSelectedRow();
            if (row >= 0 && validasi()) {
                int id = (Integer) modelTabel.getValueAt(row, 0);
                Transaksi transaksi = findTransaksiById(id);
                
                if (transaksi != null) {
                    int berat = Integer.parseInt(textBerat.getText());
                    LayananItem selectedLayanan = (LayananItem) comboLayanan.getSelectedItem();
                    String namaLayanan = selectedLayanan.getNama();
                    
                    int hasil = LayananManager.hitungHarga(namaLayanan, berat);
                    
                    transaksi.setNamaPelanggan(textNama.getText());
                    transaksi.setNoHP(textNoHP.getText());
                    transaksi.setLayanan(namaLayanan);
                    transaksi.setBerat(berat);
                    transaksi.setTotalHarga(hasil);
                    transaksi.setStatus(comboStatus.getSelectedItem().toString());
                    
                    // Set estimasi selesai dari data layanan database
                    LocalDate estimasi = (LocalDate) labelEstimasi.getClientProperty("estimasi");
                    if (estimasi != null) {
                        transaksi.setEstimasiSelesai(estimasi);
                    }

                    transaksiDAO.updateTransaksi(transaksi, UserSession.getUsername());
                    loadTransaksiData(); // Reload dari database
                    reset();
                    JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
                }
            } else if (row < 0) {
                JOptionPane.showMessageDialog(null, "Pilih data yang akan diupdate!");
            }
        });

        // Logika Hapus
        btnHapus.addActionListener(e -> {
            int row = tabelData.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(null, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (Integer) modelTabel.getValueAt(row, 0);
                    transaksiDAO.deleteTransaksi(id);
                    loadTransaksiData(); // Reload dari database
                    JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus!");
            }
        });
        
        // Logika Klik Tabel
        tabelData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabelData.getSelectedRow();
                if(row >= 0) {
                    int id = (Integer) modelTabel.getValueAt(row, 0);
                    Transaksi transaksi = findTransaksiById(id);
                    
                    if (transaksi != null) {
                        textNama.setText(transaksi.getNamaPelanggan());
                        textNoHP.setText(transaksi.getNoHP());
                        
                        // Cari layanan by nama
                        for (int i = 0; i < comboLayanan.getItemCount(); i++) {
                            LayananItem item = comboLayanan.getItemAt(i);
                            if (item.getNama().equals(transaksi.getLayanan())) {
                                comboLayanan.setSelectedIndex(i);
                                break;
                            }
                        }
                        
                        textBerat.setText(String.valueOf(transaksi.getBerat()));
                        comboStatus.setSelectedItem(transaksi.getStatus());
                        labelTotalHarga.setText("Rp " + transaksi.getTotalHarga());
                        labelTotalHarga.putClientProperty("nilai", transaksi.getTotalHarga());
                        labelEstimasi.setText("Estimasi: " + transaksi.getEstimasiSelesaiFormatted());
                        labelEstimasi.putClientProperty("estimasi", transaksi.getEstimasiSelesai());
                    }
                }
            }
        });
        
        // Logika Laporan (Owner only)
        btnLaporan.addActionListener(e -> {
            if (UserSession.isOwner()) {
                tampilkanLaporan();
            }
        });

        // Logika Manage User (Owner only)
        btnManageUser.addActionListener(e -> {
            if (UserSession.isOwner()) {
                tampilkanManageUser();
            }
        });

        // Logika Edit Layanan (Owner only)
        btnEditLayanan.addActionListener(e -> {
            if (UserSession.isOwner()) {
                tampilkanEditLayanan();
            }
        });

        btnLogout.addActionListener(e -> {
            UserSession.logout();
            dispose();
            tampilkanLayarLogin();
        });

        // Load data dari database
        loadTransaksiData();

        // Refresh UI
        revalidate();
        repaint();
        setVisible(true);
    }

    // Method untuk load data transaksi dari database
    private void loadTransaksiData() {
        List<Transaksi> allTransaksi = transaksiDAO.getAllTransaksi();
        modelTabel.setRowCount(0); // Clear existing data
        for (Transaksi t : allTransaksi) {
            modelTabel.addRow(new Object[]{
                t.getId(),
                t.getNamaPelanggan(),
                t.getNoHP(),
                t.getLayanan(),
                t.getBerat() + " kg",
                "Rp " + t.getTotalHarga(),
                t.getStatus(),
                t.getTanggalMasukFormatted(),
                t.getEstimasiSelesaiFormatted(),
                t.getNamaPetugas()
            });
        }
        updateTotal();
    }

    // Helper Methods
    private void addLabel(String t, int x, int y) {
        JLabel l = new JLabel(t);
        l.setBounds(x, y, 100, 25);
        add(l);
    }
    private JTextField addInput(int x, int y, int w) {
        JTextField t = new JTextField();
        t.setBounds(x, y, w, 25);
        add(t);
        return t;
    }
    private void updateTotal() {
        int total = transaksiDAO.getTotalPendapatan();
        labelTotalPendapatan.setText("Total Pendapatan: Rp " + total);
    }
    private void reset() {
        textNama.setText("");
        textNoHP.setText("");
        textBerat.setText("");
        comboStatus.setSelectedIndex(0);
        labelTotalHarga.setText("Rp 0");
        labelTotalHarga.putClientProperty("nilai", null);
        labelEstimasi.setText("Estimasi: -");
        labelEstimasi.putClientProperty("estimasi", null);
        tabelData.clearSelection();
    }
    private boolean validasi() {
        if(textNama.getText().isEmpty() || labelTotalHarga.getClientProperty("nilai") == null) {
            JOptionPane.showMessageDialog(null, "Lengkapi data!");
            return false;
        }
        return true;
    }

    private Transaksi findTransaksiById(int id) {
        List<Transaksi> allTransaksi = transaksiDAO.getAllTransaksi();
        for (Transaksi t : allTransaksi) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    // Fitur Laporan Keuangan
    private void tampilkanLaporan() {
        JFrame frameLaporan = new JFrame("Laporan Keuangan");
        frameLaporan.setSize(800, 600);
        frameLaporan.setLayout(null);
        frameLaporan.setLocationRelativeTo(null);

        JLabel lblJudul = new JLabel("LAPORAN KEUANGAN");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 20));
        lblJudul.setBounds(0, 20, 800, 30);
        lblJudul.setHorizontalAlignment(SwingConstants.CENTER);
        frameLaporan.add(lblJudul);

        JLabel lblPeriode = new JLabel("Pilih Periode:");
        lblPeriode.setBounds(50, 70, 100, 25);
        frameLaporan.add(lblPeriode);

        String[] periodeOpt = {"Hari Ini", "Bulan Ini", "Semua"};
        JComboBox<String> comboPeriode = new JComboBox<>(periodeOpt);
        comboPeriode.setBounds(150, 70, 150, 25);
        frameLaporan.add(comboPeriode);

        JButton btnTampilkan = new JButton("Tampilkan");
        btnTampilkan.setBounds(310, 70, 100, 25);
        frameLaporan.add(btnTampilkan);

        // Tabel Laporan
        String[] colLaporan = {"ID", "Nama", "Layanan", "Total", "Status", "Tanggal"};
        DefaultTableModel modelLaporan = new DefaultTableModel(colLaporan, 0);
        JTable tabelLaporan = new JTable(modelLaporan);
        JScrollPane scrollLaporan = new JScrollPane(tabelLaporan);
        scrollLaporan.setBounds(50, 120, 700, 300);
        frameLaporan.add(scrollLaporan);

        JLabel lblTotalLaporan = new JLabel("Total Pendapatan: Rp 0");
        lblTotalLaporan.setBounds(50, 440, 400, 30);
        lblTotalLaporan.setFont(new Font("Arial", Font.BOLD, 16));
        frameLaporan.add(lblTotalLaporan);

        JLabel lblJumlahTransaksi = new JLabel("Jumlah Transaksi: 0");
        lblJumlahTransaksi.setBounds(50, 470, 400, 30);
        lblJumlahTransaksi.setFont(new Font("Arial", Font.PLAIN, 14));
        frameLaporan.add(lblJumlahTransaksi);

        JButton btnTutup = new JButton("Tutup");
        btnTutup.setBounds(650, 520, 100, 30);
        frameLaporan.add(btnTutup);

        btnTampilkan.addActionListener(e -> {
            modelLaporan.setRowCount(0);
            String periode = comboPeriode.getSelectedItem().toString();
            LocalDate today = LocalDate.now();
            List<Transaksi> transaksiList;
            int total = 0;

            if (periode.equals("Hari Ini")) {
                transaksiList = transaksiDAO.getTransaksiByDate(today);
            } else if (periode.equals("Bulan Ini")) {
                transaksiList = transaksiDAO.getTransaksiByMonth(today.getYear(), today.getMonthValue());
            } else {
                transaksiList = transaksiDAO.getAllTransaksi();
            }

            // Hitung total dari transaksiList
            total = 0;
            for (Transaksi t : transaksiList) {
                total += t.getTotalHarga();
            }

            for (Transaksi t : transaksiList) {
                modelLaporan.addRow(new Object[]{
                    t.getId(),
                    t.getNamaPelanggan(),
                    t.getLayanan(),
                    "Rp " + t.getTotalHarga(),
                    t.getStatus(),
                    t.getTanggalMasukFormatted()
                });
            }

            lblTotalLaporan.setText("Total Pendapatan: Rp " + total);
            lblJumlahTransaksi.setText("Jumlah Transaksi: " + transaksiList.size());
        });

        btnTutup.addActionListener(e -> frameLaporan.dispose());

        frameLaporan.setVisible(true);
    }

    // Fitur Manage User (Owner only)
    private void tampilkanManageUser() {
        JFrame frameUser = new JFrame("Kelola User");
        frameUser.setSize(800, 600);
        frameUser.setLayout(null);
        frameUser.setLocationRelativeTo(null);

        JLabel lblJudul = new JLabel("MANAJEMEN USER");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 20));
        lblJudul.setBounds(0, 20, 800, 30);
        lblJudul.setHorizontalAlignment(SwingConstants.CENTER);
        frameUser.add(lblJudul);

        // Tab Panel
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(20, 60, 750, 450);
        
        // Panel User List
        JPanel panelUserList = createUserListPanel();
        tabbedPane.addTab("Daftar User", panelUserList);
        
        // Panel Request List
        JPanel panelRequestList = createRequestListPanel();
        int requestPendingCount = accessRequestDAO.getPendingRequestCount();
        String tabTitle = requestPendingCount > 0 ? "Request Access (" + requestPendingCount + ")" : "Request Access";
        tabbedPane.addTab(tabTitle, panelRequestList);
        
        frameUser.add(tabbedPane);

        JButton btnTutup = new JButton("Tutup");
        btnTutup.setBounds(670, 520, 100, 30);
        frameUser.add(btnTutup);
        btnTutup.addActionListener(e -> frameUser.dispose());

        frameUser.setVisible(true);
    }

    // Panel untuk Daftar User
    private JPanel createUserListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(750, 450));

        // Form Input
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(30, 20, 100, 25);
        panel.add(lblUsername);
        JTextField txtUsername = new JTextField();
        txtUsername.setBounds(130, 20, 150, 25);
        panel.add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(30, 55, 100, 25);
        panel.add(lblPassword);
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(130, 55, 150, 25);
        panel.add(txtPassword);

        JLabel lblNama = new JLabel("Nama Lengkap:");
        lblNama.setBounds(30, 90, 100, 25);
        panel.add(lblNama);
        JTextField txtNamaUser = new JTextField();
        txtNamaUser.setBounds(130, 90, 150, 25);
        panel.add(txtNamaUser);

        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(30, 125, 100, 25);
        panel.add(lblRole);
        String[] roles = {"KARYAWAN", "OWNER"};
        JComboBox<String> comboRole = new JComboBox<>(roles);
        comboRole.setBounds(130, 125, 150, 25);
        panel.add(comboRole);

        JButton btnTambah = new JButton("Tambah");
        btnTambah.setBounds(300, 20, 100, 30);
        btnTambah.setBackground(Color.GREEN);
        panel.add(btnTambah);

        JButton btnHapusUser = new JButton("Hapus");
        btnHapusUser.setBounds(300, 60, 100, 30);
        btnHapusUser.setBackground(Color.RED);
        btnHapusUser.setForeground(Color.WHITE);
        panel.add(btnHapusUser);

        // Tabel User
        String[] colUser = {"Username", "Nama Lengkap", "Role"};
        DefaultTableModel modelUser = new DefaultTableModel(colUser, 0);
        JTable tabelUser = new JTable(modelUser);
        JScrollPane scrollUser = new JScrollPane(tabelUser);
        scrollUser.setBounds(30, 170, 680, 230);
        panel.add(scrollUser);

        // Load data user
        Runnable loadUsers = () -> {
            modelUser.setRowCount(0);
            for (User u : UserSession.getAllUsers()) {
                modelUser.addRow(new Object[]{u.getUsername(), u.getNamaLengkap(), u.getRole()});
            }
        };
        loadUsers.run();

        btnTambah.addActionListener(e -> {
            if (txtUsername.getText().isEmpty() || txtPassword.getPassword().length == 0 || txtNamaUser.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Lengkapi semua field!");
                return;
            }
            UserSession.daftarBaru(
                txtUsername.getText(),
                new String(txtPassword.getPassword()),
                comboRole.getSelectedItem().toString(),
                txtNamaUser.getText()
            );
            JOptionPane.showMessageDialog(null, "User berhasil ditambahkan!");
            txtUsername.setText("");
            txtPassword.setText("");
            txtNamaUser.setText("");
            loadUsers.run();
        });

        btnHapusUser.addActionListener(e -> {
            int row = tabelUser.getSelectedRow();
            if (row >= 0) {
                String username = modelUser.getValueAt(row, 0).toString();
                if (username.equals(UserSession.getUsername())) {
                    JOptionPane.showMessageDialog(null, "Tidak bisa hapus user yang sedang login!");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(null, "Hapus user " + username + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    UserSession.deleteUser(username);
                    JOptionPane.showMessageDialog(null, "User berhasil dihapus!");
                    loadUsers.run();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih user yang akan dihapus!");
            }
        });

        return panel;
    }

    // Panel untuk Request Access List
    private JPanel createRequestListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(750, 450));

        JLabel lblInfo = new JLabel("Daftar Request Access dari Calon User");
        lblInfo.setBounds(10, 10, 400, 25);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblInfo);

        // Tabel Request
        String[] colRequest = {"ID", "Username", "Nama Lengkap", "Alasan", "Tanggal", "Status"};
        DefaultTableModel modelRequest = new DefaultTableModel(colRequest, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tabelRequest = new JTable(modelRequest);
        tabelRequest.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelRequest.getColumnModel().getColumn(0).setPreferredWidth(30);
        tabelRequest.getColumnModel().getColumn(3).setPreferredWidth(200);
        JScrollPane scrollRequest = new JScrollPane(tabelRequest);
        scrollRequest.setBounds(10, 45, 700, 200);
        panel.add(scrollRequest);

        // Detail Request
        JLabel lblDetail = new JLabel("Detail Request:");
        lblDetail.setBounds(10, 255, 150, 25);
        lblDetail.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblDetail);

        JTextArea txtDetail = new JTextArea();
        txtDetail.setEditable(false);
        txtDetail.setLineWrap(true);
        txtDetail.setWrapStyleWord(true);
        txtDetail.setFont(new Font("Arial", Font.PLAIN, 11));
        JScrollPane scrollDetail = new JScrollPane(txtDetail);
        scrollDetail.setBounds(10, 285, 530, 100);
        panel.add(scrollDetail);

        JButton btnApprove = new JButton("✓ Approve");
        btnApprove.setBounds(560, 285, 130, 40);
        btnApprove.setBackground(new Color(40, 167, 69));
        btnApprove.setForeground(Color.WHITE);
        btnApprove.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(btnApprove);

        JButton btnDecline = new JButton("✗ Decline");
        btnDecline.setBounds(560, 335, 130, 40);
        btnDecline.setBackground(new Color(220, 53, 69));
        btnDecline.setForeground(Color.WHITE);
        btnDecline.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(btnDecline);

        // Load data request
        Runnable loadRequests = () -> {
            modelRequest.setRowCount(0);
            List<AccessRequest> allRequests = accessRequestDAO.getAllAccessRequests();
            for (AccessRequest ar : allRequests) {
                modelRequest.addRow(new Object[]{
                    ar.getId(),
                    ar.getUsername(),
                    ar.getNamaLengkap(),
                    ar.getAlasan().length() > 50 ? ar.getAlasan().substring(0, 47) + "..." : ar.getAlasan(),
                    ar.getTanggalRequestFormatted(),
                    ar.getStatus()
                });
            }
        };
        loadRequests.run();

        // Klik tabel untuk show detail
        tabelRequest.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabelRequest.getSelectedRow();
                if (row >= 0) {
                    int id = (Integer) modelRequest.getValueAt(row, 0);
                    List<AccessRequest> allRequests = accessRequestDAO.getAllAccessRequests();
                    AccessRequest request = allRequests.stream()
                        .filter(r -> r.getId() == id)
                        .findFirst()
                        .orElse(null);
                    
                    if (request != null) {
                        txtDetail.setText(
                            "Username: " + request.getUsername() + "\n" +
                            "Nama: " + request.getNamaLengkap() + "\n" +
                            "Tanggal: " + request.getTanggalRequestFormatted() + "\n" +
                            "Status: " + request.getStatus() + "\n\n" +
                            "Alasan:\n" + request.getAlasan()
                        );
                    }
                }
            }
        });

        btnApprove.addActionListener(e -> {
            int row = tabelRequest.getSelectedRow();
            if (row >= 0) {
                int id = (Integer) modelRequest.getValueAt(row, 0);
                List<AccessRequest> allRequests = accessRequestDAO.getAllAccessRequests();
                AccessRequest request = allRequests.stream()
                    .filter(r -> r.getId() == id)
                    .findFirst()
                    .orElse(null);
                
                if (request != null && request.getStatus().equals("PENDING")) {
                    int confirm = JOptionPane.showConfirmDialog(null, 
                        "Approve request dari " + request.getNamaLengkap() + "?", 
                        "Konfirmasi Approve", 
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Tambahkan user baru
                        UserSession.daftarBaru(
                            request.getUsername(),
                            request.getPassword(),
                            "KARYAWAN",
                            request.getNamaLengkap()
                        );
                        int currentUserId = userDAO.getUserId(UserSession.getUsername());
                        accessRequestDAO.updateAccessRequestStatus(id, "APPROVED", currentUserId);
                        JOptionPane.showMessageDialog(null, "Request approved! User berhasil ditambahkan.");
                        loadRequests.run();
                        txtDetail.setText("");
                    }
                } else if (request != null) {
                    JOptionPane.showMessageDialog(null, "Request ini sudah diproses!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih request yang akan di-approve!");
            }
        });

        btnDecline.addActionListener(e -> {
            int row = tabelRequest.getSelectedRow();
            if (row >= 0) {
                int id = (Integer) modelRequest.getValueAt(row, 0);
                List<AccessRequest> allRequests = accessRequestDAO.getAllAccessRequests();
                AccessRequest request = allRequests.stream()
                    .filter(r -> r.getId() == id)
                    .findFirst()
                    .orElse(null);
                
                if (request != null && request.getStatus().equals("PENDING")) {
                    int confirm = JOptionPane.showConfirmDialog(null, 
                        "Decline request dari " + request.getNamaLengkap() + "?", 
                        "Konfirmasi Decline", 
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        int currentUserId = userDAO.getUserId(UserSession.getUsername());
                        accessRequestDAO.updateAccessRequestStatus(id, "DECLINED", currentUserId);
                        JOptionPane.showMessageDialog(null, "Request declined.");
                        loadRequests.run();
                        txtDetail.setText("");
                    }
                } else if (request != null) {
                    JOptionPane.showMessageDialog(null, "Request ini sudah diproses!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih request yang akan di-decline!");
            }
        });

        return panel;
    }

    // Update combo layanan dengan data terbaru
    private void updateComboLayanan() {
        comboLayanan.removeAllItems();
        for (LayananItem item : LayananManager.getAllLayanan()) {
            comboLayanan.addItem(item);
        }
    }

    // Fitur Edit Layanan (Owner only)
    private void tampilkanEditLayanan() {
        JFrame frameLayanan = new JFrame("Edit Layanan");
        frameLayanan.setSize(700, 500);
        frameLayanan.setLayout(null);
        frameLayanan.setLocationRelativeTo(null);

        JLabel lblJudul = new JLabel("MANAJEMEN LAYANAN");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 20));
        lblJudul.setBounds(0, 20, 700, 30);
        lblJudul.setHorizontalAlignment(SwingConstants.CENTER);
        frameLayanan.add(lblJudul);

        // Form Input
        JLabel lblNamaLayanan = new JLabel("Nama Layanan:");
        lblNamaLayanan.setBounds(50, 70, 120, 25);
        frameLayanan.add(lblNamaLayanan);
        JTextField txtNamaLayanan = new JTextField();
        txtNamaLayanan.setBounds(170, 70, 180, 25);
        frameLayanan.add(txtNamaLayanan);

        JLabel lblHarga = new JLabel("Harga per Kg:");
        lblHarga.setBounds(50, 105, 120, 25);
        frameLayanan.add(lblHarga);
        JTextField txtHarga = new JTextField();
        txtHarga.setBounds(170, 105, 180, 25);
        frameLayanan.add(txtHarga);

        JLabel lblEstimasi = new JLabel("Estimasi (hari):");
        lblEstimasi.setBounds(50, 140, 120, 25);
        frameLayanan.add(lblEstimasi);
        JTextField txtEstimasi = new JTextField();
        txtEstimasi.setBounds(170, 140, 180, 25);
        frameLayanan.add(txtEstimasi);

        JButton btnTambah = new JButton("Tambah");
        btnTambah.setBounds(370, 70, 100, 30);
        btnTambah.setBackground(Color.GREEN);
        frameLayanan.add(btnTambah);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(370, 105, 100, 30);
        btnUpdate.setBackground(Color.YELLOW);
        frameLayanan.add(btnUpdate);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setBounds(370, 140, 100, 30);
        btnHapus.setBackground(Color.RED);
        btnHapus.setForeground(Color.WHITE);
        frameLayanan.add(btnHapus);

        // Tabel Layanan
        String[] colLayanan = {"Nama Layanan", "Harga per Kg", "Estimasi (hari)"};
        DefaultTableModel modelLayanan = new DefaultTableModel(colLayanan, 0);
        JTable tabelLayanan = new JTable(modelLayanan);
        JScrollPane scrollLayanan = new JScrollPane(tabelLayanan);
        scrollLayanan.setBounds(50, 190, 600, 200);
        frameLayanan.add(scrollLayanan);

        JButton btnTutup = new JButton("Tutup");
        btnTutup.setBounds(550, 410, 100, 30);
        frameLayanan.add(btnTutup);

        // Load data layanan
        Runnable loadLayanan = () -> {
            modelLayanan.setRowCount(0);
            for (LayananItem item : LayananManager.getAllLayanan()) {
                modelLayanan.addRow(new Object[]{
                    item.getNama(),
                    "Rp " + item.getHargaPerKg(),
                    item.getEstimasiHari() + " hari"
                });
            }
        };
        loadLayanan.run();

        // Klik tabel untuk load data ke form
        tabelLayanan.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabelLayanan.getSelectedRow();
                if (row >= 0) {
                    String namaLayanan = modelLayanan.getValueAt(row, 0).toString();
                    LayananItem item = LayananManager.getLayananByNama(namaLayanan);
                    if (item != null) {
                        txtNamaLayanan.setText(item.getNama());
                        txtHarga.setText(String.valueOf(item.getHargaPerKg()));
                        txtEstimasi.setText(String.valueOf(item.getEstimasiHari()));
                    }
                }
            }
        });

        btnTambah.addActionListener(e -> {
            try {
                if (txtNamaLayanan.getText().isEmpty() || txtHarga.getText().isEmpty() || txtEstimasi.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Lengkapi semua field!");
                    return;
                }
                
                String nama = txtNamaLayanan.getText();
                int harga = Integer.parseInt(txtHarga.getText());
                int estimasi = Integer.parseInt(txtEstimasi.getText());
                
                // Cek duplikat nama
                if (LayananManager.getLayananByNama(nama) != null) {
                    JOptionPane.showMessageDialog(null, "Layanan dengan nama tersebut sudah ada!");
                    return;
                }
                
                LayananManager.addLayanan(nama, harga, estimasi);
                JOptionPane.showMessageDialog(null, "Layanan berhasil ditambahkan!");
                txtNamaLayanan.setText("");
                txtHarga.setText("");
                txtEstimasi.setText("");
                loadLayanan.run();
                updateComboLayanan();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Harga dan estimasi harus berupa angka!");
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                int row = tabelLayanan.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(null, "Pilih layanan yang akan diupdate!");
                    return;
                }
                
                if (txtNamaLayanan.getText().isEmpty() || txtHarga.getText().isEmpty() || txtEstimasi.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Lengkapi semua field!");
                    return;
                }
                
                String namaLama = modelLayanan.getValueAt(row, 0).toString();
                String namaBaru = txtNamaLayanan.getText();
                int harga = Integer.parseInt(txtHarga.getText());
                int estimasi = Integer.parseInt(txtEstimasi.getText());
                
                // Jika nama berubah, cek duplikat
                if (!namaLama.equals(namaBaru) && LayananManager.getLayananByNama(namaBaru) != null) {
                    JOptionPane.showMessageDialog(null, "Layanan dengan nama tersebut sudah ada!");
                    return;
                }
                
                LayananManager.updateLayanan(namaLama, namaBaru, harga, estimasi);
                JOptionPane.showMessageDialog(null, "Layanan berhasil diupdate!");
                txtNamaLayanan.setText("");
                txtHarga.setText("");
                txtEstimasi.setText("");
                loadLayanan.run();
                updateComboLayanan();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Harga dan estimasi harus berupa angka!");
            }
        });

        btnHapus.addActionListener(e -> {
            int row = tabelLayanan.getSelectedRow();
            if (row >= 0) {
                String namaLayanan = modelLayanan.getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(null, "Hapus layanan " + namaLayanan + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    LayananManager.deleteLayanan(namaLayanan);
                    JOptionPane.showMessageDialog(null, "Layanan berhasil dihapus!");
                    txtNamaLayanan.setText("");
                    txtHarga.setText("");
                    txtEstimasi.setText("");
                    loadLayanan.run();
                    updateComboLayanan();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih layanan yang akan dihapus!");
            }
        });

        btnTutup.addActionListener(e -> frameLayanan.dispose());

        frameLayanan.setVisible(true);
    }
}
