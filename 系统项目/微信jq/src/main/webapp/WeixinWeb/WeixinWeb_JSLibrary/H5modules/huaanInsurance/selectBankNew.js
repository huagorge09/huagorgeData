$(function(){
	initBankList()
	operation()
	bankWatch()
	locationPage()
})
/**
 * 查询银行卡列表
 */
function initBankList(){
    var toast=$(document).dialog({
        type : 'toast',
        infoIcon: '/WeixinWeb/WeixinWeb_Images/H5modules/huaanInsurance/loading.gif',
        infoText: '数据读取中'
    });
	$.ajax({
		async:false,
		url : "/WeixinService/setUp/queryBankLogoList.xhtml ",
		data : {},
		dataType : "json",
		type:"POST",
		success : function(data){
			if(data.resultCode=="0000"){
				toast.close()
				var html="";
				var list=data.data;
				for(var i=0;i<list.length;i++){
					html+='<li><img src='+list[i].bankLogoCode+'>'+list[i].bankName+'</li>'
				}
				$(".bankList ul").append(html)
			}else{
				toast.close()
				$(document).dialog({type : 'notice',infoText: data.resultMsg,autoClose: 1500,position: 'center'});
			}
		},
		error:function(){
			toast.close()
			$(document).dialog({type : 'notice',infoText: '服务器异常，请稍后再试',autoClose: 1500,position: 'center'});
		}
	});
}
/**
 * 按钮操作事件
 */
function operation(){
	$("#close").click(function(){
		$("input[name=bank]").val("");
		$(this).hide()
		$(".bankList li").show();
		$(".noSerach").hide()
	})
	$("#cancel,.mask").click(function(){
		$("input[name=bank]").removeClass("focus").val("");
		$(this).hide()
		$(".mask,#close").hide()
		$(".bankList li").show()
		$(".noSerach").hide()
	})
	$(".mask").click(function(){
		$("input[name=bank]").removeClass("focus").val("");
		$(this).hide()
		$("#cancel").hide()
		$(".bankList li").show()
		$(".noSerach").hide()
	})
}
/**
 * 输入框监听
 */
function bankWatch(){
	$("input[name=bank]").on("focus",function(){
		$(this).addClass("focus");
		if($(this).val()==""){
			$(".mask").show()
		}
		$(".serach span").show()
	})
	$("input[name=bank]").on("input",function(){
		var bank=$(this).val();
		var bankList=$(".bankList li")
		var i=0;
		if($(this).val()!=""){
			bankList.each(function(){
				var text=$(this).text();
				if(text.indexOf(bank)>-1){
					$(this).show();
					i++;
				}else{
					$(this).hide();
				}
			})
			if(i==0){
				$(".noSerach").show()
			}else{
				$(".noSerach").hide()
			}
			$("#close").show()
			$(".mask").hide()
		}else{
			$("#close").hide()
			$(".mask").show()
			$(".bankList li").show()
			$(".noSerach").hide()
		}
	})
}

function locationPage(){
	$(".bankList li").on("click",function(){
		localStorage.setItem("bankName",$(this).text())
		location.href="/WeixinService/H5modules/huaanInsurance/realName.html"
	})
}
