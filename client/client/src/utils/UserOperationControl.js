
class UserOperationControl{
    // 防抖
    debounce(fn,delay){
        // 即使一开始设置了timer为null；
        // 但后续事件触发的真正函数是下面这个匿名函数
        // args接收的是与事件绑定的对象相关的属性或其他参数
        var timer = null;
        return function(...args){
            // 如果原有的timer存在，则销毁原有的timer
            if(timer){
                clearTimeout(timer);
            // 然后重新创建新的timer
            }else{
                timer = setTimeout(()=>{
                    // 如果这里不用箭头函数（本身没有this指向，会指向外一层函数），用function声明
                    // 需要在外一层函数先获取this，再放到这里
                    // args也一样
                    fn.apply(this,args);
                },delay)
            }
        }
    }
    // 节流函数
    throttle(fn,delay){
        var setTimer = true;
        return function(...args){
            if(!setTimer)return;
            setTimer = false;
            
            setTimeout(()=>{
                fn.apply(this,args);
                setTimer = true;
            },delay)
        }
    }
}
export default new UserOperationControl();