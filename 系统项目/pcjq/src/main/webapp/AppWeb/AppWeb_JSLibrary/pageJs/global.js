$(document).ready(function(){
	$('#nav').find('li').hover(function(){
		$(this).find('div').stop().css('display','block').animate({opacity:1, top:120},600);
	},function(){
		$(this).find('div').stop().animate({opacity:0, top:120},400,function(){
			$(this).css('display','none');
		});
	})


	// var navCur = $('#nav').find('.cur').size() > 0 ? $('#nav').find('.cur') : 0;
	// var iLeft = 0;
	// var iOpacity = 0;

	// navLeft = $('#nav').offset().left;
	// if (navCur) {
	// 	iLeft = navCur.offset().left - navLeft;
	// 	iOpacity = 1;
	// 	$('#nav_line').css({left:iLeft}).animate({opacity:1},300);
	// }

	// navLeft = $('#nav').offset().left;
	// $('#nav li').hover(function(){
	// 	var newLeft = $(this).offset().left;
	// 	if (!navCur) {
	// 		iLeft = newLeft;
	// 	};
	// 	$('#nav_line').stop().animate({left:newLeft, opacity:1},600);
	// })
	// $('#nav').mouseleave(function(){
	// 	$('#nav_line').stop().animate({left:iLeft, opacity:iOpacity},500);
	// })
})