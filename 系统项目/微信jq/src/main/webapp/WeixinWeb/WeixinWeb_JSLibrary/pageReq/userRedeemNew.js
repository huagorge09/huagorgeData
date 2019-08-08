﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿var fundId = "";
var balanceTotal = 0;
var serialno = "";
var redeemAmount = 0;  //该交易账号下的总到期赎回份额
var redemptionShareQuty = 0; //数据库中当前订单的到期赎回份额
//高风险提示 购买读秒
$(document).ready(function (e) {
    queryUserName();
    $(".header .top-a h2").html("赎回");
    document.title = "赎回";
    fundId = getUrlParameter('fundId');
    fundId = fundId.replace('#', "");
    var period = "";
    period = getUrlParameter('period');
    period = period.replace('#', "");
    serialno = getUrlParameter('serialNo');
    serialno = serialno.replace('#', "");
//	queryUserWarehouse(tradeacco,fundId)
    if (serialno != null && serialno != "") {
        queryTradeInfoByTradeNo(serialno, period);
    }
    queryFundInfo(fundId, period);
    getUserRequest("userRedeem");/* 此处subPath为页面内行为 */

});

/**
 */
function goToFundInfo() {
    var id = $("#fundId").val();
    redirectUrl("/WeixinService/business/query/fundInfoNew.shtml?fundId=" + id);
}

/**
 * 验证赎回金额
 */
function checkedMoney() {
    var typeId = $("#typeId").val();
    var balance = 0;
    if (typeId == '0110') {
        balance = $('#subQuty').val();   //财富宝订单总份额
    } else {
        balance = $("#redemptionShare").text().replace(/,/g, "");   //长利可赎回份额
    }
    var money = $("#money").val().replace(/,/g, "");    //赎回份额
    if (typeId == '0110') {
        money = $("#money2").val().replace(/,/g, "");    //赎回份额
    }


    var re = /^[0-9]+.?[0-9]*$/;
    var flag = false;
    if (!re.test(money)) {
        errorRemark("赎回份额只能输入数字");
        return false;
    }

    var renewQuty = balance - money;
    renewQuty = parseFloat(renewQuty).toFixed(2);
    $(".renewQuty").html(formatNumber(unformat(renewQuty)));

    if (parseFloat(money) > parseFloat(balance)) {
        errorRemark("赎回份额不能大于可赎回份额")
    } else if (money == "") {
        errorRemark("赎回份额不能为空")
    } else {
        flag = true
    }
    if (flag == true) {
        flag = checkRedeemMoney();
    }

    money = parseFloat(money).toFixed(2);//赎回份额
    var latestNewValue = parseFloat($("#latestNewValue").val() ? $("#latestNewValue").val() : '1.0000'); //最新净值
    var redeemMoney = parseFloat(money * latestNewValue).toFixed(2);//预计赎回金额
    var reNewMoney2 = parseFloat(renewQuty * latestNewValue).toFixed(2);//预计存入下一期金额
    $("#money").val(formatNumber(unformat(money)));
    if (typeId == '0110') {
        $("#money2").val(formatNumber(unformat(money)));//到期赎回份额
        $("#redeemQuty").html(formatNumber(unformat(redeemMoney)));//预计赎回金额
        $("#reNewMoney").html(formatNumber(unformat(reNewMoney2)));//预计滚入下一期金额
        $('#redeemMoney').html(formatNumber(unformat(redeemMoney)))
        $('#reNewMoney2').html(formatNumber(unformat(reNewMoney2)))
        redeemTipsConfirm = false;
    }
    if(typeId == '0500'){
    	$("#moneyTotal").html(formatNumber(unformat(redeemMoney)));
    }
    return flag;
}


$("#money").change(function () {
    checkedMoney();
});

$("#money2").change(function () {
    checkedMoney();
});


//用户点击去下单
function userGotoRedeem() {
    var flag1 = checkedMoney();
    var custno = $("#custno").val();
    var tradeacco = $(".userBankList p span").attr("data-tradeacco");
    var money = $("#money").val().replace(/,/g, "");
    if (flag1) {

        $("#scroller").hide()
        $("#selectBankSection").removeClass("hide")
        $("#moneyText").text($("#money").val() + "份")
        var moneyFromat = toUpperCase(money).replace(/分|整/g, "").replace("角", "拾");
        if (moneyFromat.substring(moneyFromat.length - 1, moneyFromat.length) == '元') {
            moneyFromat = moneyFromat.replace("元", "");
        } else {
            moneyFromat = moneyFromat.replace("元", "点");
        }
        $("#uppercase").text(moneyFromat)
        $.ajax({
            async: false,
            url: "/WeixinService/business/queryTradeInfoByCustNo.xhtml",
            type: "post",
            dataType: 'json',
            data: {
                "fundid": fundId,
                "tradeAcco": tradeacco,
                "subquty": money
            },
            success: function (res) {
                if (res.resultCode == "0000") {
                    var tradeinfo = res.data;
                    var bankAcco = tradeinfo.bankAcco;
                    var bankLongName = tradeinfo.bankLongName;
                    var bankAcnm = tradeinfo.bankAcnm;
                    var bankAcco1 = bankAcco.substring(bankAcco.length - 4, bankAcco.length);
                    $('.bank_01').text(bankLongName + "(尾号" + bankAcco1 + ")");
                    $('.bank_02').text(bankLongName);
                    $('.bank_03').text(bankAcnm);
                    $('.bank_04').text(bankAcco);
                    $('#tradeAcco').val(tradeinfo.tradeacco)
                    $('#serialNo').val(tradeinfo.serialno)
                    $('#bankAcco').val(tradeinfo.bankAcco)
                    $('#period').val(tradeinfo.period)
                    $("#custNo").val(tradeinfo.custno);
                }
            },
            error: function () {
                errorRemark("网络繁忙，请稍后再试。");
            }
        })

    }
}

/**
 *查询相关信息
 */
function queryFundInfo(fundId, period) {
    if ("" == period) {
        period = $('#period').val();
    }
    $.ajax({
        async: false,
        url: "/WeixinService/business/queryFundInfo.xhtml",
        data: {
            "fundId": fundId,
            "period": period
        },
        dataType: "json",
        type: 'post',
        cache: false,
        error: function (textStatus, errorThrown) {
            errorRemark("网络繁忙，请稍后再试。");
        },
        success: function (data) {
            if (data.returnCode == '0000') {
                if (data.fundInfo != null) {
                    var fundInfo = data.fundInfo;
                    $("#fundName").html(fundInfo.typeName + "-" + fundInfo.adname);
                    $("#fundId").val(fundInfo.fundId)

                    $("#adname").val(fundInfo.adname)
                    $("#paymentinter").val(fundInfo.paymentinter)
                    $('#latestNewValue').val(fundInfo.latestNewValue);
                    $('#typeId').val(fundInfo.typeId);
                    $('#minHoldingMoney').val(fundInfo.minHoldingMoney);
                    if (typeof (fundInfo.templetId) != undefined) {
                        $("#templetId").val(fundInfo.templetId);
                    }
                    $('.maturityDate').html(fundInfo.maturityDate);
                    if (fundInfo.typeId == '0110') {
                        $("#buyMoneySection").hide();
                        $("#buyMoneySection2").show();
                        $(".buy-money-input input").css({width: "45%"});
                        $(".header .top-a h2").html("到期资金安排");
                        document.title = "到期资金安排";
                    }
                } else {
                    errorRemark("当前产品不存在");
                }
            } else {
                errorRemark(data.returnMsg);
            }
        }
    });

}

/* 查询订单详情 */
function queryTradeInfoByTradeNo(serialno, period) {
    $.ajax({
        async: false,
        url: "/WeixinService/business/queryTradeInfoByTradeNo.xhtml",
        data: {
            "serialno": serialno,
            "period": null
        },
        dataType: "json",
        cache: false,
        type: "post",
        error: function (textStatus, errorThrown) {
        },
        success: function (data) {

            if (data.returnCode != null && data.returnCode == "0000") {
                if (data.dto == null && data.tradeList == null) {
                    errorRemark("当前订单信息有误");
                    redirectUrl("/WeixinService/business/query/fundListNew.shtml");
                    return;
                } else {
                    $(".totalQuty").html(formatNumber(unformat(parseFloat(data.dto.subquty).toFixed(2))));
                    $('#subQuty').val(data.dto.subquty);

                    var redemptionShare = (!data.dto.redemptionShare && data.dto.renew == "N") ? data.dto.subquty : data.dto.redemptionShare;
                    redemptionShare = redemptionShare ? redemptionShare : "0";

                    $('.renewQuty').html(formatNumber(unformat(parseFloat(data.dto.subquty - redemptionShare).toFixed(2))));
                    $("#money2").val(formatNumber(unformat(parseFloat(redemptionShare).toFixed(2))));    //赎回份额

                    // 最新净值
                    var latestNewValue = data.dto.fundInfoDtoV2.latestNewValue?data.dto.fundInfoDtoV2.latestNewValue:'1.0000';
                    // 预计赎回金额
                    var redeemMoney = redemptionShare * latestNewValue;
                    $('#redeemMoney').html(formatNumber(unformat(parseFloat(redeemMoney).toFixed(2))));
                    // 预计滚入下一期金额
                    var reNewMoney2 = (data.dto.subquty - redemptionShare) * latestNewValue;
                    $('#reNewMoney').html(formatNumber(unformat(parseFloat(reNewMoney2).toFixed(2))));
                    $('.bank_05').text(data.dto.bankLongName); //收款银行
                    $('.bank_06').text(data.dto.bankAcnm);	//收款人姓名
                    $('.bank_07').text(data.dto.bankAcco); //银行账号
                    //$('#custName').val(data.dto.bankAcnm);
                    $('#period').val(data.dto.period)
                    $('#tradeAcco').val(data.dto.tradeacco)
                    redemptionShareQuty = redemptionShare;
                }
            }
        },
        error: function () {
            errorRemark("网络繁忙，请稍后再试。");
        }
    })
}

/**
 * 查询用户持仓金额
 */
//function queryUserWarehouse(tradeacco,fundId){
//	 $.ajax({
//  	async:false,
//		url:"/WeixinService/business/queryCustTradeInfo.xhtml",
//		type:"post",
//		dataType:'json',
//		data:{
//			"fundCode":fundId,
//			"tradeAcco":tradeacco
//		},
//		success:function(res){
//		     if(res.returnCode=="0000"){
//		    	if(res.buySatte=="Y"){
//		    		var fundinfo = res.data;
//		    		var totalAmount=formatNumber(fundinfo.balance, ',')
//		    		$("#balance").val(fundinfo.balance);
//		    		$("#custNo").val(fundinfo.custno);
//		    		$("#redemptionShare").html(totalAmount);
//		    	}else{
//		    		$("#redemptionShare").html("0");
//		    	}
//		     }
//		},
//		error:function(){
//			errorRemark("网络繁忙，请稍后再试。"); 
//	   }
//	})
//}

/**
 * 用户提交赎回
 */
function fundTrade() {
    var serialNo = $('#serialNo').val();//订单号
    var tradeAcco = $('#tradeAcco').val();//交易账号
    var custno = $('#custNo').val();//客户编号
    var fundid = $('#fundId').val();//基金代码
    var mobile = $('#mobile').val();//电话
    var fundname = $('#adname').val();//产品名
    var paymentinter = $('#paymentinter').val();//到期日+？
    if (!paymentinter) {
        paymentinter = 1;
    }
    var money = $("#money").val().replace(/,/g, "");//赎回份额
    var bankAcco = $('#bankAcco').val();//银行卡号
    var period = $('#period').val();//期限
    if ($('#typeId').val() == '0110') {
        money = $("#money2").val().replace(/,/g, "");//赎回份额
        var flag1 = checkedMoney();
        if (flag1) {
            $.ajax({
                url: "/WeixinService/business/updateOrderRedemptionShare.xhtml",
                data: {
                    "serialno": serialno,
                    "tradeamt": money
                },
                dataType: "json",
                cache: false,
                type: "POST",
                success: function (data) {
                    if (data.returnCode == "0000") {

                        $("#buyMoneySection2").hide();
                        $("#offLineSuccessSection2").removeClass("hide");
                    } else {
                        errorRemark("网络繁忙，请稍后再试。");
                        setTimeout(function () {
                            location.href = "/WeixinService/business/query/fundInfoNew.shtml?fundId=" + fundid + "&period=" + period
                        }, 3000);
                    }
                },
                error: function (textStatus, errorThrown) {
                    errorRemark("网络繁忙，请稍后再试。");
                }
            })
        }
    } else {
        $.ajax({
            url: "/WeixinService/business/addOpLog.xhtml",
            data: {
                "custNo": custno,
                "optType": "03"//主动赎回
            },
            dataType: "json",
            cache: false,
            type: "POST",
            success: function (data) {
                if (data.returnCode == "0000") {
                } else {
                    errorRemark("网络繁忙，请稍后再试。");
                }
            },
            error: function (textStatus, errorThrown) {
                errorRemark("网络繁忙，请稍后再试。");
            }
        })

        $.ajax({
            async: false,
            url: "/WeixinService/business/redemptionOrder.xhtml",
            data: {
                "serialNo": serialNo,
                "tradeAcco": tradeAcco,
                "custno": custno,
                "fundid": fundid,
                "mobile": mobile,
                "money": money,
                "bankAcco": bankAcco,
                "fundname": fundname,
                "paymentinter": paymentinter
            },
            dataType: "json",
            cache: false,
            type: "POST",
            success: function (data) {
                if (data.returnCode == "0000") {
                    $("#selectBankSection").hide();
                    $("#offLineSuccessSection").removeClass("hide");
                } else {
                    errorRemark("系统异常，赎回失败");
                    setTimeout(function () {
                        location.href = "/WeixinService/business/query/fundInfoNew.shtml?fundId=" + fundid + "&period=" + period
                    }, 3000);

                }
            },
            error: function (textStatus, errorThrown) {
                errorRemark("网络繁忙，请稍后再试。");
            }
        })

    }

}

function checkRedeemMoney() {
    // 活期类产品输入金额时校验是否低于最低持仓金额
    var typeId = $("#typeId").val();
    var latestNewValue = $("#latestNewValue").val();
    if (!latestNewValue) {
        latestNewValue = "1";
    }
    if (typeId == '0500' || typeId == '0110') {
        var inputbalance = $("#money").val().replace(/,/g, ""); //输入的份额
        if (typeId == '0110') {
            inputbalance = $("#money2").val().replace(/,/g, ""); //输入的份额
        }
        var showInputbalance = inputbalance;
        var custName = $('#custName').val();
        var userHasMoney = parseFloat(parseFloat(balanceTotal * latestNewValue).toFixed(2)); // 用户拥有的份额  * 最新净值 = 当前本金+利益
        var userRedeemMoney = parseFloat(parseFloat(inputbalance * latestNewValue).toFixed(2)); // 用户赎回的份额  * 最新净值 = 赎回的金额
        var surplusMoney = parseFloat(parseFloat(userHasMoney - userRedeemMoney).toFixed(2)); // 剩余金额
        var showSurplusMoney = parseFloat(parseFloat(balanceTotal - inputbalance).toFixed(2));
        if (typeId == '0110') {
        	surplusMoney = parseFloat(parseFloat(surplusMoney - parseFloat(parseFloat(redeemAmount * latestNewValue).toFixed(2))).toFixed(2));
        	showSurplusMoney = parseFloat(parseFloat(showSurplusMoney - redeemAmount).toFixed(2));
        }
        var minHoldingMoney = parseFloat($('#minHoldingMoney').val()); // 最低持仓金额
        if (surplusMoney != 0 && surplusMoney < minHoldingMoney && !redeemTipsConfirm) {
            var content = $('#redeemTips').html();
            $("#supportBankList").html("尊敬的{custName}，您设置的赎回份额 ({redeemMoney}份) 导致滚存入下一期份额 ({c}份) 低于产品最低持有金额 ({d}元)，请全部赎回或输入更少的份额，谢谢。");
            content = $('#redeemTips').html();

            showSurplusMoney = formatNumber(parseFloat(showSurplusMoney).toFixed(2));
            showInputbalance = formatNumber(parseFloat(showInputbalance).toFixed(2));
            inputbalance = formatNumber(parseFloat(inputbalance).toFixed(2));
            surplusMoney = formatNumber(parseFloat(surplusMoney).toFixed(2));
            minHoldingMoney = formatNumber(parseFloat(minHoldingMoney).toFixed(2));
            content = content.replace("{redeemMoney}", showInputbalance).replace("{c}", showSurplusMoney).replace("{d}", minHoldingMoney).replace("{custName}", custName);
            //content = content.replace("{surplusMoney}",surplusMoney.toFixed(2)).replace("{minHoldingMoney}",minHoldingMoney+"").replace("{redeemMoney}",inputbalance);
            $('#redeemTips').html(content);
            $('#redeemTips').show();
            return false;
        } else {
            return true;
        }
    }
    return true;
}

function closeTip() {
    $('#redeemTips').hide();
    //$('#redeemTips').html('您赎回后剩余金额为{surplusMoney}，低于该产品最低持仓金额{minHoldingMoney}，系统将会将您剩余份额全部赎回');
}

var redeemTipsConfirm = false;

function closeTipAndGotoRedeem() {
    $('#redeemTips').hide();
    redeemTipsConfirm = true;
    if ($('#typeId').val() != '0110') {
        userGotoRedeem();
    }
}

/**
 * 查询用户中文姓名
 */
function queryUserName() {
    $.ajax({
        async: false,
        url: "/WeixinService/business/queryUserinfo.xhtml",
        data: "",
        dataType: "json",
        cache: false,
        type: "post",
        error: function (textStatus, errorThrown) {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function (data) {
            if (data.returnCode != null && data.returnCode == "0000") {
                $("#custName").val(data.custName);
            }
        }
    });
}

$(function () {

    $(".userBankList p").click(function () {
        $("#selection-tradeAcco-list").show()
    })
    $("#cover-content-x").click(function () {
        $("#selection-tradeAcco-list").hide()
    })

    $(document).on("click", "#bankList_off li", function () {
        $(this).addClass("act").siblings().removeClass("act");
        setTimeout(function () {
            $("#selection-tradeAcco-list").hide()
        }, 300)
        $("#money").val("")
    })

    $(".buy-money-input i").click(function () {
        var total = $("#redemptionShare").text();
        $("#money").val(formatNumber(total));
        var latestNewValue = parseFloat($("#latestNewValue").val() ? $("#latestNewValue").val() : '1.0000');
        $("#moneyTotal").html(formatNumber(parseFloat(total.replace(/,/g, "")*latestNewValue).toFixed(2)));
    })

    $(".strongFont").click(function () {
        if ($('#typeId').val() == '0110') {
        	var latestNewValue = parseFloat($("#latestNewValue").val() ? $("#latestNewValue").val() : '1.0000');
            var total = $("#subQuty").val();
            $("#money2").val(formatNumber(total));
            $('#redeemMoney').text(formatNumber(parseFloat(total.replace(/,/g, "")*latestNewValue).toFixed(2)));
            $('#reNewMoney').text(formatNumber('0.00'));

        }
    })

    var fundid = $('#fundId').val();//基金代码
    var tradeacco = getUrlParameter('tradeacco');

    $.ajax({
        async: false,
        url: "/WeixinService/business/queryCanRedeemBankInfoByFundCode.xhtml",
        data: {
            "fundCode": fundid,
        },
        dataType: "json",
        cache: false,
        type: "POST",
        success: function (data) {
            if (data.resultCode == "0000") {
                var list = data.data;
                var first = data.data[0];
                var html = "";
                if (tradeacco == "") {
                    $(".userBankList p span").html(data.data[0].banklongname + '(尾号' + data.data[0].bankaccodisplay.substring(14, 19) + ')')
                    $(".userBankList p span").attr("data-total", data.data[0].total);
                    $(".userBankList p span").attr("data-balance", data.data[0].balance);
                    $(".userBankList p span").attr("data-tradeacco", data.data[0].tradeacco);
                    $("#totalRedemption").html(formatNumber(data.data[0].total));
                    $("#redemptionShare").html(formatNumber(data.data[0].balance));
                    var latestNewValue = parseFloat($("#latestNewValue").val() ? $("#latestNewValue").val() : '1.0000');
                    $("#bt1").html(formatNumber(parseFloat(data.data[0].balance.replace(/,/g, "")*latestNewValue).toFixed(2)));
					$("#moneyTotal").html('0.00');
                    $('#tradeAcco').val(first.tradeacco);
                } else {
                    $(document).ready(function () {
                        $("#bankList_off li").each(function () {
                            if (tradeacco == $(this).attr("data-tradeacc")) {
                                sureTradeAcco(this);
                                $(this).addClass("act").siblings().removeClass("act")
                            }
                        })
                    })
                }

                for (var i = 0; i < list.length; i++) {
                    var bankName = data.data[i].banklongname + '(尾号' + data.data[i].bankaccodisplay.substring(14, 19) + ')'
                    if (i == 0) {
                        html += '<li data-balance=' + formatNumber(list[i].balance) + ' data-total=' + formatNumber(list[i].total) + ' data-tradeacc=' + list[i].tradeacco + ' data-bankname=' + list[i].banklongname + '  class="act" onclick="sureTradeAcco(this);"><em>' + bankName + '</em><span>可赎回<i>' + formatNumber(data.data[i].total) + '份</i></span></li>'
                    } else {
                        html += '<li data-balance=' + formatNumber(list[i].balance) + ' data-total=' + formatNumber(list[i].total) + ' data-tradeacc=' + list[i].tradeacco + ' data-bankname=' + list[i].banklongname + ' onclick="sureTradeAcco(this);"><em>' + bankName + '</em><span>可赎回<i>' + formatNumber(data.data[i].total) + '份</i></span></li>'
                    }
                    if (list[i].tradeacco == $('#tradeAcco').val()) {
                        balanceTotal = parseFloat(list[i].balance);
                    }
                }


                $("#bankList_off").append(html)
            } else {
                errorRemark("网络繁忙，请稍后再试。");
            }
        },
        error: function (textStatus, errorThrown) {
            errorRemark("网络繁忙，请稍后再试。");
        }
    });
	queryTradeInfoList();
})

function sureTradeAcco(obj) {
    var bankName = $(obj).find("em").text();
    var tradeacc = $(obj).attr("data-tradeacc");
    var balance = $(obj).attr("data-balance");
    var total = $(obj).attr("data-total");
    $(".userBankList p span").attr("data-total", total)
    $(".userBankList p span").attr("data-tradeacco", tradeacc)
    $(".userBankList p span").text(bankName)
    $("#totalRedemption").html(total)
    $("#redemptionShare").html(balance)
    $("#money2").val().replace(/,/g, "");
    var latestNewValue = parseFloat($("#latestNewValue").val() ? $("#latestNewValue").val() : '1.0000');
    $("#bt1").html(formatNumber(parseFloat(balance.replace(/,/g, "")*latestNewValue).toFixed(2)));
    $("#moneyTotal").html('0.00');
    balanceTotal = balance.replace(/,/g, "");
}

function queryTradeInfoList() {

    var totalSubQuty = 0;
    /* 查询财富宝产品续存中的订单列表 */
    $.ajax({
        async: false,
        url: "/WeixinService/business/queryTradeInfoList.xhtml",
        data: {
            "fundId": fundId,
            "applyst": 'G'
        },
        dataType: "json",
        cache: false,
        type: "post",
        error: function (textStatus, errorThrown) {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function (data) {
            var list = data.list;
            if (list != null && list.length > 0) {
                $.each(list, function (i, item) {
                    if (item.tradeacco == $('#tradeAcco').val() && item.serialno != serialno) {
                        item.redemptionShare = (!item.redemptionShare && item.renew == "N") ? item.subquty : item.redemptionShare;
                        item.redemptionShare = item.redemptionShare ? item.redemptionShare : "0";
                        redeemAmount += parseFloat(item.redemptionShare);
                        totalSubQuty += parseFloat(item.subquty);
                    }

                });
            }
        }
    });
}

// 该金额用昨日净值估计，仅供参考
$('.questionRedeem').click(function () {
    $('#redeemMoneyBox').show()
    $('#back_pop').show()
})

// 关闭持仓金额提示框
function closeHoldTip() {
    $('#redeemMoneyBox').hide()
    $('#back_pop').hide()
}