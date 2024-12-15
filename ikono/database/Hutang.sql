CREATE TABLE `hutang`.`hutang` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nama` VARCHAR(60) NULL,
  `jumlah` DECIMAL(10,2) NULL,
  `jenis_kelamin` ENUM('Pria', 'wanita') NULL,
  `status` ENUM('Belum lunas', 'Lunas') NULL,
  `tanggal` DATE NULL,
  PRIMARY KEY (`id`));
  
  
 