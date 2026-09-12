// 生产环境，测试和正式
const IS_PROD = ['production'].includes(process.env.NODE_ENV);
const devServerPort = 8081;
const baseApi = process.env.VUE_APP_BASE_API || '/api';
const baseHost = process.env.VUE_APP_BASE_HOST || '';
const developmentBackend = baseHost || 'http://127.0.0.1:5000';
const TransformPages = require('uni-read-pages');
const { webpack } = new TransformPages();

module.exports = {
  lintOnSave: !IS_PROD,
  productionSourceMap: false,

  devServer: {
    port: devServerPort,
    open: false,
    overlay: {
      warnings: false,
      errors: true,
    },
    proxy: {
      [baseApi]: {
        target: developmentBackend + baseApi, // 后端接口测试环境地址  配nginx 时使用
        changeOrigin: true, // 是否允许跨越
        // pathRewrite: {
        //   '^/api': '/api', // 重写 不配nginx 时使用
        // },
        // rewrite: (path) => path.replace(new RegExp('^' + process.env.VUE_APP_BASE_API), ''),
        pathRewrite: {
          ['^' + baseApi]: '',
        },
      },
    },
  },
  // pluginOptions: {
  // 	'style-resources-loader': {
  // 		preProcessor: 'scss',
  // 		patterns: [
  // 			path.resolve(__dirname, 'src/styles/_variables.scss'),
  // 			path.resolve(__dirname, 'src/styles/_mixins.scss')
  // 		]
  // 	}
  // },
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        ROUTES: webpack.DefinePlugin.runtimeValue(() => {
          const tfPages = new TransformPages({
            includes: ['path', 'name', 'aliasPath', 'meta'],
          });
          return JSON.stringify(tfPages.routes);
        }, true),
      }),
    ],
  },
};
