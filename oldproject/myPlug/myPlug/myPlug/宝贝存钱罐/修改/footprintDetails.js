var articleId = getUrlParam("articleId");
var viewId = localStorage.getItem('currentViewId');//首页选中罐子
var is_jhApply = true;//防止重复申请精华
var efsDiscussId;//回复id

$(function () {
    page();
});
//页面交互
var page = function () {

    _paramData.shareType = "newFootPrintShare";//分享类型
    _paramData.needRw = "Y";//分享类型：Y:需要重写分享基础类
    _paramData.bizType = "";//业务类型
    _paramData.bizKey = "";//业务值
    _paramData.articleId = articleId;//帖子id
    SHARE.COMM.share();

    initData();//初始化数据

    function initData(){
        $.ajax({
            url: '/SL_LEM/growthFootPrint/footPrintDetail.do',
            type: 'POST',
            dataType: 'json',
            async:false,
            data:"viewId="+viewId+"&articleId="+articleId,
            error: function(){
                $(this).alertFnOne({
                    title: '温馨提示',
                    content: '查询数据异常，请重试'
                });
            },
            success: function(data){
                if(data.errorCode == "N"){
                    $(this).alertFnOne({
                        title: '温馨提示',
                        content: '此足迹已被删除！'
                    });
                }else{
                    appendHtml(data);
                }
            }
        });
    }

    function appendHtml(artInfo){
        $("#arttext").html(artInfo.articleInfo.articleContent);
        $("#artCread").html(artInfo.articleInfo.releaseDate.substring(0,16));
        $(".wrap .user").find(".head").attr("src",artInfo.articleInfo.wxHeadPic);
        $(".wrap .user").find(".name").html(artInfo.articleInfo.nickName);
        $(".bottom .daShang").html('打赏('+artInfo.rewardList.length+')<input type="hidden" id="bindId" value="'+artInfo.articleInfo.bindUserId+'"/><input type="hidden" id="childName" value="'+artInfo.articleInfo.insName+'"/>');
        $(".bottom .pingLun").html('评论('+artInfo.discussList.length+')');
        $(".bottom .dianZan").html('点赞('+artInfo.likeList.length+')');
        //$(".daShang").find("#bindId").val(artInfo.articleInfo.bindUserId);
        //$(".daShang").find("#childName").val(artInfo.articleInfo.insName);
        $(".dayItem").attr("id",articleId);
        $(".dayItem .head").attr("id",artInfo.articleInfo.userId);
        $(".dayItem .more").attr("href","/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintHomePage.html?userId="+artInfo.articleInfo.userId);
        if(artInfo.articleInfo.isLike == 'Y'){
            $(".bottom .dianZan").addClass("dianZanAction dianZan-animation");
        }

        if(artInfo.articleInfo.isFire != 'JH'){
            $(".fabulous").hide();
        }

        //主角信息
        if(artInfo.articleInfo.childList.length > 0){
            $(".childrenWrap").html(childInfo(artInfo.articleInfo));
        }

        //图片
        if(artInfo.articleInfo.articlePicList.length > 0){
            $(".picture").html(picInfo(artInfo.articleInfo));
        }

        //录音
        if(null != artInfo.articleInfo.voiceId && artInfo.articleInfo.voiceId != ""){
            $(".record").html(voiceInfo(artInfo.articleInfo));
        }

        //主题，话题
        themeAct(artInfo.articleInfo);

        //点赞
        if(artInfo.likeList.length > 0){
            $("#dianZan").html('<div class="title">点赞('+artInfo.likeList.length+')</div><div class="content"><div class="dianZanList clearFloat">'+dianzan(artInfo.likeList)+'</div></div>');
        }else{
            $("#dianZan").html('<div class="title">点赞(0)</div>');
        }

        //评论
        if(artInfo.discussList.length > 0){
            $("#pingLun").html('<div class="title">评论('+artInfo.discussList.length+')</div><div class="content">'+pingLun(artInfo.discussList)+'</div>');
        }else{
            $("#pingLun").html('<div class="title">评论(0)</div>');
        }

        //打赏
        if(artInfo.rewardList.length > 0){
            $("#daShang").html('<div class="title">打赏('+artInfo.rewardList.length+')</div><div class="pingLunList">'+daShang(artInfo.rewardList)+'</div>');
        }else{
            $("#daShang").html('<div class="title">打赏(0)</div>');
        }


        publicPage();
    }

    //href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintHomePage.html?userId='+dsInfo[i].userId+'"

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
                '<div class="text callComment">'+dsInfo[i].content+'</div>'+
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

    //评论
    function pingLun(plInfo){
        var plHtml = "";
        for ( var i = 0; i < plInfo.length; i++) {
            var nickRoom = "";
            if(null != plInfo[i].efsDiscussId && plInfo[i].efsDiscussId != ""){
                nickRoom = '<a class="name" id="dis_'+plInfo[i].discussId+'" >'+plInfo[i].nickName+'</a>回复<a class="name" >'+plInfo[i].efsNickName+'</a>';
            }else{
                nickRoom = '<a class="name" id="dis_'+plInfo[i].discussId+'" >'+plInfo[i].nickName+'</a>';
            }

            plHtml += '<div class="pingLunList" id="'+plInfo[i].discussId+'">'+
                '<div class="item">'+
                '<a class="head" >'+
                '<img src="'+plInfo[i].headImgUrl+'">'+
                '</a>'+
                '<div class="content">'+
                '<div class="cenZhu">'+nickRoom+'&nbsp;&nbsp;&nbsp;&nbsp;<span class="time">'+plInfo[i].discussTime.substring(0,plInfo[i].discussTime.length - 3)+'</span>'+
                '</div>'+
                '<div class="text callComment">'+plInfo[i].discussContent+'</div>'+
                '</div>'+
                '</div>'+
                '</div>';
        }

        return plHtml;
    }

    //href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintHomePage.html?userId='+dzInfo[i].userId+'"

    //点赞信息
    function dianzan(dzInfo){
        var dzHtml = "";
        for ( var i = 0; i < dzInfo.length; i++) {
            dzHtml += '<a class="item" ><img src="'+dzInfo[i].headImgUrl+'"></a>';
        }

        return dzHtml;
    }


    //主题、话题
    function themeAct(avtInfo){
        var html = "";
        if((null != avtInfo.themeId && avtInfo.themeId != "") && (null != avtInfo.activityId && avtInfo.activityId != "")){
            html =  '<span id='+avtInfo.activityId+' class="text navigateTopic">#<span class="tape"></span>'+avtInfo.activityName+'#</span>'+
                '<span id='+avtInfo.themeId+' class="text navigateTheme">#'+avtInfo.themeName+'#</span>';
        }else if(null != avtInfo.activityId && avtInfo.activityId != ""){
            html = '<span id='+avtInfo.activityId+' class="text navigateTopic">#<span class="tape"></span>'+avtInfo.activityName+'#</span>';
        }else if(null != avtInfo.themeId && avtInfo.themeId != ""){
            html = '<span id='+avtInfo.themeid+' class="text navigateTheme">#'+avtInfo.themeName+'#</span>';
        }else {
            return "";
        }

        $(".topic-theme").html(html);
    }

    //录音
    function voiceInfo(voiceInfo){
        if(null != voiceInfo.voiceId && voiceInfo.voiceId != ""){
            return  '<audio src="'+voiceInfo.voiceUrl+'"></audio>'+
                '<div class="BarAudioPlayer">'+
                '<div class="currentTime">00:00</div>'+
                '<div class="scheduleBar">'+
                '<div class="currentSchedule"></div>'+
                '<div class="button"></div>'+
                '</div>'+
                '<div class="endTime">00:00</div>'+
                '</div>';
        }else{
            return "";
        }
    }

    //图片信息
    function picInfo(picInfo){
        if(picInfo.articlePicList.length > 0){
            var picHtml = "";
            var pictureImgLoad;
            //一张图片需单独处理
            if(picInfo.articlePicList.length == 1){
                picHtml += '<div class="picture singlePicture clearFloat">';
                pictureImgLoad = "";
            }else {
                picHtml += '<div class="picture clearFloat">';
                pictureImgLoad = "common.pictureImgLoad(this)";
            }
            for ( var i = 0; i < picInfo.articlePicList.length; i++) {
                picHtml += '<div class="imgWrap"><img src="'+picInfo.articlePicList[i].smallPicUrl+'" data-original="'+picInfo.articlePicList[i].smallPicUrl+'" onload="'+pictureImgLoad+'" picNo="'+picInfo.articlePicList[i].picNo+'" ></div>';
            }
            picHtml +="</div>";
            return picHtml;
        }else{
            return "";
        }
    }

    //孩子信息
    function childInfo(childInfo){
        if(childInfo.childList.length > 0){
            var childHtml = "";
            for ( var i = 0; i < childInfo.childList.length; i++) {
                if(null != childInfo.childList[i].nickName && childInfo.childList[i].nickName != ""){
                    childHtml += '<div class="children">'+
                        '<span class="childrenInfo">'+
                        '<span class="name">'+childInfo.childList[i].nickName+'</span>'+
                        '<span class="year">'+childInfo.childList[i].ageDesc+'</span>'+
                        '</span>'+
                        '</div>';
                }
            }
            return childHtml;
        }else{
            return "";
        }
    }

    /*字符串转成日期类型*/
    function StringToDate(date)
    {
        var arr = [];
        if (typeof date === 'string') {
            arr = date.split('-');
        } else {
            arr[0] = date.getFullYear();
            var month = date.getMonth() + 1;
            arr[1] = month < 10 ? '0' + month : month;
            var day = date.getDate();
            arr[2] = day < 10 ? '0' + day : day;
        }
        return arr[0] + '年' + arr[1] + '月' + arr[2] +'日';
    }

    //评论
    function pingLunSub(plInfo){
        var plHtml = "";
        for ( var i = 0; i < plInfo.length; i++) {
            var nickRoom = "";
            if(null != plInfo[i].efsDiscussId && plInfo[i].efsDiscussId != ""){
                nickRoom = '<a class="name" id="dis_'+plInfo[i].discussId+'" href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintHomePage.html?userId='+plInfo[i].userId+'">'+plInfo[i].nickName+'</a>回复<a class="name" href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintHomePage.html?userId='+plInfo[i].efsUserId+'">'+plInfo[i].efsNickName+'</a>';
            }else{
                nickRoom = '<a class="name" id="dis_'+plInfo[i].discussId+'" href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintHomePage.html?userId='+plInfo[i].userId+'">'+plInfo[i].nickName+'</a>';
            }

            plHtml += '<div class="pingLunList" id="'+plInfo[i].discussId+'">'+
                '<div class="item">'+
                '<a class="head" href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintHomePage.html?userId='+plInfo[i].userId+'">'+
                '<img src="'+plInfo[i].headImgUrl+'">'+
                '</a>'+
                '<div class="content">'+
                '<div class="cenZhu">'+nickRoom+'&nbsp;&nbsp;&nbsp;&nbsp;<span class="time">'+plInfo[i].discussTime.substring(0,plInfo[i].discussTime.length - 3)+'</span>'+
                '</div>'+
                '<div class="text callComment">'+plInfo[i].discussContent+'</div>'+
                '</div>'+
                '</div>'+
                '</div>';
        }

        return plHtml;
    }

    //来自评论，弹窗评论弹窗
    if(common.getUrlSearchParams("source") == "comment"){
        efsDiscussId = "";

        common.commentBox({
            placeholder:"说上几句吧！",
            callback:function (res) {
                if(null == res.trim() || res.trim() == ""){
                    common.autoHintBox({content:"内容不能为空！"});
                    return false;
                }

                $.ajax({
                    url : '/SL_LEM/grownArticle/addDiscussInfo.do',
                    type : 'POST',
                    dataType : 'json',
                    data : 'articleId=' + articleId + '&content=' + res
                    + '&efsDiscussId=' + efsDiscussId,
                    success : function(data) {
                        if (data.resultCode == 'Y') {
                            var discussList = data.discussList;
                            $("#pingLun").html('<div class="title">评论 （'+discussList.length+'）</div><div class="content">'+pingLunSub(discussList)+'</div>');

                            var pingLun = $(".dayItem .pingLun");
                            pingLun.text("评论("+(Number(pingLun.text().substring(3,pingLun.text().length-1))+1)+")").addClass("daShang-animation");

                            common.autoHintBox({content:"评论成功"});
                        }
                    },
                    error : function() {
                        $(this).alertFnOne({
                            title: '温馨提示',
                            content: '评论失败，链接出错，请重试！'
                        });
                    }
                });
            }
        });
    }

    footprintOperation();//足迹操作

    comment();//评论

    function comment(){
        $(".callComment").on("click",function () {
            efsDiscussId = $(this).parent().parent().parent().attr("id");
            var huifu = "回复"+$("#dis_"+efsDiscussId).html();

            common.commentBox({
                placeholder:huifu,
                callback:function (res) {
                    if(null == res.trim() || res.trim() == ""){
                        common.autoHintBox({content:"内容不能为空！"});
                        return false;
                    }

                    $.ajax({
                        url : '/SL_LEM/grownArticle/addDiscussInfo.do',
                        type : 'POST',
                        dataType : 'json',
                        data : 'articleId=' + articleId + '&content=' + res
                        + '&efsDiscussId=' + efsDiscussId,
                        success : function(data) {
                            if (data.resultCode == 'Y') {
                                var discussList = data.discussList;
                                $("#pingLun").html('<div class="title">评论 （'+discussList.length+'）</div><div class="content">'+pingLunSub(discussList)+'</div>');

                                var pingLun = $(".dayItem .pingLun");
                                pingLun.text("评论("+(Number(pingLun.text().substring(3,pingLun.text().length-1))+1)+")").addClass("daShang-animation");
                                comment();//评论
                                common.autoHintBox({content:"回复成功"});
                            }
                        },
                        error : function() {
                            $(this).alertFnOne({
                                title: '温馨提示',
                                content: '评论失败，链接出错，请重试！'
                            });
                        }
                    });
                }
            });
        });
    }

    function footprintOperation() {
        //评论
        $(".dayItem .pingLun").on("click",function () {
            efsDiscussId = "";

            common.commentBox({
                placeholder:"说上几句吧！",
                callback:function (res) {
                    if(null == res.trim() || res.trim() == ""){
                        common.autoHintBox({content:"内容不能为空！"});
                        return false;
                    }

                    $.ajax({
                        url : '/SL_LEM/grownArticle/addDiscussInfo.do',
                        type : 'POST',
                        dataType : 'json',
                        data : 'articleId=' + articleId + '&content=' + res
                        + '&efsDiscussId=' + efsDiscussId,
                        success : function(data) {
                            if (data.resultCode == 'Y') {
                                var discussList = data.discussList;
                                $("#pingLun").html('<div class="title">评论 （'+discussList.length+'）</div><div class="content">'+pingLunSub(discussList)+'</div>');

                                var pingLun = $(".dayItem .pingLun");
                                pingLun.text("评论("+(Number(pingLun.text().substring(3,pingLun.text().length-1))+1)+")").addClass("daShang-animation");
                                comment();//评论
                                common.autoHintBox({content:"评论成功"});
                            }
                        },
                        error : function() {
                            $(this).alertFnOne({
                                title: '温馨提示',
                                content: '评论失败，链接出错，请重试！'
                            });
                        }
                    });
                }
            });
            return false
        });

        //头像点击
        $(".dayItem .head").on("click",function () {
            return;
            var uuId = $(this).attr("id");
            location.href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintHomePage.html?userId="+uuId;
            return false
        });
    }

    //话题
    $(".navigateTopic").on("click",function () {
        var topicId = $(this).attr("id");
        location.href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/topic.html?topicId="+topicId;
        return false;
    });
    //主题
    $(".navigateTheme").on("click",function () {
        var themeId = $(this).attr("id");
        location.href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/theme.html?themeId="+themeId;
        return false;
    });

    //加载更多
    loadMore();
    function loadMore() {
        //加载更多点赞
        $("#loadMoreButton-dianZan").on("click",function () {
            console.log("加载更多点赞");
        });
        //加载更多评论
        $("#loadMoreButton-pingLun").on("click",function () {
            console.log("加载更多评论");
        });
        //加载更多打赏
        $("#loadMoreButton-daShang").on("click",function () {
            console.log("加载更多打赏");
        });
    }

    //来自二维码
    sourceCode();
    function sourceCode() {
        var isSourceCode = false;//是否来自二维码
        var isCan = true;//是否开罐
        var href = "http://www.baidu.com";
        if(isSourceCode){
            if(isCan){
                $(".page").prepend('<a class="guangGaoLink" href="'+href+'">进入我的宝贝存钱罐写足迹 ></a>');
            }else {
                $(".page").prepend('<a class="guangGaoLink" href="'+href+'">开通宝贝存钱罐就可以写足迹啦 ></a>');
            }
        }
    }
};

function getUrlParam(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.split('?')[2].match(reg);  //匹配目标参数
    if (r!=null) return unescape(r[2]); return null; //返回参数值
}
