<template>
  <!-- day/month切换 -->
  <div class="switch">
    <div class="switch_box">
      <input
        class="switch_checkbox"
        type="checkbox"
        :checked="defaultCheck"
        @click="isDayOrMon($event)"
      />
      <div class="switch_text"></div>
      <div class="switch_layer"></div>
    </div>
  </div>
</template>
<script>
export default {
  name: "DayMonthSwitch",
  props: ["roomId", "date", "defaultCheck"],
  methods: {
    isDayOrMon: function (e) {
      if (e.target.checked == true) {
        this.$router.push({
          path: "/meeting/offline/room_mon_detail",
          query: { roomId: this.roomId, date: this.date },
        });
      } else {
        this.$router.push({
          path: "/meeting/offline/room_day_detail",
          query: { roomId: this.roomId, date: this.date },
        });
      }
    },
  },
};
</script>
<style lang="less" scoped>
@import "@/assets/css/var.less";
// 日月切换按钮
.switch {
  // 限制宽、高
  // flex: 2;

  &_box {
    // 方便利用父相子绝进行定位
    position: relative;
    width: 80px;
    height: 36px;
    margin: 0 auto 0 auto;
    border-radius: 100px;
    overflow: hidden;
  }

  // 最上面一层
  &_checkbox {
    // 防止被设置了position为absolute的元素覆盖
    position: relative;
    width: 100%;
    height: 100%;
    padding: 0;
    margin: 0;
    opacity: 0;
    cursor: pointer;
    z-index: 3;
  }
  &_checkbox:checked + &_text::before {
    content: "MON";
    // 利用left实现移动效果
    left: 48px;
    background-color: @color_blue;
  }

  &_checkbox:checked + &_layer {
    background-color: @color_blue;
  }

  // 中间层
  &_text {
    z-index: 2;
    transition: 0.3s ease all;

    // position:absolute;
    // 默认情况
    &::before {
      content: "DAY";
      position: absolute;
      top: 4px;
      left: 4px;
      width: 20px;
      height: 10px;
      color: @color_white;
      font-size: 10px;
      font-weight: bold;
      text-align: center;
      line-height: 1;
      padding: 9px 4px;
      background-color: @color_bold_green;
      border-radius: 50%;
      transition: 0.3s cubic-bezier(0.18, 0.89, 0.35, 1.15) all;
    }
  }
  // 最底层，控制背景颜色
  &_layer {
    width: 100%;
    background-color: #ebf7fc;
    transition: 0.3s ease all;
    z-index: 1;
  }

  &_text,
  &_layer {
    // 都是相对switch box进行定位
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
  }

  // &.day,
  // &.month {
  //     position:absolute;
  //     margin:0;
  //     padding:0;
  //     float:left;
  //     width: 70px;
  //     height: 28px;
  //     text-align:center;
  //     line-height:28px;
  //     // display:inline-block;
  //     // border:1px solid;
  //     background-color: rgba(255,255,255,0);

  //     &.active {
  //         border:inherit;
  //         background-color: @color_blue;
  //         color: @color_white;
  //     }

  // }
}
</style>
