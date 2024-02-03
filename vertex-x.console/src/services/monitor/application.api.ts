import { request } from "@umijs/max";

/**
 * 获取堆内存信息
 */
export const getApplicationInformation = (key: string, tag: string, options?: API.RequestOption) => {
  return request<API.RestResponse<any>>('/monitor/application/' + key, {
    method: 'GET',
    params: {
      tag
    },
    ...(options || {}),
  });
}
