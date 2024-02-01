import React, { useCallback } from 'react';
import { history, useModel } from '@umijs/max';
import { Modal, Spin } from 'antd';
import { flushSync } from 'react-dom';
import { LogoutOutlined, SettingOutlined, UserOutlined } from '@ant-design/icons';
import { createStyles } from 'antd-style';
import { stringify } from 'querystring';
import type { MenuInfo } from 'rc-menu/lib/interface';
import HeaderDropdown from '../HeaderDropdown';
import { logout } from "@/services/security/user.api";
import { clearUserInfo } from "@/utils/securityUtils";

export type GlobalHeaderRightProps = {
  menu?: boolean;
  children?: React.ReactNode;
};

export const AvatarName = () => {
  const {initialState} = useModel('@@initialState');
  const {userinfo} = initialState || {};
  return <span className="anticon">{userinfo?.name}</span>;
};

const useStyles = createStyles(({token}) => {
  return {
    action: {
      display: 'flex',
      height: '48px',
      marginLeft: 'auto',
      overflow: 'hidden',
      alignItems: 'center',
      padding: '0 8px',
      cursor: 'pointer',
      borderRadius: token.borderRadius,
      '&:hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
  };
});

export const AvatarDropdown: React.FC<GlobalHeaderRightProps> = ({menu, children}) => {
  const {styles} = useStyles();
  const {initialState, setInitialState} = useModel('@@initialState');

  const onMenuClick = useCallback(
    (event: MenuInfo) => {
      switch (event.key) {
        case 'logout':
          doLogout();
          break;
        default:
          break;
      }
    }, [setInitialState]);

  /**
   * 退出登录操作
   */
  const doLogout = () => {
    Modal.confirm({
      title: '退出登录',
      content: '确定退出登录吗？',
      onOk: async () => {
        const resp = await logout();
        if (resp?.success) {
          flushSync(() => {
            setInitialState((s) => ({...s, userinfo: undefined}));
          });
          clearUserInfo();
          const {search, pathname} = window.location;
          const urlParams = new URL(window.location.href).searchParams;
          const redirect = urlParams.get('redirect');
          if (window.location.pathname !== '/login' && !redirect) {
            history.replace({
              pathname: '/login',
              search: stringify({
                redirect: pathname + search,
              }),
            });
          }
        }
      }
    });
  };

  const loading = (
    <span className={styles.action}>
      <Spin
        size="small"
        style={{
          marginLeft: 8,
          marginRight: 8,
        }}
      />
    </span>
  );

  if (!initialState) {
    return loading;
  }

  const {userinfo} = initialState;
  if (!userinfo || !userinfo.name) {
    return loading;
  }

  const menuItems = [
    ...(
      menu ? [
        {key: 'center', icon: <UserOutlined/>, label: '个人中心'},
        {key: 'settings', icon: <SettingOutlined/>, label: '个人设置'},
        {type: 'divider' as const},
      ] : []),
    {key: 'logout', icon: <LogoutOutlined/>, label: '退出登录'},
  ];

  return (
    <HeaderDropdown
      menu={{
        selectedKeys: [],
        onClick: onMenuClick,
        items: menuItems,
      }}
    >
      {children}
    </HeaderDropdown>
  );
};
