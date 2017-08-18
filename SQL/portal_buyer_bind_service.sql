/*
Navicat MySQL Data Transfer

Source Server         : MySQL-Local-Root
Source Server Version : 50525
Source Host           : localhost:3306
Source Database       : platformkf

Target Server Type    : MYSQL
Target Server Version : 50525
File Encoding         : 65001

Date: 2017-08-18 19:03:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for portal_buyer_bind_service
-- ----------------------------
DROP TABLE IF EXISTS `portal_buyer_bind_service`;
CREATE TABLE `portal_buyer_bind_service` (
  `c_id` varchar(35) NOT NULL DEFAULT '',
  `c_userid` varchar(35) DEFAULT NULL COMMENT '用户id（shop_buyer中的c_uid）',
  `c_keyid` varchar(35) DEFAULT NULL COMMENT 'keyid',
  `c_texnum` varchar(30) DEFAULT NULL COMMENT '税号',
  `c_serviceid` varchar(30) DEFAULT NULL COMMENT '服务单位id',
  `crmNo` varchar(10) DEFAULT NULL,
  `departId` varchar(16) DEFAULT NULL COMMENT '部门id',
  `dt_adddate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  `dt_editdate` datetime DEFAULT NULL COMMENT '修改时间',
  `c_sysid` char(2) DEFAULT NULL COMMENT '数据来源',
  `c_syncstate` char(2) DEFAULT NULL COMMENT '是否已同步',
  `c_areacode` char(8) DEFAULT '' COMMENT '地区',
  `c_realname` varchar(35) DEFAULT NULL COMMENT '真实姓名',
  `c_faxnum` varchar(20) DEFAULT NULL COMMENT '传真号',
  `c_servicetaxnum` varchar(30) DEFAULT NULL COMMENT '服务税号',
  `c_taxauthorityid` varchar(35) DEFAULT NULL,
  `c_companyname` varchar(100) DEFAULT NULL COMMENT '公司名称',
  `c_address` varchar(100) DEFAULT NULL COMMENT '公司地址',
  PRIMARY KEY (`c_id`),
  KEY `c_texnum` (`c_texnum`) USING BTREE,
  KEY `c_serviceid` (`c_serviceid`) USING BTREE,
  KEY `c_keyidAndcrmNo` (`c_keyid`,`crmNo`) USING BTREE,
  KEY `c_userid` (`c_userid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户和服务单位关联中间表';
