var i = 0;
var k = 0;
checkCompanyUser()
$(document).ready(function () {
    getUserRequest("pc_otherOrderDetail");
    $(".nav.fr ul li a").removeClass("current");
    document.title = "订单详情_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
    queryTradeInfoByTradeNo();
    $(".box-third .content .box01").click(function () {
        $(this).toggleClass("act").siblings().removeClass("act");
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
/* 查询订单信息*/
function queryTradeInfoByTradeNo() {
    var serialno = getUrlParameter("serialno");
    var fundId = getUrlParameter("fundId");
    var channleNo = getUrlParameter("channleNo");
    if (serialno == null || typeof(serialno) == "undefined") {
        serialno = "";
    } else {
        serialno = removeSpecialStr(serialno);
    }
    if (fundId == null || typeof(fundId) == "undefined") {
        fundId = "";
    } else {
        fundId = removeSpecialStr(fundId);
    }
    if (channleNo == null || typeof(channleNo) == "undefined") {
        channleNo = "";
    } else {
        channleNo = removeSpecialStr(channleNo);
    }
    $.ajax({
        async: false,
        url: "/AppService/setUp/queryCompanyOtherDetailOrder.xhtml",
        type: "post",
        dataType: 'json',
        data: {
            serialno: serialno,
            fundId: fundId,
            channleNo: channleNo
        },
        error: function () {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function (data, textStatus) {
            var dto = data.agentFundDto;
            if (data.agentFundDto != null) {
                console.log(dto.fundnm)
                var fundInfoDto = data.fundInfoDtoV2;
                if (fundInfoDto != null) {
                    var typeId = fundInfoDto.typeId;

                    var fundIDS = fundInfoDto.fundId;
                    $("#typeId").val(typeId);
                    $("#fundId").val(fundInfoDto.fundId);
                    $("#period").val(fundInfoDto.period);
                    $("#serialno").val(dto.serialno);
                    $("#money").val(fundInfoDto.money);
                    $("#isSubPrdAppraisement").val(fundInfoDto.isSubPrdAppraisement);
                    $("#outerId").val(fundInfoDto.outerId);
                    /* 隐藏input赋值 */
                    var today = "" + fundInfoDto.currentWorkdate;
                    var appointEndDate = fundInfoDto.appointEndDate;
                    /*预约结束日期*/
                    var profit = fundInfoDto.profit;
                    /* 产品预期收益率*/
                    var termInDay = fundInfoDto.termInDay;
                    /* 存续期限，以天为单位*/
                    var term = fundInfoDto.term;
                    /* 存续期限，存展现，不带单位*/
                    var termUnit = fundInfoDto.termUnit;
                    /* 存续期限单位*/
                    var scale = parseFloat(fundInfoDto.scale);
                    /* 产品规模*/
                    var displayLimit = parseFloat(fundInfoDto.displayLimit);
                    /* 页面展示剩余额度*/
                    var money = parseFloat(fundInfoDto.money);
                    /* 最低认购金额*/
                    var moneyStep = parseFloat(fundInfoDto.moneyStep);
                    /* 认购步长*/
//					var sellPercent = numMulti(numDiv((scale-displayLimit),scale),100).toFixed(0);/* 产品额度(销售进度百分比)*/
                    var profitTemp = numMulti(parseFloat(profit), 100).toFixed(2) + "";
                    var appointDate = fundInfoDto.appointDate;
                    /*预约开始日期*/
                    var appointEndDate = fundInfoDto.appointEndDate;
                    /*预约结束日期*/
                    var salesDate = fundInfoDto.salesDate;
                    /*发售日*/
                    var subdeadLine = fundInfoDto.subdeadLine;
                    /*认购截止*/
                    var interestDate = fundInfoDto.interestDate;
                    /*起息日期*/
                    var maturityDate = fundInfoDto.maturityDate;
                    /*到期日期*/
                    var paymentDate = fundInfoDto.paymentDate;
                    /*产品到期清盘，预计打款日期*/
                    var currentWorkdate = fundInfoDto.currentWorkdate;
                    /*当前工作日*/
                    var isSubPrdAppraisement = fundInfoDto.isSubPrdAppraisement;
                    var prjLName = fundInfoDto.prjLName;

                    /*-------------------- 订单详情展示  S --------------------*/
                    var subamt = dto.bugAmt;
                    if (!isNaN(parseFloat(subamt))) {
                        subamt = "<em>" + formatNumber(subamt) + "</em><i>元</i>";
                    }
                    var benefit = dto.profit;
                    /* 预期收益 */
                    var shareAmt = dto.shareAmt;
                    /* 分配金额 */
                    if (isNaN(benefit) || isNaN(parseFloat(benefit))) {
                        benefit = "<em>---</em>";
                    } else {
                        if (parseFloat(benefit) > 0) {
                            benefit = "<em>+" + format(parseFloat(benefit)) + "</em><i>元</i>";
                        } else if (parseFloat(benefit) < 0) {
                            benefit = "<em>-" + format(parseFloat(benefit)) + "</em><i>元</i>";
                        } else {
                            benefit = "<em>" + format(parseFloat(benefit)) + "</em><i>元</i>";
                        }
                    }
                    if (isNaN(shareAmt) || isNaN(parseFloat(shareAmt))) {
                        shareAmt = "<em>---</em>";
                    } else {
                        if (parseFloat(shareAmt) > 0) {
                            shareAmt = "<em>+" + format(parseFloat(shareAmt)) + "</em><i>元</i>";
                        } else if (parseFloat(shareAmt) < 0) {
                            shareAmt = "<em>-" + format(parseFloat(shareAmt)) + "</em><i>元</i>";
                        } else {
                            shareAmt = "<em>" + format(parseFloat(shareAmt)) + "</em><i>元</i>";
                        }
                    }

                    if (dto.applySt != null && dto.applySt == "G") {/* 存续中 */

                        $("#orderDetail h1.floatincome").html("存续中...").addClass("saveextend");
                        $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("买入金额：" + subamt);
                        /* 订单金额 */

                        if (fundInfoDto.typeId != null && fundInfoDto.typeId == "0100") {/*固定收益产品*/
//							$("#orderDetail .head-righ-con div.order-info p:eq(1)").html("预期收益："+benefit);/* 预期收益 */
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").remove();
                            /* 预期收益 */
                        } else if (fundInfoDto.typeId != null && (fundInfoDto.typeId == "0110" || fundInfoDto.typeId == "0210" || fundInfoDto.typeId == "0220" )) {/*净值产品*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("持仓盈亏：" + benefit);
                            /* 持仓盈亏 */
                        } else if (fundInfoDto.typeId != null && fundInfoDto.typeId == "0300") {/*浮动收益产品*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("已分配金额：" + shareAmt);
                            /* 预期收益 */
                        } else {/*默认浮动收益产品展示*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("已分配金额：" + shareAmt);
                            /* 预期收益 */
                        }
                        $("#orderDetail .head-righ-con div.check-cancel").remove();
                        /*去掉按钮--查看汇款信息和取消订单的按钮*/
                        $("#orderDetail .head-righ-con p.point").html("产品处于存续期，预计将于<em>" + formatDate1(maturityDate) + "</em>到期");
                        /*订单提示语*/
                    } else if (dto.applySt != null && dto.applySt == "Z") {/* 已到期 */

                        $("#orderDetail h1.floatincome").html("已到期...").addClass("timeout");

                        $("#orderDetail .head-righ-con div.order-info p:eq(0)").html("买入金额：" + subamt + "</em>");
                        /* 订单金额 */
                        if (fundInfoDto.typeId != null && fundInfoDto.typeId == "0100") {/*固定收益产品*/
                            //$("#orderDetail .head-righ-con div.order-info p:eq(1)").html("投资收益：" + benefit);
                            /* 预期收益 */
                        } else if (fundInfoDto.typeId != null && (fundInfoDto.typeId == "0210" || fundInfoDto.typeId == "0220")) {/*净值产品*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("持仓盈亏：" + benefit);
                            /* 持仓盈亏 */
                            /*$("#orderDetail .head-righ-con div.order-info p:eq(1)").html("持仓盈亏："+benefit); 预期收益 */
                        } else if (fundInfoDto.typeId != null && fundInfoDto.typeId == "0300") {/*浮动收益产品*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("总分配金额：" + shareAmt);
                            /* 预期收益 */
                        } else {/*默认浮动收益产品展示*/
                            $("#orderDetail .head-righ-con div.order-info p:eq(1)").html("总分配金额：" + shareAmt);
                            /* 预期收益 */
                        }
                        $("#orderDetail .head-righ-con div.check-cancel").remove();
                        /*去掉按钮--查看汇款信息和取消订单的按钮*/
                        $("#orderDetail .head-righ-con p.point").html("产品已到期，预计将于5个工作日内完成收益分配");
                        /*订单提示语*/
                    }
                    /*-------------------- 订单详情展示  E --------------------*/

                    queryEstimate();

                    /*-------------------- 产品详情  个性化展示  S --------------------*/
                    if (typeId != null && typeId == "0100") { /* 固定收益 */
                        $("#top_left_01").show();
                        $("#top_left_02,#top_left_03,#top_left_04").remove();

                        $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                        $("#term").html("<em>" + term + "</em>" + termUnit);
                        $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");
                        $("#QAtelG").show();
                    } else if (typeId != null && typeId == "0110") { /* 固收净值 */
                        $("#top_left_04").show();
                        $("#top_left_01,#top_left_02,#top_left_03").remove();

                        if (dto.applySt != null && dto.applySt == "G") {/* 存续中 */
                            $("#top_left_04 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);
                            /*最新净值*/
                            $("#top_left_04 #netval_profit").html("<em>" + numMulti((parseFloat(fundInfoDto.latestNewValue) - dto.confirmNav), 100).toFixed(2) + "</em>%");
                            /*累计收益率*/
                            $("#top_left_04 #netval_text").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + fundInfoDto.lastNavDate + "</i></b></em>");
                            /*描述*/
                            $("#top_left_04 #maxNetValue").html(parseFloat(fundInfoDto.maxNav).toFixed(4));
                            /*历史最高净值*/
                        } else if (dto.applySt != null && dto.applySt == "Z") {/* 已到期 */

                            $("#top_left_04 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);
                            /*最新净值*/
                            $("#top_left_04 #netval_profit").html("<em>" + numMulti((parseFloat(fundInfoDto.latestNewValue) - dto.confirmNav), 100).toFixed(2) + "</em>%");
                            /*累计收益率*/
                            $("#top_left_04 #netval_text").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + fundInfoDto.lastNavDate + "</i></b></em>");
                            /*描述*/
                            $("#top_left_04 #maxNetValue").html(parseFloat(fundInfoDto.maxNav).toFixed(4));
                            /*历史最高净值*/
                        }

                    } else if (typeId != null && (typeId == "0210" || typeId == "0220")) {/* 开放净值类产品/封闭净值类产品*/
                        $("#top_left_03").show();
                        $("#top_left_01,#top_left_02,#top_left_04").remove();
                        $("#profit_text").html("官网直销免认购费");

                        if (dto.applySt != null && dto.applySt == "G") {/* 存续中 */

                            $("#top_left_03 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);
                            /*最新净值*/
                            $("#top_left_03 #netval_profit").html("<em>" + numMulti((parseFloat(fundInfoDto.latestNewValue) - dto.confirmNav), 100).toFixed(2) + "</em>%");
                            /*累计收益率*/
                            $("#top_left_03 #netval_text").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + fundInfoDto.lastNavDate + "</i></b></em>");
                            /*描述*/
                            $("#top_left_03 #maxNetValue").html(parseFloat(fundInfoDto.maxNav).toFixed(4));
                            /*历史最高净值*/


                            // $("#netval").show();/* 净值相关查询 */
                            if ("n" == isSubPrdAppraisement.toLowerCase()) {
                                 downText(prjLName);
                             }
                            // queryEstimateByFundId("-1");
                            // queryEstimateByFundIdByPage("1");
                            /* 查询产品净值 */
                        } else if (dto.applySt != null && dto.applySt == "Z") {/* 已到期 */

                            $("#top_left_03 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);
                            /*最新净值*/
                            $("#top_left_03 #netval_profit").html("<em>" + numMulti((parseFloat(fundInfoDto.latestNewValue) - dto.confirmNav), 100).toFixed(2) + "</em>%");
                            /*累计收益率*/
                            $("#top_left_03 #netval_text").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + fundInfoDto.lastNavDate + "</i></b></em>");
                            /*描述*/
                            $("#top_left_03 #maxNetValue").html(parseFloat(fundInfoDto.maxNav).toFixed(4));
                            /*历史最高净值*/

                            // $("#netval").show();
                            // /* 净值相关查询 */
                             if ("n" == isSubPrdAppraisement.toLowerCase()) {
                                 downText(prjLName);
                             }
                            // queryEstimateByFundId("-1");
                            // queryEstimateByFundIdByPage("1");
                            /* 查询产品净值 */
                        }
                    } else if (typeId != null && typeId == "0300") {/* 浮动收益 */
                        $("#top_left_02").show();
                        $("#top_left_01,#top_left_03,#top_left_04").remove();

                        $("#profit").html("<i>" + profitTemp.split(".")[0] + ".</i>" + profitTemp.split(".")[1] + "%");
                        $("#term").html("<em>" + term + "</em>" + termUnit);
                        $("#scale").html("<em>" + numDiv(scale, 10000) + "</em>万");
                        $("#QAtelF").show();
                    }
                    /*-------------------- 产品详情  个性化展示  E --------------------*/


                    /*-------------------- 产品详情  通用展示  S --------------------*/
                    $("#adname").html(dto.fundnm);
                    /* 产品名称*/
                    $("#typeName").html(fundInfoDto.typeName);
                    /* 产品类型名称*/
                    $("#adtext").html(fundInfoDto.adtext);
                    /* 广告语*/
                    $("#appoint_date_tips").html(parseInt(unformat1(appointEndDate).substr(0, 4), 10) + "年" + parseInt(unformat1(appointEndDate).substr(4, 2), 10) + "月" + parseInt(unformat1(appointEndDate).substr(6, 2), 10) + "日15:00");

                    $("#appointDate").html(formatDate(appointDate));
                    /*预约开始日期*/
                    $("#salesDate").html(formatDate(salesDate));
                    /*发售日*/
                    $("#subdeadLine").html(formatDate(subdeadLine));
                    /*认购截止*/
                    $("#maturityDate").html(formatDate(maturityDate));
                    /*到期日期*/

                    if (daysBetween(currentWorkdate, maturityDate) >= 0) {/*到期日期*/
                        $("#timeAxis .process-right.fl dl").removeClass("act");
                        $("#timeAxis .process-right.fl dl:lt(4)").addClass("act");
                    } else if (daysBetween(currentWorkdate, subdeadLine) > 0) {/*认购截止日期*/
                        $("#timeAxis .process-right.fl dl").removeClass("act");
                        $("#timeAxis .process-right.fl dl:lt(3)").addClass("act");
                    } else if (daysBetween(currentWorkdate, subdeadLine) == 0) {/*认购截止日期*/
                        $("#timeAxis .process-right.fl dl").removeClass("act");
                        $("#timeAxis .process-right.fl dl:lt(3)").addClass("act");
                    } else if (daysBetween(currentWorkdate, salesDate) >= 0) {/*发售日*/
                        $("#timeAxis .process-right.fl dl").removeClass("act");
                        $("#timeAxis .process-right.fl dl:lt(2)").addClass("act");

                    } else if (daysBetween(currentWorkdate, appointDate) >= 0) {/*预约日*/
                        $("#timeAxis .process-right.fl dl").removeClass("act");
                        $("#timeAxis .process-right.fl dl:lt(1)").addClass("act");

                    } else if (daysBetween(currentWorkdate, appointDate) < 0) {/*预约日*/
                        $("#timeAxis .process-right.fl dl").removeClass("act");
                    }
                    /*-------------------- 产品详情  通用展示  E --------------------*/


                    var elementList = fundInfoDto.elementList;
                    if (elementList != null && elementList.length > 0) {
                        var htmls1 = "";
                        var htmls2 = "";
                        var htmls3 = "";
                        $.each(elementList, function (i, item) {
                            if (item.type > 100 && item.type < 200) {/* 产品基本信息： 110-小标题； 120-大标题； 130-图片*/

                                if (item.type == 110) {
                                    htmls1 += "<li class='align'><span class='litter_title'>" + item.title + "</span><em>" + item.content + "</em></li>";
                                } else if (item.type == 120) {
                                    htmls2 += "<div class='box'>";
                                    htmls2 += "<span class='left'>" + item.title + "</span>";
                                    htmls2 += "<span style='word-break: break-all' class='right'>" + item.content + "</span>";
                                    htmls2 += "</div>";
                                }
                            }
                            if (item.type > 200 && item.type < 300) {/* 投资项目信息： 210-小标题； 220-大标题； 230-图片*/

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
                                if (htmls1 == "") {
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
                }

                /*合同*/
                /*20180103crm数据迁移需求, 移除查询旧版PDF合同*/
                /*查询 产品合同 txt*/
                queryEcontrantByFundIdN();

                /*问题展示*/
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
/*查询产品净值(曲线图)*/

function queryEstimateByFundId(dateTime){
	var fundId = $("#fundId").val();
	var isSubPrdAppraisement = $("#isSubPrdAppraisement").val();
    //var outerId = $("#outerId").val();
    //子产品非估值 查询时为 系列产品净值图  产品净值不再对产品进行估值
    // if("n" == isSubPrdAppraisement.toLowerCase()){
    // 	fundId = outerId;
    // }

	$("#highchart .highchart_nav ul li").removeClass("act");
	if(parseInt(dateTime,10) == -1){
		$("#highchart .highchart_nav ul li:eq(0)").addClass("act");
	}else if(parseInt(dateTime,10) == -3){
		$("#highchart .highchart_nav ul li:eq(1)").addClass("act");
	}else if(parseInt(dateTime,10) == -6){
		$("#highchart .highchart_nav ul li:eq(2)").addClass("act");
	}else if(parseInt(dateTime,10) == -12){
		$("#highchart .highchart_nav ul li:eq(3)").addClass("act");
	}
	$.ajax({
		async:true,
		url : "/AppService/setUp/queryCompanyUserFundNetValueByImgTable.xhtml",
		data : {
			fundId:fundId,
			dateTime:dateTime,
		},
		dataType : "json",
        type:"POST",
		cache : false,
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data){
			var xVals = "";
			var yVals = "";
			var netVals = "";
			var fluctuates = "";
			var htmls_1 = "";
			var productEstimateList = "";
			var stepNum = 1;
			if(data != null){
				productEstimateList = data.productEstimateList;
			}
			if (productEstimateList != null && productEstimateList != "" && productEstimateList.length > 0) {
				stepNum = Math.ceil(numDiv(parseInt(productEstimateList.length,10),20));

				var maxVal = "1";
				if(productEstimateList != null && productEstimateList.length > 0){
					maxVal = productEstimateList[0].maxNetValue;
				}
				var yVarMaxlti = 0;
				var yValMinlti=0;

				var newNetVal = 0;
				var oldNetVal = 0;

				$.each(productEstimateList,function(i, item){
					xVals += item.eDate + ";";
					netVals += item.netValue + ";";
					fluctuates += item.fluctuate + ";";
					if (yValMinlti>item.netValue) {
						yValMinlti = item.netValue;
					}
					if (yVarMaxlti < item.netValue) {
						yVarMaxlti = item.netValue;
					}
					if(i == 0){
						newNetVal = item.netValue;
						yValMinlti = item.netValue;
						yVarMaxlti = item.netValue;
					}else if(i == productEstimateList.length-1){
						oldNetVal = item.netValue;
					}
				})
				yVarMaxlti=numMulti(yVarMaxlti,1.2)
				var min = (numDiv(yValMinlti,5)*4).toFixed(4);
				var max=yVarMaxlti;

				var dispartity=max-min;
				var first=numAdd(min,(numDiv(dispartity,4)*1).toFixed(4));
				var second=numAdd(min,(numDiv(dispartity,4)*2).toFixed(4));
				var third=numAdd(min,(numDiv(dispartity,4)*3).toFixed(4));
				var yVals = min+ ";" + first  +";"+second +";"+third + ";" + max.toFixed(4);

				if(xVals.substr(xVals.length-1) == ";"){
					xVals = xVals.substr(0,xVals.length-1);
				}
				if(netVals.substr(netVals.length-1) == ";"){
					netVals = netVals.substr(0,netVals.length-1);
				}
				if(fluctuates.substr(fluctuates.length-1) == ";"){
					fluctuates = fluctuates.substr(0,fluctuates.length-1);
				}

				var temp = "";
				if(oldNetVal == null || oldNetVal == "" || oldNetVal==0){
					temp = "<em>--</em>";
				}else{
					temp = numMulti(numDiv((newNetVal-oldNetVal),oldNetVal),100);
					if (temp < 0) {
						temp = "<em class='green'>" + numMulti(numDiv((newNetVal - oldNetVal), oldNetVal), 100).toFixed(2) + "%</em>";
					} else if(temp == 0){
						temp = "<em style='color:#666666'>" + numMulti(numDiv((newNetVal - oldNetVal), oldNetVal), 100).toFixed(2) + "%</em>";
					} else {
						temp = "<em>+" + numMulti(numDiv((newNetVal - oldNetVal), oldNetVal), 100).toFixed(2) + "%</em>";
					}
				}
				if(dateTime != null && dateTime == -1){
					htmls_1 += "最近1个月涨跌幅"+temp;
				}else if(dateTime != null && dateTime == -3){
					htmls_1 += "最近3个月涨跌幅"+temp;
				}else if(dateTime != null && dateTime == -6){
					htmls_1 += "最近6个月涨跌幅"+temp;
				}else if(dateTime != null && dateTime == -12){
					htmls_1 += "最近1年涨跌幅"+temp;
				}
				$("#profitByDate").html(htmls_1);

				showHighchart1(xVals,yVals,netVals,fluctuates,stepNum);
			}else{
				if(dateTime != null && dateTime == -1){
					htmls_1 += "最近1个月涨跌幅<em>--</em>";
				}else if(dateTime != null && dateTime == -3){
					htmls_1 += "最近3个月涨跌幅<em>--</em>";
				}else if(dateTime != null && dateTime == -6){
					htmls_1 += "最近6个月涨跌幅<em>--</em>";
				}else if(dateTime != null && dateTime == -12){
					htmls_1 += "最近1年涨跌幅<em>--</em>";
				}
				$("#profitByDate").html(htmls_1);
				showHighchart1("0","0","0","0",stepNum);
			}
		}
	})
}
/* 查询产品净值*/
function queryEstimateByFundIdByPage(pages){
	var fundId = $("#fundId").val();
	var pageInput = $("#page").val();
	var page = returnPage(pages);

	var isSubPrdAppraisement = $("#isSubPrdAppraisement").val();
    //var outerId = $("#outerId").val();
    //子产品非估值 查询时为 系列产品净值图  产品净值不再对产品进行估值
    // if("n" == isSubPrdAppraisement.toLowerCase()){
    // 	fundId = outerId;
    // }

	$.ajax({
		async:true,
		url : "/AppService/setUp/queryCompanyUserFundNetValue.xhtml",
		data : {
			fundId:fundId,
			page:page
		},
		dataType : "json",
        type:"POST",
		cache : false,
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data){
			var htmls = "";
			var maxPages = data.maxPages;
			var page = data.page;
			$("#maxPages").val(maxPages);
			$("#page").val(page);

			var list = data.productEstimates;
			if(list != null && list.length > 0){
				htmls += "<div id='tableContent'>";
				htmls += "<div class='highchart01-data'>";
				htmls += "<table width='880' border='1'>";
				htmls += "<tr>";
				htmls += "<th height='30' align='center' bgcolor='#fafafa' scope='col'>日期</th>";
				htmls += "<th align='center' bgcolor='#fafafa' scope='col'>单位净值</th>";
				htmls += "<th align='center' bgcolor='#fafafa' scope='col'>累计净值</th>";
				htmls += "<th align='center' bgcolor='#fafafa' scope='col'>区间涨幅</th>";
				htmls += "</tr>";

				$.each(list,function(i, item){
					htmls += "<tr>";
					htmls += "<td height='30' align='center'>"+formatDate(item.eDate)+"</td>";
					htmls += "<td align='center'>"+parseFloat(item.netValue).toFixed(4)+"</td>";
					htmls += "<td align='center'>"+parseFloat(item.accNetValue).toFixed(4)+"</td>";
					if(parseFloat(item.fluctuate) > 0){
						htmls += "<td align='center' style='color: #ca132c;'>+" + parseFloat(numMulti(item.fluctuate, 100)).toFixed(2) + "%</td>";
					}else if(parseFloat(item.fluctuate) < 0){
						htmls += "<td align='center' style='color: #57BA4D;'>" + parseFloat(numMulti(item.fluctuate, 100)).toFixed(2) + "%</td>";
					}else{
						htmls += "<td align='center'>" + parseFloat(numMulti(item.fluctuate, 100)).toFixed(2) + "%</td>";
					}
					htmls += "</tr>";
				})

				htmls += "</table>";
				htmls += "</div>";

				htmls += "<div class='nav-href estimate'>";
				htmls += "<a href='javascript:queryEstimateByFundIdByPage(\""+(page-1)+"\")' class='pre'></a>";
				if(page == 1){
					htmls += "<a class='act' href='javascript:queryEstimateByFundIdByPage(\"1\")'>1</a>";
				}else{
					htmls += "<a href='javascript:queryEstimateByFundIdByPage(\"1\")'>1</a>";
				}
				if(page > 3){/* 左边加...*/
					htmls += "<a class='more'>...</a>";
				}

				if((page-1) >1){/* 上一页*/
					htmls += "<a href='javascript:queryEstimateByFundIdByPage(\""+(page-1)+"\")'>"+(page-1)+"</a>";
				}

				if(page != 1 && page != maxPages){/* 当前页*/
					htmls += "<a class='act' href='javascript:queryEstimateByFundIdByPage(\""+(page)+"\")'>"+(page)+"</a>";
				}

				if(page+1 < maxPages){/*下一页*/
					htmls += "<a href='javascript:queryEstimateByFundIdByPage(\""+(page+1)+"\")'>"+(page+1)+"</a>";
				}

				if((maxPages - page) > 2){/* 右边加...*/
					htmls += "<a class='more'>...</a>";
				}
				if(page != 1){
					if(maxPages == page){
						htmls += "<a class='act' href='javascript:queryEstimateByFundIdByPage(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}else if(maxPages > page){
						htmls += "<a href='javascript:queryEstimateByFundIdByPage(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}
				}else{
					if(maxPages == page){

					}else if(maxPages > page){
						htmls += "<a href='javascript:queryEstimateByFundIdByPage(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}
				}
				htmls += "<a href='javascript:queryEstimateByFundIdByPage(\""+(page+1)+"\")' class='next'></a>";
				htmls += "</div>";
				htmls += "</div>";
				$("#tableContent").remove();
				$("#netvalContent").append(htmls);
                $("#netval").show();
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
/* 最近1个月*/
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

    /* 将查询出来的要展示在tooltip位置的值转化为json格式的字符串*/
    str += "[";
    for (var i = 0; i < netArray.length && i < fluctuateArray.length; i++) {
        str += '{"y":' + netArray[i] + ',"fluctuate":' + fluctuateArray[i] + '},';
    }
    str += "]";
    str = str.substr(0, str.lastIndexOf(",")) + str.substr(str.lastIndexOf(",") + 1);

    /* 将json格式字符串转化为json数组*/
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
            gridLineDashStyle: "Dot", /* 竖网格线样式*/
            gridLineColor: "#ccc", /* 竖网格线颜色*/
            gridLineWidth: 0,
            labels: {
                step: stepNum, /* 间隔步长*/
                staggerLines: 1, /* 显示x轴的行数*/
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
/*4个时间节点点击显示事件*/
function show_fundInfo(_id) {
    $("#fundInfo_tips").show();
    $("#" + _id).siblings().hide();
    $("#" + _id).show();
}
/*4个时间节点点击隐藏事件*/
function close_fundInfo(_id) {
    $("#" + _id).hide();
    $("#" + _id).children().hide();
}

/*查询电子合同 TXT*/
function queryEcontrantByFundIdN() {
    var fundid = $("#fundId").val();
    var money = $("#money").val();
    var period = $("#period").val()
    $.ajax({
        async: false,
        url: "/AppService/setUp/queryCompanyUserFundEcontract.xhtml",
        data: {
            "fundId": fundid,
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
    var link = "/company/companyContract.shtml?period=" + period + "&fundId=" + fundid + "&money=" + m;
    window.open(link);
}

function downText(prjLName) {
    $('#text1').html("注:该净值为《" + prjLName + "》 份额净值");
    $('#text1').show();
    $('#highchart1').css('margin', '0 auto 4px');
}

function showText(prjLName) {
    $('#text1').html("注:该净值为《" + prjLName + "》 份额净值");
    $('#text1').show();
    $('#highchart1').css('margin', '0 auto 4px');
}

function checkCompanyUser(){
    $.ajax({
        async:false,
        url: "/AppService/setUp/checkCompanyUser.xhtml",
        dataType: "json",
        type:"POST",
        data: {},
        cache: false,
        error : function(textStatus, errorThrown) {
            show_tips("网络繁忙，请稍后再试。");
        },
        success : function (data) {
            if(data.returnCode != null && data.returnCode == "0000"){
                $("#loginYesPublic").show();
                $("#login_userinfo_public").html(data.fundAccoEncry).prop("href","/company/companyAccountInfo.shtml");
                $("#loginYes").hide();
                $("#loginNo").hide();
                $(".My-center").attr("href","/company/companyAccountInfo.shtml");
            }else{
                goToURL("/login/login.shtml");
            }
        }
    });
}
/* 退出登录 */
function checkOutLoginForCompany(){
    $.ajax({
        async : true,
        url : "/AppService/setUp/logoutForCompany.xhtml",
        data : "",
        dataType : "json",
        cache : false,
        type : "post",
        error : function(textStatus, errorThrown) {
        },
        success : function(data) {
            window.location.href = "/login/login.shtml";
        }
    });
};
//查询并显示净值

function queryEstimate(){
    queryEstimateByFundId("-1");
    queryEstimateByFundIdByPage("1");
}
