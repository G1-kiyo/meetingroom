<template>
  <aside class="sidebar--inner">
    <!-- 基本信息 -->
    <div class="sidebar_basicinfo">
      <div>
        <!-- 头像图片 -->
        <img :src="userJobInfo.avatar" :alt="userJobInfo.truename" />
      </div>

      <div>
        <span>{{ userJobInfo.truename }}</span
        ><br />
        <span>{{ userJobInfo.department }}</span>
        <span>{{ userJobInfo.position }}</span>
      </div>
    </div>
    <el-divider></el-divider>
    <!-- 导航栏 -->
    <ul class="sidebar_nav" @click="onNavSelected($event)">
      <li class="profile">
        <el-icon :size="20">
          <UserFilled />
        </el-icon>
        <span>{{ nav.profile.label }}</span>
      </li>
      <li class="online">
        <el-icon :size="20">
          <Platform />
        </el-icon>
        <span>{{ nav.online.label }}</span>
      </li>
      <li class="book">
        <el-icon :size="20">
          <Management />
        </el-icon>
        <span>{{ nav.book.label }}</span>
      </li>
      <li class="file">
        <el-icon :size="20">
          <Upload />
        </el-icon>
        <span>{{ nav.file.label }}</span>
      </li>
    </ul>
    <el-divider></el-divider>
    <!-- 其他操作 -->
    <ul class="sidebar_ope" @click="onOpeSelected($event)">
      <li class="help">
        <el-icon :size="20">
          <Help />
        </el-icon>
        <span>{{ ope.help.label }}</span>
      </li>
      <li class="logout">
        <span
          class="iconfont icon-tuichu"
          :style="{ margin: 0, fontSize: '20px' }"
        ></span>
        <span>{{ ope.logout.label }}</span>
      </li>
    </ul>
  </aside>
</template>
<script>
import store from "@/store.js";
export default {
  name: "Sidebar",
  props: ["userJobInfo"],
  data() {
    return {
      nav: {
        profile: {
          label: "My Profile",
          route: { path: "/user/user_profile" },
        },
        online: {
          label: "Online Meeting",
          route: { path: "/meeting/online/online_meeting_schedule" },
        },
        book: {
          label: "Book Meeting-Room",
          route: { path: "/meeting/offline/room_info_query" },
        },
        file: {
          label: "File Management",
          route: { path: "/file/file_management_index" },
        },
      },
      ope: {
        help: {
          label: "Help",
          route: { path: "/help" },
        },
        logout: {
          label: "Log Out",
        },
      },
    };
  },
  methods: {
    // 主菜单栏选中
    onNavSelected: function (e) {
      if (e.currentTarget.className && e.currentTarget.className != "sidebar_nav") {
        this.$router.push(this.nav[e.currentTarget.class]);
      }
    },
    // 其他操作栏选中
    onOpeSelected: function (e) {
      if (e.currentTarget.className == "help") {
        this.$router.push(this.ope[e.currentTarget.className]);
      }
      if (e.currentTarget.className == "logout") {
        store.commit("clearToken");
        store.commit("clearUserId");
        this.$router.push({ path: "/user/login" });
      }
    },
  },
};
</script>
<style lang="less" scoped>
@import "@/assets/css/var.less"; // 侧边栏
.sidebar {
  &--inner {
    height: 450px;
    margin: 50px 0 0 27px;
    padding: 45px 17px 17px 25px;
    background-color: #64cb53;
    border: none;
    border-radius: 3px;
    color: @color_white;
    font-size: 14px;
  }

  // 基本信息栏
  &_basicinfo {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    & > div:first-child {
      display: inline-block;
      border-radius: 50%;
      width: 66px;
      height: 66px;
      & > img {
        width: 66px;
        height: 66px;
        border-radius: 50%;
      }
    }

    & > div:nth-child(2) {
      display: inline-block;
      margin-left: 15px;
      & > span {
        line-height: 20px;
      }
    }
  }

  // 导航栏+其他操作栏
  &_nav,
  &_ope {
    margin-left: 0;
    padding: 0;

    & > li {
      border: none;
      height: 45px;
      margin: none;
      text-align: left;
      padding: none;
      list-style: none;

      & > span {
        margin-left: 10px;
      }
    }
  }

  &_ope {
    margin-top: 10px;
  }
}
</style>
