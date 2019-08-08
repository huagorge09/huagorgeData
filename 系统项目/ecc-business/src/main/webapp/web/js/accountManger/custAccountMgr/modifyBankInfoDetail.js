var PRIMARY_PATH = "";
var ACCOUNT_PATH = "";
var PROJEC_TPATH = "";

function setPath(projectPath,path,accountPath){
	PROJEC_TPATH = projectPath;
	PRIMARY_PATH = path;
	ACCOUNT_PATH = accountPath;
}

var tradeaccos = $("#tradeaccos").val();
var custtp = $("#invtp").val();
var custArr = tradeaccos.split(",");
var custlength = custArr.length;

var oidlist = $("#oidlist").val();
oidlist = !!oidlist ? eval(oidlist) : "";
var ocontlist = $("#ocontactlist").val();
ocontlist = !!ocontlist ? eval(ocontlist) : "";
var doclist = $("#documentlist").val();
doclist = !!doclist ? eval(doclist) : "";


$(function(){
	doSelect($("#docbusinesstp").val());
	
	$('.select2_width').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	
	initData(oidlist, ocontlist);
	
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
	
	var specriskLevel = $("#specriskLevel").val();
	if ("1" == specriskLevel) {
		$("#indSpecRiskLevel").click();
		$("#specRiskLevel").click();
	}
	//queryFileNo();
	
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
	
	$("#openAddr").change();
});

function showTypeContent(id, tId) {
	$(".typeContent").hide();
	$("#" + id).show();
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
 trstr += $("#orgcontinfo>tr:eq(0)>td:eq(4)").clone(true).find("#ocontact").attr("id","oocontact").attr("value",contnm).end().html();
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
	var invtp = $("#invtp").val();
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


function doSubmit() {
	var qParam = {};
	qParam.tradeaccos = $("#tradeaccos").val();
	qParam.openName = trim($("#openName").val());
	if (qParam.openName == '') {
		ctools.alert("请输入开户银行！", "", "error");
		$("#openName").focus();
		return false;
	}
	qParam.bankAccoNm = $("#bankAccoNm").val();
	if (qParam.bankAccoNm == '') {
		ctools.alert("请输入银行户名！", "", "error");
		$("#bankAccoNm").focus();
		return false;
	}
	qParam.openAddr = $("#openAddr").val();
	if (qParam.openAddr == '--') {
		ctools.alert("请选择开户地省份！", "", "error");
		$("#openAddr").focus();
		return false;
	}
	qParam.openbankcity = $("#openbankcity").val();
	if (qParam.openbankcity == '') {
		ctools.alert("请选择开户地市！", "", "error");
		$("#openbankcity").focus();
		return false;
	}
	qParam.bnkNo = $("#bnkNo").val();
	qParam.bankAcco = $("#bankAcco").val();
	if (qParam.bankAcco == '') {
		ctools.alert("请输入银行账号！", "", "error");
		$("#bankAcco").focus();
		return false;
	}

	qParam.permissionId = $("#permissionId").val();
	qParam.operatorId = $("#operatorId").val();
	qParam.trustType = $("#trustType").val();

	// 资料信息
	var document = "";
	var docflag = false;
	$("input[name='doccheck']").each(function(data) {
		if ($(this).is(":checked")) {
			document += $(this).attr("id") + "-" + "1" + ",";

		} else {
			document += $(this).attr("id") + "-" + "0" + ",";
		}
		docflag = true;
	});
	if (!docflag) {// 如果未选中，传一个空值
		document = "";
	}

	qParam.docbusinesstp = $("#docbusinesstp").val();
	qParam.isalldoc = $("#isalldoc").val();
	qParam.iforiginal = $("#isoriginal").val();
	qParam.ifsaved = $("#issaved").val();
	qParam.isupload = $("#isupload").val();
	qParam.remarkinfo = trim($("#remarkinfo").val());
	qParam.document = document;
	qParam.isscan = $("#isscan").val();
	qParam.keepaddress = $("#keepaddress").val();
	qParam.salesaccmanager = $("#salesaccmanager").val();
	qParam.fileno = $("#fileno").val();

	$.ajax({
		url : ACCOUNT_PATH + "modifyBankInfo.do",
		dataType : "json",
		type : "POST",
		data : {
			"baseInfo" : JSON.stringify(qParam)
		},
		cache : false,
		async : false,
		success : function(data) {
			var errcode = data.errcode;
			var errMsg = data.errmsg;
			if (errcode == "0000") {
				ctools.alert_sweet('申请提交成功！', "success", "" ,
						function() {
							window.close();
						});
			} else {
				ctools.alert_sweet('申请提交失败！', "error", "失败原因：" + errMsg);
			}
		}
	});
}