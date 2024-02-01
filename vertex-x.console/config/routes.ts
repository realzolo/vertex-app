export default [
  {
    path: '/',
    redirect: '/login'
  },
  {
    name: '用户登录',
    path: '/login',
    component: 'login',
    layout: false,
  },
  {
    name: '欢迎页',
    path: '/overview',
    icon: 'smile',
    component: './Welcome'
  },
  // {
  //   name: '管理页面',
  //   path: '/admin',
  //   icon: 'crown',
  //   access: '',
  //   routes: [
  //     {
  //       path: '/admin',
  //       redirect: '/admin/sub-page'
  //     },
  //     {
  //       name: '子页面',
  //       path: '/admin/sub-page',
  //       component: './Admin'
  //     },
  //   ],
  // },
  {
    name: '表单样例',
    path: '/list',
    icon: 'table',
    component: './TableList'
  },
  {
    path: '*',
    layout: false,
    component: './404'
  },
];
