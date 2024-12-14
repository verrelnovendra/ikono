CREATE TABLE returns_sales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rma_number VARCHAR(50),
    receipt_number VARCHAR(50),
    customer_name VARCHAR(100),
    item_id VARCHAR(50),
    description VARCHAR(255),
    quantity INT,
    return_reason VARCHAR(100),
    refund_method VARCHAR(50),
    comments TEXT
);
