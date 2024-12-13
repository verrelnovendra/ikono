CREATE DATABASE  IF NOT EXISTS `chartofaccounts` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `chartofaccounts`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: chartofaccounts
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chartofaccounts`
--

DROP TABLE IF EXISTS `chartofaccounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chartofaccounts` (
  `AccountNumber` int NOT NULL,
  `Category` enum('Assets','Liabilities','Equity','Revenue','Expenses') NOT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `Balance` double DEFAULT '0',
  PRIMARY KEY (`AccountNumber`),
  UNIQUE KEY `AccountNumber` (`AccountNumber`),
  KEY `category_idx` (`Category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chartofaccounts`
--

LOCK TABLES `chartofaccounts` WRITE;
/*!40000 ALTER TABLE `chartofaccounts` DISABLE KEYS */;
INSERT INTO `chartofaccounts` VALUES (1000,'Liabilities','ya',5),(1010,'Assets','Cash - Checking Account',NULL),(1020,'Assets','Cash - Savings Account',NULL),(1100,'Assets','Inventory',500),(1300,'Assets','Prepaid Expenses',NULL),(1400,'Assets','Fixed Assets - Equipment',NULL),(1500,'Assets','Fixed Assets - Furniture',NULL),(2000,'Liabilities','Accounts Payable',NULL),(2100,'Liabilities','Accured Liabilities',NULL),(2200,'Liabilities','Bank Loan',NULL),(3000,'Equity','Owner\'s Equity',NULL),(3100,'Equity','Retained Earnings',NULL),(4000,'Revenue','Sales Revenue',NULL),(4050,'Revenue','Service Revenue',NULL),(5000,'Revenue','Cost of Goods Sold',NULL),(5100,'Expenses','Rent Expense',NULL),(5200,'Expenses','Utilities Expense',NULL),(5300,'Expenses','Salary Expense',NULL),(5400,'Expenses','Marketing Expense',NULL),(5500,'Expenses','Repair & Maintenance',NULL),(8000,'Assets','tes',NULL);
/*!40000 ALTER TABLE `chartofaccounts` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-13 14:07:21
