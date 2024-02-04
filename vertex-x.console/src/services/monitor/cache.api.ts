import { request } from "@umijs/max";

/**
 * 获取堆内存信息
 */
export const getRedisInformation = (options?: API.RequestOption) => {
  return request<API.RestResponse<Rs.Monitor.RedisInfo>>('/monitor/redis', {
    method: 'GET',
    ...(options || {}),
  });
}
