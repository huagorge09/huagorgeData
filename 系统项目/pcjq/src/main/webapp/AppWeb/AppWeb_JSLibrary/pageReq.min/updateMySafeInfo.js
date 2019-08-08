var isChange = false;
$(function(){
    $(".close_address").click(function(){
    	$("#div_cover_bg").hide();
    	$("#detailAddress").val("");
    	$(".error_msg").text("");
    });
    $(".abandon_btn").click(function(){
    	$("#ware_cover_bg").hide();
    });
    $(".continue_btn").click(function(){
    	$.ajax({
            async: !1,
            url: "/AppService/business/appConversionUserInvtp.xhtml",
            data: {
            	apptp :"0"
            },
            dataType: "json",
            cache: !1,
            type: "POST",
            error: function() {
                show_tips("网络繁忙，请稍后再试。");
            },
            success: function(n) {
                if(n != null && n.resultCode =="0000"){
                	$("#ware_cover_bg").hide();
                	showAlertMsg("");
                	queryUserinfo();
                }else{
                	$("#ware_cover_bg").hide();
                	showAlert("申请为专业投资者失败。");
                }
            }
        });
    });
    $("#common-box-context span.list").click(function(){
		$(this).removeClass("act").siblings().removeClass("act");
		$(this).addClass("act");
		var dataInput = $(this).attr("data-input");
		if("Y"==dataInput){
			$(this).parent().find("div").show();
		}else{
			$(this).parent().find("div").find("input").val("");
			$(this).parent().find("div").hide();
		}
		
		var subjoinFlag = true;
		var isControl = $(".common-box-context ul li:eq(13) span.act input").val();
		var isNotBeneficiary = $(".common-box-context ul li:eq(14) span.act input").val();
		var isBadHonesty = $(".common-box-context ul li:eq(15) span.act input").val();
		
		if((undefined != isControl && !isControl) || (undefined != isNotBeneficiary && !isNotBeneficiary) || (undefined != isBadHonesty && !isBadHonesty) ){
			subjoinFlag = false;
		}
		
		var actLen = $("#common-box-context span.act").length;
		if(actLen >= 18 && subjoinFlag){
			$("#common-box-sub-btn").addClass("act");
			$("#common-box-sub-btn").bind("click",function(){subRiskLevel();})
		}else{
			$("#common-box-sub-btn").removeClass("act");
			$("#common-box-sub-btn").unbind("click");
		}
	});
    $("#common-bg-res-btn").click(function(){
    	$(".common-box-context span.list").removeClass("act");
    	$("#common-box-sub-btn").removeClass("act");
    	$("#common-box-sub-btn").unbind("click");
    	$(".common-box-context span.list div").hide();
    	$(".common-box-context span.list div input").val("");
    	$("#common-box-context").scrollTop(0);
    });
    $("#close_common_bg").click(function(){
    	$("#common_cover_bg").hide();
    	$(".common-bg-res-btn").click();
    });
    showTaxNationArea();
    setTimeout(function(){queryBirthDateByInvtpSwitch()},3000);
    
});

/*传索引值查询对应选中的值--data-score*/
function getScoreVal(index){
	return  $(".common-box-context ul li:eq("+index+") span.act").attr("data-score");
}

function showAddress(){
	var nation = $("input[name='nation']").val();
	var province = $("input[name='province']").val();
	var city = $("input[name='city']").val();
	var addr = $("input[name='addr']").val();

	$('select.select').ui_select();
	$("#nationality").parent().parent().html('<select class="select" id="nationality" name="nationality" style="width: 218px;"></select>');
	var nationalityData = queryParamList("DS","DS_NATION","");
	var str = "";
	str+="<option value=''>请选择</option>";
	for (var i = 0; i < nationalityData.length; i++) {
		var temp = nationalityData[i];
		str+="<option value = '"+temp.pmco+"'>"+temp.pmnm+"</option>";
	}
	$("#nationality").html(str);
	$('#nationality').ui_select({
		onChange:function (value, item) {
			if(!isChange){
				$("#detailAddress").val("");
			}
			isChange = true;
			showSelect(value,true);
		}
	});
	$("#div_cover_bg").show();
	showSelect("",false);
}

function updateAddress(){
	$(".error_msg").text("");
	var nationality = $('#nationality').data('ui-select').val();
	if(nationality == '' || nationality == null){
		$(".error_msg").text("国籍不能为空！");
		return;
	}
	var province = $('#province').data('ui-select').val();
	var provinceStat = $("#province").parent().parent().is(":hidden");
	if((province == '' || province == null) && !provinceStat){
		$(".error_msg").text("省份不能为空！");
		return;
	}
	var city = $('#city').data('ui-select').val();
	var stat = $("#city").parent().parent().is(":hidden");
	if((city == '' || city == null) && !stat){
		$(".error_msg").text("城市不能为空！");
		return;
	}
	var detailAddress = $("#detailAddress").val();
	//入库
	$.ajax({
        async: !1,
        url: "/AppService/business/updateCmfUserBaseInfo.xhtml",
        data: {
        	nation :nationality,
        	province : province,
        	city : city,
        	addr : detailAddress,
        	voccode : ""
        },
        dataType: "json",
        cache: !1,
        type: "POST",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {
        	if(n != null && n.resultCode =="0000"){
        		$(".error_msg").text("选择地址成功！");
        		$(".close_address").click();
        		queryUserinfo();
            }else{
            	$(".error_msg").text("选择地址失败！");
            }
        }
    });
}

function showSelect(value,type){
	var nation = $("input[name='nation']").val();
	var province = $("input[name='province']").val();
	var city = $("input[name='city']").val();
	var addr = $("input[name='addr']").val();
	if(type){
		$(".error_msg").text("");
		if(value == "" || value == null){
			$("#province").parent().parent().html('<select class="select" id="province" name="province" style="width: 218px;"></select>');
			$(".province").show();
			$(".city").show();
			var str = "<option value=''>请选择</option>";
		    $("#province").html(str);
		    $('#province').ui_select();
		    $("#city").parent().parent().html('<select class="select" id="city" name="city" style="width: 218px;"></select>');
			$("#city").parent().parent().show();
			var str = "<option value=''>请选择</option>";
		    $("#city").html(str);
			$('#city').ui_select();
		}else{
			$("#province").parent().parent().html('<select class="select" id="province" name="province" style="width: 218px;"></select>');
			var provinceData = queryParamList("SYSTEM","DS_PROVINCE",value);
			if(provinceData.length > 0){
				$(".province").show();
				$(".city").show();
				var str = "";
				str+="<option value=''>请选择</option>";
				for (var i = 0; i < provinceData.length; i++) {
					var temp = provinceData[i];
					str+="<option value = '"+temp.pmco+"'>"+temp.pmnm+"</option>";
				}
			    $("#province").html(str);
			    $('#province').ui_select({
			    	onChange:function (value, item) {
			    		if(!isChange){
							$("#detailAddress").val("");
						}
			    		isChange = true;
			    		$(".error_msg").text("");
			    		if(value == "" || value == null){
							$("#city").parent().parent().html('<select class="select" id="city" name="city" style="width: 218px;"></select>');
							$("#city").parent().parent().show();
							var str = "<option value=''>请选择</option>";
						    $("#city").html(str);
							$('#city').ui_select();
						}else{
							var cityData = queryParamList("SYSTEM","DS_CITYCODE",value);
				    		if(cityData.length > 1){
				    			$("#city").parent().parent().html('<select class="select" id="city" name="city" style="width: 218px;"></select>');
				    			$("#city").parent().parent().show();
								var str = "";
								str+="<option value=''>请选择</option>";
								for (var i = 0; i < cityData.length; i++) {
									var temp = cityData[i];
									str+="<option value = '"+temp.pmco+"'>"+temp.pmnm+"</option>";
								}
							    $("#city").html(str);
								$('#city').ui_select({
							    	onChange:function (value, item) {
							    		if(!isChange){
											$("#detailAddress").val("");
										}
							    		isChange = true;
							    		$(".error_msg").text("");
							    	}
							    });
				    		}else{
				    			$("#city").parent().parent().html('<select class="select" id="city" name="city" style="width: 218px;"></select>');
				    			$("#city").parent().parent().show();
				    			var str = "<option value=''>请选择</option>";
				    		    $("#city").html(str);
				    			$('#city').ui_select();
				    			$(".city").hide();
				    		}
						}
			    	}
			    });
			    $("#city").parent().parent().html('<select class="select" id="city" name="city" style="width: 218px;"></select>');
				$("#city").parent().parent().show();
				var str = "<option value=''>请选择</option>";
			    $("#city").html(str);
				$('#city').ui_select();
			}else{
				var str = "<option value=''>请选择</option>";
			    $("#province").html(str);
			    $('#province').ui_select();
			    $(".province").hide();
			    $('#province').ui_select();
			    $("#city").parent().parent().html('<select class="select" id="city" name="city" style="width: 218px;"></select>');
				$("#city").parent().parent().show();
				var str = "<option value=''>请选择</option>";
			    $("#city").html(str);
				$('#city').ui_select();
				$(".city").hide();
			}
		}
	}else{
		if(province != "" && province != null){
			var provinceData = queryParamList("SYSTEM","DS_PROVINCE",nation);
			if(provinceData.length > 0){
				$(".province").show();
				$(".city").show();
				var str = "";
				str+="<option value=''>请选择</option>";
				for (var i = 0; i < provinceData.length; i++) {
					var temp = provinceData[i];
					str+="<option value = '"+temp.pmco+"'>"+temp.pmnm+"</option>";
				}
			    $("#province").html(str);
			    $('#province').ui_select();
			}else{
				$(".province").hide();
				$(".city").hide();
				$('#province').ui_select();
			}
		}
		
		$(".error_msg").text("");
		$("#province").parent().parent().html('<select class="select" id="province" name="province" style="width: 218px;"></select>');
		$(".province").show();
		$(".city").show();
		var str = "<option value=''>请选择</option>";
	    $("#province").html(str);
	    $('#province').ui_select();
	    $("#city").parent().parent().html('<select class="select" id="city" name="city" style="width: 218px;"></select>');
		$("#city").parent().parent().show();
		var str = "<option value=''>请选择</option>";
	    $("#city").html(str);
		$('#city').ui_select();
		if(nation != "" && nation != null){
			$('#nationality').data('ui-select').val(""+nation+"");
		}
		if(province != "" && province != null){
			$('#province').data('ui-select').val(""+province+"");
		}
		if(city != "" && city != null){
			$('#city').data('ui-select').val(""+city+"");
		}
		if(addr != "" && addr != null){
			$("#detailAddress").val(addr);
			isChange = false;
		}
	}
}

function queryParamList(paramType,paramKey,pmValueOne){
    var data = null;
    $.ajax({
        async: !1,
        url: "/AppService/business/queryParamList.xhtml",
        data: {
            paramType :paramType,
            paramKey : paramKey,
            pmValueOne : pmValueOne
        },
        dataType: "json",
        cache: !1,
        type: "POST",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {
            if(n != null && n.resultCode =="0000"){
                data = n.data;
            }
        }
    });
    return data;
}

function showAlert(text){
	$("#alert_cover_bg .align-center").text(text);
	$("#alert_cover_bg").show();
	$(".btn-close-alert").unbind("click");
	$(".btn-close-alert").click(function(){
		$("#alert_cover_bg").hide();
    });
}

function showAlertKnow(text){
	$("#alert_cover_bg .align-center").text(text);
	$("#alert_cover_bg").show();
	$(".btn-close-alert").unbind("click");
	$(".btn-close-alert").click(function(){
    	window.location.href="/AppService/business/bank/realName.shtml";
    });
}

function showInvprtpDiv(type){
	var realNameStatusEm = $("#realNameStatusEm").html();
	if(realNameStatusEm == '未认证'){
		showAlertKnow("请您完成实名鉴权");
	}else{
		if('0' == type){
			$("#ware_cover_bg").show();
		}else if(type == 'N'){
			//showAlert("您的专业型用户身份证明材料正在审批中，请耐心等待。");
			showApplyIng("","0");
		}else if(type == 'C'){
			$("#ware_cover_bg").show();
		}else if("Y" == type){
			$("#common_cover_bg").show();
			$(".common-box-context span.list").removeClass("act");
	    	$("#common-box-sub-btn").removeClass("act");
	    	$("#common-box-sub-btn").unbind("click");
	    	$("#common-box-context").scrollTop(0);
	    	operatingRecord(pageSourceId,pageId,"event_investorTtypeId","");
			dateOfBirthChange();
		}else if("E" == type){
			$("#invest_knowledge_bg").show();
			$(".common-box-context-invest span.list").removeClass("act");
	    	$(".invest_knowledge_bg_btn").removeClass("act");
	    	$(".invest_knowledge_bg_btn").unbind("click");
	    	$(".common-box-context-invest").scrollTop(0);
			
			/**
			 * 关闭申请转换为专业投资者投资知识测试问卷弹框
			 */
			$(".close_invest_bg").unbind("click");
			$(".close_invest_bg").click(function(){
				$("#invest_knowledge_bg").hide();
			})
			
			/**
			 * 选择答案事件
			 */
			$(".common-box-context-invest span.list").unbind("click");
			$(".common-box-context-invest span.list").click(function(){
				$(this).removeClass("act").siblings().removeClass("act");
				$(this).addClass("act");
				var actLen = $(".common-box-context-invest span.act").length;
				if(actLen >= 20){
					$(".invest_knowledge_bg_btn").addClass("act");
					/**
					 * 提交问卷事件
					 */
					$(".invest_knowledge_bg_btn").unbind("click");
					$(".invest_knowledge_bg_res_btn").unbind("click");
					$(".invest_knowledge_bg_btn").click(function(){
						var evalAnswer = 0;/* 临时变量*/
						var len = $(".common-box-context-invest span.list.act").length;
						for(var i = 0; i < len; i++){
							var score = $(".common-box-context-invest ul li:eq("+i+") span.act").attr("data-score");
							if(score == "Y"){
								evalAnswer++;
							}
						}
						evalAnswer = evalAnswer*5;
						
						$("#invest_knowledge_bg").hide();
						//入库
						$.ajax({
							async: !1,
							url: "/AppService/business/passTestUpdateUserInvtp.xhtml",
							data:{
								'invprtpScore' : evalAnswer
							},
							dataType: "json",
							cache: !1,
							type: "POST",
							error: function() {
								show_tips("网络繁忙，请稍后再试。");
							},
							success: function(n) {
								if(n != null && n.resultCode =="0000"){
									if(evalAnswer >= 60){
										$("#alert_invest_knowledge_success").show();
										$("#btn-close-success").unbind("click");
										$("#btn-close-success").click(function(){
											$("#alert_invest_knowledge_success").hide();
										});
										queryUserinfo();
									}else{
										$("#alert_invest_knowledge_reset").show();
										$("#btn_reset_test").unbind("click");
										$("#btn_reset_test").click(function(){
											$("#alert_invest_knowledge_reset").hide();
											$("#invest_knowledge_bg").show();
											$(".invest_knowledge_bg_res_btn").click();
										});
										$("#btn_cancel_test").unbind("click");
										$("#btn_cancel_test").click(function(){
											$("#alert_invest_knowledge_reset").hide();
										});
									}
								}else if(n != null && n.resultCode !="0000"){
									show_tips(n.resultMsg);
								}else{
									show_tips("提交投资知识评测异常。");
								}
							}
						});
					});
				}
				
				/**
				 * 重置选项
				 */
				$(".invest_knowledge_bg_res_btn").click(function(){
					$(".common-box-context-invest span.list").removeClass("act");
			    	$(".invest_knowledge_bg_btn").removeClass("act");
			    	$(".invest_knowledge_bg_btn").unbind("click");
			    	$(".common-box-context-invest").scrollTop(0);
				});
			});
		}
	}
}

function subRiskLevel(){
	var evalAnswer = 0;/* 临时变量*/
	var len = $("#common-box-context span.list.act").length;
	var val = 0;/* 相加的值*/
	for(var i = 0; i < len; i++){
		evalAnswer = evalAnswer != "" ? evalAnswer + ',' + parseInt(getScoreVal(i),10) + getThisValDesc(i) + getThisInputVal(i) : parseInt(getScoreVal(i),10) + getThisValDesc(i) + getThisInputVal(i);
		val = parseInt(getScoreVal(i),10) + parseInt(val,10);
	}
	var riskLevel = "";	/*等级：风险级别 1-保守型,2-稳健型,3-C5-积极型(0-20:2,21-60:3,61-100:4)*/
    var channelCode = "NET";		/*渠道代码 NET-网站，IVR-自动语音，DIR-直销  (网上交易填写 NET)*/ 
    var evalType = "M";	/*测评类型 T-问卷测评，M-客户自评,网上交易填写 M*/
    var evalFormno = val;		/*填写用户分数*/
    var status = "N";		/*填写N*/
    
  //请问您是否具有完全民事行为能力
    var isCompAbility = $("#common-box-context ul li:eq(16) span.act").attr("data-value");
    //请问您是否没有风险容忍度或者不愿承受任何投资损失
    var isEndureLoss = $("#common-box-context ul li:eq(17) span.act").attr("data-value");
    
    if("N" == isCompAbility || "Y" == isEndureLoss){
    	val = 0;
    	evalFormno = val;
    }
    
    var revenue = $("#common_cover_checkbox").get(0).checked;
	if(!revenue){
		show_tips("请阅读并勾选声明！");
		return;
	}
    
    /*(20-40:1;41-60:2;61-100:3)*/
    if(val >= 24 && val <=34){
    	riskLevel = "1";
    }else if(val >= 35 && val <=48){
    	riskLevel = "2";
    }else if(val >= 49 && val <=62){
    	riskLevel = "3";
    }else if(val >= 63 && val <=80){
    	riskLevel = "4";
    }else if(val > 81){
    	riskLevel = "5";
    }else{
    	riskLevel = "1";
    }
    $.ajax({
		type: "POST",
    	async:false,
        url: "/AppService/business/appConversionUserInvtp.xhtml",
        dataType: "json",
        data: {
        	"apptp": "1",
            "riskLevel":riskLevel,		/*等级：风险级别 1-保守型,2-稳健型,3-C5-积极型(21-40:1,41-60:2,61-100:3)*/
            "channelCode":channelCode,		/*渠道代码 NET-网站，IVR-自动语音，DIR-直销  (网上交易填写 NET)*/ 
            "evalType":evalType,		/*测评类型 T-问卷测评，M-客户自评,网上交易填写 M*/
            "evalFormno":	evalFormno,		/*填写用户分数*/
            "status":	status,			/*填写N*/
            "evalAnswer": evalAnswer
        },
        cache: false,
        error : function(textStatus, errorThrown) {  
            show_tips("网络繁忙，请稍后再试。");  
        }, 
        success : function (data) {
        	var htmls = "";
        	var temp = "";
        	var tempName = "";
        	if(riskLevel == "1"){
        		temp = "C1-保守型（低风险承受能力）";
        		tempName = "低风险产品";
        	}else if(riskLevel == "2"){
        		temp = "C2-稳健型（中低风险承受能力）";
        		tempName = "中低风险及以下产品";
        	}else if(riskLevel == "3"){
        		temp = "C3-平衡型（中风险承受能力）";
        		tempName = "中风险及以下产品";
        	}else if(riskLevel == "4"){
        		temp = "C4-成长型（中高风险承受能力）";
        		tempName = "中高风险及以下产品";
        	}else if(riskLevel == "5"){
        		temp = "C5-积极型（高风险承受能力）";
        		tempName = "高风险及以下产品";
        	}else{
        		temp = "C1-保守型（低风险承受能力）";
        		tempName = "低风险产品";
        	}
        	if(data != null && data.returnCode == "0000"){
        		$("#common_cover_bg").hide();
        		$("#riskLeverCustLever").html(temp);
        		$("#evalValiDate").html("问卷有效期："+data.evalValiDate);
        		$("#evalDate").html("测评时间&nbsp;&nbsp;&nbsp;&nbsp;："+data.evalDispDateTime);
        		$("#riskEvel").html("适宜产品等级："+tempName);
        		$("#common_cover_risk").show();
        		operatingRecord(pageSourceId,pageId,"event_002_commitQuestionId","");
        	}else{
        		show_tips("申请为普通投资者失败："+data.returnMsg);
        	}
        }
	});
};

/**
 * 格式化年月日
 * @param date
 * @returns
 */
function formatDate(date){
	if(date == null || date == ""){
		return "";
	}
	date = date + "";
	date = unformat1(date);
	return date.substr(0,4)+"年"+date.substr(4,2)+"月"+date.substr(6,2)+"日"+date.substr(8,2)+"时"+date.substr(10,2)+"分";
}




//最低年龄限制 民事行为能力 只能选 否
var mayAgeToDay = 16;
function dateOfBirthChange(){
	$("#common_cover_checkbox").attr("checked",false);
	$(".w_text").val("");
	//出生日期
	var thisVal = $("#birthDateFrm").val().replace(/-/g,"");
	var nowDate= new Date();
	var nowYear =nowDate.getFullYear();
	var nowMonth = nowDate.getMonth() + 1;
	var nowDate = nowDate.getDate();
	if(nowMonth < 10){
		nowMonth = "0" + nowMonth;
	}
	if(nowDate < 10){
		nowDate = "0" + nowDate;
	}
	
	var limitAge = (parseInt(nowYear)-parseInt(mayAgeToDay)) + "" + nowMonth + nowDate;
	
	var dataDiff = daysBetween(limitAge,thisVal);
	//时间差
	if(dataDiff < 0 || !thisVal || isNaN(dataDiff) ){
		$(".common-box-context ul li:eq(16) span.list:eq(1) div p").html("年龄不满16岁，不具有完全民事行为能力。<a href='/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO'>请根据实际情况填写出生日期。</a>");
		$(".common-box-context ul li:eq(16) span.list:eq(0)").unbind("click");
		$(".common-box-context ul li:eq(16) span.list:eq(1)").bind("click");
		$(".common-box-context ul li:eq(16) span.list:eq(1)").click();
	}else{
		$("#common-box-context ul li:eq(16) span.list").removeClass("act");
		$("#common-box-context ul li:eq(16) span.list").bind("click",function(e){optionBindClickEvent(e)});
	}
}



/*传索引值查询对应选中的值--data-value*/
function getThisValDesc(index){
	var value= $("#common-box-context ul li:eq("+index+") span.act").attr("data-value");
	if(!!value){
		return  ":"+value;
	}
	return ":";
}

/*传索引值查询对应输入的值-*/
function getThisInputVal(index){
	var value = $("#common-box-context ul li:eq("+index+") span.act input").val();
	if(!!value){
		return  ":"+value;
	}
	return ":";
}


function riskLevel_again_back(){
	$("#common_cover_risk").hide();
	$("#common-box-context span.list").removeClass("act");
	
	$("#common-box-context span.list div").hide();
	$("#common-box-context span.list div input").val("");
	
	$(".common-box-sub-btn").removeClass("act");
	$(".common-box-sub-btn").unbind("click");
	$("#common_cover_bg").show();
	dateOfBirthChange();
	operatingRecord(pageSourceId,pageId,"event_002_reassessId","");
	$("#common-box-context").scrollTop(0);
}

/**
 * 20171009版本新增功能
 */
function chengeBirthDate(){
	var birthDateFrm = $("#birthDateFrm").val();
	if(birthDateFrm != "" && birthDateFrm != null){
		$("#birthDateText").val(birthDateFrm);
	}else{
		$("#birthDateText").val("1985-01-01");
	}
	$("#div_birthDate_bg").show();
}
 /**
  * 提交修改出生日期
  */
function submitBirthDate(){
	var birthDateText = $("#birthDateText").val();
	$("#birthMsg").text("");
	if(birthDateText == ""){
		$("#birthMsg").text("出生日期不能为空！");
		return;
	}
	//入库
	$.ajax({
        async: !1,
        url: "/AppService/business/updateCmfUserBaseInfo.xhtml",
        data: {
        	nation :"",
        	province : "",
        	city : "",
        	addr : "",
        	voccode : "",
        	birthDate:birthDateText
        },
        dataType: "json",
        cache: !1,
        type: "POST",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {
        	if(n != null && n.resultCode =="0000"){
        		hideBirthDateDiv();
        		showAlert("选择出生日期成功！");
        		queryUserinfo();
            }else{
            	hideBirthDateDiv();
            	showAlert("选择出生日期失败！");
            }
        }
    });
}

function hideBirthDateDiv(){
	$("#div_birthDate_bg").hide();
}

/**
 * 申请专业投资者提交成功提醒
 * @param text
 */
function showAlertMsg(text){
	if(text != "" && text != null){
		$("#alert_message").html(text);
	}
	$("#alert_message_bg").show();
	$(".btn-close-alert").unbind("click");
	$(".btn-close-alert_msg").click(function(){
		$("#alert_message_bg").hide();
    });
}

/**
 * 专业投资者申请中
 * @param text
 */
function showApplyIng(text,type){
	if(text != "" && text != null){
		$("#alert_applyIng").html(text);
	}
	$("#alert_applyIng_bg").show();
	
	$(".btn-close-applyIng").unbind("click");
	$(".btn-close-cancel").unbind("click");
	
	if(type == "0"){
		$("#pic_flow").attr("src","/AppWeb/AppWeb_Images/images/pic_flow.png");
		$(".btn-close-applyIng").html("我知道了");
		$(".btn-close-applyIng").click(function(){
			$("#alert_applyIng_bg").hide();
		});
	}else{
		$("#pic_flow").attr("src","/AppWeb/AppWeb_Images/images/pic_flow1.png");
		$("#apply_pass_begin_knowledge").show();
		$(".btn-close-applyIng").click(function(){
			showInvprtpDiv('E');
			$("#alert_applyIng_bg").hide();
		});
	}
	
	/**
	 * 取消申请专业投资者
	 */
	$(".btn-close-cancel").click(function(){
		invprtpCancelConfirm();
	});
}

function invprtpCancelConfirm(){
	$("#cancelInvprtp_bg").show();
	$("#cancelInvprtp_bg input[name=confirm]").bind("click",function(){
		close_tips('cancelInvprtp_bg');
		cancelAppInvtp();
	});
} 


function cancelAppInvtp(){
	$.ajax({
        async: !1,
        url: "/AppService/business/cancelAppConversionUserInvtp.xhtml",
        data: {},
        dataType: "json",
        cache: !1,
        type: "post",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {
        	if(n != null && n.resultCode =="0000"){
        		$("#alert_applyIng_bg").hide();
        		showAlert("取消申请专业投资者成功！");
        		$("#invprtTpCancel").remove();
        		queryUserinfo();
            }else{
            	$("#alert_applyIng_bg").hide();
            	showAlert("取消申请专业投资者失败！");
            }
        }
    });
}

/**
 * 选择税收居民类型
 */
function showRevenueResidentDiv(){
	$("#revenue_resident_bg").show();
	$("#revenue_hint").attr("checked",false);
	/*$("#taxResidentType").val("2");
	$("#taxResidentType").change();*/
	queryUserTaxInfo();
	showUserTaxInfo();
	
	$(".btn_submit_revenue").unbind("click");
	$(".btn_submit_revenue").click(function(){
		var falg = checkTaxResidentData();
		if(!falg){
			show_tips("请完善税收居民信息！");
			return;
		}
		
		var taxResidentType = $("#taxResidentType").val();
		if(taxResidentType != "1" && taxResidentType != ""){
			var birth_nationVal = $("#birth_nation").val();
			var birth_regionVal = $("#birth_region").val();
			if(birth_nationVal == "" || birth_regionVal == ""){
				show_tips("请选择出生地！");
				return;
			}
		}
		
		var revenue = $("#revenue_hint").get(0).checked;
		if(!revenue){
			show_tips("请阅读并勾选声明！");
			return;
		}
		
		//获取 税收居民信息
		var taxResidentType = $("#taxResidentType").val();
        var taxResidentData = encodeURI(getTaxResidentData());
		//入库
		$.ajax({
	        async: !1,
	        url: "/AppService/business/updateCmfUserBaseInfo.xhtml",
	        data: {
	        	nation :"",
	        	province : "",
	        	city : "",
	        	addr : "",
	        	voccode : "",
	        	birthDate:"",
	        	"taxResidentType" : taxResidentType,
                "taxResidentData" : taxResidentData
	        },
	        dataType: "json",
	        cache: !1,
	        type: "POST",
	        error: function() {
	            show_tips("网络繁忙，请稍后再试。");
	        },
	        success: function(n) {
	        	if(n != null && n.resultCode =="0000"){
	        		$("#revenue_resident_bg").hide();
	        		showAlert("选择税收居民类型成功！");
	        		queryUserinfo();
	            }else{
	            	$("#revenue_resident_bg").hide();
	            	showAlert("选择税收居民类型失败！");
	            }
	        }
	    });
	});
	$(".close_revenue_resident_bg").unbind("click");
	$(".close_revenue_resident_bg").click(function(){
		$("#revenue_resident_bg").hide();
	});
}

/**
 * 显示税收居民类型说明
 */
function showResidentTypeExplain(){
	$("#revenue_resident_type_bg").show();
	$("#revenue_resident_bg").hide();
	$(".btn-close").unbind("click");
	$(".btn-close").click(function(){
		$("#revenue_resident_type_bg").hide();
		$("#revenue_resident_bg").show();
	});
	$(".btn-confirm_yes").unbind("click");
	$(".btn-confirm_yes").click(function(){
		$("#revenue_resident_type_bg").hide();
		$("#revenue_resident_bg").show();
	});
}

var TAX_ADD_ID = 1;
//从模板 初始化 税收 问题
function initTaxNation(){
	//获取模板内容
	var addContent = $("#tax_template").html();
	//添加至目标
	var target = $("div[class=tax_add_div]:last");
	//目标id
	var targetId ="tax_add_"+TAX_ADD_ID;
	//开始添加元素
	target.after("<div class=\"tax_add_div\" id=\""+targetId+"\">"+addContent+"</div>");
	//以下为 修改 按钮参数 和 初始化控件
	//$("#taxResidentType").ui_select();
	//$("#taxResidentType").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#"+targetId+" select[name=taxNationality]:eq(0)").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#"+targetId+" select[name=notCodeCause]:eq(0)").select2({allowClear: false,minimumResultsForSearch:Infinity});
	return targetId;
}


function initTaxNationality(targetId,taxResidentType,taxNationality){
	var targetNode = $("#"+targetId+" select[name=taxNationality]");
	
	if(!!taxResidentType && "1" == taxResidentType){
		var taxNationTemp = $("#taxNation_temp").html();
		targetNode.html(taxNationTemp);
		targetNode.find("option[value!=1]option[value!='']").remove();
	}
	
	if(!!taxResidentType && "2" == taxResidentType){
		var taxNationTemp = $("#taxNation_temp").html();
		targetNode.html(taxNationTemp);
		targetNode.find("option[value=1]option[value!='']").remove();
	}
	
	if(!!taxResidentType && "3" == taxResidentType){
		var taxNationTemp = $("#taxNation_temp").html();
		targetNode.html(taxNationTemp);
		if(!!taxNationality && "1"== taxNationality){
			targetNode.find("option[value!=1]option[value!='']").remove();
			$("#"+targetId + " img").remove();
		}else{
			targetNode.find("option[value=1]option[value!='']").remove();
		}
		
	}
}

/*
function initTaxNationality(targetId,taxResidentType){
	var value = taxResidentType;
	var targetNode = $("#"+targetId+" select[name=taxNationality]");
	if(!!value && "1" == value){
		var taxNationTemp = $("#taxNation_temp").html();
		targetNode.html(taxNationTemp);
		targetNode.find("option[value!=1]option[value!='']").remove();
	}
	
	if(!!value && "2" == value){
		var taxNationTemp = $("#taxNation_temp").html();
		targetNode.html(taxNationTemp);
		targetNode.find("option[value=1]option[value!='']").remove();
	}
	
	if(!!value && "3" == value){
		var taxNationTemp = $("#taxNation_temp").html();
		targetNode.html(taxNationTemp);
		targetNode.find("option[value!=1]option[value!='']").remove();
	}
	targetNode.select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#"+targetId+" select[name=taxArea]").select2({allowClear: false,minimumResultsForSearch:Infinity});
}*/

function initTaxNationalityByChange(taxResidentType){
	var value = taxResidentType;
	//获取模板内容
	var addContent = $("#tax_template").html();
	//添加至目标
	var target = $("div[class=tax_add_div]:last");
	//目标id
	var targetId ="tax_add_"+TAX_ADD_ID;
	//开始添加元素
	target.after("<div class=\"tax_add_div\" id=\""+targetId+"\">"+addContent+"</div>");
	
	var targetNode = $("#"+targetId+" select[name=taxNationality]");
	
	if(!!value && "1" == value){
		var taxNationTemp = $("#taxNation_temp").html();
		targetNode.html(taxNationTemp);
		targetNode.find("option[value!=1]option[value!='']").remove();
	}
	
	if(!!value && "2" == value){
		var taxNationTemp = $("#taxNation_temp").html();
		targetNode.html(taxNationTemp);
		targetNode.find("option[value=1]option[value!='']").remove();
	}
	
	if(!!value && "3" == value){
		var taxNationTemp = $("#taxNation_temp").html();
		targetNode.html(taxNationTemp);
		targetNode.find("option[value!=1]option[value!='']").remove();
		$("#"+targetId + " img").remove();
		
		
		var newTargetId = addTaxNation();
		var newTargetNode = $("#"+newTargetId+" select[name=taxNationality]");
		newTargetNode.html(taxNationTemp);
		newTargetNode.find("option[value=1]option[value!='']").remove();
		newTargetNode.select2({allowClear: false,minimumResultsForSearch:Infinity});
		$("#"+newTargetId+" select[name=taxArea]").select2({allowClear: false,minimumResultsForSearch:Infinity});
		$("#"+newTargetId+" img").attr("src","/AppWeb/AppWeb_Images/images/bt_add1.png").attr("onclick","addTaxNation()");
	}
	targetNode.select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#"+targetId+" select[name=taxArea]").select2({allowClear: false,minimumResultsForSearch:Infinity});
}

function addTaxNation(){
	TAX_ADD_ID++;
	//获取模板内容
	var addContent = $("#tax_template").html();
	//添加至目标
	var target = $("div[class=tax_add_div]:last");
	//目标id
	var targetId ="tax_add_"+TAX_ADD_ID;
	//开始添加元素
	target.after("<div class=\"tax_add_div\" id=\""+targetId+"\">"+addContent+"</div>");
	//以下为 修改 按钮参数 和 初始化控件
	$("#"+targetId+" img").attr("src","/AppWeb/AppWeb_Images/images/bt_reduction.png").attr("onclick","delTaxNation('"+targetId+"')");
	$("#"+targetId+" img").addClass("minus");
	
	var targetNode = $("#"+targetId+" select[name=taxNationality]");
	var taxNationTemp = $("#taxNation_temp").html();
	targetNode.html(taxNationTemp);
	targetNode.find("option[value=1]option[value!='']").remove();
	targetNode.change();
	
	$("#"+targetId+" select[name=taxNationality]:eq(0)").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#"+targetId+" select[name=notCodeCause]:eq(0)").select2({allowClear: false,minimumResultsForSearch:Infinity});
	
	/*var num = $(".minus").length;
	if(num >= 0){
		$(".risk-add-list").css({"height":"250px","overflow":"auto"});
	}*/
	$(".risk-add-list").unbind('scroll');
	$(".risk-add-list").bind('scroll');
	return targetId;
}

function delTaxNation(elementId){
	$("#"+elementId).remove();
	/*var num = $(".minus").length;
	if(num <= 0){
		$(".risk-add-list").css({"height":"auto","overflow":"hidden"});
	}*/
}

/**
 * 税收居民类型 发生改变时
 */
function taxResidentChange(element){
	//$(document).scrollTop($(element).offset().top);
	var value = element.value;
	//将税收居民国等内容 全部删除
	$("div[class=tax_add_div]div[id!=tax_template]").remove();
	if(!!value && "1" != value){
		if(value == "2"){
			$(".addTaxNation").hide();
		}else{
			$(".addTaxNation").show();
		}
		birthplaceDiv();
		initTaxNationalityByChange(value);
	}else{
		$(".birthplace").hide();
	}
	/*var num = $(".minus").length;
	if(num <= 0){
		$(".risk-add-list").css({"height":"auto","overflow":"hidden"});
	}*/
	$(".risk-add-list").unbind('scroll');
	$(".risk-add-list").bind('scroll');
}

/**
 * 是否 无纳税人识别号
 */
function isTaxpayerEvent(e){
	var checked = e.checked;
	var target = $(e);
	if(checked){
		//如果为 是
		target.parent().prev().attr("readonly","readonly");
		target.parent().prev().attr("disabled","disabled");
		target.parent().prev().val(null);
		target.parent().parent().parent().parent().next().find("select[name='notCodeCause']").val("");
		target.parent().parent().parent().parent().next().find("select[name='notCodeCause']").select2({allowClear: false,minimumResultsForSearch:Infinity});
		target.parent().parent().parent().parent().next().find("select[name='notCodeCause']").change();
		target.parent().parent().parent().parent().next().show();
	}else{
		target.parent().prev().removeAttr("readonly");
		target.parent().prev().removeAttr("disabled");
		target.parent().parent().parent().parent().next().hide();
	}
}

function notCodeCauseEvent(e){
	var target = $(e);
	target.parent().parent().parent().next().find("input[name='notGetCause']").val("");
	if(!target.val() ||  "1" == target.val()){
		target.parent().parent().parent().next().hide();
	}else{
		target.parent().parent().parent().next().show();
	}
}

/**
 * 校验 税收信息 是否填写完毕 
 * 请完善税收居民信息
 * @returns 任一条件不满足则返回 false
 */
function checkTaxResidentData(){
	var flag = true;
	var taxResidentType = $("select[name=taxResidentType]:eq(0)");
	
	if(!taxResidentType.val()){
		flag = false;
	}
	//居民类型为 非居民 则 校验以下信息
	if(flag && "1" != taxResidentType.val()){
		var taxResList = $("div[class=tax_add_div]div[id!=tax_template]");
		for (var i = 0; i < taxResList.length; i++) {
			var taxRes = $(taxResList[i]);
			
			var taxNationality,taxArea,taxpayerCode, isTaxpayerCode, notCodeCause, notGetCause;
			taxNationality = taxRes.find("select[name=taxNationality]:eq(0)");
			taxArea = taxRes.find("select[name=taxArea]:eq(0)");
			taxpayerCode = taxRes.find("input[name=taxpayerCode]:eq(0)");
			isTaxpayerCode = taxRes.find("input[name=isTaxpayerCode]:eq(0)");
			notCodeCause = taxRes.find("select[name=notCodeCause]:eq(0)");
			notGetCause = taxRes.find("input[name=notGetCause]:eq(0)");
			//纳税居民国是否为空 
			if(!taxNationality.val() || !taxArea.val()){
				flag = false;
			}
			
			//无纳税号 是否勾选
			if("checked" != isTaxpayerCode.attr("checked")){
				//不勾选 则纳税号 必填
				if(!taxpayerCode.val()){
					flag = false;
				}
			}else{
				//如果勾选 无识别号原因必填
				if(!notCodeCause.val()){
					flag = false;
				}else{
					//如果填的是 未取得 则 未取得原因必填
					if("2" == notCodeCause.val()){
						if(!notGetCause.val()){
							flag = false;
						}
					}
				}
			}
		}
	}
	return flag;
}

/**
 * 获取 税收信息数据 
 * 多税收信息 按层级获取后封装为json字符串
 * @returns 封装后的税收居民数据
 */
function getTaxResidentData(){debugger;
	var taxResidentData = "";
	var taxResidentType = $("select[name=taxResidentType]:eq(0)");
	//居民类型为 非居民 则 获取居民信息
	if("1" != taxResidentType.val()){
		var taxResList = $("div[class=tax_add_div]div[id!=tax_template]");
		var taxResValList = new Array();
		var birth_nationVal = $("#birth_nation").val();
		var birth_regionVal = $("#birth_region").val();
		var birth_address = $("#birth_address").val();
		
		for (var i = 0; i < taxResList.length; i++) {
			var taxResVal = {};
			var taxRes = $(taxResList[i]);
			var taxNationality,taxArea, taxpayerCode, isTaxpayerCode, notCodeCause, notGetCause;
			taxNationality = taxRes.find("select[name=taxNationality]:eq(0)");
			taxArea = taxRes.find("select[name=taxArea]:eq(0)");
			taxpayerCode = taxRes.find("input[name=taxpayerCode]:eq(0)");
			isTaxpayerCode = taxRes.find("input[name=isTaxpayerCode]:eq(0)");
			notCodeCause = taxRes.find("select[name=notCodeCause]:eq(0)");
			notGetCause = taxRes.find("input[name=notGetCause]:eq(0)");
			//序号
			taxResVal.sortNo = i;
			//纳税居民国
			taxResVal.taxNationality = taxNationality.val();
			taxResVal.taxArea = taxArea.val();
			//无纳税号 是否勾选
			if("checked" != isTaxpayerCode.attr("checked")){
				//不勾选 则纳税号 必填
				taxResVal.taxPayerCode = taxpayerCode.val();
				taxResVal.taxNotCodeCause = "";
				taxResVal.taxnotGetCause = "";
			}else{
				//如果勾选 纳税号 置为空
				taxResVal.taxPayerCode = "";
				taxResVal.taxNotCodeCause = notCodeCause.val();
				//如果填的是 未取得 则 未取得原因必填
				if("2" == notCodeCause.val()){
					taxResVal.taxnotGetCause = notGetCause.val();
				}
			}
			taxResVal.taxBirthNation = birth_nationVal;
			taxResVal.taxBirthRegion = birth_regionVal;
			taxResVal.taxBirthAddress = birth_address;
			
			taxResValList.push(taxResVal);
		}
		taxResidentData = JSON.stringify(taxResValList);
	}
	return taxResidentData;
}


/**
 * 查询用户税收居民信息
 */
function queryUserTaxInfo(){
	$.ajax({
        async: !1,
        url: "/AppService/business/queryUserTaxInfo.xhtml",
        data: {},
        dataType: "text",
        cache: !1,
        type: "post",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {
            if (!!n) {
            	$("input[name='taxResidentData']").val(n);
            }
        }
    });
}

/**
 * 加载税收居民类型数据
 */
function showUserTaxInfo(){debugger;
	$("div[class=tax_add_div]div[id!=tax_template]").remove();
	TAX_ADD_ID = 1;
	var taxResidentType = $("input[name='taxResidentType']").val();
	$("#taxResidentType").val(taxResidentType);
	$("#taxResidentType").select2({allowClear: false,minimumResultsForSearch:Infinity});
	
	//仅为大陆
	if("1" == taxResidentType){
		$(".birthplace").hide();
		return;
	}
	if(taxResidentType == "2"){
		$(".addTaxNation").hide();
	}else{
		$(".addTaxNation").show();
	}
	birthplaceDiv();
	var taxResidentData = $("input[name='taxResidentData']").val();
	var jsonArrayData = JSON.parse(taxResidentData);
	for (var i = 0; i < jsonArrayData.length; i++) {
		var taxResident = jsonArrayData[i];
		var targetId = "";
		if("1" == TAX_ADD_ID){
			targetId = initTaxNation();
			TAX_ADD_ID++;
		}else{
			targetId = addTaxNation();
		}
		var taxNationality = taxResident.taxNationality;
		var taxArea =  taxResident.taxArea;
		var taxPayerCode = taxResident.taxPayerCode;
		var taxNotCodeCause = taxResident.taxNotCodeCause;
		var taxnotGetCause = taxResident.taxnotGetCause;
		var taxBirthNation = taxResident.taxBirthNation;
		var taxBirthRegion = taxResident.taxBirthRegion;
		var taxBirthAddress = taxResident.taxBirthAddress;
		debugger;
		//初始化 选项框
		initTaxNationality(targetId,taxResidentType,taxNationality);
		if(i == 1){
			$("#"+targetId+" img").attr("src","/AppWeb/AppWeb_Images/images/bt_add1.png").attr("onclick","addTaxNation()");
		}
		initTaxArea(targetId,taxNationality);
		
		$("#" + targetId + " select[name=taxNationality]:eq(0)").val(taxNationality);
		$("#" + targetId + " select[name=taxNationality]:eq(0)").change();
		$("#" + targetId + " select[name=taxNationality]:eq(0)").select2({allowClear: false,minimumResultsForSearch:Infinity});
		$("#" + targetId + " select[name=taxArea]:eq(0)").val(taxArea);
		$("#" + targetId + " select[name=taxArea]:eq(0)").change();
		$("#" + targetId + " input[name=taxpayerCode]:eq(0)").val(taxPayerCode);
		if(!taxPayerCode){
			$("#" + targetId + " input[name=isTaxpayerCode]:eq(0)").click();
			$("#" + targetId + " input[name=isTaxpayerCode]:eq(0)").attr("checked","checked");
			$("#" + targetId + " select[name=notCodeCause]:eq(0)").val(taxNotCodeCause);
			$("#" + targetId + " select[name=notCodeCause]:eq(0)").change();
			$("#" + targetId + " input[name=notGetCause]:eq(0)").val(taxnotGetCause);
		}
		//$("#"+targetId+" select[class*=select2]").select2({allowClear: false,minimumResultsForSearch:Infinity});
		//出生地数据加载
		$("#birth_nation").val(taxBirthNation);
		$("#birth_nation").change();
		$("#birth_nation").select2({allowClear: false,minimumResultsForSearch:Infinity});
		
		$("#birth_region").val(taxBirthRegion);
		$("#birth_region").change();
		$("#birth_region").select2({allowClear: false,minimumResultsForSearch:Infinity});
		
		$("#birth_address").val(taxBirthAddress);
		
	}
	if(jsonArrayData.length <= 0){
		$("#taxResidentType").change();
	}
	$(".risk-add-list").unbind('scroll');
	$(".risk-add-list").bind('scroll');
}

/**
 * 出生地选择框显示
 */
function birthplaceDiv(){
	$(".birthplace").show();
	$("#birth_nation").val("");
	$("#birth_region").val("");
	$(".birth_address").val("");
	$("#birth_nation").val("").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#birth_region").val("").select2({allowClear: false,minimumResultsForSearch:Infinity});
}

/**
 * 出生地国家发生变化时事件
 */
function changeNation(){
	var birth_nation = $("#birth_nation").val();
	var defOption = '<option value="">请选择</option>';
	if(birth_nation == "1"){
		var option = "<option value='1'>中国</option>";
		$("#birth_region").html(defOption+option);
	}else if(birth_nation == "2"){
		var option = "<option value='1'>香港</option>";
		$("#birth_region").html(defOption+option);
	}else if(birth_nation == "3"){
		var option = "<option value='1'>澳门</option>";
		$("#birth_region").html(defOption+option);
	}else if(birth_nation == "4"){
		var option = "<option value='1'>台湾</option>";
		$("#birth_region").html(defOption+option);
	}else if(birth_nation == "5"){
		var option = "";
		var data = queryParamList("","NATION","");
		for (var i = 0; i < data.length; i++) {
			var temp = data[i];
			if(temp.pmco == "156"){
				continue;
			}
			option+="<option value = '"+temp.pmco+"'>"+temp.pmnm+"</option>";
		}
		$("#birth_region").html(defOption+option);
	}else{
		$("#birth_region").html(defOption);
	}
	$("#birth_region").val("");
	$("#birth_region").change();
	$(".birth_region").select2({allowClear: false,minimumResultsForSearch:Infinity});
}

/**
 * 显示风险评测历史记录
 */
function showHistory(){
	$("#risk_history_bg").show();
	$.ajax({
        async: !1,
        url: "/AppService/business/queryUserRiskHistory.xhtml",
        dataType: "json",
        cache: !1,
        type: "POST",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(data) {
        	if (!!data) {
        		var jsonArrayData = data;
        		var html = "";
        		for (var i = 0; i < jsonArrayData.length; i++) {
        			var riskData = jsonArrayData[i];
        			html+='<tr class="risk_tr">';
        			html+='<td class="risk_td1">'+riskData.riskEvalDate+'</td>';
        			html+='<td class="risk_td2">'+riskData.riskLevel+'</td></tr>';
        			$("#risk_h_data").html(html);
        		}
        	}
        }
    });
	
	
	$(".close_risk_history_bg").unbind("click");
	$(".close_risk_history_bg").click(function(){
		$("#risk_history_bg").hide();
    });
}

/**
 * 查询税收居民国
 */
function showTaxNationArea(){
	var nationList = queryParamList("","NATION","");
	var target = $("#taxArea_temp select");
	for( var i = 0 ; i < nationList.length ; i ++ ){
		var nation = nationList[i];
		var pmco = nation.pmco;
		var pmnm = nation.pmnm;
		if("156" == pmco){
			continue;
		}
		var option = "<option value=\""+pmco+"\">"+pmnm+"</option>";
		target.append(option);
	}
}

/**
 * 税收居民国 发生改变时
 */
function taxNationalityChange(element){
	var value = element.value;
	var taxAreaTempHtml = $("#taxArea_temp").html();
	$(element).parent().next().html(taxAreaTempHtml);
	var targetNode = $(element).parent().next().find("select[name=taxArea]");
	if(!value){
		targetNode.find("option[value!='']").remove();
	}
	
	if(!!value && "5" != value){
		targetNode.find("option[value!=156-"+value+"]option[value!='']").remove();
	}
	
	if(!!value && "5" == value){
		targetNode.find("option[value=156-1]option[value!='']").remove();
		targetNode.find("option[value=156-2]option[value!='']").remove();
		targetNode.find("option[value=156-3]option[value!='']").remove();
		targetNode.find("option[value=156-4]option[value!='']").remove();
	}
	targetNode.select2({allowClear: false,minimumResultsForSearch:Infinity});
}

function initTaxArea(targetId,taxNationality){
	var taxAreaTempHtml = $("#taxArea_temp").html();
	var targetNode = $("#"+ targetId +" select[name=taxArea]");
	targetNode.parent().html(taxAreaTempHtml);
	targetNode = $("#"+ targetId +" select[name=taxArea]");
	if(!taxNationality){
		targetNode.find("option[value!='']").remove();
	}
	
	if(!!taxNationality && "5" != taxNationality){
		targetNode.find("option[value!=156-"+taxNationality+"]option[value!='']").remove();
	}
	
	if(!!taxNationality && "5" == taxNationality){
		targetNode.find("option[value=156-1]option[value!='']").remove();
		targetNode.find("option[value=156-2]option[value!='']").remove();
		targetNode.find("option[value=156-3]option[value!='']").remove();
		targetNode.find("option[value=156-4]option[value!='']").remove();
	}
	targetNode.select2({allowClear: false,minimumResultsForSearch:Infinity});
}


//专业转普通 最低年龄限制 民事行为能力 只能选 否
var mayAgeToDay = 16;
function queryBirthDateByInvtpSwitch(){
	$(".w_text").val("");
	//出生日期
	var thisVal = $("#birthDateFrm").val().replace(/-/g,"");
	var nowDate= new Date();
	var nowYear =nowDate.getFullYear();
	var nowMonth = nowDate.getMonth() + 1;
	var nowDate = nowDate.getDate();
	if(nowMonth < 10){
		nowMonth = "0" + nowMonth;
	}
	if(nowDate < 10){
		nowDate = "0" + nowDate;
	}
	
	var limitAge = (parseInt(nowYear)-parseInt(mayAgeToDay)) + "" + nowMonth + nowDate;
	
	var dataDiff = daysBetween(limitAge,thisVal);
	//时间差
	if(dataDiff < 0 || !thisVal || isNaN(dataDiff)){
		$(".common-box-context ul li:eq(16) span.list:eq(1) div p").html("年龄不满16岁，不具有完全民事行为能力。<a href='/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO'>请根据实际情况填写出生日期。</a>");
		$(".common-box-context ul li:eq(16) span.list:eq(0)").unbind("click");
		$(".common-box-context ul li:eq(16) span.list:eq(1)").bind("click");
		$(".common-box-context ul li:eq(16) span.list:eq(1)").click();
	}else{
		$(".common-box-context ul li:eq(16) span.list").removeClass("act");
		$(".common-box-context ul li:eq(16) span.list").bind("click",function(e){optionBindClickEvent(e)});
	}
}

function subjoinChange(element){
	var target = $(element);
	target.parent().click();
}