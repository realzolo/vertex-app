import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

/** @desc 查询菜单列表 */
export function listMenu(query?: T.MenuQuery) {
  return http.get<TreeNode<T.MenuResp>[]>(`/permission/tree`, query)
}

/** @desc 查询菜单详情 */
export function getMenu(id: number) {
  return http.get<T.MenuResp>(`/permission/${id}`)
}

/** @desc 新增菜单 */
export function addMenu(data: any) {
  return http.post<boolean>(`/permission/add`, data)
}

/** @desc 修改菜单 */
export function updateMenu(data: any, id: number) {
  return http.put(`/permission/${id}`, data)
}

/** @desc 删除菜单 */
export function deleteMenu(id: number) {
  return http.del(`/permission/${id}`)
}

/** @desc 清除菜单缓存 */
export function clearMenuCache() {
  return http.del(`/permission/cache`)
}
