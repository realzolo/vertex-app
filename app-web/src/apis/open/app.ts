import http from '@/utils/http'

const BASE_URL = '/open/app'

export interface AppResp {
  id: string
  name: string
  appKey: string
  status: string
  expirationTime: string
  appDesc: string
  createUserString: string
  updateUserString: string
}
export interface AppDetailResp {
  id: string
  name: string
  appKey: string
  status: string
  expirationTime: string
  appDesc: string
  createTime: string
  updateUser: string
  updateTime: string
  createUserString: string
  updateUserString: string
}
export interface AppQuery {
  name: string
  appKey: string
  sort: Array<string>
}
export interface AppPageQuery extends AppQuery, PageQuery {}

/** @desc 查询应用列表 */
export function listApp(query: AppPageQuery) {
  return http.get<PageRes<AppResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询应用详情 */
export function getApp(id: string) {
  return http.get<AppDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增应用 */
export function addApp(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改应用 */
export function updateApp(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除应用 */
export function deleteApp(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出应用 */
export function exportApp(query: AppQuery) {
  return http.download(`${BASE_URL}/export`, query)
}

/** @desc 查看AK */
export function getAppSecret(id: string) {
  return http.get(`${BASE_URL}/${id}/appsecret`)
}

/** @desc 刷新AK */
export function refreshAppSecret(id: string) {
  return http.get(`${BASE_URL}/${id}/refreshas`)
}
