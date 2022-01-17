-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: rasa
-- ------------------------------------------------------
-- Server version	8.0.20

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
-- Table structure for table `chitietduan`
--

DROP TABLE IF EXISTS `chitietduan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chitietduan` (
  `idda` int NOT NULL,
  `idnv` int NOT NULL,
  PRIMARY KEY (`idda`,`idnv`),
  KEY `fk_duan_has_nhanvien_nhanvien1_idx` (`idnv`),
  KEY `fk_duan_has_nhanvien_duan1_idx` (`idda`),
  CONSTRAINT `fk_duan_has_nhanvien_duan1` FOREIGN KEY (`idda`) REFERENCES `duan` (`idda`),
  CONSTRAINT `fk_duan_has_nhanvien_nhanvien1` FOREIGN KEY (`idnv`) REFERENCES `nhanvien` (`idnv`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chitietduan`
--

LOCK TABLES `chitietduan` WRITE;
/*!40000 ALTER TABLE `chitietduan` DISABLE KEYS */;
INSERT INTO `chitietduan` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(2,6),(2,7),(2,8),(2,9),(2,10),(3,11),(3,12),(3,13),(3,14),(3,15),(4,16),(4,17),(4,18),(4,19),(4,20),(5,21),(5,22),(5,23),(5,24),(5,25);
/*!40000 ALTER TABLE `chitietduan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chitietevent`
--

DROP TABLE IF EXISTS `chitietevent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chitietevent` (
  `idev` int NOT NULL,
  `idnv` int NOT NULL,
  PRIMARY KEY (`idev`,`idnv`),
  KEY `fk_event_has_nhanvien_nhanvien1_idx` (`idnv`),
  KEY `fk_event_has_nhanvien_event1_idx` (`idev`),
  CONSTRAINT `fk_event_has_nhanvien_event1` FOREIGN KEY (`idev`) REFERENCES `event` (`idev`),
  CONSTRAINT `fk_event_has_nhanvien_nhanvien1` FOREIGN KEY (`idnv`) REFERENCES `nhanvien` (`idnv`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chitietevent`
--

LOCK TABLES `chitietevent` WRITE;
/*!40000 ALTER TABLE `chitietevent` DISABLE KEYS */;
INSERT INTO `chitietevent` VALUES (1,1),(1,2),(1,3),(1,4),(2,4),(2,5),(2,6),(2,7),(2,8),(3,8),(3,9),(3,10),(3,11),(4,12),(4,13),(4,14),(4,15),(5,15),(5,16),(5,17),(5,18),(6,19),(6,20),(6,21),(7,22),(7,23),(7,24),(7,25);
/*!40000 ALTER TABLE `chitietevent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chucvu`
--

DROP TABLE IF EXISTS `chucvu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chucvu` (
  `idchucvu` int NOT NULL,
  `tenchucvu` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idchucvu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chucvu`
--

LOCK TABLES `chucvu` WRITE;
/*!40000 ALTER TABLE `chucvu` DISABLE KEYS */;
INSERT INTO `chucvu` VALUES (1,'trưởng phòng'),(2,'phó phòng'),(3,'nhân viên'),(4,'thực tập sinh');
/*!40000 ALTER TABLE `chucvu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `duan`
--

DROP TABLE IF EXISTS `duan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `duan` (
  `idda` int NOT NULL AUTO_INCREMENT,
  `tenda` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `thoigianthuchien` date NOT NULL,
  `kinhphi` bigint NOT NULL,
  `diadiem` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `ghichu` varchar(5000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`idda`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `duan`
--

LOCK TABLES `duan` WRITE;
/*!40000 ALTER TABLE `duan` DISABLE KEYS */;
INSERT INTO `duan` VALUES (1,'Phần mềm Android','2020-06-21',200000,'Tầng 20','xây dựng phần mềm Android'),(2,'Chatbot','2020-05-21',100000,'tòa nhà PVI','xây dựng chatbot office'),(3,'Cơ sở mới','2019-10-09',2000000000,' Tây hồ','xây dựng cơ sở mới có sức chứa trên 2000 người'),(4,'Phần mềm iOS','2020-07-21',300000,'Tầng 15','xây dựng phần mềm iOS'),(5,'Chương trình Web','2020-07-20',400000,'Tầng 13','xây dựng chương trình Web');
/*!40000 ALTER TABLE `duan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `idev` int NOT NULL AUTO_INCREMENT,
  `tenev` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `thoigiantochuc` date NOT NULL,
  `kinhphi` bigint NOT NULL,
  `diadiem` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `ghichu` varchar(5000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`idev`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (1,'Họp','2020-05-04',100000,'tòa nhà pvi','họp kiểm tra'),(2,'Quay số trúng thưởng','2020-05-11',200000,'tầng 1','xem ai trúng quà tết'),(3,'Thi thể thao','2020-05-22',300000,'công viên cầu giấy','thi các hoạt động thể thao nâng cao sức khỏe cho nhân viên'),(4,'Thi esports','2020-05-06',400000,'game center số 13 càu giấy','cuộc thi  cho các game thủ hàng đàu công ty'),(5,'Hoạt động ngoài trời','2020-05-08',500000,'sân công ty','nướng'),(6,'Từ thiện','2020-05-11',600000,'tập chung sảnh công ty','giúp đỡ người nghèo khu vực cầu giấy'),(7,'Bảo vệ môi trường','2020-05-04',700000,'sảnh công ty tập chung','đạp xe  tuyên truyền');
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhanvien`
--

DROP TABLE IF EXISTS `nhanvien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhanvien` (
  `idnv` int NOT NULL AUTO_INCREMENT,
  `hoten` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `ngaysinh` date NOT NULL,
  `gioitinh` varchar(5) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `diachi` varchar(1000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `sdt` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `idpb` int NOT NULL,
  `idchucvu` int NOT NULL,
  `ghichu` varchar(5000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`idnv`),
  KEY `idchucvu_idx` (`idchucvu`),
  KEY `idpb_idx` (`idpb`),
  CONSTRAINT `idchucvu` FOREIGN KEY (`idchucvu`) REFERENCES `chucvu` (`idchucvu`),
  CONSTRAINT `idpb` FOREIGN KEY (`idpb`) REFERENCES `phongban` (`idpb`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhanvien`
--

LOCK TABLES `nhanvien` WRITE;
/*!40000 ALTER TABLE `nhanvien` DISABLE KEYS */;
INSERT INTO `nhanvien` VALUES (1,'Hướng Xuân An','1997-03-29','Nam','triều khúc hà nội','0968124437','an2931997@gmial.com',1,4,'sinh viên thực tập'),(2,'Nguyễn Văn An','2020-03-18','Nữ','số 2 trần phú hà đông hà nội','12422041','abc@gmail.com',2,1,'nhân viên có thâm niên'),(3,'Nguyễn Văn Long','1998-01-01','Nam','số 1 trần phú hà đông hà nội','0123456789','long123456@gmail.com',1,1,'nhân viên có tashc nhiệm, hoàn thành nhiều dự án'),(4,'Hà Minh Hiếu','1997-11-12','Nam','số 12 Nguyễn Trãi, Thanh Xuân, Hà Nội','01234444334','hiu12345@gmail.com',3,1,'không có\r\n'),(5,'Bùi Tuấn Anh','1997-06-05','Nam','số 86 ngõ 122 phùng khoang mỗ lao','019203987462','Tuan123455@gmial.com',4,1,'không có'),(6,'Đào Văn Châu','1997-01-01','Nữ','số 12 đường cầu giấy','0124856123','chauop@gmail.com',6,1,'chăm chỉ làm việc, có trách nhiệm'),(7,'Trần Văn Vũ','1993-12-03','Nam','số 123 đê la thành hà  Nội','03761274123','vu1245@gmail.com',5,1,'thâm niên có cố gắng, cẩn thận'),(8,'Đinh Thị Lý','1994-10-04','Nữ','số 54 Trần duy Hưng , Hà Nội','054812578921','LY123@gmail.com',7,1,'tỉ mỉ chính xác, có khả năng giao tiếp tốt'),(9,'Nguyễn Ngọc Anh','1993-09-08','Nữ','số 34 hồ tùng mậu hà nội','04871245932734','ngocanh@gmial.com',2,2,'không có'),(10,'Vũ Thành Long','1998-03-14','Nam','số 122 Nguyễn Trãi, Thanh Xuân, Hà Nội','0382694327','sepdra143@gmail.com',2,3,'không có'),(11,'Vũ Ngọc Hiếu','1998-05-05','Nam','số 154 Trần duy Hưng , Hà Nội','0246987568','hieu123@gmail.com',2,2,'không có'),(12,'Nguyễn Anh Tuấn','1998-06-06','Nam','số 24 Nguyễn Xiển, Hà Nội','0569431853','tuan123456@gmail.com',1,3,'không có'),(13,'Nguyễn Quang Minh','1998-05-02','Nam','số 48 Nguyễn Du, Hà Nội','0548963496','quangminh@gmail.com',1,4,'không có'),(14,'Bùi Thị Ngọc','1996-06-08','Nữ','số 40 Tô Vĩnh Diện, Hà Nội','0364964792','ngoc23465@gmail.com',3,2,'không có'),(15,'Đoàn Thị Hương','1995-06-07','Nữ','số 60 đường Láng ,Hà Nội','0619493236','huong@gmail.com',3,3,'không có'),(16,'Vũ Đình Khang','1996-03-06','Nam','số 20 Tôn Đức Thắng ','0367493636','khang@gmail.com',4,2,'không có'),(17,'Nguyễn Hương Vi','1995-03-05','Nữ','số 30 Láng, Hà Nội','0664926649','huongvi@gmail.com',4,3,'không có'),(18,'Nguyễn Bích Ngọc ','1997-03-08','Nữ ','số 41 Láng Hạ, Hà Nội','0664941616','bichngoc@gmail.com',5,2,'không có'),(19,'Hoàng Thị Huệ','1998-12-12','Nữ','số 40 Láng, Hà Nội','0654198912','hoanghue@gmail.com',5,3,'không có'),(20,'Nguyễn Thu Hằng','1998-12-12','Nữ','số 420 Tô Vĩnh Diện, Hà Nội','0651111961','thuhang@gmail.com',6,2,'không có'),(21,'Đặng Thu Hà','1992-11-11','Nữ','số 242 Nguyễn Xiển, Hà Nội','0561992399','thuha@gmail.com',6,3,'không có'),(22,'Bùi Minh Hiếu','1991-12-03','Nam','số 342 hồ tùng mậu hà nội','0566199523','minhhieu@gmail.com',7,2,'không có'),(23,'Trần Văn Thanh','1998-05-03','Nam','số 1222 Nguyễn Trãi, Thanh Xuân, Hà Nội','0658949233','vanthanh@gmail.com',7,3,'không có'),(24,'Nguyễn Ngọc Ánh','1992-11-11','Nữ','số 38 Nguyễn Du, Hà Nội','0367493636','ngocanh@gmail.com',2,3,'không có'),(25,'Bùi Tuấn Ngọc','1995-03-05','Nam','số 32 Láng, Hà Nội','0561992399','tuanngoc@gmail.com',2,3,'không có');
/*!40000 ALTER TABLE `nhanvien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phongban`
--

DROP TABLE IF EXISTS `phongban`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phongban` (
  `idpb` int NOT NULL AUTO_INCREMENT,
  `tenpb` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `diadiem` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `sonhanvien` int NOT NULL,
  `ghichu` varchar(5000) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  PRIMARY KEY (`idpb`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phongban`
--

LOCK TABLES `phongban` WRITE;
/*!40000 ALTER TABLE `phongban` DISABLE KEYS */;
INSERT INTO `phongban` VALUES (1,'Nhân sự','tầng 1',20,'quản lý vấn đề nhân sự'),(2,'Hành chính','tầng 1',10,'xử lý vấn đề hành chính'),(3,'Kinh doanh','tầng 2',20,'quản lý vần đề buôn bán sản phẩm'),(4,'Tài chính','tầng 3',15,'phòng tài chính của công ty'),(5,'Marketing','tầng 4',12,'lo vấn đề quảng bá '),(6,'Tester','tầng 11 a',30,'kiểm thử các sản phẩm\r\nđưa ra đánh giá cũng như những vấn đề'),(7,'Kỹ thuật','tầng 10',20,'lo các vấn đề kỹ thuật của công ty');
/*!40000 ALTER TABLE `phongban` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-06 13:46:26
