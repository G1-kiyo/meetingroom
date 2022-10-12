<template>
  <div class="member">
    <div class="member_extra">
      <!-- title+搜索栏 -->
      <div>Team members</div>
      <el-icon :size="24" class="member_extra_close" @click="$emit('handleMemberClosed')">
        <CircleCloseFilled />
      </el-icon>
      <div class="member_extra_search" @mouseleave="searchResultShow = false">
        <el-input
          class="search_input"
          v-model="memberNameInput"
          placeholder="find an employee"
          @focus="searchMemberName"
          @input="searchMemberName"
        >
          <template #prepend>
            <el-icon>
              <Search />
            </el-icon>
          </template>
        </el-input>
        <div class="search_result" v-if="searchResultShow">
          <ul class="search_result_list">
            <li v-if="searchResult.length == 0">Member information is not found.</li>
            <div v-else>
              <li class="list_item" v-for="member in searchResult" :key="member.userId">
                <search-member-item
                  :search="searchResultShow"
                  :member="member"
                  @addMember="addMember"
                  @removeMember="removeMember"
                ></search-member-item>
              </li>
            </div>
          </ul>
        </div>
      </div>
    </div>

    <el-divider></el-divider>
    <!-- 主要的成员信息 -->
    <div class="member_info">
      <button
        class="member_info_batchselection"
        @click="batchSelection = !batchSelection"
      >
        {{ batchSelection == true ? "Cancel" : "Batch Selection" }}
      </button>
      <ul class="member_info_list">
        <li v-for="(departmentInfo, key, index) in infoGroupByDepartment" :key="key">
          <member-department
            :batchSelection="batchSelection"
            :search="searchResultShow"
            :departmentInfo="departmentInfo"
            :department="key"
            :selectedNum="selectedMembers.length"
            @addMember="addMember"
            @removeMember="removeMember"
          ></member-department>
        </li>
      </ul>
    </div>
    <el-divider></el-divider>
    <div class="member--already_in">
      <span class="already_in_title">Already In</span>
      <!-- v-for循环最好需要搭配key，且key不应简单设为index -->
      <div v-for="member in selectedMembers" :key="member.userId">
        <selected-member-item
          :member="member"
          @addMember="addMember"
          @removeMember="removeMember"
        ></selected-member-item>
      </div>
    </div>
  </div>
</template>

<script>
import SearchMemberItem from "./member/SearchMemberItem.vue";
// import MemberItem from "./member/MemberItem.vue";
import store from "@/store.js";
import SelectedMemberItem from "./member/SelectedMemberItem.vue";
import MemberGroupByDepartment from "./member/MemberGroupByDepartment.vue";
export default {
  name: "MemberInfo",
  emits: ["handleMemberClosed"],
  components: {
    "search-member-item": SearchMemberItem,
    // "member-item": MemberItem,
    "selected-member-item": SelectedMemberItem,
    "member-department": MemberGroupByDepartment,
  },
  data() {
    return {
      memberNameInput: "",
      searchResult: [],
      allMemberInfo: [],
      infoGroupByDepartment: {},
      searchResultShow: false,
      selectedMembers: store.state.selectedMember,
      batchSelection: false,
    };
  },
  created() {
    this.showMember();
  },

  methods: {
    addMember: function (memberInfo) {
      store.commit("addMember", { member: memberInfo });
      // this.selectedMembers.push(memberInfo);
      // this.$emit("handleSelectedMember", this.selectedMembers);
    },
    removeMember: function (userId) {
      // for (let i = 0; i < this.selectedMembers.length; i++) {
      //   if (this.selectedMembers[i].userId == userId) {
      //     store.commit("removeMember", { index: i });
      //     // this.selectedMembers.splice(i, 1);
      //   }
      // }
      store.commit("removeMember", {
        userId: userId,
      }); // this.$emit("handleSelectedMember", this.selectedMembers);
      // console.log(this.selectedMembers);
    },
    searchMemberName: function () {
      this.searchResultShow = true;
      this.searchResult = [];
      var pattern = new RegExp(".*" + this.memberNameInput + ".*");
      for (var item of this.allMemberInfo) {
        // var user = this.userJobInfo[key];
        if (
          pattern.test(item.truename) ||
          pattern.test(item.department) ||
          pattern.test(item.position)
        ) {
          this.searchResult.push(item);
        }
      }
    },
    showMember: function () {
      this.$api.requestAllMember({}).then((res) => {
        this.allMemberInfo = res.data.allMemberInfo;
        // console.log(this.allMemberInfo);
        this.allMemberInfoProcess(this.allMemberInfo);
      });
    },
    //按部门分类所有成员
    allMemberInfoProcess: function (allMemberInfo) {
      this.infoGroupByDepartment = {};
      for (var item of allMemberInfo) {
        if (item.department in this.infoGroupByDepartment) {
          item["selected"] = false;
          this.infoGroupByDepartment[item.department].push(item);
        } else {
          this.infoGroupByDepartment[item.department] = [];
          item["selected"] = false;
          this.infoGroupByDepartment[item.department].push(item);
        }
      }
      // this.infoGroupByDepartment = newMap;
    },
  },
};
</script>
<style lang="less" scoped>
@import "@/assets/css/var.less";

.member {
  width: 388px;
  height: 559px;
  position: fixed;
  left: 50%;
  top: 50%;
  margin-left: -194px;
  margin-top: -279.5px;
  z-index: 9999;
  background-color: @color_white;
  border: none;
  box-shadow: 1px 1px 10px 1px #cccccc;
  border-radius: 3px;

  &_extra {
    //title
    padding: 0 25px 0 10px;
    & > div:first-child {
      display: inline-block;
      font-size: 20px;
      font-family: "raleway";
    }
    //关闭按钮
    &_close {
      // width: 24px;
      // height: 24px;
      position: absolute;
      right: 0px;
    }
    // 搜索栏
    &_search {
      position: relative;
      // 输入框
      .search_input {
        height: 27px;
        width: 330px;
        margin-top: 10px;
      }
      // 结果栏
      .search_result {
        // position: absolute;
        /* bottom: -5px; */
        left: 0px;
        width: calc(100% - 20px);
        /* right: 0; */
        height: 180px;
        overflow-y: scroll;
        position: absolute;
        z-index: 3;
        border: none;
        background-color: white;
        box-shadow: 1px 1px 10px 2px #cccccc;
        &::-webkit-scrollbar {
          display: none;
        }
        &_list {
          padding: 5px;
          margin: 0;
          list-style-type: none;
          .list_item {
            width: 100%;
            border: none none solid none;
            border-color: #cccccc;
            margin: 0 0 10px 0;
            padding: 0;
            display: flex;
            /* flex-grow: 1; */
            flex-direction: row;
            align-items: center;
            /* width: 100%; */
          }
        }
      }
    }
  }
  &_info {
    padding: 0 25px 0 10px;
    &_batchselection {
      position: relative;
      left: 69%;
      width: 110px;
      height: 30px;
      background-color: @color_blue;
      border: none;
      color: white;
      border-radius: 3px;
    }
    &_list {
      margin: 15px 0 0 0;
      height: 200px;
      padding: 0;
      list-style: none;
      // position: relative;
      border: 1px solid;
      overflow-y: scroll;
      .member_item {
        padding: 0 10px;
        margin-bottom: 10px;
        width: 100%;
        border-width: 1px;
        border-style: none none solid none;
        border-color: #cccccc;
      }
      &::-webkit-scrollbar {
        display: none;
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
    }
  }
  &--already_in {
    height: 100px;
    overflow-y: scroll;
    &::-webkit-scrollbar {
      width: 3px;
      height: 180px;
    }
    &::-webkit-scrollbar-thumb {
      box-shadow: inset 0 0 0px rgba(240, 240, 240, 0.5);
      background-color: #62ab63;
    }
    &::-webkit-scrollbar-track {
      //内投影/offset-x/offset-y/阴影模糊blur/颜色color
      box-shadow: inset 0 0 0px rgba(240, 240, 240, 0.5);
      border-radius: 10px;
      background-color: rgba(240, 240, 240, 0.5);
    }
    & > span {
      font-family: "raleway";
      font-size: 20px;
      display: block;
    }
    & > div {
      margin: 10px 0 0 10px;
      display: inline-flex;
      width: 100px;
      height: 30px;
      position: relative;
      text-align: center;
      flex-direction: row;
      align-items: center;
      border-radius: 50px;
      background-color: rgba(148, 154, 235, 0.3);
    }
  }
}
</style>
