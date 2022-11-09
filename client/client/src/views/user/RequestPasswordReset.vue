<template>
  <div>
    <!-- 类似于面包屑的标签 -->
    <tag tagName="PASSWORD RETRIEVAL"></tag>
    <!-- 返回指引 -->
    <!-- <div :class="returnTag" @click="returnToOrigin"><span class="iconfont icon-fanhui"></span></div> -->

    <main class="reset">
      <!-- 引导语 -->
      <p class="reset_guidance" v-if="guidanceShow">
        You can retrieve your password in the following two ways
      </p>
      <!-- 手机找回方式框 -->
      <div class="reset_phone" @click="phoneDetail" v-if="phoneMethodShow">
        <span class="iconfont icon-shouji"></span>
        <div>Mobile Phone Verification Code</div>
      </div>
      <div class="reset_phone_form" v-if="phoneFormShow">
        <p>
          Enter your phone number below and we'll send you the verification code.You can
          use it to get access to the password reset page
        </p>
        <el-form
          :model="phoneForm"
          :rules="phoneRetrievalRules"
          label-position="top"
          status-icon
          ref="phoneForm"
          class="phone-retrieval_form"
        >
          <el-form-item prop="phone" label="PHONE">
            <el-input v-model="phoneForm.phone"></el-input>
          </el-form-item>
          <el-form-item prop="code" label="VERIFICATION CODE">
            <el-input v-model="phoneForm.code">
              <template #append>
                <el-button @click="getCode">{{ appendMessage }}</el-button>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item class="buttongroup">
            <!--button的type值默认为submit，点击会与form搭配，提交到form的action指定的地址，并刷新  -->
            <button
              class="buttongroup_confirm"
              type="button"
              @click="validateForm('phoneForm')"
            >
              CONFIRM
            </button>
            <button class="buttongroup_cancel" type="button" @click="returnToOrigin">
              CANCEL
            </button>
          </el-form-item>
        </el-form>
      </div>
      <!-- 邮箱找回方式框 -->
      <div class="reset_email" @click="emailDetail" v-if="emailMethodShow">
        <span class="iconfont icon-youxiang"></span>
        <div>E-MAIL</div>
      </div>
      <div class="reset_email_form" v-if="emailFormShow">
        <p>
          Enter your email address below and we'll send you an email with instructions on
          how to reset your password.
        </p>
        <el-form
          :model="emailForm"
          :rules="emailRetrievalRules"
          ref="emailForm"
          class="email-retrieval_form"
        >
          <el-form-item prop="email" label="EMAIL">
            <el-input v-model="emailForm.email"></el-input>
          </el-form-item>
          <el-form-item class="buttongroup">
            <button
              class="buttongroup_confirm"
              type="button"
              @click="validateForm('emailForm')"
            >
              CONFIRM
            </button>
            <button class="buttongroup_cancel" type="button" @click="returnToOrigin">
              CANCEL
            </button>
          </el-form-item>
        </el-form>
      </div>
    </main>
  </div>
</template>

<script>
import TagWidget from "@/components/TagWidget";
import BlankPage from "@/views/warn/BlankPage";
import { ElMessage } from "element-plus";
export default {
  name: "RequestPasswordReset",
  // 导入组件后，一定要记得注册
  components: {
    tag: TagWidget,
  },
  data() {
    return {
      phoneMethod: "reset_phone-unfocused",
      emailMethod: "reset_email-unfocused",
      phoneMethodShow: true,
      emailMethodShow: true,
      phoneFormShow: false,
      emailFormShow: false,
      guidanceShow: true,
      phoneForm: {
        phone: "",
        code: "",
      },
      emailForm: {
        email: "",
      },
      startTimer: false,
      expiredTime: 60,
      phoneRetrievalRules: {
        phone: [
          {
            required: true,
            message: "Please input your phone number.",
          },
          {
            validator: this.$elFormValidator.validatePhone,
            trigger: "blur",
          },
          {
            max: 11,
            message: "The length of phone number cannot exceed 11.",
            trigger: "blur",
          },
        ],
        code: [
          {
            required: true,
            message: "Please input your verification code.",
            trigger: "blur",
          },
          {
            validator: this.validateCode,
            trigger: "blur",
          },
        ],
      },
      emailRetrievalRules: {
        email: [
          {
            required: true,
            message: "Please enter your email address.",
            trigger: "blur",
          },
          {
            type: "email",
            message: "Incorrect email format.",
            trigger: "blur",
          },
        ],
      },
    };
  },
  computed: {
    appendMessage() {
      return this.startTimer ? "valid within " + this.expiredTime + "s" : "CLICK HERE";
    },
  },
  methods: {
    phoneDetail: function () {
      this.phoneFormShow = true;
      this.emailMethodShow = false;
      this.guidanceShow = false;
    },
    emailDetail: function () {
      this.emailFormShow = true;
      this.phoneMethodShow = false;
      this.guidanceShow = false;
    },
    returnToOrigin: function () {
      this.$router.push({
        path: "/blank",
      });
    },
    setTimer: function () {
      if (this.expiredTime > 0) {
        this.expiredTime = this.expiredTime - 1;
      } else {
        this.startTimer = false;
      }
      const that = this;
      setTimeout(function () {
        that.setTimer();
      }, 1000);
    },
    getCode: function () {
      if (this.phoneForm.phone == "") {
        ElMessage({
          message: "Please input your phone number.",
          type: "warning",
        });
      } else {
        this.$api
          .requestSendCode({
            params: {
              phone: this.phoneForm.phone,
            },
          })
          .then(() => {
            this.startTimer = true;
            this.setTimer();
          })
          .catch((err) => {
            throw new Error(err);
          });
      }
    },
    validateCode: function (rule, value, callback) {
      if (/[0-9A-Za-z]{6}/.test(value)) {
        callback();
      } else {
        callback(new Error("The length of verification code should be 6."));
      }
    },
    // validateForm: function (formname) {
    //     const form = this.refs[formname];
    //     if (!form) throw new Error("The form is not found!");
    //     form.validate((valid) => {
    //         if (valid) {
    //             this.$api.uploadPhoneForm({
    //                 params: {
    //                     phone: this.phone,
    //                     code: this.code
    //                 }
    //             }).then((resolve) => {
    //                 resolve();
    //                 this.router.push({ path: "/password_reset" });
    //             })
    //         } else {
    //             throw new Error("Valid failed!");
    //         }
    //     })
    // },

    validateForm: function (formname) {
      const form = this.$refs[formname];
      if (formname == "phoneForm") {
        this.$api
          .requestPwdResetWithPhone({
            params: {
              phone: this.phoneForm.phone,
              code: this.phoneForm.code,
            },
          })
          .then((res) => {
            if (res.data.password_reset_uid) {
              this.$router.push({
                path: "/password_reset/phone/" + res.data.password_reset_uid,
              });
            }
            // this.$router.addRoute({
            //   name: "active_password_reset",
            //   path: "/password_reset/" + uid,
            //   component: BlankPage,
            // });
            // this.$router.push({name:"active_password_reset"})
          })
          .catch((err) => {
            throw new Error(err);
          });
      } else {
        this.$elFormValidator
          .validateForm(form)
          .then(() => {
            this.$api
              .requestPwdResetWithEmail({
                params: {
                  email: this.emailForm.email,
                },
              })
              .then(() => {
                // var uid = res.data.password_reset_uid;
                // this.$router.push({name:"blank",path:"/blank/"+uid})
                // 添加新路由时，如果name重合，会覆盖之前同名的路由
                // this.$router.addRoute({ name: "active_password_reset", path: "/password_reset/" + uid, component: BlankPage })
              });
          })
          .catch((err) => {
            throw new Error(err);
          });
      }
    },
    // resetForm: function (formname) {
    //     const formRef = this.$refs[formname];
    //     if (!formRef) throw new Error("The form is not found!");
    //     formRef.resetFields();
    // },
  },
};
</script>
