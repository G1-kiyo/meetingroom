<template>
  <div>
    <!-- 类似于面包屑的标签 -->
    <TagWidget tagName="REGISTER"></TagWidget>
    <!-- 登录成功或失败的弹窗提示 -->
    <!-- <tip-popout :tipState="tipState" :msg="msg" /> -->
    <!-- 头像 -->
    <div class="register_avatar" ref="register_avatar">
      <!-- 头像图片展示区域 -->
      <div class="avatar_img" ref="avatar_img">
        <img v-if="isAvatarActive" :src="avatarImg" alt="Avatar" ref="avatarImg" />
        <span v-else class="iconfont icon-touxiang"></span>
      </div>
      <!-- 头像图片上传 -->
      <div class="avatar_upload" @click="previewAvatar">
        <!-- <input type="file" @click.capture="uploadAvatar" /> -->
        <!-- <i class="iconfont_leftArrow">&#xe738;</i> -->
        <span class="iconfont icon-jiantou-zuo"></span>
        <div>UPLOAD MY OWN AVATAR</div>
      </div>
    </div>
    <main>
      <!-- 注册表单 -->
      <!-- 这里的:model与v-model不一样 -->
      <el-form
        :model="form"
        :rules="rules"
        label-position="top"
        status-icon
        ref="form"
        class="register_form"
      >
        <!-- :prop与rules相关联，:label就是标签名 -->
        <el-form-item
          v-for="(item, index) in infoArray"
          :key="index"
          :prop="item"
          :label="item.toUpperCase()"
          class="register_formItem"
        >
          <!-- 用插槽自定义label标签 -->
          <!-- 不能直接用item取 -->
          <el-input v-model="form[infoArray[index]]"></el-input>
        </el-form-item>
        <!-- 按钮组 -->
        <el-form-item>
          <!-- 判断图片是否上传 -->
          <el-button
            v-if="avatarMeta != null"
            @click="submitForm('form')"
            class="register_button_save"
            >SAVE</el-button
          >
          <el-button
            v-else
            @click="avatarUploadConfirm('form')"
            class="register_button_save"
            >SAVE</el-button
          >
          <el-button @click="resetForm('form')" class="register_button_cancel"
            >CANCEL</el-button
          >
        </el-form-item>
      </el-form>
    </main>
  </div>
</template>

<script>
// import TipPopout from "../../components/TipPopout";
import { ElMessageBox, ElMessage } from "element-plus";
import { resolve } from "path";
import TagWidget from "../../components/TagWidget";
// import TagWidget from "@/components/TagWidget.vue";
// import qs from "qs";
export default {
  name: "UserRegister",
  components: { TagWidget },
  mounted() {
    // 当子元素设置position为absolute，父元素高度塌陷
    // 只能（目前）利用js动态设置父元素固定高度
    var height = this.$refs["avatar_img"].clientHeight;
    this.$refs["register_avatar"].style.height = height + "px";
  },
  data() {
    return {
      avatarImg: "",
      avatarMeta: null,
      // imgUploadConfirm: false,
      isAvatarActive: false,
      infoArray: ["nickname", "truename", "password", "phone", "email"],
      form: {
        nickname: "",
        truename: "",
        password: "",
        phone: "",
        email: "",
      },
      // form表单验证规则
      rules: {
        nickname: [
          {
            required: true,
            message: "Please input your nickname.",
            trigger: "blur",
          },
          {
            min: 3,
            max: 8,
            message: "The length of nickname should be 3 to 8.",
            trigger: "blur",
          },
        ],
        truename: [
          {
            required: true,
            message: "Please input your truename.",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: "Please input your password.",
            trigger: "blur",
          },
          {
            min: 6,
            max: 11,
            message: "The length of password should be 6 to 11.",
            trigger: "blur",
          },
          {
            validator: this.$elFormValidator.validatePwd,
            trigger: "blur",
          },
        ],
        phone: [
          {
            required: true,
            message: "Please input your phone number.",
            trigger: "blur",
          },
          {
            validator: this.$elFormValidator.validatePhone,
            trigger: "blur",
          },
        ],
        email: [
          {
            required: true,
            message: "Please input your email address.",
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
  methods: {
    previewAvatar: function () {
      // 模拟input点击上传文件
      var input = document.createElement("input");
      input.setAttribute("type", "file");
      input.click();
      input.addEventListener("change", (event) => {
        // console.log(event.target.files);
        var file = event.target.files[0];
        if (file) {
          var url = URL.createObjectURL(file);
          this.avatarImg = url;
          // console.log(this.avatarImg);
          this.isAvatarActive = true;
          var formData = new FormData();
          formData.append("smfile", file);
          formData.append("format", "json");
          this.avatarMeta = formData;
        }
      });
    },
    // 上传头像
    uploadAvatar: function () {
      // 传输文件用formdata，直接用file类型进行传输，为空值
      // qs插件提供的stringify利用&将formdata序列化并拼接，不能序列化file
      return new Promise((resolve, reject) => {
        this.$api
          .uploadAvatar({
            data: this.avatarMeta,
          })
          .then((res) => {
            if (res.data.success) {
              this.avatarImg = res.data.data.url;
              // resolve(this.avatarImg);
              // resolve();
              resolve(this.avatarImg);
            }
          });
      });
    },
    // 对头像是否上传的确认
    avatarUploadConfirm: function (formname) {
      ElMessageBox.confirm(
        "You haven't uploaded your avatar yet. Are" +
          " you sure you are submitting the registration" +
          " information directly? If you select the confirm button," +
          "you will be automatically assigned a default avatar",
        "AvatarUploadConfirm",
        {
          distinguishCancelAndClose: true,
          confirmButtonText: "confirm",
          cancelButtonText: "cancel",
        }
      )
        .then(() => {
          this.submitForm(formname);
        })
        .catch((action) => {
          ElMessage({
            type: "info",
            message:
              action === "cancel"
                ? "submitting registration information cancelled."
                : "Proceeding to submitting registration information.",
          });
        });
    },
    // 提交表单
    uploadAvatarSettled: async function () {
      const uploadAvatar = await this.uploadAvatar();
      return uploadAvatar;
      // uploadAvatar.then();
    },
    submitForm: function (formname) {
      this.uploadAvatarSettled().then((res) => {
        // console.log(123);
        const formRef = this.$refs[formname];
        this.$elFormValidator
          .validateForm(formRef)
          .then(() => {
            this.$api
              .userRegister({
                data: {
                  username: this.form.nickname,
                  truename: this.form.truename,
                  password: this.form.password,
                  phone: this.form.phone,
                  email: this.form.email,
                  avatar: res,
                },
              })
              .then(() => {
                // resolve();
                this.$router.push({ path: "/user/index" });
              })
              .catch((err) => {
                this.$router.go(-1);
                throw new Error(err);
              });
          })
          .catch((err) => {
            throw new Error(err);
          });
      });
    },

    resetForm: function (formname) {
      const formRef = this.$refs[formname];
      this.$elFormValidator.resetForm(formRef);
    },
  },
};
</script>

<style scope>
#app {
  margin-top: 0px;
}
</style>
