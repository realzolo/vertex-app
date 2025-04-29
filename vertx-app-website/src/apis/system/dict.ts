import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

/** @desc 查询字典列表 */
export function listDictGroups(query?: T.DictQuery) {
  return http.get<T.DictResp[]>(`/dictionary/groups`, query)
}

/** @desc 查询字典详情 */
export function getDictGroup(id: number) {
  return http.get<T.DictResp>(`/dictionary/${id}`)
}

/** @desc 新增字典 */
export function createDictGroup(data: any) {
  return http.post(`/dictionary`, data)
}

/** @desc 修改字典 */
export function updateDictGroup(data: any) {
  return http.put(`/dictionary`, data)
}

/** @desc 删除字典 */
export function deleteDictGroup(id: number) {
  return http.del(`/dictionary/${id}`)
}

/** @desc 清除字典缓存 */
export function clearDictCache(code: number) {
  return http.del(`/dictionary/cache/${code}`)
}

/** @desc 查询字典项列表 */
export function listDictItem(query: T.DictItemPageQuery) {
  return http.get<PagePack<T.DictItemResp[]>>(`/dictionary/items`, query)
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
export function updateDictItem(data: any, id: number) {
  return http.put(`/dictionary/${id}`, data)
}

/** @desc 删除字典项 */
export function deleteDictItem(id: number) {
  return http.del(`/dictionary/${id}`)
}
