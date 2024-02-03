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
  {
    name: '系统监控',
    path: '/monitor',
    icon: 'crown',
    routes: [
      {
        name: '应用监控',
        path: '/monitor/application',
        component: './monitor/application'
      },
      {
        name: '缓存监控',
        path: '/monitor/cache',
        component: './monitor/cache'
      },
      {
        name: '数据库监控',
        path: '/monitor/database',
        component: './monitor/database'
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
    path: '*',
    layout: true,
    component: './404'
  },
];
