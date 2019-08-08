/*
 * Created by cgh on 2018/04/20
 * ���ܣ����������
 */
;(function($){
	var scheduleBox=function(options){
		this.per=options.per;
		this.timer=options.timer;
		console.log(options)
		var html="";
		html+='<div class="scheduleBox">'+
					'<div class="mask"></div>'+
					'<div class="scheduleBar center">'+
						'<div class="currentBar"></div>'+
						'<div class="percent"></div>'+
					'</div>'+
				'</div>'
		$("body").append(html);
		$(".scheduleBox").find(".currentBar").css("width", this.per+"%");	
		this.setCurrent();
	}
	scheduleBox.prototype.setCurrent=function(){
		var _this=this;
		if(this.per < 0){
			this.per = 0;
		} else if(this.per > 100){
			this.per = 100;
		}
		var percent=Number(this.per);
		    setInterval(function(){
				percent++
				if(percent<101){
					$(".percent").html(percent+"%");
					$(".scheduleBox").find(".currentBar").css("width", percent+"%");
				}else{
					$(".scheduleBox").remove();
				}
			},this.timer)
	}
	window.scheduleBox = scheduleBox;
})(jQuery);