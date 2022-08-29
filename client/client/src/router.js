import {createRouter,createWebHistory} from "vue-router"
import UserLogin from "./views/user/UserLogin.vue"
import PasswordReset from "./views/user/PasswordReset.vue"
import RequestPasswordReset from "./views/user/RequestPasswordReset.vue"
import UserRegister from "./views/user/UserRegister.vue"
import UserProfile from "./views/user/UserProfile.vue"
import OnlineMeetingSchedule from "./views/meeting/online/OnlineMeetingSchedule.vue"
import RoomInfoQuery from "./views/meeting/offline/RoomInfoQuery.vue"
import RoomDayDetail from "./views/meeting/offline/RoomDayDetail.vue"
import RoomMonDetail from "./views/meeting/offline/RoomMonDetail.vue"
import MyRoomCheck from "./views/user/user-profile/MyRoomCheck.vue"
import RoomCheckDetail from "./views/user/user-profile/RoomCheckDetail.vue"
import FileManagementIndex from "./views/file/FileManagementIndex.vue"
import AskForHelp from "./views/help/AskForHelp.vue"
import Setting from "./views/help/SettingPage.vue"
import BlankPage from "./components/BlankPage.vue"
import ExpiredLink from "./views/warn/ExpiredLink.vue"
import NotFound from "./views/warn/NotFound.vue"

const routes = [
    {name:"blank",path:"/blank",component:BlankPage,meta:{requireAuth:false}},
    {name:"user_login",path: "/user/login", component: UserLogin,meta:{requireAuth:false}},
    {name:"user_register",path:"/user/register",component:UserRegister,meta:{requireAuth:false}},
    {name:"request_password_reset",path:"/request_password_reset",component:RequestPasswordReset,meta:{requireAuth:false,phoneValidate:true}},
    {name:"password_reset_phone",path:"/password_reset/phone/:uid",component:PasswordReset,props:true,meta:{requireAuth:false}},
    {name:"password_reset_email",path:"/password_reset/email/:uid",component:PasswordReset,props:true,meta:{requireAuth:false}},
    {name:"user_profile",path:"/user/user_profile",component:UserProfile,meta:{requireAuth:true}},
    {name:"my_room_check",path:"/user/profile/my_room_check",component:MyRoomCheck,meta:{requireAuth:true}},
    {name:"room_check_detail",path:"/user/profile/room_check_detail/:meetingId",component:RoomCheckDetail,props:true,meta:{requireAuth:true}},
    {name:"online_meeting_schedule",path:"/meeting/online/online_meeting_schedule",component:OnlineMeetingSchedule,meta:{requireAuth:true}},
    {name:"room_info_query",path:"/meeting/offline/room_info_query",component:RoomInfoQuery,meta:{requireAuth:true}},
    {name:"room_day_detail",path:"/meeting/offline/room_day_detail",component:RoomDayDetail,meta:{requireAuth:true}},
    {name:"room_mon_detail",path:"/meeting/offline/room_mon_detail",component:RoomMonDetail,meta:{requireAuth:true}},
    {name:"file_management_index",path:"/file/file_management_index",component:FileManagementIndex,meta:{requireAuth:true}},
    {name:"help",path:"/help",component:AskForHelp,meta:{requireAuth:false}},
    {name:"setting",path:"/setting",component:Setting,meta:{requireAuth:true}},
    {name:"expired_link",path:"/expired_link",component:ExpiredLink,meta:{requireAuth:false}},
    {name:"not_found",path:"/not_found",component:NotFound,meta:{requireAuth:false}}

]
// 创建路由
const router = createRouter({
    history: createWebHistory(),
    routes
})
router.beforeEach((to,from,next)=>{
    if(to.path=="/user/login"){
        next();
    }else{
        if(to.meta.requireAuth){
            // console.log("this is"+store.state.token)
            if(sessionStorage.getItem("token")&&sessionStorage.getItem("token")!=""){
                next();
            }else{
                next({path:"/user/login"});
            }
        }else{
            next();
        }

    }
    // if(to.name=="active_password_reset"){
    //     var uid = to.path.split("/")[1];
    //     console.log(uid)
    //     if(uid.startsWith("$2a")){
    //         router.getRoutes.forEach((route)=>{
    //             router.addRoute(route);
    //         })
    //         next({...to,replace:true})
    //     }else{
    //         next();
    //     }
    // }else{
    //     next();
    // }
})
export default router