export default [
  {
    path: '/user',
    layout: false,
    routes:
      [
        {
          name: '用户登录',
          path: '/user/login',
          component: './User/Login'
        }
      ]
  },
  {
    name: '欢迎页',
    path: '/welcome',
    icon: 'smile',
    component: './Welcome'
  },
  {
    name: '管理页面',
    path: '/admin',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      {
        path: '/admin',
        redirect: '/admin/sub-page'
      },
      {
        name: '子页面',
        path: '/admin/sub-page',
        component: './Admin'
      },
    ],
  },
  {
    name: '表单样例',
    path: '/list',
    icon: 'table',
    component: './TableList'
  },
  {
    path: '/',
    redirect: '/welcome'
  },
  {
    path: '*',
    layout: false,
    component: './404'
  },
];
