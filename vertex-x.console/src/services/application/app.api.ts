import { request } from "@umijs/max";

/**
 * 获取应用版本号
 */
export const getAppVersion = (options?: API.RequestOption) => {
  return request<API.RestResponse<string>>('/application/version', {
    method: 'GET',
    ...(options || {}),
  });
}
