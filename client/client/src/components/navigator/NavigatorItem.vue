<template>
  <div class="navigator_item" @click="onClick">
    <!--插槽包括图片和文字  -->
    <slot name="navigator-item-img"></slot>
    <slot name="navigator-item-text"></slot>
  </div>
</template>

<script>
export default {
  name: "NavigatorItem",
  props: ["path", "func"],
  methods: {
    onClick: function () {
      if (this.path) {
        this.$router.push({ path: this.path });
      }
      if (this.func) {
        this.func();
      }
    },
  },
};
</script>
<style lang="less" scoped>
.horizontal {
  & > .navigator_item {
    display: inline-block;
    width: 90px;
    height: 30px;
    text-align: center;
    line-height: 30px;
    &.--active {
      border-bottom: 3px solid #00008b;
      & > .text {
        color: #00008b;
        font-weight: bold;
      }
    }
  }
}
.vertical {
  & > .navigator_item {
    width: 72px;
    height: 67px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    &.--active {
      .iconfont[data-type="1"] {
        color: #264de8;
      }
      .iconfont[data-type="2"] {
        color: #ff0000;
      }
      .text {
      }
    }
  }
}
</style>
