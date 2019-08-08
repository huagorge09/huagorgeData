var articleId = getUrlParam("articleId");
var viewId = localStorage.getItem('currentViewId');//首页选中罐子
var is_jhApply = true;//防止重复申请精华

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


    footprintOperation();//足迹操作

    comment();//评论


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

                appendHtml(data);
            }
        });
    }

    function appendHtml(artInfo){

        $("#createdDate").html(StringToDate(artInfo.articleInfo.releaseDate.substring(0,10)));
        $("#arttext").html(artInfo.articleInfo.articleContent);
        //精华申请
        if(artInfo.showReqBtn == '01'){
            $("#applyFabulous").html("申请精华");
        }else if(artInfo.showReqBtn == '02'){
            $("#applyFabulous").html("审核中");
        }else{
            if(artInfo.showJH == 'Y'){
                $("#is_jh").val(artInfo.showJH);
                $("#applyFabulous").html("精华");
            }else{
                $("#applyFabulous").hide();
            }
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
            $(".dianZan").html('<div class="title">点赞('+artInfo.likeList.length+')</div><div class="content"><div class="dianZanList clearFloat">'+dianzan(artInfo.likeList)+'</div></div>');
        }else{
            $(".dianZan").html('<div class="title">点赞(0)</div>');
        }

        //评论
        if(artInfo.discussList.length > 0){
            $(".pingLun").html('<div class="title">评论('+artInfo.discussList.length+')</div><div class="content">'+pingLun(artInfo.discussList)+'</div>');
        }else{
            $(".pingLun").html('<div class="title">评论(0)</div>');
        }

        //打赏
        if(artInfo.rewardList.length > 0){
            $(".daShang").html('<div class="title">打赏('+artInfo.rewardList.length+')</div><div class="pingLunList">'+daShang(artInfo.rewardList)+'</div>');
        }else{
            $(".daShang").html('<div class="title">打赏(0)</div>');
        }


        publicPage();
    }

    //打赏
    function daShang(dsInfo){
        var dsHtml = "";
        for ( var i = 0; i < dsInfo.length; i++) {
            dsHtml += '<div class="item">'+
                '<a class="head" href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintHomePage.html">'+
                '<img src="'+dsInfo[i].headImgUrl+'">'+
                '</a>'+
                '<div class="content">'+
                '<div class="cenZhu">'+
                '<a class="name" href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintHomePage.html">'+dsInfo[i].nickName+'</a>'+
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

    //点赞信息
    function dianzan(dzInfo){
        var dzHtml = "";
        for ( var i = 0; i < dzInfo.length; i++) {
            dzHtml += '<a class="item" href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintHomePage.html?userId='+dzInfo[i].userId+'"><img src="'+dzInfo[i].headImgUrl+'"></a>';
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
            html = '<span id='+avtInfo.themeId+' class="text navigateTheme">#'+avtInfo.themeName+'#</span>';
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

    function comment(){
        $(".callComment").on("click",function () {
            var disId = $(this).parent().parent().parent().attr("id");
            var huifu = "回复"+$("#dis_"+disId).html();
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
                        + '&efsDiscussId=' + disId,
                        success : function(data) {
                            if (data.resultCode == 'Y') {
                                var discussList = data.discussList;
                                $(".pingLun").html('<div class="title">评论 （'+discussList.length+'）</div><div class="content">'+pingLun(discussList)+'</div>');

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
        //删除
        $("#delete").on("click",function () {
            common.affirmHintBox({
                content:"确定要删除吗?",
                callback:function (res) {
                    //确定
                    if(res == "confirm"){
                        $.ajax({
                            url: '/SL_LEM/grownArticle/deleteArticle.do',
                            type: 'POST',
                            dataType: 'json',
                            data:'articleId=' + articleId,
                            error: function(){
                                $(this).alertFnOne({
                                    title: '温馨提示',
                                    content: '删除失败，链接出错，请重试！'
                                });
                            },
                            success: function(data){
                                if(data.resultCode == 'Y'){
                                    common.autoHintBox({
                                        content:"已删除",
                                        callback:function () {
                                            location.href = "/SL_LEM/grown/growthFootPrint/my.html";
                                        }
                                    });
                                }else{
                                    $(this).alertFnOne({
                                        title: '温馨提示',
                                        content:  '删除失败，请重试！'
                                    });
                                }
                            }
                        });
                    }
                    //取消
                    else if(res == "cancel"){

                    }
                }
            });
        });
        //申请精华
        $("#applyFabulous").on("click",function () {
            //精华不需要申请
            if('Y' == $("#is_jh").val()){
                return false;
            }

            if(is_jhApply){
                is_jhApply = false;
                var obj = $(this);
                $.ajax({
                    url: "/SL_LEM/article/beImportant.do",
                    type: 'POST',
                    dataType: 'json',
                    data:'art_id=' + articleId,
                    error: function(){
                        $(this).alertFnOne({
                            title: '温馨提示',
                            content: '操作失败，请重试！'
                        });
                    },
                    success: function(data){
                        is_jhApply = true;
                        if(data.succesFlag == 'L'){
                            $(this).alertFnOne({
                                title: '温馨提示',
                                content:  '存钱罐达到童生Lv.1级才可以申请精华哦，累计存满6次、满200元即可达到。 ',
                                buttonStr:'去升级',
                                callback: goAddPos,
                                cancelButton: 'Y'
                            });
                        }else if(data.succesFlag == 'Y'){
                            obj.text("审核中").parents(".moreWrap").find(".more").removeClass("more-pull").siblings(".list").hide();
                            common.autoHintBox({content:"申请精华足迹审核中..."});
                        }else{
                            $(this).alertFnOne({
                                title: '温馨提示',
                                content:  data.resultMessage
                            });
                        }
                    }
                });
            }

        });

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

        //分享
        $("#fenXiang").on("click",function () {
            _paramData.shareType = "newFootPrintShare";//分享类型
            _paramData.needRw = "Y";//分享类型：Y:需要重写分享基础类
            _paramData.bizType = "";//业务类型
            _paramData.bizKey = "";//业务值
            _paramData.articleId = articleId;//帖子id
            SHARE.COMM.share();

            common.shareBox("向亲友们晒一晒宝宝的成长足迹吧~");
        });
    }

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
};

function getUrlParam(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r!=null) return unescape(r[2]); return null; //返回参数值
}
