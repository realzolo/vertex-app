/** 用户类型 */
export interface UserInfo {
  // id: string
  // username: string
  // nickname: string
  // gender: 0 | 1 | 2
  // email: string
  // phone: string
  // avatar: string
  // pwdResetTime: string
  // pwdExpired: boolean
  // registrationDate: string
  // deptName: string
  // roles: string[]
  // permissions: string[]
  id: number | null,
  creator: number | null,
  createTime: string,
  updater:number |  null,
  updateTime: string,
  username: string,
  nickname: string,
  name: string,
  introduction: string,
  avatar: string,
  gender: 0 | 1 | 2,
  birthday: string,
  phone: string,
  email: string,
  roles: string[],
  permissions: string[],
  status: number
}

/** 路由类型 */
export interface RouteItem {
  id: string
  title: string
  parentId: string
  type: 1 | 2 | 3
  path: string
  name: string
  component: string
  redirect: string
  icon: string
  isExternal: boolean
  isHidden: boolean
  isCache: boolean
  permission: string
  roles: string[]
  sort: number
  status: 0 | 1
  children: RouteItem[]
  activeMenu: string
  alwaysShow: boolean
  breadcrumb: boolean
  showInTabs: boolean
  affix: boolean
}

/** 账号登录请求参数 */
export interface AccountLoginReq {
  username: string
  password: string
  captcha: string
  uuid: string
}

/** 手机号登录请求参数 */
export interface PhoneLoginReq {
  phone: string
  captcha: string
}

/** 邮箱登录请求参数 */
export interface EmailLoginReq {
  email: string
  captcha: string
}

// 登录响应类型
export interface LoginResp {
  user: {}
  jwt: {
    token: string,
    expire: number
  }
}

// 第三方登录授权类型
export interface SocialAuthAuthorizeResp {
  authorizeUrl: string
}
