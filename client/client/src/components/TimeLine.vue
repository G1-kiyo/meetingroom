<template>
  <ul ref="box">
    <li
      :style="{ height: item.meta.height }"
      class="timeline"
      :class="{ active: item.meta.isBooked }"
      v-for="(item, index) in timeList"
      :key="index"
    >
      <span></span>
      <div class="timeline_content" v-if="item.meta.isBooked">
        <div class="timeline_content_title">{{ item.meta.dayMeetingInfo.title }}</div>
        <div
          class="timeline_content_description"
          :style="{
            height:
              item.meta.dayMeetingInfo.timeSpan == 1
                ? '60px'
                : parseInt(item.meta.height) * 0.6 + 'px',
          }"
        >
          {{ item.meta.dayMeetingInfo.description }}
        </div>
        <div class="timeline_content_username">
          {{ item.meta.dayMeetingInfo.username }}
        </div>
      </div>
      <div v-if="item.meta.isBooked">
        <div class="time">
          <!-- 从1开始 -->

          <span
            v-for="n in item.meta.dayMeetingInfo.timeSpan"
            :key="n"
            class="time_label"
            :style="{
              top: timeBoxHeight * (n - 1) + 'px',
            }"
            >{{
              $dateProcess.dateFormat(
                $dateProcess.datePlus(
                  date + " " + item.startTime,
                  (n - 1) * timeStep * 60 * 1000
                ),
                "HH:mm"
              )
            }}</span
          >
        </div>
      </div>
      <div v-else>
        <span class="time_label" :style="{ top: 0 }">{{ item.startTime }}</span>
      </div>
    </li>
  </ul>
</template>

<script>
export default {
  name: "TimeLine",
  props: ["startTime", "endTime", "timeStep", "dayMeetingInfo"],
  emits: ["callConfig"],
  data() {
    return {
      timeList: [],
      newdayMeetingInfo: [],
      date: this.$dateProcess.dateFormat(new Date(), "YYYY-MM-DD"),
      totalTimeCount: 0,
      timeBoxHeight: 120,
      box: null,
    };
  },
  //DOM树还未渲染完成，不能直接调用this.$refs
  created() {
    this.generateTimeList();
  },
  mounted() {
    this.$emit("callConfig", {
      timeBoxHeight: this.timeBoxHeight,
      total: this.$refs.box.getBoundingClientRect().height,
    });
  },
  watch: {
    dayMeetingInfo: function (newVal, oldVal) {
      if (newVal != oldVal) {
        this.generateTimeList();
      }
    },
  },
  methods: {
    generateTimeList: function () {
      // 不考虑用深拷贝，需要实时更新
      this.newdayMeetingInfo = this.dayMeetingInfo;
      this.timeList = [];
      // var date = this.$dateProcess.dateFormat(new Date(), "YYYY-MM-DD");
      var newStartTime = this.date + " " + this.startTime;
      var newEndTime = this.date + " " + this.endTime;
      this.totalTimeCount =
        this.$dateProcess.dateDiff(newStartTime, newEndTime, "minute") / this.timeStep;
      // console.log(this.totalTimeCount);
      // console.log(this.$dateProcess.dateDiff(this.startTime, this.endTime, "minute"));
      for (let i = 0; i <= this.totalTimeCount; i++) {
        var time = {
          startTime: this.$dateProcess.dateFormat(
            this.$dateProcess.datePlus(newStartTime, i * this.timeStep * 60 * 1000),
            "HH:mm"
          ),
          endTime: this.$dateProcess.dateFormat(
            this.$dateProcess.datePlus(newStartTime, (i + 1) * this.timeStep * 60 * 1000),
            "HH:mm"
          ),
          meta: {
            height: this.timeBoxHeight,
            isBooked: false,
            dayMeetingInfo: [],
          },
        };
        this.timeList.push(time);
      }
      this.countMeetingTimeSpan();
      this.testMeetingTime(newStartTime);
      this.combineTimePeriod();
      // this.completeTimeList();
      // console.log(this.timeList);
    },
    countMeetingTimeSpan: function () {
      // 遍历的是key
      var totalTimeSpan = 0;
      //确认是否有会议预约记录
      if (this.newdayMeetingInfo[0].roomId != null) {
        for (var item of this.newdayMeetingInfo) {
          var span =
            this.$dateProcess.dateDiff(item.startTime, item.endTime, "minute") /
            this.timeStep;
          item["timeSpan"] = span;
          totalTimeSpan = totalTimeSpan + (span > 1 ? span - 1 : 0);
        }
      }
      return totalTimeSpan;
    },
    testMeetingTime: function (startTime) {
      if (this.newdayMeetingInfo[0].roomId != null) {
        for (var item of this.newdayMeetingInfo) {
          var index =
            this.$dateProcess.dateDiff(
              this.date + " " + item.startTime.split(" ")[1],
              startTime,
              "minute"
            ) / this.timeStep;

          this.timeList[index].meta = {
            isBooked: true,
            dayMeetingInfo: item,
          };
        }
      }
    },
    combineTimePeriod: function () {
      for (let i = 0; i < this.timeList.length; i++) {
        if (
          this.timeList[i].meta.isBooked == true &&
          this.timeList[i].meta.dayMeetingInfo.timeSpan > 1
        ) {
          var timespan = this.timeList[i].meta.dayMeetingInfo.timeSpan;
          this.timeList[i].endTime = this.$dateProcess.dateFormat(
            this.$dateProcess.datePlus(
              this.date + " " + this.timeList[i].startTime,
              timespan * this.timeStep
            ),
            "HH:mm"
          );
          this.timeList[i].meta.height = timespan * this.timeBoxHeight + "px";
          //如果是删除，返回的是被删除的哪个元素
          this.timeList.splice(i + 1, timespan - 1);
        }
      }
    },
    completeTimeList: function () {
      var last = this.timeList.length;
      for (let i = 0; i < 24 - last; i++) {
        var time = {
          startTime: this.$dateProcess.dateFormat(
            this.$dateProcess.datePlus(
              this.date + " " + this.timeList[last - 1].startTime,
              (i + 1) * this.timeStep * 60 * 1000
            ),
            "HH:mm"
          ),
          endTime: this.$dateProcess.dateFormat(
            this.$dateProcess.datePlus(
              this.date + " " + this.timeList[last - 1].startTime,
              (i + 2) * this.timeStep * 60 * 1000
            ),
            "HH:mm"
          ),
          meta: {
            height: this.timeBoxHeight,
            isBooked: false,
            dayMeetingInfo: [],
          },
        };
        this.timeList.push(time);
      }
    },
  },
};
</script>
<style lang="less" scoped>
@import "@/assets/css/var.less";
ul {
  // grid-area: info;
  list-style-type: none;
  margin: 0;
  padding: 0;
  // 父相子绝
  position: relative;
  top: 0;
  &::before {
    //连续完整的时间轴
    //若只是在li中的span设置，并不能连续
    content: "";
    display: block;
    width: 0;
    height: 100%;
    border: 1px solid #bcdbc5;
    position: absolute;
    top: 0;
    left: 45px;
  }
  .timeline {
    // 表示3个值：上 左=右 下
    // 表示2个值：上=下 左=右
    margin: 0px 60px 0px;
    position: relative;
    height: 120px;
    padding: 0px 20px;
    border-radius: 3px;
    width: 85%;
    // & > span {
    //   content: "";
    //   display: block;
    //   width: 0;
    //   height: 100%;
    //   border: 1px solid #bcdbc5;
    //   position: absolute;
    //   top: 0;
    //   left: -30px;
    // }
    &.active {
      background-color: #64cb53;
      color: @color_white;
      line-height: 20px;
    }
    &_content {
      padding-top: 10px;
      &_title {
        text-transform: uppercase;
        font-weight: 700;
        margin-bottom: 5px;
      }
      &_description {
        height: 60px;
        overflow-y: scroll;
        &::first-letter {
          text-transform: capitalize;
          line-height: 1.7;
        }
        &::-webkit-scrollbar {
          width: 3px;
          height: 20px;
        }
      }
      &_username {
        margin-top: 5px;
        text-transform: capitalize;
        font-style: italic;
        text-align: right;
        margin-right: 20px;
      }
    }

    .time_label {
      position: absolute;
      left: -60px;
      color: @color_black;
      font-size: 14px;
      //   font-weight: bold;
    }
  }
}
</style>
