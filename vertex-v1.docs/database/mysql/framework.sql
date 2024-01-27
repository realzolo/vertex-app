CREATE TABLE vx_user
(
    id           BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    code         VARCHAR(50)  default '' COMMENT '用户编码',
    agency_code  BIGINT       DEFAULT NULL COMMENT '所属组织编码',
    username     VARCHAR(25)  default '' COMMENT '用户名',
    password     VARCHAR(100) default '' COMMENT '密码',
    nickname     VARCHAR(25)  default '' COMMENT '用户昵称',
    name         VARCHAR(25)  default '' COMMENT '用户姓名',
    introduction VARCHAR(255) default '' COMMENT '用户简介',
    avatar       VARCHAR(255) default '' COMMENT '头像地址',
    gender       INT UNSIGNED default 0 COMMENT '性别',
    birthday     DATE         default NULL COMMENT '生日',
    phone        VARCHAR(25)  default '' COMMENT '电话号码',
    email        VARCHAR(50)  default '' COMMENT '电子邮箱',
    pwd_exp_date DATE         default NULL COMMENT '密码过期时间',
    status       INT UNSIGNED default 0 COMMENT '账号状态',
    -- 自定义字段结束 --
    creator      VARCHAR(50)  default '' COMMENT '创建人',
    create_time  DATETIME COMMENT '创建时间',
    updater      VARCHAR(50)  default '' COMMENT '更新人',
    update_time  DATETIME COMMENT '更新时间',
    version      INT UNSIGNED default 0 COMMENT '版本号',
    deleted      bit          default b'0' COMMENT '是否删除'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 10001 COMMENT ='用户';

CREATE TABLE vx_agency
(
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    code        VARCHAR(50)  default '' COMMENT '组织编码',
    name        VARCHAR(25)  default '' COMMENT '组织名称',
    parent_code VARCHAR(50)  default '' COMMENT '父级编码',
    description VARCHAR(255) default '' COMMENT '组织简介',
    level       INT UNSIGNED default 0 COMMENT '组织级别',
    search_path VARCHAR(255) default '' COMMENT '搜索路径',
    sort        INT UNSIGNED default 0 COMMENT '排序号',
    status      INT UNSIGNED default 0 COMMENT '组织状态',
    -- 自定义字段结束 --
    creator     VARCHAR(50)  default '' COMMENT '创建人',
    create_time DATETIME COMMENT '创建时间',
    updater     VARCHAR(50)  default '' COMMENT '更新人',
    update_time DATETIME COMMENT '更新时间',
    version     INT UNSIGNED default 0 COMMENT '版本号',
    deleted     bit          default b'0' COMMENT '是否删除'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 10000 COMMENT ='组织机构';

CREATE TABLE vx_api_exception_log
(
    id                           BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 自定义字段开始 --
    user_id                      BIGINT       DEFAULT NULL COMMENT '用户ID',
    request_method               VARCHAR(50)  default '' COMMENT '请求方法名',
    request_url                  VARCHAR(255) default '' COMMENT '访问地址',
    request_params               VARCHAR(255) default '' COMMENT '请求参数',
    user_ip                      VARCHAR(50)  default '' COMMENT '用户IP',
    user_agent                   VARCHAR(255) default '' COMMENT '浏览器UA',
    exception_name               VARCHAR(255) default '' COMMENT '异常名',
    exception_class_name         VARCHAR(255) default '' COMMENT '异常发生的类全名',
    exception_file_name          VARCHAR(255) default '' COMMENT '异常发生的类文件',
    exception_method_name        VARCHAR(255) default '' COMMENT '异常发生的方法名',
    exception_line_number        INT UNSIGNED COMMENT '异常发生的方法所在行',
    exception_stack_trace        VARCHAR(255) default '' COMMENT '异常的栈轨迹异常的栈轨迹',
    exception_root_cause_message VARCHAR(255) default '' COMMENT '异常导致的根消息',
    exception_message            VARCHAR(255) default '' COMMENT '异常导致的消息',
    -- 自定义字段结束 --
    creator                      VARCHAR(50)  default '' COMMENT '创建人',
    create_time                  DATETIME COMMENT '创建时间',
    updater                      VARCHAR(50)  default '' COMMENT '更新人',
    update_time                  DATETIME COMMENT '更新时间',
    version                      INT UNSIGNED default 0 COMMENT '版本号',
    deleted                      bit          default b'0' COMMENT '是否删除'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 10000 COMMENT ='API错误日志';