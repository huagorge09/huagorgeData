/**
 * create by huagorge on 01.19
 */
'use strict';
;(function($){

	var dropList=function(obj,option,callback){
		this.obj=obj;
		var defaults={
			isGenerate:false,    //初始第几个显示
		}
		var config=$.extend({},defaults,option);
		this.config=config;
		this.callback=callback;
		this.init();
	}
	dropList.prototype={
		init:function(){
			var element=this.obj;
			if(this.config.isGenerate){
				var listData=this.config.listDate;
				var ele='<ul style="display:none">';
				$.each(listData, function(i,e) {
					ele+='<li>'+e+'</li>'
				});
				ele+="</ul>";
				$(element).after(ele)
			}
			this.eleEvent();
		},
		eleEvent:function(){
			var dropLi=$(this.obj).parents(".ui-drop").find("ul li");
			var _this=this;
			$(this.obj).click(function(){
				var flag=dropLi.parents("ul").is(":visible");
				if(flag){
					$(this).parents(".ui-drop").find("ul").slideUp();
				}else{
					$(this).parents(".ui-drop").find("ul").slideDown();
				}
			})
			dropLi.click(function(){
				var value=$(this).text();
				$(this).parents("ul").siblings("input").val(value);
				$(this).parents("ul").slideUp();
				typeof _this.callback == "function" && _this.callback();
			})
			$("body").click(function(){
   			   $(".ui-drop").find("ul").slideUp();
			})
			$(".ui-drop").click(function(event){
				event.stopPropagation()
			})
		} 
	}
	window.dropList=dropList;
})(jQuery)



