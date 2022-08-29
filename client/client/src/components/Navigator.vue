<template>
  <!-- 顶部导航栏 -->
  <el-menu
    router
    :default-active="activeIndex"
    background-color="rgba(255,255,255,0)"
    text-color="#00008B"
    class="common_menu"
    mode="horizontal"
  >
    <el-menu-item index="/meeting/offline/room_info_query">Overview</el-menu-item>
    <el-menu-item index="/user/user_profile">Profile</el-menu-item>
    <el-menu-item index="/meeting/online/online_meeting_schedule"
      >Online Meeting</el-menu-item
    >
    <el-menu-item :index="currentIndex">Book Meeting-Room</el-menu-item>
    <el-menu-item index="/file/file_management_index">File</el-menu-item>
  </el-menu>
</template>
<script>
export default {
  name: "Navigator",
};
</script>
<style scoped>
.common_menu {
  /* margin-top: 30px; */
}
</style>
