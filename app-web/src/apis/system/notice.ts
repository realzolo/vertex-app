import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

/** @desc 查询公告列表 */
export function listNotice(query: T.NoticePageQuery) {
  return http.get<PagePack<T.NoticeResp[]>>(`/notice/page`, query)
}

/** @desc 查询公告详情 */
export function getNotice(id: string) {
  return http.get<T.NoticeResp>(`/notice/${id}`)
}

/** @desc 新增公告 */
export function addNotice(data: any) {
  return http.post(`/notice/create`, data)
}

/** @desc 修改公告 */
export function updateNotice(data: any, id: string) {
  return http.put(`/notice/${id}`, data)
}

/** @desc 删除公告 */
export function deleteNotice(ids: string | Array<number>) {
  return http.del(`/notice/${ids}`)
}
