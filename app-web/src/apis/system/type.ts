/** 用户类型 */
export interface UserResp {
  id: string
  username: string
  nickname: string
  avatar: string
  gender: 0 | 1
  email: string
  phone: string
  introduction: string
  status: 0 | 1 | 2 | 3
  builtin?: boolean
  createTime: string
  updateTime: string
  department: DataPairRecord
  roles: Array<DataPairRecord>
  description: string
}

export type UserDetailResp = UserResp & {
  pwdResetTime?: string
}

export interface UserImportResp {
  importKey: string
  totalRows: number
  validRows: number
  duplicateUserRows: number
  duplicateEmailRows: number
  duplicatePhoneRows: number
}

export interface UserQuery {
  description?: string
  status?: number
  createTime?: Array<string>
  departmentId?: number
  userIds?: Array<number>
  roleId?: number
}

export interface UserPageQuery extends UserQuery, PageQuery {
}

/** 角色类型 */
export interface RoleResp {
  id: string
  name: string
  code: string
  sort: number
  description: string
  dataScope: number
  builtin: boolean
  createUserString: string
  createTime: string
  updateUserString: string
  updateTime: string
  disabled: boolean
}

export type RoleDetailResp = RoleResp & {
  menuIds: Array<number>
  deptIds: Array<number>
  menuCheckStrictly: boolean
  deptCheckStrictly: boolean
}

export interface RoleUserResp {
  id: number
  username: string
  nickname: string
  gender: 0 | 1 | 2
  description: string
  status: 0 | 1
  builtin?: boolean
  departmentId: string
  department: DataPairRecord
  roles: Array<DataPairRecord>
  disabled: boolean
}

export interface RoleQuery {
  description?: string
  sort: Array<string>
}

export interface RoleUserQuery {
  description?: string
  sort: Array<string>
}

export interface RoleUserPageQuery extends RoleUserQuery, PageQuery {
}

/** 菜单类型 */
export interface MenuResp {
  id: number
  title: string
  parentId: number
  type: 1 | 2 | 3
  path: string
  name: string
  component: string
  redirect: string
  icon: string
  isExternal: boolean
  isCache: boolean
  isHidden: boolean
  permission: string
  sort: number
  status: 0 | 1
  createTime: string
  updateTime: string
}

export interface MenuQuery {
  title?: string
  status?: number
}

/** 部门类型 */
export interface DeptResp {
  id: number
  name: string
  sort: number
  status: 0 | 1
  builtin: boolean
  description: string
  createTime: string
  updateTime: string
  parentId: number
}

export interface DeptQuery {
  description?: string
  status?: number
}

/** 字典类型 */
export interface DictResp {
  id: number
  name: string
  value: string
  builtin: boolean
  type: number
  remark: string
  createTime: string
  updateTime: string
}

export interface DictQuery {
  description?: string
  sort: Array<string>
}

export interface DictItemResp {
  id: number
  name: string
  value: string
  color: string
  sort: number
  remark: string
  type: string
  status: 0 | 1
  builtin: boolean
  groupId: number
  createTime: string
  updateTime: string
}

export interface DictItemQuery {
  remark?: string
  status?: number
  sort: Array<string>
  groupId: number
}

export interface DictItemPageQuery extends DictItemQuery, PageQuery {
}

/** 公告类型 */
export interface NoticeResp {
  id?: string
  title?: string
  content: string
  status?: number
  type?: string
  effectiveTime?: string
  terminateTime?: string
  noticeScope?: number
  noticeUsers?: Array<string>
  createUserString?: string
  createTime?: string
  updateUserString?: string
  updateTime?: string
}

export interface NoticeQuery {
  title?: string
  type?: string
  sort: Array<string>
}

export interface NoticePageQuery extends NoticeQuery, PageQuery {
}

/** 文件类型 */
export interface FileItem {
  id: string
  name: string
  size: number
  url: string
  parentPath: string
  absPath: string
  metadata: string
  md5: string
  contentType: string
  thumbnailSize: number
  thumbnailUrl: string
  thumbnailMetadata: string
  extension: string
  type: number
  storageId: string
  storageName: string
  createUserString: string
  createTime: string
  updateUserString: string
  updateTime: string
}

/** 文件资源统计信息 */
export interface FileStatisticsResp {
  type: string
  size: any
  number: number
  unit: string
  data: Array<FileStatisticsResp>
}

export interface FileQuery {
  name?: string
  type?: string
  absPath?: string
  sort: Array<string>
}

export interface FilePageQuery extends FileQuery, PageQuery {
}

/** 存储类型 */
export interface StorageResp {
  id: string
  name: string
  code: string
  type: number
  accessKey: string
  secretKey: string
  endpoint: string
  bucketName: string
  domain: string
  description: string
  isDefault: boolean
  sort: number
  status: number
  createUserString: string
  createTime: string
  updateUserString: string
  updateTime: string
}

export interface StorageQuery {
  description?: string
  type?: number
  sort: Array<string>
}

/** 终端类型 */
export interface ClientResp {
  id: string
  clientId: string
  clientKey: string
  clientSecret: string
  authType: string
  clientType: string
  activeTimeout: string
  timeout: string
  status: string
  createUser: string
  createTime: string
  updateUser: string
  updateTime: string
  createUserString: string
  updateUserString: string
}

export interface ClientDetailResp {
  id: string
  clientId: string
  clientKey: string
  clientSecret: string
  authType: string
  clientType: string
  activeTimeout: string
  timeout: string
  status: string
  createUser: string
  createTime: string
  updateUser: string
  updateTime: string
  createUserString: string
  updateUserString: string
}

export interface ClientQuery {
  clientKey: string
  clientSecret: string
  authType: string[]
  clientType: string
  status: string
  sort: Array<string>
}

export interface ClientPageQuery extends ClientQuery, PageQuery {
}

/** 系统参数类型 */
export interface OptionResp {
  id: string
  name: string
  code: string
  value: string
  description: string
}

export interface OptionQuery {
  subject: string
}

/** 基础配置类型 */
export interface BasicConfig {
  SITE_FAVICON: string
  SITE_LOGO: string
  SITE_TITLE: string
  SITE_COPYRIGHT: string
  SITE_BEIAN: string
}

/** 基础配置类型 */
export interface SiteConfig {
  SITE_FAVICON: DataPairRecord | string
  SITE_LOGO: DataPairRecord | string
  SITE_TITLE: DataPairRecord | string
  SITE_DESCRIPTION: DataPairRecord | string
  SITE_COPYRIGHT: DataPairRecord | string
  SITE_BEIAN: DataPairRecord | string
}

/** 安全配置类型 */
export interface SecurityConfig {
  PASSWORD_ERROR_LOCK_COUNT: DataPairRecord
  PASSWORD_ERROR_LOCK_MINUTES: DataPairRecord
  PASSWORD_EXPIRATION_DAYS: DataPairRecord
  PASSWORD_EXPIRATION_WARNING_DAYS: DataPairRecord
  PASSWORD_REPETITION_TIMES: DataPairRecord
  PASSWORD_MIN_LENGTH: DataPairRecord
  PASSWORD_ALLOW_CONTAIN_USERNAME: DataPairRecord
  PASSWORD_REQUIRE_SYMBOLS: DataPairRecord
}

/** 邮箱配置类型 */
export interface MailConfig {
  MAIL_PROTOCOL: DataPairRecord
  MAIL_HOST: DataPairRecord
  MAIL_PORT: DataPairRecord
  MAIL_USERNAME: DataPairRecord
  MAIL_PASSWORD: DataPairRecord
  MAIL_SSL_ENABLED: DataPairRecord
  MAIL_SSL_PORT: DataPairRecord
}

/** 登录配置类型 */
export interface LoginConfig {
  LOGIN_CAPTCHA_ENABLED: DataPairRecord
}

/** 短信配置类型 */
export interface SmsConfigResp {
  id: string
  name: string
  supplier: string
  accessKey: string
  secretKey: string
  signature: string
  templateId: string
  weight: string
  retryInterval: string
  maxRetries: string
  maximum: string
  supplierConfig: string
  status: number
  createUser: string
  createTime: string
  updateUser: string
  updateTime: string
  createUserString: string
  updateUserString: string
  disabled: boolean
}

export interface SmsConfigQuery {
  name: string | undefined
  supplier: string | undefined
  accessKey: string | undefined
  sort: Array<string>
}

export interface SmsConfigPageQuery extends SmsConfigQuery, PageQuery {
}

/** 短信日志类型 */
export interface SmsLogResp {
  id: string
  configId: string
  phone: string
  params: string
  status: number
  resMsg: string
  createUser: string
  createTime: string
  updateUser: string
  updateTime: string
  createUserString: string
  updateUserString: string
}

export interface SmsLogQuery {
  configId: string | undefined
  phone: string | undefined
  status: number | undefined
  sort: Array<string>
}

export interface SmsLogPageQuery extends SmsLogQuery, PageQuery {
}

/** 绑定三方账号信息 */
export interface BindSocialAccountRes {
  source: string
  description: string
}

/** 系统消息类型 */
export interface MessageResp {
  id: string
  title: string
  content: string
  type: number
  isRead: boolean
  readTime?: string
  createUserString?: string
  createTime: string
}

export interface MessageQuery {
  title?: string
  type?: number
  isRead?: boolean
  sort: Array<string>
}

export interface MessagePageQuery extends MessageQuery, PageQuery {
}
