import axios from 'axios'
import qs from 'query-string'
import type { AxiosError, AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { useUserStore } from '@/stores'
import { getToken } from '@/utils/auth'
import modalErrorWrapper from '@/utils/modal-error-wrapper'
import messageErrorWrapper from '@/utils/message-error-wrapper'
import notificationErrorWrapper from '@/utils/notification-error-wrapper'
import router from '@/router'

interface ICodeMessage {
  [propName: number]: string
}

const StatusCodeMessage: ICodeMessage = {
  10200: '服务器成功返回请求的数据',
  10201: '新建或修改数据成功。',
  10202: '一个请求已经进入后台排队（异步任务）',
  10204: '删除数据成功',
  10400: '请求错误(400)',
  10401: '未授权，请重新登录(401)',
  10403: '拒绝访问(403)',
  10404: '请求出错(404)',
  10408: '请求超时(408)',
  10500: '服务器错误(500)',
  10501: '服务未实现(501)',
  10502: '网络错误(502)',
  10503: '服务不可用(503)',
  10504: '网络超时(504)',
}

const http: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_PREFIX ?? import.meta.env.VITE_API_BASE_URL,
  timeout: 30 * 1000,
})

const handleError = (message: string) => {
  if (message.length >= 15) {
    return notificationErrorWrapper({
      content: message || '服务器端错误',
      duration: 5 * 1000,
    })
  }
  return messageErrorWrapper({
    content: message || '服务器端错误',
    duration: 5 * 1000,
  })
}

// 请求拦截器
http.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const token = getToken()
    if (token) {
      if (!config.headers) {
        config.headers = {}
      }
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error),
)

// 响应拦截器
http.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response
    const { success, code, message } = data

    if (response.request.responseType === 'blob') {
      const contentType = data.type
      if (contentType.startsWith('application/json')) {
        const reader = new FileReader()
        reader.readAsText(data)
        reader.onload = () => {
          const { success, message } = JSON.parse(reader.result as string)
          if (!success) {
            handleError(message)
          }
        }
        return Promise.reject(message)
      } else {
        return response
      }
    }

    if (success) {
      return response
    }

    // Token 失效
    if (code === '10401' && response.config.url !== '/auth/user/info') {
      modalErrorWrapper({
        title: '提示',
        content: message,
        maskClosable: false,
        escToClose: false,
        okText: '重新登录',
        async onOk() {
          const userStore = useUserStore()
          await userStore.logoutCallBack()
          await router.replace('/login')
        },
      })
    } else {
      handleError(message)
    }
    return Promise.reject(new Error(message || '服务器端错误'))
  },
  (error: AxiosError) => {
    if (!error.response) {
      handleError('网络连接失败，请检查您的网络')
      return Promise.reject(error)
    }
    const status = error.response?.status
    const errorMsg = StatusCodeMessage[status] || '服务器暂时未响应，请刷新页面并重试。若无法解决，请联系管理员'
    handleError(errorMsg)
    return Promise.reject(error)
  },
)

const request = async <T = unknown>(config: AxiosRequestConfig): Promise<GenericResponse<T>> => {
  return http.request<T>(config)
    .then((res: AxiosResponse) => res.data)
    .catch((err: { message: string }) => Promise.reject(err))
}

const requestNative = async <T = unknown>(config: AxiosRequestConfig): Promise<AxiosResponse> => {
  return http.request<T>(config)
    .then((res: AxiosResponse) => res)
    .catch((err: { message: string }) => Promise.reject(err))
}

const createRequest = (method: string) => {
  return <T = any>(url: string, params?: object, config?: AxiosRequestConfig): Promise<GenericResponse<T>> => {
    return request({
      method,
      url,
      [method === 'get' ? 'params' : 'data']: params,
      ...(method === 'get'
        ? {
            paramsSerializer: (obj) => qs.stringify(obj),
          }
        : {}),
      ...config,
    })
  }
}

const download = (url: string, params?: object, config?: AxiosRequestConfig): Promise<AxiosResponse> => {
  return requestNative({
    method: 'get',
    url,
    responseType: 'blob',
    params,
    paramsSerializer: (obj) => qs.stringify(obj),
    ...config,
  })
}

export default {
  get: createRequest('get'),
  post: createRequest('post'),
  put: createRequest('put'),
  patch: createRequest('patch'),
  del: createRequest('delete'),
  request,
  requestNative,
  download,
}
