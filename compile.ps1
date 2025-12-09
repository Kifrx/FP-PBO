# Script untuk hanya compile (tanpa run)
# File: compile.ps1

Write-Host "=== Kompilasi Aplikasi Laundry ===" -ForegroundColor Cyan

# Cek apakah folder lib dan JDBC driver ada
if (-not (Test-Path "lib\mysql-connector-j-8.3.0.jar")) {
    Write-Host "ERROR: JDBC Driver tidak ditemukan!" -ForegroundColor Red
    Write-Host "Silakan download mysql-connector-j-8.3.0.jar ke folder lib\" -ForegroundColor Yellow
    Write-Host "Lihat lib\README.md untuk instruksi download" -ForegroundColor Yellow
    exit 1
}

# Buat folder bin jika belum ada
if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
    Write-Host "Folder bin dibuat" -ForegroundColor Green
}

# Hapus file class lama
if (Test-Path "bin\*.class") {
    Remove-Item "bin\*.class"
    Write-Host "File class lama dihapus" -ForegroundColor Yellow
}

# Compile semua file Java
Write-Host "Compiling Java files..." -ForegroundColor Yellow
javac -cp "lib\mysql-connector-j-8.3.0.jar" -d bin src\*.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "Kompilasi berhasil!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Untuk menjalankan aplikasi, gunakan:" -ForegroundColor Cyan
    Write-Host "  .\run.ps1" -ForegroundColor White
    Write-Host "atau:" -ForegroundColor Cyan
    Write-Host '  java -cp "bin;lib\mysql-connector-j-8.3.0.jar" AplikasiLaundry' -ForegroundColor White
} else {
    Write-Host "Kompilasi gagal! Cek error di atas." -ForegroundColor Red
    exit 1
}
