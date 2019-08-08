$(document).ready(function(e) {
	activityStatisticalLog("1");
	var bodyHeight=$(window).height();
    $(".page,.swiper-wrapper,.swiper-slide").height(bodyHeight);
	var swiper = new Swiper('.swiper-container', {
	    pagination: '.swiper-pagination',
	    paginationClickable: true,
	    direction: 'vertical',
	    onSlideChangeEnd:function(swiper){
	        changePaged(swiper.activeIndex);
	    }
	});
});
function test(x){
    $("#loading").text(x);
}
$(window).resize(function(){
    var bodyHeight=$(window).height();
    $(".page,.swiper-wrapper,.swiper-slide").height(bodyHeight);
})
/*页面切换完成后触发事件 i：页面页码 ，从0开始*/
function changePaged(i){
    $(".page0"+i+" div").css({visibility: "hidden"});/*前一页动画元素隐藏*/
    $(".page").removeClass("active");
    i+=1;
    $(".page0"+i+"").addClass("active");
    $(".page0"+i+" div").css({ visibility: "visible"});
    $(".page0"+(i+1)+" div").css({visibility: "hidden"});/*后一页动画元素隐藏*/
    activityStatisticalLog(i);
}

/*领券*/
function draw(){
	/*判断是否领券->判断是否还有券->判断是否登录/注册(这里前两步判断后台接口有判断，暂时不做判断，只做是否登录判断)*/
	requestWaitDivShow();
	$.ajax({
		async : false,
		url : "/WeixinService/activity/fatherDayDrawTicket.xhtml",
        data: {},
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
        	errorRemark("网络繁忙，暂时无法领取！");
        }, 
        success : function (data){
        	if(data.returnCode=="0000"){
        		/*领取成功,电影券,以逗号隔开*/
        		redirectUrl("/WeixinService/activity/fartherDayActivity/fartherDaySuccess.shtml");
        	}else if(data.returnCode=="0010"){
        		/*奖品已领完*/
        		redirectUrl("/WeixinService/activity/fartherDayActivity/fartherDayBroughtOut.shtml");
        	}else if(data.returnCode=="0020"){
        		/*已经领取过,电影券,以逗号隔开*/
				var strs = new Array(); /* 定义一数组 */
				strs = data.returnMsg.split(","); /* 字符分割 */
				redirectUrl("/WeixinService/activity/fartherDayActivity/fartherDayRepetition.shtml?ticket1=" + strs[0] + "&ticket2=" + strs[1]);
        	}else if(data.returnCode=="9998"){
        		errorRemark("关键参数为空");
        	}else if(data.returnCode=="9999"){
        		errorRemark(data.returnMsg);
        	}else if(data.returnCode=="8001"){
        		/*跳转到注册页*/
        		redirectUrl("/WeixinService/weixinLogin/register.shtml?entrance=fartherDayActivity");
        	}else{
        		errorRemark("网络繁忙，请稍后再试!");
        	}
        }
	});
}
