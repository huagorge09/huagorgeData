$(function(){
	offer.queryUserInfo();
	offer.eleBind();
	offer.queryAwardList();
})
var userFullname="" //用户名
var mobile=""//手机号码
var userinfoId="" 
var offer={
	queryAwardList:function(){  //初始获取用户奖品
		$.ajax({
			url:config.service.queryUserAward,
			type:"post",
			async:"false",
			dataType:'json',
			data : {
				"userId":userId,
				"activityId":activityId,
				"awardType":"0"
			},
			success:function(data){
				if(data.returnCode=="0"){
					    var list=data.data
					    if(list.length>0){  //有优惠券了
					 	$(".item").show();
					 	var html=""
					 	for (var i=0;i<list.length;i++) {
					 		if(list[i].awardState=="0"){					 			
					 				html+='<li class="dswkb">';
					 	    }else if(list[i].awardState=="1"){   
					 				html+='<li class="dswkb status1">'
					 		}else if(list[i].awardState=="2"){
					 				html+='<li class="dswkb status2">'
					 		}
						          html+='<div class="itemName"></div>'+
						    			'<div class="itemInfo">'+   
								    		'<p><em>￥</em>'+list[i].awardPrice+'</p>'+
								    		'<p>有效日期：'+list[i].invalidDate.substring(0,10)+'</p>'+
							    		'</div>';
						    if(list[i].awardState=="0"){
						    	html+='<div>'+
						    	  	      '<a cusAwardId='+list[i].cusAwardId+' href="javascript:;" onclick="offer.goExchangeBtn(this)">去兑换</a>'+
						    		  '</div>';
						    }else if(list[i].awardState=="1"||list[i].awardState=="2"){
						    	html+='<div class="grayIcon"></div>';
						    }
		                    html+='<i></i></li>';	
					 	}
					 	$(".item").html("")
					 	$(".item").append(html);
					 	
					 	$(".tips").show()
					 }else{
					 	$(".none").show();
					 }
				}
				msgTip.loadingRemove();
			 },
			 error:function(data){
				msgTip.autoBox({
					contain: "系统异常"
				});
				msgTip.loadingRemove();
			 },
			 beforeSend:function(){
			 	msgTip.loadingAdd("数据加载中...");
			 }
		})
	},
	queryUserInfo:function(){  //初始获取用户信息
		$.ajax({
			url:config.service.queryUserAwardInfo,
			type:"post",
			async:"false",
			dataType:'json',
			data : {
				 "userId":userId,
				 "activityId":activityId
			},
			success:function(data) {
				if(data.returnCode=="0"){
					if(data.data.length>0){
						$(".awardInfoText").show();
						$("#username").text(data.data[0].userFullName);
						$("#mobilePhone").text(data.data[0].mobile);
						$("input[name=userName]").val(data.data[0].userFullName);
						$("input[name=mobilePhone]").val(data.data[0].mobile);
						userFullname=data.data[0].userFullName;
						mobile=data.data[0].mobile;
						userinfoId=data.data[0].userinfoId;
					}
				}
			},
			error:function(){
				msgTip.autoBox({
					contain: "系统异常"
				});
			}
	    })
	},
	updateDateSumbit:function(userName,mobilePhone){  //更新用户兑奖信息
		var _self=this;
		$.ajax({
			url:config.service.updateUserAwardInfo,
			type:"post",
			async:"false",
			dataType:'json',
			data : {
				"userId":userId,
				"userFullName":userName,
				"mobile":mobilePhone,
				"activityId":activityId,
				"userinfoId":userinfoId
			},
			success:function(data) {
				if(data.returnCode=="0"){
				    operatingRecord(pageSource,"event_wx_fdClickEditUserInfo",pageId,"","","","")
					$(".sumitPop .content").html("修改成功")
					$(".sumitPop").show();
					setTimeout(function(){
						$(".sumitPop,.awardInfo input,.save").hide();
						$(".awardInfo li span,.edit").show();
						$(".awardInfo li").removeClass("editLi");
						offer.queryAwardList();
						offer.queryUserInfo();
					},2000)
					
				}
			},
			error:function(){
                 msgTip.autoBox({
						contain: "系统异常，请稍后再试"
			    });	
			}
		})
	},
	goExchangeBtn:function(awardId){ //用户兑将点击
	    operatingRecord(pageSource,"event_wx_fdFillInUserInfo",pageId,"","","","") 
		var award=$(awardId).attr("cusawardid");
		var _self=this;
		if($(".awardInfo").is(":visible")){
			_self.goExchangeSubmit(award)
		}else{
			$(".cashPrize").show().attr("awardId",award);
		}
		
	},
	popUserSubmit:function(){  //用户初次兑奖
		var _self=this;
		var userName=$.trim($("input[name=username2]").val());
		var mobilePhone=$.trim($("input[name=mobilePhone2]").val());
		var awardId=$(".cashPrize").attr("awardId");
		var flag=true;
		if(userName==""||!ischinese(userName)){
			$("input[name=username2]").val("")
			$("input[name=username2]").attr("placeholder","请输入2-10字中文姓名");
			$("input[name=username2]").addClass("colorRed")
			flag=false;
		}	
		if (!isMobile(mobilePhone)) {
			$("input[name=mobilePhone2]").val("")
			$("input[name=mobilePhone2]").attr("placeholder","请输入正确的手机号码");
			$("input[name=mobilePhone2]").addClass("colorRed")
			flag=false;
		}
		if(flag){
			 userFullname=userName //用户名
              mobile=mobilePhone//手机号码
			_self.goExchangeSubmit(awardId)
		}
	},
	goExchangeSubmit:function(award){   //用户兑奖
		var _self=this;
		var param={
			"userId":userId,
			"userFullName":userFullname,
			"mobile":mobile,
			"cusAwardId":award,
  		    "activityId":activityId
		}
		$.ajax({
			url:config.service.saveUserAwardInfo,
			type:"post",
			async:"false",
			dataType:'json',
			data:param,
			success:function(data) {
				if(data.returnCode=="0"){
					$(".sumitPop .content").html("兑奖成功")
					$(".sumitPop").show();
					setTimeout(function(){
						$(".sumitPop").hide();
					},2000)
					_self.queryAwardList();
					$(".cashPrize").hide();
					$(".cashPrize input").val("");
					 operatingRecord(pageSource,"event_wx_fdGetAward",pageId,"","","","") 
				}else{
					msgTip.autoBox({
						contain: "兑奖失败"
					});
				}
			},
			error:function(){
				msgTip.autoBox({
					contain: "系统异常"
				});
			}
		})
		_self.queryUserInfo()
	},
	action:{  
		edit:function(){  //编辑数据
			$("#username,#mobilePhone,.edit").hide();
			$("input[name=userName],input[name=mobilePhone],.save").show();
			$(".awardInfo li").addClass("editLi")
		},
		save:function(){  //用户保存数据
			var _self=this;
			var userName=$.trim($("input[name=userName]").val());
			var mobilePhone=$.trim($("input[name=mobilePhone]").val());
			var flag=true;
			if(userName==""||!ischinese(userName)){
				$("input[name=userName]").val("")
				$("input[name=userName]").attr("placeholder","请输入2-10字中文姓名");
				$("input[name=userName]").addClass("colorRed")
				flag=false;
			}	
			if (!isMobile(mobilePhone)) {
				$("input[name=mobilePhone]").val("")
				$("input[name=mobilePhone]").attr("placeholder","请输入正确的手机号码");
				$("input[name=mobilePhone]").addClass("colorRed")
				flag=false;
			}
			if(flag){
				
				offer.updateDateSumbit(userName,mobilePhone);
			}
		},
		returnIndex:function(){
			location.href="draw.html?toUserId="+toUserId+"&subscribeChannel="+subscribeChannel+"&pageSource="+pageId+"&eventId=event_wx_fdReturnIndexFromMyAword"
		}
		
	},
	eleBind:function(){
		$("input").focus(function(){
			$(this).addClass("focus");
			$(this).removeClass("colorRed")
		})
		$("input").blur(function(){
			$(this).removeClass("focus")
		})
		$(".mask").click(function(){
			$(".sumitPop").hide()
		})
		$("#close").click(function(){
			$(".cashPrize").hide();
			$(".cashPrize input").val("");
			$(".cashPrize input").removeClass("colorRed")
		})
	}
}





var pageId="wx_fatherday_awardList";
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
