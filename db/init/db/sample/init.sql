CREATE SCHEMA IF NOT EXISTS `sample` DEFAULT CHARACTER SET utf8 ;
USE `sample`;

CREATE TABLE tb_user (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Sequence',
  `user_id` VARCHAR(20) NOT NULL COMMENT 'ID',
  `name` VARCHAR(20) NOT NULL COMMENT 'Name',
  `email` VARCHAR(125) NOT NULL COMMENT 'Email',
  `status` TINYINT NOT NULL COMMENT 'Status',
  `delete_yn` CHAR(1) NOT NULL COMMENT 'Delete Status',
  `reg_dt` DATETIME NOT NULL COMMENT 'Register Date',
  `chg_dt` DATETIME NOT NULL COMMENT 'Modified Date',
  PRIMARY KEY(`id`),
  UNIQUE KEY `udx_tb_user_1` (`user_id`),
  UNIQUE KEY `udx_tb_user_2` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='User Information';