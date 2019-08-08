var  userId=getUrlSearchParams("userid") ? getUrlSearchParams("userid") : decodeURI(getCookie("userId")) ;
addCookie("userId", userId);
$(function () {
	index.pageEventBind()
    index.initPageData()
})
var index={
    initPageData:function(){
        if (userId==null||userId=="") {
           location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host + "/WeixinService/H5modules/H5Content/yearActivity/index.html"
        }else{
        	this.isPrizeOut()
	        this.isLottery();  
	        this.isActivityEnd()
        }
    },
    /**
     * 页面事件绑定
     */
    pageEventBind:function(){
    	$(".rules").click(function(){
        	$(".rulesText").show()
        })
        $(".rulesMask .close").click(function(){
        	$(".rulesText").hide()
        })
        $(".outMask  .close").click(function(){
        	$(".outMask").hide()
        })
        var srcDev="/WeixinWeb/WeixinWeb_Images/H5modules/yearActivity/wxtestQrcode-unlimit.jpg";
        var srcPrd="/WeixinWeb/WeixinWeb_Images/H5modules/yearActivity/wxprodQrcode-unlimit.jpg";
        if(host.indexOf("wxtest1")>-1){
        	$("#codeImg").attr("src",srcDev)
        }else{
        	$("#codeImg").attr("src",srcPrd)
        }
    },
    /**
     * 判断用户是否授权
     */
    isUeserAuth:function(msg){
    	 var _self=this;
    	 $.ajax({
            url:config.service.isHighAuth,
            type:"post",
            async:false,
            dataType:'json',
            data : {
                "userId":userId
            },
            success:function(data) {
                if (data.returnCode=="0"){
                	if(!data.data.flag){
                		localStorage.setItem("sendMsg","true")
                		var baseInfo=host + "/auth/proxy.html?scope=snsapi_userinfo&target_url=" + host + "/WeixinService/H5modules/H5Content/yearActivity/index.html"
                	    location.href = baseInfo;
                	    return;
                	}
                	if(msg=="1"||localStorage.getItem("sendMsg")=="true"){
                		 _self.sendTemplateMsg()
                		 localStorage.removeItem("sendMsg")
	                     setTimeout(function(){
	                     	wx.closeWindow();
	                     },500)
                	}

                }
            }
    	}) 
    },
    /**
     * 判断活动是否结束
     */
    isActivityEnd:function(){
        var data = queryParamList("SYSTEM", "ACTIVITY_DT", "END_TIME"); //活动结束时间
        try {
            if (data.data.length > 0) {
                var plan = data.data[0].pmnm;
                var beginTime = this.getNowFormatDate() //当前时间
                var endTime = plan.substring(0, 4) + "/" + plan.substring(4, 6) + "/" + plan.substring(6, 8)+" "+plan.substring(8, 10)+":"+plan.substring(10, 12)+":"+plan.substring(12, 14)
			    var dateDiff =(Date.parse(endTime)-Date.parse(beginTime))/3600/1000;
                if (dateDiff >0) {
                    return true;
                } else {
                	$(".timeOut,.prizesucc").show();
                	$(".rules,.prizeBox p,.prizeBox h2,.prizeBox .money").remove()
                	$(".prizeBox").addClass("endlogo");
                	$("body").addClass("endBody");
                	$(".prizesucc").addClass("end")
                	$(".prizeMask,.redPack").hide();
                	$(".titH2").remove()
                	var custserName=$("#custserName").text()
                	if(custserName==""){
	                    $("#custserName").text("程婷")
	                    $("#custserMobile").text("13620972138")
	                    $("#custserMobileTel").attr("href","tel:13620972138")
                	}
                	
                    return false;
                }
            }
        }
        catch (e) {
            console.log(e)
        }
    },
    /**
     * 判断券是否用完
     */
    isPrizeOut:function(){
        var data = queryParamListNum("SYSTEM", "YMH_AWARD", ""); //活动结束时间
        var flag=true
        if(data.data.num<=0){
            $(".offerOut").show();
           flag=false
        }
        return flag;
    },
    /**
     * 判断用户是否已经抽奖
     */
    isLottery:function(){
        var _self=this
        $.ajax({
            url:config.service.isLottery,
            type:"post",
            async:false,
            dataType:'json',
            data : {
                "userId":userId,
                "activityId":activityId
            },
            success:function(data) {
                if (data.returnCode=="0"){
                    if(data.data.state=="1"){
                        _self.isUeserAuth("2");
                        $(".redPack").hide()
                        $(".prizesucc,.rules").show()
                        _self.queryAllCustService()
                        _self.queryUserPrize()
                        _self.initEndAct()
                    }else{
                        $(".redPack").show()
                        $(".prizesucc").hide()
                        _self.initEndAct()
                    }
                }else if(data.returnCode=="1"){  //解密失败重新授权
                    location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host + "/WeixinService/H5modules/H5Content/yearActivity/index.html"
                }else{
                	$(document).dialog({type : 'notice',infoText: data.returnMsg,autoClose: 1500,position: 'center'});
                }
            },
            error:function () {
                $(document).dialog({type : 'notice',infoText: '服务器异常，请刷新页面',autoClose: 1500,position: 'center'});
            }
        })
    },
    /**
     * 用户抽奖方法
     */
    openPrize:function () {
    	
    	var flag=this.isActivityEnd()
    	if(flag){
    		var flag2=this.isPrizeOut()
    		if(flag2){
	    			var toast1=$(document).dialog({
		            type : 'toast',
		            infoIcon: '/WeixinWeb/WeixinWeb_Images/H5modules/yearActivity/loading.gif',
		            infoText: '抽奖中'
		        });
		        $.ajax({
		            url:config.service.lotteryLogic,
		            type:"post",
		            async:false,
		            dataType:'json',
		            data : {
		                "userId":userId,
		                "activityId":activityId
		            },
		            success:function(data) {
		                toast1.close()
		                if(data.returnCode=="0"){
		                    var prize=data.data.award.awardPrice;
		                    if(prize=="10000"){
		                        $(".prizeMask").show()
		                        $(".prizeMask .money").addClass("mon1")
		                    }else if(prize=="50000"){
		                        $(".prizeMask").show()
		                        $(".prizeMask .money").addClass("mon2")
		                    }else if(prize=="100000"){
		                        $(".prizeMask").show()
		                        $(".prizeMask .money").addClass("mon3")
		                    }
		                }else if(data.returnCode=="1"){
		                	$(".qrcodeMask").show()
		//                  $(document).dialog({type : 'notice',infoText: '请关注招商财富公众号',autoClose: 1500,position: 'center'});
		                }else if(data.returnCode=="2"){
		                    $(document).dialog({type : 'notice',infoText: '您已经抽过奖',autoClose: 1500,position: 'center'});
		                }else if(data.returnCode=="3"){
		                    $(document).dialog({type : 'notice',infoText: '活动太火爆，奖品已抽完！',autoClose: 1500,position: 'center'});
		                }else{
		                    $(document).dialog({type : 'notice',infoText: '服务器异常，请稍后再试',autoClose: 1500,position: 'center'});
		                }
		            },
		            error:function () {
		                toast1.close()
		                $(document).dialog({type : 'notice',infoText: '服务器异常，请稍后再试',autoClose: 1500,position: 'center'});
		            }
		        })
    		}else{
    			$(".offerOut").show()
    		}
	    	
    	}else{
    		$(".timeOut").show()
    	}
        
    },
    /**
     * 查询客户经理顾问
     */
    queryAllCustService:function () {
        $.ajax({
            url:config.service.queryAllCustService,
            type:"post",
            async:false,
            dataType:'json',
            data : {
                "userId":userId,
                "activityId":activityId
            },
            success:function(data) {
                if(data.returnCode=="0"){
                    $("#custserName").text(data.data.object.custserName)
                    $("#custserMobile").text(data.data.object.custserMobile)
                    $("#custserMobileTel").attr("href","tel:"+data.data.object.custserMobile)
                }else{
                    $(document).dialog({type : 'notice',infoText: '服务器异常，请稍后再试',autoClose: 1500,position: 'center'});
                }
            },
            error:function () {
                $(document).dialog({type : 'notice',infoText: '服务器异常，请稍后再试',autoClose: 1500,position: 'center'});
            }
        })
    },
    /**
     * 查询用户奖品
     */
    queryUserPrize:function () {
        $.ajax({
            url:config.service.queryUserAward,
            type:"post",
            async:false,
            dataType:'json',
            data : {
                "userId":userId,
                "activityId":activityId
            },
            success:function(data) {
                if(data.returnCode=="0"){
                    var awardPrice=outputdollars(data.data[0].awardPrice);
                    $("#prizeMoney").text(awardPrice)
                }else{
                    $(document).dialog({type : 'notice',infoText: '服务器异常，请稍后再试',autoClose: 1500,position: 'center'});
                }
            },
            error:function () {
                $(document).dialog({type : 'notice',infoText: '服务器异常，请稍后再试',autoClose: 1500,position: 'center'});
            }
        })
    },
    /**
     * 发送模板消息
     */
    sendTemplateMsg:function () {
        $.ajax({
            url:config.service.sendTemplateMsg,
            type:"post",
            async:false,
            dataType:'json',
            data : {
                "userId":userId,
                "activityId":activityId
            },
            success:function(data) {
                
            }
           
        })
    },
    initEndAct:function(){
    	if(!this.isPrizeOut()){
            $(".redPack").hide()
            $(".prizesucc").show()
            $(".rules,.prizeBox p,.prizeBox h2,.prizeBox .money").remove()
	    	$(".prizeBox").addClass("endlogo");
	    	$("body").addClass("endBody");
	    	$(".prizesucc").addClass("end")
	    	$(".prizeMask,.redPack").hide()
	    	$(".titH2").remove()
	    	var custserName=$("#custserName").text()
	    	if(custserName==""){
	            $("#custserName").text("程婷")
	            $("#custserMobile").text("13620972138")
	            $("#custserMobileTel").attr("href","tel:13620972138")
	    	}
        }
    },
    /**
     * 用户确认
     */
    userSure:function () {
        $(".prizeMask").hide();
        this.isUeserAuth("1");
    },
    closeQrcode:function(){
    	$(".qrcodeMask").hide()
    },
    /**
     * 获取当前系统时间
     */
    getNowFormatDate:function(){
    	var date = new Date();
		var seperator1 = "/";
		var seperator2 = ":";
		var month = date.getMonth() + 1;
		var strDate = date.getDate();
		var getHours=date.getHours()
		var getMinutes=date.getMinutes()
		var getSeconds=date.getSeconds()
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		if (getHours >= 1 && getHours <= 9) {
			getHours = "0" + getHours;
		}
		if (getMinutes >= 0 && getMinutes <= 9) {
			getMinutes = "0" + getMinutes;
		}
		if (getSeconds >= 0 && getSeconds <= 9) {
			getSeconds = "0" + getSeconds;
		}
		
		var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
				+ " " + getHours + seperator2 + getMinutes
				+ seperator2 + getSeconds;
		return currentdate;
    }
}
