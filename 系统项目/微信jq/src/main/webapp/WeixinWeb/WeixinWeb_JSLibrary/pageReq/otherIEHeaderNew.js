$(document).ready(function(e){
	var local = document.referrer;
	if(local=="" || local==null){
		$("#titleBack").removeClass("come-back");
		$("#titleBack").addClass("come-back01");
		$("#titleBack").attr("href","javascript:void(0)");
	}
});

function toBack(){
	var local = document.referrer;
	if(local=="" || local==null){
		redirectUrl('/WeixinService/indexNew.shtml');
	}else{
		history.go(-1);
	}
}


/* 页头的菜单列表显示与隐藏 */
function menuList(){
	$("#menuListDiv").toggle();
	if($("#menuListDiv").css("display")=="none"){
		$("#iconMenu").removeClass("active");
	}else{
		$("#iconMenu").addClass("active");
	}
	
	if("undefined"!= typeof myScroll ){
		myScroll.refresh();
	}
}

function toAhref(url,i){
	$("#menu1,#menu2,#menu3").removeClass("act");
	$("#menu"+i).addClass("act");
	redirectUrl(url);
}
dataStatistics()
function dataStatistics(){
	var url=location.href;
	if(url.indexOf("cmwachina")>-1){
		var _hmt = _hmt || [];
		(function() {
		  var hm = document.createElement("script");
		  hm.src = "https://hm.baidu.com/hm.js?afce23bbabb14d025b1571144242dded";
		  var s = document.getElementsByTagName("script")[0]; 
		  s.parentNode.insertBefore(hm, s);
		})();
	}
}
