<template>
  <div
    class="navigator"
    :class="{ horizontal: mode == 'horizontal', vertical: mode == 'vertical' }"
  >
    <slot name="navigator-item" :currentIndex="currentIndex"></slot>
  </div>
</template>

<script>
import NavigatorItem from "./NavigatorItem.vue";
export default {
  name: "Navigator",
  components: {
    "navigator-item": NavigatorItem,
  },
  props: ["mode", "default-index"],
  data() {
    return {
      currentIndex: this["default-index"],
    };
  },
  methods: {},
};
</script>
<style scoped lang="less">
.navigator {
  border: none;
  background-color: white;
}
</style>
