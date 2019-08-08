var swiper = new Swiper('.swiper-container', {
    slidesPerView: 2,
    autoplay: 3000,
    spaceBetween:55,
    centeredSlides: true,
	onTransitionEnd: function(swiper){
	    $(".desc p").eq(swiper.activeIndex).show().siblings().hide()
	},
	autoplayDisableOnInteraction : false
});



function goDraw(){
	location.href="draw.html?toUserId="+toUserId+"&subscribeChannel="+subscribeChannel+"&pageSource="+pageId+"&eventId=event_wx_gotoIndex"
}
var pageId="wx_fatherday_bootPage"
var eventId=""
/**
 * 初始化数据埋点
 */
dataRecord() 
function dataRecord() {
	if(getUrlSearchParams("pageSource")) {
		pageSource = getUrlSearchParams("pageSource")
	} else {
		pageSource = pageId;
	}
	if(getUrlSearchParams("eventId")) {
		eventId = getUrlSearchParams("eventId")
	}
	if(eventId && pageSource) {
		operatingRecord(pageSource,eventId,pageId,"","","","")
	}
}
