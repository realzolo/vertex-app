import type { RouteRecordNormalized } from 'vue-router';
import { UserState } from '@/store/modules/user/types';
import axios, { AxiosResponse } from "axios";

export interface LoginData {
  username: string;
  password?: string;
  email?: string;
  captcha: string;
}

export interface LoginResult {
  token: string;
}

type CustomAxiosResponse<T = unknown> = Omit<AxiosResponse<T>, 'data'>;

export function login(
  type: string,
  data: LoginData
): CustomAxiosResponse<LoginResult> {
  switch (type) {
    case 'idPassword':
      return axios.post<LoginResult>('/auth/login', data);
    // case 'email':
    //   return axios.post<LoginRes>('/api/user/email', data);
    default:
      throw new Error('Invalid login type');
  }
}

export function logout() {
  return axios.post<LoginResult>('/api/user/logout');
}

export function getUserInfo() {
  return axios.post<UserState>('/api/user/info');
}

export function getMenuList() {
  return axios.post<RouteRecordNormalized[]>('/api/user/menu');
}
