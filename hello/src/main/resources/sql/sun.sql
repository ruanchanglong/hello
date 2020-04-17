

CREATE TABLE `common_error_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_code` char(3) NOT NULL,
  `project_name` varchar(32) DEFAULT NULL,
  `module_code` char(3) NOT NULL,
  `module_name` varchar(32) DEFAULT NULL,
  `error_code` char(3) NOT NULL,
  `error_message` varchar(256) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;


CREATE TABLE `datalake_dict_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(128) NOT NULL COMMENT '字典组Code',
  `item_id` varchar(128) NOT NULL COMMENT '字典项Code',
  `item_value` varchar(1024) NOT NULL COMMENT '字典项值',
  `item_desc` varchar(255) DEFAULT NULL COMMENT '字典描述',
  `status` varchar(16) NOT NULL COMMENT '状态：VALID有效;INVALID无效',
  `sort` int(11) NOT NULL COMMENT '排序',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_dict_item_group_id` (`group_id`,`item_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20190610233756 DEFAULT CHARSET=utf8 COMMENT='字典项表';