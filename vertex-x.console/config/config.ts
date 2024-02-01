// https://umijs.org/config/
import {defineConfig} from '@umijs/max';
import {join} from 'path';
import defaultSettings from './setting';
import proxy from './proxy';
import routes from './routes';

const {REACT_APP_ENV = 'dev'} = process.env;
export default defineConfig({
  /**
   * hash 模式, 让 build 之后的产物包含 hash 后缀。通常用于增量发布和避免浏览器加载缓存。
   * @doc https://umijs.org/docs/api/config#hash
   */
  hash: true,

  // vite: {},

  /**
   * 路由配置
   */
  routes,

  /**
   * 主题的配置
   */
  theme: {
    // 如果不想要 configProvide 动态设置主题需要把这个设置为 default
    // 只有设置为 variable， 才能使用 configProvide 动态设置主色调
    'root-entry-name': 'variable',
  },

  /**
   * 忽略 moment 的 locale 文件，用于减少产物尺寸
   */
  ignoreMomentLocale: true,

  /**
   * 代理配置
   */
  proxy: proxy[REACT_APP_ENV as keyof typeof proxy],

  /**
   * 快速热更新
   */
  fastRefresh: true,

  //============== 以下都是max的插件配置 ===============
  /**
   * 数据流插件
   */
  model: {},

  /**
   * 一个全局的初始数据流，可以用它在插件之间共享数据
   * @description 可以用来存放一些全局的数据，比如用户信息，或者一些全局的状态，全局初始状态在整个 Umi 项目的最开始创建。
   * @doc https://umijs.org/docs/max/data-flow#%E5%85%A8%E5%B1%80%E5%88%9D%E5%A7%8B%E7%8A%B6%E6%80%81
   */
  initialState: {},

  /**
   * @name layout 插件
   * @doc https://umijs.org/docs/max/layout-menu
   */
  title: 'Vertex Application',
  layout: {
    locale: true,
    ...defaultSettings,
  },
  /**
   * @name moment2dayjs 插件
   * @description 将项目中的 moment 替换为 dayjs
   * @doc https://umijs.org/docs/max/moment2dayjs
   */
  moment2dayjs: {
    preset: 'antd',
    plugins: ['duration'],
  },
  /**
   * @name antd 插件
   * @doc https://umijs.org/docs/max/antd#antd
   */
  antd: {},
  /**
   * 网络请求配置
   * @doc https://umijs.org/docs/max/request
   */
  request: {},
  /**
   * 权限插件
   * @doc https://umijs.org/docs/max/access
   */
  access: {},
  /**
   * <head> 中额外的 script
   */
  headScripts: [
    // 解决首次加载时白屏的问题
    {
      src: '/scripts/loading.js',
      async: true,
    },
  ],
  //================ pro 插件配置 =================
  presets: ['umi-presets-pro'],
  /**
   * openAPI 插件的配置
   * @doc https://pro.ant.design/zh-cn/docs/openapi/
   */
  openAPI: [
    {
      requestLibPath: "import { request } from '@umijs/max'",
      // 或者使用在线的版本
      // schemaPath: "https://gw.alipayobjects.com/os/antfincdn/M%24jrzTTYJN/oneapi.json"
      schemaPath: join(__dirname, 'oneapi.json'),
      mock: false,
    },
    {
      requestLibPath: "import { request } from '@umijs/max'",
      schemaPath: 'https://gw.alipayobjects.com/os/antfincdn/CA1dOm%2631B/openapi.json',
      projectName: 'swagger',
    },
  ],
  mfsu: {
    strategy: 'normal',
  },
  esbuildMinifyIIFE: true,
  requestRecord: {},
});
