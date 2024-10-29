CREATE TABLE IF NOT EXISTS template_table
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --

    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号',
    deleted     BIT          DEFAULT b'0' COMMENT '是否删除'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 10000 COMMENT = '模板表';