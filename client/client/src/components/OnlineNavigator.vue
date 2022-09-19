<template>
  <div class="online_navigator">
    <div
      v-for="item in nav"
      :key="item.label"
      class="navigator_item"
      @focus="item.focus = true"
      @blur="item.focus = false"
    >
      <router-link v-if="item.router" :to="item.path"
        ><div class="navigator_item_icon">
          <el-icon
            v-if="item.el"
            size="37px"
            class="normal_icon"
            :style="{ color: item.focus ? item.iconColor : '#666666' }"
            >{{ test }}</el-icon
          >
          <i
            v-else
            :class="item.icon"
            class="normal_icon"
            :style="{ color: item.focus ? item.iconColor : '#666666' }"
          ></i>
        </div>
      </router-link>
      <div v-else class="navigator_item_icon">
        <el-icon
          v-if="item.el"
          size="30px"
          class="normal_icon"
          :style="{ color: item.focus ? item.iconColor : '#666666' }"
          v-html="item.icon"
        ></el-icon>
        <i
          v-else
          :class="item.icon"
          class="normal_icon"
          :style="{ color: item.focus ? item.iconColor : '#666666' }"
        ></i>
      </div>
      <span class="navigator_item_text">{{ item.label }}</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "OnlineNavigator",
  props: ["test"],
  data() {
    return {
      nav: {
        home: {
          router: true,
          el: true,
          label: "Home",
          path: "/meeting/online/conversation_choice",
          icon: "<HomeFilled />",
          iconColor: "#264DE8",
          focus: true,
        },
        group: {
          router: true,
          el: false,
          label: "Group",
          path: "/meeting/online/group_query",
          icon: "iconfont icon-qunzu",
          iconColor: "#264DE8",
          focus: false,
        },
        history: {
          router: false,
          el: true,
          label: "History",
          icon: "<Clock />",
          iconColor: "#264DE8",
          focus: false,
        },
        record: {
          router: false,
          el: false,
          label: "Record",
          icon: "iconfont icon-record-circle-fill",
          iconColor: "#FF0000",
          focus: false,
        },
        exit: {
          router: false,
          el: true,
          label: "Exit",
          icon: "<SwitchButton />",
          iconColor: "#FF0000",
          focus: false,
        },
      },
    };
  },
};
</script>
<style lang="less" scoped>
//导航容器
.online_navigator {
  position: fixed;
  float: left;
  border: none;
  background-color: white;
  margin-left: 5px;
  //单个导航项
  .navigator_item {
    width: 72px;
    height: 67px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
}
</style>
