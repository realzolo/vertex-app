CREATE TABLE IF NOT EXISTS template_table
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --

    -- 自定义字段结束 --
    creator     VARCHAR(50) default '' COMMENT '创建人',
    create_time DATETIME COMMENT '创建时间',
    updater     VARCHAR(50) default '' COMMENT '更新人',
    update_time DATETIME COMMENT '更新时间',
    version     INT UNSIGNED default 0 COMMENT '版本号',
    deleted     bit         default b'0' COMMENT '是否删除'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 10000 COMMENT ='模板表';