/*
Navicat MySQL Data Transfer

Source Server         : LOCAL
Source Server Version : 50550
Source Host           : localhost:3306
Source Database       : pegawai

Target Server Type    : MYSQL
Target Server Version : 50550
File Encoding         : 65001

Date: 2016-11-29 17:21:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app_config
-- ----------------------------
DROP TABLE IF EXISTS `app_config`;
CREATE TABLE `app_config` (
  `CONFIG_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONFIG_NAME` varchar(255) NOT NULL,
  `CONFIG_VALUE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`CONFIG_ID`),
  UNIQUE KEY `config_name_unique` (`CONFIG_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_config
-- ----------------------------
INSERT INTO `app_config` VALUES ('1', 'app_brand', 'PT. SASTRA  MAS Estetika');
INSERT INTO `app_config` VALUES ('2', 'app_desc', 'PT. SASTRA  MAS Estetika adalah perusahaan yang dipercaya dengan kualitas yang dihasilkan. Hal ini menjadikan PT. SASTRA  MAS Estetika menjadi salah satu perusahaan yang dipercaya di wilayah Sesetan dan sekitarnya');
INSERT INTO `app_config` VALUES ('3', 'app_footer', '&copy Copyright 2015. All rights reserved<br/> PT. SASTRA  MAS Estetika - Denpasar Bali');

-- ----------------------------
-- Table structure for app_group
-- ----------------------------
DROP TABLE IF EXISTS `app_group`;
CREATE TABLE `app_group` (
  `GROUP_ID` bigint(20) NOT NULL,
  `GROUP_NAME` varchar(30) DEFAULT NULL,
  `DESCRIPTION` text,
  PRIMARY KEY (`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_group
-- ----------------------------
INSERT INTO `app_group` VALUES ('504404604166397359', 'Admin', 'Admin');
INSERT INTO `app_group` VALUES ('504404615225112675', 'Management', 'Management');
INSERT INTO `app_group` VALUES ('504404615225199773', 'Pegawai', 'Pegawai');

-- ----------------------------
-- Table structure for app_privilege
-- ----------------------------
DROP TABLE IF EXISTS `app_privilege`;
CREATE TABLE `app_privilege` (
  `PRIV_ID` bigint(20) NOT NULL,
  `PRIV_NAME` varchar(100) DEFAULT NULL,
  `DESCRIPTION` text,
  PRIMARY KEY (`PRIV_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_privilege
-- ----------------------------
INSERT INTO `app_privilege` VALUES ('1', 'User Management', 'User Management');
INSERT INTO `app_privilege` VALUES ('2', 'Maintenance Master Data', 'Maintenance Master Data');
INSERT INTO `app_privilege` VALUES ('3', 'Pembuatan Jadwal', 'Membuat Jadwal Dengan Algoritma Genetika');
INSERT INTO `app_privilege` VALUES ('4', 'Lihat Master Data', 'Lihat Master Data');
INSERT INTO `app_privilege` VALUES ('5', 'Lihat Jadwal', 'Lihat Jadwal');
INSERT INTO `app_privilege` VALUES ('10', 'Setting Aplikasi', 'Settingan Aplikasi');

-- ----------------------------
-- Table structure for app_user
-- ----------------------------
DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user` (
  `USER_ID` bigint(20) NOT NULL,
  `LOGIN_ID` varchar(30) DEFAULT NULL,
  `PASSWORD` varchar(30) DEFAULT NULL,
  `FULL_NAME` varchar(50) DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `USER_STATUS` int(1) DEFAULT '0',
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of app_user
-- ----------------------------
INSERT INTO `app_user` VALUES ('504404604167511665', 'admin', 'admin', 'Administrator', 'admin@admin.com', '2');
INSERT INTO `app_user` VALUES ('504404615225007512', 'maryono', 'maryono', 'Maryono', 'maryono@yahoo.com', '1');
INSERT INTO `app_user` VALUES ('504404615225228269', 'jaka', 'jaka', 'Jaka', 'jaka@yahoo.com', '2');
INSERT INTO `app_user` VALUES ('504404615225264352', 'joni', 'joni', 'Joni', 'joni@yahoo.com', '1');
INSERT INTO `app_user` VALUES ('504404615225294386', 'hadi', 'hadi', 'Hadi', 'hadi@yahoo.com', '1');
INSERT INTO `app_user` VALUES ('504404619377934027', 'test', 'test', 'test', 'test@test.com', '1');
INSERT INTO `app_user` VALUES ('504404619378270041', 'testing', 'test', 'test', 'test@test.com', '1');
INSERT INTO `app_user` VALUES ('504404638599962793', 'ayu', 'ayu', 'Ayu Nhary', 'ayu.nhary@gmail.com', '2');

-- ----------------------------
-- Table structure for group_priv
-- ----------------------------
DROP TABLE IF EXISTS `group_priv`;
CREATE TABLE `group_priv` (
  `GROUP_ID` bigint(20) NOT NULL,
  `PRIV_ID` bigint(20) NOT NULL,
  KEY `GROUP_ID` (`GROUP_ID`) USING BTREE,
  KEY `PRIV_ID` (`PRIV_ID`) USING BTREE,
  CONSTRAINT `group_priv_ibfk_1` FOREIGN KEY (`GROUP_ID`) REFERENCES `app_group` (`GROUP_ID`),
  CONSTRAINT `group_priv_ibfk_2` FOREIGN KEY (`PRIV_ID`) REFERENCES `app_privilege` (`PRIV_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of group_priv
-- ----------------------------
INSERT INTO `group_priv` VALUES ('504404615225112675', '2');
INSERT INTO `group_priv` VALUES ('504404615225112675', '3');
INSERT INTO `group_priv` VALUES ('504404615225112675', '5');
INSERT INTO `group_priv` VALUES ('504404615225199773', '5');
INSERT INTO `group_priv` VALUES ('504404604166397359', '1');
INSERT INTO `group_priv` VALUES ('504404604166397359', '2');
INSERT INTO `group_priv` VALUES ('504404604166397359', '3');
INSERT INTO `group_priv` VALUES ('504404604166397359', '5');
INSERT INTO `group_priv` VALUES ('504404604166397359', '10');

-- ----------------------------
-- Table structure for jadwal
-- ----------------------------
DROP TABLE IF EXISTS `jadwal`;
CREATE TABLE `jadwal` (
  `ID_JADWAL` bigint(20) NOT NULL,
  `NAMA_JADWAL` varchar(100) DEFAULT NULL,
  `STATUS` int(1) DEFAULT '0',
  `NILAI_FITNESS` float DEFAULT '0',
  PRIMARY KEY (`ID_JADWAL`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of jadwal
-- ----------------------------
INSERT INTO `jadwal` VALUES ('504404619291471823', 'Jadwal 1', '0', '0');
INSERT INTO `jadwal` VALUES ('504404619291479451', 'Jadwal 2', '0', '0');
INSERT INTO `jadwal` VALUES ('504404619291486577', 'Jadwal 3', '0', '0');
INSERT INTO `jadwal` VALUES ('504404619375670280', 'Jadwal Confirm', '1', '0.3');

-- ----------------------------
-- Table structure for jadwal_detail
-- ----------------------------
DROP TABLE IF EXISTS `jadwal_detail`;
CREATE TABLE `jadwal_detail` (
  `ID_DETAIL_JADWAL` bigint(20) NOT NULL,
  `ID_JADWAL` bigint(20) NOT NULL,
  `ID_PROYEK` bigint(20) NOT NULL,
  `NIP` bigint(20) NOT NULL,
  `JAM_MULAI` datetime DEFAULT NULL,
  `JAM_SELESAI` datetime DEFAULT NULL,
  PRIMARY KEY (`ID_DETAIL_JADWAL`),
  KEY `ID_JADWAL` (`ID_JADWAL`) USING BTREE,
  KEY `ID_PROYEK` (`ID_PROYEK`) USING BTREE,
  KEY `NIP` (`NIP`) USING BTREE,
  CONSTRAINT `jadwal_detail_ibfk_1` FOREIGN KEY (`ID_JADWAL`) REFERENCES `jadwal` (`ID_JADWAL`),
  CONSTRAINT `jadwal_detail_ibfk_2` FOREIGN KEY (`ID_PROYEK`) REFERENCES `proyek` (`ID_PROYEK`),
  CONSTRAINT `jadwal_detail_ibfk_3` FOREIGN KEY (`NIP`) REFERENCES `pegawai` (`NIP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of jadwal_detail
-- ----------------------------
INSERT INTO `jadwal_detail` VALUES ('504404619291497610', '504404619291471823', '504404617128338998', '504404617547614263', '2016-04-01 17:00:00', '2016-04-01 17:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291509327', '504404619291479451', '504404617636006567', '504404617547632275', '2016-04-05 17:00:00', '2016-04-05 17:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291517144', '504404619291471823', '504404617813390357', '504404617547614263', '2016-04-01 17:00:00', '2016-04-01 17:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291527064', '504404619291471823', '504404617813423414', '504404617813542610', '2016-04-01 17:00:00', '2016-04-01 17:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291536477', '504404619291471823', '504404617813464272', '504404617547614263', '2016-04-01 17:00:00', '2016-04-01 17:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291549228', '504404619291479451', '504404617128338998', '504404617813583952', '2016-04-01 17:00:00', '2016-04-01 17:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291564076', '504404619291479451', '504404617636006567', '504404617547614263', '2016-04-01 17:00:00', '2016-04-01 17:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291586648', '504404619291479451', '504404617813390357', '504404617813542610', '2016-04-01 17:00:00', '2016-04-01 17:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291603509', '504404619291479451', '504404617813423414', '504404617813560002', '2016-04-01 17:00:00', '2016-04-01 17:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291615263', '504404619291471823', '504404617813464272', '504404617547632275', '2016-04-01 17:35:00', '2016-04-01 17:35:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291649376', '504404619291486577', '504404617128338998', '504404617547632275', '2016-04-01 17:35:00', '2016-04-01 17:35:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291666581', '504404619291486577', '504404617636006567', '504404617547632275', '2016-04-01 17:35:00', '2016-04-01 17:35:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291682758', '504404619291486577', '504404617813390357', '504404617547614263', '2016-04-05 17:00:00', '2016-04-05 17:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291695000', '504404619291486577', '504404617813423414', '504404617547614263', '2016-04-05 17:00:00', '2016-04-05 17:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619291721738', '504404619291486577', '504404617813464272', '504404617813583952', '2016-04-01 17:00:00', '2016-04-01 17:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619375670468', '504404619375670280', '504404617128338998', '504404617547632275', '2016-02-03 16:40:00', '2016-02-03 18:40:00');
INSERT INTO `jadwal_detail` VALUES ('504404619375670570', '504404619375670280', '504404617636006567', '504404617813583952', '2016-03-03 13:00:00', '2016-03-04 13:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619375670650', '504404619375670280', '504404617813390357', '504404617547614263', '2016-04-01 14:00:00', '2016-04-07 14:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619375670691', '504404619375670280', '504404617813423414', '504404617813542610', '2016-04-01 14:00:00', '2016-04-01 19:00:00');
INSERT INTO `jadwal_detail` VALUES ('504404619375670725', '504404619375670280', '504404617813464272', '504404617813560002', '2016-04-01 07:00:00', '2016-04-01 19:00:00');

-- ----------------------------
-- Table structure for karyawan
-- ----------------------------
DROP TABLE IF EXISTS `karyawan`;
CREATE TABLE `karyawan` (
  `NIK` bigint(20) NOT NULL,
  `NAMA_KARYAWAN` varchar(100) DEFAULT NULL,
  `TTL` datetime DEFAULT NULL,
  `JABATAN` varchar(30) DEFAULT NULL,
  `ALAMAT` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`NIK`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of karyawan
-- ----------------------------
INSERT INTO `karyawan` VALUES ('504404617547614263', 'Maryono', '2016-03-29 00:00:00', 'Admin', 'Denpasar');
INSERT INTO `karyawan` VALUES ('504404617547632275', 'Agus', '2016-03-29 00:00:00', 'Teknisi', 'Denpasar');
INSERT INTO `karyawan` VALUES ('504404617813542610', 'Putu', '2016-04-01 00:00:00', 'Teknisi', 'Denpasar');
INSERT INTO `karyawan` VALUES ('504404617813560002', 'Made', '2016-04-01 00:00:00', 'Teknisi', 'Denpasar');
INSERT INTO `karyawan` VALUES ('504404617813583952', 'Eko', '2016-04-01 00:00:00', 'Teknisi', 'Denpasar');
INSERT INTO `karyawan` VALUES ('504404638560171224', '', '2016-11-28 00:00:00', '', '');

-- ----------------------------
-- Table structure for keahlian
-- ----------------------------
DROP TABLE IF EXISTS `keahlian`;
CREATE TABLE `keahlian` (
  `KODE_KEAHLIAN` bigint(20) NOT NULL,
  `NAMA_KEAHLIAN` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`KODE_KEAHLIAN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of keahlian
-- ----------------------------
INSERT INTO `keahlian` VALUES ('504404617546586367', 'Komputer Jaringan');
INSERT INTO `keahlian` VALUES ('504404617546630643', 'Komputer Software');
INSERT INTO `keahlian` VALUES ('504404617546640526', 'Komputer Hardware');

-- ----------------------------
-- Table structure for keahlian_karyawan
-- ----------------------------
DROP TABLE IF EXISTS `keahlian_karyawan`;
CREATE TABLE `keahlian_karyawan` (
  `KODE_KEAHLIAN` bigint(20) NOT NULL,
  `NIK` bigint(20) NOT NULL,
  KEY `NIK` (`NIK`) USING BTREE,
  KEY `KODE_KEAHLIAN` (`KODE_KEAHLIAN`) USING BTREE,
  CONSTRAINT `keahlian_karyawan_ibfk_1` FOREIGN KEY (`NIK`) REFERENCES `karyawan` (`NIK`),
  CONSTRAINT `keahlian_karyawan_ibfk_2` FOREIGN KEY (`KODE_KEAHLIAN`) REFERENCES `keahlian` (`KODE_KEAHLIAN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of keahlian_karyawan
-- ----------------------------
INSERT INTO `keahlian_karyawan` VALUES ('504404617546586367', '504404617547614263');
INSERT INTO `keahlian_karyawan` VALUES ('504404617546640526', '504404617547632275');
INSERT INTO `keahlian_karyawan` VALUES ('504404617546630643', '504404617813542610');
INSERT INTO `keahlian_karyawan` VALUES ('504404617546586367', '504404617813560002');
INSERT INTO `keahlian_karyawan` VALUES ('504404617546640526', '504404617813583952');
INSERT INTO `keahlian_karyawan` VALUES ('504404617546586367', '504404638560171224');

-- ----------------------------
-- Table structure for pegawai
-- ----------------------------
DROP TABLE IF EXISTS `pegawai`;
CREATE TABLE `pegawai` (
  `NIP` bigint(20) NOT NULL,
  `NAMA_PEGAWAI` varchar(100) DEFAULT NULL,
  `JENIS_KELAMIN` char(1) DEFAULT NULL,
  `TEMPAT_LAHIR` varchar(255) DEFAULT NULL,
  `TGL_LAHIR` datetime DEFAULT NULL,
  `ALAMAT` varchar(100) DEFAULT NULL,
  `PENDIDIKAN` varchar(255) DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `JABATAN` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`NIP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of pegawai
-- ----------------------------
INSERT INTO `pegawai` VALUES ('504404617547614263', 'Maryono', 'L', null, '2016-03-29 00:00:00', 'Denpasar', 'S1', null, 'Admin');
INSERT INTO `pegawai` VALUES ('504404617547632275', 'Agus', 'L', '', '1990-11-28 00:00:00', 'JL. Pulau Batam Denpasar', 'S1', 'agus@gmail.com', 'Teknisi');
INSERT INTO `pegawai` VALUES ('504404617813542610', 'Putu', 'L', null, '2016-04-01 00:00:00', 'Denpasar', 'D3', null, 'Teknisi');
INSERT INTO `pegawai` VALUES ('504404617813560002', 'Made', 'L', null, '2016-04-01 00:00:00', 'Denpasar', 'D3', null, 'Teknisi');
INSERT INTO `pegawai` VALUES ('504404617813583952', 'Eko', 'L', null, '2016-04-01 00:00:00', 'Denpasar', 'D3', null, 'Teknisi');
INSERT INTO `pegawai` VALUES ('504404638606399810', 'Ayu Nhary', 'P', '', '1994-03-09 00:00:00', 'JL. Tukad Pakerisan 100x', 'Strata 1', 'ayu.nhary@gmail.com', 'Sekertaris');

-- ----------------------------
-- Table structure for proyek
-- ----------------------------
DROP TABLE IF EXISTS `proyek`;
CREATE TABLE `proyek` (
  `ID_PROYEK` bigint(20) NOT NULL,
  `NAMA_PROYEK` varchar(100) DEFAULT NULL,
  `WAKTU_MULAI` datetime DEFAULT NULL,
  `WAKTU_SELESAI` datetime DEFAULT NULL,
  `TOTAL` float DEFAULT NULL,
  PRIMARY KEY (`ID_PROYEK`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of proyek
-- ----------------------------
INSERT INTO `proyek` VALUES ('504404617128338998', 'Maintenance Komputer PT. Asaparis', '2016-02-03 16:40:00', '2016-02-03 18:40:00', '2');
INSERT INTO `proyek` VALUES ('504404617636006567', 'Maintenance Jaringan PT. Duta Karya', '2016-03-03 13:00:00', '2016-03-04 13:00:00', '24');
INSERT INTO `proyek` VALUES ('504404617813390357', 'Pemasangan Jaringan RS. Kasih Ibu', '2016-04-01 14:00:00', '2016-04-07 14:00:00', '144');
INSERT INTO `proyek` VALUES ('504404617813423414', 'Instalasi Software Surya Mandiri', '2016-04-01 14:00:00', '2016-04-01 19:00:00', '5');
INSERT INTO `proyek` VALUES ('504404617813464272', 'Maintenance Jaringan Hotel Aneka', '2016-04-01 07:00:00', '2016-04-01 19:00:00', '12');

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `USER_ID` bigint(20) NOT NULL DEFAULT '0',
  `GROUP_ID` bigint(20) NOT NULL DEFAULT '0',
  KEY `FK_app_user` (`USER_ID`) USING BTREE,
  KEY `FK_app_group` (`GROUP_ID`) USING BTREE,
  CONSTRAINT `user_group_ibfk_1` FOREIGN KEY (`GROUP_ID`) REFERENCES `app_group` (`GROUP_ID`),
  CONSTRAINT `user_group_ibfk_2` FOREIGN KEY (`USER_ID`) REFERENCES `app_user` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_group
-- ----------------------------
INSERT INTO `user_group` VALUES ('504404604167511665', '504404604166397359');
INSERT INTO `user_group` VALUES ('504404615225228269', '504404615225112675');
INSERT INTO `user_group` VALUES ('504404615225264352', '504404615225199773');
INSERT INTO `user_group` VALUES ('504404638599962793', '504404604166397359');
