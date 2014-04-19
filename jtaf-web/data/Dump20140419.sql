CREATE DATABASE  IF NOT EXISTS `jtaf` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `jtaf`;
-- MySQL dump 10.13  Distrib 5.6.13, for osx10.6 (i386)
--
-- Host: 127.0.0.1    Database: jtaf
-- ------------------------------------------------------
-- Server version	5.6.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `athlete`
--

DROP TABLE IF EXISTS `athlete`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `athlete` (
  `id` bigint(20) NOT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `series_id` bigint(20) DEFAULT NULL,
  `yearOfBirth` int(11) NOT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `club_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_l67v7hlm0w8m4i7maynveecd` (`category_id`),
  KEY `FK_4n2rh646l89g91vhffa0h77i1` (`club_id`),
  CONSTRAINT `FK_4n2rh646l89g91vhffa0h77i1` FOREIGN KEY (`club_id`) REFERENCES `club` (`id`),
  CONSTRAINT `FK_l67v7hlm0w8m4i7maynveecd` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `athlete`
--

LOCK TABLES `athlete` WRITE;
/*!40000 ALTER TABLE `athlete` DISABLE KEYS */;
INSERT INTO `athlete` VALUES (48,'Kevin','m','Grimm',10,1997,24,28),(49,'Gian','m','Ballif',10,1998,24,29),(50,'Alexander','m','Bennet',10,1999,36,28),(51,'Joël','m','Grosjean',10,1999,36,29),(52,'Miomon','m','Schneider-Ludorff',10,1999,36,28),(53,'Fabian','m','Grimm',10,1999,36,28),(54,'Sédric','m','Scholder',10,2000,36,29),(55,'Luc','m','Künzler',10,2000,36,29),(56,'Julian','m','Christen',10,2001,37,29),(57,'Adirano','m','Campiotti',10,2001,37,29),(58,'Delano','m','Winkler',10,2002,37,29),(59,'Robbie','m','Gilchrist',10,2003,38,28),(60,'Lani','m','Scholer',10,2002,37,29),(61,'Miguel','m','Brander',10,2003,38,28),(62,'Rrezart','m','Kokolari',10,2003,38,30),(63,'Reto','m','Dubler',10,2002,37,28),(64,'Lucien','m','Büchli-Köhli',10,2002,37,29),(65,'Nicola','m','Künzler',10,2002,37,29),(66,'Valérian Luc','m','Huguelet',10,2003,38,29),(67,'Noah','m','Christen',10,2003,38,29),(68,'Téo','m','Emery',10,2003,38,30),(69,'Maxime','m','Sunier',10,2003,38,31),(70,'Eric','m','Aeberhardt',10,2003,38,29),(71,'Antoine','m','Sunier',10,2004,38,31),(72,'Julian','m','Berner',10,2004,38,28),(73,'Thibault','m','Biselli',10,2004,38,30),(74,'Félicien','m','Stauffer',10,2004,38,31),(75,'Jonas','m','Teutsch',10,2004,38,29),(76,'Jeremias','m','Teutsch',10,2004,38,29),(77,'Maël','m','Burkhard',10,2005,39,31),(78,'Mathis','m','Künzler',10,2005,39,29),(79,'Luc','m','Perrot',10,2005,39,29),(80,'Matthieu','m','Rais',10,2004,38,30),(81,'Kaene','m','Rollier',10,2005,39,31),(82,'Raïn','m','Schaffter',10,2006,39,31),(83,'Terry','m','Conrad',10,2006,39,31),(84,'Linus','m','Moser',10,2006,39,29),(85,'Sami','m','Simeoni',10,2007,40,31),(86,'Lea','f','Forti',10,1997,41,29),(87,'Fabienne','f','Müller',10,1997,41,29),(88,'Mélanie','f','Grosjean',10,1997,41,29),(89,'Jil','f','Rüfenacht',10,1998,41,29),(90,'Caroline','f','Sahli',10,1998,41,31),(91,'Patricia','f','Reist',10,1998,41,31),(92,'Laila','f','Huguet',10,2000,42,31),(93,'Michelle','f','Feuz',10,2001,43,28),(94,'Zoé','f','Gilchrist',10,2000,42,28),(95,'Anaelle','f','Giauque',10,2001,43,31),(96,'Lia','f','Rüfenacht',10,2001,43,29),(97,'Pauline','f','Aubry',10,2001,43,31),(98,'Joy','f','Ballif',10,2001,43,29),(99,'Loriane','f','Sunier',10,2001,43,31),(100,'Elia','f','Risse',10,2003,44,30),(101,'Muriel','f','Sprunger',10,2002,43,31),(102,'Julie','f','Aubry',10,2003,44,31),(103,'Antonia','f','Anker',10,2003,44,28),(104,'Elodie','f','Fröhlicher',10,2003,44,31),(105,'Meryl','f','Sunier',10,2002,43,31),(106,'Ayla','f','Winkler',10,2002,43,29),(107,'Nora','f','Michel',10,2003,44,29),(108,'Anja','f','Dubler',10,2005,45,28),(109,'Jamina','f','Brander',10,2004,44,28),(110,'Olga','f','Blanchard',10,2005,45,30),(111,'Léonie','f','Gfeller',10,2004,44,29),(112,'Zora','f','Schaffter',10,2004,44,31),(113,'Keira','f','Pedone',10,2004,44,30),(114,'Melyna','f','St-Louis',10,2004,44,30),(115,'Zoé','f','Zurgilgen',10,2005,45,29),(116,'Anouk','f','Peter',10,2005,45,29),(117,'Sacha','f','Girard-Pupenko',10,2005,45,30),(118,'Félicia','f','Conrad',10,2006,45,31),(119,'Manon','f','Perrinjaquet',10,2006,45,31),(120,'Caroline','f','Winkelmann',10,2006,45,31),(121,'Leïla','f','Schaffter',10,2006,45,31),(122,'Lina','f','Michel',10,2006,45,29),(123,'Muriel','f','Frésard',10,2008,46,31),(124,'Amélie','f','Stauffer',10,2008,46,31),(164,'Gian','m','Ballif',137,1998,153,29),(165,'Kevin','m','Grimm',137,1997,152,28),(166,'Alexander','m','Bennet',137,1999,153,28),(167,'Fabian','m','Grimm',137,1999,153,28),(168,'Joël','m','Grosjean',137,1999,153,29),(169,'Luc','m','Künzler',137,2000,154,29),(170,'Miomon','m','Schneider-Ludorff',137,1999,153,28),(171,'Sédric','m','Scholer',137,2000,154,29),(172,'Lucien','m','Büchli-Köhli',137,2002,155,29),(173,'Adirano','m','Campiotti',137,2001,154,29),(174,'Julian','m','Christen',137,2001,154,29),(175,'Reto','m','Dubler',137,2002,155,28),(176,'Nicola','m','Künzler',137,2002,155,29),(177,'Lani','m','Scholer',137,2002,155,29),(178,'Delano','m','Winkler',137,2002,155,29),(179,'Eric','m','Aeberhardt',137,2003,155,29),(180,'Julian','m','Berner',137,2004,156,28),(181,'Thibault','m','Biselli',137,2004,156,30),(182,'Miguel','m','Brander',137,2003,155,28),(183,'Noah','m','Christen',137,2003,155,29),(184,'Téo','m','Emery',137,2003,155,30),(185,'Robbie','m','Gilchrist',137,2003,155,28),(186,'Valérian Luc','m','Huguelet',137,2003,155,29),(187,'Rrezart','m','Kokolari',137,2003,155,30),(188,'Matthieu','m','Rais',137,2004,156,30),(189,'Félicien','m','Stauffer',137,2004,156,31),(190,'Antoine','m','Sunier',137,2004,156,31),(191,'Maxime','m','Sunier',137,2003,155,31),(192,'Jeremias','m','Teutsch',137,2004,156,29),(193,'Jonas','m','Teutsch',137,2004,156,29),(194,'Maël','m','Burkhard',137,2005,156,31),(195,'Terry','m','Conrad',137,2006,157,31),(196,'Mathis','m','Künzler',137,2005,156,29),(197,'Linus','m','Moser',137,2006,157,29),(198,'Luc','m','Perrot',137,2005,156,29),(199,'Kaene','m','Rollier',137,2005,156,31),(200,'Raïn','m','Schaffter',137,2006,157,31),(201,'Sami','m','Simeoni',137,2007,157,31),(202,'Lea','f','Forti',137,1997,158,29),(203,'Mélanie','f','Grosjean',137,1997,158,29),(204,'Fabienne','f','Müller',137,1997,158,29),(205,'Patricia','f','Reist',137,1998,159,31),(206,'Jil','f','Rüfenacht',137,1998,159,29),(207,'Caroline','f','Sahli',137,1998,159,31),(208,'Zoé','f','Gilchrist',137,2000,160,28),(209,'Laila','f','Huguet',137,2000,160,31),(210,'Pauline','f','Aubry',137,2001,160,31),(211,'Joy','f','Ballif',137,2001,160,29),(212,'Michelle','f','Feuz',137,2001,160,28),(213,'Anaelle','f','Giauque',137,2001,160,31),(214,'Lia','f','Rüfenacht',137,2001,160,29),(215,'Muriel','f','Sprunger',137,2002,161,31),(216,'Loriane','f','Sunier',137,2001,160,31),(217,'Meryl','f','Sunier',137,2002,161,31),(218,'Ayla','f','Winkler',137,2002,161,29),(219,'Antonia','f','Anker',137,2003,161,28),(220,'Julie','f','Aubry',137,2003,161,31),(221,'Jamina','f','Brander',137,2004,162,28),(222,'Elodie','f','Fröhlicher',137,2003,161,31),(223,'Léonie','f','Gfeller',137,2004,162,29),(224,'Nora','f','Michel',137,2003,161,29),(225,'Keira','f','Pedone',137,2004,162,30),(226,'Elia','f','Risse',137,2003,161,30),(227,'Zora','f','Schaffter',137,2004,162,31),(228,'Melyna','f','St-Louis',137,2004,162,30),(229,'Olga','f','Blanchard',137,2005,162,30),(230,'Félicia','f','Conrad',137,2006,163,31),(231,'Anja','f','Dubler',137,2005,162,28),(232,'Sacha','f','Girard-Pupenko',137,2005,162,30),(233,'Lina','f','Michel',137,2006,163,29),(234,'Manon','f','Perrinjaquet',137,2006,163,31),(235,'Anouk','f','Peter',137,2005,162,29),(236,'Leïla','f','Schaffter',137,2006,163,31),(237,'Caroline','f','Winkelmann',137,2006,163,31),(238,'Zoé','f','Zurgilgen',137,2005,162,29),(239,'Muriel','f','Frésard',137,2008,163,31),(240,'Amélie','f','Stauffer',137,2008,163,31),(501,'Sabine','f','Castagnoli',137,2003,161,30),(506,'Amelia','f','Fröhlicher',137,2002,161,31);
/*!40000 ALTER TABLE `athlete` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL,
  `abbreviation` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `series_id` bigint(20) DEFAULT NULL,
  `yearFrom` int(11) NOT NULL,
  `yearTo` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (24,'A','m','Knaben',10,1900,1998),(36,'B','m','Knaben',10,1999,2000),(37,'C','m','Knaben',10,2001,2002),(38,'D','m','Knaben',10,2003,2004),(39,'E','m','Knaben',10,2005,2006),(40,'F','m','Knaben',10,2007,2099),(41,'G','f','Mädchen',10,1900,1998),(42,'H','f','Mädchen',10,1999,2000),(43,'I','f','Mädchen',10,2001,2002),(44,'K','f','Mädchen',10,2003,2004),(45,'L','f','Mädchen',10,2005,2006),(46,'M','f','Mädchen',10,2007,2099),(152,'A','m','Knaben',137,1900,1997),(153,'B','m','Knaben',137,1998,1999),(154,'C','m','Knaben',137,2000,2001),(155,'D','m','Knaben',137,2002,2003),(156,'E','m','Knaben',137,2004,2005),(157,'F','m','Knaben',137,2006,2099),(158,'G','f','Mädchen',137,1900,1997),(159,'H','f','Mädchen',137,1998,1999),(160,'I','f','Mädchen',137,2000,2001),(161,'K','f','Mädchen',137,2002,2003),(162,'L','f','Mädchen',137,2004,2005),(163,'M','f','Mädchen',137,2006,2099);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category_event`
--

DROP TABLE IF EXISTS `category_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category_event` (
  `Category_id` bigint(20) NOT NULL,
  `events_id` bigint(20) NOT NULL,
  `position` int(11) NOT NULL,
  PRIMARY KEY (`Category_id`,`events_id`),
  UNIQUE KEY `Category_id` (`Category_id`,`events_id`),
  KEY `FK_grfopgithc30iceeqadfeuo9q` (`events_id`),
  KEY `FK_8y01rg6kyjeave3ouubms8hiw` (`Category_id`),
  CONSTRAINT `FK_8y01rg6kyjeave3ouubms8hiw` FOREIGN KEY (`Category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FK_grfopgithc30iceeqadfeuo9q` FOREIGN KEY (`events_id`) REFERENCES `event` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_event`
--

LOCK TABLES `category_event` WRITE;
/*!40000 ALTER TABLE `category_event` DISABLE KEYS */;
INSERT INTO `category_event` VALUES (24,13,0),(24,14,3),(24,15,1),(24,16,2),(36,13,0),(36,14,3),(36,15,1),(36,16,2),(37,13,0),(37,14,3),(37,15,1),(37,135,2),(38,13,0),(38,14,3),(38,15,1),(38,17,2),(39,12,0),(39,14,3),(39,15,1),(39,17,2),(40,12,0),(40,14,3),(40,15,1),(40,17,2),(41,19,0),(41,20,3),(41,21,1),(41,136,2),(42,19,0),(42,20,3),(42,21,1),(42,136,2),(43,19,0),(43,20,3),(43,21,1),(43,136,2),(44,19,0),(44,20,3),(44,21,1),(44,23,2),(45,18,0),(45,20,3),(45,21,1),(45,23,2),(46,18,0),(46,20,3),(46,21,1),(46,23,2),(152,140,3),(152,142,0),(152,148,2),(152,150,1),(153,140,3),(153,142,0),(153,148,2),(153,150,1),(154,140,3),(154,142,0),(154,146,2),(154,150,1),(155,140,3),(155,142,0),(155,144,2),(155,150,1),(156,138,0),(156,144,2),(156,150,1),(156,363,3),(157,138,0),(157,144,2),(157,409,3),(157,411,1),(158,141,3),(158,143,0),(158,147,2),(158,151,1),(159,141,3),(159,143,0),(159,147,2),(159,151,1),(160,141,3),(160,143,0),(160,147,2),(160,151,1),(161,141,3),(161,143,0),(161,145,2),(161,151,1),(162,139,0),(162,145,2),(162,151,1),(162,364,3),(163,139,0),(163,145,2),(163,410,3),(163,412,1);
/*!40000 ALTER TABLE `category_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `club`
--

DROP TABLE IF EXISTS `club`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `club` (
  `id` bigint(20) NOT NULL,
  `abbreviation` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `space_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_2al9oeijtauda42fspy13xj9q` (`space_id`),
  CONSTRAINT `FK_2al9oeijtauda42fspy13xj9q` FOREIGN KEY (`space_id`) REFERENCES `tspace` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `club`
--

LOCK TABLES `club` WRITE;
/*!40000 ALTER TABLE `club` DISABLE KEYS */;
INSERT INTO `club` VALUES (28,'TVE','Erlach',7),(29,'TVT','Twann',7),(30,'TVLL','Le Landeron',7),(31,'TVN','Nods',7),(32,'TVLN','La Neuveville',7);
/*!40000 ALTER TABLE `club` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `competition`
--

DROP TABLE IF EXISTS `competition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `competition` (
  `id` bigint(20) NOT NULL,
  `competitionDate` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `series_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competition`
--

LOCK TABLES `competition` WRITE;
/*!40000 ALTER TABLE `competition` DISABLE KEYS */;
INSERT INTO `competition` VALUES (11,'2014-05-03','CIS 1 Erlach',10),(241,'2013-05-25','CIS 2 Twann',137);
/*!40000 ALTER TABLE `competition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event` (
  `id` bigint(20) NOT NULL,
  `a` double NOT NULL,
  `b` double NOT NULL,
  `c` double NOT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `series_id` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `longName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (12,17.686955,1397,2.1,'m','60',10,'run','60 m'),(13,10.54596,1778,2.1,'m','80',10,'run','80 m'),(14,0.086375,18833,2.1,'m','600',10,'run_long','600 m'),(15,180.85908,190,1,'m','weit',10,'jump_throw','Weitsprung'),(16,82.491673,178,0.9,'m','kugel 4',10,'jump_throw','Kugel 4 kg'),(17,18,800,0.9,'m','ball',10,'jump_throw','Ball 80 g'),(18,19.742424,1417,2.1,'f','60',10,'run','60 m'),(19,11.754907,1803,2.1,'f','80',10,'run','80 m'),(20,0.089752,19543,2.1,'f','600',10,'run_long','600 m'),(21,220.628792,180,1,'f','weit',10,'jump_throw','Weitsprung'),(22,83.435373,130,0.9,'f','kugel 4',10,'jump_throw','Kugel 4 kg'),(23,22,500,0.9,'f','ball',10,'jump_throw','Ball 80 g'),(135,82.491673,178,0.9,'m','kugel 3',10,'jump_throw','Kugel 3 kg'),(136,83.435373,130,0.9,'f','kugel 3',10,'jump_throw','Kugel 3 kg'),(138,17.686955,1397,2.1,'m','60',137,'run','60 m'),(139,19.742424,1417,2.1,'f','60',137,'run','60 m'),(140,0.086375,18833,2.1,'m','600',137,'run_long','600 m'),(141,0.089752,19543,2.1,'f','600',137,'run_long','600 m'),(142,10.54596,1778,2.1,'m','80',137,'run','80 m'),(143,11.754907,1803,2.1,'f','80',137,'run','80 m'),(144,18,800,0.9,'m','ball',137,'jump_throw','Ball'),(145,22,500,0.9,'f','ball',137,'jump_throw','Ball'),(146,82.491673,178,0.9,'m','kugel 3',137,'jump_throw','Kugel 3 kg'),(147,83.435373,130,0.9,'f','kugel 3',137,'jump_throw','Kugel 3 kg'),(148,82.491673,178,0.9,'m','kugel 4',137,'jump_throw','Kugel 4 kg'),(149,83.435373,130,0.9,'f','kugel 4',137,'jump_throw','Kugel 4 kg'),(150,180.85908,190,1,'m','weit',137,'jump_throw','Weitsprung'),(151,220.628792,180,1,'f','weit',137,'jump_throw','Weitsprung'),(363,0.086375,19833,2.1,'m','600+20',137,'run_long','600 m'),(364,0.089752,20543,2.1,'f','600+20',137,'run_long','600 m'),(409,0.086375,21833,2.1,'m','600+40',137,'run_long','600 m'),(410,0.089752,22543,2.1,'f','600+40',137,'run_long','600 m'),(411,180.85908,130,1,'m','weit-60',137,'jump_throw','Weitsprung'),(412,220.628792,120,1,'f','weit-60',137,'jump_throw','Weitsprung');
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (629);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `result`
--

DROP TABLE IF EXISTS `result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `result` (
  `id` bigint(20) NOT NULL,
  `points` int(11) NOT NULL,
  `result` varchar(255) DEFAULT NULL,
  `competition_id` bigint(20) DEFAULT NULL,
  `event_id` bigint(20) DEFAULT NULL,
  `athlete_id` bigint(20) DEFAULT NULL,
  `postition` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_fgsnqjp4kjtcw7pqqprhoe0w5` (`competition_id`),
  KEY `FK_a8ybbjrmfoskysuvhucm3b6n` (`event_id`),
  KEY `FK_ieu66eh4d6imsavwjwtgnekhy` (`athlete_id`),
  CONSTRAINT `FK_a8ybbjrmfoskysuvhucm3b6n` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`),
  CONSTRAINT `FK_fgsnqjp4kjtcw7pqqprhoe0w5` FOREIGN KEY (`competition_id`) REFERENCES `competition` (`id`),
  CONSTRAINT `FK_ieu66eh4d6imsavwjwtgnekhy` FOREIGN KEY (`athlete_id`) REFERENCES `athlete` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `result`
--

LOCK TABLES `result` WRITE;
/*!40000 ALTER TABLE `result` DISABLE KEYS */;
INSERT INTO `result` VALUES (243,331,'3.73',241,150,165,1),(244,389,'7.38',241,148,165,2),(245,145,'2.34',241,140,165,3),(251,371,'3.95',241,150,166,1),(252,365,'7.00',241,148,166,2),(253,203,'2.28',241,140,166,3),(255,344,'3.80',241,150,168,1),(256,257,'5.31',241,148,168,2),(257,395,'2.13',241,140,168,3),(263,307,'3.60',241,150,167,1),(264,297,'5.93',241,148,167,2),(265,0,'3.15',241,140,167,3),(267,260,'13.18',241,142,165,0),(268,541,'11.26',241,142,164,0),(269,434,'4.30',241,150,164,1),(270,388,'7.36',241,148,164,2),(271,525,'2.05',241,140,164,3),(272,390,'12.20',241,142,166,0),(273,334,'12.60',241,142,168,0),(274,284,'12.98',241,142,170,0),(275,335,'3.75',241,150,170,1),(276,292,'5.85',241,148,170,2),(277,128,'2.36',241,140,170,3),(278,248,'13.28',241,142,167,0),(280,246,'13.30',241,142,171,0),(281,298,'3.55',241,150,171,1),(282,266,'5.46',241,146,171,2),(283,337,'2.17',241,140,171,3),(285,282,'3.46',241,150,169,1),(286,373,'7.12',241,146,169,2),(287,163,'2.32',241,140,169,3),(288,228,'13.46',241,142,169,0),(289,273,'13.07',241,142,174,0),(290,295,'3.53',241,150,174,1),(291,224,'4.82',241,146,174,2),(292,236,'2.25',241,140,174,3),(293,198,'13.74',241,142,173,0),(294,150,'2.73',241,150,173,1),(295,137,'3.53',241,146,173,2),(296,128,'2.36',241,140,173,3),(297,332,'12.61',241,142,178,0),(298,345,'3.81',241,150,178,1),(299,281,'29.18',241,144,178,2),(300,351,'2.16',241,140,178,3),(301,639,'10.72',241,142,185,0),(302,239,'3.22',241,150,185,1),(303,289,'29.83',241,144,185,2),(304,120,'2.37',241,140,185,3),(305,282,'13.00',241,142,177,0),(306,264,'3.36',241,150,177,1),(307,331,'33.45',241,144,177,2),(308,104,'2.39',241,140,177,3),(309,188,'13.84',241,142,182,0),(310,235,'3.20',241,150,182,1),(311,285,'29.52',241,144,182,2),(312,225,'2.26',241,140,182,3),(313,134,'14.42',241,142,187,0),(314,217,'3.10',241,150,187,1),(315,352,'35.19',241,144,187,2),(316,136,'2.35',241,140,187,3),(317,194,'13.78',241,142,175,0),(318,253,'3.30',241,150,175,1),(319,299,'30.69',241,144,175,2),(320,70,'2.44',241,140,175,3),(322,208,'3.05',241,150,172,1),(323,223,'24.36',241,144,172,2),(324,104,'2.39',241,140,172,3),(325,221,'13.52',241,142,172,0),(326,157,'14.16',241,142,176,0),(327,230,'3.17',241,150,176,1),(328,190,'21.71',241,144,176,2),(329,48,'2.48',241,140,176,3),(330,40,'15.90',241,142,186,0),(331,188,'2.94',241,150,186,1),(332,232,'25.14',241,144,186,2),(333,83,'2.42',241,140,186,3),(335,132,'2.63',241,150,183,1),(336,97,'14.51',241,144,183,2),(337,27,'16.21',241,142,184,0),(338,186,'2.93',241,150,184,1),(339,162,'19.49',241,144,184,2),(340,7,'3.00',241,140,184,3),(341,56,'15.56',241,142,191,0),(342,54,'2.20',241,150,191,1),(343,160,'19.34',241,144,191,2),(344,27,'2.53',241,140,191,3),(345,26,'16.23',241,142,179,0),(346,63,'2.25',241,150,179,1),(347,125,'16.62',241,144,179,2),(348,0,'3.27',241,140,179,3),(365,162,'11.10',241,138,190,0),(366,181,'2.90',241,150,190,1),(367,231,'25.01',241,144,190,2),(368,260,'2.33',241,363,190,3),(369,134,'11.35',241,138,180,0),(370,150,'2.73',241,150,180,1),(371,244,'26.14',241,144,180,2),(372,128,'2.46',241,363,180,3),(373,81,'11.91',241,138,181,0),(374,181,'2.90',241,150,181,1),(375,214,'23.67',241,144,181,2),(376,97,'2.50',241,363,181,3),(377,227,'10.60',241,138,189,0),(378,101,'2.46',241,150,189,1),(379,196,'22.17',241,144,189,2),(380,48,'2.58',241,363,189,3),(381,99,'11.70',241,138,193,0),(382,172,'2.85',241,150,193,1),(383,187,'21.44',241,144,193,2),(384,2,'3.14',241,363,193,3),(385,112,'11.56',241,138,192,0),(386,154,'2.75',241,150,192,1),(387,70,'12.52',241,144,192,2),(388,77,'2.53',241,363,192,3),(389,5,'13.40',241,138,194,0),(390,27,'2.05',241,150,194,1),(391,199,'22.47',241,144,194,2),(392,6,'3.11',241,363,194,3),(393,10,'13.22',241,138,196,0),(394,118,'2.55',241,150,196,1),(395,68,'12.36',241,144,196,2),(396,0,'3.21',241,363,196,3),(397,3,'13.55',241,138,198,0),(398,99,'2.45',241,150,198,1),(399,56,'11.51',241,144,198,2),(400,4,'3.12',241,363,198,3),(401,0,'14.10',241,138,188,0),(402,18,'2.00',241,150,188,1),(403,124,'16.55',241,144,188,2),(404,0,'3.26',241,363,188,3),(405,14,'13.08',241,138,199,0),(406,18,'2.00',241,150,199,1),(407,50,'11.10',241,144,199,2),(408,3,'3.13',241,363,199,3),(413,41,'12.48',241,138,200,0),(414,136,'2.05',241,411,200,1),(415,0,'7.73',241,144,200,2),(416,77,'3.13',241,409,200,3),(417,1,'13.72',241,138,195,0),(418,96,'1.83',241,411,195,1),(419,32,'9.92',241,144,195,2),(420,9,'3.29',241,409,195,3),(421,0,'14.64',241,138,197,0),(422,72,'1.70',241,411,197,1),(423,37,'10.22',241,144,197,2),(424,12,'3.28',241,409,197,3),(425,0,'15.94',241,138,201,0),(426,36,'1.50',241,411,201,1),(427,1,'8.02',241,144,201,2),(428,59,'3.16',241,409,201,3),(432,364,'12.90',241,143,204,0),(433,395,'3.59',241,151,204,1),(434,501,'8.62',241,147,204,2),(435,367,'2.23',241,141,204,3),(436,337,'13.09',241,143,203,0),(437,421,'3.71',241,151,203,1),(438,347,'6.17',241,147,203,2),(439,171,'2.39',241,141,203,3),(440,121,'15.00',241,143,206,0),(441,437,'3.78',241,151,206,1),(442,370,'6.54',241,147,206,2),(443,32,'2.59',241,141,206,3),(444,163,'14.53',241,143,207,0),(445,254,'2.95',241,151,207,1),(446,384,'6.76',241,147,207,2),(447,2,'3.11',241,141,207,3),(448,81,'15.53',241,143,205,0),(449,110,'2.30',241,151,205,1),(450,365,'6.45',241,147,205,2),(451,0,'3.56',241,141,205,3),(452,468,'12.25',241,143,209,0),(453,379,'3.52',241,151,209,1),(454,300,'5.45',241,147,209,2),(455,247,'2.32',241,141,209,3),(456,412,'12.59',241,143,212,0),(457,408,'3.65',241,151,212,1),(458,307,'5.55',241,147,212,2),(459,212,'2.35',241,141,212,3),(460,169,'14.47',241,143,208,0),(461,397,'3.60',241,151,208,1),(462,306,'5.53',241,147,208,2),(463,74,'2.51',241,141,208,3),(464,292,'13.41',241,143,213,0),(465,340,'3.34',241,151,213,1),(466,222,'4.27',241,147,213,2),(467,101,'2.47',241,141,213,3),(475,218,'2.79',241,151,211,1),(476,253,'4.73',241,147,211,2),(477,116,'15.05',241,143,216,0),(478,150,'2.48',241,151,216,1),(479,246,'4.62',241,147,216,2),(480,0,'3.22',241,141,216,3),(481,165,'14.51',241,143,226,0),(482,327,'3.28',241,151,226,1),(483,177,'15.13',241,145,226,2),(484,191,'2.37',241,141,226,3),(485,155,'14.62',241,143,215,0),(486,205,'2.73',241,151,215,1),(487,235,'18.92',241,145,215,2),(488,94,'2.48',241,141,215,3),(489,155,'14.62',241,143,220,0),(490,260,'2.98',241,151,220,1),(491,211,'17.31',241,145,220,2),(492,24,'3.01',241,141,220,3),(493,446,'12.38',241,143,219,0),(494,55,'2.05',241,151,219,1),(495,121,'11.64',241,145,219,2),(496,0,'3.15',241,141,219,3),(502,145,'14.72',241,143,501,0),(503,165,'2.55',241,151,501,1),(504,206,'16.99',241,145,501,2),(505,0,'3.22',241,141,501,3),(507,95,'15.32',241,143,506,0),(508,214,'2.77',241,151,506,1),(509,238,'19.10',241,145,506,2),(510,62,'2.53',241,141,506,3),(511,44,'16.16',241,143,222,0),(512,135,'2.41',241,151,222,1),(513,287,'22.36',241,145,222,2),(514,0,'3.38',241,141,222,3),(518,120,'15.01',241,143,218,0),(519,172,'2.58',241,151,218,1),(520,71,'8.68',241,145,218,2),(521,41,'2.57',241,141,218,3),(522,66,'15.75',241,143,224,0),(523,143,'2.45',241,151,224,1),(524,103,'10.55',241,145,224,2),(525,12,'3.05',241,141,224,3),(529,342,'10.28',241,139,221,0),(530,258,'2.97',241,151,221,1),(531,189,'15.92',241,145,221,2),(532,235,'2.43',241,364,221,3),(533,0,'20.85',241,139,229,0),(534,177,'2.60',241,151,229,1),(535,206,'17.01',241,145,229,2),(536,0,'3.36',241,364,229,3),(537,59,'12.49',241,139,223,0),(538,93,'2.22',241,151,223,1),(539,144,'13.09',241,145,223,2),(540,36,'3.08',241,364,223,3),(541,47,'12.66',241,139,227,0),(542,77,'2.15',241,151,227,1),(543,157,'13.87',241,145,227,2),(544,41,'3.07',241,364,227,3),(545,68,'12.37',241,139,225,0),(546,73,'2.13',241,151,225,1),(547,99,'10.29',241,145,225,2),(548,46,'3.06',241,364,225,3),(549,1,'13.89',241,139,228,0),(550,88,'2.20',241,151,228,1),(551,145,'13.10',241,145,228,2),(552,0,'3.48',241,364,228,3),(553,42,'12.74',241,139,238,0),(554,55,'2.05',241,151,238,1),(555,125,'11.89',241,145,238,2),(556,0,'',241,364,238,3),(557,42,'12.74',241,139,235,0),(558,77,'2.15',241,151,235,1),(559,76,'8.98',241,145,235,2),(560,15,'3.14',241,364,235,3),(561,10,'13.46',241,139,232,0),(562,55,'2.05',241,151,232,1),(563,85,'9.47',241,145,232,2),(564,0,'3.46',241,364,232,3),(565,28,'12.99',241,139,230,0),(566,265,'2.4',241,412,230,1),(567,73,'8.79',241,145,230,2),(568,134,'3.13',241,410,230,3),(569,86,'12.15',241,139,234,0),(570,51,'1.43',241,412,234,1),(571,116,'11.32',241,145,234,2),(572,142,'3.12',241,410,234,3),(576,0,'14.11',241,139,236,0),(577,210,'2.15',241,412,236,1),(578,84,'9.46',241,145,236,2),(579,46,'3.26',241,410,236,3),(580,22,'13.11',241,139,233,0),(581,188,'2.05',241,412,233,1),(582,35,'6.66',241,145,233,2),(583,36,'3.28',241,410,233,3),(593,92,'14.97',241,142,183,0),(594,90,'2.41',241,140,183,3),(595,504,'12.04',241,143,202,0),(596,503,'4.08',241,151,202,1),(597,453,'7.86',241,147,202,2),(598,381,'2.22',241,141,202,3),(599,132,'14.87',241,143,214,0),(600,203,'2.72',241,151,214,1),(601,265,'4.91',241,147,214,2),(602,235,'2.33',241,141,214,3),(603,166,'14.50',241,143,210,0),(604,298,'3.15',241,151,210,1),(605,292,'5.32',241,147,210,2),(606,0,'3.27',241,141,210,3),(607,101,'15.25',241,143,211,0),(608,125,'2.44',241,141,211,3),(609,22,'16.68',241,143,217,0),(610,161,'2.53',241,151,217,1),(611,222,'18.03',241,145,217,2),(612,4,'3.09',241,141,217,3),(613,339,'10.30',241,139,231,0),(614,227,'2.83',241,151,231,1),(615,283,'22.11',241,145,231,2),(616,477,'2.26',241,364,231,3),(617,0,'14.35',241,139,237,0),(618,203,'2.12',241,412,237,1),(619,80,'9.18',241,145,237,2),(620,87,'3.19',241,410,237,3),(621,0,'18.82',241,139,239,0),(622,2,'1.21',241,412,239,1),(623,0,'3.79',241,145,239,2),(624,0,'4.13',241,410,239,3),(625,0,'15.39',241,139,240,0),(626,2,'1.21',241,412,240,1),(627,0,'4.23',241,145,240,2),(628,0,'3.46',241,410,240,3);
/*!40000 ALTER TABLE `result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `securitygroup`
--

DROP TABLE IF EXISTS `securitygroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `securitygroup` (
  `email` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `securitygroup`
--

LOCK TABLES `securitygroup` WRITE;
/*!40000 ALTER TABLE `securitygroup` DISABLE KEYS */;
INSERT INTO `securitygroup` VALUES ('simon@martinelli.ch','user');
/*!40000 ALTER TABLE `securitygroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `securityuser`
--

DROP TABLE IF EXISTS `securityuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `securityuser` (
  `email` varchar(255) NOT NULL,
  `confirmationId` varchar(255) DEFAULT NULL,
  `confirmed` tinyint(1) NOT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `secret` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `securityuser`
--

LOCK TABLES `securityuser` WRITE;
/*!40000 ALTER TABLE `securityuser` DISABLE KEYS */;
INSERT INTO `securityuser` VALUES ('simon@martinelli.ch',NULL,1,'Simon','Martinelli','009g5j/5XBby/F8XR9u/Wg==');
/*!40000 ALTER TABLE `securityuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `series`
--

DROP TABLE IF EXISTS `series`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `series` (
  `id` bigint(20) NOT NULL,
  `logo` longblob,
  `name` varchar(255) DEFAULT NULL,
  `space_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_l5ok6a2py2dkxp6kwnq9411jm` (`space_id`),
  CONSTRAINT `FK_l5ok6a2py2dkxp6kwnq9411jm` FOREIGN KEY (`space_id`) REFERENCES `tspace` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `series`
--

LOCK TABLES `series` WRITE;
/*!40000 ALTER TABLE `series` DISABLE KEYS */;
INSERT INTO `series` VALUES (10,NULL,'CIS 2014',7),(137,NULL,'CIS 2013',7);
/*!40000 ALTER TABLE `series` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tspace`
--

DROP TABLE IF EXISTS `tspace`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tspace` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tspace`
--

LOCK TABLES `tspace` WRITE;
/*!40000 ALTER TABLE `tspace` DISABLE KEYS */;
INSERT INTO `tspace` VALUES (7,'CIS'),(9,'CIS');
/*!40000 ALTER TABLE `tspace` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userspace`
--

DROP TABLE IF EXISTS `userspace`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userspace` (
  `id` bigint(20) NOT NULL,
  `role` int(11) DEFAULT NULL,
  `space_id` bigint(20) DEFAULT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_500db1b1ipsocfsyn57mg44x1` (`space_id`),
  KEY `FK_dojpg9l7q2rgfaob1jj1odj14` (`user_email`),
  CONSTRAINT `FK_500db1b1ipsocfsyn57mg44x1` FOREIGN KEY (`space_id`) REFERENCES `tspace` (`id`),
  CONSTRAINT `FK_dojpg9l7q2rgfaob1jj1odj14` FOREIGN KEY (`user_email`) REFERENCES `securityuser` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userspace`
--

LOCK TABLES `userspace` WRITE;
/*!40000 ALTER TABLE `userspace` DISABLE KEYS */;
INSERT INTO `userspace` VALUES (8,0,7,'simon@martinelli.ch');
/*!40000 ALTER TABLE `userspace` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-04-19 18:59:05
