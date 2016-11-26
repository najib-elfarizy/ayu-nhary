/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50536
Source Host           : localhost:3306
Source Database       : pegawai

Target Server Type    : MYSQL
Target Server Version : 50536
File Encoding         : 65001

Date: 2016-11-26 15:24:32
*/

SET FOREIGN_KEY_CHECKS=0;

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
-- Table structure for group_priv
-- ----------------------------
DROP TABLE IF EXISTS `group_priv`;
CREATE TABLE `group_priv` (
  `GROUP_ID` bigint(20) NOT NULL,
  `PRIV_ID` bigint(20) NOT NULL,
  KEY `GROUP_ID` (`GROUP_ID`),
  KEY `PRIV_ID` (`PRIV_ID`),
  CONSTRAINT `group_priv_ibfk_1` FOREIGN KEY (`GROUP_ID`) REFERENCES `app_group` (`GROUP_ID`),
  CONSTRAINT `group_priv_ibfk_2` FOREIGN KEY (`PRIV_ID`) REFERENCES `app_privilege` (`PRIV_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
-- Table structure for jadwal_detail
-- ----------------------------
DROP TABLE IF EXISTS `jadwal_detail`;
CREATE TABLE `jadwal_detail` (
  `ID_DETAIL_JADWAL` bigint(20) NOT NULL,
  `ID_JADWAL` bigint(20) NOT NULL,
  `ID_PROYEK` bigint(20) NOT NULL,
  `NIK` bigint(20) NOT NULL,
  `JAM_MULAI` datetime DEFAULT NULL,
  `JAM_SELESAI` datetime DEFAULT NULL,
  PRIMARY KEY (`ID_DETAIL_JADWAL`),
  KEY `ID_JADWAL` (`ID_JADWAL`),
  KEY `ID_PROYEK` (`ID_PROYEK`),
  KEY `NIK` (`NIK`),
  CONSTRAINT `jadwal_detail_ibfk_1` FOREIGN KEY (`ID_JADWAL`) REFERENCES `jadwal` (`ID_JADWAL`),
  CONSTRAINT `jadwal_detail_ibfk_2` FOREIGN KEY (`ID_PROYEK`) REFERENCES `proyek` (`ID_PROYEK`),
  CONSTRAINT `jadwal_detail_ibfk_3` FOREIGN KEY (`NIK`) REFERENCES `karyawan` (`NIK`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
-- Table structure for keahlian
-- ----------------------------
DROP TABLE IF EXISTS `keahlian`;
CREATE TABLE `keahlian` (
  `KODE_KEAHLIAN` bigint(20) NOT NULL,
  `NAMA_KEAHLIAN` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`KODE_KEAHLIAN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for keahlian_karyawan
-- ----------------------------
DROP TABLE IF EXISTS `keahlian_karyawan`;
CREATE TABLE `keahlian_karyawan` (
  `KODE_KEAHLIAN` bigint(20) NOT NULL,
  `NIK` bigint(20) NOT NULL,
  KEY `NIK` (`NIK`),
  KEY `KODE_KEAHLIAN` (`KODE_KEAHLIAN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
-- Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `USER_ID` bigint(20) NOT NULL DEFAULT '0',
  `GROUP_ID` bigint(20) NOT NULL DEFAULT '0',
  KEY `FK_app_user` (`USER_ID`),
  KEY `FK_app_group` (`GROUP_ID`),
  CONSTRAINT `user_group_ibfk_1` FOREIGN KEY (`GROUP_ID`) REFERENCES `app_group` (`GROUP_ID`),
  CONSTRAINT `user_group_ibfk_2` FOREIGN KEY (`USER_ID`) REFERENCES `app_user` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
