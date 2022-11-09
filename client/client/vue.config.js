const { defineConfig } = require('@vue/cli-service');
const NodePolyfillPlugin = require('node-polyfill-webpack-plugin');
const HtmlWebpackExternalsPlugin = require("html-webpack-externals-plugin");
const { BundleAnalyzerPlugin } = require('webpack-bundle-analyzer');
const { processSlotOutlet } = require('@vue/compiler-core');


module.exports = defineConfig({
  configureWebpack: config => {
    const commonPlugins = [];
    const prodPlugins = [];
    const devPlugins = [];
    let totalPlugins = [];
    commonPlugins.push(new NodePolyfillPlugin());
    commonPlugins.push(new BundleAnalyzerPlugin());

    if (process.env.NODE_ENV === "production") {

      prodPlugins.push(new HtmlWebpackExternalsPlugin({
        externals: [
          {
            module: "vue",
            // cdn服务商 unpkg jsdelivr cdnjs
            entry: "https://unpkg.com/vue@3.2.13/dist/vue.global.js",
            global: "Vue"
          },
          {
            module: "element-plus",
            entry: "https://unpkg.com/element-plus@2.1.10/dist/index.full.js",
            global: "ELEMENT"
          }
        ]
      }))
      totalPlugins = [...prodPlugins, ...commonPlugins];
      totalPlugins.forEach((item) => { config.plugins.push(item) });

    } else if (process.env.NODE_ENV === "development") {
      totalPlugins = [...commonPlugins, ...devPlugins];
      totalPlugins.forEach((item) => { config.plugins.push(item) });
      // config.plugins本身就有内置的插件,包括解析vue文件的vue-loader
      // console.log(config.plugins)
    }


  },
  transpileDependencies: true,
  devServer:{
    host:"localhost",
    port:8082
  },
  css: {
    loaderOptions: {
      css: {},
      less: {
        lessOptions: {
          javascriptEnabled: true
        }
      },
      postcss: {
        postcssOptions: {
          plugins: [
            require('postcss-pxtorem')({ // 把px单位换算成rem单位
              rootValue: 16, //换算基数， 
              unitPrecision: 3, //允许REM单位增长到的十进制数字,小数点后保留的位数。
              propList: ['*'],
              exclude: /(node_module)/,  //默认false，可以（reg）利用正则表达式排除某些文件夹的方法，例如/(node_module)/ 。如果想把前端UI框架内的px也转换成rem，请把此属性设为默认值
              selectorBlackList: ['.van', 'el'], //要忽略并保留为px的选择器，本项目我是用的vant ui框架，所以忽略他
              mediaQuery: false,  //（布尔值）允许在媒体查询中转换px。
              minPixelValue: 1 //设置要替换的最小像素值
            })
          ]
        }
      }
    }
  },

})
