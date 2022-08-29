<template>
  <span
    :class="'referrence_line ' + type"
    :ref="'referrenceLine' + type"
    :data-time="type + ' ' + time"
  ></span>
</template>

<script>
export default {
  name: "ReferrenceLine",
  props: ["config", "minTime", "maxTime", "type"],
  emits: ["outputTime"],
  data() {
    return {
      lineTop: 0,
      lastlineTop: 0,
      time: "09:00",
    };
  },
  created() {
    this.$nextTick(() => {
      this.lineTop = this.$refs["referrenceLine" + this.type].style.top;
      this.lastlineTop = this.$refs["referrenceLine" + this.type].style.top;
      this.dragEvent();
    });
  },
  computed: {},
  methods: {
    dragEvent: function () {
      //禁止选中文字
      document.addEventListener("selectstart", function (e) {
        e.preventDefault();
      });
      var that = this;
      var line = this.$refs["referrenceLine" + this.type];
      line.onmousedown = function (e) {
        var y = e.clientY;
        var top = parseInt(getComputedStyle(line).top);
        that.lastlineTop = top;
        document.onmousemove = function (e) {
          var dis = e.clientY - y;
          var newTop = top + dis;
          if (newTop < 0) {
            newTop = 0;
          } else if (newTop > that.config.total) {
            newTop = that.config.total;
          }

          that.lineTop = newTop;
          //如果是纯数字赋值undefined
          //直接读取该style.top为数字+px，这与getcomputedstyle是一致的
          //而getBoundingClientRect则是纯数字
          line.style.top = that.lineTop + "px";
          document.onmouseup = function () {
            that.getTime();
            that.testTime();
            document.onmousemove = null;
          };
        };
      };
    },
    getTime: function () {
      // 不会取整
      var index = this.lineTop / this.config.timeBoxHeight;
      index = Math.floor(index);
      this.time = this.$dateProcess.dateFormat(
        this.$dateProcess.datePlus("2022-08-04 09:00", index * 30 * 60 * 1000),
        "HH:mm"
      );
    },
    testTime: function () {
      console.log(this.lastlineTop, this.lineTop);
      if (this.minTime) {
        if (this.time < this.minTime) {
          this.$refs["referrenceLine" + this.type].style.top = this.lastlineTop + "px";
          this.time = this.minTime;
        } else {
          this.$refs["referrenceLine" + this.type].style.top = this.lineTop + "px";
        }
      } else if (this.maxTime) {
        if (this.time > this.maxTime) {
          this.$refs["referrenceLine" + this.type].style.top = this.lastlineTop + "px";
          this.time = this.maxTime;
        } else {
          this.$refs["referrenceLine" + this.type].style.top = this.lineTop + "px";
        }
      }
      this.$emit("outputTime", this.time);
    },
  },
};
</script>
<style lang="less" scoped>
@import "@/assets/css/var.less";
.referrence_line {
  display: block;
  height: 0;
  width: 85%;
  position: absolute;
  left: 30px;
  // top: 0;
  &::before {
    content: "";
    display: block;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    border: none;
    position: absolute;
    left: -7.5px;
    top: -5.5px;
  }
  &::after {
    content: attr(data-time);
    position: absolute;
    right: 10px;
    top: -12px;
    font-size: 12px;
  }
  &.start {
    top: 0px;
    border: 1px solid @color_blue;
    &::before {
      background-color: @color_blue;
    }
    &::after {
      color: @color_blue;
    }
  }
  &.end {
    top: 20px;
    border: 1px solid @color_black;
    &::before {
      background-color: @color_black;
    }
    &::after {
      color: @color_black;
    }
  }
}
</style>
