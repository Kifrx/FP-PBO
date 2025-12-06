import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class AplikasiLaundry extends JFrame {

    JTextField textNama, textNoHP, textBerat;
    JComboBox<String> comboLayanan;
    JLabel labelTotalHarga, labelTotalPendapatan, labelHeaderGambar;
    JTable tabelData;
    DefaultTableModel modelTabel;

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

        JLabel lblReg = new JLabel("Belum punya akun? Daftar disini");
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
                tampilkanLayarRegistrasi();
            }
        });

        frameLogin.setVisible(true);
    }

    //Registrasi view
    private void tampilkanLayarRegistrasi() {
        JFrame frameReg = new JFrame("Daftar Akun");
        frameReg.setSize(600, 400);
        frameReg.setLayout(null);
        frameReg.setLocationRelativeTo(null);
        frameReg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel lblJudul = new JLabel("DAFTAR BARU");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 24));
        lblJudul.setBounds(0, 40, 600, 40);
        lblJudul.setHorizontalAlignment(SwingConstants.CENTER);
        frameReg.add(lblJudul);

        JTextField txtUser = new JTextField(); 
        txtUser.setBounds(200, 110, 200, 30);
        frameReg.add(txtUser);
        
        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(200, 160, 200, 30);
        frameReg.add(txtPass);

        JButton btnDaftar = new JButton("SIMPAN");
        btnDaftar.setBounds(200, 220, 200, 40);
        frameReg.add(btnDaftar);

        btnDaftar.addActionListener(e -> {
            // Update data
            UserSession.daftarBaru(txtUser.getText(), new String(txtPass.getPassword()));
            JOptionPane.showMessageDialog(null, "Akun dibuat! Silakan Login.");
            frameReg.dispose();
            tampilkanLayarLogin();
        });
        
        frameReg.setVisible(true);
    }

    //Dashboard view
    private void tampilkanDashboard() {
        setTitle("Sistem Laundry - User: " + UserSession.getUsername());
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Header Gambar
        labelHeaderGambar = new JLabel("[ AREA GAMBAR LOGO ]");
        labelHeaderGambar.setOpaque(true);
        labelHeaderGambar.setBackground(Color.DARK_GRAY);
        labelHeaderGambar.setForeground(Color.WHITE);
        labelHeaderGambar.setHorizontalAlignment(SwingConstants.CENTER);
        labelHeaderGambar.setBounds(0, 0, 900, 150);
        
        // Uncomment ini ya gesss untuk gambar, nantik masukin gambarnya:
        // labelHeaderGambar.setIcon(new ImageIcon("header.jpg"));
        add(labelHeaderGambar);

        int y = 170;

        // Input Form
        addLabel("Nama:", 50, y);
        textNama = addInput(150, y, 250);
        
        addLabel("No HP:", 50, y+40);
        textNoHP = addInput(150, y+40, 250);
        
        addLabel("Layanan:", 450, y);
        String[] menu = {"Cuci + Setrika", "Setrika Saja", "Cuci Kering"};
        comboLayanan = new JComboBox<>(menu);
        comboLayanan.setBounds(550, y, 200, 25);
        add(comboLayanan);
        
        addLabel("Berat (kg):", 450, y+40);
        textBerat = addInput(550, y+40, 100);

        // Tombol Hitung
        JButton btnHitung = new JButton("Hitung");
        btnHitung.setBounds(550, y+80, 100, 25);
        add(btnHitung);

        labelTotalHarga = new JLabel("Rp 0");
        labelTotalHarga.setBounds(660, y+80, 200, 25);
        labelTotalHarga.setForeground(Color.BLUE);
        labelTotalHarga.setFont(new Font("Arial", Font.BOLD, 14));
        add(labelTotalHarga);

        // Tombol CRUD
        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(50, y+130, 100, 35);
        btnSimpan.setBackground(Color.GREEN);
        add(btnSimpan);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(160, y+130, 100, 35);
        btnUpdate.setBackground(Color.YELLOW);
        add(btnUpdate);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setBounds(270, y+130, 100, 35);
        btnHapus.setBackground(Color.RED);
        btnHapus.setForeground(Color.WHITE);
        add(btnHapus);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(750, y+130, 100, 35);
        add(btnLogout);

        // Tabel
        String[] col = {"Nama", "HP", "Layanan", "Berat", "Total"};
        modelTabel = new DefaultTableModel(col, 0);
        tabelData = new JTable(modelTabel);
        JScrollPane scroll = new JScrollPane(tabelData);
        scroll.setBounds(50, y+180, 800, 200);
        add(scroll);

        // Total Pendapatan
        labelTotalPendapatan = new JLabel("Total Pendapatan: Rp 0");
        labelTotalPendapatan.setBounds(50, y+400, 400, 30);
        labelTotalPendapatan.setFont(new Font("Arial", Font.BOLD, 18));
        add(labelTotalPendapatan);

        btnHitung.addActionListener(e -> {
            try {
                int berat = Integer.parseInt(textBerat.getText());
                String pilihan = comboLayanan.getSelectedItem().toString();
                

                Layanan layananObj = null;

                if (pilihan.equals("Cuci + Setrika")) {
                    layananObj = new CuciKomplit();
                } else if (pilihan.equals("Setrika Saja")) {
                    layananObj = new SetrikaSaja();
                } else if (pilihan.equals("Cuci Kering")) {
                    layananObj = new CuciKering();
                }
                
                int hasil = layananObj.hitungHarga(berat);
                
                labelTotalHarga.setText("Rp " + hasil);
                labelTotalHarga.putClientProperty("nilai", hasil);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Berat harus angka!");
            }
        });

        // Logika Simpan
        btnSimpan.addActionListener(e -> {
            if (validasi()) {
                modelTabel.addRow(new Object[]{
                    textNama.getText(), textNoHP.getText(), comboLayanan.getSelectedItem(),
                    textBerat.getText(), labelTotalHarga.getClientProperty("nilai")
                });
                updateTotal();
                reset();
            }
        });
        
        // Logika Update
        btnUpdate.addActionListener(e -> {
            int row = tabelData.getSelectedRow();
            if (row >= 0 && validasi()) {
            
                int berat = Integer.parseInt(textBerat.getText());
                String pilihan = comboLayanan.getSelectedItem().toString();
                Layanan layananObj = null;
                if (pilihan.equals("Cuci + Setrika")) layananObj = new CuciKomplit();
                else if (pilihan.equals("Setrika Saja")) layananObj = new SetrikaSaja();
                else layananObj = new CuciKering();
                
                int hasil = layananObj.hitungHarga(berat);

                modelTabel.setValueAt(textNama.getText(), row, 0);
                modelTabel.setValueAt(textNoHP.getText(), row, 1);
                modelTabel.setValueAt(comboLayanan.getSelectedItem(), row, 2);
                modelTabel.setValueAt(textBerat.getText(), row, 3);
                modelTabel.setValueAt(hasil, row, 4);
                updateTotal();
                reset();
            }
        });

        // Logika Hapus
        btnHapus.addActionListener(e -> {
            int row = tabelData.getSelectedRow();
            if (row >= 0) {
                modelTabel.removeRow(row);
                updateTotal();
            }
        });
        
        // Logika Klik Tabel
        tabelData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabelData.getSelectedRow();
                if(row >=0) {
                    textNama.setText(modelTabel.getValueAt(row, 0).toString());
                    textNoHP.setText(modelTabel.getValueAt(row, 1).toString());
                    comboLayanan.setSelectedItem(modelTabel.getValueAt(row, 2).toString());
                    textBerat.setText(modelTabel.getValueAt(row, 3).toString());
                    labelTotalHarga.setText("Rp " + modelTabel.getValueAt(row, 4));
                    labelTotalHarga.putClientProperty("nilai", modelTabel.getValueAt(row, 4));
                }
            }
        });
        
        btnLogout.addActionListener(e -> {
            dispose();
            tampilkanLayarLogin();
        });

        setVisible(true);
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
        int total = 0;
        for (int i = 0; i < modelTabel.getRowCount(); i++) total += Integer.parseInt(modelTabel.getValueAt(i, 4).toString());
        labelTotalPendapatan.setText("Total Pendapatan: Rp " + total);
    }
    private void reset() {
        textNama.setText(""); textNoHP.setText(""); textBerat.setText("");
        labelTotalHarga.setText("Rp 0");
        labelTotalHarga.putClientProperty("nilai", null);
    }
    private boolean validasi() {
        if(textNama.getText().isEmpty() || labelTotalHarga.getClientProperty("nilai") == null) {
            JOptionPane.showMessageDialog(null, "Lengkapi data!"); return false;
        } return true;
    }
}
