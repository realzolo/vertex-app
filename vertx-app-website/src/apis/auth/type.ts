/** 用户类型 */
export interface UserInfo {
  id: string
  username: string
  nickname: string
  gender: 0 | 1 | 2
  email: string
  phone: string
  avatar: string
  pwdResetTime: string
  pwdExpired: boolean
  registrationDate: string
  department: DataPairRecord | null
  roles: Array<DataPairRecord>
  permissions: string[]
  createTime: string
}

/** 路由类型 */
export interface RouteItem {
  id: string
  title: string
  parentId: string
  icon: string
  children: RouteItem[]
  data: {
    type: 1 | 2 | 3
    path: string
    name: string
    component: string
    redirect: string
    isExternal: boolean
    isHidden: boolean
    isCache: boolean
    permission: string
    roles: string[]
    sort: number
    status: 0 | 1
    activeMenu: string
    alwaysShow: boolean
    breadcrumb: boolean
    showInTabs: boolean
    affix: boolean
  }
}

/** 认证类型 */
export type AuthType = 'ACCOUNT' | 'PHONE' | 'EMAIL' | 'SOCIAL'

export const AuthTypeConstants = {
  ACCOUNT: 'ACCOUNT',
  PHONE: 'PHONE',
  EMAIL: 'EMAIL',
  SOCIAL: 'SOCIAL',
} as const

/** 基础认证请求接口 */
export interface AuthReq {
  clientId?: string
  authType?: AuthType
}

/** 账号登录请求参数 */
export interface AccountLoginReq extends AuthReq {
  username: string
  password: string
  verificationCode: string
  fingerprint: string
}

/** 手机号登录请求参数 */
export interface PhoneLoginReq extends AuthReq {
  phone: string
  verificationCode: string
}

/** 邮箱登录请求参数 */
export interface EmailLoginReq extends AuthReq {
  email: string
  verificationCode: string
}

/** 登录响应类型 */
export interface LoginResp {
  user: object
  jwt: {
    token: string
    expire: number
  }
}

/** 第三方登录授权类型 */
export interface SocialAuthAuthorizeResp {
  authorizeUrl: string
}
