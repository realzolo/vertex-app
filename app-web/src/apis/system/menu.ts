import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

const BASE_URL = '/system/menu'

/** @desc 查询菜单列表 */
export function listMenu(query: T.MenuQuery) {
  return http.get<T.MenuResp[]>(`/permission/tree`, query)
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
