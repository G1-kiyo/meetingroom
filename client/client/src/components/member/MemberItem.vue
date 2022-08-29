<template>
  <el-checkbox v-model="memberInfo.selected"></el-checkbox>
  <div class="member_info_list member_item_info">
    <el-avatar :size="50" :src="memberInfo.avatar" />
    <div>
      <span>{{ memberInfo.truename }}</span>
      <div>
        <span>{{ memberInfo.position }}</span>
        <span :style="{ marginLeft: '5px' }">{{ memberInfo.department }}</span>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "MemberItem",
  props: ["member", "selectAll", "search"],
  emits: ["addMember", "removeMember"],
  data() {
    return {
      memberInfo: this.member,
    };
  },
  watch: {
    selectAll: {
      handler: function (newVal) {
        if (newVal == true) {
          this.memberInfo.selected = true;
        } else {
          this.memberInfo.selected = false;
        }
      },
    },
    "memberInfo.selected": {
      handler: function (newVal) {
        if (newVal == true && !this.search) {
          this.$emit("addMember", this.memberInfo);
        } else if (newVal == false && !this.search) {
          this.$emit("removeMember", this.memberInfo.userId);
        }
      },
    },
  },
};
</script>
<style lang="less" scoped>
.member_info_list {
  //列表项
  &.member_item {
    padding: 0 10px;
    margin-bottom: 10px;
    width: 100%;
    border: none none solid none;
    border-color: #cccccc;
    //头像+文字信息
    &_info {
      display: inline-block;
      margin-left: 15px;
      & > div {
        margin-left: 20px;
        display: inline-block;
      }
    }
  }
}
</style>
