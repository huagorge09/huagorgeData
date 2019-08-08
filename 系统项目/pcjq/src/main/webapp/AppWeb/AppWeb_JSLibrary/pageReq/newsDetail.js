$(document).ready(function(e) {
    $(".nav.fr ul li a").removeClass("current").eq(2).addClass("current");
    var type = getUrlParameter("type")
    type = removeSpecialStr(type);
    type = type.replace("#", "");
    if(type != null && type == "news"){
        $("#pageContext").html("< 公司公告").attr("href","/AppService/article/newsList.shtml?type=2");
    }
    if(type=="original"){
        $("#pageContext").html("< 原创文章").attr("href","/AppService/article/newsList.shtml?type=3");
    }
    if (type=="publicity") {
        $("#pageContext").html("< 信息公示").attr("href","/AppService/article/infoPublicity.shtml");
        $(".nav.fr ul li a").removeClass("current").eq(5).addClass("current");
    }
    queryArticleContent();
    //queryHotFundByType();
    document.title = "招商财富_"+$("#title").text();
    console.log("1")

});
/* 查询详情 */
function queryArticleContent(){
    var articleId = getUrlParameter("articleId");
    articleId = removeSpecialStr(articleId);
    $.ajax({
        async:false,
        url: "/AppService/article/queryArticleContent.xhtml",
        data: {
            "articleId":articleId
        },
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {
        },
        success : function (data){
            $("#title").html(data.title);
            $("#publishDate").html(data.publishDate);
            $("#content").html(data.content);

        }
    });
}
/* 查询详情 */
function queryHotFundByType(){
    var articleId = getUrlParameter("articleId");
    articleId = removeSpecialStr(articleId);
    $.ajax({
        async:false,
        url: "/AppService/article/queryHotFundByType.xhtml",
        data: {},
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {
        },
        success : function (data){
            var fundInfo = data.fundInfoDtoV2;
            if(fundInfo != null){
                $("#fundLink").attr("href","/AppService/business/fund/fundDetail.shtml?fundid="+fundInfo.fundId);
                $("#adname").html(fundInfo.adname);
                $("#profit").html("<em>"+parseInt(numMulti(fundInfo.profit,100),10)+"</em><b></b><i>%</i><span>+</span>");
            }
        }
    });
}
/**
 * 图片尺寸超过容器时则把宽度变成百分之百
 */
$(document).ready(function(e) {
    $("#content img").each(function(){
        var _this=$(this);
        var image=new Image();
        image.src=$(this).attr("src");
        image.onload=function(){
            if(_this.width()>656){
                _this.css("width","100%")
            }
        }
    })
});