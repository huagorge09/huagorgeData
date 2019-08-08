/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	$('.select2').select2();
});

/**
 * 提交修改数据
 */
function doSubmit(modifytype){
	var permissionId = $("input[name=permissionId]").val();
	var custno = $("input[name=custno]").val();
	var tradeacco = $("input[name=tradeacco]").val();
	var serialno = $("input[name=serialno]").val();
	var modifytype = modifytype;
	var fundid = $("select[name=fundid]").val();
	var subamt = $("input[name=subamt]").val();
	if(!!subamt){
		subamt = subamt.replace(/,/ig,"");
	}
	var subquty = $("input[name=subquty]").val();
	if(!!subquty){
		subquty = subquty.replace(/,/ig,"");
	}
	var ofundid = $("select[name=ofundid]").val();
	var largeflag = $("select[name=largeflag]").val();
	var melonmd = $("select[name=melonmd]").val();
	var melonpercent = $("input[name=melonpercent]").val();
	var oldserialno = $("input[name=oldserialno]").val();
	var onetpoint = $("input[name=onetpoint]").val();
	var oseatno = $("select[name=oseatno]").val();
	var dsapkind = $("input[name=dsapkind]").val();
	var operatorId = $("input[name=operatorId]").val();
	
	ctools.confirm({text:"是否确认提交该笔申请？"},function(isConfirm){
		if(isConfirm){
			$.ajax({
				type : "POST",
				async: false, 
				dataType : "json",
				url : BASE_PATH +'capitalService/server/tradeModify.xhtml',
				data:{
					"permissionId":permissionId,"custno":custno,
					"tradeacco":tradeacco,"serialno":serialno,
					"modifytype":modifytype,"fundid":fundid,
					"subamt":subamt,"subquty":subquty,
					"ofundid":ofundid,"largeflag":largeflag,
					"melonmd":melonmd,"melonpercent":melonpercent,
					"oldserialno":oldserialno,"onetpoint":onetpoint,
					"oseatno":oseatno,"dsapkind":dsapkind,"operatorId":operatorId
				},
				success : function(data) {
					if(data.errCode == "0000"){
						ctools.alert_sweet('提交成功！', "success", "申请编号："+data.serialno , function(){
							window.close();
							var type = typeof(window.opener.queryByCondition);
							if("function" == type){
								window.opener.queryByCondition(false);
							}
						});
					}else{
						ctools.alert_sweet('提交失败！', "error", "申请编号："+data.serialno+"\n失败原因："+data.errMsg);
					}
				}
			});
		}
	});
}

function onMoneyChange(event){
	var subAmt = event.value;
	var subAmtFmt = qianfenwei(subAmt);
	$("#CapMoneyqianfenwei").html(subAmtFmt);
	$("#CapMoney").html(capMoneyNoCheck(event.value,"subAmt"));
}

function qianfenwei(objvalue){
	if(objvalue == ""){
		return "";
    }
	objvalue = objvalue.replace(/,/ig,"");
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
	xxje = xxje.replace(/,/ig,"");
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