CREATE TABLE Products (
    ProductID INT AUTO_INCREMENT PRIMARY KEY,
    ProductName VARCHAR(100),
    Category VARCHAR(50),
    Stock INT,
    UnitPrice DECIMAL(10, 2)
);


CREATE TABLE vendor (
  vendor_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  contact_person VARCHAR(100) NOT NULL DEFAULT 'Unknown',
  email VARCHAR(150) NOT NULL,
  phone VARCHAR(15) NOT NULL DEFAULT 'Unknown',
  address TINYTEXT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (vendor_id),
  UNIQUE INDEX name_UNIQUE (name ASC) VISIBLE,
  UNIQUE INDEX email_UNIQUE (email ASC) VISIBLE
);

CREATE TABLE PurchaseTransactions (
    PurchaseID INT AUTO_INCREMENT PRIMARY KEY,
    PurchaseDate DATE,
    TotalAmount DECIMAL(15, 2),
    PaymentStatus ENUM('Pending', 'Paid'),
    PaymentMethod ENUM('Cash', 'Transfer', 'Credit'),
    vendor_id INT,
    FOREIGN KEY (vendor_id) REFERENCES vendor(vendor_id)
);

CREATE TABLE PurchaseDetails (
    PurchaseDetailID INT AUTO_INCREMENT PRIMARY KEY,
    PurchaseID INT,
    ProductID INT,
    Quantity INT,
    UnitPrice DECIMAL(10, 2),
    Subtotal DECIMAL(15, 2),
    FOREIGN KEY (PurchaseID) REFERENCES PurchaseTransactions(PurchaseID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
) ;

CREATE TABLE PurchasePayments (
    PaymentID INT AUTO_INCREMENT PRIMARY KEY,
    PurchaseID INT,
    PaymentDate DATE,
    AmountPaid DECIMAL(15, 2),
    PaymentMethod ENUM('Cash', 'Transfer', 'Credit Card'),
    FOREIGN KEY (PurchaseID) REFERENCES PurchaseTransactions(PurchaseID)
);
