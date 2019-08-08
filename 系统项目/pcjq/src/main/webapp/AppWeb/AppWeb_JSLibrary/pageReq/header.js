$(document).ready(function () {
    $(".page-top-right .weixin").hover(function () {
        $(".weixin_img").show();
    }, function () {
        $(".weixin_img").hide();
    })
    headerInfo();
//  document.title = "招商财富";
})
/*退出登录*/
function checkOutLogin() {
    $.ajax({
        async: false,
        url: "/AppService/setUp/checkOutLogin.xhtml",
        dataType: "json",
        type: "POST",
        data: {},
        cache: false,
        error: function (textStatus, errorThrown) {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function (data) {
            window.location.href = "/login/login.shtml";
        }
    });
}

function headerInfo() {
    $.ajax({
        async: false,
        url: "/AppService/setUp/headerInfo.xhtml",
        dataType: "json",
        type: "POST",
        data: {},
        cache: false,
        error: function (textStatus, errorThrown) {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function (data) {
            if (data.returnCode == '0000') {
                $("#loginYes").show();
                $("#loginNo").hide();
                $("#userId").val(data.cmfUserId);
                $("#login_userinfo").html(data.name).prop("href", "/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO");
            } else {
                $("#loginNo").show();
                $("#loginYes").hide();
            }
            if($("#loginYesPublic").is(":visible")){
                $("#loginNo").hide();
            }
        }
    });
}
/* 弹窗提示关闭 */
function close_tips(_id) {
    $("#" + _id).hide();
}
/* 弹窗提示显示  无按钮 */
function show_tips3(tipsMsg) {
    $("#hint_tips_btn3").show().find("span").html(tipsMsg);
}
/* 弹窗提示显示  单按钮 */
function show_tips(tipsMsg, tipsBtn) {
    if (!tipsBtn) {
        tipsBtn = "确认";
    }
    $("#hint_tips_btn1").show().find("span").html(tipsMsg);
    $("#hint_tips_btn1").show().find("input").val(tipsBtn);
}
/* 弹窗提示显示  双按钮 */
function show_tips2(tipsMsg, btn1, btn2, btn1Link, Func) {
    Func = Func || function () {
        };
    $("#hint_tips_btn2").show().find("span").html(tipsMsg);
    $("#tips_btn1_btn").val(btn1).attr("onclick", "gotoUrl('" + btn1Link + "')");
    $("#tips_btn2_btn").val(btn2).attr("onclick", Func + "()");
}
/* 弹窗提示关闭 */
function close_tips2() {
    $("#hint_tips_btn2").hide();
}
/* 跳转链接 */
function gotoUrl(link) {
    window.location.href = link;
}
/* 新窗口打开链接 */
function openToUrl(link) {
    window.open(link);
}
