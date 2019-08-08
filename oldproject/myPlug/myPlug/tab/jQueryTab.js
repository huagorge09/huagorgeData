/*
 * Created by cgh on 2017/06/17.
 */
if(typeof jQuery=='undefined'){
	alert("请引用jq框架")
}
;(function($){
	var tab=function(obj,config){
		var _this=this;   //保存当前对象Tab  
		this.loop=0;  //计数初始值为0
		this.obj=obj; //元素对象
		this.timer=null;  //定时器定义为空
		this.default={  //默认配置
		     "type":"click",  //鼠标触发的类型
			 "effect":"fade",  //切换方式
		     "show":1,    //初始显示第几个
			 "auto":false  //自动	  
		}
		this.opts=$.extend(true, this.default,config);  //利用jquery 原型extend 参数覆盖，得到最新的配置参数
	    this.tabNav=this.obj.find('.tab-nav li');    //保存插件对象及配置参数
		this.tabItem=this.obj.find('.tab-wrap .tab-item'); //保存插件对象及配置参数  
		if(this.opts.type=="click"){  //判断促发的类型
			this.tabNav.bind("click",function(){  
				_this.currentChange($(this))  //传递当前对象到主函数
			})
		}else{
			this.tabNav.mouseover(function(){  
				_this.currentChange($(this)) //传递当前对象到主函数
			})
		}
		if(this.default.auto){  //如果有值，则调用定时器，并且传递时间
		    this.autoPlay(this);  
		    this.obj.hover(function(){
		    	  clearInterval(_this.timer);//鼠标经过清除自动  
		    },function(){
		    	 _this.autoPlay(_this);   //离开启动定时器
		    })
		}
		this.init();
	}
	tab.prototype={
		init:function(){   //初始化
			var index=this.opts.show-1;  //获取显示第几个
			this.loop=index;  
			if(index>this.tabItem.size()-1){  //判断当前坐标是否大于总个数，大于的话重头开始
				index=0;
			}
			this.tabNav.eq(index).addClass("active").siblings().removeClass("active");   
			this.tabItem.eq(index).addClass("active").siblings().removeClass("active");
		},
		currentChange:function(ele){  
			var index=ele.index();
			ele.addClass("active").siblings().removeClass("active");
			if(this.opts.effect=="default"){
				this.tabItem.eq(index).addClass("active").siblings().removeClass("active");
			}else if(this.opts.effect==="fade"){
				this.tabItem.eq(index).stop().fadeIn().siblings().stop().fadeOut();
			}
			if(this.default.auto){ 
				this.loop=index;  
			}
		},
		autoPlay:function(_this){
			var size=this.tabItem.size(); //获取循环的个数
			this.timer=setInterval(function(){
				 _this.loop++; 
				 if(_this.loop>=size){
				 	 _this.loop=0;  
				 }
				 _this.currentChange(_this.tabNav.eq(_this.loop));//传入li，进行自动切换  
			},this.default.auto)
		}
	}
	$.fn.extend({
		tab:function(){
			this.each(function(){
				new tab($(this))
			})
			return this
		}
	})
	window.tab=tab;  //把tab注册到window对象中
})(jQuery);
