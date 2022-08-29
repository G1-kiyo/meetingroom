import {createStore} from "vuex";

const store = createStore({
    state(){
      return {
        token:sessionStorage.getItem("token"),
        userId:sessionStorage.getItem("userId"),
        selectedMember:[]
      }
    },
    mutations:{
      setToken(state,payload){
    
        state.token = payload.token;
        sessionStorage.setItem("token",payload.token)
        // console.log(state.token)
      },
      clearToken(state){
        state.token = "";
        sessionStorage.setItem("token","")
      },
      setUserId(state,payload){
        state.userId = payload.userId
        sessionStorage.setItem("userId",payload.userId)
      },
      clearUserId(state){
        state.userId = "",
        sessionStorage.setItem("userId","")
      },
      addMember(state,payload){
        state.selectedMember.push(payload.member);
      },
      removeMember(state,payload){
        for(let i=0;i<state.selectedMember.length;i++){
          if(state.selectedMember[i].userId==payload.userId){
            state.selectedMember.splice(i,1)
          }
        }
      }
    }
  })

export default store;