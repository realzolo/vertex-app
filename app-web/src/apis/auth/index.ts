import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

/** @desc 账号登录 */
export function accountLogin(req: T.AccountLoginReq) {
  return http.post<T.LoginResp>(`/auth/login`, req)
}

/** @desc 手机号登录 */
export function phoneLogin(req: T.PhoneLoginReq) {
  return http.post<T.LoginResp>(`/auth/login`, req)
}

/** @desc 邮箱登录 */
export function emailLogin(req: T.EmailLoginReq) {
  return http.post<T.LoginResp>(`/auth/email/login`, req)
}

/** @desc 三方账号登录 */
export function socialLogin(req: any) {
  return http.post<T.LoginResp>(`/auth/login`, req)
}

/** @desc 三方账号登录授权 */
export function socialAuth(source: string) {
  return http.get<T.SocialAuthAuthorizeResp>(`/auth/${source}`)
}

/** @desc 退出登录 */
export function logout() {
  return http.post(`/auth/logout`)
}

/** @desc 获取用户信息 */
export const getUserInfo = () => {
  return http.get<T.UserInfo>(`/user/me`)
}

/** @desc 获取路由信息 */
export const getUserRoute = () => {
  return http.get<T.RouteItem[]>(`/role/route`)
}
