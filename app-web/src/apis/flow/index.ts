import type * as T from './type'
import http from '@/utils/http'

const BASE_URL = '/flow-graph'

/** 新建流程图 */
export function createFlowGraph(req: T.FowGraph) {
  return http.post<T.FowGraph>(`${BASE_URL}`, req)
}

/** 更新流程图 */
export function updateFlowGraph(req: T.FowGraph) {
  return http.put<T.FowGraph>(`${BASE_URL}`, req)
}

/** 删除流程图 */
export function deleteFlowGraph(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** 获取流程图详情 */
export function getFlowGraph(id: number) {
  return http.get<T.FowGraph>(`${BASE_URL}/${id}`)
}

/** 获取流程图列表 */
export function listFlowGraph(query: T.FlowGraphPageQuery) {
  return http.get<T.FlowGraphPageResp>(`${BASE_URL}/page`, query)
}
