var myScroll;

$(document).ready(function(e){
	/*修改公共页头名称*/
	$(".header .top-a h2").html("产品合同");
	document.title="产品合同";
	myScroll = new IScroll('#wrapper', {
		scrollbars : true,
		mouseWheel : true,
		interactiveScrollbars : true,
		shrinkScrollbars : 'scale',
		fadeScrollbars : true
	});
	
	var fundId = getUrlParameter('templet');
	var period = getUrlParameter('period');
  	var money = getUrlParameter('money');
	
  	if(isNaN(money)){
  		errorRemark("购买金额格式有误，请返回购买页修改");
  		return;
  	}
  	
  	var temp = toUpperCase(numDiv(money,10000));
  	var upperCaseMoney = "";
  	if(temp=="errorMoney" || temp=="moneyMax"){
  		errorRemark("购买金额格式有误，请返回购买页修改");
  		return;
  	}else{
  		upperCaseMoney = temp.replace("元整","");
  	}
  	
  	queryFundContractById(fundId,money/10000,upperCaseMoney,period);
  	
  	myScroll.refresh();
  	getUserRequest("fund-contract");/* 此处subPath为页面内行为 */
});

document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

function queryFundContractById(fundId,money,upperMoney,period){
	$.ajax({
		async:false,
		url : "/WeixinService/business/queryFundContractDetail.xhtml",
		data : {
			"fundId":fundId,
			"status":"Y",
			"period":period
		},
		dataType : "json",
		cache : false,
		type:"POST",
		error : function(textStatus,errorThrown){
			errorRemark("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			if(data.returnCode=='0000'){
				if(data.fundContractDto!=null){
					
					var dd2 = data.fundContractDto.templateCont2;
					dd2 = dd2.replace("#amount#",money);
					dd2 = dd2.replace("#capitalAmount#",upperMoney);
					
					$("#dd1").html(data.fundContractDto.templateCont1);
					$("#title2").html(data.fundContractDto.templateTitle2);
					$("#dd2").html(dd2);
					$("#title3").html(data.fundContractDto.templateTitle3);
					$("#dd3").html(data.fundContractDto.templateCont3);
					$("#title4").html(data.fundContractDto.templateTitle4);
					$("#dd4").html(data.fundContractDto.templateCont4);
				}else{
					errorRemark("合同加载失败，请稍后再试");  
					toBack();
				}
			}else{
				errorRemark("合同加载失败，请稍后再试");  
				toBack();
			}
		}
	});
}
function showAndHide(_id){
    $("#"+_id).toggle();
    $(".xy_content dd").not("#"+_id).hide();
    toTop();
    myScroll.refresh();
}
function toTop(){
	/*回到顶部   （即将滚动条的位置设置为0px,0px）*/
	myScroll.scrollTo(0,0,0);
	myScroll.refresh();
	return true;
}