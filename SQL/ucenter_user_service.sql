/*
Navicat MySQL Data Transfer

Source Server         : MySQL-Local-Root
Source Server Version : 50525
Source Host           : localhost:3306
Source Database       : usercenter

Target Server Type    : MYSQL
Target Server Version : 50525
File Encoding         : 65001

Date: 2017-08-18 19:02:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ucenter_user_service
-- ----------------------------
DROP TABLE IF EXISTS `ucenter_user_service`;
CREATE TABLE `ucenter_user_service` (
  `c_id` varchar(35) NOT NULL DEFAULT '' COMMENT '主键id',
  `c_user_id` varchar(35) DEFAULT NULL COMMENT '用户id',
  `c_key_id` varchar(35) DEFAULT NULL COMMENT 'CRM主键id',
  `c_serviceid` varchar(35) DEFAULT NULL COMMENT '服务单位id',
  `c_crmNo` varchar(10) DEFAULT NULL COMMENT 'CRMno号',
  `c_taxnum` varchar(35) DEFAULT NULL COMMENT '税号',
  `c_depart_id` varchar(35) DEFAULT NULL COMMENT '部门id',
  `dt_adddate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  `dt_editdate` datetime DEFAULT NULL COMMENT '修改时间',
  `c_sysid` char(2) DEFAULT '9' COMMENT '系统数据来源标记,{"0","CRM"},{"1","REMOTE"},{"2","SHOP"},{"3","WEIXIN"},{"4","RZPT"},{"5","SuperMarket"},{"6","CLOUDACCOUNT"},{"7","APP"},{"8","PORTAL"}{9:""ucenterUser}{10:"开放平台"}}{11:"oa"}',
  `c_taxbureau` varchar(50) DEFAULT NULL COMMENT '所属税局',
  `c_field_id2` varchar(50) DEFAULT NULL COMMENT '备用字段',
  `c_field_id3` varchar(50) DEFAULT NULL COMMENT '备用字段',
  `c_field_id4` varchar(50) DEFAULT NULL COMMENT '备用字段',
  `c_isvip` char(2) DEFAULT '0' COMMENT '是否vip：0否1是',
  PRIMARY KEY (`c_id`),
  UNIQUE KEY `c_id` (`c_id`) USING BTREE,
  KEY `c_user_id` (`c_user_id`) USING BTREE,
  KEY `c_key_id` (`c_key_id`) USING BTREE,
  KEY `c_serviceid` (`c_serviceid`) USING BTREE,
  KEY `c_crmNo` (`c_crmNo`) USING BTREE,
  KEY `c_taxnum` (`c_taxnum`) USING BTREE,
  KEY `c_taxbureau` (`c_taxbureau`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户服务单位表';
