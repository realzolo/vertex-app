import type * as T from './type'
import http from '@/utils/http'

const BASE_URL = '/user/profile'

/** @desc 上传头像 */
export function uploadAvatar(data: FormData) {
  return http.patch(`/user/profile/avatar`, data)
}

/** @desc 修改用户基本信息 */
export function updateUserBaseInfo(data: { nickname: string, gender: number }) {
  return http.patch(`/user/profile/basic/info`, data)
}

/** @desc 修改密码 */
export function updateUserPassword(data: { oldPassword: string, newPassword: string }) {
  return http.patch(`/user/profile/password`, data)
}

/** @desc 修改手机号 */
export function updateUserPhone(data: { phone: string, captcha: string, oldPassword: string }) {
  return http.patch(`/user/profile/phone`, data)
}

/** @desc 修改邮箱 */
export function updateUserEmail(data: { email: string, captcha: string, oldPassword: string }) {
  return http.patch(`/user/profile/email`, data)
}

/** @desc 获取绑定的三方账号 */
export function listUserSocial() {
  return http.get<T.BindSocialAccountRes[]>(`/user/profile/social`)
}

/** @desc 绑定三方账号 */
export function bindSocialAccount(source: string, data: any) {
  return http.post(`/user/profile/social/${source}`, data)
}

/** @desc 解绑三方账号 */
export function unbindSocialAccount(source: string) {
  return http.del(`/user/profile/social/${source}`)
}
