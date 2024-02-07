import React, { ReactNode } from 'react';
import { App as AntdApp, AvatarProps, ConfigProvider } from 'antd';
import { type RunTimeLayoutConfig } from "@umijs/max";
import { AvatarDropdown, AvatarName, Footer, Question } from "@/components";
import { InitialStateType } from "@@/plugin-initialState/@@initialState";
import NProgress from "nprogress";
import zhCN from "antd/locale/zh_CN";
import { isLoginPage } from "@/utils/pathUtils";
import { handleUnauthorized } from "@/utils/securityUtils";

const antdConfig = {
  locale: zhCN,
  theme: {
    token: {
      fontFamily: 'Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, \'微软雅黑\', Arial, sans-serif',
    }
  },
  prefixCls: 'vertex-app',
  iconPrefixCls: 'vertex-app',
}

const Layout: RunTimeLayoutConfig = ({initialState, setInitialState}) => {
  return {
    ...initialState?.settings,
    contentStyle: {},
    childrenRender: (dom) => childrenRender(dom),
    avatarProps: avatarProps(initialState),
    // waterMarkProps: waterMarkProps(initialState),
    appList: appListConfig(),
    bgLayoutImgList: bgLayoutImgList(),
    onPageChange: () => onPageChange(initialState),
    actionsRender: () => actionsRender(initialState),
    menuFooterRender: () => menuFooterRender(),
    footerRender: () => <Footer/>,
  };
};

/**
 * 水印配置
 */
const waterMarkProps = (initialState: InitialStateType) => {
  return {
    content: initialState?.userinfo?.nickname || 'Vertex App',
  }
}

/**
 * 功能项配置
 */
const actionsRender = (initialState: InitialStateType) => {
  return [
    <Question key="doc"/>
  ]
};

/**
 * 头像配置
 */
const avatarProps = (initialState: InitialStateType) => {
  return {
    src: initialState?.userinfo?.avatar,
    title: <AvatarName/>,
    render: (_: AvatarProps, avatarChildren: ReactNode) => {
      return <AvatarDropdown>{avatarChildren}</AvatarDropdown>;
    },
  }
}

/** Menu Footer 配置 */
const menuFooterRender = () => {
  return (
    <p
      style={{
        textAlign: 'center',
        paddingBlockStart: 12,
      }}
    >
      Vertex App @ 2024
    </p>
  );
};

/** 跨站点导航列表 */
const appListConfig = () => {
  return [
    {
      title: 'GitHub',
      desc: 'Vertex Admin',
      url: 'https://github.com/realzolo/vertex-admin',
      target: '_blank'
    }
  ]
}

/**
 * 背景图片
 */
const bgLayoutImgList = () => {
  return [
    {
      src: 'https://img.alicdn.com/imgextra/i2/O1CN01O4etvp1DvpFLKfuWq_!!6000000000279-2-tps-609-606.png',
      left: 85,
      bottom: 100,
      height: '303px',
    },
    {
      src: 'https://img.alicdn.com/imgextra/i2/O1CN01O4etvp1DvpFLKfuWq_!!6000000000279-2-tps-609-606.png',
      bottom: -68,
      right: -45,
      height: '303px',
    },
    {
      src: 'https://img.alicdn.com/imgextra/i3/O1CN018NxReL1shX85Yz6Cx_!!6000000005798-2-tps-884-496.png',
      bottom: 0,
      left: 0,
      width: '331px',
    },
  ]
};

/**
 * 页面切换
 */
const onPageChange = (initialState: InitialStateType) => {
  NProgress.done();
  // 如果没有登录，重定向到 login
  if (!initialState?.userinfo && !isLoginPage()) {
    handleUnauthorized();
  }
}

/**
 * 子组件渲染
 */
const childrenRender = (children: JSX.Element) => {
  NProgress.start();
  return (
    <ConfigProvider {...antdConfig} >
      <AntdApp>
        {children}
      </AntdApp>
    </ConfigProvider>
  )
}

export default Layout;
