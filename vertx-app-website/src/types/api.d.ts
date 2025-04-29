/** 接口返回数据格式 */
interface GenericResponse<T> {
  code: number
  data: T
  message: string
  success: boolean
  traceId: string
  timestamp: string
}

/** 分页响应数据格式 */
interface PagePack<T> {
  items: T
  pageNumber: number
  pageSize: number
  total: number
}

/** 分页请求数据格式 */
interface PageQuery {
  pageNumber: number
  pageSize: number
}
