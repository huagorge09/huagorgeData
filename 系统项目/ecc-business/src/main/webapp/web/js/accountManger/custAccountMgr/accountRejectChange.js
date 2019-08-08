var PRIMARY_PATH = "";
var ACCOUNT_PATH = "";
var PROJEC_TPATH = "";

function setPath(projectPath,path,accountPath){
	PROJEC_TPATH = projectPath;
	PRIMARY_PATH = path;
	ACCOUNT_PATH = accountPath;
}

var oidlist = $("#oidlist").val();
oidlist = !!oidlist ? eval(oidlist) : "";
var ocontlist = $("#ocontactlist").val();
ocontlist = !!ocontlist ? eval(ocontlist) : "";
var doclist = $("#documentlist").val();
doclist = !!doclist ? eval(doclist) : "";

var dto = $("#dto").val();
dto = !!dto ? JSON.parse(dto) : "";


var apkind = $("#dsapkind").val();

$(function(){
	
	$('.select2_width').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_q').select2({width: "160px"});
	
	
	$("#ptaxType").change();
	$("#otaxType").change();
	$("#pinvprtp").change();
	$("#oinvprtp").change();
	$("#investProInstType").change();
	$("#negativeNotFinaInst").change();
	
	initData(dto);
	
	doSelect($("#docbusinesstpVal").val());

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
		doSubmit();
	});
	
	//queryFileNo();

	var specRiskLevel = $("#specriskLevel").val();
    if("1" == specRiskLevel){
	    $("#indSpecRiskLevel").click();
	    $("#instSpecRiskLevel").click();
    }
    
    $("#userCustName").val($("#invnm").val());
    

	$("#isalldoc").change(function() {
		if ($("#isalldoc").val() == 1) {
			$("#documentinfo input[type='checkbox']").each(function(data) {
				$(this).prop("checked", true);// 全选
			});
		} else {
			$("#documentinfo input[type='checkbox']").each(function(data) {
				$(this).prop("checked", false);// 取消全选
			});
		}
	});
    
	$("#btnBack").click(function() {
		setTimeout(function(){
			$("select").change();
		}, 100);
		$("#reset").click();
	});
	
	initCategoryInfo();
	
    WASP_WIDGET.triggerDateStyleWithYMD("oinstrepvalidate");
    WASP_WIDGET.triggerDateStyleWithYMD("oprincipalvalidt");
    WASP_WIDGET.triggerDateStyleWithYMD("ocontvalidate");
    WASP_WIDGET.triggerDateStyleWithYMD("oholdingvalidate");
    WASP_WIDGET.triggerDateStyleWithYMD("oidvalidate");
    WASP_WIDGET.triggerDateStyleWithYMD("pidvalidate");
    WASP_WIDGET.triggerDateStyleWithYMD("birthDate");
	WASP_WIDGET.triggerDateStyleWithYMD("BirthDate2");
    
});




function initData(obj)
{
   if(obj.invtp=='1')
   {
      initPLS(obj);
      $("#tabPslInfo").show();
      $("#tabOrgInfo").hide();
      //2010-11-03 wdw add
      if(apkind == '003'){//如果是基本资料修改，隐藏银行信息
      	$("#pbankinfo").hide();
      }else if(apkind == '0B1'){//如果是银行资料修改，隐藏除银行以外的信息
      	$("#pidtpinfo").hide();
      	$("#potherinfo").hide();
      	$("#tabDocument").hide();
      }
   
   }
   else
   {
      initORG(obj);
      $("#tabPslInfo").hide();
      $("#tabOrgInfo").show();
      if(apkind == '003'){//如果是基本资料修改，隐藏银行信息
      	$("#obankinfo").hide();
      }else if(apkind == '0B1'){//如果是银行资料修改，隐藏除银行以外的信息
      	$("#idinfo").hide();
      	$("#oinstrepinfo").hide();
      	$("#oprincipalinfo").hide();
      	$("#continfo").hide();
      	$("#ootherinfo").hide();
      	$("#oidinfo").hide();
      	$("#ocontInfo").hide();
      	$("#tabDocument").hide();
      }
   }
    $('.select2_width').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_q').select2({width: "160px"});
}

function initPLS(obj)
{    
   $("#ptano").empty().append(obj.tano);
   $("#serialno").val(obj.serialno);
   $("#pcustno").empty().append(obj.custno);
   var custno=obj.custno;
   $("#custons").val(custno);
  
   $("#pdsapkind").empty().append(obj.dsapkindnm);
   $("#pfundacco").val(obj.fundacco);
   $("#pfundaccodiv").empty().append(obj.fundacco);
   $("#ptradeacco").val(obj.tradeacco);
   $("#ptradeaccodiv").empty().append(obj.tradeacco);
   
   $("#pinvnm").val(obj.invnm);
   $("#pidtp").val(obj.idtp);      
   $("#pidno").val(obj.idno);   
   $("#pidvalidate").val(obj.idvalidate); 
   
   $("#pbnkNO").val(obj.bnkNo);
   $("#popenName").val(obj.openName);        
   $("#pbankaccoNm").val(obj.bankAccoNm);
   $("#pbankAcco").val(obj.bankAcco);       
   $("#popenAddr").val(obj.openAddr); 
   $("#hiddenpopenbankcity").val(obj.openBankCity);
   $("#popenAddr").change();
   
   $("#psex").val(obj.sex);
   $("#pnationalitycode").val(obj.nationalitycode);      
   $("#pedlevel").val(obj.edlevel);   
   $("#pvoccode").val(obj.voccode);       
   $("#pincome").val(obj.income);   
   $("#pcustrisklevl").val(obj.custrisklevl);
   $("#ptel").val(obj.tel);      
   $("#phousetel").val(obj.housetel);   
   $("#pmobile").val(obj.mobile);       
   $("#pfax").val(obj.fax);   
   $("#pfaxdelegate").val(obj.faxdelegate);
   $("#pemail").val(obj.email);
   $("#ppostcode").val(obj.postcode);       
   $("#paddr").val(obj.addr);
   $("#pslShsecacc").val(obj.shsecacc);
   $("#pslSzsecacc").val(obj.szsecacc);
   
   $("#pcustabbrcode").val(obj.acctabbr);
   $("#pcustabbrcode").change();
   $("#pamlrisktype").val(obj.amlrisktype);
   $("#pfxqremark").val(obj.fxqremark);
   
   $("#docbusinesstp").val(obj.docbusinesstp);
   $("#isalldoc").val(obj.ifalldocument);
   $("#isoriginal").val(obj.ifOriginal);      
   $("#issaved").val(obj.ifsaved);
   $("#isupload").val(obj.isupload);
   $("#remarkinfo").val(obj.remarkinfo);
   $("#isscan").val(obj.isscan);
   $("#keepaddress").val(obj.keepaddress);
   $("#salesaccmanager").val(obj.salesaccmanager);
   $("#tabPslInfo select").change();
}

function initORG(obj){
	   
   $("#serialno").val(obj.serialno);
   $("#otano").empty().append(obj.tano);
   $("#ocustno").empty().append(obj.custno);
   var custno=obj.custno;
   
   $("#custons").val(custno);
   
   $("#odsapkind").empty().append(obj.dsapkindnm);
   $("#ofundacco").val(obj.fundacco);
   $("#ofundaccodiv").empty().append(obj.fundacco);
   $("#otradeacco").val(obj.tradeacco);
   $("#otradeaccodiv").empty().append(obj.fundacco);
   
   $("#oinvnm").val(obj.invnm);
   $("#oidtp").val(obj.idtp);      
   $("#oidno").val(obj.idno);   
   $("#oidvalidate").val(obj.idvalidate); 
   
   $("#obnkNO").val(obj.bnkNo);      
   $("#oopenName").val(obj.openName);      
   $("#obankaccoNm").val(obj.bankAccoNm);   
   $("#obankAcco").val(obj.bankAcco);       
   $("#oopenAddr").val(obj.openAddr); 
   $("#oopenAddr").change();
   //$("#oopenbankcity").val(obj.openBankCity); 
   $("#hiddenoopenbankcity").val(obj.openBankCity);
   
   $("#ocontactgrant").val(obj.contactgrant);
   $("#ocontact").val(obj.contact);      
   $("#ocontactnation").val(obj.contactnation);   
   $("#ocontidtp").val(obj.contidtp);       
   $("#ocontidno").val(obj.contidno); 
   $("#ocontvalidate").val(obj.contvalidate);
   $("#ocontphone").val(obj.contphone);      
   $("#ocontfax").val(obj.contfax);   
   $("#ocontmobile").val(obj.contmobile);       
   $("#ocontemail").val(obj.contemail);
   $("#orgContAddr").val(obj.contAddr);
   $("#orgContPostcode").val(obj.contPostcode);             
   
   $("#oprincipalname").val(obj.principalname);
   $("#oprincipalnation").val(obj.principalnation);      
   $("#oprincipalidtp").val(obj.principalidtp);   
   $("#oprincipalidno").val(obj.principalidno);       
   $("#oprincipalvalidt").val(obj.principalvalidt);       
	
   $("#oinstrepnm").val(obj.instrepnm);
   $("#oinstrepnation").val(obj.instrepnation);      
   $("#oinstrepidtp").val(obj.instrepidtp);   
   $("#oinstrepidno").val(obj.instrepidno);       
   $("#oinstrepvalidate").val(obj.instrepvalidate);  
                   
   $("#oholdingname").val(obj.holdingname);      
   $("#oholdingidtp").val(obj.holdingidtp);   
   $("#oholdingidno").val(obj.holdingidno);       
   $("#oholdingvalidate").val(obj.holdingvalidate);
   $("#obeneficiary").val(obj.beneficiary);   
   $("#ocustrisklevl").val(obj.custrisklevl);
   $("#oofficeTel").val(obj.tel);       
   $("#ofax").val(obj.fax);
   $("#opostcode").val(obj.postcode);       
   $("#oaddr").val(obj.addr);
   
   $("#orgShsecacc").val(obj.shsecacc);
   $("#orgSzsecacc").val(obj.szsecacc);

   $("#custabbrcode").val(obj.acctabbr);
   $("#custabbrcode").change();
   $("#amlrisktype").val(obj.amlrisktype);
   $("#fxqremark").val(obj.fxqremark);  
   
   $("#docbusinesstp").val(obj.docbusinesstp);
   $("#isalldoc").val(obj.ifalldocument);
   $("#isoriginal").val(obj.ifOriginal);      
   $("#issaved").val(obj.ifsaved);
   $("#isupload").val(obj.isupload);
   $("#remarkinfo").val(obj.remarkinfo);
   $("#isscan").val(obj.isscan);
   $("#keepaddress").val(obj.keepaddress);
   $("#salesaccmanager").val(obj.salesaccmanager);
   $("#tabOrgInfo select").change();

       
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



function doSubmit()
{
	var custtp = $("#invtp").val();
    var custInfo;
    if(custtp == '1')  
    {
       custInfo = getPersonalInfo();
    }
    else
    {
       custInfo = getOrgInfo();
    }
    if(!validateinfo(custInfo)){
    	return false;
    }


    var flag = checkResidentInfo();
    if(!flag){
		  return;
    }
    //客户号
    custInfo.custno = $("#custno").val();
    //税收居民信息       
    custInfo.taxresident = $("input[name=taxresident]").val();
    
    $.ajax({
		url: ACCOUNT_PATH+"accountRejectModify.do",  
		dataType: "json",
		type: "POST",
		data:  {
			"custInfo" : JSON.stringify(custInfo)
		},
		cache: false,
		async: false,
		success: function(data) {
			var errcode = data.errcode;
		    var errMsg =data.errmsg;
		    if(errcode == "0000"){
		    	ctools.alert_sweet('申请提交成功！', "success", "" , function(){
		    		window.opener.queryByCondtion(true);
		    		window.close();
				});
		    }else{
		    	ctools.alert_sweet('申请提交失败！', "error", "失败原因："+errMsg);
		    }
		}
	});
    
    
}

function doBack() {
	window.close();
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
 $("#oidinfo #ooidno").eq(idflag-1).val(idno);
 $("#oidinfo #ooidvalidate"+idflag).eq(idflag-1).val(idvalidate);
 
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
 trstr += $("#continfo>tr:eq(0)").find('td').eq(3).clone(true).find("#ocontact").attr("id","oocontact").attr("value",contnm).end().html();
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
 trstr +=$("#continfo>tr:eq(3)").clone(true).find("#ocontphone").attr("id","oocontphone").attr("value",conttel).end().find("#ocontfax").attr("id","oocontfax").attr("value",contfax).end().html();
 trstr +="</tr>"; 
 //第六行
 trstr +="<tr>";
 trstr +=$("#continfo>tr:eq(4)").clone(true).find("#ocontmobile").attr("id","oocontmobile").attr("value",contmobile).end().find("#ocontemail").attr("id","oocontemail").attr("value",contemail).end().html();
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

// 删除证件信息
function delIdRow(rownum) {
	var count = $("#oidinfo>tr").length;
	var ss = 0;
	for (var i = 0; i < count; i++) {
		if ($("#oidinfo>tr").eq(i).html() == $(rownum).parents().parents()
				.html()) {
			ss = i;
		}
	}
	var trRow = ss - 3;
	for (var i = 0; i < 4; i++) {
		$("#oidinfo>tr").eq(trRow).remove();
	}
}

// 删除经办人信息
function delContRow(rownum) {
	var count = $("#ocontInfo>tr").length;
	var ss = 0;
	for (var i = 0; i < count; i++) {
		if ($("#ocontInfo>tr").eq(i).html() == $(rownum).parents().parents()
				.html()) {
			ss = i;
		}
	}
	var trRow = ss - 7;
	for (var i = 0; i < 8; i++) {
		$("#ocontInfo>tr").eq(trRow).remove();
	}
}
// ------------------------------------其他证件和经办人信息----------------------------------------------------------------------------------------
// ------------------------------------------------------------资料信息--------------------------------------------------------------------------
$.fn.selVal = function(v) {
	if ($(this).val() != v) {
		$(this).val(v).trigger("change");
	} else {
		// doSelect(v);wdw 2010-09-14注释掉
	}
};

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
	var isalldoc = $("#isalldoc").val();
	if(isalldoc == "1"){
		$("#isalldoc").change();
	}
}


//获取客户二级分组
function getSecond(pmco,id) {
	var hinstrepcode = $("#hinstrepcode").val();
	var html = "<option value=''>--</option>";
	$("#"+id).html(html);
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
			$("#"+id).html(html);
			$("#"+id).val(hinstrepcode);
			$("#"+id).select2("destroy");
			$("#"+id).select2({width: "160px"}).trigger('change');
		},
		error : function() {
			ctools.alert("客户二级分组查询失败！","","error");
		}
	});
}

//获取城市集合
function getCitys(pmco, flag,city,selectCity) {
	if (flag == '1') {
		city = $("#"+city);
		selectCity = "#"+selectCity;
	} else {
		city = $("#"+city);
		selectCity = "#"+selectCity;
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

//专业投资者信息 展示 收集
function interactInvProInfo(element) {
	var openAccType = $("#selOpenType").val();
	var invprtp = element.value;
	$("#invprtp").val(invprtp);
	var instBg = $("tbody[name=instInvestPro]");
	var perBg = $("tbody[name=personInstInvestPro]");
	var tabPslExtInfo = $("#tabPslInfo");
	var tabOrgExtInfo = $("#tabOrgInfo");

	//开户类型 机构开户 且 专业投资者 
	instBg.hide();
	perBg.hide();
	$("tr[class=instNotResidentBg]").hide();
	$("tr[class=negativeNotFinaInstBg]").hide();
	
	//个人-普通 收入 客户风险等级 展示
	tabPslExtInfo.find("#pincome").parent().parent().parent().show();
	tabOrgExtInfo.find("#ocustrisklevl").parent().parent().prev().show();
	tabOrgExtInfo.find("#ocustrisklevl").parent().parent().show();
	//个人开户 专业投资者
	if ("1" == openAccType && "0" == invprtp) {
		perBg.show();
		tabPslExtInfo.find("#pincome").parent().parent().parent().hide();
	}

	//机构开户 专业投资者
	if ("0" == openAccType && "0" == invprtp) {
		instBg.show();
		tabOrgExtInfo.find("#ocustrisklevl").parent().parent().prev().hide();
		tabOrgExtInfo.find("#ocustrisklevl").parent().parent().hide();
	}

	//机构类型为 其他法人及组织  机构开户 普通投资者
	if ("0" == openAccType && ("1" == invprtp || "0" == invprtp ) ) {
		$("tr[class=instNotResidentBg]").show();
	}else{
		$("tr[class=instNotResidentBg] select").val("0");
		$("tr[class=instNotResidentBg]").hide();
	}
	
	$("tr[class=instNotResidentBg] select").change();
	//触发资料变动
	doSelect($("#docbusinesstp").val());
	
	resetInvestClassTh("potherinfo", "potherinfoTh");
	resetInvestClassTh("ootherinfo", "ootherinfoTh");
	//个人 - 分类 专业 th 动态展示
	resetInvestClassTh("investClassInfo", "investClassInfoTh");
	resetInvestClassTh("personInstInvestPro", "personInstInvestTh");
	//机构 - 分类 专业 th 动态展示
	resetInvestClassTh("instInvestPro", "instInvestProTh");
}

/*********************0414修改***************************************/
function negativeNotFinaChange(element) {
	var value = element.value;
	var controlPerTaxDeclVal = $("#controlPerTaxDeclVal").val();
	$("tr[class=negativeNotFinaInstBg] select").val("");
	if (value == "1" || value == "2") {
		$("[class=negativeNotFinaInstBg]").show();
		if(!!controlPerTaxDeclVal){
			$("select[name=controlPerTaxDecl]").val(controlPerTaxDeclVal);
		}else{
			$("#controlPerTaxDecl").val("0");
		}
		
	} else {
		$("[class=negativeNotFinaInstBg]").hide();
		$("#controllerInfo").hide();
		$("#controllerInfo").find("input,select").val("");
	}
	$("#controlPerTaxDecl").change();
	$("#controllerInfo").find("select").change();
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
}


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
	$("#ptaxType,#otaxType").change();
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
	//getSexAndBirth();
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
		$("select[name=sex]").val(obj[0].sex);
		$("select[name=sex]").change();
		$("input[name=birthDate]").val(obj[0].birthDate);
		$("select[name=birth_nation]").val(obj[0].taxBirthNation);
		$("select[name=birth_nation]").change();
		$("select[name=birth_region]").val(obj[0].taxBirthRegion);
		$("select[name=birth_region]").change();
		$("input[name=birth_address]").val(obj[0].taxBirthAddress);
		$("select[name=reside_nation]").val(obj[0].taxResideNation);
		$("select[name=reside_nation]").change();
		$("select[name=reside_region]").val(obj[0].taxResideRegion);
		$("select[name=reside_region]").change();
		$("input[name=reside_address]").val(obj[0].taxResideAddress);
		$("input[name=reside_address_english]").val(obj[0].taxResideAddressEnglish);
		$("select[name=taxNationality]").val(obj[0].taxNationality);
		$("select[name=taxNationality]").change();
		$("select[name=taxArea]").val(obj[0].taxArea);
		$("select[name=taxArea]").change();
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
}

/*************************************20180412新增*********************************************/
/**
 * 税收居民身份类型选择
 */
function taxTypeChange(element) {
	var value = element.value;
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

//-----------------------------------------------取修改信息--------------------------------------
function getPersonalInfo()
{
   var personalInfo = {};
   personalInfo.custtp = "1";
   personalInfo.permissionId = $("#permissionId").val();
   personalInfo.operatorId = $("#operatorId").val();
   personalInfo.trustTp = "1";  
   personalInfo.tano = $("#tano").val();
   personalInfo.oserialno = $("#serialno").val();
         
   personalInfo.pcustno = $("#pcustno").val();
   personalInfo.pdsapkind = $("#pdsapkind").val();
   personalInfo.pfundacco = $("#pfundacco").val();
   personalInfo.ptradeacco = $("#ptradeacco").val(); 
   personalInfo.invNm = trim($("#pinvnm").val());
   personalInfo.invIdtp = $("#pidtp").val();
   personalInfo.invIdno = trim($("#pidno").val());
   personalInfo.invIdValidate = $("#pidvalidate").val(); 
   personalInfo.invprtp = $("#pinvprtp").val(); 
           
   
   personalInfo.bankno = $("#pbnkNO").val();
   personalInfo.openname = trim($("#popenName").val()); 
   personalInfo.bankacconm = trim($("#pbankaccoNm").val());
   personalInfo.bankacco = trim($("#pbankAcco").val());
   personalInfo.openlocation = $("#popenAddr").val();
   personalInfo.openbankcity = $("#popenbankcity").val();

   personalInfo.psex = $("#psex").val();
   personalInfo.pnationalitycode = $("#pnationalitycode").val(); 
   personalInfo.pedlevel = $("#pedlevel").val();
   personalInfo.pvoccode = $("#pvoccode").val();
   personalInfo.pincome = $("#pincome").val();
   personalInfo.pcustrisklevl = $("#pcustrisklevl").val();
   
   //原风险等级及专业类型
   personalInfo.preInvprtp = $("#preInvprtp").val();
   personalInfo.preRisklevel = $("#preRisklevel").val();
   
   //特殊用户风险等级
   personalInfo.specriskLevel = "0";
   if($("#indSpecRiskLevel").is(':checked')){
 	  personalInfo.pcustrisklevl = "1";
 	  personalInfo.specriskLevel = "1";
   }
   
   personalInfo.ptel = trim($("#ptel").val()); 
   personalInfo.phousetel = trim($("#phousetel").val());
   personalInfo.pmobile = trim($("#pmobile").val());
   personalInfo.pfax = trim($("#pfax").val());                                         
   personalInfo.pfaxdelegate = $("#pfaxdelegate").val();
   personalInfo.pemail = trim($("#pemail").val());
   personalInfo.postcode = trim($("#ppostcode").val());
   personalInfo.addr = trim($("#paddr").val());
   personalInfo.shsecacc = trim($("#pslShsecacc").val());
   personalInfo.szsecacc = trim($("#pslSzsecacc").val());
                                        
   personalInfo.businesstp = $("#pbusinesstp").val();
   personalInfo.corptype = $("#pcompanytp").val(); 
   personalInfo.regioncode = $("#pregiontp").val();
   personalInfo.fxqtype = $("#pamlrisktype").val();
   personalInfo.fxqdesc = trim($("#pfxqremark").val());
   
   personalInfo.docbusinesstp = $("#docbusinesstp").val(); 
   personalInfo.isalldoc = $("#isalldoc").val();
   personalInfo.iforiginal = $("#isoriginal").val();
   personalInfo.ifsaved = $("#issaved").val();
   personalInfo.isupload = $("#isupload").val();
   personalInfo.remarkinfo = trim($("#remarkinfo").val());
   
   personalInfo.isscan=$("#isscan").val();
   personalInfo.keepaddress=	$("#keepaddress").val();
   personalInfo.salesaccmanager= $("#salesaccmanager").val();  	  
	  //投资者适当性 信息 收集
	  personalInfo.taxType = $("#ptaxType").val();
	  personalInfo.taxTypeDecl = "";
	  
	  if(!!personalInfo.taxType && "1" != personalInfo.taxType){
	  	personalInfo.taxTypeDecl = $("#ptaxTypeDecl").val();
	  }
   personalInfo.threeAnnualIncome = "";
   personalInfo.financialAsset = "";
   personalInfo.indInvExperience = "";
   personalInfo.relatedWorkExp = "";
   personalInfo.finProfessions = "";
   //专业投资者 才需收集信息
	  if(!!personalInfo.invprtp && "0" == personalInfo.invprtp){
		  personalInfo.pcustrisklevl = "1";
		  personalInfo.pincome = "0";
		  personalInfo.specriskLevel = "0";
		  personalInfo.threeAnnualIncome = $("#threeAnnualIncome").val();
	      personalInfo.financialAsset = $("#financialAsset").val();
	      personalInfo.indInvExperience = $("#indInvExperience").val();
	      personalInfo.relatedWorkExp = $("#relatedWorkExp").val();
	      personalInfo.finProfessions = $("#finProfessions").val();
	  }
	  
   //资料信息 
   var document="";
   $("input[name='doccheck']").each(function(data)
   {
      if($(this).attr("checked"))
      {
         document +=$(this).attr("id")+"-"+"1"+",";
      }
      else
      {
         document +=$(this).attr("id")+"-"+"0"+",";
      }         
   });
   personalInfo.document = document;
   
   return personalInfo;
   

}   

function getOrgInfo()
{
   var orgInfo = {};   
   
   orgInfo.custtp = "0";
   orgInfo.permissionId = $("#permissionId").val();
   orgInfo.operatorId = $("#operatorId").val();
   orgInfo.trustTp = "1";  
   orgInfo.tano = $("#tano").val();
   orgInfo.oserialno = $("#serialno").val();    
              
   orgInfo.invNm = trim($("#oinvnm").val()); 
   orgInfo.invIdtp = $("#oidtp").val(); 
   orgInfo.invIdno = trim($("#oidno").val());
   orgInfo.invIdValidate = $("#oidvalidate").val();  
   
   orgInfo.bankno = $("#obnkNO").val();  
   orgInfo.openname = trim($("#oopenName").val()); 
   orgInfo.bankacconm = trim($("#obankaccoNm").val());
   orgInfo.bankacco = trim($("#obankAcco").val());
   orgInfo.openlocation = $("#oopenAddr").val();
   orgInfo.openbankcity = $("#oopenbankcity").val();
   
   orgInfo.tel = trim($("#oofficeTel").val());
   orgInfo.fax = trim($("#ofax").val());
   orgInfo.postcode = trim($("#opostcode").val());
   orgInfo.addr = trim($("#oaddr").val());
   orgInfo.shsecacc = trim($("#orgShsecacc").val());
   orgInfo.szsecacc = trim($("#orgSzsecacc").val());
   
   orgInfo.docbusinesstp = $("#docbusinesstp").val(); 
   orgInfo.isalldoc = $("#isalldoc").val();
   orgInfo.iforiginal = $("#isoriginal").val();
   orgInfo.ifsaved = $("#issaved").val();
   orgInfo.isupload = $("#isupload").val();
   orgInfo.remarkinfo = trim($("#remarkinfo").val());
   
   orgInfo.isscan=$("#isscan").val();
	  orgInfo.keepaddress=	$("#keepaddress").val();
	  orgInfo.salesaccmanager= $("#salesaccmanager").val();  
   //资料信息 
   var document="";
   $("input[name='doccheck']").each(function(data)
   {
      if($(this).attr("checked"))
      {
         document +=$(this).attr("id")+"-"+"1"+",";
      }
      else
      {
         document +=$(this).attr("id")+"-"+"0"+",";
      }         
   });
   orgInfo.document = document;
   
   orgInfo.custsimpnm = $("#custabbrcode").val();
   orgInfo.instrepcode = $("#custinstreprcode").val();                                         
   orgInfo.businesstp = $("#businesstp").val();
   orgInfo.corptype = $("#companytp").val(); 
   orgInfo.regioncode = $("#regiontp").val();
   orgInfo.fxqtype = $("#amlrisktype").val();
   orgInfo.fxqdesc = trim($("#fxqremark").val());                                         
    
   orgInfo.orgInstrepnm = trim($("#oinstrepnm").val());  
   orgInfo.orgInstrepnation = $("#oinstrepnation").val(); 
   orgInfo.orgInstrepidtp = $("#oinstrepidtp").val();
   orgInfo.orgInstrepidno = trim($("#oinstrepidno").val());
   orgInfo.orgInstrepidvalidate = $("#oinstrepvalidate").val();    
   orgInfo.invprtp = $("#oinvprtp").val();    
   
   orgInfo.orgPrincipalname = trim($("#oprincipalname").val());  
   orgInfo.orgPrincipalnation = $("#oprincipalnation").val(); 
   orgInfo.orgPrincipalidtp = $("#oprincipalidtp").val();
   orgInfo.orgPrincipalidno = trim($("#oprincipalidno").val());
   orgInfo.orgPrincipalidvalidate = $("#oprincipalvalidt").val();    
   
   orgInfo.orgContactright = $("#ocontactgrant").val();  
   orgInfo.orgContnm = trim($("#ocontact").val()); 
   orgInfo.orgContnation = $("#ocontactnation").val();
   orgInfo.orgContidtp = $("#ocontidtp").val();
   orgInfo.orgContidno = trim($("#ocontidno").val());  
   orgInfo.orgContidvalidate = $("#ocontvalidate").val();  
   orgInfo.orgContphone = trim($("#ocontphone").val()); 
   orgInfo.orgContfax = trim($("#ocontfax").val());
   orgInfo.orgContmobile = trim($("#ocontmobile").val());
   orgInfo.orgContemail = trim($("#ocontemail").val());  
   orgInfo.orgContAddr = trim($("#orgContAddr").val());     
   orgInfo.orgContPostcode = trim($("#orgContPostcode").val()); 
   
   orgInfo.orgHoldingname = trim($("#oholdingname").val()); 
   orgInfo.orgHoldingidtp = $("#oholdingidtp").val();
   orgInfo.orgHoldingidno = trim($("#oholdingidno").val());
   orgInfo.orgHoldingidvalidate = $("#oholdingvalidate").val();  
   orgInfo.orgBeneficiarynm = trim($("#obeneficiary").val());
   orgInfo.orgRiskLevel = $("#ocustrisklevl").val();   
   
   //原风险等级及专业类型
   orgInfo.preInvprtp = $("#preInvprtp").val();
   orgInfo.preRisklevel = $("#preRisklevel").val();
 	//特殊用户风险等级
   orgInfo.specriskLevel = "0";
   if($("#instSpecRiskLevel").is(':checked')){
 	  orgInfo.orgRiskLevel = "1";
 	  orgInfo.specriskLevel = "1";
   }
   
   orgInfo.taxType = $("#otaxType").val();
   orgInfo.taxTypeDecl = "";
   if(!!orgInfo.taxType && "1" != orgInfo.taxType){
   	orgInfo.taxTypeDecl = $("#otaxTypeDecl").val();
	  }
   orgInfo.investProInstType = "";
   orgInfo.investProInstSecond = "";
   orgInfo.oneYearEndNetAsset = "";
   orgInfo.oneYearEndFinAsset = "";
   orgInfo.investExperience = "";
   orgInfo.negativeNotFinaInst = "";
   orgInfo.controlPerTaxDecl = "";
	  if(!!orgInfo.invprtp && "0" == orgInfo.invprtp){
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
		   }
	  }

	//普通投资者 需要收集 消极非金融机构
   /*if(!!orgInfo.invprtp && "1" == orgInfo.invprtp){*/
	   orgInfo.negativeNotFinaInst = $("#negativeNotFinaInst").val();
 	   orgInfo.controlPerTaxDecl = $("#controlPerTaxDecl").val();
   /*}*/
   
   //其他证件信息
   var ooidtpStr = "";
   var ooidnoStr = "";  
   var ooidvalidateStr = ""; 
   $(".orgInvIdtpTd select[id='ooidtp']").each(function(data){
      if($(this).val()=="" || $(this).val() == null)
      {
         ooidtpStr += "$,";
      }
      else
      {      
         ooidtpStr += $(this).val()+",";
      }
   });
   $("input[id='ooidno']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         ooidnoStr += "$,";
      }
      else
      {      
         ooidnoStr += $(this).val()+",";
      }
   });
   //由于增加了长期有效功能，原使用ID取值有误，现改为name取值
   $("input[name='ooidvalidate']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         ooidvalidateStr += "$,";
      }
      else
      {      
         ooidvalidateStr += $(this).val()+",";
      }
   });
   orgInfo.oidtpStr = ooidtpStr;
   orgInfo.oidnoStr = ooidnoStr;
   orgInfo.oidvalidateStr = ooidvalidateStr; 
   
   //其他经办人信息 
   var oocontactgrant = "";
   var oocontact = "";
   var oocontactnation = "";
   var oocontidtp = "";
   var oocontidno = "";
   var oocontvalidate = "";
   var oocontphone = "";
   var oocontfax = "";
   var oocontmobile = "";
   var oocontemail = "";
   var oOrgContactAddr = "";
   var oOrgContactPostcode = "";

   $("#ocontInfo select[id='oocontactgrant']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oocontactgrant += "$,";
      }
      else
      {
         oocontactgrant += $(this).val()+",";
      }         
   });
   $("#ocontInfo input[id='oocontact']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oocontact += "$,";
      }
      else
      {
         oocontact += $(this).val()+",";
      }         
   });
   $("#ocontInfo select[id='oocontactnation']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oocontactnation += "$,";
      }
      else
      {      
         oocontactnation += $(this).val()+",";
      }
   });
   $("#ocontInfo select[id='oocontidtp']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oocontidtp += "$,";
      }   
      else
      {   
         oocontidtp += $(this).val()+",";
      }
   });        
   $("#ocontInfo input[id='oocontidno']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oocontidno += "$,";
      }  
      else
      {    
         oocontidno += $(this).val()+",";
      }
   });
	  //由于增加了长期有效功能，原使用ID取值有误，现改为name取值
   $("#ocontInfo input[name='oocontvalidate']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oocontvalidate += "$,";
      } 
      else
      {       
         oocontvalidate += $(this).val()+",";
      }
   });
   
   $("#ocontInfo input[id='oocontphone']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oocontphone += "$,";
      } 
      else
      {      
         oocontphone += $(this).val()+",";
      }
   });
   $("#ocontInfo input[id='oocontfax']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oocontfax += "$,";
      }
      else
      {      
         oocontfax += $(this).val()+",";
      }
   });
   $("#ocontInfo input[id='oocontmobile']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oocontmobile += "$,";
      } 
      else
      {     
         oocontmobile += $(this).val()+",";
      }
   }); 
   $("#ocontInfo input[id='oocontemail']").each(function(data)
   {
      if($(this).val()=="" || $(this).val() == null)
      {
         oocontemail += "$,";
      } 
      else
      {      
         oocontemail += $(this).val()+",";
      }
   });
   $("#ocontInfo input[id='oocontAddr']").each(function(data)
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
   $("#ocontInfo input[id='oocontPostcode']").each(function(data)
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
   orgInfo.oOrgContactright = oocontactgrant; 
   orgInfo.oOrgContact = oocontact; 
   orgInfo.oOrgContactnation = oocontactnation; 
   orgInfo.oOrgContactidtp = oocontidtp; 
   orgInfo.oOrgContactidno = oocontidno; 
   orgInfo.oOrgContactidvalidate = oocontvalidate; 
   orgInfo.oOrgContacttel = oocontphone; 
   orgInfo.oOrgContactfax = oocontfax; 
   orgInfo.oOrgContactmobile = oocontmobile; 
   orgInfo.oOrgContactemail = oocontemail;
   orgInfo.oOrgContactAddr = oOrgContactAddr;
   orgInfo.oOrgContactPostcode = oOrgContactPostcode;   
   //机构类型
   orgInfo.orgType = $("select[name='organType']").val();
   return orgInfo;   
}


/**
 * 添加税收元素
 */
var addT=0;
function addResident(obj) {
	var html = '<tr class="since addResidentEle add'+addT+'">'
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
			+ '<tr class="since addResidentEle add'+addT+'">'
			+ '<td><font color="red">*</font>纳税人识别号：</td>'
			+ '<td class="white-bg" colspan="4"><div class="col-sm-11 form-inner">'
			+ '<input type="text" name="taxpayerCode" class="form-control" style="float: left;">'
			+ '<label style="line-height: 34px;">'
			+ '<input name="isTaxpayerEvent" class="i-checks isTaxpayerEvent" type="checkbox" onclick="isTaxpayer(this)">无<i class="residentEach"></i>'
			+ '</label></div></td></tr>'
			+ '<tr class="since sinceNotCodeCause addResidentEle add'+addT+'" style="display:none">'
			+ '<td><font color="red">*</font>无识别号的原因：</td>'
			+ '<td class="white-bg" colspan="4"><div class="col-sm-11 form-inner">'
			+ '<select class="form-control select2 notCodeCause" name="notCodeCause" onchange="notCodeCauseEvent(this)">'
			+ '<option value="">--</option>'
			+ '<option value="1">居民国（地区）不发放纳税人识别号</option>'
			+ '<option value="2">账号持有人未能取得纳税人识别号</option>'
			+ '</select>'
			+ '</div></td></tr>'
			+ '<tr class="since addResidentEle add'+addT+'" style="display:none">'
			+ '<td><font color="red">*</font>未取得原因：</td>'
			+ '<td class="white-bg" colspan="4"><div class="col-sm-11 form-inner">'
			+ '<input type="text" class="form-control" name="notGetCause"></div></td></tr>';
	$("#residentType").append(html);
	
	$('.notCodeCause').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	addT++;
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
	var selOpenType = $("#selOpenType").val();
	/* if (taxType != "1" && !!taxType) { */
	if ($("#residentType").is(":visible")) {
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
			if ($("#controllerInfo").is(":visible")) {
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
	//根据不同的税收居民身份
	$("select[name=taxNationality]").each(function(i){
		var taxNationality = $("select[name=taxNationality]").eq(i).val();
		var taxArea = $("select[name=taxArea]").eq(i).val();
		if ($("select[name=taxNationality]").eq(i).is(":visible") && $("select[name=taxArea]").eq(i).is(":visible")) {
			if (taxNationality == "" || taxArea == "") {
				flag = false;
				return flag;
			} else {
				$(".isTaxpayerEvent").each(function(i) {
					if ($(".isTaxpayerEvent").eq(i).is(":visible")) {
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
	if ($("#residentType").is(":visible")) {
		$(".residentEach").each(function(i) {
			if ($(".isTaxpayerEvent").eq(i).is(":visible")) {
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
		ctools.alert("请填写控制人出生城市！","","error");
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

function customReset() {
	$("#ptaxType").change();
	$("#otaxType").change();
 	$("#pinvprtp").change();
 	$("#oinvprtp").change();
	$("#investProInstType").change();
	$("#negativeNotFinaInst").change();
	$('.select2_width').select2({
		allowClear : true,
		minimumResultsForSearch : Infinity
	});
	
	$('#negativeNotFinaInst').val("1");
	$('#negativeNotFinaInst').select2("destroy");
	$('#negativeNotFinaInst').select2({
		allowClear : true,
		minimumResultsForSearch : Infinity
	}).trigger('change');
}


function validateinfo(baseInfo) {
	var nowdate = nowDate;
	if (baseInfo.custtp == 1) {
		if (apkind != '0B1') {
			if (baseInfo.invNm == "") {
				ctools.alert("请输入投资者名称！","","error");
				$("#pinvnm").focus();
				return false;
			}
			if (baseInfo.invIdno == "") {
				ctools.alert("请输入注册登记证件号码！","","error");
				$("#pidno").focus();
				return false;
			}
			if (baseInfo.invIdValidate == ''
					|| baseInfo.invIdValidate <= nowdate) {
				ctools.alert("注册登记证件有效期为空或已过期！","","error");
				return false;
			}
			if (baseInfo.ptel == '' && baseInfo.phousetel == ''
					&& baseInfo.pmobile == '') {
				ctools.alert("办公电话、住宅电话、移动电话至少要填一项！","","error");
				return false;
			}
			if (baseInfo.addr == "") {
				ctools.alert("请输入通讯地址！","","error");
				$("#paddr").focus();
				return false;
			}
			if (trim(baseInfo.postcode) == "") {
				ctools.alert("请输入邮政编码！","","error");
				$("#ppostcode").focus();
				return false;
			}
		}
		if (apkind != '003') {
			if (baseInfo.openname == "") {
				ctools.alert("请输入预留银行全称！","","error");
				$("#popenName").focus();
				return false;
			}
			if (baseInfo.openlocation == "") {
				ctools.alert("请选择预留银行开户地省！","","error");
				$("#popenAddr").focus();
				return false;
			}
			if (baseInfo.openbankcity == "") {
				ctools.alert("请选择预留银行开户地市！","","error");
				$("#popenbankcity").focus();
				return false;
			}
			if (baseInfo.bankacconm == "") {
				ctools.alert("请输入预留银行户名！","","error");
				$("#pbankaccoNm").focus();
				return false;
			}
			if (baseInfo.bankacco == "") {
				ctools.alert("请输入预留银行账号！","","error");
				$("#pbankAcco").focus();
				return false;
			}
		}
	} else {
		if (apkind != '0B1') {
			if (baseInfo.invNm == "") {
				ctools.alert("请输入投资者名称！","","error");
				$("#oinvnm").focus();
				return false;
			}
			if (baseInfo.invIdno == "") {
				ctools.alert("请输入注册登记证件号码！","","error");
				$("#oidno").focus();
				return false;
			}
			if (baseInfo.invIdValidate == ''
					|| baseInfo.invIdValidate <= nowdate) {
				ctools.alert("注册登记证件有效期为空或已过期！","","error");
				return false;
			}
			if (baseInfo.orgInstrepnm == "") {
				ctools.alert("请输入法定代表人姓名！","","error");
				$("#oinstrepnm").focus();
				return false;
			}
			if (baseInfo.orgInstrepidno == "") {
				ctools.alert("请输入法定代表人证件号码！","","error");
				$("#oinstrepidno").focus();
				return false;
			}
			if (baseInfo.orgInstrepidvalidate == ''
					|| baseInfo.orgInstrepidvalidate <= nowdate) {
				ctools.alert("法定代表人证件有效期为空或已过期！","","error");
				return false;
			}
			if (baseInfo.orgPrincipalname == "") {
				ctools.alert("请输入机构负责人姓名！","","error");
				$("#oprincipalname").focus();
				return false;
			}
			if (baseInfo.orgPrincipalidno == "") {
				ctools.alert("请输入机构负责人证件号码！","","error");
				$("#oprincipalidno").focus();
				return false;
			}
			if (baseInfo.orgPrincipalidvalidate == ''
					|| baseInfo.orgPrincipalidvalidate <= nowdate) {
				ctools.alert("机构负责人证件有效期为空或已过期！","","error");
				return false;
			}
			if (baseInfo.orgContnm == "") {
				ctools.alert("请输入经办人姓名！","","error");
				$("#ocontact").focus();
				return false;
			}
			if (baseInfo.orgContidno == "") {
				ctools.alert("请输入经办人证件号码！","","error");
				$("#ocontidno").focus();
				return false;
			}
			if (baseInfo.orgContidvalidate == ''
					|| baseInfo.orgContidvalidate <= nowdate) {
				ctools.alert("经办人证件有效期为空或已过期！","","error");
				return false;
			}
			if (baseInfo.orgContphone == "") {
				ctools.alert("请输入经办人办公电话！","","error");
				$("#ocontphone").focus();
				return false;
			}
			if (baseInfo.orgContfax == "") {
				ctools.alert("请输入经办人传真号码！","","error");
				$("#ocontfax").focus();
				return false;
			}
			if (baseInfo.addr == "") {
				ctools.alert("请输入办公地址！","","error");
				$("#oaddr").focus();
				return false;
			}
			if (trim(baseInfo.postcode) == "") {
				ctools.alert("请输入邮政编码！","","error");
				$("#opostcode").focus();
				return false;
			}
			if (baseInfo.orgHoldingidvalidate != ''
					&& baseInfo.orgHoldingidvalidate <= nowdate) {
				ctools.alert("控股股东证件已过期！","","error");
				return false;
			}
		}
		if (apkind != '003') {
			if (baseInfo.openname == "") {
				ctools.alert("请输入预留银行全称！","","error");
				$("#oopenName").focus();
				return false;
			}
			if (baseInfo.openlocation == "") {
				ctools.alert("请选择预留银行开户地省！","","error");
				$("#oopenAddr").focus();
				return false;
			}
			if (baseInfo.openbankcity == "") {
				ctools.alert("请选择预留银行开户地市！","","error");
				$("#oopenbankcity").focus();
				return false;
			}
			if (baseInfo.bankacconm == "") {
				ctools.alert("请输入预留银行户名！","","error");
				$("#obankaccoNm").focus();
				return false;
			}
			if (baseInfo.bankacco == "") {
				ctools.alert("请输入预留银行账号！","","error");
				$("#obankAcco").focus();
				return false;
			}
		}
	}
	return true;
};
function trim(str){ //删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
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
}