/**
 * create by huagorge on 11.23
 */
;(function($) {
	var scroll = function(obj, option, callback) {
		var obj = obj
		this.options = option;
		var defaults = {
			timer: 1000,  //滚动的时间
			line: 1,     //滚动的行数
			speed: 1000,  //滚动的速度
			hover:false  //鼠标经过是否滚动
		}
		this.options = $.extend({}, defaults, this.options);
		var height = -$(obj).find("ul li").height() * this.options.line;
		var scrollObject = $(obj).find("ul");
		var t = setInterval(sorollAnimate, this.options.speed);
		var _this = this;
		function sorollAnimate() {
			scrollObject.animate({
					marginTop: height
				}, _this.options.timer,
				function() {
					scrollObject.css({
						marginTop: "0px"
					}).find("li:first").appendTo(scrollObject)
				})
		}
		if(defaults.hover){
			var _this=this;
			scrollObject.hover(function(){
                clearInterval(t);
			},function(){
				setInterval(sorollAnimate, _this.options.speed);
			})
		}
		var callData={
			"test":"ddddd"
		}
		typeof callback == "function" && callback(callData);

//		if(typeof(callback)=="function"){
//			callback()
//		}
	}
	window.scroll = scroll;
})(jQuery)