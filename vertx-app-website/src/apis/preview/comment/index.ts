import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

const BASE_URL = '/open/app'

/** @desc 查询评论列表 */
export function listComment(query: T.CommentPageQuery) {
  return http.get<PagePack<T.CommentResp[]>>(`/comment/page`, query)
}

/** @desc 查询评论详情 */
export function getComment(id: string) {
  return http.get<T.CommentResp>(`/comment/${id}`)
}

/** @desc 新增评论 */
export function addComment(data: any) {
  return http.post(`/comment`, data)
}

/** @desc 修改评论 */
export function updateComment(data: any, id: string) {
  return http.put(`/comment/${id}`, data)
}

/** @desc 删除评论 */
export function deleteComment(id: string) {
  return http.del(`/comment/${id}`)
}

/** @desc 导出评论 */
export function exportComment(query: T.CommentQuery) {
  return http.download(`/comment/export`, query)
}
