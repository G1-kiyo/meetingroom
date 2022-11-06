<template>
  <div class="group-detail">
    <!--启动视频、音频等相关活动  -->
    <div class="g-start">
      <!--选中成员  -->
      <div class="g-start-member">
        <div class="m-item" v-for="item in memberList" :key="item.userId">
          <el-avatar :size="30" :src="item.avatar"></el-avatar>
          <span>{{ item.truename }}</span>
          <el-icon class="m-item-remove" @click="removeMember(item.userId)" color="red"
            ><CircleCloseFilled
          /></el-icon>
        </div>
        <div class="m-extra">
          <span v-if="isMore">{{ moreText }}</span>
          <div class="m-extra-ope">
            <div>
              <el-icon color="#666666" @click="toVideoPattern"><VideoCamera /></el-icon>
            </div>
            <div>
              <el-icon color="#666666" @click="toVideoPattern"><Microphone /></el-icon>
            </div>
            <div>
              <el-icon color="#666666"><ChatSquare /></el-icon>
            </div>
          </div>
        </div>
      </div>
      <!-- 搜索成员 -->
      <div class="g-start-search">
        <el-input
          v-model="searchVal"
          placeholder="search member"
          @input="search"
          @blur="searchResultShow = false"
          ><template #prepend
            ><el-icon><Search /></el-icon></template
        ></el-input>
        <ul class="search-result" v-show="searchResultShow">
          <li v-for="(re, index) in searchResult" :key="index" @click="showInDetail(re)">
            {{ re }}
          </li>
        </ul>
      </div>
    </div>
    <!-- 主要成员信息 -->
    <main class="g-main">
      <div class="g-main-cont" v-for="(group, key, index) in groupList" :key="key">
        <div class="brief-info">
          <div class="groupInfo">
            <div>{{ key }}</div>
            <span>{{
              group.memberlist.length + group.memberlist.length > 1 ? "members" : "member"
            }}</span>
          </div>
          <div class="memberInfo">
            <span>Members</span>
            <div>
              <div v-for="n in 3" :key="n">
                {{ group.memberlist[n - 1].truename[0] }}
              </div>
              <el-icon @click="group.detailShow = true"><MoreFilled /></el-icon>
            </div>
          </div>
        </div>
        <div class="detail" v-show="group.detailShow">
          <span @click="group.detailShow = false">Fold</span>
          <ul>
            <li
              v-for="member in group.memberlist"
              :key="member.userId"
              @click="selectMember(member)"
            >
              <el-avatar :size="30" :src="member.avatar"></el-avatar>
              <div>
                <span>{{ member.truename }}</span>
                <span>{{ member.position + " " + member.department }}</span>
              </div>
              <svg
                t="1666106875248"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="8653"
                width="48"
                height="48"
              >
                <!-- viewBox="x, y, width, height" // x:左上角横坐标，y:左上角纵坐标，width:宽度，height:高度

                1.当viewbox中的width和height1是svg宽高的一半时，相当于svg会被放大一倍

                2.当viewbox中的width和height1是svg宽高的二倍时，相当于svg会被缩小一倍 -->
                <path
                  d="M12.8 512c0 275.2512 223.9488 499.2 499.2 499.2s499.2-223.9488 499.2-499.2a502.1184 502.1184 0 0 0-282.88-449.9968 38.4 38.4 0 1 0-33.28 69.2224A424.96 424.96 0 0 1 934.4 512c0 232.9088-189.4912 422.4-422.4 422.4S89.6 744.9088 89.6 512 279.0912 89.6 512 89.6a38.4 38.4 0 0 0 0-76.8C236.7488 12.8 12.8 236.7488 12.8 512z"
                  fill="#438CFF"
                  p-id="8654"
                  class="circle"
                  :class="{ ani: member.selected }"
                ></path>
                <path
                  d="M285.7472 466.7392a38.4 38.4 0 1 0-54.3232 54.3232l180.992 180.992a38.2976 38.2976 0 0 0 54.272 0l325.8368-325.7856a38.4 38.4 0 1 0-54.3232-54.3232l-298.7008 298.6496-153.7536-153.856z"
                  fill="#438CFF"
                  p-id="8655"
                  class="tick"
                  :class="{ ani: member.selected }"
                ></path>
              </svg>
            </li>
          </ul>
        </div>
      </div>
    </main>
  </div>
</template>
<script>
import { reactive, ref, inject } from "vue";
import store from "@/store.js";
function useMember() {
  const memberList = ref(store.state.selectedMember);
  const removeMember = function (userId) {
    store.commit("removeMember", { userId: userId });
    memberList.value = store.state.selectedMember;
  };
  const selectMember = function (member) {
    store.commit("addMember", { member: member });
    for (let member of store.state.selectedMember) {
      member.selected = true;
    }
    memberList.value = store.state.selectedMember;
  };

  return { memberList, removeMember, selectMember };
}
function useSearch() {
  const searchVal = ref("");
  //   const matchMemberGroup = ref([]);
  const search = function () {
    let s = new RegExp(".*" + searchVal + ".*");
    for (let group in memberInfo.groupList) {
      for (let member of memberInfo.groupList[group]) {
        if (searchVal.test(member.truename)) {
          memberInfo.groupList[group].detailShow = true;
          break;
        }
      }
    }
    return { searchVal, search };
  };
}
export default {
  setup() {
    // 一般来说基本数据类型用ref，引用类型用reactive
    // 但reactive声明的变量替换整个对象，会更改引用对象，造成响应无效
    // 在这种情况下若仍想用reactive，可以将需要更改的整个对象放在该变量的属性下
    // 简单点可以用ref，直接替换xxx.value=[]
    const memberInfo = reactive({
      allMember: [],
      groupList: {},
    });
    //关于在main.js声明的全局指令，可以先在main.js文件provide，然后在需要引入的文件中inject
    const api = inject("$api");
    // 可以在onmounted处请求数据
    onMounted(async () => {
      // await返回的数据是请求成功后获得的res数据，即then后的结果
      let result = await api.requestAllMember({});
      memberInfo.allMember = result.data.allMemberInfo;
      memberInfo.groupList = allMemberInfoProcess(memberInfo.allMember);
    });

    //按部门分类所有成员
    const allMemberInfoProcess = function (allMemberInfo) {
      memberInfo.groupList = {};
      for (var item of allMemberInfo) {
        if (item.department in memberInfo.groupList) {
          item["selected"] = false;
          memberInfo.groupList[item.department].detailShow = false;
          memberInfo.groupList[item.department].memberlist.push(item);
        } else {
          memberInfo.groupList[item.department] = [];
          item["selected"] = false;
          memberInfo.groupList[item.department].detailShow = false;
          memberInfo.groupList[item.department].memberlist.push(item);
        }
      }
      // this.infoGroupByDepartment = newMap;
    };
    //成员列表
    const { memberList, removeMember, selectMember } = useMember();
    //搜索
    const { searchVal, search } = useSearch();
    return {
      memberInfo,
      allMemberInfoProcess,
      memberList,
      removeMember,
      selectMember,
      searchVal,
      search,
    };
  },
};
</script>
