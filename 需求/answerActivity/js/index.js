var openId="";  //用户openid
var score=0;     //用户得分
var imgUrl="";   //用户头像
var prizeId=""   //奖品id
var userName=""  //用户信息
var userTel=""   //用户
var index={
	init:function(){ //初始化页面
		var activeEnd=config.activeEnd; //活动结束时间
		var nowTime = new Date()  //当前时间
		if(new Date(format(activeEnd))>new Date(format(nowTime))) {   //活动开始中
            var _self=this;
			if(_self.queryUserInfo()){
				_self.firstJoinPage();
			}
		} else{ //活动结束
			$(".period").show();
		}
	},
	queryUserInfo:function(){  //获取用户openId、头像、是否关注
		   var userid=getUrlSearchParams("userid")
           var flag=true;
			$.ajax({
				url:config.service.isSubscribeByOpenid,
				async:false,
				type:"post",
				data:{
					userid:userid
				},
				dataType:"json",
				success:function(res){
					if(res.returnCode=="0"){
						$("#body").show();
						if(res.data!=""){
							openId=res.data.openId;
							imgUrl=res.data.imgUrl;
							$(".wxUserImg").attr("src",imgUrl.replace("http","https"));
							if(res.data.status=="0"){ //未关注
								flag=false;
								$(".follow").show();
							}
						}
					}else{
						//window.location.href="https://wxtest1.cmwachina.com/auth/proxy.html?target_url=https://wxtest1.cmwachina.com/WeixinService/activity/answerActivity/index.html "
					}
					
				}
			})
	
		return flag
		
		
	},
    firstJoinPage:function(){  //判断用户是否首次进入活动页面及是否有抽奖机会
    	$.ajax({
			url:config.service.checkFirstIntoActivity,
			type:"post",
			data:{"openId":openId},
			dataType:"json",
			success:function(res){
				if(res.returnCode=="0"){
					if(res.data.firstJoin=="1"){   //非首次进入活动页
						
					}
					else if(res.data.firstJoin=="0"){
						$(".silk").show();
					}
					if(res.data.isDraw=="1"){  //大于60分，且有抽奖机会
					   $(".btnDraw").show();
					}
				}
			}
		})
    },
	answer:function(){  //用户答题及组装
		var i=0;
        var _self=this;
		_self.assemble();	
		$(document).on("click",".answerList li",function(){
		
			if($(this).parents(".answerBox").attr("isAnswer")=="false"){
				$(".legend p").hide();
				$(this).parents(".answerBox").attr("isanswer","true")
				if($(this).attr("data-serial")==$(this).parent("ul").attr("trueanswer")){
						$(this).addClass("true");
						score+=10;
						$(".score").html(score+"分")
				}else{
					$(this).addClass("error");
				}
				if(i==0){
						$(".legend p").show();
				}
				i++;
				if(i<10){
					setTimeout(function(){
						$(".answerBox").eq(i).show().siblings().hide();
					},1500)
				}
				if(i==10){
					var data=_self.saveUserAnswer(score)
					if(score>=60){
						$(".cgsuccess").show();
						$("#currentScore").html(score+"分");
						$("#maxChange").html(data.changeNum);
						$("#maxScore").html(data.maxScore)
					}else{
						$(".cgerror").show();
						$(".cgerror .score").text(score+"分");
					}
					i=0;
				}
			}
		})
	},
    assemble:function(){   //组件答题题目
    	$(".answerGather").html("");
    	var html="";
    	$.each(answerData, function(i,e) {
			if(i==0){
			    html+='<div class="answerBox" isAnswer="false">'
			}else{
			    html+='<div class="answerBox" isAnswer="false" style="display:none">'
			}
            html+='<div class="serize"><span>'+e.n+'</span>/10</div>'+
	         	 	 '<h2>'+e.q+'</h2>'
			if(i==0){
				html+='<div class="legend">'+
			           '<p style="display: none;"><span>回答正确</span> <span>回答错误</span></p>'+
			           '</div>';
			}else{
				html+='<div class="legend">'+   
				     '</div>';
			}
			html+='<ul trueAnswer='+e.trueAnswer+' class="answerList">';
		    $.each(e.a, function(i,e) {
		        	html+='<li data-serial="'+e.serial+'">'+e.t+'</li>'
		    });
			html+='</ul></div>';
		});
		$(".answerGather").append(html);
    },
    saveUserAnswer:function(score){  //保存用户答题信息
    	var userData={};
		$.ajax({
			async:false,
			url:config.service.userAnswer,
			type:"post",
			data:{
				"openId":openId,
				"score":score
			},
			dataType:"json",
			success:function(res){
				if(res.returnCode=="0"){
					 userData.maxScore=res.data.maxScore;
					 userData.changeNum=res.data.changeNum;
				}
			}
		})
		return userData
	},
    queryMyPrize:function(){ //查询我的奖品
    	var _self=this;
    	$.ajax({
			url:config.service.firstJoinPage,
			type:"post",
			data:{"openId":openId},
			dataType:"json",
			success:function(res){
					if(res.data.prizeInfo.length>0&&res.data.prizeInfo){
						var data=res.data.prizeInfo;
						var prize=res.data.userInfo;
						var html="";
						$(".hasPrize").show();
						$(".noPrize").hide();
						if(prize.username!=""||prize.userTel!=""){
							$(".userInfo").show();
							$("#UName").text(prize.username);
							$("#UTel").text(prize.userTel);
							userName=prize.username  //用户信息
                            userTel=prize.userTel   //用户
						}
						_self.prizeListAssemble(data);
						
						
					}else{
						$(".hasPrize").hide();
						$(".noPrize").show();
					}
				
			}
		})
    },
    prizeListAssemble: function(data) {  //组装我的奖品列表
    	$(".hasPrize").html("");
    	$.each(data, function(i, e) {
    		switch(e.status) {
    			case "1": //已经兑奖
    				html += '<li class="status status2 dswkb">' +
    					'<div class="money">' +
    					'<div><em>￥</em>' + e.prizeMoney + '</div>' +
    					'<div>现金券</div>' +
    					'</div>' +
    					'<div class="offerIn">' +
    					'<p>恭喜您中奖啦！</p>' +
    					'<p>' + e.dateTimeStart + '至' + e.dateTimeEnd + '使用</p>' +
    					'</div>' +
    					'<div class="offerStatus"></div>' +
    					'</li>';
    				break;
    			case "2": //未兑奖
    				html += '<li class="status status1 dswkb">' +
    					'<div class="money">' +
    					'<div><em>￥</em>100</div>' +
    					'<div>现金券</div>' +
    					'</div>' +
    					'<div class="offerIn">' +
    					'<p>恭喜您中奖啦！</p>' +
    					'<p>' + e.dateTimeStart + '至' + e.dateTimeEnd + '使用</p>' +
    					'</div>' +
    					'<div class="offerStatus" data-id="' + e.prizeId + '"> 去兑换</div>' +
    					'</li>';
    				break;
    			case "3": //已过期
    				html += '<li class="status status3 dswkb">' +
    					'<div class="money">' +
    					'<div><em>￥</em>' + e.prizeMoney + '</div>' +
    					'<div>现金券</div>' +
    					'</div>' +
    					'<div class="offerIn">' +
    					'<p>恭喜您中奖啦！</p>' +
    					'<p>' + e.dateTimeStart + '至' + e.dateTimeEnd + '使用</p>' +
    					'</div>' +
    					'<div class="offerStatus"></div>' +
    					'</li>';
    				break;
    			default:
    				break;
    		}
    	})
    	$(".hasPrize").append(html);
    },
    userDateSumbit:function(prizeId){   //当有用户信息资料的时候，不显示弹窗直接兑奖
    		$.ajax({
			async : false,
			url:config.service.userGetAward,
			type : "post",
			dataType : 'json',
			data : {
				"userName":userName,
				"userTel":userTel,
				"prizeId":prizeId,
				"openId":openId
			},
			success : function(data) {
				if(data.returnCode=="0"){
                     $(".reservesSuc").show();
				}else{
					alert("系统异常")
				}
			},
			error : function() {
                alert("系统异常");
			}
		});
    }
    
}


var operation={
	btnAnsw:function(){
		$(".bg1").addClass("filter")
		$(".answer").show();
		$(".opeartion .btn").hide();
		index.answer();
	},
	showBag:function(){
		$(".explain").show();
		index.queryMyPrize();
	},
	iKnow:function(){
		$(".silk").hide();
	},
	goDraw:function(){
		score=0;
        this.sucContinueDraw()
	},
	closeExplain:function(){
		$(".explain").hide();
	},
	eleBind:{
		tabChange:function(){  //切换选项卡
		 	$(".tabChange li").on("click",function(){
		 		$(this).addClass("current").siblings().removeClass("current");
		 		var eq=$(this).index();
		 		$(".tabContent>div").eq(eq).show().siblings().hide();
		 	})
		},
		cashPrize:function(){ //去兑换
			$(document).on("click",".status1 .offerStatus",function(){
				prizeId=$(this).attr("data-id");
				if(userName==""||userTel==""){
					$(".reserves").show();
				}else{
					index.userDateSumbit(prizeId)
				}
				
			})
		}
	},
	againAnswer:function(){
		$(".cgerror").hide();
		$(".answerList li").unbind("click");
		$(".answer").show();
	    index.answer();
	    score=0;
	    $(".score").html("0分")
	},
	goIndex:function(){
		$(".answer,.cgsuccess,.cgerror,.winPrize").hide();
		$(".bg1").removeClass("filter");
		$(".opeartion .btn").show();
	},
	sucContinueDraw:function(){  //用户点击继续抽奖
		$.ajax({
					async : false,
					url:config.service.checkUserIsCanLuckDraw,
					type:"post",
					data:{
						"openId":openId,
					},
					dataType:"json",
					success:function(res){
						if(res.returnCode=="0"){
							$(".winSuc").hide();
							if(res.luckDrawStatus=="0"){
								$(".infoTips").show();
							}else if(res.isLuckDraw=="1"){
								$(".share2").show();
							}else if(res.isLuckDraw=="2"){
								$(".draw").show();
								$(".cgsuccess,.answer").hide();
							}
						}
					}
					
		})
	},
	tipsIknow:function(){  //用户点击信息提示知道了
		$(".infoTips").hide();
	},
	btnDraw:function(){  //首页直接点击抽奖
		this.sucContinueDraw()
	},
	seeMyPrizeList:function(){  //抽中奖品后查看我的奖品
		$(".explain").show();
		$(".tabChange li").eq(1).addClass("current").siblings().removeClass("current");
		$(".tabContent>div").eq(1).show().siblings().hide();
		$(".winSuc").hide();
		index.queryMyPrize();
	}
//	userLuckDraw:function(){   //模拟用户摇奖,需要删除
//		$(".draw .content").click(function(){
//			$.ajax({
//					async : false,
//					url:"",
//					type:"get",
//					data:{
//						"openId":openId,
//					},
//					dataType:"json",
//					success:function(res){
//						if(res.returnCode=="0"){
//							$(".draw").hide();
//							if(res.prizeLevel!="0"){
//			                    $(".winSuc").show();
//							}else{
//								$(".winErr").show();
//							}
//						}
//					}
//			})
//			 $(".draw").hide();
//			 $(".winSuc").show();
//		})
//	}
}

initPage()
function initPage(){
	operation.eleBind.tabChange();
	operation.eleBind.cashPrize();
	//operation.userLuckDraw();
	index.init();
}

