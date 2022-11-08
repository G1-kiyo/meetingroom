import ClipboardJS from "clipboard"
import { ElMessage } from "element-plus";

const handleClipboardSuccess = function(e){
    ElMessage({
        type:"success",
        message:"copied successfully!"
    })
}
const handleClipboardFailure = function(e){
    ElMessage.error("fail to copy.")
}
const clipboard = function(selector,clip){
    if(typeof clip ==="string"){
        var clipboard = new ClipboardJS(selector,{
            text:function(){
                return clip;
            }
        })
    }else if(typeof clip ==="object"){
        var clipboard = new ClipboardJS(selector,{
            target:function(){
                return clip;
            }
        })
    }
    clipboard.on('success',function(e){
        handleClipboardSuccess(e);
        clipboard.destroy();
    })
    clipboard.on('error',function(e){
        handleClipboardFailure(e);
        clipboard.destroy();
    })
}

export default clipboard;