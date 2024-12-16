CREATE TABLE retur_pembelian (
    id_retur INT AUTO_INCREMENT NOT NULL,
    id_pembelian VARCHAR(10) NOT NULL,
    product_id INT NOT NULL,
    jumlah INT NOT NULL,
    alasan_retur TEXT NOT NULL,
    PRIMARY KEY (id_retur),
    FOREIGN KEY (id_pembelian) REFERENCES pembelian(id_pembelian) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT ON UPDATE CASCADE
);
