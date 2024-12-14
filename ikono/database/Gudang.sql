CREATE TABLE gudang (
    id_gudang INT AUTO_INCREMENT PRIMARY KEY,  -- ID gudang, bersifat unik
    nama_gudang VARCHAR(255) NOT NULL,          -- Nama gudang
    lokasi TEXT NOT NULL,                       -- Lokasi gudang
    kota VARCHAR(100),                          -- Kota lokasi gudang
    kapasitas INT NOT NULL,                     -- Kapasitas maksimum gudang
    jumlah_barang INT DEFAULT 0,                -- Jumlah barang saat ini di gudang
    no_telepon VARCHAR(15),                     -- Nomor telepon gudang
      email VARCHAR(100),                         -- Email gudang
    status ENUM('Aktif', 'Non-Aktif') DEFAULT 'Aktif', -- Status gudang
    tanggal_dibuat TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Tanggal pembuatan data gudang
);

INSERT INTO gudang (nama_gudang, lokasi, kota, kapasitas, jumlah_barang, no_telepon, email)
VALUES 
('Gudang Utama', 'Jl. Industri No. 1, Jakarta', 'Jakarta', 1000, 500, '08111111111', 'gudangutama@example.com'),
('Gudang Barat', 'Jl. Pertanian No. 12, Bandung', 'Bandung', 800, 200, '08222222222', 'gudangbarat@example.com'),
('Gudang Timur', 'Jl. Perdagangan No. 7, Surabaya', 'Surabaya', 1200, 700, '08333333333', 'gudangtimur@example.com');