<template>
  <img
    :src="meetingroomInfo.roomImg"
    class="image"
    :style="{ width: '200px', height: '120px' }"
  />
  <div class="details" style="padding: 10px">
    <div class="bottom">
      <span :style="{ fontSize: '14px', float: 'left' }">{{
        meetingroomInfo.roomname
      }}</span>
      <span :style="{ fontSize: '14px', float: 'right', color: '#999999' }">{{
        meetingroomInfo.scale
      }}</span>
    </div>
    <div :style="{ fontSize: '14px', textAlign: 'left' }">
      {{ meetingroomInfo.status }}
    </div>
  </div>
</template>
<script>
export default {
  name: "MeetingRoomCard",
  props: ["meetingroomInfo", "date"],
  data() {
    return {
      info: this.meetingroomInfo,
    };
  },
  watch: {
    // meetingroomInfo:{
    //     immediate:true,
    //     deep:true,
    //     handler(newVal,oldVal){
    //         this.info = newVal;
    //     }
    // }
  },
  methods: {},
};
</script>
<style lang="less" scoped>
.details {
  // border:solid;
}
.bottom {
  height: 20px;
  //   margin-top: 13px;
  //   line-height: 12px;
  //   display: flex;
  //   justify-content: space-between;
  //   align-items: center;
}
</style>
