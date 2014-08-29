/*
Navicat MySQL Data Transfer

Source Server         : LocalMySQL
Source Server Version : 50612
Source Host           : 127.0.0.1:3306
Source Database       : fin_news_helper

Target Server Type    : MYSQL
Target Server Version : 50612
File Encoding         : 65001

Date: 2014-08-29 16:40:56
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `keyword`
-- ----------------------------
DROP TABLE IF EXISTS `keyword`;
CREATE TABLE `keyword` (
  `word` varchar(10) DEFAULT NULL,
  `id` int(8) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of keyword
-- ----------------------------

-- ----------------------------
-- Table structure for `mail_account`
-- ----------------------------
DROP TABLE IF EXISTS `mail_account`;
CREATE TABLE `mail_account` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `mailAddr` varchar(255) NOT NULL DEFAULT '',
  `username` varchar(255) NOT NULL DEFAULT '',
  `password` varchar(255) NOT NULL DEFAULT '',
  `userTimes` tinyint(4) NOT NULL DEFAULT '0',
  `isActive` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail_account
-- ----------------------------
INSERT INTO mail_account VALUES ('1', 'workemail2009@126.com', 'workemail2009@126.com', 'lplplplp', '0', '1');

-- ----------------------------
-- Table structure for `news_record`
-- ----------------------------
DROP TABLE IF EXISTS `news_record`;
CREATE TABLE `news_record` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `description` text,
  `newsUrl` varchar(255) DEFAULT NULL,
  `dateTime` bigint(15) DEFAULT NULL,
  `pubfrom` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unquie_url` (`newsUrl`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of news_record
-- ----------------------------

-- ----------------------------
-- Table structure for `notify_hist`
-- ----------------------------
DROP TABLE IF EXISTS `notify_hist`;
CREATE TABLE `notify_hist` (
  `id` varchar(255) NOT NULL,
  `lastNotifyTime` bigint(15) DEFAULT NULL,
  `titleList` text,
  `descList` text,
  `lastNotifyResult` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of notify_hist
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `keywords` text,
  `stocks` text,
  `email` varchar(255) NOT NULL DEFAULT '',
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
