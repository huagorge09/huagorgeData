var myScroll;
$(document).ready(function(e){
	$(".header .top-a h2").html("关于财富");
	document.title="关于财富";
	myScroll = new IScroll('#wrapper', {
		scrollbars : true,
		mouseWheel : true,
		interactiveScrollbars : true,
		shrinkScrollbars : 'scale',
		fadeScrollbars : true
	});
	myScroll.refresh();
	getUserRequest("aboutUs");/* 此处subPath为页面内行为 */
});

document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);