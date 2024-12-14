use dokter;

CREATE TABLE doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    specialization_id INT,
    email VARCHAR(100),
    phone_number VARCHAR(15),
    address TEXT,
    hire_date DATE,
    FOREIGN KEY (specialization_id) REFERENCES specializations(specialization_id)
);

INSERT INTO doctors (first_name, last_name, specialization_id, email, phone_number, address, hire_date)
VALUES 
('Dr. Andi', 'Santoso', 1, 'andi.santoso@example.com', '1234567890', '123 Main St, Jakarta', '2020-01-01'),
('Dr. Siti', 'Rahayu', 2, 'siti.rahayu@example.com', '0987654321', '456 Elm St, Surabaya', '2019-03-15'),
('Dr. Dewi', 'Pramesti', 3, 'dewi.pramesti@example.com', '1122334455', '789 Pine St, Bandung', '2021-06-10'),
('Dr. Budi', 'Haryanto', 4, 'budi.haryanto@example.com', '5566778899', '321 Oak St, Bali', '2018-08-20'),
('Dr. Maya', 'Putri', 5, 'maya.putri@example.com', '6677889900', '654 Maple St, Yogyakarta', '2022-11-11');

select*from doctors;

CREATE TABLE specializations (
    specialization_id INT AUTO_INCREMENT PRIMARY KEY,
    specialization_name VARCHAR(100) NOT NULL
);
INSERT INTO specializations (specialization_name) VALUES ('Dokter Umum');
INSERT INTO specializations (specialization_name) VALUES ('Dokter Gigi');
INSERT INTO specializations (specialization_name) VALUES ('Spesialis Anak');
INSERT INTO specializations (specialization_name) VALUES ('Spesialis Bedah');
INSERT INTO specializations (specialization_name) VALUES ('Dokter Kulit');

SELECT 
    doctors.first_name,
    doctors.last_name,
    specializations.specialization_name
FROM 
    doctors
JOIN 
    specializations ON doctors.specialization_id = specializations.specialization_id;
    
CREATE TABLE jadwal_praktek (
    id_jadwal INT AUTO_INCREMENT PRIMARY KEY,
    id_dokter INT,
    hari_praktek VARCHAR(20) NOT NULL,
    jam_mulai TIME NOT NULL,
    jam_selesai TIME NOT NULL,
    keterangan VARCHAR(255),
    lokasi_praktek VARCHAR(255), 
    FOREIGN KEY (id_dokter) REFERENCES dokter(id_dokter)
);
    







