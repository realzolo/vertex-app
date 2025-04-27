import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

const BASE_URL = '/vc'

/** @desc 获取图片验证码 */
export function getImageCaptcha(fingerprint: string) {
  return http.get<T.ImageCaptchaResp>(`/vc/digit-image`, { fingerprint })
}

/** @desc 获取短信验证码 */
export function getSmsCaptcha(phone: string, captchaReq: T.BehaviorCaptchaReq) {
  return http.get<boolean>(`/vc/sms?phone=${phone}&captchaVerification=${encodeURIComponent(captchaReq.captchaVerification || '')}`)
}

/** @desc 获取邮箱验证码 */
export function getEmailCaptcha(email: string) {
  return http.get<boolean>(`/vc/email`, { email })
}

/** @desc 获取行为验证码 */
export function getBehaviorCaptcha(req: any) {
  return http.get<T.BehaviorCaptchaResp>(`/vc/behavior`, req)
}

/** @desc 校验行为验证码 */
export function checkBehaviorCaptcha(req: any) {
  return http.post<T.CheckBehaviorCaptchaResp>(`/vc/behavior`, req)
}
