CREATE DATABASE IF NOT EXISTS project_db CHARACTER SET utf8;
USE `project_db`;
DROP TABLE IF EXISTS `slides`;
CREATE TABLE `slides` (
  `slides_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
  `title` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '内容',
  `url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '链接',
  `img` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '轮播图',
  `hits` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '点击量',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`slides_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='轮播图';
DROP TABLE IF EXISTS `auth`;
CREATE TABLE `auth` (
  `auth_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '授权ID',
  `user_group` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户组',
  `mod_name` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '模块名',
  `table_name` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '表名',
  `page_title` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '页面标题',
  `path` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '路由路径',
  `parent` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '父级菜单',
  `parent_sort` int(10) NOT NULL DEFAULT '0' COMMENT '父级菜单排序',
  `position` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '位置',
  `mode` varchar(32) CHARACTER SET utf8 NOT NULL DEFAULT '_blank' COMMENT '跳转方式',
  `add` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否可增加',
  `del` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否可删除',
  `set` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否可修改',
  `get` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否可查看',
  `field_add` text CHARACTER SET utf8 COMMENT '添加字段',
  `field_set` text CHARACTER SET utf8 COMMENT '修改字段',
  `field_get` text CHARACTER SET utf8 COMMENT '查询字段',
  `table_nav_name` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '跨表导航名称',
  `table_nav` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '跨表导航',
  `option` text CHARACTER SET utf8 COMMENT '配置',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`auth_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户权限管理';
DROP TABLE IF EXISTS `upload`;
CREATE TABLE `upload` (
  `upload_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '上传ID',
  `name` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '文件名',
  `path` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '访问路径',
  `file` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '文件路径',
  `display` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '显示顺序',
  `father_id` int(11) DEFAULT '0' COMMENT '父级ID',
  `dir` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '文件夹',
  `type` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '文件类型',
  PRIMARY KEY (`upload_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文件上传';
insert into `upload` values ('1','movie.mp4','/upload/movie.mp4','',null,'0',null,'video');
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `group_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户组ID',
  `display` smallint(4) unsigned NOT NULL DEFAULT '100' COMMENT '显示顺序',
  `name` varchar(16) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '描述',
  `source_table` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '来源表',
  `source_field` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '来源字段',
  `source_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '来源ID',
  `register` smallint(1) unsigned DEFAULT '0' COMMENT '注册位置',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户组';
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `article_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `title` varchar(125) NOT NULL DEFAULT '' COMMENT '标题',
  `type` varchar(64) NOT NULL DEFAULT '0' COMMENT '文章分类',
  `hits` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '点击数',
  `praise_len` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `source` varchar(255) DEFAULT NULL COMMENT '来源',
  `url` varchar(255) DEFAULT NULL COMMENT '来源地址',
  `tag` varchar(255) DEFAULT NULL COMMENT '标签',
  `content` longtext COMMENT '正文',
  `img` varchar(255) DEFAULT NULL COMMENT '封面图',
  `description` text COMMENT '文章描述',
  PRIMARY KEY (`article_id`,`title`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文章';
DROP TABLE IF EXISTS `access_token`;
CREATE TABLE `access_token` (
  `token_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '临时访问牌ID',
  `token` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '临时访问牌',
  `info` text CHARACTER SET utf8 COMMENT '信息',
  `maxage` int(2) NOT NULL DEFAULT '2' COMMENT '最大寿命：默认2小时',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户编号',
  PRIMARY KEY (`token_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='登陆访问时长';
insert into `access_token` values ('57','5accf85cb6a7f06f0aa2968deadaec1b',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('58','46ff1d4d07714f046ba07b34bffe0af9',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('59','ed9d6cba9826fda1beafcd9326be7a86',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('60','c99763c1833ea0785d9e2b81da3fd28f',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('61','33fbfaccd6d1cb9143e4129bd919d4b0',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('62','493e13da5f293ba67a56a0fe3e1fa6cf',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('63','c4b48e9e2160db09c703041a8fee0a1f',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('64','d13cdaefd3823c360c959a02a262f71d',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('65','6c6ff426fd77ea5a2025ce5ed2e42c8a',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('66','80930065a61ffcdd5cbb75f60932973c',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('67','94114763cf2e3b020495d8a27096d4ef',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('68','761052c551c97c9317bc3aa475c85b84',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('69','7c44ef14131a0ba7c16aa16cef104065',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('70','96380f3d9542c80d04bdade1cf7635a5',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('71','bfdc7acfcbf5763fda81945b60961222',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('72','170a598e51ae8ae2badde20a42fe171d',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('73','c82c357488c75926a92d8a9608d4b367',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('74','4d35290c023f407a820f37dbbb1ceb09',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('75','8d19162776682b695c0f62f3c7a92fec',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('76','a7ea2cdc9a2be179e19200e593ad5a69',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('77','c79a554f9832adc01f19682c5d576bc4',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('78','1c7d95001fa09951a679841c8100ad1f',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('79','776da1bcdd01ddb3cbf0a37fa13fc5b0',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('80','d336e88e57c329d0166931292c1fac41',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('81','37a40f526a6c82fc6110b512802d35bf',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('82','691ad331771f4109206d58aeee572371',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('83','9942e458886219960d3344b4a6a6fbec',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('84','e9939a8b7ccf9f548f0bbb5664981f96',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('85','f5b27912060d1909bef61fab9d96faae',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('86','7c5888682f1d449eb1b62f0054a79fbf',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('87','00dfdc6ac21c4a9da80fd71c990764d1',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('88','3cce592bc72840ab932ce96d85a194da',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('89','43fdaa989a644ad683ef4b4d488e8629',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('90','d6a3cecadacff0dbd6b43b25372cc2a2',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('91','5570bc5b07b3589f4ef8553bd46eb0d1',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('92','5570bc5b07b3589f4ef8553bd46eb0d1',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('93','26c553bd2ee2ab6605d18dfd310d85f9',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('94','3fd52f81236ed2c37ff91a6696d4e47a',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('95','893332e9ee67d60d8312b3700c58a359',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('96','b7844068ade535b2e517df4a40948703',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('97','179b37a5e1893c3af6b946bd5a1c8625',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('98','3a47b8a040a83ebbc9194cb255dc668c',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('99','afa60196afb77dcc2b520ed13a817560',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('100','7fc6d9b324f8c0a3a1784d04ef132692',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
insert into `access_token` values ('101','84e31b291f2bde6b7ceb27af5fe8eee3',null,'2','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','1');
DROP TABLE IF EXISTS `code_token`;
CREATE TABLE `code_token` (
  `code_token_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '验证码ID',
  `token` varchar(255) CHARACTER SET latin1 DEFAULT NULL COMMENT '令牌',
  `code` varchar(255) CHARACTER SET latin1 DEFAULT NULL COMMENT '验证码',
  `expire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '失效时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`code_token_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='验证码';
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
  `collect_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '收藏人ID',
  `source_table` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '来源表',
  `source_field` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '来源字段',
  `source_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '来源ID',
  `title` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '标题',
  `img` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '封面',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`collect_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='收藏';
DROP TABLE IF EXISTS `forum_type`;
CREATE TABLE `forum_type` (
  `type_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(16) NOT NULL DEFAULT '' COMMENT '分类名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `url` varchar(255) DEFAULT NULL COMMENT '外链地址',
  `father_id` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '上级分类ID',
  `icon` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '分类图标',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='论坛分类';
insert into `forum_type` values ('1','分类一','分类一','/article/list?type_id=1','0',null,'2025-03-31 18:21:49.0','2025-03-31 18:21:49.0');
insert into `forum_type` values ('2','分类二','分类二','/article/list?type_id=2','0',null,'2025-03-31 18:21:49.0','2025-03-31 18:21:49.0');
insert into `forum_type` values ('3','分类三','分类三','/article/list?type_id=3','0',null,'2025-03-31 18:21:49.0','2025-03-31 18:21:49.0');
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `notice_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(125) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '标题',
  `content` longtext CHARACTER SET utf8 COMMENT '正文',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='公告';
insert into `notice` values ('1','网站公告','<p>公告，是指政府、团体对重大事件当众正式公布或者公开宣告，宣布。国务院2012年4月16日发布、2012年7月1日起施行的《党政机关公文处理工作条例》，对公告的使用表述为：“适用于向国内外宣布重要事项或者法定事项”。其中包含两方面的内容：一是向国内外宣布重要事项，公布依据政策、法令采取的重大行动等；二是向国内外宣布法定事项，公布依据法律规定告知国内外的有关重要规定和重大行动等。</p>','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0');
insert into `notice` values ('2','关于我们','<p>       一个网站要取得成功，要有先进的理念、先进的思想，更为重要的是抢占先机，及时行动。网络世界可谓一日千里、 日新月异，一个网站只有把握先机，抓住机遇，才</p><p>可能有更多的机会获得成功，可能处于网络行业发展的致高点，可能创建出成功的网站，才能能获得成功。要知道一种网站新模式在网络上只有保持几天的优势,因为人们很容易“COPY" 和模仿，因此，唯有不</p><p>断创新，不失时机地推出新的服务、新的模式、新的思想，网站才可能长久立于不败之地。</p>','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0');
insert into `notice` values ('3','联系方式','<h3>网站内容及品牌合作</h3><p>Email：xxxx@qq.com</p><h3>商务合作</h3><p>电话：010-xxxxxxx</p><p>Email：xxxx@qq.com</p><h3><br></h3><h3><br></h3><p><br></p>','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0');
insert into `notice` values ('4','网站介绍','<p>此处可上传文字、图片、视频、超链接、表格等内容区</p>','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0');
DROP TABLE IF EXISTS `article_type`;
CREATE TABLE `article_type` (
  `type_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `display` smallint(4) unsigned NOT NULL DEFAULT '100' COMMENT '显示顺序',
  `name` varchar(16) NOT NULL DEFAULT '' COMMENT '分类名称',
  `father_id` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '上级分类ID',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `icon` text COMMENT '分类图标',
  `url` varchar(255) DEFAULT NULL COMMENT '外链地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文章分类';
DROP TABLE IF EXISTS `praise`;
CREATE TABLE `praise` (
  `praise_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '点赞人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `source_table` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '来源表',
  `source_field` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '来源字段',
  `source_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '来源ID',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '点赞状态:1为点赞，0已取消',
  PRIMARY KEY (`praise_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='点赞';
DROP TABLE IF EXISTS `forum`;
CREATE TABLE `forum` (
  `forum_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '论坛ID',
  `display` smallint(5) unsigned NOT NULL DEFAULT '100' COMMENT '排序',
  `user_id` mediumint(8) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `nickname` varchar(16) CHARACTER SET utf8 DEFAULT '' COMMENT '昵称',
  `praise_len` int(10) DEFAULT '0' COMMENT '点赞数',
  `hits` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '访问数',
  `title` varchar(125) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '标题',
  `keywords` varchar(125) CHARACTER SET utf8 DEFAULT NULL COMMENT '关键词',
  `description` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '描述',
  `url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '来源地址',
  `tag` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '标签',
  `img` text CHARACTER SET utf8 COMMENT '封面图',
  `content` longtext CHARACTER SET utf8 COMMENT '正文',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `avatar` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '发帖人头像',
  `type` varchar(64) NOT NULL DEFAULT '0' COMMENT '论坛分类',
  `istop` int(10) NOT NULL DEFAULT '0' COMMENT '是否置顶',
  PRIMARY KEY (`forum_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='论坛';
insert into `forum` values ('1','100','1','小明','1','150','测试标题','关键字1','描述','#','标签','/upload/forum_1.jpg','<h1>fafgwagbagbwgwag</h1>','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','http://localhost:5000/upload/jingdian (11)_15.jpg','分类二','0');
insert into `forum` values ('2','100','2','小明','0','30','测试标题2','关键字2','dec','#','标签','/upload/forum_2.jpg','<p>测试文章内容2</p>','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','','分类一','0');
insert into `forum` values ('3','100','2','小红','0','42','测试标题3','关键字3','dec2','#','标签','/upload/forum_3.jpg','<p>测试文章内容3</p>','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','','分类二','0');
insert into `forum` values ('4','100','2','小红','0','22','测试标题4','关键字4','dec3','#','标签','/upload/forum_4.jpg','<p>测试文章内容4</p>','2025-03-31 18:21:49.0','2025-03-31 18:21:49.0','','分类三','0');
DROP TABLE IF EXISTS `hits`;
CREATE TABLE `hits` (
  `hits_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '点赞人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `source_table` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '来源表',
  `source_field` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '来源字段',
  `source_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '来源ID',
  PRIMARY KEY (`hits_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户点击';
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `comment_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '评论人ID',
  `reply_to_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '回复评论ID',
  `content` longtext CHARACTER SET utf8 COMMENT '内容',
  `nickname` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '头像地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `source_table` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '来源表',
  `source_field` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '来源字段',
  `source_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '来源ID',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='评论';
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `state` smallint(1) unsigned NOT NULL DEFAULT '1' COMMENT '账户状态：(1可用|2异常|3已冻结|4已注销)',
  `user_group` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '所在用户组',
  `login_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上次登录时间',
  `phone` varchar(11) CHARACTER SET utf8 DEFAULT NULL COMMENT '手机号码',
  `phone_state` smallint(1) unsigned NOT NULL DEFAULT '0' COMMENT '手机认证：(0未认证|1审核中|2已认证)',
  `username` varchar(16) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '用户名',
  `nickname` varchar(16) CHARACTER SET utf8 DEFAULT '' COMMENT '昵称',
  `password` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '密码',
  `email` varchar(64) CHARACTER SET utf8 DEFAULT '' COMMENT '邮箱',
  `email_state` smallint(1) unsigned NOT NULL DEFAULT '0' COMMENT '邮箱认证：(0未认证|1审核中|2已认证)',
  `avatar` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '头像地址',
  `open_id` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '针对获取用户信息字段',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `vip_level` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '会员等级',
  `vip_discount` double(10,2) DEFAULT '0.00' COMMENT '会员折扣',
  `integral` int(11) DEFAULT '0' COMMENT '积分',
  `balance` double(10,2) DEFAULT '0.00' COMMENT '余额',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户账户';
insert into `user` values ('1','1','管理员','2025-03-02 13:36:54.0',null,'0','admin','admin','bfd59291e825b5f2bbf1eb76569f8fe7','','0','/api/upload/admin_avatar.jpg',null,'2025-03-31 17:35:13.0',null,'0.0','0','0.0');
ALTER TABLE `user` DROP COLUMN balance;
ALTER TABLE `user` DROP COLUMN integral;
ALTER TABLE `user` DROP COLUMN vip_level;
ALTER TABLE `user` DROP COLUMN vip_discount;
UPDATE `user` SET password = 'asd123';
UPDATE user SET avatar = '/api/upload/admin_avatar.jpg';
DROP TABLE IF EXISTS `registered_user`;
CREATE TABLE `registered_user`(registered_user_id int(11) NOT NULL AUTO_INCREMENT COMMENT '注册用户ID',
`user_name` varchar(64) comment '用户姓名',
`user_gender` varchar(64) comment '用户性别',
`examine_state` varchar(16) DEFAULT '已通过' NOT NULL comment '审核状态',
`user_id` int(11) DEFAULT '0' NOT NULL comment '用户ID',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (registered_user_id))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '注册用户';
insert into `registered_user` values (1,'用户姓名1','用户性别1','已通过',2,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into user(user_group,username,nickname,password,avatar) values ('注册用户','yonghu1','yonghu1','asd123','/api/upload/default_avatar.jpg');
insert into `registered_user` values (2,'用户姓名2','用户性别2','已通过',3,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into user(user_group,username,nickname,password,avatar) values ('注册用户','yonghu2','yonghu2','asd123','/api/upload/default_avatar.jpg');
insert into `registered_user` values (3,'用户姓名3','用户性别3','已通过',4,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into user(user_group,username,nickname,password,avatar) values ('注册用户','yonghu3','yonghu3','asd123','/api/upload/default_avatar.jpg');

DROP TABLE IF EXISTS `skin_detection`;
CREATE TABLE `skin_detection`(skin_detection_id int(11) NOT NULL AUTO_INCREMENT COMMENT '皮肤检测ID',
`registered_user` int(11) DEFAULT 0 comment '注册用户',
`user_name` varchar(64) comment '用户姓名',
`user_gender` varchar(64) comment '用户性别',
`user_age` double(8,2) DEFAULT 0 comment '用户年龄',
`entry_date` date comment '录入日期',
`detection_picture` varchar(255) comment '检测图片',
`skin_condition` text comment '皮肤情况',
`test_results` varchar(64) comment '检测结果',
`test_description` text comment '检测说明',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (skin_detection_id))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '皮肤检测';
insert into `skin_detection` values (1,0,'用户姓名1','用户性别1',1,'2025-04-04','','皮肤情况1','检测结果1','检测说明1','2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `skin_detection` values (2,0,'用户姓名2','用户性别2',2,'2025-04-04','','皮肤情况2','检测结果2','检测说明2','2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `skin_detection` values (3,0,'用户姓名3','用户性别3',3,'2025-04-04','','皮肤情况3','检测结果3','检测说明3','2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `skin_detection` values (4,0,'用户姓名4','用户性别4',4,'2025-04-04','','皮肤情况4','检测结果4','检测说明4','2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `skin_detection` values (5,0,'用户姓名5','用户性别5',5,'2025-04-04','','皮肤情况5','检测结果5','检测说明5','2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `skin_detection` values (6,0,'用户姓名6','用户性别6',6,'2025-04-04','','皮肤情况6','检测结果6','检测说明6','2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `skin_detection` values (7,0,'用户姓名7','用户性别7',7,'2025-04-04','','皮肤情况7','检测结果7','检测说明7','2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `skin_detection` values (8,0,'用户姓名8','用户性别8',8,'2025-04-04','','皮肤情况8','检测结果8','检测说明8','2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `skin_detection` values (9,0,'用户姓名9','用户性别9',9,'2025-04-04','','皮肤情况9','检测结果9','检测说明9','2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `skin_detection` values (10,0,'用户姓名10','用户性别10',10,'2025-04-04','','皮肤情况10','检测结果10','检测说明10','2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `skin_detection` values (11,0,'用户姓名11','用户性别11',11,'2025-04-04','','皮肤情况11','检测结果11','检测说明11','2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `skin_detection` values (12,0,'用户姓名12','用户性别12',12,'2025-04-04','','皮肤情况12','检测结果12','检测说明12','2025-04-04 21:23:15','2025-04-04 21:23:15');

DROP TABLE IF EXISTS `knowledge_popularization`;
CREATE TABLE `knowledge_popularization`(knowledge_popularization_id int(11) NOT NULL AUTO_INCREMENT COMMENT '知识科普ID',
`name_of_skin_disease` varchar(64) comment '皮肤病名称',
`types_of_skin_diseases` varchar(64) comment '皮肤病种类',
`release_date` date comment '发布日期',
`treatment_mode` varchar(64) comment '治疗方式',
`popular_science_pictures` varchar(255) comment '科普图片',
`popular_science_video` varchar(255) comment '科普视频',
`skin_disease_introduction` text comment '皮肤病介绍',
`treatment_plan` text comment '治疗方案',
`science_popularization_details` longtext comment '科普详情',
`hits` int(11) DEFAULT 0 NOT NULL comment '点击数',
`praise_len` int(11) DEFAULT 0 NOT NULL comment '点赞数',
`collect_len` int(11) DEFAULT 0 NOT NULL comment '收藏数',
`comment_len` int(11) DEFAULT 0 NOT NULL comment '评论数',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (knowledge_popularization_id))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '知识科普';
insert into `knowledge_popularization` values (1,'皮肤病名称1','皮肤病种类1','2025-04-04','治疗方式1','/api/upload/1908019007013781504.jpg','','皮肤病介绍1','治疗方案1','此处可上传文字、图片、视频、超链接等内容区1',388,490,464,0,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `knowledge_popularization` values (2,'皮肤病名称2','皮肤病种类2','2025-04-04','治疗方式2','/api/upload/1908018995965984768.jpg','','皮肤病介绍2','治疗方案2','此处可上传文字、图片、视频、超链接等内容区2',761,239,160,0,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `knowledge_popularization` values (3,'皮肤病名称3','皮肤病种类3','2025-04-04','治疗方式3','/api/upload/1908018926835466240.jpg','','皮肤病介绍3','治疗方案3','此处可上传文字、图片、视频、超链接等内容区3',202,887,23,0,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `knowledge_popularization` values (4,'皮肤病名称4','皮肤病种类4','2025-04-04','治疗方式4','/api/upload/1908018948733927425.jpg','','皮肤病介绍4','治疗方案4','此处可上传文字、图片、视频、超链接等内容区4',842,788,920,0,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `knowledge_popularization` values (5,'皮肤病名称5','皮肤病种类5','2025-04-04','治疗方式5','/api/upload/1908019031051337728.jpg','','皮肤病介绍5','治疗方案5','此处可上传文字、图片、视频、超链接等内容区5',8,44,522,0,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `knowledge_popularization` values (6,'皮肤病名称6','皮肤病种类6','2025-04-04','治疗方式6','/api/upload/1908018973023141889.jpg','','皮肤病介绍6','治疗方案6','此处可上传文字、图片、视频、超链接等内容区6',372,738,595,0,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `knowledge_popularization` values (7,'皮肤病名称7','皮肤病种类7','2025-04-04','治疗方式7','/api/upload/1908018937056985089.jpg','','皮肤病介绍7','治疗方案7','此处可上传文字、图片、视频、超链接等内容区7',272,143,55,0,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `knowledge_popularization` values (8,'皮肤病名称8','皮肤病种类8','2025-04-04','治疗方式8','/api/upload/1908019020217450497.jpg','','皮肤病介绍8','治疗方案8','此处可上传文字、图片、视频、超链接等内容区8',562,617,315,0,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `knowledge_popularization` values (9,'皮肤病名称9','皮肤病种类9','2025-04-04','治疗方式9','/api/upload/1908018961744658433.jpg','','皮肤病介绍9','治疗方案9','此处可上传文字、图片、视频、超链接等内容区9',286,10,322,0,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `knowledge_popularization` values (10,'皮肤病名称10','皮肤病种类10','2025-04-04','治疗方式10','/api/upload/1908018984674918400.jpg','','皮肤病介绍10','治疗方案10','此处可上传文字、图片、视频、超链接等内容区10',893,493,355,0,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `knowledge_popularization` values (11,'皮肤病名称11','皮肤病种类11','2025-04-04','治疗方式11','/api/upload/1908018916450369537.jpg','','皮肤病介绍11','治疗方案11','此处可上传文字、图片、视频、超链接等内容区11',172,638,677,0,'2025-04-04 21:23:15','2025-04-04 21:23:15');
insert into `knowledge_popularization` values (12,'皮肤病名称12','皮肤病种类12','2025-04-04','治疗方式12','/api/upload/1908018903120871424.jpg','','皮肤病介绍12','治疗方案12','此处可上传文字、图片、视频、超链接等内容区12',192,602,27,0,'2025-04-04 21:23:15','2025-04-04 21:23:15');

DROP TABLE IF EXISTS `hospital_information`;
CREATE TABLE `hospital_information`(hospital_information_id int(11) NOT NULL AUTO_INCREMENT COMMENT '医院信息ID',
`hospital_name` varchar(64) comment '医院名称',
`hospital_address` varchar(64) comment '医院地址',
`type_of_hospital` varchar(64) comment '医院类型',
`distance_from_hospital` varchar(64) comment '医院距离',
`hospital_phone_number` varchar(64) comment '医院电话',
`hospital_score` varchar(64) comment '医院评分',
`hospital_pictures` varchar(255) comment '医院图片',
`areas_of_expertise` text comment '擅长领域',
`hospital_introduction` text comment '医院介绍',
`hospital_details` longtext comment '医院详情',
`hits` int(11) DEFAULT 0 NOT NULL comment '点击数',
`praise_len` int(11) DEFAULT 0 NOT NULL comment '点赞数',
`collect_len` int(11) DEFAULT 0 NOT NULL comment '收藏数',
`comment_len` int(11) DEFAULT 0 NOT NULL comment '评论数',
`recommend` int(11) DEFAULT '0' NOT NULL comment '智能推荐',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (hospital_information_id))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '医院信息';
insert into `hospital_information` values (1,'医院名称1','医院地址1','医院类型1','医院距离1','医院电话1','医院评分1','/api/upload/1816337650810880000.jpg','擅长领域1','医院介绍1','此处可上传文字、图片、视频、超链接等内容区1',104,624,95,0,0,'2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `hospital_information` values (2,'医院名称2','医院地址2','医院类型2','医院距离2','医院电话2','医院评分2','/api/upload/1816337788325330944.jpg','擅长领域2','医院介绍2','此处可上传文字、图片、视频、超链接等内容区2',908,327,44,0,0,'2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `hospital_information` values (3,'医院名称3','医院地址3','医院类型3','医院距离3','医院电话3','医院评分3','/api/upload/1816337597853597697.jpg','擅长领域3','医院介绍3','此处可上传文字、图片、视频、超链接等内容区3',425,592,519,0,0,'2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `hospital_information` values (4,'医院名称4','医院地址4','医院类型4','医院距离4','医院电话4','医院评分4','/api/upload/1816337432853872640.jpg','擅长领域4','医院介绍4','此处可上传文字、图片、视频、超链接等内容区4',633,912,979,0,0,'2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `hospital_information` values (5,'医院名称5','医院地址5','医院类型5','医院距离5','医院电话5','医院评分5','/api/upload/1816337475136651264.jpg','擅长领域5','医院介绍5','此处可上传文字、图片、视频、超链接等内容区5',6,715,292,0,0,'2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `hospital_information` values (6,'医院名称6','医院地址6','医院类型6','医院距离6','医院电话6','医院评分6','/api/upload/1816338255088451584.jpg','擅长领域6','医院介绍6','此处可上传文字、图片、视频、超链接等内容区6',300,268,807,0,0,'2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `hospital_information` values (7,'医院名称7','医院地址7','医院类型7','医院距离7','医院电话7','医院评分7','/api/upload/1816338307366256641.jpg','擅长领域7','医院介绍7','此处可上传文字、图片、视频、超链接等内容区7',767,576,20,0,0,'2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `hospital_information` values (8,'医院名称8','医院地址8','医院类型8','医院距离8','医院电话8','医院评分8','/api/upload/1816337329858543616.jpg','擅长领域8','医院介绍8','此处可上传文字、图片、视频、超链接等内容区8',19,490,147,0,0,'2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `hospital_information` values (9,'医院名称9','医院地址9','医院类型9','医院距离9','医院电话9','医院评分9','/api/upload/1816337369113034753.jpg','擅长领域9','医院介绍9','此处可上传文字、图片、视频、超链接等内容区9',737,258,349,0,0,'2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `hospital_information` values (10,'医院名称10','医院地址10','医院类型10','医院距离10','医院电话10','医院评分10','/api/upload/1816337709191397377.jpg','擅长领域10','医院介绍10','此处可上传文字、图片、视频、超链接等内容区10',915,960,214,0,0,'2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `hospital_information` values (11,'医院名称11','医院地址11','医院类型11','医院距离11','医院电话11','医院评分11','/api/upload/1816337521903140865.jpg','擅长领域11','医院介绍11','此处可上传文字、图片、视频、超链接等内容区11',99,433,880,0,0,'2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `hospital_information` values (12,'医院名称12','医院地址12','医院类型12','医院距离12','医院电话12','医院评分12','/api/upload/1816337276850929664.jpg','擅长领域12','医院介绍12','此处可上传文字、图片、视频、超链接等内容区12',366,571,759,0,0,'2025-04-04 21:23:16','2025-04-04 21:23:16');

DROP TABLE IF EXISTS `online_consultation`;
CREATE TABLE `online_consultation`(online_consultation_id int(11) NOT NULL AUTO_INCREMENT COMMENT '在线咨询ID',
`registered_user` int(11) DEFAULT 0 comment '注册用户',
`user_name` varchar(64) comment '用户姓名',
`advisory_title` varchar(64) comment '咨询标题',
`consulting_category` varchar(64) comment '咨询类别',
`consulting_content` text comment '咨询内容',
`reply_content` text comment '回复内容',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (online_consultation_id))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '在线咨询';
insert into `online_consultation` values (1,0,'用户姓名1','咨询标题1','咨询类别1','咨询内容1','回复内容1','2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `online_consultation` values (2,0,'用户姓名2','咨询标题2','咨询类别2','咨询内容2','回复内容2','2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `online_consultation` values (3,0,'用户姓名3','咨询标题3','咨询类别3','咨询内容3','回复内容3','2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `online_consultation` values (4,0,'用户姓名4','咨询标题4','咨询类别4','咨询内容4','回复内容4','2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `online_consultation` values (5,0,'用户姓名5','咨询标题5','咨询类别5','咨询内容5','回复内容5','2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `online_consultation` values (6,0,'用户姓名6','咨询标题6','咨询类别6','咨询内容6','回复内容6','2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `online_consultation` values (7,0,'用户姓名7','咨询标题7','咨询类别7','咨询内容7','回复内容7','2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `online_consultation` values (8,0,'用户姓名8','咨询标题8','咨询类别8','咨询内容8','回复内容8','2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `online_consultation` values (9,0,'用户姓名9','咨询标题9','咨询类别9','咨询内容9','回复内容9','2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `online_consultation` values (10,0,'用户姓名10','咨询标题10','咨询类别10','咨询内容10','回复内容10','2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `online_consultation` values (11,0,'用户姓名11','咨询标题11','咨询类别11','咨询内容11','回复内容11','2025-04-04 21:23:16','2025-04-04 21:23:16');
insert into `online_consultation` values (12,0,'用户姓名12','咨询标题12','咨询类别12','咨询内容12','回复内容12','2025-04-04 21:23:16','2025-04-04 21:23:16');

insert into `auth` values ('1','管理员','注册用户','registered_user','注册用户','/registered_user/table','','0','','_blank','1','1','1','1','user_name,user_gender','user_name,user_gender','user_name,user_gender',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('2','管理员','注册用户','registered_user','用户详情','/registered_user/view','','0','','_blank','1','1','1','1','user_name,user_gender','user_name,user_gender','user_name,user_gender',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('3','管理员','皮肤检测','skin_detection','皮肤检测','/skin_detection/table','','0','','_blank','1','1','1','1','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('4','管理员','皮肤检测','skin_detection','检测详情','/skin_detection/view','','0','','_blank','1','1','1','1','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('5','管理员','皮肤检测','skin_detection','皮肤检测','/skin_detection/edit','','0','top','_blank','1','1','1','1','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('6','管理员','知识科普','knowledge_popularization','知识科普','/knowledge_popularization/table','','0','','_blank','1','1','1','1','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details',null,'0','{"can_show_comment":true}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('7','管理员','知识科普','knowledge_popularization','科普详情','/knowledge_popularization/view','','0','','_blank','1','1','1','1','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('8','管理员','知识科普','knowledge_popularization','知识科普','/knowledge_popularization/list','','0','top','_blank','1','1','1','1','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('9','管理员','知识科普','knowledge_popularization','科普详情','/knowledge_popularization/details','','0','','_blank','1','1','1','1','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details',null,'0','{"can_comment":true,"can_show_comment":true}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('10','管理员','医院信息','hospital_information','医院信息','/hospital_information/table','','0','','_blank','1','1','1','1','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details',null,'0','{"can_show_comment":true}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('11','管理员','医院信息','hospital_information','信息详情','/hospital_information/view','','0','','_blank','1','1','1','1','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('12','管理员','医院信息','hospital_information','医院信息','/hospital_information/list','','0','top','_blank','1','1','1','1','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('13','管理员','医院信息','hospital_information','信息详情','/hospital_information/details','','0','','_blank','1','1','1','1','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details',null,'0','{"can_comment":true,"can_show_comment":true}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('14','管理员','在线咨询','online_consultation','在线咨询','/online_consultation/table','','0','','_blank','1','1','1','1','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('15','管理员','在线咨询','online_consultation','咨询详情','/online_consultation/view','','0','','_blank','1','1','1','1','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('16','管理员','在线咨询','online_consultation','在线咨询','/online_consultation/edit','','0','top','_blank','1','1','1','1','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('17','管理员','我的收藏','collect','我的收藏','/collect/list','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('18','管理员','评论','comment','评论列表','/comment/table','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('19','管理员','评论','comment','评论详情','/comment/view','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('20','管理员','评论','comment','我的评论','/comment/list','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('21','管理员','评论','comment','评论详情','/comment/details','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('22','管理员','病友交流','forum','病友交流','/forum/table','','0','','_blank','1','1','1','1','','','',null,'0','{"print":true,"export_db":true,"import_db":true}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('23','管理员','病友交流','forum','交流详情','/forum/view','','0','','_blank','1','1','1','1','','','',null,'0','{"print":true}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('24','管理员','病友交流','forum','病友交流','/forum/list','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('25','管理员','病友交流','forum','交流详情','/forum/details','','0','','_blank','1','1','1','1','','','',null,'0','{"can_comment":true,"can_show_comment":true}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('26','管理员','交流分类','forum_type','交流分类','/forum_type/table','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('27','管理员','交流分类','forum_type','分类详情','/forum_type/view','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('28','管理员','新闻资讯','article','新闻资讯','/article/table','','0','','_blank','1','1','1','1','','','',null,'0','{"print":true,"export_db":true,"import_db":true}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('29','管理员','新闻资讯','article','资讯详情','/article/view','','0','','_blank','1','1','1','1','','','',null,'0','{"print":true}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('30','管理员','新闻资讯','article','新闻资讯','/article/list','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('31','管理员','新闻资讯','article','资讯详情','/article/details','','0','','_blank','1','1','1','1','','','',null,'0','{"can_comment":true,"can_show_comment":true}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('32','管理员','资讯分类','article_type','资讯分类','/article_type/table','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('33','管理员','资讯分类','article_type','分类详情','/article_type/view','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('34','管理员','公告信息','notice','公告信息','/notice/table','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('35','管理员','公告信息','notice','信息详情','/notice/view','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('36','管理员','公告信息','notice','公告信息','/notice/list','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('37','管理员','公告信息','notice','信息详情','/notice/details','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('38','游客','注册用户','registered_user','注册用户','/registered_user/table','','0','','_blank','1','0','0','0','user_name,user_gender','user_name,user_gender','user_name,user_gender',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('39','游客','注册用户','registered_user','用户详情','/registered_user/view','','0','','_blank','1','0','0','0','user_name,user_gender','user_name,user_gender','user_name,user_gender',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('40','游客','皮肤检测','skin_detection','皮肤检测','/skin_detection/table','','0','','_blank','0','0','0','0','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('41','游客','皮肤检测','skin_detection','检测详情','/skin_detection/view','','0','','_blank','0','0','0','0','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('42','游客','皮肤检测','skin_detection','皮肤检测','/skin_detection/edit','','0','top','_blank','0','0','0','0','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('43','游客','知识科普','knowledge_popularization','知识科普','/knowledge_popularization/table','','0','','_blank','0','0','0','0','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details',null,'0','{"can_show_comment":false}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('44','游客','知识科普','knowledge_popularization','科普详情','/knowledge_popularization/view','','0','','_blank','0','0','0','0','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('45','游客','知识科普','knowledge_popularization','知识科普','/knowledge_popularization/list','','0','top','_blank','0','0','0','1','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('46','游客','知识科普','knowledge_popularization','科普详情','/knowledge_popularization/details','','0','','_blank','0','0','0','1','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details',null,'0','{"can_comment":false,"can_show_comment":false}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('47','游客','医院信息','hospital_information','医院信息','/hospital_information/table','','0','','_blank','0','0','0','0','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details',null,'0','{"can_show_comment":false}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('48','游客','医院信息','hospital_information','信息详情','/hospital_information/view','','0','','_blank','0','0','0','0','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('49','游客','医院信息','hospital_information','医院信息','/hospital_information/list','','0','top','_blank','0','0','0','1','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('50','游客','医院信息','hospital_information','信息详情','/hospital_information/details','','0','','_blank','0','0','0','1','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details',null,'0','{"can_comment":false,"can_show_comment":false}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('51','游客','在线咨询','online_consultation','在线咨询','/online_consultation/table','','0','','_blank','0','0','0','0','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('52','游客','在线咨询','online_consultation','咨询详情','/online_consultation/view','','0','','_blank','0','0','0','0','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('53','游客','在线咨询','online_consultation','在线咨询','/online_consultation/edit','','0','top','_blank','0','0','0','0','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('54','游客','我的收藏','collect','我的收藏','/collect/list','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('55','游客','评论','comment','评论列表','/comment/table','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('56','游客','评论','comment','评论详情','/comment/view','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('57','游客','评论','comment','我的评论','/comment/list','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('58','游客','评论','comment','评论详情','/comment/details','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('59','游客','病友交流','forum','病友交流','/forum/table','','0','','_blank','0','0','0','0','','','',null,'0','{"print":false,"export_db":false,"import_db":false}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('60','游客','病友交流','forum','交流详情','/forum/view','','0','','_blank','0','0','0','0','','','',null,'0','{"print":false}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('61','游客','病友交流','forum','病友交流','/forum/list','','0','','_blank','0','0','0','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('62','游客','病友交流','forum','交流详情','/forum/details','','0','','_blank','0','0','0','1','','','',null,'0','{"can_comment":false,"can_show_comment":false}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('63','游客','交流分类','forum_type','交流分类','/forum_type/table','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('64','游客','交流分类','forum_type','分类详情','/forum_type/view','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('65','游客','新闻资讯','article','新闻资讯','/article/table','','0','','_blank','0','0','0','0','','','',null,'0','{"print":false,"export_db":false,"import_db":false}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('66','游客','新闻资讯','article','资讯详情','/article/view','','0','','_blank','0','0','0','0','','','',null,'0','{"print":false}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('67','游客','新闻资讯','article','新闻资讯','/article/list','','0','','_blank','0','0','0','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('68','游客','新闻资讯','article','资讯详情','/article/details','','0','','_blank','0','0','0','1','','','',null,'0','{"can_comment":false,"can_show_comment":false}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('69','游客','资讯分类','article_type','资讯分类','/article_type/table','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('70','游客','资讯分类','article_type','分类详情','/article_type/view','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('71','游客','公告信息','notice','公告信息','/notice/table','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('72','游客','公告信息','notice','信息详情','/notice/view','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('73','游客','公告信息','notice','公告信息','/notice/list','','0','','_blank','0','0','0','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('74','游客','公告信息','notice','信息详情','/notice/details','','0','','_blank','0','0','0','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('75','注册用户','注册用户','registered_user','注册用户','/registered_user/table','','0','','_blank','0','0','0','0','user_name,user_gender','user_name,user_gender','user_name,user_gender',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('76','注册用户','注册用户','registered_user','用户详情','/registered_user/view','','0','','_blank','0','0','1','1','user_name,user_gender','user_name,user_gender','user_name,user_gender',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('77','注册用户','皮肤检测','skin_detection','皮肤检测','/skin_detection/table','','0','','_blank','0','0','0','1','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('78','注册用户','皮肤检测','skin_detection','检测详情','/skin_detection/view','','0','','_blank','0','0','0','1','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition,test_results,test_description',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('79','注册用户','皮肤检测','skin_detection','皮肤检测','/skin_detection/edit','','0','top','_blank','1','1','1','1','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition','registered_user,user_name,user_gender,user_age,entry_date,detection_picture,skin_condition',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('80','注册用户','知识科普','knowledge_popularization','知识科普','/knowledge_popularization/table','','0','','_blank','0','0','0','0','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details',null,'0','{"can_show_comment":false}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('81','注册用户','知识科普','knowledge_popularization','科普详情','/knowledge_popularization/view','','0','','_blank','0','0','0','0','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('82','注册用户','知识科普','knowledge_popularization','知识科普','/knowledge_popularization/list','','0','top','_blank','1','1','1','1','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('83','注册用户','知识科普','knowledge_popularization','科普详情','/knowledge_popularization/details','','0','','_blank','1','1','1','1','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details','name_of_skin_disease,types_of_skin_diseases,release_date,treatment_mode,popular_science_pictures,popular_science_video,skin_disease_introduction,treatment_plan,science_popularization_details',null,'0','{"can_comment":true,"can_show_comment":true}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('84','注册用户','医院信息','hospital_information','医院信息','/hospital_information/table','','0','','_blank','0','0','0','0','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details',null,'0','{"can_show_comment":false}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('85','注册用户','医院信息','hospital_information','信息详情','/hospital_information/view','','0','','_blank','0','0','0','0','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('86','注册用户','医院信息','hospital_information','医院信息','/hospital_information/list','','0','top','_blank','1','1','1','1','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('87','注册用户','医院信息','hospital_information','信息详情','/hospital_information/details','','0','','_blank','1','1','1','1','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details','hospital_name,hospital_address,type_of_hospital,distance_from_hospital,hospital_phone_number,hospital_score,hospital_pictures,areas_of_expertise,hospital_introduction,hospital_details',null,'0','{"can_comment":true,"can_show_comment":true}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('88','注册用户','在线咨询','online_consultation','在线咨询','/online_consultation/table','','0','','_blank','0','0','0','1','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('89','注册用户','在线咨询','online_consultation','咨询详情','/online_consultation/view','','0','','_blank','0','0','0','1','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content','registered_user,user_name,advisory_title,consulting_category,consulting_content,reply_content',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('90','注册用户','在线咨询','online_consultation','在线咨询','/online_consultation/edit','','0','top','_blank','1','1','1','1','registered_user,user_name,advisory_title,consulting_category,consulting_content','registered_user,user_name,advisory_title,consulting_category,consulting_content','registered_user,user_name,advisory_title,consulting_category,consulting_content',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('91','注册用户','我的收藏','collect','我的收藏','/collect/list','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('92','注册用户','评论','comment','评论列表','/comment/table','','0','','_blank','0','1','0','1','','','',null,'0','{}','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `auth` values ('93','注册用户','评论','comment','评论详情','/comment/view','','0','','_blank','0','1','0','1','','','',null,'0','{}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('94','注册用户','评论','comment','我的评论','/comment/list','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('95','注册用户','评论','comment','评论详情','/comment/details','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('96','注册用户','病友交流','forum','病友交流','/forum/table','','0','','_blank','0','1','0','1','','','',null,'0','{"print":false,"export_db":false,"import_db":false}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('97','注册用户','病友交流','forum','交流详情','/forum/view','','0','','_blank','0','1','0','1','','','',null,'0','{"print":false}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('98','注册用户','病友交流','forum','病友交流','/forum/list','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('99','注册用户','病友交流','forum','交流详情','/forum/details','','0','','_blank','1','1','1','1','','','',null,'0','{"can_comment":true,"can_show_comment":true}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('100','注册用户','交流分类','forum_type','交流分类','/forum_type/table','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('101','注册用户','交流分类','forum_type','分类详情','/forum_type/view','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('102','注册用户','新闻资讯','article','新闻资讯','/article/table','','0','','_blank','0','0','0','0','','','',null,'0','{"print":false,"export_db":false,"import_db":false}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('103','注册用户','新闻资讯','article','资讯详情','/article/view','','0','','_blank','0','0','0','0','','','',null,'0','{"print":false}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('104','注册用户','新闻资讯','article','新闻资讯','/article/list','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('105','注册用户','新闻资讯','article','资讯详情','/article/details','','0','','_blank','1','1','1','1','','','',null,'0','{"can_comment":true,"can_show_comment":true}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('106','注册用户','资讯分类','article_type','资讯分类','/article_type/table','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('107','注册用户','资讯分类','article_type','分类详情','/article_type/view','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('108','注册用户','公告信息','notice','公告信息','/notice/table','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('109','注册用户','公告信息','notice','信息详情','/notice/view','','0','','_blank','0','0','0','0','','','',null,'0','{}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('110','注册用户','公告信息','notice','公告信息','/notice/list','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `auth` values ('111','注册用户','公告信息','notice','信息详情','/notice/details','','0','','_blank','1','1','1','1','','','',null,'0','{}','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `user_group` values ('1','100','管理员',null,'','','0','0','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `user_group` values ('2','100','游客',null,'','','0','0','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
insert into `user_group` values ('3','100','注册用户',null,'registered_user','registered_user_id','0','3','2025-04-04 21:23:16.0','2025-04-04 21:23:16.0');
ALTER TABLE `comment` ADD FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;
ALTER TABLE `collect` ADD FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;
ALTER TABLE `praise` ADD FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;
insert into `slides` values ('1','轮播图1','内容1','/article/details?article=1','/api/upload/1907997079649648640.jpg','90','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `slides` values ('2','轮播图2','内容2','/article/details?article=2','/api/upload/1907997071357509633.jpg','668','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `slides` values ('3','轮播图3','内容3','/article/details?article=3','/api/upload/1907997089489485824.jpg','958','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');
insert into `article` values ('1','中医教你几个小妙招缓解鼻塞、眼痒','皮肤病','212','0','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0',null,null,null,'<p>春天本该是踏青赏花的好时节，可对很多人来说，却意味着鼻塞、打喷嚏、眼睛痒、皮肤红疹……没错，过敏又来了！</p><p><br></p><p>近些年，过敏性鼻炎、过敏性结膜炎、过敏性皮炎等过敏性疾病发病率越来越高。春季气候多变，乍暖还寒，花粉浓度高，阳气亏虚、过敏体质的患者都比较容易中招。中医教你几个小妙招，来对付春季过敏。</p><p><br></p><p>中医角度看过敏？</p><p><br></p><p>过敏的主要症状是瘙痒、鼻塞、打喷嚏、流鼻涕眼泪等。中医认为，风胜则痒。从外因来说，是由于特殊的风邪（过敏原）侵袭人体，郁阻于鼻、眼、皮肤腠理等部位，内不得通、外不得泄，导致营卫不和、气血运行失常。从内因来说，正所谓“邪之所凑，其气必虚”，过敏的发生多数与肺、脾、肾三脏的功能失调有关。</p><p><br></p><p>肺主气、司呼吸，开窍于鼻，主皮毛。肺气虚弱，卫外不固，腠理疏松，邪气易于进犯鼻窍与皮肤。</p><p><br></p><p>脾主运化，为气血生化之源。脾气虚弱，运化失常，气血不足，水湿停聚，是清涕等不正常体液的来源。</p><p><br></p><p>肾主纳气，为先天之本，肾阳不足，温煦失职，则肺脾皆失温养，易受寒邪。</p><p><br></p><p>中医认为，肺、脾、肾是人体水液代谢的主要器官，肺为水之上源，主通调水道；脾主运化水湿；肾主水液，肾的气化功能对人体津液的输布和排泄起重要调节作用。从临床来看，过敏的患者大多数阳气不足，兼有湿气或者湿热，人体产生内湿主要也是与肺脾肾三脏有关。</p><p><br></p><p>中医治疗过敏，是采取“辨证+辨病+三因制宜”的综合原则，在针对局部症状的基础上，注重整体调节，疏风、清热、除湿等祛邪方法和益气、固本、扶正多种手段相结合，从根本上改善体质，增强机体抗病能力，消除不正常的过敏状态，从而达到标本兼治的目的。</p><p><br></p><p>中医防过敏妙招，简单好用！</p><p><br></p><p>按按这几个穴位，缓解鼻塞、眼痒</p><p><br></p><p>百会穴</p><p><br></p><p>位置：正坐位，前发际正中直上5寸。或于两耳尖连线的中点定穴。</p><p><br></p><p>主治：头痛、头晕、鼻塞、耳鸣等。</p><p><br></p><p>保健方法：端坐在椅子上，用手掌按摩头顶中央的百会穴，每次按顺时针方向和逆时针方向各按摩50圈，每日2-3次。以一手的中指或食指点按百会穴，先由轻渐重地按3-5下，然后再向左、向右各旋转揉动30-50次。可以疏通经络，提升督脉的阳气。</p><p><br></p><p>风池穴</p><p><br></p><p>位置：在颈部，当枕骨之下，与风府穴相平，胸锁乳突肌与斜方肌上端之间的凹陷处。简单取穴法：大拇指、中指放在头的枕部两侧，轻轻的往下滑动，两边的凹陷处即是，与耳垂齐平。</p><p><br></p><p>主治：头痛，头晕，感冒，鼻炎，鼻衄，视疲劳，目赤肿痛，迎风流泪，耳鸣，耳聋，颈项强痛，落枕等。</p><p><br></p><p>保健方法：两拇指或食中指持续向上方点按，或快速上下擦动。</p><p><br></p><p>迎香穴</p><p><br></p><p>位置：面部鼻翼外缘中点，鼻唇沟中。</p><p><br></p><p>主治：鼻塞、喷嚏、流鼻涕。</p><p><br></p><p>保健方法：双手大鱼际搓热后，擦摩鼻旁鼻唇沟，反复纵向擦摩，使局部发红发热即可。</p><p><br></p><p>鼻通穴</p><p><br></p><p>又名上迎香穴</p><p><br></p><p>位置：面部鼻翼软骨与鼻甲的交界处，近鼻唇沟上端处。</p><p><br></p><p>主治：同迎香穴。</p><p><br></p><p>保健方法：同迎香穴。</p><p><br></p><p>印堂穴</p><p><br></p><p>位置：前额部，当两眉头间连线与前正中线之交点处。</p><p><br></p><p>主治：头痛、头晕、鼻炎、眼部不适等。</p><p><br></p><p>保健方法：用拇指、食指或者中指的指腹点按，然后反复揉搓。</p><p><br></p><p>还可艾灸肺俞穴、脾俞穴、肾俞穴、足三里、三阴交、神阙、关元等穴位，可以温阳通络，固本培元，提高机体抵抗力。</p><p><br></p><p>外用小方子，改善过敏症状</p><p><br></p><p>给大家推荐两个外用的小方子，可以临时改善鼻塞流涕、眼痒等过敏症状。</p><p><br></p><p>鼻炎方：辛夷花5克、苍耳子3克、白芷5克、鹅不食草5克、细辛3克、薄荷5克、藿香5克、藁本5克。</p><p><br></p><p>结膜炎方：桑叶5克、菊花5克、薄荷5克、木贼5克。</p><p><br></p><p>方法一：将这些药碎为细末，做成香包，发作时嗅闻。</p><p><br></p><p>方法二，将这些药打碎，开水浸泡10-20分钟，吸闻蒸腾热气，或熏蒸眼部。</p><p><br></p><p>日常防过敏小贴士</p><p><br></p><p>注意防护，不去密集场所，戴口罩，室内定期通风。</p><p><br></p><p>注意手、眼、鼻清洁，可用生理盐水冲洗鼻腔和眼部。</p><p><br></p><p>饮食宜清淡，避免生冷、辛辣、油腻、过咸过甜食物。</p><p><br></p><p>作息规律，合理睡眠，建议尽量在23点前入睡。</p><p><br></p><p>保持心态稳定。生气发怒郁闷等不良情绪对身体有较大影响。</p><p><br></p><p>适当运动。推荐八段锦、太极拳、五禽戏、易筋经等传统功法，强筋健骨、疏通经络、调畅气血，有助于提升脏腑功能。</p><p><br></p><p>最后，过敏性疾病具有反复发作、缠绵难愈的特点，症状严重者建议尽快到专业医疗机构治疗。</p>','/api/upload/1907998669248921601.jpg',null);
insert into `article` values ('2','武汉皮肤科专家团队走进社区,分享炎症性皮肤病科普知识','皮肤病','970','0','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0',null,null,null,'<p>痒到晚上睡觉都睡不好、钻心地痒……这可能是很多炎症性皮肤病患者的心声。</p><p><br></p><p>研究显示，炎症性皮肤病患者每天承受10多个小时的剧烈瘙痒，给患者个人和家庭都带来难以忍受的折磨。9月18日，由中国人体健康科技促进会牵头发起的炎症性皮肤病医学科普公益行项目，在武汉市古田街社区卫生服务中心开展，来自武汉市第一医院的多位专家通过现场授课、答疑的方式为社区居民科普了炎症性皮肤病及特应性皮炎的疾病知识，旨在提高公众对炎症性皮肤病的认识，尽可能做到早期发现、干预、治疗。</p><p><br></p><p>活动中，武汉市第一医院皮肤科主任陈柳青教授介绍了炎症性皮肤病的基础知识，胡枫教授、陈志平教授以炎症性皮肤病的代表“特应性皮炎”为例，分别讲解了如何应对疾病的治疗和瘙痒症状等内容。</p><p><br></p><p>皮肤作为人体最大的器官，皮肤类疾病也是最常见的疾病之一。相关数据显示，在我国皮肤患病率高达40%-70%，炎症性皮肤病尤为常见。多种因素导致患者对医疗的诉求也不尽相同。以特应性皮炎为例，数据显示全国特应性皮炎患者约7000万人，患者群体庞大。疾病的主要症状是瘙痒，导致很多患者尤其是孩子晚间无法入眠，失眠对患者的身体和心理健康产生负面影响，甚至可能导致日间学习和工作困难，形成恶性循环。此外，患者皮损轻重不一且位置不同，暴露部位的红斑、红疹和糜烂等，可能为患者在社会交往的活动中带来困扰，造成较大的社交和心理压力。近些年来，随着生物制剂和小分子药物等创新靶向药物的问世，阿布昔替尼等药物近期获批青少年人群和高剂量起始治疗，特应性皮炎治疗进入新时代。</p><p><br></p><p>活动现场，针对患者关注的问题，与会专家为大家现场解疑释惑，围绕提升对于炎症性皮肤病和瘙痒的治疗预期和信心，如何及时就诊，如何与医生顺畅交流，为大家提供了丰富的参考借鉴。</p><p><br></p><p>据悉，炎症性皮肤病医学科普公益行项目将在全国不同地区开展50场活动，这将进一步加大炎症性皮肤病的健康知识的普及和传播力度，以高度社会责任感发挥专业医疗力量，为公众提供科学权威的健康科普知识，不断提高社会公众健康素养水平，让患者更好地与医生沟通，增强实现“无痒愈肤，无恙新生”的信心。</p>','/api/upload/1907998341124325377.jpg',null);
insert into `article` values ('3','炎症性皮肤病医学科普公益行项目在沪启动','皮肤病','968','0','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0',null,null,null,'<p>痒到晚上睡觉都睡不好”“钻心的痒”。这可能是很多炎症性皮肤病患者的心声。研究显示，炎症性皮肤病患者每天承受 10多个小时的剧烈瘙痒，给患者个人和家庭都带来难以忍受的折磨。</p><p>3月20日，由中国人体健康科技促进会牵头发起的炎症性皮肤病医学科普公益行项目在上海开展。来自复旦大学附属华山医院的多位皮肤科专家通过现场授课、答疑的方式为患者科普了炎症性皮肤病及特应性皮炎的疾病知识，旨在提高公众对炎症性皮肤病的认识，尽可能做到早期发现、干预、治疗，帮助患者无痒愈肤、焕发新生。</p><p>皮肤作为人体最大的器官，皮肤类疾病也是最常见的疾病之一。相关数据显示，在我国皮肤患病率高达40%-70%，炎症性皮肤病尤为常见。多种因素导致患者对医疗的诉求也不尽相同。</p><p>以特应性皮炎为例，数据显示全国特应性皮炎患者约7千万人，患者群体庞大。疾病的主要症状是瘙痒，以至于很多患者尤其是小孩，晚间无法入睡，失眠对患者的身体和心理健康产生负面影响，甚至陷入恶性循环，进而导致日间学习和工作困难。</p><p>此外，患者皮损轻重不一且位置不同，暴露部位的红斑、红疹和糜烂等，可能为患者在社会交往的活动中带来困扰，造成较大的社交和心理压力，近些年来，随着生物制剂和小分子药物等创新靶向药物的问世，特应性皮炎治疗正进入新时代。</p><p>近期皮肤科学术研究发展迅速。作为行业规模最大、影响最大、最具代表性的皮肤科学会承办的年会，第83届美国皮肤科学会年会于3月7-11日在美国佛罗里达州奥兰多召开。在会上，阿布昔替尼全球最大规模JAK1抑制剂真实世界研究AHEAD（Abrocitinib CHinese rEgistry on AD）阶段性研究成果国际首发。AHEAD研究为高选择性JAK1抑制剂临床应用提供更多真实世界医学循证数据，进一步夯实临床科学依据、优化治疗策略，让真实世界证据真正赋能临床实践。</p>','/api/upload/1907997379072622592.jpg',null);
insert into `article` values ('4','重庆华肤皮肤病医院科普知识大放送','皮肤病','581','0','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0',null,null,null,'<p>在繁忙的生活节奏中，我们常常忽略了对肌肤的呵护，而肌肤作为我们身体的第一道防线，其健康状态直接关系到我们的整体健康和外在形象。为了帮助大家更好地了解肌肤健康的重要性，掌握常见皮肤问题的预防方法，重庆华肤皮肤病医院特别推出本期科普知识大放送。</p><p><br></p><p>【皮肤科实时放送】守护肌肤健康，重庆华肤皮肤病医院科普知识大放送</p><p><br></p><p>一、肌肤健康的重要性</p><p><br></p><p>肌肤不仅是我们的外在形象展示，更是身体健康的晴雨表。健康的肌肤能够有效阻挡外界有害物质，保持身体内部环境的稳定。同时，肌肤的代谢和免疫功能也是维持身体健康的重要因素。因此，守护肌肤健康，就是守护我们的整体健康。</p><p><br></p><p>二、常见皮肤问题及预防方法</p><p><br></p><p>痤疮</p><p><br></p><p>痤疮是常见的皮肤问题之一，主要表现为面部、背部等部位的粉刺、丘疹、脓疱等。预防痤疮的关键在于保持皮肤清洁，避免油脂堆积。此外，饮食调理也比较重要，应避免过多摄入高糖、高脂、辛辣等刺激性食物。</p><p><br></p><p>湿疹</p><p><br></p><p>湿疹是一种常见的过敏性皮肤病，表现为皮肤红肿、瘙痒、起疱等症状。预防湿疹，除了避免过敏原外，还应保持皮肤湿润，避免过度清洁和使用刺激性护肤品。</p><p><br></p><p>色斑</p><p><br></p><p>色斑是由于皮肤黑色素增多而形成的，与紫外线照射、内分泌失调等因素有关。预防色斑，关键在于防晒和调节内分泌。外出时应涂抹防晒霜，避免紫外线直接照射；同时，保持良好的作息习惯和饮食健康，有助于调节内分泌。</p><p><br></p><p>三、重庆华肤皮肤病医院的专业服务</p><p><br></p><p>作为一家专业的皮肤病医院，重庆华肤皮肤病医院拥有一支经验丰富、技术专业的医疗团队。我们致力于为患者提供全面、专业、个性化的皮肤病诊疗服务。</p><p><br></p><p>针对不同类型的皮肤问题，我们采用中西医结合的治疗方法，结合现代医疗技术和传统中医理论，为患者量身定制治疗方案。同时，我们还注重患者的心理疏导和健康教育，帮助患者树立战胜疾病的信心，提高自我保健能力。</p><p><br></p><p>此外，重庆华肤皮肤病医院还定期开展科普讲座和义诊活动，旨在提高公众对皮肤病的认识和重视程度，传播健康的生活方式和护肤理念。</p><p><br></p><p>重庆华肤皮肤病医院温馨提示：守护肌肤健康是我们每个人的责任。通过了解常见皮肤问题的预防方法和寻求专业的医疗服务，我们可以更好地保护自己的肌肤健康。重庆华肤皮肤病医院愿与您携手共创健康美丽的肌肤未来！</p>','/api/upload/1908018606206091265.jpg',null);
insert into `article` values ('5','皮肤过敏季来临，专家提示关注皮肤科“一号疾病”防治','皮肤病','373','0','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0',null,null,null,'<p>近期，皮肤科就诊的患者显著增多。中华医学会皮肤性病学分会副主任委员、中日友好医院皮肤科主任崔勇提醒公众，应重视皮肤日常防护及疾病防治，全方位守护皮肤健康。</p><p><br></p><p>皮肤是人体最大的器官，也是健康的第一道防线。然而，随着生活节奏的加快和环境因素的变化，我国皮肤病的发病率持续上升。皮肤病的症状复杂多样，不同类型的皮肤病表现各异，但都可能会影响患者的工作或学习，造成生活质量下降。</p><p><br></p><p>被视为皮肤科“一号疾病”的特应性皮炎患者人数近20年有持续上升趋势，成为不容忽视的公共卫生挑战。《中国特应性皮炎诊疗指南》提到，特应性皮炎的发病与遗传和环境等因素关系密切，春季气温变化、过敏原、光照等外部环境因素刺激，都可能导致特应性皮炎反复或加重。</p><p><br></p><p>崔勇表示，皮肤的健康状态直接影响整体健康。以特应性皮炎为例，患者可能长期易遭受剧烈瘙痒、严重皮损、睡眠中断等疾病困扰，也容易合并过敏性鼻炎、哮喘等共病，严重影响生活质量。需要注意的是，特应性皮炎是一种慢性疾病，需要患者长期进行管理。因此，特应性皮炎患者应遵循慢病防治理念，接受规范治疗、长期管理以及健康指导，以有效控制病情、提升生活质量。</p><p><br></p><p>中华医学会皮肤性病学分会发起的《皮肤健康三年行动（2024-2026）》去年6月启动，计划通过一系列活动普及健康知识，传递皮肤健康新观念。其中，“365问”系列科普图书中的《特应性皮炎365问》已于去年9月启动，今年2月成立了百人编委会，该书计划于6月出版。“我们的目标是通过‘每日一问，终身陪伴’的方式，为读者提供全面、权威、实用的科普知识，帮助患者科学管理病情。”崔勇说。</p><p><br></p>','/api/upload/1907997795860611073.jpg',null);
insert into `article` values ('6','熊儿寨乡：皮肤科专家送“抗敏锦囊”','皮肤病','591','0','2025-04-04 21:23:17.0','2025-04-04 21:23:17.0',null,null,null,'<p>春天来了，随之而来的花粉、尘螨等过敏原也让不少人的皮肤“遭了殃”。近日，熊儿寨乡社区卫生服务中心组织的一场“春季过敏原导致皮肤病的防治”健康知识讲座，吸引了众多村民。区中医医院皮肤科主治医师王泽明为大家支招，让村民收获了“抗敏锦囊”。</p><p><br></p><p>讲座现场，王医生从春季常见过敏原、过敏性皮肤病的临床表现、预防措施及治疗方法等方面进行了详细讲解。并指出，春季常见的过敏原包括花粉、尘螨、霉菌等，这些物质容易引发荨麻疹、湿疹、过敏性皮炎等皮肤问题。</p><p><br></p><p>王医生特别强调：预防胜于治疗，过敏体质人群在春季应尽量减少户外活动，外出时佩戴口罩，回家后及时清洗面部和双手。同时，保持室内清洁，定期更换床单被罩，避免使用刺激性强的洗涤剂。</p><p><br></p><p>“以前过敏就知道抹药膏，今天才明白还有这么多注意事项。”村民张大妈说。窗外春光正好，这场接地气的健康课让村民们掌握了与春天“和平共处”的秘诀。</p><p><br></p><p>熊儿寨乡社区卫生服务中心主任王雅红表示：这种大医院专家+社区服务的医联体模式，让健康科普不再走过场。我们将定期开展各类健康教育活动，把防病于未然的理念传递给更多居民。</p>','/api/upload/1907998008759287809.jpg',null);
insert into `article_type` values ('1','100','皮肤病','0',null,null,null,'2025-04-04 21:23:17.0','2025-04-04 21:23:17.0');

UPDATE forum 
SET img = CONCAT('/api',img);
