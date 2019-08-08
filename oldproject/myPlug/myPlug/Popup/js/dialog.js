/**
 * create by huagorge on 01.31
 */
var dialog={
	alert:function(object){
		this.type=object.type;   //1、为只有一个确认按钮  2、为含有确认取消按钮
		this.title = object.title ||"提示信息";
		this.content=object.content||"提示内容";
        this.confirmText = object.confirmText || "确定";
        this.cancelText = object.cancelText || "取消";
        this.confirm=object.confirm;
        this.cancel=object.cancel;
        this.init();
	},
	init:function(){
		var _this=this;
		var type=this.type;
		var html="";
	    if(type=="1"){
	    	html='<div class="dialog">'+
					'<div class="mask"></div>'+
					'<div class="alertPopup">'+
						'<h2 class="title">'+this.title+'</h2>'+
						'<div class="contain">'+this.content+'</div>'+
						'<div class="btn" id="confirm">'+this.confirmText+'</div>'+
					'</div>'+
				'</div>'
	    }else{
	    	html='<div class="dialog">'+
					'<div class="mask"></div>'+
					'<div class="alertPopup single">'+
						'<h2 class="title">'+this.title+'</h2>'+
						'<div class="contain">'+this.content+'</div>'+
						'<div class="btn clearFloat">'+
				            '<div id="cancel">'+this.cancelText+'</div>'+
				            '<div id="confirm">'+this.confirmText+'</div>'+
			           ' </div>'+
					'</div>'+
				'</div>'
	    }
	    if($(".dialog").length>0){
	    	$(".dialog").remove();
	    }
	    $("body").append(html);
	    $(document).on("click","#confirm",function(){
	    	$(".dialog").remove();
	    	typeof _this.confirm=="function"&&_this.confirm();
	    })
	     $(document).on("click","#cancel",function(){
	    	$(".dialog").remove();
	    	typeof _this.cancel=="function"&&_this.cancel();
	    })
	}
}
