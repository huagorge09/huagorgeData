$(function () {
    page();
});
//页面交互
var page = function () {
    //来自评论，弹窗评论弹窗
    if(common.getUrlSearchParams("source") == "comment"){
        common.commentBox({
            placeholder:"回复测试",
            callback:function (res) {
                common.autoHintBox({
                    content:"发送："+res
                });
                var pingLun = $(".dayItem .pingLun");
                pingLun.text("打赏("+(Number(pingLun.text().substring(3,pingLun.text().length-1))+1)+")").addClass("daShang-animation");
            }
        });
    }

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
        //评论
        $(".dayItem .pingLun").on("click",function () {
            var that = $(this);
            that.removeClass("daShang-animation");
            common.commentBox({
                placeholder:"回复测试",
                callback:function (res) {
                    common.autoHintBox({
                        content:"发送："+res
                    });
                    that.text("评论("+(Number(that.text().substring(3,that.text().length-1).replace(/[^0-9]/g,''))+1)+")").addClass("daShang-animation");
                }
            });
            return false
        });

        //头像点击
        $(".dayItem .head").on("click",function () {
            location.href="footprintHomePage.html";
            return false
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
