/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.1.61 : Database - jess
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`jess` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `jess`;

/*Table structure for table `demo_tables` */

DROP TABLE IF EXISTS `demo_tables`;

CREATE TABLE `demo_tables` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_area_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `demo_tables` */

insert  into `demo_tables`(`id`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'1'),('2','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('3','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('4','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('5','1','2013-05-27 08:00:00','1','2014-09-05 15:07:08','3243242323','0'),('6','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'1'),('7','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'1'),('8','1','2013-05-27 08:00:00','1','2014-09-04 16:17:50','222','1'),('9','1','2013-05-27 08:00:00','1','2014-09-04 16:17:45','11','1'),('10','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('11','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('12','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('13','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('14','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0');

/*Table structure for table `gen_scene` */

DROP TABLE IF EXISTS `gen_scene`;

CREATE TABLE `gen_scene` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `scene_type` varchar(8) DEFAULT NULL COMMENT '场景类型',
  `name` varchar(200) DEFAULT NULL COMMENT '主表名称',
  `comments` varchar(500) DEFAULT NULL COMMENT '描述',
  `class_name` varchar(100) DEFAULT NULL COMMENT '主表实体类名称',
  `subname` varchar(200) DEFAULT NULL COMMENT '从表名称',
  `subclass_name` varchar(100) DEFAULT NULL COMMENT '从表实体类名称',
  `page_size` int(4) DEFAULT NULL COMMENT '主表列表每页记录数',
  `subpage_size` int(4) DEFAULT NULL COMMENT '从表列表每页记录数',
  `form_column` int(2) DEFAULT NULL COMMENT '主表form每行记录数',
  `subform_column` int(2) DEFAULT NULL COMMENT '从表form每行记录数',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务表';

/*Data for the table `gen_scene` */

insert  into `gen_scene`(`id`,`scene_type`,`name`,`comments`,`class_name`,`subname`,`subclass_name`,`page_size`,`subpage_size`,`form_column`,`subform_column`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('6ba8e97204ec417cbcaf5bb052d2dfad','3','gen_scene','可编辑列表','EditTest',NULL,NULL,10,NULL,NULL,NULL,NULL,'2015-04-08 13:50:15','1','2015-04-08 16:31:12','22','0'),('76b42c10481d4a309b91b951741a4271','1','gen_scene','单表','MyTest',NULL,NULL,10,NULL,NULL,NULL,NULL,'2015-03-27 12:20:10','1','2015-04-08 16:31:12','11','0'),('b9e52f8094d64a49827f31d81bf846bb','2','gen_scene','主从表','FatherTest','gen_scene_column','ChildTest',10,10,NULL,NULL,NULL,'2015-03-27 00:00:00','1','2015-04-08 16:31:12','11','0');

/*Table structure for table `gen_scene_column` */

DROP TABLE IF EXISTS `gen_scene_column`;

CREATE TABLE `gen_scene_column` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `gen_scene_id` varchar(64) DEFAULT NULL COMMENT '归属场景主键',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `comments` varchar(500) DEFAULT NULL COMMENT '描述',
  `jdbc_type` varchar(100) DEFAULT NULL COMMENT '列的数据类型的字节长度',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键',
  `is_null` char(1) DEFAULT NULL COMMENT '是否可为空',
  `is_entity` char(1) DEFAULT NULL COMMENT '是否entity字段',
  `is_form` char(1) DEFAULT NULL COMMENT '是否表单字段',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否可编辑',
  `is_relate` char(1) DEFAULT NULL COMMENT '是否级联操作',
  `relate_entity` varchar(50) DEFAULT NULL COMMENT '级联实体',
  `relate_op` varchar(8) DEFAULT NULL COMMENT '级联操作',
  `query_type` varchar(200) DEFAULT NULL COMMENT '查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）',
  `show_type` varchar(200) DEFAULT NULL COMMENT '字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）',
  `dict_type` varchar(200) DEFAULT NULL COMMENT '字典类型',
  `settings` varchar(2000) DEFAULT NULL COMMENT '其它设置（扩展字段JSON）',
  `sort` decimal(10,0) DEFAULT NULL COMMENT '排序（升序）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  `is_subtable` char(1) DEFAULT NULL COMMENT '是否从表',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务表字段';

/*Data for the table `gen_scene_column` */

insert  into `gen_scene_column`(`id`,`gen_scene_id`,`name`,`comments`,`jdbc_type`,`java_type`,`java_field`,`is_pk`,`is_null`,`is_entity`,`is_form`,`is_list`,`is_query`,`is_edit`,`is_relate`,`relate_entity`,`relate_op`,`query_type`,`show_type`,`dict_type`,`settings`,`sort`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`,`is_subtable`) values ('06a934a4934242f8abad86aa8cc962b6','b9e52f8094d64a49827f31d81bf846bb','dict_type','字典类型','varchar(200)','String','dictType',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('088db9b2b48645e7b76fed3d5eefb6d5','b9e52f8094d64a49827f31d81bf846bb','java_type','JAVA类型','varchar(500)','String','javaType',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('1074dcbbc8e548bb9886534c95fe5b77','76b42c10481d4a309b91b951741a4271','remarks','备注信息','varchar(255)','String','remarks',NULL,'1','1','1','1','1','0','0','','0','1','1','',NULL,NULL,'1','2015-03-27 12:20:10','1','2015-04-08 08:38:50',NULL,'0','0'),('12c4c1997c0f49b3a759d760773acb0f','6ba8e97204ec417cbcaf5bb052d2dfad','remarks','备注信息','varchar(255)','String','remarks',NULL,'1','1','0','1','0','1','0','','0','0','1',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('1502967c5ea0451b87477d12d90d49f1','76b42c10481d4a309b91b951741a4271','id','主键','varchar(64)','String','id',NULL,'0','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:20:10','1','2015-04-08 08:38:50',NULL,'0','0'),('1bb554dc6f544c3d905a7e158ff89d9f','b9e52f8094d64a49827f31d81bf846bb','remarks','备注信息','varchar(255)','String','remarks',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('1f8abcb32dbc4b75892b3d0960982d59','b9e52f8094d64a49827f31d81bf846bb','is_form','是否表单字段','char(1)','String','isForm',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('219e01c3e45644739bb845b7ce61ac99','76b42c10481d4a309b91b951741a4271','create_by','创建者','varchar(64)','String','createBy',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:20:10','1','2015-04-08 08:38:50',NULL,'0','0'),('23f9b2ddaeab4e25aacedaf6da4cd5a3','b9e52f8094d64a49827f31d81bf846bb','name','主表名称','varchar(200)','String','name',NULL,'1','1','1','1','1','0','0','','0','1','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','0'),('27b01891443e4ec4a0794b6da4a42d20','b9e52f8094d64a49827f31d81bf846bb','create_by','创建者','varchar(64)','String','createBy',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('29d9b7e1592e4eacad46b81950e1878f','b9e52f8094d64a49827f31d81bf846bb','name','名称','varchar(200)','String','name',NULL,'1','1','1','1','1','0','0','','0','1','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('2e9cebe29ba447f18c6ff09b7a136807','b9e52f8094d64a49827f31d81bf846bb','update_date','更新时间','datetime','Date','updateDate',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('2f214063fec04cf680d5a1cdec53cb3d','6ba8e97204ec417cbcaf5bb052d2dfad','subpage_size','从表列表每页记录数','int(4)','Integer','subpageSize',NULL,'1','1','0','0','0','0','0','','0','','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('31121fb103a0459f8dd8653a76c52cd0','b9e52f8094d64a49827f31d81bf846bb','is_null','是否可为空','char(1)','String','isNull',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('3609ecc85dfb4650b13a74693643c58e','76b42c10481d4a309b91b951741a4271','subname','从表名称','varchar(200)','String','subname',NULL,'1','1','1','1','1','0','0','','0','1','0','',NULL,NULL,'1','2015-03-27 12:20:10','1','2015-04-08 08:38:50',NULL,'0','0'),('36a70237008a44739dfd5634cb452b56','b9e52f8094d64a49827f31d81bf846bb','sort','排序（升序）','decimal(10,0)','Long','sort',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('3831803f20344d619adb552c18433e1b','b9e52f8094d64a49827f31d81bf846bb','settings','其它设置（扩展字段JSON）','varchar(2000)','String','settings',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('39dcbfbbd26a4b078b8b50b8bb280252','76b42c10481d4a309b91b951741a4271','del_flag','删除标记（0：正常；1：删除）','char(1)','String','delFlag',NULL,'0','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:20:10','1','2015-04-08 08:38:50',NULL,'0','0'),('48d31c02724b4f63bfb2864b2268ea9c','b9e52f8094d64a49827f31d81bf846bb','java_field','JAVA字段名','varchar(200)','String','javaField',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('490842bd556f40e7a8b9c29625f46445','6ba8e97204ec417cbcaf5bb052d2dfad','del_flag','删除标记（0：正常；1：删除）','char(1)','String','delFlag',NULL,'0','1','0','0','0','0','0','','0','','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('49671e71a3b54ae0b49b3a7ef45985e2','b9e52f8094d64a49827f31d81bf846bb','scene_type','场景类型','varchar(8)','String','sceneType',NULL,'1','1','1','1','1','0','0','','0','1','2','sys_gen_scene_type',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','0'),('52512f6ece39482490fe740d111b297d','76b42c10481d4a309b91b951741a4271','comments','描述','varchar(500)','String','comments',NULL,'1','1','1','1','0','0','0','','0','','1','',NULL,NULL,'1','2015-03-27 12:20:10','1','2015-04-08 08:38:50',NULL,'0','0'),('5326e56874b94aeca085907e4cb4292f','b9e52f8094d64a49827f31d81bf846bb','create_by','创建者','varchar(64)','String','createBy',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','0'),('558c019702a04077ab0269a97e5ee25c','6ba8e97204ec417cbcaf5bb052d2dfad','page_size','主表列表每页记录数','int(4)','Integer','pageSize',NULL,'1','1','0','0','0','0','0','','0','','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('57803b7fb25744e9813afd199d409915','b9e52f8094d64a49827f31d81bf846bb','create_date','创建时间','datetime','Date','createDate',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('5da71c5de16846b28a94a5ca66069829','b9e52f8094d64a49827f31d81bf846bb','is_edit','是否可编辑','char(1)','String','isEdit',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('63d8b1d32fda4fa9ae18883b3a21596e','6ba8e97204ec417cbcaf5bb052d2dfad','name','主表名称','varchar(200)','String','name',NULL,'1','1','0','1','1','1','0','','0','1','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('652905e508b34ab2944efde3530b6318','b9e52f8094d64a49827f31d81bf846bb','is_query','是否查询字段','char(1)','String','isQuery',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('6811bc4aaf9643e58a57d43238984241','6ba8e97204ec417cbcaf5bb052d2dfad','update_date','更新时间','datetime','Date','updateDate',NULL,'1','1','0','0','0','0','0','','0','','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('691101ea5e88482cbe29cacc5a49c84a','b9e52f8094d64a49827f31d81bf846bb','del_flag','删除标记（0：正常；1：删除）','char(1)','String','delFlag',NULL,'0','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','0'),('6a16fa9321964ba9b815073d4258829e','b9e52f8094d64a49827f31d81bf846bb','update_by','更新者','varchar(64)','String','updateBy',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('77929bfc3163426ca5f457a1e7ff2c40','b9e52f8094d64a49827f31d81bf846bb','comments','描述','varchar(500)','String','comments',NULL,'1','1','1','1','0','0','0','','0','','1','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','0'),('78f47b37fda046f8b727ae66f2847379','b9e52f8094d64a49827f31d81bf846bb','is_pk','是否主键','char(1)','String','isPk',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('7b304bd490864ec3b1b31ccff0c90edc','76b42c10481d4a309b91b951741a4271','scene_type','场景类型','varchar(8)','String','sceneType',NULL,'1','1','1','1','1','0','0','','0','1','2','sys_gen_scene_type',NULL,NULL,'1','2015-03-27 12:20:10','1','2015-04-08 08:38:50',NULL,'0','0'),('7ef6aeb6d2694573ab5ee81782cc750b','6ba8e97204ec417cbcaf5bb052d2dfad','create_by','创建者','varchar(64)','String','createBy',NULL,'1','1','0','0','0','0','0','','0','','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('8436c8ac356e4c1fa2093cf8c767dfd6','b9e52f8094d64a49827f31d81bf846bb','relate_op','级联操作','varchar(8)','String','relateOp',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('86fb9d26f82d4845aac20c07693054a7','b9e52f8094d64a49827f31d81bf846bb','id','主键','varchar(64)','String','id',NULL,'0','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('898e505d044f429b860c27198261c86a','76b42c10481d4a309b91b951741a4271','class_name','主表实体类名称','varchar(100)','String','className',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:20:10','1','2015-04-08 08:38:50',NULL,'0','0'),('89be26101ca541519b94be63b62d3fff','b9e52f8094d64a49827f31d81bf846bb','relate_entity','级联实体','varchar(50)','String','relateEntity',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('8fa17c325a5a43d89ae616f0f0771230','b9e52f8094d64a49827f31d81bf846bb','comments','描述','varchar(500)','String','comments',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('90880e315fcb4c61a4c86caf7654d053','76b42c10481d4a309b91b951741a4271','name','主表名称','varchar(200)','String','name',NULL,'1','1','1','1','1','0','0','','0','1','0','',NULL,NULL,'1','2015-03-27 12:20:10','1','2015-04-08 08:38:50',NULL,'0','0'),('92927441f57a4c738a3410125633fc27','6ba8e97204ec417cbcaf5bb052d2dfad','update_by','更新者','varchar(64)','String','updateBy',NULL,'1','1','0','0','0','0','0','','0','','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('94334973f6854c2b981a064d37c52da8','6ba8e97204ec417cbcaf5bb052d2dfad','create_date','创建时间','datetime','Date','createDate',NULL,'1','1','0','1','1','1','0','','0','','3',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('95507d0176d64f58b6d4000e15ec9158','6ba8e97204ec417cbcaf5bb052d2dfad','subclass_name','从表实体类名称','varchar(100)','String','subclassName',NULL,'1','1','0','0','0','0','0','','0','','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('9e5c85c89501446bb85600097f08317e','b9e52f8094d64a49827f31d81bf846bb','create_date','创建时间','datetime','Date','createDate',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','0'),('9f99ac034ae1412e965a1a86f2b98add','b9e52f8094d64a49827f31d81bf846bb','query_type','查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）','varchar(200)','String','queryType',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('a4e5f436138a49178e5c6c8daa5cf709','6ba8e97204ec417cbcaf5bb052d2dfad','form_column','主表form每行记录数','int(2)','Integer','formColumn',NULL,'1','1','0','0','0','0','0','','0','','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('a6aa85fb0b004273993a3d5c3c583061','b9e52f8094d64a49827f31d81bf846bb','del_flag','删除标记（0：正常；1：删除）','char(1)','String','delFlag',NULL,'0','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('af82231a61e94b458d882f76d30882b0','76b42c10481d4a309b91b951741a4271','subclass_name','从表实体类名称','varchar(100)','String','subclassName',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:20:10','1','2015-04-08 08:38:50',NULL,'0','0'),('b826dd50ed4a447cba219ef1bd00ebbd','b9e52f8094d64a49827f31d81bf846bb','jdbc_type','列的数据类型的字节长度','varchar(100)','String','jdbcType',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('b9c1666f4a0b48d08a62e0739e0bc737','b9e52f8094d64a49827f31d81bf846bb','class_name','主表实体类名称','varchar(100)','String','className',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','0'),('bf5a0cf279ee4cb0a1ac8e596a449333','b9e52f8094d64a49827f31d81bf846bb','update_date','更新时间','datetime','Date','updateDate',NULL,'1','1','1','1','1','0','0','','0','0','3','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','0'),('bfff5b53796c498682dbaa6c0efcd21a','76b42c10481d4a309b91b951741a4271','update_by','更新者','varchar(64)','String','updateBy',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:20:10','1','2015-04-08 08:38:50',NULL,'0','0'),('cae356b8f87c4ded9c90009ea6f921d2','b9e52f8094d64a49827f31d81bf846bb','is_subtable','是否从表','char(1)','String','isSubtable',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('cc59de83500e4e45a2e5845b1169f527','76b42c10481d4a309b91b951741a4271','create_date','创建时间','datetime','Date','createDate',NULL,'1','1','1','1','0','0','0','','0','','3','',NULL,NULL,'1','2015-03-27 12:20:10','1','2015-04-08 08:38:50',NULL,'0','0'),('d0cc2ff78b2047ae8c9c280864dba705','b9e52f8094d64a49827f31d81bf846bb','subname','从表名称','varchar(200)','String','subname',NULL,'1','1','1','1','1','0','0','','0','1','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','0'),('db70cbb043574108880b71279458aedf','6ba8e97204ec417cbcaf5bb052d2dfad','scene_type','场景类型','varchar(8)','String','sceneType',NULL,'1','1','0','1','1','1','0','','0','0','2','sys_gen_scene_type',NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('dcb10e4194e54b64b69d70341c934177','b9e52f8094d64a49827f31d81bf846bb','gen_scene_id','归属场景主键','varchar(64)','String','fatherTest.name',NULL,'1','1','1','1','0','0','2','FatherTest','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('dfca89ea7df04e3dab7d1095d4731fab','6ba8e97204ec417cbcaf5bb052d2dfad','class_name','主表实体类名称','varchar(100)','String','className',NULL,'1','1','0','0','0','0','0','','0','','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('dfd2cb8edda14fb4a75de91daadfdc2c','6ba8e97204ec417cbcaf5bb052d2dfad','subform_column','从表form每行记录数','int(2)','Integer','subformColumn',NULL,'1','1','0','0','0','0','0','','0','','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('e1c236e5e7f545bcaca6218f0e605d76','b9e52f8094d64a49827f31d81bf846bb','subclass_name','从表实体类名称','varchar(100)','String','subclassName',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','0'),('e32ca433d8024f85a2bb66587d0cc5ed','6ba8e97204ec417cbcaf5bb052d2dfad','subname','从表名称','varchar(200)','String','subname',NULL,'1','1','0','0','0','0','0','','0','','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('e6f386382dfe40f5b3c39bc0216ec2f1','b9e52f8094d64a49827f31d81bf846bb','id','主键','varchar(64)','String','id',NULL,'0','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','0'),('e78d1b56d1f847279ab250d9553ca545','6ba8e97204ec417cbcaf5bb052d2dfad','comments','描述','varchar(500)','String','comments',NULL,'1','1','0','0','0','0','0','','0','','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('ebc9e0cc7b8744a8a824c9672a2e7c2c','b9e52f8094d64a49827f31d81bf846bb','update_by','更新者','varchar(64)','String','updateBy',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','0'),('f0c6ebea16aa4a18915c3cfee4fbb258','b9e52f8094d64a49827f31d81bf846bb','is_list','是否列表字段','char(1)','String','isList',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('f27030df8ac441aab970a8b1260cea3e','b9e52f8094d64a49827f31d81bf846bb','is_entity','是否entity字段','char(1)','String','isEntity',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('f3748e384c854c0cb32940c4de92dde1','76b42c10481d4a309b91b951741a4271','update_date','更新时间','datetime','Date','updateDate',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:20:10','1','2015-04-08 08:38:50',NULL,'0','0'),('f40daa5d8d734afb9e577929d1b94fbd','b9e52f8094d64a49827f31d81bf846bb','remarks','备注信息','varchar(255)','String','remarks',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','0'),('f5bfd436fe3e452d83488b56bf8114d9','6ba8e97204ec417cbcaf5bb052d2dfad','id','主键','varchar(64)','String','id',NULL,'0','1','0','0','0','0','0','','0','','0',NULL,NULL,NULL,'1','2015-04-08 13:50:15','1','2015-04-08 16:20:12',NULL,'0','0'),('f889a2d017d147768bbdd39adbbf51bb','b9e52f8094d64a49827f31d81bf846bb','is_relate','是否级联操作','char(1)','String','isRelate',NULL,'1','1','0','0','0','0','0','','0','','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1'),('fe62c1e01af04bffb77c858aab09fbad','b9e52f8094d64a49827f31d81bf846bb','show_type','字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）','varchar(200)','String','showType',NULL,'1','1','1','1','1','0','0','','0','1','0','',NULL,NULL,'1','2015-03-27 12:46:57','1','2015-03-31 13:57:47',NULL,'0','1');

/*Table structure for table `gen_scene_relate_column` */

DROP TABLE IF EXISTS `gen_scene_relate_column`;

CREATE TABLE `gen_scene_relate_column` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `gen_scene_id` varchar(64) DEFAULT NULL COMMENT '归属场景主键',
  `relate_type` varchar(8) DEFAULT NULL COMMENT '级联关系',
  `relate_entity` varchar(50) DEFAULT NULL COMMENT '级联实体',
  `relate_op` varchar(8) DEFAULT NULL COMMENT '级联操作',
  `java_field` varchar(50) DEFAULT NULL COMMENT 'java字段名称',
  `mappedby` varchar(50) DEFAULT NULL COMMENT 'mappedby',
  `joincolumn` varchar(50) DEFAULT NULL COMMENT 'joincolumn',
  `jointable` varchar(50) DEFAULT NULL COMMENT 'jointable',
  `inverse_joincolumn` varchar(50) DEFAULT NULL COMMENT 'inversejoincolumn',
  `settings` varchar(2000) DEFAULT NULL COMMENT '其它设置（扩展字段JSON）',
  `sort` int(8) DEFAULT NULL COMMENT '排序（升序）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  `is_subtable` char(1) DEFAULT NULL COMMENT '是否从表',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务表字段';

/*Data for the table `gen_scene_relate_column` */

insert  into `gen_scene_relate_column`(`id`,`gen_scene_id`,`relate_type`,`relate_entity`,`relate_op`,`java_field`,`mappedby`,`joincolumn`,`jointable`,`inverse_joincolumn`,`settings`,`sort`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`,`is_subtable`) values ('001fd1b880d64b01be26c8c84494b1c1','com.lnint.jess.generator.entity.GenScene@35941226','1','3','0',NULL,'3','3','3','3',NULL,NULL,'1','2015-03-31 16:26:37','1','2015-03-31 16:26:37',NULL,'0','0'),('19ca5821cad7400cb45b1bb5f1eee9a2','com.lnint.jess.generator.entity.GenScene@2bf982','3','ChildTest','0',NULL,'fatherTest','','','',NULL,NULL,'1','2015-03-27 12:49:06','1','2015-03-27 12:49:06',NULL,'0','0'),('1acc3b7e61544c40b86d6e5e2ee9e748','b9e52f8094d64a49827f31d81bf846bb','3','ChildTest','0',NULL,'fatherTest','','','',NULL,NULL,'1','2015-03-31 13:57:47','1','2015-03-31 13:57:47',NULL,'0','0'),('21fcf48c6fca49cb8bab90bb5e3d770e','2dc77377750949559ff136be39cacaf4','1','1',NULL,NULL,'1',NULL,'1',NULL,NULL,NULL,'1','2015-04-03 15:27:07','1','2015-04-03 15:27:07',NULL,'0','0'),('35f94e5079c44718a39b214704ea4544','395c83a763ee4ca0a66991a3caab004b','1','1',NULL,NULL,'1',NULL,'1',NULL,NULL,NULL,'1','2015-04-03 15:24:15','1','2015-04-03 15:24:15',NULL,'0','0'),('39b74eae154b4725b592708b6b005c5e','com.lnint.jess.generator.entity.GenScene@11a7d31c','1','4','0',NULL,'4','4','4','4',NULL,NULL,'1','2015-04-01 15:02:06','1','2015-04-01 15:02:06',NULL,'0','0'),('3a81f3bd27fc4192bc55f2759e329193','com.lnint.jess.generator.entity.GenScene@7de34e81','1','3','0',NULL,'3','3','3','3',NULL,NULL,'1','2015-04-01 14:51:33','1','2015-04-01 14:51:33',NULL,'0','0'),('3b16ce93a18b4569a5c043255276d0be','com.lnint.jess.generator.entity.GenScene@7de34e81','1','1','0',NULL,'1','1','1','99',NULL,NULL,'1','2015-04-01 14:51:45','1','2015-04-01 14:51:45',NULL,'0','0'),('4660532fac5848b59ad06973ef2b300b','8d3ca018c36d40ce9623c65a1e044188','1','2',NULL,NULL,'2',NULL,'2',NULL,NULL,NULL,'1','2015-04-03 15:29:30','1','2015-04-03 15:29:30',NULL,'0','1'),('4f40a80522a14af29f4c8c4f206d2790','com.lnint.jess.generator.entity.GenScene@1f00fea9','1','3','0',NULL,'3','3','3','3',NULL,NULL,'1','2015-04-01 15:03:05','1','2015-04-01 15:03:05',NULL,'0','0'),('6068512db70d4b768a91ed35ab92505f','9a02fafe721c4921a31511f6241b1a1b','1','2','0',NULL,'2','2','2','2',NULL,NULL,'1','2015-04-01 15:39:11','1','2015-04-01 15:39:11',NULL,'0','1'),('8d9cef3d734c4c198872d5e77fe11a48','com.lnint.jess.generator.entity.GenScene@11a7d31c','1','3','0',NULL,'3','3','3','3',NULL,NULL,'1','2015-04-01 15:01:38','1','2015-04-01 15:01:38',NULL,'0','0'),('8e812bf543354194946839ec2f9d73e6','com.lnint.jess.generator.entity.GenScene@9e98f9','3','ChildTest','0',NULL,'fatherTest','','','',NULL,NULL,'1','2015-03-31 13:29:56','1','2015-03-31 13:29:56',NULL,'0','0'),('9ab77b13a95246b19b190d11bd03a72d','com.lnint.jess.generator.entity.GenScene@1f00fea9','1','5','0',NULL,'5','5','5','5',NULL,NULL,'1','2015-04-01 15:03:13','1','2015-04-01 15:03:13',NULL,'0','0'),('a4a28999d64c46bbbfe233019b2f02c8','com.lnint.jess.generator.entity.GenScene@10f9dcc4','1','2','0',NULL,'2','2','2','2',NULL,NULL,'1','2015-04-01 15:38:34','1','2015-04-01 15:38:34',NULL,'0','1'),('a4d67b640d3449ae9f67c3936e7687c2','com.lnint.jess.generator.entity.GenScene@5286f53c','1','3','0',NULL,'3','3','3','3',NULL,NULL,'1','2015-04-01 15:00:30','1','2015-04-01 15:00:30',NULL,'0','0'),('a63fd5e5853a4459a4d210f9b65df39d','com.lnint.jess.generator.entity.GenScene@10f9dcc4','1','1','0',NULL,'1','1','1','1',NULL,NULL,'1','2015-04-01 15:38:27','1','2015-04-01 15:38:27',NULL,'0','0'),('b573312566ca445e8334716b2c17fb89','com.lnint.jess.generator.entity.GenScene@1b50ca2','3','TwoTest','0',NULL,'oneTest','','','',NULL,NULL,'1','2015-03-30 15:17:43','1','2015-03-30 15:17:43',NULL,'0','0'),('b8567626cbe54111a25c52c3f2be7831','8d3ca018c36d40ce9623c65a1e044188','1','1',NULL,NULL,'1',NULL,'1',NULL,NULL,NULL,'1','2015-04-03 15:29:30','1','2015-04-03 15:29:30',NULL,'0','0'),('c38e85179a00427983874a784d0236cd','com.lnint.jess.generator.entity.GenScene@3ac3b645','1','','0',NULL,'','','','',NULL,NULL,'1','2015-04-01 15:29:28','1','2015-04-01 15:29:28',NULL,'0','0'),('c814e1967beb4bb19a22bde1b14c4364','com.lnint.jess.generator.entity.GenScene@5286f53c','1','1','0',NULL,'1','1','1','99',NULL,NULL,'1','2015-04-01 15:00:34','1','2015-04-01 15:00:34',NULL,'0','0'),('cc17814187364967a722f69862642681','com.lnint.jess.generator.entity.GenScene@11a7d31c','1','5','0',NULL,'5','5','5','5',NULL,NULL,'1','2015-04-01 15:02:05','1','2015-04-01 15:02:05',NULL,'0','0'),('cf3bc453af524aad8d2fef863195a78b','com.lnint.jess.generator.entity.GenScene@e4c77b','3','GenSceneColumn','0',NULL,'genScene','','','',NULL,NULL,'1','2015-03-30 15:15:53','1','2015-03-30 15:15:53',NULL,'0','0'),('d5f814f981464474a16b122222eba6c2','com.lnint.jess.generator.entity.GenScene@5286f53c','1','5','0',NULL,'5','5','5','5',NULL,NULL,'1','2015-04-01 15:00:41','1','2015-04-01 15:00:41',NULL,'0','0'),('d6126e85096e49a89b9bd72ef33c8d4d','com.lnint.jess.generator.entity.GenScene@1f00fea9','1','4','0',NULL,'4','4','4','4',NULL,NULL,'1','2015-04-01 15:03:01','1','2015-04-01 15:03:01',NULL,'0','0'),('efb2dbed7a064084a7e9e8032284cebc','9c3dbcc933c54ad58d5091fc99a06057','3','TwoTest','0',NULL,'oneTest','','','',NULL,NULL,'1','2015-03-30 15:21:34','1','2015-03-30 15:21:34',NULL,'0','0'),('f19bff5fd765477f84bc8ccc7050c772','com.lnint.jess.generator.entity.GenScene@7de34e81','1','4','0',NULL,'4','4','4','4',NULL,NULL,'1','2015-04-01 14:51:52','1','2015-04-01 14:51:52',NULL,'0','0'),('f2dc67afdce54521a7a06787b1a5389e','com.lnint.jess.generator.entity.GenScene@35941226','1','1','0',NULL,'1','1','1','1',NULL,NULL,'1','2015-03-31 16:26:30','1','2015-03-31 16:26:30',NULL,'0','0'),('f3214a7981da40b6b2b0edd6c1f5138d','9a02fafe721c4921a31511f6241b1a1b','1','1','0',NULL,'1','1','1','1',NULL,NULL,'1','2015-04-01 15:39:10','1','2015-04-01 15:39:10',NULL,'0','0'),('f5cba768a40f4f4ca3799a0de5f3c8f1','com.lnint.jess.generator.entity.GenScene@3ac3b645','1','','0',NULL,'','','','',NULL,NULL,'1','2015-04-01 15:29:27','1','2015-04-01 15:29:27',NULL,'0','0'),('fdee657ccde54c54978fc434146e0b39','com.lnint.jess.generator.entity.GenScene@5286f53c','1','4','0',NULL,'4','4','4','4',NULL,NULL,'1','2015-04-01 15:00:38','1','2015-04-01 15:00:38',NULL,'0','0');

/*Table structure for table `gen_scheme` */

DROP TABLE IF EXISTS `gen_scheme`;

CREATE TABLE `gen_scheme` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `category` varchar(2000) DEFAULT NULL COMMENT '分类',
  `package_name` varchar(500) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `sub_module_name` varchar(30) DEFAULT NULL COMMENT '生成子模块名',
  `location` varchar(500) DEFAULT NULL COMMENT '生成路径',
  `function_name` varchar(500) DEFAULT NULL COMMENT '生成功能名',
  `function_name_simple` varchar(100) DEFAULT NULL COMMENT '生成功能名（简写）',
  `function_author` varchar(100) DEFAULT NULL COMMENT '生成功能作者',
  `gen_scene_id` varchar(200) DEFAULT NULL COMMENT '生成场景主键',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  `replace_file` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='生成方案';

/*Data for the table `gen_scheme` */

insert  into `gen_scheme`(`id`,`name`,`category`,`package_name`,`module_name`,`sub_module_name`,`location`,`function_name`,`function_name_simple`,`function_author`,`gen_scene_id`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`,`replace_file`) values ('1e8f6924afa04294bebc6e5a414d6541','主从表测试',NULL,'com.lnint.jess.generator','ccc','ddd','E:/workspaceSSH/jess/','小二测试2','小二测试2','小二','b9e52f8094d64a49827f31d81bf846bb','1','2015-03-27 15:20:23','1','2015-04-03 16:08:22','','0','1'),('31718f4aa9b8427cadacff2a74003b31','可编辑列表测试',NULL,'com.lnint.jess.generator','aa','bb','E:/workspaceSSH/jess/','小二测试','小二测试','小二','6ba8e97204ec417cbcaf5bb052d2dfad','1','2015-04-03 11:04:43','1','2015-04-08 16:20:21','','0','1'),('d656844271d54db8845ca7dc1eabeae3','单表测试',NULL,'com.lnint.jess.modules','aaa','bbb','E:/workspaceSSH/jess/','小二测试','小二测试','小二','76b42c10481d4a309b91b951741a4271','1','2015-03-27 12:28:39','1','2015-04-10 08:41:27','','0','1');

/*Table structure for table `sys_area` */

DROP TABLE IF EXISTS `sys_area`;

CREATE TABLE `sys_area` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `parent_id` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `code` varchar(100) DEFAULT NULL COMMENT '区域编码',
  `name` varchar(100) NOT NULL COMMENT '区域名称',
  `type` char(1) DEFAULT NULL COMMENT '区域类型',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_area_parent_id` (`parent_id`),
  KEY `sys_area_parent_ids` (`parent_ids`(333)),
  KEY `sys_area_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='区域表';

/*Data for the table `sys_area` */

insert  into `sys_area`(`id`,`parent_id`,`parent_ids`,`code`,`name`,`type`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','0','0,','0000','全球','0','1','2013-05-27 08:00:00','1','2014-09-08 13:25:28','顶级-勿删','0'),('2','1','0,1,','086','中国','1','1','2013-05-27 08:00:00','1','2014-09-08 13:25:28','中华人民共和国','0'),('3','1','0,1,','001','美国','1','1','2013-05-27 08:00:00','1','2014-09-08 13:11:22','美利坚合众国','0'),('e45f7842bafd483eb08a8a644bc9a4a2','f345b7ff624241f380de8b97191e27ad','0,1,2,0e8e3efa5f5147b6b4b88dec552d88a4,f345b7ff624241f380de8b97191e27ad,','086370101','市中区','4','1','2014-09-08 13:18:43','1','2014-09-08 13:25:28','','0'),('48aed8e87fb04d6fa386e819cfdcf3f6','0e8e3efa5f5147b6b4b88dec552d88a4','0,1,2,0e8e3efa5f5147b6b4b88dec552d88a4,','0863702','青岛市','3','1','2014-09-08 13:18:19','1','2014-09-08 13:25:28','','0'),('f345b7ff624241f380de8b97191e27ad','0e8e3efa5f5147b6b4b88dec552d88a4','0,1,2,0e8e3efa5f5147b6b4b88dec552d88a4,','0863701','济南市','3','1','2014-09-08 13:18:04','1','2014-09-08 13:25:28','','1'),('0e8e3efa5f5147b6b4b88dec552d88a4','2','0,1,2,','08637','山东省','2','1','2014-09-08 13:16:34','1','2014-09-08 13:25:28','','0'),('ede9bac878b540c0b167051d5df6f5f0','2','0,1,2,','08610','北京市','2','1','2014-09-08 13:14:53','1','2014-09-08 13:25:28','','0'),('7dc81afd8e2c4c20b0ec6eda0b114274','ede9bac878b540c0b167051d5df6f5f0','0,1,2,ede9bac878b540c0b167051d5df6f5f0,','0861001','丰台区','3','1','2014-09-08 13:15:59','1','2014-09-08 13:25:28','','0'),('5f36126375d540079256c7a68e84d34d','f345b7ff624241f380de8b97191e27ad','0,1,2,0e8e3efa5f5147b6b4b88dec552d88a4,f345b7ff624241f380de8b97191e27ad,','086370102','历城区','4','1','2014-09-08 13:19:03','1','2014-09-08 13:25:28','','0'),('a57c695f9aca43b2b27a5131c132c996','5f36126375d540079256c7a68e84d34d','0,1,2,0e8e3efa5f5147b6b4b88dec552d88a4,f345b7ff624241f380de8b97191e27ad,5f36126375d540079256c7a68e84d34d,','08637010201','洪家楼','5','1','2014-09-08 13:19:26','1','2014-09-08 13:25:28','','0'),('81912555875148aabb6d9478ca67389d','a57c695f9aca43b2b27a5131c132c996','0,1,2,0e8e3efa5f5147b6b4b88dec552d88a4,f345b7ff624241f380de8b97191e27ad,5f36126375d540079256c7a68e84d34d,a57c695f9aca43b2b27a5131c132c996,','0863701020101','七里堡居委会','6','1','2014-09-08 13:23:17','1','2014-09-08 13:25:28','','0');

/*Table structure for table `sys_dict` */

DROP TABLE IF EXISTS `sys_dict`;

CREATE TABLE `sys_dict` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `label` varchar(100) NOT NULL COMMENT '标签名',
  `value` varchar(100) NOT NULL COMMENT '数据值',
  `type` varchar(100) NOT NULL COMMENT '类型',
  `description` varchar(100) NOT NULL COMMENT '描述',
  `sort` int(11) NOT NULL COMMENT '排序（升序）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_dict_value` (`value`),
  KEY `sys_dict_label` (`label`),
  KEY `sys_dict_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='字典表';

/*Data for the table `sys_dict` */

insert  into `sys_dict`(`id`,`label`,`value`,`type`,`description`,`sort`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('3','显示','1','sys_show_hide','显示/隐藏',10,'1','2013-05-27 08:00:00','1','2015-04-03 10:20:03',NULL,'0'),('4','隐藏','0','sys_show_hide','显示/隐藏',20,'1','2013-05-27 08:00:00','1','2015-04-03 10:20:22',NULL,'0'),('5','是','1','sys_yes_no','是/否',10,'1','2013-05-27 08:00:00','1','2015-04-03 10:22:19',NULL,'0'),('6','否','0','sys_yes_no','是/否',20,'1','2013-05-27 08:00:00','1','2015-04-03 10:22:45',NULL,'0'),('12','默认主题','default','sys_theme','主题方案',10,'1','2013-05-27 08:00:00','1','2015-04-03 10:25:08',NULL,'0'),('13','天蓝主题','cerulean','sys_theme','主题方案',20,'1','2013-05-27 08:00:00','1','2015-04-03 10:25:21',NULL,'0'),('14','橙色主题','readable','sys_theme','主题方案',30,'1','2013-05-27 08:00:00','1','2015-04-03 10:25:30',NULL,'0'),('15','红色主题','united','sys_theme','主题方案',40,'1','2013-05-27 08:00:00','1','2015-04-03 10:25:37',NULL,'0'),('16','Flat主题','flat','sys_theme','主题方案',60,'1','2013-05-27 08:00:00','1','2015-04-03 10:25:48',NULL,'0'),('17','国家','1','sys_area_type','区域类型',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('18','省份、直辖市','2','sys_area_type','区域类型',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('19','地市','3','sys_area_type','区域类型',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('20','区县','4','sys_area_type','区域类型',40,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('21','公司','1','sys_office_type','机构类型',60,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('22','部门','2','sys_office_type','机构类型',70,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('23','一级','1','sys_office_grade','机构等级',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('24','二级','2','sys_office_grade','机构等级',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('25','三级','3','sys_office_grade','机构等级',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('26','四级','4','sys_office_grade','机构等级',40,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('27','所有数据','1','sys_data_scope','数据范围',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('28','所在公司及以下数据','2','sys_data_scope','数据范围',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('29','所在公司数据','3','sys_data_scope','数据范围',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('30','所在部门及以下数据','4','sys_data_scope','数据范围',40,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('31','所在部门数据','5','sys_data_scope','数据范围',50,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('32','仅本人数据','8','sys_data_scope','数据范围',90,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('33','按明细设置','9','sys_data_scope','数据范围',100,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('34','系统管理','1','sys_user_type','用户类型',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('35','部门经理','2','sys_user_type','用户类型',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('36','普通用户','3','sys_user_type','用户类型',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('62','接入日志','1','sys_log_type','日志类型',30,'1','2013-06-03 08:00:00','1','2013-06-03 08:00:00',NULL,'0'),('63','异常日志','2','sys_log_type','日志类型',40,'1','2013-06-03 08:00:00','1','2013-06-03 08:00:00',NULL,'0'),('9a47571986b8412f8c77e6845d2b3379','全球','0','sys_area_type','区域类型',1,'1','2014-09-08 13:20:41','1','2014-09-08 13:20:47',NULL,'0'),('6b36ae191b75481dbee4e60c75c80766','乡镇街道','5','sys_area_type','区域类型',50,'1','2014-09-08 13:21:42','1','2014-09-08 13:21:48',NULL,'0'),('d89209a5672b40c59f337872cb522965','社区居委会','6','sys_area_type','区域类型',60,'1','2014-09-08 13:22:28','1','2014-09-08 13:22:28',NULL,'0'),('8b0e1f1c8b3e4bcaa0408359d3cf44fc','总部','99','sys_office_type','机构类型',10,'1','2014-09-08 13:35:59','1','2014-09-08 13:41:05',NULL,'0'),('a9c03a9b2a5544b2a925bfff4ec9b0a1','科室','3','sys_office_type','机构类型',90,'1','2014-09-08 13:36:38','1','2014-09-08 13:41:15',NULL,'0'),('95a37195acab419d9f92ad2898e90d2c','单表场景','1','sys_gen_scene_type','代码生成场景',10,'1','2015-03-20 11:23:51','1','2015-03-20 11:23:51',NULL,'0'),('b8268e05e09a49fca3811f2511c351e5','父子表','2','sys_gen_scene_type','代码生成场景',20,'1','2015-03-20 11:24:08','1','2015-03-20 11:24:08',NULL,'0'),('c62205e0e4f14b5782f2af0cd9f4af87','可编辑列表','3','sys_gen_scene_type','代码生成场景',30,'1','2015-03-20 11:24:20','1','2015-03-20 11:24:20',NULL,'0'),('b825913ed53647a79d04e90e1c71206e','无','0','sys_gen_is_relate','级联关系',10,'1','2015-03-23 09:56:01','1','2015-03-23 09:56:01',NULL,'0'),('e892b742d66645eb9c4a52c13837e168','一对一','1','sys_gen_is_relate','级联关系',20,'1','2015-03-23 09:56:25','1','2015-03-23 10:00:06',NULL,'0'),('7fe29e140e19419a821126fffd89bd2e','多对一','2','sys_gen_is_relate','级联关系',30,'1','2015-03-23 09:56:44','1','2015-03-23 10:00:12',NULL,'0'),('bdbc2aa085b6440896acaf76a29993a6','无操作','0','sys_gen_relate_op','级联操作',10,'1','2015-03-23 10:12:34','1','2015-03-23 10:12:34',NULL,'0'),('cf0a508f671448d89a765c26742b61c3','级联删除','1','sys_gen_relate_op','级联操作',20,'1','2015-03-23 10:13:14','1','2015-03-23 10:13:14',NULL,'0'),('feba3f3e60b14234b7105ba4dc55fcec','级联刷新','2','sys_gen_relate_op','级联操作',30,'1','2015-03-23 10:13:39','1','2015-03-23 10:13:39',NULL,'0'),('8a6a28271a754bbcb2513c6d98df858d','级联修改','3','sys_gen_relate_op','级联操作',40,'1','2015-03-23 10:14:17','1','2015-03-23 10:14:17',NULL,'0'),('ee79a894c66d45f7b6522ed67a9d42d6','级联新增','4','sys_gen_relate_op','级联操作',50,'1','2015-03-23 10:14:41','1','2015-03-23 10:14:41',NULL,'0'),('1777050984f44ae78b278f5eecb9dc0e','ALL','5','sys_gen_relate_op','级联操作',60,'1','2015-03-23 10:14:57','1','2015-03-23 10:14:57',NULL,'0'),('c0a9c6303e644c2aaedc396761fa454c','文本框','0','sys_gen_show_type','显示控件',10,'1','2015-03-23 10:44:52','1','2015-03-23 10:44:52',NULL,'0'),('b808ff61ade742b4bcdfc1d3b364a38b','文本域','1','sys_gen_show_type','显示控件',20,'1','2015-03-23 10:45:16','1','2015-03-23 10:45:16',NULL,'0'),('fa8906a6683144f9be95467739d19259','下拉框','2','sys_gen_show_type','显示控件',30,'1','2015-03-23 10:45:33','1','2015-03-23 10:45:33',NULL,'0'),('f6f4d7422d4e442385c4a149d80162fe','复选框','3','sys_gen_show_type','显示控件',40,'1','2015-03-23 10:45:47','1','2015-03-23 10:45:47',NULL,'1'),('12a62ef691234ec7848b198d53a65690','单选框','4','sys_gen_show_type','显示控件',50,'1','2015-03-23 10:46:03','1','2015-03-23 10:46:03',NULL,'1'),('6433bb002c30461f84b76f91fc6c2f6d','字典选择','5','sys_gen_show_type','显示控件',60,'1','2015-03-23 10:46:31','1','2015-03-23 10:46:31',NULL,'1'),('6b3d12cfb6e5430a90b4ecabe76b376c','人员选择','6','sys_gen_show_type','显示控件',70,'1','2015-03-23 10:46:51','1','2015-03-23 10:46:51',NULL,'1'),('cb701b85bd284804a96604a1a60b0feb','部门选择','7','sys_gen_show_type','显示控件',80,'1','2015-03-23 10:47:16','1','2015-03-23 10:47:16',NULL,'1'),('1909f8d642d64ed0b4472690a0da977d','区域选择','8','sys_gen_show_type','显示控件',90,'1','2015-03-23 10:47:36','1','2015-03-23 10:47:36',NULL,'1'),('5c97d1066db44d79b6db92e82dcbb712','日期','3','sys_gen_show_type','显示控件',100,'1','2015-03-25 14:19:34','1','2015-03-31 13:56:45',NULL,'0'),('f893262bebca4b748fbf691ea3e87bc6','一对多单向','2','sys_gen_relate','添加级联关系',2,'1','2015-03-27 11:22:25','1','2015-03-30 13:20:41',NULL,'0'),('b0a868cb22824ec59530d564c3d09e1d','一对多双向','3','sys_gen_relate','添加级联关系',3,'1','2015-03-27 11:23:36','1','2015-03-30 13:20:47',NULL,'0'),('8450f4daf1874bfb8ce2a91690d693f4','多对多','4','sys_gen_relate','添加级联关系',5,'1','2015-03-27 11:24:13','1','2015-03-30 13:20:55',NULL,'0'),('da1e4f83e317442eb9c3cb0268f7a77d','多对多关系被维护端','5','sys_gen_relate','添加级联关系',4,'1','2015-03-27 11:24:38','1','2015-03-30 13:19:09',NULL,'0'),('3b52f8a223ef45a19ccfbd1926847ede','一对一双向关系被维护端','1','sys_gen_relate','添加级联关系',1,'1','2015-03-30 13:20:25','1','2015-03-30 13:20:34',NULL,'0');

/*Table structure for table `sys_log` */

DROP TABLE IF EXISTS `sys_log`;

CREATE TABLE `sys_log` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `type` char(1) DEFAULT '1' COMMENT '日志类型',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `remote_addr` varchar(255) DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(255) DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',
  `method` varchar(5) DEFAULT NULL COMMENT '操作方式',
  `params` text COMMENT '操作提交的数据',
  `exception` text COMMENT '异常信息',
  PRIMARY KEY (`id`),
  KEY `sys_log_create_by` (`create_by`),
  KEY `sys_log_request_uri` (`request_uri`),
  KEY `sys_log_type` (`type`),
  KEY `sys_log_create_date` (`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='日志表';

/*Data for the table `sys_log` */

/*Table structure for table `sys_mdict` */

DROP TABLE IF EXISTS `sys_mdict`;

CREATE TABLE `sys_mdict` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `parent_id` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `sort` int(11) DEFAULT NULL COMMENT '排序（升序）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_mdict_parent_id` (`parent_id`),
  KEY `sys_mdict_parent_ids` (`parent_ids`(333)),
  KEY `sys_mdict_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='多级字典表';

/*Data for the table `sys_mdict` */

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `parent_id` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '菜单名称',
  `href` varchar(255) DEFAULT NULL COMMENT '链接',
  `target` varchar(20) DEFAULT NULL COMMENT '目标',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `sort` int(11) NOT NULL COMMENT '排序（升序）',
  `is_show` char(1) NOT NULL COMMENT '是否在菜单中显示',
  `is_activiti` char(1) DEFAULT NULL COMMENT '是否同步工作流',
  `permission` varchar(2000) DEFAULT NULL COMMENT '权限标识',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_menu_parent_id` (`parent_id`),
  KEY `sys_menu_parent_ids` (`parent_ids`(333)),
  KEY `sys_menu_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='菜单表';

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`id`,`parent_id`,`parent_ids`,`name`,`href`,`target`,`icon`,`sort`,`is_show`,`is_activiti`,`permission`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','0','0,','顶级菜单',NULL,NULL,NULL,0,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('2','1','0,1,','系统设置','','','certificate',999,'1','0','','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('3','2','0,1,2,','系统设置',NULL,NULL,NULL,980,'1','0',NULL,'1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('4','3','0,1,2,3,','菜单管理','/sys/menu/',NULL,'list-alt',30,'1','0',NULL,'1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('5','4','0,1,2,3,4,','查看',NULL,NULL,NULL,30,'0','0','sys:menu:view','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('6','4','0,1,2,3,4,','修改',NULL,NULL,NULL,30,'0','0','sys:menu:edit','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('7','3','0,1,2,3,','角色管理','/sys/role/',NULL,'lock',50,'1','0',NULL,'1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('8','7','0,1,2,3,7,','查看',NULL,NULL,NULL,30,'0','0','sys:role:view','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('9','7','0,1,2,3,7,','修改',NULL,NULL,NULL,30,'0','0','sys:role:edit','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('10','3','0,1,2,3,','字典管理','/sys/dict/',NULL,'th-list',60,'1','0',NULL,'1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('11','10','0,1,2,3,10,','查看',NULL,NULL,NULL,30,'0','0','sys:dict:view','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('12','10','0,1,2,3,10,','修改',NULL,NULL,NULL,30,'0','0','sys:dict:edit','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('13','2','0,1,2,','机构用户',NULL,NULL,NULL,970,'1','0',NULL,'1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('14','13','0,1,2,13,','区域管理','/sys/area/',NULL,'th',50,'1','0',NULL,'1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('15','14','0,1,2,13,14,','查看',NULL,NULL,NULL,30,'0','0','sys:area:view','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('16','14','0,1,2,13,14,','修改',NULL,NULL,NULL,30,'0','0','sys:area:edit','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('17','13','0,1,2,13,','机构管理','/sys/office/',NULL,'th-large',40,'1','0',NULL,'1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('18','17','0,1,2,13,17,','查看',NULL,NULL,NULL,30,'0','0','sys:office:view','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('19','17','0,1,2,13,17,','修改',NULL,NULL,NULL,30,'0','0','sys:office:edit','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('20','13','0,1,2,13,','用户管理','/sys/user/',NULL,'user',30,'1','0',NULL,'1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('21','20','0,1,2,13,20,','查看',NULL,NULL,NULL,30,'0','0','sys:user:view','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('22','20','0,1,2,13,20,','修改',NULL,NULL,NULL,30,'0','0','sys:user:edit','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('24','23','0,1,2,23','项目首页','http://.com','_blank',NULL,30,'1','0',NULL,'1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('25','23','0,1,2,23','项目支持','http://.com/donation.html','_blank',NULL,50,'1','0',NULL,'1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('26','23','0,1,2,23','论坛交流','http://bbs..com','_blank',NULL,80,'1','0',NULL,'1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('67','2','0,1,2,','日志查询',NULL,NULL,NULL,985,'1','0',NULL,'1','2013-06-03 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('68','67','0,1,2,67,','日志查询','/sys/log',NULL,'pencil',30,'1','0','sys:log:view','1','2013-06-03 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('69','2','0,1,2,','流程管理',NULL,NULL,NULL,983,'1','0',NULL,'1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('70','69','0,1,2,69,','流程管理','/sys/workflow/processList',NULL,'road',300,'1','0','sys:workflow:edit','1','2013-05-27 08:00:00','1','2014-12-18 14:35:53',NULL,'0'),('d4e6d97ea25c4c51aa3b341134ca441d','1','0,1,','首页','','','ok',1,'1','0','','2','2014-09-15 15:39:16','1','2014-12-18 14:35:00',NULL,'0'),('806c0a30b98340eb9313878826e5f006','d4e6d97ea25c4c51aa3b341134ca441d','0,1,d4e6d97ea25c4c51aa3b341134ca441d,','网站导航','','','',30,'1','0','','1','2014-09-15 16:27:44','1','2014-12-18 14:35:00',NULL,'0'),('bade8aa64f4043a68878ec6ea8fbaea2','806c0a30b98340eb9313878826e5f006','0,1,d4e6d97ea25c4c51aa3b341134ca441d,806c0a30b98340eb9313878826e5f006,','内部链接','/knowledge/knowcenter/bbs/hao123','','home',30,'1','0','ln:welocome:hao123','1','2014-09-15 16:28:05','1','2015-02-04 10:04:38',NULL,'0'),('b5bd6e48db524ff185eb17eab5769993','2','0,1,2,','代码生成','','','asterisk',981,'1','0','','1','2014-12-18 14:34:10','1','2014-12-18 14:53:33',NULL,'0'),('c572aea956ea42c18048df4f4ae7f40a','b5bd6e48db524ff185eb17eab5769993','0,1,2,b5bd6e48db524ff185eb17eab5769993,','场景配置','/generator/genScene/list','','leaf',30,'1','0','generator:genScene:view,generator:genScene:edit,generator:genScheme:edit','1','2014-12-18 14:37:14','1','2015-04-09 13:09:47',NULL,'0'),('50a0437311f142c9807c849b001d1def','b5bd6e48db524ff185eb17eab5769993','0,1,2,b5bd6e48db524ff185eb17eab5769993,','生成方案配置','/generator/genScheme/list','','tags',30,'1','0','gen:genScheme:view,gen:genScheme:edit','1','2015-03-20 09:51:14','1','2015-04-09 13:09:02',NULL,'0');

/*Table structure for table `sys_office` */

DROP TABLE IF EXISTS `sys_office`;

CREATE TABLE `sys_office` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `parent_id` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `area_id` varchar(64) NOT NULL COMMENT '归属区域',
  `code` varchar(100) DEFAULT NULL COMMENT '区域编码',
  `name` varchar(100) NOT NULL COMMENT '机构名称',
  `type` char(1) NOT NULL COMMENT '机构类型',
  `grade` char(1) NOT NULL COMMENT '机构等级',
  `address` varchar(255) DEFAULT NULL COMMENT '联系地址',
  `zip_code` varchar(100) DEFAULT NULL COMMENT '邮政编码',
  `master` varchar(100) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `fax` varchar(200) DEFAULT NULL COMMENT '传真',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_office_parent_id` (`parent_id`),
  KEY `sys_office_parent_ids` (`parent_ids`(333)),
  KEY `sys_office_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='机构表';

/*Data for the table `sys_office` */

insert  into `sys_office`(`id`,`parent_id`,`parent_ids`,`area_id`,`code`,`name`,`type`,`grade`,`address`,`zip_code`,`master`,`phone`,`fax`,`email`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','0','0,','2','100000','公司总部','1','1',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('2','1','0,1,','ede9bac878b540c0b167051d5df6f5f0','100001','董事会','2','1','','','','','','','1','2013-05-27 08:00:00','1','2014-09-08 13:43:53','','0'),('4','1','0,1,','ede9bac878b540c0b167051d5df6f5f0','100003','营销部','2','1','','','','','','','1','2013-05-27 08:00:00','1','2014-09-15 16:04:36','','0');

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `office_id` varchar(64) DEFAULT NULL COMMENT '归属机构',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `enname` varchar(100) DEFAULT NULL COMMENT '角色英文名称',
  `role_type` varchar(100) DEFAULT NULL COMMENT '权限类型',
  `data_scope` char(1) DEFAULT NULL COMMENT '数据范围',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_role_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='角色表';

/*Data for the table `sys_role` */

insert  into `sys_role`(`id`,`office_id`,`name`,`enname`,`role_type`,`data_scope`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','1','admin超级系统管理员',NULL,NULL,'1','1','2013-05-27 08:00:00','1','2014-10-18 10:48:34',NULL,'0');

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  `menu_id` varchar(64) NOT NULL COMMENT '菜单编号',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='角色-菜单';

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`role_id`,`menu_id`) values ('1','1'),('1','10'),('1','11'),('1','12'),('1','13'),('1','14'),('1','15'),('1','16'),('1','17'),('1','18'),('1','19'),('1','1a70c909e83a4426b39a4558050c607d'),('1','2'),('1','20'),('1','21'),('1','22'),('1','23'),('1','24'),('1','25'),('1','25e17376d2144a46b0022f4128ca01c8'),('1','26'),('1','2b702186af8246c5b33a28460f2e457a'),('1','2b874ef04d494f80abf4af7ead694c3c'),('1','3'),('1','31'),('1','32'),('1','33'),('1','34'),('1','35'),('1','36'),('1','37'),('1','38'),('1','39'),('1','3a5654f92b7b40c78a327f906ca159d7'),('1','3c7b6956a1ce48eeb63b39e55c7230f3'),('1','4'),('1','40'),('1','41'),('1','419014f4d37d4fef8090b501e98700c5'),('1','42'),('1','43'),('1','44'),('1','45'),('1','46'),('1','47'),('1','48'),('1','49'),('1','5'),('1','50'),('1','51'),('1','52'),('1','53'),('1','54'),('1','55'),('1','56'),('1','57'),('1','58'),('1','589b4f0bf5b94d7bb096eaee19279cad'),('1','59'),('1','5ee0f34b991341ffa05abba75b7d1a97'),('1','6'),('1','60'),('1','61'),('1','6182c1406ca34f6e8ecc81bd65894a8a'),('1','62'),('1','63'),('1','64'),('1','65'),('1','66'),('1','67'),('1','68'),('1','69'),('1','6c46588d4157415eb29a68807d46d5f4'),('1','7'),('1','70'),('1','71'),('1','72'),('1','725b1024eb1e49f696ca2ac2b200a40a'),('1','73'),('1','74'),('1','75'),('1','76'),('1','77'),('1','78'),('1','79'),('1','7df21b1c4b674af5813939c864fcbee6'),('1','8'),('1','80'),('1','806c0a30b98340eb9313878826e5f006'),('1','896d167f60c44356873fc3409c6eae8b'),('1','9'),('1','97a1046208c2451699211c19fbf03eab'),('1','9860ac24267642c29dc66eb2f0b113f5'),('1','aee1c6ac8c16446d8931081d5e304be7'),('1','bade8aa64f4043a68878ec6ea8fbaea2'),('1','bdcccee418ed4b99b91195d11b87ba3b'),('1','c930500c963547c69d57fa8930983178'),('1','d0d3f8166d644b9198c85b0eaa0ea0f5'),('1','d4e6d97ea25c4c51aa3b341134ca441d'),('1','da38bc0510484249900ad5d464afb186'),('1','dd0ddfb6804542fa9a4cdde062ba0e1a'),('1','df204779e7f24a318c4c9c9ae5b79d2e'),('1','e32c507841584ab19d4bd734303e8a5e'),('1','e4e16b3908674e82a5770c1cb59a97ae'),('1','f1b4e7ad633e46b780951225653c0a36'),('1','f4cf1f5c46dd4d8d8129ba9351917396'),('1','f63708c6633d43fea9bdcd0327f25d02');

/*Table structure for table `sys_role_office` */

DROP TABLE IF EXISTS `sys_role_office`;

CREATE TABLE `sys_role_office` (
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  `office_id` varchar(64) NOT NULL COMMENT '机构编号',
  PRIMARY KEY (`role_id`,`office_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='角色-机构';

/*Data for the table `sys_role_office` */

insert  into `sys_role_office`(`role_id`,`office_id`) values ('1','1'),('1','2'),('1','3'),('1','37'),('1','3f19e624ec41420e8b060e6ad5c3f761'),('1','4'),('1','42812cf0b3fc4d198ab236a69f70edc0'),('1','4d88d73bf2174724987178f6f24f9d3f'),('1','6495a29ab6a04260a33e4ead93589761'),('1','8995113d63d4486c9a57a733cdd8917a'),('1','8ebdadfa595f441b895aba9fd3345f73'),('1','97bbae07237149ffa268e809a250abff'),('1','9b6a947de6a442218dd302b8985eb550'),('1','c6d75ba67e52482ca319ff005af2709f'),('1','cc7ad06d08ee45a796b73460771b66a0'),('1','cf30362e68444323b7ed80be1e44a71e'),('1','d8a9e0c0efc6437999ca6464bee96e12');

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `company_id` varchar(64) NOT NULL COMMENT '归属公司',
  `office_id` varchar(64) NOT NULL COMMENT '归属部门',
  `login_name` varchar(100) NOT NULL COMMENT '登录名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `no` varchar(100) DEFAULT NULL COMMENT '工号',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(200) DEFAULT NULL COMMENT '手机',
  `user_type` char(1) DEFAULT NULL COMMENT '用户类型',
  `login_ip` varchar(100) DEFAULT NULL COMMENT '最后登陆IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_user_office_id` (`office_id`),
  KEY `sys_user_login_name` (`login_name`),
  KEY `sys_user_company_id` (`company_id`),
  KEY `sys_user_update_date` (`update_date`),
  KEY `sys_user_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `sys_user` */

insert  into `sys_user`(`id`,`company_id`,`office_id`,`login_name`,`password`,`no`,`name`,`email`,`phone`,`mobile`,`user_type`,`login_ip`,`login_date`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','1','1','admin','ef028d660741f4559e9023388e831b46f41c2fc91fb2174d8ba3e5ec','88888','超级管理员','admin@163.com','88101234','18605318888',NULL,'127.0.0.1','2015-04-10 08:40:39','1','2015-04-09 14:43:04','1','2015-04-09 14:43:04','最高管理员勿删','0');

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `user_id` varchar(64) NOT NULL COMMENT '用户编号',
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户-角色';

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`user_id`,`role_id`) values ('1','1');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
