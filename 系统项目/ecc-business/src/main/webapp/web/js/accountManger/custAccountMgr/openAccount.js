var PROJEC_TPATH = "";
var PRIMARY_PATH = "";
var ACCOUNT_PATH = "";

function setPath(projectPath,path,accountPath){
	PROJEC_TPATH = projectPath;
	PRIMARY_PATH = path;
	ACCOUNT_PATH = accountPath;
}

$(function(){
	WASP_WIDGET.triggerDateStyleWithYMD("pslInvIdValidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgInstrepidvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgPrincipalidvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgContidvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgHoldingidvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgInvIdvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("birthDate");
	WASP_WIDGET.triggerDateStyleWithYMD("BirthDate2");
	
	showBaseInfo();
	$("#selOpenType").change(function(){ 
		baseInfo();
		interactInvProInfo();
		clearOffLineInfo(true);
		changeVal();
		var selOpenType = $("#selOpenType").val();
		initEnglishNameTr(selOpenType);
		queryWaspOffInvestInfo();
		antiMoneyLaunValid();
    });
	$('.select2_width').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.select2-q').select2({width: "180px"});
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('#qOffLineInvestName').select2({allowClear: false});
	initGrid();
	
	$("#claseBtn").click(function() {
		setTimeout(function(){
			$("select").change();
		}, 100);
		$("#reset").click();
	});
	queryWaspOffInvestInfo();
});

function showTypeContent(id,tId){
	$(".typeContent").hide();
	$("#"+id).show();
	if(tId == 'btnBaseInfo'){
		baseInfo();
	}else if(tId == 'btnExtInfo'){
		extInfo();
	}else if(tId == 'btnCategoryInfo'){
		categoryInfo();
		resetInvestClassTh("investClassInfo", "classInfoTh");
		resetInvestClassTh("instInvestPro", "instInvestProTh");
		changeVal();
	}else if(tId == 'btnDocumentInfo'){
		documentInfo();
	}
};


function showBaseInfo()//显示基本信息
{
	var selOpenType = $("#selOpenType").val();
	
	if(selOpenType == 1)
	{
		$("#querytable1").show();
		$("#querytable2").hide();
		$("#querytable3").hide();
		$("#tabPslBaseInfo").show();
		$("#tabOrgBaseInfo").hide();
		$(".instNotResidentBg").hide();
	}
	if(selOpenType == 2)
	{
		$("#querytable1").hide();
		$("#querytable2").show();
		$("#querytable3").hide();
		$("#tabPslBaseInfo").hide();
		$("#tabOrgBaseInfo").show();
		$(".instNotResidentBg").show();
	}
	if(selOpenType == 3)
	{
		$("#querytable1").hide();
		$("#querytable2").show();
		$("#querytable3").show();
		$("#tabPslBaseInfo").hide();
		$("#tabOrgBaseInfo").show();
		$(".instNotResidentBg").show();
	}      
}


var rList = {};
var doclist;
doSelect($("#docbusinesstp").val());//wdw 2010-09-14添加，初始化资料信息
baseInfo();
$(document).ready(function() {
	// 填充下拉框数据
	queryuccustinfo();

	$("#btnQuery").mousedown(function() {
		queryInfo();
	});
	// 界面操作
	$("#btnSubmit").mousedown(function() {
		dosubmit();
	});
	$("#btnAddId").mousedown(function() {
		addIDRow("", "", "");
	});
	$("#btnAddCont").mousedown(function() {
		addContRow("", "", "", "", "", "", "", "", "", "", "", "");
	});
	$("#docbusinesstp").change(function() {
		doSelect($("#docbusinesstp").val());
	});
	$("#isalldoc").change(function() {
		doisallSelect();
	});
	
	
	
	
	
	$("#seltgr").change(function() {
		seltgrChange();
	});
	$("#seltzglr").change(function() {
		seltzglrChange();
	});
	// 初始化文件编号和存档位置
	queryFileNo();
	interactInvProInfo();
	$("#investProInstType").change();
});

function seltgrChange(){
	$("#seltzglr").unbind("change");
	$("#seltzglr").val('--');
	$("#seltzglr").select2({minimumResultsForSearch:Infinity});
	queryBakCustInfoByCustno('', 'I');// 清空seltzglr自动填充信息
	var tgr = $("#seltgr").val();
	queryBakCustInfoByCustno(tgr, "T");
	$("#seltzglr").change(function() {
		seltzglrChange();
	});
}

function seltzglrChange(){
	$("#seltgr").val('--');
	$("#seltgr").select2("destroy");
	$("#seltgr").select2({minimumResultsForSearch:Infinity});
	$("#seltgr").unbind("change");
	var tzglr = $("#seltzglr").val();
	// add by zhuyl01 2014-12-17 增加通讯地址、邮编、技术监督局代码和有效期 同步
	queryBakCustInfoByCustno("", "T");
	queryBakCustInfoByCustno(tzglr, "I");
	$("#seltgr").change(function() {
		seltgrChange();
	});
}


function queryFileNo() // 初始化文件编号和存档位置
{
	$.ajax({
		// 任务生效
		url: ACCOUNT_PATH+"getMaxFileNo.do",  
		dataType: "json",
		type: "POST",
		cache: false,
		async: false,
		success: function(data) {
			$("#keepaddress").val("");
			$("#fileno").val("");
			var returnCode=data.returnCode;
			var returnMsg=data.returnMsg;
			var fileNo=data.fileNo;
			var keepAddress=data.keepAddress;
			if("0000"!=returnCode){
				ctools.alert(returnMsg+"！","","error");
			}else{
				$("#keepaddress").val(keepAddress);
				$("#fileno").val(fileNo);  
			}
		}
	});
} 

//------------------------------------------------------显示控制------------------------------------------------------------------------------
/*********************0414修改***************************************/
function showQueryInfo() // 显示客户信息
{
	$("#querytable").show();
	showBaseInfo();
	hideExtInfo();
	$("#categoryInfo").hide();
	$("#documentInfo").hide();
	$("#controllerInfo").hide();
}
function baseInfo() // 显示基本信息
{
	$("#querytable").show();
	$("#tabtainfo").show();
	$("#mandatorTable").show();
	showBaseInfo();
	hideExtInfo();
	$("#categoryInfo").hide();
	$("#documentInfo").hide();
	$("#controllerInfo").hide();
}
function extInfo()// 附加信息
{
	$("#querytable").hide();
	$("#tabtainfo").hide();
	$("#mandatorTable").hide();
	$("#querytable1").hide();
	$("#querytable2").hide();
	$("#querytable3").hide();
	hideBaseInfo();
	showExtInfo();
	$("#categoryInfo").hide();
	$("#documentInfo").hide();
	$("#controllerInfo").hide();
}
function categoryInfo()// 分类信息
{
	$("#querytable").hide();
	$("#tabtainfo").hide();
	$("#mandatorTable").hide();
	$("#querytable1").hide();
	$("#querytable2").hide();
	$("#querytable3").hide();
	hideBaseInfo();
	hideExtInfo();
	$("#categoryInfo").show();
	$("#documentInfo").hide();
	$("#residentType").show();
	getTaxType($("#taxType").val(), false);
	if ($("select[name=controlPerTaxDecl]").val() == "1") {
		$("#controllerInfo").show();
	} else {
		$("#controllerInfo").hide();
	}
	getSexAndBirth();
}
function documentInfo()// 资料信息
{
	$("#querytable").hide();
	$("#tabtainfo").hide();
	$("#mandatorTable").hide();
	$("#querytable1").hide();
	$("#querytable2").hide();
	$("#querytable3").hide();
	hideBaseInfo();
	hideExtInfo();
	$("#categoryInfo").hide();
	$("#documentInfo").show();
	$("#controllerInfo").hide();
}
/** *******************0414修改************************************** */



 
function showExtInfo()// 显示附加信息
{
	if ($("#selOpenType").val() == 1) {
		$("#tabPslExtInfo").show();
		$("#tabOrgExtInfo").hide();
	}
	if ($("#selOpenType").val() == 2) {
		$("#tabPslExtInfo").hide();
		$("#tabOrgExtInfo").show();
	}
	if ($("#selOpenType").val() == 3) {
		$("#tabPslExtInfo").hide();
		$("#tabOrgExtInfo").show();
	}
}
function hideExtInfo()// 隐藏附加信息
{
	$("#tabPslExtInfo").hide();
	$("#tabOrgExtInfo").hide();
}
function hideBaseInfo()// 隐藏基本信息
{
	$("#tabPslBaseInfo").hide();
	$("#tabOrgBaseInfo").hide();
}
// --------------------------------------------------------------------------------------------------------------------------------------------
function doisallSelect()// 资料是否齐全
{
	if ($("#isalldoc").val() == 1) {
		$("#documentinfo input[name='document']").each(function(data) {
			$(this).prop("checked", true);// 全选
		});
	} else {
		$("#documentinfo input[name='document']").each(function(data) {
			$(this).prop("checked", false);// 取消全选
		});
	}
}   


// --------------------------------------------------------------初始化备案客户列表-------------------------------------------------------------
function queryuccustinfo()  //查询备案客户列表
{
   
	$.ajax({
		type : "post",
		url : ACCOUNT_PATH+"queryBakCust.do",
		dataType : "json",
		data:{
			'custno':"",
			'role'	:""
		},
		success : function(data) {
			handlequeryuccustinfo(data);
		},
		error : function() {
			ctools.alert("查询备案客户列表失败！","","error");
		}
	});
} 
  
function handlequeryuccustinfo(object)
{            
   if(object!=null)
   {
      $("#seltgr").empty();
      $("#seltzglr").empty();
      $("#seltgr").append("<option value='--' selected>--</option>");
      $("#seltzglr").append("<option value='--' selected>--</option>");         
      for(var i=0;i<object.length;i++)
      {
         if(object[i].rcdcustrole=='T')
         {
        	 $("#seltgr").append('<option value="'+object[i].custno+'">'+object[i].custno+"&nbsp;&nbsp;"+object[i].invnm+"</option>");
        	 $('#seltgr').select2("destroy");
        	 $("#seltgr").select2({allowClear: false,minimumResultsForSearch:Infinity});
         }
         else
         {
        	 $("#seltzglr").append('<option value="'+object[i].custno+'">'+object[i].custno+"&nbsp;&nbsp;"+object[i].invnm+"</option>");
        	 $("#seltzglr").select2("destroy");
        	 $("#seltzglr").select2({allowClear: false,minimumResultsForSearch:Infinity});
         }
      }
   }
}
//-------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------查询备案客户详细信息-------------------------------------------------------------    
function queryBakCustInfoByCustno(custno,custrole)
{
   if(custno==null || custno=="")
   {
      if(custrole == "T")
      {
         $("#orgContactright").val("");
         $("#orgContnm").val("");
         $("#orgContnation").val("");
         $("#orgContidtp").val("");
         $("#orgContidno").val("");      
         $("#orgContidvalidate").val("");
         $("#orgContphone").val("");
         $("#orgContfax").val("");
         $("#orgContmobile").val("");
         $("#orgContemail").val("");
         $("#orgContAddr").val("");
         $("#orgContPostcode").val("");
         $("#orgRiskLevel").val("");
         $("#orgRiskLevel").change();
         emptyInfo();
         tcustlist = "";
      }
      else
      {
         $("#orgInstrepnm").val("");
         $("#orgInstrepnation").val("");
         $("#orgInstrepidtp").val("");
         $("#orgInstrepidno").val("");
         $("#orgInstrepidvalidate").val("");
   
         $("#orgPrincipalname").val("");
         $("#orgPrincipalnation").val("");
         $("#orgPrincipalidtp").val("");
         $("#orgPrincipalidno").val("");
         $("#orgPrincipalidvalidate").val("");
         emptyocontinfo();
         icustlist = "";
      }
      $('#orgContactright').select2("destroy");
      $('#orgContnation').select2("destroy");
      $('#orgContidtp').select2("destroy");
      $('#orgInstrepnation').select2("destroy");
      $('#orgPrincipalidtp').select2("destroy");
      $('#orgContactright').select2({minimumResultsForSearch:Infinity});
      $('#orgContnation').select2({minimumResultsForSearch:Infinity});
      $('#orgContidtp').select2({minimumResultsForSearch:Infinity});
      $('#orgInstrepnation').select2({minimumResultsForSearch:Infinity});
      $('#orgPrincipalidtp').select2({minimumResultsForSearch:Infinity});
      $('#orgContactright').change();
      $('#orgContnation').change();
      $('#orgContidtp').change();
      $('#orgInstrepnation').change();
      $('#orgPrincipalidtp').change();
      $("#orgInstrepidtp").change();
      //addocontinfo();
   }
   else
   {      
      $.ajax({
	  		type : "post",
	  		url : ACCOUNT_PATH+"queryBakCust.do",
	  		dataType : "json",
	  		data:{
	  			'custno': custno,
	  			'role'	: ""
	  		},
	  		cache: false,
			async: true,
	  		success : function(json) {
	  			if(custrole=='T'){
	          		tcustlist = json;//填充托管理人
	          	}else{
	          		icustlist = json;//填充投资管理人
	          	}
	            handlequeryBakCustInfoByCustno(json,custrole);
	  		},
	  		error : function() {
	  			ctools.alert("查询备案客户列表失败！","","error");
	  		}
      });
   }
} 
var tcustlist;//托管人信息
var icustlist;//投资管理人信息
function handlequeryBakCustInfoByCustno(custlist,custrole)
{
   var dataObj = {};
   if(custlist!=null && custlist.length==1)
   {
      dataObj = custlist[0];
   }

   //托管人填充经办人信息
   if(custrole=='T')
   {
     $("#orgContactright").val(dataObj.contactgrant);
     $("#orgContnm").val(dataObj.contact);
     $("#orgContnation").val(dataObj.contactnation);
     $("#orgContidtp").val(dataObj.contidtp);
     $("#orgContidno").val(dataObj.contidno);
     $("#orgContidvalidate").val(dataObj.contvalidate);
     $("#orgContphone").val(dataObj.contphone);
     $("#orgContfax").val(dataObj.contfax);
     $("#orgContmobile").val(dataObj.contmobile);
     $("#orgContemail").val(dataObj.contemail);
     $("#orgContAddr").val(dataObj.contAddr);
     $("#orgContPostcode").val(dataObj.contPostcode);
     $("#orgAddr").val(dataObj.addr);
     $("#orgPostcode").val(dataObj.postcode);
     $("#orgContnation").change();
     $("#orgContidtp").change();
     
     emptyInfo();
     var oidlist = dataObj.oidlist;
     if(!!oidlist && oidlist.length>0)
     {
         for(var i=0;i<oidlist.length;i++)
         {            
            addIDRow(oidlist[i].idtp,oidlist[i].idno,oidlist[i].idvalidate);
         }
     }
     $("#orgRiskLevel").val(dataObj.riskLevel);//托管人填充客户风险承受能力
     $("#orgRiskLevel").change();
     
     $("#orgInstrepnm").val(dataObj.instrepnm);
     $("#orgInstrepnation").val(dataObj.instrepnation);
     $("#orgInstrepidtp").val(dataObj.instrepidtp);
     $("#orgInstrepidno").val(dataObj.instrepidno);
     $("#orgInstrepidvalidate").val(dataObj.instrepvalidate);
   
     $("#orgPrincipalname").val(dataObj.principalname);
     $("#orgPrincipalnation").val(dataObj.principalnation);
     $("#orgPrincipalidtp").val(dataObj.principalidtp);
     $("#orgPrincipalidno").val(dataObj.principalidno);
     $("#orgPrincipalidvalidate").val(dataObj.principalvalidt);
     
   }
   //管理人填充法人信息
   else
   {
	  
	 $("#orgContactright").val(dataObj.contactgrant);
     $("#orgContnm").val(dataObj.contact);
     $("#orgContnation").val(dataObj.contactnation);
     $("#orgContidtp").val(dataObj.contidtp);
     $("#orgContidno").val(dataObj.contidno);
     $("#orgContidvalidate").val(dataObj.contvalidate);
     $("#orgContphone").val(dataObj.contphone);
     $("#orgContfax").val(dataObj.contfax);
     $("#orgContmobile").val(dataObj.contmobile);
     $("#orgContemail").val(dataObj.contemail);
     $("#orgContAddr").val(dataObj.contAddr);
     $("#orgContPostcode").val(dataObj.contPostcode);
     $("#orgAddr").val(dataObj.addr);
     $("#orgPostcode").val(dataObj.postcode);
     emptyInfo();
     var oidlist = dataObj.oidlist;
     if(!!oidlist && oidlist.length>0)
     {
         for(var i=0;i<oidlist.length;i++)
         {            
            addIDRow(oidlist[i].idtp,oidlist[i].idno,oidlist[i].idvalidate);
         }
     }
     $("#orgRiskLevel").val(dataObj.riskLevel);//托管人填充客户风险承受能力
     $("#orgRiskLevel").change();
	   
     $("#orgInstrepnm").val(dataObj.instrepnm);
     $("#orgInstrepnation").val(dataObj.instrepnation);
     $("#orgInstrepidtp").val(dataObj.instrepidtp);
     $("#orgInstrepidno").val(dataObj.instrepidno);
     $("#orgInstrepidvalidate").val(dataObj.instrepvalidate);
   
     $("#orgPrincipalname").val(dataObj.principalname);
     $("#orgPrincipalnation").val(dataObj.principalnation);
     $("#orgPrincipalidtp").val(dataObj.principalidtp);
     $("#orgPrincipalidno").val(dataObj.principalidno);
     $("#orgPrincipalidvalidate").val(dataObj.principalvalidt);
     
   }
   $('#orgContactright').change();
   $('#orgContnation').change();
   $('#orgContidtp').change();
   $('#orgInstrepnation').change();
   $('#orgPrincipalidtp').change();
   $("#orgInstrepidtp").change();
   addocontinfo();
}    

function emptyInfo()
{
   $("#oidinfo>tr:gt(0)").each(function(data)
   {               
      $(this).remove();
   }); 
   $("#ocontInfo>tr:gt(0)").each(function(data)
   {               
      $(this).remove();
   });
}
function emptyocontinfo(){
 contflag = 0;
	$("#ocontInfo>tr:gt(0)").each(function(data)
   {               
      $(this).remove();
   });
}
function addocontinfo() {
	var tcustObj = "";
	var otcustlist = "";
	if (tcustlist != null && tcustlist.length == 1) {
		tcustObj = tcustlist[0];
		otcustlist = tcustObj.ocontactlist;
	}
	var icustObj = "";
	var oicustlist = "";
	if (icustlist != null && icustlist.length == 1) {
		icustObj = icustlist[0];
		oicustlist = icustObj.ocontactlist;
	}
	emptyocontinfo();
	/*if (icustObj != null && icustObj != '') {// 把投资管理人的主经办人放到其他经办人首位
		addContRow(icustObj.contactgrant, icustObj.contact,
				icustObj.contactnation, icustObj.contidtp, icustObj.contidno,
				icustObj.contvalidate, icustObj.contphone, icustObj.contfax,
				icustObj.contmobile, icustObj.contemail, icustObj.contAddr,
				icustObj.contPostcode);
	}
	if (tcustObj != null && tcustObj != '') {// 把托管人的主经办人放到其他经办人首位
		addContRow(tcustObj.contactgrant, tcustObj.contact,
				tcustObj.contactnation, tcustObj.contidtp, tcustObj.contidno,
				tcustObj.contvalidate, tcustObj.contphone, tcustObj.contfax,
				tcustObj.contmobile, tcustObj.contemail, tcustObj.contAddr,
				tcustObj.contPostcode);
	}*/
	if (otcustlist != null && otcustlist.length > 0)// 填充托管人其他经办人信息
	{
		for (var j = 0; j < otcustlist.length; j++) {
			addContRow(otcustlist[j].contactgrant, otcustlist[j].contact,
					otcustlist[j].contactnation, otcustlist[j].contidtp,
					otcustlist[j].contidno, otcustlist[j].contvalidate,
					otcustlist[j].contphone, otcustlist[j].contfax,
					otcustlist[j].contmobile, otcustlist[j].contemail,
					otcustlist[j].contAddr, otcustlist[j].contPostcode);
		}
	}
	if (oicustlist != null && oicustlist.length > 0) {// 把投资管理人其他经办人带出来
		for (var j = 0; j < oicustlist.length; j++) {
			addContRow(oicustlist[j].contactgrant, oicustlist[j].contact,
					oicustlist[j].contactnation, oicustlist[j].contidtp,
					oicustlist[j].contidno, oicustlist[j].contvalidate,
					oicustlist[j].contphone, oicustlist[j].contfax,
					oicustlist[j].contmobile, oicustlist[j].contemail,
					oicustlist[j].contAddr, oicustlist[j].contPostcode);
		}
	}
}
//--------------------------------------------------------------查询备案客户详细信息-------------------------------------------------------------   
//-----------------------------------------------取注册信息------------------------------------------------------------------------------------
function getPersonalInfo()
{
   var personalInfo = {};  
   personalInfo.custTp = "1";               
   personalInfo.permissionId = $("#permissionId").val();
   personalInfo.operatorId = $("#operatorId").val();
   var opentype = "";
   $("input[name='opentype']").each(function(data)
   {
      if($(this).is(':checked'))
      {
         opentype = $(this).val();
      }   
   });
   personalInfo.opentype = opentype;
   personalInfo.fundacc= trim($("#fundacc").val()); 
   personalInfo.trustTp = $("#trusttp").val();  
   
   var tanos="";
   $("input[name='talist']").each(function(data)
   {
      if($(this).is(':checked'))
      {
         tanos +=$(this).attr("id")+",";
      }   
   });
   personalInfo.tano = tanos;            
                   
   //基本->证件信息
   personalInfo.invNm = trim($("#pslInvNm").val());  
   personalInfo.invIdtp = $("#pslInvIdtp").val(); 
   personalInfo.invIdno = trim($("#pslInvIdno").val()); 
   personalInfo.invIdValidate = $("#pslInvIdValidate").val();
   personalInfo.offInvSerialno = trim($("#offInvSerialno").val()); 
   
   //基本->银行信息
   personalInfo.bankno = $("#selbankno").val();  
   personalInfo.openname = trim($("#openname").val()); 
   personalInfo.openlocation = $("#openlocation").val();//省
   personalInfo.openbankcity = $("#openbankcity").val();//市
   personalInfo.bankacconm = trim($("#bankacconm").val());   
   personalInfo.bankacco = trim($("#bankacco").val());    
   
   //基本->其他信息      
   personalInfo.addr = trim($("#pslAddr").val());  
   personalInfo.postcode = trim($("#pslPostCode").val());   
   personalInfo.pslInvOfficeTel = trim($("#pslInvOfficeTel").val()); 
   personalInfo.pslInvHomeTel = trim($("#pslInvHomeTel").val()); 
   personalInfo.pslInvMobile = trim($("#pslInvMobile").val()); 
   personalInfo.pslInvFax = trim($("#pslInvFax").val()); 
   personalInfo.plsFaxDelegate = trim($("#plsFaxDelegate").val()); 
   personalInfo.pslInvEmail = trim($("#pslInvEmail").val());
   personalInfo.shsecacc = trim($("#pslShsecacc").val());
   personalInfo.szsecacc = trim($("#pslSzsecacc").val());
   //personalInfo.plsBillDelivery = $("#plsBillDelivery").val();     
   
   //附加信息->其他   
   personalInfo.plsSex = $("#plsSex").val(); 
   personalInfo.plsInvNation = $("#plsInvNation").val(); 
   personalInfo.plsInvEducation = $("#plsInvEducation").val(); 
   personalInfo.plsInvJob = $("#plsInvJob").val(); 
   personalInfo.plsInvIncome = $("#plsInvIncome").val(); 
   personalInfo.plsInvRisk = $("#plsInvRisk").val();
   //特殊用户风险等级
   personalInfo.specriskLevel = "0";
   if($("#indSpecRiskLevel").is(':checked')){
 	  personalInfo.plsInvRisk = "1";
 	  personalInfo.specriskLevel = "1";
   }
   //分类信息
   personalInfo.custsimpnm = $("#custsimpnm").val();
   personalInfo.instrepcode = $("#instrepcode").val();
   personalInfo.businesstp = $("#businesstp").val();
   personalInfo.corptype = $("#corptype").val();     
   personalInfo.regioncode = $("#regioncode").val();     
   personalInfo.fxqtype = $("#fxqtype").val();         
   personalInfo.fxqdesc = trim($("#fxqdesc").val());                          
   personalInfo.invprtype=$('#invprtp').val();
   personalInfo.taxType = $("#taxType").val();
   personalInfo.taxTypeDecl = $("#taxTypeDecl").val();
   personalInfo.threeAnnualIncome = "";
   personalInfo.financialAsset = "";
   personalInfo.indInvExperience = "";
   personalInfo.relatedWorkExp = "";
   personalInfo.finProfessions = "";
   //专业投资者 才需收集信息
	  if(!!personalInfo.invprtype && "0" == personalInfo.invprtype){
		  personalInfo.plsInvRisk = "1";
		  personalInfo.plsInvIncome = "0";
		  personalInfo.specriskLevel = "0";
		  personalInfo.threeAnnualIncome = $("#threeAnnualIncome").val();
	      personalInfo.financialAsset = $("#financialAsset").val();
	      personalInfo.indInvExperience = $("#indInvExperience").val();
	      personalInfo.relatedWorkExp = $("#relatedWorkExp").val();
	      personalInfo.finProfessions = $("#finProfessions").val();
	  }
   
   //资料信息 
   var document=""; 

   $("input[name='document']").each(function(data)
   {
      if($(this).is(':checked'))
      {
         document +=$(this).attr("id")+"-"+"1"+",";
      }
      else
      {
         document +=$(this).attr("id")+"-"+"0"+",";
      }         
   });
   personalInfo.docbusinesstp = $("#docbusinesstp").val(); 
   personalInfo.isalldoc = $("#isalldoc").val();      
   personalInfo.iforiginal = $("#isoriginal").val(); 
   personalInfo.ifsaved = $("#issaved").val();
   personalInfo.isupload = $("#isupload").val();
   personalInfo.remarkinfo = trim($("#remarkinfo").val());
   personalInfo.document = document;   
   personalInfo.isscan=$("#isscan").val();
   personalInfo.keepaddress=	$("#keepaddress").val();
   personalInfo.salesaccmanager= $("#salesaccmanager").val();  	  
	  personalInfo.fileno=  $("#fileno").val();
   return personalInfo;
}

function getOrgInfo()
{
   var orgInfo = {};
   orgInfo.custTp = "0";
   orgInfo.permissionId = $("#permissionId").val();
   orgInfo.operatorId = $("#operatorId").val();
   var opentype = "";
   $("input[name='opentype']").each(function(data)
   {
      if($(this).is(':checked'))
      {
         opentype = $(this).val();
      }   
   });
   orgInfo.opentype = opentype;
   orgInfo.fundacc= trim($("#fundacc").val()); 
   orgInfo.trustTp = $("#trusttp").val();    
   
   var tanos="";
   $("input[name='talist']").each(function(data)
   {
      if($(this).is(':checked'))
      {
         tanos +=$(this).attr("id")+",";
      }   
   });
   orgInfo.tano = tanos;      
         
   //基本->证件信息
   orgInfo.invNm = trim($("#orgInvNm").val());  
   orgInfo.invIdtp = $("#orgInvIdtp").val(); 
   orgInfo.invIdno = trim($("#orgInvIdno").val());
   orgInfo.invIdValidate = $("#orgInvIdvalidate").val();        
   orgInfo.offInvSerialno = trim($("#offInvSerialno").val()); 
   //基本->银行信息
   orgInfo.bankno = $("#orgselbankno").val();  
   orgInfo.openname = trim($("#orgopenname").val()); 
   orgInfo.openlocation = $("#orgopenlocation").val();
   orgInfo.openbankcity = $("#orgopenbankcity").val();//市
   orgInfo.bankacconm = trim($("#orgbankacconm").val());   
   orgInfo.bankacco = trim($("#orgbankacco").val());   
   
   //基本->法人信息
   orgInfo.orgInstrepnm = trim($("#orgInstrepnm").val()); 
   orgInfo.orgInstrepnation = $("#orgInstrepnation").val(); 
   orgInfo.orgInstrepidtp = $("#orgInstrepidtp").val(); 
   orgInfo.orgInstrepidno = trim($("#orgInstrepidno").val()); 
   orgInfo.orgInstrepidvalidate = $("#orgInstrepidvalidate").val(); 
   
   //基本->机构负责人信息
   orgInfo.orgPrincipalname = trim($("#orgPrincipalname").val()); 
   orgInfo.orgPrincipalnation = $("#orgPrincipalnation").val(); 
   orgInfo.orgPrincipalidtp = $("#orgPrincipalidtp").val(); 
   orgInfo.orgPrincipalidno = trim($("#orgPrincipalidno").val());  
   orgInfo.orgPrincipalidvalidate = $("#orgPrincipalidvalidate").val();
   
   //基本->经办人员信息
   orgInfo.orgContactright = $("#orgContactright").val(); 
   orgInfo.orgContnm = trim($("#orgContnm").val()); 
   orgInfo.orgContnation = $("#orgContnation").val(); 
   orgInfo.orgContidtp = $("#orgContidtp").val();  
   orgInfo.orgContidno = trim($("#orgContidno").val());  
   orgInfo.orgContidvalidate = $("#orgContidvalidate").val();
   orgInfo.orgContphone = trim($("#orgContphone").val());
   orgInfo.orgContfax = trim($("#orgContfax").val());     
   orgInfo.orgContmobile = trim($("#orgContmobile").val());     
   orgInfo.orgContemail = trim($("#orgContemail").val());
   orgInfo.orgContAddr = trim($("#orgContAddr").val());     
   orgInfo.orgContPostcode = trim($("#orgContPostcode").val());                           
   
   //基本->其他信息
   orgInfo.addr = trim($("#orgAddr").val()); 
   orgInfo.postcode = trim($("#orgPostcode").val());
   orgInfo.orgInvOfficeTel = trim($("#orgInvOfficeTel").val());
   orgInfo.orgInvFax = trim($("#orgInvFax").val());
   orgInfo.shsecacc = trim($("#orgShsecacc").val());
   orgInfo.szsecacc = trim($("#orgSzsecacc").val());
	  orgInfo.organType = trim($("#organType").val());
   
   //附加->其他信息 
   orgInfo.orgHoldingname = trim($("#orgHoldingname").val());  
   orgInfo.orgHoldingidtp = $("#orgHoldingidtp").val();
   orgInfo.orgHoldingidno = trim($("#orgHoldingidno").val());     
   orgInfo.orgHoldingidvalidate = $("#orgHoldingidvalidate").val();         
   orgInfo.orgBeneficiarynm = trim($("#orgBeneficiarynm").val());     
   orgInfo.orgRiskLevel = $("#orgRiskLevel").val();
   //特殊用户风险等级
   orgInfo.specriskLevel = "0";
   if($("#instSpecRiskLevel").is(':checked')){
 	  orgInfo.orgRiskLevel = "1";
 	  orgInfo.specriskLevel = "1";
   }
   
   //分类信息
   orgInfo.custsimpnm = $("#custsimpnm").val();
   orgInfo.instrepcode = $("#instrepcode").val();
   orgInfo.businesstp = $("#businesstp").val();
   orgInfo.corptype = $("#corptype").val();     
   orgInfo.regioncode = $("#regioncode").val();     
   orgInfo.fxqtype = $("#fxqtype").val();         
   orgInfo.fxqdesc = trim($("#fxqdesc").val());
   //专业投资者
   orgInfo.invprtype=$('#invprtp').val();
   orgInfo.taxType = $("#taxType").val();
   orgInfo.taxTypeDecl = $("#taxTypeDecl").val();
   orgInfo.investProInstType = "";
   orgInfo.investProInstSecond = "";
   orgInfo.oneYearEndNetAsset = "";
   orgInfo.oneYearEndFinAsset = "";
   orgInfo.investExperience = "";
   orgInfo.negativeNotFinaInst = "";
   orgInfo.controlPerTaxDecl = "";
   orgInfo.taxresident ="";
	  //普通投资者 需要收集 消极非金融机构
   /* if(!!orgInfo.invprtype && "1" == orgInfo.invprtype){
 	  orgInfo.negativeNotFinaInst = $("#negativeNotFinaInst").val();
	      orgInfo.controlPerTaxDecl = "";
	      if(!!orgInfo.negativeNotFinaInst && "1" == orgInfo.negativeNotFinaInst){
		      orgInfo.controlPerTaxDecl = $("#controlPerTaxDecl").val();
		  }
   } */
   orgInfo.negativeNotFinaInst = $("#negativeNotFinaInst").val();
   orgInfo.controlPerTaxDecl = $("#controlPerTaxDecl").val();
	  if(!!orgInfo.invprtype && "0" == orgInfo.invprtype){
		  //专业投资者 客户风险等级不需 填写
		  orgInfo.orgRiskLevel = "1";
		  orgInfo.specriskLevel = "0";
		  orgInfo.investProInstType = $("#investProInstType").val();
		  orgInfo.investProInstSecond = $("#investProInstSecond").val();
		  //其他法人才需填写 如下信息
		  if(!!orgInfo.investProInstType && "3" == orgInfo.investProInstType){
		      orgInfo.oneYearEndNetAsset = $("#oneYearEndNetAsset").val();
		      orgInfo.oneYearEndFinAsset = $("#oneYearEndFinAsset").val();
		      orgInfo.investExperience = $("#investExperience").val();
		      orgInfo.negativeNotFinaInst = $("#negativeNotFinaInst").val();
		      orgInfo.controlPerTaxDecl = $("#controlPerTaxDecl").val();
		      /**orgInfo.controlPerTaxDecl = "";
		      if(!!orgInfo.negativeNotFinaInst && "1" == orgInfo.negativeNotFinaInst){
			      orgInfo.controlPerTaxDecl = $("#controlPerTaxDecl").val();
			  } */
		   }
	  }
   

	   // 资料信息
	var document = "";
	$("input[name='document']").each(function(data) {
		if ($(this).is(":checked")) {
			document += $(this).attr("id") + "-" + "1" + ",";
		} else {
			document += $(this).attr("id") + "-" + "0" + ",";
		}
	});
	orgInfo.docbusinesstp = $("#docbusinesstp").val();
	orgInfo.isalldoc = $("#isalldoc").val();
	orgInfo.document = document;
	orgInfo.iforiginal = $("#isoriginal").val();
	orgInfo.ifsaved = $("#issaved").val();
	orgInfo.isupload = $("#isupload").val();
	orgInfo.remarkinfo = trim($("#remarkinfo").val());
	orgInfo.isscan = $("#isscan").val();
	orgInfo.keepaddress = $("#keepaddress").val();
	orgInfo.salesaccmanager = $("#salesaccmanager").val();
	orgInfo.fileno = $("#fileno").val();

	// 附加->其他证件信息
	var oidtpStr = "";
	var oidnoStr = "";
	var oidvalidateStr = "";
	$(".orgInvIdtpTd select[id='ooidtp']").each(function(data) {
		if ($(this).val() == "" || $(this).val() == null) {
			oidtpStr += "$,";
		} else {
			oidtpStr += $(this).val() + ",";
		}
	});
	$("input[id='ooidno']").each(function(data) {
		if ($(this).val() == "" || $(this).val() == null) {
			oidnoStr += "$,";
		} else {
			oidnoStr += $(this).val() + ",";
		}
	});
   // 由于id用于做长期有效，不好取值，现改为用name取值
   $("input[name='ooidvalidate']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oidvalidateStr += "$,";
      }
      else
      {      
         oidvalidateStr += $(this).val()+",";
      }
   });
   orgInfo.oidtpStr = oidtpStr;
   orgInfo.oidnoStr = oidnoStr;
   orgInfo.oidvalidateStr = oidvalidateStr; 
   
   //附加->其他经办人信息 
   var oOrgContactright = "";
   var oOrgContact = "";
   var oOrgContactnation = "";
   var oOrgContactidtp = "";
   var oOrgContactidno = "";
   var oOrgContactidvalidate = "";
   var oOrgContacttel = "";
   var oOrgContactfax = "";
   var oOrgContactmobile = "";
   var oOrgContactemail = "";
   var oOrgContactAddr = "";
   var oOrgContactPostcode = "";
    
   
   $("select[id='oocontactgrant']").each(function(data)
   {         
      if($(this).val()=="" || $(this).val() == null)
      {
         oOrgContactright += "$,";
      }
      else
      {
         oOrgContactright += $(this).val()+",";
      }            
   });
   $("input[id='oocontact']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oOrgContact += "$,";
      }
      else
      {
         oOrgContact += $(this).val()+",";
      }         
   });
   $("select[id='oocontactnation']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oOrgContactnation += "$,";
      }
      else
      {      
         oOrgContactnation += $(this).val()+",";
      }
   });
   $("select[id='oocontidtp']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oOrgContactidtp += "$,";
      }   
      else
      {   
         oOrgContactidtp += $(this).val()+",";
      }         
   });
   $("input[id='oocontidno']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oOrgContactidno += "$,";
      }  
      else
      {    
         oOrgContactidno += $(this).val()+",";
      }
   });
   //由于id用于做长期有效，不好取值，现改为用name取值
   $("input[name='oocontvalidate']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oOrgContactidvalidate += "$,";
      } 
      else
      {       
         oOrgContactidvalidate += $(this).val()+",";
      }
   });
   $("input[id='oocontphone']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oOrgContacttel += "$,";
      } 
      else
      {      
         oOrgContacttel += $(this).val()+",";
      }
   });
   $("input[id='oocontfax']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oOrgContactfax += "$,";
      }
      else
      {      
         oOrgContactfax += $(this).val()+",";
      }
   });
   $("input[id='oocontmobile']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oOrgContactmobile += "$,";
      } 
      else
      {     
         oOrgContactmobile += $(this).val()+",";
      }
   });
   $("input[id='oocontemail']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oOrgContactemail += "$,";
      } 
      else
      {      
         oOrgContactemail += $(this).val()+",";
      }
   });
   $("input[id='oocontAddr']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oOrgContactAddr += "$,";
      } 
      else
      {      
         oOrgContactAddr += $(this).val()+",";
      }
   });
   $("input[id='oocontPostcode']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oOrgContactPostcode += "$,";
      } 
      else
      {
         oOrgContactPostcode += $(this).val()+",";
      }
   });
   orgInfo.oOrgContactright = oOrgContactright; 
   orgInfo.oOrgContact = oOrgContact;  
   orgInfo.oOrgContactnation = oOrgContactnation; 
   orgInfo.oOrgContactidtp = oOrgContactidtp;    
   orgInfo.oOrgContactidno = oOrgContactidno;     
   orgInfo.oOrgContactidvalidate = oOrgContactidvalidate;    
   orgInfo.oOrgContacttel = oOrgContacttel;  
   orgInfo.oOrgContactfax = oOrgContactfax;   
   orgInfo.oOrgContactmobile = oOrgContactmobile;  
   orgInfo.oOrgContactemail = oOrgContactemail;
   orgInfo.oOrgContactAddr = oOrgContactAddr;
   orgInfo.oOrgContactPostcode = oOrgContactPostcode;
   return orgInfo;   
}
//--------------------------------------------------------------取注册信息----------------------------------------------------------------------

//------------------------------------其他证件和经办人信息---------------------------------------------------------------------------------------- 
//增加证件信息
var idflag=0;//用于动态设置证件有效期的长期有效功能，由于ID改变，原取值方式也需要修改
var valueFlag = 0;
function addIDRow(idtp,idno,idvalidate)
{
	idtp = (idtp == undefined || idtp == null) ? "" : idtp;
	idno = (idno == undefined || idno == null) ? "" : idno;
	idvalidate = (idvalidate == undefined || idvalidate == null) ? "" : idvalidate;
	
	idflag = Number(idflag) + 1;
   //修改行数
   var rownum = $("#oidinfo>tr:first>th:first").attr('rowspan')+4;
   var idNum = Math.floor(rownum/4);
   $("#oidinfo>tr:first>th:first").attr('rowspan',rownum);      
   //第一行
   var trstr = "<tr><td style='text-align: center;' colspan='4'>证件信息</td></tr>";
   //第二行      
   trstr +="<tr><td>证件类型：</td><td class='white-bg orgInvIdtpTd'>";
   trstr += $(".orgInvIdtpDiv").clone(true).find("select").attr("valueFlag"+valueFlag,idtp).attr("value",idtp).end().html();
   trstr +="</td></tr>";
   //第三行
   trstr +="<tr>";
   trstr +="<td>证件号码：</td>";
   trstr +="<td class='white-bg'><div class='col-sm-11 form-inner'><input type='text' name='ooidno' id='ooidno' class='form-control' value='"+idno+"' maxlength='30'></div></td>";
	trstr +="<td>证件有效期：</td>";
	trstr +="<td><div class='col-sm-11 form-inner'><input name='ooidvalidate' style='float: left;' class='form-control' id='ooidvalidate"+idflag+"' type='text' value='"+idvalidate+"'/>";
	trstr +='<label style="line-height: 34px;margin: 0;">';
	trstr +='<input class="i-checks" id="changeooidvalidate'+idflag+'" type="checkbox" ';
	trstr +='onclick="changetime(\'changeooidvalidate'+idflag+'\',\'ooidvalidate'+idflag+'\');"/>长期</label><br></div>';
	trstr +="</td></tr>";
   //第四行
   trstr +="<tr><td colspan = '4' align='right'><input class='btn btn-primary btn-add' type=button value='删除' id='btnDelId"+idNum+"' onclick='delIdRow(this);'/></td></tr>";
   $("#oidinfo").append(trstr);
   $(".orgInvIdtpTd .orgInvIdtp").addClass("select2");
   $('.orgInvIdtpTd .orgInvIdtp').select2({allowClear: false,minimumResultsForSearch:Infinity}).trigger('change');
   
   if(!!idtp){
	   $("#oidinfo select[valueFlag"+valueFlag+"]").val($("#oidinfo select[valueFlag"+valueFlag+"]").attr("value"));
	   $("#oidinfo select[valueFlag"+valueFlag+"]").change();
   }
   WASP_WIDGET.triggerDateStyleWithYMD("ooidvalidate"+idflag);
   ++valueFlag;
}  

//增加经办人
var contflag=0;//用于动态设置证件有效期的长期有效功能，由于ID改变，原取值方式也需要修改
var contValueFlag = 0;
function addContRow(contgrant,contnm,contnation,contidtp,contidno,contidvalidate,conttel,contfax,contmobile,contemail,contAddr,contPostcode)
{
	
	contgrant = contgrant == undefined ? "" : contgrant;
	contnm = contnm == undefined ? "" : contnm;
	contnation = contnation == undefined ? "" : contnation;
	contidtp = contidtp == undefined ? "" : contidtp;
	contidno = contidno == undefined ? "" : contidno;
	contidvalidate = contidvalidate == undefined ? "" : contidvalidate;
	conttel = conttel == undefined ? "" : conttel;
	contfax = contfax == undefined ? "" : contfax;
	contmobile = contmobile == undefined ? "" : contmobile;
	contemail = contemail == undefined ? "" : contemail;
	contAddr = contAddr == undefined ? "" : contAddr;
	contPostcode = contPostcode == undefined ? "" : contPostcode;
	
 //包含相同的经办人，只同步一次
if(!!contnm && !!contidno){
	if($("input[id='oocontact'][value='"+contnm+"']").length > 0 
			   &&  $("input[id='oocontidno'][value='"+contidno+"']").length > 0){
		   return;
	 }
}
	   
 //修改行数
 var rownum = $("#ocontInfo>tr:first>th:first").attr('rowspan')+8;
 var idNum = Math.floor(rownum/8);
 contflag = Number(contflag)+1;
 var flag = 0;
 $("#ocontInfo>tr:first>th:first").attr('rowspan',rownum);
 
 //第一行      
 var trstr = "<tr><td style='text-align: center;' colspan='4'>经办人信息</td></tr>";
 //第二行
 trstr +="<tr><td><font color='red'>*</font>经办人授权范围：</td><td class='orgContactrightTd white-bg'>";
 trstr += $(".orgContactrightDiv").clone(true).find("select").attr("id","oocontactgrant").attr("ocontactgrantFlag"+contValueFlag,contgrant).attr("value",contgrant).end().html();
 
 trstr +="</td><td><font color='red'>*</font>经办人姓名：</td><td class='white-bg'>";
 trstr += $("#continfo>tr:eq(0)").find('td').eq(3).clone(true).find("#orgContnm").attr("id","oocontact").attr("value",contnm).end().html();
 trstr +="</td></tr>";               
 //第三行
 trstr +="<tr>";
 trstr +="<td>经办人国籍：</td><td class='orgContnationTd white-bg'>"+$(".orgContnationDiv").clone(true).find(".orgContnation").attr("id","oocontactnation").attr("oocontactnationFlag"+contValueFlag,contnation).attr("value",contnation).end().html()+"</td>";
 trstr +="<td><font color='red'>*</font>经办人证件类型：</td>" +
 		   "<td class='orgContidtpTd white-bg'>"+$(".orgContidtpDiv").clone(true).find(".orgContidtp").attr("id","oocontidtp").attr("oocontidtpFlag"+contValueFlag,contidtp).attr("value",contidtp).end().html()+"</td>";
 trstr +="</tr>";
 //第四行
 trstr +="<tr>";
 trstr +="<td><font color='red'>*</font>经办人证件号码：</td>";
 trstr +="<td class='white-bg'><div class='col-sm-11 form-inner'><input type='text' name='oocontidno' id='oocontidno' class='form-control' value='"+contidno+"' maxlength='18'></div></td>";
 trstr +="<td><font color='red'>*</font>经办人证件有效期：</td>";
 trstr +="<td class='white-bg'><div class='col-sm-11 form-inner'>" +
 		   "<input name='oocontvalidate' style='float: left;' class='form-control' id='oocontvalidate"+contflag+"' type='text' value='"+contidvalidate+"'/>";
 trstr +='<label style="line-height: 34px;">';
 trstr +='<input class="i-checks" id="changeocontvalidate'+contflag+'" type="checkbox" ';
 trstr +='onclick="changetime(\'changeocontvalidate'+contflag+'\',\'oocontvalidate'+contflag+'\');"/>长期</label><br></div>';
 trstr +="</td></tr>";
 //第五行
 trstr +="<tr>";
 trstr +=$("#continfo>tr:eq(3)").clone(true).find("#orgContphone").attr("id","oocontphone").attr("value",conttel).end().find("#orgContfax").attr("id","oocontfax").attr("value",contfax).end().html();
 trstr +="</tr>"; 
 //第六行
 trstr +="<tr>";
 trstr +=$("#continfo>tr:eq(4)").clone(true).find("#orgContmobile").attr("id","oocontmobile").attr("value",contmobile).end().find("#orgContemail").attr("id","oocontemail").attr("value",contemail).end().html();
 trstr +="</tr>";
 //第七行
 trstr +="<tr>";
 trstr +=$("#continfo>tr:eq(5)").clone(true).find("#orgContAddr").attr("id","oocontAddr").attr("value",contAddr).end().find("#orgContPostcode").attr("id","oocontPostcode").attr("value",contPostcode).end().html();
 trstr +="</tr>";
 //第八行
 trstr +="<tr><td colspan='4' align='right'><input class='btn btn-primary btn-add' type=button value='删除' id='btnDelCont"+idNum+"' onclick='delContRow(this);'/></td></tr>";
 $("#ocontInfo").append(trstr);
 
 
 if (!!contgrant) {
	$("select[ocontactgrantFlag"+contValueFlag+"]").val($("select[ocontactgrantFlag"+contValueFlag+"]").attr("value"));
	$("select[ocontactgrantFlag"+contValueFlag+"]").change();
 }
 if (!!contnation) {
	$("select[oocontactnationFlag"+contValueFlag+"]").val($("select[oocontactnationFlag"+contValueFlag+"]").attr("value"));
	$("select[oocontactnationFlag"+contValueFlag+"]").change();
 }
 if (!!contidtp) {
	$("select[oocontidtpFlag"+contValueFlag+"]").val($("select[oocontidtpFlag"+contValueFlag+"]").attr("value"));
	$("select[oocontidtpFlag"+contValueFlag+"]").change();
 }

 $(".orgContactrightTd select").addClass("select2");
 $('.orgContactrightTd select').select2({allowClear: false,minimumResultsForSearch:Infinity}).trigger('change');
 
 $(".orgContnationTd select").addClass("select2");
 $(".orgContnationTd select").select2({allowClear: false,minimumResultsForSearch:Infinity}).trigger('change');
 
 $(".orgContidtpTd select").addClass("select2");
 $(".orgContidtpTd select").select2({allowClear: false,minimumResultsForSearch:Infinity}).trigger('change');
 
 WASP_WIDGET.triggerDateStyleWithYMD("oocontvalidate"+contflag);
 ++flag;
 ++contValueFlag;
}

//删除证件信息
function delIdRow(rownum)
{
   var count = $("#oidinfo>tr").length;
   var ss = 0;
   for(var i=0;i<count;i++)
   {
      if($("#oidinfo>tr").eq(i).html()==$(rownum).parents().parents().html())
      {
         ss = i;
      }
   }
   var trRow = ss-3;
   for(var i=0;i<4;i++)
   {          
      $("#oidinfo>tr").eq(trRow).remove();           
   }
}

//删除经办人信息
function delContRow(rownum)
{
   var count = $("#ocontInfo>tr").length;
   var ss = 0;
   for(var i=0;i<count;i++)
   {
      if($("#ocontInfo>tr").eq(i).html()==$(rownum).parents().parents().html())
      {
         ss = i;
      }
   }
   var trRow = ss-7;
   for(var i=0;i<8;i++)
   {          
      $("#ocontInfo>tr").eq(trRow).remove();           
   }
}   
//------------------------------------其他证件和经办人信息----------------------------------------------------------------------------------------
//------------------------------------------------------------资料信息--------------------------------------------------------------------------
$.fn.selVal = function(v)
{      
   if($(this).val() != v)
   {
	     $(this).val(v).trigger("change");
	  }
	  else
	  {
	     //doSelect(v);wdw 2010-09-14注释掉
	  }
};

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
	var invtp = $("#selOpenType").val();
	var invprtp = $("#invprtp").val();
	var invProDocMap = {} ;
	invProDocMap.ap01 = "07";
	invProDocMap.ap02 = "23";
	var trstr = "<td class='white-bg' colspan='3' style='padding: 0px;'>";
		trstr += "<table style='border: 0;margin-bottom: 0;' class='table table-bordered maintable' id='documentTable'";
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
   				if(doclist[j].existsflag=='1' && doclist[j].custdocument==object[i].PMCO && docbusinesstp==doclist[j].docbusinesstp){
   					flag = true;
   				}
   			}
   			if (flag) {
   				trstr+="<label><input class='i-checks' type='checkbox' checked id='"+object[i].PMCO+"' name='document' value='"+object[i].PMCO+"' >"+object[i].PMNM+"</label>";
			}else{
				trstr+="<label><input class='i-checks' type='checkbox' id='"+object[i].PMCO+"' name='document' value='"+object[i].PMCO+"' >"+object[i].PMNM+"</label>";
			}
		}else{
			trstr+="<label><input class='i-checks' type='checkbox' id='"+object[i].PMCO+"' name='document' value='"+object[i].PMCO+"' >"+object[i].PMNM+"</label>";
		}
		//trstr+="<label><input class='i-checks' type='checkbox' name='document' id='"+object[i].PMCO+"' value='"+object[i].PMCO+"' >"+object[i].PMNM+"</label>";
		trstr+="</div></td></tr>";
   }
    trstr+="</table></td>";
	$("#documentinfo").append(trstr);               
	$("#documentTable").find("td:last").css("border-bottom",'0');
	var isalldoc = $("#isalldoc").val();
	if(isalldoc == "1"){
		$("#isalldoc").change();
	}
}

//------------------------------------------------------------资料信息--------------------------------------------------------------------------
   
//------------------------------------------------------------开户查询和提交---------------------------------------------------------------------
function queryInfo() //查询客户信息
{
   var selOpenType = $("#selOpenType").val();
   var inputQueryFundAcc = $("#inputQueryFundAcc").val();
   var inputQueryFundAcc1 = $("#inputQueryFundAcc1").val();
   
   var inputQueryTradeAcc = $("#inputQueryTradeAcc").val();
   var inputQueryTradeAcc1 = $("#inputQueryTradeAcc1").val();
   
   var fundAcc = "";
   var tradeAcc = "";
   if("1" == selOpenType ){
	   fundAcc = inputQueryFundAcc;
	   tradeAcc = inputQueryTradeAcc;
   }else{
	   fundAcc = inputQueryFundAcc1;
	   tradeAcc = inputQueryTradeAcc1;
   }
   
   $.ajax({
		url: ACCOUNT_PATH+"queryAccoByfundAcc.do",  
		dataType: "json",
		data:{
			'fundAcc' : fundAcc,
			'tradeAcc' : tradeAcc
		},
		type: "POST",
		cache: false,
		async: false,
		success: function(data) {
			if(data.errcode == "9000"){
	          	ctools.alert("客户号不存在！","","error");
	          	return;
	        }if(data.errcode == "9999"){
	          	ctools.alert("查询异常！","","error");
	          	return;
	        }else{
	        	handlequeryInfo(data);
	        }
		}
	});
} 

function handlequeryInfo(obj)
{ 
   if(obj.invtp ==1)
   {
      fillPersonalInfo(obj);
   }
   else
   {
      fillOrgInfo(obj);
   }
   doclist = obj.documentlist;
   doSelect($("#docbusinesstp").val());
}

function fillPersonalInfo(obj) {
	$("#pslInvNm").val(obj.invnm);
	$("#pslInvIdtp").val(obj.idtp);
	$("#pslInvIdno").val(obj.idno);
	$("#pslInvIdValidate").val(obj.idvalidate);

	$("#selbankno").val(obj.bnkNo);
	$("#openname").val(obj.openName);
	$("#openlocation").val(obj.openAddr);
	$("#hopenbankcity").val(obj.openBankCity);
	$("#bankacconm").val(obj.bankAccoNm);
	$("#bankacco").val(obj.bankAcco);

	$("#pslInvOfficeTel").val(obj.tel);
	$("#pslInvHomeTel").val(obj.housetel);
	$("#pslInvMobile").val(obj.mobile);
	$("#pslInvFax").val(obj.fax);
	$("#plsFaxDelegate").val(obj.faxdelegate);
	$("#pslInvEmail").val(obj.email);
	$("#pslAddr").val(obj.addr);
	$("#pslPostCode").val(obj.postcode);
	$("#pslShsecacc").val(obj.shsecacc);
	$("#pslSzsecacc").val(obj.szsecacc);

	$("#plsSex").val(obj.sex);
	$("#plsInvNation").val(obj.nationalitycode);
	$("#plsInvEducation").val(obj.edlevel);
	$("#plsInvJob").val(obj.voccode);
	$("#plsInvIncome").val(obj.income);
	$("#plsInvRisk").val(obj.custrisklevl);
	if ("1" == obj.specriskLevel) {
		$("#plsInvRisk").val(1);
		$("#indSpecRiskLevel").click();
	}
	$("#custsimpnm").val(obj.custabbrcode);
	$("#instrepcode").val(obj.instrepcode);
	if(!!obj.businesstp){
		$("#businesstp").val(obj.businesstp);
	}
	if(!!obj.industryType){
		$("#corptype").val(obj.industryType);
	}
	if(!!obj.regiontp){
		$("#regioncode").val(obj.regiontp);
	}
	if(!!obj.amlrisktype){
		$("#fxqtype").val(obj.amlrisktype);
	}
	$("#fxqdesc").val(obj.fxqremark);

	$("#docbusinesstp").val(obj.docbusinesstp);
	$("#isalldoc").val(obj.ifalldocument);
	$("#isoriginal").val(obj.ifOriginal);
	$("#issaved").val(obj.ifsaved);
	$("#isupload").val(obj.isupload);
	$("#remarkinfo").val(obj.remarkinfo);
	$("#isscan").val(obj.isscan);
	$("#salesaccmanager").val(obj.salesaccmanager);

	$("#taxType").val(obj.taxType);
	$("#taxType").change();
	$("#taxTypeDecl").val(obj.taxTypeDecl);

	// 分类->投资者分类
	$("#invprtp").val(obj.invprtp);
	$("#invprtp").change();
	if ("0" == obj.invprtp) {
		$("#threeAnnualIncome").val(obj.threeAnnualIncome);
		$("#financialAsset").val(obj.financialAsset);
		$("#indInvExperience").val(obj.indInvExperience);
		$("#relatedWorkExp").val(obj.relatedWorkExp);
		$("#finProfessions").val(obj.finProfessions);
	}
	
	$("#tabPslBaseInfo select").change();
	$("#categoryInfoContent select").change();
	$("#extInfoContent select").change();
	$("#documentInfoContent select").change();
	
	// 加载城市
	getCitys(obj.openAddr, '1'); 
}

function fillOrgInfo(obj) {
	// 基本->证件信息
	$("#orgInvNm").val(obj.invnm);
	$("#orgInvIdtp").val(obj.idtp);
	$("#orgInvIdno").val(obj.idno);
	$("#orgInvIdvalidate").val(obj.idvalidate);

	// 基本->银行信息
	$("#orgselbankno").val(obj.bnkNo);
	$("#orgopenname").val(obj.openName);
	$("#orgopenlocation").val(obj.openAddr);
	// $("#orgopenbankcity").val(obj.openBankCity);
	$("#horgopenbankcity").val(obj.openBankCity);
	$("#orgbankacconm").val(obj.bankAccoNm);
	$("#orgbankacco").val(obj.bankAcco);

	// 基本->法人信息
	$("#orgInstrepnm").val(obj.instrepnm);
	$("#orgInstrepnation").val(obj.instrepnation);
	$("#orgInstrepidtp").val(obj.instrepidtp);
	$("#orgInstrepidno").val(obj.instrepidno);
	$("#orgInstrepidvalidate").val(obj.instrepvalidate);

	// 基本->负责人信息
	$("#orgPrincipalname").val(obj.principalname);
	$("#orgPrincipalnation").val(obj.principalnation);
	$("#orgPrincipalidtp").val(obj.principalidtp);
	$("#orgPrincipalidno").val(obj.principalidno);
	$("#orgPrincipalidvalidate").val(obj.principalvalidt);

	// 基本->经办人员信息
	$("#orgContactright").val(obj.contactgrant);
	$("#orgContnm").val(obj.contact);
	$("#orgContnation").val(obj.contactnation);
	$("#orgContidtp").val(obj.contidtp);
	$("#orgContidno").val(obj.contidno);
	$("#orgContidvalidate").val(obj.contvalidate);
	$("#orgContphone").val(obj.contphone);
	$("#orgContfax").val(obj.contfax);
	$("#orgContmobile").val(obj.contmobile);
	$("#orgContemail").val(obj.contemail);
	$("#orgContAddr").val(obj.contAddr);
	$("#orgContPostcode").val(obj.contPostcode);
	// 基本->其他信息
	$("#orgAddr").val(obj.addr);
	$("#orgPostcode").val(obj.postcode);
	$("#orgInvOfficeTel").val(obj.tel);
	$("#orgInvFax").val(obj.fax);
	$("#orgShsecacc").val(obj.shsecacc);
	$("#orgSzsecacc").val(obj.szsecacc);

	// 附加->其他信息
	$("#orgHoldingname").val(obj.holdingname);
	$("#orgHoldingidtp").val(obj.holdingidtp);
	$("#orgHoldingidno").val(obj.holdingidno);
	$("#orgHoldingidvalidate").val(obj.holdingvalidate);
	$("#orgBeneficiarynm").val(obj.beneficiary);
	$("#orgRiskLevel").val(obj.custrisklevl);
	if ("1" == obj.specriskLevel) {
		$("#orgRiskLevel").val(1);
		$("#instSpecRiskLevel").click();

	}
	$("#orgRiskLevel").change();
	// 分类信息
	$("#custsimpnm").val(obj.acctabbr);
	$("#hinstrepcode").val(obj.instrepcode);
	$("#instrepcode").val(obj.instrepcode);
	if (obj.acctabbr != '') {
		getSecond(obj.acctabbr);// 加载二级简称
	}

	$("#taxType").val(obj.taxType);
	$("#taxType").change();
	$("#taxTypeDecl").val(obj.taxTypeDecl);

	// 分类->投资者分类
	$("#invprtp").val(obj.invprtp);
	$("#invprtp").change();
	if ("0" == obj.invprtp) {
		$("#investProInstType").val(obj.investProInstType);
		$("#investProInstType").change();
		$("#investProInstSecond").val(obj.investProInstSecond);
		$("#oneYearEndNetAsset").val(obj.oneYearEndNetAsset);
		$("#oneYearEndFinAsset").val(obj.oneYearEndFinAsset);
		$("#investExperience").val(obj.investExperience);
		$("#negativeNotFinaInst").val(obj.negativeNotFinaInst);
		$("#negativeNotFinaInst").change();
		$("#controlPerTaxDecl").val(obj.controlPerTaxDecl);
	}

	if(!!obj.businesstp){
		$("#businesstp").val(obj.businesstp);
	}
	if(!!obj.industryType){
		$("#corptype").val(obj.industryType);
	}
	if(!!obj.regiontp){
		$("#regioncode").val(obj.regiontp);
	}
	if(!!obj.amlrisktype){
		$("#fxqtype").val(obj.amlrisktype);
	}
	
	
	
	$("#fxqdesc").val(obj.fxqremark);
	// 资料信息
	$("#docbusinesstp").val(obj.docbusinesstp);
	$("#isalldoc").val(obj.ifalldocument);
	$("#isoriginal").val(obj.ifOriginal);
	$("#issaved").val(obj.ifsaved);
	$("#isupload").val(obj.isupload);
	$("#isscan").val(obj.isscan);
	$("#salesaccmanager").val(obj.salesaccmanager);
	$("#remarkinfo").val(obj.remarkinfo);
	
	$("#tabOrgBaseInfo select").change();
	$("#categoryInfoContent select").change();
	$("#extInfoContent select").change();
	$("#documentInfoContent select").change();
	
	emptyInfo();
	var oidlist = obj.oidlist;
	var ocontlist = obj.ocontactlist;
	if (!!oidlist && oidlist.length > 0) {
		for (var i = 0; i < oidlist.length; i++) {
			addIDRow(oidlist[i].idtp, oidlist[i].idno, oidlist[i].idvalidate);
		}
	}
	if (!!ocontlist && ocontlist.length > 0) {
		for (var j = 0; j < ocontlist.length; j++) {
			addContRow(ocontlist[j].contactgrant, ocontlist[j].contact,
					ocontlist[j].contactnation, ocontlist[j].contidtp,
					ocontlist[j].contidno, ocontlist[j].contvalidate,
					ocontlist[j].contphone, ocontlist[j].contfax,
					ocontlist[j].contmobile, ocontlist[j].contemail,
					ocontlist[j].contAddr, ocontlist[j].contPostcode);
		}
	}
	
	// 加载城市
	getCitys(obj.openAddr, '0');
}         
      
function dosubmit()
{
	var baseInfo;
	var opentp = $("#selOpenType").val();
	if(opentp=="1")//个人开户
	{         
		baseInfo = getPersonalInfo();
	}
	else{
		autofillContid();
		baseInfo = getOrgInfo(); 
	}
   
	if(!validateinfo(baseInfo)){
		return false;
	}
   
	var flag = checkResidentInfo();
	if(!flag){
		return false;
	}
	//税收居民信息       
	baseInfo.taxresident = $("input[name=taxresident]").val();
   
	var fileno=$("#fileno").val();
   
	$.ajax({
		type : "post",
		url : ACCOUNT_PATH+"getFileNo.do",
		data: {
			'fileNo' : fileno
		},
		dataType : "json",
		success : function(data) {
			if(0 == data.fileCount){
				$.ajax({
					url: ACCOUNT_PATH+"openCustom.do",  
					dataType: "json",
					type: "POST",
					data:  {
						"baseInfo" : JSON.stringify(baseInfo)
					},
					cache: false,
					async: false,
					success: function(data) {
						handleOpen(data);
					}
				});
	     	 }else{
	     		 ctools.alert("文件编号："+$("#fileno").val()+"已存在,已经重新初始化编号,请重新提交！","","error");
	     		 queryFileNo();
	     	 }
		}
	});
}

function handleOpen(obj)
{
   var errcode = obj.errcode;
   var errMsg =obj.errmsg;
   
   if(errcode == "0000"){
   		ctools.alert_sweet('开户成功！', "success", "" , function(){
			window.location.reload();
		});
   }else{
   		ctools.alert_sweet('开户失败！', "error", "失败原因："+errMsg);
   }
}


function autofillContid(){
	   //当基本信息中的经办人信息没有填写的时候，自动同步经办人信息
	   if($("#orgContnm").val() == '' && $("#orgContidno").val() == ''){
		 var tarIdArr = ['orgContactright',	'orgContnm',
		 				'orgContnation',	'orgContidtp',
		 				'orgContidno',		'orgContidvalidate',
		 				'orgContphone',		'orgContfax',
		 				'orgContmobile',	'orgContemail',
		 				'orgContAddr',		'orgContPostcode'];
		 var dataIdArr = ['oocontactgrant',	'oocontact',
		 				'oocontactnation',	'oocontidtp',
		 				'oocontidno',		'oocontvalidate',
		 				'oocontphone',		'oocontfax',
		 				'oocontmobile',		'oocontemail',
		 				'oocontAddr',		'oocontPostcode'];
		 for(var i = 0 ; i<tarIdArr.length;i++){
			 try{
				 $("#"+tarIdArr[i]).val($("#"+dataIdArr[i]).val());
				 //证件有效期的ID有变化，在最后特殊处理
				 $("#orgContidvalidate").val($("[name='oocontvalidate']:eq(0)").val());
			 }catch(e){}
		 }
	 }
}

function validateinfo(baseInfo){
	var nowdate = nowDate;
	if (baseInfo.opentype == '008') {
		if (baseInfo.fundacc == '') {
			ctools.alert("已有基金账号开户必须输入基金账号!","","error");
			$("#fundacc").focus();
			return false;
		}
	}
	if (baseInfo.custTp == 1) {
		if (baseInfo.tano == "") {
			ctools.alert("请选择TA类型！","","error");
			return false;
		}
		if (baseInfo.invNm == "") {
			ctools.alert("请输入投资者名称！","","error");
			$("#pslInvNm").focus();
			return false;
		}
		if (baseInfo.invIdno == "") {
			ctools.alert("请输入注册登记证件号码！","","error");
			$("#pslInvIdno").focus();
			return false;
		}
		if (baseInfo.invIdValidate == ''
				|| baseInfo.invIdValidate <= nowdate) {
			ctools.alert("注册登记证件有效期为空或已过期!","","error");
			return false;
		}
		if (baseInfo.openname == "") {
			ctools.alert("请输入预留银行全称！","","error");
			$("#openname").focus();
			return false;
		}
		if (baseInfo.openlocation == "") {
			ctools.alert("请选择预留银行开户地省!","","error");
			$("#openlocation").focus();
			return false;
		}
		if (baseInfo.openbankcity == "") {
			ctools.alert("请选择预留银行开户地市!","","error");
			$("#openbankcity").focus();
			return false;
		}
		if (baseInfo.bankacconm == "") {
			ctools.alert("请输入预留银行户名！","","error");
			$("#bankacconm").focus();
			return false;
		}
		if (baseInfo.bankacco == "") {
			ctools.alert("请输入预留银行账号！","","error");
			$("#bankacco").focus();
			return false;
		}
		if (baseInfo.pslInvOfficeTel == '' && baseInfo.pslInvHomeTel == ''
				&& baseInfo.pslInvMobile == '') {
			ctools.alert("办公电话、住宅电话、移动电话至少要填一项!","","error");
			return false;
		}

		var typeAddr = "通讯地址";
		if (baseInfo.addr == "") {
			ctools.alert("请输入" + typeAddr + "！","","error");
			$("#pslAddr").focus();
			return false;
		}
		if (trim(baseInfo.postcode) == "") {
			ctools.alert("请输入邮政编码！","","error");
			$("#pslPostCode").focus();
			return false;
		}
	} else {

		if (baseInfo.invNm == "") {
			ctools.alert("请输入投资者名称！","","error");
			$("#orgInvNm").focus();
			return false;
		}
		if (baseInfo.tano == "") {
			ctools.alert("请选择TA类型！","","error");
			return false;
		}
		if (baseInfo.invIdno == "") {
			ctools.alert("请输入注册登记证件号码！","","error");
			$("#orgInvIdno").focus();
			return false;
		}
		if (baseInfo.invIdValidate == ''
				|| baseInfo.invIdValidate <= nowdate) {
			ctools.alert("注册登记证件有效期为空或已过期!","","error");
			return false;
		}
		if (baseInfo.openname == "") {
			ctools.alert("请输入预留银行全称！","","error");
			$("#orgopenname").focus();
			return false;
		}
		if (baseInfo.openlocation == "") {
			ctools.alert("请选择预留银行开户地省!","","error");
			$("#orgopenlocation").focus();
			return false;
		}
		if (baseInfo.openbankcity == "") {
			ctools.alert("请选择预留银行开户地市!","","error");
			$("#orgopenbankcity").focus();
			return false;
		}
		if (baseInfo.bankacconm == "") {
			ctools.alert("请输入预留银行户名！","","error");
			$("#orgbankacconm").focus();
			return false;
		}
		if (baseInfo.bankacco == "") {
			ctools.alert("请输入预留银行账号！","","error");
			$("#orgbankacco").focus();
			return false;
		}
		if (baseInfo.orgInstrepnm == "") {
			ctools.alert("请输入法定代表人姓名！","","error");
			$("#orgInstrepnm").focus();
			return false;
		}
		if (baseInfo.orgInstrepidno == "") {
			ctools.alert("请输入法定代表人证件号码！","","error");
			$("#orgInstrepidno").focus();
			return false;
		}
		if (baseInfo.orgInstrepidvalidate == ''
				|| baseInfo.orgInstrepidvalidate <= nowdate) {
			ctools.alert("法定代表人证件有效期为空或已过期!","","error");
			return false;
		}
		if (baseInfo.orgPrincipalname == "") {
			ctools.alert("请输入机构负责人姓名！","","error");
			$("#orgPrincipalname").focus();
			return false;
		}
		if (baseInfo.orgPrincipalidno == "") {
			ctools.alert("请输入机构负责人证件号码！","","error");
			$("#orgPrincipalidno").focus();
			return false;
		}
		if (baseInfo.orgPrincipalidvalidate == ''
				|| baseInfo.orgPrincipalidvalidate <= nowdate) {
			ctools.alert("机构负责人证件有效期为空或已过期!","","error");
			return false;
		}
		if (baseInfo.orgContnm == "") {
			ctools.alert("请输入经办人姓名！","","error");
			$("#orgContnm").focus();
			return false;
		}
		if (baseInfo.orgContidno == "") {
			ctools.alert("请输入经办人证件号码！","","error");
			$("#orgContidno").focus();
			return false;
		}
		if (baseInfo.orgContidvalidate == ''
				|| baseInfo.orgContidvalidate <= nowdate) {
			ctools.alert("经办人证件有效期为空或已过期!","","error");
			return false;
		}
		if (baseInfo.orgContphone == "") {
			ctools.alert("请输入经办人办公电话！","","error");
			$("#orgContphone").focus();
			return false;
		}
		if (baseInfo.orgContfax == "") {
			ctools.alert("请输入经办人传真号码！","","error");
			$("#orgContfax").focus();
			return false;
		}
		var typeAddr = "办公地址";
		if (baseInfo.addr == "") {
			ctools.alert("请输入" + typeAddr + "！","","error");
			$("#pslAddr").focus();
			return false;
		}
		if (trim(baseInfo.postcode) == "") {
			ctools.alert("请输入邮政编码！","","error");
			$("#orgPostcode").focus();
			return false;
		}
		if (baseInfo.orgHoldingidvalidate != ''
				&& baseInfo.orgHoldingidvalidate <= nowdate) {
			ctools.alert("控股股东证件已过期!","","error");
			return false;
		}
	}
	return true;
}
//------------------------------------------------------------开户查询和提交---------------------------------------------------------------------

//wdw add 20100727
//获取客户二级分组
function getSecond(pmco) {
	var html = "<option value=''>--</option>";
	$("#instrepcode").html(html);
	if(pmco == ""){
		$("#instrepcode").change();
		return;
	}
	$.ajax({
		type : "post",
		url : PRIMARY_PATH+"queryMatchParamList.do?pmst=DSCUSTGROUP&pmky=SECONDGROUP&pmv1="+pmco,
		dataType : "json",
		contentType : 'application/json;charset=utf-8',
		success : function(data) {
			if (!!data) {
				for (var i = 0; i < data.length; i++) {
					var item = data[i];
					html+="<option value=\""+item.PMCO+"\">"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
				}
				$("#instrepcode").html(html);
			}
		},
		error : function() {
			ctools.alert("客户二级分组查询失败！","","error");
		}
	});
	$("#instrepcode").change();
}

//wdw add 20100906
//获取城市集合
function getCitys(pmco, flag) {
	if (flag == '1') {
		city = $("#openbankcity");
		selectCity = "#hopenbankcity";
	} else {
		city = $("#orgopenbankcity");
		selectCity = "#horgopenbankcity";
	}
	var html = "<option value=''>--</option>";
	if(pmco == ""){
		$(city).html(html);
		$(city).change();
		return;
	}
	
	$.ajax({
		type : "post",
		url : PRIMARY_PATH+"queryMatchParamList.do?pmst=SYSTEM&pmky=DS_CITYCODE&pmv1="+pmco,
		dataType : "json",
		contentType : 'application/json;charset=utf-8',
		success : function(data) {
			if (!!data) {
				for (var i = 0; i < data.length; i++) {
					var item = data[i];
					if(item.PMCO == $(selectCity).val()){
						html+="<option value=\""+item.PMCO+"\" selected>"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
					}else{
						html+="<option value=\""+item.PMCO+"\">"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
					}
				}
				$(city).html(html);
				$(city).select2("destroy");
				$(city).select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
			}
		},
		error : function() {
			ctools.alert("城市信息查询失败！","","error");
		}
	});
}

function changetime(checkboxId, inputId) {
	var isChecked = $("#"+checkboxId).is(':checked');
	
	if (isChecked) {
		$("#"+inputId).val("2099-12-31");
		$("#"+inputId).attr("disabled",true);
		$("#"+inputId).blur();
	} else {
		$("#"+inputId).val("");
		$("#"+inputId).attr("disabled",false);
		$("#"+inputId).focus();
	}
	$("#"+inputId).change();
}
function trim(str) { //删除左右两端的空格
	if(!!str){
		return str.replace(/(^\s*)|(\s*$)/g, "");	
	}else{
		return str;
	}
}
//修改法人信息时默认带出机构负责人信息
function changeInstrep(instrep, princ, isSelect) {
	var isVisible  = $("#"+princ).is(":visible");
	if (isVisible) {
		if(isSelect){
			$("#"+princ).val($(instrep).val());
			$("#"+princ).change();
		}else{
			$("#"+princ).val($(instrep).val());
		}
	}
	
}
//改变基金账号录入状态
function changeFundaccstatus(flag) {
	if (flag) {
		$("#fundacc").attr("disabled",true);
	} else {
		$("#fundacc").attr("disabled",false);
	}
}
//基金余额查询
function querybalance(fundacc) {
	var inputQueryFundAcc = trim(document.getElementById(fundacc).value);
	if (inputQueryFundAcc == '') {
		ctools.alert("基金账号为空！","","error");
		return false;
	}  
	openDialog(ACCOUNT_PATH+"openFundBalanceView.do?fundacct=" + inputQueryFundAcc);
	//window.open(ACCOUNT_PATH+"openFundBalanceView.do?fundacct=" + inputQueryFundAcc);
}
//控制输入内容
function ctrlInput(inputObj, outObj, valueLength) {
	var inputValue = document.getElementById(inputObj).value;
	if (inputValue == '') {
		return;
	}
	document.getElementById(outObj).innerHTML = "";
	var retValue = "";
	var len = 0;
	for (var i = 0; i < inputValue.length; i++) {
		var strCode = inputValue.charCodeAt(i);
		if (parseInt(strCode) == 8212) {//全角或中文状态下输入'_'
			document.getElementById(inputObj).value = retValue;
			document.getElementById(outObj).innerHTML = "<font color='red'>&nbsp;有全角符号,输入无效</font>";
			return;
		}
		if (parseInt(strCode) > 65248 || parseInt(strCode) == 12288) {//判断是否为全角
			document.getElementById(inputObj).value = retValue;
			document.getElementById(outObj).innerHTML = "<font color='red'>&nbsp;有全角符号,输入无效</font>";
			return;
		}
		if (parseInt(strCode) > 127 || parseInt(strCode) == 94) {//如果不是普通英文字符,安2字节算
			len += 2;
		} else {
			len++;
		}
		if (len > valueLength) {
			document.getElementById(inputObj).value = retValue;
			document.getElementById(outObj).innerHTML = "<font color='red'>&nbsp;超出长度限制</font>";
			return;
		}
		retValue += inputValue.substr(i, 1);
	}
}

function makeCenter() {
	$('#choose-box-wrapper').modal();
	$('#choose-box-wrapper').modal('show');
}

function queryWaspUserBankInfo() {
	var orgInvNm = $("#orgInvNm").val();
	if (orgInvNm == "") {
		ctools.alert("请输入投资者名称！","","error");
		return false;
	}
	makeCenter();
	queryByCondtion();
}

function checkradio() {
	var para = "";
	var radioValue = document.getElementsByName("radio");
	if (radioValue != null && radioValue.length == null) {
		if (radioValue.checked) {
			para = radioValue.value;
		} else {
			ctools.alert("请选择预留银行信息！","","error");
			return false;
		}
	}

	if (radioValue != null && radioValue.length != null) {
		var isChecked = false;
		for (var i = 0; i < radioValue.length; i++) {
			if (radioValue[i].checked) {
				para = radioValue[i].value;
				isChecked = true;
			}
		}
		if (!isChecked) {
			ctools.alert("请选择预留银行信息！","","error");
			return false;
		}
	}

	var paraValue = para.split('#');
	if (paraValue.length == 3) {
		$("#orgbankacconm").val(paraValue[0]);
		$("#orgopenname").val(paraValue[1]);
		$("#orgbankacco").val(paraValue[2]);
		$("#orgopenlocation").val("");
		$("#orgopenbankcity").val("");
		$("#closeModel").click();
	} else {
		ctools.alert("预留银行数据信息不全！","","error");
	}

}

function queryOpenUserInfo() {
	var userName = $("#orgInvNm").val();
	if (userName != "") {
		$("#prompt").html("");
		$.ajax({
			url:PROJEC_TPATH+"service/accountManager/queryOpenUserInfo.do",  
			dataType: "json",
			data:{
				'sp[userName]' : userName
			},
			type: "POST",
			cache: false,
			async: false,
			success: function(data) {
				if (data != null && "1" == data.returnMsg) {
					ctools.alert("温馨提示：该投资者名称已开户！","","error");
				}
			},
			error : function() {
				ctools.alert("投资者名称校验异常！","","error");
			}
		});
	}
}

function getInvestProInstSecond(element) {
	var pmst = "";
	var pmky = "";
	var thisValue = element.value;
	var selOpenType = $("#selOpenType").val();
	var invprtp = $("#invprtp").val();
	if (!thisValue) {
		return;
	};
	$("#investProInstSecond").find("option").length = 1;
	$("#investProInstSecond").val("");
	$("[class=instInvestProInfoBg]").hide();
	$("[class=instNotResidentBg]").hide();
	var html = '<option value="">--</option>';
	$("#investProInstSecond").html(html);
	if ("0" == thisValue) {
		pmst = "INVPROINSTTYPE";
		pmky = "FINANCIALINST";
	} else if ("1" == thisValue) {
		pmst = "INVPROINSTTYPE";
		pmky = "FINPRODUCT";
	} else if ("2" == thisValue) {
		pmst = "INVPROINSTTYPE";
		pmky = "OTHERFUND";
	} else if ("3" == thisValue) {
		//执行其他法人及组织 
		$("[class=instInvestProInfoBg]").show();
		$("tr[class=instNotResidentBg]").show();
		$("#investProInstSecond").change();
		resetInvestClassTh("investClassInfo", "classInfoTh");
		resetInvestClassTh("instInvestPro", "instInvestProTh");
		return;
	}
	;
	// 其他法人及组织 普通投资者 开户为 机构
	if ((("1" == invprtp) || ("0" == invprtp))
			&& ("2" == selOpenType || "3" == selOpenType)) {
		$("tr[class=instNotResidentBg]").show();
	}

	var html = "<option value=''>--</option>";
	$("#investProInstSecond").html(html);
	$.ajax({
		type : "post",
		url : PRIMARY_PATH+"queryMatchParamList.do?pmst=" + pmst + "&pmky=" + pmky,
		dataType : "json",
		success : function(data) {
			if (!!data) {
				for (var i = 0; i < data.length; i++) {
					var item = data[i];
					html+="<option value=\""+item.PMCO+"\">"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
				}
				$("#investProInstSecond").html(html);
				
				if ("0" == thisValue) {
					$("#investProInstSecond").val("");
				} else if ("1" == thisValue) {
					$("#investProInstSecond").val("1");
				} else if ("2" == thisValue) {
					$("#investProInstSecond").val("0");
				} else if ("3" == thisValue) {
					$("#investProInstSecond").hide();
				} else {
					$("#investProInstSecond").val("");
				};
				$("#investProInstSecond").change();
			}
		},
		error : function() {
			ctools.alert("查询失败！","","error");
		}
	});
	$("#investProInstSecond").change();
	resetInvestClassTh("investClassInfo", "classInfoTh");
	resetInvestClassTh("instInvestPro", "instInvestProTh");
}
//专业投资者信息 展示 收集
function interactInvProInfo() {
	var openAccType = $("#selOpenType").val();
	var invprtp = $("#invprtp").val();
	var instBg = $("#instInvestPro");
	var perBg = $("#personInstInvestPro");
	var tabPslExtInfo = $("#tabPslExtInfo");
	var tabOrgExtInfo = $("#tabOrgExtInfo");

	//开户类型 机构开户 且 专业投资者 
	$(instBg).hide();
	$(perBg).hide();
	$("tr[class=instNotResidentBg]").hide();
	$("tr[class=negativeNotFinaInstBg]").hide();
	$("tr[class=instNotResidentBg] select").val("");
	$("tr[class=instNotResidentBg] select").change();
	//个人-普通 收入 客户风险等级 展示
	$(tabPslExtInfo).find("#plsInvIncome").parent().parent().parent().parent().show();
	$(tabOrgExtInfo).find("#orgRiskLevel").parent().parent().parent().prev().show();;
	$(tabOrgExtInfo).find("#orgRiskLevel").parent().parent().parent().show();
	$(tabOrgExtInfo).find("#orgRiskLevel").parent().parent().prev().show();
	$(tabOrgExtInfo).find("#orgRiskLevel").parent().parent().show();
	$("#extInfoOtherTh").attr("rowspan", "3");
	//个人开户 专业投资者
	if ("1" == openAccType && "0" == invprtp) {
		$(perBg).show();
		tabPslExtInfo.find("#plsInvIncome").parent().parent().parent().hide();
		$("#extInfoOtherTh").attr("rowspan", "2");
	}else{
		tabPslExtInfo.find("#plsInvIncome").parent().parent().parent().show();
		$("#extInfoOtherTh").attr("rowspan", "3");
	}

	//机构开户 专业投资者
	if (("2" == openAccType || "3" == openAccType) && "0" == invprtp) {
		$(instBg).show();
		$(tabOrgExtInfo).find("#orgRiskLevel").parent().parent().prev().hide();
		$(tabOrgExtInfo).find("#orgRiskLevel").parent().parent().hide();
	}
	//机构类型为 其他法人及组织  机构开户 普通投资者
	var investProInstType = $("#investProInstType").val();
	if (("2" == openAccType || "3" == openAccType)
			&& ("1" == invprtp || "3" == investProInstType)) {
		$("tr[class=instNotResidentBg]").show();
	} else {
		$("tr[class=instNotResidentBg]").hide();
		$("tr[class=negativeNotFinaInstBg]").hide();
	}
	//触发资料变动
	doSelect($("#docbusinesstp").val());
	resetInvestClassTh("investClassInfo", "classInfoTh");
	resetInvestClassTh("instInvestPro", "instInvestProTh");
	$("#investProInstType").change();
}


/*********************0414修改***************************************/
function negativeNotFinaChange(element) {
	var value = element.value;
	$("tr[class=negativeNotFinaInstBg] select").val("");
	if (value == "1" || value == "2") {
		$("[class=negativeNotFinaInstBg]").show();
		$("select[name=controlPerTaxDecl]").find("option[value=0]").attr(
				"selected", true);

	} else {
		$("[class=negativeNotFinaInstBg]").hide();
		$("#controllerInfo").hide();
		$("#controllerInfo").find("input,select").val("");
	}
	$("#controlPerTaxDecl").val("0");
	$("#controlPerTaxDecl").change();
	resetInvestClassTh("investClassInfo", "classInfoTh");
}
/*********************0414修改***************************************/

function resetInvestClassTh(id, targetTh) {
	//
	var rowspan = 0;
	var trList = $("#" + id).find("tr");
	for (var i = 0; i < trList.length; i++) {
		var tr = trList[i];
		var isHidden = $(tr).is(":hidden");
		if (!isHidden) {
			rowspan = i + 1;
		}
	}
	if (rowspan <= 0) {
		return;
	}
	$("#" + targetTh).attr("rowspan", rowspan);
	$("#" + targetTh).attr("rowspan", rowspan);
}

function customReset() {
	var instBg = $("#instInvestPro");
	var perBg = $("#personInstInvestPro");
	$(instBg).hide();
	$(perBg).hide();
	$("[class=instInvestProInfoBg] select").val("");
	$("[class=instInvestProInfoBg]").hide();
	$("[class=otherTaxTypeInfoBg] select").val("");
	$("[class=otherTaxTypeInfoBg]").hide();

	$("[class=negativeNotFinaInstBg] select").val("");
	$("[class=negativeNotFinaInstBg]").hide();
	$("[class=instNotResidentBg] #negativeNotFinaInst").val("");
	$("[class=instNotResidentBg] select").val("");
	$("[class=instNotResidentBg] select").change();
	$("[class=instNotResidentBg]").show();
	resetInvestClassTh("investClassInfo", "classInfoTh");
	resetInvestClassTh("instInvestPro", "instInvestProTh");
}

function changeSpecRiskLevel(ele, targetId) {
	var checked = ele.checked;
	if (checked) {
		document.getElementById(targetId).value = "1";
		document.getElementById(targetId).disabled = true;
		document.getElementById(targetId).style.backgroundColor = "#dddddd";
	} else {
		document.getElementById(targetId).disabled = false;
		document.getElementById(targetId).style.backgroundColor = "";
	}
	$('#'+targetId).select2({allowClear: false,minimumResultsForSearch:Infinity});
}

var financialFundType = "2";
function queryWaspOffInvestInfo() {
	var qInvName = $("#qOffLineInvestName").val();
	var selOpenType = $("#selOpenType").val();
	var oneToOneInvestName = document.getElementById('qOffLineInvestName');
	oneToOneInvestName.options.length = 1;
	$("#oneToOneInvestName").change();
	$.ajax({
		url: ACCOUNT_PATH+"queryCbpOffLineOTOInvInfo.do",  
		dataType: "json",
		data:{
			'sp[offInvName]' : qInvName,
			'sp[offInvType]' : selOpenType
		},
		type: "POST",
		cache: false,
		async: false,
		success: function(data) {
			if (!!data) {
				for (var i = 0; i < data.length; i++) {
					var dto = data[i];
					var offInvIdno = dto.offInvIdno;
					if (financialFundType == dto.offInvType) {
						offInvIdno = dto.offCounterIdNo;
					}
					var jsonString = dto.offInvSerialno + ","
							+ dto.offInvName + "," + dto.offInvIdtp + ","
							+ offInvIdno;
					var text = dto.offInvName;
					var value = jsonString;
					var option = new Option(text, value);
					oneToOneInvestName.options.add(option); //这个兼容IE与firefox
				}
				if (data.length <= 0) {
					clearOffLineInfo(false);
				}
			} else {//查询无数据 需要将 原 序列号置空
				clearOffLineInfo(false);
			}
		},
		error : function() {
			ctools.alert("查询线下一对一 客户信息失败！","","error");
		}
	});
	$("#qOffLineInvestName").change();
}

function offLineInvNameClick(element) {
	element.size = 1;
	element.style.display = "none";
	var value = element.value;
	if (!!value) {
		//开户类别
		var selOpenType = $("#selOpenType").val();
		var dto = value.split(",");
		$("#qOffLineInvestName").val(dto[1]);
		//个人开户
		if ("1" == selOpenType) {
			$("#offInvSerialno").val(dto[0]);
			$("#pslInvNm").val(dto[1]);
			$("#pslInvIdtp").val(dto[2]);
			$("#pslInvIdno").val(dto[3]);
			$("#pslInvIdno").keyup();
			$("#pslInvIdtp").change();
			//机构开户
		} else if ("2" == selOpenType || "3" == selOpenType) {
			$("#offInvSerialno").val(dto[0]);
			$("#orgInvNm").val(dto[1]);
			$("#orgInvIdtp").val(dto[2]);
			$("#orgInvIdno").val(dto[3]);
			$("#orgInvNm").change();
			$("#orgInvIdno").keyup();
			$("#orgInvIdtp").change();
		}
	} else {
		$("#offInvSerialno").val("");
		$("#qOffLineInvestName").val("");

		$("#pslInvNm").val("");
		$("#pslInvIdtp").val("0");
		$("#pslInvIdno").val("");
		$("#pslInvIdno").keyup();

		$("#orgInvNm").val("");
		$("#orgInvIdtp").val("11");
		$("#orgInvIdno").val("");
		$("#orgInvNm").change();
		$("#orgInvIdno").keyup();
		
		$("#pslInvIdtp").change();
		$("#orgInvIdtp").change();
	}
}
function clearOffLineInfo(hiTab) {
	$("#offInvSerialno").val("");
	$("#qOffLineInvestName").val("");

	$("#pslInvNm").val("");
	$("#pslInvIdtp").val("0");
	$("#pslInvIdno").val("");
	$("#pslInvIdno").keyup();

	$("#orgInvNm").val("");
	$("#orgInvIdtp").val("11");
	$("#orgInvIdno").val("");
	$("#orgInvNm").change();
	$("#orgInvIdno").keyup();
	
	$("#pslInvIdtp").change();
	$("#orgInvIdtp").change();
	
}

function qInvNameBlurFunc(element) {
	var invName = $(element).val();
	if (!invName) {
		clearOffLineInfo(true);
	}
}

/*************************************20180412新增*********************************************/
/**
 * 税收居民身份类型选择
 */
function taxTypeChange(element) {
	var value = element.value;
	
	$("tr[class=otherTaxTypeInfoBg] select").val("");
	if (!!value && "1" != value) {
		$("[class=otherTaxTypeInfoBg]").show();
	} else {
		$("[class=otherTaxTypeInfoBg]").hide();
	}
	resetInvestClassTh("investClassInfo", "classInfoTh");
	
	getTaxType(value, true);
	changeVal();
	var selOpenType = $("#selOpenType").val();
	initEnglishNameTr(selOpenType);
	if (value == '3') {
		var htmlStr = '<option value="">--</option><option value="1">中国</option>';
		$(".taxNationality select[name='taxNationality']").html(htmlStr);
	} else {
		var htmlStr = '<option value="">--</option><option value="1">中国</option><option value="2">中国香港</option><option value="3">中国澳门</option><option value="4">中国台湾</option><option value="5">海外</option>';
		$(".taxNationality select[name='taxNationality']").html(htmlStr);
	}
   	$('.select_addr1').select2("destroy");
	$('.select_addr2').select2("destroy");
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	
	$("#residentType").find("select").change();
	$("#controllerInfo").find("select").change();
}

function changeVal(){
	var selOpenType = $("#selOpenType").val();
	var reg = /^[\u4E00-\u9FA5]+$/;
	if (selOpenType == '1') {
		var pslInvNm = $("#pslInvNm").val();
		if (reg.test(pslInvNm)) {
			$("#userCustName").val(pslInvNm);
		}
	} else if (selOpenType == '2' || selOpenType == '3') {
		var orgInvNm = $("#orgInvNm").val();
		if (reg.test(orgInvNm)) {
			$("#userCustName").val(orgInvNm);
		}
	}
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
 * 类型切换
 * @param {Object} value
 */
function getTaxType(value, isClear) {
	var controlPerTaxDecl = $("#controlPerTaxDecl").val();
	if (value == "2") {
		$("#residentType").show();
		$("#residentTitle").text("税收居民信息");
		if (isClear) {
			$("#residentType input,#residentType select").val("");
			$("#residentType select").change();
			cancelTaxpayerCode();
		}
		$(".addResidentEle").remove();
		$(".since").hide();
		getSexAndBirth();
	} else if (value == "3") {
		$("#residentType").show();
		$("#residentTitle").text("税收居民信息");
		if (isClear) {
			$("#residentType input,#residentType select").val("");
			$("#residentType select").change();
			cancelTaxpayerCode();
		}
		$(".since").show();
		$(".sinceNotCodeCause").hide();
		getSexAndBirth();
	}else if(controlPerTaxDecl == '1'){
		$("#residentType").show();
		$("#residentTitle").text("税收居民信息");
		if (isClear) {
			$("#residentType input,#residentType select").val("");
			$("#residentType select").change();
			cancelTaxpayerCode();
		}
		
		$(".addResidentEle").remove();
		$(".since").hide();
		getSexAndBirth();
	} else {
		$("#residentType").hide();
	}
	$(".causeText").hide();
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
	checkObj.prev().removeAttr("readonly");
	checkObj.prev().removeAttr("disabled");
	checkObj.parents("tr").next().val("").hide();
	checkObj.parents("tr").next().val("").hide();
}
/**
 * 添加税收元素
 */
function addResident(obj) {
	var html = '<tr class="since addResidentEle ">'
			+ '<td><font color="red">*</font>税收居民国(地区)</td>'
			+ '<td class="white-bg" colspan="4"><div class="col-sm-11 form-inner">'
			+ '<select class="form-control select_addr1" name="taxNationality" onchange="changeNation(this)">'
			+ '<option value="">--</option>'
			+ '<option value="1">中国</option>'
			+ '<option value="2">中国香港</option>'
			+ '<option value="3">中国澳门</option>'
			+ '<option value="4">中国台湾</option>'
			+ '<option value="5">海外</option>'
			+ '</select>&nbsp;'
			+ '<select class="form-control select_addr2" name="taxArea">'
			+ '<option value="">--</option>'
			+ '</select>'
			+ '<span class="addResident" onclick="minResident(this);">-</span></div></td></tr>'
			+ '<tr class="since addResidentEle">'
			+ '<td><font color="red">*</font>纳税人识别号：</td>'
			+ '<td class="white-bg" colspan="4"><div class="col-sm-11 form-inner">'
			+ '<input type="text" name="taxpayerCode" class="form-control" style="float: left;">'
			+ '<label style="line-height: 34px;margin: 0;">'
			+ '<input name="isTaxpayerEvent" class="i-checks isTaxpayerEvent" type="checkbox" onclick="isTaxpayer(this)">无<i class="residentEach"></i>'
			+ '</label></div></td></tr>'
			+ '<tr class="since sinceNotCodeCause addResidentEle" style="display:none">'
			+ '<td><font color="red">*</font>无识别号的原因：</td>'
			+ '<td class="white-bg" colspan="4"><div class="col-sm-11 form-inner">'
			+ '<select class="form-control select2 notCodeCause" name="notCodeCause" onchange="notCodeCauseEvent(this)">'
			+ '<option value="">--</option>'
			+ '<option value="1">居民国（地区）不发放纳税人识别号</option>'
			+ '<option value="2">账号持有人未能取得纳税人识别号</option>'
			+ '</select>'
			+ '</div></td></tr>'
			+ '<tr class="since addResidentEle" style="display:none">'
			+ '<td><font color="red">*</font>未取得原因：</td>'
			+ '<td class="white-bg" colspan="4"><div class="col-sm-11 form-inner">'
			+ '<input type="text" class="form-control" name="notGetCause"></div></td></tr>';
	$("#residentType").append(html);
	
	$('.notCodeCause').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
}
/**
 * 删除税收元素
 * @param {Object} obj
 */
function minResident(obj) {
	$(obj).parents("tr").next().remove();
	$(obj).parents("tr").next().remove();
	$(obj).parents("tr").next().remove();
	$(obj).parents("tr").remove();
}
/**
 * 提交信息组装
 */
function checkResidentInfo() {
	var info = {};
	var re = /^[\u2E80-\u9FFF]+$/;
	var taxType = $("#taxType").val();
	var controlPerTaxDecl = $("#controlPerTaxDecl").val();
	var selOpenType = $("#selOpenType").val();
	if ((taxType != "1" && !!taxType) || controlPerTaxDecl == "1") { 
		var userCustName = $("input[name=userCustName]").val(); //中文姓名
		var firstName = $("input[name=firstName]").val(); //firstName
		var englishName = $("input[name=englishName]").val(); //englishName
		if (selOpenType == "1") {
			var sex = $("select[name=sex]").val(); //性别
			var birthDate = $("input[name=birthDate]").val(); //出生日期
		} else {
			var sex = ""; //性别
			var birthDate = ""; //出生日期
		}
		var taxBirthNation = $("select[name=birth_nation]").val(); //出生地  国籍
		var taxBirthRegion = $("select[name=birth_region]").val(); //出生地  省份
		var taxBirthAddress = $("input[name=birth_address]").val(); //出生地  详细地址
		var taxResideNation = $("select[name=reside_nation]").val(); //现居国家 国籍
		var taxResideRegion = $("select[name=reside_region]").val(); //现居国家 省份
		var taxResideAddress = $("input[name=reside_address]").val(); //现居国家 详细地址
		var taxResideAddressEnglish = $(
				"input[name=reside_address_english]").val(); //Present Address：
		info.userCustName = userCustName;
		info.firstName = firstName;
		info.englishName = englishName;
		info.taxBirthNation = taxBirthNation;
		info.taxBirthRegion = taxBirthRegion;
		info.taxBirthAddress = taxBirthAddress;
		info.taxResideNation = taxResideNation;
		info.taxResideRegion = taxResideRegion;
		info.taxResideAddress = taxResideAddress;
		info.taxResideAddressEnglish = taxResideAddressEnglish;
		if (selOpenType == "1") {
			if (firstName == "") {
				ctools.alert("请填写First Name!","","error");
				return false;
			}
			if (re.test(firstName)) {
				ctools.alert("First Name只能填写英文或拼音！","","error");
				return false;
			}
			if (englishName == "") {
				ctools.alert("请填写Last Name！","","error");
				return false;
			}
			if (re.test(englishName)) {
				ctools.alert("Last Name只能填写英文或拼音！","","error");
				return false;
			}
			if (sex == "") {
				ctools.alert("请选择性别！","","error");
				return false;
			}
			if (birthDate == "") {
				ctools.alert("请选择出生日期！","","error");
				return false;
			}
			if (taxBirthNation == "" || taxBirthRegion == "") {
				ctools.alert("请选择出生地！","","error");
				return false;
			}
		} else {
			if (englishName == "") {
				ctools.alert("请填写English Name！","","error");
				return false;
			}
			if (re.test(englishName)) {
				ctools.alert("English Name只能填写英文或拼音！","","error");
				return false;
			}
		}

		if (taxResideNation == "" || taxResideRegion == "") {
			ctools.alert("请选择"+$(".resideNation").html(),"","error");
			return false;
		}
		if (taxResideAddress == "") {
			ctools.alert("请填写"+$(".resideAddress").html(),"","error");
			return false;
		}
		if (taxResideAddressEnglish == "") {
			ctools.alert("请填写Present Address！","","error");
			return false;
		}
		if (re.test(taxResideAddressEnglish)) {
			ctools.alert("Present Address请填写英文或拼音地址！","","error");
			return false;
		}
		if (!checkTaxResidentData()) {
			ctools.alert("请填写税收信息！","","error");
			return false;
		} else {
			if ((taxType != "1" && !!taxType) && controlPerTaxDecl == "1") { 
				var flag = checkControllerInfo(); //校验控制人相关信息
				if (flag) {
					var residentData = JSON.stringify(getTaxResidentData(info));
					$("input[name=taxresident]").val(residentData.replace(/\[]/g, "''"));
				} else {
					return flag;
				}

			} else {
				var residentData = JSON.stringify(getTaxResidentData(info));
				$("input[name=taxresident]").val(residentData.replace(/\[]/g, "''"));
			}

		}
	}
	return true;
}

//校验税收信息
function checkTaxResidentData() {
	var flag = true;
	var taxType = $("#taxType").val();
	//根据不同的税收居民身份
	$("select[name=taxNationality]").each(function(i){
		if(taxType == '2' && i == 1){
			return true;
		}
		var taxNationality = $("select[name=taxNationality]").eq(i).val();
		var taxArea = $("select[name=taxArea]").eq(i).val();
		if (!!taxNationality && !!taxArea) {
			if (taxNationality == "" || taxArea == "") {
				flag = false;
				return flag;
			} else {
				$(".isTaxpayerEvent").each(function(i) {
					var select1 = $(this).parents("tr").prev().find("select").eq(0).val();
					var select2 = $(this).parents("tr").prev().find("select").eq(1).val();
					if (!!select1 && !!select2) {
						if ($(this).is(':checked')) {
							var payerCode = $(this).parents("tr").next().find("select");
							var TaxpayerCode = $(this).parents("tr").next().find("select").val();
							if (TaxpayerCode != "") {
								if (payerCode.val() == "2") {
									if (payerCode.parents("tr").next().find("input").val() == "") {
										flag = false;
									}
								}
							} else {
								flag = false;
							}
						} else {
							if ($(this).parent().parent().parent().find("input[name=taxpayerCode]").val() == "") {
								flag = false;
							}
						}
					}
				});
			}
		}else{
			flag = false;
			return flag;
		}
	});
	return flag;
}

/**
 * 获取 税收信息数据 
 * 多税收信息 按层级获取后封装为json字符串
 * @returns 封装后的税收居民数据
 */
function getTaxResidentData(taxInfo) {
	var residentArr = new Array();
	var taxType = $("#taxType").val();
	var controlPerTaxDecl = $("#controlPerTaxDecl").val();
	if ((taxType != "1" && !!taxType) || controlPerTaxDecl == "1") {
		$(".residentEach").each(function(i) {
			var taxNationality = $(this).parents("tr").prev().find("select[name=taxNationality]").val();
			var taxArea = $(this).parents("tr").prev().find("select[name=taxArea]").val();
			if (!!taxNationality && !!taxArea) {
				var taxInfoResident = {
					firstName : taxInfo.firstName,
					englishName : taxInfo.englishName,
					taxBirthNation : taxInfo.taxBirthNation,
					taxBirthRegion : taxInfo.taxBirthRegion,
					taxBirthAddress : taxInfo.taxBirthAddress,
					taxResideNation : taxInfo.taxResideNation,
					taxResideRegion : taxInfo.taxResideRegion,
					taxResideAddress : taxInfo.taxResideAddress,
					taxResideAddressEnglish : taxInfo.taxResideAddressEnglish
				};
				var selOpenType = $("#selOpenType").val();
				if (selOpenType == "1") {
					taxInfoResident.sex = $("select[name=sex]").val();
					taxInfoResident.birthDate = $("input[name=birthDate]").val();
				} else {
					taxInfoResident.sex = "";
					taxInfoResident.birthDate = "";
				}
				taxInfoResident.sortNo = i;
				taxInfoResident.taxNationality = $(this).parents("tr").prev().find("select[name=taxNationality]").val();
				taxInfoResident.taxArea = $(this).parents("tr").prev().find("select[name=taxArea]").val();
				taxInfoResident.taxPayerCode = $(this).parent().siblings("input").val();
				var isTaxpayerEvent = $(this).siblings("input[type='checkbox']").is(':checked');
				if (isTaxpayerEvent) {
					taxInfoResident.taxNotCodeCause = $(this).parents("tr").next().find("select[name=notCodeCause]").val();
					taxInfoResident.taxnotGetCause = $(this).parents("tr").next().next().find("input[name=notGetCause]").val();
				} else {
					taxInfoResident.taxNotCodeCause = "";
					taxInfoResident.taxnotGetCause = "";
				}

				//控制人信息
				if ($("#selOpenType").val() != '1'&& !!$("#selOpenType").val()) {
					taxInfoResident.chineseName2 = $("input[name=ChineseName2]").val(); //控制人中文姓名：
					taxInfoResident.englishFamliyName3 = $("input[name=EnglishFamliyName3]").val(); //控制人英文姓
					taxInfoResident.englishFirstName3 = $("input[name=EnglishFirstName3]").val(); //控制人英文名
					taxInfoResident.controllerType = $("select[name=ControllerType]").val(); //控制人类型
					taxInfoResident.conNonResiFlag = $("input[name=ConNonResiFlag]").val(); //控制人非居民标识
					taxInfoResident.conShareRatio = $("input[name=ConShareRatio]").val(); //控制人持股比例
					taxInfoResident.livingCountry2Code = $("select[name=LivingCountry2]").val(); //控制人现居国家
					taxInfoResident.livingCountry2 = $("select[name=LivingCountry21]").val(); //控制人现居国家
					taxInfoResident.livingAddress5 = $("input[name=LivingAddress5]").val(); //控制人现居地址
					taxInfoResident.livingAddress7 = $("input[name=LivingAddress7]").val(); //控制人现居地址英文
					taxInfoResident.regRegionCode2Code = $("select[name=RegRegionCode2]").val(); //控制人国籍
					taxInfoResident.regRegionCode2 = $("select[name=RegRegionCode21]").val();//控制人国籍
					taxInfoResident.birthDate2 = $("input[name=BirthDate2]").val(); //控制人出生日期
					taxInfoResident.birthCountry2Code = $("select[name=BirthCountry2]").val(); //控制人出生国家
					taxInfoResident.birthCountry2 = $("select[name=BirthCountry21]").val(); //控制人出生国家
					taxInfoResident.birthCity2 = $("input[name=BirthCity2]").val(); //控制人出生城市英文
					taxInfoResident.taxCountry2Code = $("select[name=TaxCountry2]").val(); //控制人税收居民国
					taxInfoResident.taxCountry2 = $("select[name=TaxCountry21]").val(); //控制人税收居民国
					taxInfoResident.taxID2 = $("input[name=TaxID2]").val(); //纳税人识别号
					var isTaxpayerEvent2 = $("input[name='isTaxpayerEvent2']").is(':checked');
					if (isTaxpayerEvent2) {
						taxInfoResident.specificationCode = $("select[name=notCodeCause2]").val(); //无识别号的原因
						taxInfoResident.specification2 = $("input[name=Specification2]").val(); //未取得原因
					} else {
						taxInfoResident.specificationCode = "";
						taxInfoResident.specification2 = "";
					}
				}
				residentArr.push(taxInfoResident);
			}
		});
	}
	return residentArr;
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

/*************************************20180412新增*********************************************/

/*************************************20180413新增s*********************************************/
/*
 * 存在非居民控制人
 */
function controllerPerTaxDecl(ele) {
	if (ele.value == "1") {
		$("#residentType").show();
		$("#controllerInfo").show();
	} else {
		$("#residentType").hide();
		$("#residentType").find("input,select").val("");
		$("#controllerInfo").hide();
		$("#controllerInfo").find("input,select").val("");
	}
	$(".controTr").hide();
	$(".controTr").eq(0).prev().find("input[name=TaxID2]").removeAttr(
			"disabled");
	$(".controTr").eq(0).prev().find("input[name=isTaxpayerEvent2]").attr(
			"checked", false);
	$("input[name=ConNonResiFlag]").val("1");
	$("#taxType").change();
}

/**
 * 提交信息校验
 */
function checkControllerInfo() {
	var info = {};
	var re = /^[\u2E80-\u9FFF]+$/;
	var num = /0\.[0-9]+/;
	var flag = true;
	var chineseName2 = $("input[name=ChineseName2]").val(); //中文姓名2
	var englishFamliyName3 = $("input[name=EnglishFamliyName3]").val(); //英文姓3
	var englishFirstName3 = $("input[name=EnglishFirstName3]").val(); //英文名
	var controllerType = $("select[name=ControllerType]").val(); //控制人类型
	var conNonResiFlag = $("input[name=ConNonResiFlag]").val(); //控制人非居民标识
	var conShareRatio = $("input[name=ConShareRatio]").val(); //控制人持股比例
	var livingCountry2 = $("select[name=LivingCountry2]").val(); //现居国家2
	var livingCountry21 = $("select[name=LivingCountry21]").val(); //现居国家2
	var livingAddress5 = $("input[name=LivingAddress5]").val(); //现居地址5
	var livingAddress7 = $("input[name=LivingAddress7]").val(); //现居地址英文7
	var regRegionCode2 = $("select[name=RegRegionCode2]").val(); //国籍
	var regRegionCode21 = $("select[name=RegRegionCode21]").val(); //国籍
	var birthDate2 = $("input[name=BirthDate2]").val(); //出生日期2
	var birthCountry2 = $("select[name=BirthCountry2]").val(); //出生国家2
	var birthCountry21 = $("select[name=BirthCountry21]").val(); //出生国家2
	var birthCity2 = $("input[name=BirthCity2]").val(); //出生城市2
	var isType = $("input[name=taxType]").val();
	if (chineseName2 == "") {
		ctools.alert("请填写控制人中文姓名！","","error");
		flag = false;

	} else if (englishFamliyName3 == "") {
		ctools.alert("请填写控制人英文姓！","","error");
		flag = false;
	} else if (re.test(englishFamliyName3)) {
		ctools.alert("英文姓只能填写英文或拼音！","","error");
		flag = false;
	} else if (englishFirstName3 == "") {
		ctools.alert("请填写控制人英文名！","","error");
		flag = false;
	} else if (re.test(englishFirstName3)) {
		ctools.alert("英文名只能填写英文或拼音！","","error");
		flag = false;
	} else if (controllerType == "") {
		ctools.alert("请选择控制人类型！","","error");
		flag = false;
	} else if (conNonResiFlag == "") {
		ctools.alert("请填写控制人非居民标识！","","error");
		flag = false;
	}/*  else if (conShareRatio == "") {
		ctools.alert("请填写控制人持股比例！","","error");
		flag = false;
	} else if (!num.test(parseFloat(conShareRatio))) {
		ctools.alert("控制人持股比例不正确！","","error");
		flag = false;
	}*/ 
	else if (livingCountry2 == "" || livingCountry21 == "") {
		ctools.alert("请选择控制人现居国家！","","error");
		flag = false;
	} else if (livingAddress5 == "") {
		ctools.alert("请填写控制人现居地址！","","error");
		flag = false;
	} else if (livingAddress7 == "") {
		ctools.alert("请填写控制人现居地址英文！","","error");
		flag = false;
	} else if (re.test(livingAddress7)) {
		ctools.alert("控制人现居地址英文只能填写英文或拼音！","","error");
		flag = false;
	}/*  else if (regRegionCode2 == "" || regRegionCode21 == "") {
		flag = false;
		alert("请填写控制人国籍");
	}  */else if (birthDate2 == "") {
		ctools.alert("请选择控制人出生日期！","","error");
		flag = false;
	} else if (birthCountry2 == "" || birthCountry21 == "") {
		ctools.alert("请选择控制人出生国家！","","error");
		flag = false;
	} else if (birthCity2 == "") {
		ctools.alert("请填写控制人出生城市英文！","","error");
		flag = false;
	} else if (re.test(birthCity2)) {
		ctools.alert("控制人出生城市英文只能填写英文或拼音！","","error");
		flag = false;
	} else if (!checkControllerData()) {
		ctools.alert("请完善控制人税收信息！","","error");
		flag = false;
	}
	return flag;
}
/**
 * 校验控制人税收
 */
function checkControllerData() {
	var flag = true;
	var taxCountry2 = $("select[name=TaxCountry2]").val();
	var taxCountry21 = $("select[name=TaxCountry21]").val();
	if (taxCountry2 == "" || taxCountry21 == "") {
		flag = false;
	}
	if ($("input[name=isTaxpayerEvent2]").is(':checked')) {
		var notCodeCause2 = $("select[name=notCodeCause2]").val();
		if (notCodeCause2 != "") {
			if (notCodeCause2 == "2") {
				if ($("input[name=Specification2]").val() == "") {
					flag = false;
				}
			}
		} else {
			flag = false;
		}
	} else {
		if ($("input[name=TaxID2]").val() == "") {
			flag = false;
		}
	}
	return flag;
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

$("#btnReset").click(function() {
	$("#taxType").change();
});


var $cbpBankInfoList = $('#cbpBankInfoList');
function initGrid(){
	$cbpBankInfoList.jqGrid({
		url: ACCOUNT_PATH+'queryWaspUserBankInfo.do',
		datatype:"local",
        colNames: ["","预留银行全称","预留银行户名","预留银行账号"],
        colModel: [
            { name: 'option', index: 'option', width:50, align:'right', sortable: false},
            { name: 'bankAccoNm', index: 'bankAccoNm', sortable: false },
            { name: 'openName', index: 'openName', sortable: false},
            { name: 'bankAcco', index:'bankAcco',key: true, sortable: false }
        ],
        rownumbers: false,
        prmNames: {search:"search",page:"pageNo",rows:"limit"},
        height: '280px',
        width: false,
        shrinkToFit:false,
        viewrecords: false,
        shrinkToFit: false,
		gridComplete: function() {
			var ids = $cbpBankInfoList.jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){
				var id = ids[i];
				var rowData = $cbpBankInfoList.jqGrid('getRowData', id);	
            	var cub = "<input type='radio' onchange = \"selectRow('"+rowData.bankAcco+"');\" name='radio' value='"+rowData.openName+"#"+rowData.bankAccoNm+"#"+rowData.bankAcco+"'/>";
            	$cbpBankInfoList.jqGrid('setRowData',ids[i],{option: cub });
			}
		},
		onSelectRow : function(rowid,iCol,cellContent,event){
			$("tr[id='"+rowid+"']").find("input[type='radio']").attr("checked",true);
		}
    });
    $cbpBankInfoList.jqGrid('setFrozenColumns');
    jqGridResize($cbpBankInfoList);
}

function initWidget(){
	//注册清空事件
    WASP_WIDGET.registerResetClearEvent();
}

function queryByCondtion(flag){
	var orgInvNm = $("#orgInvNm").val();
    var postData = $cbpBankInfoList.jqGrid("getGridParam", "postData");
    $.extend(postData,{
    	'sp[accName]': orgInvNm
    });

    $cbpBankInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
}

function selectRow(bankAcco){
	$('#cbpBankInfoList').jqGrid('setSelection',bankAcco);
}


/**
 * 反洗钱校验
 */
function antiMoneyLaunValid(){
	/*$("#PSL_ANTIMONEYLAUN_ISPASS").text("");
	$("#INST_ANTIMONEYLAUN_ISPASS").text("");
	$("#PSL_ANTIMONEYLAUN_REMARK").text("");
	$("#INST_ANTIMONEYLAUN_REMARK").text("");
	$("#PSL_ANTIMONEYLAUN_BLACKLIST").hide();
	$("#INST_ANTIMONEYLAUN_BLACKLIST").hide();*/
	$("#ANTIMONEYLAUN_BLACKLIST").text("");
	
	//投资者类型
	var invtp = $("#selOpenType").val();
	//证件类型
	var idtp = $("#orgInvIdtp").val();
	//证件号码
	var idno = $("#orgInvIdno").val();
	
	//个人投资者 重新取值
	if("1" == invtp){
		idtp = $("#pslInvIdtp").val();
		idno = $("#pslInvIdno").val();
	}
	
	//填完信息后才获取数据
	if(!invtp || !idtp || !idno){
		return ;
	}
	
	$.ajax({
		type : "post",
		url : ACCOUNT_PATH+"antiMoneyLaunValid.do",
		dataType : "json",
		data:{
			"sp[invtp]"	:invtp,
			"sp[idtp]"	:idtp,
			"sp[idno]"	:idno
		},
		success : function(data) {
			if(!data || "0000" == data.isPass){
				//无数据通过
				$("#ANTIMONEYLAUN_BLACKLIST").attr("color","green");
				$("#ANTIMONEYLAUN_BLACKLIST").text("反洗钱黑名单校验：通过");
			}else{
				$("#ANTIMONEYLAUN_BLACKLIST").attr("color","red");
				$("#ANTIMONEYLAUN_BLACKLIST").text("反洗钱黑名单校验：不通过");
			}
		}
	});
}