/**
 * Created by ex-chengh on 2018/7/12.
 */
var userId="";
var userid=getUrlSearchParams("userid")?getUrlSearchParams("userid"):localStorage.getItem("userid");
initPage()
function initPage(){
    queryUserInfo();
    userid=getUrlSearchParams("userid")?getUrlSearchParams("userid"):localStorage.getItem("userid");
    var host=window.location.protocol+"//"+window.location.host;
    if(!userid){  //判断用户有没有静默授权
        location.href=host+"/auth/proxy-silent.html?scope=snsapi_base&target_url="+host+"/WeixinService/H5modules/H5Content/registerCourtesy/index.html";
    }
    localStorage.setItem("target","proList");
}


/**
 * 查询用户信息
 */
function queryUserInfo(){
    if(!!userid){  //如果有userid
        $.ajax({
            url:apiHost + "/api/wxuserinfo",
            async:false,
            type:"get",
            data:{
                userId:userid
            },
            dataType:"json",
            success:function(res){
                if(res.success){
                    userId=res.resp.userId;
                    localStorage.setItem("userId",userId);
                    localStorage.setItem("userid",userid);
                }
            }
        })
    }
}
