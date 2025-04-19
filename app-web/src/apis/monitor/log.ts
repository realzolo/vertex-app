import type * as T from './type'
import http from '@/utils/http'
import type { PageRes } from '@/types/api'

export type * from './type'

const BASE_URL = '/system/log'

/** @desc 查询日志列表 */
export function listLog(query: T.LogPageQuery) {
  return http.get<PageRes<T.LogResp[]>>(`/login-history/page`, query)
}

/** @desc 查询日志详情 */
export function getLog(id: string) {
  return http.get<T.LogDetailResp>(`/login-history/${id}`)
}

/** @desc 导出登录日志 */
export function exportLoginLog(query: T.LogQuery) {
  return http.download<any>(`${BASE_URL}/export/login`, query)
}

/** @desc 导出操作日志 */
export function exportOperationLog(query: T.LogQuery) {
  return http.download<any>(`${BASE_URL}/export/operation`, query)
}
