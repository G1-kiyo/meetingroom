<template>
  <el-avatar :size="50" :src="memberInfo.avatar" :style="{ marginRight: '15px' }" />
  <div class="item_info_text">
    <span>{{ memberInfo.truename }}</span>
    <div>
      <span>{{ memberInfo.position }}</span>
      <span :style="{ marginLeft: '5px' }">{{ memberInfo.department }}</span>
    </div>
  </div>
  <div
    class="item_operation"
    @mouseover="operationShow = true"
    @mouseleave="operationShow = false"
  >
    <el-icon><MoreFilled /></el-icon>
    <ul v-if="operationShow">
      <li @click="memberInfo.selected = true">Add member</li>
      <li @click="memberInfo.selected = false">Remove member</li>
    </ul>
  </div>
</template>
<script>
export default {
  name: "SearchMemberItem",
  props: ["member", "search"],
  data() {
    return {
      memberInfo: this.member,
      operationShow: false,
    };
  },
  emits: ["addMember", "removeMember"],
  watch: {
    "memberInfo.selected": {
      handler: function (newVal) {
        if (newVal == true && this.search) {
          this.$emit("addMember", this.memberInfo);
        } else if (newVal == false && this.search) {
          this.$emit("removeMember", this.memberInfo.userId);
        }
      },
    },
  },
};
</script>
<style lang="less" scoped>
.item_info {
  &_text {
    margin-left: 20px;
    display: inline-block;
  }
}
//操作栏(包括添加和删除)
.item_operation {
  /* float: right; */
  display: inline-flex;
  flex-grow: 1;
  flex-direction: row;
  justify-content: flex-end;
  & > ul {
    margin: 0;
    padding: 0;
    list-style-type: none;
    position: absolute;
    box-shadow: 2px 2px 7px 0.5px #cccccc;
    background-color: white;
    & > li {
      margin: 0;
      padding: 0;
      width: 168px;
      height: 26px;
      text-align: left;
      border-width: 1px;
      border-style: none none solid none;
      border-color: #cccccc;
      line-height: 26px;
    }
  }
}
</style>
