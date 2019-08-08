var PRIMARY_PATH = "";
var BASE_PATH = "";
var queryValidate = null;
function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	$("#accountType").change();
	loadData();
	validateForm();
	addFromValidate();
});

/**
 * 选择查询类型
 * @param accountType
 */
function setType(accountType){
	if(!!queryValidate){
		queryValidate.resetForm();
	}
	if(accountType == 'TRADEACCO'){		//交易账号查询
		$(".changeText").val("");
		$("#fundacco").attr("disabled",true);
		$("#tradeacco").attr("disabled",false);
	} else if(accountType == 'FUNDACCO'){	//基金账号查询
		$(".changeText").val("");
		$("#fundacco").attr("disabled",false);
		$("#tradeacco").attr("disabled",true);
	}
}

function clearQueryFlag(){
	$("#queryflag").val("N");
	$("#subQuty").val(null);
	onMoneyChange();
}
/**
 * 加载页面所需数据
 * @param fundId
 */
function loadData(){
	$.ajax({
		url: BASE_PATH +'capitalService/server/loadInitData.xhtml',   
		dataType: "json",
		type: "POST",
		cache: false,
		async: true,
		success: function(data) {
			var trustTypeArray = data.trustTypeArray;
			var seatList = data.seatList;
			var trustTypeHtml = "";
			var seatHtml = "";
			seatHtml+="<option value='--'>- -</option>";
			for (var j = 0; j < trustTypeArray.length; j++) {
				var trustType = trustTypeArray[j];
				trustTypeHtml+="<option value="+trustType.pmco+">"+trustType.pmco+"　"+trustType.pmnm+"</option>";
			}
			for (var m = 0; m < seatList.length; m++) {
				var seat = seatList[m];
				seatHtml+="<option value="+seat.seatno+">"+seat.seatno+"　"+seat.seatnm+"</option>";
			}
			
			$("#trustType").html(trustTypeHtml);
			$("#trustType").val("3");
			$("#seatno").html(seatHtml);
			$('.select2').select2({allowClear: false,minimumResultsForSearch:Infinity});
		}
	});
}

/**
 * 选择对方销售商带出对方网点
 */
function checkTano(){
    var checkSeatno = $("#seatno").val();
    if(checkSeatno != ""){
       $("#netpoint").val(checkSeatno);
    }else{
    	$("#netpoint").val("");
    }
}

/**
 * 
 * @param fundid
 */
function queryFundInfoData(fundid){
	$("#subQuty").val("");
	var fundid = fundid;
	var contactQry = $("#contact").val();
	var tradeacco = $("#tradeacco").val();
	onMoneyChange();
	if(!(fundid == '') && !(tradeacco == '')){
		//$("#queryflag").val("Y");
		$("#queryFundInfo").val("Y");
	}
	queryAvailable();
}

function onMoneyChange(){
	var subQuty = $("#subQuty").val();
	var subQutyFmt = qianfenwei(subQuty);
	$("#CapMoneyqianfenwei").html(subQutyFmt);
	$("#CapMoney").html(capMoneyNoCheck($("#subQuty").val(),"subQuty"));
}


function qianfenwei(objvalue){
	if(objvalue == ""){
		return "";
    }
	if ( !objvalue.match(/^(\d{1,}(|(\.{1}\d{0,2})))$|^(\.{1}\d{1,2})$/) )
	{
		return ""; 
	} else if(objvalue.length>0 && objvalue.substr(0,1) == 0){		//第一位为0
		return ""; 
	}
	var num = new Number(objvalue);
	num = num.toFixed(2);
	var re=/(\d{1,3})(?=(\d{3})+(?:$|\.))/g;//转换为千分位
	return num.replace(re,"$1,");
}

function capMoneyNoCheck(xxje,amtNM) {

	var low	;				
	var i,k,j, l_xx1; 			
	var cap = "", dxnr, xx1, unit, lastDigit = "", endUnit ="", lastUnit = "" ; 	
	
	var digits = "零壹贰叁肆伍陆柒捌玖"; 
	var units = "分角元拾佰仟万拾佰仟亿拾佰仟"; 
		
	if(xxje == ""){
		return "";
	}
	if ( !xxje.match(/^(\d{1,}(|(\.{1}\d{0,2})))$|^(\.{1}\d{1,2})$/) )
	{
		return "<font color=red>您输入的金额格式不正确！</font>"; 
	} else if(xxje.length>0 && xxje.substr(0,1) == 0){		//第一位为0
		return "<font color=red>您输入的金额格式不正确！</font>"; 
	}
	low = parseFloat(xxje);	
	//if (isNaN(low)) return "输入无效"; 

	xx1 = Math.round(low * 100.0) + "" ;
	l_xx1 = xx1.length; 
	
	for (i=0; i<l_xx1; i++) { 
		j = l_xx1 -1 - i; 
		unit = units.substr(j, 1); 			
		k = parseInt(xx1.substr(i, 1)); 
		digit = digits.substr(k, 1);			
		cap = cap + digit + unit;
	}	
	
	cap = cap.replace( /零分|零角|零拾|零佰|零仟/g, "零");
	cap = cap.replace( /零+/g, "零");
	cap = cap.replace( /零亿/g, "亿");
	cap = cap.replace( /零万/g, "万");
	cap = cap.replace( /零元/g, "元");
	cap = cap.replace( /亿万/g, "亿");
	cap = cap.replace( /^壹拾/, "拾");
	cap = cap.replace( /零$/, "整");
	
	if (cap == "整") cap = "零元整";
	
	cap="<font color=red>"+ cap +"</font>";
	return cap; 
}

function validateForm(){
	// 在键盘按下并释放及提交后验证提交表单
	queryValidate = $("#switchInQuery").validate({
		rules: {
			tradeacco: {
				checkTradeacco: true
			},
			fundacco: {
				checkFundacco: true
			}
		},
		messages: {
			tradeacco: {
				checkTradeacco: "交易账号不能为空！"
			},
			fundacco: {
				checkFundacco: "基金账号不能为空！"
			}
		}
	});
};

function addFromValidate(){
	// 在键盘按下并释放及提交后验证提交表单
	$("#switchIn").validate({
	    rules: {
     	    checkno: {
     	    	checkNo: true
     	    },
     	    checkpwd: {
     	    	checkPwd: true
     	    },
     	    fundid: {
     	    	checkFundId: true
     	    },
     	    oldserialno:{
    	    	checkOldserialno: true,
    	    	checkOldserialnoNumber:true
    	    	
    	    },
     	    seatno:{
    	    	checkSeatno: true
     	    },
     	    switchInShare: {
     	    	requiredSubQuty : true,
    	    	checkSubQuty: true,
    	    	checkSubQutySize: true
    	    },
    	    contact: {
    	    	checkContact: true
     	    }
	    },
	    messages: {
	        checkno: {
	        	checkNo: "主管工号：必须填写！"
	        },
	        checkpwd: {
	        	checkPwd: "主管密码：必须填写！"
	        },
	        fundid: {
	        	checkFundId: "基金名称：必须填写！"
	        },
	        oldserialno:{
     	    	checkOldserialno: "申请编号：必须填写！",
     	    	checkOldserialnoNumber : "申请编号格式不正确！"
     	    },
     	    seatno:{
    	    	checkSeatno: "对方销售商：必须填写！"
     	    },
     	    switchInShare: {
	        	requiredSubQuty : "转入份额：必须填写！",
	        	checkSubQuty: "",
	        	checkSubQutySize: "您填写的份额大于可用份额！"
	        },
    	    contact: {
     	    	checkContact: "请选择经办人！"
     	    }
	    }
	});
};


/**
 *  验证交易账号是否为空
 */
jQuery.validator.methods["checkTradeacco"]=(function(value,element){
	var accountType = $("#accountType").val();
	var tradeacco = $("#tradeacco").val();
	var success=false;
	if(accountType == 'TRADEACCO' && tradeacco == ''){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证基金账号是否为空
 */
jQuery.validator.methods["checkFundacco"]=(function(value,element){
	var accountType = $("#accountType").val();
	var fundacco = $("#fundacco").val();
	var success=false;
	if(accountType == 'FUNDACCO' && fundacco == ''){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证主管工号是否为空
 */
jQuery.validator.methods["checkNo"]=(function(value,element){
	var success=false;
	var checkno = $("#checkno").val();
	var queryflag = $("#queryflag").val();
	if(isAudit == 'Y' && checkno == "" && queryflag == "Y"){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证主管密码不能为空
 */
jQuery.validator.methods["checkPwd"]=(function(value,element){
	var success=false;
	var checkpwd = $("#checkpwd").val();
	var queryflag = $("#queryflag").val();
	if(isAudit == 'Y' && checkpwd == "" && queryflag == "Y"){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证基金名称是否为空
 */
jQuery.validator.methods["checkFundId"]=(function(value,element){
	var fundId = $("#fundid").val();
	var success=false;
	var queryflag = $("#queryflag").val();
	if((fundId == "--" || fundId == null) && queryflag == "Y"){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证申请编号是否为空
 */
jQuery.validator.methods["checkOldserialno"]=(function(value,element){
	var oldserialno = $("#oldserialno").val();
	var success=false;
	var queryflag = $("#queryflag").val();
	if((oldserialno == "" || oldserialno == null) && queryflag == "Y"){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证申请编号 是否合法
 */
jQuery.validator.methods["checkOldserialnoNumber"]=(function(value,element){
	var success=false;
	var queryflag = $("#queryflag").val();
	if(!(value).match(/^\w+$/) && value != "" && queryflag == "Y"){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证对方销售商是否为空
 */
jQuery.validator.methods["checkSeatno"]=(function(value,element){
	var seatno = $("#seatno").val();
	var success=false;
	var queryflag = $("#queryflag").val();
	if((seatno == "--" || seatno == null) && queryflag == "Y"){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证转入份额是否合法
 */
jQuery.validator.methods["requiredSubQuty"]=(function(value,element){
	var subQuty = $("#subQuty").val();
	var success=false;
	var queryflag = $("#queryflag").val();
	if(subQuty == "" && queryflag == "Y"){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证转入份额是否合法
 */
jQuery.validator.methods["checkSubQuty"]=(function(value,element){
	var subQuty = $("#subQuty").val();
	var success=false;
	var queryflag = $("#queryflag").val();
	if(!(subQuty).match(/^(\d{1,}(|(\.{1}\d{0,2})))$|^(\.{1}\d{1,2})$/) && subQuty != "" && queryflag == "Y"){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证转入份额是否合法
 */
jQuery.validator.methods["checkSubQutySize"]=(function(value,element){
	var subQuty = $("#subQuty").val();
	var available = $(".available").val();
	var success=false;
	var queryflag = $("#queryflag").val();
	if(subQuty != ""  && queryflag == "Y"){
		if(Number(subQuty) > Number(available)){   //2010-02-09
			return false;		
		}else{
			return true;
		}
	}else{
		success= true;
	}
	return success;
});

/**
 *  验证经办人是否为空
 */
jQuery.validator.methods["checkContact"]=(function(value,element){
	var contact = $("#contact").val();
	var invtpNm = $(".invtpNm").val();
	var success=false;
	var queryflag = $("#queryflag").val();
	if(invtpNm == '0' && (contact == '' || contact == null) && queryflag == "Y"){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 * 查询按钮查询用户信息数据
 */
function queryClientInfo(){
	if($("#switchInQuery").valid()){
		$(".invnm").html("");
		$(".invtp").html("");
		$(".idtpNm").html("");
		$(".idno").html("");
		$(".bankAcco").html("");
		$(".openName").html("");
		$(".custrisklevl").val();
		$(".invprtpString").html("");
		$(".invprStyle").hide();
		$(".custno").val("");
		$(".hidtradeacco").val("");
		$(".custname").val("");
		$(".hidfundacco").val("");
		$(".inputTd").remove();
		//基金名称
		$(".fundName").html("");
		//可用份额
		$(".avaliableDis").html("");
		$(".fundBanlanceHtml").remove();
		
		
		var tradeacco = $("#tradeacco").val();
		var fundacco = $("#fundacco").val();
		//提交表单
		$.ajax({
			url: BASE_PATH +'capitalService/server/tradeaccoQryBySimp.xhtml',   
			dataType: "json",
			type: "POST",
			data:{
				'tradeacco' : tradeacco,
				'fundacco' : fundacco
			},
			cache: false,
			async: true,
			success: function(data) {
				$("#queryflag").val("Y");
				var date = new Date();//获取当前时间 
				var time = date.Format("yyyyMMdd");
				if(data.errcode == "0000"){
					$(".invnm").html(data.invnm);
					$(".invtp").html(data.invtp);
					$(".invtpNm").val(data.invtpNm);
					var invprtp = data.invprtp;
					if(invprtp==null || invprtp == ""){
						$(".invprStyle").hide();
					}else{
						if(invprtp == "1"){
							$(".invprStyle").show();
							$(".invprtpString").html("普通投资者");
						}else{
							$(".invprStyle").hide();
							$(".invprtpString").html("专业投资者");;
						}
					};
					$(".invprtp").val(invprtp);
					$(".idtpNm").html(data.idtp);
					$(".idno").html(data.idno);
					$(".bankAcco").html(data.bankAcco);
					$(".openName").html(data.openName);
					
					$(".custno").val(data.custno);
					$(".hidtradeacco").val(data.tradeacco);
					
					$(".custname").val(data.invnm);
					$(".hidfundacco").val(data.fundacco);

					
					var idvalidate = data.idvalidate;
					if(idvalidate == null || idvalidate.length == 0){
						idvalidate = "20991231";
					}
					var instrepvalidate = data.instrepvalidate;
					if(instrepvalidate == null || instrepvalidate.length == 0){
						instrepvalidate = "20991231";
					}
					var custrisklevl = data.custrisklevl;
					if(custrisklevl == null || custrisklevl.length == 0){
						custrisklevl = "";
					}
					$(".custrisklevl").val(custrisklevl);
					var custriskdate = data.custriskdate;
					var limitdate = "";
					var syear = "";
					var iyear = 0;
					if(custriskdate != null && custriskdate.length > 0)
					{
						syear = custriskdate.substring(0,4);
							
						iyear = parseInt(syear)+3;
						limitdate = iyear+custriskdate.substring(4);
						
						if(limitdate < time)
						{
							toastr.warning('', '温馨提示：客户风险评估已经过期！');
						}
					}else{
						toastr.warning('', '温馨提示：该客户没有最新风险评估数据！');
					}
					
					var contList = data.contList;
					var contHtml = "";
					/*contHtml+="<option value='--'>请选择</option>";*/
					for (var j = 0; j < contList.length; j++) {
						var temp = contList[j];
						if(!temp){
							continue;
						}
						contHtml+="<option value="+temp.contact+","+temp.contidno+","+temp.contidtpName+","+temp.contvalidate+">"+temp.contact+"</option>";
					}
					$("#contact").html(contHtml);
					$('#contact').select2({allowClear: false,minimumResultsForSearch:Infinity});
					$('#contact').change();
					var tano = data.tano;
					$("#tano").val(tano);
					
					var fundBalanceList = data.fundbalanceList;
					var fundBalanceHtml = "";
					fundBalanceHtml+="<option value='--'>- -</option>";
					for (var l = 0; l < fundBalanceList.length; l++) {
						var temp = fundBalanceList[l];
						fundBalanceHtml+="<option value="+temp.fundid+">"+temp.fundid+"　"+temp.fundnm+"</option>";
					}
					$("#fundid").html(fundBalanceHtml);
					$('#fundid').select2();
					
					if(idvalidate < time){
						toastr.warning('', '开户证件已过期！');
						$("#queryflag").val("N");
					}else{
						//toastr.warning('', '开户证件将于'+idvalidate+'日过期！');
					}
					
					if(instrepvalidate < time){
						toastr.warning('', '法人证件已过期！');
						$("#queryflag").val("N");
					}else{
						//toastr.warning('', '法人证件将于'+idvalidate+'日过期！');
					}
					
					var appendHtml = "";
					for (var k = 0; k < contList.length; k++) {
						var temp = contList[k];
						if(!temp){
							continue;
						}
						temp.contact = temp.contact == null?'':temp.contact;
						temp.contidtp = temp.contidtp == null?'':temp.contidtp;
						temp.contidno = temp.contidno == null?'':temp.contidno;
						temp.contphone = temp.contphone == null?'':temp.contphone;
						temp.contfax = temp.contfax == null?'':temp.contfax;
						appendHtml+='<tr class="inputTd">';
						appendHtml+='<td>经办人：</td>';
						appendHtml+='<td class="white-bg form-inner" colspan="3">';
						appendHtml+='<span class="">'+temp.contact+'</span>';
						appendHtml+='</td>';
						appendHtml+='</tr>';
						appendHtml+='<tr class="inputTd">';
						appendHtml+='<td>经办人证件：</td>';
						appendHtml+='<td class="white-bg form-inner">';
						appendHtml+='<span class="">'+temp.contidtp+'</span>';
						appendHtml+='</td>';
						appendHtml+='<td>经办人号码：</td>';
						appendHtml+='<td class="white-bg form-inner">';
						appendHtml+='<span class="">'+temp.contidno+'</span>';
						appendHtml+='</td>';
						appendHtml+='</tr>';
						appendHtml+='<tr class="inputTd">';
						appendHtml+='<td>经办人电话：</td>';
						appendHtml+='<td class="white-bg form-inner">';
						appendHtml+='<span class="">'+temp.contphone+'</span>';
						appendHtml+='</td>';
						appendHtml+='<td>经办人传真：</td>';
						appendHtml+='<td class="white-bg form-inner">';
						appendHtml+='<span class="">'+temp.contfax+'</span>';
						appendHtml+='</td>';
						appendHtml+='</tr>';
					}
					$("#checknoTr").before(appendHtml);
					$("#fundid").change();
				}else{
					$("#queryflag").val("N");
					ctools.alert_sweet('查询失败！', "error", data.errmsg);
				}
			}
		});
	}
}

function contset(contact){
	if(contact != ''){
		var contactvalue = contact.split(",");
		$("input[name=hidcontact]").val(contactvalue[0]);
		$("input[name=hidcontidno]").val(contactvalue[1]);
		$("input[name=hidcontidtp]").val(contactvalue[2]);
		$("input[name=hidcontvalidate]").val(contactvalue[3]);
	} else {
		$("input[name=hidcontact]").val("");
		$("input[name=hidcontidno]").val("");
		$("input[name=hidcontidtp]").val("");
		$("input[name=hidcontvalidate]").val("");
	}
}


/**
 * 查询当前基金可用份额
 */
function queryAvailable(){
	var custno = $(".custno").val();
	var tradeacco = $("#tradeacco").val();
	var fundacco = $("#fundacco").val();
	var fundid = $("#fundid").val();
	$(".fundBanlanceHtml").remove();
	$.ajax({
		type : "POST",
		async: false, 
		dataType : "json",
		url : BASE_PATH +'capitalService/server/queryAvailable.xhtml',
		data:{
			'custno' : custno,
			'tradeacco' : tradeacco,
			'fundacco' : fundacco,
			'fundidForQry': fundid
		},
		success : function(data) {
			if(data.errcode == "0000"){
				$(".available").val(data.availableBalance);
				//基金名称
				$(".fundName").html($("#fundid").find("option:selected").text());
				//可用份额
				$(".avaliableDis").html(data.availableBalance);
			}
		}
	});
}

/**
 * 改变下拉框验证样式
 */
function changeStyle($this){
	var id = $($this).attr("id");
	$(".select2").removeClass("error");
	$(".select2-selection").removeClass("error");
	$(".select2-selection__rendered").removeClass("error");
	$(".select2-selection__arrow").removeClass("error");
	$("label[for="+id+"]").remove();
}

/**
 * 提交申购数据
 */
function checkSubmit(){
	var queryflag = $("#queryflag").val();
	var flag = true;
	if(queryflag != 'Y'){
		toastr.warning('', '请先查询！');
		return false;
	}
	if($("#switchIn").valid()){
		var checkno = $("#checkno").val(); 
		var checkpwd = $("#checkpwd").val();
		if(isAudit == 'Y'){
			$.ajax({
				type : "POST",
				async: false, 
				dataType : "json",
				url : BASE_PATH +'capitalService/server/checkPermission.xhtml',
				data:{
					'checkno':checkno,
					'checkpwd':checkpwd
				},
				success : function(data) {
					if(data.result == "0"){
						ctools.alert_sweet('授权失败：主管工号或密码错误！', "error", "");
						flag = false;
						return false;
					}
				}
			});
		}
		if(!flag){
			return;
		}
		doSubmit();
	}
}

/**
 * 提交申购数据
 * @returns {Boolean}
 */
function doSubmit(){
	var trustType = $("#trustType").val();
	$(".trustType").val(trustType);
	var custname = $(".custname").val();
	var tradeAcco = $(".hidtradeacco").val();
	var fundid = $("#fundid").val();
	var subQuty = $("#subQuty").val();
	var fundacco = $(".hidfundacco").val();
	var title = "是否确认提交该笔申请？";
	ctools.confirm({text:title},function(isConfirm){
		if(isConfirm){
			$.ajax({
				type : "POST",
				async: false, 
				dataType : "json",
				data: $('#switchIn').serialize(),// 你的formid
				url : BASE_PATH +'capitalService/server/submitRollOut.xhtml',
				success : function(data) {
					if(data.errCode == "0000"){
						ctools.alert_sweet('申请提交成功！', "success", "申请编号："+data.serialno , function(){
							window.location.reload();
						});
					}else{
						ctools.alert_sweet('申请提交失败！', "error", "申请编号："+data.serialno+"\n失败原因："+data.errMsg);
					}
				}
			});
		}
	});
}