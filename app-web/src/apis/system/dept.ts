import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

const BASE_URL = '/department'

/** @desc 查询部门列表 */
export function listDept(query: T.DeptQuery) {
  return http.get<T.DeptResp[]>(`${BASE_URL}/tree`, query)
}

/** @desc 查询部门详情 */
export function getDept(id: number) {
  return http.get<T.DeptResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增部门 */
export function addDept(data: any) {
  return http.post<boolean>(`${BASE_URL}`, data)
}

/** @desc 修改部门 */
export function updateDept(data: any, id: number) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除部门 */
export function deleteDept(id: number) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出部门 */
export function exportDept(query: T.DeptQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
