$(function(){
	fragment.initFragList()
	fragment.isSign()
	var countS=""
})
var fragment={
	/**
	 * 初始化宝箱碎片
	 */
	initFragList:function(){
		var _self=this;
		$.ajax({
			url:config.service.shardList,
			type:"post",
			dataType:'json',
			data : {
				"userId":userId,
				"activityId":activityId
			},
			success : function(data) {
				if(data.returnCode=="0"){
					var fragArr=data.data.list;
					var copperArr=[];
					var silverArr=[];
					var goldArr=[];
					for (var i=0;i<fragArr.length;i++) {
						if(fragArr[i].type=="0"){//铜箱子
                           	copperArr.push({
	                            "type": fragArr[i].type,
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
					_self.sortFrag(copperArr,silverArr,goldArr)

				}else{
					msgTip.autoBox({
						 contain: "系统异常，请稍后再试"
					});
				}
			},
			error : function() {
					msgTip.autoBox({
						contain: "系统异常，请稍后再试"
					});
			}
		});
	},
	sortFrag:function(copperArr,silverArr,goldArr){   //碎片排列组合
		var _self=this;
		for (var i=0;i<copperArr.length;i++) { //铜处理
		     if(copperArr[i].sort=="1"){//碎片1
		         _self.allSort(copperArr,i,"0","copper div em");
		     }
		     if(copperArr[i].sort=="2"){//碎片2
		         _self.allSort(copperArr,i,"1","copper div em")
		     }
		     if(copperArr[i].sort=="3"){//碎片3
		         _self.allSort(copperArr,i,"2","copper div em")
		     }
		     if(copperArr[i].sort=="4"){//碎片4
		         _self.allSort(copperArr,i,"3","copper div em")
		     }
		}
		for (var i=0;i<silverArr.length;i++) { //铜处理
		     if(silverArr[i].sort=="1"){//碎片1

		         _self.allSort(silverArr,i,"0","silver div em")
		     }
		     if(silverArr[i].sort=="2"){//碎片2
		         _self.allSort(silverArr,i,"1","silver div em")
		     }
		     if(silverArr[i].sort=="3"){//碎片3
		         _self.allSort(silverArr,i,"2","silver div em")
		     }
		     if(silverArr[i].sort=="4"){//碎片4
		         _self.allSort(silverArr,i,"3","silver div em")
		     }
		}
		for (var i=0;i<goldArr.length;i++) { //铜处理
		     if(goldArr[i].sort=="1"){//碎片1
		         _self.allSort(goldArr,i,"0","gold div em")
		     }
		     if(goldArr[i].sort=="2"){//碎片2
		         _self.allSort(goldArr,i,"1","gold div em")
		     }
		     if(goldArr[i].sort=="3"){//碎片3
		         _self.allSort(goldArr,i,"2","gold div em")
		     }
		     if(goldArr[i].sort=="4"){//碎片4
		         _self.allSort(goldArr,i,"3","gold div em")
		     }
		}
		_self.isAllfarg(copperArr,"copper div em","0");
		_self.isAllfarg(silverArr,"silver div em","1");
		_self.isAllfarg(goldArr,"gold div em","2");
	},
	/**
	 * @param {Object} arr  箱子种类数组
	 * @param {Object} i   索引
	 * @param {Object} n   盒子索引
	 * @param {Object} obj 操作对象
	 */
	allSort:function(arr,i,n,obj){
		 if(arr[i].num=="0"){
		       $("#"+obj).eq(n).addClass("kong");
		       $("#"+obj).eq(n).find("b").remove();
		 }else{
		       $("#"+obj).eq(n).remove("kong");
		       $("#"+obj).eq(n).html("<b>"+arr[i].num+"</b>")
		 }
	},
	/**
	 * 判断盒子是否集齐
	 */
	isAllfarg:function(arr,obj){
	   var flag=true;
	   var n=0
	   for (var i=0;i<arr.length;i++) {
	   	   if(arr[i].num=="0"){
	   	   	  flag=false;
	    	  n++
	   	   }
	   }
	   if(!flag){
	   	  $("#"+obj).eq(4).addClass("kong");
	   	  $("#"+obj).eq(4).removeClass("man");
	   }else if(flag){
	   	  $("#"+obj).eq(4).addClass("man");
	   }
        $("#"+obj).attr("data-isOpen",flag);
    	$("#"+obj).attr("data-still",n);
    	if(flag){
    		$("#"+obj).addClass("shake");
    	}else{
    		$("#"+obj).removeClass("shake");
    	}
       if(obj=="copper div em"){
      	   $("#"+obj).attr("data-type","TONGBOX");
       }else if(obj=="silver div em"){
           $("#"+obj).attr("data-type","YINBOX");
       }else if(obj=="gold div em"){
       	    $("#"+obj).attr("data-type","JINBOX");
       }
	},
	openBoxPrize:function(obj){  //用户点击开罐
		var _self=this;
		var isopen=$(obj).attr("data-isopen");   //是否可以开罐
		var still=$(obj).attr("data-still");     //获取还差几个
		var type=$(obj).attr("data-type");
		if(type=="TONGBOX"){
		      operatingRecord(pageSource,"event_wx_fdClickCopperBoxFragment",pageId,"","","","")
			  if(isopen=="false"){
			  	 $(".openFragNo").show()
		         $(".openFragNo .openBox").eq(0).show().siblings("openBox").hide();
		         $(".openFragNo .openBox").eq(0).find("em").text(still);
		           operatingRecord(pageSource,"event_wx_fdCanNotOpenCopperBoxFragment",pageId,"","","","")
			  }else{
			  	 _self.openDataAward(type)
			  }
		}else if(type=="YINBOX"){
		      operatingRecord(pageSource,"event_wx_fdClickSilverBoxFragment",pageId,"","","","")
			  if(isopen=="false"){
			  	 $(".openFragNo").show()
		         $(".openFragNo .openBox").eq(1).show().siblings("openBox").hide();
		         $(".openFragNo .openBox").eq(1).find("em").text(still);
		          operatingRecord(pageSource,"event_wx_fdCanNotOpenSilverBoxFragment",pageId,"","","","")
			  }else{
			  	 _self.openDataAward(type)
			  }
		}else if(type=="JINBOX"){
		      operatingRecord(pageSource,"event_wx_fdClickGoldBoxFragment",pageId,"","","","")
			  if(isopen=="false"){
			  	 $(".openFragNo").show()
		         $(".openFragNo .openBox").eq(2).show().siblings("openBox").hide();
		         $(".openFragNo .openBox").eq(2).find("em").text(still);
		          operatingRecord(pageSource,"event_wx_fdCanNotOpenGoldBoxFragment",pageId,"","","","")
			  }else{
			  	_self.openDataAward(type)
			  }
		}
		addCookie("firstJoin",true);
		$(".sure").hide();
	},
	openDataAward:function(type){ //用户开盒子
		 var _self=this;
		 if(subscribe=="1"){
		  $.ajax({
			url:config.service.luckDraw,
			type:"post",
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
							  operatingRecord(pageSource,"event_wx_fdClickBoxGetDebrisFragmentOnC",pageId,"","","","")
							}else if(type=="YINBOX"){
							  operatingRecord(pageSource,"event_wx_fdClickBoxGetDebrisFragmentOnS",pageId,"","","","")
							}else if(type=="JINBOX"){
							  operatingRecord(pageSource,"event_wx_fdClickBoxGetDebrisFragmentOnG",pageId,"","","","")
							}
					}else if(data.state=="1"){
							$(".winPrize,.quanBox").show();
							if(type=="TONGBOX"){//铜宝箱中奖
								$(".quanBox .quan i").addClass("copper")
								$(".quanBox .quan i").removeClass("silver gold")
								operatingRecord(pageSource,"event_wx_fdClickBoxGetVoucherFragmentOnC",pageId,"","","","")
							}else if(type=="YINBOX"){//银宝箱中奖
								$(".quanBox .quan i").addClass("silver");
								$(".quanBox .quan").removeClass("copper gold");
								operatingRecord(pageSource,"event_wx_fdClickBoxGetVoucherFragmentOnS",pageId,"","","","")
							}else if(type=="JINBOX"){ //金宝箱中奖
								$(".quanBox .quan i").addClass("gold")
								$(".quanBox .quan").removeClass("silver copper")
								operatingRecord(pageSource,"event_wx_fdClickBoxGetVoucherFragmentOnG",pageId,"","","","")
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
							contain: "活动火爆中，请稍后再抽奖"
						});
					}
				}else{
					msgTip.autoBox({
						contain: "活动火爆中，请稍后再抽奖"
					});
				}
				msgTip.loadingRemove();
			},
			beforeSend:function(){
			 	msgTip.loadingAdd("抽奖中...");
			},
			error:function(){
				msgTip.autoBox({
						contain: "抽奖系统异常"
				});
			}
		})
	   }else{
	   	  if(subscribeChannel=="ctrip"){
	   	      $("#codeChannel").attr("src", "images/wxqrcode-ctrip.png")
	   	  }else{
	   	  	  $("#codeChannel").attr("src","images/wxqrcode-cmwa.png")
	   	  }
	   	  $(".follow").show()

	   	  operatingRecord(pageSource,"event_wx_fdClickBoxButNotSubscribeFragment",pageId,"","","","")

//	   	  if(type=="TONGBOX"){
//        	operatingRecord(pageSource,"event_wx_fdClickBoxButNotSubscribeFragmentOnC",pageId,"","","","")
//        }else if(type=="YINBOX"){
//        	operatingRecord(pageSource,"event_wx_fdClickBoxButNotSubscribeFragmentOnS",pageId,"","","","")
//        }else if(type=="JINBOX"){
//        	operatingRecord(pageSource,"event_wx_fdClickBoxButNotSubscribeFragmentOnG",pageId,"","","","")
//        }  	
	   }
	   _self.initFragList();
	},
	/**
	 * 判断用户是否可以签到
	 */
	isSign:function(){
		var _self=this;
	    $.ajax({
			url:config.service.checkUserCanSignIn,
			type:"post",
			dataType:'json',
			data : {
				"userId":userId
			},
			success:function(data){
				if(data.returnCode=="0"){
					var count=data.data.signTime;
					if(!data.data.canSignIn){//不能签到
						$(".singInRing").addClass("alreadySign");
						$(".singInRing p").eq(0).html("已领取")
						if(data.data.nextSignTime.length>10){
							countdownNum=data.data.nextSignTime.replace("-","/").replace("-","/")              //2018-06-09 03:10:10
							countTime()
							countS=3000
						}else{
								countdownNum=data.data.nextSignTime.replace("-","/").replace("-","/")+" 00:00:00"  //2018-06-09 12:00:00
		                        countTime();
								var date1=new Date()
								var date2=new Date(countdownNum)
								countS=(date1.getTime()-date2.getTime())  //时间相差的毫秒数
						}
					}else{  //可以签到
						$(".singInRing").addClass("noneSign");
						$("#countTips").show();
						$("#countTips2,#countdown").hide()
						$(".singInRing p").eq(0).html("签到")
					}
					switch(count){
						case 1:
						    $(".fragmentProgress").addClass("step1");
						    $(".fragmentProgress li").eq(0).find("p").html("已领取")
						    break;
						case 2:
						    $(".fragmentProgress").addClass("step1 step2");
						    $(".fragmentProgress li").eq(0).find("p").html("已领取")
						    $(".fragmentProgress li").eq(1).find("p").html("已领取")
						    break;
						case 3:
						    $(".fragmentProgress").addClass("step1 step2 step3")
						    $(".fragmentProgress li").eq(0).find("p").html("已领取")
						    $(".fragmentProgress li").eq(1).find("p").html("已领取")
						    $(".fragmentProgress li").eq(2).find("p").html("已领取")
						    break;
						case 4:
						    $(".fragmentProgress").addClass("step1 step2 step3 step4");
						    $("#countTips2").show();
						    $("#countTips1,#countdown").hide();
						    $(".fragmentProgress li").eq(0).find("p").html("已领取")
						    $(".fragmentProgress li").eq(1).find("p").html("已领取")
						    $(".fragmentProgress li").eq(2).find("p").html("已领取")
						    $(".fragmentProgress li").eq(3).find("p").html("已领取")
						    break;
					}

				}else{
					msgTip.autoBox({
						contain: "查询失败，请稍后再试"
					});
				}
			},
			error:function(){
//			    msgTip.autoBox({
//					contain: "系统异常"
//				});
			}
		})
	},
	getFragMent:function(){  //用户签到方法
		var _self=this;
     	$.ajax({
			url:config.service.userSignIn,
			type:"post",
			dataType:'json',
			data : {
				"activityId":activityId,
				"userId":userId,
				"toUserId":toUserId
			},
			success:function(data){
				if(data.returnCode=="0"){
					$(".getFrag").hide();
					_self.initFragList();
					operatingRecord(pageSource,"event_wx_fdClickGetDebrisInSignIn",pageId,"","","","")
				}else{
					msgTip.autoBox({
						contain: "签到失败，请稍后再试"
					});
					$(".getFrag").hide();
				}
			},
			error:function(){
				msgTip.autoBox({
					contain: "系统异常，请稍后再试"
				});
			}
	   })
	},
}


	/**
	 * 用户的行为操作
	 */
var action={
		returnIndex:function(){  //返回首页
			location.href="draw.html?toUserId="+toUserId+"&subscribeChannel="+subscribeChannel+"&pageSource="+pageId+"&eventId=event_wx_returnIndexSignIn"
		},
		getSignFrag:function(){ //用户签到
		   if($(".singInRing").hasClass("noneSign")){
		   	  $(".getFrag").show();
		   }
		},
		closeOpen:function(){  //开罐弹窗关闭
			$(".openFragNo,.winPrize").hide();
			$(".openFragNo .openBox,.winPrize .quanBox,.winPrize .fragBox").hide();
			fragment.initFragList();
		},
		closeOpen:function(){  //开罐弹窗关闭
			$(".openFragNo,.winPrize").hide();
			$(".openFragNo .openBox,.winPrize .quanBox,.winPrize .fragBox").hide();
			fragment.initFragList();
		},
		openInvete:function(){
			$(".openFragNo,.winPrize").hide();
			$(".openFragNo .openBox,.winPrize .quanBox,.winPrize .fragBox").hide();
			showMyInvestor();
			operatingRecord(pageSource,"event_wx_fdClickShareQrcodeFragment",pageId,"","","","")
		},
		topInvete:function(){
			showMyInvestor();
			operatingRecord(pageSource,"event_wx_fdClickShareBtOnTopFragment",pageId,"","","","")
		},
		closeShare:function(){
			$(".subscribe").hide();
		},
		userGetFragment:function(){
			if($(".fragBox").is(":visible")){
			    location.reload()
			}else{
				location.href="offer.html?toUserId="+toUserId+"&subscribeChannel="+subscribeChannel+"&pageSource="+pageId+"&eventId=event_wx_returnOfferSignIn"
			}
			$(".openFragNo,.winPrize").hide();
			$(".openFragNo .openBox,.winPrize .quanBox,.winPrize .fragBox").hide();
		},
		rating:function(){
			location.href="rankingList2.html?toUserId="+toUserId+"&subscribeChannel="+subscribeChannel+"&pageSource="+pageId+"&eventId=event_wx_fdFragmentToHelpList"
		}
	}



var pageId="wx_fatherday_fragment"
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
	showMyInvestor('load')
})
