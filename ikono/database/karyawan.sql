use karyawan;

create table master_data_karyawan (
ID_Karyawan VARCHAR(10) PRIMARY KEY,
    Nama VARCHAR(100) NOT NULL,
    Tanggal_Lahir DATE NOT NULL,
    Jenis_Kelamin ENUM('Laki-laki', 'Perempuan') NOT NULL,
    Jabatan VARCHAR(50) NOT NULL,
    Tanggal_Masuk DATE NOT NULL,
    Gaji DECIMAL(15, 2) NOT NULL
);
select * from master_data_karyawan;
insert into master_data_karyawan(ID_Karyawan, Nama, Tanggal_Lahir, Jenis_Kelamin, Jabatan, Tanggal_Masuk, Gaji)
VALUES ('1', 'faiz ekahadi', '2006-01-11-', 'laki-laki', 'bos gede', '2015-06-01', 10000);
insert into master_data_karyawan(ID_Karyawan, Nama, Tanggal_Lahir, Jenis_Kelamin, Jabatan, Tanggal_Masuk, Gaji)
VALUES ('2', 'Marcel', '2006-01-11-', 'laki-laki', 'HRD', '2014-06-01', 20000);