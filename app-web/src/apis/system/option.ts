import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

const BASE_URL = '/runtime-configuration'

/** @desc 查询参数列表 */
export function listOption(subject: string) {
  return http.get<T.OptionResp[]>(`${BASE_URL}/list/${subject}`)
}

/** @desc 修改参数 */
export function updateOption(data: any) {
  return http.put(`${BASE_URL}`, data)
}

/** @desc 重置参数 */
export function resetOptionValue(subject: string) {
  return http.patch(`${BASE_URL}/reset/${subject}`)
}
