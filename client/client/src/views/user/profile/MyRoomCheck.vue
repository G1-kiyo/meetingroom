<template>
  <div class="roomcheck">
    <!--侧边栏  -->
    <div class="roomcheck_sidebar--outer">
      <sidebar :userJobInfo="userJobInfo"></sidebar>
    </div>
    <div class="roomcheck_info">
      <div class="roomcheck_info_index">
        <!--导航栏  -->
        <nav class="index_nav">
          <div
            class="index_nav_item"
            :class="{ selected: item.focus }"
            v-for="item in nav"
            :key="item.label"
          >
            {{ item.label }}
          </div>
        </nav>
        <!-- 主要内容 -->
        <main class="index_content">
          <!-- 会议预定信息 -->
          <div
            class="main_request"
            v-for="request in myRoomBookInfo"
            :key="request.meetingId"
          >
            <span class="main_request_time">{{
              request.startTime.split(" ")[1] + "-" + request.endTime.split(" ")[1]
            }}</span>
            <div class="main_request_item">
              <span :style="{ backgroundColor: '#00008B' }">meeting_id</span>
              <span>{{ request.meetingId }}</span>
            </div>
            <div class="main_request_item">
              <span :style="{ backgroundColor: '#AE5148' }">room_id</span>
              <span>{{ request.roomId }}</span>
            </div>
            <div class="main_request_item">
              <span :style="{ backgroundColor: '#8B005D' }">meeting_title</span>
              <span>{{ request.title }}</span>
            </div>
            <div class="main_request_item">
              <span :style="{ backgroundColor: '#8B7700' }">member_num</span>
              <span>{{ request.memberCount }}</span>
            </div>
            <div class="main_request_buttongroup">
              <button
                class="main_request_buttongroup check"
                @click="checkDetail(request.meetingId)"
              >
                CHECK
              </button>
              <button
                class="main_request_buttongroup delete"
                @click="warningBox(request.meetingId)"
              >
                DELETE
              </button>
            </div>
            <el-divider :style="{ borderColor: '#666666' }"></el-divider>
          </div>
        </main>

        <!-- 日历 -->
        <div class="main_datepicker--outer">
          <date-picker v-model="date" color="green" trim-weeks mode="date"></date-picker>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Sidebar from "@/components/Sidebar.vue";
import store from "@/store.js";
import { DatePicker } from "v-calendar";
import "v-calendar/dist/style.css";
import { ElMessageBox, ElMessage } from "element-plus";
export default {
  name: "MyRoomCheck",
  components: {
    sidebar: Sidebar,
    "date-picker": DatePicker,
  },
  data() {
    return {
      nav: {
        basic: {
          label: "basic",
          focus: false,
          path: "/user/profile/basic",
        },
        schedule: {
          label: "schedule",
          focus: false,
          path: "/user/profile/schedule",
        },
        update: {
          label: "update",
          focus: false,
          path: "/user/profile/update",
        },
        request: {
          label: "request",
          focus: true,
          path: "/user/profile/my_room_check",
        },
      },
      myRoomBookInfo: [],
      date: new Date(),
      infoGroupByDepartment: {},
      userJobInfo: {},
    };
  },
  created() {
    this.requestMyRoomBookInfo();
    this.$api
      .requestUserJobInfo({
        params: {
          userId: store.state.userId,
        },
      })
      .then((res) => {
        this.userJobInfo = res.data.userJobInfo;
      });
  },
  computed: {},
  watch: {
    date: function (newVal, oldVal) {
      if (newVal != oldVal) {
        this.requestMyRoomBookInfo();
      }
    },
    // 监听路由
    $route(to) {
      for (var key in this.nav) {
        if (this.nav[key].path == to.path) {
          this.nav[key].focus = true;
        } else {
          this.nav[key].focus = false;
        }
      }
    },
  },
  methods: {
    checkDetail: function (meetingId) {
      this.$router.push({ path: "/user/profile/room_check_detail/" + meetingId });
    },
    requestMyRoomBookInfo: function () {
      this.$api
        .requestMyRoomBookInfo({
          params: {
            userId: store.state.userId,
            date: this.$dateProcess.dateFormat(this.date, "YYYY-MM-DD"),
          },
        })
        .then((res) => {
          this.myRoomBookInfo = res.data;
        });
    },
    deleteMyRoomBookInfo: function (meetingId) {
      var formData = new FormData();
      formData.append("meetingId", meetingId);
      this.$api
        .deleteMyRoomBookInfo({
          data: formData,
        })
        .then(() => {
          ElMessage({
            type: "success",
            message: "Delete completed",
          });
          this.myRoomBookInfo = this.myRoomBookInfo.filter((item) => {
            return item.meetingId != meetingId;
          });
        });
    },
    warningBox: function (meetingId) {
      ElMessageBox.confirm("Are you sure you want to delete this request", "Warning", {
        confirmButtonText: "YES",
        cancelButtonText: "Cancel",
        type: "warning",
        center: true,
      }).then(() => {
        this.deleteMyRoomBookInfo(meetingId);
      });
    },
  },
};
</script>
<style>
.datepicker {
  width: 90% !important;
}
.select_start {
  width: 90% !important;
}

.select_end {
  width: 90% !important;
}
</style>
