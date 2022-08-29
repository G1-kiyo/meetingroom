import axios from "axios";
import { ElMessage, ElLoading } from "element-plus";



// 创建axios实例,基本配置
const service = axios.create({
    baseURL: "http://localhost:8080/",
    headers: {
        'Content-Type': 'application/json'
    },
    timeout: 15000
})



// Add a request interceptor
service.interceptors.request.use(function (config) {
    // Do something before request is sent
    // 在请求时附上JWTtoken
    // console.log("check"+store.state.token)
    if(sessionStorage.getItem("token")!=""&&sessionStorage.getItem("token")!=null){
        config.headers = {
            Authorization:"Bearer "+sessionStorage.getItem("token")
        }
    }
    
    return config;
}, function (error) {
    // Do something with request error
    return Promise.reject(error);
});

// Add a response interceptor
// 若后端对特定的状态码有相应中文或特定的解释，可以后续再另外说明(switch)
service.interceptors.response.use(function (response) {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    // ElMessage(response.statusText);
    // console.log(response);
    if (response.status == 200) {
        ElMessage({
            message: response.statusText==""?response.data.message:response.statusText,
            type: 'success'
        })
        
        // 可以用storage或cookie保存token
        // if(response.data.data.token){
        //     Cookies.set("token",response.data.data.token);
        // }
    } else {
        ElMessage({
            message: response.statusText==""?response.data.message:response.statusText,
            type: 'warning'
        })
    }
    
    return response;
}, function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    console.log(error);
    ElMessage.error(error.response.statusText==""?error.response.data.message:error.response.statusText);
    return Promise.reject(error);
});

function request(url, method,config,loading,) {
    let loadingInstance;
    if (loading) loadingInstance = ElLoading.service({ text: "loading,please wait for a moment." });

    return new Promise((resolve, reject) => {
        service({
            url: url,
            method: method,
            // 展开运算符
            ...config
        }).then((res) => {
            // 在导入request方法后可利用promise的then或者catch或finally进行下一步的功能扩展，
            // 比如数据类型转换
            if (res.status == 200) {
                resolve(res.data);
            } else {
                reject(new Error("Something wrong happened which caused an unsuccessful request!"));
            }
        }).catch((error) => {
            reject(error);
        }).finally(() => {
            if(loading)loadingInstance.close();
        })
    })

}

const AxiosHttp = {
    get: function getRequest(url, config, loading) {
        return request(url, 'get', config, loading);
    },
    post: function posttRequest(url, config, loading) {
        return request(url, 'post', config, loading);
    }
}

export default AxiosHttp;