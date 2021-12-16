-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: payments
-- ------------------------------------------------------
-- Server version	8.0.21

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
-- Table structure for table `card`
--

DROP TABLE IF EXISTS `card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `card` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `number` varchar(16) NOT NULL,
  `end_date` date NOT NULL,
  `cvv` int NOT NULL,
  `balance` decimal(13,2) unsigned NOT NULL,
  `status` enum('blocked','active','unblock_request') NOT NULL,
  `pin` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_UNIQUE` (`number`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card`
--

LOCK TABLES `card` WRITE;
/*!40000 ALTER TABLE `card` DISABLE KEYS */;
INSERT INTO `card` VALUES (1,'Название','1234567899876543','2024-11-12',1,9298.86,'active',455),(2,'Card2','9876543210123456','2022-05-25',321,539.17,'active',4321),(3,'Card3','7889455612231245','2021-01-02',66,393.32,'blocked',500),(4,'Mycard1231','8364123534128312','2023-04-05',777,215.00,'active',1321),(5,'TestCard','9898989839128891','2025-12-21',999,50.00,'active',5656),(11,NULL,'4333333333333333','2020-02-22',342,0.00,'blocked',5455),(12,' Моя Карта','6555644555444545','2021-12-12',100,25.00,'active',5454),(13,'Карта','4723464861358936','2021-06-09',555,0.00,'blocked',1112),(14,'','0923759825943857','2023-06-07',912,111.00,'unblock_request',9878),(17,'','3123123123123123','2024-05-07',321,0.00,'unblock_request',1233),(18,' ,/.,m','5656545458454455','2024-01-01',657,531.26,'unblock_request',9999),(20,'dsda','4554545587877777','2026-11-27',1,658.87,'active',0),(21,'-___-__--__-','5466545645645646','2026-11-27',540,740.69,'active',55),(22,'аыва','6564899541548561','2026-12-10',656,0.00,'active',1212),(23,'вфыфыв','9087654345678907','2026-12-16',456,980.00,'active',1235);
/*!40000 ALTER TABLE `card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `card_id_from` int NOT NULL,
  `card_id_to` int NOT NULL,
  `date` datetime NOT NULL,
  `amount` decimal(13,2) unsigned NOT NULL,
  `status` enum('prepared','sent') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_card_has_card_card2_idx` (`card_id_to`),
  KEY `fk_card_has_card_card1_idx` (`card_id_from`),
  CONSTRAINT `fk_card_has_card_card1` FOREIGN KEY (`card_id_from`) REFERENCES `card` (`id`),
  CONSTRAINT `fk_card_has_card_card2` FOREIGN KEY (`card_id_to`) REFERENCES `card` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,2,1,'2021-10-12 13:51:10',500.00,'sent'),(2,1,2,'2021-11-01 20:29:56',122.00,'sent'),(3,2,3,'2021-01-23 22:20:22',56.00,'sent'),(5,2,3,'2021-11-11 10:10:10',45.00,'prepared'),(6,1,3,'2021-11-17 12:54:21',65.00,'prepared'),(7,1,3,'2021-11-17 12:58:11',65.00,'prepared'),(8,1,4,'2021-11-17 13:04:46',123.00,'prepared'),(9,1,2,'2021-11-17 13:10:03',12.00,'prepared'),(10,1,2,'2021-12-12 22:37:59',3.00,'sent'),(11,1,3,'2021-11-24 11:01:51',344.00,'sent'),(12,1,2,'2021-11-24 11:34:22',6.00,'sent'),(14,1,3,'2021-11-17 18:23:56',32.00,'sent'),(15,2,3,'2021-11-17 18:26:31',12.00,'prepared'),(16,2,3,'2021-11-17 18:26:50',12.00,'prepared'),(17,1,3,'2021-11-24 12:38:46',56.56,'sent'),(19,2,3,'2020-02-01 00:45:59',125.00,'sent'),(20,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(21,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(22,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(23,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(24,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(25,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(26,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(27,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(28,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(29,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(30,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(31,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(32,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(33,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(34,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(35,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(36,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(37,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(38,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(39,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(40,4,3,'2020-12-30 23:00:00',1000.00,'sent'),(41,2,1,'2021-11-23 19:54:30',23.00,'prepared'),(42,2,1,'2021-11-24 12:03:08',23.00,'sent'),(43,2,3,'2021-11-23 20:01:30',15.00,'prepared'),(44,2,12,'2021-11-25 11:52:44',10.00,'sent'),(45,2,12,'2021-11-24 12:20:52',15.00,'sent'),(46,4,1,'2021-11-24 12:34:11',25.00,'sent'),(47,11,1,'2021-11-24 13:01:01',3.00,'prepared'),(48,11,1,'2021-11-24 13:01:18',5.00,'sent'),(49,14,1,'2021-11-24 13:48:25',12.00,'sent'),(50,2,1,'2021-11-25 11:48:48',40.00,'sent'),(51,2,1,'2021-11-25 11:49:13',30.00,'prepared'),(53,20,1,'2021-11-30 21:07:02',40.52,'sent'),(54,1,2,'2021-11-30 21:25:55',90.95,'sent'),(55,2,1,'2021-11-30 21:40:52',40.40,'sent'),(56,1,20,'2021-11-30 21:42:54',1000.50,'sent'),(57,20,18,'2021-11-30 21:53:05',100.12,'sent'),(58,2,1,'2021-11-30 22:02:31',50.50,'sent'),(59,20,1,'2021-11-30 22:06:40',50.99,'sent'),(60,1,2,'2021-12-01 10:58:38',120.33,'sent'),(61,1,2,'2021-12-01 11:27:28',21.07,'sent'),(63,20,18,'2021-12-10 15:26:44',100.00,'sent'),(64,20,18,'2021-12-10 15:29:49',100.00,'sent'),(65,20,18,'2021-12-10 15:30:14',100.00,'sent'),(66,21,1,'2021-12-12 22:43:15',100.00,'sent'),(67,21,1,'2021-12-12 22:48:18',50.56,'sent'),(68,21,18,'2021-12-12 22:54:46',80.59,'sent'),(69,21,2,'2021-12-12 23:08:39',50.33,'sent'),(70,21,18,'2021-12-12 23:25:59',50.55,'sent'),(71,21,1,'2021-12-12 23:38:07',30.33,'sent'),(73,21,1,'2021-12-13 22:46:42',33.33,'sent'),(75,21,1,'2021-12-15 09:09:53',10.67,'sent'),(76,21,3,'2021-12-15 09:24:06',10.99,'sent'),(77,21,1,'2021-12-15 13:07:15',1.32,'sent'),(78,21,2,'2021-12-15 13:08:05',12.00,'sent'),(79,21,3,'2021-12-15 23:40:29',156.65,'sent'),(80,21,1,'2021-12-15 23:46:05',55.65,'sent'),(81,21,2,'2021-12-15 23:54:21',5.55,'sent'),(82,21,2,'2021-12-16 00:01:04',10.23,'sent'),(83,21,3,'2021-12-16 00:02:02',100.56,'sent'),(85,23,1,'2021-12-16 08:55:21',10.00,'sent'),(86,23,1,'2021-12-16 08:56:12',10.00,'sent');
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (2,'admin'),(1,'user');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `login` varchar(45) NOT NULL,
  `roles_id` int NOT NULL,
  `pass` varchar(64) NOT NULL,
  `email` varchar(45) NOT NULL,
  `status` enum('blocked','active') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  KEY `fk_user_roles_idx` (`roles_id`),
  CONSTRAINT `fk_user_roles` FOREIGN KEY (`roles_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'dmytro',2,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dima.sigov1@gmail.com','active'),(4,'fdadf',1,'A63AB36162A4F4EE6622CCD787B0A048C26B93ACFC05C6B1843659B253C3C00B','dasd@mail.com','active'),(7,'test_User1235',1,'A5C3436D827B45765A53B36A6B569D83C132565DFD38841C3E540F5F1C2F02D0','dima.sigov3@gmail.com','active'),(8,'test_User123',1,'A5C3436D827B45765A53B36A6B569D83C132565DFD38841C3E540F5F1C2F02D0','dima.sigov3@gmail.com','active'),(9,'s4v5fbxb',1,'BD7AB97A2E2B247212BDE67D1E83DFE0825F7D9F89E069A515D47C901C358772','dima.sigov1@gmail.com','active'),(10,'Sihov',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','Sihov@mail.ua','active'),(11,'Sigov2',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','Sihov@mail.ua','blocked'),(12,'Sihov3',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','Sihov@mail.ua','active'),(13,'TestUser',1,'399141BE1D30AC2656D89EEDCF0D8DCEDAA72D6C29BF959CAE243DC7B1442CF6','qwerty@gmail.com','active'),(17,'Login',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dmytro@mail.com','active'),(18,'Abrakadabra',1,'BD7AB97A2E2B247212BDE67D1E83DFE0825F7D9F89E069A515D47C901C358772','dmytro@fsvb.bsd','active'),(19,'djkasdj',1,'A5C3436D827B45765A53B36A6B569D83C132565DFD38841C3E540F5F1C2F02D0','dmytro@mail.com','active'),(20,'MyLogin',1,'C9FF56956F228F66581852DD879866B9AB04B4484D1F4C6B672FF8DA37837187','dima.sigov7@gmail.com','active'),(21,'fd-da',1,'2A8610AEFDD0028C6BF074DD18721C0EF8BC43241CC7A653D7AEDF2036BDF6B3','es@afda.com','active'),(22,'dlaskmd_dasd',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dima.sigov3@gmail.com','active'),(23,'dsgsg_dasf',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dima.sigov3@gmail.com','active'),(24,'dafjl_sadas',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dima.sigov3@gmail.com','active'),(25,'dasd___As',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dima.sigov3@gmail.com','active'),(26,'afad_-da',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dima.sigov3@gmail.com','active'),(27,'dsdASas',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dima.sigov1@gmail.com','active'),(28,'jhUDHKas',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dima.sigov1@gmail.com','active'),(29,'dsda123',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dima.sigov3@gmail.com','active'),(30,'dajkfnakjdf',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dima.sigov3@gmail.com','active'),(31,'dasopdiuaohl',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dadfzmdn@fazdf.com','active'),(32,'_S-da-',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dima.sigov3@gmail.com','active'),(33,'dasdasdaaa',1,'A63AB36162A4F4EE6622CCD787B0A048C26B93ACFC05C6B1843659B253C3C00B','dima.sigov1@gmail.com','active'),(34,'vflkjdzlvz',1,'A63AB36162A4F4EE6622CCD787B0A048C26B93ACFC05C6B1843659B253C3C00B','dima.sigov3@gmail.com','active'),(35,'davavaxz',1,'A63AB36162A4F4EE6622CCD787B0A048C26B93ACFC05C6B1843659B253C3C00B','dima.sigov3@gmail.com','active'),(36,'asdafxz-ds',1,'A63AB36162A4F4EE6622CCD787B0A048C26B93ACFC05C6B1843659B253C3C00B','dima.sigov3@gmail.com','active'),(37,'dsadasfqfeqa',1,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','dima.sigov3@gmail.com','active'),(38,'dalkjvbfkv',1,'A63AB36162A4F4EE6622CCD787B0A048C26B93ACFC05C6B1843659B253C3C00B','dima.sigov3@gmail.com','active'),(39,'dafADFd',1,'A63AB36162A4F4EE6622CCD787B0A048C26B93ACFC05C6B1843659B253C3C00B','dima.sigov1@gmail.com','active'),(40,'dasfsljln',1,'A63AB36162A4F4EE6622CCD787B0A048C26B93ACFC05C6B1843659B253C3C00B','dima.sigov3@gmail.com','active'),(41,'edasdafsdf',1,'A63AB36162A4F4EE6622CCD787B0A048C26B93ACFC05C6B1843659B253C3C00B','dima.sigov3@gmail.com','active'),(42,'fcdsvbasvsz',1,'A63AB36162A4F4EE6622CCD787B0A048C26B93ACFC05C6B1843659B253C3C00B','dima.sigov1@gmail.com','active'),(43,'ssd_--dA',1,'A63AB36162A4F4EE6622CCD787B0A048C26B93ACFC05C6B1843659B253C3C00B','dima.sigov3@gmail.com','active');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_card`
--

DROP TABLE IF EXISTS `user_has_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_has_card` (
  `user_id` int NOT NULL,
  `card_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`card_id`),
  KEY `fk_user_has_card_card1_idx` (`card_id`),
  KEY `fk_user_has_card_user1_idx` (`user_id`),
  CONSTRAINT `fk_user_has_card_card1` FOREIGN KEY (`card_id`) REFERENCES `card` (`id`),
  CONSTRAINT `fk_user_has_card_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_card`
--

LOCK TABLES `user_has_card` WRITE;
/*!40000 ALTER TABLE `user_has_card` DISABLE KEYS */;
INSERT INTO `user_has_card` VALUES (4,1),(8,1),(4,2),(7,2),(11,4),(17,13),(8,14),(17,14),(4,17),(4,18),(13,18),(4,20),(21,20),(8,21),(21,22),(8,23);
/*!40000 ALTER TABLE `user_has_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'payments'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-12-16  9:26:18
