import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

const BASE_URL = '/schedule/job'

/** @desc 查询任务组列表 */
export function listGroup() {
  return http.get(`/schedule/job/group`)
}

/** @desc 查询任务列表 */
export function listJob(query: T.JobPageQuery) {
  return http.get<PagePack<T.JobResp[]>>(`/schedule/job/page`, query)
}

/** @desc 新增任务 */
export function addJob(data: any) {
  return http.post(`/schedule/job`, data)
}

/** @desc 修改任务 */
export function updateJob(data: any, id: number) {
  return http.put(`/schedule/job/${id}`, data)
}

/** @desc 修改任务状态 */
export function updateJobStatus(data: any, id: number) {
  return http.patch(`/schedule/job/${id}/status`, data)
}

/** @desc 删除任务 */
export function deleteJob(id: number) {
  return http.del(`/schedule/job/${id}`)
}

/** @desc 执行任务 */
export function triggerJob(id: number) {
  return http.post(`/schedule/job/trigger/${id}`)
}
