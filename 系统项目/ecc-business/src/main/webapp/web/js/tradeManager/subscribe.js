/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";
var subscribeQuery = null;
function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	$("#accountType").change();
	loadData();
	getAllSubFunds();
	validateForm();
	addFromValidate();
});

/**
 * 是否显示合同是否原件字样
 */
function showdiv(){
	var contractsign = $("#contractsign").val();
	if(contractsign=='1'){
		$(".isoriginal").show();
	}else{
		$(".isoriginal").hide();
	}
}

/**
 * 选择查询类型
 * @param accountType
 */
function setType(accountType){
	if(!!subscribeQuery){
		subscribeQuery.resetForm();
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

/**
 * 加载页面所需数据
 * @param fundId
 */
function loadData(){
	$.ajax({
		url: BASE_PATH+'capitalService/server/loadAccountData.xhtml',
		dataType: "json",
		type: "POST",
		cache: false,
		async: false,
		success: function(data) {
			var trustTypeArray = data.trustTypeArray;
			var invtpArray = data.invtpArray;
			var idtpArray = data.idtpArray;
			var contractSignArray = data.contractSignArray;
			var trustTypeHtml = "";
			var invtpHtml = "";
			var idtpHtml = "";
			var contractSignHtml = "";
			for (var j = 0; j < trustTypeArray.length; j++) {
				var trustType = trustTypeArray[j];
				trustTypeHtml+="<option value="+trustType.pmco+">"+trustType.pmco+"　"+trustType.pmnm+"</option>";
			}
			for (var m = 0; m < contractSignArray.length; m++) {
				var contractSign = contractSignArray[m];
				contractSignHtml+="<option value="+contractSign.pmco+">"+contractSign.pmco+"　"+contractSign.pmnm+"</option>";
			}
			
			$("#trustType").html(trustTypeHtml);
			$("#trustType").val("3");
			$("#contractsign").html(contractSignHtml);
			
			$('.select2').select2({allowClear: false,minimumResultsForSearch:Infinity});
		}
	});
}
	
/**
 * 取认购并且对应该客户TANO的基金信息
 */
function getAllSubFunds(){
	var tano = $("#tano").val();
	$.ajax({
		url: BASE_PATH+'capitalService/server/getAllSubFunds.xhtml',   
		dataType: "json",
		data:{
			"fundst" : "S",
			"tano" : tano
		},
		type: "POST",
		cache: false,
		async: false,
		success: function(data) {
			var fundIdHtml = "";
			/*fundIdHtml+="<option value='--'>--</option>";*/
			for (var j = 0; j < data.length; j++) {
				var temp = data[j];
				fundIdHtml+="<option value="+temp.fundId+">"+temp.fundId+"　"+temp.fundShortNm+"</option>";
			}
			$("#fundid").html(fundIdHtml);
			$('#fundid').select2();
		}
	});
}

/**
 * 
 * @param fundid
 */
function queryFundInfoData(fundid){
	var fundid = fundid;
	var contactQry = $("#contact").val();
	var tradeacco = $("#tradeacco").val();
	$("#subAmt").val(null);
	onMoneyChange();
	if(!(fundid == '') && !(tradeacco == '')){
		//$("#queryflag").val("Y");
		$("#queryFundInfo").val("Y");
	}
}

function clearQueryFlag(){
	$("#queryflag").val("N");
	$("#subAmt").val(null);
	onMoneyChange();
}
//查询折扣率
function queryDiscount(){
	var fundid = $("#fundid").val();//基金ID
	var tradeacco = $("#tradeacco").val();//交易账号
	var fundacco = $("#fundacco").val();//基金账号
	var subAmt = $("#subAmt").val();//认购金额
	if(fundid != "" && subAmt != "" && (tradeacco != "" || fundacco != "")){
		$.ajax({
			url: BASE_PATH+'capitalService/server/findDiscount.xhtml',   
			dataType: "json",
			data:{
				'fundid' : fundid,
				'tradeacco' : tradeacco,
				'fundacco' : fundacco,
				'subAmt' : subAmt,
				"apkind" : "020"
			},
			type: "POST",
			cache: false,
			async: true,
			success: function(data) {
				$("#discountDiv").html(data.disCount);
			}
		});
	}
}

function onMoneyChange(){
	var subAmt = $("#subAmt").val();
	var subAmtFmt = qianfenwei(subAmt);
	$("#CapMoneyqianfenwei").html(subAmtFmt);
	$("#CapMoney").html(capMoneyNoCheck($("#subAmt").val(),"subAmt"));
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
		return "<font color=red>您输入的金额格式不正确</font>"; 
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
	subscribeQuery = $("#subscribeQuery").validate({
		ignore:":disabled",
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
	$("#subscribe").validate({
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
     	    subAmt: {
     	    	required : true,
    	    	checkSubAmt: true,
    	    	checkSubAmtSize: true
    	    },
    	    contact: {
    	    	checkContact: true
     	    }/*,
     	    voicerecord: {
    	    	checkVoicerecord: true
    	    }*/
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
	        subAmt: {
	        	required : "认购金额：必须填写！",
	        	checkSubAmt: ""
	        },
    	    contact: {
     	    	checkContact: "请选择经办人！"
     	    }/*,
     	    voicerecord: {
     	    	checkVoicerecord: "录音编号：必须填写！"
     	    }*/
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
	if((fundId == "" || fundId == null) && queryflag == "Y"){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证认购金额是否合法
 */
jQuery.validator.methods["checkSubAmt"]=(function(value,element){
	var subAmt = $("#subAmt").val();
	var success=false;
	var queryflag = $("#queryflag").val();
	if(!(subAmt).match(/^(\d{1,}(|(\.{1}\d{0,2})))$|^(\.{1}\d{1,2})$/)  && queryflag == "Y"){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证认购金额是否合法
 */
jQuery.validator.addMethod("checkSubAmtSize",function(value,element){
	var subAmt = $("#subAmt").val();
	var fundId = $("#fundid").val();
	var success=false;
	var customMsg = "";
	var queryflag = $("#queryflag").val();
	if(subAmt != ""  && queryflag == "Y"){
		$.ajax({
			type : "POST",
			async: false, 
			dataType : "json",
			url : BASE_PATH+'capitalService/server/getFundInfo.xhtml',
			data:{
				'fundId':fundId
			},
			success : function(data) {
				var minSubAmt = data.minSubAmt;
				if((Number(subAmt) < Number(minSubAmt))){
					success= false;
					customMsg = "认购金额不能小于最低限额："+minSubAmt+"！";
				}else{
					success= true;
				}
			},
			error : function(data) {
				success= false;
			}
		});
	}else{
		success= true;
	}
	jQuery.validator.messages.checkSubAmtSize = customMsg;
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
 *  验证录音编号是否为空
 */
jQuery.validator.methods["checkVoicerecord"]=(function(value,element){
	var voicerecord = $("#voicerecord").val();
	var invprtp = $(".invprtp").val();
	var success=false;
	var queryflag = $("#queryflag").val();
	if(invprtp!=null && invprtp =='1' && (voicerecord ==null || voicerecord =='') && queryflag == "Y"){
		success = false
	}else{
		success = true
	}
	return success;
});


function queryClientInfo(){
	if($("#subscribeQuery").valid()){
		$(".invnm").html("");
		$(".invtp").html("");
		$(".idtpNm").html("");
		$(".idno").html("");
		$(".bankAcco").html("");
		$(".openName").html("");
		$(".custrisklevl").val();
		$(".invprtpString").html("");
		$(".invprStyle").hide();
		$("#voicerecord").val("");
		$(".custno").val("");
		$(".hidtradeacco").val("");
		$(".custname").val("");
		$(".hidfundacco").val("");
		$(".inputTd").remove();
		
		var tradeacco = $("#tradeacco").val();
		var fundacco = $("#fundacco").val();
		//提交表单
		$.ajax({
			url: BASE_PATH+'capitalService/server/tradeaccoQry.xhtml',   
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
					$("input[name=specriskLevel]").val(data.specriskLevel);
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
					var tano = data.gettano;
					$("#tano").val(tano);
					getAllSubFunds();
					
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
					$("#appendContact").after(appendHtml);
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
 * 提交认购数据
 */
function checkSubmit(){
	var queryflag = $("#queryflag").val();
	var flag = true;
	if(queryflag != 'Y'){
		toastr.warning('', '请先查询！');
		return false;
	}
	
	if($("#subscribe").valid()){
		var checkno = $("#checkno").val(); 
		var checkpwd = $("#checkpwd").val();
		if(isAudit == 'Y'){
			$.ajax({
				type : "POST",
				async: false, 
				dataType : "json",
				url : BASE_PATH+'capitalService/server/checkPermission.xhtml',
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
		var fundId = $("#fundid").val();
		var contvalidate = $("#hidcontvalidate").val();
		contvalidate = contvalidate.substring(0,4)+'/'+contvalidate.substring(4,6)+'/'+contvalidate.substring(6,8);
		var date = new Date();//获取当前时间 
		var today = date.Format("yyyyMMdd");
		today = today.substring(0,4)+'/'+today.substring(4,6)+'/'+today.substring(6,8);
		if(contvalidate !="" && Date.parse(contvalidate) < Date.parse(today)){
			toastr.warning('', '经办人证件已过期！');
			return false;
		}
		
		var invprtp = $(".invprtp").val();
		if(invprtp!=null && invprtp=='0'){
			doSubmit();
			return true;
		}
		var custrisklevl = $(".custrisklevl").val();
		if(custrisklevl == null || custrisklevl == '' || custrisklevl=='0'){
			ctools.alert_sweet('', "error", "请进行风险评级，当前默认为保守型！");
			return false;
		}
		
		var specriskLevel = $("input[name=specriskLevel]").val();
		if(specriskLevel == '1'){
			ctools.alert_sweet('', "error", "该客户风险承受能力为C0-最低，不可购买超过其风险承受能力的产品！");
			return false;
		}
		
		$.ajax({
			type : "POST",
			async: false,
			dataType : "json",
			url : BASE_PATH+'capitalService/server/getFundInfo.xhtml',
			data:{
				'fundId':fundId
			},
			success : function(data) {
				var resultCode = "N";
				var fundrisklevl = data.fundRiskLevel;
				if(fundrisklevl != null && "X" != custrisklevl){
			    	if (fundrisklevl <= custrisklevl){
			    		resultCode = "Y";
			    	} else {
			    		resultCode = "N";
			    	}
				} else {
					resultCode = "N";
				}
				if(resultCode == 'Y'){
					$(".hidriskflag").val("Y");	//匹配
					doSubmit();
				} else {
					var custriskString="";
					var fundriskString="";
					if(custrisklevl!=null && custrisklevl=='1'){
						custriskString='保守'
					}else if(custrisklevl!=null && custrisklevl=='2'){
						custriskString='稳健'
					}else if(custrisklevl!=null && custrisklevl=='3'){
						custriskString='平衡'
					}else if(custrisklevl!=null && custrisklevl=='4'){
						custriskString='成长'
					}else if(custrisklevl!=null && custrisklevl=='5'){
						custriskString='积极'
					}
					
					if(fundrisklevl!=null && fundrisklevl=='1'){
						fundriskString='低风险'
					}else if(fundrisklevl!=null && fundrisklevl=='2'){
						fundriskString='中低风险'
					}else if(fundrisklevl!=null && fundrisklevl=='3'){
						fundriskString='中风险'
					}else if(fundrisklevl!=null && fundrisklevl=='4'){
						fundriskString='中高风险'
					}else if(fundrisklevl!=null && fundrisklevl=='5'){
						fundriskString='高风险'
					}
					/*if(custrisklevl!=null && custrisklevl=='1' && fundrisklevl!=null &&  fundrisklevl!='1'){
						ctools.alert_sweet('', "error", "该用户为保守型，只能购买低风险产品");
						return false;
					}*/
					var title="客户风险承受能力为："+custriskString+"型，与产品风险等级："+fundriskString+"不匹配，请确认是否进行二次风险揭示？";
					ctools.confirm({text:title},function(isConfirm){
						if(isConfirm){
							$(".hidriskflag").val("N");	//不匹配
							doSubmit();
						}
					});
				}
			},
			error : function(data) {
				success= false;
			}
		});
	}
}

/**
 * 提交认购数据
 * @returns {Boolean}
 */
function doSubmit(){
	var trustType = $("#trustType").val();
	$(".trustType").val(trustType);
	var custname = $(".custname").val();
	var tradeAcco = $(".hidtradeacco").val();
	var fundid = $("#fundid").val();
	var subamt = $("#subAmt").val();
	var fundacco = $(".hidfundacco").val();
	var title = "客户名称："+custname +"\n交易账号："+tradeAcco+"\n认购金额："+subamt+"\n基金代码："+fundid+"\n基金账号："+fundacco+"\n\n是否确认提交该笔申请？";
	ctools.confirm({text:title},function(isConfirm){
		if(isConfirm){
			$.ajax({
				type : "POST",
				async: false, 
				dataType : "json",
				data: $('#subscribe').serialize(),// 你的formid
				url : BASE_PATH+'capitalService/server/submitSubscribe.xhtml',
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
