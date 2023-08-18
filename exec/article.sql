-- MariaDB dump 10.19-11.0.2-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: 200ok
-- ------------------------------------------------------
-- Server version	11.0.2-MariaDB-1:11.0.2+maria~ubu2204

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `BATCH_JOB_EXECUTION`
--

DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BATCH_JOB_EXECUTION` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `CREATE_TIME` datetime(6) NOT NULL,
  `START_TIME` datetime(6) DEFAULT NULL,
  `END_TIME` datetime(6) DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `EXIT_CODE` varchar(2500) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime(6) DEFAULT NULL,
  `JOB_CONFIGURATION_LOCATION` varchar(2500) DEFAULT NULL,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  KEY `JOB_INST_EXEC_FK` (`JOB_INSTANCE_ID`),
  CONSTRAINT `JOB_INST_EXEC_FK` FOREIGN KEY (`JOB_INSTANCE_ID`) REFERENCES `BATCH_JOB_INSTANCE` (`JOB_INSTANCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BATCH_JOB_EXECUTION`
--

LOCK TABLES `BATCH_JOB_EXECUTION` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION` DISABLE KEYS */;
INSERT INTO `BATCH_JOB_EXECUTION` VALUES
(0,2,0,'2023-08-17 01:00:00.050000','2023-08-17 01:00:00.087000','2023-08-17 01:00:00.976000','COMPLETED','COMPLETED','','2023-08-17 01:00:00.976000',NULL);
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BATCH_JOB_EXECUTION_CONTEXT`
--

DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_CONTEXT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BATCH_JOB_EXECUTION_CONTEXT` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text DEFAULT NULL,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_CTX_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BATCH_JOB_EXECUTION_CONTEXT`
--

LOCK TABLES `BATCH_JOB_EXECUTION_CONTEXT` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_CONTEXT` DISABLE KEYS */;
INSERT INTO `BATCH_JOB_EXECUTION_CONTEXT` VALUES
(0,'{\"@class\":\"java.util.HashMap\"}',NULL);
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_CONTEXT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BATCH_JOB_EXECUTION_PARAMS`
--

DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_PARAMS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BATCH_JOB_EXECUTION_PARAMS` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `TYPE_CD` varchar(6) NOT NULL,
  `KEY_NAME` varchar(100) NOT NULL,
  `STRING_VAL` varchar(250) DEFAULT NULL,
  `DATE_VAL` datetime(6) DEFAULT NULL,
  `LONG_VAL` bigint(20) DEFAULT NULL,
  `DOUBLE_VAL` double DEFAULT NULL,
  `IDENTIFYING` char(1) NOT NULL,
  KEY `JOB_EXEC_PARAMS_FK` (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_PARAMS_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BATCH_JOB_EXECUTION_PARAMS`
--

LOCK TABLES `BATCH_JOB_EXECUTION_PARAMS` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_PARAMS` DISABLE KEYS */;
INSERT INTO `BATCH_JOB_EXECUTION_PARAMS` VALUES
(0,'STRING','requestDate','20230817','1970-01-01 00:00:00.000000',0,0,'Y');
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_PARAMS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BATCH_JOB_EXECUTION_SEQ`
--

DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BATCH_JOB_EXECUTION_SEQ` (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BATCH_JOB_EXECUTION_SEQ`
--

LOCK TABLES `BATCH_JOB_EXECUTION_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_SEQ` DISABLE KEYS */;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BATCH_JOB_INSTANCE`
--

DROP TABLE IF EXISTS `BATCH_JOB_INSTANCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BATCH_JOB_INSTANCE` (
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_NAME` varchar(100) NOT NULL,
  `JOB_KEY` varchar(32) NOT NULL,
  PRIMARY KEY (`JOB_INSTANCE_ID`),
  UNIQUE KEY `JOB_INST_UN` (`JOB_NAME`,`JOB_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BATCH_JOB_INSTANCE`
--

LOCK TABLES `BATCH_JOB_INSTANCE` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_INSTANCE` DISABLE KEYS */;
INSERT INTO `BATCH_JOB_INSTANCE` VALUES
(0,0,'REMOVE_FRAME','4c08f855ca9d7ec30f6227a152b3c689');
/*!40000 ALTER TABLE `BATCH_JOB_INSTANCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BATCH_JOB_SEQ`
--

DROP TABLE IF EXISTS `BATCH_JOB_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BATCH_JOB_SEQ` (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BATCH_JOB_SEQ`
--

LOCK TABLES `BATCH_JOB_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_SEQ` DISABLE KEYS */;
/*!40000 ALTER TABLE `BATCH_JOB_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BATCH_STEP_EXECUTION`
--

DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BATCH_STEP_EXECUTION` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) NOT NULL,
  `STEP_NAME` varchar(100) NOT NULL,
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `START_TIME` datetime(6) NOT NULL,
  `END_TIME` datetime(6) DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `COMMIT_COUNT` bigint(20) DEFAULT NULL,
  `READ_COUNT` bigint(20) DEFAULT NULL,
  `FILTER_COUNT` bigint(20) DEFAULT NULL,
  `WRITE_COUNT` bigint(20) DEFAULT NULL,
  `READ_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `WRITE_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `PROCESS_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `ROLLBACK_COUNT` bigint(20) DEFAULT NULL,
  `EXIT_CODE` varchar(2500) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  KEY `JOB_EXEC_STEP_FK` (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_STEP_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BATCH_STEP_EXECUTION`
--

LOCK TABLES `BATCH_STEP_EXECUTION` WRITE;
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION` DISABLE KEYS */;
INSERT INTO `BATCH_STEP_EXECUTION` VALUES
(0,3,'removeStep',0,'2023-08-17 01:00:00.104000','2023-08-17 01:00:00.969000','COMPLETED',1,0,0,0,0,0,0,0,'COMPLETED','','2023-08-17 01:00:00.970000');
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BATCH_STEP_EXECUTION_CONTEXT`
--

DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION_CONTEXT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BATCH_STEP_EXECUTION_CONTEXT` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text DEFAULT NULL,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  CONSTRAINT `STEP_EXEC_CTX_FK` FOREIGN KEY (`STEP_EXECUTION_ID`) REFERENCES `BATCH_STEP_EXECUTION` (`STEP_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BATCH_STEP_EXECUTION_CONTEXT`
--

LOCK TABLES `BATCH_STEP_EXECUTION_CONTEXT` WRITE;
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_CONTEXT` DISABLE KEYS */;
INSERT INTO `BATCH_STEP_EXECUTION_CONTEXT` VALUES
(0,'{\"@class\":\"java.util.HashMap\",\"batch.taskletType\":\"org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter\",\"batch.stepType\":\"org.springframework.batch.core.step.tasklet.TaskletStep\"}',NULL);
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_CONTEXT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BATCH_STEP_EXECUTION_SEQ`
--

DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BATCH_STEP_EXECUTION_SEQ` (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BATCH_STEP_EXECUTION_SEQ`
--

LOCK TABLES `BATCH_STEP_EXECUTION_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_SEQ` DISABLE KEYS */;
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article` (
  `type` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp NULL DEFAULT NULL,
  `modified_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `member_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `article_member_id_to_member_id` (`member_id`),
  CONSTRAINT `article_member_id_to_member_id` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES
('N',36,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',37,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',38,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',39,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',40,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',41,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',42,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',43,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',44,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',45,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',46,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',47,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',48,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',49,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',50,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',51,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',52,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',53,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',54,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',55,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',56,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',57,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',58,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',59,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',60,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',61,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',62,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',63,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',64,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',65,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',66,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',67,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',68,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',69,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',70,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',71,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',72,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',73,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',74,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',75,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',76,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',77,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',78,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',79,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',80,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',81,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',82,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',83,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('N',84,'2023-08-17 09:02:56','2023-08-17 09:02:56',1),
('F',86,'2023-08-17 09:32:20','2023-08-17 09:32:20',1),
('I',87,'2023-08-17 09:33:33','2023-08-17 09:33:33',1),
('F',88,'2023-08-17 09:34:34','2023-08-17 09:34:34',1),
('F',90,'2023-08-17 09:44:35','2023-08-17 09:44:35',1),
('F',92,'2023-08-17 12:57:01','2023-08-17 12:57:01',15),
('F',94,'2023-08-17 13:11:23','2023-08-17 13:11:23',15),
('I',95,'2023-08-17 13:40:32','2023-08-17 13:40:32',15),
('F',97,'2023-08-17 13:45:56','2023-08-17 13:45:56',5),
('F',99,'2023-08-17 14:08:49','2023-08-17 14:08:49',15),
('F',100,'2023-08-17 14:09:16','2023-08-17 14:09:16',5),
('I',101,'2023-08-17 14:11:03','2023-08-17 14:11:03',15),
('I',102,'2023-08-17 14:11:12','2023-08-17 14:11:12',15),
('F',103,'2023-08-17 14:17:34','2023-08-17 14:17:34',5),
('I',104,'2023-08-17 14:29:36','2023-08-17 14:29:36',15),
('F',105,'2023-08-17 14:41:39','2023-08-17 14:41:39',21),
('F',106,'2023-08-17 14:44:03','2023-08-17 14:44:03',21),
('F',107,'2023-08-17 14:44:52','2023-08-17 14:44:52',44),
('F',108,'2023-08-17 14:45:07','2023-08-17 14:45:07',21),
('F',109,'2023-08-17 14:48:34','2023-08-17 14:48:34',2),
('F',110,'2023-08-17 14:48:53','2023-08-17 14:48:53',21),
('F',111,'2023-08-17 14:52:11','2023-08-17 14:52:11',21),
('F',112,'2023-08-17 14:54:37','2023-08-17 14:54:37',21),
('F',113,'2023-08-17 14:56:21','2023-08-17 14:56:21',21),
('F',114,'2023-08-17 14:59:31','2023-08-17 14:59:31',21),
('F',115,'2023-08-17 15:01:02','2023-08-17 15:01:02',21),
('I',116,'2023-08-17 15:01:46','2023-08-17 15:01:46',2),
('F',117,'2023-08-17 15:05:57','2023-08-17 15:05:57',21),
('F',118,'2023-08-17 15:20:13','2023-08-17 15:20:13',5),
('F',119,'2023-08-17 15:20:38','2023-08-17 15:20:38',5),
('F',122,'2023-08-17 15:27:29','2023-08-17 15:27:29',5),
('F',123,'2023-08-17 15:31:31','2023-08-17 15:31:31',5),
('F',124,'2023-08-17 15:32:44','2023-08-17 15:32:44',5),
('F',125,'2023-08-17 15:33:27','2023-08-17 15:33:27',5),
('F',126,'2023-08-17 15:34:20','2023-08-17 15:34:20',5),
('F',127,'2023-08-17 15:35:38','2023-08-17 15:35:38',5),
('I',128,'2023-08-17 18:21:51','2023-08-17 18:21:51',2),
('F',129,'2023-08-17 18:46:56','2023-08-17 18:46:56',15),
('I',130,'2023-08-17 23:29:45','2023-08-17 23:29:45',21),
('I',131,'2023-08-17 23:29:48','2023-08-17 23:29:48',21),
('F',132,'2023-08-17 23:58:40','2023-08-17 23:58:40',1),
('F',135,'2023-08-18 00:52:50','2023-08-18 00:52:50',1),
('F',136,'2023-08-18 01:44:37','2023-08-18 01:44:37',21),
('I',137,'2023-08-18 02:34:16','2023-08-18 02:34:16',21),
('I',138,'2023-08-18 02:34:22','2023-08-18 02:34:22',21);
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `frame`
--

DROP TABLE IF EXISTS `frame`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `frame` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp NULL DEFAULT NULL,
  `modified_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `frame_specification` varchar(255) NOT NULL,
  `link` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `open_yn` char(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `frame`
--

LOCK TABLES `frame` WRITE;
/*!40000 ALTER TABLE `frame` DISABLE KEYS */;
INSERT INTO `frame` VALUES
(29,'2023-08-17 09:32:20','2023-08-17 09:32:20','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/932a86f4-9a7c-4b11-baba-f0e047b932d4.png','png','Y','png'),
(30,'2023-08-17 09:34:34','2023-08-17 09:34:34','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/c4e49437-626e-4ddd-bb56-264a90c4e28f.png','png','Y','png'),
(31,'2023-08-17 09:44:35','2023-08-17 09:44:35','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/9deeb7c2-66ee-4f3a-bdd6-5e4211d5f0b0.png','png','Y','png'),
(32,'2023-08-17 12:56:50','2023-08-17 12:56:50','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/cc685ce9-738a-476e-951c-794f879d4185.png','png','N','png'),
(33,'2023-08-17 12:57:01','2023-08-17 12:57:01','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/d3a2781d-a6e1-42c7-902c-2e126bcdc2d1.png','png','N','png'),
(34,'2023-08-17 13:11:23','2023-08-17 13:11:23','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/87820845-76b3-4d9d-9ea7-29eca5d7b407.png','png','Y','png'),
(35,'2023-08-17 13:45:56','2023-08-17 13:45:56','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/1ddaa52d-1175-492b-882c-5347382a0c9c.png','png','Y','png'),
(36,'2023-08-17 13:59:29','2023-08-17 13:59:29','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/7d81b481-ce68-4c11-8254-4a81c38dab59.png','png','Y','png'),
(37,'2023-08-17 14:08:49','2023-08-17 14:08:49','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/d9bec97b-0f95-4cf0-b252-4343571432eb.png','png','Y','png'),
(38,'2023-08-17 14:09:16','2023-08-17 14:09:16','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/ce6851f3-c5c8-46cf-8b7a-cc1dec964280.png','png','Y','png'),
(39,'2023-08-17 14:17:34','2023-08-17 14:17:34','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/7dbb5e09-c4ba-4af7-97a9-c3c4f3396287.png','png','Y','png'),
(40,'2023-08-17 14:41:39','2023-08-17 14:41:39','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/98c28199-03a6-4d89-b36f-cb3b0ed69008.png','png','Y','png'),
(41,'2023-08-17 14:44:03','2023-08-17 14:44:03','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/0d3133a0-f5e9-4312-9694-604e14a838c0.png','png','Y','png'),
(42,'2023-08-17 14:44:52','2023-08-17 14:45:10','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/407b7d3b-2c88-407b-9f79-7954b0fb5728.png','png','N','png'),
(43,'2023-08-17 14:45:07','2023-08-17 14:45:07','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/332f3747-889c-4870-b7a1-fb444d4e3e3d.png','png','Y','png'),
(44,'2023-08-17 14:48:34','2023-08-17 14:48:34','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/ee2d56ab-bbe2-40a7-9c74-31f37b6ecadf.png','png','Y','png'),
(45,'2023-08-17 14:48:53','2023-08-17 14:48:53','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/7c69bde0-2359-4fdd-bfb8-83951c76c65a.png','png','Y','png'),
(46,'2023-08-17 14:52:11','2023-08-17 14:52:11','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/34ef520c-6012-465b-9240-5044bb1f9abf.png','png','Y','png'),
(47,'2023-08-17 14:54:37','2023-08-17 14:54:37','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/8079559a-ea99-4537-af1a-2298b3c40d2f.png','png','Y','png'),
(48,'2023-08-17 14:56:21','2023-08-17 14:56:21','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/64f53465-9b34-4633-b922-338ca504f380.png','png','Y','png'),
(49,'2023-08-17 14:59:31','2023-08-17 14:59:31','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/0b244b84-2083-4389-a6dc-122162530307.png','png','Y','png'),
(50,'2023-08-17 15:01:02','2023-08-17 15:01:02','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/ef031d90-e29d-4a98-919b-b2c8b7853707.png','png','Y','png'),
(51,'2023-08-17 15:05:57','2023-08-17 15:05:57','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/a78f6dba-0af5-48eb-9afa-6548fc49be46.png','png','Y','png'),
(52,'2023-08-17 15:20:13','2023-08-17 15:20:13','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/3ce15e2a-c4cf-4eb9-82f3-7ab58530d4e0.png','png','Y','png'),
(53,'2023-08-17 15:20:38','2023-08-17 15:20:38','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/5751b922-ac22-4d11-896d-26ed9fd27d42.png','png','Y','png'),
(54,'2023-08-17 15:21:11','2023-08-17 15:21:11','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/ea9d3c27-f23e-4eb3-9f6e-90e15325c0f6.png','png','Y','png'),
(55,'2023-08-17 15:21:18','2023-08-17 15:21:18','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/a2a8c054-52c0-4764-806f-9b388cf0f743.png','png','Y','png'),
(56,'2023-08-17 15:27:29','2023-08-17 15:27:29','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/5c1971a6-4715-4739-97b5-901aef035474.png','png','Y','png'),
(57,'2023-08-17 15:31:31','2023-08-17 15:31:31','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/79b5e601-0f13-4b5a-aa95-451dea1d2377.png','png','Y','png'),
(58,'2023-08-17 15:32:44','2023-08-17 15:32:44','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/5b5a37d4-9dbc-4fd0-9726-7c69f6de1b2b.png','png','Y','png'),
(59,'2023-08-17 15:33:27','2023-08-17 15:33:27','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/bb802e45-9c07-4e4e-977a-17983c7fad7d.png','png','Y','png'),
(60,'2023-08-17 15:34:20','2023-08-17 15:34:20','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/056af6a9-8c26-4cd7-bf44-d341e05f0a67.png','png','Y','png'),
(61,'2023-08-17 15:35:38','2023-08-17 15:35:38','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/aac89990-a202-4735-8697-56f861cbc6eb.png','png','Y','png'),
(62,'2023-08-17 18:46:56','2023-08-17 18:46:56','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/e0df39a8-e989-4cd5-b3ba-5be270897fdc.png','png','N','png'),
(63,'2023-08-17 23:58:40','2023-08-17 23:58:40','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/21537191-1f99-4e69-a416-894d46ce24b6.png','png','Y','png'),
(64,'2023-08-18 00:39:38','2023-08-18 00:39:38','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/46ed0b78-3998-4819-91ab-a6bd571d4f1f.png','png','Y','png'),
(65,'2023-08-18 00:48:28','2023-08-18 00:48:28','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/aedc46bb-c891-439c-b187-430c456e9b58.png','png','Y','png'),
(66,'2023-08-18 00:52:50','2023-08-18 00:52:50','VERTICAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/73dbbd0c-0030-41bb-adb5-e2af716579df.png','png','Y','png'),
(67,'2023-08-18 01:44:37','2023-08-18 01:44:37','HORIZONTAL','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/frame/707a450c-c31f-4954-9c02-7c9e266300f8.png','png','Y','png');
/*!40000 ALTER TABLE `frame` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `frame_article`
--

DROP TABLE IF EXISTS `frame_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `frame_article` (
  `subject` varchar(255) NOT NULL,
  `id` bigint(20) NOT NULL,
  `frame_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `frame_article_frame_id_to_frame_id` (`frame_id`),
  CONSTRAINT `frame_article_frame_id_to_frame_id` FOREIGN KEY (`frame_id`) REFERENCES `frame` (`id`),
  CONSTRAINT `frame_article_id_to_article_id` FOREIGN KEY (`id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `frame_article`
--

LOCK TABLES `frame_article` WRITE;
/*!40000 ALTER TABLE `frame_article` DISABLE KEYS */;
INSERT INTO `frame_article` VALUES
('ㅎㅎㅎ',86,29),
('빨간프레임',88,30),
('파란프레임',90,31),
('은광이와 함께 ^^',92,33),
('고양이',94,34),
('고양이',97,35),
('근본이',99,37),
('땡큐 킹세종',100,38),
('핑크 팬더와 함께',103,39),
('스폰지밥',105,40),
('러브러브',106,41),
('치즈와 함께',107,42),
('심슨도넛',108,43),
('wood',109,44),
('파워퍼프걸',110,45),
('도라에몽~',111,46),
('모드리치와 찰칵',112,47),
('레알레알',113,48),
('홀란드 실물 생각보다 멋있음',114,49),
('스파이더맨',115,50),
('할란드 할란드 젧트 디 바예른 하벤',117,51),
('호라이젠탈입니까?',118,52),
('버티컬입니까?',119,53),
('강아지발자국',122,56),
('가자아 옐로우블랙 bvb',123,57),
('블랙화이트 체크',124,58),
('홀로그램',125,59),
('duck',126,60),
('짱구야',127,61),
('cheese ^^',129,62),
('autumn',132,63),
('skypoo',135,66),
('sky',136,67);
/*!40000 ALTER TABLE `frame_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `frequently_asked_question`
--

DROP TABLE IF EXISTS `frequently_asked_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `frequently_asked_question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `answer` text NOT NULL,
  `question` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `frequently_asked_question`
--

LOCK TABLES `frequently_asked_question` WRITE;
/*!40000 ALTER TABLE `frequently_asked_question` DISABLE KEYS */;
INSERT INTO `frequently_asked_question` VALUES
(1,'마이 페이지에서 회원 탈퇴하시면 됩니다.','회원 탈퇴는 어떻게 하나요?'),
(2,'회원 비회원 모두 이용 가능합니다.','사이트는 어떻게 이용하나요?'),
(3,'메인 페이지에서 생성할 수 있습니다.','방은 어떻게 만드나요?'),
(4,'게시판은 아무나 이용가능하나 등록은 로그인된 유저만 가능합니다','게시판은 어떻게 이용하나요?'),
(5,'방장이 프레임을 고르신 후 사진을 찍으시면 됩니다.','사진은 어떻게 찍나요?'),
(6,'프레임 게시판에서 규격에 맞게 생성하실 수 있습니다','프레임은 어떻게 만드나요?'),
(7,'비밀 번호 변경은 어떻게 하나요?','비밀번호 변경은 어떻게 하나요?'),
(8,'비밀번호 찾기는 로그인 페이지에서 가능합니다','비밀번호 찾기는 어떻게 하나요?'),
(9,'로그인 페이지에서 회원 가입하시면 됩니다.','회원가입은 어디서 하나요?'),
(10,'방이 만들어지고 일정 시간이 지나면 자동으로 끊깁니다.','방에서 계속 대화할 수 있나요?'),
(11,'해당 사이트는 아이디 찾기를 별도로 제공하지 않습니다.','아이디 찾기는 어떻게 하나요?'),
(12,'현재로서는 기본적으로 가로형과 세로형이 존재합니다.','사진은 어떤 규격이 있나요?'),
(13,'사진을 찍을 때 일반 모드와 게임 모드가 있습니다.','사진은 어떤 식으로 찍나요?'),
(14,'사진을 찍을 시에 컨셉이 별도로 없고 찍으실 수 있습니다.','일반 모드가 무엇인가요?'),
(15,'사진을 찍을 시에 컨셉이 무작위로 주어지고 그에 맞게 찍으시면 됩니다','게임 모드가 무엇인가요?'),
(16,'마이 페이지에서 회원 탈퇴하시면 됩니다.','회원 탈퇴는 어떻게 하나요?'),
(17,'회원 비회원 모두 이용 가능합니다.','사이트는 어떻게 이용하나요?'),
(18,'메인 페이지에서 생성할 수 있습니다.','방은 어떻게 만드나요?'),
(19,'게시판은 아무나 이용가능하나 등록은 로그인된 유저만 가능합니다','게시판은 어떻게 이용하나요?'),
(20,'방장이 프레임을 고르신 후 사진을 찍으시면 됩니다.','사진은 어떻게 찍나요?'),
(21,'프레임 게시판에서 규격에 맞게 생성하실 수 있습니다','프레임은 어떻게 만드나요?'),
(22,'비밀 번호 변경은 어떻게 하나요?','비밀번호 변경은 어떻게 하나요?'),
(23,'비밀번호 찾기는 로그인 페이지에서 가능합니다','비밀번호 찾기는 어떻게 하나요?'),
(24,'로그인 페이지에서 회원 가입하시면 됩니다.','회원가입은 어디서 하나요?'),
(25,'방이 만들어지고 일정 시간이 지나면 자동으로 끊깁니다.','방에서 계속 대화할 수 있나요?'),
(26,'해당 사이트는 아이디 찾기를 별도로 제공하지 않습니다.','아이디 찾기는 어떻게 하나요?'),
(27,'현재로서는 기본적으로 가로형과 세로형이 존재합니다.','사진은 어떤 규격이 있나요?'),
(28,'사진을 찍을 때 일반 모드와 게임 모드가 있습니다.','사진은 어떤 식으로 찍나요?'),
(29,'사진을 찍을 시에 컨셉이 별도로 없고 찍으실 수 있습니다.','일반 모드가 무엇인가요?'),
(30,'사진을 찍을 시에 컨셉이 무작위로 주어지고 그에 맞게 찍으시면 됩니다','게임 모드가 무엇인가요?'),
(31,'마이 페이지에서 회원 탈퇴하시면 됩니다.','회원 탈퇴는 어떻게 하나요?'),
(32,'회원 비회원 모두 이용 가능합니다.','사이트는 어떻게 이용하나요?'),
(33,'메인 페이지에서 생성할 수 있습니다.','방은 어떻게 만드나요?'),
(34,'게시판은 아무나 이용가능하나 등록은 로그인된 유저만 가능합니다','게시판은 어떻게 이용하나요?'),
(35,'방장이 프레임을 고르신 후 사진을 찍으시면 됩니다.','사진은 어떻게 찍나요?'),
(36,'프레임 게시판에서 규격에 맞게 생성하실 수 있습니다','프레임은 어떻게 만드나요?'),
(37,'비밀 번호 변경은 어떻게 하나요?','비밀번호 변경은 어떻게 하나요?'),
(38,'비밀번호 찾기는 로그인 페이지에서 가능합니다','비밀번호 찾기는 어떻게 하나요?'),
(39,'로그인 페이지에서 회원 가입하시면 됩니다.','회원가입은 어디서 하나요?'),
(40,'방이 만들어지고 일정 시간이 지나면 자동으로 끊깁니다.','방에서 계속 대화할 수 있나요?'),
(41,'해당 사이트는 아이디 찾기를 별도로 제공하지 않습니다.','아이디 찾기는 어떻게 하나요?'),
(42,'현재로서는 기본적으로 가로형과 세로형이 존재합니다.','사진은 어떤 규격이 있나요?'),
(43,'사진을 찍을 때 일반 모드와 게임 모드가 있습니다.','사진은 어떤 식으로 찍나요?'),
(44,'사진을 찍을 시에 컨셉이 별도로 없고 찍으실 수 있습니다.','일반 모드가 무엇인가요?'),
(45,'사진을 찍을 시에 컨셉이 무작위로 주어지고 그에 맞게 찍으시면 됩니다','게임 모드가 무엇인가요?'),
(46,'마이 페이지에서 회원 탈퇴하시면 됩니다.','회원 탈퇴는 어떻게 하나요?'),
(47,'회원 비회원 모두 이용 가능합니다.','사이트는 어떻게 이용하나요?'),
(48,'메인 페이지에서 생성할 수 있습니다.','방은 어떻게 만드나요?'),
(49,'게시판은 아무나 이용가능하나 등록은 로그인된 유저만 가능합니다','게시판은 어떻게 이용하나요?'),
(50,'방장이 프레임을 고르신 후 사진을 찍으시면 됩니다.','사진은 어떻게 찍나요?'),
(51,'프레임 게시판에서 규격에 맞게 생성하실 수 있습니다','프레임은 어떻게 만드나요?'),
(52,'비밀 번호 변경은 어떻게 하나요?','비밀번호 변경은 어떻게 하나요?'),
(53,'비밀번호 찾기는 로그인 페이지에서 가능합니다','비밀번호 찾기는 어떻게 하나요?'),
(54,'로그인 페이지에서 회원 가입하시면 됩니다.','회원가입은 어디서 하나요?'),
(55,'방이 만들어지고 일정 시간이 지나면 자동으로 끊깁니다.','방에서 계속 대화할 수 있나요?'),
(56,'해당 사이트는 아이디 찾기를 별도로 제공하지 않습니다.','아이디 찾기는 어떻게 하나요?'),
(57,'현재로서는 기본적으로 가로형과 세로형이 존재합니다.','사진은 어떤 규격이 있나요?'),
(58,'사진을 찍을 때 일반 모드와 게임 모드가 있습니다.','사진은 어떤 식으로 찍나요?'),
(59,'사진을 찍을 시에 컨셉이 별도로 없고 찍으실 수 있습니다.','일반 모드가 무엇인가요?'),
(60,'사진을 찍을 시에 컨셉이 무작위로 주어지고 그에 맞게 찍으시면 됩니다','게임 모드가 무엇인가요?'),
(61,'마이 페이지에서 회원 탈퇴하시면 됩니다.','회원 탈퇴는 어떻게 하나요?'),
(62,'회원 비회원 모두 이용 가능합니다.','사이트는 어떻게 이용하나요?'),
(63,'메인 페이지에서 생성할 수 있습니다.','방은 어떻게 만드나요?'),
(64,'게시판은 아무나 이용가능하나 등록은 로그인된 유저만 가능합니다','게시판은 어떻게 이용하나요?'),
(65,'방장이 프레임을 고르신 후 사진을 찍으시면 됩니다.','사진은 어떻게 찍나요?'),
(66,'프레임 게시판에서 규격에 맞게 생성하실 수 있습니다','프레임은 어떻게 만드나요?'),
(67,'비밀 번호 변경은 어떻게 하나요?','비밀번호 변경은 어떻게 하나요?'),
(68,'비밀번호 찾기는 로그인 페이지에서 가능합니다','비밀번호 찾기는 어떻게 하나요?'),
(69,'로그인 페이지에서 회원 가입하시면 됩니다.','회원가입은 어디서 하나요?'),
(70,'방이 만들어지고 일정 시간이 지나면 자동으로 끊깁니다.','방에서 계속 대화할 수 있나요?'),
(71,'해당 사이트는 아이디 찾기를 별도로 제공하지 않습니다.','아이디 찾기는 어떻게 하나요?'),
(72,'현재로서는 기본적으로 가로형과 세로형이 존재합니다.','사진은 어떤 규격이 있나요?'),
(73,'사진을 찍을 때 일반 모드와 게임 모드가 있습니다.','사진은 어떤 식으로 찍나요?'),
(74,'사진을 찍을 시에 컨셉이 별도로 없고 찍으실 수 있습니다.','일반 모드가 무엇인가요?'),
(75,'사진을 찍을 시에 컨셉이 무작위로 주어지고 그에 맞게 찍으시면 됩니다','게임 모드가 무엇인가요?'),
(76,'마이 페이지에서 회원 탈퇴하시면 됩니다.','회원 탈퇴는 어떻게 하나요?'),
(77,'회원 비회원 모두 이용 가능합니다.','사이트는 어떻게 이용하나요?'),
(78,'메인 페이지에서 생성할 수 있습니다.','방은 어떻게 만드나요?'),
(79,'게시판은 아무나 이용가능하나 등록은 로그인된 유저만 가능합니다','게시판은 어떻게 이용하나요?'),
(80,'방장이 프레임을 고르신 후 사진을 찍으시면 됩니다.','사진은 어떻게 찍나요?'),
(81,'프레임 게시판에서 규격에 맞게 생성하실 수 있습니다','프레임은 어떻게 만드나요?'),
(82,'비밀 번호 변경은 어떻게 하나요?','비밀번호 변경은 어떻게 하나요?'),
(83,'비밀번호 찾기는 로그인 페이지에서 가능합니다','비밀번호 찾기는 어떻게 하나요?'),
(84,'로그인 페이지에서 회원 가입하시면 됩니다.','회원가입은 어디서 하나요?'),
(85,'방이 만들어지고 일정 시간이 지나면 자동으로 끊깁니다.','방에서 계속 대화할 수 있나요?'),
(86,'해당 사이트는 아이디 찾기를 별도로 제공하지 않습니다.','아이디 찾기는 어떻게 하나요?'),
(87,'현재로서는 기본적으로 가로형과 세로형이 존재합니다.','사진은 어떤 규격이 있나요?'),
(88,'사진을 찍을 때 일반 모드와 게임 모드가 있습니다.','사진은 어떤 식으로 찍나요?'),
(89,'사진을 찍을 시에 컨셉이 별도로 없고 찍으실 수 있습니다.','일반 모드가 무엇인가요?'),
(90,'사진을 찍을 시에 컨셉이 무작위로 주어지고 그에 맞게 찍으시면 됩니다','게임 모드가 무엇인가요?'),
(91,'마이 페이지에서 회원 탈퇴하시면 됩니다.','회원 탈퇴는 어떻게 하나요?'),
(92,'회원 비회원 모두 이용 가능합니다.','사이트는 어떻게 이용하나요?'),
(93,'메인 페이지에서 생성할 수 있습니다.','방은 어떻게 만드나요?'),
(94,'게시판은 아무나 이용가능하나 등록은 로그인된 유저만 가능합니다','게시판은 어떻게 이용하나요?'),
(95,'방장이 프레임을 고르신 후 사진을 찍으시면 됩니다.','사진은 어떻게 찍나요?'),
(96,'프레임 게시판에서 규격에 맞게 생성하실 수 있습니다','프레임은 어떻게 만드나요?'),
(97,'비밀 번호 변경은 어떻게 하나요?','비밀번호 변경은 어떻게 하나요?'),
(98,'비밀번호 찾기는 로그인 페이지에서 가능합니다','비밀번호 찾기는 어떻게 하나요?'),
(99,'로그인 페이지에서 회원 가입하시면 됩니다.','회원가입은 어디서 하나요?'),
(100,'방이 만들어지고 일정 시간이 지나면 자동으로 끊깁니다.','방에서 계속 대화할 수 있나요?'),
(101,'해당 사이트는 아이디 찾기를 별도로 제공하지 않습니다.','아이디 찾기는 어떻게 하나요?'),
(102,'현재로서는 기본적으로 가로형과 세로형이 존재합니다.','사진은 어떤 규격이 있나요?'),
(103,'사진을 찍을 때 일반 모드와 게임 모드가 있습니다.','사진은 어떤 식으로 찍나요?'),
(104,'사진을 찍을 시에 컨셉이 별도로 없고 찍으실 수 있습니다.','일반 모드가 무엇인가요?'),
(105,'사진을 찍을 시에 컨셉이 무작위로 주어지고 그에 맞게 찍으시면 됩니다','게임 모드가 무엇인가요?'),
(106,'마이 페이지에서 회원 탈퇴하시면 됩니다.','회원 탈퇴는 어떻게 하나요?'),
(107,'회원 비회원 모두 이용 가능합니다.','사이트는 어떻게 이용하나요?'),
(108,'메인 페이지에서 생성할 수 있습니다.','방은 어떻게 만드나요?'),
(109,'게시판은 아무나 이용가능하나 등록은 로그인된 유저만 가능합니다','게시판은 어떻게 이용하나요?'),
(110,'방장이 프레임을 고르신 후 사진을 찍으시면 됩니다.','사진은 어떻게 찍나요?'),
(111,'프레임 게시판에서 규격에 맞게 생성하실 수 있습니다','프레임은 어떻게 만드나요?'),
(112,'비밀 번호 변경은 어떻게 하나요?','비밀번호 변경은 어떻게 하나요?'),
(113,'비밀번호 찾기는 로그인 페이지에서 가능합니다','비밀번호 찾기는 어떻게 하나요?'),
(114,'로그인 페이지에서 회원 가입하시면 됩니다.','회원가입은 어디서 하나요?'),
(115,'방이 만들어지고 일정 시간이 지나면 자동으로 끊깁니다.','방에서 계속 대화할 수 있나요?'),
(116,'해당 사이트는 아이디 찾기를 별도로 제공하지 않습니다.','아이디 찾기는 어떻게 하나요?'),
(117,'현재로서는 기본적으로 가로형과 세로형이 존재합니다.','사진은 어떤 규격이 있나요?'),
(118,'사진을 찍을 때 일반 모드와 게임 모드가 있습니다.','사진은 어떤 식으로 찍나요?'),
(119,'사진을 찍을 시에 컨셉이 별도로 없고 찍으실 수 있습니다.','일반 모드가 무엇인가요?'),
(120,'사진을 찍을 시에 컨셉이 무작위로 주어지고 그에 맞게 찍으시면 됩니다','게임 모드가 무엇인가요?'),
(121,'마이 페이지에서 회원 탈퇴하시면 됩니다.','회원 탈퇴는 어떻게 하나요?'),
(122,'회원 비회원 모두 이용 가능합니다.','사이트는 어떻게 이용하나요?'),
(123,'메인 페이지에서 생성할 수 있습니다.','방은 어떻게 만드나요?'),
(124,'게시판은 아무나 이용가능하나 등록은 로그인된 유저만 가능합니다','게시판은 어떻게 이용하나요?'),
(125,'방장이 프레임을 고르신 후 사진을 찍으시면 됩니다.','사진은 어떻게 찍나요?'),
(126,'프레임 게시판에서 규격에 맞게 생성하실 수 있습니다','프레임은 어떻게 만드나요?'),
(127,'비밀 번호 변경은 어떻게 하나요?','비밀번호 변경은 어떻게 하나요?'),
(128,'비밀번호 찾기는 로그인 페이지에서 가능합니다','비밀번호 찾기는 어떻게 하나요?'),
(129,'로그인 페이지에서 회원 가입하시면 됩니다.','회원가입은 어디서 하나요?'),
(130,'방이 만들어지고 일정 시간이 지나면 자동으로 끊깁니다.','방에서 계속 대화할 수 있나요?'),
(131,'해당 사이트는 아이디 찾기를 별도로 제공하지 않습니다.','아이디 찾기는 어떻게 하나요?'),
(132,'현재로서는 기본적으로 가로형과 세로형이 존재합니다.','사진은 어떤 규격이 있나요?'),
(133,'사진을 찍을 때 일반 모드와 게임 모드가 있습니다.','사진은 어떤 식으로 찍나요?'),
(134,'사진을 찍을 시에 컨셉이 별도로 없고 찍으실 수 있습니다.','일반 모드가 무엇인가요?'),
(135,'사진을 찍을 시에 컨셉이 무작위로 주어지고 그에 맞게 찍으시면 됩니다','게임 모드가 무엇인가요?'),
(136,'마이 페이지에서 회원 탈퇴하시면 됩니다.','회원 탈퇴는 어떻게 하나요?'),
(137,'회원 비회원 모두 이용 가능합니다.','사이트는 어떻게 이용하나요?'),
(138,'메인 페이지에서 생성할 수 있습니다.','방은 어떻게 만드나요?'),
(139,'게시판은 아무나 이용가능하나 등록은 로그인된 유저만 가능합니다','게시판은 어떻게 이용하나요?'),
(140,'방장이 프레임을 고르신 후 사진을 찍으시면 됩니다.','사진은 어떻게 찍나요?'),
(141,'프레임 게시판에서 규격에 맞게 생성하실 수 있습니다','프레임은 어떻게 만드나요?'),
(142,'비밀 번호 변경은 어떻게 하나요?','비밀번호 변경은 어떻게 하나요?'),
(143,'비밀번호 찾기는 로그인 페이지에서 가능합니다','비밀번호 찾기는 어떻게 하나요?'),
(144,'로그인 페이지에서 회원 가입하시면 됩니다.','회원가입은 어디서 하나요?'),
(145,'방이 만들어지고 일정 시간이 지나면 자동으로 끊깁니다.','방에서 계속 대화할 수 있나요?'),
(146,'해당 사이트는 아이디 찾기를 별도로 제공하지 않습니다.','아이디 찾기는 어떻게 하나요?'),
(147,'현재로서는 기본적으로 가로형과 세로형이 존재합니다.','사진은 어떤 규격이 있나요?'),
(148,'사진을 찍을 때 일반 모드와 게임 모드가 있습니다.','사진은 어떤 식으로 찍나요?'),
(149,'사진을 찍을 시에 컨셉이 별도로 없고 찍으실 수 있습니다.','일반 모드가 무엇인가요?'),
(150,'사진을 찍을 시에 컨셉이 무작위로 주어지고 그에 맞게 찍으시면 됩니다','게임 모드가 무엇인가요?');
/*!40000 ALTER TABLE `frequently_asked_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp NULL DEFAULT NULL,
  `modified_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `link` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` VALUES
(18,'2023-08-17 09:33:29','2023-08-17 09:33:29','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/3a46bb04-5a8b-4483-8411-a2ba4839b65d.jpg','3a46bb04-5a8b-4483-8411-a2ba4839b65d','jpg'),
(19,'2023-08-17 09:35:09','2023-08-17 09:35:09','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/368dedfc-a016-49b2-81af-7de700831c3e.jpg','368dedfc-a016-49b2-81af-7de700831c3e','jpg'),
(20,'2023-08-17 09:45:02','2023-08-17 09:45:02','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/748c7f46-2b62-4297-937b-ddc03ed4cb8f.jpg','748c7f46-2b62-4297-937b-ddc03ed4cb8f','jpg'),
(21,'2023-08-17 09:51:43','2023-08-17 09:51:43','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/f051d45f-9708-4ab6-9dae-7fb965486fa4.jpg','f051d45f-9708-4ab6-9dae-7fb965486fa4','jpg'),
(22,'2023-08-17 12:59:28','2023-08-17 12:59:28','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/66a439ec-0c4b-42b7-964b-fe2fbf43c1d2.jpg','66a439ec-0c4b-42b7-964b-fe2fbf43c1d2','jpg'),
(23,'2023-08-17 12:59:34','2023-08-17 12:59:34','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/5f407e56-3e31-41d6-a1aa-e92c8d2df49a.jpg','5f407e56-3e31-41d6-a1aa-e92c8d2df49a','jpg'),
(24,'2023-08-17 13:01:30','2023-08-17 13:01:30','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/a0165a24-9789-4ddb-8689-2cf3151d61f6.jpg','a0165a24-9789-4ddb-8689-2cf3151d61f6','jpg'),
(25,'2023-08-17 13:05:40','2023-08-17 13:05:40','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/929e11f1-14c2-44bf-9a89-085c8917e42f.jpg','929e11f1-14c2-44bf-9a89-085c8917e42f','jpg'),
(26,'2023-08-17 13:13:25','2023-08-17 13:13:25','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/191da158-f57d-41e4-b7d3-d9d5db3efeb7.jpg','191da158-f57d-41e4-b7d3-d9d5db3efeb7','jpg'),
(27,'2023-08-17 13:14:23','2023-08-17 13:14:23','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/595c4d87-77d9-4e9c-b2b6-a144a6cca982.jpg','595c4d87-77d9-4e9c-b2b6-a144a6cca982','jpg'),
(28,'2023-08-17 13:15:54','2023-08-17 13:15:54','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/c84f856b-f5cb-41db-8599-9e2c27562657.jpg','c84f856b-f5cb-41db-8599-9e2c27562657','jpg'),
(29,'2023-08-17 13:17:36','2023-08-17 13:17:36','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/03704e7e-ce19-4a85-be08-0f634cda89de.jpg','03704e7e-ce19-4a85-be08-0f634cda89de','jpg'),
(30,'2023-08-17 13:25:09','2023-08-17 13:25:09','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/de8b19c9-7f3f-4464-bb88-17f18906b743.jpg','de8b19c9-7f3f-4464-bb88-17f18906b743','jpg'),
(31,'2023-08-17 13:39:53','2023-08-17 13:39:53','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/28ac88e1-9e42-4abf-92ef-9dedc7649705.jpg','28ac88e1-9e42-4abf-92ef-9dedc7649705','jpg'),
(32,'2023-08-17 13:45:30','2023-08-17 13:45:30','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/ff9f6613-9a8c-4105-8511-2db6a8007366.jpg','ff9f6613-9a8c-4105-8511-2db6a8007366','jpg'),
(33,'2023-08-17 13:56:45','2023-08-17 13:56:45','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/e9812cac-aca1-426c-9736-62fbfedfbbb9.jpg','e9812cac-aca1-426c-9736-62fbfedfbbb9','jpg'),
(34,'2023-08-17 14:10:34','2023-08-17 14:10:34','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/25a21337-7ce7-4b6c-815d-4b5bf4a78071.jpg','25a21337-7ce7-4b6c-815d-4b5bf4a78071','jpg'),
(35,'2023-08-17 14:15:51','2023-08-17 14:15:51','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/48321a40-7a90-4bfa-9358-db6a019ad0c7.jpg','48321a40-7a90-4bfa-9358-db6a019ad0c7','jpg'),
(36,'2023-08-17 14:18:03','2023-08-17 14:18:03','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/b7afa259-7015-40b4-941c-1cb9af7b563a.jpg','b7afa259-7015-40b4-941c-1cb9af7b563a','jpg'),
(37,'2023-08-17 14:19:25','2023-08-17 14:19:25','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/ac7ba51b-9caa-418e-af5c-13e5bac12682.jpg','ac7ba51b-9caa-418e-af5c-13e5bac12682','jpg'),
(38,'2023-08-17 14:20:26','2023-08-17 14:20:26','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/f77a4c5a-7520-487a-9b40-d450ff51ecf7.jpg','f77a4c5a-7520-487a-9b40-d450ff51ecf7','jpg'),
(39,'2023-08-17 14:22:24','2023-08-17 14:22:24','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/5ffa83e0-22a5-4a29-b2fe-7d028451eefe.jpg','5ffa83e0-22a5-4a29-b2fe-7d028451eefe','jpg'),
(40,'2023-08-17 14:23:15','2023-08-17 14:23:15','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/637743b2-35e6-42f3-b7e0-9de543ccd16f.jpg','637743b2-35e6-42f3-b7e0-9de543ccd16f','jpg'),
(41,'2023-08-17 14:23:36','2023-08-17 14:23:36','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/7c768756-11ab-4173-a4a7-bec1beef90ac.jpg','7c768756-11ab-4173-a4a7-bec1beef90ac','jpg'),
(42,'2023-08-17 14:25:36','2023-08-17 14:25:36','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/2aca6612-7ce0-473d-9647-959eb65050d7.jpg','2aca6612-7ce0-473d-9647-959eb65050d7','jpg'),
(43,'2023-08-17 14:26:33','2023-08-17 14:26:33','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/eb8bf69b-8541-42d2-9bad-872da189fe3e.jpg','eb8bf69b-8541-42d2-9bad-872da189fe3e','jpg'),
(44,'2023-08-17 14:29:28','2023-08-17 14:29:28','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/e467999e-7642-454c-b536-dca168f6bdc5.jpg','e467999e-7642-454c-b536-dca168f6bdc5','jpg'),
(45,'2023-08-17 14:39:43','2023-08-17 14:39:43','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/3537d32f-ceb9-4f0d-a4b4-32557988b304.jpg','3537d32f-ceb9-4f0d-a4b4-32557988b304','jpg'),
(46,'2023-08-17 14:42:06','2023-08-17 14:42:06','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/bc0f6d2a-a02e-46c4-9be5-f30b53000441.jpg','bc0f6d2a-a02e-46c4-9be5-f30b53000441','jpg'),
(47,'2023-08-17 14:50:30','2023-08-17 14:50:30','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/e4283ead-c0a0-4909-98b9-e360610a9548.jpg','e4283ead-c0a0-4909-98b9-e360610a9548','jpg'),
(48,'2023-08-17 14:52:30','2023-08-17 14:52:30','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/6b2dd5cb-7f49-4cfd-96a0-7d88eb5161a5.jpg','6b2dd5cb-7f49-4cfd-96a0-7d88eb5161a5','jpg'),
(49,'2023-08-17 14:59:36','2023-08-17 14:59:36','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/e5358a67-a0dc-4d27-9f29-a8bec409a4b6.jpg','e5358a67-a0dc-4d27-9f29-a8bec409a4b6','jpg'),
(50,'2023-08-17 15:01:42','2023-08-17 15:01:42','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/5c935b63-0e7c-4fd6-a991-9ab1862012fd.jpg','5c935b63-0e7c-4fd6-a991-9ab1862012fd','jpg'),
(51,'2023-08-17 15:08:02','2023-08-17 15:08:02','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/238f3f5c-1583-4c78-84bb-8b6fbe99439d.jpg','238f3f5c-1583-4c78-84bb-8b6fbe99439d','jpg'),
(52,'2023-08-17 17:05:28','2023-08-17 17:05:28','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/f2f3a5f4-91da-47a0-adf2-a536efb5801a.jpg','f2f3a5f4-91da-47a0-adf2-a536efb5801a','jpg'),
(53,'2023-08-17 17:05:28','2023-08-17 17:05:28','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/667a2a35-5b49-48f2-8639-260409a40e6a.jpg','667a2a35-5b49-48f2-8639-260409a40e6a','jpg'),
(54,'2023-08-17 17:33:14','2023-08-17 17:33:14','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/ac214698-37a1-4f0a-b646-7677a0cad1b2.jpg','ac214698-37a1-4f0a-b646-7677a0cad1b2','jpg'),
(55,'2023-08-17 17:33:15','2023-08-17 17:33:15','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/f66c9054-8499-4cbc-b30d-f89668a82dbf.jpg','f66c9054-8499-4cbc-b30d-f89668a82dbf','jpg'),
(56,'2023-08-17 17:44:46','2023-08-17 17:44:46','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/68f2abd3-78c0-4da3-b364-b2572dc41c13.jpg','68f2abd3-78c0-4da3-b364-b2572dc41c13','jpg'),
(57,'2023-08-17 17:50:01','2023-08-17 17:50:01','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/a0f3dc27-8490-4f1f-86b4-2abc1648b44a.jpg','a0f3dc27-8490-4f1f-86b4-2abc1648b44a','jpg'),
(58,'2023-08-17 17:51:52','2023-08-17 17:51:52','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/7a5ae23c-7ba1-485a-b792-93ce726c77a3.jpg','7a5ae23c-7ba1-485a-b792-93ce726c77a3','jpg'),
(59,'2023-08-17 17:54:57','2023-08-17 17:54:57','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/1d7f5fc8-b07f-4132-a64a-a48caa491738.jpg','1d7f5fc8-b07f-4132-a64a-a48caa491738','jpg'),
(60,'2023-08-17 17:54:58','2023-08-17 17:54:58','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/59ea25a3-e7d8-4029-9a5d-1fc35d73f747.jpg','59ea25a3-e7d8-4029-9a5d-1fc35d73f747','jpg'),
(61,'2023-08-17 18:05:39','2023-08-17 18:05:39','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/ee5f9661-b538-42fc-b6fe-448fcc2d8692.jpg','ee5f9661-b538-42fc-b6fe-448fcc2d8692','jpg'),
(62,'2023-08-17 18:17:00','2023-08-17 18:17:00','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/0866d52a-7784-4cc9-b5d8-2f148d344247.jpg','0866d52a-7784-4cc9-b5d8-2f148d344247','jpg'),
(63,'2023-08-17 18:20:37','2023-08-17 18:20:37','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/227909f4-b1f9-4bc7-9ffb-577c8c4bef32.jpg','227909f4-b1f9-4bc7-9ffb-577c8c4bef32','jpg'),
(64,'2023-08-17 18:23:35','2023-08-17 18:23:35','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/0cf31966-a09b-4f58-92de-87a8a29d0980.jpg','0cf31966-a09b-4f58-92de-87a8a29d0980','jpg'),
(65,'2023-08-17 18:40:01','2023-08-17 18:40:01','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/0b76aa86-44e1-4f8f-b13f-ce52d0a122f9.jpg','0b76aa86-44e1-4f8f-b13f-ce52d0a122f9','jpg'),
(66,'2023-08-17 18:47:24','2023-08-17 18:47:24','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/5c320988-b382-4d00-9a3d-276ff610c652.jpg','5c320988-b382-4d00-9a3d-276ff610c652','jpg'),
(67,'2023-08-17 19:12:23','2023-08-17 19:12:23','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/8234ed6e-6dea-4f89-81dc-144c06dcd6d8.jpg','8234ed6e-6dea-4f89-81dc-144c06dcd6d8','jpg'),
(68,'2023-08-17 19:14:37','2023-08-17 19:14:37','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/18ff4bfe-0207-4090-bb5c-91216c52ac23.jpg','18ff4bfe-0207-4090-bb5c-91216c52ac23','jpg'),
(69,'2023-08-17 19:20:51','2023-08-17 19:20:51','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/3193e026-36a6-450d-aaa9-6664411d507c.jpg','3193e026-36a6-450d-aaa9-6664411d507c','jpg'),
(70,'2023-08-17 23:29:30','2023-08-17 23:29:30','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/4cbe0cac-7544-47b0-aded-486a0b35f28c.jpg','4cbe0cac-7544-47b0-aded-486a0b35f28c','jpg'),
(71,'2023-08-17 23:31:53','2023-08-17 23:31:53','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/a7fe511c-cd5f-4812-95bf-fd1587c25161.jpg','a7fe511c-cd5f-4812-95bf-fd1587c25161','jpg'),
(72,'2023-08-17 23:33:03','2023-08-17 23:33:03','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/0a36ed41-bb58-4f8a-9272-f1bbb0c8f1b0.jpg','0a36ed41-bb58-4f8a-9272-f1bbb0c8f1b0','jpg'),
(73,'2023-08-17 23:40:45','2023-08-17 23:40:45','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/2f87f9cc-de08-42a8-8203-373841aaf759.jpg','2f87f9cc-de08-42a8-8203-373841aaf759','jpg'),
(74,'2023-08-17 23:42:17','2023-08-17 23:42:17','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/ab78a6d9-0467-44c5-80e8-6bd55f2f565a.jpg','ab78a6d9-0467-44c5-80e8-6bd55f2f565a','jpg'),
(75,'2023-08-17 23:43:09','2023-08-17 23:43:09','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/6266c882-59e5-4fb0-94a4-3ee22ff9831a.jpg','6266c882-59e5-4fb0-94a4-3ee22ff9831a','jpg'),
(76,'2023-08-17 23:45:01','2023-08-17 23:45:01','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/d4a5f1be-2a05-485c-9913-bc4b2f623274.jpg','d4a5f1be-2a05-485c-9913-bc4b2f623274','jpg'),
(77,'2023-08-17 23:45:56','2023-08-17 23:45:56','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/6a3afbfa-899c-42c1-a3a9-e7175b30438b.jpg','6a3afbfa-899c-42c1-a3a9-e7175b30438b','jpg'),
(78,'2023-08-17 23:47:05','2023-08-17 23:47:05','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/4737ef17-1aff-4ec3-af23-c96ef329d0e3.jpg','4737ef17-1aff-4ec3-af23-c96ef329d0e3','jpg'),
(79,'2023-08-17 23:48:15','2023-08-17 23:48:15','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/9ea7166c-2048-48f9-bbba-971ff7783add.jpg','9ea7166c-2048-48f9-bbba-971ff7783add','jpg'),
(80,'2023-08-17 23:51:56','2023-08-17 23:51:56','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/bbdfb94a-821d-48ca-bf74-ff6e2f6bceed.jpg','bbdfb94a-821d-48ca-bf74-ff6e2f6bceed','jpg'),
(81,'2023-08-17 23:59:36','2023-08-17 23:59:36','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/1e56e693-0b7b-4057-8157-1f6a2f75af1c.jpg','1e56e693-0b7b-4057-8157-1f6a2f75af1c','jpg'),
(82,'2023-08-18 00:19:30','2023-08-18 00:19:30','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/b691a366-03a3-492c-b900-7c85c60e1339.jpg','b691a366-03a3-492c-b900-7c85c60e1339','jpg'),
(83,'2023-08-18 00:49:07','2023-08-18 00:49:07','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/11a530a7-c353-4463-83c6-69be10d64383.jpg','11a530a7-c353-4463-83c6-69be10d64383','jpg'),
(84,'2023-08-18 00:50:37','2023-08-18 00:50:37','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/0328b4dc-f2c2-4582-8a1c-888181c4bf87.jpg','0328b4dc-f2c2-4582-8a1c-888181c4bf87','jpg'),
(85,'2023-08-18 00:53:42','2023-08-18 00:53:42','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/d77af0b4-97d9-4149-bcc1-cd6fd7dd9335.jpg','d77af0b4-97d9-4149-bcc1-cd6fd7dd9335','jpg'),
(86,'2023-08-18 01:03:29','2023-08-18 01:03:29','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/a50ccd93-c5ea-4ce7-8b7e-68d35cd00566.jpg','a50ccd93-c5ea-4ce7-8b7e-68d35cd00566','jpg'),
(87,'2023-08-18 01:04:42','2023-08-18 01:04:42','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/94f6a7bb-48fb-43f9-9612-9977a51ed968.jpg','94f6a7bb-48fb-43f9-9612-9977a51ed968','jpg'),
(88,'2023-08-18 01:05:47','2023-08-18 01:05:47','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/4a5e6d5c-3757-4d0c-b72d-b326bf1a719d.jpg','4a5e6d5c-3757-4d0c-b72d-b326bf1a719d','jpg'),
(89,'2023-08-18 01:10:05','2023-08-18 01:10:05','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/35497261-3f9a-4b9e-ab5d-76868ec6a2cc.jpg','35497261-3f9a-4b9e-ab5d-76868ec6a2cc','jpg'),
(90,'2023-08-18 01:24:34','2023-08-18 01:24:34','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/7f61a78e-3088-4e9a-85bc-f33eecf346e0.jpg','7f61a78e-3088-4e9a-85bc-f33eecf346e0','jpg'),
(91,'2023-08-18 01:25:12','2023-08-18 01:25:12','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/4407c021-d0a5-4f26-abae-94b4e57325b2.jpg','4407c021-d0a5-4f26-abae-94b4e57325b2','jpg'),
(92,'2023-08-18 01:26:52','2023-08-18 01:26:52','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/cb300d49-15ba-446d-9d72-eaf3ff8fb538.jpg','cb300d49-15ba-446d-9d72-eaf3ff8fb538','jpg'),
(93,'2023-08-18 01:27:50','2023-08-18 01:27:50','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/7bbcda51-50fb-4669-83f5-a71177c0efd3.jpg','7bbcda51-50fb-4669-83f5-a71177c0efd3','jpg'),
(94,'2023-08-18 01:33:45','2023-08-18 01:33:45','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/ef0f634c-4ef2-47df-bf49-40b1d09e6cfc.jpg','ef0f634c-4ef2-47df-bf49-40b1d09e6cfc','jpg'),
(95,'2023-08-18 01:35:08','2023-08-18 01:35:08','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/f7b24501-6317-4cde-879a-bf3cb9827961.jpg','f7b24501-6317-4cde-879a-bf3cb9827961','jpg'),
(96,'2023-08-18 01:35:55','2023-08-18 01:35:55','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/44fa1c38-a5dc-46f1-a25a-11f90fe6433d.jpg','44fa1c38-a5dc-46f1-a25a-11f90fe6433d','jpg'),
(97,'2023-08-18 01:49:44','2023-08-18 01:49:44','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/3ce9f195-4178-4e30-a043-91d54ffeab36.jpg','3ce9f195-4178-4e30-a043-91d54ffeab36','jpg'),
(98,'2023-08-18 01:54:51','2023-08-18 01:54:51','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/b15b23dd-3829-4fbc-b1af-cea1c362d677.jpg','b15b23dd-3829-4fbc-b1af-cea1c362d677','jpg'),
(99,'2023-08-18 01:55:17','2023-08-18 01:55:17','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/91791782-d8ce-49f7-82f3-74b7d60050e9.jpg','91791782-d8ce-49f7-82f3-74b7d60050e9','jpg'),
(100,'2023-08-18 01:56:12','2023-08-18 01:56:12','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/bbc9216a-697c-4700-a168-940c71fb68d4.jpg','bbc9216a-697c-4700-a168-940c71fb68d4','jpg'),
(101,'2023-08-18 01:58:01','2023-08-18 01:58:01','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/b6325014-7a85-4444-9601-6c8a3527bb8d.jpg','b6325014-7a85-4444-9601-6c8a3527bb8d','jpg'),
(102,'2023-08-18 01:58:40','2023-08-18 01:58:40','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/6ec877f4-415c-4bd4-838f-6e0aaa486281.jpg','6ec877f4-415c-4bd4-838f-6e0aaa486281','jpg'),
(103,'2023-08-18 02:08:20','2023-08-18 02:08:20','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/ddcbb2a4-040b-4111-89be-0cb906cea6ec.jpg','ddcbb2a4-040b-4111-89be-0cb906cea6ec','jpg'),
(104,'2023-08-18 02:08:57','2023-08-18 02:08:57','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/3d769785-02c8-47f4-ac68-a0ec11129126.jpg','3d769785-02c8-47f4-ac68-a0ec11129126','jpg'),
(105,'2023-08-18 02:21:14','2023-08-18 02:21:14','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/102fdb54-d522-45c3-bc47-fa2968a8b176.jpg','102fdb54-d522-45c3-bc47-fa2968a8b176','jpg'),
(106,'2023-08-18 02:21:57','2023-08-18 02:21:57','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/b7c77891-a394-49dc-b817-a91c60e221a0.jpg','b7c77891-a394-49dc-b817-a91c60e221a0','jpg'),
(107,'2023-08-18 02:22:44','2023-08-18 02:22:44','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/f559689a-ef97-43fd-99f1-efcf16485821.jpg','f559689a-ef97-43fd-99f1-efcf16485821','jpg'),
(108,'2023-08-18 02:23:24','2023-08-18 02:23:24','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/d8a2f7fc-2c1b-481b-a0c8-c6c6d6b358e4.jpg','d8a2f7fc-2c1b-481b-a0c8-c6c6d6b358e4','jpg'),
(109,'2023-08-18 02:24:09','2023-08-18 02:24:09','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/08c79549-d55c-457d-8a9b-84232bf6e989.jpg','08c79549-d55c-457d-8a9b-84232bf6e989','jpg'),
(110,'2023-08-18 02:25:03','2023-08-18 02:25:03','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/cb9b206f-5136-45de-8d4f-0af916de0456.jpg','cb9b206f-5136-45de-8d4f-0af916de0456','jpg'),
(111,'2023-08-18 02:34:09','2023-08-18 02:34:09','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/image/202fd2f6-e87b-451e-a6fc-789639c86ece.jpg','202fd2f6-e87b-451e-a6fc-789639c86ece','jpg');
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_article`
--

DROP TABLE IF EXISTS `image_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image_article` (
  `id` bigint(20) NOT NULL,
  `image_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `iamge_article_image_id_to_image_id` (`image_id`),
  CONSTRAINT `iamge_article_image_id_to_image_id` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`),
  CONSTRAINT `image_article_id_to_article_id` FOREIGN KEY (`id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_article`
--

LOCK TABLES `image_article` WRITE;
/*!40000 ALTER TABLE `image_article` DISABLE KEYS */;
INSERT INTO `image_article` VALUES
(95,31),
(101,34),
(102,34),
(104,44),
(116,50),
(128,63),
(130,70),
(131,70),
(137,111),
(138,111);
/*!40000 ALTER TABLE `image_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_owner`
--

DROP TABLE IF EXISTS `image_owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image_owner` (
  `image_id` bigint(20) NOT NULL,
  `member_id` bigint(20) NOT NULL,
  PRIMARY KEY (`image_id`,`member_id`),
  KEY `image_owner_member_id_to_member_id` (`member_id`),
  CONSTRAINT `image_owner_image_id_to_image_id` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`),
  CONSTRAINT `image_owner_member_id_to_member_id` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_owner`
--

LOCK TABLES `image_owner` WRITE;
/*!40000 ALTER TABLE `image_owner` DISABLE KEYS */;
INSERT INTO `image_owner` VALUES
(20,21),
(21,21),
(52,21),
(53,21),
(54,21),
(55,21),
(56,21),
(57,21),
(58,21),
(59,21),
(60,21),
(61,21),
(62,21),
(63,21),
(64,21),
(65,21),
(66,21),
(70,21),
(72,21),
(82,21),
(103,21),
(104,21),
(105,21),
(106,21),
(108,21),
(109,21),
(110,21),
(111,21);
/*!40000 ALTER TABLE `image_owner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_tag`
--

DROP TABLE IF EXISTS `image_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image_tag` (
  `image_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  PRIMARY KEY (`image_id`,`tag_id`),
  KEY `image_tag_tag_id_to_tag_id` (`tag_id`),
  CONSTRAINT `image_tag_image_id_to_image_id` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`),
  CONSTRAINT `image_tag_tag_id_to_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_tag`
--

LOCK TABLES `image_tag` WRITE;
/*!40000 ALTER TABLE `image_tag` DISABLE KEYS */;
INSERT INTO `image_tag` VALUES
(51,1),
(54,1),
(55,1),
(56,1),
(57,1),
(58,1),
(59,1),
(60,1),
(61,1),
(62,1),
(63,1),
(64,1),
(65,1),
(66,1),
(67,1),
(68,1),
(69,1),
(71,1),
(73,1),
(74,1),
(75,1),
(76,1),
(77,1),
(78,1),
(79,1),
(80,1),
(81,1),
(82,1),
(83,1),
(84,1),
(85,1),
(86,1),
(87,1),
(88,1),
(89,1),
(90,1),
(91,1),
(92,1),
(93,1),
(94,1),
(95,1),
(96,1),
(97,1),
(98,1),
(99,1),
(100,1),
(101,1),
(102,1),
(103,1),
(104,1),
(105,1),
(106,1),
(107,1),
(108,1),
(109,1),
(110,1),
(111,1),
(51,2),
(54,2),
(55,2),
(56,2),
(57,2),
(58,2),
(59,2),
(60,2),
(61,2),
(62,2),
(63,2),
(64,2),
(65,2),
(66,2),
(67,2),
(68,2),
(69,2),
(71,2),
(73,2),
(74,2),
(75,2),
(76,2),
(77,2),
(78,2),
(79,2),
(80,2),
(81,2),
(82,2),
(83,2),
(84,2),
(85,2),
(86,2),
(87,2),
(88,2),
(89,2),
(90,2),
(91,2),
(92,2),
(93,2),
(94,2),
(95,2),
(96,2),
(97,2),
(98,2),
(99,2),
(100,2),
(101,2),
(102,2),
(103,2),
(104,2),
(105,2),
(106,2),
(107,2),
(108,2),
(109,2),
(110,2),
(111,2),
(51,3),
(54,3),
(55,3),
(56,3),
(57,3),
(58,3),
(59,3),
(60,3),
(61,3),
(62,3),
(63,3),
(64,3),
(65,3),
(66,3),
(67,3),
(68,3),
(69,3),
(71,3),
(73,3),
(74,3),
(75,3),
(76,3),
(77,3),
(78,3),
(79,3),
(80,3),
(81,3),
(82,3),
(83,3),
(84,3),
(85,3),
(86,3),
(87,3),
(88,3),
(89,3),
(90,3),
(91,3),
(92,3),
(93,3),
(94,3),
(95,3),
(96,3),
(97,3),
(98,3),
(99,3),
(100,3),
(101,3),
(102,3),
(103,3),
(104,3),
(105,3),
(106,3),
(107,3),
(108,3),
(109,3),
(110,3),
(111,3),
(51,4),
(54,4),
(55,4),
(56,4),
(57,4),
(58,4),
(59,4),
(60,4),
(61,4),
(62,4),
(63,4),
(64,4),
(65,4),
(66,4),
(67,4),
(68,4),
(69,4),
(71,4),
(73,4),
(74,4),
(75,4),
(76,4),
(77,4),
(78,4),
(79,4),
(80,4),
(81,4),
(82,4),
(83,4),
(84,4),
(85,4),
(86,4),
(87,4),
(88,4),
(89,4),
(90,4),
(91,4),
(92,4),
(93,4),
(94,4),
(95,4),
(96,4),
(97,4),
(98,4),
(99,4),
(100,4),
(101,4),
(102,4),
(103,4),
(104,4),
(105,4),
(106,4),
(107,4),
(108,4),
(109,4),
(110,4),
(111,4);
/*!40000 ALTER TABLE `image_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lover`
--

DROP TABLE IF EXISTS `lover`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lover` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) DEFAULT NULL,
  `article_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Unique_member_id_article_id` (`member_id`,`article_id`),
  KEY `lover_article_id_to_article_id` (`article_id`),
  CONSTRAINT `loer_member_id_to_member_id` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
  CONSTRAINT `lover_article_id_to_article_id` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lover`
--

LOCK TABLES `lover` WRITE;
/*!40000 ALTER TABLE `lover` DISABLE KEYS */;
/*!40000 ALTER TABLE `lover` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp NULL DEFAULT NULL,
  `modified_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `age` int(11) DEFAULT NULL,
  `del_yn` char(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `gender_fm` char(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `personal_agreement_yn` char(255) DEFAULT NULL,
  `profile` varchar(255) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  `sns_id` varchar(255) DEFAULT NULL,
  `sns_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Unique_nickname` (`nickname`),
  UNIQUE KEY `Unique_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES
(1,'2023-08-14 06:53:36','2023-08-17 09:05:31',25,'N','19207203j@gmail.com','F','김싸피','아싸링','$2a$10$t1IGV0V85/gYmbZsGDrXcOTRdyUYiDULuA1Qy65dkDp6HbWfZiOHG',NULL,'https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/profile/e0b01a37-d58c-4687-97a8-ba209daac78a.jpg','MEMBER',NULL,NULL),
(2,'2023-08-14 07:16:27','2023-08-14 07:16:27',NULL,'N','sid98816@naver.com','M','dd','hihi','$2a$10$zMRAIYBovk2ezZ7D6T6vf.QXSc46Eboo7kYyg8hZt12Mgaf/bxhGa','T',NULL,'MEMBER',NULL,NULL),
(3,'2023-08-16 07:00:03','2023-08-16 07:00:03',NULL,NULL,'1f7b2b46-c7e8-4170-8bb8-e673a06353b3',NULL,NULL,'GUEST wre','$2a$10$/mBEXf9eo97XhRCdK9uXaeXqzNGphPTtVIxMrDsxrLAoEAuIcLseu',NULL,NULL,'GUEST',NULL,NULL),
(4,'2023-08-16 08:13:24','2023-08-16 08:13:24',NULL,NULL,'10128bf2-bfac-48e3-9cac-a14a19f40303',NULL,NULL,'GUEST dfs','$2a$10$xsR1DbLG9c3Nv3DMpUcFueJiqkbtvL7jkDj6zimwGI9qunE4X3IQu',NULL,NULL,'GUEST',NULL,NULL),
(5,'2023-08-16 12:05:07','2023-08-16 12:05:07',28,'N','sk618dev@gmail.com','F','KS','루카 모드리치 사랑해','$2a$10$qmtfx8HznFTdyZzy/B/lEeTvKeD72e8GXKeHzCUWvbXQUVjOoE3Hu','T',NULL,'MEMBER',NULL,NULL),
(6,'2023-08-16 13:10:43','2023-08-16 13:10:43',NULL,NULL,'8d2fc229-ccc9-442d-ba9c-e8a46005c738',NULL,NULL,'GUEST ui','$2a$10$7IS0xfp14tUT/2cQIsKA..Mz7u91xk97513Bai7uNE2gw56lQmhga',NULL,NULL,'GUEST',NULL,NULL),
(7,'2023-08-16 13:31:37','2023-08-16 13:31:37',NULL,NULL,'a6e87c7d-a3cb-4a06-ac73-bf6ae256e90b',NULL,NULL,'GUEST fes','$2a$10$frV6ZITV2YFraSpSrbyfaOGf.ATqjVrMJxG9mU9WkhBs.p4GOhiAq',NULL,NULL,'GUEST',NULL,NULL),
(8,'2023-08-16 14:21:16','2023-08-16 14:21:16',NULL,NULL,'c3ab6e78-7fa4-4566-b119-8dc9f4beef25',NULL,NULL,'GUEST sda','$2a$10$yTB0secHzQ8GmwJYJtK5Nu4j5E4GDjLsbj.pa491yQYHY.v4XSMMC',NULL,NULL,'GUEST',NULL,NULL),
(9,'2023-08-16 14:34:18','2023-08-16 14:34:18',NULL,NULL,'2d0884fc-42dd-4680-a518-e974e88a21ce',NULL,NULL,'GUEST fsd','$2a$10$kZeFKEsh4ov5ZhfZFktXt.DY8h27QoevQJAEFcH/ceaQ0eideIkB2',NULL,NULL,'GUEST',NULL,NULL),
(10,'2023-08-16 14:36:34','2023-08-16 14:36:34',NULL,NULL,'84bb5dfa-ba68-4717-90ae-9dd356f659d1',NULL,NULL,'GUEST dsfcx','$2a$10$8svRiXPzGSmQZ/q0CdVWFuK3zMQuRl.tRlZAJsXvPKQxJYqlOLpEq',NULL,NULL,'GUEST',NULL,NULL),
(11,'2023-08-16 16:58:46','2023-08-16 16:58:46',NULL,NULL,'37c23d97-a2a1-4da9-ba37-62a5d098bf6c',NULL,NULL,'GUEST rew','$2a$10$50oq4.tpa1iiKEhtrQqGq.P8E3rOyGSwlmTBuT8WrRl1dmyFzLBLG',NULL,NULL,'GUEST',NULL,NULL),
(12,'2023-08-17 00:21:30','2023-08-17 00:21:30',NULL,NULL,'9ab070f8-a0f0-4521-b74f-5cd00639eb8d',NULL,NULL,'GUEST kp','$2a$10$BkkVaeAifQSHXdwwZV/dBeoDORVg2ji815seaIbMoDnXbiVwrzIFW',NULL,NULL,'GUEST',NULL,NULL),
(13,'2023-08-17 00:39:01','2023-08-17 00:39:01',NULL,NULL,'1d772c0a-2f21-46fe-95f0-1b5e1c7d78ab',NULL,NULL,'GUEST 3','$2a$10$QFcbXOCUP5PqDbwao55ENet8JapRFIyBpUpU4XYI2OJfk1RCHZ.x6',NULL,NULL,'GUEST',NULL,NULL),
(14,'2023-08-17 00:46:13','2023-08-17 00:46:13',NULL,NULL,'47696782-d6ac-47c3-ada8-1379e28b7984',NULL,NULL,'GUEST ew','$2a$10$uC/sleeABDGZfdnCzwyqde5wZGbt8X.jRN/vtQawyaSMPYnnkQ.1q',NULL,NULL,'GUEST',NULL,NULL),
(15,'2023-08-17 01:05:05','2023-08-17 13:47:15',NULL,'N','thk1104@gmail.com',NULL,'','cheese','$2a$10$Odjf5uQWkldlmCWwwQjwaeqZfhSv14GGAEcJTBJfwi7UGruaFDfVq','T','https://200oks3bucket.s3.ap-northeast-2.amazonaws.com/profile/590726fc-0063-4c8e-a7ef-7a69b026fb06.jpg','MEMBER',NULL,NULL),
(16,'2023-08-17 01:05:28','2023-08-17 01:05:28',NULL,NULL,'45001d75-d3f7-4f34-bc83-523936e0fc9b',NULL,NULL,'GUEST ewzc','$2a$10$nz7.wxukDQ7BvJBZRIWSCeIxKQuLZyKFrVNdiAunpCkxS8mEE/282',NULL,NULL,'GUEST',NULL,NULL),
(17,'2023-08-17 01:11:40','2023-08-17 04:18:21',100,'N','admin@admin.com','M','나폴레옹','imp','$2a$10$ECJhXjxKhTWfgzcIJJoxb.mwgpKlQaG/6koB/yX/FZ6gYM2DL9KkC','T',NULL,'ADMIN',NULL,NULL),
(18,'2023-08-17 02:24:44','2023-08-17 02:24:44',NULL,NULL,'f8b010c4-4d2c-4d73-a6fd-fbf1e6297d38',NULL,NULL,'GUEST ssda','$2a$10$1Fr8paeA3hWD85aGa0KG5O0M0M.XQcZz.efVAD/6wXZoUZIc.bKuC',NULL,NULL,'GUEST',NULL,NULL),
(19,'2023-08-17 02:29:45','2023-08-17 02:29:45',NULL,NULL,'e10bbee9-3efe-4622-950e-5fa53cbf53ea',NULL,NULL,'GUEST csa','$2a$10$jEMbYpBbdqC88ft3CkGPZOey6QdaWT2O8MijS5jC/7Em0Sn96nglS',NULL,NULL,'GUEST',NULL,NULL),
(20,'2023-08-17 03:32:20','2023-08-17 03:32:20',27,'N','hunn2023@gmail.com','M','김동훈','안녕하세요3반에서왔어요','$2a$10$I2WNkttxJB/lsCEnn/ZApOktCifkGtslwKizVm6ZXCAiWFRUvUd4K','T',NULL,'MEMBER',NULL,NULL),
(21,'2023-08-17 04:21:14','2023-08-17 04:25:34',100000,'N','darkped@naver.com','M','징기스칸','member','$2a$10$aCQJ9vguyikQK5BCIsXJGuDLoduZLMimGB.Z8zoa4Bb0YygHZs3HK','T',NULL,'MEMBER',NULL,NULL),
(22,'2023-08-17 04:45:24','2023-08-17 04:45:24',NULL,NULL,'c1c66a2c-1e42-4415-b5ef-9c7bac12cdae',NULL,NULL,'GUEST 324','$2a$10$55QHLTTWZsmjziVjsIUo5OpFsbYPSbKir9FC.FhLyoBOL7UxkW63m',NULL,NULL,'GUEST',NULL,NULL),
(23,'2023-08-17 04:59:43','2023-08-17 04:59:43',NULL,NULL,'344ce91e-4260-458e-8e41-028b0d38248f',NULL,NULL,'GUEST wer','$2a$10$RD.6ry6GNORtaefze.pxRepSGdEpo.c0Vz9rOswI16Vd6k3oSa9u6',NULL,NULL,'GUEST',NULL,NULL),
(24,'2023-08-17 05:02:19','2023-08-17 05:02:19',NULL,NULL,'adf801ec-c74e-4953-835d-69f0a69b529a',NULL,NULL,'GUEST ml','$2a$10$Gc0WAZuRe.0NmEYZE2gHkOAiuYKlokwEppFJEKJ5mUbQh/O9mL9Oq',NULL,NULL,'GUEST',NULL,NULL),
(25,'2023-08-17 05:33:14','2023-08-17 05:33:14',NULL,NULL,'dbc03cc6-7748-4193-935d-448441c2f00d',NULL,NULL,'GUEST xc','$2a$10$qpLxfsBEn0IULYd4eMRfSufcPAe.vOgPMryfSMm6hMC/p3a76DUzq',NULL,NULL,'GUEST',NULL,NULL),
(26,'2023-08-17 05:33:39','2023-08-17 05:33:39',NULL,NULL,'3ba38b3b-5756-47b6-be91-6e44a1e75def',NULL,NULL,'GUEST zxvx','$2a$10$l1mUWrJK/68LC6aEz56CHuUkcDJ.9K5/60YWHBeUTBgL43FwMBQO.',NULL,NULL,'GUEST',NULL,NULL),
(27,'2023-08-17 05:34:14','2023-08-17 05:34:14',NULL,NULL,'c23c611e-1e49-477a-af20-ae5923770882',NULL,NULL,'GUEST erer','$2a$10$56eblyLIWjFfyWxuYjRYZe2eUjJXO7EHhWrRpVce/8E/jndahVGya',NULL,NULL,'GUEST',NULL,NULL),
(28,'2023-08-17 05:34:54','2023-08-17 05:34:54',NULL,NULL,'e8204214-8160-4733-b28f-97d19f112a30',NULL,NULL,'GUEST 4646','$2a$10$/6aVclWl3Nu.LTJK0EkX9uSOs1Nmo0vIi.vl/7V2n8zJxwryZNH4.',NULL,NULL,'GUEST',NULL,NULL),
(29,'2023-08-17 05:39:28','2023-08-17 05:39:28',NULL,NULL,'f8be05dd-0b48-46c0-be36-1cd34460f1e5',NULL,NULL,'GUEST lee','$2a$10$eCLG0JC2I105N9vsoEKefO1dKsZpWe53Np/rc9nbDU1dVV/WpWPXS',NULL,NULL,'GUEST',NULL,NULL),
(30,'2023-08-17 08:49:21','2023-08-17 08:49:21',NULL,NULL,'2c87dd68-a849-4456-9162-940cdb942b00',NULL,NULL,'GUEST 019af6ff-5a83-4087-9496-9cb775627ae6 qw','$2a$10$t8lgNOZ8spi3jZKeifT1jeWGlN5E4RS8WqD.djELXM3A2a.203mOy',NULL,NULL,'GUEST',NULL,NULL),
(31,'2023-08-17 08:52:15','2023-08-17 08:52:15',NULL,NULL,'5faa19fe-027e-4b41-bc72-e1aae4b9e3cc',NULL,NULL,'GUEST 544f5bb4-55ad-4ae7-bbfa-dae1324684f1 ㅁㄴㅇ','$2a$10$FE48BYdG1ZEOBzKfflhSR.z2sz8hp13pWFXKIqJ8dsWJz6EifxrlG',NULL,NULL,'GUEST',NULL,NULL),
(32,'2023-08-17 08:52:20','2023-08-17 08:52:20',NULL,NULL,'51eb0c46-c04a-4fee-bf72-febc1c7d25a4',NULL,NULL,'GUEST 52fc31d1-e8e3-44f4-a6e3-b9b56ffb5485 ㅁㄴㅇ','$2a$10$Gs55fzCIxn7dmLn/.s48m.Qa3BECVupYL0rI7pLg8TEsw7c59dTqO',NULL,NULL,'GUEST',NULL,NULL),
(33,'2023-08-17 09:20:41','2023-08-17 09:20:41',NULL,NULL,'bef3f0a9-1f01-4d11-a4ef-68c15b877619',NULL,NULL,'GUEST 83f8ab5f-36a3-4126-adbb-7e77e45ca53e lec','$2a$10$YNGGi7urDgUYvvZjMCL3De.7gx8MBMk9DFf.Amks7ukiTQ2y0UDK6',NULL,NULL,'GUEST',NULL,NULL),
(34,'2023-08-17 09:43:48','2023-08-17 09:43:48',NULL,NULL,'0aba85ac-3558-45a3-84ee-2863c9223537',NULL,NULL,'GUEST 5f0372e6-b35b-490f-87e0-b1bee5100695 안뇽4반임','$2a$10$wz6ob7zbEacu0P0NZadFquEL4p/1bUR.ZHVck/80SpT4Hrx4Fxs9u',NULL,NULL,'GUEST',NULL,NULL),
(35,'2023-08-17 09:55:57','2023-08-17 09:55:57',NULL,NULL,'f3fd34f2-de4b-4d71-8efa-8561cdaab465',NULL,NULL,'GUEST 5ba10970-226b-48c2-b73f-787794af4802 cheese','$2a$10$ohb.0EEc6e/k8W0D19aOTuE2VnpKHWfW6T.Gt7ndU88TIR2t7L1ZK',NULL,NULL,'GUEST',NULL,NULL),
(36,'2023-08-17 11:43:35','2023-08-17 11:43:35',NULL,NULL,'69fd7836-02d3-43eb-88f7-a2a553e0ebaf',NULL,NULL,'GUEST a73b04b7-a4af-4085-9178-03b3f2b3e226 d','$2a$10$VGB3Dt9mVqBh9DmJf9FzO.bgu/aoMn4fT9nl4i5KZ8lYLP/MaHu9a',NULL,NULL,'GUEST',NULL,NULL),
(37,'2023-08-17 12:16:01','2023-08-17 12:16:01',NULL,NULL,'0a359d6a-e51b-4c1f-8759-357f6e883129',NULL,NULL,'GUEST c5396287-2249-468b-81f2-b6575eb9d158 ds','$2a$10$3XKokxr03kkaZ9N6cEX8K.XH0dA9a08xd6PywZQYe6reO1pIe3wFy',NULL,NULL,'GUEST',NULL,NULL),
(38,'2023-08-17 12:45:12','2023-08-17 12:45:12',NULL,NULL,'6245c0ad-5d0d-47ea-a1b7-618dfe128d34',NULL,NULL,'GUEST 88c0d5cd-7597-46aa-a348-be6e66054c1f 뼈다귀','$2a$10$ESwIcvWMtEHjjY1f4raZ3OSgx2Wmdj/2IfxrBeC7Vd4.KIlMCyWGO',NULL,NULL,'GUEST',NULL,NULL),
(39,'2023-08-17 12:53:11','2023-08-17 12:53:11',NULL,NULL,'0d129e3b-cfe0-4803-ac80-7c56061aff1e',NULL,NULL,'GUEST 45ef3821-5ca8-48e9-b837-2bcccf0c5f0f sdsa','$2a$10$zyWff9/hIsdV2OXV6YN.TecCD5.0J/nCAplyl37J1c3TGZM/FjRhe',NULL,NULL,'GUEST',NULL,NULL),
(40,'2023-08-17 13:05:15','2023-08-17 13:05:15',NULL,NULL,'f6f7a861-914b-450e-a9f7-f94ade5cc295',NULL,NULL,'GUEST dc1dbf86-f8eb-461b-98f0-84a0bf296ec5 wersd','$2a$10$gztW3uXXxSgEBiertUNSC.aA10EzeTz.gzCLkmb9LURjNViNaROtC',NULL,NULL,'GUEST',NULL,NULL),
(41,'2023-08-17 13:13:34','2023-08-17 13:13:34',NULL,NULL,'2f9c1e59-4db5-44d9-b500-0a2f97b67406',NULL,NULL,'GUEST 9a44da2d-be9e-4083-8273-8260ca659e2f qwqr','$2a$10$R1QMQAYzN7eI6X/Irgg19OPIPnWfXSXJBYAGbS41SclKgDgWemcVK',NULL,NULL,'GUEST',NULL,NULL),
(42,'2023-08-17 13:32:49','2023-08-17 13:32:49',NULL,NULL,'2725535c-5ee0-4a9e-8604-c90ecb07d31d',NULL,NULL,'GUEST a7f3fb4f-c1de-44c6-afce-7da7287a468a 234','$2a$10$7Wdw/HsEnnboL2mJLSC7KulEZ4IRrZC7TVaE3vpOQM0pT0GrZMUkq',NULL,NULL,'GUEST',NULL,NULL),
(43,'2023-08-17 14:20:07','2023-08-17 14:20:07',NULL,NULL,'453df5e5-1ca1-4ad7-ba0f-03960697a5a9',NULL,NULL,'GUEST 7e87af85-3e87-47a7-aecb-8e3212689c5b dds','$2a$10$1qwHwZkb.RAhRPvT3wDiSuEgO6XLHePYNYADzmoCHmMfRyGv6wh8u',NULL,NULL,'GUEST',NULL,NULL),
(44,'2023-08-17 14:48:28','2023-08-17 18:26:07',6,'N','gofoxfox@naver.com','M','케빈','늑대','$2a$10$aFDa8Pzlqg253H9uKs17mez5lSCdHdQ0TtGgQqaEt.mpuSV/jfZVS','T',NULL,'MEMBER',NULL,NULL),
(46,'2023-08-17 15:01:35','2023-08-17 15:01:35',2,'N','cksgud410@gmail.com','M','develop','lch','$2a$10$6oQg6f6o88nEw5iUZ4ug.e2paBhfrmynKf3hpdl5dhSN/aA.nCUMW','T',NULL,'MEMBER',NULL,NULL),
(47,'2023-08-17 17:14:01','2023-08-17 17:14:01',NULL,NULL,'682d9f81-c62e-40f7-9d91-b77f35ff4772',NULL,NULL,'GUEST 1d373b8a-9a56-4000-a883-37e383b87364 여우','$2a$10$8gOJ8rlAce0nSnO.8rwwSucvAeQ7ddoB./U9u/TEV3mAlHzrlBya6',NULL,NULL,'GUEST',NULL,NULL),
(48,'2023-08-17 19:30:57','2023-08-17 19:30:57',NULL,NULL,'f73988c1-3be8-4551-a5a6-fbc6733ecfb7',NULL,NULL,'GUEST 0cca7b0b-8d00-4792-ad9b-554cf25046f8 14','$2a$10$neQ5tPd3oWwuTN/KkZqMwOZNejtIiUC/Cqt0Un72DjP6NuP2lx5Yu',NULL,NULL,'GUEST',NULL,NULL),
(49,'2023-08-17 19:39:05','2023-08-17 19:39:05',NULL,NULL,'8aa584d0-50bd-43c5-81c9-0d4bafc72a47',NULL,NULL,'GUEST a15d14f6-5658-44c1-b09f-092080f9d66e qe','$2a$10$uQc0/MdTnEaTJVIxgpHk..xv0CXp3m6a0Tp5fxj4BF1kWnWj2uvqy',NULL,NULL,'GUEST',NULL,NULL),
(50,'2023-08-17 21:06:00','2023-08-17 21:06:00',NULL,NULL,'aedb6a73-b6c0-4162-b1ae-6b44384b98b7',NULL,NULL,'GUEST 258ad71e-efe0-4431-a0d8-3023d44ff6bb sdf','$2a$10$TDLClwsr5kx1XSp/.PGP7eSE0bjDgsDalIEx2j55Cqud06S66YkCy',NULL,NULL,'GUEST',NULL,NULL),
(51,'2023-08-17 23:31:39','2023-08-17 23:31:39',NULL,NULL,'82a2da0d-9fe2-406b-95bb-f11cc3ada30a',NULL,NULL,'GUEST ffe67614-322d-40f6-a95c-7c5ad2e4066a Ttt','$2a$10$IYeDX7Fxg4pigWahMM2GquUSAo0ToLqEZV/1k.IOGqVs/wOT3G9l6',NULL,NULL,'GUEST',NULL,NULL),
(52,'2023-08-17 23:40:59','2023-08-17 23:40:59',NULL,NULL,'548dd58e-381c-4532-8040-5425de667bcc',NULL,NULL,'GUEST 3ade53cd-c6b5-4c9f-a0fc-78aa1a3d98ec asd','$2a$10$/3cR9rsoAB.CUx2pBP2B/.YnUkZoJ9z/RcfiCSbu4mEpYkviZsHRG',NULL,NULL,'GUEST',NULL,NULL),
(53,'2023-08-18 00:13:04','2023-08-18 00:13:04',NULL,NULL,'a35eb3c8-4c28-4701-8024-3f54d0183758',NULL,NULL,'GUEST 883450fc-18ea-408e-a6e6-589f8b19c55e sad','$2a$10$/6ns/WjnANpBAl7naNsHFu5ZqJGdI7/11j62lToNyPVUbevEY8Z96',NULL,NULL,'GUEST',NULL,NULL),
(54,'2023-08-18 00:18:40','2023-08-18 00:18:40',NULL,NULL,'71ac3dfa-9410-45dc-afec-81812b16acf0',NULL,NULL,'GUEST e9bf0a1c-dbd9-4694-a78c-1e3debdf5c69 12312','$2a$10$crNkGtgyF/1o39xBvWjz1uIQs/MV.EbnnDfNZccYhYPMSTW3CCg8.',NULL,NULL,'GUEST',NULL,NULL),
(55,'2023-08-18 01:25:51','2023-08-18 01:25:51',NULL,NULL,'bbe1cbcb-ed7a-4d7d-85d3-5f19cd0a3146',NULL,NULL,'GUEST 261625cb-161a-4d2b-a89c-9f75c602feac 12','$2a$10$q/TxdS2UVfP3KJ8T4QhDmueJWs2qVn0XTqA94cFd77uvgqn7vESmm',NULL,NULL,'GUEST',NULL,NULL),
(56,'2023-08-18 01:27:50','2023-08-18 01:27:50',NULL,NULL,'9cf425b9-6005-49b7-b536-5b05d1f372aa',NULL,NULL,'GUEST 66dffaf5-e784-4015-a7ef-f5d1e94f3122 lch970','$2a$10$61wsliw0wK1X2PautqzDs.P8jsvKM7agXzbkhk7j5PSXWafxmgGVy',NULL,NULL,'GUEST',NULL,NULL),
(57,'2023-08-18 01:42:32','2023-08-18 01:42:32',NULL,NULL,'6ad07683-7cb3-4593-9ca6-917477c588f5',NULL,NULL,'GUEST 40e26f8e-734a-4707-94aa-a2c02a54483c 포렛','$2a$10$3vOoNgeWXkj5TgAAiL4GmefIeZqCcDyem6ZI84kKiqDdRUaw5Qo3y',NULL,NULL,'GUEST',NULL,NULL),
(58,'2023-08-18 02:22:21','2023-08-18 02:22:21',NULL,NULL,'812353af-19f5-4c39-b7ba-b44288ab2d9c',NULL,NULL,'GUEST 09352f78-fc4d-4830-bd8c-9ae9f3899926 혜림','$2a$10$c9lqheJJz.u4CeYwSUHozeWeUB5zc2Sc7Jw0bnHd.52FRYS19fIQK',NULL,NULL,'GUEST',NULL,NULL),
(59,'2023-08-18 02:23:13','2023-08-18 02:23:13',NULL,NULL,'3e5deb89-4319-41ac-a2e4-c2374f140ada',NULL,NULL,'GUEST 52641a31-42b5-41f0-9693-ac677ac9d543 태코','$2a$10$8MosZVX/6dh9NPF6KCcU/OsB85qzfph7KSaBAG/2gOs6W8yINqweq',NULL,NULL,'GUEST',NULL,NULL),
(60,'2023-08-18 02:31:08','2023-08-18 02:31:08',NULL,NULL,'0baa0fde-0f0c-42c5-bd97-5f319c0aa8d3',NULL,NULL,'GUEST 1ff2feb4-dbcb-488b-a1b0-d168aed65609 헤림','$2a$10$Non.UEAfOHx46ysj5yRSAe0/rpEVjPzASDzh7Jw/R4JORdii6aZLO',NULL,NULL,'GUEST',NULL,NULL);
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice_article`
--

DROP TABLE IF EXISTS `notice_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notice_article` (
  `content` varchar(255) DEFAULT NULL,
  `hit` bigint(20) NOT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `notice_article_id_to_article_id` FOREIGN KEY (`id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice_article`
--

LOCK TABLES `notice_article` WRITE;
/*!40000 ALTER TABLE `notice_article` DISABLE KEYS */;
INSERT INTO `notice_article` VALUES
('10시 점검입니다',0,'오전 점검 안내',36),
('서버 업데이트 예정',0,'서버 업데이트 안내',37),
('새로운 이벤트 안내',0,'이벤트 공지',38),
('휴일 휴무 안내',0,'휴일 휴무 공지',39),
('코로나19 관련 안내',0,'코로나19 대응',40),
('시스템 정기 점검 안내',0,'시스템 점검',41),
('장애 공지',0,'서비스 장애 안내',42),
('서비스 이용 방법',0,'서비스 이용 안내',43),
('이용 약관 변경 안내',0,'약관 변경',44),
('보안 강화 안내',0,'보안 강화 조치',45),
('새로운 이벤트 안내 - 할인 이벤트가 시작되었습니다!',128,'이벤트 안내',46),
('[주의] 서비스 점검 일정 변경 안내',0,'서비스 점검',47),
('변경된 회원 혜택 안내',0,'회원 혜택 변경',48),
('추석 연휴 배송 지연 안내',0,'배송 지연',49),
('서비스 이용 약관 변경 사항 안내',0,'약관 변경',50),
('시스템 정기 점검 안내',0,'시스템 점검',51),
('장애 공지',0,'서비스 장애 안내',52),
('서비스 이용 방법',0,'서비스 이용 안내',53),
('이용 약관 변경 안내',0,'약관 변경',54),
('보안 강화 안내',0,'보안 강화 조치',55),
('새로운 이벤트 안내 - 할인 이벤트가 시작되었습니다!',128,'이벤트 안내',56),
('[주의] 서비스 점검 일정 변경 안내',0,'서비스 점검',57),
('변경된 회원 혜택 안내',0,'회원 혜택 변경',58),
('추석 연휴 배송 지연 안내',0,'배송 지연',59),
('서비스 이용 약관 변경 사항 안내',0,'약관 변경',60),
('시스템 정기 점검 안내',0,'시스템 점검',61),
('장애 공지',0,'서비스 장애 안내',62),
('서비스 이용 방법',0,'서비스 이용 안내',63),
('이용 약관 변경 안내',0,'약관 변경',64),
('보안 강화 안내',0,'보안 강화 조치',65),
('새로운 이벤트 안내 - 할인 이벤트가 시작되었습니다!',128,'이벤트 안내',66),
('[주의] 서비스 점검 일정 변경 안내',0,'서비스 점검',67),
('변경된 회원 혜택 안내',0,'회원 혜택 변경',68),
('추석 연휴 배송 지연 안내',0,'배송 지연',69),
('서비스 이용 약관 변경 사항 안내',0,'약관 변경',70),
('[주의] 서비스 점검 일정 변경 안내',0,'서비스 점검',71),
('[주의] 서비스 점검 일정 변경 안내',0,'서비스 점검',72),
('코로나19 관련 서비스 이용 안내 - 마스크 착용 필수',0,'코로나19 관련 안내',73),
('서비스 이용약관 변경 사항 안내',0,'약관 변경 안내',74),
('회원 정보 유출 주의 안내',0,'정보 유출 주의',75),
('개인정보 처리 방침 변경',0,'개인정보 처리 방침',76),
('회원 탈퇴 방법 안내',0,'회원 탈퇴 방법',77),
('환경 보호를 위한 포장재 사용 최소화 안내',0,'포장재 사용 최소화',78),
('이메일 서비스 장애 안내',0,'이메일 서비스 장애',79),
('회원 가입 방법 안내',0,'회원 가입 방법',80),
('서비스 이용 안내 - 이용 시간 변경',0,'이용 시간 변경',81),
('서비스 이용 약관 개정 안내',0,'약관 개정 안내',82),
('정기 정검 안내',0,'정기 정검 안내',83),
('안녕하세요 서비스 개편 공지입니다.',0,'서비스 개편 공지',84);
/*!40000 ALTER TABLE `notice_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp NULL DEFAULT NULL,
  `modified_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES
(1,NULL,'2023-08-15 10:21:07','귀엽게'),
(2,NULL,'2023-08-15 10:21:07','멋있게'),
(3,NULL,'2023-08-15 10:21:07','찬란하게'),
(4,NULL,'2023-08-15 10:21:07','소심하게');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-18  3:54:29
