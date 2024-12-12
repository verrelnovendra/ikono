USE transaksi;

CREATE TABLE PurchaseTransactions (
    PurchaseID INT AUTO_INCREMENT PRIMARY KEY,
    PurchaseDate DATE,
    SupplierID INT,
    TotalAmount DECIMAL(15, 2),
    PaymentStatus ENUM('Pending', 'Paid'),
    PaymentMethod ENUM('Cash', 'Transfer', 'Credit'),
    FOREIGN KEY (SupplierID) REFERENCES Suppliers(SupplierID)
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
);

CREATE TABLE PurchasePayments (
    PaymentID INT AUTO_INCREMENT PRIMARY KEY,
    PurchaseID INT,
    PaymentDate DATE,
    AmountPaid DECIMAL(15, 2),
    PaymentMethod ENUM('Cash', 'Transfer', 'Credit Card'),
    FOREIGN KEY (PurchaseID) REFERENCES PurchaseTransactions(PurchaseID)
);
