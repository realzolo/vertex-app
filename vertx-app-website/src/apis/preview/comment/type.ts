import type { UserResp } from '@/apis'

/** 评论类型 */
export interface CommentReq {
  parentId: number | null
  objectId: number
  content: string
}

export interface CommentResp {
  id: number
  parentId: number | null
  businessType: string
  objectId: number
  content: string
  address: string
  replies: CommentResp[]
  createTime: string
  author: UserResp
  upvotes: number
  upvoted: boolean
}

export interface CommentQuery {
  objectId: number
  sortType: string
}

export interface CommentPageQuery extends CommentQuery, PageQuery {}
