-- MySQL dump 10.13  Distrib 5.7.25, for Linux (x86_64)
--
-- Host: localhost    Database: account
-- ------------------------------------------------------
-- Server version	5.7.25-0ubuntu0.18.04.2

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
-- Table structure for table `t_account`
--

DROP TABLE IF EXISTS `t_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(60) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `first_time` bigint(20) DEFAULT '0',
  `times` int(11) DEFAULT '0',
  `last_login` bigint(20) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=armscii8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_account`
--

LOCK TABLES `t_account` WRITE;
/*!40000 ALTER TABLE `t_account` DISABLE KEYS */;
INSERT INTO `t_account` VALUES (2,'txtx','$2a$10$keTgsbqbLmtsCApk5PfYqOyXRm0vfED8gs7QTbaZQL5wDu32uJV32','51101661@qq.com',0,20190316024133,0,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=armscii8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_account_role_map`
--

LOCK TABLES `t_account_role_map` WRITE;
/*!40000 ALTER TABLE `t_account_role_map` DISABLE KEYS */;
INSERT INTO `t_account_role_map` VALUES (2,2,1);
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
-- Table structure for table `t_log`
--

DROP TABLE IF EXISTS `t_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `date` bigint(20) DEFAULT NULL,
  `gid` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `like_num` int(11) DEFAULT NULL,
  `unlike_num` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log`
--

LOCK TABLES `t_log` WRITE;
/*!40000 ALTER TABLE `t_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_log_detial`
--

DROP TABLE IF EXISTS `t_log_detial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_log_detial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lid` int(11) DEFAULT NULL,
  `res_url` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log_detial`
--

LOCK TABLES `t_log_detial` WRITE;
/*!40000 ALTER TABLE `t_log_detial` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_log_detial` ENABLE KEYS */;
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
  `desc` varchar(45) DEFAULT NULL,
  `date` bigint(20) DEFAULT NULL,
  `like_num` int(11) DEFAULT NULL,
  `unlike_num` int(11) DEFAULT NULL,
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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-04-26 23:25:01
-- MySQL dump 10.13  Distrib 5.7.25, for Linux (x86_64)
--
-- Host: localhost    Database: resource
-- ------------------------------------------------------
-- Server version	5.7.25-0ubuntu0.18.04.2

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
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_account_category_map`
--

LOCK TABLES `t_account_category_map` WRITE;
/*!40000 ALTER TABLE `t_account_category_map` DISABLE KEYS */;
INSERT INTO `t_account_category_map` VALUES (14,2,10),(15,2,13),(16,2,14),(17,2,15);
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_category`
--

LOCK TABLES `t_category` WRITE;
/*!40000 ALTER TABLE `t_category` DISABLE KEYS */;
INSERT INTO `t_category` VALUES (8,'联网状态'),(9,'WiFi设置'),(10,'bb'),(11,'10'),(12,'jlkj'),(13,'kkl'),(14,'ll'),(15,'jj');
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
  `gid` int(11) DEFAULT NULL,
  `title` varchar(45) DEFAULT NULL,
  `date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log`
--

LOCK TABLES `t_log` WRITE;
/*!40000 ALTER TABLE `t_log` DISABLE KEYS */;
INSERT INTO `t_log` VALUES (1,2,12,0,'jjkljlj',20190421072631,NULL),(2,2,12,0,'jjkljlj',20190421072927,NULL),(3,2,12,0,'jjkljlj',20190421072949,NULL),(4,2,10,0,'hkjhjkh',20190421074239,NULL),(5,2,10,0,'hkjhjkh',20190421074243,NULL),(6,2,10,0,'hjkhjk',20190421074404,NULL),(7,2,10,0,'jhkhkj',20190421074540,NULL),(8,2,10,0,'kjnk',20190421074727,NULL),(9,2,10,0,'hjihjkhk',20190421075307,NULL),(10,2,10,0,'hjihjkhk',20190421075311,NULL),(11,2,13,0,'hkhjk',20190421075710,NULL),(12,2,10,0,'nnm,n,m',20190421075816,NULL),(13,2,10,0,'nnm,n,m',20190421075845,NULL),(14,2,14,0,'jlkkll',20190421080203,NULL),(15,2,14,0,'jlkkll',20190421080426,NULL),(16,2,10,0,'hjkbbv',20190421082404,NULL),(17,2,10,0,'hjkbbv',20190421083625,NULL),(18,2,15,0,'gjghj',20190421083705,NULL),(19,2,15,0,'gjghj',20190421083733,NULL),(20,2,10,0,'hjkhjk',20190421083850,NULL),(21,2,10,0,'hghj',20190421083940,NULL),(22,2,10,0,'ghgj',20190421084029,NULL),(23,2,10,0,'hahahaha',20190421030538,NULL);
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
  `htmllog` text,
  `res_url` tinytext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log_detail`
--

LOCK TABLES `t_log_detail` WRITE;
/*!40000 ALTER TABLE `t_log_detail` DISABLE KEYS */;
INSERT INTO `t_log_detail` VALUES (1,1,NULL,'/home/tanxin/Documents/resources/2/20190421072631/1'),(2,2,NULL,'/home/tanxin/Documents/resources/2/20190421072931/2'),(3,3,NULL,'/home/tanxin/Documents/resources/2/20190421072949/3'),(4,4,NULL,'/home/tanxin/Documents/resources/2/20190421074239/4'),(5,5,NULL,'/home/tanxin/Documents/resources/2/20190421074243/5'),(6,6,NULL,'/home/tanxin/Documents/resources/2/20190421074404/6'),(7,7,NULL,'/home/tanxin/Documents/resources/2/20190421074540/7'),(8,8,NULL,'/home/tanxin/Documents/resources/2/20190421074728/8'),(9,9,NULL,'/home/tanxin/Documents/resources/2/20190421075307/9'),(10,10,NULL,'/home/tanxin/Documents/resources/2/20190421075311/10'),(11,11,NULL,'/home/tanxin/Documents/resources/2/20190421075710/11'),(12,12,NULL,'/home/tanxin/Documents/resources/2/20190421075816/12'),(13,13,NULL,'/home/tanxin/Documents/resources/2/20190421075845/13'),(14,14,NULL,'/home/tanxin/Documents/resources/2/20190421080203/14'),(15,15,NULL,'/home/tanxin/Documents/resources/2/20190421080426/15'),(16,16,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1555835041390\"><source src=\"blob:http://192.168.0.2:10006/46f2db39-8a52-4368-9698-586cb3394e19/1555330954538.mp4\"></video><p></p><p><br></p>','/home/tanxin/Documents/resources/2/20190421082404/16'),(17,17,NULL,'/home/tanxin/Documents/resources/2/20190421083625/17'),(18,18,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1555835823960\"><source src=\"blob:http://192.168.0.2:10006/6f0d7e38-4989-47e2-9c20-0afa9631fc50/S213 Peppa’s Christmas-国语高清.mp4\"></video><p></p><p><br></p>','/home/tanxin/Documents/resources/2/20190421083705/18'),(19,19,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1555835823960\"><source src=\"blob:http://192.168.0.2:10006/6f0d7e38-4989-47e2-9c20-0afa9631fc50/S213 Peppa’s Christmas-国语高清.mp4\"></video><p></p><p><br></p>','/home/tanxin/Documents/resources/2/20190421083733/19'),(20,20,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1555835928903\"><source src=\"blob:http://192.168.0.2:10006/cbfc697b-2d1f-4275-9f65-4d9328f513e3/《Hello, Reindeer Children Christmas Song super sing songs》-国语高清.mp4\"></video><p></p><p><br></p>','/home/tanxin/Documents/resources/2/20190421083850/20'),(21,21,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1555835978022\"><source src=\"blob:http://192.168.0.2:10006/d10a6ff2-8136-4c85-a5ff-04ccb729054c/1555330954538.mp4\"></video><p></p><p><br></p>','/home/tanxin/Documents/resources/2/20190421083940/21'),(22,22,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1555836028718\"><source src=\"/《Hello, Reindeer Children Christmas Song super sing songs》-国语高清.FLV\"></video><p></p><p><br></p>','/home/tanxin/Documents/resources/2/20190421084029/22'),(23,23,'<p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1555859136153\"><source src=\"blob:http://192.168.0.2:10006/f8b942c6-92f6-43f5-aab3-afe9bb605260/S213 Peppa’s Christmas-国语高清.mp4\"></video><p></p><p><br></p>','/home/tanxin/Documents/resources/2/20190421030538/23');
/*!40000 ALTER TABLE `t_log_detail` ENABLE KEYS */;
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
  `date` bigint(20) DEFAULT NULL,
  `desc` varchar(45) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_tag`
--

LOCK TABLES `t_tag` WRITE;
/*!40000 ALTER TABLE `t_tag` DISABLE KEYS */;
INSERT INTO `t_tag` VALUES (1,'abc'),(2,'abc'),(3,'abc123'),(4,'mmmn');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_tag_log_map`
--

LOCK TABLES `t_tag_log_map` WRITE;
/*!40000 ALTER TABLE `t_tag_log_map` DISABLE KEYS */;
INSERT INTO `t_tag_log_map` VALUES (2,1,1),(3,2,2);
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

-- Dump completed on 2019-04-26 23:25:01
