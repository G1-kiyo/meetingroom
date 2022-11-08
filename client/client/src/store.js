import { createStore } from "vuex";
import api from "@/api/Api";

const store = createStore({
  state() {
    return {
      token: sessionStorage.getItem("token"),
      userId: sessionStorage.getItem("userId"),
      selectedMember: []
    }
  },
  mutations: {
    setToken(state, payload) {

      state.token = payload.token;
      sessionStorage.setItem("token", payload.token)
      // console.log(state.token)
    },
    clearToken(state) {
      state.token = "";
      sessionStorage.setItem("token", "")
    },
    setUserId(state, payload) {
      state.userId = payload.userId
      sessionStorage.setItem("userId", payload.userId)
    },
    clearUserId(state) {
      state.userId = "",
        sessionStorage.setItem("userId", "")
    },
    addMember(state, payload) {
      state.selectedMember.push(payload.member);
    },
    removeMember(state, payload) {
      for (let i = 0; i < state.selectedMember.length; i++) {
        if (state.selectedMember[i].userId == payload.userId) {
          state.selectedMember.splice(i, 1)
        }
      }
    },
    removeAllMember(state) {
      state.selectedMember = []
    },
    getUserInfo(state) {
      return api.requestUserJobInfo({
        params: state.userId
      });
    },
    getDynamicRoutes(state,payload) {
      api.requestDynamicRoutes({}).then((res) => {
        const allRoutes = res.data;
        const authList = payload.authList;
        const matchedRoutes = filterRoutes(allRoutes, authList);
        for (let route of matchedRoutes) {
          router.addRoute(formatDynamicRoutes(route, allRoutes));
        }
        sessionStorage.setItem("auth",authList);
        sessionStorage.setItem("routes",matchedRoutes);

      })
    }
  },
  actions: {
    getUserInfo({ commit }) {
      commit("getUserInfo");
    },
    getDynamicRoutes({ commit },payload) {
      commit("getDynamicRoutes",payload);
    },

  }
})

export default store;