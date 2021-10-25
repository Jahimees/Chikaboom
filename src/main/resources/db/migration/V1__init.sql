USE chikaboom;

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

DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `idAccount` varchar(36) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `surname` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `password` varchar(45) NOT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `registrationDate` date NOT NULL,
  PRIMARY KEY (`idAccount`),
  UNIQUE KEY `idAccount_UNIQUE` (`idAccount`),
  UNIQUE KEY `phone_UNIQUE` (`phone`),
  UNIQUE KEY `login` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `client`;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `idClient` varchar(36) NOT NULL,
  `idAccount` varchar(36) NOT NULL,
  PRIMARY KEY (`idClient`),
  UNIQUE KEY `idClient_UNIQUE` (`idClient`),
  UNIQUE KEY `idAccount_UNIQUE` (`idAccount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `comment`;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `idComment` varchar(36) NOT NULL,
  `idMaster` varchar(36) NOT NULL,
  `idClient` varchar(36) NOT NULL,
  `message` text NOT NULL,
  `isClientMessage` tinyint NOT NULL,
  `rate` int NOT NULL,
  PRIMARY KEY (`idComment`),
  UNIQUE KEY `idComment_UNIQUE` (`idComment`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `master`;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `master` (
  `idMaster` varchar(36) NOT NULL,
  `idAccount` varchar(36) NOT NULL,
  `address` varchar(45) NOT NULL,
  `about` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`idMaster`),
  UNIQUE KEY `idMaster_UNIQUE` (`idMaster`),
  UNIQUE KEY `idAccount_UNIQUE` (`idAccount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `master` WRITE;
/*!40000 ALTER TABLE `master` DISABLE KEYS */;
/*!40000 ALTER TABLE `master` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `service`;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
  `idService` varchar(36) NOT NULL,
  `idMaster` varchar(36) NOT NULL,
  `idServiceType` varchar(36) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `cost` double NOT NULL,
  PRIMARY KEY (`idService`),
  UNIQUE KEY `idService_UNIQUE` (`idService`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `servicetype`;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servicetype` (
  `idServiceType` varchar(36) NOT NULL,
  `typeName` varchar(45) NOT NULL,
  PRIMARY KEY (`idServiceType`),
  UNIQUE KEY `idServiceType_UNIQUE` (`idServiceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `servicetype` WRITE;
/*!40000 ALTER TABLE `servicetype` DISABLE KEYS */;
/*!40000 ALTER TABLE `servicetype` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `work`;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work` (
  `idWork` varchar(36) NOT NULL,
  `idMaster` varchar(45) NOT NULL,
  `image` varchar(45) DEFAULT NULL,
  `comment` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idWork`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='					';

LOCK TABLES `work` WRITE;
/*!40000 ALTER TABLE `work` DISABLE KEYS */;
/*!40000 ALTER TABLE `work` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
