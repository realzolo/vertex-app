import type { TreeNodeData } from '@arco-design/web-vue'
import http from '@/utils/http'

/** @desc 查询部门树 */
export function listDeptTree(query: { description: string | unknown }) {
  return http.get<TreeNodeData[]>(`/department/public/tree`, query)
}

/** @desc 查询菜单树 */
export function listMenuTree(query: { description: string }) {
  return http.get<TreeNodeData[]>(`/permission/tree`, query)
}

/** @desc 查询用户列表 */
export function listUserDict(query?: { status: number }) {
  return http.get<DictionaryEntry[]>(`/user/dict`, query)
}

/** @desc 查询角色列表 */
export function listRoleDict(query?: { name: string, status: number }) {
  return http.get<DictionaryEntry[]>(`/role/dict`, query)
}

/** @desc 查询字典列表 */
export function listCommonDict(group: string) {
  return http.get<DictionaryEntry[]>(`/dictionary`, { group })
}

/** @desc 查询系统配置参数 */
export function listSiteOptionDict() {
  return http.get<DictionaryEntry[]>(`/config/site/dict`)
}

/** @desc 上传文件 */
export function uploadFile(data: FormData) {
  return http.post(`/storage/file`, data)
}
