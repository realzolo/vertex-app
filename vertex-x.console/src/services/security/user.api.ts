import { request } from '@umijs/max';

/**
 * 用户登录
 */
export const login = (body?: Request.Security.UserLogin, options?: API.RequestOption) => {
  return request<API.RestResponse<Response.Security.UserAuthentication>>('/auth/login', {
    method: 'POST',
    data: body,
    ...(options || {}),
  });
}

/**
 * 获取用户信息
 */
export const queryCurrentUser = (options?: API.RequestOption): Promise<API.RestResponse<Model.Security.User>> => {
  return request<any>('/user/me', {
    method: 'GET',
    ...(options || {}),
  });
}


/**
 * 用户登出
 */
export const logout = (options?: API.RequestOption) => {
  return request<API.RestResponse<void>>('/auth/logout', {
    method: 'POST',
    ...(options || {}),
  });
}
