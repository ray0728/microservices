-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 129.9.159.4    Database: account
-- ------------------------------------------------------
-- Server version	5.7.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `account`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `account` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `account`;

--
-- Table structure for table `t_account`
--

DROP TABLE IF EXISTS `t_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `first_time` bigint(20) DEFAULT '0',
  `signature` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT 'This person is lazy and has no messages.',
  `resume` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatar` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `times` int(11) DEFAULT '0',
  `last_login` bigint(20) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_account`
--

LOCK TABLES `t_account` WRITE;
/*!40000 ALTER TABLE `t_account` DISABLE KEYS */;
INSERT INTO `t_account` VALUES (1,'ray0728','$2a$10$a3Ecwyh/F6ZfS9rBiHJf9.KDyud7Yv7KXTKga1gvzfBd1d9CQitCi','51101661@qq.com',0,20190812110240,'This person is lazy and has no messages.',NULL,NULL,0,0);
/*!40000 ALTER TABLE `t_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_account_role_map`
--

DROP TABLE IF EXISTS `t_account_role_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_account_role_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `rid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=armscii8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_account_role_map`
--

LOCK TABLES `t_account_role_map` WRITE;
/*!40000 ALTER TABLE `t_account_role_map` DISABLE KEYS */;
INSERT INTO `t_account_role_map` VALUES (1,1,1),(2,2,1),(3,3,1),(4,4,1),(5,5,1),(6,6,1),(7,7,1),(8,8,1),(9,1,1),(10,1,1),(11,1,1),(12,2,1),(13,3,1);
/*!40000 ALTER TABLE `t_account_role_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_group`
--

DROP TABLE IF EXISTS `t_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `desc` varchar(45) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `create_date` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_group`
--

LOCK TABLES `t_group` WRITE;
/*!40000 ALTER TABLE `t_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_group_member`
--

DROP TABLE IF EXISTS `t_group_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_group_member` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gid` int(11) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_group_member`
--

LOCK TABLES `t_group_member` WRITE;
/*!40000 ALTER TABLE `t_group_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_group_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_invitation_code`
--

DROP TABLE IF EXISTS `t_invitation_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_invitation_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `fuid` int(11) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `used` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_invitation_code`
--

LOCK TABLES `t_invitation_code` WRITE;
/*!40000 ALTER TABLE `t_invitation_code` DISABLE KEYS */;
INSERT INTO `t_invitation_code` VALUES (1,0,1,'Ray20!6O7zB',0);
/*!40000 ALTER TABLE `t_invitation_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_role`
--

DROP TABLE IF EXISTS `t_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=armscii8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_role`
--

LOCK TABLES `t_role` WRITE;
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `resource`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `resource` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `resource`;

--
-- Table structure for table `t_account_category_map`
--

DROP TABLE IF EXISTS `t_account_category_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_account_category_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `type_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_account_category_map`
--

LOCK TABLES `t_account_category_map` WRITE;
/*!40000 ALTER TABLE `t_account_category_map` DISABLE KEYS */;
INSERT INTO `t_account_category_map` VALUES (1,2,1),(2,2,2),(3,2,4),(4,2,3),(5,2,5);
/*!40000 ALTER TABLE `t_account_category_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_category`
--

DROP TABLE IF EXISTS `t_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `desc` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_category`
--

LOCK TABLES `t_category` WRITE;
/*!40000 ALTER TABLE `t_category` DISABLE KEYS */;
INSERT INTO `t_category` VALUES (1,'游记'),(2,'家庭日记'),(3,'test'),(4,'test'),(5,'家装');
/*!40000 ALTER TABLE `t_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_log`
--

DROP TABLE IF EXISTS `t_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `gid` int(11) DEFAULT '0',
  `title` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log`
--

LOCK TABLES `t_log` WRITE;
/*!40000 ALTER TABLE `t_log` DISABLE KEYS */;
INSERT INTO `t_log` VALUES (1,2,1,0,'hhhh',20190806114432,3),(2,2,2,0,'I am RAY',20190808062914,3),(3,2,2,0,'I am RAY',20190808062918,3),(4,2,1,0,'I am RAY',20190808062921,0),(5,2,1,0,NULL,20190809110911,3),(6,2,1,0,'yyy',20190809110919,3),(7,2,1,0,NULL,20190809110924,3),(8,2,1,0,NULL,20190809110927,3),(9,2,1,0,'yyy',20190809110933,3),(10,2,1,0,'yyy',20190809110951,3),(11,2,1,0,'yyy',20190809111021,3),(12,2,1,0,'yyy',20190809111310,3),(13,2,1,0,'碧桂园',20190811082632,0);
/*!40000 ALTER TABLE `t_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_log_detail`
--

DROP TABLE IF EXISTS `t_log_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_log_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lid` int(11) DEFAULT NULL,
  `htmllog` mediumtext,
  `res_url` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log_detail`
--

LOCK TABLES `t_log_detail` WRITE;
/*!40000 ALTER TABLE `t_log_detail` DISABLE KEYS */;
INSERT INTO `t_log_detail` VALUES (1,1,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1565134688203\"><source src=\"/api/res/blog/video/1/VklEXzIwMTkwNzI4XzE3MDI1NC5tcDQ=\" type=\"application/x-mpegURL\"></video><p></p><p><br></p>','/mnt/resource/2/20190806/1'),(2,2,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1565245746788\"><source src=\"/api/res/blog/video/2/OC42IDE2LjMwIOWkj+eRnumbqiAgMTg2MjgwNTc5NDEg77yI5pu05pS554mILm1wNA==\" type=\"application/x-mpegURL\"></video><p></p><p><br></p>','/mnt/resource/2/20190808/2'),(3,3,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1565245746788\"><source src=\"/api/res/blog/video/3/OC42IDE2LjMwIOWkj+eRnumbqiAgMTg2MjgwNTc5NDEg77yI5pu05pS554mILm1wNA==\" type=\"application/x-mpegURL\"></video><p></p><p><br></p>','/mnt/resource/2/20190808/3'),(4,4,'<p><a href=\"https://github.com/ray0728/streamserver/releases/tag/untagged-bd6015236f9e75327fea\" style=\"color: rgb(3, 102, 214); font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;; font-size: 32px; font-variant-numeric: normal; font-variant-east-asian: normal;\">master-20190810144442-6</a></p><p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1565511625939\"><source src=\"/api/res/blog/video/4/VklEXzIwMTkwNzA4XzE5NTQ0My5tcDQ=\" type=\"application/x-mpegURL\"></video><p></p><p><br></p>','/mnt/resource/2/20190808/4'),(5,5,'','/mnt/resource/2/20190809/5'),(6,6,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1565392148041\"><source src=\"/api/res/blog/video/6/VklEXzIwMTkwODAxXzE1MzQxNi5tcDQ=\" type=\"application/x-mpegURL\"></video><p></p><p><br></p>','/mnt/resource/2/20190809/6'),(7,7,'','/mnt/resource/2/20190809/7'),(8,8,'','/mnt/resource/2/20190809/8'),(9,9,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1565392148041\"><source src=\"/api/res/blog/video/9/VklEXzIwMTkwODAxXzE1MzQxNi5tcDQ=\" type=\"application/x-mpegURL\"></video><p></p><p><br></p>','/mnt/resource/2/20190809/9'),(10,10,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1565392148041\"><source src=\"/api/res/blog/video/10/VklEXzIwMTkwODAxXzE1MzQxNi5tcDQ=\" type=\"application/x-mpegURL\"></video><p></p><p><br></p>','/mnt/resource/2/20190809/10'),(11,11,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1565392148041\"><source src=\"/api/res/blog/video/11/VklEXzIwMTkwODAxXzE1MzQxNi5tcDQ=\" type=\"application/x-mpegURL\"></video><p></p><p><br></p>','/mnt/resource/2/20190809/11'),(12,12,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1565392148041\"><source src=\"/api/res/blog/video/12/VklEXzIwMTkwODAxXzE1MzQxNi5tcDQ=\" type=\"application/x-mpegURL\"></video><p></p><p><br></p>','/mnt/resource/2/20190809/12'),(13,13,'<p>碧桂园资料</p><p><br></p><div><img src=\"/api/res/blog/img/13/bW1leHBvcnQxNTY0NzMxOTk5NjQ2LmpwZw==\" style=\"width: 100%;\"></div><div><br></div><p><br></p><div><img src=\"/api/res/blog/img/13/bW1leHBvcnQxNTY0NjY4MTg5MDk2LmpwZw==\" style=\"width: 100%;\"></div><div><br></div><div><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1565513533740\"><source src=\"/api/res/blog/video/13/VklEXzIwMTkwNjI2XzE5NDEwMS5tcDQ=\" type=\"application/x-mpegURL\"></video></div><div><br></div>','/mnt/resource/2/20190811/13');
/*!40000 ALTER TABLE `t_log_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_quotations`
--

DROP TABLE IF EXISTS `t_quotations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_quotations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `desc_chinese` varchar(500) DEFAULT NULL,
  `desc_english` varchar(500) DEFAULT NULL,
  `author` varchar(45) DEFAULT NULL,
  `source` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_quotations`
--

LOCK TABLES `t_quotations` WRITE;
/*!40000 ALTER TABLE `t_quotations` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_quotations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_reply`
--

DROP TABLE IF EXISTS `t_reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_reply` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lid` int(11) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `guest_name` varchar(45) DEFAULT NULL,
  `guest_email` varchar(45) DEFAULT NULL,
  `date` bigint(20) DEFAULT NULL,
  `desc` varchar(45) DEFAULT NULL,
  `avatar` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_reply`
--

LOCK TABLES `t_reply` WRITE;
/*!40000 ALTER TABLE `t_reply` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_reply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_tag`
--

DROP TABLE IF EXISTS `t_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `desc` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_tag`
--

LOCK TABLES `t_tag` WRITE;
/*!40000 ALTER TABLE `t_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_tag_log_map`
--

DROP TABLE IF EXISTS `t_tag_log_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_tag_log_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tid` int(11) NOT NULL,
  `lid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_tag_log_map`
--

LOCK TABLES `t_tag_log_map` WRITE;
/*!40000 ALTER TABLE `t_tag_log_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_tag_log_map` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-08-19  6:44:17
