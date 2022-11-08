<template>
  <div class="bg">
    <navigator></navigator>
    <div class="mondetail">
      <!-- 操作栏 -->
      <div class="mondetail_operation">
        <!-- class名就是组件内最外层的元素的class名 -->
        <date-group
          class="mondetail_operation_dategroup"
          :dateGroup="dateGroup"
          :date="date"
          @updateDate="updateDate"
        ></date-group>
        <!-- 仅调整day -->
        <div class="mondetail_operation_daymodify">
          <el-icon @click="toFormerMonth" :size="30">
            <CaretLeft />
          </el-icon>
          <el-icon @click="toNextMonth" :size="30" :style="{ marginLeft: '15px' }">
            <CaretRight />
          </el-icon>
        </div>
        <!-- day/month切换 -->
        <custom-switch
          class="roomdetail_operation_switch"
          defaultCheck="true"
          defaultValue="DAY"
          theOtherValue="MON"
          @checkStateChange="changeToDay"
        ></custom-switch>
      </div>
      <!-- 日历 -->
      <table class="mondetail_main">
        <!--表头  -->
        <thead>
          <th v-for="(item, index) in weekday" :key="index">{{ item }}</th>
        </thead>
        <tbody>
          <!-- 按行列展示 -->
          <tr v-for="row in generateRows()" :key="row">
            <td
              v-for="col in 7"
              :key="col"
              :data-index="col + 7 * (row - 1)"
              @click="toDayDetail($event)"
            >
              <!-- 单元格内的信息 -->
              <calendar-item
                ref="calendar-item"
                :dataCell="col + 7 * (row - 1)"
                :monthMeetingInfo="monthMeetingInfo"
                :calendar="calendar"
              ></calendar-item>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script>
import Navigator from "@/components/Navigator.vue";
import DateGroup from "@/components/DateGroup.vue";
import CustomSwitch from "@/components/CustomSwitch.vue";
import CalendarItem from "@/components/CalendarItem.vue";
import debounce from "lodash/debounce";
export default {
  name: "RoomMonDetail",
  components: {
    navigator: Navigator,
    "date-group": DateGroup,
    "custom-switch": CustomSwitch,
    "calendar-item": CalendarItem,
  },
  created() {
    this.requestMonthMeetingInfo();
  },
  data() {
    return {
      dateGroup: {
        month: {
          label: "month",
          focus: false,
          enterVal: "",
        },
        year: {
          label: "year",
          focus: false,
          enterVal: "",
        },
      },
      roomId: this.$route.query.roomId,
      date: this.$dateProcess.dateFormat(this.$route.query.date, "YYYY-MM-DD"),
      weekday: ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"],
      monthMeetingInfo: {},
      rows: 0,
      // bgColor: "#228B22",
    };
  },
  computed: {
    calendar: function () {
      return this.generateCalendar(this.date);
    },
  },
  watch: {
    date: function (newVal, oldVal) {
      if (newVal != oldVal) {
        this.date = newVal;
        this.requestMonthMeetingInfo();
      }
    },
  },
  methods: {
    generateRows: function () {
      return Math.ceil((this.calendar.totalDay + this.calendar.firstDay) / 7);
    },
    toDayDetail: function (e) {
      if (
        e.target.dataset.index >= this.calendar.firstDay + 1 ||
        e.target.dataset.index <= this.calendar.totalDay
      ) {
        var date = this.$refs["calendar-item"][e.target.dataset.index - 1].formattedDate;
        this.$router.push({
          path: "/meeting/offline/room_day_detail",
          query: { roomId: this.roomId, date: date },
        });
      }
    },
    updateDate: function (val) {
      this.date = val;
    },
    generateCalendar: function (date) {
      var localDate = new Date(date).getDate();
      var month = new Date(date).getMonth();
      var year = new Date(this.date).getFullYear();
      //当要求当月的天数，此时月份参数即为你要求的月份的数字
      // 但如果不是，即第三个参数并不为0，那么月份参数需为你要求的月份的数字-1
      var totalDay = new Date(year, month + 1, 0).getDate();
      var firstDay = new Date(year, month, 1).getDay();
      // console.log(firstDay);
      var lastDay = new Date(year, month, totalDay).getDay();
      if (month == 0) {
        var lastMonthTotalDay = new Date(year - 1, 12, 0).getDate();
        var nextMonthTotalDay = new Date(year, month + 2, 0).getDate();
      } else if (month == 11) {
        lastMonthTotalDay = new Date(year, month, 0).getDate();
        nextMonthTotalDay = new Date(year + 1, 1, 0).getDate();
      } else {
        lastMonthTotalDay = new Date(year, month, 0).getDate();
        // console.log(month);
        nextMonthTotalDay = new Date(year, month + 2, 0).getDate();
      }
      return {
        date: localDate,
        month: month,
        year: year,
        totalDay: totalDay,
        firstDay: firstDay,
        lastDay: lastDay,
        lastMonthTotalDay: lastMonthTotalDay,
        nextMonthTotalDay: nextMonthTotalDay,
      };
    },
    requestMonthMeetingInfo: debounce(function () {
      this.$api
        .requestMonthMeetingInfo({
          params: { roomId: this.roomId, date: this.date },
        })
        .then((res) => {
          this.monthMeetingInfo = res.data;
        });
    }, 500),
    toFormerMonth: function () {
      if (this.calendar.month == 0) {
        var lastYear = this.calendar.year - 1;
        var lastDate =
          this.calendar.date > this.calendar.lastMonthTotalDay
            ? this.calendar.lastMonthTotalDay
            : this.calendar.date;
        this.date = this.$dateProcess.dateFormat(
          new Date(lastYear, 11, lastDate),
          "YYYY-MM-DD"
        );
      } else {
        this.date = this.$dateProcess.dateFormat(
          new Date(this.calendar.year, this.calendar.month - 1, this.calendar.date),
          "YYYY-MM-DD"
        );
      }
    },
    toNextMonth: function () {
      if (this.calendar.month == 11) {
        var nextYear = this.calendar.year + 1;
        var nextDate =
          this.calendar.date > this.calendar.nextMonthTotalDay
            ? this.calendar.nextMonthTotalDay
            : this.calendar.date;
        this.date = this.$dateProcess.dateFormat(
          new Date(nextYear, 0, nextDate),
          "YYYY-MM-DD"
        );
      } else {
        this.date = this.$dateProcess.dateFormat(
          new Date(this.calendar.year, this.calendar.month + 1, this.calendar.date),
          "YYYY-MM-DD"
        );
      }
    },
    changeToDay: function () {
      this.$router.push({
        path: "/meeting/offline/room_day_detail",
        query: { roomId: this.roomId, date: this.date },
      });
    },
  },
};
</script>
<style lang="less" scoped>
@import "@/assets/css/var.less";
.bg {
  background-color: @color_bg_green;
  height: 100%;
  padding: 0 23px;
}
</style>
