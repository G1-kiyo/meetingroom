<template>
  <div class="department_name">
    <span :style="{ verticalAlign: 'top', lineHeight: '32px' }">{{ department }}</span>
    <!-- 利用箭头函数传递自定义参数 -->
    <el-checkbox v-if="batchSelection" :style="{ marginLeft: '10px' }" v-model="selectAll"
      >Select All</el-checkbox
    >
  </div>
  <div v-for="member in departmentInfo" :key="member.userId" class="member_item">
    <member-item
      :search="search"
      :selectAll="selectAll"
      :member="member"
      @addMember="addMember"
      @removeMember="removeMember"
    ></member-item>
  </div>
</template>
<script>
import MemberItem from "./MemberItem.vue";
export default {
  name: "MemberGroupByDepartment",
  props: ["departmentInfo", "department", "selectedNum", "search", "batchSelection"],
  emits: ["addMember", "removeMember"],
  components: {
    "member-item": MemberItem,
  },
  data() {
    return { selectAll: false };
  },
  watch: {
    // selectAllMembers: function (newVal, oldVal) {
    //   this.allChecked = newVal;
    //   console.log(this.allChecked);
    //   // console.log(this.selectAllMembers);
    // },
  },
  methods: {
    addMember: function (memberInfo) {
      this.$emit("addMember", memberInfo);
    },
    removeMember: function (userId) {
      this.$emit("removeMember", userId);
      if (this.selectAll == true && this.selectedNum == 1) this.selectAll = false;
    },
  },
};
</script>
<style lang="less">
@import "@/assets/css/var.less";
.member_item {
  padding: 0 10px;
  margin-bottom: 10px;
  width: 100%;
  border-width: 1px;
  border-style: none none solid none;
  border-color: #cccccc;
}
.department_name {
  position: sticky;
  //当小于该top值时，实现固定定位
  top: 0px;
  width: 100%;
  height: 24px;
  color: @color_white;
  font-weight: bold;
  background-color: #979dee;
  text-align: center;
  z-index: 2;
}
</style>
