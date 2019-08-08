var loadingSrc = "/SL_LEM/grown/grown_resource/images/loading.gif";
var articleId = "";
var is_jhApply = true;//防止重复申请精华
var viewId = localStorage.getItem('currentViewId');//首页选中罐子
var keyWord = ''; //关键字
var isHasPhoto = ''; //是否有照片
var isVoice = '';  //是否有录音
var isJH = ''; //是否精华帖子
var beginDate = '';//选择时间筛选
var pageNum = 1;//当前页数
var isInit = false;//是否第一次初始化
var artData;

$(function () {
    page();
});

function againSearch(){
    $("body").append("<div class='mask' id='screenBlockMask' style='z-index: 98'></div>");
    $("#screenBlock").show();
}

//页面交互
var page = function () {

    _paramData.shareType = "newFpPageShare";//分享类型
    _paramData.needRw = "Y";//分享类型：Y:需要重写分享基础类
    _paramData.bizType = "";//业务类型
    _paramData.bizKey = "";//业务值
    _paramData.pageType = "01";//区分页面类型01：我的主页，02：广场页 ,03:足迹主题页，04：话题页
    SHARE.COMM.share();

    window.addEventListener("scroll",function (e) {
        scrollToMonth();//获取滑动到的月份
        pullLoad();//上拉分页加载
    });

    message();//消息

    initData();//初始化我的足迹数据

    var date = $(".monthItem").eq(0).children(".title").text();//获取第一个时间

    $("#scrollToMonth").text(date).addClass("date-current");//设置顶部月份

    dateSelect();//时间选择

    screen();//筛选

    function message(){
        $.ajax({
            url: '/SL_LEM/growthFootPrint/getUnReadMessage.do',
            type: 'get',
            dataType: 'json',
            async:false,
            data:"",
            success: function(data){
                if(data.errorCode == '0'){
                    if(data.hasNewMessage == 'Y'){
                        $(".newMessage").find("img").attr("src",data.headImgUrl);
                        $(".newMessage").find("span").html(data.newMessageNum+"条新消息");
                    }
                }
            }
        });
    }

    function initData(){
        $.ajax({
            url: '/SL_LEM/growthFootPrint/myFootPrint.do',
            type: 'POST',
            dataType: 'json',
            async:false,
            data:"viewId="+viewId+"&keyWord="+keyWord+"&isHasPhoto="+isHasPhoto+"&isVoice="+isVoice+"&isJH="+isJH+"&beginDate="+beginDate+"&pageNum="+pageNum,
            error: function(){
                $(this).alertFnOne({
                    title: '温馨提示',
                    content: '查询数据异常，请重试'
                });
            },
            success: function(data){
                artData = data;
                appendHtml(data);
            }
        });
    }

    /**
     * 组装页面数据
     */
    function appendHtml(resultData){
        var sumLength = resultData.footprintList.length;
        var moreDate = new Date();
        if(sumLength > 0){
            moreDate = resultData.footprintList[0].grownArticleDate;
        }else{
            if(null != beginDate && beginDate != ""){
                moreDate = beginDate;
            }
        }

        if(!isInit){
            var time = (new Date).getFullYear() * 13 + (new Date).getMonth() + 1;
            $('.monthList').append('<div class="monthItem"><div class="title">'+dateDisplay(moreDate)+'</div><div class="dayList clearFloat" id="y_month_'+time+'"></div></div>');
            $('#y_month_' + time).append('<div class="dayItem myAd">'+
                '<div class="date">'+(new Date()).getDate()+'<span>日</span></div>'+
                '<div class="context">'+guideLanguage(resultData)+'</div>'+
                '</div>');

            isInit = true;
        }

        if(sumLength > 0){
            // 构造条目列表
            for (i = 0; i < sumLength; i++) {
                var arr = resultData.footprintList[i].grownArticleDate.split('-');
                var arrId = 'y_month_' + (arr[0] * 13 + Number(arr[1]));
                if ($('#' + arrId).length == 0) {
                    $('.monthList').append('<div class="monthItem"><div class="title">'+dateDisplay(resultData.footprintList[i].grownArticleDate)+'</div><div class="dayList clearFloat" id="'+arrId+'"></div></div>');
                }
            }

            for(var i = 0;i < sumLength; i++){
                var arr = resultData.footprintList[i].grownArticleDate.split('-');
                var arrId = '#y_month_' + (arr[0] * 13 + Number(arr[1]));
                $.each(resultData.footprintList[i].grownArticleList, function(entryIndex, item) {
                    $(arrId).append('<div class="dayItem" id=\''+item.articleId+'\'>'+
                        '<div class="date">'+item.releaseDate.substring(8, 10)+'<span>日</span></div>'+
                        '<div class="context">'+
                        '<div class="wrap">'+
                        '<div class="top clearFloat">'+
                        '<div class="childrenWrap">'+childInfo(item)+'</div>'+
                        jhFalg(item)+
                        '<div class="operation">'+
                        '<div class="share"></div>'+
                        '<div class="moreWrap">'+
                        '<div class="more"></div>'+
                        '<div class="list">'+
                        '<div class="generatePhotos">足迹相片</div>'+
                        jhApply(item)+
                        '<div class="edit">编辑</div>'+
                        '<div class="delete">删除</div>'+
                        '</div>'+
                        '</div>'+
                        '</div>'+
                        '</div>'+
                        '<div class="text">'+item.articleContent+'</div>'+
                        picInfo(item)+
                        voiceInfo(item)+''+themeAct(item)+'</div>'+
                        '<div class="bottom">'+
                        '<div class="daShang ban">打赏('+item.pigpointNum+')</div>'+
                        '<div class="pingLun ban">评论('+item.discussNum+')</div>'+
                        '<div class="dianZan ban">点赞('+item.likeNum+')</div>'+
                        '</div>'+
                        '</div>'+
                        '</div>');
                });
            }

            publicPage();
            footprintOperation();//足迹操作
            footprintOperationNew();//足迹操作

            $(".notContain").hide();
        }else{
            if(!isInit){
                $(".notContain").show().html('<a class="handle">哇，还没有记录过宝贝的成长足迹，现在就写一个呗~</a>');
            }else{
                $(".notContain").show().html('<a class="handle" href="javascript:againSearch();">没有找到相应足迹,<span>重新筛选</span></a>');
            }
        }
    }

    //精华申请
    function jhApply(jhInfo){
        if(jhInfo.showReqBtn == '01'){
            return '<div class="applyFabulous">申请精华</div>';
        }else if(jhInfo.showReqBtn == '02'){
            return '<div class="applyFabulous">审核中</div>';
        }else{
            return "";
        }
    }

    //精华图标
    function jhFalg(jhInfo){
        if(jhInfo.showJH == 'Y'){
            return '<div class="fabulous"></div>';
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
                picHtml += '<div class="imgWrap"><img src="'+picInfo.articlePicList[i].smallPicUrl+'" data-original="'+picInfo.articlePicList[i].smallPicUrl+'" onload="'+pictureImgLoad+'" ></div>';
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

    //节日、话题、那年今日，成长
    function guideLanguage(avtInfo){
        if(avtInfo.guideType == '01'){
            return '<div class="block festival"><div class="text">'+avtInfo.desc+'</div><div class="navigation">去晒图 ></div></div>';
        }else if(avtInfo.guideType == '02'){
            return '<div class="block topic"><div class="text">'+avtInfo.desc+'</div><div class="navigation">去录音 ></div></div>';
        }else if(avtInfo.guideType == '03'){
            if(avtInfo.subGuideType == '01'){
                return '<div class="block growUp 01"><div class="text">'+avtInfo.desc+'</div><div class="navigation">去晒图 ></div></div>';
            }else{
                return '<div class="block growUp 02"><div class="text">'+avtInfo.desc+'</div><div class="navigation">去录音 ></div></div>';
            }
        }else if(avtInfo.guideType == '04'){
            return '<div class="block memory"><div class="text">'+avtInfo.desc+'</div><div class="navigation">去看看 ></div></div>';
        }else{
            return "";
        }

    }

    // 时间显示处理
    function dateDisplay(date) {
        var arr = [];
        if (typeof date === 'string') {
            arr = date.split('-');
        } else {
            arr[0] = date.getFullYear();
            var month = date.getMonth() + 1;
            arr[1] = month < 10 ? '0' + month : month;
        }
        return arr[0] + '年' + arr[1] + '月';
    }

    function screen() {
        //点击筛选
        $("#screen").on("click",function () {
            if($(this).hasClass("screen-show")){
                $(this).removeClass("screen-show");
                $("#screenBlockMask").remove();
                $("#screenBlock").hide();
            }else {
                $("#screen").addClass("screen-show");
                $("body").append("<div class='mask' id='screenBlockMask' style='z-index: 98'></div>");
                $("#screenBlock").show();
                //遮盖层关闭筛选弹框
                $("#screenBlockMask").on("click",function () {
                    closeScreenBlock();
                });
            }
        });

        //重新筛选
        $(".notContain").on("click","span",function () {
            $("#screen").addClass("screen-show");
            $("body").append("<div class='mask' id='screenBlockMask' style='z-index: 98'></div>");
            $("#screenBlock").show();
        });
        //搜索框点击
        $("#screenBlock .shouSuoButton").on("click",function () {
            $(this).hide();
            $("#screenInput").focus();
        });

        //筛选输入
        $("#screenInput").on("input",function () {
            if($(this).val().length>0){
                $(this).siblings(".delete").show();
            }else {
                $(this).siblings(".delete").hide();
            }
            isVia();
        });

        //筛选输入框删除按钮点击
        $("#screenBlock .delete").on("click",function () {
            $(this).siblings("input").val("").focus();
            isVia();
        });

        //筛选条件选择
        $("#screenBlock .select .option").on("click",function () {
            if($(this).hasClass("current")){
                $(this).removeClass("current");
            }else {
                //无照片跟有照片不能同选；无录音跟有录音不能同选
                if($(this).hasClass("photo")){
                    $(this).siblings(".photo").removeClass("current");
                }else if($(this).hasClass("tape")){
                    $(this).siblings(".tape").removeClass("current");
                }
                $(this).addClass("current");
            }
            isVia();
        });

        //关闭按钮关闭筛选弹框
        $("#screenBlockClose").on("click",function () {
            closeScreenBlock();
        });

        //是否可以提交
        function isVia() {
            if($("#screenBlock .select .current").length>0 || $("#screenInput").val().length>0){
                $("#screenBlock .button").css("display","flex");
            }else {
                $("#screenBlock .button").css("display","none");
            }
        }

        //关闭筛选弹窗
        function closeScreenBlock() {
            $("#screen").removeClass("screen-show");
            $("#screenBlockMask").remove();
            $("#screenBlock").hide();
        }
        //筛选重置
        $("#screenReset").on("click",function () {
            $("#screenInput").val("").siblings(".shouSuoButton").show().siblings(".delete").hide();
            $("#screenBlock .select .current").removeClass("current");
            $("#screenBlock .button").css("display","none");
            $("#screen").removeClass("screen-active");
            closeScreenBlock();
        });

        //筛选确定
        $("#screenConfirm").on("click",function () {
            closeScreenBlock();
            keyWord = '';
            isHasPhoto = '';
            isVoice = '';
            isJH = '';
            isInit = false;

            keyWord = $("#screenInput").val();

            if($("#is_photo_y").hasClass("current")){
                isHasPhoto = "01";
            }
            if($("#is_photo_n").hasClass("current")){
                isHasPhoto = "02";
            }

            if($("#is_tape_y").hasClass("current")){
                isVoice = "01";
            }
            if($("#is_tape_n").hasClass("current")){
                isVoice = "02";
            }

            if($(".jh").hasClass("current")){
                isJH = "Y";
            }

            $(".monthList").html('');
            initData();//初始化我的足迹数据
            $("#screen").addClass("screen-active");
        });
    }

    function footprintOperation() {
        //足迹点击态及跳转
        var delayed,tips,guide,context;
        $(".dayItem").unbind('click').on("click",function () {
            var that = $(this);
            $(this).addClass("dayItemClick");
            clearTimeout(delayed);
            delayed = setTimeout(function () {
                that.removeClass("dayItemClick");
            },300);
            //是否是广告
            if(!that.hasClass("myAd")){
                var artId = that.attr("id");
                location.href = "/SL_LEM/grown/growthFootPrint/myFootprintDetails.html?articleId="+artId;
            }else {
                //去录音
                if(that.find(".block").hasClass("topic")){
                    context = artData.desc;
                    tips = artData.tips;
                    guide = artData.guide;
                    localStorage.setItem("context",context);
                    localStorage.setItem("tips",tips);
                    localStorage.setItem("guide",guide);
                    location.href = "/SL_LEM/grown/growthFootPrint/tape.html?editType=guideTopic&activityId="+artData.businessId;
                }
                //回忆
                else if(that.find(".block").hasClass("memory")){
                    location.href = "/SL_LEM/grown/growthFootPrint/myFootprintDetails.html?articleId="+artData.businessId;
                }
                //成长
                if(that.find(".block").hasClass("growUp")){
                    context = artData.desc;
                    localStorage.setItem("context",context);
                    if(that.find(".block").hasClass("01")){
                        location.href = "/SL_LEM/grown/growthFootPrint/writefootprints.html?editType=growUp01&activityId="+artData.businessId;
                    }else{
                        location.href = "/SL_LEM/grown/growthFootPrint/tape.html?editType=growUp02&activityId="+artData.businessId;
                    }
                }
                //节日
                if(that.find(".block").hasClass("festival")){
                    context = artData.desc;
                    tips = artData.tips;
                    guide = artData.guide;
                    localStorage.setItem("context",context);
                    localStorage.setItem("tips",tips);
                    localStorage.setItem("guide",guide);
                    location.href = "/SL_LEM/grown/growthFootPrint/writefootprints.html?editType=festival&activityId="+artData.businessId;
                }
            }
        });

        //足迹编辑展开
        $(".dayItem .more").unbind('click').on("click",function () {
            if($(this).hasClass("more-pull")){
                $(this).removeClass("more-pull").siblings(".list").hide();
            }else {
                $(this).addClass("more-pull").siblings(".list").show();
            }
            $(this).siblings(".moreTip").remove();
        });
        $('.dayItem .moreWrap').unbind('click').click(function(e) {
            e.stopPropagation();
        });

        //足迹相册
        $(".dayItem .generatePhotos").on("click",function () {
            //没有图片则提示
            if(!($(this).parents(".dayItem").find(".imgWrap").length > 0)){
                common.affirmHintBox({
                    content:"先添加照片<br/>再整体存到手机相册里更具纪念意义哦~",
                    confirm:"先添加照片",
                    cancel:"不添，就这样存",
                    callback:function (res) {
                        if(res == "confirm"){
                            location.href = "writefootprints.html";
                        }else {
                            location.href = "generatePhotos.html";
                        }
                    }
                });
            }else {
                location.href = "generatePhotos.html";
            }
            return false
        });

        //足迹删除
        $(".dayItem .delete").unbind('click').on("click",function () {
            var delarticleId = $(this).parents(".dayItem").attr("id");
            $(this).parents(".moreWrap").find(".more").removeClass("more-pull").siblings(".list").hide();
            common.affirmHintBox({
                content:"确定要删除吗?",
                callback:function (res) {
                    //确定
                    if(res == "confirm"){
                        $.ajax({
                            url: '/SL_LEM/grownArticle/deleteArticle.do',
                            type: 'POST',
                            dataType: 'json',
                            data:'articleId=' + delarticleId,
                            error: function(){
                                $(this).alertFnOne({
                                    title: '温馨提示',
                                    content: '删除失败，链接出错，请重试！'
                                });
                            },
                            success: function(data){
                                if(data.resultCode == 'Y'){
                                    if($("#"+delarticleId).parent().parent().parent().find(".dayItem").length == 1){
                                        $("#"+delarticleId).parent().parent().parent().remove();
                                    }else{
                                        $("#"+delarticleId).remove();
                                    }
                                    common.autoHintBox({
                                        content:"删除成功"
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

        //编辑
        $(".dayItem .edit").unbind('click').on("click",function () {
            var editarticleId = $(this).parents(".dayItem").attr("id");
            $(this).parents(".moreWrap").find(".more").removeClass("more-pull").siblings(".list").hide();
            var chilItem = $(this).parents(".dayItem").find(".childrenWrap").find(".children");
            location.href="/SL_LEM/grown/growthFootPrint/writefootprints.html?source=footprintEdit&articleId="+editarticleId+"&chilLength="+chilItem.length;
        });

        //申请精华
        $(".dayItem .applyFabulous").unbind('click').on("click",function () {
            var jharticleId = $(this).parents(".dayItem").attr("id");
            if(null == jharticleId || jharticleId == ""){
                return false;
            }
            if(is_jhApply){
                is_jhApply = false;
                var obj = $(this);
                $.ajax({
                    url: "/SL_LEM/article/beImportant.do",
                    type: 'POST',
                    dataType: 'json',
                    data:'art_id=' + jharticleId,
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
                            /*$(this).alertFnOne({
                             title: '温馨提示',
                             content:  data.resultMessage
                             });*/
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

        //分享
        $(".dayItem .share").unbind('click').on("click",function () {
            var shareArticleId = $(this).parents(".dayItem").attr("id");

            _paramData.shareType = "newFootPrintShare";//分享类型
            _paramData.needRw = "Y";//分享类型：Y:需要重写分享基础类
            _paramData.bizType = "";//业务类型
            _paramData.bizKey = "";//业务值
            _paramData.articleId = shareArticleId;//帖子id
            SHARE.COMM.share();

            common.shareBox("向亲友们晒一晒宝宝的成长足迹吧~");
            return false
        });
    }

    function scrollToMonth() {
        var h = $("#fixedTop").height()+10;
        var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
        var date = "";
        $(".monthItem").each(function () {
            if($(this).offset().top - scrollTop < h){
                date = $(this).children(".title").text();
            }
        });
        date ? $("#scrollToMonth").text(date):$("#scrollToMonth").text($(".monthItem").eq(0).children(".title").text());
    }

    function dateSelect() {
        var h = $("#fixedTop").height()+5;
        $("#scrollToMonth").mobiscroll().date({
            theme: "default",
            mode: "scroller",
            display: "bottom",
            animate: "slideup",
            defaultValue:new Date(),
            lang: "zh",
            dateFormat: 'yy/mm/dd', //返回结果格式化为年月格式  
            onBeforeShow:function(inst){
                inst.settings.wheels[0].length>2 ? inst.settings.wheels[0].pop() : null;
            }, //弹掉“日”滚轮  
            onSelect:function (valueText, inst) {
                var dateText = valueText.substring(0,4) + "年" + (valueText.substring(5,6) == '0' ? valueText.substring(6,7) : valueText.substring(5,7)) + "月";
                var curDate = new Date();
                var y = curDate.getFullYear()+'';
                var m = (curDate.getMonth() + 1) < 10 ? '0'+(curDate.getMonth() + 1):(curDate.getMonth() + 1)+'';
                var m2 = valueText.substring(5,7);

                if(valueText.substring(0,4) != y || m2 != m){
                    beginDate = valueText.substring(0,4)+'-'+m2;
                }else{
                    beginDate= '';
                }
                initData();//初始化我的足迹数据
                $("#scrollToMonth").text(dateText);
                //滑动到选择的时间
                $(".monthItem").each(function () {
                    if($(this).children(".title").text() == dateText){
                        $('html,body').animate({
                            scrollTop: $(this).offset().top - h
                        }, 500);
                    }
                });
            }
        });
    }
    //滑动加载
    function pullLoad() {
        //滑动到了底部
        if(common.isScrollBottom.scrollBottom(3)){
            console.log("aa");
            common.addLoad.haveInHand();//开始加载

            if(artData.pageCount <= pageNum){
                common.addLoad.end();//加载结束
                common.addLoad.noMore();//没有更多了
            }else{
                pageNum = pageNum + 1;
                initData();//初始化我的足迹数据
                setTimeout(function () {
                    common.addLoad.end();//加载结束
                },2000);
            }
            // common.addLoad.noMore();//没有更多了
        }
    }
};

