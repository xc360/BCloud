/*
 Navicat Premium Data Transfer

 Source Server         : mysql.ccxc.vip
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : mysql.ccxc.vip:3306
 Source Schema         : BCloud

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 08/03/2024 15:50:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for xc_api_supplier
-- ----------------------------
DROP TABLE IF EXISTS `xc_api_supplier`;
CREATE TABLE `xc_api_supplier`
(
    `id`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息供应商名称',
    `code`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息供应商标识',
    `access_id`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问id',
    `access_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问秘钥',
    `other_config`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '其他配置，json格式',
    `app_id`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用主键',
    `status`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `update_time`   timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间 不能为空',
    `create_time`   timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间 不能为空',
    `version`       int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `code__app_id` (`code` ASC, `app_id` ASC) USING BTREE COMMENT 'code和app_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_app
-- ----------------------------
DROP TABLE IF EXISTS `xc_app`;
CREATE TABLE `xc_app`
(
    `id`                       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '主键id',
    `app_name`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用程序名称',
    `app_id`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用程序id',
    `app_secret`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用程序秘钥',
    `user_id`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '用户主键',
    `group_id`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '用户组主键',
    `domain`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '域名',
    `local_ip`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '客户端ip',
    `home_url`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '首页地址',
    `refresh_url`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '刷新地址',
    `logo_url`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT 'logo图片',
    `is_open`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否公开，对应字典：whether，0：是，1：否',
    `is_coexist`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否同时在线，对应字典：whether，0：是，1：否',
    `show_user_authority`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否显示用户权限，对应字典：whether，0：是，1：否',
    `token_valid_time`         bigint                                                        NOT NULL DEFAULT 3600000 COMMENT 'token有效时间',
    `refresh_token_valid_time` bigint                                                        NOT NULL DEFAULT 7200000 COMMENT '刷新token有效时间',
    `sign_valid_time`          bigint                                                        NOT NULL COMMENT '签名有效时间',
    `open_email_login`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否打开邮箱登录，对应字典：whether，0：是，1：否',
    `open_phone_login`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否打开手机登录，对应字典：whether，0：是，1：否',
    `open_phone_forget`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否打开手机找回密码，对应字典：whether，0：是，1：否',
    `open_email_forget`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否打开邮箱找回密码，对应字典：whether，0：是，1：否',
    `open_phone_register`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否打开手机注册，对应字典：whether，0：是，1：否',
    `open_email_register`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否打开邮箱注册，对应字典：whether，0：是，1：否',
    `status`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `audit_status`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '审核状态，对应字典：auditStatus，0：未申请，1：未审核，2：已审核，3：已拒绝',
    `apply_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    `reason`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '申请，拒绝，取消原因',
    `create_time`              timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`                  int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `app_id` (`app_id` ASC) USING BTREE COMMENT '唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_applet
-- ----------------------------
DROP TABLE IF EXISTS `xc_applet`;
CREATE TABLE `xc_applet`
(
    `id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `type`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '小程序类型',
    `applet_id`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '小程序ID',
    `app_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用id',
    `user_id`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `user_id__app_id__type` (`user_id` ASC, `app_id` ASC, `type` ASC) USING BTREE COMMENT 'type，user_id，app_id唯一索引',
    UNIQUE INDEX `app_id__applet_id__type` (`app_id` ASC, `applet_id` ASC, `type` ASC) USING BTREE COMMENT 'type，app_id，wx_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_authority
-- ----------------------------
DROP TABLE IF EXISTS `xc_authority`;
CREATE TABLE `xc_authority`
(
    `id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
    `describe`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '权限描述',
    `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限code',
    `type`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限类型，对应字典：authorityType，0：菜单，1：接口，2：按钮，3：开放菜单，4：应用权限，5：用户信息权限',
    `url`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '跳转地址',
    `icon`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '菜单图标',
    `node`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点',
    `parent_node` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上级节点',
    `seq`         int                                                           NOT NULL DEFAULT 0 COMMENT '排序',
    `hidden`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否隐藏，对应字典：whether，0：是，1：否',
    `app_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用id',
    `status`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `app_id__code` (`code` ASC, `app_id` ASC) USING BTREE COMMENT 'app_id和code唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_authorize
-- ----------------------------
DROP TABLE IF EXISTS `xc_authorize`;
CREATE TABLE `xc_authorize`
(
    `id`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `app_id`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '应用id',
    `authority_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限id',
    `audit_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '审核状态，对应字典：auditStatus，0：未申请，1：未审核，2：已审核，3：已拒绝',
    `apply_time`   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    `reason`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '申请，拒绝，取消原因',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `app_id__authority_id` (`app_id` ASC, `authority_id` ASC) USING BTREE COMMENT 'app_id和authority_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_column
-- ----------------------------
DROP TABLE IF EXISTS `xc_column`;
CREATE TABLE `xc_column`
(
    `id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '栏目名称',
    `label`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '栏目名称',
    `describe`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '栏目描述',
    `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '栏目标识',
    `data_type`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据类型',
    `url`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '跳转地址',
    `icon`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '栏目图标',
    `column_size` int                                                           NOT NULL DEFAULT 1 COMMENT '栏目大小',
    `node`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点',
    `parent_node` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上级节点',
    `seq`         int                                                           NOT NULL DEFAULT 0 COMMENT '排序',
    `app_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '应用主键',
    `status`      varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `code__app_id` (`code` ASC, `app_id` ASC) USING BTREE COMMENT 'code,app_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_data_type
-- ----------------------------
DROP TABLE IF EXISTS `xc_data_type`;
CREATE TABLE `xc_data_type`
(
    `id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '类型名称',
    `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '类型标识',
    `status`      varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `app_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用主键',
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `code__app_id` (`code` ASC, `app_id` ASC) USING BTREE COMMENT 'code和app_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_deleted_user
-- ----------------------------
DROP TABLE IF EXISTS `xc_deleted_user`;
CREATE TABLE `xc_deleted_user`
(
    `id`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `user_id`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '用户id',
    `account`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
    `email`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '邮箱',
    `phone`         varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '手机号',
    `password`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '密码',
    `access_id`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问ID',
    `access_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问秘钥',
    `fail_record`   int                                                           NOT NULL DEFAULT 0 COMMENT '登录失败次数',
    `initial_admin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否是初始化管理员，对应字典：whether，0：是，1：否',
    `delete_time`   datetime                                                      NOT NULL COMMENT '删除时间',
    `create_time`   timestamp                                                     NULL     DEFAULT NULL COMMENT '创建时间',
    `update_time`   timestamp                                                     NULL     DEFAULT NULL COMMENT '更新时间',
    `version`       int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_dict
-- ----------------------------
DROP TABLE IF EXISTS `xc_dict`;
CREATE TABLE `xc_dict`
(
    `id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
    `name`        varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '字典名称',
    `value`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典值',
    `type`        varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '字典类型',
    `seq`         int                                                           NOT NULL DEFAULT 0 COMMENT '排序',
    `app_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用主键id',
    `status`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `app_id__type__value` (`value` ASC, `type` ASC, `app_id` ASC) USING BTREE COMMENT 'app_id和type和value唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '系统字典表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_disable_ip
-- ----------------------------
DROP TABLE IF EXISTS `xc_disable_ip`;
CREATE TABLE `xc_disable_ip`
(
    `id`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `ip_address`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ip地址',
    `disable_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '禁用原因',
    `disable_time`   bigint                                                        NULL     DEFAULT 0 COMMENT '禁用时间，0：永久禁用',
    `app_id`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用主键',
    `status`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `update_time`    timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间 不能为空',
    `create_time`    timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间 不能为空',
    `version`        int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `ip_address__app_id` (`ip_address` ASC, `app_id` ASC) USING BTREE COMMENT 'ip_addres和app_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_group
-- ----------------------------
DROP TABLE IF EXISTS `xc_group`;
CREATE TABLE `xc_group`
(
    `id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账户组名称',
    `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账户组标识',
    `describe`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '账户组描述',
    `app_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用主键id',
    `status`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `code__app_id` (`code` ASC, `app_id` ASC) USING BTREE COMMENT '标识应用唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_group__role
-- ----------------------------
DROP TABLE IF EXISTS `xc_group__role`;
CREATE TABLE `xc_group__role`
(
    `id`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
    `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组id',
    `role_id`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `group_id__role_id` (`group_id` ASC, `role_id` ASC) USING BTREE COMMENT 'group_id和role_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_info
-- ----------------------------
DROP TABLE IF EXISTS `xc_info`;
CREATE TABLE `xc_info`
(
    `id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `key`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标识',
    `value`       longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NOT NULL COMMENT '内容',
    `describe`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '信息名称',
    `type`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '信息类型，对应字典：infoType，0：客户端信息，1：后台信息',
    `app_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用主键',
    `status`      varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `key__app_id` (`key` ASC, `app_id` ASC) USING BTREE COMMENT 'key和app_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_message_log
-- ----------------------------
DROP TABLE IF EXISTS `xc_message_log`;
CREATE TABLE `xc_message_log`
(
    `id`                    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `account`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息接收者账号',
    `content`               longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NOT NULL COMMENT '消息内容',
    `message_template_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息模板标识',
    `app_id`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用主键',
    `status`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否成功，0：成功，1：未成功',
    `update_time`           timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间 不能为空',
    `create_time`           timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间 不能为空',
    `version`               int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_message_template
-- ----------------------------
DROP TABLE IF EXISTS `xc_message_template`;
CREATE TABLE `xc_message_template`
(
    `id`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `name`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `code`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息标识',
    `sign_name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '签名',
    `template_code`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '模板code',
    `template`          longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '模板',
    `other_config`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '其他配置，json格式',
    `api_supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口供应商标识',
    `app_id`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用主键',
    `status`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `update_time`       timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间 不能为空',
    `create_time`       timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间 不能为空',
    `version`           int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `code__app_id` (`code` ASC, `app_id` ASC) USING BTREE COMMENT 'code和app_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_page
-- ----------------------------
DROP TABLE IF EXISTS `xc_page`;
CREATE TABLE `xc_page`
(
    `id`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `name`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '页面名称',
    `code`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
    `data_type`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据类型',
    `seo_title`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT 'seo标题',
    `seo_keywords`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT 'seo关键字',
    `seo_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT 'seo描述',
    `file_path`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '页面文件地址',
    `redirect_url`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '页面重定向地址',
    `menu_code`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '菜单标识',
    `app_id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用主键',
    `status`          varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `create_time`     timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `code__app_id` (`code` ASC, `app_id` ASC) USING BTREE COMMENT 'code和app_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_page__column
-- ----------------------------
DROP TABLE IF EXISTS `xc_page__column`;
CREATE TABLE `xc_page__column`
(
    `id`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `page_id`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单组件',
    `column_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '栏目主键',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `page_id__column_id` (`page_id` ASC, `column_id` ASC) USING BTREE COMMENT 'page_id和column_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_phone_backup
-- ----------------------------
DROP TABLE IF EXISTS `xc_phone_backup`;
CREATE TABLE `xc_phone_backup`
(
    `phone_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
    PRIMARY KEY (`phone_no`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_phone_info_backup
-- ----------------------------
DROP TABLE IF EXISTS `xc_phone_info_backup`;
CREATE TABLE `xc_phone_info_backup`
(
    `id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `phone`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
    `isp`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '运营商',
    `province`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '省',
    `city`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '市',
    `type`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '类型',
    `area_code`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '区域编码',
    `city_code`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '区号',
    `postal_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '邮政编码',
    `latitude`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '归属地纬度',
    `longitude`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '归属地经度',
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `phone` (`phone` ASC) USING BTREE COMMENT '手机号唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_role
-- ----------------------------
DROP TABLE IF EXISTS `xc_role`;
CREATE TABLE `xc_role`
(
    `id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
    `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色标识',
    `describe`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '描述',
    `type`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '角色类型，对应字典：roleType，0：普通角色，1：基础角色',
    `app_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用主键',
    `status`      varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `code__app_id` (`code` ASC, `app_id` ASC) USING BTREE COMMENT '标识应用唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_role__authority
-- ----------------------------
DROP TABLE IF EXISTS `xc_role__authority`;
CREATE TABLE `xc_role__authority`
(
    `id`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `role_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
    `authority_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `role_id__authority_id` (`role_id` ASC, `authority_id` ASC) USING BTREE COMMENT 'role_id和authority_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_statistics
-- ----------------------------
DROP TABLE IF EXISTS `xc_statistics`;
CREATE TABLE `xc_statistics`
(
    `id`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `type`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型，0：开始，1：异常，2：结束',
    `statistics_time` datetime                                                      NULL DEFAULT NULL COMMENT '统计时间',
    `client_ip`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端ip地址',
    `url`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户访问url',
    `user_id`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户主键',
    `param_json`      json                                                          NULL COMMENT '请求参数',
    `response_time`   decimal(10, 3)                                                NULL DEFAULT NULL COMMENT '响应时长，单位秒',
    `result_json`     json                                                          NULL COMMENT '返回参数',
    `error_type`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常类型，0：操作异常，1：系统异常',
    `error_message`   longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '异常消息',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `index_client_ip` (`client_ip` ASC) USING BTREE COMMENT '客户端ip索引',
    INDEX `index_statistics_time` (`statistics_time` ASC) USING BTREE COMMENT '统计时间索引',
    INDEX `index_type` (`type` ASC) USING BTREE COMMENT '类型索引',
    INDEX `index_error_type` (`error_type` ASC) USING BTREE COMMENT '错误类型索引',
    INDEX `index_url` (`url` ASC) USING BTREE COMMENT 'url地址索引',
    INDEX `index_user_id` (`user_id` ASC) USING BTREE COMMENT '用户主键索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_tree_dict
-- ----------------------------
DROP TABLE IF EXISTS `xc_tree_dict`;
CREATE TABLE `xc_tree_dict`
(
    `id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
    `type`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '树形字典类型',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '树形字典名称',
    `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '树形字典code',
    `node`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点',
    `parent_node` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '上级节点',
    `seq`         int                                                           NOT NULL DEFAULT 0 COMMENT '排序',
    `app_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用id',
    `status`      varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `app_id__type__node` (`type` ASC, `node` ASC, `app_id` ASC) USING BTREE COMMENT '应用类型节点唯一标识',
    UNIQUE INDEX `app_id__type__code` (`type` ASC, `code` ASC, `app_id` ASC) USING BTREE COMMENT '应用类型标识唯一标识'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '更新时间\r\n2020年2月'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_user
-- ----------------------------
DROP TABLE IF EXISTS `xc_user`;
CREATE TABLE `xc_user`
(
    `id`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `account`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
    `email`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '邮箱',
    `phone`         varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '手机号',
    `password`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '密码',
    `access_id`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问ID',
    `access_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问秘钥',
    `fail_record`   int                                                           NOT NULL DEFAULT 0 COMMENT '登录失败次数',
    `initial_admin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否是初始化管理员，对应字典：whether，0：是，1：否',
    `create_time`   timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`       int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `account` (`account` ASC) USING BTREE COMMENT '账号唯一索引',
    UNIQUE INDEX `access_id` (`access_id` ASC) USING BTREE COMMENT '访问ID唯一索引',
    UNIQUE INDEX `email` (`email` ASC) USING BTREE COMMENT '邮箱唯一索引',
    UNIQUE INDEX `phone` (`phone` ASC) USING BTREE COMMENT '手机号唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_user__group
-- ----------------------------
DROP TABLE IF EXISTS `xc_user__group`;
CREATE TABLE `xc_user__group`
(
    `id`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组id',
    `user_id`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `group_id__user_id` (`group_id` ASC, `user_id` ASC) USING BTREE COMMENT 'group_id和user_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_user__role
-- ----------------------------
DROP TABLE IF EXISTS `xc_user__role`;
CREATE TABLE `xc_user__role`
(
    `id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
    `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `user_id__role_id` (`user_id` ASC, `role_id` ASC) USING BTREE COMMENT 'user_id和role_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_user_authorize
-- ----------------------------
DROP TABLE IF EXISTS `xc_user_authorize`;
CREATE TABLE `xc_user_authorize`
(
    `id`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `user_id`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
    `app_id`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用id',
    `authorize_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `user_id__app_id` (`user_id` ASC, `app_id` ASC) USING BTREE COMMENT 'user_id和app_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_user_authorize__authority
-- ----------------------------
DROP TABLE IF EXISTS `xc_user_authorize__authority`;
CREATE TABLE `xc_user_authorize__authority`
(
    `id`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
    `user_authorize_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用id',
    `authority_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限主键',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `user_id__app_id__authority_id` (`user_authorize_id` ASC, `authority_id` ASC) USING BTREE COMMENT '唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_user_info
-- ----------------------------
DROP TABLE IF EXISTS `xc_user_info`;
CREATE TABLE `xc_user_info`
(
    `id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
    `user_id`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '用户id',
    `nick_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '昵称',
    `portrait`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '头像',
    `explain`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '个人说明',
    `birthday`    date                                                          NULL     DEFAULT NULL COMMENT '生日',
    `sex`         varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL COMMENT '性别：对应字典：sex，0：女，1：男',
    `region`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '区域',
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     int UNSIGNED                                                  NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `user_id` (`user_id` ASC) USING BTREE COMMENT '用户id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for xc_version
-- ----------------------------
DROP TABLE IF EXISTS `xc_version`;
CREATE TABLE `xc_version`
(
    `id`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `type`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本类型，对应字典：versionType，0：网页应用，1：软件包应用，2：安卓应用',
    `app_version`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '应用版本号',
    `package_url`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '安装包地址',
    `doc_content`       longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '文档内容',
    `update_content`    longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '更新内容',
    `agreement_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '注册协议内容',
    `force_upgrade`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否强制升级，对应字典：whether，0：是，1：否',
    `app_id`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用主键',
    `status`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态，对应字典：effectStatus，0：有效，1：无效',
    `create_time`       timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`           int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `type__app_version__app_id` (`app_version` ASC, `app_id` ASC, `type` ASC) USING BTREE COMMENT 'app_version和app_id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

-- 创建基础用户
INSERT INTO `xc_user`(`id`, `account`, `email`, `phone`, `password`, `access_id`, `access_secret`, `fail_record`, `initial_admin`, `create_time`, `update_time`, `version`) VALUES ('1', 'admin', 'xiaozhaomailbox@qq.com', NULL, '861951f1314e524731615481b15d97d5d22086df39f90220', 'U1684891843', '55651da6180a72690f173893d4b47d96d41a33448a71a088', 0, '0', '2020-04-18 20:00:04', '2023-08-01 11:15:42', 890);

-- 创建基础用户信息
INSERT INTO `xc_user_info`(`id`, `user_id`, `nick_name`, `portrait`, `explain`, `birthday`, `sex`, `create_time`, `update_time`, `version`) VALUES ('1', '1', '管理员', '', '一个刻苦认真的程序员!', '2020-11-06', '1', '2020-07-06 13:53:14', '2021-10-25 10:23:17', 152);

-- 创建角色
INSERT INTO `xc_role` (`id`, `name`, `code`, `describe`, `type`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1659141149980639234', '超级管理员', 'admin', NULL, '0', '3', '0', '2023-05-18 18:17:30', '2023-05-18 18:17:30', 0);

-- 创建用户在角色关联
INSERT INTO `xc_user__role` (`id`, `user_id`, `role_id`) VALUES ('1659141448640249858', '1', '1659141149980639234');

-- 创建应用信息
INSERT INTO `xc_app` (`id`, `app_name`, `app_id`, `app_secret`, `user_id`, `group_id`, `domain`, `local_ip`, `home_url`, `refresh_url`, `logo_url`, `is_open`, `is_coexist`, `show_user_authority`, `token_valid_time`, `refresh_token_valid_time`, `sign_valid_time`, `open_email_login`, `open_phone_login`, `open_phone_forget`, `open_email_forget`, `open_phone_register`, `open_email_register`, `status`, `audit_status`, `apply_time`, `reason`, `create_time`, `update_time`, `version`) VALUES ('3', '基础服务平台', 'xc1832640471', '24cf2034b07252d681372f8650461e79e43113292a25091f', '1', NULL, NULL, NULL, '/BCloud/admin', NULL, '${xcFileUrl}/BCloud/app/1/logo/202204282011138218.png', '0', '0', '1', 3600000, 7200000, 1800000, '0', '0', '0', '0', '0', '0', '0', '2', '2022-04-28 20:38:15', NULL, '2020-08-17 16:30:15', '2023-08-30 09:40:57', 86);

-- 接口供应商，需要配置自己的access_id和access_secret
DELETE FROM `xc_api_supplier` WHERE app_id='3';
INSERT INTO `xc_api_supplier` (`id`, `name`, `code`, `access_id`, `access_secret`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1464113014281089026', '阿里短信', 'ali_sms', '', '', '3', '0', '2022-10-28 13:45:06', '2021-11-26 14:05:19', 3);
INSERT INTO `xc_api_supplier` (`id`, `name`, `code`, `access_id`, `access_secret`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1464113307756539906', '腾讯邮箱', 'qq_mail', '', '', '3', '0', '2022-10-28 13:45:21', '2021-11-26 14:06:29', 3);

-- 消息模板，需要配置自己的access_id和access_secret
DELETE FROM `xc_message_template` WHERE app_id='3';
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1355010409228603393', '手机验证码-登录', 'phone_login', '捷腾信息', 'SMS_207520902', NULL, '', 'ali_sms', '3', '0', '2022-10-28 13:45:57', '2021-01-29 12:30:31', 4);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1355024574106292225', '手机验证码-注册', 'phone_register', '捷腾信息', 'SMS_207520901', NULL, '', 'ali_sms', '3', '0', '2022-10-28 13:45:57', '2021-01-29 13:26:48', 3);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1355025072456716290', '手机验证码-找回密码', 'phone_forget_password', '捷腾信息', 'SMS_207520903', NULL, '', 'ali_sms', '3', '0', '2022-10-28 13:45:57', '2021-01-29 13:28:47', 2);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1355025273158356994', '手机验证码-修改手机号', 'phone_update', '捷腾信息', 'SMS_227900030', NULL, '', 'ali_sms', '3', '0', '2022-10-28 13:45:57', '2021-01-29 13:29:35', 4);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1355025490398138369', '手机验证码-注销', 'phone_unsubscribe', '捷腾信息', 'SMS_207520904', NULL, '', 'ali_sms', '3', '0', '2022-10-28 13:45:57', '2021-01-29 13:30:27', 2);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1355025974425014273', '邮箱验证码-登录', 'email_login', '捷腾信息', NULL, '<div style=\"width:100%;text-align: center\">\n    <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">\n        您好！你正在使用本系统的邮箱登录功能，本次请求的验证码为：\n    </p>\n    <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">\n        <b style=\"font-size:18px;color:#f90\" th:text=\"${code}\"></b>\n    </p>\n    <p\n            style=\"margin:0px 0px 0px 10px;padding:0;line-height:30px;font-size:14px;color:#979797;font-family:\'宋体\',arial,sans-serif\">\n        (为了保障您帐号的安全性，请在5分钟内完成验证。)</p>\n</div>', '', 'qq_mail', '3', '0', '2022-10-28 13:46:11', '2021-01-29 13:32:22', 5);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1355027177917632514', '邮箱验证码-注册', 'email_register', '捷腾信息', NULL, '<div style=\"width:100%;text-align: center\">\n    <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">\n        您好！你正在使用本系统的邮箱注册功能，本次请求的验证码为：\n    </p>\n    <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">\n        <b style=\"font-size:18px;color:#f90\" th:text=\"${code}\"></b>\n    </p>\n    <p\n            style=\"margin:0px 0px 0px 10px;padding:0;line-height:30px;font-size:14px;color:#979797;font-family:\'宋体\',arial,sans-serif\">\n        (为了保障您帐号的安全性，请在5分钟内完成验证。)</p>\n</div>', '', 'qq_mail', '3', '0', '2022-10-28 13:46:11', '2021-01-29 13:37:09', 2);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1355027518188933121', '邮箱验证码-找回密码', 'email_forget_password', '捷腾信息', NULL, '<div style=\"width:100%;text-align: center\">\n    <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">\n        您好！你正在使用本系统的邮箱找回密码功能，本次请求的验证码为：\n    </p>\n    <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">\n        <b style=\"font-size:18px;color:#f90\" th:text=\"${code}\"></b>\n    </p>\n    <p\n            style=\"margin:0px 0px 0px 10px;padding:0;line-height:30px;font-size:14px;color:#979797;font-family:\'宋体\',arial,sans-serif\">\n        (为了保障您帐号的安全性，请在5分钟内完成验证。)</p>\n</div>', '', 'qq_mail', '3', '0', '2022-10-28 13:46:11', '2021-01-29 13:38:30', 3);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1355027767263481857', '邮箱验证码-修改邮箱', 'email_update', '捷腾信息', NULL, '<div style=\"width:100%;text-align: center\">\r\n    <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">\r\n        您好！你正在使用本系统的绑定邮箱功能，本次请求的验证码为：\r\n    </p>\r\n    <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">\r\n        <b style=\"font-size:18px;color:#f90\" th:text=\"${code}\"></b>\r\n    </p>\r\n    <p\r\n            style=\"margin:0px 0px 0px 10px;padding:0;line-height:30px;font-size:14px;color:#979797;font-family:\'宋体\',arial,sans-serif\">\r\n        (为了保障您帐号的安全性，请在5分钟内完成验证。)</p>\r\n</div>', '', 'qq_mail', '3', '0', '2022-10-28 13:46:11', '2021-01-29 13:39:29', 3);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1355028242134192130', '邮箱验证码-注销', 'email_unsubscribe', '捷腾信息', NULL, '<div style=\"width:100%;text-align: center\"> <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\"> 您好！你正在使用本系统的注销账户功能，本次请求的验证码为： </p> <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\"> <b style=\"font-size:18px;color:#f90\" th:text=\"${code}\"></b> </p> <p style=\"margin:0px 0px 0px 10px;padding:0;line-height:30px;font-size:14px;color:#979797;font-family:\'宋体\',arial,sans-serif\"> (为了保障您帐号的安全性，请在5分钟内完成验证。)</p> </div>	', '', 'qq_mail', '3', '0', '2022-10-28 13:46:12', '2021-01-29 13:41:23', 4);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1460462194939858946', '邮箱验证码-验证邮箱修改手机号', 'email_update_phone', '捷腾信息', NULL, '<div style=\"width:100%;text-align: center\">\r\n  <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">\r\n    您好！你正在使用本系统的修改手机号功能，本次请求的验证码为：\r\n  </p>\r\n  <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">\r\n    <b style=\"font-size:18px;color:#f90\" th:text=\"${code}\"></b>\r\n  </p>\r\n  <p\r\n    style=\"margin:0px 0px 0px 10px;padding:0;line-height:30px;font-size:14px;color:#979797;font-family:\'宋体\',arial,sans-serif\">\r\n    (为了保障您帐号的安全性，请在5分钟内完成验证。)</p>\r\n</div>', NULL, 'qq_mail', '3', '0', '2022-10-28 13:46:12', '2021-11-16 12:18:17', 2);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1460463864163799042', '邮箱验证码-验证邮箱修改邮箱', 'email_update_email', '捷腾信息', NULL, '<div style=\"width:100%;text-align: center\">\r\n  <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">\r\n    您好！你正在使用本系统的修改邮箱功能，本次请求的验证码为：\r\n  </p>\r\n  <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">\r\n    <b style=\"font-size:18px;color:#f90\" th:text=\"${code}\"></b>\r\n  </p>\r\n  <p\r\n    style=\"margin:0px 0px 0px 10px;padding:0;line-height:30px;font-size:14px;color:#979797;font-family:\'宋体\',arial,sans-serif\">\r\n    (为了保障您帐号的安全性，请在5分钟内完成验证。)</p>\r\n</div>', NULL, 'qq_mail', '3', '0', '2022-10-28 13:46:12', '2021-11-16 12:24:55', 1);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1460481629461389313', '手机验证码-修改手机号确认', 'phone_update_phone', '捷腾信息', 'SMS_207530983', NULL, NULL, 'ali_sms', '3', '0', '2022-10-28 13:45:57', '2021-11-16 13:35:31', 2);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1460481944260681729', '手机验证码-修改邮箱确认', 'phone_update_email', '捷腾信息', 'SMS_227900031', NULL, NULL, 'ali_sms', '3', '0', '2022-10-28 13:45:58', '2021-11-16 13:36:46', 3);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1684033203561582593', '系统异常通知-手机', 'phone_exception_notice', '捷腾信息', 'SMS_462090125', NULL, NULL, 'ali_sms', '3', '1', '2023-07-26 11:02:56', '2023-07-26 10:49:38', 2);
INSERT INTO `xc_message_template` (`id`, `name`, `code`, `sign_name`, `template_code`, `template`, `other_config`, `api_supplier_code`, `app_id`, `status`, `update_time`, `create_time`, `version`) VALUES ('1684033127007145986', '系统异常通知-邮箱', 'email_exception_notice', '捷腾信息', NULL, '<div style=\"width:100%;text-align: center\">\n    <p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#33363C;\">\n        <span style=\"color: #09A183;\" th:text=\"${message}\"></span>\n    </p>\n</div>', NULL, 'qq_mail', '3', '0', '2023-07-26 11:03:56', '2023-07-26 10:49:19', 2);

-- 基础平台字典
DELETE FROM `xc_dict` WHERE app_id='3';
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906363907272705', '有效', '0', 'effectStatus', 0, '3', '0', '2020-10-24 15:39:35', '2021-09-07 16:43:11', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906364330897410', '无效', '1', 'effectStatus', 0, '3', '0', '2020-10-24 15:39:35', '2021-09-07 16:43:11', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906364758716417', '未申请', '0', 'auditStatus', 0, '3', '0', '2020-10-24 15:39:35', '2021-09-07 16:43:11', 1);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906365173952514', '已申请', '1', 'auditStatus', 0, '3', '0', '2020-10-24 15:39:36', '2021-09-07 16:43:12', 1);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906365589188609', '审核通过', '2', 'auditStatus', 0, '3', '0', '2020-10-24 15:39:36', '2021-09-07 16:43:12', 1);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906366000230401', '正常', '0', 'userStatus', 0, '3', '0', '2020-10-24 15:39:36', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906366566461442', '锁定', '1', 'userStatus', 0, '3', '0', '2020-10-24 15:39:36', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906366981697538', '是', '0', 'whether', 0, '3', '0', '2020-10-24 15:39:36', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906367388545026', '否', '1', 'whether', 0, '3', '0', '2020-10-24 15:39:36', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906367812169729', '男', '1', 'sex', 0, '3', '0', '2020-10-24 15:39:36', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906368252571649', '女', '0', 'sex', 0, '3', '0', '2020-10-24 15:39:36', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906368672002049', '菜单', '0', 'authorityType', 0, '3', '0', '2020-10-24 15:39:36', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906369129181185', '接口', '1', 'authorityType', 0, '3', '0', '2020-10-24 15:39:36', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906369536028673', '按钮', '2', 'authorityType', 0, '3', '0', '2020-10-24 15:39:37', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906370362306562', '应用权限', '4', 'authorityType', 0, '3', '0', '2020-10-24 15:39:37', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906370823680002', '用户信息权限', '5', 'authorityType', 0, '3', '0', '2020-10-24 15:39:37', '2021-09-08 09:36:29', 1);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906371234721793', '网页应用', '0', 'versionType', 0, '3', '0', '2020-10-24 15:39:37', '2022-01-25 13:35:51', 1);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906371654152194', '软件包应用', '1', 'versionType', 0, '3', '0', '2020-10-24 15:39:37', '2022-01-25 13:36:15', 1);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906372081971201', '普通角色', '0', 'roleType', 0, '3', '0', '2020-10-24 15:39:37', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906372501401601', '基础角色', '1', 'roleType', 0, '3', '0', '2020-10-24 15:39:37', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906373034078210', '客户端信息', '0', 'infoType', 0, '3', '0', '2020-10-24 15:39:37', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1319906373466091521', '后台信息', '1', 'infoType', 0, '3', '0', '2020-10-24 15:39:38', '2021-09-07 16:43:12', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1321804505022861314', '审核未通过', '3', 'auditStatus', 0, '3', '0', '2020-10-29 21:22:06', '2021-09-07 16:43:12', 1);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1354975153586921473', '阿里短信', '0', 'messageType', 1, '3', '0', '2021-01-29 10:10:25', '2021-09-07 20:59:11', 2);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1354975287775289345', '腾讯短信', '1', 'messageType', 3, '3', '0', '2021-01-29 10:10:57', '2021-09-07 20:59:14', 2);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1354975374052122625', '腾讯邮箱', '2', 'messageType', 2, '3', '0', '2021-01-29 10:11:18', '2021-09-07 20:59:14', 3);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1473119620289343490', '开放菜单', '3', 'menuType', 1, '3', '0', '2021-12-21 10:34:21', '2021-12-21 10:34:21', 0);
INSERT INTO `xc_dict` (`id`, `name`, `value`, `type`, `seq`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1485849021791690753', '安卓应用', '2', 'versionType', 3, '3', '0', '2022-01-25 13:36:27', '2022-01-25 13:36:27', 0);

-- 创建角色权限关联
DELETE FROM `xc_role__authority` WHERE role_id='1659141149980639234';
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011225718785', '1659141149980639234', '1355137685716885505');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1451940676912005122', '1659141149980639234', '1355137685830131713');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011842281474', '1659141149980639234', '1355137685863686145');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011238301697', '1659141149980639234', '1355137685897240578');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011246690305', '1659141149980639234', '1355137685930795010');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011250884611', '1659141149980639234', '1355137685960155138');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011255078914', '1659141149980639234', '1355137685993709570');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467522', '1659141149980639234', '1355137686027264002');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467525', '1659141149980639234', '1355137686090178561');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011439628290', '1659141149980639234', '1355137686123732994');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011439628292', '1659141149980639234', '1355137686153093122');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011452211203', '1659141149980639234', '1355137686199230465');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011334770689', '1659141149980639234', '1355137686228590593');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011368325121', '1659141149980639234', '1355137686262145026');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011376713730', '1659141149980639234', '1355137686291505153');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011380908034', '1659141149980639234', '1355137686329253889');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011397685249', '1659141149980639234', '1355137686367002626');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011401879554', '1659141149980639234', '1355137686392168450');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011406073858', '1659141149980639234', '1355137686585106433');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011414462465', '1659141149980639234', '1355137686773850113');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011418656769', '1659141149980639234', '1355137686840958977');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011468988417', '1659141149980639234', '1355137687059062785');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011473182722', '1659141149980639234', '1355137687289749505');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011481571329', '1659141149980639234', '1355137687444938754');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011485765634', '1659141149980639234', '1355137687570767873');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011489959939', '1659141149980639234', '1355137687692402689');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011502542850', '1659141149980639234', '1355137687734345729');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011506737154', '1659141149980639234', '1355137687814037505');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011515125761', '1659141149980639234', '1355137687847591938');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011515125763', '1659141149980639234', '1355137687881146369');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011515125765', '1659141149980639234', '1355137687914700802');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1451940676916199426', '1659141149980639234', '1355137687948255233');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1451940676920393730', '1659141149980639234', '1355137687986003970');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1451940676920393732', '1659141149980639234', '1355137688019558402');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011557068801', '1659141149980639234', '1355137688053112834');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011565457409', '1659141149980639234', '1355137688082472962');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011569651713', '1659141149980639234', '1355137688116027393');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011578040322', '1659141149980639234', '1355137688145387522');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011590623234', '1659141149980639234', '1355137688183136257');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011859058690', '1659141149980639234', '1355137688212496385');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011875835906', '1659141149980639234', '1355137688246050818');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011204747266', '1659141149980639234', '1355137688308965377');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011351547905', '1659141149980639234', '1355137688338325506');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011389296642', '1659141149980639234', '1355137688371879938');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011431239683', '1659141149980639234', '1355137688401240066');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011460599810', '1659141149980639234', '1355137688434794498');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011498348546', '1659141149980639234', '1355137688468348929');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011548680193', '1659141149980639234', '1355137688518680578');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011200552961', '1659141149980639234', '1355137688552235009');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011213135873', '1659141149980639234', '1355137688581595137');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011221524482', '1659141149980639234', '1355137688623538178');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011863252994', '1659141149980639234', '1355137688657092610');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011880030211', '1659141149980639234', '1355137688690647042');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011607400451', '1659141149980639234', '1355137688732590081');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011917778945', '1659141149980639234', '1355137688766144513');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011921973250', '1659141149980639234', '1355137688799698945');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011921973251', '1659141149980639234', '1355137688829059073');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011888418818', '1659141149980639234', '1355137688862613506');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1451940676912005123', '1659141149980639234', '1355137688908750849');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011611594753', '1659141149980639234', '1355137688938110978');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467523', '1659141149980639234', '1355137689030385666');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467524', '1659141149980639234', '1355137689101688834');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011846475777', '1659141149980639234', '1355137689131048961');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011846475778', '1659141149980639234', '1355137689164603394');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011250884610', '1659141149980639234', '1355137689193963522');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011838087169', '1659141149980639234', '1355137689223323650');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011242496002', '1659141149980639234', '1355137689290432513');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011926167553', '1659141149980639234', '1355137689319792642');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011234107394', '1659141149980639234', '1355137689349152769');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467526', '1659141149980639234', '1355137689382707202');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011842281475', '1659141149980639234', '1355137689412067330');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011573846017', '1659141149980639234', '1355137689563062274');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011552874497', '1659141149980639234', '1355137689596616705');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011565457410', '1659141149980639234', '1355137689625976833');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011561263106', '1659141149980639234', '1355137689659531266');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011594817538', '1659141149980639234', '1355137689709862914');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011582234626', '1659141149980639234', '1355137689739223041');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011615789057', '1659141149980639234', '1355137689772777473');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011393490946', '1659141149980639234', '1355137689802137602');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011397685250', '1659141149980639234', '1355137689835692033');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011406073857', '1659141149980639234', '1355137689865052162');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011410268161', '1659141149980639234', '1355137689894412289');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011259273218', '1659141149980639234', '1355137689953132545');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011418656770', '1659141149980639234', '1355137689986686977');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467521', '1659141149980639234', '1355137690028630018');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011867447298', '1659141149980639234', '1355137690062184450');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011515125766', '1659141149980639234', '1355137690125099010');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011515125764', '1659141149980639234', '1355137690305454082');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011498348547', '1659141149980639234', '1355137690498392065');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011510931458', '1659141149980639234', '1355137690561306625');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011506737153', '1659141149980639234', '1355137690590666753');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011515125762', '1659141149980639234', '1355137690624221186');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011439628291', '1659141149980639234', '1355137690653581313');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011456405506', '1659141149980639234', '1355137690687135745');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011435433986', '1659141149980639234', '1355137690716495874');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011452211202', '1659141149980639234', '1355137690750050305');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467539', '1659141149980639234', '1355137690779410433');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011460599809', '1659141149980639234', '1355137690812964866');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011464794114', '1659141149980639234', '1355137690842324994');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011494154241', '1659141149980639234', '1355137690871685122');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011473182721', '1659141149980639234', '1355137690905239553');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011477377025', '1659141149980639234', '1355137690934599682');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011489959938', '1659141149980639234', '1355137690968154114');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011481571330', '1659141149980639234', '1355137690997514242');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011385102337', '1659141149980639234', '1355137691026874369');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011376713731', '1659141149980639234', '1355137691060428802');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011389296641', '1659141149980639234', '1355137691089788930');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011372519425', '1659141149980639234', '1355137691119149058');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011364130818', '1659141149980639234', '1355137691274338306');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467540', '1659141149980639234', '1355137691328864257');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011326382081', '1659141149980639234', '1355137691362418690');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011330576385', '1659141149980639234', '1355137691391778818');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011338964993', '1659141149980639234', '1355137691425333249');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1451940676920393731', '1659141149980639234', '1355137691492442114');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1451940676920393729', '1659141149980639234', '1355137691521802241');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011871641601', '1659141149980639234', '1355137692171919362');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011875835905', '1659141149980639234', '1355137692234833921');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1451940676920393735', '1659141149980639234', '1355137692264194050');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1451940676920393734', '1659141149980639234', '1355137692297748481');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011850670081', '1659141149980639234', '1355137692423577601');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011846475779', '1659141149980639234', '1355137692452937729');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011645149185', '1659141149980639234', '1355137692989808642');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011867447299', '1659141149980639234', '1356941738931298306');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467536', '1659141149980639234', '1356947408963411970');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467528', '1659141149980639234', '1356947666279768067');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467530', '1659141149980639234', '1356947747137560579');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467532', '1659141149980639234', '1356947799654440963');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467534', '1659141149980639234', '1356954199411564546');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467535', '1659141149980639234', '1356954712752431106');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467531', '1659141149980639234', '1356954754724831234');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467527', '1659141149980639234', '1356954840057946115');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467529', '1659141149980639234', '1356954911889596419');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467533', '1659141149980639234', '1356954988540502018');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011854864385', '1659141149980639234', '1356955554868011010');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011343159298', '1659141149980639234', '1357296673644527618');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011338964994', '1659141149980639234', '1357296806884982786');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467537', '1659141149980639234', '1385489615137792001');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011427045378', '1659141149980639234', '1408317966591832065');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011431239682', '1659141149980639234', '1408318127904763907');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011422851074', '1659141149980639234', '1408318375846850562');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011427045379', '1659141149980639234', '1408318461549064195');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011599011841', '1659141149980639234', '1408318611000504321');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011607400450', '1659141149980639234', '1408318693800259586');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011594817539', '1659141149980639234', '1408318875619143682');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011603206145', '1659141149980639234', '1408318962537705475');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011859058691', '1659141149980639234', '1417815814350180354');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011880030210', '1659141149980639234', '1417819414203863041');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011263467538', '1659141149980639234', '1417822459809632257');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011208941570', '1659141149980639234', '1435191547674423297');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011741618178', '1659141149980639234', '1435192638965215234');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011783561218', '1659141149980639234', '1435196892580868097');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011829698562', '1659141149980639234', '1435198091925319681');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011678703622', '1659141149980639234', '1435200081141100546');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011716452353', '1659141149980639234', '1435200658243776513');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011720646658', '1659141149980639234', '1435200713436622850');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011720646659', '1659141149980639234', '1435201001245569026');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011678703621', '1659141149980639234', '1435201107847999489');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011678703623', '1659141149980639234', '1435201153549135873');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011716452354', '1659141149980639234', '1435201526175297538');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011833892865', '1659141149980639234', '1435202763335593986');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011754201089', '1659141149980639234', '1435204581725761537');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011758395394', '1659141149980639234', '1435204624406999041');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011766784001', '1659141149980639234', '1435204674298245121');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011770978306', '1659141149980639234', '1435204743311323137');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011750006786', '1659141149980639234', '1435204857706770433');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011758395393', '1659141149980639234', '1435204943820025858');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011762589698', '1659141149980639234', '1435205002594807810');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011791949825', '1659141149980639234', '1435205415083634691');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011796144129', '1659141149980639234', '1435205480644800513');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011800338435', '1659141149980639234', '1435205524353642498');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011804532739', '1659141149980639234', '1435205574718844929');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011808727043', '1659141149980639234', '1435205611951681538');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011812921345', '1659141149980639234', '1435205762384588802');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011817115649', '1659141149980639234', '1435205806970040321');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011787755521', '1659141149980639234', '1435205845964484609');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011791949826', '1659141149980639234', '1435205902302375937');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011800338434', '1659141149980639234', '1435205946560671745');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011804532738', '1659141149980639234', '1435206058733137922');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011808727042', '1659141149980639234', '1435206094430859265');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011821309953', '1659141149980639234', '1435206257442484225');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011515125768', '1659141149980639234', '1435211120511422466');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011544485890', '1659141149980639234', '1435211152631402497');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011515125767', '1659141149980639234', '1435211242276261890');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011515125769', '1659141149980639234', '1435211278926090242');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011678703620', '1659141149980639234', '1435211840065245185');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011666120706', '1659141149980639234', '1435220401990275074');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011670315011', '1659141149980639234', '1435220428506664962');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011678703618', '1659141149980639234', '1435220456210042881');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011678703619', '1659141149980639234', '1435220525386698753');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011678703617', '1659141149980639234', '1435220592503951362');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011670315010', '1659141149980639234', '1435220664180412417');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011666120705', '1659141149980639234', '1435220712419102721');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011729035267', '1659141149980639234', '1435847594990460930');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011737423873', '1659141149980639234', '1435847637617172481');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011779366914', '1659141149980639234', '1435847770849239041');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011779366915', '1659141149980639234', '1435847812469317634');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011825504258', '1659141149980639234', '1435847936851402754');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011829698561', '1659141149980639234', '1435848006338437122');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011733229569', '1659141149980639234', '1435848273083588609');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011729035266', '1659141149980639234', '1435849398868336641');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011775172610', '1659141149980639234', '1435849971671851009');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011821309954', '1659141149980639234', '1435850088944590850');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011926167554', '1659141149980639234', '1437951632989405186');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011930361857', '1659141149980639234', '1437953568111247362');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1450764011934556162', '1659141149980639234', '1437958941295689729');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1451941535465062402', '1659141149980639234', '1451941497733103617');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710732013569', '1659141149980639234', '1460139726190161922');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710736207873', '1659141149980639234', '1460139849309761537');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710618767363', '1659141149980639234', '1463842640996913153');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710727819265', '1659141149980639234', '1463843214173720578');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710576824322', '1659141149980639234', '1463878490828926977');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710585212931', '1659141149980639234', '1463878634546753537');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710585212933', '1659141149980639234', '1463878747755212802');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710585212935', '1659141149980639234', '1463878791652798466');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710585212937', '1659141149980639234', '1463878835122565122');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710618767362', '1659141149980639234', '1463878881217966082');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710618767365', '1659141149980639234', '1463878954182078466');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710643933186', '1659141149980639234', '1463879052844691458');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710643933188', '1659141149980639234', '1463879082880102402');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710660710401', '1659141149980639234', '1463879108566020097');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710690070530', '1659141149980639234', '1463879143198388226');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710694264834', '1659141149980639234', '1463879173661618178');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710585212930', '1659141149980639234', '1463879308873396226');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710585212932', '1659141149980639234', '1463879394919542786');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710585212934', '1659141149980639234', '1463879478629462018');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710585212936', '1659141149980639234', '1463879537622347778');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710585212938', '1659141149980639234', '1463879580832067585');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710711042050', '1659141149980639234', '1463879663170449409');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710618767364', '1659141149980639234', '1463879710234734594');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710618767366', '1659141149980639234', '1463879765943480322');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710643933187', '1659141149980639234', '1463879816753278977');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710656516098', '1659141149980639234', '1463879887083368450');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710681681921', '1659141149980639234', '1463879955500855298');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710694264833', '1659141149980639234', '1463879990892392450');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473289315298992129', '1659141149980639234', '1464070319194464258');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473289315294797825', '1659141149980639234', '1464070429324304385');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710547464194', '1659141149980639234', '1470956341090025473');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710740402177', '1659141149980639234', '1470956442353106945');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710744596481', '1659141149980639234', '1470957452752551937');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1473288710572630017', '1659141149980639234', '1473118958558195714');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836123938818', '1659141149980639234', '1585628871522291713');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836128133121', '1659141149980639234', '1585628922290147330');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836132327426', '1659141149980639234', '1585629412948217858');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836136521729', '1659141149980639234', '1585629466865995778');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836123938817', '1659141149980639234', '1585629559920824321');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836128133122', '1659141149980639234', '1585629622420148225');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836132327427', '1659141149980639234', '1585629679424933889');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836140716033', '1659141149980639234', '1585630959421657090');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836140716034', '1659141149980639234', '1585631412335185922');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836144910338', '1659141149980639234', '1585631512004431873');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836149104641', '1659141149980639234', '1585631559332958209');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836136521730', '1659141149980639234', '1585631628060823553');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836140716035', '1659141149980639234', '1585631705575755777');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836144910339', '1659141149980639234', '1585631761494216705');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836115550210', '1659141149980639234', '1585631917249695745');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836119744514', '1659141149980639234', '1585631957233995777');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836111355906', '1659141149980639234', '1585632030361686018');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1585868836115550211', '1659141149980639234', '1585632081976791041');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1687925905206329346', '1659141149980639234', '1687923629142097922');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1687925905051140098', '1659141149980639234', '1687923882176069633');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1687925905126637569', '1659141149980639234', '1687924072777826305');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1687925904971448322', '1659141149980639234', '1687924137638543362');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1687926889034858497', '1659141149980639234', '1687926767290990594');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1687929063529824257', '1659141149980639234', '1687927571729137666');
INSERT INTO `xc_role__authority`(`id`, `role_id`, `authority_id`) VALUES ('1687930082649227266', '1659141149980639234', '1687929621175123969');

-- 基础平台权限
DELETE FROM `xc_authority` WHERE app_id='3';
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137685716885505', '修改密码', NULL, 'B_UPDATE_PASSWORD', '2', NULL, NULL, '1320705742095126529', 'root', 7, '1', '3', '0', '2021-01-29 20:56:16', '2021-11-25 20:04:39', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137685830131713', '我的资料', NULL, 'B_QUERY_USER_INFO', '2', NULL, NULL, '1320705742183206913', 'root', 8, '1', '3', '0', '2021-01-29 20:56:16', '2021-11-25 20:04:35', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137685863686145', '用户取消授权', NULL, 'B_USER_AUTHORIZE_DELETE', '2', NULL, NULL, '1320705742225149953', '1320705744473296897', 0, '1', '3', '0', '2021-01-29 20:56:16', '2023-08-06 04:29:03', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137685897240578', '新增应用', NULL, 'B_APP_ADD', '2', NULL, NULL, '1320705742262898690', '1320705744431353857', 1, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-21 20:23:45', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137685930795010', '编辑应用', NULL, 'B_APP_EDIT', '2', NULL, NULL, '1320705742334201858', '1320705744431353857', 2, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-21 20:23:46', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137685960155138', '删除应用', NULL, 'B_APP_DELETE', '2', NULL, NULL, '1320705742376144897', '1320705744431353857', 3, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-21 20:23:46', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137685993709570', '发布应用', NULL, 'B_APP_RELEASE', '2', NULL, NULL, '1320705742418087938', '1320705744431353857', 4, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-21 20:23:46', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686027264002', '申请授权', NULL, 'B_APP_APPLY_AUTHORIZE', '2', NULL, NULL, '1320705742460030978', '1320705744431353857', 5, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-21 20:23:46', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686090178561', '查询秘钥', NULL, 'B_APP_QUERY_SECRET', '2', NULL, NULL, '1320705742501974017', '1320705744431353857', 6, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-21 20:23:46', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686123732994', '用户组添加', NULL, 'B_APP_GROUP_ADD', '2', NULL, NULL, '1320705742543917058', '1320705744292941826', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:05', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686153093122', '用户组编辑', NULL, 'B_APP_GROUP_EDIT', '2', NULL, NULL, '1320705742585860097', '1320705744292941826', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:05', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686199230465', '用户组删除', NULL, 'B_APP_GROUP_DELETE', '2', NULL, NULL, '1320705742631997441', '1320705744292941826', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:05', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686228590593', '用户编辑', NULL, 'B_APP_USER_EDIT', '2', NULL, NULL, '1320705742682329089', '1320705744162918402', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:05', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686262145026', '角色添加', NULL, 'B_APP_ROLE_ADD', '2', '', NULL, '1320705742724272129', '1320705744204861441', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:06', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686291505153', '角色编辑', NULL, 'B_APP_ROLE_EDIT', '2', NULL, NULL, '1320705742766215169', '1320705744204861441', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:06', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686329253889', '角色删除', NULL, 'B_APP_ROLE_DELETE', '2', NULL, NULL, '1320705742803963905', '1320705744204861441', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:06', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686367002626', '权限添加', NULL, 'B_APP_AUTHORITY_ADD', '2', NULL, NULL, '1320705742887849985', '1320705744250998786', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:06', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686392168450', '权限编辑', NULL, 'B_APP_AUTHORITY_EDIT', '2', NULL, NULL, '1320705742929793025', '1320705744250998786', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:06', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686585106433', '权限删除', NULL, 'B_APP_AUTHORITY_DELETE', '2', NULL, NULL, '1320705742971736065', '1320705744250998786', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:06', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686773850113', '权限导入', NULL, 'B_APP_AUTHORITY_IMPORT', '2', NULL, NULL, '1320705743013679106', '1320705744250998786', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:07', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137686840958977', '权限导出', NULL, 'B_APP_AUTHORITY_EXPORT', '2', '', NULL, '1320705743055622146', '1320705744250998786', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:07', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137687059062785', '信息添加', NULL, 'B_APP_INFO_ADD', '2', NULL, NULL, '1320705743097565186', '1320705744347467777', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:07', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137687289749505', '信息编辑', NULL, 'B_APP_INFO_EDIT', '2', NULL, NULL, '1320705743147896833', '1320705744347467777', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:07', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137687444938754', '信息删除', NULL, 'B_APP_INFO_DELETE', '2', NULL, NULL, '1320705743189839873', '1320705744347467777', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:07', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137687570767873', '信息导入', NULL, 'B_APP_INFO_IMPORT', '2', NULL, NULL, '1320705743231782913', '1320705744347467777', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:08', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137687692402689', '信息导出', NULL, 'B_APP_INFO_EXPORT', '2', NULL, NULL, '1320705743273725953', '1320705744347467777', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:08', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137687734345729', '字典添加', NULL, 'B_APP_DICT_ADD', '2', NULL, NULL, '1320705743315668993', '1320705744389410817', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:08', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137687814037505', '字典编辑', NULL, 'B_APP_DICT_EDIT', '2', NULL, NULL, '1320705743357612034', '1320705744389410817', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:08', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137687847591938', '字典删除', NULL, 'B_APP_DICT_DELETE', '2', NULL, NULL, '1320705743399555073', '1320705744389410817', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:08', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137687881146369', '字典导入', NULL, 'B_APP_DICT_IMPORT', '2', NULL, NULL, '1320705743441498114', '1320705744389410817', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:08', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137687914700802', '字典导出', NULL, 'B_APP_DICT_EXPORT', '2', NULL, NULL, '1320705743483441153', '1320705744389410817', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:09', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137687948255233', '修改用户信息', NULL, 'B_UPDATE_USER_INFO', '2', NULL, NULL, '1320705743525384194', '1320705742183206913', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:09', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137687986003970', '修改手机号', NULL, 'B_UPDATE_PHONE', '2', NULL, NULL, '1320705743567327234', '1320705742183206913', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:09', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688019558402', '修改邮箱', NULL, 'B_UPDATE_EMAIL', '2', NULL, NULL, '1320705743609270274', '1320705742183206913', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:09', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688053112834', '树形字典添加', NULL, 'B_APP_TREE_DICT_ADD', '2', NULL, NULL, '1320705743751876610', '1320705744645263361', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-08-11 23:40:00', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688082472962', '树形字典编辑', NULL, 'B_APP_TREE_DICT_EDIT', '2', NULL, NULL, '1320705743860928513', '1320705744645263361', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-08-11 23:39:55', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688116027393', '树形字典删除', NULL, 'B_APP_TREE_DICT_DELETE', '2', NULL, NULL, '1320705743902871553', '1320705744645263361', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-08-11 23:39:50', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688145387522', '树形字典导入', NULL, 'B_APP_TREE_DICT_IMPORT', '2', NULL, NULL, '1320705743944814593', '1320705744645263361', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-08-11 23:40:13', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688183136257', '树形字典导出', NULL, 'B_APP_TREE_DICT_EXPORT', '2', NULL, NULL, '1320705743990951937', '1320705744645263361', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-08-11 23:40:08', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688212496385', '应用审核', NULL, 'B_APP_AUDIT', '2', NULL, NULL, '1320705744032894978', '1417815814249517057', 0, '1', '3', '0', '2021-01-29 20:56:16', '2023-08-06 04:45:59', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688246050818', '审核权限', NULL, 'B_AUTHORITY_AUDIT', '2', NULL, NULL, '1320705744074838017', '1417819414094811137', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-21 20:12:35', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688308965377', '基础管理', NULL, 'home-basicManage', '0', '/home/basicManage', NULL, '1320705744120975362', 'root', 2, '1', '3', '0', '2021-01-29 20:56:16', '2021-09-07 19:57:33', 55);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688338325506', '用户管理', NULL, 'home-basicManage-userManage', '0', '/home/basicManage/userManage', NULL, '1320705744162918402', '1320705744120975362', 2, '1', '3', '0', '2021-01-29 20:56:16', '2021-08-11 23:31:53', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688371879938', '角色管理', NULL, 'home-basicManage-roleManage', '0', '/home/basicManage/roleManage', NULL, '1320705744204861441', '1320705744120975362', 3, '1', '3', '0', '2021-01-29 20:56:16', '2021-08-11 23:31:54', 3);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688401240066', '权限管理', NULL, 'home-basicManage-authorityManage', '0', '/home/basicManage/authorityManage', NULL, '1320705744250998786', '1320705744120975362', 4, '1', '3', '0', '2021-01-29 20:56:16', '2021-08-11 23:31:58', 5);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688434794498', '用户组管理', NULL, 'home-basicManage-groupManage', '0', '/home/basicManage/groupManage', NULL, '1320705744292941826', '1320705744120975362', 1, '1', '3', '0', '2021-01-29 20:56:16', '2021-08-11 23:31:36', 3);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688468348929', '信息管理', NULL, 'home-basicManage-infoManage', '0', '/home/basicManage/infoManage', NULL, '1320705744347467777', '1320705744120975362', 6, '1', '3', '0', '2021-01-29 20:56:16', '2021-12-21 10:32:38', 6);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688518680578', '字典管理', NULL, 'home-basicManage-dictManage', '0', '/home/basicManage/dictManage', NULL, '1320705744389410817', '1320705744120975362', 7, '1', '3', '0', '2021-01-29 20:56:16', '2021-12-21 10:32:36', 4);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688552235009', '应用管理', NULL, 'home-appManage', '0', '/home/appManage', NULL, '1320705744431353857', 'root', 1, '1', '3', '0', '2021-01-29 20:56:16', '2021-09-07 19:57:07', 50);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688581595137', '授权管理', NULL, 'home-authorizeManage', '0', '/home/authorizeManage', NULL, '1320705744473296897', 'root', 5, '1', '3', '0', '2021-01-29 20:56:16', '2021-11-25 20:04:46', 44);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688623538178', '审核管理', NULL, 'home-auditManage', '0', '/home/auditManage', NULL, '1320705744519434241', 'root', 6, '1', '3', '0', '2021-01-29 20:56:16', '2021-11-25 20:04:45', 52);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688657092610', '应用审核', NULL, 'home-auditManage-app', '0', '/home/auditManage/app', NULL, '1320705744561377281', '1320705744519434241', 1, '1', '3', '0', '2021-01-29 20:56:16', '2023-08-06 04:33:48', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688690647042', '权限审核', NULL, 'home-auditManage-authority', '0', '/home/auditManage/authority', NULL, '1320705744603320321', '1320705744519434241', 2, '1', '3', '0', '2021-01-29 20:56:16', '2023-08-06 04:33:38', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688732590081', '树形字典', NULL, 'home-basicManage-treeDictManage', '0', '/home/basicManage/treeDictManage', NULL, '1320705744645263361', '1320705744120975362', 8, '1', '3', '0', '2021-01-29 20:56:16', '2021-12-21 10:32:31', 9);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688766144513', '获取基础信息', NULL, 'getMyAppUser', '1', NULL, NULL, '1320705997649874946', 'root', 9, '1', '3', '0', '2021-01-29 20:56:16', '2021-11-25 20:04:34', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688799698945', '获取当前用户信息', NULL, 'getCurrentUserInfo', '1', NULL, NULL, '1320711300869132289', 'root', 10, '1', '3', '0', '2021-01-29 20:56:16', '2021-11-25 20:04:33', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688829059073', '退出登录', NULL, 'deleteMyAppToken', '1', NULL, NULL, '1320711385610850305', 'root', 11, '1', '3', '0', '2021-01-29 20:56:16', '2021-11-25 20:04:33', 10);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688862613506', '修改密码', NULL, 'updateMyAppUserPassword', '1', NULL, NULL, '1320711505689579521', '1320705742095126529', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:13', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688908750849', '修改当前用户信息', NULL, 'updateCurrentUserInfo', '1', NULL, NULL, '1320711689840496641', '1320705743525384194', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:13', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137688938110978', '获取应用集合', NULL, 'getAppList', '1', NULL, NULL, '1320711831800909826', '1320705744120975362', 9, '1', '3', '0', '2021-01-29 20:56:16', '2021-12-21 10:32:31', 20);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689030385666', '获取应用秘钥', NULL, 'getAppSecret', '1', NULL, NULL, '1320712319267115009', '1320705742501974017', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:14', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689101688834', '更新密钥', NULL, 'updateAppSecret', '1', NULL, NULL, '1320712450578190338', '1320705742501974017', 0, '1', '3', '0', '2021-01-29 20:56:16', '2021-07-01 12:14:14', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689131048961', '审核接口', NULL, 'updateManageAuditApp', '1', NULL, NULL, '1320712632250273793', '1320705744561377281', 5, '1', '3', '0', '2021-01-29 20:56:16', '2023-08-06 04:39:55', 9);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689164603394', '审核应用列表分页', NULL, 'getManageAuditAppPage', '1', NULL, NULL, '1320712821212057602', '1320705744561377281', 3, '1', '3', '0', '2021-01-29 20:56:17', '2023-08-06 04:39:31', 8);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689193963522', '删除应用', NULL, 'deleteCurrentUserApp', '1', NULL, NULL, '1320713299241078786', '1320705742376144897', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:14', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689223323650', '用户取消授权', NULL, 'deleteCurrentUserAuthorize', '1', NULL, NULL, '1320713434457051138', '1320705742225149953', 0, '1', '3', '0', '2021-01-29 20:56:17', '2023-08-06 04:27:50', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689290432513', '修改应用', NULL, 'updateCurrentUserApp', '1', NULL, NULL, '1320714210285850625', '1320705742334201858', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:15', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689319792642', '获取当前用户的应用集合', NULL, 'getCurrentUserAppList', '1', NULL, NULL, '1320714447104643074', 'root', 12, '1', '3', '0', '2021-01-29 20:56:17', '2021-11-25 20:04:33', 10);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689349152769', '创建应用', NULL, 'createCurrentUserApp', '1', NULL, NULL, '1320714995245649922', '1320705742262898690', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:15', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689382707202', '应用分页', NULL, 'getCurrentUserAppPage', '1', NULL, NULL, '1320715428664053761', '1320705744431353857', 9, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-21 20:23:56', 3);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689412067330', '用户授权分页', NULL, 'getCurrentUserAuthorizePage', '1', NULL, NULL, '1320715521412698114', '1320705744473296897', 0, '1', '3', '0', '2021-01-29 20:56:17', '2023-08-06 04:27:25', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689563062274', '批量创建树形字典', NULL, 'createAppTreeDictList', '1', NULL, NULL, '1320715662018351105', '1320705743944814593', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-08-11 23:28:05', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689596616705', '创建树形字典', NULL, 'createAppTreeDict', '1', NULL, NULL, '1320715726228951042', '1320705743751876610', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-08-11 23:27:41', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689625976833', '删除树形字典', NULL, 'deleteAppTreeDict', '1', NULL, NULL, '1320715809615908865', '1320705743902871553', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-08-11 23:27:46', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689659531266', '修改树形字典', NULL, 'updateAppTreeDict', '1', NULL, NULL, '1320715900519059458', '1320705743860928513', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-08-11 23:27:51', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689709862914', '获取树形字典分页数据', NULL, 'getAppTreeDictPage', '1', NULL, NULL, '1320715959969124353', '1320705744645263361', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-08-11 23:27:56', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689739223041', '获取树形字典集合', NULL, 'getAppTreeDictList', '1', NULL, NULL, '1320716068740009986', '1320705743990951937', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-08-11 23:28:01', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689772777473', '获取权限集合', NULL, 'getAppAuthorityList', '1', NULL, NULL, '1320716329952874498', '1320705744120975362', 10, '1', '3', '0', '2021-01-29 20:56:17', '2021-12-21 10:32:27', 12);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689802137602', '创建权限', NULL, 'createAppAuthority', '1', NULL, NULL, '1320716457665236994', '1320705742887849985', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:16', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689835692033', '修改权限', NULL, 'updateAppAuthority', '1', NULL, NULL, '1320716614855168001', '1320705742929793025', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:17', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689865052162', '删除权限', NULL, 'deleteAppAuthority', '1', NULL, NULL, '1320716701857615874', '1320705742971736065', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:17', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689894412289', '批量创建权限', NULL, 'createAppAuthorityList', '1', NULL, NULL, '1320716950386905090', '1320705743013679106', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:17', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689953132545', '更新应用授权', NULL, 'updateAppAuthorize', '1', NULL, NULL, '1320717112324788226', '1320705742460030978', 0, '1', '3', '0', '2021-01-29 20:56:17', '2023-08-06 04:25:49', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137689986686977', '权限分页', NULL, 'getAppAuthorityPage', '1', NULL, NULL, '1320717240490135554', '1320705744250998786', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:17', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690028630018', '应用授权权限分页', NULL, 'getAppAuthorizeAuthorityPage', '1', NULL, NULL, '1320717590769045505', '1320705742460030978', 0, '1', '3', '0', '2021-01-29 20:56:17', '2023-08-06 04:25:12', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690062184450', '审核应用权限', NULL, 'updateAuditAppAuthorize', '1', NULL, NULL, '1320717700823388162', '1320705744603320321', 3, '1', '3', '0', '2021-01-29 20:56:17', '2023-08-06 04:51:33', 8);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690125099010', '字典分页', NULL, 'getAppDictPage', '1', NULL, NULL, '1320717910198849537', '1320705744389410817', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:18', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690305454082', '获取字典集合', NULL, 'getAppDictList', '1', NULL, NULL, '1320718148020080642', '1320705743483441153', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:18', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690498392065', '创建字典', NULL, 'createAppDict', '1', NULL, NULL, '1320718246980489217', '1320705743315668993', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:19', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690561306625', '删除字典', NULL, 'deleteAppDict', '1', NULL, NULL, '1320718498936524802', '1320705743399555073', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:19', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690590666753', '修改字典', NULL, 'updateAppDict', '1', NULL, NULL, '1320718568339673089', '1320705743357612034', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:19', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690624221186', '批量创建字典', NULL, 'createAppDictList', '1', NULL, NULL, '1320719464880541698', '1320705743441498114', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:19', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690653581313', '修改用户组', NULL, 'updateAppGroup', '1', NULL, NULL, '1320719852870438913', '1320705742585860097', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:19', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690687135745', '查询用户组', NULL, 'getAppGroup', '1', NULL, NULL, '1320720280853024770', '1320705744292941826', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:20', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690716495874', '创建用户组', NULL, 'createAppGroup', '1', NULL, NULL, '1320720409622351873', '1320705742543917058', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:20', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690750050305', '删除用户组', NULL, 'deleteAppGroup', '1', NULL, NULL, '1320728863791517698', '1320705742631997441', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:20', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690779410433', '获取用户组集合', NULL, 'getAppGroupList', '1', NULL, NULL, '1320729047791439874', '1320705742682329089', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:20', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690812964866', '用户组分页', NULL, 'getAppGroupPage', '1', NULL, NULL, '1320729211935526913', '1320705744292941826', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:20', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690842324994', '创建信息', NULL, 'createAppInfo', '1', NULL, NULL, '1320729302008205314', '1320705743097565186', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:21', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690871685122', '信息分页', NULL, 'getAppInfoPage', '1', NULL, NULL, '1320729370656378881', '1320705744347467777', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:21', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690905239553', '修改信息', NULL, 'updateAppInfo', '1', NULL, NULL, '1320729432186818562', '1320705743147896833', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:21', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690934599682', '删除信息', NULL, 'deleteAppInfo', '1', NULL, NULL, '1320729517771591682', '1320705743189839873', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:21', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690968154114', '获取的信息集合', NULL, 'getAppInfoList', '1', NULL, NULL, '1320729646712885250', '1320705743273725953', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:22', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137690997514242', '创建信息集合', NULL, 'createAppInfoList', '1', NULL, NULL, '1320729724966014978', '1320705743231782913', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:22', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691026874369', '角色分页', NULL, 'getAppRolePage', '1', NULL, NULL, '1320729850774163457', '1320705744204861441', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:22', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691060428802', '删除角色', NULL, 'deleteAppRole', '1', NULL, NULL, '1320729912212328450', '1320705742803963905', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:22', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691089788930', '查询角色', NULL, 'getAppRole', '1', NULL, NULL, '1320729990729699329', '1320705744204861441', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:22', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691119149058', '修改角色', NULL, 'updateAppRole', '1', NULL, NULL, '1320730040285401089', '1320705742766215169', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:22', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691274338306', '创建角色', NULL, 'createAppRole', '1', NULL, NULL, '1320730090180841473', '1320705742724272129', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:23', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691328864257', '获取角色集合', NULL, 'getAppRoleList', '1', NULL, NULL, '1320730183919341570', '1320705742682329089', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:23', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691362418690', '获取用户的组和角色', NULL, 'getUserGroupRole', '1', NULL, NULL, '1320730345970470913', '1320705742682329089', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:23', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691391778818', '创建用户的用户，组，角色关联', NULL, 'updateUserGroupRole', '1', NULL, NULL, '1320730468704194561', '1320705742682329089', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:23', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691425333249', '用户分页', NULL, 'getAppUserPage', '1', NULL, NULL, '1320730517093879809', '1320705744162918402', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:24', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691492442114', '修改用户邮箱', NULL, 'updateMyAppUserMail', '1', NULL, NULL, '1324591286390767618', '1320705743609270274', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:24', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691521802241', '修改用户手机号', NULL, 'updateMyAppUserPhone', '1', NULL, NULL, '1324591403671896065', '1320705743567327234', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:24', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691618271233', '获取信息集合', NULL, 'getOpenInfoList', '4', NULL, NULL, '1324707166672068609', 'root', 13, '1', '3', '0', '2021-01-29 20:56:17', '2021-11-25 20:04:33', 11);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691651825666', '获取字典集合', NULL, 'getOpenDictList', '4', NULL, NULL, '1324707267868041217', 'root', 14, '1', '3', '0', '2021-01-29 20:56:17', '2021-11-25 20:04:33', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691681185793', '获取菜单集合', NULL, 'getOpenAuthorityList', '4', NULL, NULL, '1324707327821422593', 'root', 15, '1', '3', '0', '2021-01-29 20:56:17', '2021-11-25 20:04:33', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691714740226', '获取token', NULL, 'getOpenToken', '4', NULL, NULL, '1324707415293632513', 'root', 16, '1', '3', '0', '2021-01-29 20:56:17', '2021-11-25 20:04:33', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691744100354', '删除token', NULL, 'deleteOpenToken', '4', NULL, NULL, '1324707472424247298', 'root', 17, '1', '3', '0', '2021-01-29 20:56:17', '2021-11-25 20:04:33', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691777654785', '刷新token', NULL, 'updateOpenToken', '4', NULL, NULL, '1324707530397917185', 'root', 18, '1', '3', '0', '2021-01-29 20:56:17', '2021-11-25 20:04:33', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691807014914', '获取用户集合', NULL, 'getOpenUserList', '4', NULL, NULL, '1324707598572134401', 'root', 19, '1', '3', '0', '2021-01-29 20:56:17', '2021-11-25 20:04:33', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691848957954', '获取用户', NULL, 'getOpenUser', '5', NULL, NULL, '1324707677135642625', 'root', 20, '1', '3', '0', '2021-01-29 20:56:17', '2021-11-25 20:04:33', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691882512385', '修改密码', NULL, 'updateOpenUserPassword', '5', NULL, NULL, '1324707735545520129', 'root', 21, '1', '3', '0', '2021-01-29 20:56:17', '2021-11-25 20:04:33', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137691911872513', '获取树形字典集合', NULL, 'getOpenTreeDictList', '4', NULL, NULL, '1324707894719356930', 'root', 22, '1', '3', '0', '2021-01-29 20:56:17', '2021-11-25 20:04:33', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137692041895937', '修改用户邮箱', NULL, 'updateOpenUserMail', '5', NULL, NULL, '1324708245790990337', 'root', 25, '1', '3', '0', '2021-01-29 20:56:17', '2021-09-07 19:58:04', 6);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137692075450369', '修改用户手机号', NULL, 'updateOpenUserPhone', '5', NULL, NULL, '1324708394361626625', 'root', 26, '1', '3', '0', '2021-01-29 20:56:17', '2021-09-07 19:58:04', 6);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137692109004801', '获取应用基础信息', NULL, 'getOpenApp', '4', NULL, NULL, '1327465461119901697', 'root', 27, '1', '3', '0', '2021-01-29 20:56:17', '2021-09-07 19:58:03', 6);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137692171919362', '已审核', NULL, 'home-auditManage-authority-list-adopt', '0', '/home/auditManage/authority/list/adopt', NULL, '1338017042656763905', '1320705744603320321', 2, '1', '3', '0', '2021-01-29 20:56:17', '2023-08-06 04:51:35', 14);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137692234833921', '审核应用权限分页', NULL, 'getAuditAppAuthorizePage', '1', NULL, NULL, '1338038384171880449', '1320705744603320321', 4, '1', '3', '0', '2021-01-29 20:56:17', '2023-08-06 04:40:25', 6);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137692264194050', '注销账户', NULL, 'B_DELETE_USER', '2', NULL, NULL, '1339956068322131969', '1320705742183206913', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:28', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137692297748481', '删除用户', NULL, 'deleteMyAppUser', '1', NULL, NULL, '1339956214447489025', '1339956068322131969', 0, '1', '3', '0', '2021-01-29 20:56:17', '2021-07-01 12:14:28', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137692327108609', '删除用户', NULL, 'deleteOpenUser', '5', NULL, NULL, '1339956620447727617', 'root', 28, '1', '3', '0', '2021-01-29 20:56:17', '2021-11-25 20:04:33', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137692356468738', '获取已删除用户Id集合', NULL, 'getOpenDeletedUserList', '4', NULL, NULL, '1339956903307395074', 'root', 29, '1', '3', '0', '2021-01-29 20:56:17', '2021-11-25 20:04:33', 7);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137692423577601', '已审核', NULL, 'home-auditManage-app-list-adopt', '0', '/home/auditManage/app/list/adopt', NULL, '1343443669566816258', '1320705744561377281', 2, '1', '3', '0', '2021-01-29 20:56:17', '2023-08-06 04:37:32', 9);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137692452937729', '拒绝审核', NULL, 'B_APP_AUDIT_REJECT', '2', '', NULL, '1343446221465255937', '1343443669566816258', 2, '1', '3', '0', '2021-01-29 20:56:17', '2023-08-06 04:45:16', 22);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1355137692989808642', '接口管理', NULL, 'home-apiManage', '0', '/home/apiManage', NULL, '1354713328152698881', 'root', 4, '1', '3', '0', '2021-01-29 20:56:17', '2022-01-13 20:55:07', 15);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1356941738931298306', '拒绝审核', NULL, 'B_AUTHORITY_AUDIT_REJECT', '2', NULL, NULL, '1356941738931298305', '1338017042656763905', 0, '1', '3', '0', '2021-02-03 20:24:56', '2023-08-06 04:52:17', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1356947408963411970', '版本管理', NULL, 'B_APP_VERSION', '2', '', NULL, '1356947408963411969', '1320705744431353857', 8, '1', '3', '0', '2021-02-03 20:47:27', '2021-07-22 00:41:17', 4);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1356947666279768067', '版本添加', NULL, 'B_APP_VERSION_ADD', '2', NULL, NULL, '1356947666279768066', '1356947408963411969', 0, '1', '3', '0', '2021-02-03 20:48:29', '2021-08-11 23:41:36', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1356947747137560579', '版本编辑', NULL, 'B_APP_VERSION_EDIT', '2', NULL, NULL, '1356947747137560578', '1356947408963411969', 0, '1', '3', '0', '2021-02-03 20:48:48', '2021-08-11 23:41:41', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1356947799654440963', '版本文档', NULL, 'B_APP_VERSION_DOC', '2', NULL, NULL, '1356947799654440962', '1356947408963411969', 0, '1', '3', '0', '2021-02-03 20:49:00', '2021-08-11 23:41:32', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1356954199411564546', '版本删除', NULL, 'B_APP_VERSION_DELETE', '2', NULL, NULL, '1356954199411564545', '1356947408963411969', 0, '1', '3', '0', '2021-02-03 21:14:26', '2021-08-11 23:41:27', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1356954712752431106', '应用版本分页', NULL, 'getAppVersionPage', '1', NULL, NULL, '1356954712752431105', '1356947408963411969', 0, '1', '3', '0', '2021-02-03 21:16:29', '2021-07-01 12:14:33', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1356954754724831234', '查询应用版本', NULL, 'getAppVersion', '1', NULL, NULL, '1356954754724831233', '1356947799654440962', 0, '1', '3', '0', '2021-02-03 21:16:39', '2021-07-01 12:14:33', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1356954840057946115', '创建应用版本', NULL, 'createAppVersion', '1', NULL, NULL, '1356954840057946114', '1356947666279768066', 0, '1', '3', '0', '2021-02-03 21:16:59', '2021-07-01 12:14:33', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1356954911889596419', '修改应用版本', NULL, 'updateAppVersion', '1', NULL, NULL, '1356954911889596418', '1356947747137560578', 0, '1', '3', '0', '2021-02-03 21:17:16', '2021-07-01 12:14:33', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1356954988540502018', '删除应用版本', NULL, 'deleteAppVersion', '1', NULL, NULL, '1356954988540502017', '1356954199411564545', 0, '1', '3', '0', '2021-02-03 21:17:34', '2021-07-01 12:14:33', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1356955554868011010', '获取应用信息', NULL, 'getManageAuditApp', '1', NULL, NULL, '1356955554868011009', '1320705744561377281', 4, '1', '3', '0', '2021-02-03 21:19:49', '2023-08-06 04:40:04', 8);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1357296673644527618', '用户详情', NULL, 'B_APP_USER_DETAILS', '2', NULL, NULL, '1357296673644527617', '1320705744162918402', 0, '1', '3', '0', '2021-02-04 19:55:19', '2021-07-01 12:14:33', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1357296806884982786', '获取应用的用户', NULL, 'getAppUser', '1', NULL, NULL, '1357296806884982785', '1357296673644527617', 0, '1', '3', '0', '2021-02-04 19:55:50', '2021-07-01 12:14:34', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1385489615137792001', '获取用户的用户组集合', NULL, 'getMyAppUserGroupList', '1', NULL, NULL, '1385489615125209090', '1320705744431353857', 10, '1', '3', '0', '2021-04-23 15:04:03', '2021-07-21 20:23:55', 4);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1385496599668973570', '获取开放用户的用户组集合', NULL, 'getOpenUserGroupList', '5', '', NULL, '1385496599660584961', 'root', 30, '1', '3', '0', '2021-04-23 15:31:48', '2021-11-25 20:04:33', 9);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1403623305197449219', '获取应用分页', NULL, 'getOpenAppPage', '4', NULL, NULL, '1403623305197449218', 'root', 31, '1', '3', '0', '2021-06-12 16:00:48', '2021-11-25 20:04:33', 8);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1403623400617865218', '获取应用用户权限', NULL, 'getOpenUserAuthorityList', '4', NULL, NULL, '1403623400613670914', 'root', 32, '1', '3', '0', '2021-06-12 16:01:11', '2021-11-25 20:04:33', 8);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1403623488299790338', '获取版本分页', NULL, 'getOpenVersionPage', '4', NULL, NULL, '1403623488299790337', 'root', 33, '1', '3', '0', '2021-06-12 16:01:32', '2021-11-25 20:04:34', 8);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1403623552652996611', '获取版本信息', NULL, 'getOpenAppVersion', '4', NULL, NULL, '1403623552652996610', 'root', 34, '1', '3', '0', '2021-06-12 16:01:47', '2021-11-25 20:04:34', 8);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1403689515578667009', '获取最新版本', NULL, 'getOpenNewestVersion', '4', NULL, NULL, '1403689515574472706', 'root', 35, '1', '3', '0', '2021-06-12 20:23:54', '2021-11-25 20:04:34', 8);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1403689748563865602', '获取版本集合', NULL, 'getOpenVersionList', '4', NULL, NULL, '1403689748559671298', 'root', 36, '1', '3', '0', '2021-06-12 20:24:50', '2021-11-25 20:04:34', 8);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1403692168287203331', '创建消息', NULL, 'createOpenMessage', '4', NULL, NULL, '1403692168287203330', 'root', 37, '1', '3', '0', '2021-06-12 20:34:26', '2021-11-25 23:10:16', 9);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1403692353239232515', '获取外网ip地址', NULL, 'getOpenOuterIp', '4', NULL, NULL, '1403692353239232514', 'root', 38, '1', '3', '0', '2021-06-12 20:35:11', '2021-11-25 20:04:34', 8);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1408317966591832065', '权限上移', NULL, 'B_APP_AUTHORITY_UP', '2', NULL, NULL, '1408317966587637761', '1320705744250998786', 0, '1', '3', '0', '2021-06-25 14:55:43', '2021-07-01 12:14:55', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1408318127904763907', '权限下移', NULL, 'B_APP_AUTHORITY_DOWN', '2', NULL, NULL, '1408318127904763906', '1320705744250998786', 0, '1', '3', '0', '2021-06-25 14:56:21', '2021-07-01 12:14:55', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1408318375846850562', '上移权限', NULL, 'updateAppAuthorityUp', '1', NULL, NULL, '1408318375846850561', '1408317966587637761', 0, '1', '3', '0', '2021-06-25 14:57:20', '2021-07-01 12:14:56', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1408318461549064195', '下移权限', NULL, 'updateAppAuthorityDown', '1', NULL, NULL, '1408318461549064194', '1408318127904763906', 0, '1', '3', '0', '2021-06-25 14:57:41', '2021-07-01 12:14:56', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1408318611000504321', '树形字典上移', NULL, 'B_APP_TREE_DICT_UP', '2', NULL, NULL, '1408318610996310018', '1320705744645263361', 0, '1', '3', '0', '2021-06-25 14:58:16', '2021-08-11 23:39:38', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1408318693800259586', '树形字典下移', NULL, 'B_APP_TREE_DICT_DOWN', '2', NULL, NULL, '1408318693800259585', '1320705744645263361', 0, '1', '3', '0', '2021-06-25 14:58:36', '2021-08-11 23:39:44', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1408318875619143682', '上移树形字典', NULL, 'updateAppTreeDictUp', '1', NULL, NULL, '1408318875619143681', '1408318610996310018', 0, '1', '3', '0', '2021-06-25 14:59:19', '2021-08-11 23:27:24', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1408318962537705475', '下移树形字典', NULL, 'updateAppTreeDictDown', '1', NULL, NULL, '1408318962537705474', '1408318693800259585', 0, '1', '3', '0', '2021-06-25 14:59:40', '2021-08-11 23:27:31', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1417815814350180354', '审核', NULL, 'home-auditManage-app-list-audit', '0', '/home/auditManage/app/list/audit', NULL, '1417815814249517057', '1320705744561377281', 1, '1', '3', '0', '2021-07-21 19:56:48', '2023-08-06 04:37:28', 10);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1417819414203863041', '审核', NULL, 'home-auditManage-authority-list-audit', '0', '/home/auditManage/authority/list/audit', NULL, '1417819414094811137', '1320705744603320321', 1, '1', '3', '0', '2021-07-21 20:11:06', '2023-08-06 04:51:35', 16);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1417822459809632257', '取消发布', NULL, 'B_APP_CANCEL_RELEASE', '2', NULL, NULL, '1417822459750912002', '1320705744431353857', 7, '1', '3', '0', '2021-07-21 20:23:13', '2021-07-21 20:23:58', 6);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1425360427465945089', '获取栏目信息', NULL, 'getOpenColumn', '4', NULL, NULL, '1425360427453362177', 'root', 39, '1', '3', '0', '2021-08-11 15:36:22', '2021-11-25 20:04:34', 5);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1425660594266140673', '创建信息集合', NULL, 'createOpenInfoList', '4', NULL, NULL, '1425660594261946369', 'root', 40, '1', '3', '0', '2021-08-12 11:29:07', '2021-11-25 20:04:34', 3);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435191547674423297', '网站基础', NULL, 'home-websiteManage', '0', '/home/websiteManage', NULL, '1435191547582148609', 'root', 3, '1', '3', '0', '2021-09-07 18:41:45', '2021-09-07 19:58:31', 45);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435192638965215234', '页面管理', NULL, 'home-websiteManage-pageManage', '0', '/home/websiteManage/pageManage', NULL, '1435192638889717761', '1435191547582148609', 1, '1', '3', '0', '2021-09-07 18:46:05', '2021-09-07 19:04:48', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435196892580868097', '数据类型', NULL, 'home-websiteManage-dataTypeManage', '0', '/home/websiteManage/dataTypeManage', NULL, '1435196892522147842', '1435191547582148609', 2, '1', '3', '0', '2021-09-07 19:02:59', '2021-09-07 19:11:04', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435198091925319681', '栏目管理', NULL, 'home-websiteManage-columnManage', '0', '/home/websiteManage/columnManage', NULL, '1435198091858210817', '1435191547582148609', 3, '1', '3', '0', '2021-09-07 19:07:45', '2021-09-07 19:07:45', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435200081141100546', '页面添加', NULL, 'B_APP_PAGE_ADD', '2', '', '', '1435200081069797378', '1435192638889717761', 1, '1', '3', '0', '2021-09-07 19:15:39', '2021-09-07 19:15:39', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435200658243776513', '页面编辑', NULL, 'B_APP_PAGE_EDIT', '2', NULL, NULL, '1435200658168279042', '1435192638889717761', 2, '1', '3', '0', '2021-09-07 19:17:57', '2021-09-07 19:20:19', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435200713436622850', '页面删除', NULL, 'B_APP_PAGE_DELETE', '2', NULL, NULL, '1435200713361125378', '1435192638889717761', 3, '1', '3', '0', '2021-09-07 19:18:10', '2021-09-07 19:18:10', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435201001245569026', '页面分页', NULL, 'getAppPagePage', '1', NULL, NULL, '1435201001174265857', '1435192638889717761', 4, '1', '3', '0', '2021-09-07 19:19:19', '2021-09-07 19:20:56', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435201107847999489', '创建页面', NULL, 'createAppPage', '1', NULL, NULL, '1435201107776696321', '1435200081069797378', 1, '1', '3', '0', '2021-09-07 19:19:44', '2021-09-07 19:19:44', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435201153549135873', '修改页面', NULL, 'updateAppPage', '1', NULL, NULL, '1435201153473638401', '1435200658168279042', 5, '1', '3', '0', '2021-09-07 19:19:55', '2021-09-07 19:21:11', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435201526175297538', '页面删除', NULL, 'deleteAppPage', '1', NULL, NULL, '1435201526103994370', '1435200713361125378', 1, '1', '3', '0', '2021-09-07 19:21:24', '2021-09-07 19:21:24', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435202763335593986', '获取栏目集合', NULL, 'getAppColumnList', '1', '', NULL, '1435202763260096514', '1435191547582148609', 4, '1', '3', '0', '2021-09-07 19:26:19', '2021-09-07 19:26:19', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435204581725761537', '数据类型添加', NULL, 'B_APP_DATA_TYPE_ADD', '2', NULL, NULL, '1435204581650264065', '1435196892522147842', 1, '1', '3', '0', '2021-09-07 19:33:32', '2021-09-07 19:33:32', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435204624406999041', '数据类型编辑', NULL, 'B_APP_DATA_TYPE_EDIT', '2', NULL, NULL, '1435204624335695873', '1435196892522147842', 2, '1', '3', '0', '2021-09-07 19:33:43', '2021-09-07 19:33:43', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435204674298245121', '数据类型删除', NULL, 'B_APP_DATA_TYPE_DELETE', '2', NULL, NULL, '1435204674231136257', '1435196892522147842', 3, '1', '3', '0', '2021-09-07 19:33:55', '2021-09-07 19:33:55', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435204743311323137', '数据类型分页', NULL, 'getAppDataTypePage', '2', NULL, NULL, '1435204743235825665', '1435196892522147842', 4, '1', '3', '0', '2021-09-07 19:34:11', '2021-09-07 19:34:11', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435204857706770433', '创建数据类型', NULL, 'createAppDataType', '1', '', NULL, '1435204857627078658', '1435204581650264065', 1, '1', '3', '0', '2021-09-07 19:34:38', '2021-09-07 19:35:49', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435204943820025858', '修改数据类型', NULL, 'updateAppDataType', '1', '', NULL, '1435204943752916993', '1435204624335695873', 1, '1', '3', '0', '2021-09-07 19:34:59', '2021-09-07 19:36:13', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435205002594807810', '删除数据类型', NULL, 'deleteAppDataType', '1', '', NULL, '1435205002536087553', '1435204674231136257', 5, '1', '3', '0', '2021-09-07 19:35:13', '2021-09-07 19:35:37', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435205415083634691', '栏目添加', NULL, 'B_APP_COLUMN_ADD', '2', NULL, NULL, '1435205415083634690', '1435198091858210817', 1, '1', '3', '0', '2021-09-07 19:36:51', '2021-09-07 19:36:51', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435205480644800513', '栏目编辑', NULL, 'B_APP_COLUMN_EDIT', '2', NULL, NULL, '1435205480569303042', '1435198091858210817', 2, '1', '3', '0', '2021-09-07 19:37:07', '2021-09-07 19:37:07', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435205524353642498', '栏目删除', NULL, 'B_APP_COLUMN_DELETE', '2', NULL, NULL, '1435205524282339330', '1435198091858210817', 3, '1', '3', '0', '2021-09-07 19:37:17', '2021-09-07 19:37:17', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435205574718844929', '栏目上移', NULL, 'B_APP_COLUMN_UP', '2', NULL, NULL, '1435205574660124674', '1435198091858210817', 4, '1', '3', '0', '2021-09-07 19:37:29', '2021-09-07 19:37:29', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435205611951681538', '栏目下移', NULL, 'B_APP_COLUMN_DOWN', '2', NULL, NULL, '1435205611876184066', '1435198091858210817', 5, '1', '3', '0', '2021-09-07 19:37:38', '2021-09-07 19:37:38', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435205762384588802', '栏目分页', NULL, 'getAppColumnPage', '1', NULL, NULL, '1435205762309091329', '1435198091858210817', 6, '1', '3', '0', '2021-09-07 19:38:14', '2021-09-07 19:38:14', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435205806970040321', '查询栏目', NULL, 'getAppColumn', '1', NULL, NULL, '1435205806907125761', '1435198091858210817', 7, '1', '3', '0', '2021-09-07 19:38:25', '2021-09-07 19:38:25', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435205845964484609', '创建栏目', NULL, 'createAppColumn', '1', NULL, NULL, '1435205845893181441', '1435205415083634690', 1, '1', '3', '0', '2021-09-07 19:38:34', '2021-09-07 19:38:34', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435205902302375937', '修改栏目', NULL, 'updateAppColumn', '1', NULL, NULL, '1435205902214295554', '1435205480569303042', 1, '1', '3', '0', '2021-09-07 19:38:47', '2021-09-07 19:38:47', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435205946560671745', '删除栏目', NULL, 'deleteAppColumn', '1', NULL, NULL, '1435205946497757185', '1435205524282339330', 1, '1', '3', '0', '2021-09-07 19:38:58', '2021-09-07 19:38:58', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435206058733137922', '上移栏目', NULL, 'updateAppColumnUp', '1', NULL, NULL, '1435206058666029058', '1435205574660124674', 1, '1', '3', '0', '2021-09-07 19:39:25', '2021-09-07 19:39:25', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435206094430859265', '下移栏目', NULL, 'updateAppColumnDown', '1', NULL, NULL, '1435206094355361794', '1435205611876184066', 1, '1', '3', '0', '2021-09-07 19:39:33', '2021-09-07 19:39:33', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435206257442484225', '数据类型集合', NULL, 'getAppDataTypeList', '1', NULL, NULL, '1435206257379569665', '1435198091858210817', 8, '1', '3', '0', '2021-09-07 19:40:12', '2021-09-07 19:40:12', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435211120511422466', '字典上移', NULL, 'B_APP_DICT_UP', '2', NULL, NULL, '1435211120452702209', '1320705744389410817', 7, '1', '3', '0', '2021-09-07 19:59:31', '2021-09-07 19:59:31', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435211152631402497', '字典下移', NULL, 'B_APP_DICT_DOWN', '2', NULL, NULL, '1435211152581070849', '1320705744389410817', 8, '1', '3', '0', '2021-09-07 19:59:39', '2021-09-07 19:59:39', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435211242276261890', '上移字典', NULL, 'updateAppDictUp', '1', NULL, NULL, '1435211242209153026', '1435211120452702209', 1, '1', '3', '0', '2021-09-07 20:00:00', '2021-09-07 20:00:00', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435211278926090242', '下移字典', NULL, 'updateAppDictDown', '1', NULL, NULL, '1435211278867369986', '1435211152581070849', 1, '1', '3', '0', '2021-09-07 20:00:09', '2021-09-07 20:00:09', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435211840065245185', 'ip管理', NULL, 'home-basicManage-disableIpManage', '0', '/home/basicManage/disableIpManage', NULL, '1435211840010719234', '1320705744120975362', 12, '1', '3', '0', '2021-09-07 20:02:23', '2021-12-21 10:32:24', 3);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435220401990275074', '禁用ip删除', NULL, 'B_APP_DISABLE_IP_ADD', '2', NULL, NULL, '1435220401885417473', '1435211840010719234', 1, '1', '3', '0', '2021-09-07 20:36:24', '2021-09-07 20:36:24', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435220428506664962', '禁用ip编辑', NULL, 'B_APP_DISABLE_IP_DELETE', '2', NULL, NULL, '1435220428431167489', '1435211840010719234', 2, '1', '3', '0', '2021-09-07 20:36:31', '2021-09-07 20:36:31', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435220456210042881', '禁用ip添加', NULL, 'B_APP_DISABLE_IP_EDIT', '2', NULL, NULL, '1435220456147128322', '1435211840010719234', 3, '1', '3', '0', '2021-09-07 20:36:37', '2021-09-07 20:36:37', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435220525386698753', '获取禁用ip分页数据', NULL, 'getAppDisableIpPage', '1', NULL, NULL, '1435220525319589889', '1435211840010719234', 4, '1', '3', '0', '2021-09-07 20:36:54', '2021-09-07 20:36:54', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435220592503951362', '创建禁用ip', NULL, 'createAppDisableIp', '1', NULL, NULL, '1435220592441036802', '1435220456147128322', 1, '1', '3', '0', '2021-09-07 20:37:10', '2021-09-07 20:37:10', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435220664180412417', '修改禁用ip', NULL, 'updateAppDisableIp', '1', NULL, NULL, '1435220664134275073', '1435220428431167489', 1, '1', '3', '0', '2021-09-07 20:37:27', '2021-09-07 20:37:27', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435220712419102721', '删除禁用ip', NULL, 'deleteAppDisableIp', '1', NULL, NULL, '1435220712301662210', '1435220401885417473', 1, '1', '3', '0', '2021-09-07 20:37:38', '2021-09-07 20:37:38', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435413802781798401', '获取页面集合', NULL, 'getOpenPageList', '4', '', NULL, '1435413802760826882', 'root', 41, '1', '3', '0', '2021-09-08 09:24:53', '2021-11-25 20:04:34', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435479159576043521', '获取栏目集合', NULL, 'getOpenColumnList', '4', '', NULL, '1435479159534100481', 'root', 23, '1', '3', '0', '2021-09-08 13:44:36', '2021-11-25 20:04:33', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435529609184649218', '获取禁用IP地址集合', NULL, 'getOpenDisableIpList', '4', '', NULL, '1435529609176260609', 'root', 24, '1', '3', '0', '2021-09-08 17:05:04', '2021-11-25 20:04:33', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435847594990460930', '页面导入', NULL, 'B_APP_PAGE_IMPORT', '2', NULL, NULL, '1435847594982072322', '1435192638889717761', 5, '1', '3', '0', '2021-09-09 14:08:38', '2021-09-09 14:08:38', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435847637617172481', '页面导出', NULL, 'B_APP_PAGE_EXPORT', '2', NULL, NULL, '1435847637608783874', '1435192638889717761', 6, '1', '3', '0', '2021-09-09 14:08:48', '2021-09-09 14:09:03', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435847770849239041', '数据类型导入', NULL, 'B_APP_DATA_TYPE_IMPORT', '2', NULL, NULL, '1435847770840850434', '1435196892522147842', 5, '1', '3', '0', '2021-09-09 14:09:19', '2021-09-09 14:09:19', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435847812469317634', '数据类型导出', NULL, 'B_APP_DATA_TYPE_EXPORT', '2', NULL, NULL, '1435847812460929026', '1435196892522147842', 6, '1', '3', '0', '2021-09-09 14:09:29', '2021-09-09 14:09:29', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435847936851402754', '栏目导入', NULL, 'B_APP_COLUMN_IMPORT', '2', NULL, NULL, '1435847936843014145', '1435198091858210817', 9, '1', '3', '0', '2021-09-09 14:09:59', '2021-09-09 14:09:59', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435848006338437122', '栏目导出', NULL, 'B_APP_COLUMN_EXPORT', '2', NULL, NULL, '1435848006334242818', '1435198091858210817', 10, '1', '3', '0', '2021-09-09 14:10:16', '2021-09-09 14:10:16', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435848273083588609', '页面集合', NULL, 'getAppPageList', '1', '', NULL, '1435848273075200001', '1435847637608783874', 7, '1', '3', '0', '2021-09-09 14:11:19', '2021-09-09 14:19:16', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435849398868336641', '批量创建页面', NULL, 'createAppPageList', '1', '', NULL, '1435849398864142337', '1435847594982072322', 1, '1', '3', '0', '2021-09-09 14:15:48', '2021-09-09 14:16:56', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435849971671851009', '批量创建数据类型', NULL, 'createAppDataTypeList', '1', '', NULL, '1435849971667656705', '1435847770840850434', 1, '1', '3', '0', '2021-09-09 14:18:04', '2021-09-09 14:18:04', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1435850088944590850', '批量创建栏目', NULL, 'createAppColumnList', '1', '', NULL, '1435850088936202241', '1435847936843014145', 1, '1', '3', '0', '2021-09-09 14:18:32', '2021-09-09 14:18:32', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1437951632989405186', '上传文件签名', NULL, 'getUploadFileSign', '1', '', NULL, '1437951632892936194', 'root', 43, '1', '3', '0', '2021-09-15 09:29:19', '2021-11-25 20:04:34', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1437953568111247362', '获取我的菜单集合', NULL, 'getMyAppMenuList', '1', '', NULL, '1437953568048332802', 'root', 45, '1', '3', '0', '2021-09-15 09:37:01', '2021-11-25 20:04:34', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1437958941295689729', '获取我的字典集合', NULL, 'getMyAppDictList', '1', '', NULL, '1437958941228580866', 'root', 47, '1', '3', '0', '2021-09-15 09:58:22', '2021-11-25 20:04:34', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1451791326474907650', '获取基础平台的区域集合', NULL, 'getOpenBasicAreaList', '4', '', NULL, '1451791326466519042', 'root', 48, '1', '3', '0', '2021-10-23 14:03:19', '2021-10-23 17:07:42', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1451941497733103617', '获取区域集合', NULL, 'getMyAppAreaList', '1', '', NULL, '1451941497716326401', '1320705742183206913', 6, '1', '3', '0', '2021-10-24 00:00:03', '2021-11-04 17:48:10', 3);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1460139726190161922', '创建用户验证码', NULL, 'createMyAppUserCaptcha', '1', '', NULL, '1460139726177579009', '1320705742183206913', 6, '1', '3', '0', '2021-11-15 14:56:53', '2021-11-15 14:56:53', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1460139849309761537', '获取用户验证码', NULL, 'getMyAppUserCaptcha', '1', '', NULL, '1460139849305567233', '1320705742183206913', 7, '1', '3', '0', '2021-11-15 14:57:22', '2021-11-15 14:57:22', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1460140111302766594', '创建用户的验证码', NULL, 'createOpenUserCaptcha', '5', NULL, NULL, '1460140111298572289', 'root', 42, '1', '3', '0', '2021-11-15 14:58:25', '2021-11-25 20:04:34', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1460140172317306882', '获取用户的验证码', NULL, 'getOpenUserCaptcha', '5', NULL, NULL, '1460140172308918274', 'root', 44, '1', '3', '0', '2021-11-15 14:58:39', '2021-11-25 20:04:34', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1460140238683779073', '创建验证码', NULL, 'createOpenCaptcha', '4', '', NULL, '1460140238679584770', 'root', 46, '1', '3', '0', '2021-11-15 14:58:55', '2021-11-25 20:04:34', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1460140299715096578', '获取验证码', NULL, 'getOpenCaptcha', '4', '', NULL, '1460140299710902273', 'root', 49, '1', '3', '0', '2021-11-15 14:59:10', '2021-11-25 20:04:34', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463842640996913153', '供应商管理', NULL, 'home-apiManage-supplierManage', '0', '/home/apiManage/supplierManage', NULL, '1463842640929804289', '1354713328152698881', 6, '1', '3', '0', '2021-11-25 20:10:57', '2022-01-13 20:55:46', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463843214173720578', '消息管理', NULL, 'home-apiManage-messageManage', '0', '/home/apiManage/messageManage', NULL, '1463843214106611714', '1354713328152698881', 7, '1', '3', '0', '2021-11-25 20:13:13', '2022-01-13 21:57:42', 5);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463878490828926977', '接口供应商分页', NULL, 'getAppApiSupplierPage', '1', '', NULL, '1463878490694709250', '1463842640929804289', 1, '1', '3', '0', '2021-11-25 22:33:24', '2021-11-25 22:33:24', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463878634546753537', '接口供应商添加', NULL, 'B_APP_API_SUPPLIER_ADD', '2', '', NULL, '1463878634479644674', '1463842640929804289', 2, '1', '3', '0', '2021-11-25 22:33:58', '2021-11-25 22:33:58', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463878747755212802', '接口供应商编辑', NULL, 'B_APP_API_SUPPLIER_EDIT', '2', NULL, NULL, '1463878747688103937', '1463842640929804289', 3, '1', '3', '0', '2021-11-25 22:34:25', '2021-11-25 22:34:25', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463878791652798466', '接口供应商删除', NULL, 'B_APP_API_SUPPLIER_DELETE', '2', NULL, NULL, '1463878791585689601', '1463842640929804289', 4, '1', '3', '0', '2021-11-25 22:34:36', '2021-11-25 22:34:36', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463878835122565122', '接口供应商导入', NULL, 'B_APP_API_SUPPLIER_IMPORT', '2', NULL, NULL, '1463878835055456257', '1463842640929804289', 5, '1', '3', '0', '2021-11-25 22:34:46', '2021-11-25 22:34:46', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463878881217966082', '接口供应商导出', NULL, 'B_APP_API_SUPPLIER_EXPORT', '2', NULL, NULL, '1463878881150857218', '1463842640929804289', 6, '1', '3', '0', '2021-11-25 22:34:57', '2021-11-25 22:34:57', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463878954182078466', '消息模板添加', NULL, 'B_APP_MESSAGE_TEMPLATE_ADD', '2', NULL, NULL, '1463878954119163906', '1463843214106611714', 1, '1', '3', '0', '2021-11-25 22:35:15', '2021-11-25 22:35:15', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879052844691458', '消息模板编辑', NULL, 'B_APP_MESSAGE_TEMPLATE_EDIT', '2', NULL, NULL, '1463879052781776897', '1463843214106611714', 2, '1', '3', '0', '2021-11-25 22:35:38', '2021-11-25 22:35:38', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879082880102402', '消息模板删除', NULL, 'B_APP_MESSAGE_TEMPLATE_DELETE', '2', NULL, NULL, '1463879082817187842', '1463843214106611714', 3, '1', '3', '0', '2021-11-25 22:35:45', '2021-11-25 22:35:45', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879108566020097', '消息模板详情', NULL, 'B_APP_MESSAGE_TEMPLATE_DETAILS', '2', NULL, NULL, '1463879108503105538', '1463843214106611714', 4, '1', '3', '0', '2021-11-25 22:35:51', '2021-11-25 22:35:51', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879143198388226', '消息模板导入', NULL, 'B_APP_MESSAGE_TEMPLATE_IMPORT', '2', NULL, NULL, '1463879143131279361', '1463843214106611714', 5, '1', '3', '0', '2021-11-25 22:36:00', '2021-11-25 22:36:00', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879173661618178', '消息模板导出', NULL, 'B_APP_MESSAGE_TEMPLATE_EXPORT', '2', NULL, NULL, '1463879173590315009', '1463843214106611714', 6, '1', '3', '0', '2021-11-25 22:36:07', '2021-11-25 22:36:07', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879308873396226', '创建接口供应商', NULL, 'createAppApiSupplier', '1', '', NULL, '1463879308806287362', '1463878634479644674', 1, '1', '3', '0', '2021-11-25 22:36:39', '2021-11-25 23:06:40', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879394919542786', '修改接口供应商', NULL, 'updateAppApiSupplier', '1', '', NULL, '1463879394852433922', '1463878747688103937', 1, '1', '3', '0', '2021-11-25 22:37:00', '2021-11-25 23:06:49', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879478629462018', '删除接口供应商', NULL, 'deleteAppApiSupplier', '1', '', NULL, '1463879478562353154', '1463878791585689601', 1, '1', '3', '0', '2021-11-25 22:37:20', '2021-11-25 23:07:02', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879537622347778', '批量创建接口供应商', NULL, 'createAppApiSupplierList', '1', '', NULL, '1463879537555238914', '1463878835055456257', 1, '1', '3', '0', '2021-11-25 22:37:34', '2021-11-25 23:07:15', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879580832067585', '获取接口供应商集合', NULL, 'getAppApiSupplierList', '1', '', NULL, '1463879580764958722', '1463878881150857218', 1, '1', '3', '0', '2021-11-25 22:37:44', '2021-11-25 23:07:27', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879663170449409', '消息模板分页', NULL, 'getAppMessageTemplatePage', '1', '', NULL, '1463879663103340546', '1463843214106611714', 7, '1', '3', '0', '2021-11-25 22:38:04', '2021-11-25 22:41:39', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879710234734594', '创建消息模板', NULL, 'createAppMessageTemplate', '1', '', NULL, '1463879710171820034', '1463878954119163906', 1, '1', '3', '0', '2021-11-25 22:38:15', '2021-11-25 22:41:27', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879765943480322', '修改消息模板', NULL, 'updateAppMessageTemplate', '1', '', NULL, '1463879765880565762', '1463879052781776897', 1, '1', '3', '0', '2021-11-25 22:38:28', '2021-11-25 22:42:12', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879816753278977', '删除消息模板', NULL, 'deleteAppMessageTemplate', '1', '', NULL, '1463879816686170114', '1463879082817187842', 1, '1', '3', '0', '2021-11-25 22:38:40', '2021-11-25 22:42:46', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879887083368450', '消息日志分页', NULL, 'getAppMessageLogPage', '1', '', NULL, '1463879887016259586', '1463879108503105538', 1, '1', '3', '0', '2021-11-25 22:38:57', '2021-11-25 23:07:47', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879955500855298', '批量创建消息模板', NULL, 'createAppMessageTemplateList', '1', '', NULL, '1463879955433746434', '1463879143131279361', 1, '1', '3', '0', '2021-11-25 22:39:13', '2021-11-25 23:07:58', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463879990892392450', '获取消息模板集合', NULL, 'getAppMessageTemplateList', '1', '', NULL, '1463879990825283585', '1463879173590315009', 1, '1', '3', '0', '2021-11-25 22:39:22', '2021-11-25 23:08:16', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463887841207611393', '创建消息解析', NULL, 'createOpenMessageAnalysis', '4', NULL, NULL, '1463887841153085442', 'root', 50, '1', '3', '0', '2021-11-25 23:10:33', '2021-11-25 23:10:33', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463887892495560705', '获取接口供应商', NULL, 'getOpenApiSupplier', '4', NULL, NULL, '1463887892428451841', 'root', 51, '1', '3', '0', '2021-11-25 23:10:46', '2021-11-25 23:10:46', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1463887944534290434', '获取消息模板', NULL, 'getOpenMessageTemplate', '4', NULL, NULL, '1463887944467181569', 'root', 52, '1', '3', '0', '2021-11-25 23:10:58', '2021-11-25 23:10:58', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1464070319194464258', '应用刷新', NULL, 'B_APP_REFRESH', '2', '', NULL, '1464070319135744002', '1320705744431353857', 11, '1', '3', '0', '2021-11-26 11:15:42', '2021-11-26 11:15:42', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1464070429324304385', '刷新应用', NULL, 'updateCurrentUserAppRefresh', '1', '', '', '1464070429257195521', '1464070319135744002', 1, '1', '3', '0', '2021-11-26 11:16:08', '2021-11-26 11:16:08', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1470956341090025473', 'Access管理', NULL, 'B_ACCESS_MANAGE', '2', '', NULL, '1470956341006139394', 'root', 53, '1', '3', '0', '2021-12-15 11:18:15', '2021-12-15 11:18:15', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1470956442353106945', '获取当前用户访问秘钥', NULL, 'getCurrentUserAccessSecret', '1', '', '', '1470956442252443649', '1470956341006139394', 1, '1', '3', '0', '2021-12-15 11:18:39', '2021-12-15 11:18:39', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1470957452752551937', '更新当前用户访问秘钥', NULL, 'updateCurrentUserAccessSecret', '1', '', '', '1470957452693831681', '1470956341006139394', 2, '1', '3', '0', '2021-12-15 11:22:40', '2021-12-15 11:22:40', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1473118958558195714', '网站菜单', NULL, 'home-websiteManage-menuManage', '0', '/home/websiteManage/menuManage', NULL, '1473118958545612802', '1435191547582148609', 5, '1', '3', '0', '2021-12-21 10:31:43', '2021-12-21 10:50:11', 9);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1481678883769110529', '获取数据类型集合', NULL, 'getOpenDataTypeList', '4', '', '', '1481678883760721921', 'root', 56, '1', '3', '0', '2022-01-14 01:25:48', '2022-01-14 01:25:48', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1485850085798535170', '创建小程序token信息', NULL, 'createOpenAppletToken', '4', '', '', '1485850085790146562', 'root', 57, '1', '3', '0', '2022-01-25 13:40:40', '2022-01-25 13:40:40', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1485850151384866817', '获取小程序信息', NULL, 'getOpenAppletByApplet', '4', '', '', '1485850151380672514', 'root', 58, '1', '3', '0', '2022-01-25 13:40:56', '2023-12-27 14:05:03', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1584379767644688385', '获取图片验证码', '', 'getOpenImgCaptcha', '4', '', '', '1584379767640494082', 'root', 59, '1', '3', '0', '2022-10-24 11:02:27', '2022-10-24 11:02:27', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585628871522291713', '角色导入', NULL, 'B_APP_ROLE_IMPORT', '2', NULL, NULL, '1585628871329353729', '1320705744204861441', 6, '1', '3', '0', '2022-10-27 21:45:57', '2022-10-27 21:51:10', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585628922290147330', '角色导出', NULL, 'B_APP_ROLE_EXPORT', '2', NULL, NULL, '1585628922093015042', '1320705744204861441', 7, '1', '3', '0', '2022-10-27 21:46:09', '2022-10-27 21:50:42', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585629412948217858', '角色关联导入', NULL, 'B_APP_ROLE_AUTHORITY_RELATION_IMPORT', '2', NULL, NULL, '1585629412688171009', '1320705744204861441', 8, '1', '3', '0', '2022-10-27 21:48:06', '2022-10-27 21:53:14', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585629466865995778', '角色关联导出', NULL, 'B_APP_ROLE_AUTHORITY_RELATION_EXPORT', '2', NULL, NULL, '1585629466735972354', '1320705744204861441', 9, '1', '3', '0', '2022-10-27 21:48:19', '2022-10-27 21:52:55', 2);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585629559920824321', '创建角色集合', NULL, 'createAppRoleList', '2', NULL, NULL, '1585629559723692033', '1585628871329353729', 1, '1', '3', '0', '2022-10-27 21:48:41', '2022-10-27 21:48:41', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585629622420148225', '创建角色权限关联集合', NULL, 'createAppRoleAuthorityRelationList', '2', NULL, NULL, '1585629622298513410', '1585629412688171009', 1, '1', '3', '0', '2022-10-27 21:48:56', '2022-10-27 21:48:56', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585629679424933889', '获取的角色权限关联集合', NULL, 'getAppRoleAuthorityRelationList', '2', NULL, NULL, '1585629679169081345', '1585629466735972354', 1, '1', '3', '0', '2022-10-27 21:49:10', '2022-10-27 21:49:10', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585630959421657090', '用户组导入', NULL, 'B_APP_GROUP_IMPORT', '2', NULL, NULL, '1585630959228719105', '1320705744292941826', 6, '1', '3', '0', '2022-10-27 21:54:15', '2022-10-27 21:54:15', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585631412335185922', '用户组导出', NULL, 'B_APP_GROUP_EXPORT', '2', NULL, NULL, '1585631412142247937', '1320705744292941826', 7, '1', '3', '0', '2022-10-27 21:56:03', '2022-10-27 21:56:03', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585631512004431873', '用户组关联导入', NULL, 'B_APP_GROUP_ROLE_RELATION_IMPORT', '2', NULL, NULL, '1585631511811493889', '1320705744292941826', 8, '1', '3', '0', '2022-10-27 21:56:26', '2022-10-27 21:56:26', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585631559332958209', '用户组关联导出', NULL, 'B_APP_GROUP_ROLE_RELATION_EXPORT', '2', NULL, NULL, '1585631559135825921', '1320705744292941826', 9, '1', '3', '0', '2022-10-27 21:56:38', '2022-10-27 21:56:38', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585631628060823553', '创建用户组集合', NULL, 'createAppGroupList', '2', NULL, NULL, '1585631627867885569', '1585630959228719105', 1, '1', '3', '0', '2022-10-27 21:56:54', '2022-10-27 21:56:54', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585631705575755777', '创建用户组角色关联集合', NULL, 'createAppGroupRoleRelationList', '2', NULL, NULL, '1585631705382817793', '1585631511811493889', 1, '1', '3', '0', '2022-10-27 21:57:13', '2022-10-27 21:57:13', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585631761494216705', '获取的用户组角色关联集合', NULL, 'getAppGroupRoleRelationList', '2', NULL, NULL, '1585631761301278721', '1585631559135825921', 10, '1', '3', '0', '2022-10-27 21:57:26', '2022-10-27 21:57:35', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585631917249695745', '页面关联导入', NULL, 'B_APP_PAGE_COLUMN_RELATION_IMPORT', '2', NULL, NULL, '1585631917052563458', '1435192638889717761', 7, '1', '3', '0', '2022-10-27 21:58:03', '2022-10-27 21:58:03', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585631957233995777', '页面关联导出', NULL, 'B_APP_PAGE_COLUMN_RELATION_EXPORT', '2', NULL, NULL, '1585631957099778049', '1435192638889717761', 8, '1', '3', '0', '2022-10-27 21:58:13', '2022-10-27 21:58:13', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585632030361686018', '创建页面栏目关联集合', NULL, 'createAppPageColumnRelationList', '2', NULL, NULL, '1585632030164553730', '1585631917052563458', 1, '1', '3', '0', '2022-10-27 21:58:30', '2022-10-27 21:58:30', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585632081976791041', '获取的页面栏目关联集合', NULL, 'getAppPageColumnRelationList', '2', NULL, NULL, '1585632081783853057', '1585631957099778049', 1, '1', '3', '0', '2022-10-27 21:58:42', '2022-10-27 21:58:42', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1585905221199335425', '根据账号获取用户', NULL, 'getOpenUserByAccount', '4', '', '', '1585905221190946817', 'root', 58, '1', '3', '0', '2022-10-28 16:04:04', '2022-10-28 16:04:04', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1684032426201862145', '创建系统错误消息', NULL, 'createOpenSystemErrorNoticeMessage', '4', '', '', '1684032426147336193', 'root', 59, '1', '3', '0', '2023-07-26 10:46:32', '2023-07-26 10:46:32', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1687922126805319682', '获取授权码', NULL, 'createOpenToken', '4', '', '', '1687922126754988034', 'root', 60, '1', '3', '0', '2023-08-06 04:22:49', '2023-08-06 04:22:49', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1687922180806983682', '注册', NULL, 'createOpenRegister', '4', '', '', '1687922180765040641', 'root', 61, '1', '3', '0', '2023-08-06 04:23:02', '2023-08-06 04:23:02', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1687922233520996353', '找回密码', NULL, 'createOpenForgetPassword', '4', '', '', '1687922233479053313', 'root', 62, '1', '3', '0', '2023-08-06 04:23:15', '2023-08-06 04:23:15', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1687922286348255233', '验证账号', NULL, 'getOpenVerifyAccount', '4', '', '', '1687922286310506498', 'root', 63, '1', '3', '0', '2023-08-06 04:23:27', '2023-08-06 04:23:27', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1687923629142097922', '用户授权详情', NULL, 'B_USER_AUTHORIZE_DETAILS', '2', '', NULL, '1687923629100154882', '1320705744473296897', 3, '1', '3', '0', '2023-08-06 04:28:47', '2023-08-06 04:28:55', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1687923882176069633', '用户取消授权的权限', NULL, 'B_USER_AUTHORIZE_AUTHORITY_DELETE', '2', '', NULL, '1687923882134126593', '1687923629100154882', 1, '1', '3', '0', '2023-08-06 04:29:48', '2023-08-06 04:29:48', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1687924072777826305', '用户授权权限分页', NULL, 'getCurrentUserAuthorizeAuthorityPage', '1', NULL, NULL, '1687924072735883265', '1687923629100154882', 2, '1', '3', '0', '2023-08-06 04:30:33', '2023-08-06 04:30:33', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1687924137638543362', '用户取消权限授权', NULL, 'deleteCurrentUserAuthorizeAuthority', '1', NULL, NULL, '1687924137596600322', '1687923882134126593', 1, '1', '3', '0', '2023-08-06 04:30:49', '2023-08-06 04:30:49', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1687926767290990594', '获取审核应用权限', NULL, 'getAuditAppAuthorize', '1', '', '', '1687926767249047553', '1320705744603320321', 5, '1', '3', '0', '2023-08-06 04:41:16', '2023-08-06 04:41:16', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1687927571729137666', '审核详情', NULL, 'B_APP_AUDIT_DETAILS', '2', '', NULL, '1687927571687194626', '1343443669566816258', 0, '1', '3', '0', '2023-08-06 04:44:27', '2023-08-06 04:45:16', 22);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1687929621175123969', '审核详情', NULL, 'B_AUTHORITY_AUDIT_DETAILS', '2', '', NULL, '1687929621137375233', '1338017042656763905', 2, '1', '3', '0', '2023-08-06 04:52:36', '2023-08-06 04:52:36', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1688198754554949633', '获取用户授权权限集合', NULL, 'getOpenUserAuthorizeAuthorityList', '4', '', '', '1688198754513006594', 'root', 64, '1', '3', '0', '2023-08-06 22:42:02', '2023-08-06 22:42:02', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1688726665372409858', '刷新应用TOKEN的权限', NULL, 'updateOpenAppTokenAuthority', '4', '', '', '1688726665317883906', 'root', 65, '1', '3', '0', '2023-08-08 09:39:46', '2023-08-08 09:39:46', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1719014721924923393', '获取树形节点集合', NULL, 'getOpenTreeNodeList', '4', '', '', '1719014721874591746', 'root', 66, '1', '3', '0', '2023-10-30 23:33:42', '2023-10-30 23:33:42', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1719022678700040194', '获取区域节点集合', NULL, 'getMyAppAreaNodeList', '1', '', '', '1719022678649708546', '1320705742183206913', 8, '1', '3', '0', '2023-10-31 00:05:19', '2023-10-31 00:05:19', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1719024825827172354', '获取基础平台的区域节点集合', NULL, 'getOpenBasicAreaNodeList', '4', '', '', '1719024825781035010', 'root', 67, '1', '3', '0', '2023-10-31 00:13:51', '2023-10-31 00:13:51', 0);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1726463551440158722', '获取用户下载签名', NULL, 'getOpenUserDownloadSign', '5', '', '', '1726463551394021377', 'root', 68, '1', '3', '0', '2023-11-20 12:52:41', '2023-11-20 15:00:34', 1);
INSERT INTO `xc_authority` (`id`, `name`, `describe`, `code`, `type`, `url`, `icon`, `node`, `parent_node`, `seq`, `hidden`, `app_id`, `status`, `create_time`, `update_time`, `version`) VALUES ('1739888892691120129', '获取用户的小程序信息', NULL, 'getOpenAppletByUser', '4', '', '', '1739888892653371393', 'root', 69, '1', '3', '0', '2023-12-27 14:00:12', '2023-12-27 14:00:12', 0);
