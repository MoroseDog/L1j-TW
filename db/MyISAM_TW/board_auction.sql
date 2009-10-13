/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50402
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50402
File Encoding         : 65001

Date: 2009-10-13 13:07:19
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `board_auction`
-- ----------------------------
DROP TABLE IF EXISTS `board_auction`;
CREATE TABLE `board_auction` (
  `house_id` int(10) unsigned NOT NULL DEFAULT '0',
  `house_name` varchar(45) NOT NULL DEFAULT '',
  `house_area` int(10) unsigned NOT NULL DEFAULT '0',
  `deadline` datetime DEFAULT NULL,
  `price` int(10) unsigned NOT NULL DEFAULT '0',
  `location` varchar(45) NOT NULL DEFAULT '',
  `old_owner` varchar(45) NOT NULL DEFAULT '',
  `old_owner_id` int(10) unsigned NOT NULL DEFAULT '0',
  `bidder` varchar(45) NOT NULL DEFAULT '',
  `bidder_id` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`house_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of board_auction
-- ----------------------------
INSERT INTO `board_auction` VALUES ('262145', '奇岩血盟小屋1', '78', '2008-02-18 00:00:00', '100000', '奇岩1號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262146', '奇岩血盟小屋2', '45', '2008-02-18 00:00:00', '100000', '奇岩2號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262147', '奇岩血盟小屋3', '120', '2008-02-18 00:00:00', '100000', '奇岩3號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262148', '奇岩血盟小屋4', '45', '2008-02-18 00:00:00', '100000', '奇岩4號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262149', '奇岩血盟小屋5', '78', '2008-02-18 00:00:00', '100000', '奇岩5號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262150', '奇岩血盟小屋6', '120', '2008-02-18 00:00:00', '100000', '奇岩6號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262151', '奇岩血盟小屋7', '45', '2008-02-18 00:00:00', '100000', '奇岩7號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262152', '奇岩血盟小屋8', '78', '2008-02-18 00:00:00', '100000', '奇岩8號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262153', '奇岩血盟小屋9', '78', '2008-02-18 00:00:00', '100000', '奇岩9號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262154', '奇岩血盟小屋10', '120', '2008-02-18 00:00:00', '100000', '奇岩10號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262155', '奇岩血盟小屋11', '78', '2008-02-18 00:00:00', '100000', '奇岩11號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262156', '奇岩血盟小屋12', '78', '2008-02-18 00:00:00', '100000', '奇岩12號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262157', '奇岩血盟小屋13', '120', '2008-02-18 00:00:00', '100000', '奇岩13號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262158', '奇岩血盟小屋14', '78', '2008-02-18 00:00:00', '100000', '奇岩14號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262159', '奇岩血盟小屋15', '45', '2008-02-18 00:00:00', '100000', '奇岩15號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262160', '奇岩血盟小屋16', '78', '2008-02-18 00:00:00', '100000', '奇岩16號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262161', '奇岩血盟小屋17', '45', '2008-02-18 00:00:00', '100000', '奇岩17號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262162', '奇岩血盟小屋18', '120', '2008-02-18 00:00:00', '100000', '奇岩18號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262163', '奇岩血盟小屋19', '78', '2008-02-18 00:00:00', '100000', '奇岩19號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262164', '奇岩血盟小屋20', '78', '2008-02-18 00:00:00', '100000', '奇岩20號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262165', '奇岩血盟小屋21', '45', '2008-02-18 00:00:00', '100000', '奇岩21號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262166', '奇岩血盟小屋22', '120', '2008-02-18 00:00:00', '100000', '奇岩22號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262167', '奇岩血盟小屋23', '78', '2008-02-18 00:00:00', '100000', '奇岩23號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262168', '奇岩血盟小屋24', '45', '2008-02-18 00:00:00', '100000', '奇岩24號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262169', '奇岩血盟小屋25', '45', '2008-02-18 00:00:00', '100000', '奇岩25號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262170', '奇岩血盟小屋26', '120', '2008-02-18 00:00:00', '100000', '奇岩26號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262171', '奇岩血盟小屋27', '78', '2008-02-18 00:00:00', '100000', '奇岩27號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262172', '奇岩血盟小屋28', '45', '2008-02-18 00:00:00', '100000', '奇岩28號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262173', '奇岩血盟小屋29', '78', '2008-02-18 00:00:00', '100000', '奇岩29號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262174', '奇岩血盟小屋30', '45', '2008-02-18 00:00:00', '100000', '奇岩30號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262175', '奇岩血盟小屋31', '78', '2008-02-18 00:00:00', '100000', '奇岩31號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262176', '奇岩血盟小屋32', '78', '2008-02-18 00:00:00', '100000', '奇岩32號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262177', '奇岩血盟小屋33', '45', '2008-02-18 00:00:00', '100000', '奇岩33號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262178', '奇岩血盟小屋34', '45', '2008-02-18 00:00:00', '100000', '奇岩34號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262179', '奇岩血盟小屋35', '120', '2008-02-18 00:00:00', '100000', '奇岩35號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262180', '奇岩血盟小屋36', '45', '2008-02-18 00:00:00', '100000', '奇岩36號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262181', '奇岩血盟小屋37', '78', '2008-02-18 00:00:00', '100000', '奇岩37號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262182', '奇岩血盟小屋38', '78', '2008-02-18 00:00:00', '100000', '奇岩38號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262183', '奇岩血盟小屋39', '45', '2008-02-18 00:00:00', '100000', '奇岩39號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262184', '奇岩血盟小屋40', '78', '2008-02-18 00:00:00', '100000', '奇岩40號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262185', '奇岩血盟小屋41', '78', '2008-02-18 00:00:00', '100000', '奇岩41號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262186', '奇岩血盟小屋42', '45', '2008-02-18 00:00:00', '100000', '奇岩42號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262187', '奇岩血盟小屋43', '120', '2008-02-18 00:00:00', '100000', '奇岩43號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262188', '奇岩血盟小屋44', '120', '2008-02-18 00:00:00', '100000', '奇岩44號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('262189', '奇岩血盟小屋45', '78', '2008-02-18 00:00:00', '100000', '奇岩45號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('327681', '海音血盟小屋1', '0', '2008-02-18 00:00:00', '100000', '海音1號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('327682', '海音血盟小屋2', '0', '2008-02-18 00:00:00', '100000', '海音2號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('327683', '海音血盟小屋3', '0', '2008-02-18 00:00:00', '100000', '海音3號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('327684', '海音血盟小屋4', '0', '2008-02-18 00:00:00', '100000', '海音4號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('327685', '海音血盟小屋5', '0', '2008-02-18 00:00:00', '100000', '海音5號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('327686', '海音血盟小屋6', '0', '2008-02-18 00:00:00', '100000', '海音6號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('327687', '海音血盟小屋7', '0', '2008-02-18 00:00:00', '100000', '海音7號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('327688', '海音血盟小屋8', '0', '2008-02-18 00:00:00', '100000', '海音8號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('327689', '海音血盟小屋9', '0', '2008-02-18 00:00:00', '100000', '海音9號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('327690', '海音血盟小屋10', '0', '2008-02-18 00:00:00', '100000', '海音10號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('327691', '海音血盟小屋11', '0', '2008-02-18 00:00:00', '100000', '海音11號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('524289', '古魯丁血盟小屋1', '48', '2008-09-20 00:00:00', '100000', '古魯丁1號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('524290', '古魯丁血盟小屋2', '71', '2008-09-20 00:00:00', '100000', '古魯丁2號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('524291', '古魯丁血盟小屋3', '48', '2008-09-20 00:00:00', '100000', '古魯丁3號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('524292', '古魯丁血盟小屋4', '48', '2008-09-20 00:00:00', '100000', '古魯丁4號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('524293', '古魯丁血盟小屋5', '82', '2008-09-20 00:00:00', '100000', '古魯丁5號血盟小屋', '', '0', '', '0');
INSERT INTO `board_auction` VALUES ('524294', '古魯丁血盟小屋6', '40', '2008-09-20 00:00:00', '100000', '古魯丁6號血盟小屋', '', '0', '', '0');
