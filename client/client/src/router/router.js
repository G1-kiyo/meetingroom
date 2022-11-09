import {createRouter,createWebHistory} from "vue-router"
import UserLogin from "@/views/user/UserLogin.vue"
import PasswordReset from "@/views/user/PasswordReset.vue"
import RequestPasswordReset from "@/views/user/RequestPasswordReset.vue"
import UserRegister from "@/views/user/UserRegister.vue"
import UserProfile from "@/views/user/UserProfile.vue"
import OnlineConversationChoice from "@/views/meeting/online/OnlineConversationChoice.vue"
import OnlineVideoPattern from "@/views/meeting/online/OnlineVideoPattern.vue"
import OnlineScheduleDetail from "@/views/meeting/online/OnlineScheduleDetail.vue"
import OnlineGroupQuery from "@/views/meeting/online/OnlineGroupQuery.vue"
import RoomInfoQuery from "@/views/meeting/offline/RoomInfoQuery.vue"
import RoomDayDetail from "@/views/meeting/offline/RoomDayDetail.vue"
import RoomMonDetail from "@/views/meeting/offline/RoomMonDetail.vue"
import MyRoomCheck from "@/views/user/profile/MyRoomCheck.vue"
import RoomCheckDetail from "@/views/user/profile/RoomCheckDetail.vue"
import FileManagementIndex from "@/views/file/FileManagementIndex.vue"
import AskForHelp from "@/views/help/AskForHelp.vue"
import Setting from "@/views/help/SettingPage.vue"
import BlankPage from "@/views/warn/BlankPage.vue"
import ExpiredLink from "@/views/warn/ExpiredLink.vue"
import NotFound from "@/views/warn/NotFound.vue"
import { filterRoutes,formatDynamicRoutes } from "./DynamicRouter"
import store from "@/store"

// public routes
const routes = [
    {name:"blank",path:"/blank",component:BlankPage,meta:{requireAuth:false}},
    {name:"user_login",path: "/user/login", component: UserLogin,meta:{requireAuth:false}},
    {name:"user_register",path:"/user/register",component:UserRegister,meta:{requireAuth:false}},
    {name:"request_password_reset",path:"/request_password_reset",component:RequestPasswordReset,meta:{requireAuth:false,phoneValidate:true}},
    {name:"password_reset_phone",path:"/password_reset/phone/:uid",component:PasswordReset,props:true,meta:{requireAuth:false}},
    {name:"password_reset_email",path:"/password_reset/email/:uid",component:PasswordReset,props:true,meta:{requireAuth:false}},
    {name:"help",path:"/help",component:AskForHelp,meta:{requireAuth:false}},
    {name:"expired_link",path:"/expired_link",component:ExpiredLink,meta:{requireAuth:false}},
    {name:"not_found",path:"/not_found",component:NotFound,meta:{requireAuth:false}}

]
// 需要权限的路由表
const authRoutes = [
    {name:"user_profile",path:"/user/user_profile",component:UserProfile,meta:{requireAuth:true}},
    {name:"my_room_check",path:"/user/profile/my_room_check",component:MyRoomCheck,meta:{requireAuth:true}},
    {name:"room_check_detail",path:"/user/profile/room_check_detail/:meetingId",component:RoomCheckDetail,props:true,meta:{requireAuth:true}},
    {name:"online_conversation_choice",path:"/meeting/online/conversation_choice",component:OnlineConversationChoice,meta:{requireAuth:true}},
    {name:"online_video_pattern",path:"/meeting/online/video_pattern",component:OnlineVideoPattern,meta:{requireAuth:true}},
    {name:"online_schedule_detail",path:"/meeting/online/schedule_detail",component:OnlineScheduleDetail,meta:{requireAuth:true}},
    {name:"online_group_query",path:"/meeting/online/group_query",component:OnlineGroupQuery,meta:{requireAuth:true}},
    {name:"room_info_query",path:"/meeting/offline/room_info_query",component:RoomInfoQuery,meta:{requireAuth:true}},
    {name:"room_day_detail",path:"/meeting/offline/room_day_detail",component:RoomDayDetail,meta:{requireAuth:true}},
    {name:"room_mon_detail",path:"/meeting/offline/room_mon_detail",component:RoomMonDetail,meta:{requireAuth:true}},
    {name:"file_management_index",path:"/file/file_management_index",component:FileManagementIndex,meta:{requireAuth:true}},
    {name:"setting",path:"/setting",component:Setting,meta:{requireAuth:true}},
]
// 创建路由
const router = createRouter({
    history: createWebHistory(),
    routes
})
router.beforeEach(async(to,from,next)=>{
    if(to.path=="/user/login"){
        next();
    }else{
        // 判断是否需要权限
        if(to.meta.requireAuth){
            // console.log("this is"+store.state.token)
            // 判断是否登录，如无登录跳转到登录界面
            if(sessionStorage.getItem("token")&&sessionStorage.getItem("token")!=""){
                // 判断是否存在动态路由
                if(sessionStorage.getItem("auth")&&sessionStorage.getItem("routes")){
                    next();
                }else{
                    const userInfo = await store.dispatch("getUserInfo");
                    store.dispatch("getDynamicRoutes",{
                        authList:userInfo.data.authority
                    })
                }
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