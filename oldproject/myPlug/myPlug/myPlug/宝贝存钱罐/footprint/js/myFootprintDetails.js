$(function () {
    page();
});
//页面交互
var page = function () {

    footprintOperation();//足迹操作

    comment();//评论

    function comment(){
        $(".callComment").on("click",function () {
            common.commentBox({
                placeholder:"回复测试",
                callback:function (res) {
                    common.autoHintBox({
                        content:"发送："+res
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
                        common.autoHintBox({
                            content:"已删除",
                            callback:function () {
                                location.href = "my.html";
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
            $(this).text("审核中");
            common.autoHintBox({
                content:"申请精华足迹审核中..."
            });
        });
        //分享
        $("#fenXiang").on("click",function () {
            common.shareBox("向亲友们晒一晒宝宝的成长足迹吧~");
        });
    }

    //加载更多
    loadMore();
    function loadMore() {
        //加载更多点赞
        $("#dianZan").on("click","#loadMoreButton-dianZan",function () {
            console.log("加载更多点赞");
        });
        //加载更多评论
        $("pingLun").on("click","#loadMoreButton-pingLun",function () {
            console.log("加载更多评论");
        });
        //加载更多打赏
        $("daShang").on("click","#loadMoreButton-daShang",function () {
            console.log("加载更多打赏");
        });
    }
};
