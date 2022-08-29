<template>
  <div>
    <!-- 类似于面包屑的标签 -->
    <tag tagName="PASSWORD RETRIEVAL"></tag>
    <main class="f-reset">
      <!-- 引导语 -->
      <p class="f-reset_guidance">
        Please enter a new password and confirm to complete the password reset
      </p>
      <!-- 重置密码表单 -->
      <el-form
        :model="resetForm"
        ref="resetForm"
        label-position="left"
        :rules="resetFormRules"
      >
        <!-- 需要修改密码的账号 -->
        <el-form-item prop="account" label="YOUR ACCOUNT">
          <el-input v-model="resetForm.account" placeholder="PHONE OR EMAIL"></el-input>
        </el-form-item>
        <!--新密码  -->
        <el-form-item prop="password" label="YOUR NEW PASSWORD">
          <el-input v-model="resetForm.password"></el-input>
        </el-form-item>
        <!-- 确认密码 -->
        <el-form-item
          prop="e-password"
          label="ENTER AGAIN"
          :style="{ marginTop: '30px' }"
        >
          <el-input v-model="resetForm.epassword"></el-input>
        </el-form-item>
        <!-- 按钮组 -->
        <el-form-item class="buttongroup">
          <el-button class="buttongroup_confirm" @click="validateForm">
            CONFIRM
          </el-button>
          <el-button class="buttongroup_cancel" @click="cancelForm"> CANCEL </el-button>
        </el-form-item>
      </el-form>
    </main>
  </div>
</template>

<script>
import TagWidget from "../../components/TagWidget";

export default {
  name: "PasswordReset",
  // 利用router，传props值
  props: ["uid"],
  components: {
    tag: TagWidget,
  },

  beforeRouteEnter: function (to, from, next) {
    if (to.name == "password_reset_email") {
      next((vm) => {
        if (vm.uid) {
          vm.$api
            .validateEmailLink({
              params: {
                uid: vm.uid,
              },
            })
            .then((res) => {
              var isExpired = res.data.link_status;
              if (isExpired == true) {
                // 如果邮件链接已失效，则引导至失效链接页面
                vm.$router.replace({
                  name: "expired_link",
                });
              } else {
                // 如果邮件链接还未失效，则跳转至新的密码重置页面
                vm.$router.replace({
                  name: "password_reset",
                });
              }
            });
        }
      });
    } else {
      next();
    }
  },
  data() {
    return {
      resetForm: {
        account: "",
        password: "",
        epassword: "",
      },
      // 规则下的键需与el-form-item中的prop属性保持一致，且input的v-model需包括form model值
      resetFormRules: {
        account: [
          {
            required: true,
            message: "Please enter your account.",
            trigger: "blur",
          },
        ],
        password: [
          {
            max: 11,
            message: "The length of password cannot exceed 11 characters.",
            trigger: "blur",
          },
          {
            validator: this.$elFormValidator.validatePwd,
            trigger: "blur",
          },
          {
            required: true,
            message: "Please enter your password.",
            trigger: "blur",
          },
        ],
        epassword: [
          {
            validator: this.validateEPwd,
            trigger: "blur",
          },
          {
            required: true,
            message: "Please enter your password again.",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    // 验证两次输入的密码是否相同
    validateEPwd: function (rule, value, callback) {
      if (
        this.resetForm.password != "" &&
        this.resetForm.epassword == this.resetForm.password
      ) {
        callback();
      } else {
        callback(
          new Error(
            "It does not match the password you entered for the first time, please check and try again."
          )
        );
      }
    },
    // 验证并提交表单
    validateForm: function () {
      const form = this.$refs["resetForm"];
      this.$elFormValidator
        .validateForm(form)
        .then(() => {
          this.$api
            .submitPwdResetForm({
              data: {
                username: this.resetForm.account,
                password: this.resetForm.password,
              },
            })
            .then(() => {
              // 后续这里还需要根据返回的消息值或代码判断是否能够跳转到新页面
              // 其他页面也同样需要作进一步修正
              // 还需要做好消息提示
              this.$router.push({ path: "/user/login" });
            })
            .catch((err) => {
              throw new Error(err);
            });
        })
        .catch((err) => {
          throw new Error(err);
        });
    },
    // 重置表单
    cancelForm: function () {
      const form = this.$refs["resetForm"];
      this.$elFormValidator.resetForm(form);
    },
  },
};
</script>
