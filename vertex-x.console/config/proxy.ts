/**
 * 代理配置
 * -------------------------------
 * @doc https://umijs.org/docs/guides/proxy
 * @doc https://github.com/chimurai/http-proxy-middleware
 */
export default {
  // 本地开发服务器代理配置
  dev: {
    '/api/': {
      // 要代理的地址
      target: 'http://127.0.0.1:10240/vertex',
      changeOrigin: true,
      pathRewrite: {
        '^/api': '',
      },
    },
  },
};
