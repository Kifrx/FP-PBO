# Aplikasi Laundry - Final Project PBO

Aplikasi desktop untuk manajemen laundry dengan fitur lengkap menggunakan Java Swing dan MySQL database.

## Fitur Utama

### Untuk Semua User
- Input transaksi laundry (nama, HP, layanan, berat)
- Update data transaksi
- Hapus transaksi
- Lihat daftar transaksi dengan detail lengkap
- Tracking status laundry (BELUM, PROSES, DONE)
- Estimasi selesai otomatis berdasarkan jenis layanan
- Perhitungan harga otomatis

### Untuk Owner 
- Laporan Keuangan: Filter harian, bulanan, atau semua periode
- Kelola User: Tambah, edit, hapus user (owner/karyawan)
- Approve Request Access: Sistem persetujuan untuk user baru
- Edit Layanan: Tambah, edit, hapus layanan laundry
- Total Pendapatan: Tracking pendapatan real-time

##  Prasyarat

1. **Java Development Kit (JDK) 21**
   - Download: https://adoptium.net/

2. **XAMPP** (untuk MySQL)
   - Download: https://www.apachefriends.org/
   - Port MySQL: 3306
   - Username: `root`
   - Password: (kosong)

3. **MySQL JDBC Driver**

## Setup & Instalasi

### 1. Clone atau Download Project

```bash
git clone
cd FP-PBO
```

### 2. Download JDBC Driver

**Opsi A: Otomatis dengan PowerShell**
```powershell
cd lib
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar" -OutFile "mysql-connector-j-8.3.0.jar"
```

**Opsi B: Manual Download**
- Kunjungi: https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar
- Simpan ke folder `lib/`

### 3. Setup Database

1. Start MySQL dari XAMPP Control Panel
2. Buka MySQL admin
3. Klik tab **SQL**
4. Copy-paste semua isi file `database/laundry_system.sql`
5. Klik **Go**

Database `laundry_system` akan terbuat dengan data default:
- Owner: `owner` / `owner123`
- Karyawan: `karyawan1` / `karyawan123`

### 4. Compile & Run

**Menggunakan PowerShell Script**
```powershell
.\run.ps1
```

**Atau Manual**
```powershell
# Compile
javac -cp "lib\mysql-connector-j-8.3.0.jar" -d bin src\*.java

# Run
java -cp "bin;lib\mysql-connector-j-8.3.0.jar" AplikasiLaundry
```

**Menggunakan VS Code**
1. Install extension: **Extension Pack for Java**
2. Open folder `FP-PBO`
3. Klik kanan `AplikasiLaundry.java` → **Run Java**

## Struktur Project

```
FP-PBO/
├── .vscode/
│   └── settings.json          # Konfigurasi VS Code
├── database/
│   └── laundry_system.sql     # Database schema + data default
├── lib/
│   └── mysql-connector-j-8.3.0.jar  # JDBC Driver 
├── src/
│   ├── AplikasiLaundry.java   # Main GUI application
│   ├── DatabaseConnection.java # Singleton connection
│   ├── UserDAO.java           # User data access
│   ├── LayananDAO.java        # Layanan data access
│   ├── TransaksiDAO.java      # Transaksi data access
│   ├── AccessRequestDAO.java  # Request access data access
│   ├── User.java              # User entity
│   ├── UserSession.java       # Session management
│   ├── LayananItem.java       # Layanan entity
│   ├── LayananManager.java    # Layanan business logic
│   ├── Transaksi.java         # Transaksi entity
│   ├── AccessRequest.java     # Access request entity
│   ├── Layanan.java           # Abstract layanan
│   ├── CuciKering.java        # Layanan cuci kering
│   ├── CuciKomplit.java       # Layanan cuci komplit
│   └── SetrikaSaja.java       # Layanan setrika
├── compile.ps1                # Script compile
├── run.ps1                    # Script compile & run
└── README.md                  # File ini
```

##  Database Schema

### Tabel `users`
```sql
user_id (PK) | username (UNIQUE) | password | role | nama_lengkap | created_at
```

### Tabel `layanan`
```sql
layanan_id (PK) | nama_layanan (UNIQUE) | harga_per_kg | estimasi_hari | is_active
```

### Tabel `transaksi`
```sql
transaksi_id (PK) | nama_pelanggan | no_hp | layanan_id (FK) | berat_kg | 
total_harga | status | tanggal_masuk | estimasi_selesai | user_id (FK)
```

### Tabel `access_requests`
```sql
request_id (PK) | username | password | nama_lengkap | alasan | 
tanggal_request | status
```

##  User Default

### Owner (Full Access)
- Username: `owner`
- Password: `owner123`
- Akses: Semua fitur

### Karyawan (Limited Access)
- Username: `karyawan1`
- Password: `karyawan123`
- Akses: Input, update, delete transaksi saja

##  Cara Penggunaan

### Login
1. Jalankan aplikasi
2. Masukkan username dan password
3. Klik **Login**
4. Atau klik **Request Access** untuk mendaftar (perlu approval owner)

### Input Transaksi Baru
1. Isi **Nama Pelanggan** dan **No HP**
2. Pilih **Layanan** dari dropdown
3. Masukkan **Berat (kg)**
4. Pilih **Status** (BELUM/PROSES/DONE)
5. Klik **Hitung** untuk lihat total harga dan estimasi
6. Klik **Simpan**

### Update Transaksi
1. Klik row di tabel yang ingin diupdate
2. Data akan muncul di form input
3. Edit data yang diperlukan
4. Klik **Update**

### Hapus Transaksi
1. Klik row di tabel yang ingin dihapus
2. Klik **Hapus**
3. Konfirmasi penghapusan

### Laporan Keuangan (Owner)
1. Klik **Laporan**
2. Pilih periode: Hari Ini / Bulan Ini / Semua
3. Klik **Tampilkan**
4. Lihat total pendapatan dan jumlah transaksi

### Kelola User (Owner)
1. Klik **Kelola User**
2. Tab **Daftar User**: CRUD user
3. Tab **Request Access**: Approve/decline request dari calon user

### Edit Layanan (Owner)
1. Klik **Edit Layanan**
2. Lihat daftar layanan saat ini
3. Klik **Tambah** untuk layanan baru
4. Pilih layanan → **Edit** atau **Hapus**
