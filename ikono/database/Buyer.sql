CREATE TABLE pembeli (
    id_pembeli INT AUTO_INCREMENT PRIMARY KEY,  -- ID pembeli, bersifat unik
    nama_lengkap VARCHAR(255) NOT NULL,          -- Nama lengkap pembeli
    alamat TEXT NOT NULL,                       -- Alamat pembeli
    kota VARCHAR(100),                          -- Kota pembeli
    kode_pos VARCHAR(10),                       -- Kode Pos
    no_telepon VARCHAR(15),                     -- Nomor telepon pembeli
    email VARCHAR(100),                         -- Email pembeli
    jenis_kelamin ENUM('L', 'P'),                -- Jenis kelamin (L: Laki-laki, P: Perempuan)
    tanggal_daftar TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Tanggal pendaftaran pembeli
    status ENUM('Aktif', 'Non-Aktif') DEFAULT 'Aktif'  -- Status pembeli
);

INSERT INTO pembeli (nama_lengkap, alamat, kota, kode_pos, no_telepon, email, jenis_kelamin)
VALUES 
('John Doe', 'Jl. Raya No. 10, Jakarta', 'Jakarta', '12345', '08123456789', 'johndoe@example.com', 'L'),
('Jane Smith', 'Jl. Melati No. 5, Bandung', 'Bandung', '54321', '08234567890', 'janesmith@example.com', 'P'),
('Michael Johnson', 'Jl. Anggrek No. 7, Surabaya', 'Surabaya', '67890', '08345678901', 'michaeljohnson@example.com', 'L');
    