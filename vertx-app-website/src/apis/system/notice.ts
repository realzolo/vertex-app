import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

/** @desc 查询公告列表 */
export function listNotice(query: T.NoticePageQuery) {
  return http.get<PagePack<T.NoticeResp[]>>(`/notice/page`, query)
}

/** @desc 查询公告详情 */
export function getNotice(id: number) {
  return http.get<T.NoticeResp>(`/notice/${id}`)
}

/** @desc 新增公告 */
export function addNotice(data: any) {
  return http.post(`/notice`, data)
}

/** @desc 修改公告 */
export function updateNotice(data: any) {
  return http.put(`/notice`, data)
}

/** @desc 删除公告 */
export function deleteNotice(ids: number | Array<number>) {
  return http.del(`/notice/${ids}`)
}
