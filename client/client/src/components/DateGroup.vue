<template>
  <!-- 日期操作栏 -->
  <div class="date">
    <div
      class="date_item"
      v-for="item in localDateGroup"
      :key="item.label"
      @click="item.focus = true"
      @mouseleave="inputHidden(item.label)"
    >
      <!-- mouseenter/mouseleave没有冒泡，但mouseover/mouseout有 -->
      <div class="user_input" v-if="item.focus">
        <span class="label1">{{ item.label }}</span>
        <el-input
          class="date_el_input"
          v-model="item.enterVal"
          @blur="validateDate(item.label, item.enterVal)"
        >
          ></el-input
        >
        <!-- <span class="label2" v-show="item.label == 'day'">,</span> -->
      </div>
      <span class="label2" v-else>{{ defaultDate[item.label] }}</span>
      <span class="label3" v-show="item.label == 'day'">,</span>
    </div>
  </div>
</template>
<script>
import { ElMessage } from "element-plus";
export default {
  name: "DateGroup",
  props: ["dateGroup", "date"],
  emits: ["updateDate"],
  data() {
    return {
      localDateGroup: this.dateGroup,
      localDate: this.date,
    };
  },
  computed: {
    defaultDate: function () {
      return {
        month: this.translateMonth(new Date(this.localDate).getMonth()),
        day: new Date(this.localDate).getDate(),
        year: new Date(this.localDate).getFullYear(),
      };
    },
  },
  watch: {
    date: function (newVal, oldVal) {
      if (newVal != oldVal) {
        this.localDate = newVal;
      }
    },
  },
  methods: {
    translateMonth(month) {
      var monthList = [
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec",
      ];
      return monthList[month];
    },
    inputHidden: function (dateType) {
      const that = this;
      setTimeout(function () {
        //匿名函数的this指向window
        that.localDateGroup[dateType].focus = false;
      }, 500);
    },
    validateDate: function (dateType, val) {
      var newVal = parseInt(val);
      if (/[^\d+]/.test(newVal)) {
        ElMessage({
          type: "warning",
          message: "Any number is allowed.",
        });
      } else {
        if (dateType == "month") {
          if (newVal < 1 || newVal > 12) {
            ElMessage({
              type: "warning",
              message: "Illegal month input",
            });
          } else {
            this.localDate = this.$dateProcess.dateFormat(
              this.localDate.replace(/(?<=-)\d+(?=-.*)/, newVal),
              "YYYY-MM-DD"
            );
          }
        } else if (dateType == "day") {
          var year = this.localDate.split("-")[0];
          var month = this.localDate.split("-")[1];
          // console.log(new Date(year, month, 0).getDate());
          if (newVal < 1 || newVal > new Date(year, month, 0).getDate()) {
            ElMessage({
              type: "warning",
              message: "Illegal day input",
            });
          } else {
            this.localDate = this.$dateProcess.dateFormat(
              this.localDate.replace(/(?<=-)\d+$/, newVal),
              "YYYY-MM-DD"
            );
          }
        } else if (dateType == "year") {
          if (newVal < 2019 || newVal > new Date().getFullYear) {
            ElMessage({
              type: "warning",
              message: "Illegal year input",
            });
          } else {
            this.localDate = this.$dateProcess.dateFormat(
              this.localDate.replace(/^(?!-)\d+(?=-.*)/, newVal),
              "YYYY-MM-DD"
            );
          }
        }
      }
      // this.dateGroup[dateType].focus = false;
      this.$emit("updateDate", this.localDate);
    },
  },
};
</script>
<style lang="less" scoped>
// 日期栏
.date {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;

  // flex:4;
  &_item {
    display: inline-block;
    margin-right: 10px;

    .label2,
    .label3 {
      font-size: 32px;
      line-height: 40px;
    }

    .user_input {
      // margin-left: 10px;
      margin-top: -15px;
      display: inline-block;
      .label1 {
        display: block;
        font-size: 14px;
      }
    }
  }
}
</style>
<style>
.el-input .date_el_input {
  width: 20px;
  /* height: 25px; */
  /* display: inline-block; */
}

.date_el_input .el-input__inner {
  width: 20px;
  height: 25px;
  /* display: inline-block; */
}
</style>
