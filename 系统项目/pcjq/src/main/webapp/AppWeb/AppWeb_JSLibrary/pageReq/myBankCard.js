$(document).ready(function(){
	document.title = "我的银行卡_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	getUserRequest("pc_applicationGroups_myBankCardNo");
	queryUserInfo();
	queryMyBankCard();
})
/* 我的银行卡信息 */
function queryMyBankCard() {
	$.ajax({
		async : false,
		url : "/AppService/business/queryMyBankCardNo.xhtml",
		data : {},
		dataType : "json",
		cache : false,
		type : "POST",
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			var htmls = "";
			var className = "";
			var bankName = "";
			var realbankno = "";
			var payMethod ="";
			var bankStatus = "";
			if (data.returnCode == '0000') {
				var payMode = "";
				var bankNo = "";
				var protoNo = "";
				var clickMethed = "";
				$.each(data.tradeAcctlist,function(i, item) {
					/* 银行卡信息展示 */
					realbankno = item.realBankNo;
					if (realbankno == '007') {/* 招商银行 */
						className = 'n1';
					} else if (realbankno == '003') {/* 农业银行 */
						className = 'n2';
					} else if (realbankno == '005') {/* 建设银行 */
						className = 'n3';
					} else if (realbankno == '008') {/* 中信银行 */
						className = 'n4';
					} else if (realbankno == '004') {/* 中国银行 */
						className = 'n5';
					} else if (realbankno == '012') {/* 光大银行 */
						className = 'n6';
					} else if (realbankno == '006') {/* 交通银行 */
						className = 'n7';
					} else if (realbankno == '002') {/* 工商银行 */
						className = 'n8';
					} else if (realbankno == '011') {/* 兴业银行 */
						className = 'n9';
					} else if (realbankno == '601') {/* 平安银行 */
						className = 'n10';
					} else if (realbankno == '015') {/* 邮储银行 */
						className = 'n11';
					} else if (realbankno == '009') {/* 浦发银行 */
						className = 'n12';
					} else if (realbankno == '060') {/* 广发银行 */
						className = 'n13';
					} else if (realbankno == '014') {/* 民生银行 */
						className = 'n14';
					} else if (realbankno == '017') {/* 华夏银行 */
						className = 'n15';
					} else if (realbankno == '032') {/* 天津银行 */
						className = 'n17';
					} else {/* 默认的银行卡样式 */
						className = 'n1';
					}
					/*if(data.payMode=='001'){
						payMethod='线上B2B';
						bankStatus = "dredged";
					}else if(data.payMode=='010'){
						payMethod='线上B2C';
						bankStatus = "dredge";
					}else{
						payMethod='暂不支持线上支付';
						bankStatus = "redredge";
					}*/
					payMode = item.payMode;
					bankNo = item.bankNo;
					protoNo = item.protoNo;
					debugger;
					if(bankNo != null && bankNo == "002"){
						
						if(protoNo != null && typeof(protoNo) != "undefined" && protoNo != "" && $.trim(protoNo).length > 0){
							
							if(payMode != null && payMode == "7"){/* 线下汇款  线上B2C 线上B2B*/
								payMethod='已开通线上支付';
								bankStatus = "dredged";
							}else if(payMode != null && payMode == "6"){/* 线下汇款  线上B2C*/ 
								payMethod='已开通线上支付';
								bankStatus = "dredged";
							}else if(payMode != null && payMode == "5"){/* 线下汇款  线上B2B*/
								payMethod='已开通线上支付';
								bankStatus = "dredged";
							}else if(payMode != null && payMode == "4"){/* 线下汇款*/
								payMethod='暂不支持线上支付';
								bankStatus = "redredge";
							}else if(payMode != null && payMode == "3"){/* 线上B2C 线上B2B*/
								payMethod='已开通线上支付';
								bankStatus = "dredged";
							}else if(payMode != null && payMode == "2"){/* 线上B2C*/
								payMethod='已开通线上支付';
								bankStatus = "dredged";
							}else if(payMode != null && payMode == "1"){/* 线上B2B*/
								payMethod='已开通线上支付';
								bankStatus = "dredged";
							}else{/* 默认支持线下汇款 */
								payMethod='暂不支持线上支付';
								bankStatus = "redredge";
							}
						}else{
//							payMethod='暂不支持线上支付';
							payMethod='未开通线上支付';
							bankStatus = "redredge";
						}
						
					}else{
//						payMethod='暂不支持线上支付';
//						bankStatus = "redredge";
						if(payMode != null && payMode == "7"){/* 线下汇款  线上B2C 线上B2B*/
							payMethod='已开通线上支付';
							bankStatus = "dredged";
						}else if(payMode != null && payMode == "6"){/* 线下汇款  线上B2C*/ 
							payMethod='已开通线上支付';
							bankStatus = "dredged";
						}else if(payMode != null && payMode == "5"){/* 线下汇款  线上B2B*/
							payMethod='已开通线上支付';
							bankStatus = "dredged";
						}else if(payMode != null && payMode == "4"){/* 线下汇款*/
							payMethod='暂不支持线上支付';
							bankStatus = "redredge";
						}else if(payMode != null && payMode == "3"){/* 线上B2C 线上B2B*/
							payMethod='已开通线上支付';
							bankStatus = "dredged";
						}else if(payMode != null && payMode == "2"){/* 线上B2C*/
							payMethod='已开通线上支付';
							bankStatus = "dredged";
						}else if(payMode != null && payMode == "1"){/* 线上B2B*/
							payMethod='已开通线上支付';
							bankStatus = "dredged";
						}else{/* 默认支持线下汇款 */
							payMethod='暂不支持线上支付';
							bankStatus = "redredge";
						}
						
					}
					if(bankNo != null && bankNo == "002" && (protoNo == null || typeof(protoNo) == "undefined" || protoNo == "" && $.trim(protoNo).length == 0)){
						clickMethed = "onclick='contractSign_tips(\"card_div_"+i+"\")'";
					}
					/*data-tradeAcct:交易账号；data-channelNo:渠道号；data-bankNo:银行号*/
					htmls += "<div id='card_div_"+i+"' class='mybank-bg "+className+"' data-tradeacct='"+item.tradeAcco+"' data-bankno='"+item.realBankNo+"' data-channelno='"+item.bankNo+"'>";
					htmls += "<h3 class='name'>"+item.bankNm+"</h3>";
					htmls += "<span class='bankNo'>"+replaceStr2(item.bankAccoDisplay,4, 4)+"</span> ";
					htmls += "<span class='bt-delete'><a href='#' onclick='showCanelBindDiv(\""+ item.tradeAcco+ "\")'>删除卡片</a><span "+clickMethed+" class='point "+bankStatus+"'>"+payMethod+"</span>";
					htmls += "</span> <span class='watermark'></span>";
					htmls += "</div>";
					clickMethed = "";
				});
				$(".mybank-tab-con").html(htmls);
			} else {
				$("#addBankCard").attr("href","javascript:show_tips('网络繁忙，请稍后再试')");
			}
		}
	});
}
function showCanelBindDiv(tradeAcco) {
	$("#deleteBankCard").show();
	$("#deleteBank").attr("onclick","cancelBindBankCard('" + tradeAcco + "');");
}

function closeCanelBindDiv(){
	$("#deleteBankCard").hide();
}
/* 解除绑定银行卡 */
function cancelBindBankCard(tradeAcco) {
	closeCanelBindDiv();
	$.ajax({
		async : false,
		url : "/AppService/business/canalBindBankCard.xhtml",
		data : {
			"tradeAcco" : tradeAcco
		},
		dataType : "json",
		type : "POST",
		cache : false,
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				show_tips("操作成功！");
				queryMyBankCard();
			} else if (data.returnCode == "9027") {
				show_tips("开户当天无法取消或更换此银行卡，请您明天再试!");
			} else if (data.returnCode == "9020") {
				show_tips(data.returnMsg);
			} else if (data.returnCode == "9000") {
				show_tips("参数为空！");
			} else if (data.returnCode == "8000") {
				show_tips(data.returnMsg);
			} else if(data.returnCode == "9999"){
				show_tips("网络繁忙，请稍后再试");
			}else{
				show_tips(data.returnMsg);
			}
		}
	});
}

/*查询用户信息  此处主要查询是否为30用户*/
function queryUserInfo() {
	var url = "/AppService/business/queryUserinfo.xhtml";
	$.ajax({
		async : false,
		url : url,
		data : "",
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				/*如果未设置安全码，已经鉴权跳转到设置安全码页面，未鉴权跳转到鉴权页面*/
				if(data.userType!='30'){
					gotoRealName();
				}
			} else if (data.returnCode == "8000") {
				goToURL("/login/login.shtml");
			} else {
				show_tips("网络繁忙，请稍后再试");
			}
		}
	});
}
/*签约*/
function contractSign(_id){
	var tradeAcct = $("#"+_id).attr("data-tradeacct");
	var bankNo = $("#"+_id).attr("data-bankno");/*银行代码*/
	var channelNo = $("#"+_id).attr("data-channelno");/*渠道代码*/
	if(bankNo == null || bankNo != "002" || tradeAcct == null || tradeAcct == "" || channelNo == null || channelNo == ""){
		return;
	}
	$.ajax({
		async : false,
		url : "/AppService/business/contractSign.xhtml",
		data : {
			"tradeAcct" : tradeAcct,
			"bankNo" : bankNo,
			"channelNo" : channelNo
		},
		type : "POST",
		dataType : "json",
		cache : false,
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			var returnCode = data.returnCode;
			var returnMsg = data.returnMsg;
			if(returnCode != null && returnCode == "0000"){
				var dto = data.icbcSignParaDto;
				/*$("#order").attr("ACTION",dto.signOnlineUrl);*/
				
				$("#contractSign_interfaceName").val(dto.interfaceName);
				$("#contractSign_interfaceVersion").val(dto.interfaceVersion);
				$("#contractSign_selserialNo").val(dto.selserialNo);
				$("#contractSign_payNo").val(dto.payNo);
				$("#contractSign_selcorpId").val(dto.selcorpId);
				$("#contractSign_selaccountNo").val(dto.selaccountNo);
				$("#contractSign_regDate").val(dto.regDate);
				$("#contractSign_HSURL").val(dto.HSURL);
				$("#contractSign_merCertID").val(dto.merCertID);
				$("#contractSign_Language").val(dto.language);
				$("#contractSign_certDate").val(dto.certDate);
				$("#contractSign_allowFinalDate").val(dto.allowFinalDate);
				$("#contractSign_accountNo").val(dto.accountNo);
				$("#contractSign_certData").val(dto.certData);
				
				$("#contractSign_order").submit();
				
				$("#contractSign_div_02").hide();
				$("#contractSign_div_03").show();/*查询签约弹窗*/
				$("#tips_btn3_btn").attr("href","javascript:queryCommandByBankAccoNo('"+_id+"');");/*查询签约*/
			}
			
		}
	});
}
/*查询签约*/
function queryCommandByBankAccoNo(_id){
	var tradeAcct = $("#"+_id).attr("data-tradeacct");
	var bankNo = $("#"+_id).attr("data-bankno");/*银行代码*/
	var channelNo = $("#"+_id).attr("data-channelno");/*渠道代码*/
	if(bankNo == null || bankNo != "002" || tradeAcct == null || tradeAcct == "" || channelNo == null || channelNo == ""){
		return;
	}
	$.ajax({
		async : false,
		url : "/AppService/business/queryCommandByBankAccoNo.xhtml",
		data : {
			"tradeAcct" : tradeAcct,
			"bankNo" : bankNo,
			"channelNo" : channelNo
		},
		type : "POST",
		dataType : "json",
		cache : false,
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			var returnCode = data.returnCode;
			var returnMsg = data.returnMsg;

			if(returnCode != null && returnCode == "0000"){
				show_tips("签约成功");
				$("#contractSign_div_03").hide();
				queryMyBankCard();
			}else{
				$("#contractSign_div_02").show();
				$("#contractSign_div_03").hide();
				show_tips(data.returnMsg);
			}
		}
	});
}
 
function contractSign_close(_id){
	$("#"+_id).hide();
	$("#paytypeList #offline-pay-way").click().change().parent().show();
}
function contractSign_tips(_id){
	$("#contractSign_div_02").show();
	$("#tips_btn1_btn").attr("onclick","contractSign('"+_id+"')");
}