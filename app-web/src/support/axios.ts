import type { AxiosRequestConfig, AxiosResponse } from 'axios';
import axios from 'axios';
import { Message } from '@arco-design/web-vue';
import { getToken } from '@/utils/auth';

export interface HttpResponse<T = unknown> {
  code: number;
  success: boolean;
  message: string;
  data: T;
  traceId: string;
  timestamp: string;
}

const axiosInstance = axios.create({
  timeout: 10000,
  baseURL: import.meta.env.VITE_API_BASE_URL
    ? import.meta.env.VITE_API_BASE_URL
    : '',
});

let errorHandler: (response: AxiosResponse<HttpResponse>) => void;

/**
 * 业务异常类型
 */
// eslint-disable-next-line no-shadow
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
 * 请求拦截器
 */
const requestInterceptor = {
  onFulfilled: (config: AxiosRequestConfig) => {
    const token = getToken();
    if (token) {
      if (!config.headers) {
        config.headers = {};
      }
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  onRejected: (error: Error | any) => {
    return Promise.reject(error);
  },
};

const responseInterceptor = {
  onFulfilled: (response: AxiosResponse<HttpResponse>) => {
    if (response.status !== 200 || !response.data?.success) {
      errorHandler(response);
      const message =
        response.data.message || response.statusText || '请求失败，请稍后重试';
      return Promise.reject(new Error(message));
    }
    return response.data;
  },
  onRejected: (error: Error | any) => {
    Message.error({
      content: error.msg || 'Request Error',
      duration: 5 * 1000,
    });
    return Promise.reject(error);
  },
};

/**
 * 业务异常处理
 */
errorHandler = (response: AxiosResponse<HttpResponse>) => {
  const { code, message } = response.data;
  if (code !== BizHttpStatusType.SUCCESS) {
    switch (code) {
      case BizHttpStatusType.BAD_REQUEST:
        // do something...
        Message.error(message);
        break;
      case BizHttpStatusType.UNAUTHORIZED:
        // handleUnauthorized();
        Message.error(message);
        break;
      case BizHttpStatusType.FORBIDDEN:
        // do something...
        Message.warning(message);
        break;
      case BizHttpStatusType.NOT_FOUND:
        // do something...
        Message.error(message);
        break;
      case BizHttpStatusType.METHOD_NOT_ALLOWED:
        // do something...
        Message.error(message);
        break;
      case BizHttpStatusType.LOCKED:
        // do something...
        Message.warning(message);
        break;
      case BizHttpStatusType.TOO_MANY_REQUESTS:
        // do something...
        Message.warning(message);
        break;
      case BizHttpStatusType.INTERNAL_SERVER_ERROR:
        // do something...
        Message.error(message);
        break;
      case BizHttpStatusType.NOT_IMPLEMENTED:
        // do something...
        Message.error(message);
        break;
      case BizHttpStatusType.ERROR_CONFIGURATION:
        // do something...
        Message.error(message);
        break;
      case BizHttpStatusType.REPEATED_REQUESTS:
        // do something...
        Message.warning(message);
        break;
      default:
        Message.error(message || '请求失败，请稍后重试');
    }
  }
};

axiosInstance.interceptors.request.use(
  // @ts-ignore
  requestInterceptor.onFulfilled,
  requestInterceptor.onRejected
);

axiosInstance.interceptors.response.use(
  // @ts-ignore
  responseInterceptor.onFulfilled,
  responseInterceptor.onRejected
);

export default axiosInstance;
