

create database pasien;

use pasien;

create table data_pasien(
id_pasien int AUTO_INCREMENT Primary Key,
nama_lengkap varchar(50),
tanggal_lahir date not null,
jenis_kelamin ENUM('Laki-Laki', 'Perempuan') not null,
alamat text not null,
nomor_telepon varchar(50),
email varchar(100),
golongan_darah ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'),
status_pernikahan ENUM('Belum nikah', 'Sudah Menikah', 'Cerai'),
riwayat_penyakit text,
alergi text,
tanggal_registrasi DATE NOT NULL
);

