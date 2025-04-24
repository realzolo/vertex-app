import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

/** @desc 查询参数列表 */
export function listOption(subject: string) {
  return http.get<DataPairRecord[]>(`/config/list/${subject}`)
}

/** @desc 修改参数 */
export function updateOption(data: any) {
  return http.put(`/config`, data)
}

/** @desc 重置参数 */
export function resetOptionValue(query: T.OptionQuery) {
  return http.patch(`/config/value`, query)
}
