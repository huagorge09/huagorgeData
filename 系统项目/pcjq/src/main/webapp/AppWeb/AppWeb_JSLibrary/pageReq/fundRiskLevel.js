var TAX_ADD_ID = 1;
var t_delay;
$(document).ready(function(e) {
	queryUserBaseInfo();
	var flag = checkIsRiskLevel();
	//校验 通过则 不加载如下
	if(flag){
		return;
	}
	$("#taxResidentType").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("select[name=birthNation]").select2({allowClear: false,minimumResultsForSearch:Infinity});
	/* 风险测评  选项点击控制 */
	$(".risk-con-list ul li span.list").click(function(e){optionBindClickEvent(e)});
	$("input[name='dateOfBirth']").change();
	
	queryUserTaxInfo();
	showTaxNationArea();
	showAddress();
	showProfession();
	showUserTaxInfo();
});

function optionBindClickEvent(e){
	$(e.target).removeClass("act").siblings().removeClass("act");
	$(e.target).addClass("act");
	var dataInput = $(e.target).attr("data-input");
	if("Y"==dataInput || !!dataInput){
		$(e.target).parent().find("div").show();
	}else{
		$(e.target).parent().find("div").find("input").val("");
		$(e.target).parent().find("div").hide();
	}
}
/*重置选项*/
function resetOption(){
	$(".risk-con-list ul li span.list").removeClass("act");
	$(".risk-con-list ul li span.list div input").val("");
	$(".risk-con-list ul li span.list div").hide();
	/*$('#submitRiskBtn').unbind("click");
	$('#submitRiskBtn').removeClass("click");*/
	$("#questionContent").scrollTop(0);
}
/*传索引值查询对应选中的值--data-score*/
function getThisVal(index){
	return  $(".risk-con-list ul li:eq("+index+") span.act").attr("data-score");
}

/*传索引值查询对应选中的值--data-value*/
function getThisValDesc(index){
	var value= $(".risk-con-list ul li:eq("+index+") span.act").attr("data-value");
	if(!!value){
		return  ":"+value;
	}
	return ":";
}

/*传索引值查询对应输入的值-*/
function getThisInputVal(index){
	var value = $(".risk-con-list ul li:eq("+index+") span.act input").val();
	if(!!value){
		return  ":"+value;
	}
	return ":";
}

var QUESTION_COUNT = 18;

function check(){
	///答案的选项数
	var len = $(".risk-con-list ul li span.list.act").length;
	var valiFlag = false;
	
	if (len!=null && len==QUESTION_COUNT) {
		valiFlag = true;
	}else{
		valiFlag = false;
	}
	
	/* 国籍等信息为空 */
	var nationality = $("select[name=nationality]").val();
	var province = $("select[name=province]").val();
	var city = $("select[name=city]").val();
	var profId = $("select[name=profId]").val();
	var provinceDiv = $("div[class*=province]").is(":hidden");
	var cityDiv = $("div[class*=city]").is(":hidden");
	var dateOfBirth = $("input[name=dateOfBirth]:eq(0)").val();
	var fundRiskDeclConfirm = document.getElementById("fundRiskDeclConfirm").checked;
	
	if(!!nationality && valiFlag){
		//国籍不为空才校验 省市
		valiFlag = true;
		//不隐藏 且前面无错
		if(!provinceDiv && valiFlag){
			//值不为空
			if(!!province && valiFlag){
				valiFlag = true;
			}else{
				valiFlag = false;
			}
		}
		
		if(!cityDiv && valiFlag){
			if(!!city && valiFlag){
				valiFlag = true;
			}else{
				valiFlag = false;
			}
		}
	}else{
		valiFlag = false;
	}
	//职业
	if(!!profId && valiFlag){
		valiFlag = true;
	}else{
		valiFlag = false;
	}
	//出生日期
	if(valiFlag && !dateOfBirth){
		valiFlag = true;
	}else{
		valiFlag = false;
	}
	//税收居民信息是否完整
	if(valiFlag && checkTaxResidentData()){
		valiFlag = true;
	}else{
		valiFlag = false;
	}
	//勾选声明
	if(valiFlag && fundRiskDeclConfirm){
		valiFlag = true;
	}else{
		valiFlag = false;
	}
	
	if(valiFlag){
		$('#submitRiskBtn').bind("click",function(){riskLevel();});
		$('#submitRiskBtn').addClass("click");
	}else{
		$('#submitRiskBtn').unbind("click");
		$('#submitRiskBtn').removeClass("click");
	}
}

/*风险测评提交*/
function riskLevel(){
	var len = $(".risk-con-list ul li span.list.act").length;
	var evalAnswer = 0;/* 临时变量*/
	var val = 0;/* 相加的值*/
	
	/*国籍等信息不能为空校验*/
	var nationality = $("select[name=nationality]").val();
	var province = $("select[name=province]").val();
	var city = $("select[name=city]").val();
	var detailAddress = $("input[name=detailAddress]").val();
	var profId = $("select[name=profId]").val();
	var provinceDiv = $("div[class*=province]").is(":hidden");
	var cityDiv = $("div[class*=city]").is(":hidden");
	var dateOfBirth = $("input[name=dateOfBirth]:eq(0)").val();
	var taxResidentType = $("select[name=taxResidentType]:eq(0)").val();
	var fundRiskDeclConfirm = document.getElementById("fundRiskDeclConfirm").checked;
	var birthAddress = $("input[name=birthAddress]:eq(0)").val();
	
	var isControl = $(".risk-con-list ul li:eq(13) span.act input").val();
	var isNotBeneficiary = $(".risk-con-list ul li:eq(14) span.act input").val();
	var isBadHonesty = $(".risk-con-list ul li:eq(15) span.act input").val();
	
	if(!profId){
		show_tips("请选择职业！");
		return ;
	}
	
	//出生日期
	if(!dateOfBirth){
		show_tips("请填写出生日期！");
		return ;
	}
	
	if(!!nationality){
		//国籍不为空才校验 省市
		//不隐藏 且前面无错
		if(!provinceDiv && !province){
			show_tips("请选择省份！");
			return ;
		}
		
		if(!cityDiv && !city){
			show_tips("请选择城市！");
			return ;
		};
	}else{
		show_tips("请选择国籍！");
		return ;
	}
	
	var taxResidentType = $("#taxResidentType").val()
	if(taxResidentType == "2" || taxResidentType == "3"){
		var englishSurname = $("#englishSurname").val();
		var englishName = $("#englishName").val();
		
		if(englishSurname == ""){
			show_tips("请填写Last Name！");
			return;
		}
		if(englishName == ""){
			show_tips("请填写First Name！");
			return;
		}
		
		var re=/^[\u2E80-\u9FFF]+$/;
		if(re.test(englishSurname)){
			show_tips("Last Name只能填写英文或拼音！");
			return ;
		}
		if(re.test(englishName)){
			show_tips("First Name只能填写英文或拼音！");
			return ;
		}

		var reside_nation = $("#reside_nation").val();
		if(reside_nation == ""){
			show_tips("请选择现居国家！");
			return ;
		}
		var reside_region = $("#reside_region").val();
		if(reside_region == ""){
			show_tips("请选择现居地区！");
			return ;
		}
		var reside_address = $("#reside_address").val();
		var reside_address_english = $("#reside_address_english").val();
		if(reside_nation != '5'){
			if(reside_address == ""){
				show_tips("请填写现居地址！");
				return ;
			}
		}
		if(reside_address_english == ""){
			show_tips("请填写Present Address！");
			return ;
		}

		if(re.test(reside_address_english)){
			show_tips("Present Address请填写英文或拼音地址！");
			return ;
		}
	}
	
	if(!checkTaxResidentData() || !taxResidentType){
		show_tips("请完善税收居民信息！");
		return ;
	}
	
	if("1" !=taxResidentType){
		if(!!birthAddress && birthAddress.length < 5){
			show_tips("出生地详细地址不能少于五个字！");
			return;
		}
	}
	
	if(undefined != isControl && !isControl){
		show_tips("请说明实际控制人！");
		return ;
	}
	
	if(undefined != isNotBeneficiary && !isNotBeneficiary){
		show_tips("请说明实际受益人！");
		return ;
	}
	
	if(undefined != isBadHonesty && !isBadHonesty){
		show_tips("请说明情况，如信用卡逾期还款！");
		return ;
	}
	
	//勾选声明
	if(!fundRiskDeclConfirm){
		show_tips("请阅读并勾选同意声明！");
		return ;
	}
	
	
	if(len != null && len == QUESTION_COUNT){
		/* 提交*/
		for(var i = 0; i < len; i++){
			evalAnswer = evalAnswer != "" ? evalAnswer + ',' + parseInt(getThisVal(i),10) + getThisValDesc(i) + getThisInputVal(i) : parseInt(getThisVal(i),10) + getThisValDesc(i) + getThisInputVal(i);
			val = parseInt(getThisVal(i),10) + parseInt(val,10);
		}
		
        var riskLevel = "";	/*等级：风险级别 1-保守型,2-稳健型,3-平衡型,4-C4-成长型,5-C5-积极型  */
        var channelCode = "NET";		/*渠道代码 NET-网站，IVR-自动语音，DIR-直销  (网上交易填写 NET)*/ 
        var evalType = "M";	/*测评类型 T-问卷测评，M-客户自评,网上交易填写 M*/
        var evalFormno = val;		/*填写用户分数*/
        var status = "N";		/*填写N*/
        //请问您是否具有完全民事行为能力
        var isCompAbility = $(".risk-con-list ul li:eq(16) span.act").attr("data-value");
        //请问您是否没有风险容忍度或者不愿承受任何投资损失
        var isEndureLoss = $(".risk-con-list ul li:eq(17) span.act").attr("data-value");
        
        if("N" == isCompAbility || "Y" == isEndureLoss){
        	val = 0;
        	evalFormno = val;
        }
        
        /*(20-40:1;41-60:2;61-100:3)
         * 20170627:(24-34:1,35-48:2,49-62:3,63-80:4,80-Max:5)
         * */
        if(val >= 24 && val <=34){
        	riskLevel = "1";
        }else if(val >= 35 && val <=48){
        	riskLevel = "2";
        }else if(val >= 49 && val <=62){
        	riskLevel = "3";
        }else if(val >= 63 && val <=80){
        	riskLevel = "4";
        }else if(val >= 81){
        	riskLevel = "5";
        }else{
        	riskLevel = "1";
        }
        
        //获取 税收居民信息
        var taxResidentData = encodeURI(getTaxResidentData());
		$.ajax({
			type: "POST",
        	async:false,
            url: "/AppService/business/setUserRiskLevel.xhtml",
            dataType: "json",
            data: {
                "riskLevel":riskLevel,		/*等级：风险级别 1-保守型,2-稳健型,3-平衡型,4-C4-成长型,5-C5-积极型*/
                "channelCode":channelCode,		/*渠道代码 NET-网站，IVR-自动语音，DIR-直销  (网上交易填写 NET)*/ 
                "evalType":evalType,		/*测评类型 T-问卷测评，M-客户自评,网上交易填写 M*/
                "evalFormno":	evalFormno,		/*填写用户分数*/
                "status":	status,			/*填写N*/
                "evalAnswer": evalAnswer,
                "nationality":nationality,
                "province":province,
                "city":city,
                "detailAddress":detailAddress,
                "profId":profId,
                "dateOfBirth" : dateOfBirth,
                "taxResidentType" : taxResidentType,
                "taxResidentData" : taxResidentData
                
            },
            cache: false,
            error : function(textStatus, errorThrown) {  
                show_tips("网络繁忙，请稍后再试。");  
            }, 
            success : function (data) {
            	var temp = "";
            	if(riskLevel == "1"){
            		temp = "C1-保守型";
            	}else if(riskLevel == "2"){
            		temp = "C2-稳健型";
            	}else if(riskLevel == "3"){
            		temp = "C3-平衡型";
            	}else if(riskLevel == "4"){
            		temp = "C4-成长型";
            	}else if(riskLevel == "5"){
            		temp = "C5-积极型";
            	}else{
            		temp = "C1-保守型";
            	}
            	if(data != null && data.returnCode == "0000"){
            		var riskLevelName,bearAbility,evalDispDateTime,evalValiDate,custRiskLevel,fundRiskLevel;
        			riskLevelName = temp;
        			
            		if(!!data.bearAbility){
            			bearAbility = data.bearAbility;
            		}
            		if(!!data.evalDispDateTime){
            			evalDispDateTime = data.evalDispDateTime;
            		}
            		if(!!data.evalValiDate){
            			evalValiDate = data.evalValiDate;
            		}
            		if(!!data.custRiskLevel){
            			custRiskLevel = data.custRiskLevel;
            		}
            		if(!!data.fundRiskLevel){
            			fundRiskLevel = data.fundRiskLevel;
            		}
            		
            		riskLevel_again1();
            		//showRiskLevelResult(riskLevelName,bearAbility,evalDispDateTime,evalValiDate,custRiskLevel,fundRiskLevel);
            		/*$("#riskLever_score").html(data.userRiskLevelDto.evalFormno+"<i>分</i>");
            		$("#riskLever_custLever").html(temp + "客户");
            		$("#risk_div_01").hide();
            		$("#risk_div_02").show();
            		$('.quesiton-bottom a').hide();
            		$('.cover_bg .top').html('风险测评完成');*/
					
					$("#riskLevelName").html(riskLevelName);
					$("#bearAbility").html(bearAbility);
					$("#evalDispDateTime").html(evalDispDateTime);
					$("#evalValiDate").html(evalValiDate);
					$("#custRiskLevel").html(custRiskLevel);
					$("#fundRiskLevel").html(fundRiskLevel);
					$("div[class=riskContent]:eq(0)").hide();
					$("div[class=riskResult]:eq(0)").show();
	//var flag = prodSwitch();
	//if(flag == false){
					t_delay = setInterval (function(){
						queryUserIsRealName();
					}, 5000);
					
            		operatingRecord(pageSourceId,pageId,"event_003_commitQuestionId","");
            		scroll(0,0);/* 回到顶部*/
            	}else{
            		showRiskLevelResult("评级失败："+data.returnMsg,"","","","");
//            		$("#risk_div_01").hide();
            		scroll(0,0);/* 回到顶部*/
            	}
            }
		});
	}else{
		show_tips("请答完题再提交！");
	}
}

/**
 * 获取用户是否实名
 */
function queryUserIsRealName(){
	$.ajax({
		async : false,
		url : "/AppService/business/queryUserinfo.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode != null && data.returnCode == "0000") {
				//是否实名
				var userType = data.userType;
				if(userType == null || userType == '10'){
					$("#isToRealName a").attr("href","/AppService/business/bank/realName.shtml?pageSourceId="+pageSourceId+"&eventId=event_user_realNameId");
					$("#isToRealName").show();
					$(".result-bottom .result-bottom-left").remove();
					$(".result-bottom .result-bottom-right").css('margin-left',"130px");
					t_delay = setInterval (function(){
						$("#isToRealName .result-bottom-left a")[0].click();
                	}, 5000);
				}else{
					var riskUrl = getCookie("riskUrl");
					if(!riskUrl){
						riskUrl = "/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO";
					}
					addCookie('riskUrl','');
					t_delay = setInterval (function(){
						window.location.href=riskUrl
					}, 3000);
				}
			}
		}
	});
}
function detailTips(){
	clearInterval(t_delay);
	$("#detailText").show();
	pageEventData("01","02");
}
function closeDetailText(){
	$("#detailText").hide();
	pageEventData("01","03");
	queryUserIsRealName();
}

/*重新评测*/
function riskLevel_again(){
	resetOption();
	$("div[class=riskResult]:eq(0)").hide();
	$("div[class=riskContent]:eq(0)").show();
	$('.cover_bg .top').html('请进行风险测评');
//	$("#risk_div_01").show();
//	$("#risk_div_02").hide();
//	$("#risk_div_03").hide();
	$('.quesiton-bottom a').show();
	clearInterval(t_delay);
	document.getElementById("fundRiskDeclConfirm").checked = false;
	operatingRecord(pageSourceId,pageId,"event_003_reassessId","");
	$("#questionContent").scrollTop(0);/* 回到顶部*/
}

/*重新评测*/
function riskLevel_again1(){
	resetOption();
	$("div[class=riskResult]:eq(0)").hide();
	$("div[class=riskContent]:eq(0)").show();
	$('.cover_bg .top').html('请进行风险测评');
//	$("#risk_div_01").show();
//	$("#risk_div_02").hide();
//	$("#risk_div_03").hide();
	$('.quesiton-bottom a').show();
	clearInterval(t_delay);
	document.getElementById("fundRiskDeclConfirm").checked = false;
	$("#questionContent").scrollTop(0);/* 回到顶部*/
}

function showAddress(){
	$('select.select').ui_select();
	$("#nationality").parent().parent().html('<select class="select" id="nationality" name="nationality" style="width:170px;"></select>');
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
			showSelect(value,true);
//			check();
		}
	});
	
//	$("#div_cover_bg").show();
	showSelect("",false);
}

function showSelect(value,type){
	var nation = $("input[name='nation']").val();
	var province = $("input[name='province']").val();
	var city = $("input[name='city']").val();
	var addr = $("input[name='addr']").val();
	if(type){
		$("#detailAddress").val("");
		$(".error_msg").text("");
		if(value == "" || value == null){
			$("#province").parent().parent().html('<select class="select" id="province" name="province" style="width: 170px;"></select>');
			$(".province").show();
			$(".city").show();
			var str = "<option value=''>请选择</option>";
		    $("#province").html(str);
		    $('#province').ui_select();
		    $("#city").parent().parent().html('<select class="select" id="city" name="city" style="width: 170px;"></select>');
			$("#city").parent().parent().show();
			var str = "<option value=''>请选择</option>";
		    $("#city").html(str);
			$('#city').ui_select();
		}else{
			$("#province").parent().parent().html('<select class="select" id="province" name="province" style="width:170px;"></select>');
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
			    		$(".error_msg").text("");
			    		var str = "";
						str+="<option value=''>请选择</option>";
						var cityData = "";
			    		if(!!value){
			    			cityData = queryParamList("SYSTEM","DS_CITYCODE",value)
			    			for (var i = 0; i < cityData.length; i++) {
								var temp = cityData[i];
								str+="<option value = '"+temp.pmco+"'>"+temp.pmnm+"</option>";
							}
			    		}
			    		if(cityData.length <= 1){
			    			$("#city").parent().parent().hide();
			    		}else{
			    			$("#city").parent().parent().show();
			    			$("#city").parent().parent().html('<select class="select" id="city" name="city" style="width:170px;"></select>');
			    		}
						
						
					    $("#city").html(str);
//					    check();
						$('#city').ui_select({
					    	onChange:function (value, item) {
					    		$(".error_msg").text("");
//					    		check();
					    	}
					    });
			    	}
			    });
			    $("#city").parent().parent().html('<select class="select" id="city" name="city" style="width:170px;"></select>');
				$("#city").parent().parent().show();
				var str = "<option value=''>请选择</option>";
			    $("#city").html(str);
				$('#city').ui_select({
					onChange:function (value, item) {
//			    		check();
			    	}
				});
			}else{
				$("#province").html("");
				$("#city").html("");
				$(".province").hide();
				$(".city").hide();
				$('#province').ui_select({
					onChange:function (value, item) {
//			    		check();
			    	}
				});
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
			    $('#province').ui_select({
					onChange:function (value, item) {
//			    		check();
			    	}
				});
			}else{
				$("#province").html("");
				$("#city").html("");
				$(".province").hide();
				$(".city").hide();
				$('#province').ui_select({
					onChange:function (value, item) {
//			    		check();
			    	}
				});
			}
		}
		
		$(".error_msg").text("");
		$("#province").parent().parent().html('<select class="select" id="province" name="province" style="width:170px;"></select>');
		$(".province").show();
		$(".city").show();
		var str = "<option value=''>请选择</option>";
	    $("#province").html(str);
	    $('#province').ui_select({
			onChange:function (value, item) {
//	    		check();
	    	}
		});
	    $("#city").parent().parent().html('<select class="select" id="city" name="city" style="width:170px;"></select>');
		$("#city").parent().parent().show();
		var str = "<option value=''>请选择</option>";
	    $("#city").html(str);
		$('#city').ui_select({
			onChange:function (value, item) {
//	    		check();
	    	}
		});
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

function showNationality(){
	var natData = queryParamList("DS","DS_NATION","");
	var nationSelect = $("#nationality");
	if(null != natData && natData.length > 0){
		$.each(natData,function(index,data){
			var option = "<option value=\""+data.pmco+"\">"+data.pmnm+"</option>";
			nationSelect.append(option);
		});
	}
	nationSelect.ui_select({
    	onChange:function (value, item) {
    		showProvince(value);
    	}
    });
}

function showProvince(value){
	var provinceData = queryParamList("SYSTEM","DS_PROVINCE",value);
	var provinceSelect = $("#province");
	if(null != provinceData && provinceData.length > 0){
		$.each(provinceData,function(index,data){
			var option = "<option value=\""+data.pmco+"\">"+data.pmnm+"</option>";
			provinceSelect.append(option);
		});
	}
	provinceSelect.ui_select({
    	onChange:function (value, item) {
    		showCity(value);
    	}
    });
}

function showCity(value){
	var cityData = queryParamList("SYSTEM","DS_CITYCODE",value);
	var citySelect = $("#city");
	if(null != cityData && cityData.length > 0){
		$.each(cityData,function(index,data){
			var option = "<option value=\""+data.pmco+"\">"+data.pmnm+"</option>";
			citySelect.append(option);
		});
	}
	citySelect.ui_select();
}

function showProfession(){
	var profData = queryParamList("SYSTEM","VOCCODE","");
	var profId  = $("input[name=profession]").val();
	if(null != profData && profData.length > 0){
		var profSelect = $("#profId");
		$.each(profData,function(index,data){
			var option = "<option value=\""+data.pmco+"\">"+data.pmnm+"</option>";
			profSelect.append(option);
		});
	}
	$("#profId").ui_select({
		onChange:function (value, item) {
//    		check();
    	}
	});
	$('#profId').data('ui-select').val(""+profId+"");
}


function queryUserBaseInfo(){
	$.ajax({
        async: !1,
        url: "/AppService/business/queryUserinfo.xhtml",
        data: "",
        dataType: "json",
        cache: !1,
        type: "post",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {
            if (n.returnCode != null && n.returnCode == "0000") {
            	var syncInvprtpAlert = n.syncInvprtpAlert;
            	if("Y" == syncInvprtpAlert){
            		showAlertInfoByTargetId('hint_risk_update');
            	}
                var address = "";
                if(n.nationNM != "" && n.nationNM != null){
                	address+=n.nationNM;
        		}
        		if(n.provinceNM != "" && n.provinceNM != null){
        			address+="-"+n.provinceNM;
        		}
        		if(n.cityNM != "" && n.cityNM != null){
        			address+="-"+n.cityNM;
        		}
        		if(n.addr != "" && n.addr != null){
        			address+="-"+n.addr;
        		}
                $("#address").html(address);
                $("input[name='nation']").val(n.nation);
            	$("input[name='province']").val(n.province);
            	$("input[name='city']").val(n.city);
            	$("input[name='addr']").val(n.addr);
            	$("input[name='profession']").val(n.vocCode);
            	$("input[name='dateOfBirth']").val(n.birthDate);
            	$("input[name='taxResidentType']").val(n.taxResidentType);
            	$("select[name='taxResidentType']:eq(0)").val(n.taxResidentType);
            	$("#taxResidentType").select2({allowClear: false,minimumResultsForSearch:Infinity});
            }
        }
    });
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

function showUserTaxInfo(){
	var taxResidentType = $("input[name='taxResidentType']").val();
	if(taxResidentType == '1' || taxResidentType == ''){
		$(".hiddenDIv").css("height","1px");
	}else{
		$(".hiddenDIv").css("height","205px");
	}
	//仅为大陆
	if("1" == taxResidentType || !taxResidentType){
		$("#tax_birthNation_bg").hide();
		$(".resideadd").hide();
		$(".englishNameDiv").hide();
		return;
	}else{
		$("#tax_birthNation_bg").show();
		$("select[name=birthNation]").change();
		$(".englishNameDiv").show();
		$(".resideadd").show();
	}
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
		var taxArea = taxResident.taxArea;
		var taxPayerCode = taxResident.taxPayerCode;
		var taxNotCodeCause = taxResident.taxNotCodeCause;
		var taxnotGetCause = taxResident.taxnotGetCause;
		var taxBirthNation = taxResident.taxBirthNation;
		var taxBirthRegion = taxResident.taxBirthRegion;
		var taxBirthAddress = taxResident.taxBirthAddress;
		
		var taxResideNation = taxResident.taxResideNation;
		var taxResideRegion = taxResident.taxResideRegion;
		var taxResideAddress = taxResident.taxResideAddress;
		var taxResideAddressEnglish = taxResident.taxResideAddressEnglish;
		
		var englishSurname = taxResident.englishSurname;
		var englishName = taxResident.englishName;
		var custName = taxResident.custName;
		
		//初始化 选项框
		initTaxNationality(targetId,taxResidentType,taxNationality);
		initTaxArea(targetId,taxNationality);
		
		$("#" + targetId + " select[name=taxNationality]:eq(0)").val(taxNationality);
		$("#" + targetId + " select[name=taxArea]:eq(0)").val(taxArea);
		$("#" + targetId + " input[name=taxpayerCode]:eq(0)").val(taxPayerCode);
		
		$("select[name=birthNation]:eq(0)").val(taxBirthNation).change();
		$("select[name=birthArea]:eq(0)").val(taxBirthRegion);
		$("input[name=birthAddress]:eq(0)").val(taxBirthAddress);
		if(!taxPayerCode){
			$("#" + targetId + " input[name=isTaxpayerCode]:eq(0)").click();
			$("#" + targetId + " input[name=isTaxpayerCode]:eq(0)").attr("checked","checked");
			$("#" + targetId + " select[name=notCodeCause]:eq(0)").val(taxNotCodeCause);
			$("#" + targetId + " select[name=notCodeCause]:eq(0)").change();
			$("#" + targetId + " input[name=notGetCause]:eq(0)").val(taxnotGetCause);
		}
		
		$("select[name=birthNation]:eq(0)").select2({allowClear: false,minimumResultsForSearch:Infinity});
		$("select[name=birthArea]:eq(0)").select2({allowClear: false,minimumResultsForSearch:Infinity});
		$("#"+targetId+" select[class*=select2]").select2({allowClear: false,minimumResultsForSearch:Infinity});
		if(!!taxResidentType && "2" == taxResidentType){
			$("#"+targetId+" img").remove();
		}
		
		//现居住地数据加载
		$("#reside_nation").val(taxResideNation);
		$("#reside_nation").change();
		$("#reside_nation").select2({allowClear: false,minimumResultsForSearch:Infinity});
		
		$("#reside_region").val(taxResideRegion);
		$("#reside_region").change();
		$("#reside_region").select2({allowClear: false,minimumResultsForSearch:Infinity});
		
		$("#reside_address").val(taxResideAddress);
		$("#reside_address_english").val(taxResideAddressEnglish);
		
		//英文姓和英文名加载
		$("#englishSurname").val(englishSurname);
		$("#englishName").val(englishName);
	}
	//查询用户中文姓名
	queryUserName();
}

/**
 * 查询用户中文姓名
 */
function queryUserName() {
	$.ajax({
		async : false,
		url : "/AppService/business/queryUserinfo.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode != null && data.returnCode == "0000") {
				$("#userCustName").val(data.custName);
			}
		}
	});
}


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
	
	var targetNode = $("#"+targetId+" select[name=taxNationality]");
	
	//以下为 修改 按钮参数 和 初始化控件
	$("#"+targetId+" select[class=select2]").select2({allowClear: false,minimumResultsForSearch:Infinity});
	return targetId;
	
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
	
	var targetNode = $("#"+targetId+" select[name=taxNationality]");
	
	var taxNationTemp = $("#taxNation_temp").html();
	targetNode.html(taxNationTemp);
	targetNode.find("option[value=1]option[value!='']").remove();
	
	$("#"+targetId+" select[class=select2]").select2({allowClear: false,minimumResultsForSearch:Infinity});
//	$("#"+targetId+" select[name=taxNationality]:eq(0)").ui_select();
//	$("#"+targetId+" select[name=notCodeCause]:eq(0)").ui_select();
	return targetId;
}

function delTaxNation(elementId){
	$("#"+elementId).remove();
}

/**
 * 税收居民类型 发生改变时
 */
function taxResidentChange(element){
	var value = element.value;
	$("#questionContent").scrollTop(170);
	if(value == '1' || value == ''){
		$(".hiddenDIv").css("height","1px");
	}else{
		$(".hiddenDIv").css("height","125px");
	}
	//将税收居民国等内容 全部删除
	$("div[class=tax_add_div]div[id!=tax_template]").remove();
	if(!value || "1" == value){
		$("div[class*=tax_birthNation_bg]").hide();
		$(".resideadd").hide();
		$(".englishNameDiv").hide();
		$(".risk-con-list").css("margin-top","0");
		return;
	}
	$("div[class*=tax_birthNation_bg]").show();
	$("select[name=birthNation]").change();
	$(".risk-con-list").css("margin-top","125px");
	$(".resideadd").show();
	$(".englishNameDiv").show();
	
	var targetId = "";
	if(!!value && "1" != value){
		targetId = initTaxNation();
	}
	if(!!value && "2" == value){
		$("#"+targetId+" img").remove();
	}
	initTaxNationalityByChange(targetId,value);
	$("#reside_nation").val("").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#reside_region").val("").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$(".engName").val("");
	$("#tax_birthNation_bg .select2").val("");
	$("#tax_birthNation_bg .select2").change();
	$("#tax_birthNation_bg .select2").select2({allowClear: false,minimumResultsForSearch:Infinity});
	queryUserName();
}

function initTaxNationalityByChange(targetId,taxResidentType){
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
		$("#"+targetId+" img").remove();
		
		var newTargetId = addTaxNation();
		var newTargetNode = $("#"+newTargetId+" select[name=taxNationality]");
		newTargetNode.html(taxNationTemp);
		newTargetNode.find("option[value=1]option[value!='']").remove();
		$("#"+newTargetId+" img").attr("src","/AppWeb/AppWeb_Images/images/bt_add1.png").attr("onclick","addTaxNation()");
	}
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
			$("#"+targetId+" img").remove();
		}else{
			targetNode.find("option[value=1]option[value!='']").remove();
			$("#"+targetId+" img").attr("src","/AppWeb/AppWeb_Images/images/bt_add1.png").attr("onclick","addTaxNation()");
		}
	}
}
/*
function initTaxNationality(targetId,taxNationality){
	var value = taxNationality;
	var targetNode = $("#"+targetId+" select[name=taxNationality]");
	
	if(!!value && "5" != value){
		var taxNationTemp = $("#taxNation_temp").html();
		targetNode.html(taxNationTemp);
		targetNode.find("option[value!="+value+"]option[value!='']").remove();
	}
	
	if(!!value && "5" == value){
		var taxNationTemp = $("#taxNation_temp").html();
		targetNode.html(taxNationTemp);
		targetNode.find("option[value=1]option[value!='']").remove();
		targetNode.find("option[value=2]option[value!='']").remove();
		targetNode.find("option[value=3]option[value!='']").remove();
		targetNode.find("option[value=4]option[value!='']").remove();
	}
}*/


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
		target.parent().parent().parent().parent().next().show();
	}else{
		target.parent().prev().removeAttr("readonly");
		target.parent().prev().removeAttr("disabled");
		target.parent().parent().parent().parent().next().hide();
	}
}

function notCodeCauseEvent(e){
	var target = $(e);
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
	var birthNation = $("select[name=birthNation]:eq(0)");
	var birthArea = $("select[name=birthArea]:eq(0)");
	var birthAddress = $("input[name=birthAddress]:eq(0)");
	
	if(!taxResidentType.val()){
		flag = false;
	}
	
	//居民类型为 非居民 则 校验以下信息
	if(flag && "1" != taxResidentType.val()){
		if(flag && (!birthNation.val() || !birthArea.val()) ){
			flag = false;
			return flag ;
		}
		
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
function getTaxResidentData(){
	var taxResidentData = "";
	var taxResidentType = $("select[name=taxResidentType]:eq(0)");
	var birthNation = $("select[name=birthNation]:eq(0)");
	var birthArea = $("select[name=birthArea]:eq(0)");
	var birthAddress = $("input[name=birthAddress]:eq(0)");
	
	//现居国家
	var reside_nationVal = $("#reside_nation").val();
	var reside_regionVal = $("#reside_region").val();
	//现居地址及英文地址
	var reside_addressVal = $("#reside_address").val();
	var reside_address_englishVal = $("#reside_address_english").val();
	
	//英文姓和英文名
	var englishSurname = $("#englishSurname").val();
	var englishName = $("#englishName").val();
	
	//居民类型为 非居民 则 获取居民信息
	if("1" != taxResidentType.val()){
		var taxResList = $("div[class=tax_add_div]div[id!=tax_template]");
		var taxResValList = new Array();
		for (var i = 0; i < taxResList.length; i++) {
			var taxResVal = {};
			var taxRes = $(taxResList[i]);
			var taxNationality, taxArea,taxpayerCode, isTaxpayerCode, notCodeCause, notGetCause;
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
			
			taxResVal.taxBirthNation = birthNation.val();
			taxResVal.taxBirthRegion = birthArea.val();
			taxResVal.taxBirthAddress = birthAddress.val();
			
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
				taxResVal.taxnotGetCause = "";
				//如果填的是 未取得 则 未取得原因必填
				if("2" == notCodeCause.val()){
					taxResVal.taxnotGetCause = notGetCause.val();
				}
			}
			
			
			taxResVal.taxResideNation = reside_nationVal;
			taxResVal.taxResideRegion = reside_regionVal;
			taxResVal.taxResideAddress = reside_addressVal;
			taxResVal.taxResideAddressEnglish = reside_address_englishVal;
			
			
			taxResVal.englishSurname = englishSurname;
			taxResVal.englishName = englishName;
			
			taxResValList.push(taxResVal);
		}
		taxResidentData = JSON.stringify(taxResValList);
	}
	return taxResidentData;
}
//最低年龄限制 民事行为能力 只能选 否
var mayAgeToDay = 16;
function dateOfBirthChange(e){
	//出生日期
	var thisVal = e.value.replace(/-/g,"");
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
		$(".risk-con-list ul li:eq(16) span.list:eq(1) div p").html("年龄不满16岁，不具有完全民事行为能力。<a href='/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO'>请根据实际情况填写出生日期。</a>");
		$(".risk-con-list ul li:eq(16) span.list:eq(0)").unbind("click");
		$(".risk-con-list ul li:eq(16) span.list:eq(1)").bind("click");
		$(".risk-con-list ul li:eq(16) span.list:eq(1)").click();
	}else{
		$(".risk-con-list ul li:eq(16) span.list").removeClass("act");
		$(".risk-con-list ul li:eq(16) span.list").bind("click",function(e){optionBindClickEvent(e)});
		$(".risk-con-list ul li:eq(16) span.list div").hide();
		$(".risk-con-list ul li:eq(16) span.list:eq(1) div p").html("所填选项会被判定为风险承受能力最低类别投资者，请据实慎重选择。");
		$(".risk-con-list ul li:eq(16) span.list:eq(1) div p").removeAttr("title");
		$(".risk-con-list ul li:eq(16) span.list:eq(1) div").unbind("click");
	}
}

/**
 * 出生地国家发生变化时事件
 */
function changeBirthNation(e){
	var birth_nation = $(e).val();
	var targetBirthArea = $("select[name=birthArea]");
	var defOption = '<option value="">请选择</option>';
	if(birth_nation == "1"){
		var option = "<option value='1'>中国</option>";
		targetBirthArea.html(defOption+option);
	}else if(birth_nation == "2"){
		var option = "<option value='1'>香港</option>";
		targetBirthArea.html(defOption+option);
	}else if(birth_nation == "3"){
		var option = "<option value='1'>澳门</option>";
		targetBirthArea.html(defOption+option);
	}else if(birth_nation == "4"){
		var option = "<option value='1'>台湾</option>";
		targetBirthArea.html(defOption+option);
	}else if(birth_nation == "5"){
		var defOption = '<option value="">请选择</option>';
		var option = "";
		var data = queryParamList("","NATION","");
		for (var i = 0; i < data.length; i++) {
			var temp = data[i];
			if(temp.pmco == "156"){
				continue;
			}
			option+="<option value = '"+temp.pmco+"'>"+temp.pmnm+"</option>";
		}
		targetBirthArea.html(defOption+option);
	}else{
		var defOption = '<option value="">请选择</option>';
		targetBirthArea.html(defOption);
	}
	targetBirthArea.select2({allowClear: false,minimumResultsForSearch:Infinity});
}

/**
 * 居住地国家发生变化时事件
 */
function changeResideNation(){
	var reside_nation = $("#reside_nation").val();
	var defOption = '<option value="">请选择</option>';
	if(reside_nation == "1"){
		var option = "<option value='156-1'>中国</option>";
		$("#reside_region").html(defOption+option);
	}else if(reside_nation == "2"){
		var option = "<option value='156-2'>香港</option>";
		$("#reside_region").html(defOption+option);
	}else if(reside_nation == "3"){
		var option = "<option value='156-3'>澳门</option>";
		$("#reside_region").html(defOption+option);
	}else if(reside_nation == "4"){
		var option = "<option value='156-4'>台湾</option>";
		$("#reside_region").html(defOption+option);
	}else if(reside_nation == "5"){
		var option = "";
		var data = queryParamList("","NATION","");
		for (var i = 0; i < data.length; i++) {
			var temp = data[i];
			if(temp.pmco == "156"){
				continue;
			}
			option+="<option value = '"+temp.pmco+"'>"+temp.pmnm+"</option>";
		}
		$("#reside_region").html(defOption+option);
	}else{
		$("#reside_region").html(defOption);
	}
	$("#reside_region").val("");
	$("#reside_region").change();
	$("#reside_region").select2({allowClear: false,minimumResultsForSearch:Infinity});
}
