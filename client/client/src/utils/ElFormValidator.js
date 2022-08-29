

class ElFormValidator {
    // 验证手机号码
    validatePhone(rule, value, callback) {
        if (/^(13[0-9]|14[5|7]|15[0-9]|18[0|1|2|3|5|6|7|8|9]\d{8}$)/.test(value)) {
            callback()
        } else {
            callback(new Error("Incorrect phone number format."))
        }
    }
    // 验证邮箱
    validateEmail(rule, value, callback) {
        if (/^\w+([+-.]\w)*@\w+([-.]\w)*\.\w+([-.]\w)*$/.test(value)) {
            callback()
        } else {
            callback(new Error("Incorrect email format."))
        }
    }
    // 验证密码
    validatePwd(rule,value,callback){
        if(/^(?=.*\d)(?=.*[A-Z])(?=.*[a-z])[0-9A-Za-z]+$/.test(value)){
            callback()
        }else{
            callback(new Error("The password must include uppercase and lowercase letters,and number."))
        }
    }
    // 验证整个表单
    validateForm(form) {
        // const form = this.$refs[formname];
        // console.log(form);
        return new Promise((resolve, reject) => {
            if (!form) reject("The form is not found!");
            form.validate((valid) => {
                if (valid) {
                    resolve()
                } else {
                    reject("Valid failed!")
                }
            })
        })

    }
    // 重置整个表单
    resetForm(form){
        return new Promise((resolve,reject)=>{
            if(!form)reject("The form is not found!");
            form.resetFields();
            resolve()
        })
    }
}
const elFormValidator = new ElFormValidator()
export default elFormValidator