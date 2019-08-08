var PRIMARY_PATH = "";
var ACCOUNT_PATH = "";
var PROJEC_TPATH = "";

function setPath(projectPath,path,accountPath){
	PROJEC_TPATH = projectPath;
	PRIMARY_PATH = path;
	ACCOUNT_PATH = accountPath;
}

var doclist = $("#documentlist").val();
doclist = !!doclist ? eval(doclist) : "";

$(function(){
	$('.select2_width').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	
	initCategoryInfo();
	
	doSelect($("#docbusinesstpVal").val());
});

function doSubmit(checkst)
{
	var permissionId = $("#permissionId").val();
	var operatorId = $("#operatorId").val();
	var serialno = $("#serialno").val();
	
	ctools.confirm({title : "是否确认提交该笔申请？"},
	function(isConfirm){
		if(isConfirm){
			$.ajax({
				url: ACCOUNT_PATH+"accountInfoCheck.do",  
				dataType: "json",
				type: "POST",
				data:  {
					"sp[permissionId]" : permissionId,
					"sp[serialno]" : serialno,
					"sp[checkst]" : checkst,
					"sp[operatorId]" : operatorId,
					"sp[granteeId]" : operatorId
				},
				cache: false,
				async: true,
				success: function(data) {
					var errcode = data.errcode;
				    var errMsg =data.errmsg;
				    if(errcode == "0000"){
				    	ctools.alert_sweet('复核操作成功！', "success", "" , function(){
				    		window.opener.queryByCondtion(true);
				    		window.close();
						});
				    }else{
				    	ctools.alert_sweet('复核操作失败！', "error", "失败原因："+errMsg);
				    }
				}
			});
		}
	});
}

function doBack() {
	window.close();
}


function doSelect(docbusinesstp)
{
	if(!!docbusinesstp){
		$.ajax({
			type : "post",
			url : projectPath+"service/widget/queryMatchParamList.do?pmst=DS&pmky="+docbusinesstp,
			dataType : "json",
			contentType : 'application/json;charset=utf-8',
			success : function(data) {
				showDocument(data,docbusinesstp);
			},
			error : function() {
				ctools.alert("查询资料列表失败！","","error");
			}
		});
	}
}

function showDocument(object,docbusinesstp)
{
	$("#documentinfo>td:gt(0)").remove();
	var invtp = $("#invtp").val();
	var invprtp = $("#invprtpVal").val();
	var invProDocMap = {} ;
	invProDocMap.ap01 = "07";
	invProDocMap.ap02 = "23";
	var trstr = "<td class='white-bg' colspan='3' style='padding: 0px;'>";
		trstr += "<table style='border: 0;' class='table table-bordered maintable' id='documentTable'";
		trstr += "<colgroup><col width='100%'></colgroup>";
	for(var i=0;i<object.length;i++)
	{  	//个人开户
  	//不是 专业 遍历到  专业投资者资料时  跳过
		if ("0" != invprtp) {
			var invProDoc = invProDocMap.ap01 + "," + invProDocMap.ap02;
			var flag = invProDoc.indexOf(object[i].PMCO);
			if (flag >= 0) {
				continue;
			}
		} else {
			// 是专业投资者 普通业务 的材料跳过
			if ("1" == invtp) {
				var invProDoc = invProDocMap.ap02;
				var flag = invProDoc.indexOf(object[i].PMCO);
				if (flag >= 0) {
					continue;
				}
			} else {
				var invProDoc = invProDocMap.ap01;
				var flag = invProDoc.indexOf(object[i].PMCO);
				if (flag >= 0) {
					continue;
				}
			}
		}
		trstr+="<tr><td class='white-bg' style='border: 0;border-bottom: 1px solid #e2e2e2;'><div class='col-sm-10 form-inner'>";
		
		if(doclist != null && doclist.length>0){
			var flag = false;
   			for(var j=0;j<doclist.length;j++){
   				if(doclist[j].existsflag=='1' && doclist[j].custdocument==object[i].PMCO && docbusinesstp==doclist[j].businesstp){
   					flag = true;
   				}
   			}
   			if (flag) {
   				trstr+="<label><input class='i-checks' type='checkbox' checked id='"+object[i].PMCO+"' name='doccheck' value='"+object[i].PMCO+"' >"+object[i].PMNM+"</label>";
			}else{
				trstr+="<label><input class='i-checks' type='checkbox' id='"+object[i].PMCO+"' name='doccheck' value='"+object[i].PMCO+"' >"+object[i].PMNM+"</label>";
			}
		}else{
			trstr+="<label><input class='i-checks' type='checkbox' id='"+object[i].PMCO+"' name='doccheck' value='"+object[i].PMCO+"' >"+object[i].PMNM+"</label>";
		}
		trstr+="</div></td></tr>";
 }
 	trstr+="</table></td>";
	$("#documentinfo").append(trstr);               
	$("#documentTable").find("td:last").css("border-bottom",'0');
	$("#documentinfo input,select").attr("disabled",true);
	var isalldoc = $("#isalldoc").val();
	if(isalldoc == "1"){
		$("#documentinfo input[type='checkbox']").each(function(data) {
			$(this).prop("checked", true);// 全选
		});
	}
}

/**
 * 出生地、居住地、税收居民国籍触发事件
 */
function changeNation(ele) {
	var val = $(ele).val();
	var secondEle = $(ele).siblings("select");
	var defaultOption = '<option value="" selected="selected">请选择</option>';
	switch (val) {
	case "1":
		var options = "<option value='156-1'>中国</option>";
		secondEle.html(defaultOption + options);
		break;
	case "2":
		var options = "<option value='156-2'>香港</option>";
		secondEle.html(defaultOption + options);
		break;
	case "3":
		var options = "<option value='156-3'>澳门</option>";
		secondEle.html(defaultOption + options);
		break;
	case "4":
		var options = "<option value='156-4'>台湾</option>";
		secondEle.html(defaultOption + options);
		break;
	case "5":
		var options = $("#comNation").html();
		secondEle.html(defaultOption + options);
		break;
	default:
		secondEle.html(defaultOption);
	}
	secondEle.val("");
	$(secondEle).select2("destroy");
	$(secondEle).select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
}

/**
 * 记载税收居民信息
 */
function initCategoryInfo(){
	$("#residentType").hide();
	$("#controllerInfo").hide();
	var selOpenType = $("#invtp").val();
	initEnglishNameTr(selOpenType);
	getSexAndBirth();
	changeVal();
	var arrDate= $("#accountInfo").val();
	var obj=eval(arrDate);
	/**
	 * 分类信息显示逻辑的几种情况
	 * 1.中国税收居民，不显示非居民信息、不显示既是中国又是其他类型税收居民信息、不显示非居民控制人信息(当数组数据为空情况)
	 * 2.仅为居民信息，显示非居民信息信息，不显示既是中国又是其他类型税收居民，显示控制人信息(数据数据只有一条的情况，且控制人的信息字段不为空)
	 * 3.仅为居民信息，显示非居民信息信息，不显示既是中国又是其他类型税收居民信息，不显示控制人信息(数据数据只有一条的情况，且控制人的任意信息字段为空)
	 * 4.既是中国又是其他类型，不显示非居民信息信息，显示既是中国又是其他类型税收居民信息，显示控制人信息(数据信息两条以上，且控制人的信息字段不为空)
	 * 5.既是中国又是其他类型，不显示非居民信息信息，显示既是中国又是其他类型税收居民信息，不显示控制人信息(数据信息两条以上，且控制人的信息字段全部为空)
	 */
	if(arrDate!="[]"){
		residentCom();
		if(obj.length==1){   //仅为居民信息
		     residentOnlyOne();
		}else if(obj.length==2){ //既是中国又是其他类型
			 residentOnlyTwo();
			 $(".since select[name=taxNationality]").val(trim(obj[1].taxNationality));
	         $(".since select[name=taxNationality]").change();
	         $(".since select[name=taxArea]").val(trim(obj[1].taxArea));
	         $(".since select[name=taxArea]").change();
	         
		}else if(obj.length>2){
		 	 residentOnlyTwo();
		 	 $(".since select[name=taxNationality]").val(trim(obj[1].taxNationality));
	         $(".since select[name=taxNationality]").change();
	         $(".since select[name=taxArea]").val(trim(obj[1].taxArea));
	         $(".since select[name=taxArea]").change();
		 	 residentThree();
		}
		if(obj[0].chineseName2){  //如果有控制人
	     	controlVal();
	    }
	}else{
		$("select[name=taxType]").find("option[value=1]").attr("selected",true);
	}
	
	/*
	 * 居民基础信息
	 */
	function residentCom(){
		$("#residentType").show();
		$("input[name=firstName]").val(obj[0].firstName); //firstName
		$("input[name=englishName]").val(obj[0].englishName); //englishName
		$("select[name=sex]").find("option[value="+obj[0].sex+"]").attr("selected",true);
		$("input[name=birthDate]").val(obj[0].birthDate);
		$("select[name=birth_nation]").find("option[value="+obj[0].taxBirthNation+"]").attr("selected",true);
		$("select[name=birth_nation]").change();
		$("select[name=birth_region]").find("option[value="+obj[0].taxBirthRegion+"]").attr("selected",true);
		$("input[name=birth_address]").val(obj[0].taxBirthAddress);
		$("select[name=reside_nation]").find("option[value="+obj[0].taxResideNation+"]").attr("selected",true);
		$("select[name=reside_nation]").change();
		$("select[name=reside_region]").find("option[value="+obj[0].taxResideRegion+"]").attr("selected",true);
		$("input[name=reside_address]").val(obj[0].taxResideAddress);
		$("input[name=reside_address_english]").val(obj[0].taxResideAddressEnglish);
		$("select[name=taxNationality]").find("option[value="+obj[0].taxNationality+"]").attr("selected",true);
		$("select[name=taxNationality]").change();
		$("select[name=taxArea]").find("option[value="+obj[0].taxArea+"]").attr("selected",true);
	}
	
	/**
	 * 控制人信息赋值
	 */
	function controlVal(){
		$(".negativeNotFinaInstBg,#controllerInfo").show();
		
	    $("input[name=ChineseName2]").val(obj[0].chineseName2);
	    $("input[name=EnglishFamliyName3]").val(obj[0].englishFamliyName3);
	    $("input[name=EnglishFirstName3]").val(obj[0].englishFirstName3);
	    $("select[name=ControllerType]").find("option[value="+obj[0].controllerType+"]").attr("selected",true);
	    $("input[name=ConNonResiFlag]").val("1");
	    $("input[name=ConShareRatio]").val(obj[0].conShareRatio);
	    $("select[name=LivingCountry2]").find("option[value="+obj[0].livingCountry2Code+"]").attr("selected",true);
	    $("select[name=LivingCountry2]").change();
		$("select[name=LivingCountry21]").find("option[value="+obj[0].livingCountry2+"]").attr("selected",true);
	    $("input[name=LivingAddress5]").val(obj[0].livingAddress5);
	    $("input[name=LivingAddress7]").val(obj[0].livingAddress7);
	    $("select[name=RegRegionCode2]").find("option[value="+obj[0].regRegionCode2Code+"]").attr("selected",true);
	    $("select[name=RegRegionCode2]").change();
		$("select[name=RegRegionCode21]").find("option[value="+obj[0].regRegionCode2+"]").attr("selected",true);
		$("input[name=BirthDate2]").val(obj[0].birthDate2);
		$("select[name=BirthCountry2]").find("option[value="+obj[0].birthCountry2Code+"]").attr("selected",true);
		$("select[name=BirthCountry2]").change();
		$("select[name=BirthCountry21]").find("option[value="+obj[0].birthCountry2+"]").attr("selected",true);
		$("input[name=BirthCity2]").val(obj[0].birthCity2);
		$("select[name=TaxCountry2]").find("option[value="+obj[0].taxCountry2Code+"]").attr("selected",true);
		$("select[name=TaxCountry2]").change();
		$("select[name=TaxCountry21]").find("option[value="+obj[0].taxCountry2+"]").attr("selected",true);
		if(obj[0].taxID2=="" || obj[0].taxID2 == undefined){  //如果纳税人识别号 为空
		   	  var isTaxpayerEventNext=$("input[name=isTaxpayerEvent2]").parents("tr").next();
		   	  $("input[name=TaxID2]").eq(0).attr("disabled","disabled");
		   	  $("input[name=isTaxpayerEvent2]").eq(0).attr("checked",true);
		   	  isTaxpayerEventNext.show();
		   	  var taxNotCodeCause=obj[0].specificationCode;
		   	  isTaxpayerEventNext.find("select").find("option[value="+taxNotCodeCause+"]").attr("selected",true);
		   	  isTaxpayerEventNext.find("select").change();
		   	  if(taxNotCodeCause.trim()=="2"){
		   	  	 isTaxpayerEventNext.next().show();
		   	  	 isTaxpayerEventNext.next().find("input").val(obj[0].specification2);
		   	  }
		   }else{
		   	  $("input[name=TaxID2]").val(obj[0].taxID2);
		}
	}
	
	/**
	 * 仅非居民信息只有一条信息
	 */
	function residentOnlyOne(){
		   comResident();
	}
	
	/**
	 * 当居民信息只有两条
	 */
	function residentOnlyTwo(){
		 $(".since").eq(0).show();
		 $(".since").eq(1).show();
		  comResident();
		  if(obj[1].taxPayerCode=="" || obj[1].taxPayerCode==undefined){
		     $(".since").eq(1).find("input[name=taxpayerCode]").attr("disabled","disabled");
		     $(".since").eq(1).find("input[name=isTaxpayerEvent]").removeAttr("checked");
		  	 $(".since").eq(1).find("input[name=isTaxpayerEvent]").prop("checked","checked");
		  	 $(".sinceNotCodeCause").eq(0).show();
		  	 $(".sinceNotCodeCause").eq(0).find("select").find("option[value="+obj[1].taxNotCodeCause+"]").attr("selected",true);
		  	 $(".sinceNotCodeCause").eq(0).find("select").change();
		  	 if($(".sinceNotCodeCause").eq(0).find("select").val()=="2"){
		  	 	 $(".sinceNotCodeCause").eq(1).show();
		  	 	 $(".sinceNotCodeCause").eq(1).find("input").val(obj[1].taxnotGetCause);
		  	 }
		  }else if(obj[1].taxPayerCode!=""){
		  	  $(".since").eq(1).find("input").val(obj[1].taxPayerCode);
		  } 
	}
	/**
	 * 公共税收信息
	 */
	function comResident(){
		 $(".taxNationality select[name=taxNationality]").val(trim(obj[0].taxNationality));
		 $(".taxNationality select[name=taxNationality]").change();
		 $(".taxNationality select[name=taxArea]").val(trim(obj[0].taxArea));
		 $(".taxNationality select[name=taxArea]").change();
		 if(obj[0].taxPayerCode=="" || obj[0].taxPayerCode==undefined){  //如果纳税人识别号 为空
		   	  var isTaxpayerEventNext=$(".isTaxpayerEvent").eq(0).parents("tr").next();
		   	  $("input[name=taxpayerCode]").eq(0).attr("disabled","disabled");
		   	  $(".isTaxpayerEvent").eq(0).attr("checked",true);
		   	  isTaxpayerEventNext.show();
		   	  var taxNotCodeCause=obj[0].taxNotCodeCause;
		   	  isTaxpayerEventNext.find("select").find("option[value="+taxNotCodeCause+"]").attr("selected",true);
		   	  isTaxpayerEventNext.find("select").change();
		   	  if(taxNotCodeCause.trim()=="2"){
		   	  	 isTaxpayerEventNext.next().show();
		   	  	 isTaxpayerEventNext.next().find("input").val(obj[0].taxnotGetCause);
		   	  }
		  }
		  else{
		   	  $("input[name=taxpayerCode]").eq(0).val(obj[0].taxPayerCode);
		  }
	}
	/**
	 * 当数据大于2条时候
	 */
	function  residentThree(){
		var newData=obj.slice(2,arrDate.length);
		for(var i=0;i<newData.length;i++){
			if (newData[i].taxNationality != undefined && newData[i].taxArea != undefined) {
				addResident();
				$(".add"+i).find("select[name=taxNationality]").find("option[value="+newData[i].taxNationality+"]").attr("selected",true);
				$(".add"+i).find("select[name=taxNationality]").change();
				$(".add"+i).find("select[name=taxArea]").find("option[value="+newData[i].taxArea+"]").attr("selected",true);
				if(newData[i].taxPayerCode=="" || newData[i].taxPayerCode==undefined){
					$(".add"+i).eq(1).find("input[name=taxpayerCode]").attr("disabled","disabled");
					$(".add"+i).eq(1).find(".isTaxpayerEvent").attr("checked",true);
					$(".add"+i).eq(2).show();
					$(".add"+i).eq(2).find("select").find("option[value="+newData[i].taxNotCodeCause+"]").attr("selected",true);
					if($(".add"+i).eq(2).find("select").val().trim()=="2"){
			  	 	 	$(".add"+i).eq(3).show();
			  	 	 	$(".add"+i).eq(3).find("input").val(newData[i].taxnotGetCause);
					}
				}else if(newData[i].taxPayerCode!=""){
			  	  $(".add"+i).eq(1).find("input").val(newData[i].taxPayerCode);
				} 
		  	}
	  	}
	}
	
	$('.select2_width').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.notCodeCause').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$("#taxResidentsContent input,select").attr("disabled",true);
}

function changeVal(){
	var selOpenType = $("#selOpenType").val();
	var reg = /^[\u4E00-\u9FA5]+$/;
	if (selOpenType == '1') {
		var pslInvNm = $("#invNm").val();
		if (reg.test(pslInvNm)) {
			$("#userCustName").val(pslInvNm);
		}
	} else if (selOpenType == '2' || selOpenType == '3') {
		var orgInvNm = $("#invNm").val();
		if (reg.test(orgInvNm)) {
			$("#userCustName").val(orgInvNm);
		}
	}
	$("#userCustName").val($("#invnm").val());
}

function initEnglishNameTr(selOpenType) {
	var html = "";
	if (selOpenType == '1') {
		html += '<td><font color="red">*</font>First Name：</td>';
		html += '<td class="white-bg"><div class="col-sm-11 form-inner"><input type="text" name="firstName" id="firstName" class="form-control"></div></td>';
		html += '<td><font color="red">*</font>Last Name：</td>';
		html += '<td class="white-bg" colspan="2">';
		html += '<div class="col-sm-11 form-inner"><input type="text" name="englishName" id="englishName" class="form-control"></div></td>';

		$(".sexAndbirth").show();
		$(".birthAddress").show();
		$(".resideNation").html("现居国家");
		$(".resideAddress").html("现居地址");

	} else {
		html += '<td><font color="red">*</font>English Name：</td>';
		html += '<td class="white-bg" colspan="4">';
		html += '<div class="col-sm-11 form-inner"><input type="text" name="englishName" id="englishName" class="form-control"></div></td>';

		$(".sexAndbirth").hide();
		$(".birthAddress").hide();
		$(".resideNation").html("机构国家");
		$(".resideAddress").html("机构地址");
		
	}
	$(".englishNameDiv").html(html);
}

/**
 * 获取性别和出生年月
 */
function getSexAndBirth() {
	var pslInvIdtp = $("#pslInvIdtp").val();
	var pslInvIdno = $("#pslInvIdno").val();
	$("input[name=birthDate],select[name=sex]").val("");
	if (pslInvIdtp == '0' && !!pslInvIdno) {
		var Birth = getIdCard.getIdCardInfo(pslInvIdno).birthday;
		var sex = getIdCard.getIdCardInfo(pslInvIdno).gender;
		$("input[name=birthDate]").val(Birth);
		$("select[name=sex]").val(sex);
	}
	$("select[name=sex]").change();
	WASP_WIDGET.triggerDateStyleWithYMD("birthDate");
}

/**
 * 是否 无纳税人识别号
 */
function isTaxpayer(e) {
	var checked = e.checked;
	var target = $(e);
	if (checked) {
		target.parent().prev().attr("readonly", "readonly");
		target.parent().prev().attr("disabled", "disabled");
		target.parent().prev().val("");
		target.parents("tr").next().show();
		target.parents("tr").next().find("select").val("");
		target.parents("tr").next().find("select").change();
	} else {
		target.parent().prev().removeAttr("readonly");
		target.parent().prev().removeAttr("disabled");
		target.parents("tr").next().hide();
		target.parents("tr").next().find("select").val("");
		target.parents("tr").next().find("select").change();
		target.parents("tr").next().next().hide();
		target.parents("tr").next().next().find("input").val("");
	}
}
/**
 * 无识别号事件
 * @param {Object} element
 */
function notCodeCauseEvent(element) {
	var value = element.value;
	if (value == "2") {
		$(element).parents("tr").next("tr").show();
	} else {
		$(element).parents("tr").next("tr").hide();
		$(element).parents("tr").next("tr").find("input").val("");
	}
}

/**
 * 取消纳税人识别号check选中
 */
function cancelTaxpayerCode() {
	var checkObj = $("#residentType input[type=checkbox]");
	checkObj.attr("checked", false);
	checkObj.parent().prev().removeAttr("readonly");
	checkObj.parent().prev().removeAttr("disabled");
	checkObj.parents("tr").next().val("").hide();
	checkObj.parents("tr").next().val("").hide();
}

/*
 * 获取身份证的信息
 */
var getIdCard = {
	genders : {
		male : "1",
		female : "0"
	},
	getIdCardInfo : function(idCardNo) {
		var idCardInfo = {
			gender : "", //性别
			birthday : "" // 出生日期
		};
		if (idCardNo.length == 15) {
			var aday = '19' + idCardNo.substring(6, 12);
			idCardInfo.birthday = getIdCard.formateDateCN(aday);
			if (parseInt(idCardNo.charAt(14)) % 2 == 0) {
				idCardInfo.gender = getIdCard.genders.female;
			} else {
				idCardInfo.gender = getIdCard.genders.male;
			}
		} else if (idCardNo.length == 18) {
			var aday = idCardNo.substring(6, 14);
			idCardInfo.birthday = getIdCard.formateDateCN(aday);
			if (parseInt(idCardNo.charAt(16)) % 2 == 0) {
				idCardInfo.gender = getIdCard.genders.female;
			} else {
				idCardInfo.gender = getIdCard.genders.male;
			}

		}
		return idCardInfo;
	},
	formateDateCN : function(day) {
		var yyyy = day.substring(0, 4);
		var mm = day.substring(4, 6);
		var dd = day.substring(6);
		return yyyy + "-" + mm + "-" + dd;
	}
};