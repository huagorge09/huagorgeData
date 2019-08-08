$(function () {
    page();
});
var page = function () {

    footprintOperation();//足迹操作

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

        //打赏
        $(".dayItem .daShang").on("click",function () {
            return false
        });

        //分享
        $(".dayItem .share").on("click",function () {
            common.shareBox("向亲友们晒一晒宝宝的成长足迹吧~");
            return false
        });

        //分享
        $(".dayItem .head").on("click",function () {
            location.href="footprintHomePage.html";
            return false
        });
    }
};
