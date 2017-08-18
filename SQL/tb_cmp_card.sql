/*
Navicat MySQL Data Transfer

Source Server         : MySQL-Local-Root
Source Server Version : 50525
Source Host           : localhost:3306
Source Database       : dataserver

Target Server Type    : MYSQL
Target Server Version : 50525
File Encoding         : 65001

Date: 2017-08-18 19:02:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_cmp_card
-- ----------------------------
DROP TABLE IF EXISTS `tb_cmp_card`;
CREATE TABLE `tb_cmp_card` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(16) NOT NULL COMMENT '六位代码',
  `taxid` varchar(30) NOT NULL COMMENT '公司税号，纳税人识别号',
  `name` varchar(200) NOT NULL COMMENT '公司名称',
  `address` varchar(200) DEFAULT NULL COMMENT '注册地址',
  `telephone` varchar(30) DEFAULT NULL COMMENT '联系电话',
  `bank` varchar(200) DEFAULT NULL COMMENT '开户银行',
  `account` varchar(50) DEFAULT NULL COMMENT '银行账户',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态，0-可用， 9-删除',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '0-企业，1-个体工商户',
  `cert` int(11) NOT NULL DEFAULT '0' COMMENT '0-未认证，1-已认证',
  `source` int(11) NOT NULL DEFAULT '0' COMMENT '来源：\n0-未知\n10-防伪开票软件\n11-防伪开票软件(百旺)\n20-CRM\n30-诺诺网\n31-微信H5\n40-用户中心\n99-ADMIN',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最近修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `taxid_UNIQUE` (`taxid`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `idx_updatetime` (`updatetime`),
  KEY `idx_createtime` (`createtime`)
) ENGINE=InnoDB AUTO_INCREMENT=11872786 DEFAULT CHARSET=utf8 COMMENT='企业名片信息表\r\n\r\n\r\n开票服务-企业信息：\r\n• 税号（唯一索引）\r\n• 名称 (not null) \r\n• 注册地址(null)\r\n• 注册电话(null)\r\n• 开户银行(null)\r\n• 银行账户(null)\r\n• 六位代码（唯一索引）';
