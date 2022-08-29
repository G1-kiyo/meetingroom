import { createApp} from 'vue';
import store from "./store";
import App from './App.vue';
import ElementPlus from "element-plus";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import axios from "axios";
import VueAxios from "vue-axios";
import router from "./router.js"
import api from "./api/Api.js";
import elFormValidator from './utils/ElFormValidator';
import dateProcess from "./utils/DateProcess";
import UserOperationControl from './utils/UserOperationControl';
import Vue3Storage from "vue3-storage";
import "element-plus/dist/index.css";
import "./assets/css/style.less";
import "./assets/css/global.less";
import "./assets/css/var.less";

// const store = createStore({
//   store(){
//     return {
//       hasToken:""
//     }
//   },
//   mutations:{
//     refreshToken(state,payload){
//       state.hasToken = payload.token;
//     },
//     clearToken(state){
//       state.hasToken = "";
//     }
//   }
// })
//接收vue创建app，并挂载在index.html中id为app的node上
const app = createApp(App);
app.config.globalProperties.$api = api;
app.config.globalProperties.$elFormValidator = elFormValidator;
app.config.globalProperties.$dateProcess = dateProcess;
app.config.globalProperties.$userOC = UserOperationControl;

//全局注册elementplus
app.use(ElementPlus);
// 全局注册element plus图标
// Object.keys(EIIconModules).forEach((key)=>{
//     app.component(EIIconModules[key].name,EIIconModules[key]);
// })
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }
// 全局注册axios
app.use(VueAxios,axios);
// 全局注册session，处理会话信息.
// 原生js用sessionStorage或localStorage保存
// app.use(VueSession);
app.use(Vue3Storage);
// 注册路由
app.use(router);
// 注册全局状态管理器
app.use(store);
// 挂在在根元素上
app.mount("#app");