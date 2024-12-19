USE rumah_sakit;
CREATE TABLE poli (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    deskripsi TEXT
);

-- Tabel Dokter
CREATE TABLE dokter (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    spesialisasi VARCHAR(100),
    poli_id INT,
    FOREIGN KEY (poli_id) REFERENCES poli(id) ON DELETE CASCADE
);

-- Tabel Suster
CREATE TABLE suster (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    bagian VARCHAR(100),
    poli_id INT,
    FOREIGN KEY (poli_id) REFERENCES poli(id) ON DELETE CASCADE
);

-- Tambahkan data Poli
INSERT INTO poli (nama, deskripsi) VALUES 
('Poli Umum', 'Melayani konsultasi kesehatan umum'),
('Poli Gigi', 'Melayani perawatan gigi dan mulut');

-- Tambahkan data Dokter
INSERT INTO dokter (nama, spesialisasi, poli_id) VALUES
('Dr. Andi', 'Umum', 1),
('Dr. Budi', 'Gigi', 2);

-- Tambahkan data Suster
INSERT INTO suster (nama, bagian, poli_id) VALUES
('Suster Citra', 'Kesehatan Umum', 1),
('Suster Dini', 'Perawatan Gigi', 2);

select * from rumah_sakit;
