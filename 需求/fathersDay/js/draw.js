$(function(){
	draw.initPage();
	
})

var draw={
	initPage:function(){  //初始化页面数据
	    this.queryFragList();
	    this.checkUserCanSignIn();
	    this.querySystemNotice();
	    this.queryMyNotice();
	    this.noticeSetInterval();
	    if(intervalTime()){ //定时查询通知服务
		    setInterval(this.querySystemNotice,30000)
			setInterval(this.queryMyNotice,30000)
	    }
	},
	noticeSetInterval:function(){
		//jQuery(".txtMarquee-left").slide({mainCell:"#sysNotice ul",autoPlay:true,effect:"topMarquee",interTime:50,trigger:"click"});
		//var _self=this;
		// jQuery(".txtMarquee-left").slide({mainCell:".sysNotice ul",autoPlay:true,effect:"leftMarquee",vis:2,interTime:1000});
		setInterval('sysNoticeSroll("#sysNotice")',3000);  //系统通知显示轮播时间
		setInterval('sysNoticeSroll("#myNotice")',3000);   //查询后台系统通知时间
	},
	queryFragList:function(){  //查询用户碎片列表
		 var _self=this;
		 $.ajax({
			url:config.service.shardList,
			type:"post",
			async:false,
			dataType:'json',
			data : {
				"userId":userId,
				"activityId":activityId
			},
			success:function(data) {
				if(data.returnCode=="0"){
					var fragArr=data.data.list;
					var copperArr=[];
					var silverArr=[];
					var goldArr=[];
					for (var i=0;i<fragArr.length;i++) {
						if(fragArr[i].type=="0"){//铜箱子
	                       	copperArr.push({
	                            "type":fragArr[i].type, 
				                "num":fragArr[i].num, 
				                "sort":fragArr[i].sort, 
	                       	})
						}else if(fragArr[i].type=="1"){//银箱子
							silverArr.push({
	                            "type": fragArr[i].type, 
				                "num":fragArr[i].num, 
				                "sort":fragArr[i].sort, 
	                       	})
						}else if(fragArr[i].type=="2"){  //金箱子
							goldArr.push({
	                            "type": fragArr[i].type, 
				                "num":fragArr[i].num, 
				                "sort":fragArr[i].sort, 
	                       	})
						}
					}
					_self.isTreasureOpen(copperArr,"0")  
					_self.isTreasureOpen(silverArr,"1")
					_self.isTreasureOpen(goldArr,"2")
					_self.elePageAnimate(data.data.total)
				}else{
					msgTip.autoBox({
						contain: "returnMsg"
					});
				}
			},
			error:function(){
//				msgTip.autoBox({
//					contain: "系统异常"
//				});
			}
	    })
	},
	isTreasureOpen:function(treasure,type){  //初始化宝箱数据判断是否可以点击，并增加闪动动效果
		var flag=true;
		var n=0
	    for (var i=0;i<treasure.length;i++) {
	    	if(treasure[i].num=="0"){
	    		flag=false;
	    		n++
	    	}
	    }
	    if(type=="0"){
	    	$("#FDACTCBCOUNT").attr("data-isOpen",flag);
	    	$("#FDACTCBCOUNT").attr("data-still",n);
	    	$("#FDACTCBCOUNT").attr("data-type","TONGBOX");
	    	if(flag){
	    		$("#FDACTCBCOUNT").addClass("shake");
	    	}else{
	    		$("#FDACTCBCOUNT").removeClass("shake");
	    	}
	    }else if(type=="1"){
	    	$("#FDACTSBCOUNT").attr("data-isOpen",flag);
	    	$("#FDACTSBCOUNT").attr("data-still",n);
	    	$("#FDACTSBCOUNT").attr("data-type","YINBOX");
	    	if(flag){
	    		$("#FDACTSBCOUNT").addClass("shake");
	    	}else{
	    		$("#FDACTSBCOUNT").removeClass("shake");
	    	}
	    }else if(type=="2"){
	    	$("#FDACTGBCOUNT").attr("data-isOpen",flag);
	    	$("#FDACTGBCOUNT").attr("data-still",n);
	    	$("#FDACTGBCOUNT").attr("data-type","JINBOX");
	    	if(flag){
	    		$("#FDACTGBCOUNT").addClass("shake");
	    	}else{
	    		$("#FDACTGBCOUNT").removeClass("shake");
	    	}
	    }
	},
	elePageAnimate:function(total){  //页面元素效果
		var isFirstJoin=getCookie("firstJoin")
	    if(!isFirstJoin){
	    	$(".sure").show();
	    }
		if(total!=="0"){
		    $("#fragNum").show();
			$("#fragNum").html("x"+total);
		}
		if(total=="0"){
			$("#fragNum").hide()
		}
	},
	openBoxPrize:function(obj){  //用户开罐
		var _self=this;
		var isopen=$(obj).attr("data-isopen");   //是否可以开罐
		var still=$(obj).attr("data-still");     //获取还差几个
		var type=$(obj).attr("data-type");
		if(type=="TONGBOX"){
		      operatingRecord(pageSource,"event_wx_fdClickCopperBox",pageId,"","","","")
			  if(isopen=="false"){
			  	 $(".openFragNo").show()
		         $(".openFragNo .openBox").eq(0).show().siblings("openBox").hide();
		         $(".openFragNo .openBox").eq(0).find("em").text(still);
		         operatingRecord(pageSource,"event_wx_fdCanNotOpenCopperBox",pageId,"","","","") 
			  }else{
			  	 _self.openDataAward(type)
			  }
		}else if(type=="YINBOX"){
		      operatingRecord(pageSource,"event_wx_fdClickSilverBox",pageId,"","","","")
			  if(isopen=="false"){
			  	 $(".openFragNo").show()
		         $(".openFragNo .openBox").eq(1).show().siblings("openBox").hide();
		         $(".openFragNo .openBox").eq(1).find("em").text(still);
		         operatingRecord(pageSource,"event_wx_fdCanNotOpenSilverBox",pageId,"","","","") 
			  }else{
			  	 _self.openDataAward(type)
			  }
		}else if(type=="JINBOX"){
		      operatingRecord(pageSource,"event_wx_fdClickGoldBox",pageId,"","","","")
			  if(isopen=="false"){
			  	 $(".openFragNo").show()
		         $(".openFragNo .openBox").eq(2).show().siblings("openBox").hide();
		         $(".openFragNo .openBox").eq(2).find("em").text(still);
		         operatingRecord(pageSource,"event_wx_fdCanNotOpenGoldBox",pageId,"","","","") 
			  }else{
			  	_self.openDataAward(type)
			  }
		}
		addCookie("firstJoin",true);
		$(".sure").hide();  //codeUrl
	},
	openDataAward:function(type){ //用户开盒子
		 var _self=this;
		 if(subscribe=="1"){ 
		  $.ajax({
			url:config.service.luckDraw,
			type:"post",
			async:false,
			dataType:'json',
			data : {
				"userId":userId,
				"activityId":activityId,
				"boxState":type
			},
			success:function(data) {
				if(data.returnCode=="0"){
					if(data.state=="0"){//中奖的为碎片
							$(".winPrize,.fragBox").show();
							$(".fragBox em b").html(data.num);
							if(type=="TONGBOX"){
							  operatingRecord(pageSource,"event_wx_fdClickBoxGetDebrisOnC",pageId,"","","","")
							}else if(type=="YINBOX"){
							  operatingRecord(pageSource,"event_wx_fdClickBoxGetDebrisOnS",pageId,"","","","")
							}else if(type=="JINBOX"){
							  operatingRecord(pageSource,"event_wx_fdClickBoxGetDebrisOnG",pageId,"","","","")
							}
					}else if(data.state=="1"){
							$(".winPrize,.quanBox").show();
							if(type=="TONGBOX"){//铜宝箱中奖
								$(".quanBox .quan i").addClass("copper")
								$(".quanBox .quan i").removeClass("silver gold")
								operatingRecord(pageSource,"event_wx_fdClickBoxGetVoucherOnC",pageId,"","","","") 
							}else if(type=="YINBOX"){//银宝箱中奖
								$(".quanBox .quan i").addClass("silver");
								$(".quanBox .quan").removeClass("copper gold")
								operatingRecord(pageSource,"event_wx_fdClickBoxGetVoucherOnS",pageId,"","","","") 
							}else if(type=="JINBOX"){ //金宝箱中奖
								$(".quanBox .quan i").addClass("gold")
								$(".quanBox .quan").removeClass("silver copper")
								operatingRecord(pageSource,"event_wx_fdClickBoxGetVoucherOnG",pageId,"","","","") 
							}
							if(data.data.remark!=""){
								switch(data.data.remark){
									case "XC50":   //50元的
									      $(".winPrize .faceValue").html("50");
									      break;
							        case "XC100":  //100元的
									      $(".winPrize .faceValue").html("100");
									      break;
							        case "XC500":  //500元的
									      $(".winPrize .faceValue").html("500");
									      break;
							        case "XC1000":  //1000元的
									      $(".winPrize .faceValue").html("1000");
									      break;										      
								}
							}
					}else{
						msgTip.autoBox({
							contain: "活动火爆中，请刷新在尝试"
						});
					}
				}else{
					msgTip.autoBox({
						contain: "活动火爆中，请刷新在尝试"
					});
				}
				msgTip.loadingRemove();
				_self.queryFragList();
			},
			beforeSend:function(){
			 	msgTip.loadingAdd("抽奖中...");
			},
			error:function(){
//				msgTip.autoBox({
//						contain: "抽奖系统异常"
//				});
			}
		}) 
	   }else{
	   
	   	  if(subscribeChannel=="ctrip"){
	   	     $("#codeChannel").attr("src", "images/wxqrcode-ctrip.png")	 
	   	  }else{
	   	  	 $("#codeChannel").attr("src","images/wxqrcode-cmwa.png")
	   	  }
	   	  $(".follow").show()
	   	  operatingRecord(pageSource,"event_wx_fdClickBoxButNotSubscribe",pageId,"","","","") 
//	   	  if(type=="TONGBOX"){
//        	operatingRecord(pageSource,"event_wx_fdClickBoxButNotSubscribeFragmentOnC",pageId,"","","","")
//        }else if(type=="YINBOX"){
//        	operatingRecord(pageSource,"event_wx_fdClickBoxButNotSubscribeFragmentOnS",pageId,"","","","")
//        }else if(type=="JINBOX"){
//        	operatingRecord(pageSource,"event_wx_fdClickBoxButNotSubscribeFragmentOnG",pageId,"","","","")
//        }  	
	   }
	},
    checkUserCanSignIn:function(){ //查询用户是否可以签到及信息
    	$.ajax({
			url:config.service.checkUserCanSignIn,
			type:"post",
			dataType:'json',
			data : {
				"userId":userId
			},
			success:function(data) {
				if(data.returnCode=="0"){
					if(data.data.canSignIn){
						$(".getFrag").show();
						operatingRecord(pageSource,"event_wx_userCanSignIn",pageId,"","","","")
					}
				}
			},
			error:function(){
//				msgTip.autoBox({
//					contain: "系统异常"
//				});
			}
    	})
    },
    userSign:function(){  //用户签到
    	var _self=this;
    	$.ajax({
			url:config.service.userSignIn,
			type:"post",
			async:false,
			dataType:'json',
			data : {
				"activityId":activityId,
				"userId":userId,
				"toUserId":toUserId
			},
			success:function(data) {
				if(data.returnCode=="0"){
				     operatingRecord(pageSource,"event_wx_fdClickFirstGetDebris",pageId,"","","","")
					$(".getFrag").hide();	
					flyer();
				}else{
					msgTip.autoBox({
						contain: "签到失败"
					});
				}
			},
			error:function(){
//				msgTip.autoBox({
//					contain: "系统异常"
//				});
			}
    	})
    	_self.queryFragList();
    },
    querySystemNotice:function(){  //查询系统通知
    	var _self=this;
    	$.ajax({
			url:config.service.querySystemNotice,
			type:"post",
			dataType:'json',
			data : {
				"activityId":activityId,
			},
			success:function(data) {
				if(data.returnCode=="0"){
					var html="";
					if(data.data.length>0){
						for (var i=0;i<data.data.length;i++) {
							if(data.data[i].msgType=="0"){
							     html+='<li><span>'+data.data[i].nickname+'</span>成功开启'+data.data[i].awardSource+'，勇夺'+data.data[i].awardName+'，无敌是多么寂寞！</li>'
							}else if(data.data[i].msgType=="1"){
								 html+='<li><span>'+data.data[i].nickname+'</span>为<span>'+data.data[i].otherUserName+'</span>助力一张碎片，一起来地图寻宝吧</li>'
							}
						}
					}else{
						html+='<li>欢迎参与，收集地图碎片赢取旅行代金券</li>';
					}
					
					$("#sysNotice ul").html("")
					$("#sysNotice ul").append(html)
				}
			},
			error:function(){
				msgTip.autoBox({
					contain: "系统通知查询异常"
				});
			}
    	})
    },
    queryMyNotice:function(){  //查询与自己相关的通知
    	var _self=this;
    	$.ajax({
			url:config.service.queryUserNotice,
			type:"post",
			dataType:'json',
			data : {
				"activityId":activityId,
				"userId":userId
			},
			success:function(data) {
				if(data.returnCode=="0"){
					  if(data.data.length>0){
					  	  $(".myNotice").show();
					  	  var html="";
					  	  var list=data.data
					  	  for(var i=0;i<list.length;i++){
					  	  	 if(list[i].type=="0"){					  	  	     
					  	  	 	html+='<li><span>你</span>在'+data.data[i].awardSource+'觅得宝藏，斩获'+list[i].awardName+'</li>'
					  	  	 }else if(list[i].type=="1"){
					  	  	 	html+='<li><span>'+list[i].nickname+'</span>为我助力一张碎片，TA关注你很久了哦</li>'
					  	  	 }else if(list[i].type=="2"){
					  	  	 	html+='<li>你的好友<span>'+list[i].nickname+'</span>在'+data.data[i].awardSource+'成功觅得宝藏，斩获'+list[i].awardName+'，快去恭喜TA</li>'
					  	  	 }else if(list[i].type=="3"){
					  	  	    html+='<li>我为<span>'+list[i].nickname+'</span>助力一张碎片，挺TA不解释 </li>'
					  	  	 }
					  	  }
					  	  $("#myNotice ul").html("")
					  	  $("#myNotice ul").append(html)
					  }else{
					  	 $("#myNotice").hide();
					  }
                    
				}
			}
		})
    }
}

/**
 * 用户行为操作
 */
var action={
	signIn:function(){     //跳转到我的碎片
		location.href="fragment.html?toUserId="+toUserId+"&subscribeChannel="+subscribeChannel+"&pageSource="+pageId+"&eventId=event_wx_fdClickSignIn"
	},
	myAward:function(){   //跳转到我的奖品页面
		location.href="offer.html?toUserId="+toUserId+"&subscribeChannel="+subscribeChannel+"&pageSource="+pageId+"&eventId=event_wx_fdViewMyAword"
	},
	showRules:function(){  //显示规则弹窗
		$(".rules").show();
		operatingRecord(pageSource,"event_wx_fdViewRules",pageId,"","","","")
	},
	hideRules:function(){  //隐藏规则弹窗
		$(".popup.rules").hide();
		operatingRecord(pageSource,"event_wx_fdReturnIndex",pageId,"","","","")
	},
	getFragMent:function(){ //领取碎片
		draw.userSign();
	},
	friendHelp:function(){  //邀请朋友助理
		 showMyInvestor()
		 operatingRecord(pageSource,"event_wx_fdClickShareBtOnIndex",pageId,"","","","")
	},
	closeOpen:function(){  //开罐弹窗关闭
		$(".openFragNo,.winPrize").hide();
		$(".openFragNo .openBox,.winPrize .quanBox,.winPrize .fragBox").hide();
		draw.queryFragList();
	},
	openInvete:function(){
		$(".openFragNo,.winPrize").hide();
		$(".openFragNo .openBox,.winPrize .quanBox,.winPrize .fragBox").hide();
	    showMyInvestor()
	    operatingRecord(pageSource,"event_wx_fdClickShareQrcode",pageId,"","","","")
	},
	userGetFragment:function(){
		if($(".quanBox").is(":hidden")){
			flyer();
		}else{
			location.href="offer.html?toUserId="+toUserId+"&subscribeChannel="+subscribeChannel+"&pageSource="+pageId+"&eventId=event_wx_fdClickSignIn"
		}
		$(".openFragNo,.winPrize").hide();
		$(".openFragNo .openBox,.winPrize .quanBox,.winPrize .fragBox").hide();
		draw.queryFragList();
	},
	closeShare:function(){
		$(".subscribe").hide();
	},
	rank:function(){
	    location.href="rankingList2.html?toUserId="+toUserId+"&subscribeChannel="+subscribeChannel+"&pageSource="+pageId+"&eventId=event_wx_fdViewIndexToHelpList"
	}
}
/**
 * 购物车飞入效果
 */
function flyer() {
	var offset = $('#end').offset(), 
	flyer = $('<img class="u-flyer" src="images/chip2.png"/>');
	flyer.fly({
		start: {
			left: 0,
			top: 30
		},
		end: {
			left: offset.left+15,
			top: offset.top+20,
			width:20,
			height:20	
		}
	});
	setTimeout(function(){
		$(".u-flyer").fadeOut();
	},1500)
}
/**
 * 系统公告
 * @param {Object} obj
 */
function sysNoticeSroll(obj) {
       $(obj).find("ul:first").animate({
        marginTop: "-25px"
    }, 500, function() {
        $(this).css({marginTop: "0px"}).find("li:first").appendTo(this);
    });
}



var pageId="wx_fatherday_index";
var eventId="";
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
$(function(){
	showMyInvestor("load")
})
