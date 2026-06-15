-- 创建数据库
CREATE DATABASE IF NOT EXISTS micro_user DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE micro_user;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    password    VARCHAR(64)  NOT NULL COMMENT '密码(MD5)',
    email       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    phone       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    role_name   VARCHAR(50)  NOT NULL COMMENT '角色名称',
    role_code   VARCHAR(50)  NOT NULL COMMENT '角色编码',
    description VARCHAR(200) DEFAULT NULL COMMENT '角色描述',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT   NOT NULL COMMENT '用户ID',
    role_id     BIGINT   NOT NULL COMMENT '角色ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 初始化角色数据
INSERT IGNORE INTO sys_role (role_name, role_code, description) VALUES
('管理员', 'ADMIN', '系统管理员'),
('普通用户', 'USER', '普通业务用户');

-- 初始化测试用户（Postman 登录：testadmin / 123456）
INSERT IGNORE INTO sys_user (username, password, email, status)
VALUES ('testadmin', MD5('123456'), 'admin@example.com', 1);

INSERT IGNORE INTO sys_user_role (user_id, role_id)
SELECT u.id, 1
FROM sys_user u
WHERE u.username = 'testadmin'
  AND u.deleted = 0;

-- 操作日志表
CREATE TABLE IF NOT EXISTS sys_operate_log (
    id              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    operator        VARCHAR(50)   NOT NULL COMMENT '操作人(用户名)',
    operate_time    DATETIME      NOT NULL COMMENT '操作时间',
    request_path    VARCHAR(200)  NOT NULL COMMENT '接口路径',
    request_method  VARCHAR(10)   NOT NULL COMMENT '请求方法',
    request_params  TEXT                   COMMENT '请求参数(JSON)',
    response_result TEXT                   COMMENT '响应结果(JSON)',
    create_time     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_operate_time (operate_time),
    KEY idx_operator (operator)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 基础数据表
CREATE TABLE IF NOT EXISTS sys_base_data (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    type_code   VARCHAR(50)  NOT NULL COMMENT '数据类型编码',
    data_code   VARCHAR(50)  NOT NULL COMMENT '数据编码',
    data_name   VARCHAR(100) NOT NULL COMMENT '数据名称',
    sort        INT          NOT NULL DEFAULT 0 COMMENT '排序号',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_type_data (type_code, data_code),
    KEY idx_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='基础数据表';

-- 初始化基础数据
INSERT IGNORE INTO sys_base_data (type_code, data_code, data_name, sort, status) VALUES
('user_status', '0', '禁用', 1, 1),
('user_status', '1', '启用', 2, 1),
('role_type', 'ADMIN', '管理员', 1, 1),
('role_type', 'USER', '普通用户', 2, 1);

-- ============================================================
-- Navicat 测试用自定义 SQL（对应 Mapper 中的 selectUserByUsername / selectRoleByUserId）
-- ============================================================

-- 1. 根据用户名查询用户
SELECT id, username, password, email, phone, status, deleted, create_time, update_time
FROM sys_user
WHERE username = 'admin'
  AND deleted = 0
LIMIT 1;

-- 2. 根据用户 ID 查询关联角色
SELECT r.id, r.role_name, r.role_code, r.description, r.deleted, r.create_time, r.update_time
FROM sys_role r
         INNER JOIN sys_user_role ur ON r.id = ur.role_id
WHERE ur.user_id = 1
  AND r.deleted = 0;
