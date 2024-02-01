import { Settings as LayoutSettings } from '@ant-design/pro-components';
import type { RunTimeLayoutConfig } from '@umijs/max';
import { history } from '@umijs/max';
import settings from '../config/setting';
import requestConfig from './request-config';
import BasicLayout from '@/layouts/BasicLayout';
import { queryCurrentUser } from "@/services/security/user.api";
import '@/styles/index.less';
import { isLoginPage } from "@/utils/pathUtils";

const loginPath = '/login';

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  userinfo?: Model.Security.User;
  loading?: boolean;
  fetchUserInfo?: () => Promise<Model.Security.User | undefined>;
}> {
  const fetchUserInfo = async () => {
    try {
      const response = await queryCurrentUser({
        skipErrorHandler: true,
      });
      return response.data;
    } catch (error) {
      history.push(loginPath);
    }
    return undefined;
  };
  // 如果不是登录页面，执行
  if (!isLoginPage()) {
    const userinfo = await fetchUserInfo();
    return {
      fetchUserInfo,
      userinfo,
      settings: settings as Partial<LayoutSettings>,
    };
  }
  return {
    fetchUserInfo,
    settings: settings as Partial<LayoutSettings>,
  };
}

/**
 * Layout
 */
export const layout: RunTimeLayoutConfig = BasicLayout;

/**
 * Request
 */
export const request = requestConfig;
