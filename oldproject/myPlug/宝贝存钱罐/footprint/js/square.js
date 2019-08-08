$(function () {
    page();
});
var page = function () {

    //绐��婊��
    window.addEventListener("scroll",function (e) {
        tabFixed();//tab�哄�
        pullLoad();//涓����〉��浇
    });

    //褰����〃
    var luYinIScroll = new iScroll('luYinList', {
        vScrollbar:false,
        hScrollbar:false,
        scrollX: true,
        scrollY: false
    });

    //����ㄥ�绱��杞藉�姣��璁＄�瀹藉害�����
    $("#luYinList .scroller").width($("#luYinList").find(".item").length*$("#luYinList").find(".item").width());
    luYinIScroll.refresh();

    topTitle();//椤堕�������

    footprintOperation();//瓒宠抗���

    $("#luYin").on("click",function () {
       location.href = "tape.html";
        return false
    });

    function topTitle() {
        //椤堕����灞��
        $("#zhanKai").on("click",function () {
            $(this).parents(".title").hide();
            $("#shouQi").parents(".title").show();
        });

        //椤堕�����惰捣
        $("#shouQi").on("click",function () {
            $(this).parents(".title").hide();
            $("#zhanKai").parents(".title").show();
        });

        //璇�����
        $("#huaTiTongZhi").on("click",function () {
            if($(this).hasClass("yes")){
                common.affirmHintBox({
                    content:"������灏��娉��涓���寸����棰����,
                    callback:function (rel) {
                        if(rel == "confirm"){
                            $("#huaTiTongZhi").removeClass("yes");
                            common.autoHintBox({
                                content:"������"
                            });
                        }
                    }
                });
            } else {
                $(this).addClass("yes");
                common.autoHintBox({
                    title:"璁剧疆������",
                    content:"涓��娲诲��存�涓��绗���堕����浣��~"
                });
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
        //tab���
        $("#square-tab .option").on("click",function () {
            $(this).addClass("current").siblings().removeClass("current");
        });

        //瓒宠抗�瑰�璺宠浆
        var delayed;
        $(".dayItem").on("click",function () {
            var that = $(this);
            $(this).addClass("dayItemClick");
            clearTimeout(delayed);
            delayed = setTimeout(function () {
                that.removeClass("dayItemClick");
            },300);
            if($(this).hasClass("ad")){
                location.href = "topic.html";
            }else {
                location.href = "footprintDetails.html";
            }
        });

        //璇��
        $(".dayItem .pingLun").on("click",function () {
            location.href = "footprintDetails.html?source=comment";
            return false
        });

        //��韩
        $(".dayItem .share").on("click",function () {
            common.shareBox("��翰��滑�ㄨ�濂芥椿�ㄥ�~");
            return false
        });

        //澶村��瑰�
        $(".dayItem .head").on("click",function () {
            location.href="footprintHomePage.html";
            return false
        });
    }

    //婊����浇
    function pullLoad() {
        //婊���颁�搴��
        if(common.isScrollBottom.scrollBottom(3)){
            console.log("aa");
            common.addLoad.haveInHand();//寮����浇
            setTimeout(function () {
                common.addLoad.end();//��浇缁��
            },2000);
            // common.addLoad.noMore();//娌℃��村�浜�
        }
    }
};

