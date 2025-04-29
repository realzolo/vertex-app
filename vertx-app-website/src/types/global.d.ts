interface AnyObject {
  [key: string]: unknown
}

interface Options {
  label: string
  value: unknown
}

interface DictionaryEntry {
  label: string
  value: string | number
  color?: string
  disabled?: boolean
  extra?: unknown
}

interface DataPairRecord {
  id: string
  name: string
  code?: string
  value?: string | number
  description?: string
}

declare global{
  type Recordable<T = any> = Record<string, T>
}

/** 状态（1：启用；2：禁用） */
type Status = 1 | 2

/** 性别（1：男；2：女；0：未知） */
type Gender = 1 | 2 | 0

interface TreeNode<T> {
  id: number
  parentId: number
  path: string
  title: string
  icon: string
  children: TreeNode[]
  data: T
}
