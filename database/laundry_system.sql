-- ================================================
-- DATABASE SISTEM LAUNDRY
-- ================================================

-- Drop database jika sudah ada (hati-hati, data akan hilang!)
DROP DATABASE IF EXISTS laundry_system;

-- Buat database baru
CREATE DATABASE laundry_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Gunakan database
USE laundry_system;

-- ================================================
-- TABEL USERS (Manajemen User & Authentication)
-- ================================================
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('OWNER', 'KARYAWAN') NOT NULL,
    nama_lengkap VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABEL LAYANAN (Master Data Layanan Laundry)
-- ================================================
CREATE TABLE layanan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nama_layanan VARCHAR(100) UNIQUE NOT NULL,
    harga_per_kg INT NOT NULL,
    estimasi_hari INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nama_layanan (nama_layanan),
    INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABEL TRANSAKSI (Data Transaksi Laundry)
-- ================================================
CREATE TABLE transaksi (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nama_pelanggan VARCHAR(100) NOT NULL,
    no_hp VARCHAR(20) NOT NULL,
    layanan_id INT NOT NULL,
    nama_layanan VARCHAR(100) NOT NULL,
    berat INT NOT NULL,
    total_harga INT NOT NULL,
    status ENUM('BELUM', 'PROSES', 'DONE') DEFAULT 'BELUM',
    tanggal_masuk DATETIME NOT NULL,
    estimasi_selesai DATE NOT NULL,
    tanggal_selesai DATETIME NULL,
    user_id INT NOT NULL,
    nama_petugas VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (layanan_id) REFERENCES layanan(id) ON DELETE RESTRICT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    INDEX idx_status (status),
    INDEX idx_tanggal_masuk (tanggal_masuk),
    INDEX idx_pelanggan (nama_pelanggan),
    INDEX idx_no_hp (no_hp)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABEL ACCESS_REQUESTS (Request Access)
-- ================================================
CREATE TABLE access_requests (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nama_lengkap VARCHAR(100) NOT NULL,
    alasan TEXT NOT NULL,
    tanggal_request DATETIME NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'DECLINED') DEFAULT 'PENDING',
    approved_by INT NULL,
    approved_at DATETIME NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (approved_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_status (status),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- INSERT DATA DEFAULT
-- ================================================

-- Insert Default Users
INSERT INTO users (username, password, role, nama_lengkap) VALUES
('owner', 'owner123', 'OWNER', 'Admin Owner'),
('karyawan1', 'karyawan123', 'KARYAWAN', 'Karyawan Satu');

-- Insert Default Layanan
INSERT INTO layanan (nama_layanan, harga_per_kg, estimasi_hari, is_active) VALUES
('Cuci + Setrika', 6000, 2, TRUE),
('Setrika Saja', 4000, 1, TRUE),
('Cuci Kering', 3000, 1, TRUE);

-- ================================================
-- SELESAI
-- ================================================
SELECT 'Database laundry_system berhasil dibuat!' AS Status;
