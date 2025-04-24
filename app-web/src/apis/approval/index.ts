import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

const BASE_URL = '/approval-flow'

/** 新建流程模板 */
export function createFlowTemplate(req: T.FlowTemplate) {
  return http.post<T.FlowTemplate>(`${BASE_URL}`, req)
}

/** 更新流程模板 */
export function updateFlowTemplate(req: T.FlowTemplate) {
  return http.put<T.FlowTemplate>(`${BASE_URL}`, req)
}

/** 删除流程模板 */
export function deleteFlowTemplate(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** 获取流程模板详情 */
export function getFlowTemplate(id: number) {
  return http.get<T.FlowTemplate>(`${BASE_URL}/${id}`)
}

/** 获取流程模板列表 */
export function listFlowTemplate(query: T.FlowTemplatePageQuery) {
  return http.get<T.FlowTemplatePageResp>(`${BASE_URL}/page`, query)
}

export function listFlowTemplateDict() {
  return http.get<T.FlowTemplatePageResp>(`${BASE_URL}/template/dict`)
}

export function bindFlowToBusinessType(req: T.FlowBindingRelation) {
  return http.post<T.FlowTemplatePageResp>(`${BASE_URL}/bind`, req)
}

export function unbindFlowToBusinessType(req: T.FlowBindingRelation) {
  return http.del<T.FlowTemplatePageResp>(`${BASE_URL}/unbind`, req)
}

export function listFlowTemplateBindRelations(req: T.FlowBindingRelationPageQuery) {
  return http.get<T.FlowTemplatePageResp>(`${BASE_URL}/bind/page`, req)
}