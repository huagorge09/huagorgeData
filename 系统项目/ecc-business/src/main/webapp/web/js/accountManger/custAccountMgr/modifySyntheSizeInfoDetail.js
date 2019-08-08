var PRIMARY_PATH = "";
var ACCOUNT_PATH = "";
var PROJEC_TPATH = "";

function setPath(projectPath,path,accountPath){
	PROJEC_TPATH = projectPath;
	PRIMARY_PATH = path;
	ACCOUNT_PATH = accountPath;
}

var custno = $("#custno").val();
var custtp = $("#invtp").val();
var invtp = $("#invtp").val();
var custArr = custno.split(",");
var custlength = custArr.length;

if (custtp == '1') {
	if (custlength > 1) {
		$("#pchidinfodiv").hide();
	}
} else {
	if (custlength > 1) {
		$("#orgchidinfodiv").hide();
	}
}

var oidlist = $("#oidlist").val();
oidlist = !!oidlist ? eval(oidlist) : "";
var ocontlist = $("#ocontactlist").val();
ocontlist = !!ocontlist ? eval(ocontlist) : "";
var doclist = $("#documentlist").val();
doclist = !!doclist ? eval(doclist) : "";


$(function(){
	doSelect($("#docbusinesstp").val());
	WASP_WIDGET.triggerDateStyleWithYMD("pslInvIdValidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgInvIdvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgInstrepidvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgPrincipalidvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgContidvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgHoldingidvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgHoldingidvalidate");
	
	$('.select2_width').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.select2-q').select2();
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	
	initData(oidlist, ocontlist);
	showInfo();
	
	$(".updateList input[type='checkbox']").each(function(data) {
		$(this).click(function() {
			checkInfoSelected();
		});
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
	$("#btnSubmit").mousedown(function() {
		dosubmit();
	});
	
	var specriskLevel = $("#specriskLevel").val();
	if ("1" == specriskLevel) {
		$("#indSpecRiskLevel").click();
		$("#specRiskLevel").click();
	}
	//queryFileNo();
	
	
	var custsimpnm = $("#custsimpnmVal").val();
	if(custtp=='0'){
	   	//加载客户二级简称
	    getSecond(custsimpnm); 
	}
	initVal();
	
	$("#isalldoc").change(function() {
		if ($("#isalldoc").val() == 1) {
			$("#documentinfo input[name='doccheck']").each(function(data) {
				$(this).prop("checked", true);// 全选
			});
		} else {
			$("#documentinfo input[name='doccheck']").each(function(data) {
				$(this).prop("checked", false);// 取消全选
			});
		}
	});
	
	$("#claseBtn").click(function() {
		setTimeout(function(){
			$("select").change();
		}, 100);
		$("#reset").click();
	});
});

function showTypeContent(id, tId) {
	$(".typeContent").hide();
	$("#" + id).show();
	if(tId == 'btnBaseInfo'){
		baseInfo();
	}else if(tId == 'btnExtInfo'){
		extInfo();
	}else if(tId == 'btnDocumentInfo'){
		documentInfo();
	}
};

function showInfo()
{
   if(custtp=='1')
   {
      $("#tabPslBaseInfo").show();
      $("#tabOrgBaseInfo").hide();
   }
   else
   {
      $("#tabPslBaseInfo").hide();
      $("#tabOrgBaseInfo").show();
   }
} 

function baseInfo() // 显示基本信息
{
	showBaseInfo();
	hideExtInfo();
	$("#documentInfo").hide();
}

function extInfo() {
	hideBaseInfo();
	showExtInfo();
	$("#documentInfo").hide();
}

function documentInfo() {
	hideBaseInfo();
	hideExtInfo();
	$("#documentInfo").show();
}

function showBaseInfo()// 显示基本信息
{
	if (custtp == 1) {
		$("#tabPslBaseInfo").show();
		$("#tabOrgBaseInfo").hide();
	} else {
		$("#tabPslBaseInfo").hide();
		$("#tabOrgBaseInfo").show();
	}
}

function hideBaseInfo()// 隐藏基本信息
{
	$("#tabPslBaseInfo").hide();
	$("#tabOrgBaseInfo").hide();
}

function showExtInfo()// 显示附加信息
{
	if (custtp == 1) {
		$("#tabPslExtInfo").show();
		$("#tabOrgExtInfo").hide();
	} else {
		$("#tabPslExtInfo").hide();
		$("#tabOrgExtInfo").show();
	}
}

function hideExtInfo()// 隐藏附加信息
{
	$("#tabPslExtInfo").hide();
	$("#tabOrgExtInfo").hide();
}

function checkInfoSelected() {
	$(".updateList input[type='checkbox']").each(function(data) {
		if ($(this).is(":checked")) {
			if ($(this).attr("id") == 'pchidinfo') {
				$("#pidinfo").show();
			}
			if ($(this).attr("id") == 'pchotherinfo') {
				$("#potherinfo").show();
			}
			if ($(this).attr("id") == 'orgchidinfo') {
				$("#orgidinfo").show();
			}
			if ($(this).attr("id") == 'orgchinstrepinfo') {
				$("#orginstrepinfo").show();
			}
			if ($(this).attr("id") == 'orgchprincipalinfo') {
				$("#orgprincipalinfo").show();
			}
			if ($(this).attr("id") == 'orgchcontinfo') {
				$("#orgcontinfo").show();
			}
			if ($(this).attr("id") == 'orgchotherinfo') {
				$("#orgotherinfo").show();
			}
		} else {
			if ($(this).attr("id") == 'pchidinfo') {
				$("#pidinfo").hide();
			}
			if ($(this).attr("id") == 'pchotherinfo') {
				$("#potherinfo").hide();
			}
			if ($(this).attr("id") == 'orgchidinfo') {
				$("#orgidinfo").hide();
			}
			if ($(this).attr("id") == 'orgchinstrepinfo') {
				$("#orginstrepinfo").hide();
			}
			if ($(this).attr("id") == 'orgchprincipalinfo') {
				$("#orgprincipalinfo").hide();
			}
			if ($(this).attr("id") == 'orgchcontinfo') {
				$("#orgcontinfo").hide();
			}
			if ($(this).attr("id") == 'orgchotherinfo') {
				$("#orgotherinfo").hide();
			}
		}
	});
};

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
}


//------------------------------------其他证件和经办人信息---------------------------------------------------------------------------------------- 
//增加证件信息
var idflag=0;//用于动态设置证件有效期的长期有效功能，由于ID改变，原取值方式也需要修改
var valueFlag = 0;
function addIDRow(idtp,idno,idvalidate)
{
	
	idtp = idtp == undefined ? "" : idtp;
	idno = idno == undefined ? "" : idno;
	idvalidate = idvalidate == undefined ? "" : idvalidate;
	
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
	trstr +='<label style="line-height: 34px;">';
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
 trstr += $("#orgcontinfo>tr:eq(0)>td:eq(4)").clone(true).find("#orgContnm").attr("id","oocontact").attr("value",contnm).end().html();
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
 trstr +=$("#orgcontinfo>tr:eq(3)").clone(true).find("#orgContphone").attr("id","oocontphone").attr("value",conttel).end().find("#orgContfax").attr("id","oocontfax").attr("value",contfax).end().html();
 trstr +="</tr>"; 
 //第六行
 trstr +="<tr>";
 trstr +=$("#orgcontinfo>tr:eq(4)").clone(true).find("#orgContmobile").attr("id","oocontmobile").attr("value",contmobile).end().find("#orgContemail").attr("id","oocontemail").attr("value",contemail).end().html();
 trstr +="</tr>";
 //第七行
 trstr +="<tr>";
 trstr +=$("#orgcontinfo>tr:eq(5)").clone(true).find("#orgContAddr").attr("id","oocontAddr").attr("value",contAddr).end().find("#orgContPostcode").attr("id","oocontPostcode").attr("value",contPostcode).end().html();
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
	$("#documentlist").val("");
	var isalldoc = $("#isalldoc").val();
	if(isalldoc == "1"){
		$("#isalldoc").change();
	}
}

//------------------------------------------------------------资料信息--------------------------------------------------------------------------

// 初始化数据
function initData(oidlist, ocontlist) {
	if (oidlist != null) {
		if (oidlist.length > 0) {
			for (var i = 0; i < oidlist.length; i++) {
				addIDRow(oidlist[i].idtp, oidlist[i].idno,
						oidlist[i].idvalidate);
			}
		}
	}
	if (ocontlist != null) {
		if (ocontlist.length > 0) {
			for (var j = 0; j < ocontlist.length; j++) {
				addContRow(ocontlist[j].contactgrant, ocontlist[j].contact,
						ocontlist[j].contactnation, ocontlist[j].contidtp,
						ocontlist[j].contidno, ocontlist[j].contvalidate,
						ocontlist[j].contphone, ocontlist[j].contfax,
						ocontlist[j].contmobile, ocontlist[j].contemail,
						ocontlist[j].contAddr, ocontlist[j].contPostcode);
			}
		}
	}
}

function queryFileNo() // 初始化文件编号和存档位置
{
	$.ajax({
		// 任务生效
		url: PROJEC_TPATH+"service/accountManager/getMaxFileNo.do",  
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

function queryOpenUserInfo() {
	var userName = $("#orgInvNm").val();
	if (userName != "") {
		$("#prompt").html("");
		$.ajax({
			url: PROJEC_TPATH+"service/accountManager/queryOpenUserInfo.do",  
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


//--------------------------------------取提交所需信息---------------------------------------------------
function getpslmodifylist()
{
   var modifylist="";
   $(".updateList input[type='checkbox']").each(function(data)
   {
      if($(this).is(":checked"))
      {
         if($(this).attr("id")=='pchidinfo')
         {
            modifylist +="A,";
         }   
         if($(this).attr("id")=='pchotherinfo')
         {
            modifylist +="F,";
         }                                                                                        
      }       
   });
   return modifylist;   
}   

function getorgmodifylist()
{
   var modifylist="";
   $(".updateList input[type='checkbox']").each(function(data)
   {
      if($(this).is(":checked"))
      {
         if($(this).attr("id")=='orgchidinfo')
         {
            modifylist +="A,";
         }
         if($(this).attr("id")=='orgchinstrepinfo')
         {
            modifylist +="C,";
         }
         if($(this).attr("id")=='orgchprincipalinfo')
         {
            modifylist +="D,";
         }
         if($(this).attr("id")=='orgchcontinfo')
         {
            modifylist +="E,";
         }
         if($(this).attr("id")=='orgchotherinfo')
         {
            modifylist +="F,";
         }
      }                        
   });
   return modifylist;                 
} 
function getPersonalInfo()
{
   var personalInfo = {};
        
   personalInfo.custTp = custtp; 
   personalInfo.permissionId = $("#permissionId").val();
   personalInfo.operatorId = $("#operatorId").val();
   personalInfo.custnos = $("#custno").val();
   personalInfo.trusttp = $("#plstrustType").val(); 
   
   // 修改信息列表
   personalInfo.modifylist = getpslmodifylist();       
   // 基本->证件信息
   personalInfo.pslInvNm = $("#pslInvNm").val();
   personalInfo.pslInvIdtp = $("#pslInvIdtp").val(); 
   personalInfo.pslInvIdno = $("#pslInvIdno").val(); 
   personalInfo.pslInvIdValidate = $("#pslInvIdValidate").val(); 
   
   // 基本->其他信息
   personalInfo.pslInvOfficeTel = $("#pslInvOfficeTel").val(); 
   personalInfo.pslInvHomeTel = $("#pslInvHomeTel").val(); 
   personalInfo.pslInvMobile = $("#pslInvMobile").val(); 
   personalInfo.pslInvFax = $("#pslInvFax").val(); 
   personalInfo.plsFaxDelegate = $("#plsFaxDelegate").val(); 
   personalInfo.pslInvEmail = $("#pslInvEmail").val(); 
   // personalInfo.plsBillDelivery = $("#plsBillDelivery").val();
   personalInfo.plsPostCode = $("#plsPostCode").val(); 
   personalInfo.plsAddr = $("#plsAddr").val();
   personalInfo.shsecacc = $("#pslShsecacc").val();
   personalInfo.szsecacc = $("#pslSzsecacc").val();
   
   // 附加信息->其他
   personalInfo.plsSex = $("#plsSex").val(); 
   personalInfo.plsInvNation = $("#plsInvNation").val(); 
   personalInfo.plsInvEducation = $("#plsInvEducation").val(); 
   personalInfo.plsInvJob = $("#plsInvJob").val(); 
   personalInfo.plsInvIncome = $("#plsInvIncome").val(); 
   personalInfo.plsInvRisk = $("#plsInvRisk").val();
   
   //原风险等级
   personalInfo.preRisklevel = $("#preRisklevel").val();
   personalInfo.preInvprtp = $("#invprtp").val();
   // 特殊用户风险等级
   personalInfo.specriskLevel = "0";
   if($("#indSpecRiskLevel").is(':checked')){
 	  personalInfo.plsInvRisk = "1";
 	  personalInfo.specriskLevel = "1";
   }
   
   // 资料信息
   var document="";
   var docflag=false;
   $("input[name='doccheck']").each(function(data)
   {
      if($(this).is(":checked"))
      {
         document +=$(this).attr("id")+"-"+"1"+",";
         
      }
      else
      {
         document +=$(this).attr("id")+"-"+"0"+",";
      }
      docflag = true;
   });
   if(!docflag){// 如果未选中，传一个空值
   	document = "";
   }
   personalInfo.docbusinesstp = $("#docbusinesstp").val(); 
   personalInfo.isalldoc = $("#isalldoc").val();
   personalInfo.document = document;
   personalInfo.iforiginal = $("#isoriginal").val();   
   personalInfo.ifsaved = $("#issaved").val();
   personalInfo.isupload = $("#isupload").val();
   personalInfo.remarkinfo = $("#remarkinfo").val();
   
   personalInfo.isscan=$("#isscan").val();
   personalInfo.keepaddress=	$("#keepaddress").val();
   personalInfo.salesaccmanager= $("#salesaccmanager").val();  	  
	  personalInfo.fileno=  $("#fileno").val();  
   
   return personalInfo;
}

function getOrgInfo()
{
	var orgInfo = {};
   orgInfo.custTp = custtp; 
   
   orgInfo.permissionId = $("#permissionId").val();
   orgInfo.operatorId = $("#operatorId").val();
   orgInfo.custnos = $("#custno").val();
   orgInfo.trusttp = $("#orgtrustType").val(); 
   
   // 修改信息列表
   orgInfo.modifylist = getorgmodifylist();      
   // 基本->证件信息
   orgInfo.orgInvNm = $("#orgInvNm").val();  
   orgInfo.orgInvIdtp = $("#orgInvIdtp").val(); 
   orgInfo.orgInvIdno = $("#orgInvIdno").val();
   orgInfo.orgInvIdvalidate = $("#orgInvIdvalidate").val();    
	  orgInfo.orgType = $("#organType").val();
   
   // 基本->法人信息
   orgInfo.orgInstrepnm = $("#orgInstrepnm").val(); 
   orgInfo.orgInstrepnation = $("#orgInstrepnation").val(); 
   orgInfo.orgInstrepidtp = $("#orgInstrepidtp").val(); 
   orgInfo.orgInstrepidno = $("#orgInstrepidno").val(); 
   orgInfo.orgInstrepidvalidate = $("#orgInstrepidvalidate").val(); 
   
   // 基本->机构负责人信息
   orgInfo.orgPrincipalname = $("#orgPrincipalname").val(); 
   orgInfo.orgPrincipalnation = $("#orgPrincipalnation").val(); 
   orgInfo.orgPrincipalidtp = $("#orgPrincipalidtp").val(); 
   orgInfo.orgPrincipalidno = $("#orgPrincipalidno").val();  
   orgInfo.orgPrincipalidvalidate = $("#orgPrincipalidvalidate").val();     
   
   // 基本->经办人员信息
   orgInfo.orgContactright = $("#orgContactright").val(); 
   orgInfo.orgContnm = $("#orgContnm").val(); 
   orgInfo.orgContnation = $("#orgContnation").val(); 
   orgInfo.orgContidtp = $("#orgContidtp").val();  
   orgInfo.orgContidno = $("#orgContidno").val();  
   orgInfo.orgContidvalidate = $("#orgContidvalidate").val();  
   orgInfo.orgContphone = $("#orgContphone").val();
   orgInfo.orgContfax = $("#orgContfax").val();     
   orgInfo.orgContmobile = $("#orgContmobile").val();     
   orgInfo.orgContemail = $("#orgContemail").val(); 
   orgInfo.orgContAddr = $("#orgContAddr").val();     
   orgInfo.orgContPostcode = $("#orgContPostcode").val();  
   
   // 基本->其他信息
   orgInfo.orgAddr = $("#orgAddr").val(); 
   orgInfo.orgPostcode = $("#orgPostcode").val();
   orgInfo.orgInvOfficeTel = $("#orgInvOfficeTel").val();
   orgInfo.orgInvFax = $("#orgInvFax").val();
   orgInfo.shsecacc = $("#orgShsecacc").val();
   orgInfo.szsecacc = $("#orgSzsecacc").val();
   
   // 附加->其他信息
   orgInfo.orgHoldingname = $("#orgHoldingname").val();
   orgInfo.orgHoldingidtp = $("#orgHoldingidtp").val();
   orgInfo.orgHoldingidno = $("#orgHoldingidno").val();
   orgInfo.orgHoldingidvalidate = $("#orgHoldingidvalidate").val();
   orgInfo.orgBeneficiarynm = $("#orgBeneficiarynm").val();
   orgInfo.orgRiskLevel = $("#orgRiskLevel").val(); 
   
   //原风险等级
   orgInfo.preRisklevel = $("#preRisklevel").val();
   orgInfo.preInvprtp = $("#invprtp").val();
   // 特殊用户风险等级
   orgInfo.specriskLevel = "0";
   if($("#specRiskLevel").is(':checked')){
 	  orgInfo.orgRiskLevel = "1";
 	  orgInfo.specriskLevel = "1";
   }
   
   // 附加->其他证件信息
   var oidtpStr = "";
   var oidnoStr = "";  
   var oidvalidateStr = "";
   $(".orgInvIdtpTd select[id='ooidtp']").each(function(data){
      if($(this).val()=="" || $(this).val() == null)
      {
         oidtpStr += "$,";
      }
      else
      {      
         oidtpStr += $(this).val()+",";
      }
   });
   $("input[id='ooidno']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oidnoStr += "$,";
      }
      else
      {      
         oidnoStr += $(this).val()+",";
      }
   });
   // 由于增加了长期有效功能，原使用ID取值有误，现改为name取值
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
                                         
   // 附加->其他经办人信息
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
   // 由于增加了长期有效功能，原使用ID取值有误，现改为name取值
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
                                               
   // 资料信息
   var document="";
   $("input[name='doccheck']").each(function(data)
   {
      if($(this).is(":checked"))
      {
         document +=$(this).attr("id")+"-"+"1"+",";
      }
      else
      {
         document +=$(this).attr("id")+"-"+"0"+",";
      }         
   });
   orgInfo.docbusinesstp = $("#docbusinesstp").val(); 
   orgInfo.isalldoc = $("#isalldoc").val();        
   orgInfo.document = document;
   orgInfo.iforiginal = $("#isoriginal").val();   
   orgInfo.ifsaved = $("#issaved").val();
   orgInfo.isupload = $("#isupload").val();
   orgInfo.remarkinfo = $("#remarkinfo").val();
   

	orgInfo.isscan = $("#isscan").val();
	orgInfo.keepaddress = $("#keepaddress").val();
	orgInfo.salesaccmanager = $("#salesaccmanager").val();
	orgInfo.fileno = $("#fileno").val(); 
       
   return orgInfo;       
}

function getBankInfoObj()
{
   var qParam = {}; 
   qParam.tradeaccos = $("#tradeaccos").val();
   qParam.openName = trim($("#openName").val());
   qParam.bankAccoNm = $("#bankAccoNm").val();
   qParam.openAddr = $("#openAddr").val();
   qParam.openbankcity = $("#openbankcity").val();
   qParam.bnkNo = $("#bnkNo").val();
   qParam.bankAcco = $("#bankAcco").val();
   qParam.permissionId = $("#permissionId").val();
   qParam.operatorId = $("#operatorId").val();
   qParam.trustType = $("#trustType").val();      
   return qParam;
}


function getCategoryInfoObj()
{
   var qParam = {}; 
   qParam.custnos = $("#custnos").val();
   if(invtp=='1')
   {
      qParam.custsimpnm = "";
      qParam.instrepcode = "";      
   }
   else
   {
      qParam.custsimpnm = $("#custsimpnm").val();
      qParam.instrepcode = $("#instrepcode").val();
   }
   qParam.businessTp = $("#businessTp").val();
   qParam.companyTp = $("#companyTp").val();
   qParam.regionTp = $("#regionTp").val();
   qParam.fxqTp = $("#fxqTp").val();
   qParam.fxqDesc = $("#fxqDesc").val();
   
   qParam.permissionId = $("#permissionId").val();
   qParam.operatorId = $("#operatorId").val();

   return qParam;
}

// --------------------------------------取提交所需信息---------------------------------------------------

function dosubmit()
{
	var pageInfo ={};
    var baseInfo;
    if(custtp=='0')
    {
  	  baseInfo = getOrgInfo();
    }
    else
    {
  	  baseInfo = getPersonalInfo();
    }
    pageInfo['baseInfo'] = baseInfo;
    pageInfo['bankInfo'] = getBankInfoObj();
    pageInfo['categoryInfo'] = getCategoryInfoObj();
    
    if(!validateinfo(baseInfo)){
    	return false;
    }
    if(!validateBankinfo(pageInfo['bankInfo'])){
      	return false;
    }
   
   $.ajax({
		url: ACCOUNT_PATH+"modifySyntheSizeInfo.do",  
		dataType: "json",
		type: "POST",
		data:  {
			"baseInfo" : JSON.stringify(pageInfo)
		},
		cache: false,
		async: false,
		success: function(data) {
			var errcode = data.errcode;
		    var errMsg =data.errmsg;
		    if(errcode == "0000"){
		    	ctools.alert_sweet('申请提交成功！', "success", "" , function(){
		    		window.close();
				});
		    }else{
		    	ctools.alert_sweet('申请提交失败！', "error", "失败原因："+errMsg);
		    }
		}
	}); 
}


function validateinfo(baseInfo) {
	var nowdate = nowDate;
	var invnm = $("#invnm").val();
	var idno = $("#idno").val();
	if (baseInfo.custTp == 1) {
		// 修改信息控件
		var pchidinfo = document.getElementById("pchidinfo").checked;// 证件信息
		var pchotherinfo = document.getElementById("pchotherinfo").checked;// 其它信息
		if (pchidinfo) {
			if (baseInfo.pslInvNm == "") {
				ctools.alert("请输入投资者名称！","","error");
				$("#pslInvNm").focus();
				return false;
			}
			if (baseInfo.pslInvIdno == "") {
				ctools.alert("请输入注册登记证件号码！","","error");
				$("#pslInvIdno").focus();
				return false;
			}
			if (trim(invnm) != trim(baseInfo.pslInvNm)
					&& trim(idno) != trim(baseInfo.pslInvIdno)) {
				ctools.alert("投资者名称和证件号码只能修改一项！","","error");
				return false;
			}
			if (trim(baseInfo.pslInvIdValidate) == ''
					|| trim(baseInfo.pslInvIdValidate) <= nowdate) {
				ctools.alert("注册登记证件有效期为空或已过期！","","error");
				return false;
			}
		}
		if (pchotherinfo) {
			if (baseInfo.pslInvOfficeTel == '' && baseInfo.pslInvHomeTel == ''
					&& baseInfo.pslInvMobile == '') {
				ctools.alert("办公电话、住宅电话、移动电话至少要填一项！","","error");
				return false;
			}
			if (baseInfo.plsAddr == "") {
				ctools.alert("请输入通讯地址！","","error");
				$("#plsAddr").focus();
				return false;
			}
			if (trim(baseInfo.plsPostCode) == "") {
				ctools.alert("请输入邮政编码！","","error");
				$("#plsPostCode").focus();
				return false;
			}
		}
		if (!pchidinfo && !pchotherinfo) {
			ctools.alert("基本信息未变更！","","error");
			return false;
		}
	} else {
		var orgchidinfo = document.getElementById("orgchidinfo").checked;// 证件信息
		var orgchinstrepinfo = document.getElementById("orgchinstrepinfo").checked;// 法人信息
		var orgchprincipalinfo = document.getElementById("orgchprincipalinfo").checked;// 机构负责人信息
		var orgchcontinfo = document.getElementById("orgchcontinfo").checked;// 经办人信息
		var orgchotherinfo = document.getElementById("orgchotherinfo").checked;// 其它信息
		if (orgchidinfo) {// 验证证件信息
			if (baseInfo.orgInvNm == "") {
				ctools.alert("请输入投资者名称！","","error");
				$("#orgInvNm").focus();
				return false;
			}
			if (baseInfo.orgInvIdno == "") {
				ctools.alert("请输入注册登记证件号码！","","error");
				$("#orgInvIdno").focus();
				return false;
			}
			if (trim(invnm) != '' && trim(idno) != ''
					&& trim(invnm) != trim(baseInfo.orgInvNm)
					&& trim(idno) != trim(baseInfo.orgInvIdno)) {
				ctools.alert("投资者名称和注册登记证件号码只能修改一项！","","error");
				return false;
			}
			if (baseInfo.orgInvIdvalidate == ''
					|| baseInfo.orgInvIdvalidate <= nowdate) {
				ctools.alert("注册登记证件有效期为空或已过期！","","error");
				return false;
			}
		}

		if (orgchinstrepinfo) {// 验证法人信息
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
				ctools.alert("法定代表人证件有效期为空或已过期！","","error");
				return false;
			}
		}

		if (orgchprincipalinfo) {// 验证机构负责人信息
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
				ctools.alert("机构负责人证件有效期为空或已过期！","","error");
				return false;
			}
		}

		if (orgchcontinfo) {// 验证经办人信息
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
				ctools.alert("经办人证件有效期为空或已过期！","","error");
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
		}
		if (orgchotherinfo) {
			if (baseInfo.orgAddr == "") {
				ctools.alert("请输入通讯地址！","","error");
				$("#orgAddr").focus();
				return false;
			}
			if (trim(baseInfo.orgPostcode) == "") {
				ctools.alert("请输入邮政编码！","","error");
				$("#orgPostcode").focus();
				return false;
			}
		}
		if (trim(baseInfo.orgHoldingidvalidate) != ''
				&& trim(baseInfo.orgHoldingidvalidate) <= nowdate) {
			ctools.alert("控股股东证件已过期！","","error");
			return false;
		}
		if (!orgchidinfo && !orgchinstrepinfo && !orgchprincipalinfo
				&& !orgchcontinfo && !orgchotherinfo) {
			ctools.alert("基本信息未变更！","","error");
			return false;
		}
	}
	return true;
}


function validateBankinfo(qParam) {
	// 银行信息校验
	if (qParam.openName == '') {
		ctools.alert("请输入开户银行！","","error");
		$("#openName").focus();
		return false;
	}
	if (qParam.bankAccoNm == '') {
		ctools.alert("请输入银行户名！","","error");
		$("#bankAccoNm").focus();
		return false;
	}
	if (qParam.openAddr == '') {
		ctools.alert("请选择开户地省！","","error");
		$("#openAddr").focus();
		return false;
	}
	if (qParam.openbankcity == '') {
		ctools.alert("请选择开户地市！","","error");
		$("#openbankcity").focus();
		return false;
	}
	if (qParam.bankAcco == '') {
		ctools.alert("请输入银行账号！","","error");
		$("#bankAcco").focus();
		return false;
	}
	return true;
}

function initVal(){
	$("#openAddr").change();
}


function getInvestProInstSecond(element) {
	var hiInvestProInstSecond = $("#hiInvestProInstSecond").val();
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
		resetInvestClassTh("investClassInfo", "classInfoTh");
		resetInvestClassTh("instInvestPro", "instInvestProTh");
		$("#investProInstSecond").val(hiInvestProInstSecond);
		$("#investProInstSecond").select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"}).trigger('change');
		return;
	}
	;
	// 其他法人及组织 普通投资者 开户为 机构
	if ((("1" == invprtp) || ("0" == invprtp)) && ("0" == selOpenType)) {
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
			}
			$("#investProInstSecond").val(hiInvestProInstSecond);
			$("#investProInstSecond").select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"}).trigger('change');
			resetInvestClassTh("investClassInfo", "classInfoTh");
			resetInvestClassTh("instInvestPro", "instInvestProTh");
		},
		error : function() {
			ctools.alert("查询失败！","","error");
		}
	});
}

//获取客户二级分组
function getSecond(pmco) {
	var hinstrepcode = $("#hinstrepcode").val();
	var html = "<option value=''>--</option>";
	$("#instrepcode").html(html);
	if(pmco == ""){
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
					if(hinstrepcode == item.PMCO){
						html+="<option value=\""+item.PMCO+"\" selected='selected'>"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
					}else{
						html+="<option value=\""+item.PMCO+"\">"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
					}
				}
			}
			$("#instrepcode").html(html);
			$("#instrepcode").val(hinstrepcode);
			$("#instrepcode").select2("destroy");
			$("#instrepcode").select2().trigger('change');
		},
		error : function() {
			ctools.alert("客户二级分组查询失败！","","error");
		}
	});
}

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

//获取城市集合
function getCitys(pmco, flag) {
	if (flag == '1') {
		city = $("#openbankcity");
		selectCity = "#hiddencity";
	} else {
		city = $("#openbankcity");
		selectCity = "#hiddencity";
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