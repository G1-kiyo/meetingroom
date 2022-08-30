<template>
  <div class="roombook--outer">
    <div class="roombook--inner">
      <!-- 额外辅助 -->
      <div class="roombook_extra">
        <!-- 用户头像 -->
        <div class="roombook_extra_avatar">
          <img :src="userJobInfo.avatar" :alt="userJobInfo.truename" />
        </div>
        <!-- handleCommand 接受命令并进行相应处理 -->
        <el-dropdown class="roombook_extra_dropdown" @command="handleCommand">
          <el-icon :size="30"><Menu /></el-icon>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="setting">Setting</el-dropdown-item>
              <el-dropdown-item command="logout">Log out</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <!-- 主要预约表单填写处 -->
      <div class="roombook_form">
        <!-- 房间名 -->
        <div class="roombook_form_roomname">{{ bookForm.roomname }}</div>
        <!-- 会议名称 -->
        <input
          ref="meeting_title"
          class="roombook_form meeting_title"
          :value="bookForm.title"
          @blur="changeMeetingTitle"
          placeholder="Add title"
        />
        <!-- 会议日期时间 -->
        <div class="roombook_form meeting_date">
          <el-icon :style="{ marginRight: '10px' }"> <AlarmClock /> </el-icon>{{ date }}
        </div>
        <span class="roombook_form meeting_time"
          >{{ bookStartTime }}-{{ bookEndTime }}</span
        >
        <!-- 会议描述 -->
        <div class="roombook_form meeting_description">
          <div>
            <el-icon :style="{ marginRight: '10px' }"> <EditPen /> </el-icon>Add
            description
          </div>
          <el-input
            @input="userInputControl"
            type="textarea"
            maxlength="250"
            show-word-limit
            resize="none"
            :rows="3"
            class="user_input"
            :class="{ '--overflow': descriptionOverflow }"
            placeholder="No more than 250 characters"
            v-model="bookForm.description"
          ></el-input>
        </div>
        <!-- 会议成员 -->
        <div class="roombook_form meeting_member">
          <!-- 标题栏 -->
          <div>
            <i
              class="iconfont icon-kehuqunzu"
              :style="{ marginRight: '10px', fontSize: '18px' }"
            ></i
            ><span class="text">Add members</span>
            <el-icon @click="$emit('handleMemberShow')" :style="{ marginLeft: '10px' }">
              <CirclePlusFilled />
            </el-icon>
          </div>
          <!-- 操作按钮 -->
          <!-- 当选中成员超过3个 -->
          <div
            class="roombook_form meeting_member operation"
            v-if="selectedMember.length > 3"
          >
            <div v-for="n in 3" :key="n">
              <el-avatar
                :size="30"
                :src="selectedMember[n - 1].avatar"
                :style="{ marginLeft: n == 1 ? 0 : '-5px' }"
              ></el-avatar>
            </div>
            <span>{{ "+" + (selectedMember.length - 3) + "members" }}</span>
            <el-tooltip effect="dark" :content="memberNameString" placement="top">
              <el-icon class="roombook_form meeting_member operation_tooltip">
                <MoreFilled />
              </el-icon>
            </el-tooltip>
          </div>
          <!-- 当选中成员小于3个 -->
          <div class="roombook_form meeting_member operation" v-else>
            <div v-for="n in selectedMember.length" :key="n">
              <el-avatar
                :size="30"
                :src="selectedMember[n - 1].avatar"
                :style="{ marginLeft: n == 1 ? 0 : '-5px' }"
              ></el-avatar>
            </div>
            <el-tooltip effect="dark" :content="memberNameString" placement="top">
              <el-icon
                v-if="selectedMember.length != 0"
                class="roombook_form meeting_member operation_tooltip"
              >
                <MoreFilled />
              </el-icon>
            </el-tooltip>
          </div>
        </div>
      </div>
      <div class="roombook_buttongroup">
        <button class="roombook_buttongroup_book" @click="submitBookForm">
          BOOK NOW
        </button>
        <button class="roombook_buttongroup_exit" @click="exit">EXIT</button>
      </div>
    </div>
  </div>
</template>
<script>
import store from "@/store";
import { ElMessage } from "element-plus";

export default {
  name: "RoomBook",
  props: ["roomId", "date", "userJobInfo", "roomname", "bookStartTime", "bookEndTime"],
  emits: ["handleMemberShow"],
  data() {
    return {
      bookForm: {
        roomname: this.roomname,
        title: "",
        description: "",
      },
      selectedMember: store.state.selectedMember,
      selectedMemberKey: [],
      // memberNameString: this.generateMemberNameString(),
      descriptionOverflow: false,
      // memberNameString: this.generateMemberNameString(),
    };
  },
  created() {},
  computed: {
    // title: function () {
    //   console.log(this.$refs["meeting_title"].innerText);
    //   return this.$refs["meeting_title"].innerText;
    // },
    memberNameString: function () {
      return this.generateMemberNameString();
    },
  },
  methods: {
    changeMeetingTitle: function () {
      this.bookForm.title = this.$refs["meeting_title"].value;
    },
    generateMemberNameString: function () {
      // console.log(store.state.selectedMember, this.selectedMember);
      var str = "";
      this.selectedMemberKey = [];
      for (let i = 0; i < this.selectedMember.length; i++) {
        this.selectedMemberKey.push({ userId: this.selectedMember[i].userId });
        if (i != this.selectedMember.length - 1) {
          str = str + this.selectedMember[i].truename + ",";
        } else {
          str = str + this.selectedMember[i].truename;
        }
      }
      return str;
    },
    handleCommand: function (command) {
      switch (command) {
        case "setting":
          this.$router.push({ path: "/setting" });
          break;
        case "logout":
          this.logout();
          break;
        default:
          break;
      }
    },
    logout: function () {
      store.commit("clearToken");
      store.commit("clearUserId");
    },
    userInputControl: function () {
      if (this.bookForm.description.length > 250) {
        this.descriptionOverflow = true;
        this.bookForm.description = this.bookForm.description.substring(0, 250);
      } else {
        this.descriptionOverflow = false;
      }
    },
    submitBookForm: function () {
      var submitFlag = true;
      for (let key in this.bookForm) {
        if (this.bookForm[key] == "") {
          submitFlag = false;
        }
      }
      if (submitFlag) {
        this.$api
          .uploadRoomBookInfo({
            data: {
              roomId: this.roomId,
              userId: this.userJobInfo.userId,
              username: this.userJobInfo.username,
              roomname:
                this.bookForm.roomname[0].toLowerCase() +
                this.bookForm.roomname.substring(1),
              title: this.bookForm.title,
              description: this.bookForm.description,
              meetingDate: this.date,
              startTime: this.date + " " + this.bookStartTime,
              endTime: this.date + " " + this.bookEndTime,
              createTime: Date.now(),
              member: this.selectedMemberKey,
              memberCount: this.selectedMemberKey.length,
            },
          })
          .then(() => {
            store.commit("removeAllMember");
          });
      } else {
        ElMessage({
          type: "warning",
          message: "Please complete the form before you submit it.",
        });
      }
    },
    exit: function () {
      this.$router.push({ name: "room_info_query" });
    },
  },
};
</script>
<style lang="less" scoped>
@import "@/assets/css/var.less";
.roombook {
  &--outer {
    grid-area: book;
  }
  &--inner {
    position: fixed;
    padding: 0 0 0 15px;
    height: 800px;
    width: 280px;
    border: none;
    background-color: @color_white;
  }

  //额外辅助
  &_extra {
    position: absolute;
    right: 15px;
    //头像
    &_avatar {
      display: inline-block;
      width: 30px;
      height: 30px;
      border-radius: 50%;
      border: 0.8px solid;
      // box-shadow: inset 0 0 10px 5px @color_grey;
      margin-right: 10px;
      & > img {
        width: 30px;
        height: 30px;
        border-radius: 50%;
      }
    }
    &_dropdown {
      width: 30px;
      height: 30px;
    }
  }
  // 会议预约表单
  &_form {
    // 会议室
    &_roomname {
      font-size: 32px;
      font-family: "Bookman Old Style";
      color: #267915;
      font-weight: bold;
      margin-top: 70px;
    }
    // 会议信息
    &.meeting {
      // 名称
      //父相子绝 脱离文档流
      &_title {
        position: relative;
        top: 40px;
        width: 90%;
        height: 30px;
        border-style: none none solid none;
        border-width: 2px;
        border-color: #666666;
        &:focus {
          border-style: none none solid none;
          outline: none;
        }
        &::-webkit-input-placeholder {
          font-size: 30px;
          font-weight: 1.2;
          color: #999999;
          font-family: "Bookman Old Style";
        }
        &:empty::before {
          content: "Add title";
          position: absolute;
          top: -10px;
          left: 0;
          display: block;
          font-size: 30px;
          font-weight: 1.2;
          color: #999999;
          font-family: "Bookman Old Style";
        }
      }
      // 日期
      &_date {
        margin-top: 80px;
        color: #999999;
        font-weight: bold;
      }
      // 时间
      &_time {
        margin-top: 15px;
        font-size: 30px;
        font-weight: bold;
        font-family: "Bookman Old Style";
      }
      // 描述
      &_description {
        margin-top: 40px;
        & > div {
          color: #999999;
          font-weight: bold;
        }
        .user_input {
          width: 100%;
          border-radius: 50%;
          &--overflow {
            color: red;
          }
        }
      }
      // 添加成员
      &_member {
        .text {
          color: #999999;
          font-weight: bold;
        }
        &.operation {
          border: 0.8px solid #cccccc;
          width: 100%;
          height: 30px;
          border-radius: 120px;
          box-shadow: 0.5px 0.5px 3px 1px #cccccc;
          display: flex;
          flex-direction: row;
          align-items: center;
          & > div {
            display: inline-block;
          }
          & > span {
            margin-left: 15px;
            font-size: 14px;
            font-family: "Bookman Old Style";
          }
        }
        &.operation_tooltip {
          /* float: right; */
          position: absolute;
          right: 0;
        }
      }
    }
  }
  // 按钮组
  &_buttongroup {
    &_book,
    &_exit {
      width: 100%;
      height: 47px;
      border-width: 1px;
      text-align: center;
      line-height: 47px;
    }
    &_book {
      margin-top: 10px;
      background-color: @color_blue;
      color: white;
    }
    &_exit {
      margin-top: 20px;
      background-color: @color_white;
    }
  }
}
</style>
<style lang="less">
.el-input .el-input__wrapper {
  // box-shadow: 0 0 0 0px grey inset;
  // &:hover {
  //   box-shadow: 0 0 0 0px white inset;
  // }
  // &.is-focus {
  //   box-shadow: 0 0 0 0px white inset;
  // }
}
.el-input .el-input__inner {
  /* content: "Add title";
  position: absolute;
  top: -10px;
  left: 0;
  display: block; */
  // &::-webkit-input-placeholder {
  //   font-size: 30px;
  //   font-weight: 1.2;
  //   font-family: "Bookman Old Style";
  // }
}
</style>
