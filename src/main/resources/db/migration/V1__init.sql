CREATE DATABASE  IF NOT EXISTS `chikaboom` /*!40100 DEFAULT CHARACTER SET utf8mb3 COLLATE utf8mb3_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `chikaboom`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: chikaboom
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `about`
--

DROP TABLE IF EXISTS `about`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `about` (
                         `idabout` int NOT NULL AUTO_INCREMENT,
                         `text` text COLLATE utf8mb3_bin,
                         `tags` text COLLATE utf8mb3_bin,
                         `profession` varchar(45) COLLATE utf8mb3_bin DEFAULT NULL,
                         PRIMARY KEY (`idabout`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `about`
--

LOCK TABLES `about` WRITE;
/*!40000 ALTER TABLE `about` DISABLE KEYS */;
/*!40000 ALTER TABLE `about` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
                           `idaccount` int NOT NULL AUTO_INCREMENT,
                           `idphonecode` int NOT NULL,
                           `phone` varchar(15) COLLATE utf8mb3_bin NOT NULL,
                           `password` varchar(200) COLLATE utf8mb3_bin NOT NULL,
                           `username` varchar(45) COLLATE utf8mb3_bin NOT NULL,
                           `email` varchar(45) COLLATE utf8mb3_bin DEFAULT NULL,
                           `registration_date` date NOT NULL,
                           `idabout` int DEFAULT NULL,
                           `address` varchar(45) COLLATE utf8mb3_bin DEFAULT NULL,
                           `idsocial_network` varchar(45) COLLATE utf8mb3_bin DEFAULT NULL,
                           `idworking_days` int DEFAULT NULL,
                           PRIMARY KEY (`idaccount`),
                           UNIQUE KEY `nickname_UNIQUE` (`username`),
                           UNIQUE KEY `email_UNIQUE` (`email`),
                           KEY `idphonecode_idx` (`idphonecode`),
                           KEY `idabout_idx` (`idabout`),
                           CONSTRAINT `idabout` FOREIGN KEY (`idabout`) REFERENCES `about` (`idabout`),
                           CONSTRAINT `idphonecode` FOREIGN KEY (`idphonecode`) REFERENCES `phone_code` (`idphonecode`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_roles`
--

DROP TABLE IF EXISTS `account_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_roles` (
                                 `idaccount_role` int NOT NULL AUTO_INCREMENT,
                                 `roles_idrole` int NOT NULL,
                                 `account_idaccount` int NOT NULL,
                                 PRIMARY KEY (`idaccount_role`),
                                 KEY `idrole_idx` (`roles_idrole`),
                                 KEY `idaccount_idx` (`account_idaccount`),
                                 CONSTRAINT `idaccount` FOREIGN KEY (`account_idaccount`) REFERENCES `account` (`idaccount`) ON DELETE CASCADE,
                                 CONSTRAINT `idrole` FOREIGN KEY (`roles_idrole`) REFERENCES `role` (`idrole`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_roles`
--

LOCK TABLES `account_roles` WRITE;
/*!40000 ALTER TABLE `account_roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_status`
--

DROP TABLE IF EXISTS `account_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_status` (
                                  `idaccount_status` int NOT NULL AUTO_INCREMENT,
                                  `since_date` date NOT NULL,
                                  `to_date` date DEFAULT NULL,
                                  `idaccount` int NOT NULL,
                                  `idstatus` int NOT NULL,
                                  PRIMARY KEY (`idaccount_status`),
                                  KEY `fk_account_status_account1_idx` (`idaccount`),
                                  KEY `fk_account_status_status1_idx` (`idstatus`),
                                  CONSTRAINT `fk_account_status_account1` FOREIGN KEY (`idaccount`) REFERENCES `account` (`idaccount`) ON DELETE CASCADE,
                                  CONSTRAINT `fk_account_status_status1` FOREIGN KEY (`idstatus`) REFERENCES `status` (`idstatus`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_status`
--

LOCK TABLES `account_status` WRITE;
/*!40000 ALTER TABLE `account_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
                               `idappointment` int NOT NULL AUTO_INCREMENT,
                               `idaccount_master` int NOT NULL,
                               `idaccount_client` int NOT NULL,
                               `idservice` int NOT NULL,
                               `appointment_date` varchar(45) COLLATE utf8mb3_bin NOT NULL,
                               `appointment_time` varchar(45) COLLATE utf8mb3_bin NOT NULL,
                               PRIMARY KEY (`idappointment`),
                               KEY `idaccount_master_idx` (`idaccount_master`),
                               KEY `idaccount_client_idx` (`idaccount_client`),
                               KEY `idservice_idx` (`idservice`),
                               CONSTRAINT `idaccount_client` FOREIGN KEY (`idaccount_client`) REFERENCES `account` (`idaccount`) ON DELETE CASCADE,
                               CONSTRAINT `idaccount_master` FOREIGN KEY (`idaccount_master`) REFERENCES `account` (`idaccount`) ON DELETE CASCADE,
                               CONSTRAINT `idservice` FOREIGN KEY (`idservice`) REFERENCES `service` (`idservice`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone_code`
--

DROP TABLE IF EXISTS `phone_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phone_code` (
                              `idphonecode` int NOT NULL AUTO_INCREMENT,
                              `country_name` varchar(45) DEFAULT NULL,
                              `phone_code` int DEFAULT NULL,
                              `country_cut` varchar(4) DEFAULT NULL,
                              PRIMARY KEY (`idphonecode`)
) ENGINE=InnoDB AUTO_INCREMENT=239 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone_code`
--

LOCK TABLES `phone_code` WRITE;
/*!40000 ALTER TABLE `phone_code` DISABLE KEYS */;
INSERT INTO `phone_code` VALUES (1,'Австралия',61,'au'),(2,'Австрия',43,'at'),(3,'Азербайджан',994,'az'),(4,'Аландские о-ва',35818,'ax'),(5,'Албания',355,'al'),(6,'Алжир',213,'dz'),(7,'Ангилья',1264,'ai'),(8,'Ангола',244,'ao'),(9,'Андорра',376,'ad'),(10,'Антигуа и Барбуда',1268,'ag'),(11,'Аомынь',853,'mo'),(12,'Аргентина',54,'ar'),(13,'Армения',374,'am'),(14,'Аруба',297,'aw'),(15,'Афганистан',93,'af'),(16,'Багамские острова',1242,'bs'),(17,'Бангладеш',880,'bd'),(18,'Барбадос',1246,'bb'),(19,'Бахрейн',973,'bh'),(20,'Беларусь',375,'by'),(21,'Белиз',501,'bz'),(22,'Бельгия',32,'be'),(23,'Бенин',229,'bj'),(24,'Бермудские о-ва',1441,'bm'),(25,'Болгария',359,'bg'),(26,'Боливия',591,'bo'),(27,'Бонайре, Синт-Эстатиус и Саба',599,'bq'),(28,'Босния',387,'ba'),(29,'Ботсвана',267,'bw'),(30,'Бразилия',55,'br'),(31,'Британская территория в Индийском океане',246,'io'),(32,'Британские Виргинские о-ва',1284,'vg'),(33,'Бруней',673,'bn'),(34,'Буркина Фасо',226,'bf'),(35,'Бурунди',257,'bi'),(36,'Бутан',975,'bt'),(37,'Вануату',678,'vu'),(38,'Ватикан',379,'va'),(39,'Великобритания',44,'gb'),(40,'Венгрия',36,'hu'),(41,'Венесуэла',58,'ve'),(42,'Виргинские о-ва (США)',1340,'vi'),(43,'Внешние малые острова (США)',1,'um'),(44,'Восточный Тимор',670,'tl'),(45,'Вьетнам',84,'vn'),(46,'Габон',241,'ga'),(47,'Гаити',509,'ht'),(48,'Гайана',592,'gy'),(49,'Гамбия',220,'gm'),(50,'Гана',233,'gh'),(51,'Гваделупа',590,'gp'),(52,'Гватемала',502,'gt'),(53,'Гвинея',224,'gn'),(54,'Гвинея-Бисау',245,'gw'),(55,'Германия',49,'de'),(56,'Гибралтар',350,'gi'),(57,'Гондурас',504,'hn'),(58,'Гонконг',852,'hk'),(59,'Гренада',1473,'gd'),(60,'Гренландия',299,'gl'),(61,'Греция',30,'gr'),(62,'Грузия',995,'ge'),(63,'Гуам',1671,'gu'),(64,'Дания',45,'dk'),(65,'Демократическая Республика Конго',243,'cd'),(66,'Джибути',253,'dj'),(67,'Доминика',1767,'dm'),(68,'Доминиканская Республика',1809,'do'),(69,'Египет',20,'eg'),(70,'Замбия',260,'zm'),(71,'Зимбабве',263,'zw'),(72,'Израиль',972,'il'),(73,'Индия',91,'in'),(74,'Индонезия',62,'id'),(75,'Иордания',962,'jo'),(76,'Ирак',964,'iq'),(77,'Иран',98,'ir'),(78,'Ирландия',353,'ie'),(79,'Исландия',354,'is'),(80,'Испания',34,'es'),(81,'Италия',39,'it'),(82,'Йемен',967,'ye'),(83,'Казахстан',7,'kz'),(84,'Каймановы Острова',1345,'ky'),(85,'Камбоджа',855,'kh'),(86,'Камерун',237,'cm'),(87,'Канада',1,'ca'),(88,'Катар',974,'qa'),(89,'Кения',254,'ke'),(90,'Кипр',357,'cy'),(91,'Киргизия',996,'kg'),(92,'Кирибати',686,'ki'),(93,'Китай',86,'cn'),(94,'Кокосовые о-ва',61,'cc'),(95,'Колумбия',57,'co'),(96,'Коморские о-ва',269,'km'),(97,'Конго',242,'cg'),(98,'Корейская Народно-Демократическая Республика',850,'kp'),(99,'Коста-Рика',506,'cr'),(100,'Кот д’Ивуар',225,'ci'),(101,'Куба',53,'cu'),(102,'Кувейт',965,'kw'),(103,'Кюрасао',599,'cw'),(104,'Лаос',856,'la'),(105,'Латвия',371,'lv'),(106,'Лесото',266,'ls'),(107,'Либерия',231,'lr'),(108,'Ливан',961,'lb'),(109,'Ливия',218,'ly'),(110,'Литва',370,'lt'),(111,'Лихтенштейн',423,'li'),(112,'Люксембург',352,'lu'),(113,'Маврикий',230,'mu'),(114,'Мавритания',222,'mr'),(115,'Мадагаскар',261,'mg'),(116,'Майотта',262,'yt'),(117,'Македония',389,'mk'),(118,'Малави',265,'mw'),(119,'Малайзия',60,'my'),(120,'Мали',223,'ml'),(121,'Мальдивские о-ва',960,'mv'),(122,'Мальта',356,'mt'),(123,'Марокко',212,'ma'),(124,'Мартиника',596,'mq'),(125,'Маршалловы о-ва',692,'mh'),(126,'Мексика',52,'mx'),(127,'Микронезия',691,'fm'),(128,'Мозамбик',258,'mz'),(129,'Молдавия',373,'md'),(130,'Монако',377,'mc'),(131,'Монголия',976,'mn'),(132,'Монсеррат',1664,'ms'),(133,'Мьянма',95,'mm'),(134,'Намибия',264,'na'),(135,'Науру',674,'nr'),(136,'Непал',977,'np'),(137,'Нигер',227,'ne'),(138,'Нигерия',234,'ng'),(139,'Нидерланды',31,'nl'),(140,'Никарагуа',505,'ni'),(141,'Ниуе',683,'nu'),(142,'Новая Зеландия',64,'nz'),(143,'Новая Каледония',687,'nc'),(144,'Норвегия',47,'no'),(145,'ОАЭ',971,'ae'),(146,'Оман',968,'om'),(147,'Остров Норфолк',672,'nf'),(148,'Остров Рождества',61,'cx'),(149,'Остров Святого Бартоломея',590,'bl'),(150,'Остров Святого Мартина',590,'mf'),(151,'Остров Святой Елены',290,'sh'),(152,'Острова Зеленого Мыса',238,'cv'),(153,'Острова Кука',682,'ck'),(154,'Острова Тёркс и Кайкос',1649,'tc'),(155,'Острова Уоллис и Футуна',681,'wf'),(156,'Пакистан',92,'pk'),(157,'Палау',680,'pw'),(158,'Палестинские территории',970,'ps'),(159,'Панама',507,'pa'),(160,'Папуа – Новая Гвинея',675,'pg'),(161,'Парагвай',595,'py'),(162,'Перу',51,'pe'),(163,'Питкэрн',870,'pn'),(164,'Польша',48,'pl'),(165,'Португалия',351,'pt'),(166,'Пуэрто-Рико',1787,'pr'),(167,'Республика Корея',82,'kr'),(168,'Реюньон',262,'re'),(169,'Российская Федерация',7,'ru'),(170,'Руанда',250,'rw'),(171,'Румыния',40,'ro'),(172,'Сальвадор',503,'sv'),(173,'Самоа',685,'ws'),(174,'Сан-Марино',378,'sm'),(175,'Сан-Томе и Принсипе',239,'st'),(176,'Саудовская Аравия',966,'sa'),(177,'Свазиленд',268,'sz'),(178,'Северные Марианские о-ва',1670,'mp'),(179,'Сейшелы',248,'sc'),(180,'Сен-Пьер и Микелон',508,'pm'),(181,'Сенегал',221,'sn'),(182,'Сент-Винсент и Гренадины',1784,'vc'),(183,'Сент-Китс и Невис',1869,'kn'),(184,'Сент-Люсия',1758,'lc'),(185,'Сербия',381,'rs'),(186,'Сингапур',65,'sg'),(187,'Синт-Мартен',599,'sx'),(188,'Сирийская Арабская Республика',963,'sy'),(189,'Словакия',421,'sk'),(190,'Словения',386,'si'),(191,'Соединенные Штаты',1,'us'),(192,'Соломоновы Острова',677,'sb'),(193,'Сомали',252,'so'),(194,'Судан',249,'sd'),(195,'Суринам',597,'sr'),(196,'Сьерра-Леоне',232,'sl'),(197,'Таджикистан',992,'tj'),(198,'Таиланд',66,'th'),(199,'Тайвань',886,'tw'),(200,'Танзания',255,'tz'),(201,'Того',228,'tg'),(202,'Токелау',690,'tk'),(203,'Тонга',676,'to'),(204,'Тринидад и Тобаго',1868,'tt'),(205,'Тувалу',688,'tv'),(206,'Тунис',216,'tn'),(207,'Туркменистан',993,'tm'),(208,'Турция',90,'tr'),(209,'Уганда',256,'ug'),(210,'Узбекистан',998,'uz'),(211,'Украина',380,'ua'),(212,'Уругвай',598,'uy'),(213,'Фарерские о-ва',298,'fo'),(214,'Фиджи',679,'fj'),(215,'Филиппины',63,'ph'),(216,'Финляндия',358,'fi'),(217,'Фолклендские о-ва',500,'fk'),(218,'Франция',33,'fr'),(219,'Французская Гвиана',594,'gf'),(220,'Французская Полинезия',689,'pf'),(221,'Хорватия',385,'hr'),(222,'ЦАР',236,'cf'),(223,'Чад',235,'td'),(224,'Черногория',382,'me'),(225,'Чехия',420,'cz'),(226,'Чили',56,'cl'),(227,'Швейцария',41,'ch'),(228,'Швеция',46,'se'),(229,'Шри-Ланка',94,'lk'),(230,'Эквадор',593,'ec'),(231,'Экваториальная Гвинея',240,'gq'),(232,'Эритрея',291,'er'),(233,'Эстония',372,'ee'),(234,'Эфиопия',251,'et'),(235,'ЮАР',27,'za'),(236,'Южный Судан',211,'ss'),(237,'Ямайка',1876,'jm'),(238,'Япония',81,'jp');
/*!40000 ALTER TABLE `phone_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
                        `idrole` int NOT NULL AUTO_INCREMENT,
                        `name` varchar(45) COLLATE utf8mb3_bin NOT NULL,
                        PRIMARY KEY (`idrole`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ROLE_MASTER'),(2,'ROLE_CLIENT');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
                           `idservice` int NOT NULL AUTO_INCREMENT,
                           `idservice_subtype` int NOT NULL,
                           `idaccount` int NOT NULL,
                           `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                           `price` float NOT NULL,
                           `time` varchar(45) COLLATE utf8mb3_bin DEFAULT NULL,
                           PRIMARY KEY (`idservice`),
                           KEY `idservice_subtype_idx` (`idservice_subtype`),
                           CONSTRAINT `idservice_subtype` FOREIGN KEY (`idservice_subtype`) REFERENCES `service_subtype` (`idservice_subtype`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_subtype`
--

DROP TABLE IF EXISTS `service_subtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_subtype` (
                                   `idservice_subtype` int NOT NULL AUTO_INCREMENT,
                                   `idservice_type` int NOT NULL,
                                   `name` varchar(45) COLLATE utf8mb3_bin NOT NULL,
                                   PRIMARY KEY (`idservice_subtype`),
                                   KEY `idservice_type_idx` (`idservice_type`),
                                   CONSTRAINT `idservice_type` FOREIGN KEY (`idservice_type`) REFERENCES `service_type` (`idservice_type`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_subtype`
--

LOCK TABLES `service_subtype` WRITE;
/*!40000 ALTER TABLE `service_subtype` DISABLE KEYS */;
INSERT INTO `service_subtype` VALUES (1,1,'Маникюр'),(2,1,'Педикюр'),(3,2,'Стрижка'),(4,2,'Окрашивание'),(5,2,'Наращивание'),(6,2,'Укладка прически'),(7,3,'Наращивание ресниц'),(8,3,'Ламинирование ресниц'),(9,3,'Окраска ресниц'),(10,4,'Архитектура бровей'),(11,4,'Окраска бровей'),(12,4,'Тридинг бровей'),(13,4,'Укладка бровей'),(14,5,'Макияж'),(15,6,'Депиляция'),(16,6,'Эпиляция'),(17,7,'Мужские стрижки'),(18,7,'Коррекция и оформление бороды и усов'),(19,7,'Окрашивание'),(20,7,'Укладка'),(21,8,'Пилинг и обёртывание'),(22,8,'Программы ухода за лицом и телом'),(23,8,'Мезотерапия'),(24,8,'Аппаратные процедуры'),(25,8,'Тейпирование тела'),(26,8,'Чистка лица'),(27,8,'Лимфатическая массаж лица'),(28,9,'Тутуировки'),(29,9,'Татуаж'),(30,9,'Микроблейдинг'),(31,9,'Удаление перманентного макияжа'),(32,9,'Удаление татуировок'),(33,10,'Классический массаж'),(34,10,'Косметический массаж'),(35,10,'Гигиенический массаж'),(36,10,'Спортивный массаж'),(37,10,'Медицинский(лечебный) массаж');
/*!40000 ALTER TABLE `service_subtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_type`
--

DROP TABLE IF EXISTS `service_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_type` (
                                `idservice_type` int NOT NULL AUTO_INCREMENT,
                                `name` varchar(45) COLLATE utf8mb3_bin NOT NULL,
                                PRIMARY KEY (`idservice_type`),
                                UNIQUE KEY `serviceName_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_type`
--

LOCK TABLES `service_type` WRITE;
/*!40000 ALTER TABLE `service_type` DISABLE KEYS */;
INSERT INTO `service_type` VALUES (7,'Барбершоп'),(4,'Брови'),(5,'Визаж'),(6,'Депиляция/Эпиляция'),(8,'Косметология/уход за телом'),(10,'Массаж'),(1,'Ногтевой сервис'),(2,'Парикмахерские услуги'),(3,'Ресницы'),(9,'Тату/татуаж');
/*!40000 ALTER TABLE `service_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `social_network`
--

DROP TABLE IF EXISTS `social_network`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `social_network` (
                                  `idsocial_network` int NOT NULL AUTO_INCREMENT,
                                  `link` varchar(145) COLLATE utf8mb3_bin NOT NULL,
                                  PRIMARY KEY (`idsocial_network`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `social_network`
--

LOCK TABLES `social_network` WRITE;
/*!40000 ALTER TABLE `social_network` DISABLE KEYS */;
/*!40000 ALTER TABLE `social_network` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
                          `idstatus` int NOT NULL AUTO_INCREMENT,
                          `status` varchar(45) COLLATE utf8mb3_bin NOT NULL,
                          PRIMARY KEY (`idstatus`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `working_days`
--

DROP TABLE IF EXISTS `working_days`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `working_days` (
                                `idworking_days` int NOT NULL AUTO_INCREMENT,
                                `working_days` text COLLATE utf8mb3_bin,
                                `working_day_start` int DEFAULT '900',
                                `working_day_end` int DEFAULT '1800',
                                PRIMARY KEY (`idworking_days`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `working_days`
--

LOCK TABLES `working_days` WRITE;
/*!40000 ALTER TABLE `working_days` DISABLE KEYS */;
/*!40000 ALTER TABLE `working_days` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-08 16:31:25
