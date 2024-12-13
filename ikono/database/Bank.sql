CREATE TABLE Bank (
    id INT AUTO_INCREMENT PRIMARY KEY,
    accountNumber VARCHAR(20) NOT NULL UNIQUE,
    bankName VARCHAR(50) NOT NULL,
    balance DOUBLE DEFAULT 0.0,
    description VARCHAR(100)
);