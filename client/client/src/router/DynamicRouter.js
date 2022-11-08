import router from "@/router/router";
import { ElStep } from "element-plus";
import {h,resolveComponent} from "vue";


export const filterRoutes = function(allRoutes,authList){
    var matchedRoutes = [];
    for(let auth of authList){
        for(let key in allRoutes){
            if(allRoutes[key].auth==auth){
                matchedRoutes.push(allRoutes[key]);
            }
        }
    }
    return matchedRoutes;
}

export const formatDynamicRoutes = function(route,routes){
    //每个route的cid、path、name、auth、view、groupName、extends
    var formatRoutes = [];
    var routeObj = {};
    routesOptions = ["name","path","component","props","children"];
    for(let key in route){
        if(key.indexOf(routesOptions)>0){
            routeObj.key = route.key
        }
    }
    route.meta = {};
    route.meta.requireAuth = true;
    if(route.view){
        routeObj.component = ()=>{import(/*webpackChunkName: [request]*/`@/views/${route.path}`)};
    }else{
        routeObj.component = ()=>{h(resolveComponent("router-view"))};
    }
    if(route.hasChildren){
        let childRoutes = routes.filter((item)=>{
            return item.parentId==route.cid;
        })
        if(childRoutes.length!=0){
            childRoutes = childRoutes.map((childRoute)=>{
                return formatDynamicRoutes(childRoute,routes)
            })
            routeObj.children = childRoutes;
        }

    }
    return routeObj;
}

