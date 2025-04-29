import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

/** @desc 查询在线用户列表 */
export function listOnlineUser(query: T.OnlineUserPageQuery) {
  return http.get<PagePack<T.OnlineUserResp[]>>(`/login-user/page`, query)
}

/** @desc 强退在线用户 */
export function kickout(userId: number) {
  return http.del(`/login-user/force-logout/${userId}`)
}
