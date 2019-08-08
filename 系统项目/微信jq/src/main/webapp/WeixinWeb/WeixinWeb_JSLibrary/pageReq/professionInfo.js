﻿$(document).ready(function(e) {
	queryUserinfo();
	$(".header .top-a h2").html("选择职业");
	document.title="选择职业";
	var proFesseData = queryParamList("SYSTEM","VOCCODE","");
	var proFesseHtml = "";
	for (var i = 0; i < proFesseData.length; i++) {
		var temp = proFesseData[i];
		proFesseHtml+='<a class="href-point" data-value="'+temp.pmco+'" data-text="'+temp.pmnm+'">'+temp.pmnm+'<i id="area"></i></a>';
	}
	$("#section_01 .center-accountset").html(proFesseHtml);
	$("#section_01 a.href-point").click(function(){
		var val = $(this).data("value");
		$("#voccode").val(val);
		if(val == "15"){
			showPop();
			return;
		}
		updateCmfUserBaseInfo();
	})
});

function queryParamList(paramType,paramKey,pmValueOne){
    var data = null;
    $.ajax({
        async: !1,
        url: "/WeixinService/business/queryParamList.xhtml",
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

function updateCmfUserBaseInfo(){
	var voccode = $("#voccode").val();
	var otherVocation = $.trim($("#otherVocation").val());
	// if(!ischinese( otherVocation)){
	// 	errorRemark('请输入正确的职业')
	// 	return;
	// }
	if(voccode == "15" && otherVocation == ""){
		errorRemark("请填写您的职业");
		return;
	}
	if(voccode != "15"){
		otherVocation = "";
	}
	$.ajax({
        async: !1,
        url: "/WeixinService/business/updateCmfUserBaseInfo.xhtml",
        data: {
        	nation : "",
        	province : "",
        	city : "",
        	addr : "",
        	voccode : voccode,
        	birthDate : "",
        	taxResidentType : "",
        	otherVocation : otherVocation
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
            	errorRemark("选择职业失败");
            }
        }
    });
}

function queryUserinfo() {
	$.ajax({
		async: false,
		url: "/WeixinService/business/queryUserinfo.xhtml",
		data: "",
		dataType: "json",
		cache: false,
		type: "post",
		error: function (textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success: function (data) {
			if (data.returnCode != null && data.returnCode == "0000") {
				$("#otherVocation").val(data.otherVocation);
			}
		}
	});
}

function showPop(){
	$("#recommendPop").show();
	$(".backLayer").show();
}

function closePop(){
	$("#recommendPop").hide();
	$(".backLayer").hide();
}
// 校验是否为中文
function ischinese(name){
    return /^[\u4e00-\u9fa5]+$/.test(name)
}  