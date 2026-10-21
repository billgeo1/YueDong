/*
Navicat MySQL Data Transfer

Source Server         : localhost_3328
Source Server Version : 50732
Source Host           : localhost:3328
Source Database       : db_typs

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2024-03-03 10:37:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_address`
-- ----------------------------
DROP TABLE IF EXISTS `t_address`;
CREATE TABLE `t_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `xingming` varchar(20) DEFAULT NULL,
  `dizhi` varchar(50) DEFAULT NULL,
  `dianhua` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_address
-- ----------------------------
INSERT INTO `t_address` VALUES ('5', '153', '刘三', '北京市丰台区北大街11#', '13312567852');
INSERT INTO `t_address` VALUES ('7', '153', '孙俪', '山东济南历下区幸福小区1#楼13', '13878784521');

-- ----------------------------
-- Table structure for `t_admin`
-- ----------------------------
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin` (
  `id` int(11) NOT NULL,
  `username` varchar(66) DEFAULT NULL,
  `password` varchar(55) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_admin
-- ----------------------------
INSERT INTO `t_admin` VALUES ('1', 'a', 'a');

-- ----------------------------
-- Table structure for `t_cart`
-- ----------------------------
DROP TABLE IF EXISTS `t_cart`;
CREATE TABLE `t_cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `productId` int(11) NOT NULL,
  `shuliang` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_cart
-- ----------------------------

-- ----------------------------
-- Table structure for `t_duihuan`
-- ----------------------------
DROP TABLE IF EXISTS `t_duihuan`;
CREATE TABLE `t_duihuan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL COMMENT '�û�id',
  `lipin` varchar(255) DEFAULT NULL,
  `shijian` varchar(55) DEFAULT NULL COMMENT '�µ�ʱ��',
  `xingming` varchar(255) DEFAULT NULL,
  `dizhi` varchar(200) DEFAULT NULL COMMENT '�ջ��˵绰',
  `dianhua` varchar(255) DEFAULT NULL,
  `beizhu` varchar(20) DEFAULT NULL COMMENT '�ܼ�',
  `zt` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_duihuan
-- ----------------------------
INSERT INTO `t_duihuan` VALUES ('41', '153', '聚宝盆', '2024-03-03 10:35', '刘三', '北京', '13645457878', '尽快发', '已受理');

-- ----------------------------
-- Table structure for `t_gonggao`
-- ----------------------------
DROP TABLE IF EXISTS `t_gonggao`;
CREATE TABLE `t_gonggao` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `biaoti` varchar(200) DEFAULT NULL,
  `neirong` varchar(3000) DEFAULT NULL,
  `fujian` varchar(255) DEFAULT NULL,
  `fujianYuan` varchar(255) DEFAULT NULL,
  `shijian` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_gonggao
-- ----------------------------
INSERT INTO `t_gonggao` VALUES ('19', '网络购物的“生机”与“隐忧”', '伴随着互联网的普及，以及网上银行和现代物流业的发展，网络购物近年来呈现高速发展的态势，在经济社会生活中的地位日渐突出，即便是在国际金融危机的&ldquo;严冬&rdquo;中，仍然表现出如火如荼的势头，成为受危机负面影响最小、发展势头最好的行业之一。网络购物行为正在由一种网络时尚逐步成为大众消费模式，渗透到社会各个阶层，并引发网民消费行为和消费理念的转变。<br />\n我国网络购物在经历了发烧、沉寂和调整后，正迎来快速发展的新阶段，网络购物的优势日益得到网民认可。调查显示，在网络购物方面，比较活跃的网民(平均每月购物一次以上)占被访网民的45.7%，37.6%的被访网民每月购物1-5次，78.1%的被访网民在网上购买礼品的花费占到总花费的60%。（如图1）可见，在某些消费领域，人们对网络购物的垂青已经超过了传统的实体店。', '/upload/1727081C-20FF-4809-89A5-20F031ADB652.jpg', '11.jpg', '2024-03-02 08:18');
INSERT INTO `t_gonggao` VALUES ('20', '商务部关于促进网络购物健康发展的指导意见', '网络购物是依托互联网和信息技术的新型零售形式，具有流通环节少、交易费用低、资金周转快、流通效率高、销售范围广、消费者购买方便等优势。发展网络购物，有利于企业拓展营销方式、刺激消费、扩大内需、转变发展方式，有利于带动创业就业，有利于促进上下游关联企业协同发展、健全产业链。近年来，我国网络购物市场呈现消费群体不断扩大，消费规模快速增长的良好局面。同时，也存在相关政策法规、管理能力和服务水平不适应网络购物发展需要等现实问题。', '/upload/606FF97F-2F88-4593-A95A-9D568F1C7199.jpg', '2.jpg', '2024-03-02 08:20');
INSERT INTO `t_gonggao` VALUES ('21', '网络购物节:价美也要质优', '近年来，中国电子商务规模快速增长。商务部电子商务和信息化司发布的《中国电子商务报告》显示，去年，全国电子商务交易额37.21万亿元人民币，呈现出电商爆发式增长、社交电商发展迅猛、电商加快驱动产业数字化转型等特点。在这一行业发展背景下，新电商的概念应运而生。《新电商研究报告》首次为新电商进行了定义，指出新电商是随着新一代信息技术的发展，以用户为中心，对传统电&ldquo;人&rdquo;&ldquo;货&rdquo;&ldquo;场&rdquo;进行链路重构而产生的电商新形态新模式。与传统电商相比，新电商的主要特征是从功能型消费向体验式消费转变，从以产品为中心到以用户为中心，从单一场景到多场景融合，其主要类型包括直播电商、社交电商、社区电商等。', '/upload/93398EBD-96F8-4E7E-9AFD-1CB1E113A05C.jpg', '3.jpg', '2024-03-02 08:25');
INSERT INTO `t_gonggao` VALUES ('22', '网上商超走向价值战 购物体验成未来竞争壁垒', '近年来，中国一跃成为全球最大的电商市场，而凭借价格优惠、商品丰富和送货上门等优势，网上商超也正从实体大卖场、便利店等传统零售渠道手中抢夺市场份额。权威报告显示，中国的中产阶级人数已增长到1.09亿，数量庞大的中产阶级正在推动消费升级：和前些年大家在网购时看重低价相比，这些中产消费人群的偏好开始与生活品质、情感因素、个性化和设计感相关。一方面是消费者对品质消费的巨大需求，另一方面是大大小小的电商正遭遇网络流量瓶颈，维持高速增长愈发艰难。在此形势下，业界认为，谁能满足中产阶级对于品质消费的缺口，谁才能在当前网上商超大战中成为&ldquo;破局者&rdquo;。', '/upload/30C1CE1A-2DA9-49EA-80AB-F0A59663D918.jpg', '4.jpg', '2024-03-02 08:27');

-- ----------------------------
-- Table structure for `t_jifen`
-- ----------------------------
DROP TABLE IF EXISTS `t_jifen`;
CREATE TABLE `t_jifen` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `fen` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_jifen
-- ----------------------------
INSERT INTO `t_jifen` VALUES ('22', '153', '514');

-- ----------------------------
-- Table structure for `t_leibie`
-- ----------------------------
DROP TABLE IF EXISTS `t_leibie`;
CREATE TABLE `t_leibie` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `mingcheng` varchar(55) DEFAULT NULL,
  `jieshao` varchar(555) DEFAULT NULL,
  `del` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_leibie
-- ----------------------------
INSERT INTO `t_leibie` VALUES ('26', '篮球/足球', '', 'no');
INSERT INTO `t_leibie` VALUES ('27', '网球/训练器', '', 'no');
INSERT INTO `t_leibie` VALUES ('28', '乒乓球有关', '', 'no');
INSERT INTO `t_leibie` VALUES ('29', '羽毛球运动', '', 'no');
INSERT INTO `t_leibie` VALUES ('30', '其它类', '', 'no');

-- ----------------------------
-- Table structure for `t_liuyanban`
-- ----------------------------
DROP TABLE IF EXISTS `t_liuyanban`;
CREATE TABLE `t_liuyanban` (
  `id` int(55) NOT NULL AUTO_INCREMENT,
  `user_id` int(50) DEFAULT NULL,
  `neirong` varchar(200) DEFAULT NULL,
  `liuyanshi` varchar(2000) DEFAULT NULL,
  `huifu` varchar(50) DEFAULT NULL,
  `huifushi` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_liuyanban
-- ----------------------------
INSERT INTO `t_liuyanban` VALUES ('4', '153', '测试', '2024-03-02 22:04', '发的', '2024-03-02 22:05');
INSERT INTO `t_liuyanban` VALUES ('5', '153', '大幅度', '2024-03-03 10:35', '', '');

-- ----------------------------
-- Table structure for `t_order`
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '����id',
  `userId` int(11) DEFAULT NULL COMMENT '�û�id',
  `bianhao` varchar(255) DEFAULT NULL,
  `xiadanshi` varchar(55) DEFAULT NULL COMMENT '�µ�ʱ��',
  `dizhi` varchar(200) DEFAULT NULL COMMENT '�ջ��˵绰',
  `zongjine` int(20) DEFAULT NULL COMMENT '�ܼ�',
  `fkzt` varchar(55) DEFAULT NULL COMMENT '״̬��0-δ֧����1-��֧����2-��ȡ����3-�ѹرգ�4-�����',
  `fkfs` varchar(255) DEFAULT NULL,
  `zt` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES ('32', '153', '20240229095353', '2024-02-29 09:53:53', '刘三--北京市丰台区北大街11#--13312567852', '5987', '已支付', '微信', '已发货');
INSERT INTO `t_order` VALUES ('33', '153', '20240303103527', '2024-03-03 10:35:27', '刘三--北京市丰台区北大街11#--13312567852', '263', '已支付', '微信', '已发货');

-- ----------------------------
-- Table structure for `t_order_item`
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item`;
CREATE TABLE `t_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '�����е���Ʒ��¼��id',
  `orderId` int(11) NOT NULL COMMENT '������Ķ�����id',
  `productId` int(11) NOT NULL COMMENT '��Ʒ��id',
  `title` varchar(100) NOT NULL COMMENT '��Ʒ����',
  `jiage` int(20) DEFAULT NULL COMMENT '��Ʒ�۸�',
  `shuliang` int(11) DEFAULT NULL COMMENT '��������',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_order_item
-- ----------------------------
INSERT INTO `t_order_item` VALUES ('34', '32', '27', '品健（PINJIAN）网球回弹训练器固定', '54', '4');
INSERT INTO `t_order_item` VALUES ('35', '32', '24', 'Gotcha童趣标准篮球', '89', '3');
INSERT INTO `t_order_item` VALUES ('36', '32', '29', 'BUTTERFLY蝴蝶乒乓球拍【横拍】', '688', '8');
INSERT INTO `t_order_item` VALUES ('37', '33', '27', '品健（PINJIAN）网球回弹训练器固定', '54', '2');
INSERT INTO `t_order_item` VALUES ('38', '33', '23', '篮球NBA耐磨PU', '55', '1');
INSERT INTO `t_order_item` VALUES ('39', '33', '30', '川崎KAWASAKI 全碳素羽毛球单拍', '100', '1');

-- ----------------------------
-- Table structure for `t_pingjia`
-- ----------------------------
DROP TABLE IF EXISTS `t_pingjia`;
CREATE TABLE `t_pingjia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `productId` int(255) DEFAULT NULL,
  `shuxing` varchar(255) DEFAULT NULL,
  `neirong` varchar(255) DEFAULT NULL,
  `shijian` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_pingjia
-- ----------------------------
INSERT INTO `t_pingjia` VALUES ('23', '153', '29', '好评', '很好', '2024-02-29 09:51');

-- ----------------------------
-- Table structure for `t_product`
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `leibieId` int(20) DEFAULT NULL COMMENT '分类id',
  `title` varchar(100) DEFAULT NULL COMMENT '商品标题',
  `jieshao` varchar(5000) DEFAULT NULL COMMENT '商品卖点',
  `image` varchar(500) DEFAULT NULL COMMENT '图片路径',
  `price` int(20) DEFAULT NULL COMMENT '商品单价',
  `kucun` int(10) DEFAULT NULL COMMENT '库存数量',
  `del` varchar(55) DEFAULT '1' COMMENT '商品状态  1：上架   2：下架   3：删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES ('23', '26', '篮球NBA耐磨PU', '威尔胜（Wilson）篮球NBA比赛室内外耐磨PU成人儿童7号TAKE-OFF系列实战利器801', '/upload/50F88FD1-F921-4C35-960A-3B17D69E0D1D.jpg', '55', '99', 'no');
INSERT INTO `t_product` VALUES ('24', '26', 'Gotcha童趣标准篮球', '室内比赛专用5号篮球耐磨防滑成人体育用品 草绿色，发泡橡胶材质耐磨防滑', '/upload/C6FFA84E-5B27-40FA-AFB0-3EE20D3F3480.jpg', '89', '97', 'no');
INSERT INTO `t_product` VALUES ('25', '26', '红双喜DHS足球', '标准5号球训练比赛机缝耐磨FS5-5，商品毛重：400.00g商品产地：上海货号：FS5-5规格：5号/标准足球缝线：机缝足球材质：PVC(聚氯乙烯)', '/upload/1EAD0581-D214-4625-826F-2EAAB164ACBD.jpg', '76', '100', 'no');
INSERT INTO `t_product` VALUES ('26', '27', 'MF网球回弹训练器', '单人网球带线带拍网球成人儿童网球拍训练器 碳素网球拍-标准', '/upload/98085467-3238-4BEE-9751-CBD421A6607E.jpg', '67', '100', 'no');
INSERT INTO `t_product` VALUES ('27', '27', '品健（PINJIAN）网球回弹训练器固定', '单人带线球拍成人亲子初学者专业自练神器带绳 带线球*3+训练器*1，商品毛重：40.00g，商品产地：中国大陆', '/upload/98F40FF8-A9B5-4ED7-B4DB-A9F203B1020F.jpg', '54', '94', 'no');
INSERT INTO `t_product` VALUES ('28', '28', '361°乒乓球拍', '五星双面反胶快攻球拍 5星【横拍一对】送3个黄球，商品毛重：470.00g商品产地：中国大陆货号：612214112-1底板是否含碳：不含碳胶面类型：双反胶', '/upload/D53B2A66-BCB9-4619-9BD5-AADE617BE8A4.jpg', '89', '100', 'no');
INSERT INTO `t_product` VALUES ('29', '28', 'BUTTERFLY蝴蝶乒乓球拍【横拍】', '四星单拍蝴蝶王直板兵兵拍初学者训练进阶 ，商品毛重：1.0kg货号：tbc-401底板是否含碳：不含碳球拍等级：中级是否带包：无包胶面类型：双反胶', '/upload/999613DC-CC84-4044-B9F1-6ECA9B1B756B.jpg', '688', '87', 'no');
INSERT INTO `t_product` VALUES ('30', '29', '川崎KAWASAKI 全碳素羽毛球单拍', '羽毛球拍单拍 NAVIGATOR3300i （已穿线22磅），，商品毛重：280.00g商品产地：中国广东货号：3300球拍打法：防守球拍克重：3U类型：单拍适用人群：通用球拍材质：全碳素', '/upload/B9BBE4F0-C7BB-42A3-B0FC-014F2550E342.jpg', '100', '99', 'no');

-- ----------------------------
-- Table structure for `t_shoucang`
-- ----------------------------
DROP TABLE IF EXISTS `t_shoucang`;
CREATE TABLE `t_shoucang` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `productId` int(11) DEFAULT NULL,
  `shijian` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_shoucang
-- ----------------------------
INSERT INTO `t_shoucang` VALUES ('9', '153', '30', '2024-02-29 10:29');

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL,
  `xingming` varchar(20) DEFAULT NULL,
  `xingbie` varchar(55) DEFAULT NULL,
  `nianling` int(11) DEFAULT NULL,
  `dizhi` varchar(30) DEFAULT NULL,
  `dianhua` varchar(11) DEFAULT NULL,
  `youxiang` varchar(50) DEFAULT NULL COMMENT 'ͷ',
  `fujian` varchar(55) DEFAULT NULL,
  `shijian` varchar(55) DEFAULT NULL,
  `del` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('152', 'wanggang', '000000', '王刚', '男', '36', '青岛路', '13612345678', '58784454@qq.com', '', '', 'no');
INSERT INTO `t_user` VALUES ('153', 'liusan', '000000', '刘三', '男', '20', '胜利小区', '13545678989', 'liusan@163.com', '', '', 'no');
INSERT INTO `t_user` VALUES ('154', 'limei', '000000', '李梅', '女', '20', '海尔路', '13578451234', '485685@qq.com', '', '', 'no');

-- ----------------------------
-- Table structure for `t_xinwen`
-- ----------------------------
DROP TABLE IF EXISTS `t_xinwen`;
CREATE TABLE `t_xinwen` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `biaoti` varchar(200) DEFAULT NULL,
  `neirong` varchar(3000) DEFAULT NULL,
  `fujian` varchar(255) DEFAULT NULL,
  `fujianYuan` varchar(255) DEFAULT NULL,
  `shijian` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_xinwen
-- ----------------------------
INSERT INTO `t_xinwen` VALUES ('25', '生活购物:资深编辑教你如何网上买大牌', '业内人士分析说，国外大牌电商在中国市场始终&ldquo;落花有意流水无情&rdquo;的原因主要是不能很好的迎合中国消费者的消费习惯，往往造成了&ldquo;高不成、低不就&rdquo;的尴尬境地。而从普通网购消费者的角度出发，国外电商往往形象太过&ldquo;高贵冷艳&rdquo;，除了比国外店铺贵出好几成、完全丢失了&ldquo;海外购&rdquo;的价格优势之外，在用户体验、送货速度、售后服务、个性化服务方面也没有体现出国外大牌应有的优势。所以对于普通白领消费层，不能以优惠的价格来吸引，而对于高级白富美，又不能给予&ldquo;尊贵&rdquo;的购物体验，所以海外电商的失败也是合情合理。品牌自有官网也是一个选择。越来越多的国际大牌在官网上开始尝试零售，但是这些品牌网站的售价与中国专卖店一致的价格。除了没有价格优势，这些官网的购物体验往往与中国消费者习惯有着巨大的区别，消费者很容易知难而退。并且，如果你想要买一身不同品牌的单品，还需要同时关注众多品牌官网，时间笔者更喜欢去美西时尚这样能够一次提供几百个时尚品牌的网站，一次买个够!', '/upload/12EC0518-B2D5-4E7A-8F4F-3ED73D3ECF63.jpg', '5.jpg', '2024-03-02 08:28');
INSERT INTO `t_xinwen` VALUES ('26', '欧盟力促网上购物', '为了促进欧盟经济复苏和增长，欧盟委员会近日提出要加大网上交易量，并希望以此推动整个欧盟的网上购物发展，为消费者和企业的电子商务带来更多的好处，并以更低的价格、更大的商品选择权、更加优质的服务意识，为创造更多的就业机会做出努力。目前，网上采购约占欧盟销售总额的3%，网上市场有着巨大的发展潜力。但在27个欧盟成员国中，还存在许多网上购物的障碍：人们对网上销售的法规不够了解，对有关规则不够清楚；消费者很难找到足够的所要购买商品的信息；网站上商品的价格不够透明，给购物带来困难，这些问题阻碍了消费者网上购物的欲望。网上购物虽然省钱，但对消费者来说，更为安全和更加开放的网上市场才是他们最为关心的。', '/upload/3F708C57-A192-43FA-B7F0-8E6C401EAC7C.jpg', '6.jpg', '2024-03-02 08:30');
INSERT INTO `t_xinwen` VALUES ('27', '长春：白领一族热衷网上购物', '电子商务作为一种新兴的商业模式，其中很关键的一点就是支付问题，特别是使用电子货币在网上直接支付，可以很方便地通过网络购买商品，也是电子商务得以快速发展并吸引众多人关注的魅力所在。而对于网络的安全性问题，媒体更是极度地关注，对于网络黑客、程序漏洞、网上欺诈等等话题津津乐道。其结果无疑使人们对网上购物这种方式增添了不少疑虑。事实上，信用卡的使用在中国还属于一种并不很普及的信用消费支付方式，即使是在星级的饭店或是大型商场中，使用信用卡付帐也不过是采用电话线明码传输信息的方式传递信用卡的有关资料，同样存在安全问题，为什么就没有人对那些简陋的信息传输方式提出安全性的质疑，反倒是采用128位加密措施的网上支付手段，一上来却成了人们关心的安全的焦点所在。', '/upload/21AA83D9-53F9-447C-A3C3-7CE16E3DC97D.jpg', '7.jpg', '2024-03-02 08:31');
INSERT INTO `t_xinwen` VALUES ('28', '网络购物、智能家居，数字经济下的青年生活有何变化', '提到数字经济，你会想到什么?是人工智能、物联网、云计算，还是智能家居、地图导航、网络学习?不久前，中国信息通信研究院发布的《中国数字经济发展报告(2022年)》显示，2021年中国数字经济规模达到45.5万亿元，同比名义增长16.2%，占GDP比重达到39.8%。数字化浪潮下，青年正在拥抱数字经济。数字经济让青年的生活发生了哪些变化?青年感受最深的又是什么?中国青年报社出品的新一期《参数》节目中，几位青年分享了他们有关数字经济的体验和看法。&ldquo;网络购物、移动支付、智能家居、网约车、智慧医疗&hellip;&hellip;各式各样的App如雨后春笋般出现，满足了我们的各项需求。学习、工作、社交、娱乐等都更加便捷、低门槛。&rdquo;00后章润彦觉得，数字经济已经与她的生活紧密联结起来，&ldquo;前不久很多地方推出了数字消费券，通过线上App就能申领并进行支付，也让我极大地感受到了便利&rdquo;。', '/upload/6EAE2E18-3E1E-4FB1-BC67-8873C4CF8678.jpg', '8.jpg', '2024-03-02 08:33');
INSERT INTO `t_xinwen` VALUES ('29', '网上购物的好处，为什么越来越受欢迎？', '随着科技的不断发展，网上购物已经成为了一种时尚和生活方式。它以其独特的便利性和各种优势，吸引着越来越多的消费者。那么，网上购物到底有哪些好处，为什么它如此受欢迎呢？首先，网上购物给人们带来了随时随地的购物便利。无论是在家中、办公室还是在上，只要有网络连接，人们就可以轻松地浏览和购买商品。与传统实体店相比，不再需要排队等待支付或试穿衣物，省去了大量时间和精力。其次，网上购物提供了更多选择的多样性。通过在线，消费者可以轻松找到各种品牌和商品种类。无论是国内还是国际品牌，无论是时尚服饰、电子产品还是日常用品，都可以在网上找到，并且往往比实体店更加丰富。', '/upload/B58A301D-A570-4D41-8F83-5569A2E67FA4.jpg', '33.jpg', '2024-03-02 08:34');
