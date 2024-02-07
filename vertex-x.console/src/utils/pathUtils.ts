/**
 * ---------------------------------------------------------------------------------------
 * Description: 路径工具类
 * ---------------------------------------------------------------------------------------
 */


/**
 * 判断当前页面是否是登录页面
 */
export const isLoginPage = () => {
  const pathname = window.location.pathname;
  return /^\/console\/login(\?redirect=.*)?$|^\/$/.test(pathname);
};
