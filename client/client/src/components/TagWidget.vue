<template>
  <!-- 类似于面包屑的标签 -->
  <div class="tag">{{ tagName }}</div>
</template>

<script>
export default {
  name: "TagWidget",
  props: {
    tagName: String,
  },
};
</script>

<style scoped lang="less">
@import "../assets/css/global.less";
@import "../assets/css/var.less";
// 标签
.tag {
  font-size: 16px;
  text-align: left;
  margin: 30px 0 0 20px;
  display: inline-block;
  .mixin_border(none none solid none,@color_blue, 25px, auto);
}
</style>
