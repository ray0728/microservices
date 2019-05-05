CREATE DATABASE  IF NOT EXISTS `account` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `account`;
-- MySQL dump 10.13  Distrib 5.7.26, for Linux (x86_64)
--
-- Host: localhost    Database: account
-- ------------------------------------------------------
-- Server version	5.7.26-0ubuntu0.18.04.1

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

-- Dump completed on 2019-05-05 23:01:00
CREATE DATABASE  IF NOT EXISTS `resource` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `resource`;
-- MySQL dump 10.13  Distrib 5.7.26, for Linux (x86_64)
--
-- Host: localhost    Database: resource
-- ------------------------------------------------------
-- Server version	5.7.26-0ubuntu0.18.04.1

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_account_category_map`
--

LOCK TABLES `t_account_category_map` WRITE;
/*!40000 ALTER TABLE `t_account_category_map` DISABLE KEYS */;
INSERT INTO `t_account_category_map` VALUES (1,2,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_category`
--

LOCK TABLES `t_category` WRITE;
/*!40000 ALTER TABLE `t_category` DISABLE KEYS */;
INSERT INTO `t_category` VALUES (1,'NOTEBOOK');
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log`
--

LOCK TABLES `t_log` WRITE;
/*!40000 ALTER TABLE `t_log` DISABLE KEYS */;
INSERT INTO `t_log` VALUES (1,2,1,0,'matebook14',20190504031236,0),(2,2,2,0,'123444',NULL,NULL),(3,3,2,0,'67575',NULL,NULL),(4,3,1,3,'67868',NULL,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log_detail`
--

LOCK TABLES `t_log_detail` WRITE;
/*!40000 ALTER TABLE `t_log_detail` DISABLE KEYS */;
INSERT INTO `t_log_detail` VALUES (1,1,'<p>作者：Navis Li</p><p>链接：https://www.zhihu.com/question/319682979/answer/648069294</p><p>来源：知乎</p><p>著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。</p><p><br></p><p>MDT 的 MateBook 14 详细评测已经更新，我们在这把详细评测的总结部分搬运到这个回答内，如果想要看详细信息，请点击下访链接查看原文。Navis Li：​ 谁说 14 英寸没有好屏幕 — MateBook 14 评测​zhuanlan.zhihu.comMDT 的 MateBook&nbsp; 14 评测总结：&lt;img src=\"https://pic2.zhimg.com/50/v2-5c3bc9808467a46fb466b97110b73336_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2682\" data-rawheight=\"1439\" data-default-watermark-src=\"https://pic3.zhimg.com/50/v2-b78096e8d52875d2f6e2ca7a473bd605_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2682\" data-original=\"https://pic2.zhimg.com/v2-5c3bc9808467a46fb466b97110b73336_r.jpg\"/&gt;​MateBook 14 为华为目前的笔记本产品线补齐了 14 英寸级别中端定位的最后一块版图，伴随着窄边框设计的普及，目前的主流笔记本屏幕尺寸已经从&nbsp; 13 英寸升级到了 14 英寸，但与此同时由于面板供应的原因，我们会发现市场上很多 14 英寸的产品，屏幕都只有 60% 左右的 sRGB 覆盖，甚至很多厂商的同一系列产品之中，13 英寸版本都要比 14 英寸版本强很多。因此 MateBook 14 带着 MateBook 系列在屏幕方面一贯优秀的口碑，把这样一块 3:2 比例 2160*1440 分辨率，能覆盖 sRGB 的屏幕带入了 14 英寸笔记本市场，算是填补了 14 英寸非旗舰没有好屏幕的空白。&lt;img src=\"https://pic3.zhimg.com/50/v2-c00bc9f128a536de324acdcebdcf31b4_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"1267\" data-rawheight=\"753\" class=\"origin_image zh-lightbox-thumb\" width=\"1267\" data-original=\"https://pic3.zhimg.com/v2-c00bc9f128a536de324acdcebdcf31b4_r.jpg\"/&gt;在屏幕之外，MateBook 14 也有续航、接口、散热等等方面的优点，因此对于想花一个 5000~6000 档位主流笔记本价格买到 14 英寸好屏幕的消费者，MateBook 14 的优势是非常大的。优势：同价位段唯一的 3:2 比例屏幕更适合工作，拥有 2160*1400 分辨率同价位段的 14 寸屏幕产品之中能覆盖 sRGB 色域的屏幕，色准不错性能表现还不错，单独 CPU 负载的时候噪音很低接口种类的覆盖比较齐全续航较长，能满足一天使用缺点：镜面屏抗眩光能力较差在极限情况下风扇存在一定高频异音极限情况下机身左右温差明显USB-C 接口和 HDMI 外接屏幕都无法输出 4K60Hz 购买建议与适用人群：MateBook 14 的价格在 4.11 日的国内发布会上也早已经公布了，很遗憾目前没有 16GB 内存的机型，一共有三个版本：&lt;img src=\"https://pic2.zhimg.com/50/v2-c52ab88fdd7fc51e06e64eeb5d35970d_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"1269\" data-rawheight=\"952\" class=\"origin_image zh-lightbox-thumb\" width=\"1269\" data-original=\"https://pic2.zhimg.com/v2-c52ab88fdd7fc51e06e64eeb5d35970d_r.jpg\"/&gt;i5 + 8GB 内存 + 512GB 固态硬盘，售价5699i5 + 8GB 内存 + 512GB 固态硬盘 + MX 250 独立显卡，售价5999i7 + 8GB 内存 + 512GB 固态硬盘 + MX 250 独立显卡，售价6999由于独立显卡和非独立显卡的两个版本价格差距缩小到了只有 300 人民币，加上独显和非独显两个版本拥有不同的散热设计，所以这一次我们认为性价比最高的就是 i5 + MX250 独立显卡版本。因为即便你完全不需要独立显卡性能，独显版本更强的散热设计能让你在 CPU 单独负载的时候拥有非常低的噪音表现，而且表面温度也不高。至于独立显卡会不会影响续航，我们的经验是只要不运行需要独显的程序，续航差距都在 5% 以内，热量差距可以忽略不计。当然，要是以后有 16GB 内存的顶配版本开卖的话，那对于需要 16GB 内存的朋友来说，就只能搭配 i7 一起购买了。插播一条，MateBook X Pro 2019 的评测已经更新：Navis Li：除了外观都已截然不同 —&nbsp; 新 MateBook X Pro 评测​zhuanlan.zhihu.com以下为发布会当天的现场体验内容。今天MateBook X Pro 2019 和MateBook 14 都在国内正式发布，它们都已经不是第一次亮相了，但还有一款之前从来没有提过的产品，搭载高通骁龙处理器的MateBook E.三款产品的起售价格和配置信息分别是：&lt;img src=\"https://pic3.zhimg.com/50/v2-e69bd6c13e465f9ecc7e7ea310171ead_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1536\" data-default-watermark-src=\"https://pic4.zhimg.com/50/v2-613fc5c0f8d181298b42b8cfacdb5ee3_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic3.zhimg.com/v2-e69bd6c13e465f9ecc7e7ea310171ead_r.jpg\"/&gt;MateBook X Pro 2019: 7999MateBook 14: 5699MateBook E：3999可以说是基本符合预期，而且还蛮香的。我们还是先从大家关注度最高的 MateBook 14 开始说吧，MateBook 14 顾名思义代表的是14 寸，它也采用了和 MateBook X Pro &amp; 13 一样的3:2 屏幕，所以它的屏幕大小和X Pro 是一样的，在分辨率上则是和 MateBook 13 一样的 2160*1440，因此也能反映出它的定位更接近13 的主流走量，而不是 X Pro 的高端旗舰。&lt;img src=\"https://pic2.zhimg.com/50/v2-47fb20d1dfabd0e58e26aa1abec515b1_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1536\" data-default-watermark-src=\"https://pic1.zhimg.com/50/v2-09d651bfeff91585035b72ef023ea48f_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic2.zhimg.com/v2-47fb20d1dfabd0e58e26aa1abec515b1_r.jpg\"/&gt;14 目前有灰和白两种颜色，边框很窄，几乎和X Rro 没有什么区别，摄像头也与X Pro 一样是按压弹出设计。&lt;img src=\"https://pic2.zhimg.com/50/v2-dd50583e90d0e92e1cea4a39fb254922_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1536\" data-default-watermark-src=\"https://pic2.zhimg.com/50/v2-568e0d9508ca42b6f1d0bf0a694957a5_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic2.zhimg.com/v2-dd50583e90d0e92e1cea4a39fb254922_r.jpg\"/&gt;不过由于散热和整机设计的不同，转轴也接近 MateBook 13，从侧面看也就会发现MateBooo 14 的接口可能是目前MateBook 系列之中种类最全的。&lt;img src=\"https://pic3.zhimg.com/50/v2-000374e50190d4a5cc95d854e79b6143_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1536\" data-default-watermark-src=\"https://pic3.zhimg.com/50/v2-f3ea6a8e6938be866425529856d2d879_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic3.zhimg.com/v2-000374e50190d4a5cc95d854e79b6143_r.jpg\"/&gt;左侧接口是USB-C，3.5mm 耳机接口和HDMI.&lt;img src=\"https://pic3.zhimg.com/50/v2-6220f050004efaaecf8cc24e8d01ebe7_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1536\" data-default-watermark-src=\"https://pic1.zhimg.com/50/v2-76988df339fdad1c0464cecef129ab6b_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic3.zhimg.com/v2-6220f050004efaaecf8cc24e8d01ebe7_r.jpg\"/&gt;右侧则是两个USB-A 接口（一个3.0 一个2.0），可以说是包含了目前轻薄产品中大部分常用的接口。在性能上，搭载8.5 代酷睿和25W 较高性能版本的&nbsp; MX250，加上更大的机内散热空间，所以也能拥有较为不错的表现，这一点也请期待我们之后详细的评测。&lt;img src=\"https://pic4.zhimg.com/50/v2-6aaba138d593fabc75572f844c7a1927_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1536\" data-default-watermark-src=\"https://pic4.zhimg.com/50/v2-8a7a4bd1a64993c75e2c7773cc265a7a_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic4.zhimg.com/v2-6aaba138d593fabc75572f844c7a1927_r.jpg\"/&gt;总的来看，MateBook 14 应该是目前14 英寸笔记本市场之中非常有竞争力的产品，拥有同价位唯一的3:2 2160*1440 分辨率sRGB 色域屏幕；对于这样尺寸来说不错的性能表现；种类覆盖比较齐全的接口；57Wh 电池带来的较长续航。接着是全新的MateBook X Pro 2019.&lt;img src=\"https://pic2.zhimg.com/50/v2-15dd41e5c0898bee80ba6ea155fe259b_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1536\" data-default-watermark-src=\"https://pic2.zhimg.com/50/v2-a04e8e415ade0b9ef1b936edb5f0625f_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic2.zhimg.com/v2-15dd41e5c0898bee80ba6ea155fe259b_r.jpg\"/&gt;MateBook X Pro 2019 也有了一个新的颜色，在现场我们看到了粉色X Pro 2019 的真机，实际看起来颜色和之前的13 粉色接近。&lt;img src=\"https://pic2.zhimg.com/50/v2-d6054322181aecc9f607f67f39ae8f68_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1536\" data-default-watermark-src=\"https://pic4.zhimg.com/50/v2-e7322e296b470e8438c0dad183814b14_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic2.zhimg.com/v2-d6054322181aecc9f607f67f39ae8f68_r.jpg\"/&gt;MateBook X Pro 2019 的变化可能在设计上就不明显了，除了顶盖改为了HUAWEI 文字标之外，确实不太好从外观看出区别。&lt;img src=\"https://pic4.zhimg.com/50/v2-069f0208dfed9971a79520f47919b10e_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1520\" data-default-watermark-src=\"https://pic2.zhimg.com/50/v2-f806692fcbbbfc4dd3e68f15cbae8105_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic4.zhimg.com/v2-069f0208dfed9971a79520f47919b10e_r.jpg\"/&gt;因为主要的变化都在内部，首先是处理器也更新到了 8.5 代酷睿，显卡来到了 MX250 低功耗版本，USB-C 接口升级到了 40Gbps，但是除此之外其实内部的设计相对于之前也有很多小细节的改变，我们之前的 MateBook X Pro 2018 评测提到过表面温度和噪音是上一代需要改进的点，而从我的测试来看，新的 X Pro 噪音和表面温度有明显改善，我们也将会在即将更新的单品评测中有详细的解析。&lt;img src=\"https://pic4.zhimg.com/50/v2-c010c2a4aa51cede6c7b800d38e595cc_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1536\" data-default-watermark-src=\"https://pic3.zhimg.com/50/v2-2fd527fed94e86c7afa5838054e2d7c3_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic4.zhimg.com/v2-c010c2a4aa51cede6c7b800d38e595cc_r.jpg\"/&gt;最后我们还发现了首次搭载高通骁龙处理器的Windows 10 平板MateBook E，采用的还是3:2 的1440P 屏幕，支持2048 级别压感。&lt;img src=\"https://pic2.zhimg.com/50/v2-c396da033a4d15becf11041a392762e0_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1536\" data-default-watermark-src=\"https://pic1.zhimg.com/50/v2-8dc3e9234d4ab82e19a245e9d91984a8_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic2.zhimg.com/v2-c396da033a4d15becf11041a392762e0_r.jpg\"/&gt;既然是平板，那么自然可拆分，机身和键盘通过机身底部的触点和键盘保护套连接，官方保护套键盘的键程达到了1.3mm，所以手感在平板里还不错，不过也不支持背光键盘了。&lt;img src=\"https://pic1.zhimg.com/50/v2-0e0135d18da65a307df13a1e0f00adb9_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1536\" data-default-watermark-src=\"https://pic2.zhimg.com/50/v2-ac1ea2c6ef33ead761a0b5d92844d983_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic1.zhimg.com/v2-0e0135d18da65a307df13a1e0f00adb9_r.jpg\"/&gt;当然，MateBook E 最大的特点还是在高通骁龙上，MateBook E 上的搭载的是850 版本，10nm 制程8 核心，相比于第一代高通骁龙笔记本的835 提升了20% 左右。&lt;img src=\"https://pic1.zhimg.com/50/v2-efb83034e2a63f8825f6e2756e924084_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1536\" data-default-watermark-src=\"https://pic1.zhimg.com/50/v2-aaa5991ca5bd9a7909050aa1ddbce725_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic1.zhimg.com/v2-efb83034e2a63f8825f6e2756e924084_r.jpg\"/&gt;GPU 方面则是Adreno 630&lt;img src=\"https://pic2.zhimg.com/50/v2-2f7f39cca07832a725c398b811df8e6f_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1536\" data-default-watermark-src=\"https://pic4.zhimg.com/50/v2-dc3aa78d8152fcbdbb659d8e4f8374fe_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic2.zhimg.com/v2-2f7f39cca07832a725c398b811df8e6f_r.jpg\"/&gt;除了WLAM 之外，通过机身上的4*4 mimo 多天线，高通骁龙就能支持千兆4G LTE，现场使用的是中国电信的SIM，通过它就可以实现和手机一样的随时随地在线。机身内的电池容量为36.3Wh，由于高通骁龙的特性，整机续航也会有比同样电池容量采用X86 处理器的产品有更好表现。以上就是这次发布会的全部硬件内容，不过还有一个新的软件功能，和一个老用户听了会很开心的服务。首先就是Huawei Share 3.0 一碰传。&lt;img src=\"https://pic3.zhimg.com/50/v2-7f11b5a182b1afa91ef57c5e4a43346c_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"1440\" data-rawheight=\"1080\" data-default-watermark-src=\"https://pic3.zhimg.com/50/v2-15f633a9d4a551e52b84ace8e0b5fa3a_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"1440\" data-original=\"https://pic3.zhimg.com/v2-7f11b5a182b1afa91ef57c5e4a43346c_r.jpg\"/&gt;更新了三个功能，一碰传文件，一碰传录屏和共享剪贴板。&lt;img src=\"https://pic3.zhimg.com/50/v2-ebabe0dcc89aedf1af57bda8cefaca87_hd.jpg\" data-caption=\"\" data-size=\"normal\" data-rawwidth=\"2048\" data-rawheight=\"1221\" data-default-watermark-src=\"https://pic4.zhimg.com/50/v2-ebefb98595c5d96510d59f2d0cd36d19_hd.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"2048\" data-original=\"https://pic3.zhimg.com/v2-ebabe0dcc89aedf1af57bda8cefaca87_r.jpg\"/&gt;那么老版本的机器怎么办呢，这就是今天公布的新服务：除了一些很早的笔记本之外，华为将会在五月提供老机型新增NFC 标签的服务，这样就可以让老机型也享受到Huawei Share 3.0 一碰传的全部服务r。</p><p><br></p><div><img src=\"/blog/api/res/img/1/SU1HXzIwMTkwMzMxXzExNTUwMC5qcGc=\" style=\"width: 100%;\"></div><div><br></div><p></p><video controls=\"\" class=\"video-js vjs-big-play-centered\" id=\"vid_1556982732632\"><source src=\"/blog/api/res/video/1/VGhlX0JlYXV0eV9vZl9FYXJ0aC5tcDQ=\"></video><p></p><p><br></p>','/home/tanxin/Documents/resources/2/20190504031237/1');
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_reply`
--

LOCK TABLES `t_reply` WRITE;
/*!40000 ALTER TABLE `t_reply` DISABLE KEYS */;
INSERT INTO `t_reply` VALUES (1,1,1,0,'yy'),(2,2,2,0,'yy'),(3,2,3,0,'yy'),(4,1,4,0,'y'),(5,4,5,0,'y'),(6,2,6,0,'yy'),(7,4,7,0,'yy'),(8,4,8,0,'y');
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_tag`
--

LOCK TABLES `t_tag` WRITE;
/*!40000 ALTER TABLE `t_tag` DISABLE KEYS */;
INSERT INTO `t_tag` VALUES (1,'测试');
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_tag_log_map`
--

LOCK TABLES `t_tag_log_map` WRITE;
/*!40000 ALTER TABLE `t_tag_log_map` DISABLE KEYS */;
INSERT INTO `t_tag_log_map` VALUES (1,1,1);
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

-- Dump completed on 2019-05-05 23:01:00
