import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

const BASE_URL = '/system/dict'

/** @desc 查询字典列表 */
export function groupDict(query: T.DictQuery) {
  return http.get<T.DictResp[]>(`/dictionary/groups`, query)
}

/** @desc 查询字典详情 */
export function getDict(id: string) {
  return http.get<T.DictResp>(`/dictionary/${id}`)
}

/** @desc 新增字典 */
export function addDict(data: any) {
  return http.post(`/dictionary`, data)
}

/** @desc 修改字典 */
export function updateDict(data: any, id: string) {
  return http.put(`/dictionary/${id}`, data)
}

/** @desc 删除字典 */
export function deleteDict(id: string) {
  return http.del(`/dictionary/${id}`)
}

/** @desc 查询字典项列表 */
export function listDictItem(query: T.DictItemPageQuery) {
  return http.get<PageRes<T.DictItemResp[]>>(`/dictionary/items`, query)
}

/** @desc 查询字典项详情 */
export function getDictItem(id: number) {
  return http.get<T.DictItemResp>(`/dictionary/${id}`)
}

/** @desc 新增字典项 */
export function addDictItem(data: any) {
  return http.post(`/dictionary`, data)
}

/** @desc 修改字典项 */
export function updateDictItem(data: any, id: string) {
  return http.put(`/dictionary/${id}`, data)
}

/** @desc 删除字典项 */
export function deleteDictItem(id: number) {
  return http.del(`/dictionary/${id}`)
}
