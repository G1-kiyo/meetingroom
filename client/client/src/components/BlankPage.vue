<template>
  <div></div>
</template>

<script>
export default {
  name: "BlankPage",

  data() {
    return {};
  },
  beforeRouteEnter: function (to, from, next) {
    if (from.name == "request_password_reset") {
      next((vm) => {
        vm.$router.replace({
          path: from.path,
        });
      });
    } else if (from.name == "room_info_query") {
      next((vm) => {
        vm.$router.replace({
          path: from.path,
        });
      });
    } else {
      next();
    }

    // if (to.name == "password_reset") {
    //     next((vm) => {
    //         if (vm.uid) {
    //             vm.$api.validateEmailLink({
    //                 params: {
    //                     uid: vm.uid
    //                 }
    //             }).then((res) => {
    //                 var isExpired = res.data.link_status;

    //                 if (isExpired == true) {
    //                     // 如果邮件链接已失效，则引导至失效链接页面
    //                     vm.$router.replace({
    //                         name: "expired_link"
    //                     })
    //                 } else {
    //                     // 如果邮件链接还未失效，则跳转至新的密码重置页面
    //                     vm.$router.replace({
    //                         name: "password_reset"
    //                     })

    //                 }
    //             })
    //         }

    //     })

    //     // } else {
    //     //     next();
    //     // }
  },
  watch: {
    // 组件已挂载渲染，再次发生路由变化才会触发
  },
};
</script>
