<template>
  <el-avatar
    class="selectedmember_item_avatar"
    :size="30"
    :src="memberInfo.avatar"
  ></el-avatar>
  <span class="selectedmember_item_truename">{{
    memberInfo.truename.substring(0, 4)
  }}</span>
  <el-icon class="selectedmember_item_remove" @click="memberInfo.selected = false">
    <CircleCloseFilled />
  </el-icon>
</template>
<script>
export default {
  name: "SelectedMemberItem",
  props: ["member"],
  emits: ["addMember", "removeMember"],
  data() {
    return {
      memberInfo: this.member,
    };
  },
  watch: {
    "memberInfo.selected": {
      handler: function (newVal) {
        if (newVal == true) {
          this.$emit("addMember", this.memberInfo);
        } else {
          this.$emit("removeMember", this.memberInfo.userId);
        }
      },
    },
  },
  methods: {},
};
</script>
<style lang="less" scoped>
.selectedmember_item {
  display: inline-block;
  height: 30px;
  border-radius: 14px;
  border: none;
  background-color: rgba(148, 154, 235, 39);
  //头像
  &_avatar {
    position: absolute;
    left: 0;
  }
  //用户姓名
  &_truename {
    left: 35px;
    position: absolute;
    font-size: 12px;
  }
  //删除按钮
  &_remove {
    position: absolute;
    right: 0;
  }
}
</style>
