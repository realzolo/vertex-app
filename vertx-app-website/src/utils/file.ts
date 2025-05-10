import { isHttp } from '@/utils/validate'

/**
 * 文件地址拼接
 * @param fileUrl 文件地址
 */
export function appendApiPrefix(fileUrl: string) {
  if (!fileUrl) {
    return ''
  }
  if (isHttp(fileUrl)) {
    return fileUrl
  }
  return (import.meta.env.VITE_API_PREFIX ?? import.meta.env.VITE_API_BASE_URL ?? '') + fileUrl
}
