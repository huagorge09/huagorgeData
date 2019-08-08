var index = {
    userId: "",
    userAward: "",
    initPageData: function () { /*初始化页面数据*/
        var _self = this;
        this.userId = getUrlSearchParams("userid") ? getUrlSearchParams("userid") : decodeURI(localStorage.getItem("userid"));
        if (this.userId == null || this.userId == "" || this.userId == "null") {
            location.href = host + "/auth/proxy.html?scope=snsapi_userinfo&target_url=" + host + "/WeixinService/H5modules/H5Content/turntableActivity/index.html";
        } else {
            localStorage.setItem("userid", this.userId);
        }
        // this.queryUserInfo();
    },
    queryUserInfo:function() {

        if (!!userid) {  //如果有userid
            $.ajax({
                url: apiHost + "/api/wxuserinfo",
                async: false,
                type: "get",
                data: {
                    userId: userid
                },
                dataType: "json",
                success: function (res) {
                    //alert(userid)
                    if (res.success) {
                        userId = res.resp.userId;
                        localStorage.setItem("userId", userId);
                        localStorage.setItem("userid", userid);
                    } else {
                        //errorRemark("解密失败");
                        localStorage.removeItem("userId", userId);
                        localStorage.removeItem("userid", userid);
                        location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host + "/WeixinService/H5modules/H5Content/" + localStorage.getItem("target") + ".html";
                    }
                },
                error: function () {
                    errorRemark("接口获取userid服务器异常，请稍后再试。");
                }
            })
        }
    
    }

};
index.initPageData();





new scroll("#ceshi", {
    line: 1,
    timer: 1000,
    speed: 2000,
    hover: false
}, function (res) {
    console.log(res)
})

var lotteryStar = true;
$(function () {
    var timeOut = function () {  //超时函数
        $("#anli").rotate({
            angle: 0,
            duration: 10000,
            animateTo: 2160, //这里是设置请求超时后返回的角度，所以应该还是回到最原始的位置，2160是因为我要让它转6圈，就是360*6得来的
            callback: function () {
                alert('网络超时')
            }
        });
    };
    var rotateFunc = function (awards, angle, text) {  //awards:奖项，angle:奖项对应的角度
        $('#anli').stopRotate();
        $("#anli").rotate({
            angle: 0,
            duration: 5000,
            animateTo: angle + 1440, //angle是图片上各奖项对应的角度，1440是我要让指针旋转4圈。所以最后的结束的角度就是这样子^^
            callback: function () {
                // alert(text)
                if(text == 5){
                    $(".participate-in").show();
                    $(".oil-card, .set-meal,.verification-code").hide();
                }else if(text == 4){
                    $(".set-meal").show();
                    $(".oil-card, .participate-in,.verification-code").hide();
                }else if(text == 3){
                    $(".oil-card").show();
                    $(".set-meal, .participate-in,.verification-code").hide();
                }
                emptyInput();
                lotteryStar = true;
            }
        });
    };
    var address = true;
    $(document).ready(function(){
        $.ajax({
            async: true,
            url: config.service.getSubscribeState,
            data: {
                userId: userId
            },
            dataType: "json",
            type: "POST",
            success: function (data) {
                if(data.returnCode == -1){
                    location.href = host + "/auth/proxy.html?scope=snsapi_userinfo&target_url=" + host + "/WeixinService/H5modules/H5Content/turntableActivity/index.html";
                }else if(data.returnCode == 0){
                    address = data.data
                }
            },
            error: function () {
            }
        });
    });

    var userId = window.localStorage.getItem("userid");
    $(".lottery-star").click(function () {
        if(address){
            if(!activeTime()){
                wx.closeWindow()
            }else{
                if(lotteryStar){
                    $(".verification-code").show(); 
                }
                            
            }

        }else {
            $(".public-address").show();
        }
        
       
    });
    
    // 判断活动时间是否已过期
    $(document).ready(function(){
        if(!activeTime()){
            templateInformation(3);
        }
        // templateInformation(3);
    });
    // 领取奖品按钮
    $(".bont-btn").click(function(){
        templateInformation(2);

    });
    $(".btn-title").click(function(){
        templateInformation(1);
    });
    function templateInformation(data) {
        var targetMy = data;
        var tempType = `TEMPLATE_0${data}`
        $.ajax({
            async: true,
            url: config.service.sendTemplateMsg,
            data: {
                userId: userId,
                tempType: tempType
            },
            dataType: "json",
            type: "POST",
            success: function (data) {
                if(data.returnCode == '-1'){
                    location.href = host + "/auth/proxy.html?scope=snsapi_userinfo&target_url=" + host + "/WeixinService/H5modules/H5Content/turntableActivity/index.html";
                }
                // wx.closeWindow()
                if(!activeTime() ){
                    wx.closeWindow()
                    // WeixinJSBridge.call('closeWindow');
                }else if(targetMy==2){
                    wx.closeWindow()
                    // WeixinJSBridge.call('closeWindow');
                }else if(targetMy==1){
                    wx.closeWindow()
                    // WeixinJSBridge.call('closeWindow');
                }else if(targetMy==3){
                    wx.closeWindow()
                    // WeixinJSBridge.call('closeWindow');
                }
                
            },
            error: function () {
            }
        });

    }
    //验证字符串是否是数字
    function checkNumber(theObj) {
        var reg = /^[0-9]+.?[0-9]*$/;
        if (reg.test(theObj)) {
        return true;
        }
        return false;
    }
    // 点击参与抽奖按钮
   
    $(".button-btn").click(function () {
        var name = $("#name").val();
        var phone = $("#phone").val();
        var vrfCode = $("#vrfCode").val();
        if(name ==''){
            $(document).dialog({ type: 'notice', infoText: '姓名不能为空', autoClose: 1500, position: 'center' });
            return
        }
        if(phone == ''){
            $(document).dialog({ type: 'notice', infoText: '电话号码不能为空', autoClose: 1500, position: 'center' });
            return
        }
        if(vrfCode == ''){
            $(document).dialog({ type: 'notice', infoText: '验证码不能为空', autoClose: 1500, position: 'center' });
            return
        }
        if(!isMobile(phone)){
            $(document).dialog({ type: 'notice', infoText: '请输入正确的手机号码', autoClose: 1500, position: 'center' });
            return
        }
        if(!checkNumber(vrfCode)){
            $(document).dialog({ type: 'notice', infoText: '请输入正确的验证码', autoClose: 1500, position: 'center' });
            return
        }
        $.ajax({
        async: true,
        url: config.service.turnDraw,
        data: {
            telePhone: phone,
            smsCode: vrfCode,
            sessionID: sessionID,
            userId: userId,
            name: name,
            activityId: 'E83BF320190507E101D1ACD3E399D1CD'
        },
        dataType: "json",
        type: "POST",
        success: function (data) {
            if(data.returnCode == 0){
                $(".verification-code").hide();  
                lotteryStar = false;
                setTimeout(function(){ 
                    Rotate(data.data.award.awardSort)
                }, 800);               
            }else if(data.returnCode == 9996){
                $(document).dialog({ type: 'notice', infoText: '验证码错误', autoClose: 1500, position: 'center' });
                $("#vrfCode").val("");
            }else if(data.returnCode == -1){
                location.href = host + "/auth/proxy.html?scope=snsapi_userinfo&target_url=" + host + "/WeixinService/H5modules/H5Content/turntableActivity/index.html";
            }else if(data.returnCode == 2){
                // $(document).dialog({ type: 'notice', infoText: data.returnMsg, autoClose: 1500, position: 'center' });
                $(".participate-in").show();
                $(".set-meal, .verification-code,.oil-card").hide();
            }
            
        },
        error: function () {
        }
    });

    });
    function Rotate(data) {
        var data = data
        if (data == 4) {
            rotateFunc(4, -28, '4')
        }
        if (data == 2) {
            rotateFunc(2, 30, '2')
        }
        if (data == 5) {
            rotateFunc(5, 93, '5')
        }
        if (data == 3) {
            rotateFunc(3, 151, '3')
        }
        if (data == 1) {
            rotateFunc(1, 275, '1')
        }
    }
})


$(".close").click(function () {
    $(".verification-code").hide();
    emptyInput();
    lotteryStar = true;
});

function countDown(object) {  //验证码倒计时
    var elem = object.elem;
    var disable = object.disable;
    var time = parseInt(object.time);
    var txt = $(elem).text();
    if (!$(elem).hasClass(disable)) {
        $(elem).addClass(disable);
        timeDowm()
        var delay = setInterval(timeDowm, 1000);
        function timeDowm() {
            time--;
            $(elem).text(time + "s后重新发送");
            if (time < 1) {
                $(elem).text(txt);
                $(elem).removeClass(disable);
                clearInterval(delay);
            }
        }
    }
}

$(function(){
  $(".examination-box").click(function(){
    if (!$(".examination-box").hasClass("disabled")) {
        if($("#name").val()!=""){

                     var phone = $("#phone").val();
        if (phone == "") {
            $(document).dialog({ type: 'notice', infoText: '手机号码不能为空', autoClose: 1500, position: 'center' });
        }
        else if (!(/^1[34578]\d{9}$/.test(phone))) {
            $(document).dialog({ type: 'notice', infoText: '请输入正确的手机号码', autoClose: 1500, position: 'center' });
        } else {
            verification(phone); 
        }


        }else{
             $(document).dialog({ type: 'notice', infoText: '姓名不能为空', autoClose: 1500, position: 'center' });
        }
    }

  })
})

var sessionID = "";
function verification(phone) {
    $.ajax({
        async: true,
        url: config.service.getMobileVerifyCodeForHABX,
        data: {
            mobile: phone
        },
        dataType: "json",
        type: "POST",
        success: function (data) {
            if (data.returnCode == "0000") {
                countDown({
                    elem: ".examination-box",
                    disable: "disabled",
                    time: 60
                })
                sessionID = data.sessionID
            } else {
                $(document).dialog({ type: 'notice', infoText: '系统异常', autoClose: 1500, position: 'center' });
            }
        },
        error: function () {
        }
    });

}

// 查询已参加抽奖人数
$(document).ready(function () {
    $.ajax({
        async: true,
        url: config.service.queryAllWinningCount,
        data: {
            activityId: 'E83BF320190507E101D1ACD3E399D1CD'
        },
        dataType: "json",
        type: "POST",
        success: function (data) {
            $(".ginseng").text(data.data);
        },
        error: function () {
        }
    });
});
// 清空输入框内容
function emptyInput(){
    $("#name").val("");
    $("#phone").val("");
    $("#vrfCode").val("");
}



// 判断是否生产环境替换二维码
var srcDev="/WeixinWeb/WeixinWeb_Images/H5modules/turntableActivity/test_03.png";
var srcPrd="/WeixinWeb/WeixinWeb_Images/H5modules/turntableActivity/wxprodQrcode1_03.png";
if(host.indexOf("wxtest1")>-1){
    $("#codeImg").attr("src",srcDev)
}else{
    $("#codeImg").attr("src",srcPrd)
}