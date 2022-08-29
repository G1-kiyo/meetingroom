<template>
  <!--遮罩层  -->
  <span class="main_cover" v-if="isCoverShow"></span>
  <!-- 日历数字 -->
  <span
    class="main_number"
    :style="{
      color: isCoverShow ? '#CCCCCC' : '#000000',
    }"
    >{{ localDate }}</span
  >
  <!-- 会议信息 -->
  <ul class="main_list">
    <li
      class="main_list_item"
      :style="generateColor()"
      v-for="item in meetingList"
      :key="item.meetingId"
    >
      {{ item.title }}
    </li>
  </ul>
</template>
<script>
export default {
  name: "CalendarItem",
  props: ["dataCell", "monthMeetingInfo", "calendar"],
  data() {
    return {
      isCoverShow: false,
      localDate: this.dataCell,
      formattedDate: "",
    };
  },
  computed: {
    meetingList: function () {
      return this.formattedDate in this.monthMeetingInfo
        ? this.monthMeetingInfo[this.formattedDate]
        : [];
    },
  },
  watch: {
    calendar: {
      handler(newVal, oldVal) {
        if (newVal != oldVal) {
          this.isDateExisted();
          this.showDate();
          this.generateFormattedDate();
        }
      },
      deep: true,
    },
  },
  created() {
    this.isDateExisted();
    this.showDate();
    this.generateFormattedDate();
  },
  methods: {
    generateColor: function () {
      var r = Math.floor(Math.random() * 255);
      var g = Math.floor(Math.random() * 255);
      var b = Math.floor(Math.random() * 255);
      var standard = 0.299 * r + 0.587 * g + 0.114 * b;
      var fncolor = "black";
      if (standard < 150) fncolor = "white";
      var bgcolor = "rgb(" + r + "," + g + "," + b + ")";
      return { backgroundColor: bgcolor, color: fncolor };
    },
    generateFormattedDate: function () {
      this.formattedDate = this.$dateProcess.dateFormat(
        new Date(this.calendar.year, this.calendar.month, this.localDate),
        "YYYY-MM-DD"
      );
    },

    isDateExisted: function () {
      if (
        this.dataCell < this.calendar.firstDay + 1 ||
        this.dataCell - this.calendar.firstDay > this.calendar.totalDay
      ) {
        this.isCoverShow = true;
      } else {
        this.isCoverShow = false;
      }
    },
    showDate: function () {
      // console.log(this.calendar.month);
      var showDate = this.dataCell;
      if (this.calendar.firstDay == 0) {
        if (this.dataCell > this.calendar.totalDay) {
          showDate = this.dataCell - this.calendar.totalDay;
        }
      } else {
        if (this.dataCell < this.calendar.firstDay + 1) {
          showDate =
            this.calendar.lastMonthTotalDay + (this.dataCell - this.calendar.firstDay);
        } else if (this.dataCell - this.calendar.firstDay > this.calendar.totalDay) {
          showDate = this.dataCell - this.calendar.firstDay - this.calendar.totalDay;
        } else {
          showDate = this.dataCell - this.calendar.firstDay;
        }
      }
      this.localDate = showDate;
    },
  },
};
</script>
