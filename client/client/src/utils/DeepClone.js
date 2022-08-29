import _ from "lodash";
const deepClone = function(obj,method){
    var newObj = {};
    switch(method){
        // 不能拷贝function
        case "json":newObj = JSON.parse(JSON.stringify(obj));break;
        // 不能拷贝嵌套的obj
        case "assign":newObj = Object.assign({},obj);break;
        case "spread":newObj = {...obj};break;
        // 这个挺好的
        case "lodash":newObj = _.cloneDeep(obj);break;
        // 相对比较完善
        case "deepClone":newObj = selfDeepClone(obj);break;
        default:break;
    }

}
function selfDeepClone(obj){
    if(obj===null||typeof obj!=='object')return obj;
    var newObj;
    // 关于日期拷贝
    if(obj instanceof Date){
        newObj = new Date(obj.getTime());
        return newObj;
    }
    // 关于数组拷贝
    if(obj instanceof Array){
        for(let i=0;i<obj.length;i++){
            newObj[i] = selfDeepClone(obj[i])
        }
        return newObj;
    }
    // 关于对象拷贝
    if(obj instanceof Object){
        for(var key in obj){
            // 过滤原型链上其他对象属性
            if(obj.hasOwnProperty(key)){
                newObj[key] = selfDeepClone(obj[key])
            }
        }
        return newObj;
    }
}

export default deepClone;