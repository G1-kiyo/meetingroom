<template>
  <div class="room">
    <div class="roomdetail">
      <navigator></navigator>
      <main class="roomdetail_main">
        <!-- 操作栏 -->
        <div class="roomdetail_operation">
          <!-- 日期操作栏 -->
          <date-group
            class="roomdetail_operation_dategroup"
            :dateGroup="dateGroup"
            :date="date"
            @updateDate="updateDate"
          ></date-group>
          <!-- 仅调整day -->
          <div class="roomdetail_operation_daymodify">
            <el-icon @click="toFormerDay" :size="30">
              <CaretLeft />
            </el-icon>
            <el-icon @click="toNextDay" :size="30" :style="{ marginLeft: '15px' }">
              <CaretRight />
            </el-icon>
          </div>
          <!-- day/month切换 -->
          <day-month-switch
            class="roomdetail_operation_switch"
            :roomId="roomId"
            :date="date"
          ></day-month-switch>
        </div>
        <el-divider></el-divider>
        <!-- 会议室预约展示 -->
        <div class="roomdetail_info">
          <!-- 时间轴 -->
          <timeline
            ref="timeline"
            @callConfig="generateConfig"
            :startTime="startTime"
            :endTime="endTime"
            :timeStep="timeStep"
            :dayMeetingInfo="dayMeetingInfo"
          >
          </timeline>
          <!-- 两条参考线 -->
          <referrenceline-start
            :config="config"
            :type="start"
            :maxTime="bookEndTime"
            @outputTime="updateStartTime"
          ></referrenceline-start>
          <referrenceline-end
            :config="config"
            :type="end"
            :minTime="bookStartTime"
            @outputTime="updateEndTime"
          ></referrenceline-end>
        </div>
      </main>
    </div>
    <!-- 预约栏 -->
    <div class="roombook">
      <roombook
        :roomId="roomId"
        :date="date"
        :userJobInfo="userJobInfo"
        :roomname="roomname"
        :bookStartTime="bookStartTime"
        :bookEndTime="bookEndTime"
        @handleMemberShow="showMember"
      ></roombook>
    </div>
    <!-- 添加成员 -->
    <div v-if="memberShow">
      <memberinfo
        :allMemberInfo="allMemberInfo"
        :infoGroupByDepartment="infoGroupByDepartment"
        @handleMemberClosed="memberShow = false"
      ></memberinfo>
    </div>
  </div>
</template>
<script>
import store from "@/store.js";
import TimeLine from "../../../components/TimeLine";
// import ReferrenceLine1 from "../../../components/ReferrenceLine1";
import DateGroup from "@/components/DateGroup.vue";
import DayMonthSwitch from "@/components/DayMonthSwitch.vue";
import ReferrenceLine from "@/components/ReferrenceLine.vue";
import RoomBook from "../../../components/RoomBook";
import MemberInfo from "../../../components/MemberInfo";
import _ from "lodash";
import Navigator from "@/components/Navigator.vue";
// import UserOperationControl from "@/utils/UserOperationControl";
export default {
  name: "RoomDayDetail",
  // props: ["roomId"],
  components: {
    timeline: TimeLine,
    "referrenceline-start": ReferrenceLine,
    "referrenceline-end": ReferrenceLine,
    roombook: RoomBook,
    memberinfo: MemberInfo,
    navigator: Navigator,
    "date-group": DateGroup,
    "day-month-switch": DayMonthSwitch,
  },
  created() {
    const userJobInfo = this.$api.requestUserJobInfo({
      params: { userId: store.state.userId },
    });
    const dayMeetingInfo = this.$api.requestDayMeetingInfo({
      params: { roomId: this.roomId, date: this.date },
    });
    Promise.all([userJobInfo, dayMeetingInfo]).then((responses) => {
      return responses.map((response) => {
        if (response.data.userJobInfo) {
          this.userJobInfo = response.data.userJobInfo;
          // this.userJobInfoProcess(this.userJobInfo);
        }
        if (response.data.dayMeetingInfo) {
          this.dayMeetingInfo = response.data.dayMeetingInfo;
          this.dayMeetingInfoProcess(this.dayMeetingInfo);
        }
      });
    });
  },
  data() {
    return {
      roomId: this.$route.query.roomId,
      date: this.$dateProcess.dateFormat(this.$route.query.date, "YYYY-MM-DD"),
      userJobInfo: {},
      dayMeetingInfo: [],
      config: {},
      dateFocus: false,
      dateGroup: {
        month: {
          label: "month",
          focus: false,
          enterVal: "",
        },
        day: {
          label: "day",
          focus: false,
          enterVal: "",
        },
        year: {
          label: "year",
          focus: false,
          enterVal: "",
        },
      },
      isDayActive: true,
      isMonthActive: false,
      start: "start",
      end: "end",
      startTime: "09:00",
      endTime: "21:00",
      bookStartTime: "09:00",
      bookEndTime: "09:30",
      timeStep: 30,
      searchMemberName: "",
      infoGroupByDepartment: {},
      allMemberInfo: [],
      // selected:false,
      memberShow: false,
    };
  },
  computed: {
    activeIndex: function () {
      return (
        "/meeting/offline/room_day_detail" +
        "?roomId=" +
        this.roomId +
        "&date=" +
        this.date
      );
    },
    currentIndex: function () {
      return (
        "/meeting/offline/room_day_detail" +
        "?roomId=" +
        this.roomId +
        "&date=" +
        this.date
      );
    },
    roomname: function () {
      return (
        this.dayMeetingInfo[0].troomname[0].toUpperCase() +
        this.dayMeetingInfo[0].troomname.substring(1)
      );
    },
  },
  watch: {
    date: function (newVal, oldVal) {
      //这里返回的是debounce函数内的匿名函数，接收自定义参数
      if (newVal != oldVal) {
        // var requestRoomInfoDe = this.$userOC.debounce(this.requestRoomInfo, 30 * 1000);
        this.requestRoomInfo(newVal);
      }
    },
  },
  provide() {
    return {};
  },
  methods: {
    // 获取子组件传过来的数据，并更新
    updateDate: function (val) {
      this.date = val;
    },
    generateConfig(val) {
      this.config = val;
    },

    // async与await搭配，async声明在function前，async隐式返回promise对象
    // 等执行到await，不阻塞，回去执行相应的方法代码
    // 如果await后的方法中直接return value，async function.then获取到的value就是上述语句return的value
    // 如果还要结合setTimeout，需要在await后的方法内return promise，然后利用resolve传值
    // async和await实际上是基于promise实现的，也是为了简化promise嵌套的then（链式调用）
    requestRoomInfo: _.debounce(function (newVal) {
      this.$api
        .requestDayMeetingInfo({
          params: {
            roomId: this.roomId,
            date: newVal,
          },
        })
        .then((res) => {
          this.dayMeetingInfo = res.data.dayMeetingInfo;
          this.dayMeetingInfoProcess(this.dayMeetingInfo);
        });
    }, 500),
    toFormerDay: function () {
      this.date = this.$dateProcess.dateFormat(
        this.$dateProcess.dateMinus(this.date, 24 * 60 * 60 * 1000),
        "YYYY-MM-DD"
      );
    },
    toNextDay: function () {
      this.date = this.$dateProcess.dateFormat(
        this.$dateProcess.datePlus(this.date, 24 * 60 * 60 * 1000),
        "YYYY-MM-DD"
      );
    },
    updateStartTime: function (time) {
      this.bookStartTime = time;
    },
    updateEndTime: function (time) {
      this.bookEndTime = time;
    },
    showMember: function () {
      this.memberShow = true;
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
    // 规范后台传输的会议信息
    dayMeetingInfoProcess: function (val) {
      for (var item of val) {
        if (item.roomId != null) {
          item.meetingDate = this.$dateProcess.dateFormat(item.meetingDate, "YYYY-MM-DD");
          item.startTime = this.$dateProcess.dateFormat(
            item.startTime,
            "YYYY-MM-DD HH:mm:ss"
          );
          item.endTime = this.$dateProcess.dateFormat(
            item.endTime,
            "YYYY-MM-DD HH:mm:ss"
          );
          item.createTime = this.$dateProcess.dateFormat(
            item.createTime,
            "YYYY-MM-DD HH:mm:ss"
          );
        }
      }
    },
  },
};
</script>
<style lang="less" scoped></style>
