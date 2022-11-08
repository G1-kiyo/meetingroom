<template>
  <!--查看会议预定的具体信息  -->
  <div class="detail">
    <!--侧边栏  -->
    <div class="detail_sidebar--outer">
      <sidebar :userJobInfo="userJobInfo"></sidebar>
    </div>
    <div class="roomcheck_info_check">
      <!--面包屑  -->
      <el-breadcrumb class="check_breadcrumb" :separator-icon="ArrowRight">
        <el-breadcrumb-item :to="{ path: '/user/profile/basic' }"
          >My Profile</el-breadcrumb-item
        >
        <el-breadcrumb-item :to="{ path: '/user/profile/my_room_check' }"
          >request</el-breadcrumb-item
        >
        <el-breadcrumb-item>details</el-breadcrumb-item>
      </el-breadcrumb>
      <section class="checkdetail">
        <!--介绍栏  -->
        <div class="normal checkdetail_intro">
          <div class="checkdetail_intro_roomname">
            {{ roomBookDetail.roomname.substring(5) }}
          </div>
          <div class="checkdetail_intro_descriptiongroup">
            <span>{{
              roomBookDetail.roomname[0].toUpperCase() +
              roomBookDetail.roomname.substring(1)
            }}</span>
            <span>{{ roomBookDetail.meetingRoom.description }}</span>
          </div>
        </div>
        <!-- 用户基本信息 -->
        <div class="normal checkdetail_userInfo">
          <div class="edit_title">
            <span>User Information</span>
            <div @click="editUserInfo">
              {{ userInfoEditable ? "Save" : "Edit" }}
            </div>
          </div>
          <div class="itemgroup">
            <div class="item" v-for="(item, key, index) in user" :key="item.label">
              <span>{{ item.label }}</span>
              <span
                :data-key="key"
                :contentEditable="userInfoEditable"
                :style="{ color: userInfoEditable ? 'black' : '#999999' }"
                v-html="item.value"
                @blur="changeUserInfoText($event)"
              ></span>
            </div>
          </div>
        </div>
        <!-- 房间信息 -->
        <div class="normal checkdetail_roomInfo">
          <span class="title">Room Information</span>
          <div class="itemgroup">
            <div class="item" v-for="item in room" :key="item.label">
              <span>{{ item.label }}</span>
              <span>{{ item.value }}</span>
            </div>
          </div>
        </div>
        <!-- 会议信息 -->
        <div class="autostretch checkdetail_meetingInfo">
          <div class="edit_title">
            <span>Meeting Information</span>
            <div @click="editMeetingInfo">
              {{ meetingInfoEditable ? "Save" : "Edit" }}
            </div>
          </div>
          <div class="itemgroup">
            <div class="item">
              <span>Date</span>
              <el-date-picker
                class="datepicker"
                v-if="meetingInfoEditable"
                v-model="roomBookDetail.meetingDate"
                type="date"
              />
              <span v-else>{{ roomBookDetail.meetingDate }}</span>
            </div>
            <div class="item">
              <span>Start Time</span>
              <el-time-select
                class="select_start"
                v-if="meetingInfoEditable"
                v-model="roomBookDetail.startTime.split(' ')[1]"
                :max-time="roomBookDetail.endTime.split(' ')[1]"
                start="09:00"
                step="00:30"
                end="21:00"
              />
              <span v-else>{{ roomBookDetail.startTime.split(" ")[1] }}</span>
            </div>
            <div class="item">
              <span>End Time</span>
              <el-time-select
                class="select_end"
                v-if="meetingInfoEditable"
                v-model="roomBookDetail.endTime.split(' ')[1]"
                :min-time="roomBookDetail.startTime.split(' ')[1]"
                start="09:00"
                step="00:30"
                end="21:00"
              />
              <span v-else>{{ roomBookDetail.endTime.split(" ")[1] }}</span>
            </div>
          </div>
          <div class="itemgroup">
            <div class="item">
              <span>Title</span>
              <span
                data-key="title"
                :contentEditable="meetingInfoEditable"
                :style="{ color: meetingInfoEditable ? 'black' : '#999999' }"
                v-html="roomBookDetail.title"
                @blur="changeMeetingInfoText($event)"
              ></span>
            </div>
            <div class="item">
              <span>Description</span>
              <span
                data-key="description"
                :contentEditable="meetingInfoEditable"
                :style="{ color: meetingInfoEditable ? 'black' : '#999999' }"
                v-html="roomBookDetail.description"
                @blur="changeMeetingInfoText($event)"
              ></span>
            </div>
            <div class="item"></div>
          </div>
          <div class="checkdetail_meetingInfo_member">
            <span>Member</span>
            <div class="memberlist">
              <div
                class="memberitem"
                v-for="item in roomBookDetail.member"
                :key="item.userId"
              >
                <el-avatar :size="30" :src="item.avatar"></el-avatar>
                <span>{{ item.truename }}</span>
                <el-icon
                  class="memberitem_remove"
                  @click="removeMember(item.userId)"
                  color="red"
                  ><RemoveFilled
                /></el-icon>
              </div>
              <div class="member_select" @click="selectMember">
                <el-icon :size="30"><Plus /></el-icon>
              </div>
            </div>
          </div>
        </div>
        <!-- 请求状况 -->
        <div class="normal checkdetail_bookInfo">
          <span class="title">Book Information</span>
          <span class="checkdetail_bookInfo_status">Request: success</span>
          <span class="checkdetail_bookInfo_audition">{{
            "Request audit: " + roomBookDetail.audition
          }}</span>
        </div>
      </section>
    </div>
    <memberinfo
      v-if="memberShow"
      :allMemberInfo="allMemberInfo"
      :infoGroupByDepartment="infoGroupByDepartment"
      @handleMemberClosed="handleMemberClosed"
    ></memberinfo>
  </div>
</template>
<script>
import store from "@/store";
import { ElMessage } from "element-plus";
import MemberInfo from "@/components/MemberInfo.vue";
import Sidebar from "@/components/Sidebar.vue";
export default {
  name: "RoomCheckDetail",
  components: {
    memberinfo: MemberInfo,
    sidebar: Sidebar,
  },
  props: ["meetingId"],
  data() {
    return {
      userJobInfo: {},
      roomBookDetail: {},
      userInfoEditable: false,
      meetingInfoEditable: false,
      memberCount: 0,
      allMemberInfo: [],
      infoGroupByDepartment: {},
      memberShow: false,
    };
  },
  computed: {
    user: function () {
      return {
        truename: {
          label: "Name",
          value: this.roomBookDetail.user.truename,
        },
        phone: {
          label: "Phone",
          value: this.roomBookDetail.user.phone,
        },
        email: {
          label: "Email",
          value: this.roomBookDetail.user.email,
        },
      };
    },
    room: function () {
      return {
        roomId: {
          label: "ID",
          isEditable: false,
          value: this.roomBookDetail.roomId,
        },
        capacity: {
          label: "Capacity",
          isEditable: false,
          value: this.roomBookDetail.meetingRoom.scale,
        },
        contact: {
          label: "Contact",
          isEditable: false,
          value: this.roomBookDetail.meetingRoom.contact,
        },
      };
    },
  },
  created() {
    var formData = new FormData();
    formData.append("meetingId", this.meetingId);
    const userJobInfo = this.$api.requestUserJobInfo({
      params: {
        userId: store.state.userId,
      },
    });
    const roomBookDetail = this.$api.requestRoomBookDetail({
      data: formData,
    });
    Promise.all([userJobInfo, roomBookDetail]).then((responses) => {
      return responses.map((response) => {
        if (response.data.userJobInfo) {
          this.userJobInfo = response.data.userJobInfo;
        }
        if (response.data.roomBookDetail) {
          this.roomBookDetail = response.data.roomBookDetail;
        }
      });
    });
  },
  methods: {
    handleMemberClosed: function () {
      this.memberShow = false;
      for (var item of store.state.selectedMember) {
        this.roomBookDetail.member.push(item);
      }
    },
    changeUserInfoText: function (e) {
      this.user[e.target.dataset.key].value = e.target.innerHTML;
    },
    changeMeetingInfoText: function (e) {
      this.roomBookDetail[e.target.dataset.key] = e.target.innerHTML;
    },
    editUserInfo: function () {
      this.userInfoEditable = !this.userInfoEditable;
      // console.log(this.userInfoEditable);
      if (this.userInfoEditable == false) {
        this.$api.updateUserInfo({
          data: {
            userId: store.state.userId,
            truename: this.user.truename.value,
            phone: this.user.phone.value,
            email: this.user.email.value,
          },
        });
      }
    },
    generateMemberList: function () {
      var memberList = [];
      var selectedMember = this.roomBookDetail.member;
      selectedMember.map((item) => {
        memberList.push(item.userId);
      });
      this.memberCount = memberList.length;
      return memberList;
    },
    editMeetingInfo: function () {
      this.meetingInfoEditable = !this.meetingInfoEditable;
      if (this.meetingInfoEditable == false) {
        this.$api.updateMyRoomBookInfo({
          data: {
            meetingId: this.roomBookDetail.meetingId,
            meetingDate: this.roomBookDetail.meetingDate,
            startTime:
              this.roomBookDetail.meetingDate + " " + this.roomBookDetail.startTime,
            endTime: this.roomBookDetail.meetingDate + " " + this.roomBookDetail.endTime,
            title: this.roomBookDetail.title,
            description: this.roomBookDetail.description,
            createTime: Date.now(),
            member: this.generateMemberList(),
            memberCount: this.memberCount,
          },
        });
      }
    },
    removeMember: function (userId) {
      for (let i = 0; i < this.roomBookDetail.member.length; i++) {
        if (this.roomBookDetail.member[i].userId == userId) {
          console.log(this.roomBookDetail.member);
          this.roomBookDetail.member.splice(i, 1);

          // this.selectedMembers.splice(i, 1);
        }
      }
      store.commit("removeMember", {
        userId: userId,
      });
    },
    //按部门分类所有成员
    allMemberInfoProcess: function (allMemberInfo) {
      this.infoGroupByDepartment = {};
      for (var item of allMemberInfo) {
        if (item.department in this.infoGroupByDepartment) {
          item["selected"] = false;
          this.infoGroupByDepartment[item.department].push(item);
        } else {
          this.infoGroupByDepartment[item.department] = [];
          item["selected"] = false;
          this.infoGroupByDepartment[item.department].push(item);
        }
      }
      // this.infoGroupByDepartment = newMap;
    },
    selectMember: function () {
      if (this.meetingInfoEditable == true) {
        this.$api.requestAllMember({}).then((res) => {
          this.allMemberInfo = res.data.allMemberInfo;
          this.allMemberInfoProcess(this.allMemberInfo);
          this.memberShow = true;
        });
      } else {
        ElMessage({
          type: "warning",
          message: "Non-editable text field.",
        });
      }
    },
  },
};
</script>
