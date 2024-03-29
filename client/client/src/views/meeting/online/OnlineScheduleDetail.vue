<template>
  <div class="schedule">
    <main>
      <div class="invitation" v-show="invitationShow">
        <el-icon class="close" @click="hideInvitation" size="15"><CloseBold /></el-icon>
        <div>
          <i class="iconfont icon-zhucetianjiahaoyou"></i>
          <span class="ind-tit">Invite People</span>
        </div>
        <div class="ind-con">
          <p>
            Share the link<span class="ind-emp"
              >https://online_meeting/invitation/{{ curMt.meetingId }}</span
            >
          </p>
          <p>or</p>
          <p>
            Share the Meeting ID
            <span class="ind-emp">{{ curMt.meetingId }}</span> directly
          </p>
        </div>
        <div class="btn-group">
          <button
            class="btn copy-link"
            @click="
              copyToClipBoard(`https://online_meeting/invitation/${curMt.meetingId}`)
            "
          >
            Copy Link
          </button>
          <button class="btn copy-id" @click="copyToClipBoard(curMt.meetingId)">
            Copy Meeting ID
          </button>
          <button class="btn send-email" @click="showEmailInput">Email</button>
        </div>
        <el-input
          class="send-email-input"
          v-if="emailInputShow"
          v-model="receiverEmail"
          placeholder="Click button to select receivers"
          ><template #append>
            <div class="operation">
              <button
                class="in-btn operation-select"
                type="button"
                @click="selectMemberShow = true"
              >
                <i class="iconfont icon-icon-choose"></i>
              </button>
              <button
                class="in-btn operation-send"
                type="button"
                @click="sendInvitationEmail"
              >
                <i class="iconfont icon-fasong"></i>
              </button>
            </div> </template
        ></el-input>
      </div>
      <member-info
        v-show="selectMemberShow"
        @handleMemberClosed="confirmSelectedMember"
      ></member-info>
      <section class="s-mtlist">
        <div class="head">
          <el-tooltip
            class="box-item"
            effect="dark"
            content="Refresh"
            placement="left-start"
          >
            <el-icon class="h-refresh" @click="requestUpcomingOnlineMeeting" size="20"
              ><RefreshRight
            /></el-icon>
          </el-tooltip>

          <div class="h-title">Upcoming</div>
          <el-tooltip
            class="box-item"
            effect="dark"
            content="Start a new meeting"
            placement="right-start"
          >
            <el-icon class="h-invite" @click="routeToGroupQuery" size="24"
              ><CirclePlus
            /></el-icon>
          </el-tooltip>
        </div>
        <div class="personalId">
          <span>{{ personalId }}</span>
          <span>My Personal Meeting ID</span>
        </div>
        <div class="mtlist-con">
          <span class="mtlist-tit">Today</span>
          <ul>
            <li
              v-for="(item, index) in upcomingOnlineMeeting"
              v-key="item.meetingId"
              :data-index="index"
              @click="selectMeeting"
            >
              <div
                class="mtlist-con-box"
                :class="{ '--active': item.meetingId == curMt.meetingId }"
              >
                <span class="txt topic">{{ item.topic }}</span>
                <span class="txt timeperiod">{{
                  `${checkTimePeriod(
                    new Date(item.scheduleStartTime)
                  )} - ${checkTimePeriod(new Date(item.scheduleEndTime))}`
                }}</span>
                <span class="txt meetingid">Meeting Id: {{ item.meetingId }}</span>
              </div>
            </li>
          </ul>
        </div>
      </section>
      <el-divider direction="vertical" class="m-divider" />
      <section class="s-mtinvite">
        <div>
          <span class="txt topic">{{ curMt.topic }}</span>
          <div class="i-time">
            <span class="period">{{
              `${checkTimePeriod(new Date(curMt.scheduleStartTime))} - ${checkTimePeriod(
                new Date(curMt.scheduleEndTime)
              )}`
            }}</span>
            <el-divider direction="vertical" />
            <span class="willst">{{
              triggerWillStartOutput(curMt.meetingDate, curMt.schedulStartTime)
            }}</span>
          </div>
          <span class="txt meetingid">Meeting Id: {{ curMt.meetingId }}</span>
          <div class="btn-group">
            <button class="btn start">Start</button>
            <button class="btn invite" @click="showInvitation">Invite</button>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
import store from "@/store.js";
import clipboard from "@/utils/Clipboard.js";
import MemberInfo from "@/components/MemberInfo";
export default {
  name: "OnlineScheduleDetail",
  components: {
    "member-info": MemberInfo,
  },
  data() {
    return {
      upcomingOnlineMeeting: {},
      interval: null,
      invitationShow: false,
      emailInputShow: false,
      selectMemberShow: false,
      receiverEmail: "",
    };
  },
  computed: {
    curMt: function () {
      if (this.upcomingOnlineMeeting.length != 0) return this.upcomingOnlineMeeting[0];
    },
    willStart: function () {},
  },
  created() {
    this.requestUpcomingOnlineMeeting();
  },
  methods: {
    requestUpcomingOnlineMeeting: function () {
      this.$api
        .requestUpcomingOnlineMeeting({
          params: {
            date: this.$dateProcess.dateFormat(new Date(), "YYYY-MM-DD"),
            userId: store.state.userId,
          },
        })
        .then((res) => {
          this.upcomingOnlineMeeting = res.data;
        });
    },
    routeToGroupQuery: function () {
      this.$router.push({ path: "/meeting/online/group_query" });
    },
    selectMeeting: function (e) {
      this.curMt = this.upcomingOnlineMeeting[e.currentTarget.dataset.index];
    },
    outputWillStart: function (meetingDate, startTime) {
      var selectedMeetingDate = meetingDate + " " + startTime;
      var now = new Date();
      var willStart = "";
      if (new Date(selectedMeetingDate).getTime() - now.getTime() <= 0) {
        willStart = "Time is up";
      } else {
        //是否超过1个小时
        var diff = this.$dateProcess.dateDiff(selectedMeetingDate, now, "second");
        if (diff > 60 * 60) {
          //是否超过1分钟
          if (diff % (60 * 60) > 1) {
            willStart =
              "Starts in " + diff / (60 * 60) + " hours" + ((diff % (60 * 60)) + " mins");
          } else {
            willStart =
              "Starts in " + diff / (60 * 60) + " hours" + ((diff % (60 * 60)) + " min");
          }
          //刚好1小时
        } else if (diff / (60 * 60) == 1) {
          willStart = "Starts in 1 hour";
          //小于1小时
        } else {
          if (diff < 60) {
            willStart = "Starts in less than 1 mins";
          } else {
            if (diff % (60 * 60) == 1) {
              willStart = "Starts in " + (diff % (60 * 60)) + " min";
            } else if (diff % (60 * 60) > 1) {
              willStart = "Starts in " + (diff % (60 * 60)) + " mins";
            }
          }
        }
      }
      return willStart;
    },
    triggerWillStartOutput: function (meetingDate, startTime) {
      this.outputWillStart(meetingDate, startTime);
      if (this.interval) clearInterval(this.interval);
      //此时会先执行一次output内的代码，然后返回function
      //返回function的时候就和setInterval(function(){this.outputWillStart()})效果一致
      //能够实现interval立即执行（除了先调用一次output也可以采用下面的方法实现）
      this.interval = setInterval(function () {
        this.outputWillStart(meetingDate, startTime);
      }, 1000);
    },
    showInvitation: function () {
      this.invitationShow = true;
    },
    hideInvitation: function () {
      this.invitationShow = false;
      this.emailInputShow = false;
    },
    copyToClipBoard: function (text, e) {
      if (navigator.clipboard) {
        navigator.clipboard.writeText(text);
      } else {
        // let inputElement = document.createElement("input");
        // inputElement.className = "input-hidden";
        // inputElement.value = text;
        // document.body.appendChild(inputElement);
        // inputElement.select();
        // document.execCommand("copy");
        // document.body.removeChild(inputElement);
        let selector = e.currentTarget.className;
        clipboard(selector, text);
      }
    },
    showEmailInput: function () {
      this.emailInputShow = true;
    },
    sendInvitationEmail: function () {
      // let formData = new FormData();
      this.$api.requestInvitationWithEmail({
        data: {
          sender: store.state.userId,
          receiver: store.state.selectedMember,
          meetingTitle: this.curMt.meetingTitle,
          meetingDate: this.curMt.meetingDate,
          startTime: this.curMt.scheduledSTartTime,
          endTime: this.curMt.scheduledEndTime,
          meetingLink: `https://online_meeting/invitation/${this.curMt.meetingId}`,
          meetingId: this.curMt.meetingId,
        },
      });
    },
    confirmSelectedMember: function () {
      this.selectMemberShow = false;
      var receiverEmailStr = "";
      var selectedMember = store.state.selectedMember;
      var index = 0;
      for (let email of selectedMember) {
        index += 1;
        if (index == selectedMember.length) {
          receiverEmailStr += email;
        } else {
          receiverEmailStr += email + ";";
        }
      }
      this.receiverEmail = receiverEmailStr;
    },
  },
};
</script>
