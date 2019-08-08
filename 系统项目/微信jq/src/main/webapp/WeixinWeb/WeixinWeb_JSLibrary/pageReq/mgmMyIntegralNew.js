var title="";  //标题
var desc="";  //描述
var host = window.location.protocol + "//" + window.location.host;
var link=host+"/#/register";
var imgUrl=host+"/WeixinWeb/WeixinWeb_Images/H5modules/invitation/share.jpg"  //转发图标
var custName="";
var isRealName; //是否实名
var theUsermobile; //用户自己的手机号
var invitationCode="" //邀请码

$(function(){
	queryUserToRealName()
    queryHotInvent();
    eventBind()
	
	if(isRealName=="true"){
		queryUserDeatail()
	}
	if($("#scoreNum").html()=="推荐朋友"){
		$("#scoreNum").css("font-size","35px")
	}
})

//  返回上一级页面
function goback(){
    window.history.go(-1)
}

//查询热门推荐的产品信息列表
function queryHotInvent(){
    $.ajax({
        url:'/WeixinService/business/integral/exhibitionFund.xhtml',
        data:{},
        dataType:'json',//服务器返回json格式数据
        contentType:'application/json;charset=utf-8',
        type:'get',//HTTP请求类型
        success:function(data){
			if(data.errcode=="0000"){
				//动态加载兑换品列表
				var html="";
				if(data.fundList.length>0){
					$.each(data.fundList,function(i,e){
						if(e.typeName=='权益投资'){
		
							var latestNewValue=e.latestNewValue?e.latestNewValue+"%":"1.0000"
							html+= '<div class="product1">'+
								'<span class="sevenDays">'+e.adname +'</span><span class="shareProduct" onclick="shareProduct(\''+e.fundId+'\',\''+e.period+'\')"></span>'+
								'<ul><li><p class="red_standard">'+latestNewValue+'</p><p>最新净值</p></li>'+
								'<li><p class="blackNum">'+ e.money/10000+'万'+'</p><p>理财起点</p></li>'+
								'<li><p class="blackNum">'+e.moneyStep/10000+'万'+'</p><p>追加金额</p></li>'+
								'</ul></div>';
						}else if(e.typeName=='定期理财'){
							var profit=e.profit?parseFloat(((e.profit)*100)).toFixed(2)+"%":"0.00%"
							html+= '<div class="product1">'+
								'<span class="sevenDays">'+e.adname +'</span><span class="shareProduct" onclick="shareProduct(\''+e.fundId+'\',\''+e.period+'\')"></span>'+
								'<ul><li><p class="red_standard">'+profit+'</p><p>业绩报酬计提基准</p></li>'+
								'<li><p class="blackNum">'+ e.term+e.termUnit+'</p><p>理财期限</p></li>'+
								'<li><p class="blackNum">'+e.money/10000+'万'+'</p><p>理财起点</p></li>'+
								'</ul></div>';
						}else if(e.typeName=='活期理财'){
							var benefitSinceCreated=e.benefitSinceCreated?parseFloat((e.benefitSinceCreated)).toFixed(2)+"%":"--"
							html+= '<div class="product1">'+
							'<span class="sevenDays">'+e.adname +'</span><span class="shareProduct" onclick="shareProduct(\''+e.fundId+'\',\''+e.period+'\')"></span>'+
							'<ul><li><p class="red_standard">'+benefitSinceCreated+'</p><p>成立以来年化收益率</p></li>'+
							'<li><p class="blackNum">'+ e.money/10000+'万'+'</p><p>起购金额</p></li>'+
							'<li><p class="blackNum">'+e.moneyStep/10000+'万'+'</p><p>追加金额</p></li>'+
							'</ul></div>';
						}else if(e.typeName=='另类理财'){
							html+= '<div class="product1">'+
								'<span class="sevenDays">'+e.adname +'</span><span class="shareProduct" onclick="shareProduct(\''+e.fundId+'\',\''+e.period+'\')"></span>'+
								'<ul><li><p class="red_standard">浮动收益</p><p>业绩报酬计提基准</p></li>'+
								'<li><p class="blackNum">'+ e.term+e.termUnit+'</p><p>理财期限</p></li>'+
								'<li><p class="blackNum">'+e.money/10000+'万'+'</p><p>理财起点</p></li>'+
								'</ul></div>';
						}
		
					});
				}else{
					html="<p style='font-size: 17px;text-align: center;height: 45px;line-height: 45px;'>暂无推荐产品，敬请期待</p>"
				}

				$('#hotInvent').after(html);
			}   
        },
        error:function(xhr,type,errorThrown){

        }
    });

}

//跳转页面
function redirectUrl(url) {
    window.location.href = url;
}




//分享平台
function platform(){
	$("#mask").show()
}
function eventBind(){
	$("#mask").click(function(){
		$("#mask").hide()
	})
	$("#closeAct,#knowActBtn").click(function(){
		$('.backLayer').hide()
		$('#actRulesBox').hide()
	})
	
	
	$('.firstLookBtn').click(function(){
	 $('.backLayer').hide()
	 $('.realNamePop').hide()
	})
	
	// 实名跳转
	$('.toRealBtn').click(function(){
		window.location.href='/#/cardinfo';
	})
	
	// 推荐手机号弹框
	$('#myInventPhoneBtn').click(function(){
		if(isRealName=='false'){
		    $("#realNamePop,.backLayer").show();
		}else{
			$('#usePhonePop').show()
			$('.backLayer').show()
		}
	})
	
	// 点击使用积分按钮
	$('#useButton').click(function(){
		 var isRealName=$('#isRealName').val();
		if(isRealName=='true'){
			window.location.href='/WeixinService/business/mgm/useMyIntegralNew.shtml';
		}else if(isRealName=='false'){
			$('#realNamePop').show()
			$('.backLayer').show()
		}
	})
	
	//  积分使用记录按钮
	$('#toUserRecordBtn').click(function(){
		var isRealName=$('#isRealName').val();
		if(isRealName=='true'){
			window.location.href='/WeixinService/business/mgm/integralRecordNew.shtml';
		}else if(isRealName=='false'){
			$('#realNamePop').show()
			$('.backLayer').show()
		}
	})
	// 我的顾问按钮
	$('#myRecommendBtn').click(function(){
		var isRealName=$('#isRealName').val();
		if(isRealName=='true'){
			window.location.href='/#/recommend';
		}else if(isRealName=='false'){
			$('#realNamePop').show()
			$('.backLayer').show()
		}
	})
	$("#closeTel").click(function (e) { 
		$("#usePhonePop,.backLayer").hide()
	});
}

function queryIntegralDeatail(){
	link=host+"/?#/register?invitation="+invitationCode;   //转发链接
	title=custName+"向您推荐招商财富";  //标题
        desc="招商体系，值得信赖";  //描述
 }
/**
 * 分享产品
 */
function shareProduct(typeId,period){
   localStorage.setItem("mgmFundId",typeId)
   localStorage.setItem("mgmPeriod",period)  
   location.href="/#/transitionPage?fundId="+typeId+"&period="+period+"&isShare=Invitation"
}

//  查询积分详情信息
function queryUserDeatail() {
	
		$.ajax({
		    url: '/WeixinService/business/integral/getIntegralInfo.xhtml',
		    data: {},
		    dataType: 'json', //服务器返回json格式数据
		    type: 'get', //HTTP请求类型
		    success: function(data) {
				invitationCode=data.invitationCode;
				queryIntegralDeatail();
				if(data.integral<=0){
					 $('#scoreNum').html('推荐朋友').css("font-size","35px")
				}else{
				   $('#scoreNum').html(data.integral)
				}
				theUsermobile=data.mobile;
				$('#userPhone').html(data.mobile)
		    },
		    error: function(xhr, type, errorThrown) {
		
		    }
		})
}

function showActrRule(){
	//  是否要查询积分规则
	$('.backLayer').show()
	$('#actRulesBox').show()
}


// 查询用户信息 【判断是否实名】
function queryUserToRealName() {
 var resultData = {};
	$.ajax({
    	async:false,
        url: "/WeixinService/business/queryUserinfo.xhtml",
        data: "",
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
        	errorRemark("网络繁忙，请稍后再试。");  
        }, 
        success : function (data){
        	if(data.userType!=30||data.userType!='30'){
				isRealName = 'false';
				$('#realNamePop').show()
				$('.backLayer').show()
				$('#scoreNum').html('推荐朋友')
				$('#isRealName').val('false');
        	}else if(data.userType == "30"||data.userType == 30){
				isRealName='true';
				custName=data.custName;
				$('#isRealName').val('true');
			}
			
        }
    });

}


