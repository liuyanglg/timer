/*
Navicat MySQL Data Transfer

Source Server         : MySQL-Local-Root
Source Server Version : 50525
Source Host           : localhost:3306
Source Database       : dataserver

Target Server Type    : MYSQL
Target Server Version : 50525
File Encoding         : 65001

Date: 2017-08-18 19:02:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_code_taxid_serviceid
-- ----------------------------
DROP TABLE IF EXISTS `tb_code_taxid_serviceid`;
CREATE TABLE `tb_code_taxid_serviceid` (
  `code` varchar(16) DEFAULT NULL,
  `taxid` varchar(30) DEFAULT NULL,
  `serviceid` varchar(30) DEFAULT NULL,
  KEY `serviceid_index` (`serviceid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
