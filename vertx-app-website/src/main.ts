import { createApp } from 'vue'
import ArcoVue, { Card, Drawer, Modal } from '@arco-design/web-vue'
import '@/styles/arco-ui/index.less'
// import '@arco-themes/vue-gi-demo/index.less'
// import '@arco-design/web-vue/dist/arco.css'

// Vue Flow 全局样式
import '@vue-flow/core/dist/style.css'
import '@vue-flow/core/dist/theme-default.css'
import '@vue-flow/controls/dist/style.css'
import '@vue-flow/minimap/dist/style.css'

// 使用动画库
import 'animate.css/animate.min.css'

// 额外引入 Arco Design Icon图标库
import ArcoVueIcon from '@arco-design/web-vue/es/icon'

// 引入浏览器指纹
import FingerprintJS from '@fingerprintjs/fingerprintjs'

import App from './App.vue'
import router from './router'

// 自定义过渡动画
import '@/styles/css/transition.css'

// 导入全局scss主文件
import '@/styles/index.scss'

// 支持SVG
import 'virtual:svg-icons-register'

// 自定义指令
import directives from './directives'

// 状态管理
import pinia from '@/stores'

// 浏览器指纹
const fpPromise = FingerprintJS.load()
fpPromise.then((fp) => {
  fp.get().then((result) => {
    localStorage.setItem('fingerprint', result.visitorId)
  })
})

// 对特定组件进行默认配置
Card.props.bordered = false

const app = createApp(App)
Modal._context = app._context
Drawer._context = app._context

app.use(router)
app.use(pinia)
app.use(ArcoVue)
app.use(ArcoVueIcon)
app.use(directives)

app.mount('#app')
