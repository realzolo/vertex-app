import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

/** @desc 查询评论列表 */
export function listComment(query: T.CommentPageQuery) {
  return http.get<PagePack<T.CommentResp[]>>(`/comment/page`, query)
}

/** @desc 查询评论详情 */
export function getComment(id: number) {
  return http.get<T.CommentResp>(`/comment/${id}`)
}

/** @desc 新增评论 */
export function addComment(data: any) {
  return http.post(`/comment`, data)
}

/** @desc 修改评论 */
export function updateComment(data: any, id: number) {
  return http.put(`/comment/${id}`, data)
}

/** @desc 删除评论 */
export function deleteComment(id: number) {
  return http.del(`/comment/${id}`)
}

/** @desc 点赞 */
export function upvote(id: number) {
  return http.post(`/comment/${id}/upvote`)
}
