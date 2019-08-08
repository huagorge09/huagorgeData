;(function($){
	var banner=function(obj,opts,callback){
		this.obj=obj;
		var _this=this;
		this.Timer=null;
		var defaults={
			DisplayIndex:"1",    //初始第几个显示
			IsdotControl:true,   //是否需要多点控制
			IsbtnControl:true,   //是否需要左右点击按钮
		    ChangeStyle:"fade", //幻灯片的切换方式
		    Timer:"1500"          //是否自动切换默认2000ms
		}
		var btncontrol=' <div class="btn-control">'+
						 	  '<a href="javascript:;" class="left-btn"><</a>'+
						 	   '<a href="javascript:;" class="right-btn">></a>'+
						 '</div>';
	   
		var bannerwrap=$(this.obj).find(".banner-silder");
		var len=bannerwrap.find("li").length;
		this.len=len;
		var dotcontrol="";
		for (var i=0;i<len;i++) {
			dotcontrol+="<span></span>"
		}
		this.bannerwrap=bannerwrap;
		this.btncontrol=btncontrol;
		this.dotcontrol='<div class="dot-control">'+dotcontrol+'</div>';
		var config=$.extend({},defaults,opts);
		this.config=config;
		this.config.DisplayIndex=this.config.DisplayIndex-1
		this.init();
		if(this.config.Timer){
			this.timer();
		}
        if(this.config.IsdotControl){
        	this.dotchange();
        }
        if(this.config.IsbtnControl){
        	this.btnchange(_this);
        }
        if(typeof(callback)=="function"&&callback){
        	callback()
        }
	};
	banner.prototype={
		awtg:"dddd",
		init:function(){	//初始化
			var _this=this;
			var bannerwrap=this.bannerwrap;
		    var DisplayIndex=this.config.DisplayIndex;
		    $(bannerwrap).find("li").eq(DisplayIndex).addClass("active");
		    if(this.config.IsbtnControl){
		    	$(this.obj).append(this.btncontrol)
		    }
		    if(this.config.IsdotControl){
		    	$(this.obj).append(this.dotcontrol)
		    	$(this.obj).find(".dot-control").find("span").eq(DisplayIndex).addClass("active");
		    }
		   
		},
		change:function(len){  //主函数切换
			this.config.DisplayIndex=len;
			if(this.config.ChangeStyle=="fade"){
			    $(this.bannerwrap).find("li").eq(len).stop(true).fadeIn().siblings().fadeOut();
	            if(this.config.IsdotControl){
	                 $(".dot-control").find("span").stop(true).eq(len).addClass("active").siblings().removeClass("active")
	            }
			}else{
			    $(this.bannerwrap).find("li").stop(true).eq(len).addClass("active").siblings().removeClass("active")
	            if(this.config.IsdotControl){
	                 $(".dot-control").find("span").stop(true).eq(len).addClass("active").siblings().removeClass("active")
	            }
			}
		},
		timer:function(){   //定时处理
			var obj=$(this.obj);
			var _this=this;
			var len=this.len;
			var timer=this.config.Timer
            function auto(){
            	var DisplayIndex=_this.config.DisplayIndex;
            	DisplayIndex++;
		    	if(DisplayIndex>=len){
		    		DisplayIndex=0;
		    	}
		       _this.change(DisplayIndex);
            }
            var t=setInterval(auto,timer)
            $(this.obj).hover(function(){
            	 clearInterval(t)
            },function(){
            	 t=setInterval(auto,timer)
            })
		},
		dotchange:function(){  //多点控制
			var _this=this;
			$(document).on("click",".dot-control span",function(){
				 _this.change($(this).index());
			}) 
		},
		btnchange:function(_this){  //左右点击
			var _this=_this;
			$(document).on("click",".btn-control a",function(){
				var DisplayIndex=_this.config.DisplayIndex;
				if($(this).index()=="0"){
					DisplayIndex--;
					if(DisplayIndex<0){
						DisplayIndex=_this.len-1
					}
					_this.change(DisplayIndex)
				}else{
					DisplayIndex++;
					if(DisplayIndex>_this.len-1){
						DisplayIndex=0
					}
					_this.change(DisplayIndex)
				}
			}) 
		}
	}
	/*把对象引入到jquery中*/
    $.fn.Ban=function(opt){
    	var banner1 = new banner(this, opt);
    	return this;
    }
	window.banner=banner
})(jQuery)
