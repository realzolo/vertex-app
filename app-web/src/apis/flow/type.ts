export interface FowGraph {
  id?: number
  name?: string
  content: string
  status?: number
  remark?: string
  createTime?: string
  updateTime?: string
}

export interface FlowGraphPageQuery {
  name?: string
  sort: Array<string>
}

export interface FlowGraphPageResp {
}