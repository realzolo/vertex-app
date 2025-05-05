import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

/** @desc 查询角色列表 */
export function listRole(query: T.RoleQuery) {
  return http.get<T.RoleResp[]>(`/role/list`, query)
}

/** @desc 查询角色详情 */
export function getRole(id: number) {
  return http.get<T.RoleDetailResp>(`/role/${id}`)
}

/** @desc 新增角色 */
export function addRole(data: any) {
  return http.post(`/role`, data)
}

/** @desc 修改角色 */
export function updateRole(data: any, id: number) {
  return http.put(`/role/${id}`, data)
}

/** @desc 删除角色 */
export function deleteRole(ids: number | Array<number>) {
  return http.del(`/role/${ids}`)
}

/** @desc 修改角色权限 */
export function updateRolePermission(id: number, data: any) {
  return http.put(`/role/assign-permissions/${id}`, data)
}

/** @desc 查询角色关联用户 */
export function listRoleUser(id: number, query: T.RoleUserPageQuery) {
  return http.get<PagePack<T.RoleUserResp[]>>(`/role/users/${id}`, query)
}

/** @desc 分配角色给用户 */
export function assignToUsers(id: number, userIds: Array<string>) {
  return http.post(`/role/assign-role/${id}`, userIds)
}

/** @desc 取消分配角色给用户 */
export function unassignFromUsers(roleId: number, userIds: Array<number>) {
  return http.del(`/role/unassign-role/${roleId}`, userIds)
}

/** @desc 查询角色关联用户 ID */
export function listRoleUserId(id: number) {
  return http.get(`/role/${id}/user/id`)
}
