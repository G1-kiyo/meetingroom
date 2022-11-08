const { defineConfig } = require('@vue/cli-service');
const NodePolyfillPlugin = require('node-polyfill-webpack-plugin');
const HtmlWebpackExternalsPlugin = require("html-webpack-externals-plugin");
const {BundleAnalyzerPlugin} = require('webpack-bundle-analyzer');
const { processSlotOutlet } = require('@vue/compiler-core');


module.exports = defineConfig({
  configureWebpack:config=>{
    const commonPlugins = [];
    const prodPlugins = [];
    const devPlugins = [];
    commonPlugins.push(new NodePolyfillPlugin());
    commonPlugins.push(new BundleAnalyzerPlugin());

    if(process.env.NODE_ENV==="production"){
      prodPlugins.push(new HtmlWebpackExternalsPlugin({
        externals:[
          {
            module:"vue",
            // cdn服务商 unpkg jsdelivr cdnjs
            entry:"https://unpkg.com/vue@3.2.13/dist/vue.global.js",
            global:"Vue"
          },
          {
            module:"element-plus",
            entry:"https://unpkg.com/element-plus@2.1.10/dist/index.full.js",
            global:"ELEMENT"
          }
        ]
      }))
      config.plugins = [
        ...commonPlugins,
        ...prodPlugins
      ]
    }else if(process.env.NODE_ENV==="development"){
      config.plugins = [
        ...commonPlugins,
        ...devPlugins
      ]
    }

    
  },
  transpileDependencies: true,
  css:{
    loaderOptions:{
      css:{},
      less:{
        lessOptions:{
          javascriptEnabled: true
        }
      }
    }
  },
  
})
