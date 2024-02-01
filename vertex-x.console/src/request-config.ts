import type { RequestOptions } from '@@/plugin-request/request';
import type { RequestConfig } from '@umijs/max';
import { message as AntdMessage } from "antd";
import { handleUnauthorized } from "@/utils/securityUtils";

/**
 * 业务异常类型
 */
enum BizHttpStatusType {
  // OK
  SUCCESS = 10200,
  // 请求参数不正确
  BAD_REQUEST = 10400,
  // 未授权操作
  UNAUTHORIZED = 10401,
  // 无操作权限
  FORBIDDEN = 10403,
  // 请求未找到
  NOT_FOUND = 10404,
  // 请求方法不正确
  METHOD_NOT_ALLOWED = 10405,
  // 请求失败，请稍后重试
  LOCKED = 10423,
  // 请求过于频繁，请稍后重试
  TOO_MANY_REQUESTS = 10429,
  // 系统异常
  INTERNAL_SERVER_ERROR = 10500,
  // 功能未实现/未开启
  NOT_IMPLEMENTED = 10501,
  // 错误的配置项
  ERROR_CONFIGURATION = 10502,
  // 重复请求，请稍后重试
  REPEATED_REQUESTS = 10900,
}

/**
 * 基础请求配置
 */
const basicRequestConfig = {
  baseURL: '/api',
  timeout: 30000,
  timeoutErrorMessage: '请求超时，请稍后重试。',
}

/**
 * 请求拦截器
 */
const requestInterceptors = [
  (config: RequestOptions) => {
    // 携带 token
    const token = localStorage.getItem('token');
    if (token) {
      config.headers = {
        Authorization: `Bearer ${token}`,
      };
    }
    return config;
  },
];

/**
 * 响应拦截器
 */
const responseInterceptors = [
  (response: any) => {
    // 获取headers中的Authorization, 如果存在则表示需要更新token(token续期)
    const authorization = response.headers['authorization'];
    if (authorization) {
      const newToken = authorization.replace('Bearer ', '');
      const oldToken = localStorage.getItem('token');
      if (newToken !== oldToken) {
        localStorage.setItem('token', newToken);
      }
    }
    // 自定义响应结构
    const originalResponse = {...response};
    delete originalResponse.data;
    if (response.data) {
      response.data['originalResponse'] = originalResponse;
    }

    // 错误处理
    errorHandler(response);

    return response;
  },
];

/**
 * HTTP异常处理
 * @param response
 */
const errorHandler = (response: any) => {
  // 是否需要全局处理业务异常
  if (response.config.skipErrorHandler) return;

  // HTTP异常处理
  if (response.status !== 200) {
    switch (response?.status) {
      case 401:
        handleUnauthorized();
        break;
      case 403:
        antdMessage('warning', '您没有权限访问，请联系管理员。');
        break;
      case 404:
        antdMessage('error', '请求的资源不存在。');
        break;
      case 500:
        antdMessage('error', '服务器错误，请稍后重试。');
        break;
      case 504:
        antdMessage('warning', '网络连接超时，请稍后重试。');
        break;
      default:
        antdMessage('error', response.statusText || '请求失败！');
        break;
    }
  }

  // 业务异常处理
  const {code, message} = response.data;
  if (code !== BizHttpStatusType.SUCCESS) {
    switch (code) {
      case BizHttpStatusType.BAD_REQUEST:
        // do something...
        antdMessage('error', message);
        break;
      case BizHttpStatusType.UNAUTHORIZED:
        // handleUnauthorized();
        antdMessage('error', message);
        break;
      case BizHttpStatusType.FORBIDDEN:
        // do something...
        antdMessage('warning', message);
        break;
      case BizHttpStatusType.NOT_FOUND:
        // do something...
        antdMessage('error', message);
        break;
      case BizHttpStatusType.METHOD_NOT_ALLOWED:
        // do something...
        antdMessage('error', message);
        break;
      case BizHttpStatusType.LOCKED:
        // do something...
        antdMessage('warning', message);
        break;
      case BizHttpStatusType.TOO_MANY_REQUESTS:
        // do something...
        antdMessage('warning', message);
        break;
      case BizHttpStatusType.INTERNAL_SERVER_ERROR:
        // do something...
        antdMessage('error', message);
        break;
      case BizHttpStatusType.NOT_IMPLEMENTED:
        // do something...
        antdMessage('error', message);
        break;
      case BizHttpStatusType.ERROR_CONFIGURATION:
        // do something...
        antdMessage('error', message);
        break;
      case BizHttpStatusType.REPEATED_REQUESTS:
        // do something...
        antdMessage('warning', message);
        break;
      default:
        antdMessage('error', message);
    }
  }
}

const antdMessage = (method: 'warning' | 'error', message: string) => {
  AntdMessage[method](message).then();
}

export default {
  ...basicRequestConfig,
  requestInterceptors,
  responseInterceptors,
};
