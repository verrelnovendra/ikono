SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE TABLE `asuransi` (
  `id_asuransi` varchar(10) NOT NULL,
  `nama_asuransi` varchar(250) NOT NULL,
  `deskripsi` text NOT NULL,
  `harga` decimal(15,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `asuransi` (`id_asuransi`, `nama_asuransi`, `deskripsi`, `harga`) VALUES
('B154', 'BPJS Ketenagakerjaan', 'Jaminan Kecelakaan Kerja (JKK), Jaminan Kematian (JKM), Jaminan Hari Tua (JHT), Jaminan Pensiun (JP) , Jaminan Kehilangan Pekerjaan (JKP)', 350000.00),
('JH233', 'Jasa Raharja', 'Asuransi Kecelakaan', 540000.00),
('JS563', 'Jiwasraya', 'Asuransi Jiwa', 150000.00);

CREATE TABLE `asuransi_aktif` (
  `id_premi` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_asuransi` varchar(10) NOT NULL,
  `id_karyawan` varchar(10) NOT NULL,
  `siklus` enum('bulanan','tahunan') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `asuransi_aktif` (`id_premi`, `id_asuransi`, `id_karyawan`, `siklus`) VALUES
('P1734229813093', 'B154', 'A01', 'bulanan'),
('P1734229822490', 'JH233', 'A03', 'bulanan');

CREATE TABLE `karyawan` (
  `id_karyawan` varchar(10) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `tanggal_lahir` date NOT NULL,
  `jenis_kelamin` enum('laki-laki','perempuan') NOT NULL,
  `jabatan` varchar(50) NOT NULL,
  `tanggal_masuk` date NOT NULL,
  `gaji` decimal(15,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `karyawan` (`id_karyawan`, `nama`, `tanggal_lahir`, `jenis_kelamin`, `jabatan`, `tanggal_masuk`, `gaji`) VALUES
('A01', 'Asep Kurniawan', '1999-06-28', 'laki-laki', 'CEO', '2011-02-12', 500000000.00),
('A02', 'Prima Putri', '2000-03-15', 'perempuan', 'CTO', '2011-02-12', 450000000.00),
('A03', 'Sukarma Jaya', '1976-06-03', 'laki-laki', 'Driver', '2012-05-01', 3500000.00);

ALTER TABLE `asuransi`
  ADD PRIMARY KEY (`id_asuransi`);

ALTER TABLE `asuransi_aktif`
  ADD PRIMARY KEY (`id_premi`),
  ADD KEY `id_asuransi` (`id_asuransi`),
  ADD KEY `id_karyawan` (`id_karyawan`);

ALTER TABLE `karyawan`
  ADD PRIMARY KEY (`id_karyawan`);

ALTER TABLE `asuransi_aktif`
  ADD CONSTRAINT `asuransi_aktif_ibfk_1` FOREIGN KEY (`id_asuransi`) REFERENCES `asuransi` (`id_asuransi`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `asuransi_aktif_ibfk_2` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`id_karyawan`) ON DELETE RESTRICT ON UPDATE RESTRICT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
