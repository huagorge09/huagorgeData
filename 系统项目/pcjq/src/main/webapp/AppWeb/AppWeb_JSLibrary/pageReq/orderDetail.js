var i = 0;
var k = 0;
$(document).ready(function () {
   
    $(".nav.fr ul li a").removeClass("current");
    document.title = "订单详情_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
    queryTradeInfoByTradeNo();
    queryMyBankCard();
    $(".box-third .content .box01").click(function () {
        $(this).toggleClass("act").siblings().removeClass("act");
    });
    $(window).load(function () {
        $(".select-text-list").mCustomScrollbar();
    });
    /* 产品详情底部问答 b */
    $(".wordlimit").each(function (i) {
        var divH = $(this).height();
        var $p = $("p", $(this)).eq(0);
        while ($p.outerHeight() > divH) {
            $p.text($p.text().replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
        }
        ;
    });
    $(".box-third .content .box01").toggle(
        function () {
            $(this).addClass("act").siblings().removeClass("act");
            var ellipsisText = $(this).find(".right p").attr("data-title");
            $(this).find(".right p").html(ellipsisText);
        },
        function () {
            if ($(this).hasClass("act")) {
                $(this).removeClass("act").siblings().removeClass("act");
                $(".wordlimit").each(function (i) {
                    var divH = $(this).height();
                    var $p = $("p", $(this)).eq(0);
                    while ($p.outerHeight() > divH) {
                        $p.text($p.text().replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
                    }
                });
            } else {
                $(this).addClass("act").siblings().removeClass("act");
                var ellipsisText = $(this).find(".right p").attr("data-title");
                $(this).find(".right p").html(ellipsisText);
            }
        }
    );
   
    
    /* 产品详情底部问答 e */
})
/* 下一张验证码 修改银行卡 */
function getRandomCode() {
    $("#rondomCodeImg").attr("src", "/AppService/setUp/buildimageservlet.xhtml?count=" + i);
    i++;
}
function getRandomCode2() {
    $("#rondomCodeImg2").attr("src", "/AppService/setUp/buildimageservlet.xhtml?count=" + i);
    i++;
}
/* 下一张验证码 修改金额 */
function getRandomCode1() {
    $("#rondomCodeImg1").attr("src", "/AppService/setUp/buildimageservlet.xhtml?count=" + k);
    k++;
}
/* 查询订单信息 */
function queryTradeInfoByTradeNo() {
    var serialno = getUrlParameter("serialno");
    var period = getUrlParameter("period");
    serialno = removeSpecialStr(serialno);
    period = removeSpecialStr(period);
    $.ajax({
        async: false,
        url: "/AppService/business/queryTradeInfoByTradeNo.xhtml",
        type: "post",
        dataType: 'json',
        data: {
            serialno: serialno,
            period: period
        },
        error: function () {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function (data, textStatus) {
            var dto = data.appointRequestDto;
            if (data.appointRequestDto != null) {

                var fundInfoDto = data.appointRequestDto.fundInfoDtoV2;
                
                var appointRequestDto = data.appointRequestDto;
                var renew = data.appointRequestDto.renew;

                var periods = data.appointRequestDto.period;

                var isSubPrdAppraisement = fundInfoDto.isSubPrdAppraisement;
                var prjLName = fundInfoDto.prjLName;
                var outerId = fundInfoDto.outerId;

                if (fundInfoDto != null) {
                    var typeId = fundInfoDto.typeId;
                    $("#typeId").val(typeId);
                    $("#period").val(periods);
                    serialno = dto.serialno;
                    $("#fundId").val(fundInfoDto.fundId);


                    $("#tradeacco").val(dto.tradeacco);
                    $("#serialno").val(dto.serialno);
                    $("#money").val(fundInfoDto.money);
                    /* 隐藏input赋值 */
                    $("#subamt").val(dto.subamt);
                    $("#fee").val(dto.fee || 0);
                    $("#orderType").val(dto.orderType);
                    $("#money").val(formatNumber(dto.subamt, ','));
                    $("#oldMoney").val(dto.subamt);
                    $("#termInDay").val(dto.fundInfoDtoV2.termInDay);
                    $("#displayLimit").val(dto.fundInfoDtoV2.displayLimit);
                    $("#productMoney").val(dto.fundInfoDtoV2.money);
                    $("#moneyStep").val(dto.fundInfoDtoV2.moneyStep);
                    $("#expectInCome").val(dto.benefit);
                    $("#isSubPrdAppraisement").val(isSubPrdAppraisement);
                    $("#outerId").val(outerId);

                    var tradeList = data.tradeList;
                    if (tradeList != null && tradeList.length > 0) {
                        $("#bankacco").val(tradeList[0].bankacco);
                        $("#realBankName").val(tradeList[0].realBankName);
                    }
                    if (typeof (fundInfoDto.templetId) != undefined) {
                        $("#templetId").val(fundInfoDto.templetId);
                    }
                    var today = "" + fundInfoDto.currentWorkdate;
                    var appointEndDate = fundInfoDto.appointEndDate;
                    /* 预约结束日期 */
                    /* 购买类型 1，预约；2，认购；4，排队 */
                    if (daysBetween(today, appointEndDate) > 0) {
                        $("#buyType").val("2");
                        /* 认购单 */
                    } else {
                        if (fundInfoDto.displayLimit == 0) {
                            $("#buyType").val("4");
                            /* 排队单 */
                        } else {
                            $("#buyType").val("1");
                            /* 预约单 */
                        }
                    }
                    var profit = fundInfoDto.profit;
                    /* 产品预期收益率 */
                    var termInDay = fundInfoDto.termInDay;
                    /* 存续期限，以天为单位 */
                    var term = fundInfoDto.term;
                    /* 存续期限，存展现，不带单位 */
                    var termUnit = fundInfoDto.termUnit;
                    /* 存续期限单位 */
                    var scale = parseFloat(fundInfoDto.scale);
                    /* 产品规模 */
                    var displayLimit = parseFloat(fundInfoDto.displayLimit);
                    /* 页面展示剩余额度 */
                    var orderType = dto.orderType;
                    var money = parseFloat(fundInfoDto.money);
                    /* 最低认购金额 */
                    var moneyStep = parseFloat(fundInfoDto.moneyStep);
                    /* 认购步长 */

                    var sellPercent = numMulti(numDiv((scale - displayLimit), scale), 100).toFixed(0);
                    /* 产品额度(销售进度百分比) */

                    var profitTemp = numMulti(parseFloat(profit), 100).toFixed(2) + "";

                    var appointDate = fundInfoDto.appointDate;
                    /* 预约开始日期 */
                    var appointEndDate = fundInfoDto.appointEndDate;
                    /* 预约结束日期 */
                    var salesDate = fundInfoDto.salesDate;
                    /* 发售日 */
                    var subdeadLine = fundInfoDto.subdeadLine;
                    /* 认购截止 */
                    var interestDate = fundInfoDto.interestDate;
                    /* 起息日期 */
                    var maturityDate = fundInfoDto.maturityDate;
                    /* 到期日期 */
                    var paymentDate = fundInfoDto.paymentDate;
                    /* 产品到期清盘，预计打款日期 */
                    var currentWorkdate = fundInfoDto.currentWorkdate;
                    /* 当前工作日 */
                    var isDeadDay = daysBetween(currentWorkdate, salesDate);
                    var appPayMoneyText = formatDate1(salesDate) + " - " + formatDate1(subdeadLine) + "15:00前";//预约期打款时间
                    var isEnable = "";
                    if (daysBetween(currentWorkdate, maturityDate) == 0) {
                        isEnable = true;
                    } else {
                        isEnable = false;
                    }
                    var button = "";
                    if (renew == 'Y') {
                        if (isEnable) {
                            button = "<span style='font-size:16px;margin-left:11px;color:#5e5e5e'>到期自动续投</span>"
                        } else {
                            button = "<em><a href='javascript:void(0)' onclick='openTip(\"Y\")'>到期自动续投</a></>"
                        }
                    } else {
                        if (isEnable) {
                            button = "<span style='font-size:16px;margin-left:11px;color:#5e5e5e'>到期自动赎回</span>"
                        } else {
                            button = "<em> <a href='javascript:void(0)' " + isEnable + " onclick='openTip(\"N\")'>到期自动赎回</a></>"
                        }
                    }
                    getUserRequest("pc_orderDetail_product?fundId="+data.appointRequestDto.fundid+"&period="+data.appointRequestDto.period);
                    queryOrder(orderType);
                    /*-------------------- 订单详情展示  S --------------------*/
                    var subamt = dto.subamt;
                    var subquty = dto.subquty;
                    if (!isNaN(parseFloat(subamt)) && subamt > 0) {
                        if (orderType != null && orderType == "1") {/* 待付款 */
                            subamt = "<em>" + (formatNumber(parseFloat(subamt) + parseFloat((dto.fee || 0)))) + "</em><i>元</i>";
                        } else if (orderType != null && orderType == "2") {/* 已支付 */
                            subamt = "<em>" + (formatNumber(parseFloat(subamt) + parseFloat((dto.fee || 0)))) + "</em><i>元</i>";
                        } else if (orderType != null && orderType == "3") {/* 排队中 */
                            subamt = "<em>" + (formatNumber(parseFloat(subamt) + parseFloat((dto.fee || 0)))) + "</em><i>元</i>";
                        } else if (orderType != null && orderType == "6") {/* 已失效 */
                            subamt = "<em>" + (formatNumber(parseFloat(subamt) + parseFloat((dto.fee || 0)))) + "</em><i>元</i>";
                        } else if (orderType != null && orderType == "7") {/* 待确认 */
                            subamt = "<em>" + (formatNumber(parseFloat(subamt) + parseFloat((dto.fee || 0)))) + "</em><i>元</i>";
                        } else if (orderType != null && orderType == "8") {/* 受理中 */
                            subamt = "<em>" + (formatNumber(parseFloat(subamt) + parseFloat((dto.fee || 0)))) + "</em><i>元</i>";
                        } else {
                            subamt = "<em>" + formatNumber(parseFloat(subamt)) + "</em><i>元</i>";
                        }
                    }
                    var benefit = dto.benefit;
                    /* 预期收益 */
                    var shareAmt = dto.shareAmt;
                    /* 分配金额 */

                    if (isNaN(benefit) || isNaN(parseFloat(benefit))) {
                        benefit = "<em>---</em>";
                    } else {
                        if (parseFloat(benefit) > 0) {
                            benefit = "<em>+" + format(parseFloat(benefit)) + "</em><i>元</i>";
                        } else {
                            benefit = "<em>" + format(parseFloat(benefit)) + "</em><i>元</i>";
                        }
                    }

                    if (isNaN(shareAmt) || isNaN(parseFloat(shareAmt))) {
                        shareAmt = "<em>---</em>";
                    } else {
                        if (parseFloat(shareAmt) > 0) {
                            shareAmt = "<em>+" + format(parseFloat(shareAmt)) + "</em><i>元</i>";
                        } else {
                            shareAmt = "<em>" + format(parseFloat(shareAmt)) + "</em><i>元</i>";
                        }
                    }

                    $("#orderDetail .head-righ-con span.order-number").html("交易编号：" + dto.serialno);
                    /* 交易编号 */
                    /*-------------------- 订单详情展示  E --------------------*/

                    /*-------------------- 产品详情  个性化展示  S --------------------*/
                    if (typeId != null &&(typeId == "0100"||typeId == "0400")) { /* 固定收益 */
                        if (orderType != null && orderType == "1") {/* 待付款 */
                            if (daysBetween(currentWorkdate, salesDate) >= 0) {/* 销售期 */
                                $("#orderDetail h1.floatincome").html("认购中...");
                                $("#orderDetail .head-righ-con p.point").html("请尽快完成支付，确认产品份额");
                                /* 订单提示语 */
                            } else if (daysBetween(currentWorkdate, appointDate) >= 0) {/* 预约期 */
                                $("#orderDetail h1.floatincome").html("预约中...");
                                $("#orderDetail .head-righ-con p.point").html("请您于募集期(<em style='color:#ca132c;'>" + appPayMoneyText + "</em>)完成汇款");
                                /* 订单提示语 */
                            } else {/* TODO 要确定即不是认购期也不是预约期的订单该怎么显示，现在暂时显示认购中 */
                                $("#orderDetail h1.floatincome").html("认购中...");
                                $("#orderDetail .head-righ-con p.point").html("请尽快完成支付，确认产品份额");
                                /* 订单提示语 */
                            }
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("查看汇款信息").attr("href", "javascript:queryOrderShow();");
                        } else if (orderType != null && orderType == "2") {/* 已支付 */
                            $("#orderDetail h1.floatincome").html("已支付...").addClass("success");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            if(typeId == "0400"){
                            	$("#orderDetail .head-righ-con p.point").remove();
                            }else{
                            	$("#orderDetail .head-righ-con p.point").html("已收到您的款项，产品将于<em>" + formatDate1(interestDate) + "</em>正式起息");
                            }
                            /* 订单提示语 */
                        } else if (orderType != null && orderType == "3") {/* 排队中 */
                            $("#orderDetail h1.floatincome").html("排队中...").addClass("paidui");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("查看排队信息").attr("href", "javascript:queryOrderShow();");
                            $("#orderDetail .head-righ-con p.point").html("您的订单已进入排队等候，如有额度释放将第一时间通知您，额度详情请致电400-8878-555。");
                            /* 订单提示语 */
                        } else if (orderType != null && orderType == "4") {/* 存续中 */
                            $("#orderDetail h1.floatincome").html("存续中...").addClass("saveextend");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("买入金额：" + subamt);
                            /* 订单金额 */
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            $("#orderDetail .head-righ-con p.point").html("产品处于存续期，预计将于<em>" + formatDate1(maturityDate) + "</em>到期");
                            /* 订单提示语 */
                        } else if (orderType != null && orderType == "5") {/* 已到期 */
                            $("#orderDetail h1.floatincome").html("已到期...").addClass("timeout");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("买入金额：" + subamt);
                            /* 订单金额 */
                            //$("#orderDetail .head-righ-con div.order-info p:eq(1)").html("投资收益：" + benefit);
                            /* 预期收益 */
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            $("#orderDetail .head-righ-con p.point").html("产品已到期，预计将于5个工作日内完成收益分配");
                            /* 订单提示语 */
                        } else if (orderType != null && orderType == "6") {/* 已失效 */
                            $("#orderDetail h1.floatincome").html("订单已失效...").addClass("subsist");
                            $("#orderDetail .head-righ-con div.check-cancel a.check").addClass("hui").attr("href", "javascript:void(0)");
                            /* 按钮置灰 */
                            if (dto.payst != null && dto.payst == "Y") {
                                $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("支付金额：" + subamt);
                                /* 订单金额 */
                                $("#orderDetail .head-righ-con p.point").html("十分抱歉！您买入产品失败。");
                            } else {
                                $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                                /* 订单金额 */
                                $("#orderDetail .head-righ-con p.point").html("十分抱歉！由于您未在有效时间内完成汇款，订单已失效");
                            }
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                        } else if (orderType != null && orderType == "7") {/* 待确认 */
                            $("#orderDetail h1.floatincome").html("订单待确认...").addClass("verify");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("确认订单").attr("href", "/AppService/business/fund/confirmBuy.shtml?serialno=" + serialno);
                            $("#orderDetail .head-righ-con p.point").html("请尽快确认订单，完成汇款</span");
                        } else if (orderType != null && orderType == "8") {/* 受理中 */
                            $("#orderDetail h1.floatincome").html("待确认...").addClass("shouli");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            if(typeId == "0400"){
                            	$("#orderDetail .head-righ-con p.point").remove();
                            }else{
                            	$("#orderDetail .head-righ-con p.point").html("已收到您的款项，产品将于<em>" + formatDate1(interestDate) + "</em>正式起息");
                            }
                            /* 订单提示语 */
                        }
                 
                        $("#top_left_01").show();
                        $("#top_left_02,#top_left_03,#top_left_04").remove();
                        $("#profit_text").html("业绩报酬计提基准");

                        $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                        $("#term").html("<em>" + term + "</em>" + termUnit);
                        $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");
                        if (orderType != '4' && orderType != '5') {
                            $("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:" + sellPercent + "%'></b></span><i>" + sellPercent + "%</i>");
                            /* 投资进度 */
                            if (displayLimit > 0) {
                                $("#displayLimitText").html("只剩<em>" + numDiv(displayLimit, 10000) + "万</em>产品额度");
                                /* 产品额度提示语 */
                            } else {
                                $("#displayLimitText").html("<em></em>已售罄");
                                /* 产品额度提示语 */
                            }
                        } else {
                            $("#QAtelG").show();
                        }
                        if(typeId=="0400"){
                        	 $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                        }
                    }else if(typeId == "0500"){//活期理财产品
                    	debugger
                    	if (orderType != null && orderType == "1") {/* 待付款 */
                            if (daysBetween(currentWorkdate, salesDate) >= 0) {/* 销售期 */
                                $("#orderDetail h1.floatincome").html("认购中...");
                                $("#orderDetail .head-righ-con p.point").html("请尽快完成支付，确认产品份额");
                                /* 订单提示语 */
                            } else if (daysBetween(currentWorkdate, appointDate) >= 0) {/* 预约期 */
                                $("#orderDetail h1.floatincome").html("预约中...");
                                $("#orderDetail .head-righ-con p.point").html("请您于募集期(<em style='color:#ca132c;'>" + appPayMoneyText + "</em>)完成汇款");
                                /* 订单提示语 */
                            } else {/* TODO 要确定即不是认购期也不是预约期的订单该怎么显示，现在暂时显示认购中 */
                                $("#orderDetail h1.floatincome").html("认购中...");
                                $("#orderDetail .head-righ-con p.point").html("请尽快完成支付，确认产品份额");
                                /* 订单提示语 */
                            }
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("查看汇款信息").attr("href", "javascript:queryOrderShow();");
                        } else if (orderType != null && orderType == "2") {/* 已支付 */
                            $("#orderDetail h1.floatincome").html("已支付...").addClass("success");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
							if(fundInfoDto.apkind =='020' || fundInfoDto.apkind =='720' || fundInfoDto.apkind =='820'){
								$("#orderDetail .head-righ-con p.point").html("已收到您的款项，产品将于<em>" + formatDate1(fundInfoDto.interestDate) + "</em>正式起息");
							}else{
								$("#orderDetail .head-righ-con p.point").html("已收到您的款项，产品将于<em>" + formatDate1(fundInfoDto.interestDate1) + "</em>正式起息");
							}
                            /* 订单提示语 */
                        } else if (orderType != null && orderType == "3") {/* 排队中 */
                            $("#orderDetail h1.floatincome").html("排队中...").addClass("paidui");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("查看排队信息").attr("href", "javascript:queryOrderShow();");
                            $("#orderDetail .head-righ-con p.point").html("您的订单已进入排队等候，如有额度释放将第一时间通知您，额度详情请致电400-8878-555。");
                            /* 订单提示语 */
                        } else if (orderType != null && orderType == "4") {/* 存续中 */
                            $("#orderDetail h1.floatincome").html("存续中...").addClass("saveextend");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("买入金额：" + subamt);
                            /* 订单金额 */
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            $("#orderDetail .head-righ-con p.point").html("产品处于存续期");
                            /* 订单提示语 */
                        } else if (orderType != null && orderType == "5") {/* 已赎回*/
                            $("#orderDetail h1.floatincome").html("已赎回...").addClass("timeout");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("赎回份额：" + subquty);
                            //业绩报酬计提基准
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /* 预期收益 */
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            var paymentinter = "1";
                            if(fundInfoDto.paymentinter){
                            	paymentinter = fundInfoDto.paymentinter;
                            }
                            $("#orderDetail .head-righ-con p.point").html("该订单已赎回，资金预计将于T+" + paymentinter + "个工作日内到账，请注意查收");
                            /* 订单提示语 */
                        } else if (orderType != null && orderType == "6") {/* 已失效 */
                            $("#orderDetail h1.floatincome").html("订单已失效...").addClass("subsist");
                            $("#orderDetail .head-righ-con div.check-cancel a.check").addClass("hui").attr("href", "javascript:void(0)");
                            /* 按钮置灰 */
                            if (dto.payst != null && dto.payst == "Y") {
                                $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("支付金额：" + subamt);
                                /* 订单金额 */
                                $("#orderDetail .head-righ-con p.point").html("十分抱歉！您买入产品失败。");
                            } else {
                                $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                                /* 订单金额 */
                                $("#orderDetail .head-righ-con p.point").html("十分抱歉！由于您未在有效时间内完成汇款，订单已失效");
                            }
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                        } else if (orderType != null && orderType == "7") {/* 待确认 */
                            $("#orderDetail h1.floatincome").html("订单待确认...").addClass("verify");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("确认订单").attr("href", "/AppService/business/fund/confirmBuy.shtml?serialno=" + serialno);
                            $("#orderDetail .head-righ-con p.point").html("请尽快确认订单，完成汇款</span");
                        } else if (orderType != null && orderType == "8") {/* 受理中 */
                            $("#orderDetail h1.floatincome").html("待确认...").addClass("shouli");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            $("#orderDetail .head-righ-con p.point").html("已收到您的款项，产品将于<em>" + formatDate1(interestDate) + "</em>正式起息");

                            /* 订单提示语 */
                        }

                        $("#top_left_01").show();
                        $("#top_left_02,#top_left_03,#top_left_04").remove();
                        $("#profit_text").html("最新净值");
                        var latestNewValue = fundInfoDto.latestNewValue ? fundInfoDto.latestNewValue : '1.0000';
                    	$("#profit").html("<em>" + latestNewValue + "</em>");
                        $("#term_text").html("递增金额");
                        $("#term").html("<em>" + numDiv(fundInfoDto.moneyStep, 10000) + "</em>万");
                        $("#scale_text").html("起购金额");
                        $("#scale").html("<em>" + numDiv(fundInfoDto.money, 10000) + "</em>万");
                    } else if (typeId != null && (typeId == "0210" || typeId == "0220")) {/* 开放净值类产品/封闭净值类产品 */

                        if (orderType != null && orderType == "1") {/* 待付款 */

                            if (daysBetween(currentWorkdate, salesDate) >= 0) {/* 销售期 */
                                $("#orderDetail h1.floatincome").html("认购中...");
                                $("#orderDetail .head-righ-con p.point").html("请尽快完成支付，确认产品份额");
                                /* 订单提示语 */
                            } else if (daysBetween(currentWorkdate, appointDate) >= 0) {/* 预约期 */
                                $("#orderDetail h1.floatincome").html("预约中...");
                                $("#orderDetail .head-righ-con p.point").html("请您于募集期(<em style='color:#ca132c;'>" + appPayMoneyText + "</em>)完成汇款");
                                /* 订单提示语 */
                            } else {/* TODO 要确定即不是认购期也不是预约期的订单该怎么显示，现在暂时显示认购中 */
                                $("#orderDetail h1.floatincome").html("认购中...");
                                $("#orderDetail .head-righ-con p.point").html("请尽快完成支付，确认产品份额");
                                /* 订单提示语 */
                            }
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("查看汇款信息").attr("href", "javascript:queryOrderShow();");

                            if ((typeId == "0210" || typeId == "0220") && fundInfoDto.state == "0") {/* 申购 */
                                $("#top_left_03").show();
                                $("#top_left_01,#top_left_02,#top_left_04").remove();
                                $("#top_left_03 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);
                                /*最新净值*/
                                $("#top_left_03 #netval_profit").html("<em>" + numMulti((parseFloat(fundInfoDto.latestNewValue) - dto.confirmNav), 100).toFixed(2) + "</em>%");
                                /*累计收益率*/
                                $("#top_left_03 #netval_text").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                                /*描述*/
                                $("#top_left_03 #maxNetValue").html(parseFloat(fundInfoDto.maxNav).toFixed(4));
                                /*历史最高净值*/

                                if (typeId == "0210") {
                                    //$("#netval").show();
                                    /* 净值相关查询 */
                                     if ("n" == isSubPrdAppraisement.toLowerCase()) {
                                         $('#text1').html("注:该净值为《" + prjLName + "》 份额净值");
                                         $('#text1').show();
                                         $('#highchart1').css('margin', '0 auto 4px');
                                    //     queryEstimateByFundId("-12");
                                     } 
//                                         else {
                                    //     queryEstimateByFundId("-1");
                                    // }
                                    // queryEstimateByFundIdByPage("1");
                                    /* 查询产品净值 */
                                }
                            } else {
                                $("#top_left_02").show();
                                $("#top_left_01,#top_left_03,#top_left_04").remove();
                                $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                                $("#term").html("<em>" + term + "</em>" + termUnit);
                                if(fundInfoDto.groupId == "20003" && typeId == "0210"){
                                	$("#term").parent().html("");
                                }
                                $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");
                                if (displayLimit > 0) {
                                    $("#displayLimitText").html("只剩<em>" + numDiv(displayLimit, 10000) + "万</em>产品额度");
                                    /* 产品额度提示语 */
                                } else {
                                    $("#displayLimitText").css("margin-left", "184px").html("<em></em>已售罄");
                                    /* 产品额度提示语 */
                                }
                                $("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:" + sellPercent + "%'></b></span><i>" + sellPercent + "%</i>");
                                /* 投资进度 */
                            }
                        } else if (orderType != null && orderType == "2") {/* 已支付 */
                            $("#orderDetail h1.floatincome").html("已支付...").addClass("success");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            if(typeId == "0400"){
                            	$("#orderDetail .head-righ-con p.point").remove();
                            }else{
                            	$("#orderDetail .head-righ-con p.point").html("已收到您的款项，产品将于<em>" + formatDate1(interestDate) + "</em>正式起息");
                            }
                            /* 订单提示语 */

                            if ((typeId == "0210" || typeId == "0220") && fundInfoDto.state == "0") {/* 申购 */
                                $("#top_left_03").show();
                                $("#top_left_01,#top_left_02,#top_left_04").remove();
                                $("#top_left_03 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);
                                /*最新净值*/
                                $("#top_left_03 #netval_profit").html("<em>" + numMulti((parseFloat(fundInfoDto.latestNewValue) - dto.confirmNav), 100).toFixed(2) + "</em>%");
                                /*累计收益率*/
                                $("#top_left_03 #netval_text").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                                /*描述*/
                                $("#top_left_03 #maxNetValue").html(parseFloat(fundInfoDto.maxNav).toFixed(4));
                                /*历史最高净值*/

                                //$("#netval").show();
                                // /* 净值相关查询 */
                                 if ("n" == isSubPrdAppraisement.toLowerCase()) {
                                     $('#text1').html("注:该净值为《" + prjLName + "》 份额净值");
                                     $('#text1').show();
                                     $('#highchart1').css('margin', '0 auto 4px');
//                                     queryEstimateByFundId("-12");
                                 } 
                                //else {
                                //     queryEstimateByFundId("-1");
                                // }
                                // queryEstimateByFundIdByPage("1");
                                /* 查询产品净值 */
                            } else {
                                $("#top_left_02").show();
                                $("#top_left_01,#top_left_03,#top_left_04").remove();
                                $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                                $("#term").html("<em>" + term + "</em>" + termUnit);
                                if(fundInfoDto.groupId == "20003" && typeId == "0210"){
                                	$("#term").parent().html("");
                                }
                                $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");

                                $("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:" + sellPercent + "%'></b></span><i>" + sellPercent + "%</i>");
                                /* 投资进度 */
                                if (displayLimit > 0) {
                                    $("#displayLimitText").html("只剩<em>" + numDiv(displayLimit, 10000) + "万</em>产品额度");
                                    /* 产品额度提示语 */
                                } else {
                                    $("#displayLimitText").css("margin-left", "184px").html("<em></em>已售罄");
                                    /* 产品额度提示语 */
                                }
                            }
                        } else if (orderType != null && orderType == "3") {/* 排队中 */
                            $("#orderDetail h1.floatincome").html("排队中...").addClass("paidui");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("查看排队信息").attr("href", "javascript:queryOrderShow();");
                            $("#orderDetail .head-righ-con p.point").html("您的订单已进入排队等候，如有额度释放将第一时间通知您，额度详情请致电400-8878-555。");
                            /* 订单提示语 */
                            if ((typeId == "0210" || typeId == "0220") && fundInfoDto.state == "0") {/* 申购 */
                                $("#top_left_03").show();
                                $("#top_left_01,#top_left_02,#top_left_04").remove();
                                $("#top_left_03 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);
                                /*最新净值*/
                                $("#top_left_03 #netval_profit").html("<em>" + numMulti((parseFloat(fundInfoDto.latestNewValue) - dto.confirmNav), 100).toFixed(2) + "</em>%");
                                /*累计收益率*/
                                $("#top_left_03 #netval_text").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                                /*描述*/
                                $("#top_left_03 #maxNetValue").html(parseFloat(fundInfoDto.maxNav).toFixed(4));
                                /*历史最高净值*/

                                //$("#netval").show();
                                // /* 净值相关查询 */
                                 if ("n" == isSubPrdAppraisement.toLowerCase()) {
                                     $('#text1').html("注:该净值为《" + prjLName + "》 份额净值");
                                     $('#text1').show();
                                     $('#highchart1').css('margin', '0 auto 4px');
                                //     queryEstimateByFundId("-12");
                                 } 
                                 //else {
                                //     queryEstimateByFundId("-1");
                                // }
                                // queryEstimateByFundIdByPage("1");
                                /* 查询产品净值 */
                            } else {
                                $("#top_left_02").show();
                                $("#top_left_01,#top_left_03,#top_left_04").remove();
                                $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                                $("#term").html("<em>" + term + "</em>" + termUnit);
                                if(fundInfoDto.groupId == "20003" && typeId == "0210"){
                                	$("#term").parent().html("");
                                }
                                $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");

                                $("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:" + sellPercent + "%'></b></span><i>" + sellPercent + "%</i>");
                                /* 投资进度 */
                                if (displayLimit > 0) {
                                    $("#displayLimitText").html("只剩<em>" + numDiv(displayLimit, 10000) + "万</em>产品额度");
                                    /* 产品额度提示语 */
                                } else {
                                    $("#displayLimitText").css("margin-left", "184px").html("<em></em>已售罄");
                                    /* 产品额度提示语 */
                                }
                            }
                        } else if (orderType != null && orderType == "4") {/* 存续中 */
                            $("#orderDetail h1.floatincome").html("存续中...").addClass("saveextend");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("买入金额：" + subamt);
                            /* 订单金额 */
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("持仓盈亏：" + benefit);
                            /* 预期收益 */
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            $("#orderDetail .head-righ-con p.point").html("产品处于存续期，预计将于<em>" + formatDate1(maturityDate) + "</em>到期");
                            /* 订单提示语 */


                            $("#top_left_03").show();
                            $("#top_left_01,#top_left_02,#top_left_04").remove();

                            $("#QAtelF").show();

                            $("#top_left_03 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);
                            /*最新净值*/
                            $("#top_left_03 #netval_profit").html("<em>" + numMulti((parseFloat(fundInfoDto.latestNewValue) - dto.confirmNav), 100).toFixed(2) + "</em>%");
                            /*累计收益率*/
                            $("#top_left_03 #netval_text").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                            /*描述*/
                            $("#top_left_03 #maxNetValue").html(parseFloat(fundInfoDto.maxNav).toFixed(4));
                            /*历史最高净值*/

                            //$("#netval").show();
                            // /* 净值相关查询 */
                             if ("n" == isSubPrdAppraisement.toLowerCase()) {
                                 $('#text1').html("注:该净值为《" + prjLName + "》 份额净值");
                                 $('#text1').show();
                                 $('#highchart1').css('margin', '0 auto 4px');
                            //     queryEstimateByFundId("-12");
                             } 
                             //else {
                            //     queryEstimateByFundId("-1");
                            // }
                            // queryEstimateByFundIdByPage("1");
                            /* 查询产品净值 */
                        } else if (orderType != null && orderType == "5") {/* 已到期 */
                            $("#orderDetail h1.floatincome").html("已到期...").addClass("timeout");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("买入金额：" + subamt);
                            /* 订单金额 */
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("持仓盈亏：" + benefit);
                            /* 预期收益 */
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            $("#orderDetail .head-righ-con p.point").html("产品已到期，预计将于5个工作日内完成收益分配");
                            /* 订单提示语 */

                            $("#top_left_03").show();
                            $("#top_left_01,#top_left_02,#top_left_04").remove();

                            $("#QAtelF").show();

                            $("#top_left_03 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);
                            /*最新净值*/
                            $("#top_left_03 #netval_profit").html("<em>" + numMulti((parseFloat(fundInfoDto.latestNewValue) - dto.confirmNav), 100).toFixed(2) + "</em>%");
                            /*累计收益率*/
                            $("#top_left_03 #netval_text").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                            /*描述*/
                            $("#top_left_03 #maxNetValue").html(parseFloat(fundInfoDto.maxNav).toFixed(4));
                            /*历史最高净值*/

                            //$("#netval").show();
                            /* 净值相关查询 */
                             if ("n" == isSubPrdAppraisement.toLowerCase()) {
                                 $('#text1').html("注:该净值为《" + prjLName + "》 份额净值");
                                 $('#text1').show();
                                 $('#highchart1').css('margin', '0 auto 4px');
//                                 queryEstimateByFundId("-12");
                             } 
                            //else {
                            //     queryEstimateByFundId("-1");
                            // }
                            // queryEstimateByFundIdByPage("1");
                            /* 查询产品净值 */
                        } else if (orderType != null && orderType == "6") {/* 已失效 */
                            $("#orderDetail h1.floatincome").html("订单已失效...").addClass("subsist");
                            $("#orderDetail .head-righ-con div.check-cancel a.check").addClass("hui").attr("href", "javascript:void(0)");
                            /* 按钮置灰 */
                            if (dto.payst != null && dto.payst == "Y") {
                                $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("支付金额：" + subamt);
                                /* 订单金额 */
                                $("#orderDetail .head-righ-con p.point").html("十分抱歉！您买入产品失败");
                            } else {
                                $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                                /* 订单金额 */
                                $("#orderDetail .head-righ-con p.point").html("十分抱歉！由于您未在有效时间内完成汇款，订单已失效");
                            }
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/

                            if ((typeId == "0210" || typeId == "0220") && fundInfoDto.state == "0") {/* 申购 */
                                $("#top_left_03").show();
                                $("#top_left_01,#top_left_02,#top_left_04").remove();
                                $("#top_left_03 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);
                                /*最新净值*/
                                $("#top_left_03 #netval_profit").html("<em>" + numMulti((parseFloat(fundInfoDto.latestNewValue) - dto.confirmNav), 100).toFixed(2) + "</em>%");
                                /*累计收益率*/
                                $("#top_left_03 #netval_text").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                                /*描述*/
                                $("#top_left_03 #maxNetValue").html(parseFloat(fundInfoDto.maxNav).toFixed(4));
                                /*历史最高净值*/

                                // $("#netval").show();
                                // /* 净值相关查询 */
                                 if ("n" == isSubPrdAppraisement.toLowerCase()) {
                                     $('#text1').html("注:该净值为《" + prjLName + "》 份额净值");
                                     $('#text1').show();
                                     $('#highchart1').css('margin', '0 auto 4px');
                                //     queryEstimateByFundId("-12");
                                 } 
                                //else {
                                //     queryEstimateByFundId("-1");
                                // }
                                // queryEstimateByFundIdByPage("1");
                                /* 查询产品净值 */
                            } else {
                                $("#top_left_02").show();
                                $("#top_left_01,#top_left_03,#top_left_04").remove();
                                $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                                $("#term").html("<em>" + term + "</em>" + termUnit);
                                if(fundInfoDto.groupId == "20003" && typeId == "0210"){
                                	$("#term").parent().html("");
                                }
                                $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");

                                $("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:" + sellPercent + "%'></b></span><i>" + sellPercent + "%</i>");
                                /* 投资进度 */
                                if (displayLimit > 0) {
                                    $("#displayLimitText").html("只剩<em>" + numDiv(displayLimit, 10000) + "万</em>产品额度");
                                    /* 产品额度提示语 */
                                } else {
                                    $("#displayLimitText").css("margin-left", "184px").html("<em></em>已售罄");
                                    /* 产品额度提示语 */
                                }
                            }
                        } else if (orderType != null && orderType == "7") {/* 待确认 */
                            $("#orderDetail h1.floatincome").html("订单待确认...").addClass("verify");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("确认订单").attr("href", "/AppService/business/fund/confirmBuy.shtml?serialno=" + serialno);
                            $("#orderDetail .head-righ-con p.point").html("请尽快确认订单，完成汇款</span");

                            if ((typeId == "0210" || typeId == "0220") && fundInfoDto.state == "0") {/* 申购 */
                                $("#top_left_03").show();
                                $("#top_left_01,#top_left_02,#top_left_04").remove();
                                $("#top_left_03 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);
                                /*最新净值*/
                                $("#top_left_03 #netval_profit").html("<em>" + numMulti((parseFloat(fundInfoDto.latestNewValue) - dto.confirmNav), 100).toFixed(2) + "</em>%");
                                /*累计收益率*/
                                $("#top_left_03 #netval_text").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                                /*描述*/
                                $("#top_left_03 #maxNetValue").html(parseFloat(fundInfoDto.maxNav).toFixed(4));
                                /*历史最高净值*/

//                                $("#netval").show();
                                /* 净值相关查询 */
                                 if ("n" == isSubPrdAppraisement.toLowerCase()) {
                                     $('#text1').html("注:该净值为《" + prjLName + "》 份额净值");
                                     $('#text1').show();
                                     $('#highchart1').css('margin', '0 auto 4px');
                                //     queryEstimateByFundId("-12");
                                 } 
                                //else {
                                //     queryEstimateByFundId("-1");
                                // }
                                // queryEstimateByFundIdByPage("1");
                                /* 查询产品净值 */
                            } else {
                                $("#top_left_02").show();
                                $("#top_left_01,#top_left_03,#top_left_04").remove();
                                $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                                $("#term").html("<em>" + term + "</em>" + termUnit);
                                if(fundInfoDto.groupId == "20003" && typeId == "0210"){
                                	$("#term").parent().html("");
                                }
                                $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");

                                $("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:" + sellPercent + "%'></b></span><i>" + sellPercent + "%</i>");
                                /* 投资进度 */
                                if (displayLimit > 0) {
                                    $("#displayLimitText").html("只剩<em>" + numDiv(displayLimit, 10000) + "万</em>产品额度");
                                    /* 产品额度提示语 */
                                } else {
                                    $("#displayLimitText").css("margin-left", "184px").html("<em></em>已售罄");
                                    /* 产品额度提示语 */
                                }
                            }
                        } else if (orderType != null && orderType == "8") {/* 受理中 */
                            $("#orderDetail h1.floatincome").html("待确认...").addClass("shouli");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            if(typeId == "0400"){
                            	$("#orderDetail .head-righ-con p.point").remove();
                            }else{
                            	$("#orderDetail .head-righ-con p.point").html("已收到您的款项，产品将于<em>" + formatDate1(interestDate) + "</em>正式起息");
                            }
                            /* 订单提示语 */

                            if ((typeId == "0210" || typeId == "0220") && fundInfoDto.state == "0") {/* 申购 */
                                $("#top_left_03").show();
                                $("#top_left_01,#top_left_02,#top_left_04").remove();
                                $("#top_left_03 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);
                                /*最新净值*/
                                $("#top_left_03 #netval_profit").html("<em>" + numMulti((parseFloat(fundInfoDto.latestNewValue) - dto.confirmNav), 100).toFixed(2) + "</em>%");
                                /*累计收益率*/
                                $("#top_left_03 #netval_text").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                                /*描述*/
                                $("#top_left_03 #maxNetValue").html(parseFloat(fundInfoDto.maxNav).toFixed(4));
                                /*历史最高净值*/
                                //
                                // $("#netval").show();
                                // /* 净值相关查询 */
                                 if ("n" == isSubPrdAppraisement.toLowerCase()) {
                                     $('#text1').html("注:该净值为《" + prjLName + "》 份额净值");
                                     $('#text1').show();
                                     $('#highchart1').css('margin', '0 auto 4px');
//                                     queryEstimateByFundId("-12");
                                 } 
                                //else {
                                //     queryEstimateByFundId("-1");
                                // }
                                // queryEstimateByFundIdByPage("1");
                                /* 查询产品净值 */
                            } else {
                                $("#top_left_02").show();
                                $("#top_left_01,#top_left_03,#top_left_04").remove();
                                $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                                $("#term").html("<em>" + term + "</em>" + termUnit);
                                if(fundInfoDto.groupId == "20003" && typeId == "0210"){
                                	$("#term").parent().html("");
                                }
                                $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");

                                $("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:" + sellPercent + "%'></b></span><i>" + sellPercent + "%</i>");
                                /* 投资进度 */
                                if (displayLimit > 0) {
                                    $("#displayLimitText").html("只剩<em>" + numDiv(displayLimit, 10000) + "万</em>产品额度");
                                    /* 产品额度提示语 */
                                } else {
                                    $("#displayLimitText").css("margin-left", "184px").html("<em></em>已售罄");
                                    /* 产品额度提示语 */
                                }
                            }
                        }

                        $("#profit_text").html("官网直销免认购费");

                        /*if (orderType != null && orderType == "5") { 已到期
                         $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove(); 删除预期收益
                         }*/


                    } else if (typeId != null && typeId == "0110") {
                        $('#maturityDateText').html('下一到期日')
                        if (orderType != null && orderType == "1") {/* 待付款 */
                            if (daysBetween(currentWorkdate, salesDate) >= 0) {/* 销售期 */
                                $("#orderDetail h1.floatincome").html("待付款...");
                                $("#orderDetail .head-righ-con p.point").html("请尽快完成支付，确认产品份额");
                                /* 订单提示语 */
                            } else if (daysBetween(currentWorkdate, appointDate) >= 0) {/* 预约期 */
                                $("#orderDetail h1.floatincome").html("待付款...");
                                $("#orderDetail .head-righ-con p.point").html("请您于募集期(<em style='color:#ca132c;'>" + appPayMoneyText + "</em>)完成汇款");
                                /* 订单提示语 */
                            } else if (fundInfoDto.state == "0") {
                                $("#orderDetail h1.floatincome").html("待付款...");
                                $("#orderDetail .head-righ-con p.point").html("请尽快完成支付，确认产品份额");
                                /* 订单提示语 */
                            } else {/* TODO 要确定即不是认购期也不是预约期的订单该怎么显示，现在暂时显示认购中 */
                                $("#orderDetail h1.floatincome").html("待付款...");
                                $("#orderDetail .head-righ-con p.point").html("请尽快完成支付，确认产品份额");
                                /* 订单提示语 */
                            }
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("续投方式：" + button + "").hide();
                            /*续投方式*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("查看汇款信息").attr("href", "javascript:queryOrderShow();");
                            if (typeId == "0110" && fundInfoDto.state == "0") {/* 申购 */
                            	$("#top_left_04").show();
                                $("#top_left_01,#top_left_02,#top_left_03").remove();
                                var netval_new = fundInfoDto.latestNewValue;
                                if(netval_new != ""){
                                	netval_new = "<i>" + netval_new.split(".")[0] + "</i>." + netval_new.split(".")[1];
                                }
                                $("#top_left_04 #netval_new").html(netval_new);
                                /*最新净值*/
                                var netval_profit = fundInfoDto.latestNewValue;
                                if(netval_profit != ""){
                                	netval_profit = "<em>" + numMulti((parseFloat(netval_profit) - dto.confirmNav), 100).toFixed(2) + "</em>%";
                                }
                                $("#top_left_04 #netval_profit").html(netval_profit);
                                /*累计收益率*/
                                $("#top_left_04 #sellPercent").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                                /*描述*/
                                var maxNetValue = fundInfoDto.maxNav;
                                if(maxNetValue != ""){
                                	maxNetValue = parseFloat(maxNetValue).toFixed(4);
                                }
                                $("#top_left_04 #maxNetValue").html(maxNetValue);
                                /*历史最高净值*/
                            } else {
                                $("#top_left_01").show();
                                $("#top_left_02,#top_left_03,#top_left_04").remove();

                                /*$("#profit_text").html("预期收益：<em>"+profitText+"");*/
                                $("#profit_text").html("业绩报酬计提基准<em>");

                                if (isNaN(profitTemp) || profitTemp == 0) {
                                    $("#profit").html("<i></i>浮动收益");
                                } else {
                                    $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                                }
                                $("#term").html("<em>" + term + "</em>" + termUnit);
                                $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");

                                $("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:" + sellPercent + "%'></b></span><i>" + sellPercent + "%</i>");
                                /* 产品额度*/
                                if (displayLimit > 0) {
                                    if (daysBetween(currentWorkdate, subdeadLine) > 0) {/*认购截止日期*/
                                        $("#displayLimitText").html("<em></em>已售罄");
                                        /* 产品额度提示语*/
                                    } else {
                                        $("#displayLimitText").html("只剩<em>" + numDiv(displayLimit, 10000) + "万</em>产品额度");
                                        /* 产品额度提示语*/
                                    }
                                } else {
                                    $("#displayLimitText").html("<em></em>已售罄");
                                    /* 产品额度提示语*/
                                }
                            }
                        } else if (orderType != null && orderType == "2") {/* 已支付 */
                            $("#orderDetail h1.floatincome").html("已支付...").addClass("success");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            $("#orderDetail .head-righ-con p.point").html("已收到您的来款，产品将于<em>" + formatDate1(interestDate) + "</em>正式起息");
                            /* 订单提示语 */
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("续投方式：" + button + "").hide();
                            /*续投方式*/
                            if (typeId == "0110" && fundInfoDto.state == "0") {/* 申购 */
                                $("#top_left_04").show();
                                $("#top_left_01,#top_left_02,#top_left_03").remove();
                                var netval_new = fundInfoDto.latestNewValue;
                                if(netval_new != ""){
                                	netval_new = "<i>" + netval_new.split(".")[0] + "</i>." + netval_new.split(".")[1];
                                }
                                $("#top_left_04 #netval_new").html(netval_new);
                                /*最新净值*/
                                var netval_profit = fundInfoDto.latestNewValue;
                                if(netval_profit != ""){
                                	netval_profit = "<em>" + numMulti((parseFloat(netval_profit) - dto.confirmNav), 100).toFixed(2) + "</em>%";
                                }
                                $("#top_left_04 #netval_profit").html(netval_profit);
                                /*累计收益率*/
                                $("#top_left_04 #sellPercent").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                                /*描述*/
                                var maxNetValue = fundInfoDto.maxNav;
                                if(maxNetValue != ""){
                                	maxNetValue = parseFloat(maxNetValue).toFixed(4);
                                }
                                $("#top_left_04 #maxNetValue").html(maxNetValue);
                                /*历史最高净值*/
                            } else {
                                $("#top_left_01").show();
                                $("#top_left_02,#top_left_03,#top_left_04").remove();

                                /*$("#profit_text").html("预期收益：<em>"+profitText+"");*/
                                $("#profit_text").html("业绩报酬计提基准<em>");

                                if (isNaN(profitTemp) || profitTemp == 0) {
                                    $("#profit").html("<i></i>浮动收益");
                                } else {
                                    $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                                }
                                $("#term").html("<em>" + term + "</em>" + termUnit);
                                $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");

                                $("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:" + sellPercent + "%'></b></span><i>" + sellPercent + "%</i>");
                                /* 产品额度*/
                                if (displayLimit > 0) {
                                    if (daysBetween(currentWorkdate, subdeadLine) > 0) {/*认购截止日期*/
                                        $("#displayLimitText").html("<em></em>已售罄");
                                        /* 产品额度提示语*/
                                    } else {
                                        $("#displayLimitText").html("只剩<em>" + numDiv(displayLimit, 10000) + "万</em>产品额度");
                                        /* 产品额度提示语*/
                                    }
                                } else {
                                    $("#displayLimitText").html("<em></em>已售罄");
                                    /* 产品额度提示语*/
                                }
                            }
                        } else if (orderType != null && orderType == "3") {/* 排队中 */
                            $("#orderDetail h1.floatincome").html("排队中...").addClass("paidui");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("查看排队信息").attr("href", "javascript:queryOrderShow();");
                            $("#orderDetail .head-righ-con p.point").html("您的订单已进入排队等候，如有额度释放将第一时间通知您，额度详情请致电400-8878-555。");
                            /* 订单提示语 */
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("续投方式：" + button + "").hide();
                            /*续投方式*/
                            if (typeId == "0110" && fundInfoDto.state == "0") {/* 申购 */
                            	$("#top_left_04").show();
                                $("#top_left_01,#top_left_02,#top_left_03").remove();
                                var netval_new = fundInfoDto.latestNewValue;
                                if(netval_new != ""){
                                	netval_new = "<i>" + netval_new.split(".")[0] + "</i>." + netval_new.split(".")[1];
                                }
                                $("#top_left_04 #netval_new").html(netval_new);
                                /*最新净值*/
                                var netval_profit = fundInfoDto.latestNewValue;
                                if(netval_profit != ""){
                                	netval_profit = "<em>" + numMulti((parseFloat(netval_profit) - dto.confirmNav), 100).toFixed(2) + "</em>%";
                                }
                                $("#top_left_04 #netval_profit").html(netval_profit);
                                /*累计收益率*/
                                $("#top_left_04 #sellPercent").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                                /*描述*/
                                var maxNetValue = fundInfoDto.maxNav;
                                if(maxNetValue != ""){
                                	maxNetValue = parseFloat(maxNetValue).toFixed(4);
                                }
                                $("#top_left_04 #maxNetValue").html(maxNetValue);
                                /*历史最高净值*/
                            } else {
                                $("#top_left_01").show();
                                $("#top_left_02,#top_left_03,#top_left_04").remove();

                                /*$("#profit_text").html("预期收益：<em>"+profitText+"");*/
                                $("#profit_text").html("业绩报酬计提基准：<em>---");

                                if (isNaN(profitTemp) || profitTemp == 0) {
                                    $("#profit").html("<i></i>浮动收益");
                                } else {
                                    $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                                }
                                $("#term").html("<em>" + term + "</em>" + termUnit);
                                $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");

                                $("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:" + sellPercent + "%'></b></span><i>" + sellPercent + "%</i>");
                                /* 产品额度*/
                                if (displayLimit > 0) {
                                    if (daysBetween(currentWorkdate, subdeadLine) > 0) {/*认购截止日期*/
                                        $("#displayLimitText").html("<em></em>已售罄");
                                        /* 产品额度提示语*/
                                    } else {
                                        $("#displayLimitText").html("只剩<em>" + numDiv(displayLimit, 10000) + "万</em>产品额度");
                                        /* 产品额度提示语*/
                                    }
                                } else {
                                    $("#displayLimitText").html("<em></em>已售罄");
                                    /* 产品额度提示语*/
                                }
                            }
                        } else if (orderType != null && orderType == "4") {/* 存续中 */

                            $("#orderDetail h1.floatincome").html("存续中...").addClass("success");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            $("#orderDetail .head-righ-con p.point").html("产品处于存续期，预计将于<em>" + formatDate1(maturityDate) + "</em>到期");
                            /* 订单提示语 */
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("续投方式：" + button + "").hide();
                            /*续投方式*/
                            $("#top_left_04").show();
                            $("#top_left_01,#top_left_02,#top_left_03").remove();
                            var netval_new = fundInfoDto.latestNewValue;
                            if(netval_new != ""){
                            	netval_new = "<i>" + netval_new.split(".")[0] + "</i>." + netval_new.split(".")[1];
                            }
                            $("#top_left_04 #netval_new").html(netval_new);
                            /*最新净值*/
                            var netval_profit = fundInfoDto.latestNewValue;
                            if(netval_profit != ""){
                            	netval_profit = "<em>" + numMulti((parseFloat(netval_profit) - dto.confirmNav), 100).toFixed(2) + "</em>%";
                            }
                            $("#top_left_04 #netval_profit").html(netval_profit);
                            /*累计收益率*/
                            $("#top_left_04 #sellPercent").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                            /*描述*/
                            var maxNetValue = fundInfoDto.maxNav;
                            if(maxNetValue != ""){
                            	maxNetValue = parseFloat(maxNetValue).toFixed(4);
                            }
                            $("#top_left_04 #maxNetValue").html(maxNetValue);
                            /*历史最高净值*/
                        } else if (orderType != null && orderType == "5") {/* 已到期 */
                        	$("#top_left_04").show();
                            $("#top_left_01,#top_left_02,#top_left_03").remove();
                            var netval_new = fundInfoDto.latestNewValue;
                            if(netval_new != ""){
                            	netval_new = "<i>" + netval_new.split(".")[0] + "</i>." + netval_new.split(".")[1];
                            }
                            $("#top_left_04 #netval_new").html(netval_new);
                            /*最新净值*/
                            var netval_profit = fundInfoDto.latestNewValue;
                            if(netval_profit != ""){
                            	netval_profit = "<em>" + numMulti((parseFloat(netval_profit) - dto.confirmNav), 100).toFixed(2) + "</em>%";
                            }
                            $("#top_left_04 #netval_profit").html(netval_profit);
                            /*累计收益率*/
                            $("#top_left_04 #sellPercent").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                            /*描述*/
                            var maxNetValue = fundInfoDto.maxNav;
                            if(maxNetValue != ""){
                            	maxNetValue = parseFloat(maxNetValue).toFixed(4);
                            }
                            $("#top_left_04 #maxNetValue").html(maxNetValue);
                            /*历史最高净值*/

                            $("#QAtelG").show();
                            $("#orderDetail h1.floatincome").html("已到期...").addClass("timeout");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("买入金额：" + subamt);
                            /* 订单金额 */
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("投资收益：" + benefit);
                            /* 预期收益 */
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            var redemptionShare = (!appointRequestDto.redemptionShare && appointRequestDto.renew == "N") ? appointRequestDto.subquty : appointRequestDto.redemptionShare;
							redemptionShare = redemptionShare ? redemptionShare : "0";
                            var nextSubquty = parseFloat(appointRequestDto.subquty) - parseFloat(redemptionShare);
                            $("#orderDetail .head-righ-con p.point").html("到期赎回份额：" + formatNumber(parseFloat(redemptionShare).toFixed(2)) + "份<br>滚存入下一期份额：" + formatNumber(parseFloat(nextSubquty).toFixed(2)) + "份");
                        } else if (orderType != null && orderType == "6") {/* 已失效 */
                            $("#orderDetail h1.floatincome").html("订单已失效...").addClass("subsist");
                            $("#orderDetail .head-righ-con div.check-cancel a.check").addClass("hui").attr("href", "javascript:void(0)");
                            /* 按钮置灰 */
                            if (dto.payst != null && dto.payst == "Y") {
                                $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("支付金额：" + subamt);
                                /* 订单金额 */
                                $("#orderDetail .head-righ-con p.point").html("十分抱歉！您买入产品失败。");
                            } else {
                                $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                                /* 订单金额 */
                                $("#orderDetail .head-righ-con p.point").html("十分抱歉！由于您未在有效时间内完成汇款，订单已失效");
                            }
                            /*$("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();预期收益
                             $("#orderDetail h1.floatincome").html("订单待确认...").addClass("verify");
                             $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt); 订单金额
                             $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();预期收益
                             $("#orderDetail .head-righ-con div.check-cancel a.check").html("确认订单").attr("href", "/AppService/business/fund/confirmBuy.shtml?serialno=" + serialno);
                             $("#orderDetail .head-righ-con p.point").html("请尽快确认订单，完成汇款</span");*/
                            if (typeId == "0110" && fundInfoDto.state == "0") {/* 申购 */
                            	$("#top_left_04").show();
                                $("#top_left_01,#top_left_02,#top_left_03").remove();
                                var netval_new = fundInfoDto.latestNewValue;
                                if(netval_new != ""){
                                	netval_new = "<i>" + netval_new.split(".")[0] + "</i>." + netval_new.split(".")[1];
                                }
                                $("#top_left_04 #netval_new").html(netval_new);
                                /*最新净值*/
                                var netval_profit = fundInfoDto.latestNewValue;
                                if(netval_profit != ""){
                                	netval_profit = "<em>" + numMulti((parseFloat(netval_profit) - dto.confirmNav), 100).toFixed(2) + "</em>%";
                                }
                                $("#top_left_04 #netval_profit").html(netval_profit);
                                /*累计收益率*/
                                $("#top_left_04 #sellPercent").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                                /*描述*/
                                var maxNetValue = fundInfoDto.maxNav;
                                if(maxNetValue != ""){
                                	maxNetValue = parseFloat(maxNetValue).toFixed(4);
                                }
                                $("#top_left_04 #maxNetValue").html(maxNetValue);
                                /*历史最高净值*/
                            } else {
                                $("#top_left_01").show();
                                $("#top_left_02,#top_left_03,#top_left_04").remove();

                                /*$("#profit_text").html("预期收益：<em>"+profitText+"");*/
                                $("#profit_text").html("业绩报酬计提基准：<em>---");

                                if (isNaN(profitTemp) || profitTemp == 0) {
                                    $("#profit").html("<i></i>浮动收益");
                                } else {
                                    $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                                }
                                $("#term").html("<em>" + term + "</em>" + termUnit);
                                $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");

                                $("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:" + sellPercent + "%'></b></span><i>" + sellPercent + "%</i>");
                                /* 产品额度*/
                                if (displayLimit > 0) {
                                    if (daysBetween(currentWorkdate, subdeadLine) > 0) {/*认购截止日期*/
                                        $("#displayLimitText").html("<em></em>已售罄");
                                        /* 产品额度提示语*/
                                    } else {
                                        $("#displayLimitText").html("只剩<em>" + numDiv(displayLimit, 10000) + "万</em>产品额度");
                                        /* 产品额度提示语*/
                                    }
                                } else {
                                    $("#displayLimitText").html("<em></em>已售罄");
                                    /* 产品额度提示语*/
                                }
                            }
                        } else if (orderType != null && orderType == "7") {/* 待确认 */
                            $("#orderDetail h1.floatincome").html("订单待确认...").addClass("verify");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            var text = "<span style='font-size:16px;margin-left:11px;color:#5e5e5e'>";
                            text += renew == 'Y' ? "到期自动续投" : "到期自动赎回"
                            text += '</span>'
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("续投方式：" + text + "").hide();
                            /*续投方式*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("确认订单").attr("href", "/AppService/business/fund/confirmBuy.shtml?serialno=" + serialno);
                            $("#orderDetail .head-righ-con p.point").html("请尽快确认订单，完成汇款</span");
                            if (typeId == "0110" && fundInfoDto.state == "0") {/* 申购 */
                            	$("#top_left_04").show();
                                $("#top_left_01,#top_left_02,#top_left_03").remove();
                                var netval_new = fundInfoDto.latestNewValue;
                                if(netval_new != ""){
                                	netval_new = "<i>" + netval_new.split(".")[0] + "</i>." + netval_new.split(".")[1];
                                }
                                $("#top_left_04 #netval_new").html(netval_new);
                                /*最新净值*/
                                var netval_profit = fundInfoDto.latestNewValue;
                                if(netval_profit != ""){
                                	netval_profit = "<em>" + numMulti((parseFloat(netval_profit) - dto.confirmNav), 100).toFixed(2) + "</em>%";
                                }
                                $("#top_left_04 #netval_profit").html(netval_profit);
                                /*累计收益率*/
                                $("#top_left_04 #sellPercent").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + formatDate1(fundInfoDto.lastNavDate) + "</i></b></em>");
                                /*描述*/
                                var maxNetValue = fundInfoDto.maxNav;
                                if(maxNetValue != ""){
                                	maxNetValue = parseFloat(maxNetValue).toFixed(4);
                                }
                                $("#top_left_04 #maxNetValue").html(maxNetValue);
                                /*历史最高净值*/
                            } else {
                                $("#top_left_01").show();
                                $("#top_left_02,#top_left_03,#top_left_04").remove();

                                /*$("#profit_text").html("预期收益：<em>"+profitText+"");*/
                                $("#profit_text").html("业绩报酬计提基准：<em>---");

                                if (isNaN(profitTemp) || profitTemp == 0) {
                                    $("#profit").html("<i></i>浮动收益");
                                } else {
                                    $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                                }
                                $("#term").html("<em>" + term + "</em>" + termUnit);
                                $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");

                                $("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:" + sellPercent + "%'></b></span><i>" + sellPercent + "%</i>");
                                /* 产品额度*/
                                if (displayLimit > 0) {
                                    if (daysBetween(currentWorkdate, subdeadLine) > 0) {/*认购截止日期*/
                                        $("#displayLimitText").html("<em></em>已售罄");
                                        /* 产品额度提示语*/
                                    } else {
                                        $("#displayLimitText").html("只剩<em>" + numDiv(displayLimit, 10000) + "万</em>产品额度");
                                        /* 产品额度提示语*/
                                    }
                                } else {
                                    $("#displayLimitText").html("<em></em>已售罄");
                                    /* 产品额度提示语*/
                                }
                            }
                        } else if (orderType != null && orderType == "8") {/* 受理中 */
                            $("#orderDetail h1.floatincome").html("待确认...").addClass("shouli");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            if(typeId == "0400"){
                            	$("#orderDetail .head-righ-con p.point").remove();
                            }else{
                            	$("#orderDetail .head-righ-con p.point").html("已收到您的款项，产品将于<em>" + formatDate1(interestDate) + "</em>正式起息");
                            }
                            /* 订单提示语 */
                        }
                        /*$("#profit_text").html("官网直销免认购费");*/

                        /*if (orderType != null && orderType == "5") { 已到期
                         $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove(); 删除预期收益
                         }*/

                    } else if (typeId != null && typeId == "0300") {/* 浮动收益 */

                        if (orderType != null && orderType == "1") {/* 待付款 */
                            if (daysBetween(currentWorkdate, salesDate) >= 0) {/* 销售期 */
                                $("#orderDetail h1.floatincome").html("认购中...");
                                $("#orderDetail .head-righ-con p.point").html("请尽快完成支付，确认产品份额");
                                /* 订单提示语 */
                            } else if (daysBetween(currentWorkdate, appointDate) >= 0) {/* 预约期 */
                                $("#orderDetail h1.floatincome").html("预约中...");
                                $("#orderDetail .head-righ-con p.point").html("请您于募集期(<em style='color:#ca132c;'>" + appPayMoneyText + "</em>)完成汇款");
                                /* 订单提示语 */
                            } else {/* TODO 要确定即不是认购期也不是预约期的订单该怎么显示，现在暂时显示认购中 */
                                $("#orderDetail h1.floatincome").html("认购中...");
                                $("#orderDetail .head-righ-con p.point").html("请尽快完成支付，确认产品份额");
                                /* 订单提示语 */
                            }
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("查看汇款信息").attr("href", "javascript:queryOrderShow();");
                        } else if (orderType != null && orderType == "2") {/* 已支付 */
                            $("#orderDetail h1.floatincome").html("已支付...").addClass("success");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            if(typeId == "0400"){
                            	$("#orderDetail .head-righ-con p.point").remove();
                            }else{
                            	$("#orderDetail .head-righ-con p.point").html("已收到您的款项，产品将于<em>" + formatDate1(interestDate) + "</em>正式起息");
                            }
                            /* 订单提示语 */
                        } else if (orderType != null && orderType == "3") {/* 排队中 */
                            $("#orderDetail h1.floatincome").html("排队中...").addClass("paidui");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("查看排队信息").attr("href", "javascript:queryOrderShow();");
                            $("#orderDetail .head-righ-con p.point").html("您的订单已进入排队等候，如有额度释放将第一时间通知您，额度详情请致电400-8878-555。");
                            /* 订单提示语 */
                        } else if (orderType != null && orderType == "4") {/* 存续中 */
                            $("#orderDetail h1.floatincome").html("存续中...").addClass("saveextend");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("买入金额：" + subamt);
                            /* 订单金额 */
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("已分配金额：" + shareAmt);
                            /* 预期收益 */
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            $("#orderDetail .head-righ-con p.point").html("产品处于存续期，预计将于<em>" + formatDate1(maturityDate) + "</em>到期");
                            /* 订单提示语 */
                        } else if (orderType != null && orderType == "5") {/* 已到期 */
                            $("#orderDetail h1.floatincome").html("已到期...").addClass("timeout");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("买入金额：" + subamt);
                            /* 订单金额 */
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("总分配金额：" + shareAmt);
                            /* 预期收益 */
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            /* 去掉按钮--查看汇款信息和取消订单的按钮 */
                            $("#orderDetail .head-righ-con p.point").html("产品已到期，预计将于5个工作日内完成收益分配");
                            /* 订单提示语 */
                        } else if (orderType != null && orderType == "6") {/* 已失效 */
                            $("#orderDetail h1.floatincome").html("订单已失效...").addClass("subsist");
                            $("#orderDetail .head-righ-con div.check-cancel a.check").addClass("hui").attr("href", "javascript:void(0)");
                            /* 按钮置灰 */
                            if (dto.payst != null && dto.payst == "Y") {
                                $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("支付金额：" + subamt);
                                /* 订单金额 */
                                $("#orderDetail .head-righ-con p.point").html("十分抱歉！您买入产品失败");
                            } else {
                                $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                                /* 订单金额 */
                                $("#orderDetail .head-righ-con p.point").html("十分抱歉！由于您未在有效时间内完成汇款，订单已失效");
                                $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                                /*预期收益*/
                            }
                        } else if (orderType != null && orderType == "7") {/* 待确认 */
                            $("#orderDetail h1.floatincome").html("订单待确认...").addClass("verify");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel a.check").html("确认订单").attr("href", "/AppService/business/fund/confirmBuy.shtml?serialno=" + serialno);
                            $("#orderDetail .head-righ-con p.point").html("请尽快确认订单，完成汇款</span");
                        } else if (orderType != null && orderType == "8") {/* 受理中 */
                            $("#orderDetail h1.floatincome").html("待确认...").addClass("shouli");
                            $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("待支付金额：" + subamt);
                            /*订单金额*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /*预期收益*/
                            $("#orderDetail .head-righ-con div.check-cancel").remove();
                            if(typeId == "0400"){
                            	$("#orderDetail .head-righ-con p.point").remove();
                            }else{
                            	$("#orderDetail .head-righ-con p.point").html("已收到您的款项，产品将于<em>" + formatDate1(interestDate) + "</em>正式起息");
                            }
                            /* 订单提示语 */

                        }


                        $("#top_left_02").show();
                        $("#top_left_01,#top_left_03").remove();
                        if (orderType != null && orderType == "4") {
                            if (isNaN(dto.shareAmt) || isNaN(parseFloat(dto.shareAmt)) || dto.shareAmt == 0) {/* 存续中 */
                                $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("已分配金额：<em>---</em>");
                                /* 浮动收益产品 没有收益显示浮动收益 */
                            }
                        }
                        $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                        $("#term").html("<em>" + term + "</em>" + termUnit);
                        $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");

                        $("#profit_text").html("官网直销免认购费");
                        if (orderType != '4' && orderType != '5') {
                            $("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:" + sellPercent + "%'></b></span><i>" + sellPercent + "%</i>");
                            /* 投资进度 */
                            if (displayLimit > 0) {
                                $("#displayLimitText").html("只剩<em>" + numDiv(displayLimit, 10000) + "万</em>产品额度");
                                /* 产品额度提示语 */
                            } else {
                                $("#displayLimitText").css("margin-left", "184px").html("<em></em>已售罄");
                                /* 产品额度提示语 */
                            }
                        } else {
                            $("#QAtelF").show();
                        }
                    }

                    queryEstimate();

                    /*-------------------- 产品详情  个性化展示  E --------------------*/

                    /*-------------------- 产品详情  通用展示  S --------------------*/
                    $("#adname").html(fundInfoDto.adname);
                    /* 产品名称 */
                    $("#typeName").html(fundInfoDto.typeName);
                    /* 产品类型名称 */
                    $("#adtext").html(fundInfoDto.adtext);
                    /* 广告语 */


                    /*$("#appoint_date_tips").html(parseInt(unformat1(appointEndDate).substr(0, 4),10) + "年" + parseInt(unformat1(appointEndDate).substr(4, 2),10) + "月"
                     + parseInt(unformat1(appointEndDate).substr(6, 2),10) + "日15:00");
                     */

                    // 7天十四天页面展示
                    if(typeId=="0400"){
                        $("#timeAxis").hide();
                        $(".fund7Day").show();
                        if(appointRequestDto.apkind == '020' || appointRequestDto.apkind == '720' || appointRequestDto.apkind == '820'){
                        	$("#buyTime").html(fundInfoDto.interestDate);/* 认购产品且基金成立时间小于购买时间*/
                        }else{
                        	$("#buyTime").html(appointRequestDto.workdate);/* 购买时间 */
                        }
                        $("#expireTime").html(fundInfoDto.maturityDate);/* 首个可赎回日 */
                        $("#netExpireTime").html(fundInfoDto.nextMaturityDate);/* 次个可赎回日 */
                        
            			//开关
        				var seven ='0';
        				try{
        					seven = queryParamComm("SYSTEM","SHOWNETVALUE","")[0].pmco;
        				}catch(err){
        					console.log("开关查询失败");
        				}
    					if(seven == '1'){
    						/*
    						 * 7天管家类产品
    						 * 展示七日年化收益、理财起点、递增金额
    						 */
    						var sevenDayAnnualy = '--';
    	        			if(fundInfoDto.sevenDayAnnualy&&"0.00%"!=fundInfoDto.sevenDayAnnualy){
    	        				sevenDayAnnualy = fundInfoDto.sevenDayAnnualy;
    	        			}
    	        			$("#profit").html("<i>" + sevenDayAnnualy +"</i>");/*申购期七日年化收益为*/
                             $("#top_left_01 .start").html("<dt class='floatincome' id='profit'>"+sevenDayAnnualy+"</dt> <dd>七日年化</dd>");
    					}else{
                            var latestNewValue =fundInfoDto.latestNewValue;
                            if(!latestNewValue){
                            	latestNewValue ="1.0000";
                            }
                            var numlate = new Number(latestNewValue);
                            $("#top_left_01 .start").html("<dt class='floatincome' id='profit'>"+numlate.toFixed(4)+"</dt> <dd>最新净值</dd>");
    					}
                        if(fundInfoDto.term=="14"){
                           $(".fund7Day-text em").html("存续十四天")
                        }
                        if(orderType=='5'){
                            $(".fund7Day-text").html("");
                            $("#cirle .cirle").eq(0).remove();
                            $("#cirle .cirle").addClass("act");
                            $("#line span").addClass("act");
                            $(".fund7Day").addClass("readyPeriod");
                            $("#netExpireTime").parents("li").remove();
                            $("#buyTime").parents("li").find("span").html("购买");
                            $("#expireTime").parents("li").find("span").html("赎回");
                            $("#expireTime").html(appointRequestDto.redeemTime);/* 赎回日 期*/
                        }

                      }
	                    if(typeId=="0500"){
	                    	$('#timeAxis').remove();
	                    }
                        $("#appoint_date_tips").html(appPayMoneyText);
                        $("#appointDate").html(formatDate(appointDate));
                        /* 预约开始日期 */
                        $("#salesDate").html(formatDate(salesDate));
                        /* 发售日 */
                        $("#subdeadLine").html(formatDate(subdeadLine));
                        /* 认购截止 */
                        $("#maturityDate").html(formatDate(maturityDate));
                        /* 到期日期 */

                        if (daysBetween(currentWorkdate, maturityDate) >= 0) {/* 到期日期 */
                            $("#timeAxis .process-right.fl dl").removeClass("act");
                            $("#timeAxis .process-right.fl dl:lt(4)").addClass("act");
                        } else if (daysBetween(currentWorkdate, subdeadLine) > 0) {/* 认购截止日期 */
                            $("#timeAxis .process-right.fl dl").removeClass("act");
                            $("#timeAxis .process-right.fl dl:lt(3)").addClass("act");
                        } else if (daysBetween(currentWorkdate, subdeadLine) == 0) {/* 认购截止日期 */
                            $("#timeAxis .process-right.fl dl").removeClass("act");
                            $("#timeAxis .process-right.fl dl:lt(3)").addClass("act");
                        } else if (daysBetween(currentWorkdate, salesDate) >= 0) {/* 发售日 */
                            $("#timeAxis .process-right.fl dl").removeClass("act");
                            $("#timeAxis .process-right.fl dl:lt(2)").addClass("act");

                        } else if (daysBetween(currentWorkdate, appointDate) >= 0) {/* 预约日 */
                            $("#timeAxis .process-right.fl dl").removeClass("act");
                            $("#timeAxis .process-right.fl dl:lt(1)").addClass("act");

                        } else if (daysBetween(currentWorkdate, appointDate) < 0) {/* 预约日 */
                            $("#timeAxis .process-right.fl dl").removeClass("act");
                        }

                    /*-------------------- 产品详情  通用展示  E --------------------*/

                    var elementList = fundInfoDto.elementList;
                    if (elementList != null && elementList.length > 0) {
                        var htmls1 = "";
                        var htmls2 = "";
                        var htmls3 = "";
                        var bl = false;
                        $.each(elementList, function (i, item) {
                            if (item.type > 100 && item.type < 200) {/*产品基本信息：110-小标题；120-大标题；130-图片*/

                                if (item.type == 110) {
                                    bl = true;
                                    htmls1 += "<li class='align'><span class='litter_title'>" + item.title + "</span><em>" + item.content + "</em></li>";
                                } else if (item.type == 120) {
                                    htmls2 += "<div class='box'>";
                                    htmls2 += "<span class='left'>" + item.title + "</span>";
                                    htmls2 += "<span style='word-break: break-all' class='right'>" + item.content + "</span>";
                                    htmls2 += "</div>";
                                }
                            }
                            if (item.type > 200 && item.type < 300) {/*投资项目信息：210-小标题；220-大标题；230-图片*/

                                if (item.type == 220) {
                                    htmls3 += "<div class='box'>";
                                    htmls3 += "<span class='left'>" + item.title + "</span>";
                                    htmls3 += "<span style='word-break: break-all' class='right'>" + item.content + "</span>";
                                    htmls3 += "</div>";
                                } else if (item.type == 230) {
                                    htmls3 += "<div class='box'>";
                                    htmls3 += "<span class='left'>" + item.title + "</span>";
                                    htmls3 += "<span class='right'><img src='" + item.picUrl + "' height='479' width='763' alt=''></span>";
                                    htmls3 += "</div>";
                                }
                            }

                            if ((i + 1) == elementList.length) {
                                if (!bl) {
                                    $("#productInfo div.content .box01").hide();
                                } else {
                                    $("#productInfo div.content .box01 ul").html(htmls1);
                                }
                                $("#productInfo div.content").append(htmls2);
                                if (htmls1 != "" || htmls2 != "") {
                                    $("#productInfo").show();
                                }
                                if (htmls3 != "") {
                                    $("#projectInfo div.content").html(htmls3).parent().show();
                                }
                            }
                        });
                    }
                    /*-------------------- 产品详情  通用展示  E --------------------*/
                } else {
                    show_tips("没有此订单");
                    redirectUrl("/WeixinService/business/query/orderList.shtml");
                }
                queryTradeByTradeNo();
                /* 合同 */

                /*20180103crm数据迁移需求, 移除查询旧版PDF合同*/
                /* 查询 产品合同 txt */
                queryEcontrantByFundIdN();


                /* 问题展示 */
                var questionDtoList = data.questionDtoList;
                var htmls4 = "";
                if (questionDtoList != null && questionDtoList.length > 0) {
                    $.each(questionDtoList, function (i, item) {
                        htmls4 += "<div class='box01'>";
                        htmls4 += "<span class='left'>" + item.wWenti + "</span>";
                        htmls4 += "<span class='right act wordlimit'><p style='word-break: break-all' data-title='" + item.wDaan + "'>" + item.wDaan + "</p></span>";
                        htmls4 += "<i></i>";
                        htmls4 += "</div>";
                    });
                    $("#qa div.content").html(htmls4).parent().show();

                    /* 产品详情底部问答 b */
                    $(".wordlimit").each(function (i) {
                        var divH = $(this).height();
                        var $p = $("p", $(this)).eq(0);
                        while ($p.outerHeight() > divH) {
                            $p.text($p.text().replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
                        }
                        ;
                    });
                    $(".box-third .content .box01").toggle(
                        function () {
                            $(this).addClass("act").siblings().removeClass("act");
                            var ellipsisText = $(this).find(".right p").attr("data-title");
                            $(this).find(".right p").html(ellipsisText);
                        },
                        function () {
                            if ($(this).hasClass("act")) {
                                $(this).removeClass("act").siblings().removeClass("act");
                                $(".wordlimit").each(function (i) {
                                    var divH = $(this).height();
                                    var $p = $("p", $(this)).eq(0);
                                    while ($p.outerHeight() > divH) {
                                        $p.text($p.text().replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
                                    }
                                });
                            } else {
                                $(this).addClass("act").siblings().removeClass("act");
                                var ellipsisText = $(this).find(".right p").attr("data-title");
                                $(this).find(".right p").html(ellipsisText);
                            }
                        }
                    );
                }
            }
        }
    });
}
/* 查询产品净值(曲线图) */
function queryEstimateByFundId(dateTime) {
    var fundId = $("#fundId").val();
    var isSubPrdAppraisement = $("#isSubPrdAppraisement").val();
    var outerId = $("#outerId").val();
    //子产品非估值 查询时为 系列产品净值图  产品净值不再对产品进行估值
    // if("n" == isSubPrdAppraisement.toLowerCase()){
    // 	fundId = outerId;
    // }
    $("#highchart .highchart_nav ul li").removeClass("act");
    if (parseInt(dateTime, 10) == -1) {
        $("#highchart .highchart_nav ul li:eq(0)").addClass("act");
    } else if (parseInt(dateTime, 10) == -3) {
        $("#highchart .highchart_nav ul li:eq(1)").addClass("act");
    } else if (parseInt(dateTime, 10) == -6) {
        $("#highchart .highchart_nav ul li:eq(2)").addClass("act");
    } else if (parseInt(dateTime, 10) == -12) {
        $("#highchart .highchart_nav ul li:eq(3)").addClass("act");
    }
    $.ajax({
        async: true,
        url: "/AppService/business/queryEstimateByFundId.xhtml",
        data: {
            fundId: fundId,
            dateTime: dateTime,
        },
        dataType: "json",
        type: "POST",
        cache: false,
        error: function (textStatus, errorThrown) {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function (data) {
            var xVals = "";
            var yVals = "";
            var netVals = "";
            var fluctuates = "";
            var htmls_1 = "";
            var productEstimateList = "";
            var stepNum = 1;

            if (data != null) {
                productEstimateList = data.productEstimateList;
            }
            if (productEstimateList != null && productEstimateList != "" && productEstimateList.length > 0) {
                stepNum = Math.ceil(numDiv(parseInt(productEstimateList.length, 10), 20));

                var maxVal = "1";
                if (productEstimateList != null && productEstimateList.length > 0) {
                    maxVal = productEstimateList[0].maxNetValue;
                }
                var yVarMaxlti = 0;
                var yValMinlti = 0;

                var newNetVal = 0;
                var oldNetVal = 0;

//				var yVals = 0.8 + ";" +(numDiv(yValMilti,4)*1).toFixed(4) + ";" + (numDiv(yValMilti,4)*2).toFixed(4) + ";" + (numDiv(yValMilti,4)*3).toFixed(4) + ";" + (numDiv(yValMilti,4)*4).toFixed(4);


                $.each(productEstimateList, function (i, item) {
                    xVals += item.eDate + ";";
                    netVals += item.netValue + ";";
                    fluctuates += item.fluctuate + ";";
                    if (yValMinlti > item.netValue) {
                        yValMinlti = item.netValue;
                    }
                    if (yVarMaxlti < item.netValue) {
                        yVarMaxlti = item.netValue;
                    }
                    if (i == 0) {
                        newNetVal = item.netValue;
                        yValMinlti = item.netValue;
                        yVarMaxlti = item.netValue;
                    } else if (i == productEstimateList.length - 1) {
                        oldNetVal = item.netValue;
                    }
                })
		yVarMaxlti=numMulti(yVarMaxlti,1.01)
		var min = (numDiv(yValMinlti,1)*1*0.99).toFixed(4);
                var max = yVarMaxlti;

                var dispartity = max - min;
                var first = numAdd(min, (numDiv(dispartity, 4) * 1).toFixed(4));
                var second = numAdd(min, (numDiv(dispartity, 4) * 2).toFixed(4));
                var third = numAdd(min, (numDiv(dispartity, 4) * 3).toFixed(4));
                var yVals = min + ";" + first + ";" + second + ";" + third + ";" + max.toFixed(4);
                if (xVals.substr(xVals.length - 1) == ";") {
                    xVals = xVals.substr(0, xVals.length - 1);
                }
                if (netVals.substr(netVals.length - 1) == ";") {
                    netVals = netVals.substr(0, netVals.length - 1);
                }
                if (fluctuates.substr(fluctuates.length - 1) == ";") {
                    fluctuates = fluctuates.substr(0, fluctuates.length - 1);
                }

                var temp = "";
                if (oldNetVal == null || oldNetVal == "" || oldNetVal == 0) {
                    temp = "<em>--</em>";
                } else {
                    temp = numMulti(numDiv((newNetVal - oldNetVal), oldNetVal), 100);
                    if (temp < 0) {
                        temp = "<em class='green'>" + numMulti(numDiv((newNetVal - oldNetVal), oldNetVal), 100).toFixed(2) + "%</em>";
                    } else if (temp == 0) {
                        temp = "<em style='color:#666666'>" + numMulti(numDiv((newNetVal - oldNetVal), oldNetVal), 100).toFixed(2) + "%</em>";
                    } else {
                        temp = "<em>+" + numMulti(numDiv((newNetVal - oldNetVal), oldNetVal), 100).toFixed(2) + "%</em>";
                    }
                }
                if (dateTime != null && dateTime == -1) {
                    htmls_1 += "最近1个月涨跌幅" + temp;
                } else if (dateTime != null && dateTime == -3) {
                    htmls_1 += "最近3个月涨跌幅" + temp;
                } else if (dateTime != null && dateTime == -6) {
                    htmls_1 += "最近6个月涨跌幅" + temp;
                } else if (dateTime != null && dateTime == -12) {
                    htmls_1 += "最近1年涨跌幅" + temp;
                }
                $("#profitByDate").html(htmls_1);

                showHighchart1(xVals, yVals, netVals, fluctuates, stepNum);
            } else {
                if (dateTime != null && dateTime == -1) {
                    htmls_1 += "最近1个月涨跌幅<em>--</em>";
                } else if (dateTime != null && dateTime == -3) {
                    htmls_1 += "最近3个月涨跌幅<em>--</em>";
                } else if (dateTime != null && dateTime == -6) {
                    htmls_1 += "最近6个月涨跌幅<em>--</em>";
                } else if (dateTime != null && dateTime == -12) {
                    htmls_1 += "最近1年涨跌幅<em>--</em>";
                }
                $("#profitByDate").html(htmls_1);
                showHighchart1("0", "0", "0", "0", stepNum);
            }
        }
    })
}
/* 查询产品净值 */
function queryEstimateByFundIdByPage(pages) {
    var fundId = $("#fundId").val();
    var pageInput = $("#page").val();
    var page = returnPage(pages);
    var isSubPrdAppraisement = $("#isSubPrdAppraisement").val();
    var outerId = $("#outerId").val();
    //子产品非估值 查询时为 系列产品净值图  产品净值不再对产品进行估值
    // if("n" == isSubPrdAppraisement.toLowerCase()){
    // 	fundId = outerId;
    // }
    $.ajax({
        async: true,
        url: "/AppService/business/queryEstimateByFundIdByPage.xhtml",
        data: {
            fundId: fundId,
            page: page
        },
        dataType: "json",
        type: "POST",
        cache: false,
        error: function (textStatus, errorThrown) {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function (data) {
            var htmls = "";
            var maxPages = data.maxPages;
            var page = data.page;
            $("#maxPages").val(maxPages);
            $("#page").val(page);

            var list = data.productEstimates;
            if (list != null && list.length > 0) {

                htmls += "<div id='tableContent'>";
                htmls += "<div class='highchart01-data'>";
                htmls += "<table width='880' border='1'>";
                htmls += "<tr>";
                htmls += "<th height='30' align='center' bgcolor='#fafafa' scope='col'>日期</th>";
                htmls += "<th align='center' bgcolor='#fafafa' scope='col'>单位净值</th>";
                htmls += "<th align='center' bgcolor='#fafafa' scope='col'>累计净值</th>";
                htmls += "<th align='center' bgcolor='#fafafa' scope='col'>区间涨幅</th>";
                htmls += "</tr>";

                $.each(list, function (i, item) {
                    htmls += "<tr>";
                    htmls += "<td height='30' align='center'>" + formatDate(item.eDate) + "</td>";
                    htmls += "<td align='center'>" + parseFloat(item.netValue).toFixed(4) + "</td>";
                    htmls += "<td align='center'>" + parseFloat(item.accNetValue).toFixed(4) + "</td>";
                    if (parseFloat(item.fluctuate) > 0) {
                        htmls += "<td align='center' style='color: #ca132c;'>+" + parseFloat(numMulti(item.fluctuate, 100)).toFixed(2) + "%</td>";
                    } else if (parseFloat(item.fluctuate) < 0) {
                        htmls += "<td align='center' style='color: #57BA4D;'>" + parseFloat(numMulti(item.fluctuate, 100)).toFixed(2) + "%</td>";
                    } else {
                        htmls += "<td align='center'>" + parseFloat(numMulti(item.fluctuate, 100)).toFixed(2) + "%</td>";
                    }
                    htmls += "</tr>";
                })

                htmls += "</table>";
                htmls += "</div>";

                htmls += "<div class='nav-href estimate'>";
                htmls += "<a href='javascript:queryEstimateByFundIdByPage(\"" + (page - 1) + "\")' class='pre'></a>";
                if (page == 1) {
                    htmls += "<a class='act' href='javascript:queryEstimateByFundIdByPage(\"1\")'>1</a>";
                } else {
                    htmls += "<a href='javascript:queryEstimateByFundIdByPage(\"1\")'>1</a>";
                }
                if (page > 3) {/* 左边加... */
                    htmls += "<a class='more'>...</a>";
                }

                if ((page - 1) > 1) {/* 上一页 */
                    htmls += "<a href='javascript:queryEstimateByFundIdByPage(\"" + (page - 1) + "\")'>" + (page - 1) + "</a>";
                }

                if (page != 1 && page != maxPages) {/* 当前页 */
                    htmls += "<a class='act' href='javascript:queryEstimateByFundIdByPage(\"" + (page) + "\")'>" + (page) + "</a>";
                }

                if (page + 1 < maxPages) {/* 下一页 */
                    htmls += "<a href='javascript:queryEstimateByFundIdByPage(\"" + (page + 1) + "\")'>" + (page + 1) + "</a>";
                }

                if ((maxPages - page) > 2) {/* 右边加... */
                    htmls += "<a class='more'>...</a>";
                }
                if (page != 1) {
                    if (maxPages == page) {
                        htmls += "<a class='act' href='javascript:queryEstimateByFundIdByPage(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
                    } else if (maxPages > page) {
                        htmls += "<a href='javascript:queryEstimateByFundIdByPage(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
                    }
                } else {
                    if (maxPages == page) {

                    } else if (maxPages > page) {
                        htmls += "<a href='javascript:queryEstimateByFundIdByPage(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
                    }
                }
                htmls += "<a href='javascript:queryEstimateByFundIdByPage(\"" + (page + 1) + "\")' class='next'></a>";
                htmls += "</div>";
                htmls += "</div>";
                $("#netval").show();
                $("#tableContent").remove();
                $("#netvalContent").append(htmls);
            }
        }
    })
}
function returnPage(pages) {
    var page;
    var maxPages = $("#maxPages").val();

    if (parseInt(pages, 10) >= parseInt(maxPages, 10)) {
        page = maxPages;
    } else if (parseInt(pages, 10) <= 0) {
        page = "1";
    } else {
        page = pages;
    }
    return page;
}
function adjustCss() {
    for (var n = 1; n < $(".box-first .content .box01 ul li").length + 1; n++) {
        if (n % 3 == 0) {
            $(".box-first .content .box01 ul li:eq(" + (n - 1) + ")").find("span").css({
                textAlign: "left",
                marginLeft: "50px",
                width: "110px",
            });
        }
    }
}
/* 最近1个月 */
function showHighchart1(xVals, yVals, netVals, fluctuates, stepNum) {
    var xArray = new Array();
    for (var i = xVals.split(";").length - 1; i >= 0; i--) {
        xArray.push((xVals.split(";")[i]).substr(4, 2) + "-" + (xVals.split(";")[i]).substr(6, 2));
    }
    var yArray = new Array();
    $.each(yVals.split(";"), function (i, item) {
        yArray.push(yVals.split(";")[i]);
    })
    var netArray = new Array();
    for (var i = netVals.split(";").length - 1; i >= 0; i--) {
        netArray.push(netVals.split(";")[i]);
    }
    var fluctuateArray = new Array();
    for (var i = fluctuates.split(";").length - 1; i >= 0; i--) {
        fluctuateArray.push(parseFloat(numMulti(fluctuates.split(";")[i], 100)).toFixed(2));
    }

    var str = "";

    /* 将查询出来的要展示在tooltip位置的值转化为json格式的字符串 */
    str += "[";
    for (var i = 0; i < netArray.length && i < fluctuateArray.length; i++) {
        str += '{"y":' + netArray[i] + ',"fluctuate":' + fluctuateArray[i] + '},';
    }
    str += "]";
    str = str.substr(0, str.lastIndexOf(",")) + str.substr(str.lastIndexOf(",") + 1);

    /* 将json格式字符串转化为json数组 */
    var json = eval('(' + str + ')');

    $('#highchart1').highcharts({
        chart: {
            type: 'line',
            width:"923",
            height:"318"
        },
        title: {
            text: false,
            x: false,
        },
        subtitle: {
            text: false,
            x: -20
        },
        xAxis: {
            categories: xArray,
            tickmarkPlacement: 'on',
            tickLength: 5,
            gridLineWidth: 1,
            gridLineDashStyle: "Dot", /* 竖网格线样式 */
            gridLineColor: "#ccc", /* 竖网格线颜色 */
            gridLineWidth: 0,
            labels: {
                step: stepNum, /* 间隔步长 */
                staggerLines: 1, /* 显示x轴的行数 */
                overflow: 'justify',
            },
        },
        yAxis: {
            title: {
                text: false,
            },
            tickPositions: yArray,
            gridLineColor: "#ebebeb",
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        credits: {
            enabled: false,
        },
        tooltip: {
            shared: true,
            borderWidth: 0,
            followTouchMove: true,
            formatter: function () {
                var a = '<b>' + this.x + '<b><br/>当期净值：' + parseFloat(this.y).toFixed(4) + '<br/>涨跌幅度：' + parseFloat(this.points[0].point.fluctuate).toFixed(2) + "%";
                return a;
            }
        },
        legend: {
            enabled: false,
        },
        series: [{
            color: '#FF8A00',
            name: false,
            data: json
        }]
    });
}

/* 取消订单 */
function cancelAppointRequest() {
    var serialno = $("#serialno").val();
    var tradeacco = $("#tradeacco").val();

    $.ajax({
        async: false,
        url: "/AppService/business/cancelAppointRequest.xhtml",
        data: {
            "serialno": serialno,
            "tradeacco": tradeacco
        },
        dataType: "json",
        cache: false,
        type: "post",
        error: function (textStatus, errorThrown) {
            close_tips('cancelOrderDiv');
            show_tips("取消订单失败");
        },
        success: function (data) {
            if (data.returnCode != null && data.returnCode == "0000") {
                close_tips('cancelOrderDiv');
                close_tips('tips');
                show_tips("取消订单成功");
                setTimeout('gotoUrl("/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO")', 2000);
            } else {
                close_tips('cancelOrderDiv');
                close_tips('tips');
                show_tips("取消订单失败");
            }
        }
    });
}
/* 4个时间节点点击显示事件 */
function show_fundInfo(_id) {
    $("#fundInfo_tips").show();
    if ($("#typeId").val() == "0110" && _id == 'maturityDate_info') {
        $("#maturityDate_T").siblings().hide();
        $("#maturityDate_T").show();
    } else {
        $("#" + _id).siblings().hide();
        $("#" + _id).show();
    }
}
/* 4个时间节点点击隐藏事件 */
function close_fundInfo(_id) {
    $("#" + _id).hide();
    $("#" + _id).children().hide();
}

/* 查询电子合同 TXT */
function queryEcontrantByFundIdN() {
    var fundid = $("#fundId").val();
    var period = $("#period").val();
    var money = $("#money").val();
    $.ajax({
        async: false,
        url: "/AppService/business/queryFundContractById.xhtml",
        data: {
            "fundId": fundid,
            "period": period
        },
        dataType: "json",
        cache: false,
        type: "POST",
        error: function (textStatus, errorThrown) {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function (data) {
            var htmls = "";
            if (data.fundContractDto != null) {
                htmls += "<a href='javascript:toContract(\"" + fundid + "\",\"" + unformat($("#money").val()) + "\");'>《" + data.fundContractDto.templateName + "》</a>";
                $("#econtrant").html(htmls).parent().show();
            } else {
                $("#econtrant").parent().remove();
            }
        }
    });
}
function toContract(fundid, money) {
    var period = $("#period").val();
    var m = unformat($("#money").val());
    var link = "/AppService/business/fund/contract.shtml?period=" + period + "&fundId=" + fundid + "&money=" + m;
    window.open(link);
}
/* 查看汇款信息 */
function queryOrder(orderType) {
    getUserRequest("pc_orderDetail_queryOrder");
    var serialno = $("#serialno").val();
    var subamt = $("#subamt").val();
    var fee = $("#fee").val();
    var bankacco = $("#bankacco").val();
    var realBankName = $("#realBankName").val();
    bankacco = (bankacco || "").substr(bankacco.length - 4);
    var htmls = "";
    htmls += "<div class='main-con pay-offline'>";
    htmls += "<i class='close'></i>";
    if (orderType != null && orderType == "1") {
        htmls += "<h2>线下汇款信息</h2>";
    } else if (orderType != null && orderType == "3") {
        htmls += "<h2>排队信息</h2>";
        htmls += "<span class='point' style='display:block;width:395px;margin:10px auto -20px;'>尚未获得产品份额，请勿汇款!</span>";
    }
    htmls += "<ul class='update'>";
    htmls += "<li><label for='payaccounts'>付款账号：</label> <span>" + realBankName + "(尾号" + bankacco + ")</span> <a href='javascript:gotoUpdateBankCard();'>更改银行卡</a></li>";
    htmls += "<li><label for='paymoney'>付款金额：</label> <span>" + formatNumber(parseFloat(subamt || 0) + parseFloat(fee || 0)) + "元</span> <a href='javascript:gotoUpdateMoney();'>修改金额</a></li>";
    htmls += "</ul>";
    htmls += "<ul class='paremeter'>";
    htmls += "<li><em>收款银行：</em><span>招商银行总行营业部</span></li>";
    htmls += "<li><em>收款账户：</em><span>招商财富资产管理有限公司</span></li>";
    htmls += "<li><em>收款账号：</em><span>9551 0827 0000 009</span></li>";
    htmls += "</ul>";
    htmls += "<a href='javascript:close_tips(\"updateOrderDiv\");' class='btn'>我知道了</a>";
    htmls += "</div>";
    $("#updateOrderDiv").html(htmls);
}
/*展示汇款信息*/
function queryOrderShow() {
    $("#updateOrderDiv").show();
}
/* 修改银行卡弹窗 */
function gotoUpdateBankCard() {
    getUserRequest("pc_orderDetail_updateBank");
    getRandomCode();
    $("#updateOrderDiv").hide();
    queryMyBankCardShow();
}
/* 我的银行卡信息 */
function queryMyBankCard() {
    var tradeAcco = $("#tradeacco").val();
    $.ajax({
        async: false,
        url: "/AppService/business/queryMyBankCardNo.xhtml",
        data: {},
        dataType: "json",
        cache: false,
        type: "POST",
        error: function (textStatus, errorThrown) {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function (data) {
            if (data.returnCode == '0000') {
                var htmlBank = "";
                $.each(data.tradeAcctlist, function (i, item) {
                    if (item.tradeAcco == tradeAcco) {
                        $(".select-value").text(item.bankNm + "（尾号" + (item.bankAccoDisplay || 0).substr(item.bankAccoDisplay.length - 4) + ")");
                        $(".select-value").attr("data-bankNm", item.bankNm);
                        $(".select-value").attr("data-bankNo", item.bankNo);
                        $(".select-value").attr("data-bankAccoDisplay", item.bankAccoDisplay);
                        $(".select-value").attr("data-tradeAcco", item.tradeAcco);
                        htmlBank += "<a href='javascript:selectCard(" + i + ")' class='act' id='card_" + i + "'data-bankNm='" + item.bankNm + "' data-bankNo='" + item.bankNo + "' data-bankAccoDisplay='"
                            + item.bankAccoDisplay + "' data-tradeAcco='" + item.tradeAcco + "'>" + item.bankNm + "（尾号" + (item.bankAccoDisplay || 0).substr(item.bankAccoDisplay.length - 4)
                            + "）</a>";
                    } else {
                        htmlBank += "<a href='javascript:selectCard(" + i + ")' id='card_" + i + "'data-bankNm='" + item.bankNm + "' data-bankNo='" + item.bankNo + "' data-bankAccoDisplay='"
                            + item.bankAccoDisplay + "' data-tradeAcco='" + item.tradeAcco + "'>" + item.bankNm + "（尾号" + (item.bankAccoDisplay || 0).substr(item.bankAccoDisplay.length - 4)
                            + "）</a>";
                    }
                });
                $(".select-text-list").html(htmlBank);
            }
        }
    });
}
/*展示修改银行卡页面*/
function queryMyBankCardShow() {
    $("#updateOrderBankCardDiv").show();
    $(".select-value").click(function () {
        $(".select-text").toggleClass("none");
    });
    $(document).bind("click", function (e) {
        var target = $(e.target);
        if (target.closest(".select-value,.select-text").length == 0) {
            $(".select-text").addClass("none");
        }
    });
}
function selectCard(textId) {
    $(".select-value").text($("#card_" + textId).html());
    $(".select-value").attr("data-bankNm", $("#card_" + textId).attr("data-bankNm"));
    $(".select-value").attr("data-bankNo", $("#card_" + textId).attr("data-bankNo"));
    $(".select-value").attr("data-bankAccoDisplay", $("#card_" + textId).attr("data-bankAccoDisplay"));
    $(".select-value").attr("data-tradeAcco", $("#card_" + textId).attr("data-tradeAcco"));
    $("#card_" + textId).addClass("act").siblings().removeClass("act");
    $(".select-text").addClass("none");
}
/* 修改支付银行卡 */
function modifyAppointRequestBankCard() {
    var serialno = $("#serialno").val();
    var oldTradeAcco = $("#tradeacco").val();
    var tradeAcco = $(".select-value").attr("data-tradeAcco");
    var tPassWord = $("#tPassWordBank").val();
    var randomCode = $("#randomCode").val();

    if (oldTradeAcco == tradeAcco) {
        show_tips("待更换银行卡不能和原订单银行卡一致");
        return;
    } else if (tPassWord == null || tPassWord == "") {
        show_tips("请输入安全码");
        return;
    } else if (randomCode == null || randomCode == "") {
        show_tips("请输入验证码");
        return;
    } else {
        $("#tPassWordBank,#randomCode").val("");
        $.ajax({
            async: false,
            url: "/AppService/business/modifyAppointRequest.xhtml",
            data: {
                "serialno": serialno,
                "tradeAcco": tradeAcco,
                "tPassWord": tPassWord,
                "randomCode": randomCode
            },
            dataType: "json",
            cache: false,
            type: "POST",
            error: function (textStatus, errorThrown) {
                show_tips("网络繁忙，请稍后再试。");
            },
            success: function (data) {
                if (data.returnCode != null && data.returnCode == "0000") {
                    $("#updateOrderBankCardDiv").hide();
                    show_tips("修改成功");
                    setTimeout("window.location.reload();", 2000);
                } else if (data.returnCode == "USR-1I01") {
                    show_tips("错误次数过多3小时后重试");
                    $("#randomCode,#tPassWordBank").val("");
                    $("#rondomCodeImg").click();
                    return;
                } else if (data.returnCode == "USR-1I02") {
                    if (data.tPwdErrCount == 1) {
                        show_tips("安全码有误");
                        $("#tPassWordBank").val("");
                    } else if (data.tPwdErrCount > 1 && data.tPwdErrCount < 6) {
                        show_tips("安全码有误还有" + (6 - parseInt(data.tPwdErrCount, 10)) + "次机会");
                    }
                    $("#randomCode,#tPassWordBank").val("");
                    $("#rondomCodeImg").click();
                    return;
                } else {
                    show_tips(data.returnMsg);
                    $("#randomCode").val("");
                    $("#rondomCodeImg").click();
                    return;
                }
            }
        });
    }
}
/* 关闭修改银行卡弹窗 */
function closeUpdateOrderBankCard() {
    $("#tPassWordBank,#randomCode").val("");
    $("#rondomCodeImg").click();
    $("#updateOrderBankCardDiv").hide();
}
/* 关闭修改续投方式弹窗 */
function closeUpdateRenew() {
    $("#tPassWordBank,#randomCode").val("");
    $("#renewTips").hide();
}
/* 修改金额弹窗 */
function gotoUpdateMoney() {
    getUserRequest("pc_orderDetail_updateMoney");
    getRandomCode1();
    $("#updateOrderDiv").hide();
    updateMoney();
}
/* 修改金额弹窗 */
function updateMoney() {
    var money = $("#oldMoney").val();
    $("#newMoney").val(formatNumber(money, ','));
    $("#updateOrderMoneyDiv").show();
}
function modifyAppointRequestRenew() {
    var serialno = $("#serialno").val();
    var tradeAcco = $("#tradeacco").val();
    var renew = $('#renewTips input[name="renew"]:checked ').val();
    var payPassword = $('#payPassword').val();
    var randomCode2 = $('#randomCode2').val();
    var mobile = $('#mobile').val();
    if (renew == $('#oldRenew').val()) {
        show_tips("续投方式不能与原续投方式相同");
        return;
    } else if (payPassword == null || payPassword == "") {
        show_tips("请输入安全码");
        return;
    } else if (randomCode2 == null || randomCode2 == "") {
        show_tips("请输入验证码");
        return;
    } else {
        $("#payPassword,#randomCode2").val("");
        $.ajax({
            async: false,
            url: "/AppService/business/modifyAppointRequest.xhtml",
            data: {
                "serialno": serialno,
                "tradeAcco": tradeAcco,
                "renew": renew,
                "tPassWord": payPassword,
                "randomCode": randomCode2,
                "mobile": mobile
            },
            dataType: "json",
            cache: false,
            type: "POST",
            error: function (textStatus, errorThrown) {
                show_tips("网络繁忙，请稍后再试。");
            },
            success: function (data) {
                if (data.returnCode != null && data.returnCode == "0000") {
                    $("#PayPasswordDiv").hide();
                    show_tips("修改成功");
                    setTimeout("window.location.reload();", 1300);
                } else if (data.returnCode == "USR-1I01") {
                    show_tips("错误次数过多3小时后重试");
                    $("#rondomCodeImg2").click();
                    return;
                } else if (data.returnCode == "USR-1I02") {
                    if (data.tPwdErrCount == 1) {
                        show_tips("安全码有误");
                    } else if (data.tPwdErrCount > 1 && data.tPwdErrCount < 6) {
                        show_tips("安全码有误还有" + (6 - parseInt(data.tPwdErrCount, 10)) + "次机会");
                    }
                    $("#randomCode2,#payPassword").val("");
                    $("#rondomCodeImg2").click();
                    return;
                } else {
                    show_tips(data.returnMsg);
                    $("#rondomCodeImg2").click();
                    return;
                }
            }
        });
    }
}

/* 修改订单金额 */
function modifyAppointRequestMoney() {
    var serialno = $("#serialno").val();
    var tradeAcco = $("#tradeacco").val();
    var tradeAmt = unformat($("#newMoney").val());
    var tPassWord = $("#tPassWordOrder").val();
    var randomCode = $("#randomCode1").val();
    var oldMoney = $("#oldMoney").val();
    if (!checkedMoney()) {
        return;
    } else if (tradeAmt == oldMoney) {
        show_tips("待修改金额不能和原订单金额一致");
        return;
    } else if (tPassWord == null || tPassWord == "") {
        show_tips("请输入安全码");
        return;
    } else if (randomCode == null || randomCode == "") {
        show_tips("请输入验证码");
        return;
    } else {
        var fee = $("#fee").val();
        $("#tPassWordOrder,#randomCode1").val("");
        $.ajax({
            async: false,
            url: "/AppService/business/modifyAppointRequest.xhtml",
            data: {
                "serialno": serialno,
                "tradeAcco": tradeAcco,
                "tradeAmt": tradeAmt,
                "tPassWord": tPassWord,
                "randomCode": randomCode,
                "fee": fee
            },
            dataType: "json",
            cache: false,
            type: "POST",
            error: function (textStatus, errorThrown) {
                show_tips("网络繁忙，请稍后再试。");
            },
            success: function (data) {
                if (data.returnCode != null && data.returnCode == "0000") {
                    $("#updateOrderMoneyDiv").hide();
                    show_tips("修改成功");
                    setTimeout("window.location.reload();", 2000);
                } else if (data.returnCode == "USR-1I01") {
                    show_tips("错误次数过多3小时后重试");
                    $("#randomCode1,#tPassWordOrder").val("");
                    $("#rondomCodeImg1").click();
                    return;
                } else if (data.returnCode == "USR-1I02") {
                    if (data.tPwdErrCount == 1) {
                        show_tips("安全码有误");
                    } else if (data.tPwdErrCount > 1 && data.tPwdErrCount < 6) {
                        show_tips("安全码有误还有" + (6 - parseInt(data.tPwdErrCount, 10)) + "次机会");
                    }
                    $("#randomCode1,#tPassWordOrder").val("");
                    $("#rondomCodeImg1").click();
                    return;
                } else {
                    show_tips(data.returnMsg);
                    $("#randomCode1").val("");
                    $("#rondomCodeImg1").click();
                    return;
                }
            }
        });
    }
}

function queryTradeByTradeNo() {
    var tradeAcco = $('#tradeacco').val()
    $.ajax({
        async: false,
        url: "/AppService/business/queryUserTradeAcctInfo.xhtml",
        type: "post",
        dataType: 'json',
        data: {
            'tradeNo': tradeAcco
        },
        error: function () {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function (data, textStatus) {
            var dto = data.tradeAcctList;
            if (dto != null) {
                $('#mobile').val(dto[0].mobile);
            }
        }
    });
}
/* 关闭修改金额弹窗 */
function closeUpdateMoney() {
    $("#tPassWordOrder,#randomCode1").val("");
    $("#rondomCodeImg1").click();
    $("#updateOrderMoneyDiv").hide();
}
/* 判断预约金额是否符合格式 */
function checkedMoney() {
    var scale = parseFloat(unformat($("#scale").html()));
    var moneyZero = parseFloat(unformat($("#productMoney").val()));
    /* 认购起点 */
    var money = parseFloat(unformat($.trim($("#newMoney").val())));
    /* 认购金额 */
    var moneyStep = parseFloat(unformat($("#moneyStep").val()));
    /* 认购步长 */
    var displayLimit = parseFloat(unformat($("#displayLimit").val()));
    /* 剩余额度 */
    var oldMoney = $("#oldMoney").val();
    var buyType = $("#buyType").val();

    if (!Validater.isPureNumber(money)) {
        show_tips("请输入正确的预约额度");
        return false;
    } else if (money >= 1000000000) {
        show_tips("输入金额过大，请重新输入");
        return false;
    } else if (money < moneyZero || (money - moneyZero) % moneyStep != 0) {
        show_tips("本产品" + formatNumber(numDiv(moneyZero, 10000), ',') + "万起售，" + formatNumber(numDiv(moneyStep, 10000), ',') + "万递增");
        return false;
    } else if (money - oldMoney > displayLimit && buyType != "4") {
        show_tips("仅剩下" + numDiv(moneyZero + oldMoney, 10000) + "万元份额");
        return false;
    } else if (buyType == "4" && money > scale) {
        show_tips("预约金额不能高于产品发售规模");
        return false;
    } else {
        queryFeeRateList();
        return true;
    }
}
function tipsShow(_id) {
    $('#' + _id).show();
}
/* 查询费率和折扣 */
function queryFeeRateList() {
    var fundId = $("#fundId").val();
    fundId = removeSpecialStr(fundId);
    var channelNoList = "";
    var custLevel = "";
    var money = unformat($("#newMoney").val());
    $.ajax({
        async: false,
        url: "/AppService/business/queryFeeRateList.xhtml",
        type: "post",
        dataType: 'json',
        data: {
            'fundId': fundId,
            'channelNoList': channelNoList,
            'custLevel': custLevel,
            'money': money,
        },
        error: function () {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function (data, textStatus) {
            if (!!data) {
                var typeId = $("#typeId").val();
                var rate = data.rate;
                if (rate == null || rate == "" || rate == "0") {
                    /* 认购费：<em>1000</em>元 */
                } else {
                    if (typeId == "0100") {
                        $("#fee").val(data.rate);
                    } else {
                        $("#fee").val(data.rate);
                    }
                }
            }
        }
    });
}

function openTip(renew) {
    $('#renewTips').show();
    var list = $('#renewTips input[name="renew"]');
    $.each(list, function (i, item) {
        if (item.value == renew)
            $(item).attr('checked', 'checked');
    })
    $('#oldRenew').val(renew);
}

function closePayPassword() {
    $("#payPassword,#randomCode2").val("");
    $("#PayPasswordDiv").hide();
}
function alterPayPasswordDiv() {
    var renew = $('#renewTips input[name="renew"]:checked ').val();
    if (renew == $('#oldRenew').val()) {
        show_tips("续投方式不能与原续投方式相同");
        return;
    }
    $('#renewTips').hide();
    $("#PayPasswordDiv").show();
}

function queryEstimate(){
    queryEstimateByFundId("-1");
    queryEstimateByFundIdByPage("1");
}

