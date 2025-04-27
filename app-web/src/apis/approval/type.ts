export interface FlowTemplate {
  id?: number
  name?: string
  content: string
  status?: number
  remark?: string
  createTime?: string
  updateTime?: string
}

export interface FlowTemplatePageQuery {
  name?: string
  sort: Array<string>
}

export interface FlowTemplatePageResp {
}

export interface FlowBindingRelation {
  id?: number
  flowTemplateId: number
  businessTypeCode: string
}

export interface FlowBindingRelationPageQuery {
  name?: string
  sort: Array<string>
}

export interface FlowNodeCandidatesReq {
  nodeId?: number
  candidateStrategy: number
  userIds: number[]
  roleIds: number[]
  candidateSelectionType: number
  candidateSelectionScope: number
  approvalType: number
  unmannedStrategy: number
}
