import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

const BASE_URL = '/storage/platform'

/** @desc 查询存储列表 */
export function listStorage(query: T.StorageQuery) {
  return http.get<T.StorageResp[]>(`/storage/platform/list`, query)
}

/** @desc 查询存储详情 */
export function getStorage(id: string) {
  return http.get<T.StorageResp>(`/storage/platform/${id}`)
}

/** @desc 新增存储 */
export function addStorage(data: any) {
  return http.post(`/storage/platform`, data)
}

/** @desc 修改存储 */
export function updateStorage(data: any, id: string) {
  return http.put(`/storage/platform/${id}`, data)
}

/** @desc 删除存储 */
export function deleteStorage(id: string) {
  return http.del(`/storage/platform/${id}`)
}

/** @desc 修改存储状态 */
export function updateStorageStatus(data: any, id: string) {
  return http.put(`/storage/platform/${id}/status`, data)
}

/** @desc 设置默认存储 */
export function setDefaultStorage(id: string) {
  return http.put(`/storage/platform/${id}/default`)
}
