var viewId = localStorage.getItem('currentViewId');//首页选中罐子
var operateType = "01";//足迹墙默认展示同龄
var activityId,articleId;//话题ID,帖子Id

$(function () {
    page();
});
var page = function () {

    _paramData.shareType = "newFpPageShare";//分享类型
    _paramData.needRw = "Y";//分享类型：Y:需要重写分享基础类
    _paramData.bizType = "";//业务类型
    _paramData.bizKey = "";//业务值
    _paramData.pageType = "02";//区分页面类型01：我的主页，02：广场页 ,03:足迹主题页，04：话题页
    SHARE.COMM.share();


    //初始化数据
    initData();

    //足迹墙
    initFootPrintWall();


    //窗口滚动
    window.addEventListener("scroll",function (e) {
        tabFixed();//tab固定
        pullLoad();//上拉分页加载
    });

    //录音列表
    var luYinIScroll = new iScroll('luYinList', {
        vScrollbar:false,
        hScrollbar:false,
        scrollX: true,
        scrollY: false
    });

    //需要在元素加载完毕后计算宽度及刷新
    $("#luYinList .scroller").width($("#luYinList").find(".item").length*$("#luYinList").find(".item").width());
    luYinIScroll.refresh();

    topTitle();//顶部标题操作

    //初始化数据
    function initData(){
        $.ajax({
            url: '/SL_LEM/growthFootPrint/squareTop.do',
            type: 'POST',
            dataType: 'json',
            async:false,
            data:"viewId="+viewId,
            error: function(){
                $(this).alertFnOne({
                    title: '温馨提示',
                    content: '查询数据异常，请重试'
                });
            },
            success: function(data){

                if(data.fodderSubList.length > 0){
                    localStorage.setItem("guide",data.fodderSubList[0].fodderContent);
                }else{
                    localStorage.setItem("guide",data.pageDesc);
                }
                localStorage.setItem("tips",data.pageDesc);
                appendHtml(data);
            }
        });
    }

    function initFootPrintWall(){
        $.ajax({
            url: '/SL_LEM/growthFootPrint/footPrintWall.do',
            type: 'POST',
            dataType: 'json',
            async:false,
            data:"viewId="+viewId+"&operateType="+operateType,
            error: function(){
                $(this).alertFnOne({
                    title: '温馨提示',
                    content: '查询数据异常，请重试'
                });
            },
            success: function(data){

                appendWallHtml(data);
            }
        });
    }

    function appendHtml(artInfo){
        activityId = artInfo.activityId;
        $(".tuWen").find("img").attr("src",artInfo.themePicUrl);
        $(".miaoShu").html(artInfo.pageDesc);
        $("#topicMore").attr("href","/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/topic.html?topicId="+artInfo.activityId);

        //话题下top20帖子
        if(artInfo.articleList.length > 0){
            $("#luYinList #scroller").html(artTop20Info(artInfo.articleList));
        }

    }

    function appendWallHtml(wallInfo){
        $(".dayList").html('');
        if(wallInfo.wallInfoList.length > 0){
            var objHtml = "";
            for ( var i = 0; i < wallInfo.wallInfoList.length; i++) {
                objHtml += '<div class="dayItem" id=\''+wallInfo.wallInfoList[i].articleId+'\'>'+
                    '<div class="context">'+
                    '<div class="wrap">'+
                    '<div class="top clearFloat">'+
                    '<div class="user">'+
                    '<img class="head" id="'+wallInfo.wallInfoList[i].userId+'" src="'+wallInfo.wallInfoList[i].wxHeadPic+'">'+
                    '<div class="name">'+wallInfo.wallInfoList[i].nickName+'</div>'+
                    '</div>'+
                    '<div class="childrenWrap">'+childInfo(wallInfo.wallInfoList[i])+'</div>'+
                    jhFalg(wallInfo.wallInfoList[i])
                    +
                    '</div>'+
                    '<div class="text">'+wallInfo.wallInfoList[i].articleContent+'</div>'+
                    picInfo(wallInfo.wallInfoList[i])+
                    voiceInfo(wallInfo.wallInfoList[i])
                    +''+
                    themeAct(wallInfo.wallInfoList[i])+
                    '<div class="time">'+wallInfo.wallInfoList[i].releaseDate.substring(0,wallInfo.wallInfoList[i].releaseDate.length - 3)+'</div>'+
                    '</div>'+
                    '<div class="bottom may">'+
                    '<div class="daShang">打赏('+wallInfo.wallInfoList[i].pigpointNum+')'+
                    '<input type="hidden" id="bindId" value="'+wallInfo.wallInfoList[i].bindUserId+'"/>'+
                    '<input type="hidden" id="childName" value="'+wallInfo.wallInfoList[i].insName+'"/>'+
                    '</div>'+
                    '<div class="pingLun">评论('+wallInfo.wallInfoList[i].discussNum+')</div>'+
                    dianzan(wallInfo.wallInfoList[i])
                    +
                    '</div>'+
                    '</div>'+
                    '</div>';
            }

            $(".dayList").html(objHtml);
        }else{
            $(".dayList").html('<div style="text-align: center;padding-top: 2rem;"><font>没有更多记录</font></div>');
        }

        publicPage();
        footprintOperation();//足迹操作
        footprintOperationNew();//足迹操作
    }

    //点赞
    function dianzan(dianzanInfo){
        if(dianzanInfo.isLike == 'Y'){
            return '<div class="dianZan dianZanAction dianZan-animation">点赞('+dianzanInfo.likeNum+')</div>';
        }else{
            return '<div class="dianZan">点赞('+dianzanInfo.likeNum+')</div>';
        }
    }

    //精华图标
    function jhFalg(jhInfo){
        if(jhInfo.isFire == 'Y'){
            return '<div class="fabulous"></div>';
        }else {
            return "";
        }
    }

    function voiceInfo(voiceInfo){
        if(null != voiceInfo.voiceid && voiceInfo.voiceid != ""){
            return '<div class="record">'+
                '<audio src="'+voiceInfo.voiceurl+'"></audio>'+
                '<div class="BarAudioPlayer">'+
                '<div class="currentTime">00:00</div>'+
                '<div class="scheduleBar">'+
                '<div class="currentSchedule"></div>'+
                '<div class="button"></div>'+
                '</div>'+
                '<div class="endTime">00:00</div>'+
                '</div>'+
                '</div>';
        }else{
            return "";
        }
    }

    //主题、话题
    function themeAct(avtInfo){
        if((null != avtInfo.themeId && avtInfo.themeId != "") && (null != avtInfo.activityId && avtInfo.activityId != "")){
            return '<div class="topic-theme">'+
                '<span id='+avtInfo.activityId+' class="text navigateTopic">#<span class="tape"></span>'+avtInfo.activityName+'#</span>'+
                '<span id='+avtInfo.themeId+' class="text navigateTheme">#'+avtInfo.themeName+'#</span>'+
                '</div>';
        }else if(null != avtInfo.activityId && avtInfo.activityId != ""){
            return '<div class="topic-theme">'+
                '<span id='+avtInfo.activityId+' class="text navigateTopic">#<span class="tape"></span>'+avtInfo.activityName+'#</span>'+
                '</div>';
        }else if(null != avtInfo.themeId && avtInfo.themeId != ""){
            return '<div class="topic-theme">'+
                '<span id='+avtInfo.themeId+' class="text navigateTheme">#'+avtInfo.themeName+'#</span>'+
                '</div>';
        }else {
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
                        '<span class="year">'+childInfo.childList[i].ageDesc+'</span>'+isSameAge(childInfo.childList[i])+
                        '</span>'+
                        '</div>';
                }
            }
            return childHtml;
        }else{
            return "";
        }
    }

    //是否同龄
    function isSameAge(childInfo){
        if(childInfo.isSameAge == 'Y'){
            return '<span class="sameAge">同龄</span>';
        }else{
            return "";
        }
    }


    function artTop20Info(articleList){
        var topHtml = "";
        for ( var i = 0; i < articleList.length; i++) {
            topHtml += '<div class="item">'+
                '<div class="head">'+
                '<img src="'+articleList[i].headImgUrl+'">'+
                '<div class="headAudioPlayer record">'+
                '<audio src="'+articleList[i].voiceUrl+'"></audio>'+
                '<canvas></canvas>'+
                '<div class="button"></div>'+
                '</div>'+
                '</div>'+
                '<a class="user" href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintDetails.html?articleId='+articleList[i].articleId+'">'+articleList[i].nickName+'&nbsp;'+articleList[i].ageDesc+'</a>'+
                '<a class="daShang" href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintDetails.html?articleId='+articleList[i].articleId+'">打赏：'+articleList[i].rewardTimes+'份</a>'+
                '</div>';
        }

        return topHtml;
    }


    $("#luYin").on("click",function () {
        location.href = "/SL_LEM/grown/growthFootPrint/tape.html?editType=square&activityId="+activityId;
        return false
    });

    function topTitle() {
        //tab选择
        $("#square-tab .option").on("click",function () {
            $(this).addClass("current").siblings().removeClass("current");
            operateType =  $(this).attr("data");
            $(".dayList").html('');
            //足迹墙
            initFootPrintWall();
        });

        //顶部标题展开
        $("#zhanKai").on("click",function () {
            $(this).parents(".title").hide();
            $("#shouQi").parents(".title").show();
        });

        //顶部标题收起
        $("#shouQi").on("click",function () {
            $(this).parents(".title").hide();
            $("#zhanKai").parents(".title").show();
        });

        //话题通知
        $("#huaTiTongZhi").on("click",function () {
            if($(this).hasClass("yes")){
                $(this).removeClass("yes");
            } else {
                $(this).addClass("yes");
            }
        });
    }

    function tabFixed() {
        var tabHeight = $(".fixed-top").height();
        var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
        var tabTop = $("#dayContext").offset().top;
        if(scrollTop > (tabTop-tabHeight)){
            $("#square-tab").addClass("square-tab-flex");
        }else {
            $("#square-tab").removeClass("square-tab-flex");
        }
    }

    function footprintOperation() {
        //足迹点击跳转
        var delayed;
        $(".dayItem").on("click",function () {
            var that = $(this);
            articleId = that.attr("id");
            $(this).addClass("dayItemClick");
            clearTimeout(delayed);
            delayed = setTimeout(function () {
                that.removeClass("dayItemClick");
            },300);
            if($(this).hasClass("ad")){
                location.href = "/SL_LEM/pay/weixin/showPage.do?goUrl=grown/growthFootPrint/topic.html";
            }else {
                location.href = "/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintDetails.html?articleId="+articleId;
            }
        });

        //评论
        $(".dayItem .pingLun").on("click",function () {
            articleId = $(this).parent().parent().parent().attr("id");
            location.href = "/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintDetails.html?source=comment&articleId="+articleId;
            return false
        });

        //分享
        $(".dayItem .share").on("click",function () {
            common.shareBox("向亲友们推荐好活动吧~");
            return false
        });

        //头像点击
        $(".dayItem .head").on("click",function () {
            var headUserId = $(this).attr("id");

            location.href="/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintHomePage.html?userId="+headUserId;
            return false
        });
    }


    //滑动加载
    function pullLoad() {
        //滑动到了底部
        if(common.isScrollBottom.scrollBottom(3)){
            console.log("aa");
            common.addLoad.haveInHand();//开始加载
            setTimeout(function () {
                common.addLoad.end();//加载结束
            },2000);
            // common.addLoad.noMore();//没有更多了
        }
    }
};

