<template>
  <div class="video_pattern">
    <!--预约表单  -->
    <div class="video_pattern_form">
      <!--模式切换  -->
      <div class="v-tab" @click="changeTab($event)">
        <div class="schedule_tab" data-index="1">Schedule Meeting</div>
        <div class="instant_tab" data-index="2">Instant Meeting</div>
      </div>
      <!-- 会议预约 -->
      <section class="schedule_type" v-show="scheduleTypeShow">
        <div class="s-topic">
          <span>Topic</span>
          <el-input v-model="scheduleForm.topic"></el-input>
        </div>
        <div class="s-time">
          <div class="s-time_start">
            <span>Start</span>
            <el-input
              v-model="scheduleForm.startDate"
              type="date"
              placeholder="Pick a day"
              :min="minDate"
            >
              <template #suffix>
                <el-icon class="calendar-icon"><calendar /></el-icon>
              </template>
            </el-input>
            <el-time-picker
              v-model="scheduleForm.startTime"
              placeholder="Select time"
              :disabled-hours="disableHours"
              :disabled-seconds="disableSeconds"
            />
          </div>
          <div class="duration">
            <el-select
              v-model="scheduleForm.duration.hour"
              clearable
              placeholder="Select hour"
            >
              <el-option
                v-for="item in hourList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <el-select
              v-model="scheduleForm.duration.minute"
              clearable
              placeholder="Select minute"
            >
              <el-option
                v-for="item in minuteList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </div>
        </div>
        <div class="video_set">
          <div class="host">
            <span>Host</span>
            <el-radio-group v-model="scheduleForm.hostSet" class="host_set">
              <el-radio label="on" size="large">On</el-radio>
              <el-radio label="off" size="large">Off</el-radio>
            </el-radio-group>
          </div>
          <div class="host">
            <span>Participants</span>
            <el-radio-group
              v-model="scheduleForm.partcipantsSet"
              class="participants_set"
            >
              <el-radio label="on" size="large">On</el-radio>
              <el-radio label="off" size="large">Off</el-radio>
            </el-radio-group>
          </div>
        </div>
        <div class="btn_group">
          <button class="schedule_btn" type="button" @click="bindSchedule">
            Schedule
          </button>
          <button class="cancel-btn" type="button" @click="bindCancel">Cancel</button>
        </div>
      </section>
      <!-- 即时会议 -->
      <section class="instant_type" v-show="instantTypeShow">
        <div class="i-mainInfo">
          <div class="meetingId">
            <span>Meeting ID:</span><span>{{ instantForm.meetingId }}</span>
          </div>
          <div class="password">
            <span>Password:</span
            ><el-input v-model="instantForm.password" type="text"
              ><template #suffix
                ><el-icon @click="generatePassword"><Refresh /></el-icon></template
            ></el-input>
            <!-- password切换 -->
            <custom-switch
              class="password_edit_switch"
              :defaultCheck="passwordCheck"
              defaultValue=""
              theOtherValue=""
              @checkStateChange="changeCheckStatus('password')"
            ></custom-switch>
          </div>
          <div class="video">
            <span>Video:</span>
            <div class="host_set">
              <span>host</span>
              <!-- host切换 -->
              <custom-switch
                class="host_status_switch"
                :defaultCheck="hostCheck"
                defaultValue=""
                theOtherValue=""
                @checkStateChange="changeCheckStatus('host')"
              ></custom-switch>
            </div>
            <div class="participant_set">
              <span>participants</span>
              <!-- host切换 -->
              <custom-switch
                class="participant_status_switch"
                :defaultCheck="participantCheck"
                defaultValue=""
                theOtherValue=""
                @checkStateChange="changeCheckStatus('participant')"
              ></custom-switch>
            </div>
          </div>
          <div class="other">
            <span>Other:</span
            ><el-radio-group v-model="muteSet" class="mute_set">
              <el-radio label="1" size="large">Mute all participants</el-radio>
              <el-radio label="2" size="large"
                >Mute when at least 6 participants enter</el-radio
              >
            </el-radio-group>
          </div>
        </div>
        <button class="start_meeting" type="button" @click="startMeeting">Start</button>
      </section>
    </div>
    <!-- 会议邀请 -->
    <div class="video_pattern_invitation">
      <div class="title">Invite Attendee</div>
      <div>
        <el-input v-model="attendeeEmail" placeholder="Attendee Email"></el-input>
        <span>Use the commas to separate multiple invitation recipients</span>
      </div>
      <p>or</p>
      <div>
        <span>Share the link with your attendees</span
        ><el-input v-model="invitationLink" disabled="true"></el-input>
      </div>
      <div class="btn_group">
        <button class="send_email" @click="sendEmail">Send Email</button>
        <button class="copy_link" @click="copyLink">Copy Link</button>
      </div>
    </div>
  </div>
</template>
<script>
import CustomSwitch from "@/components/CustomSwitch.vue";
import store from "@/store";
import { ref, reactive, inject, computed } from "vue";
const useSchedule = function () {
  const scheduleForm = reactive({
    topic: "",
    startDate: "",
    startTime: "",
    duration: {
      hour: "",
      minute: "",
    },
    hostSet: "on",
    participantSet: "on",
  });
  const $api = inject("$api");
  const makeRange = function (start, end) {
    let result = [];
    for (let i = 0; i < end - start + 1; i++) {
      result.push(start + i);
    }
  };
  const disableHours = function () {
    const date = new Date().getHours();
    return makeRange(1, date - 1).concat(makeRange(date + 1, 24));
  };
  const disableSeconds = function () {
    return makeRange(1, 59);
  };
  const hourList = makeRange(1, 24).map((item) => {
    return item < 10 ? "0" + item : item;
  });
  const minuteList = makeRange(1, 59).map((item) => {
    return item < 10 ? "0" + item : item;
  });
  const bindSchedule = function () {
    $api.requestScheduleOnlineMeeting({
      data: {
        ...scheduleForm,
      },
    });
  };
  const bindCancel = function () {
    for (let key in scheduleForm) {
      scheduleForm[key] = "";
    }
    scheduleForm["duration"] = {
      hour: "",
      minute: "",
    };
    scheduleForm["hostSet"] = "on";
    scheduleForm["participantSet"] = "on";
  };
  return {
    scheduleForm,
    hourList,
    minuteList,
    disableHours,
    disableSeconds,
    bindSchedule,
    bindCancel,
  };
};
const useInstant = function () {
  const checkStatus = reactive({
    password: true,
    host: true,
    participant: true,
  });
  const instantForm = reactive({
    meetingId: meetingId,
    password: generatePassword(6),
  });
  const muteSet = ref(1);
  const generatePassword = function (length) {
    let reseult = "";
    for (let i = 0; i < length; i++) {
      result += Math.floor(Math.random() * 9);
    }
    instantForm.password = result;
  };
  const changeCheckStatus = function (type) {
    if (type == "host") {
      checkStatus.host = !checkStatus.host;
    } else if (type == "participant") {
      checkStatus.participant = !checkStatus.participant;
    } else if (type == "password") {
      checkStatus.password = !checkStatus.password;
    }
  };
  return { checkStatus, instantForm, muteSet, generatePassword, changeCheckStatus };
};
export default {
  name: "OnlineVideoPattern",
  componnets: {
    "custom-switch": CustomSwitch,
  },
  setup() {
    const scheduleTypeShow = ref(true);
    const instantTypeShow = ref(false);
    const requestStatus = ref(false);
    const meetingId = ref("");
    const changeTab = function (e) {
      const index = e.target.dataset.index;
      if (index == 1) {
        scheduleTypeShow.value = false;
        instantTypeShow.value = true;
      } else {
        scheduleTypeShow.value = true;
        instantTypeShow.value = false;
      }
    };
    watch(instantTypeShow, async (newValue, oldValue) => {
      if (newValue && !requestStatus) {
        meetingId = await $api.requestPersonalMeetingId({
          params: {
            userId: store.state.userId,
          },
        });
      }
    });
  },
};
</script>
