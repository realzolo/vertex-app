import type * as T from './type'
import http from '@/utils/http'

export type * from './type'

const BASE_URL = '/approval-flow'

/** 新建流程模板 */
export function createFlowTemplate(req: T.FlowTemplate) {
  return http.post<T.FlowTemplate>(`/approval-flow`, req)
}

/** 更新流程模板 */
export function updateFlowTemplate(req: T.FlowTemplate) {
  return http.put<T.FlowTemplate>(`/approval-flow`, req)
}

/** 删除流程模板 */
export function deleteFlowTemplate(id: string) {
  return http.del(`/approval-flow/${id}`)
}

/** 获取流程模板详情 */
export function getFlowTemplate(id: number) {
  return http.get<T.FlowTemplate>(`/approval-flow/${id}`)
}

/** 获取流程模板列表 */
export function listFlowTemplate(query: T.FlowTemplatePageQuery) {
  return http.get<T.FlowTemplatePageResp>(`/approval-flow/page`, query)
}

export function listFlowTemplateDict() {
  return http.get<T.FlowTemplatePageResp>(`/approval-flow/template/dict`)
}

export function bindFlowToBusinessType(req: T.FlowBindingRelation) {
  return http.post<T.FlowTemplatePageResp>(`/approval-flow/bind`, req)
}

export function unbindFlowToBusinessType(req: T.FlowBindingRelation) {
  return http.del<T.FlowTemplatePageResp>(`/approval-flow/unbind`, req)
}

export function listFlowTemplateBindRelations(req: T.FlowBindingRelationPageQuery) {
  return http.get<T.FlowTemplatePageResp>(`/approval-flow/bind/page`, req)
}

/**
 * 设置节点候选人
 * @param req
 */
export function setCandidates(req: T.FlowNodeCandidatesReq) {
  return http.post<T.FlowTemplatePageResp>(`/approval-flow/candidates`, req)
}

/**
 * 获取节点详情
 * @param nodeId
 */
export function getNodeDetails(nodeId: string) {
  return http.get<T.FlowTemplatePageResp>(`/approval-flow/node`, {nodeId})
}
