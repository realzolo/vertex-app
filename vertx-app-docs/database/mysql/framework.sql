CREATE TABLE IF NOT EXISTS app_user
(
    id           BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    username     VARCHAR(25)  DEFAULT '' COMMENT '用户名',
    password     VARCHAR(100) DEFAULT '' COMMENT '密码',
    nickname     VARCHAR(25)  DEFAULT '' COMMENT '用户昵称',
    description  VARCHAR(255) DEFAULT '' COMMENT '用户描述',
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
  COMMENT = '用户';

CREATE TABLE IF NOT EXISTS app_role
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    name        VARCHAR(25)  COMMENT '角色名称',
    code        VARCHAR(25)  COMMENT '角色Code',
    sort        INT UNSIGNED DEFAULT 0 COMMENT '排序号',
    remark      VARCHAR(255) COMMENT '备注',
    status      INT UNSIGNED DEFAULT 0 COMMENT '(0: 正常, 1: 禁用)',
    -- 自定义字段结束 --
    creator        BIGINT           DEFAULT NULL COMMENT '创建人',
    create_time    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater        BIGINT           DEFAULT NULL COMMENT '更新人',
    update_time    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version        INT UNSIGNED     DEFAULT 0 COMMENT '版本号'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '用户角色';

CREATE TABLE IF NOT EXISTS app_permission
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    title       VARCHAR(25)     COMMENT '标题',
    parent_id   BIGINT          COMMENT '上级ID',
    type        INT  COMMENT    '类型',
    path        VARCHAR(100)    COMMENT '路由地址',
    name        VARCHAR(25)     COMMENT '组件名称',
    component   VARCHAR(100)    COMMENT '组件路径',
    redirect    VARCHAR(100)    COMMENT '重定向地址',
    icon        VARCHAR(1024)   COMMENT '图标',
    is_external INT  UNSIGNED DEFAULT 0 COMMENT '是否外链',
    is_cache    INT  UNSIGNED DEFAULT 0 COMMENT '是否缓存',
    is_hidden   INT  UNSIGNED DEFAULT 0 COMMENT '是否隐藏',
    permission  INT  UNSIGNED DEFAULT 0 COMMENT '权限标识',
    sort        INT  UNSIGNED DEFAULT 0 COMMENT '排序号',
    status      INT  UNSIGNED DEFAULT 0 COMMENT '账号状态',
    -- 自定义字段结束 --
    creator        BIGINT           DEFAULT NULL COMMENT '创建人',
    create_time    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater        BIGINT           DEFAULT NULL COMMENT '更新人',
    update_time    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version        INT UNSIGNED     DEFAULT 0 COMMENT '版本号'
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '权限(菜单)';

CREATE TABLE IF NOT EXISTS app_user_role
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    user_id     BIGINT          NOT NUll COMMENT '用户ID',
    role_id     BIGINT          NOT NUll COMMENT '角色ID',
    -- 自定义字段结束 --
    creator        BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater        BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version        INT UNSIGNED DEFAULT 0 COMMENT '版本号',
    INDEX (user_id, role_id),
    UNIQUE (user_id, role_id)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '用户-角色';

CREATE TABLE IF NOT EXISTS app_role_permission
(
    id             BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    role_id        BIGINT       NOT NUll COMMENT '角色ID',
    permission_id  BIGINT       NOT NUll COMMENT '权限ID',
    -- 自定义字段结束 --
    creator        BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater        BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version        INT UNSIGNED DEFAULT 0 COMMENT '版本号',
    INDEX (role_id, permission_id),
    UNIQUE (role_id, permission_id)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '角色-权限';

CREATE TABLE IF NOT EXISTS app_exception_log
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
    exception_root_cause_message TEXT COMMENT '异常导致的根消息',
    exception_message            TEXT COMMENT '异常导致的消息',
    -- 自定义字段结束 --
    creator        BIGINT        DEFAULT NULL COMMENT '创建人',
    create_time    DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater        BIGINT        DEFAULT NULL COMMENT '更新人',
    update_time    DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version        INT UNSIGNED  DEFAULT 0 COMMENT '版本号',
    INDEX (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = 'API错误日志';

CREATE TABLE IF NOT EXISTS app_operation_log
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
  COMMENT = '操作日志';

CREATE TABLE IF NOT EXISTS app_runtime_configuration
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    subject         VARCHAR(255) COMMENT '所属主题',
    name            VARCHAR(50)  COMMENT '配置名称',
    code            VARCHAR(50)  COMMENT '配置键',
    default_value   VARCHAR(512) COMMENT '配置默认值',
    value           VARCHAR(512) COMMENT '配置值',
    description     VARCHAR(512) COMMENT '配置描述',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '运行时配置';

CREATE TABLE IF NOT EXISTS app_dictionary
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    name        VARCHAR(25)   COMMENT '字典名称',
    value       VARCHAR(25)   COMMENT '字典值',
    color       VARCHAR(25)   COMMENT '颜色值',
    `group`     VARCHAR(25)   COMMENT '分组',
    remark      VARCHAR(50)   COMMENT '字典备注',
    builtin     BIT           DEFAULT b'0' COMMENT '是否内置',
    sort        INT UNSIGNED  DEFAULT 0    COMMENT '排序',
    type        BIT           DEFAULT b'0' COMMENT '字典类型(字典/枚举)',
    status      BIT           DEFAULT b'0' COMMENT '字典状态(0: 启用，1: 禁用)',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号'
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '数据字典';

CREATE TABLE IF NOT EXISTS app_storage_strategy
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    name        VARCHAR(25)   COMMENT '存储策略名称',
    code        VARCHAR(25)   COMMENT '存储策略Code',
    type        VARCHAR(25)   COMMENT '存储策略类型',
    access_key  VARCHAR(255)  COMMENT 'accessKey',
    secret_key  VARCHAR(255)  COMMENT 'secretKey',
    endpoint    VARCHAR(100)  COMMENT '端点',
    bucket_name VARCHAR(100)  COMMENT '存储桶',
    root_path   VARCHAR(255)  COMMENT '根路径',
    domain      VARCHAR(100)  COMMENT '域名',
    is_default     BIT           DEFAULT b'0' COMMENT '是否默认(1: 默认，0: 非默认)',
    remark      VARCHAR(255)  COMMENT '字典备注',
    sort        INT UNSIGNED  DEFAULT 0   COMMENT '排序',
    status      BIT           DEFAULT b'0' COMMENT '字典状态(0: 启用，1: 禁用)',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号'
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '存储策略';

CREATE TABLE IF NOT EXISTS app_file_record
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    name                VARCHAR(255)  COMMENT '文件名称',
    size                BIGINT        COMMENT '文件大小（字节)',
    url                 VARCHAR(512)  COMMENT 'URL',
    extension           VARCHAR(25)   COMMENT '文件后缀',
    type                VARCHAR(25)   COMMENT '文件类型',
    thumbnail_size      BIGINT        COMMENT '缩略图大小（字节)',
    thumbnail_url       VARCHAR(512)  COMMENT '缩略图URL',
    storage_strategy_id BIGINT        COMMENT '存储策略 ID',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号'
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '文件记录';

CREATE TABLE IF NOT EXISTS app_department
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    name                VARCHAR(255)  COMMENT '部门名称',
    parent_id           BIGINT        COMMENT '上级ID',
    ancestors           VARCHAR(255)  COMMENT '祖级列表',
    builtin             BIT           DEFAULT b'0' COMMENT '是否内置',
    remark              VARCHAR(255)  COMMENT '备注',
    sort                INT UNSIGNED  DEFAULT 0 COMMENT '排序号',
    status              INT UNSIGNED  DEFAULT 0 COMMENT '角色状态',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号'
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '部门';

CREATE TABLE IF NOT EXISTS app_user_department
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    user_id           BIGINT        COMMENT '用户ID',
    dept_id           BIGINT        COMMENT '部门ID',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号'
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '用户-部门';

CREATE TABLE IF NOT EXISTS app_notice
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    title       VARCHAR(255)  COMMENT '标题',
    content     LONGTEXT      COMMENT '内容',
    type        INT UNSIGNED  DEFAULT 0 COMMENT '类型',
    effective_time            datetime COMMENT '生效时间',
    terminate_time            datetime COMMENT '终止时间',
    notice_scope   INT  UNSIGNED COMMENT '通知范围',
    notice_users   JSON COMMENT '通知用户',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号',
    deleted     BIT          DEFAULT b'0' COMMENT '是否删除'
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '通知公告';

CREATE TABLE IF NOT EXISTS app_login_history
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    user_id           BIGINT        COMMENT '用户ID',
    login_type        VARCHAR(25)   COMMENT '登录类型',
    login_time        DATETIME      COMMENT '更新时间',
    ip                VARCHAR(25)   COMMENT '登录IP',
    browser           VARCHAR(255)  COMMENT '浏览器',
    os                VARCHAR(255)  COMMENT '操作系统',
    location          VARCHAR(255)  COMMENT '登录地址',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号'
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '用户登录历史';

CREATE TABLE IF NOT EXISTS app_approval_flow_template
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    name        VARCHAR(255)  COMMENT '流程名称',
    type        INT UNSIGNED  COMMENT '流程类型',
    content     LONGTEXT      COMMENT '流程内容',
    status      INT UNSIGNED  DEFAULT 0 COMMENT '流程状态',
    remark      VARCHAR(255)  COMMENT '备注',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号',
    deleted     BIT          DEFAULT b'0' COMMENT '是否删除'
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '审批流程模板';

CREATE TABLE IF NOT EXISTS app_approval_flow_binding_relation
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    flow_template_id     BIGINT       COMMENT '流程模板ID',
    business_type_code   VARCHAR(255) COMMENT '业务类型编码',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号',
    deleted     BIT          DEFAULT b'0' COMMENT '是否删除'
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '业务流程关联';

CREATE TABLE IF NOT EXISTS app_approval_flow_node_candidate
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    node_id                    BIGINT         COMMENT '节点ID',
    candidate_strategy         INT UNSIGNED   COMMENT '候选人策略',
    user_ids     JSON COMMENT '候选人ID列表',
    role_ids     JSON COMMENT '候选人角色ID列表',
    candidate_selection_type   INT UNSIGNED   COMMENT '候选人选择类型',
    candidate_selection_scope  INT UNSIGNED   COMMENT '候选人选择范围',
    approval_type              INT UNSIGNED   COMMENT '审批类型',
    unmanned_strategy          INT UNSIGNED   COMMENT '无审批人审批策略',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号'
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '审批节点候选人';

CREATE TABLE IF NOT EXISTS app_approval_flow_node
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    template_id  BIGINT        COMMENT '流程模板ID',
    node_id      VARCHAR(255)       COMMENT '节点ID',
    label        VARCHAR(255)  COMMENT '节点名称',
    type         VARCHAR(255)  COMMENT '节点类型',
    prev_node_id VARCHAR(255)      COMMENT '上一个节点ID',
    next_node_id VARCHAR(255)      COMMENT '下一个节点ID',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号'
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '审批流程节点';

CREATE TABLE IF NOT EXISTS app_comment
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    parent_id     BIGINT       COMMENT '父级ID',
    business_type VARCHAR(50)  COMMENT '模块类型',
    object_id     BIGINT       COMMENT '关联对象ID',
    content       TEXT         COMMENT '评论内容',
    address       VARCHAR(255) COMMENT '评论人IP地址',
    path          VARCHAR(255) COMMENT '搜索路径',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号',
    deleted     BIT          DEFAULT b'0' COMMENT '是否删除'
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '评论';

CREATE TABLE IF NOT EXISTS app_upvote_record
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    object_type   VARCHAR(50)  COMMENT '点赞对象类型',
    object_id     BIGINT       COMMENT '点赞对象ID',
    user_id       BIGINT       COMMENT '点赞用户ID',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号',
    -- 索引 --
    UNIQUE KEY uk_app_upvote_record(object_type, object_id, user_id),
    KEY idx_user_id(user_id)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '点赞明细';

CREATE TABLE IF NOT EXISTS app_upvote_summary
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    object_type   VARCHAR(50)  COMMENT '点赞对象类型',
    object_id     BIGINT       COMMENT '点赞对象ID',
    count         BIGINT       COMMENT '点赞数量',
    -- 自定义字段结束 --
    creator     BIGINT       DEFAULT NULL COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     BIGINT       DEFAULT NULL COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT UNSIGNED DEFAULT 0 COMMENT '版本号',
    -- 索引 --
    UNIQUE KEY uk_app_upvote_summary(object_type, object_id),
    CONSTRAINT count_non_negative CHECK (count >= 0)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '点赞汇总';