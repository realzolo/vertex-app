import React, {ReactNode, useEffect} from 'react';
import {AvatarProps, MenuProps, Modal, Typography} from 'antd';
import {LogoutOutlined} from "@ant-design/icons";
import {type RunTimeLayoutConfig, useLocation} from "@umijs/max";
import {AvatarDropdown, AvatarName, Footer, Question} from "@/components";
import {InitialStateType} from "@@/plugin-initialState/@@initialState";
import {history} from "@@/core/history";
import NProgress from "nprogress";

const {Paragraph} = Typography;


const Layout: RunTimeLayoutConfig = ({initialState, setInitialState}) => {
  const location = useLocation();

  useEffect(() => {
    if (location.pathname !== "/") {
    }
  }, []);


  return {
    title: 'Vertex App',
    layout: 'mix',
    logo: 'https://img.alicdn.com/tfs/TB1YHEpwUT1gK0jSZFhXXaAtVXa-28-27.svg',
    contentStyle: {
      // height: 'calc(100vh - 56px)',
    },
    onPageChange: () => onPageChange(initialState),
    actionsRender: () => actionsRender(initialState),
    childrenRender,
    avatarProps: avatarProps(initialState),
    waterMarkProps: waterMarkProps(initialState),
    appList: appListConfig(),
    menuFooterRender: () => menuFooterRender(),
    bgLayoutImgList: bgLayoutImgList(),
    footerRender: () => <Footer/>,
    ...initialState?.settings
  };
};

/**
 * 水印配置
 */
const waterMarkProps = (initialState: InitialStateType) => {
  return {
    content: initialState?.currentUser?.name || 'Vertex App',
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
    src: initialState?.currentUser?.avatar,
    title: <AvatarName/>,
    render: (_: AvatarProps, avatarChildren: ReactNode) => {
      return <AvatarDropdown>{avatarChildren}</AvatarDropdown>;
    },
  }
}

/**
 * 头像位置下拉菜单
 */
const dropdownMenus: MenuProps['items'] = [
  {
    key: 'logout',
    icon: <LogoutOutlined/>,
    label: '退出登录',
  },
];

/** 头像位置下拉菜单点击事件 */
const onClickDropdownMenu: MenuProps['onClick'] = async ({key}) => {
  switch (key) {
    case 'logout':
      logout();
      break;
  }
}

/** 退出登录 */
const logout = () => {
  Modal.confirm({
    title: '退出登录',
    content: '确定退出登录吗？',
    onOk: async () => {
      localStorage.removeItem('token');
      localStorage.removeItem('userinfo');
      window.location.href = '/';
    }
  });
};

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
  const loginPath = '/user/login';
  const {location} = history;
  // 如果没有登录，重定向到 login
  if (!initialState?.currentUser && location.pathname !== loginPath) {
    history.push(loginPath);
  }
}

/**
 * 子组件渲染
 */
const childrenRender = (children: JSX.Element) => {
  NProgress.start();
  // setTimeout(() => {
  // }, 3000)
  return (
    <>{children}</>
  )
}

export default Layout;
