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
  props: ["mode", "defaultIndex"],
  data() {
    return {
      currentIndex: this.defaultIndex,
    };
  },
  mounted() {
    // console.log(this);
    // this.$on("updateIndex", this.updateIndex);
  },
  methods: {
    updateIndex: function (newIndex) {
      this.currentIndex = newIndex;
    },
  },
};
</script>
<style scoped lang="less">
.navigator {
  border: none;
  background-color: white;
  .horizontal {
  }
  .vertical {
    width: 72px;
  }
}
</style>
