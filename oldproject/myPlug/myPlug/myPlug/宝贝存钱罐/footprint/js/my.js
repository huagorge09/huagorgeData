$(function () {
    page();
});
//页面交互
var page = function () {

    var date = $(".monthItem").eq(0).children(".title").text();//获取第一个时间

    $("#scrollToMonth").text(date);//设置顶部月份

    window.addEventListener("scroll",function (e) {
        scrollToMonth();//获取滑动到的月份
        pullLoad();//上拉分页加载
    });

    dateSelect();//时间选择

    screen();//筛选

    footprintOperation();//足迹操作

    function screen() {
        //点击筛选
        $("#screen").on("click",function () {
            if(!$("#screenBlock").is(":hidden")){
                $("#screenBlockMask").remove();
                $("#screenBlock").hide();
            }else {
                $("body").append("<div class='mask' id='screenBlockMask' style='z-index: 98'></div>");
                $("#screenBlock").show();
                //遮盖层关闭筛选弹框
                $("#screenBlockMask").on("click",function () {
                    closeScreenBlock();
                });
            }
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
            $("#screen").addClass("screen-active");
        });
    }

    function footprintOperation() {
        //足迹点击态及跳转
        var delayed;
        $(".dayItem").on("click",function () {
            var that = $(this);
            $(this).addClass("dayItemClick");
            clearTimeout(delayed);
            delayed = setTimeout(function () {
                that.removeClass("dayItemClick");
            },300);
            //是否是广告
            if(!that.hasClass("myAd")){
                location.href = "myFootprintDetails.html";
            }else {
                //去录音
                if(that.find(".block").hasClass("topic")){
                    location.href = "tape.html";
                }
                //回忆
                else if(that.find(".block").hasClass("memory")){
                    location.href = "myFootprintDetails.html";
                }
                //成长
                if(that.find(".block").hasClass("growUp")){
                    location.href = "footprintDetails.html";
                }
                //节日
                if(that.find(".block").hasClass("festival")){
                    location.href = "myFootprintDetails.html";
                }
            }
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

        //足迹编辑展开
        $(".dayItem .more").on("click",function () {
            if($(this).hasClass("more-pull")){
                $(this).removeClass("more-pull").siblings(".list").hide();
            }else {
                $(this).addClass("more-pull").siblings(".list").show();
            }
            $(this).siblings(".moreTip").remove();
            return false
        });

        //足迹删除
        $(".dayItem .delete").on("click",function () {
            $(this).parents(".moreWrap").find(".more").removeClass("more-pull").siblings(".list").hide();
            common.affirmHintBox({
                content:"确定要删除吗?",
                callback:function (res) {
                    //确定
                    if(res == "confirm"){
                        common.autoHintBox({
                            content:"已删除"
                        });
                    }
                    //取消
                    else if(res == "cancel"){

                    }
                }
            });
            return false
        });

        //编辑
        $(".dayItem .edit").on("click",function () {
            $(this).parents(".moreWrap").find(".more").removeClass("more-pull").siblings(".list").hide();
            location.href="writefootprints.html?source=footprintEdit";
            return false
        });

        //申请精华
        $(".dayItem .applyFabulous").on("click",function () {
            $(this).text("审核中").parents(".moreWrap").find(".more").removeClass("more-pull").siblings(".list").hide();
            common.autoHintBox({content:"申请精华足迹审核中..."});
            return false
        });

        //分享
        $(".dayItem .share").on("click",function () {
            common.shareBox("向亲友们晒一晒宝宝的成长足迹吧~");
            return false
        });

        //添加足迹相片提示
        $(".dayItem .moreWrap").eq(0).append('<div class="moreTip">生成足迹相片存到手机永久保存哦~</div>');
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
                $("#scrollToMonth").text(dateText).addClass("date-current");
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
            setTimeout(function () {
                common.addLoad.end();//加载结束
            },2000);
            // common.addLoad.noMore();//没有更多了
        }
    }
};

