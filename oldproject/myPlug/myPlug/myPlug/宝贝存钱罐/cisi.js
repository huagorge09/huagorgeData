//屏幕适配
(function (doc, win) {
    var docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            var clientWidth = docEl.clientWidth;
            if(clientWidth > 1024){
                clientWidth = 1024;
            }
            if (!clientWidth) return;
            docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
        };
    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);

$(function () {
    // publicPage();
    footprintOperationNew();
});

var dashang,artId,bindId,insName,topicId,themeId,pigNum = 1,amount = 1;

function footprintOperationNew() {
    //话题
    $(".navigateTopic").unbind('click').on("click",function () {
        var topicId = $(this).attr("id");
        location.href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/topic.html?topicId="+topicId;
        return false;
    });
    //主题
    $(".navigateTheme").unbind('click').on("click",function () {
        var themeId = $(this).attr("id");
        location.href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/theme.html?themeId="+themeId;
        return false;
    });

    //打赏
    $(".dayItem .daShang").unbind('click').on("click",function () {
        artId = $(this).parent().parent().parent().attr("id");

        //红包参数
        bindId = $(this).find("#bindId").val();
        insName = $(this).find("#childName").val();

        $("#rewarZhubiBox .button").attr("id","");
        $("#rewarZhubiBox .button").unbind();
        $("#rewarHongbaoBox .button").attr("id","");
        $("#rewarHongbaoBox .button").unbind();
        if(!$(this).hasClass("ban")){
            dashang = $(this);
            dashang.removeClass("daShang-animation");
            $(".page").append("<div class='mask' id='rewarBoxMask'></div>");
            //初始化
            $(".rewardBox").removeClass('rewardBoxAnimation');
            $(".rewardBox").find(".one").addClass("current").siblings(".option").removeClass("current");
            $(".rewardBox").find("input").val("").siblings("delete").hide();
            $(".rewardBox").find(".inputDiv").hide();
            $("#rewarZhubiBox").find(".button").text("打赏1猪币");
            $("#rewarHongbaoBox").find(".tip").show();
            $("#rewarHongbaoBox").find(".button").text("打赏1元");
            $("#rewarZhubiBox .button").attr("id","artt_"+artId);
            $("#rewarHongbaoBox .button").attr("id","artthonbao_"+artId);

            $("#rewarZhubiBox").show().addClass('rewardBoxAnimation');

            //打赏按钮点击
            $("#rewarZhubiBox #artt_"+artId).on("click",function () {

                if(!$(this).hasClass("ban")){
                    $("#rewarZhubiBox").hide();
                    $("#rewarBoxMask").remove();

                    if(pigNum == '1' || pigNum == '5'){
                        pigNum = pigNum;
                    }else{
                        pigNum = $("#rewarZhubiBox").find("input").val();
                    }

                    if(null == pigNum || pigNum == ""){
                        common.autoHintBox({"content":"打赏猪币数为空"});
                        return false;
                    }

                    var param = {
                        'articleId':artId,
                        'pigpointNum':pigNum,
                        'content':'宝贝棒棒哒，奖励'+pigNum+'个小猪币！',
                        'rewardType':'01'
                    }

                    $.ajax({
                        url: '/SL_LEM/growthFootPrint/rewardPigPoint.do',
                        type: 'POST',
                        dataType: 'json',
                        async:false,
                        data:param,
                        error: function(){
                            $(this).alertFnOne({
                                title: '温馨提示',
                                content: '查询数据异常，请重试'
                            });
                        },
                        success: function(data){

                            if(data.errorCode == '0'){

                                if(dashang.hasClass("detail")){
                                    $("#daShang").html('<div class="title">打赏 （'+data.rewardList.length+'）</div><div class="pingLunList">'+daShang(data.rewardList)+'</div>');
                                }
                                $("#rewarBoxMask").remove();
                                dashang.text("打赏("+(Number(dashang.text().substring(3,dashang.text().length-1).replace(/[^0-9]/g,''))+1)+")").addClass("daShang-animation");
                                common.autoHintBox({"content":"打赏成功"});
                            }else{
                                common.autoHintBox({"content":data.message});
                            }
                        }
                    });
                }
            });


            //打赏红包按钮点击
            $("#rewarHongbaoBox #artthonbao_"+artId).on("click",function () {
                if(!$(this).hasClass("ban")){
                    $("#rewarHongbaoBox").hide();
                    $("#rewarBoxMask").remove();
                    if(amount == '1' || amount == '2'){
                        amount = amount;
                    }else{
                        amount = $("#rewarHongbaoBox").find("input").val();
                    }

                    var luckMoney = {
                        'persons' : bindId,
                        'sendType' : '1',
                        'amount' : amount,
                        'sendCount' : '1',
                        'luckMoneyClass' : '1',
                        'insName' : insName,
                        'luckAmount': amount,
                        'tarWxUserId' : bindId,
                        'entryUrl': window.location.href,
                        'remark':'聪明伶俐，快高长大！',
                        'bizKey':artId,
                        'bizType':'01',
                        'isApp':'Y',
                        'sourceLoginName':'WECHAT',
                        'isReward':"Y"//打赏
                    }

                    $.ajax({
                        url: '/SL_LEM/luckMoney/sendLuckMoney.do',
                        type: 'POST',
                        dataType: 'json',
                        async:false,
                        data:luckMoney,
                        error: function(){
                            $(this).alertFnOne({
                                title: '温馨提示',
                                content: '查询数据异常，请重试'
                            });
                        },
                        success: function(data){
                            if(data.succesFlag=='N'){
                                $(this).alertFnOne({
                                    title: '温馨提示',
                                    content: data.resultMessage
                                });
                            }else if(data.succesFlag=='Y'){
                                //赋值订单明细编号
                                LM_APPLY.init.setItemId(data.itemId);
                                var orderId=data.orderId;
                                var pay_prem = amount;
                                orderId_angin = orderId;
                                pay_prem_angin = pay_prem;
                                if(pay_prem != "0" && parseFloat(pay_prem) > 0){

                                    var callback = function(){
                                        luckMoney.shareOrderId = orderId;
                                        localStorage.setItem("grown_luckmoney",JSON.stringify(luckMoney));
                                        dashang.text("打赏("+(Number(dashang.text().substring(3,dashang.text().length-1).replace(/[^0-9]/g,''))+1)+")").addClass("daShang-animation");
                                        common.autoHintBox({"content":"打赏成功"});
                                    }

                                    var go_url = "";

                                    wechatPay("宝贝存钱罐",orderId,pay_prem,go_url,callback);


                                    if(dashang.hasClass("detail")){
                                        intiRewardInfo();
                                    }
                                    $("#rewarBoxMask").remove();
                                }
                            }else{
                                $(this).alertFnOne({
                                    title: '温馨提示',
                                    content: '系统发生错误，无结果返回'
                                });
                            }
                        }
                    });
                }
            });

            return false
        }
    });

    function intiRewardInfo(){
        $.ajax({
            url: '/SL_LEM/growthFootPrint/queryRewardList.do',
            type: 'POST',
            dataType: 'json',
            async:false,
            data:"articleId="+artId,
            error: function(){
                $(this).alertFnOne({
                    title: '温馨提示',
                    content: '查询数据异常，请重试'
                });
            },
            success: function(data){
                if(dashang.hasClass("detail")){
                    $("#daShang").html('<div class="title">打赏 （'+data.rewardList.length+'）</div><div class="pingLunList">'+daShang(data.rewardList)+'</div>');
                }
            }
        });
    }



    /**
     * 猪币
     */
    //金额选择
    $("#rewarZhubiBox .moneySelect .option").unbind('click').on("click",function () {
        pigNum = $(this).text();
        $(this).addClass("current").siblings().removeClass("current");
        $("#rewarZhubiBox .button").removeClass("ban");
        $("#rewarZhubiBox .tips").hide();
        $("#rewarZhubiBox").find(".inputDiv").css("border-color","transparent");
        if($(this).hasClass("three")){
            $("#rewarZhubiBox .button").text("打赏猪币").addClass("ban");
            $("#rewarZhubiBox .inputDiv").show().children("input").val("").siblings(".delete").hide();
        }else {
            $("#rewarZhubiBox .inputDiv").children("input").val("").siblings(".delete").hide();
            $("#rewarZhubiBox .inputDiv").hide();
            $("#rewarZhubiBox .button").text("打赏"+$(this).text()+"猪币").removeClass("ban");
        }
    });
    //猪币输入
    $("#rewarZhubiBox input").unbind('input').on("input",function () {
        var value = $(this).val().replace(/[^0-9]/g,'');
        $(this).val(value);
        $("#rewarZhubiBox .button").text("打赏"+value+"猪币");
        if(value.length>0){
            if(Number(value)>1523){
                $("#rewarZhubiBox .button").addClass("ban");
                $("#rewarZhubiBox .tips").show().text("您的猪币数只有1523哦~");
                $(this).parents(".inputDiv").css("border-color","#ff3f2b");
            }else {
                $("#rewarZhubiBox .button").removeClass("ban");
                $(this).parents(".inputDiv").css("border-color","transparent");
                $("#rewarZhubiBox .tips").hide();
            }
        }else {
            $("#rewarZhubiBox .button").addClass("ban");
            $("#rewarZhubiBox .delete").hide();
        }
    });
    //删除输入
    $("#rewarZhubiBox .delete").unbind('click').on("click",function () {
        $(this).siblings("input").val("").focus();
        $("#rewarZhubiBox .button").addClass("ban");
    });
    //关闭打赏弹窗
    $("#rewarZhubiBox .close").unbind('click').on("click",function () {
        $("#rewarZhubiBox").hide();
        $("#rewarBoxMask").remove();
    });

    //我想打赏红包
    $("#rewarZhubiBox .switch").unbind('click').on("click",function () {
        $("#tarWxUserId").val("");
        $("#insName").val("");
        $("#bizKey").val("");

        $("#rewarZhubiBox").removeClass('rewardBoxAnimation').hide();
        $('#rewarHongbaoBox').show().addClass('rewardBoxAnimation');
        $("#hbinsName").html(insName);
        $("#tarWxUserId").val(bindId);
        $("#insName").val(insName);
        $("#bizKey").val(artId);
    });

    /**
     * 红包
     */
    //金额选择
    $("#rewarHongbaoBox .moneySelect .option").unbind('click').on("click",function () {
        amount = $(this).children("span").text();
        $(this).addClass("current").siblings().removeClass("current");
        $("#rewarHongbaoBox .button").removeClass("ban");
        $("#rewarHongbaoBox .tips").hide();
        $("#rewarHongbaoBox").find(".inputDiv").css("border-color","transparent");
        if($(this).hasClass("three")){
            $("#rewarHongbaoBox .button").text("打赏红包").addClass("ban");
            $("#rewarHongbaoBox .inputDiv").show().children("input").val("").siblings(".delete").hide();
            $("#rewarHongbaoBox  .tip").hide();
        }else {
            $("#rewarHongbaoBox .button").text("打赏"+$(this).children("span").text()+"元").removeClass("ban");
            $("#rewarHongbaoBox .inputDiv").children("input").val("").siblings(".delete").hide();
            $("#rewarHongbaoBox .inputDiv").hide();
            $("#rewarHongbaoBox  .tip").show();
        }
    });
    //红包输入
    $("#rewarHongbaoBox input").unbind('input').on("input",function () {
        var value = common.moneyHandle($(this).val());
        $(this).val(value);
        $("#rewarHongbaoBox .button").text("打赏"+value+"元");
        if(value.length>0){
            if(Number(value)>=1){
                $("#rewarHongbaoBox .button").removeClass("ban");
                $("#rewarHongbaoBox .tips").hide();
                $(this).parents(".inputDiv").css("border-color","transparent");
                $("#rewarHongbaoBox  .tip").show();
            }else {
                $("#rewarHongbaoBox .button").addClass("ban");
                $("#rewarHongbaoBox .tips").show();
                $(this).parents(".inputDiv").css("border-color","#ff3f2b");
                $("#rewarHongbaoBox  .tip").hide();
            }
            $("#rewarHongbaoBox .delete").show();
        }else {
            $("#rewarHongbaoBox .button").text("打赏红包");
            $("#rewarHongbaoBox .button").addClass("ban");
            $("#rewarHongbaoBox .delete").hide();
            $("#rewarHongbaoBox  .tip").hide();
        }
    });
    //删除输入
    $("#rewarHongbaoBox .delete").unbind('click').on("click",function () {
        $(this).siblings("input").val("").focus();
        $("#rewarHongbaoBox .button").addClass("ban");
    });
    //关闭打赏弹窗
    $("#rewarHongbaoBox .close").unbind('click').on("click",function () {
        $("#rewarHongbaoBox").hide();
        $("#rewarBoxMask").remove();
    });

    //我想打赏猪币
    $("#rewarHongbaoBox .switch").unbind('click').on("click",function () {
        $("#rewarHongbaoBox").removeClass('rewardBoxAnimation').hide();
        $("#rewarZhubiBox").show().addClass('rewardBoxAnimation');
        console.log("aa");
    });
    //点击遮盖层关闭弹窗
    $(".page").on("touchend","#rewarBoxMask",function () {
        $(this).remove();
        $("#rewarZhubiBox").removeClass('rewardBoxAnimation').hide();
        $("#rewarHongbaoBox").removeClass('rewardBoxAnimation').hide();
    });

    //点赞
    $(".dayItem .dianZan").unbind('click').on("click",function (e) {
        artId = $(this).parent().parent().parent().attr("id");
        var dianzanObj = $(this);
        var is_dianzan;
        if(!$(this).hasClass("ban")){
            if($(this).hasClass("dianZanAction")){
                is_dianzan = "02";
            }else {
                is_dianzan = "01";
            }

            $.ajax({
                url: '/SL_LEM/growthFootPrint/operateCollect.do',
                type: 'POST',
                dataType: 'json',
                async:false,
                data:"articleId="+artId+"&operateType="+is_dianzan,
                error: function(){
                    $(this).alertFnOne({
                        title: '温馨提示',
                        content: '查询数据异常，请重试'
                    });
                },
                success: function(data){
                    if(data.errorCode == '0'){
                        if(is_dianzan == "01"){
                            if(dianzanObj.hasClass('detail')){
                                $("#dianZan").html('<div class="title">点赞 （'+data.likeList.length+'）</div><div class="content"><div class="dianZanList clearFloat">'+dianzan(data.likeList)+'</div></div>');
                            }

                            dianzanObj.text("点赞("+(Number(dianzanObj.text().substring(3,dianzanObj.text().length-1).replace(/[^0-9]/g,''))+1)+")");
                            dianzanObj.addClass("dianZanAction dianZan-animation");
                        }else{
                            var resNum = Number(dianzanObj.text().substring(3,dianzanObj.text().length-1).replace(/[^0-9]/g,''));
                            if(resNum > 0){
                                resNum = resNum - 1;
                            }else{
                                resNum = 0;
                            }

                            if(dianzanObj.hasClass('detail')){
                                $("#dianZan").html('<div class="title">点赞 （'+data.likeList.length+'）</div><div class="content"><div class="dianZanList clearFloat">'+dianzan(data.likeList)+'</div></div>');
                            }

                            dianzanObj.text("点赞("+resNum+")");
                            dianzanObj.removeClass("dianZanAction dianZan-animation");
                            common.autoHintBox({content:"已取消"});
                        }

                    }else{
                        common.autoHintBox({"content":data.message});
                    }
                }
            });
            return false
        }
    });
}

//href="/SL_LEM/grown/growthFootPrint/footprintHomePage.html?userId='+dsInfo[i].userId+'"
//callComment
//打赏
function daShang(dsInfo){
    var dsHtml = "";
    for ( var i = 0; i < dsInfo.length; i++) {
        dsHtml += '<div class="item">'+
            '<a class="head">'+
            '<img src="'+dsInfo[i].headImgUrl+'">'+
            '</a>'+
            '<div class="content">'+
            '<div class="cenZhu">'+
            '<a class="name">'+dsInfo[i].nickName+'</a>'+
            '<span class="tuHao">'+tuhao(dsInfo[i])+'</span>'+
            '<span class="time">'+dsInfo[i].operateTime.substring(0,dsInfo[i].operateTime.length - 3)+'</span>'+
            '</div>'+
            '<div class="text">'+dsInfo[i].content+'</div>'+
            '</div>'+
            '</div>';
    }

    return dsHtml;
}

//土豪
function tuhao(dsInfo){
    if(dsInfo.rewardType == '01'){
        return "土豪";
    }else{
        return "";
    }
}

//href="/SL_LEM/grown/growthFootPrint/footprintHomePage.html?userId='+dzInfo[i].userId+'"
//点赞信息
function dianzan(dzInfo){
    var dzHtml = "";
    for ( var i = 0; i < dzInfo.length; i++) {
        dzHtml += '<a class="item"><img src="'+dzInfo[i].headImgUrl+'"></a>';
    }

    return dzHtml;
}

//页面交互
var publicPage=function () {

    var playerButton;//当前播放按钮
    var storageAudio;//缓存音频
    if($(".circleAudioPlayer").length>0){
        var audioCanvas = $(".circleAudioPlayer").find("canvas").get(0);
        var audioCanvasCtx = audioCanvas.getContext("2d");
        audioCanvas.width = $(audioCanvas).width();
        audioCanvas.height = $(audioCanvas).height();
    }

    publicPreviewPicture();//图片预览

    publicPageScroll();//页面滑动

    publicToTop();//回到顶部

    publicAudio();//音频播放

    common.dayItemLoad();//处理足迹内容不能超过六行，超过六行显示展开，内容更新后需再次调用

    //禁止mask滑动
    $(".page").on("touchmove",".mask",function (e) {
        e.preventDefault();
        return false
    });
    //离开页面时关闭音乐
    if ("onpagehide" in window) {
        window.addEventListener("pageshow", pageshow, false);
        window.addEventListener("pagehide", pagehide, false);
    } else {
        window.addEventListener("load", pagehide, false);
        window.addEventListener("unload", pageshow, false);
    }
    function pageshow() {
        if(storageAudio){
            storageAudio.currentTime = Math.floor(Number(sessionStorage.getItem("storageAudioTime")));
        }
    }
    function pagehide() {
        //页面关闭时关闭当前播放的音频，缓存当前播放的音频
        if(storageAudio){
            sessionStorage.setItem("storageAudioUrl",storageAudio.src);
            sessionStorage.setItem("storageAudioTime",storageAudio.currentTime);
        }
        if($(".record").find(".play").length>0){
            playerButton.removeClass("play");
            playerButton.parents(".record").find("audio").get(0).pause();
            sessionStorage.setItem("storageAudioUrl",playerButton.parents(".record").find("audio").attr('src'));
            sessionStorage.setItem("storageAudioTime",playerButton.parents(".record").find("audio").get(0).currentTime);
        }
    }

    function publicPreviewPicture() {
        $(".picture img").unbind('click').on("click",function () {
            var currentUrl = $(this).attr("src");//当前图片
            var urlList=[];//预览图片列表
            $(this).parents(".picture").find("img").each(function () {
                var obj = {'src':$(this).attr("src"),'picNo':$(this).attr("picNo")};
                urlList.push(obj);
            });
            common.previewPicture({
                currentUrl:currentUrl,
                urlList:urlList
            });
            return false
        });
    }

    function publicPageScroll() {
        var initTop = document.body.scrollTop || document.documentElement.scrollTop;
        var delayed;
        window.addEventListener("scroll",function (e) {
            pageUp();
            audioSuspend();
        });
        //处理回到顶部按钮
        function pageUp() {
            //处理回到顶部按钮状态
            var endTop = document.body.scrollTop || document.documentElement.scrollTop;
            var delta = endTop - initTop;
            clearTimeout(delayed);
            if(delta < 0){
                $("#pageUp").addClass("pageUp-action");
            }else {
                $("#pageUp").removeClass("pageUp-action");
            }
            delayed = setTimeout(function () {
                $("#pageUp").removeClass("pageUp-action");
            },1000);
            initTop = endTop;
        }
        //音频悬浮
        function audioSuspend() {
            if(playerButton && playerButton.hasClass("play")){
                var top = $('.record').find(".play").offset().top;
                if((initTop > top) || (initTop+window.innerHeight < top)){
                    $(".circleAudioPlayer").show().find(".button").addClass("play");
                }else {
                    $(".circleAudioPlayer").hide().siblings(".close").hide();
                }
            }
        }
        //悬浮播放按钮
        $(".circleAudioPlayer .button").unbind('click').on("click",function () {
            if(storageAudio){
                if($(this).hasClass("play")){
                    storageAudio.pause();
                    $(this).removeClass("play").siblings(".close").show();
                }else {
                    storageAudio.play();
                    $(this).addClass("play").siblings(".close").hide();
                }
            }else {
                var audio = playerButton.parents(".record").find("audio").get(0);
                if(playerButton && playerButton.hasClass("play")){
                    audio.pause();
                    playerButton.removeClass("play");
                    $(this).removeClass("play").siblings(".close").show();
                }else {
                    audio.play();
                    playerButton.addClass("play");
                    $(this).addClass("play").siblings(".close").hide();
                }
            }
        });
        //关闭悬浮播放按钮
        $(".circleAudioPlayer .close").unbind('click').on("click",function () {
            $(this).hide();
            $(".circleAudioPlayer").hide();
            storageAudio = null;
            sessionStorage.removeItem("storageAudioUrl");
            sessionStorage.removeItem("storageAudioTime");
        })
    }

    function publicToTop() {
        $("#pageUp").on("click",function () {
            document.body.scrollTop = document.documentElement.scrollTop = 0;
        });
    }

    function publicAudio() {
        audioLoad();//音频加载
        audioPlay();//音频播放
        audioSlide();//音频滑动
        $(".BarAudioPlayer").on("click",function () {
            return false
        })
    }

    function audioLoad() {
        $(".record audio").each(function () {
            $(this).get(0).load();
            $(this).get(0).onloadedmetadata = function () {
                $(this).siblings(".BarAudioPlayer").find(".endTime").text(common.sec_to_time(Math.floor(this.duration)));
            }
        });
        //播放中的音频跳页之后继续播放
        if(sessionStorage.getItem("storageAudioUrl")){
            storageAudio = document.createElement("audio");
            storageAudio.id = "storageAudio";
            storageAudio.src = sessionStorage.getItem("storageAudioUrl");
            storageAudio.autoplay = "autoplay";
            storageAudio.preload = "preload";
            document.body.appendChild(storageAudio);
            storageAudio.currentTime = Number(sessionStorage.getItem("storageAudioTime"));
            storageAudio.play();
            //必须在微信Weixin JSAPI的WeixinJSBridgeReady才能生效
            document.addEventListener("WeixinJSBridgeReady", function () {
                storageAudio.play();
            }, false);
            $(".circleAudioPlayer").show().find(".button").addClass("play");
            var flag = true;
            storageAudio.ontimeupdate = function () {
                if(flag){
                    flag = false;
                    storageAudio.currentTime = Number(sessionStorage.getItem("storageAudioTime"));
                }
                var currentPlayTime = Math.floor(storageAudio.currentTime);
                var angle = currentPlayTime/storageAudio.duration >= 1 ? 1 : currentPlayTime/storageAudio.duration;
                audioCanvasCtx.clearRect(0,0,audioCanvas.width,audioCanvas.height);
                audioCanvasCtx.beginPath();
                audioCanvasCtx.strokeStyle = "#ffdc4e";
                audioCanvasCtx.lineWidth = 1;
                audioCanvasCtx.arc(audioCanvas.width/2,audioCanvas.height/2,audioCanvas.width/2-2,-0.5*Math.PI,-0.5*Math.PI+angle*2*Math.PI);
                audioCanvasCtx.stroke();
            };
            storageAudio.onended = function () {
                storageAudio = null;
                sessionStorage.removeItem("storageAudioUrl");
                sessionStorage.removeItem("storageAudioTime");
                $(".circleAudioPlayer").hide().siblings(".close").hide();
            };
            storageAudio.oncanplay = function () {
                storageAudio.play();
            }
        }
    }
    function audioPlay() {
        var audio;
        $(".record .button").on("click",function () {
            if(audio){
                audio.ontimeupdate = null;
            }
            if(storageAudio){
                storageAudio.pause();
                storageAudio.ontimeupdate = null;
                storageAudio = null;
                sessionStorage.removeItem("storageAudioUrl");
                sessionStorage.removeItem("storageAudioTime");
            }
            audio = $(this).parents(".record").find("audio").get(0);
            playerButton = $(this);
            var width = $(this).parents(".scheduleBar").width();
            //点击的是当前播放的
            if($(this).hasClass("play")){
                $(".circleAudioPlayer .button").removeClass("play").siblings(".close").show();
                if(audio.paused){
                    audio.play();
                    playerButton.addClass("play");
                }else {
                    audio.pause();
                    playerButton.removeClass("play");
                }
            }else {
                var play = $('.record').find(".play");
                if(play.length > 0){
                    var currentAudio = play.parents(".record").find("audio").get(0);
                    currentAudio.pause();
                    play.removeClass("play");
                }
                audio.play();
                $(".circleAudioPlayer .button").addClass("play").siblings(".close").hide();
                playerButton.addClass("play");
            }
            //头像播放器进度
            var canvas,canvasCtx;
            if(playerButton.parents(".record").hasClass("headAudioPlayer")){
                var $canvas = playerButton.siblings("canvas");
                canvas = $canvas.get(0);
                canvasCtx = canvas.getContext("2d");
                canvas.width = $canvas.width();
                canvas.height = $canvas.height();
            }
            audio.ontimeupdate = function () {
                var currentPlayTime = Math.floor(audio.currentTime);
                var angle = currentPlayTime/audio.duration >= 1 ? 1 : currentPlayTime/audio.duration;
                audioCanvasCtx.clearRect(0,0,audioCanvas.width,audioCanvas.height);
                audioCanvasCtx.beginPath();
                audioCanvasCtx.strokeStyle = "#ffdc4e";
                audioCanvasCtx.lineWidth = 1;
                audioCanvasCtx.arc(audioCanvas.width/2,audioCanvas.height/2,audioCanvas.width/2-2,-0.5*Math.PI,-0.5*Math.PI+angle*2*Math.PI);
                audioCanvasCtx.stroke();
                //头像播放器
                if(playerButton.parents(".record").hasClass("headAudioPlayer")){
                    var angle2 = currentPlayTime/audio.duration >= 1 ? 1 : currentPlayTime/audio.duration;
                    canvasCtx.clearRect(0,0,canvas.width,canvas.height);
                    canvasCtx.beginPath();
                    canvasCtx.strokeStyle = "#feda46";
                    canvasCtx.lineWidth = 2;
                    canvasCtx.arc(canvas.width/2,canvas.height/2,canvas.width/2-2,-0.5*Math.PI,-0.5*Math.PI+angle2*2*Math.PI);
                    canvasCtx.stroke();
                }
                //条形播放器
                else {
                    var left = (currentPlayTime/audio.duration >= 1 ? 1 : currentPlayTime/audio.duration)*width;
                    $(audio).siblings(".BarAudioPlayer").find(".currentTime").text(common.sec_to_time(currentPlayTime));
                    playerButton.css("left",left).siblings(".currentSchedule").css("width",left);
                }
            };
            audio.onpause = function () {
                console.log("暂停");
            };
            audio.onended = function () {
                playerButton.removeClass("play").css("left",0);
                playerButton.siblings(".currentSchedule").css("width",0);
                playerButton.parents(".record").find(".currentTime").text("00:00");
                $(".circleAudioPlayer").hide().siblings(".close").hide();
            };
            return false
        });
    }
    function audioSlide() {
        var that,startLeft,slideAudio,width,startX;
        //按钮滑动
        $(".BarAudioPlayer .button").on("touchstart",function (e) {
            that = $(this);
            startLeft = Number(that.css("left").substring(0,that.css("left").length-2));
            slideAudio = that.parents(".BarAudioPlayer").siblings("audio").get(0);
            width = that.parents(".scheduleBar").width();
            startX = e.originalEvent.targetTouches[0].pageX;
            that.on("touchmove",move);
            that.on("touchend",function () {
                that.off("touchmove",move);
            });
        });
        function move(e) {
            var moveX = e.originalEvent.targetTouches[0].pageX;
            var left = startLeft + moveX - startX;
            if (left <= 0) {
                left = 0;
            } else if (left >= width) {
                left = width;
            }
            that.css("left",left).siblings(".currentSchedule").css("width",left);
            var currentTime = Math.ceil(left/width*(Math.floor(slideAudio.duration)));
            slideAudio.currentTime = currentTime;
            that.parents(".scheduleBar").siblings(".currentTime").text(common.sec_to_time(currentTime));
            return false;
        }
        //点击滑动
        $(".scheduleBar").on("click",function (e) {
            var left=e.originalEvent.offsetX;
            var width = $(this).width();
            var slideAudio = $(this).parents(".record").find("audio").get(0);
            $(this).find(".button").css("left",left).siblings(".currentSchedule").css("width",left);
            var currentTime = Math.ceil(left/width*(Math.floor(slideAudio.duration)));
            slideAudio.currentTime = currentTime;
            $(this).siblings(".currentTime").text(common.sec_to_time(currentTime));
        })
    }
};






