$(function () {
    page();
});
var page = function () {
    //窗口滚动
    window.addEventListener("scroll",function (e) {
        tabFixed();//tab固定
        pullLoad();//上拉分页加载
    });

    footprintOperation();//足迹操作

    function tabFixed() {
        var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
        var tabTop = $("#dayContext").offset().top;
        if(scrollTop > tabTop){
            $("#square-tab").addClass("square-tab-flex");
        }else {
            $("#square-tab").removeClass("square-tab-flex");
        }
    }

    function footprintOperation(){
        //tab选择
        $("#square-tab .option").on("click",function () {
            $(this).addClass("current").siblings().removeClass("current");
        });

        //足迹点击跳转
        var delayed;
        $(".dayItem").on("click",function () {
            var that = $(this);
            $(this).addClass("dayItemClick");
            clearTimeout(delayed);
            delayed = setTimeout(function () {
                that.removeClass("dayItemClick");
            },300);
            location.href = "footprintDetails.html";
        });

        //评论
        $(".dayItem .pingLun").on("click",function () {
            location.href = "footprintDetails.html?source=comment";
            return false
        });

        //分享
        $(".dayItem .share").on("click",function () {
            common.shareBox("向亲友们晒一晒宝宝的成长足迹吧~");
            return false
        });

        //头像点击
        $(".dayItem .head").on("click",function () {
            location.href="footprintHomePage.html";
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
