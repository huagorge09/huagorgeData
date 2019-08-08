﻿function queryBirthDateInfo() {
	$("#titleBack").click(function(){
		$('#titleBack').attr("href","javascript:redirectUrl('/WeixinService/business/user/userInfo.shtml')");
	});
	$(".header .top-a h2").html("选择出生日期");
	var birthDate = GetQueryString("birthDate");
	$("#birthDate").val(birthDate);
	/*$("#birthDate").fdatepicker({
		format: 'yyyy-mm-dd'
	});*/
};

/**
 * 获取地址栏传递参数
 * @param name
 * @returns
 */
function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null){
         var val = decodeURI(r[2]);
    	 return  unescape(val);
     }else{
    	 return null;
     }
}

function saveBirthDate(){
	var birthDateText = $("#birthDate").val();
	if(birthDateText == ""){
		errorRemark("出生日期不能为空！");
		return;
	}
	birthDateText = formatDate(birthDateText);
	var flag = isDate(birthDateText);
	if(!flag){
		errorRemark("出生日期格式错误！");
		return;
	}
	
	//入库
	$.ajax({
        async: !1,
        url: "/WeixinService/business/updateCmfUserBaseInfo.xhtml",
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
        	errorRemark("网络繁忙，请稍后再试。");
        },
        success: function(n) {
        	if(n != null && n.resultCode =="0000"){
        		window.location.href="/WeixinService/business/user/userInfo.shtml";
            }else{
            	hideBirthDateDiv();
            	errorRemark("选择出生日期失败！");
            }
        }
    });
}

/**
 * 取消出生日期選擇事件
 * @returns
 */
function cacelBirthDateUpdate(){
	window.location.href="/WeixinService/business/user/userInfo.shtml";
}