import approvalIcon from './assets/icon-node-approver.svg?raw'
import ccIcon from './assets/icon-node-cc.svg?raw'
import { NodeType } from '@/views/approval/type'

export const STRATEGIES = [
  { label: '指定成员', value: 1 },
  { label: '发起人自己', value: 2 },
  { label: '发起人自选', value: 3 },
  { label: '指定角色', value: 4 },
]
export const SELF_SELECTION = [
  { label: '自选一个人', value: 1 },
  { label: '自选多个人', value: 2 },
]
export const SELECTION_SCOPE = [
  { label: '全公司', value: 1 },
  { label: '指定成员', value: 2 },
  { label: '指定角色', value: 3 },
]
export const APPROVAL_TYPES = [
  { label: '依次处理（按办理人顺序处理）', value: 1 },
  { label: '会签（须所有办理人处理）', value: 2 },
  { label: '或签（一名办理人处理即可）', value: 3 },
]
export const EMPTY_PERSON_STRATEGY = [
  { label: '自动通过', value: 1 },
  { label: '自动转交管理员', value: 2 },
  { label: '指定人员审批', value: 3 },
]
export const FLOW_NODE_OPTIONS = [
  { name: '审批人', value: NodeType.APPROVER, icon: approvalIcon, background: '#875BF7' },
  { name: '抄送人', value: NodeType.CC, icon: ccIcon, background: '#06AED4' },
]
