$(document).ready(function(){
	debugger
	$(".nav.fr ul li a").removeClass("current");
	queryEcontrantByFundId();
	document.title = "电子合同_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
})
/*查询电子合同*/
function queryEcontrantByFundId(){

	var fundId = getUrlParameter("fundId");
	var period = getUrlParameter("period");
	var money = getUrlParameter("money");
	money = removeSpecialStr(money);
	$.ajax({
		async:false,
		url : "/AppService/setUp/queryCompanyUserFundEcontract.xhtml",
		data : {
			"fundId":fundId,
			"period":period
		},
		dataType : "json",
		cache : false,
		type:"POST",
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			var htmls = "";
			if(data.fundContractDto != null){
				var templateCont1 = data.fundContractDto.templateCont1;
				var templateCont2 = data.fundContractDto.templateCont2;
				var templateCont3 = data.fundContractDto.templateCont3;
				var templateCont4 = data.fundContractDto.templateCont4;
				
				if(templateCont1 == null){
					templateCont1 = "";
				}
				if(templateCont2 == null){
					templateCont2 = "";
				}
				if(templateCont3 == null){
					templateCont3 = "";
				}
				if(templateCont4 == null){
					templateCont4 = "";
				}
				
				var formatMoney = numDiv(money,10000);
				
				templateCont1 = templateCont1.replace(/#amount#/g,formatMoney);
				templateCont2 = templateCont2.replace(/#amount#/g,formatMoney);
				templateCont3 = templateCont3.replace(/#amount#/g,formatMoney);
				templateCont4 = templateCont4.replace(/#amount#/g,formatMoney);
				
				var temp = toUpperCase(formatMoney).replace(/元整/g,"");
				
				templateCont1 = templateCont1.replace(/#capitalAmount#/g,temp);
				templateCont2 = templateCont2.replace(/#capitalAmount#/g,temp);
				templateCont3 = templateCont3.replace(/#capitalAmount#/g,temp);
				templateCont4 = templateCont4.replace(/#capitalAmount#/g,temp);
				
				
				if(templateCont1 != null && templateCont1 != ""){
					htmls += "<div class='user_xybox'>";
					htmls += "<div class='user_xycenter'>";
					htmls += "<h2 class='xy_title'>"+data.fundContractDto.templateTitle1+"</h2>";
					htmls += templateCont1;
					htmls += "</div>";
					htmls += "</div>";
				}
				if(templateCont2 != null && templateCont2 != ""){
					htmls += "<div class='user_xybox'>";
					htmls += "<div class='user_xycenter'>";
					htmls += "<h2 class='xy_title'>"+data.fundContractDto.templateTitle2+"</h2>";
					htmls += templateCont2;
					htmls += "</div>";
					htmls += "</div>";
				}

				if(templateCont3 != null && templateCont3 != ""){
					htmls += "<div class='user_xybox'>";
					htmls += "<div class='user_xycenter'>";
					htmls += "<h2 class='xy_title'>"+data.fundContractDto.templateTitle3+"</h2>";
					htmls += templateCont3;
					htmls += "</div>";
					htmls += "</div>";
				}
				if(templateCont4 != null && templateCont4 != ""){
					htmls += "<div class='user_xybox'>";
					htmls += "<div class='user_xycenter'>";
					htmls += "<h2 class='xy_title'>"+data.fundContractDto.templateTitle4+"</h2>";
					htmls += templateCont4;
					htmls += "</div>";
					htmls += "</div>";
				}
			}
			if(htmls == null || htmls == ""){
				show_tips("没有查询到相关合同。");
				window.location.href = "/AppService/applicationGroups.jsp?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO";
			}
			$(".main_bg").append(htmls);
		}
	});
}
checkCompanyUser()
function checkCompanyUser(){
	$.ajax({
		async:false,
		url: "/AppService/setUp/checkCompanyUser.xhtml",
		dataType: "json",
		type:"POST",
		data: {},
		cache: false,
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function (data) {
			if(data.returnCode != null && data.returnCode == "0000"){
				$("#loginYesPublic").show();
				$("#login_userinfo_public").html(data.fundAccoEncry).prop("href","/company/companyAccountInfo.shtml");
				$(".My-center a").prop("href","/company/companyAccountInfo.shtml");
				$("#loginYes").hide();
				$("#loginNo").hide();
				$(".My-center").attr("href","/company/companyAccountInfo.shtml");
			}else{
				goToURL("/login/login.shtml");
			}
		}
	});
}
/* 退出登录 */
function checkOutLoginForCompany(){
	$.ajax({
		async : true,
		url : "/AppService/setUp/logoutForCompany.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			window.location.href = "/login/login.shtml";
		}
	});
};