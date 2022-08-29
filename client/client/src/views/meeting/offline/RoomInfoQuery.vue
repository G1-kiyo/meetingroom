<template>
  <div
    class="meetingroom"
    :style="{
      '--grid-template-rows': gridTemplateRows,
      '--grid-template-areas': gridTemplateAreas,
    }"
  >
    <div class="meetingroom_sidebar--outer">
      <!-- 侧边栏 -->
      <sidebar :userJobInfo="userJobInfo"></sidebar>
    </div>

    <div class="meetingroom_infoshow">
      <!-- 检索结果 -->
      <div class="meetingroom_infoshow_search" v-if="search">
        <div class="meetingroom_infoshow_search nav">
          <el-breadcrumb :separator-icon="ArrowRight">
            <el-breadcrumb-item :to="{ path: '/blank' }"
              >Book Meeting-Room</el-breadcrumb-item
            >
            <el-breadcrumb-item>search result</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div
          class="meetingroom_infoshow_search result"
          :style="{ gridTemplateRows: gridTemplateRows }"
        >
          <div v-if="isFound">
            <el-card
              :body-style="{ padding: '0px' }"
              @click="toDayDetail($event)"
              v-for="item in searchResult"
              :key="item.roomId"
              :data-key="item.roomId"
              ><mtcard :meetingroomInfo="item" :date="date"></mtcard
            ></el-card>
          </div>

          <p v-else :style="{ fontSize: '14px' }">What you search is not found.</p>
        </div>
      </div>
      <main class="meetingroom_infoshow_main" v-else>
        <!-- 个人已预约按钮 -->
        <div class="meetingroom_infoshow_main mybook--outer">
          <button
            class="meetingroom_infoshow_main mybook--inner"
            @click="window.location.href = '/user/user_profile/#request'"
          >
            MY BOOKING
          </button>
        </div>
        <!-- 操作栏 -->
        <div class="meetingroom_infoshow_main operation">
          <div class="operation_timegroup">
            <!-- 日期选择 -->
            <div class="timegroup_date">
              <!-- 行内样式用:style绑定 -->
              <span :style="{ display: 'block' }">Date</span>
              <el-date-picker
                v-model="date"
                type="date"
                format="MM/DD/YYYY"
                value-format="YYYY-MM-DD"
                @change="onDateSelected"
              />
            </div>
            <!-- 时间选择 -->
            <div class="timegroup_time">
              <div class="timegroup_time_starttime">
                <span :style="{ display: 'block' }">Start Time</span>
                <el-time-select
                  v-model="startTime"
                  :max-time="endTime"
                  placeholder="start time"
                  start="09:00"
                  step="00:30"
                  end="21:00"
                />
              </div>
              <div class="timegroup_time_endtime">
                <span :style="{ display: 'block' }">End Time</span>
                <el-time-select
                  v-model="endTime"
                  :min-time="startTime"
                  placeholder="end time"
                  start="09:00"
                  step="00:30"
                  end="21:00"
                />
              </div>
            </div>
          </div>
          <!-- 其他操作 -->
          <div class="operation_other">
            <el-select
              v-model="sort"
              class="other_sort"
              @change="onOptionSelected"
              @blur="onOptionSelected"
            >
              <template #prefix>
                <span>Sort:</span>
              </template>
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <el-input
              class="other_search"
              v-model="enteredRoom"
              placeholder="search meeting-room"
              v-on:keyup.enter="searchRoom"
            >
              <template #suffix>
                <el-icon @click="searchRoom">
                  <Search />
                </el-icon>
              </template>
            </el-input>
          </div>
        </div>
        <!-- 主要会议室信息 -->
        <div
          class="meetingroom_infoshow_main roomrelated"
          :style="{ gridTemplateRows: gridTemplateRows }"
        >
          <el-card
            :body-style="{ padding: '0px' }"
            @click="toDayDetail($event)"
            v-for="item in meetingroomInfo"
            :key="item.roomId"
            :data-key="item.roomId"
            ><mtcard :meetingroomInfo="item" :date="date"></mtcard
          ></el-card>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import MeetingRoomCard from "@/components/MeetingRoomCard.vue";
import Sidebar from "@/components/Sidebar.vue";
import store from "../../../store.js";
// import { ArrowRight } from "@element-plus/icons-vue";
// import { UserFilled } from '@element-plus/icons-vue/dist/types';
export default {
  name: "RoomInfoQuery",
  components: {
    mtcard: MeetingRoomCard,
    sidebar: Sidebar,
  },
  data() {
    return {
      // gridTemplateRows: "50px 50px 200px 60vh",
      // gridTemplateAreas: "'sidebar .'\
      //                     'sidebar mybook'\
      //                     'sidebar operation'\
      //                     'sidebar room'",
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
      date: this.$dateProcess.dateFormat(new Date(), "YYYY-MM-DD"),
      startTime: "",
      endTime: "",
      sort: "",
      options: {
        scale: {
          label: "Largest scale",
          value: "Largest scale",
        },
        slot: {
          label: "Most slots",
          value: "Most slots",
        },
      },
      search: false,
      enteredRoom: "",
      searchResult: {},
      isFound: false,
      userJobInfo: {},
      originalMeetingroomInfo: {},
      //不能直接相互调用（结果会是undefined），如果两个数据存在依赖关系，可以考虑用computed
      // test1: this.userJobInfo,
      meetingroomInfo: {},
      meetingroomCount: 0,
      test: {},
    };
  },
  created() {
    // 在created或mounted预请求数据，具体区别
  },
  mounted() {
    try {
      const userJobInfo = this.$api.requestUserJobInfo({
        params: { userId: store.state.userId },
      });
      // this.$api.requestUserJobInfo({ data: { username: "d77324bbfd4111ecb37e80e82c2b533f"} })
      //     .then((res)=>{
      //         console.log(res);
      //     })
      const meetingroomInfo = this.$api.requestMeetingRoomInfo({
        params: { currentDate: this.date },
      });
      Promise.all([userJobInfo, meetingroomInfo]).then((responses) => {
        // console.log(responses)
        // 如果还有后续操作，可以用then接收第一个then return的内容
        // map和forEach区别在于前者会返回新数组，后者不会，
        // 注意：map在遍历每一个元素时，需要return 返回新的值，单语句可以忽略return
        return responses.map((response) => {
          if (response.data.userJobInfo) {
            this.userJobInfo = response.data.userJobInfo;
          }
          if (response.data.meetingroomInfo) {
            this.originalMeetingroomInfo = response.data.meetingroomInfo;
            this.meetingroomInfo = this.meetingRoomInfoProcess(
              response.data.meetingroomInfo
            );
            this.meetingroomCount = Object.keys(this.meetingroomInfo).length;
          }
        });
      });
    } catch (err) {
      throw new Error(err);
    }
  },
  computed: {
    gridTemplateRows: function () {
      return "repeat(" + Math.ceil(this.meetingroomCount) + ",250px)";
    },
    // updateMeetingroomInfo:function(){
    //     return this.meetingroomInfo;
    // }
  },
  methods: {
    // 主菜单栏选中
    onNavSelected: function (e) {
      if (e.currentTarget.className && e.currentTarget.className != "sidebar_nav") {
        this.$router.push(this.nav[e.currentTarget.class]);
      }
    },
    // 日期栏选中
    onDateSelected: function () {
      this.$api
        .requestMeetingRoomInfo({ params: { currentDate: this.date } })
        .then((res) => {
          this.meetingroomInfo = this.meetingRoomInfoProcess(res.data.meetingroomInfo);
          this.onOptionSelected();
        });
    },
    // 排序栏选中
    onOptionSelected: function () {
      if (this.sort == this.options.scale.value) {
        this.sortObj(this.meetingroomInfo, "d", "scale");
      } else if (this.sort == this.options.slot.value) {
        // console.log(this.meetingroomInfo);
        this.sortObj(this.meetingroomInfo, "d", "status");
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
    searchRoom: function () {
      this.search = true;
      // this.gridTemplateRows = "50px 60vh"
      // this.gridTemplateAreas = "'sidebar nav''sidebar result'"
      for (var roomId in this.meetingroomInfo) {
        var pattern = new RegExp(".*" + this.enteredRoom + ".*", "i");
        if (pattern.test(this.meetingroomInfo[roomId].roomname)) {
          this.searchResult[roomId] = this.meetingroomInfo[roomId];
          this.isFound = true;
        }
      }
    },
    meetingRoomInfoProcess: function (data) {
      var info = data;
      for (var item in info) {
        info[item].roomname =
          info[item].roomname.charAt(0).toUpperCase() + info[item].roomname.substring(1);
        if (info[item].status > 1) {
          if (info[item].status == 24) {
            info[item].status = "avaliable";
          } else {
            info[item].status = info[item].status + " slots free";
          }
        } else if (info[item].status == 1) {
          info[item].status = info[item].status + " slot booked free";
        } else {
          info[item].status = "no slots free";
        }
      }
      return info;
    },
    sortObj(obj, option, key) {
      var len = Object.keys(obj).length;

      // 升序排，从小到大
      if (option == "a") {
        for (let i = 0; i < len - 1; i++) {
          for (let j = 0; j < len - i - 1; j++) {
            let temp = obj[Object.keys(obj)[j]];
            // console.log(Object.keys(obj)[j]);
            if (key == "scale") {
              if (
                obj[Object.keys(obj)[j]][key].substring(1) >
                obj[Object.keys(obj)[j + 1]][key].substring(1)
              ) {
                obj[Object.keys(obj)[j]] = obj[Object.keys(obj)[j + 1]];
                obj[Object.keys(obj)[j + 1]] = temp;
              }
            } else if (key == "status") {
              let current = this.transformKeyValue(obj[Object.keys(obj)[j]][key]);
              let next = this.transformKeyValue(obj[Object.keys(obj)[j + 1]][key]);

              if (current > next) {
                obj[Object.keys(obj)[j]] = obj[Object.keys(obj)[j + 1]];
                obj[Object.keys(obj)[j + 1]] = temp;
              }
            }
          }
        }
        // 降序排，从大到小
      } else if (option == "d") {
        for (let i = 0; i < len - 1; i++) {
          for (let j = 0; j < len - i - 1; j++) {
            let temp = obj[Object.keys(obj)[j]];
            if (key == "scale") {
              if (
                obj[Object.keys(obj)[j]][key].substring(1) <
                obj[Object.keys(obj)[j + 1]][key].substring(1)
              ) {
                obj[Object.keys(obj)[j]] = obj[Object.keys(obj)[j + 1]];
                obj[Object.keys(obj)[j + 1]] = temp;
              }
            } else if (key == "status") {
              let current = this.transformKeyValue(obj[Object.keys(obj)[j]][key]);
              let next = this.transformKeyValue(obj[Object.keys(obj)[j + 1]][key]);
              // console.log(obj)
              if (current < next) {
                obj[Object.keys(obj)[j]] = obj[Object.keys(obj)[j + 1]];
                obj[Object.keys(obj)[j + 1]] = temp;
              }
            }
          }
        }
      }
    },
    transformKeyValue: function (val) {
      // console.log(val);
      var re;
      if (val.startsWith("no")) {
        re = 0;
      } else if (val.startsWith("avaliable")) {
        re = 24;
      } else {
        re = parseInt(val.match(/\d+/g)[0]);
      }
      return re;
    },
    toDayDetail: function (e) {
      // 区分query和params
      //   console.log(e);
      //currentTarget是指与事件绑定的元素，target指的是当前元素
      console.log(e);
      this.$router.push({
        path: "/meeting/offline/room_day_detail",
        query: { roomId: e.currentTarget.dataset.key, date: this.date },
      });
    },
  },
};
</script>
<style lang="less" scoped></style>
