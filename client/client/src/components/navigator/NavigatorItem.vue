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
  props: ["path", "func", "index"],
  // emits: ["updateIndex"],
  methods: {
    onClick: function () {
      if (this.path) {
        this.$router.push({ path: this.path });
      }
      if (this.func) {
        this.func();
      }
      //通过指令调用父组件的方法
      this.$parent.updateIndex(this.index);
    },
  },
};
</script>
<style lang="less" scoped>
.horizontal {
  & > .navigator_item {
    display: inline-block;
    padding: 10px;
    // width: 90px;
    // height: 30px;
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
    width: 100%;
    // height: 67px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    &.--active {
    }
  }
}
</style>
