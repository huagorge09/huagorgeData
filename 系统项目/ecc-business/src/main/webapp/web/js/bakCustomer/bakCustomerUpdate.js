var PRIMARY_PATH = "";
var BASE_PATH = "";

function setAddPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var oidlist = $("#oidlist").val();
oidlist = !!oidlist ? eval(oidlist) : "";
var ocontlist = $("#ocontactlist").val();
ocontlist = !!ocontlist ? eval(ocontlist) : "";
var num = ocontlist.length;
//初始化页面
$(function(){
	WASP_WIDGET.triggerDateStyleWithYMD("orgInstrepidvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgPrincipalidvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgContidvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgHoldingidvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("orgInvIdvalidate");
	WASP_WIDGET.triggerDateStyleWithYMD("birthDate");
	WASP_WIDGET.triggerDateStyleWithYMD("BirthDate2");
	showBaseInfo();
	initData(oidlist, ocontlist);
	$('.select2_width').select2({allowClear: true,minimumResultsForSearch:Infinity});
	$('.select2-q').select2({width: "180px"});
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$("#claseBtn").click(function() {
		setTimeout(function(){
			$("select").change();
		}, 100);
		$("#reset").click();
	});
});
//初始化数据（经办人、证件类型）
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



//切换页签
function showTypeContent(id,tId){
	$(".typeContent").hide();
	$("#"+id).show();
	if(tId == 'btnBaseInfo'){
		baseInfo();
	}else if(tId == 'btnExtInfo'){
		extInfo();
	}
};

function showBaseInfo()//显示基本信息
{
	$("#tabPslBaseInfo").hide();
	$("#tabOrgBaseInfo").show();
}

baseInfo();
$(document).ready(function() {
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
});

function baseInfo() // 显示基本信息
{
	showBaseInfo();
	hideExtInfo();
}
function extInfo()// 附加信息
{
	hideBaseInfo();
	showExtInfo();
}
/** *******************0414修改************************************** */



 
function showExtInfo()// 显示附加信息
{
	$("#tabOrgExtInfo").show();
}
function hideExtInfo()// 隐藏附加信息
{
	$("#tabOrgExtInfo").hide();
}
function hideBaseInfo()// 隐藏基本信息
{
	$("#tabOrgBaseInfo").hide();
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

/********************************************************获取表单内容*********************************************************************************/
function getOrgInfo()
{
   var orgInfo = {};    
   orgInfo.custno = $("#custno").val(); 
   orgInfo.opertp = "U";        
   orgInfo.permissionId = $("#permissionId").val();
   orgInfo.operatorId = $("#operatorId").val();
   //基本->证件信息
   orgInfo.invNm = trim($("#orgInvNm").val());  
   orgInfo.invIdtp = $("#orgInvIdtp").val(); 
   orgInfo.invIdno = trim($("#orgInvIdno").val());
   orgInfo.invIdValidate = $("#orgInvIdvalidate").val();        
   orgInfo.offInvSerialno = trim($("#offInvSerialno").val()); 
   
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
   orgInfo.bakCustRole = trim($("#bakCustRole").val());
   orgInfo.riskLevel = trim($("#riskLevel").val());
   

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
   orgInfo.oidtpStr = trim(oidtpStr);
   orgInfo.oidnoStr = trim(oidnoStr);
   orgInfo.oidvalidateStr = trim(oidvalidateStr); 
   
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
   
   $(".orgContactrightTd select[id='oocontactgrant']").each(function(data)
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
   orgInfo.oOrgContactright = trim(oOrgContactright); 
   orgInfo.oOrgContact = trim(oOrgContact);  
   orgInfo.oOrgContactnation = trim(oOrgContactnation); 
   orgInfo.oOrgContactidtp = trim(oOrgContactidtp);    
   orgInfo.oOrgContactidno = trim(oOrgContactidno);     
   orgInfo.oOrgContactidvalidate = trim(oOrgContactidvalidate);    
   orgInfo.oOrgContacttel = trim(oOrgContacttel);  
   orgInfo.oOrgContactfax = trim(oOrgContactfax);   
   orgInfo.oOrgContactmobile = trim(oOrgContactmobile);  
   orgInfo.oOrgContactemail = trim(oOrgContactemail);
   orgInfo.oOrgContactAddr = trim(oOrgContactAddr);
   orgInfo.oOrgContactPostcode = trim(oOrgContactPostcode);
   return orgInfo;   
}


//------------------------------------其他证件和经办人信息---------------------------------------------------------------------------------------- 
//增加证件信息
var idflag=0;//用于动态设置证件有效期的长期有效功能，由于ID改变，原取值方式也需要修改
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
 var trstr = "<tr><td style='text-align: center;' colspan='4'>证件信息" + idflag + "</td></tr>";
 //第二行      
 trstr +="<tr><td>证件类型：</td><td class='white-bg orgInvIdtpTd'>";
 trstr += $(".orgInvIdtpDiv").html();
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
/* trstr +="<tr><td colspan = '4' align='right'><input class='btn btn-primary btn-add' type=button value='删除' id='btnDelId"+idNum+"' onclick='delIdRow(this);'/></td></tr>";
*/ $("#oidinfo").append(trstr);
 $(".orgInvIdtpTd .orgInvIdtp").addClass("select2");
 $('.orgInvIdtpTd .orgInvIdtp').select2({allowClear: false,minimumResultsForSearch:Infinity}).trigger('change');
 
 if(!!idtp){
		 $("#oidinfo #ooidtp").eq(idflag-1).val(idtp);
		 $("#oidinfo #ooidtp").eq(idflag-1).change();
	 }
	 $("#oidinfo #ooidno").eq(idflag-1).val(idno);
	 $("#oidinfo #ooidvalidate"+idflag).eq(idflag-1).val(idvalidate);
 
 WASP_WIDGET.triggerDateStyleWithYMD("ooidvalidate"+idflag);
}  

//增加经办人
var valueFlag = 0;
var contflag=0;//用于动态设置证件有效期的长期有效功能，由于ID改变，原取值方式也需要修改
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
var trstr = "<tr><td style='text-align: center;' colspan='4'>经办人信息"+ contflag + "</td></tr>";
//第二行
trstr +="<tr><td>经办人授权范围：</td><td class='orgContactrightTd white-bg'>";
trstr += $(".orgContactrightDiv").clone(true).find("#oocontactgrant").attr("id","oocontactgrant").attr("oocontactgrant-flag"+valueFlag,contgrant).attr("value",contgrant).end().html();

trstr +="</td><td>经办人姓名：</td><td class='white-bg'>";
trstr += $("#continfo>tr:eq(0)").find('td').eq(3).clone(true).find("#orgContnm").attr("id","oocontact").attr("value",contnm).end().html();
trstr +="</td></tr>";               
//第三行
trstr +="<tr>";
trstr +="<td>经办人国籍：</td><td class='orgContnationTd white-bg'>"+$(".orgContnationDiv").clone(true).find(".orgContnation").attr("id","oocontactnation").attr("oocontactnation-flag"+valueFlag,contnation).attr("value",contnation).end().html()+"</td>";
trstr +="<td>经办人证件类型：</td>" +
		   "<td class='orgContidtpTd white-bg'>"+$(".orgContidtpDiv").clone(true).find(".orgContidtp").attr("id","oocontidtp").attr("oocontidtp-flag"+valueFlag,contidtp).attr("value",contidtp).end().html()+"</td>";
trstr +="</tr>";
//第四行
trstr +="<tr>";
trstr +="<td>经办人证件号码：</td>";
trstr +="<td class='white-bg'><div class='col-sm-11 form-inner'><input type='text' name='oocontidno' id='oocontidno' class='form-control' value='"+contidno+"' maxlength='18'></div></td>";
trstr +="<td>经办人证件有效期：</td>";
trstr +="<td class='white-bg'><div class='col-sm-11 form-inner'>" +
		   "<input name='oocontvalidate' style='float: left;' class='form-control' id='oocontvalidate"+contflag+"' type='text' value='"+contidvalidate+"'/>";
trstr +='<label style="line-height: 34px;">';
trstr +='<input class="i-checks" id="changeocontvalidate'+contflag+'" type="checkbox" ';
trstr +='onclick="changetime(\'changeocontvalidate'+contflag+'\',\'oocontvalidate'+contflag+'\');"/>长期</label><br></div>';
trstr +="</td></tr>";
//第五行
trstr +="<tr>";
trstr +=$("#continfo>tr:eq(4)").clone(true).find("#orgContphone").attr("id","oocontphone").attr("value",conttel).end().find("#orgContfax").attr("id","oocontfax").attr("value",contfax).end().html();
trstr +="</tr>";  
//第六行
trstr +="<tr>";
trstr +=$("#continfo>tr:eq(5)").clone(true).find("#orgContmobile").attr("id","oocontmobile").attr("value",contmobile).end().find("#orgContemail").attr("id","oocontemail").attr("value",contemail).end().html();
trstr +="</tr>";
//第七行
trstr +="<tr>";
trstr +=$("#continfo>tr:eq(6)").clone(true).find("#orgContAddr").attr("id","oocontAddr").attr("value",contAddr).end().find("#orgContPostcode").attr("id","oocontPostcode").attr("value",contPostcode).end().html();
trstr +="</tr>";
//第八行
/*trstr +="<tr><td colspan='4' align='right'><input class='btn btn-primary btn-add' type=button value='删除' id='btnDelCont"+idNum+"' onclick='delContRow(this);'/></td></tr>";
*/$("#ocontInfo").append(trstr);
if(valueFlag <= (num - 1 )){
	$("select[oocontactgrant-flag"+valueFlag+"]").val($("select[oocontactgrant-flag"+valueFlag+"]").attr("value"));
	$("select[oocontactgrant-flag"+valueFlag+"]").change();
	$("select[oocontactnation-flag"+valueFlag+"]").val($("select[oocontactnation-flag"+valueFlag+"]").attr("value"));
	$("select[oocontactnation-flag"+valueFlag+"]").change();
	$("select[oocontidtp-flag"+valueFlag+"]").val($("select[oocontidtp-flag"+valueFlag+"]").attr("value"));
	$("select[oocontidtp-flag"+valueFlag+"]").change();
}
valueFlag = Number(valueFlag)+1;

/*
if (!!contgrant) {
		$("#oocontactgrant").eq(flag).val(contgrant);
	}
	if (!!contnation) {
		$("#oocontactnation").eq(flag).val(contnation);
	}
	if (!!contnation) {
		$("#oocontidtp").eq(flag).val(contidtp);
	}

	 $("#oocontact").eq(flag).val(contnm);
	 $("#oocontidno").eq(flag).val(contidno);
	 $("#oocontvalidate1").eq(flag).val(contidvalidate);
	 $("#oocontphone").eq(flag).val(conttel);
	 $("#oocontfax").eq(flag).val(contfax);
	 $("#oocontmobile").eq(flag).val(contmobile);
	 $("#oocontemail").eq(flag).val(contemail);
	 $("#oocontAddr").eq(flag).val(contAddr);
	 $("#oocontPostcode").eq(flag).val(contPostcode);*/
	 
	 

$(".orgContactrightTd select").addClass("select2");
$('.orgContactrightTd select').select2({allowClear: false,minimumResultsForSearch:Infinity}).trigger('change');

$(".orgContnationTd select").addClass("select2");
$(".orgContnationTd select").select2({allowClear: false,minimumResultsForSearch:Infinity}).trigger('change');

$(".orgContidtpTd select").addClass("select2");
$(".orgContidtpTd select").select2({allowClear: false,minimumResultsForSearch:Infinity}).trigger('change');

WASP_WIDGET.triggerDateStyleWithYMD("oocontvalidate"+contflag);
flag++;
}

//删除证件信息
function delIdRow()
{	
	var count = $("#oidinfo>tr").length;
	if(count > 5)
    {
       $("#oidinfo>tr:last").remove();
       $("#oidinfo>tr:last").remove();
       $("#oidinfo>tr:last").remove();
 	   idflag = Number(idflag) - 1;
    }
}

//删除经办人信息
function delContRow()
{
 var count = $("#ocontInfo>tr").length;
 if(count > 9){
	 $("#ocontInfo>tr:last").remove();
     $("#ocontInfo>tr:last").remove();
     $("#ocontInfo>tr:last").remove();
     $("#ocontInfo>tr:last").remove();
     $("#ocontInfo>tr:last").remove();
     $("#ocontInfo>tr:last").remove();
     $("#ocontInfo>tr:last").remove();    
	 contflag = Number(contflag) - 1;
 }
 
}   

function dosubmit()
{
	/*autofillContid();*/
	var baseInfo;
	baseInfo = getOrgInfo(); 
	if(!validateinfo(baseInfo)){
		return false;
	}
	$.ajax({
		url: PRIMARY_PATH+"/updateBakCustomer.xhtml",  
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
}

function handleOpen(obj)
{
   var errcode = obj.errcode;
   var errMsg =obj.errmsg;
   if(errcode == "0000"){
   		ctools.alert_sweet('修改成功！', "success", "" , function(){
   			window.opener.queryByCondtion(true);
    		window.close();		
		});
   }else{
   		ctools.alert_sweet('修改失败！', "error", "失败原因："+errMsg);
   }
}



function validateinfo(baseInfo){
	var nowdate = nowDate;
	if (baseInfo.invNm == "") {
		ctools.alert("请输入投资者名称！","","error");
		$("#orgInvNm").focus();
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
	var typeAddr = "通讯地址";
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
	return true;
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



function initWidget(){
	//注册清空事件
    WASP_WIDGET.registerResetClearEvent();
}

