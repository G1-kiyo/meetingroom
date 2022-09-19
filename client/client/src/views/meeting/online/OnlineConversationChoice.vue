<template>
  <div class="conversation_choice">
    <!--可以设置mode、默认index、跳转路由，特定栏目执行其他功能  -->
    <navigator mode="horizontal" default-index="2">
      <!--vue2的slot插槽属性为slot和slot-scope;vue3的slot插槽改为v-slot:插槽名=来自子组件的props数据  -->
      <template v-slot:navigator-item="{ currentIndex }">
        <navigator-item
          v-for="(item, index) in fixedNavInfo"
          :key="item.label"
          :path="item.path"
          :class="{ '--active': index == currentIndex }"
        >
          <!--插槽与v-if搭配不生效  -->
          <!-- <template v-if="item.hasIcon" v-slot:navigator-item-img>
            <div>
              <i :class="item.iconfont"></i>
            </div>
          </template> -->
          <template v-slot:navigator-item-text>
            <div class="text">{{ item.label }}</div>
          </template>
        </navigator-item>
      </template>
    </navigator>
    <navigator class="online_nav" mode="vertical" default-index="0">
      <template v-slot:navigator-item="{ currentIndex }"
        ><navigator-item
          v-for="(item, index) in onlineNavInfo"
          :key="item.label"
          :path="item.path"
          :func="item.func"
          :class="{ '--active': index == currentIndex }"
        >
          <template v-slot:navigator-item-img>
            <div><i :class="item.iconfont" :data-type="index <= 2 ? 1 : 2"></i></div>
          </template>
          <template v-slot:navigator-item-text
            ><div class="text">{{ item.label }}</div>
          </template>
        </navigator-item></template
      >
    </navigator>
    <!-- 主要容器 -->
    <main class="container">
      <!-- 选择栏 -->
      <div class="choice--outer">
        <div
          class="choice"
          @mouseover="checkIsOver($event)"
          @mouseout="checkIsLeaving($event)"
        >
          <!--视频会议-->
          <router-link to="/meeting/online/video_pattern"
            ><div class="choice_video">
              <div>
                <el-icon ref="video"><VideoCamera /></el-icon>
              </div>
              <span>Video Meeting</span>
            </div></router-link
          >
          <!-- 参与会议 -->
          <div class="choice_join">
            <div>
              <el-icon ref="join"><Plus /></el-icon>
            </div>
            <span>Join Meeting</span>
          </div>
          <!-- 语音会议 -->
          <div class="choice_voice">
            <div>
              <el-icon ref="voice"><Microphone /></el-icon>
            </div>
            <span>Voice Chat</span>
          </div>
          <!-- 文字聊天 -->
          <div class="choice_text">
            <div>
              <el-icon ref="text"><ChatDotRound /></el-icon>
            </div>
            <span>Text Chat</span>
          </div>
        </div>
      </div>

      <!-- 日程表 -->
      <div class="meeting_schedule--outer">
        <div class="meeting_schedule">
          <!--当前时间  -->
          <div class="schedule_time">
            <span>{{ datetime.time }}</span>
            <span>{{ datetime.date }}</span>
          </div>
          <!-- 会议预约列表 -->
          <ul class="schedule_list">
            <li
              class="schedule_list_item"
              v-for="item in upcomingOnlineMeeting"
              :key="item.meetingId"
            >
              <div>
                <span>{{ item.topic }}</span>
                <div>
                  <button type="button" class="button more">
                    <el-icon><MoreFilled /></el-icon>
                  </button>
                  <button type="button" class="button start">Start</button>
                </div>
              </div>
              <!-- 附加标签 -->
              <div>
                <span>{{ item.meetingType }}</span>
                <span>{{ item.isInvited ? "invited" : "launch" }}</span>
              </div>
              <!-- 会议预约时间段 -->
              <span class="timeperiod">{{
                `${checkTimePeriod(new Date(item.scheduleStartTime))} - ${checkTimePeriod(
                  new Date(item.scheduleEndTime)
                )}`
              }}</span>
              <span class="meetingid">Meeting Id: {{ item.meetingId }}</span>
            </li>
          </ul>
          <!-- 点击可查看记录 -->
          <div class="schedule_upcomingInfo">
            {{ `View today's upcoming meetings(${upcomingOnlineMeeting.length})` }}
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import OnlineNavigator from "@/components/OnlineNavigator.vue";
import Navigator from "@/components/navigator/Navigator.vue";
import NavigatorItem from "@/components/navigator/NavigatorItem.vue";
import { ElMessage } from "element-plus";
import store from "@/store.js";
export default {
  name: "OnlineConversationChoice",
  components: {
    "online-navigator": OnlineNavigator,
    navigator: Navigator,
  },
  data() {
    return {
      fixedNavInfo: {
        overview: {
          label: "Overview",
          hasIcon: false,
          path: "/meeting/offline/room_info_query",
        },
        profile: {
          label: "Profile",
          hasIcon: false,
          path: "/user/user_profile",
        },
        online: {
          label: "Online Meeting",
          hasIcon: false,
          path: "/meeting/online/conversation_choice",
        },
        book: {
          label: "Book Meeting-Room",
          hasIcon: false,
          path: "/meeting/offline/room_info_query",
        },
        file: {
          label: "File",
          hasIcon: false,
          path: "/meeting/online/online_meeting_schedule",
        },
      },
      onlineNavInfo: {
        home: {
          label: "Home",
          path: "/meeting/online/conversation_choice",
          iconfont: "iconfont icon-home-fill",
          func: false,
        },
        group: {
          label: "Group",
          path: "/meeting/online/group_query",
          iconfont: "iconfont icon-qunzu",
          func: false,
        },
        history: {
          label: "History",
          path: false,
          iconfont: "iconfont icon-history",
          func: false,
        },
        record: {
          label: "Record",
          path: false,
          iconfont: "iconfont icon-record-circle-fill",
          func: false,
        },
        exit: {
          label: "Exit",
          path: false,
          iconfont: "iconfont icon-exit",
          func: false,
        },
      },
      pastDate: null,
      datetime: null,
      upcomingOnlineMeeting: [],
      wsURL: "ws://localhost:8080/ws/upcoming/" + store.state.userId,
      ws: null,
    };
  },
  //data在beforecreated和created之间加载，props也一样
  computed: {},
  created() {
    const self = this;
    this.ws = new WebSocket(this.wsURL);
    this.datetime = this.displayDatetime();
    this.pastDate = this.datetime.date;
    this.initWebsocket();
    setInterval(function () {
      self.updateDatetime();
    }, 60 * 1000);
  },
  methods: {
    transportToNewPage: function (path) {
      this.$router.push({ path: path });
    },
    initWebsocket: function () {
      if (typeof WebSocket === "undefined") {
        ElMessage({
          type: "warning",
          message: "Your browser does not support websocket.",
        });
      } else {
        this.ws.onopen = this.wsOnOpen;
        this.ws.onmessage = this.wsOnMessage;
        this.ws.onerror = this.wsOnError;
        this.ws.onclose = this.wsOnClose;
      }
    },
    wsOnOpen: function () {},
    wsOnError: function () {
      //   this.initWebsocket();
    },
    wsOnMessage: function (msg) {
      if (msg != undefined) {
        var jsonMsg = JSON.parse(msg.data);
        var data = jsonMsg.data;

        //object与map区别在于，object的key值只能是字符串或symbol，map的key可以为任意值，weakmap的key只能是对象
        //object的key是无序的，但map是有序的
        //object还包括从原型继承的属性，map则是干净的
        //object键值个数手动获取，map通过size可获得
        //从object转变为map可以是用new Map(Object.entries(obj))
        //从map转变为object可以是用Object.fromEntries(map.entries())
        for (var key of Object.keys(data)) {
          this.upcomingOnlineMeeting.push(data[key]);
        }
      }
    },
    wsOnClose: function () {
      ElMessage.error("Disconnect.");
    },
    updateDatetime: function () {
      console.log("update");
      this.datetime = this.displayDatetime();
      if (this.pastDate !== this.datetime.date) {
        //发现日期改变，发出更新请求
        this.ws.send("update");
        this.pastDate = this.datetime.date;
      }
    },
    checkIsHover: function (e) {
      if (e.event.currentTarget.className) {
        var refName = e.event.currentTarget.className.split("_")[1];
        this.$refs.refName.style.color = "#264DE8";
      }
    },
    checkIsLeaving: function (e) {
      if (e.event.currentTarget.className) {
        var refName = e.event.currentTarget.className.split("_")[1];
        this.$refs.refName.style.color = "#666666";
      }
    },
    checkTimePeriod: function (datetime) {
      var year = datetime.getFullYear();
      var month = datetime.getMonth();
      var date = datetime.getDate();
      var noon = new Date(year, month, date, 12, 0, 0);
      var time;
      if (datetime < noon) {
        time = this.$dateProcess.dateFormat(datetime, "HH:mm AM");
      } else {
        time = this.$dateProcess.dateFormat(datetime, "HH:mm PM");
      }
      return time;
    },
    displayDatetime: function () {
      //   console.log(this.$dateProcess);
      var currentDate = new Date();
      var year = currentDate.getFullYear();
      var month = currentDate.getMonth();
      var date = currentDate.getDate();
      var weekday = this.$dateProcess.generateWeekday(currentDate.getDay(), "en");
      var time = this.checkTimePeriod(currentDate);
      var formatDate =
        weekday +
        "," +
        this.$dateProcess.translateMonth(month) +
        " " +
        this.$dateProcess.dateFormat(currentDate, "dd") +
        "," +
        year;
      return {
        time: time,
        date: formatDate,
      };
    },
    requestUpcomingOnlineMeeting: function () {
      this.$api
        .requestUpcomingOnlineMeeting({
          params: {
            date: this.$dateProcess.dateFormat(new Date(), "YYYY-MM-DD"),
            userId: store.state.userId,
          },
        })
        .then((res) => {
          this.upcomingOnlineMeeting = res.data;
        });
    },
  },
};
</script>
<style scoped></style>
