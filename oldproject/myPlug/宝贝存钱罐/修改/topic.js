var topicId = getUrlParam("topicId");//话题Id
var themeId = getUrlParam("themeId");//主题Id
var viewId = localStorage.getItem('currentViewId');//首页选中罐子
var themeType = '2',pageNum = '1',articleId;//话题Id,查询类型,当前页数,帖子id

$(function () {
    page();
});
var page = function () {

    _paramData.shareType = "newFpPageShare";//分享类型
    _paramData.needRw = "Y";//分享类型：Y:需要重写分享基础类
    _paramData.bizType = "";//业务类型
    _paramData.bizKey = "";//业务值
    if(null != topicId && topicId != "null" && topicId != ""){
        _paramData.pageType = "04";//区分页面类型01：我的主页，02：广场页 ,03:足迹主题页，04：话题页
        _paramData.busId = topicId;//业务id
    }else{
        _paramData.pageType = "03";//区分页面类型01：我的主页，02：广场页 ,03:足迹主题页，04：话题页
        _paramData.busId = themeId;//业务id
    }

    SHARE.COMM.share();

    //初始化数据
    initData();

    //窗口滚动
    window.addEventListener("scroll",function (e) {
        tabFixed();//tab固定
        pullLoad();//上拉分页加载
    });

    //初始化数据
    function initData(){
        $.ajax({
            url: '/SL_LEM/growthFootPrint/getMyGrowthThemeprint.do',
            type: 'POST',
            dataType: 'json',
            async:false,
            data:"activityId="+topicId+"&themeId="+themeId+"&viewId="+viewId+"&themeType="+themeType+"&pageNum="+pageNum,
            error: function(){
                $(this).alertFnOne({
                    title: '温馨提示',
                    content: '查询数据异常，请重试'
                });
            },
            success: function(data){

                if(undefined != data.fodderSubList && data.fodderSubList.length > 0){
                    localStorage.setItem("guide",data.fodderSubList[0].fodderContent);
                }else{
                    localStorage.setItem("guide",data.pageDesc);
                }
                localStorage.setItem("tips",data.pageDesc);
                appendHtml(data);
            }
        });
    }

    //加载头部话题信息
    function appendHtml(artInfo){
        $(".tuWen").find("img").attr("src",artInfo.themePicUrl);
        $(".miaoShu").html(artInfo.pageDesc);
        $(".zuJi .num").html('已有'+artInfo.joinNum+'条足迹');

        //话题下帖子
        artHtml(artInfo);
    }

    //初始化帖子信息
    function artHtml(wallInfo){
        $(".dayList").html('');
        if(undefined != wallInfo.footprintList && wallInfo.footprintList.length > 0){
            var objHtml = "";
            for ( var i = 0; i < wallInfo.footprintList.length; i++) {
                objHtml += '<div class="dayItem" id=\''+wallInfo.footprintList[i].articleId+'\'>'+
                    '<div class="context">'+
                    '<div class="wrap">'+
                    '<div class="top clearFloat">'+
                    '<div class="user">'+
                    '<img class="head" id="'+wallInfo.footprintList[i].userId+'" src="'+wallInfo.footprintList[i].wxHeadPic+'">'+
                    '<div class="name">'+wallInfo.footprintList[i].nickName+'</div>'+
                    '</div>'+
                    '<div class="childrenWrap">'+childInfo(wallInfo.footprintList[i])+'</div>'+
                    jhFalg(wallInfo.footprintList[i])
                    +
                    '</div>'+
                    '<div class="text">'+wallInfo.footprintList[i].articleContent+'</div>'+
                    '<div class="picture clearFloat">'+picInfo(wallInfo.footprintList[i])+'</div>'+
                    voiceInfo(wallInfo.footprintList[i])
                    +''+
                    themeAct(wallInfo.footprintList[i])+
                    '<div class="time">'+wallInfo.footprintList[i].releaseDate.substring(0,wallInfo.footprintList[i].releaseDate.length - 3)+'</div>'+
                    '</div>'+
                    '<div class="bottom may">'+
                    '<div class="daShang">打赏('+wallInfo.footprintList[i].pigpointNum+')'+
                    '<input type="hidden" id="bindId" value="'+wallInfo.footprintList[i].bindUserId+'"/>'+
                    '<input type="hidden" id="childName" value="'+wallInfo.footprintList[i].insName+'"/>'+
                    '</div>'+
                    '<div class="pingLun">评论('+wallInfo.footprintList[i].discussNum+')</div>'+
                    dianzan(wallInfo.footprintList[i])
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
        if(jhInfo.isFire == 'JH'){
            return '<div class="fabulous"></div>';
        }else {
            return "";
        }
    }

    function voiceInfo(voiceInfo){
        if(null != voiceInfo.voiceId && voiceInfo.voiceId != ""){
            return '<div class="record">'+
                '<audio src="'+voiceInfo.voiceUrl+'"></audio>'+
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
            for ( var i = 0; i < picInfo.articlePicList.length; i++) {
                picHtml += '<div class="imgWrap"><img src="'+picInfo.articlePicList[i].smallPicUrl+'" data-original="'+picInfo.articlePicList[i].smallPicUrl+'" onload="common.pictureImgLoad(this)" picNo="'+picInfo.articlePicList[i].picNo+'" ></div>';
            }
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

    function tabFixed() {
        var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
        var tabTop = $("#dayContext").offset().top;
        if(scrollTop > tabTop){
            $("#square-tab").addClass("square-tab-flex");
        }else {
            $("#square-tab").removeClass("square-tab-flex");
        }
    }

    $(".luYin").on("click",function () {
        location.href = "/SL_LEM/grown/growthFootPrint/tape.html?editType=square&activityId="+topicId+"&themeId="+themeId;
        return false
    });

    //tab选择
    $("#square-tab .option").on("click",function () {
        $(this).addClass("current").siblings().removeClass("current");
        themeType =  $(this).attr("data");

        $.ajax({
            url: '/SL_LEM/growthFootPrint/getMyGrowthThemeprint.do',
            type: 'POST',
            dataType: 'json',
            async:false,
            data:"activityId="+topicId+"&themeId="+themeId+"&viewId="+viewId+"&themeType="+themeType+"&pageNum="+pageNum,
            error: function(){
                $(this).alertFnOne({
                    title: '温馨提示',
                    content: '查询数据异常，请重试'
                });
            },
            success: function(data){
                artHtml(data);
            }
        });
    });

    function footprintOperation(){
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
            location.href = "/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintDetails.html?articleId="+articleId;
        });

        //评论
        $(".dayItem .pingLun").on("click",function () {
            articleId = $(this).parent().parent().parent().attr("id");
            location.href = "/SL_LEM/pay/weixin/showPage.do?goUrl=/grown/growthFootPrint/footprintDetails.html?source=comment&articleId="+articleId;
            return false
        });

        //分享
        $(".dayItem .share").on("click",function () {
            common.shareBox();
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
}

function getUrlParam(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.split('?')[2].match(reg);  //匹配目标参数
    if (r!=null) return unescape(r[2]); return null; //返回参数值
}
