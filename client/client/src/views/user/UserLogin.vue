<template>
  <div class="login">
    <!-- 登录成功或失败的弹窗提示 -->
    <!-- <tip-popout :tipState="tipState" :msg="msg" /> -->
    <div class="login_main-section">
      <!-- MEET TO logo -->
      <img class="logo" src="../../assets/img/logo.png" alt="MEET TO" />
      <!-- 登录表单 -->
      <div class="login-form">
        <div class="login-form_account">
          <label :class="isAccountFocused ? 'account_td_focused' : 'account_td_unfocused'"
            >ACCOUNT</label
          >
          <div>
            <input
              type="text"
              name="account"
              v-model="account"
              @focus="isFocused('account', 'focus')"
              @blur="isFocused('account', 'blur')"
            />
            <label class="account_placeholder" v-if="isPlaceHolderShowed"
              >PHONE OR EMAIL</label
            >
          </div>

          <label class="account_warning">{{ accountWarning }}</label>
        </div>
        <div class="login-form_password">
          <label :class="isPwdFocused ? 'password_td_focused' : 'password_td_unfocused'"
            >PASSWORD</label
          >
          <input
            type="text"
            name="password"
            v-model="password"
            @focus="isFocused('password', 'focus')"
            @blur="isFocused('password', 'blur')"
          />
          <label class="password_warning">{{ passwordWarning }}</label>
        </div>
        <!-- 跳转到找回密码页面 -->
        <div class="forget_pwd">
          <router-link to="/request_password_reset">Forgot password?</router-link>
        </div>
        <!-- 登录/注册按钮 -->
        <div class="btn">
          <button type="button" class="btn_sign-in" @click="check()">SIGN IN</button>
          <button
            type="button"
            class="btn_create-account"
            @click="$router.push({ path: '/user/register' })"
          >
            CREATE AN ACCOUNT
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import store from "../../store.js";
export default {
  name: "UserLogin",
  // beforeCreate:function(){
  //   document.getElementsByTagName("body")[0].className = "bg"
  // },
  components: {},
  mounted: function () {
    // document.getElementsByTagName("html")[0].className = "bg";
  },
  // vue3从beforedestory改为beforeunmount
  beforeUnmount: function () {
    // document.getElementsByTagName("html")[0].className = "";
  },

  data() {
    return {
      isAccountFocused: false,
      isPwdFocused: false,
      account: "",
      password: "",
      accountWarning: "",
      passwordWarning: "",
      msg: "",
      tipState: "",
      // isPlaceHolderShowed:false
    };
  },
  computed: {
    isPlaceHolderShowed: {
      get() {
        return this.account == "" && this.isAccountFocused ? true : false;
      },
    },
  },
  methods: {
    // 监听输入框焦点
    isFocused(name, type) {
      if (name == "account") {
        if (type == "focus") {
          this.isAccountFocused = true;
        } else {
          if (this.account == "") {
            this.isAccountFocused = false;
          } else {
            this.isAccountFocused = true;
          }
        }
      }
      if (name == "password") {
        if (type == "focus") {
          this.isPwdFocused = true;
        } else {
          if (this.password == "") {
            this.isPwdFocused = false;
          } else {
            this.isAPwdFocused = true;
          }
        }
      }
    },

    // 验证表单
    check() {
      if (this.account == "" || this.password == "") {
        if (this.account == "") {
          this.accountWarning = "Please enter your account.";
        }
        if (this.password == "") {
          this.passwordWarning = "Please enter your password.";
        }
      } else {
        this.signIn();
      }
    },
    // 提交表单
    signIn() {
      this.$api
        .userLogin({
          data: {
            username: this.account,
            password: this.password,
          },
        })
        .then((res) => {
          // resolve();
          // 后续需要在后端补充jwttokenfilter的设置
          console.log("token" + res.data.token);
          store.commit("setUserId", {
            userId: res.data.userId,
          });
          store.commit("setToken", {
            token: res.data.token,
          });
          this.$router.push({ path: "/meeting/offline/room_info_query" });
        })
        .catch((err) => {
          throw new Error(err);
        });
    },
  },
};
</script>
<style scoped>
/* html {
  margin: 0;
  padding: 0;
  height: 100%;
}

.bg {
  background-image: url(../../assets/img/loginBackground.jpg);
  background-size: 50% 100%;
  background-position: right;
  background-repeat: no-repeat;
} */
</style>
