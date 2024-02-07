/**
 * ---------------------------------------------------------------------------------------
 * Description: 页面安全工具类
 * ---------------------------------------------------------------------------------------
 */

import { Modal } from "antd";
import { isLoginPage } from "@/utils/pathUtils";

/**
 * 删除localStorage用户信息
 */
export const clearUserInfo = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('userinfo');
};

/**
 * 认证信息失效提示弹窗
 */
let isUnauthorizedModalShow = false;
export const handleUnauthorized = () => {
  // 登录页无需处理
  if (isLoginPage()) return;

  // 存在token, 但是请求失败, 说明token过期
  if (localStorage.getItem('token')) {
    if (isUnauthorizedModalShow) return;
    isUnauthorizedModalShow = true;
    Modal.warning({
      title: '提示',
      content: '您的身份已过期，请重新登录。',
      okText: '重新登录',
      centered: true,
      keyboard: false,
      onOk: () => {
        isUnauthorizedModalShow = false;
        clearUserInfo();
        window.location.href = '/console/login?redirect=' + encodeURIComponent(window.location.pathname);
      }
    });
    return;
  }

  // 不存在token, 说明未登录
  clearUserInfo();
  window.location.href = '/login';
}
