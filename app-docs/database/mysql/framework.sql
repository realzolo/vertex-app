CREATE TABLE IF NOT EXISTS vx_user
(
    id           BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    username     VARCHAR(25)  DEFAULT '' COMMENT '用户名',
    password     VARCHAR(100) DEFAULT '' COMMENT '密码',
    nickname     VARCHAR(25)  DEFAULT '' COMMENT '用户昵称',
    name         VARCHAR(25)  DEFAULT '' COMMENT '用户姓名',
    introduction VARCHAR(255) DEFAULT '' COMMENT '用户简介',
    avatar       VARCHAR(255) DEFAULT '' COMMENT '头像地址',
    gender       INT UNSIGNED DEFAULT 0 COMMENT '性别',
    birthday     DATE         DEFAULT NULL COMMENT '生日',
    phone        VARCHAR(25)  DEFAULT '' COMMENT '电话号码',
    email        VARCHAR(50)  DEFAULT '' COMMENT '电子邮箱',
    pwd_exp_date DATE         DEFAULT NULL COMMENT '密码过期时间',
    status       INT UNSIGNED DEFAULT 0 COMMENT '账号状态',
    -- 自定义字段结束 --
    creator        BIGINT           DEFAULT NULL COMMENT '创建人',
    create_time    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater        BIGINT           DEFAULT NULL COMMENT '更新人',
    update_time    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version        INT UNSIGNED     DEFAULT 0 COMMENT '版本号',
    deleted        BIT              DEFAULT b'0' COMMENT '是否删除',
    INDEX (username),
    UNIQUE (username)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 10000 COMMENT = '用户';

CREATE TABLE IF NOT EXISTS vx_role
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    name        VARCHAR(25)  COMMENT '角色名称',
    code        VARCHAR(25)  COMMENT '角色Code',
    sort        INT UNSIGNED DEFAULT 0 COMMENT '排序号',
    status      INT UNSIGNED DEFAULT 0 COMMENT '(0: 正常, 1: 禁用)',
    -- 自定义字段结束 --
    creator        BIGINT           DEFAULT NULL COMMENT '创建人',
    create_time    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater        BIGINT           DEFAULT NULL COMMENT '更新人',
    update_time    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version        INT UNSIGNED     DEFAULT 0 COMMENT '版本号'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 10000 COMMENT = '用户角色';

CREATE TABLE IF NOT EXISTS vx_permission
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    name        VARCHAR(25)  COMMENT '权限名称',
    code        VARCHAR(25)  COMMENT '权限Code',
    sort        INT UNSIGNED DEFAULT 0 COMMENT '排序号',
    -- 自定义字段结束 --
    creator        BIGINT           DEFAULT NULL COMMENT '创建人',
    create_time    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater        BIGINT           DEFAULT NULL COMMENT '更新人',
    update_time    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version        INT UNSIGNED     DEFAULT 0 COMMENT '版本号',
    INDEX (code),
    UNIQUE (code)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    AUTO_INCREMENT = 10000 COMMENT = '权限';

CREATE TABLE IF NOT EXISTS vx_user_role
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    user_id     BIGINT          COMMENT '用户ID',
    role_id     BIGINT          COMMENT '角色ID',
    -- 自定义字段结束 --
    creator        BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater        BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version        INT UNSIGNED DEFAULT 0 COMMENT '版本号',
    INDEX (role_id),
    UNIQUE (role_id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    AUTO_INCREMENT = 10000 COMMENT = '用户-角色';

CREATE TABLE IF NOT EXISTS vx_role_permission
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    role_id     BIGINT          COMMENT '角色ID',
    permissions VARCHAR(2000)   DEFAULT '' COMMENT '权限列表',
    -- 自定义字段结束 --
    creator        BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater        BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version        INT UNSIGNED DEFAULT 0 COMMENT '版本号',
    INDEX (role_id),
    UNIQUE (role_id)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    AUTO_INCREMENT = 10000 COMMENT = '角色-权限';

CREATE TABLE IF NOT EXISTS vx_exception_log
(
    id                           BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    user_id                      BIGINT       DEFAULT NULL COMMENT '用户ID',
    request_method               VARCHAR(50)  DEFAULT '' COMMENT '请求方法名',
    request_url                  VARCHAR(255) DEFAULT '' COMMENT '访问地址',
    request_params               VARCHAR(255) DEFAULT '' COMMENT '请求参数',
    user_ip                      VARCHAR(50)  DEFAULT '' COMMENT '用户IP',
    user_agent                   VARCHAR(255) DEFAULT '' COMMENT '浏览器UA',
    exception_name               VARCHAR(255) DEFAULT '' COMMENT '异常名',
    exception_class_name         VARCHAR(255) DEFAULT '' COMMENT '异常发生的类全名',
    exception_file_name          VARCHAR(255) DEFAULT '' COMMENT '异常发生的类文件',
    exception_method_name        VARCHAR(255) DEFAULT '' COMMENT '异常发生的方法名',
    exception_line_number        INT UNSIGNED COMMENT '异常发生的方法所在行',
    exception_stack_trace        TEXT COMMENT '异常的栈轨迹异常的栈轨迹',
    exception_root_cause_message VARCHAR(255) DEFAULT '' COMMENT '异常导致的根消息',
    exception_message            VARCHAR(255) DEFAULT '' COMMENT '异常导致的消息',
    -- 自定义字段结束 --
    creator        BIGINT        DEFAULT NULL COMMENT '创建人',
    create_time    DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater        BIGINT        DEFAULT NULL COMMENT '更新人',
    update_time    DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version        INT UNSIGNED  DEFAULT 0 COMMENT '版本号',
    INDEX (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 10000 COMMENT = 'API错误日志';

CREATE TABLE IF NOT EXISTS vx_operation_log
(
    id             BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    user_id        BIGINT              DEFAULT NULL COMMENT '用户ID',
    component      VARCHAR(255)        DEFAULT '' COMMENT '模块名称',
    action         VARCHAR(255)        DEFAULT '' COMMENT '操作名称',
    description    VARCHAR(500)        DEFAULT '' COMMENT '操作描述',
    request_method VARCHAR(255)        DEFAULT '' COMMENT '请求方法名',
    request_url    VARCHAR(255)        DEFAULT '' COMMENT '访问地址',
    request_params VARCHAR(4096)       DEFAULT '' COMMENT '请求参数',
    request_result TEXT COMMENT '请求参数',
    user_ip        VARCHAR(50)         DEFAULT '' COMMENT '用户IP',
    user_agent     VARCHAR(255)        DEFAULT '' COMMENT '浏览器UA',
    location       VARCHAR(100)        DEFAULT '' COMMENT '地理位置',
    browser        VARCHAR(50)         DEFAULT '' COMMENT '浏览器类型',
    os             VARCHAR(50)         DEFAULT '' COMMENT '操作系统',
    status         bit                 DEFAULT b'0' COMMENT '状态: 0-成功 1-失败',
    failure_reason TEXT COMMENT '失败原因',
    time           BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '耗时（毫秒）',
    -- 自定义字段结束 --
    creator        BIGINT           DEFAULT NULL COMMENT '创建人',
    create_time    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater        BIGINT           DEFAULT NULL COMMENT '更新人',
    update_time    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version        INT UNSIGNED     DEFAULT 0 COMMENT '版本号',
    INDEX (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 10000 COMMENT = '操作日志';

CREATE TABLE IF NOT EXISTS vx_runtime_configuration
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    subject     VARCHAR(255) DEFAULT '' COMMENT '所属主题',
    config_name        VARCHAR(100) DEFAULT '' COMMENT '配置名称',
    config_key         VARCHAR(100) DEFAULT '' COMMENT '配置键',
    config_value       VARCHAR(512) COMMENT '配置值',
    config_description VARCHAR(512) COMMENT '配置描述',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 10000 COMMENT = '运行时配置';