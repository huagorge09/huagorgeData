var userIntegralNow; //用户当前积分
var isExComplete; //是否成功兑换
var isOnceSubmit;
/*
  兑换积分页面js
 */
$(function () {
    queryExThingInit();
    queryTHeDeatail()

})

//  初始化兑换物品的列表
function queryExThingInit() {
    //获取父节点
    var cardBox = $('#cardBox');
    $.ajax({
        url: '/WeixinService/business/integral/getIntegralGoods.xhtml',
        dataType: 'json', //服务器返回json格式数据
        type: 'get', //HTTP请求类型
        success: function (data) {
            if (data.errcode == '0000') {
                //动态加载兑换品列表
                var html = "";
                var type;
                $.each(data.data, function (i, e) {
                    if (e.awardName == '京东卡') {
                        type = 'jdicon';
                    } else if (e.awardName == '话费卡') {
                        type = 'phoneRateIcon';
                    }
                    html += '<div>' +
                        '<span class="prizeIcon ' + type + '"></span>' +
                        '<span class="prizeName">' + e.awardName + '</span>' +
                        '<span class="prizekAmount">' + e.awardDetail + '</span>' +
                        '<span class="toExchange" onclick="toExchange(\'' + e.awardid + '\',\'' + e.awardName + '\',\'' + e.awardDetail +
                        '\',\'' + (-e.integralChange) + '\')">兑换</span></div>'
                })
                $("#cardBox").append(html)
            }
        },
        error: function (xhr, type, errorThrown) {
            console.log("请求未成功")
        }
    });
}
//  使用积分列表 兑换产品按钮
function toExchange(awardid, awardName, awardDetail, integralChange) {
    isOnceSubmit = false;
    if (integralChange > parseFloat($("#scoreNum").text())) {
        $("#intggralNoth").show()
    } else {
        $('.awardName').html(awardName)
        $('.awardDetail').html(awardDetail)
        $('.integralChange').html(integralChange)
        $('#awardid').val(awardid)
        $(".isSureExchange").show();
    }
    // 调出蒙层
    $('.backLayer').show();

}
//  返回上一级页面
function goback() {
    window.history.go(-1)
}

// 关闭兑换弹出框
function closePop() {
    $(".isSureExchange").hide()
    // 关闭蒙层
    $('.backLayer').hide()
    $('#awardid').val('')
}

//  积分兑换确认按钮
function submitExchange(obj) {
    var goodsId = $('#awardid').val() //商品id
    var integralChange = $('.integralChange').html() //积分变化
    integralChange = '-' + integralChange;
    integralChangeX = parseInt(integralChange)
    var integralBeforeChange = userIntegralNow; //当前用户积分
    if (isOnceSubmit == false) {
        isOnceSubmit = true;
        queryTHeDeatail()
        $.ajax({
            url: '/WeixinService/business/integral/goodsExchange.xhtml',
            data: {
                'goodsId': goodsId,
                'integralChange': integralChangeX,
                'integralBeforeChange': integralBeforeChange
            },
            dataType: 'json', //服务器返回json格式数据
            type: 'post', //HTTP请求类型
            success: function (data) {
                if (data) {
                    $('.backLayer').show()
                    if (data.data == '0') {
                        var ze = '<p class="exError">兑换失败</p>';
                        $('#successDetail').html(ze)
                    } else if (data.data == '1') {
                        var be = '<span>恭喜您成功兑换<i class="awardDetail"></i><i class="awardName"></i>，</span>' +
                            '<span>稍后将把兑换信息发送到您<span class="phone"></span>手</span>' +
                            '<span>机上，请注意查收</span>';
                        // 只有成功返回才可以关闭弹框
                        $('#successContent').html(be)
                        queryTHeDeatail(); //及时更新用户积分信息
                    } else if (data.errcode != '0000') {
                        var ze = '<p class="exError">网络繁忙，请稍后再试</p>';
                        $('#successDetail').html(ze)
                    }
                    $(".isSureExchange").hide();
                    $('.successWindow').show()
                }
            },
            error: function (xhr, type, errorThrown) {
                console.log(type, errorThrown)
            }

        });
        //  请求完成后就更改 只有false才可点击提交(重新点击兑换列表才可兑换)
        isExComplete = true;
    }


}


// 关闭成功兑换窗口
function closeSuccess() {
    $('.successWindow').hide();
    // 关闭蒙层
    $('.backLayer').hide()

}

//  查询积分详情信息
function queryTHeDeatail() {
    $.ajax({
        url: '/WeixinService/business/integral/getIntegralInfo.xhtml',
        data: {},
        dataType: 'json', //服务器返回json格式数据
        type: 'get', //HTTP请求类型
        success: function (data) {
            $('#scoreNum').html(data.integral)
            userIntegralNow = data.integral;
        },
        error: function (xhr, type, errorThrown) {

        }
    });

}
