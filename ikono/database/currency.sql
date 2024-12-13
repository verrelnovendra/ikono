CREATE TABLE currencies (
    CurID VARCHAR(3) PRIMARY KEY,       -- Currency ID (e.g., EUR, USD)
    CurName VARCHAR(50) NOT NULL,       -- Name of the currency (e.g., Euro, US Dollar)
    CurSymbol VARCHAR(5),               -- Symbol of the currency (e.g., €, $, ¥)
    CurCountry VARCHAR(50),             -- Country or region of the currency
    CurExcRate DECIMAL(15, 4),          -- Exchange rate with up to 4 decimal places
    CurStatus VARCHAR(10),              -- Status of the currency (e.g., active, inactive)
    decimal_places INT                  -- Number of decimal places for the currency (e.g., 2)
);