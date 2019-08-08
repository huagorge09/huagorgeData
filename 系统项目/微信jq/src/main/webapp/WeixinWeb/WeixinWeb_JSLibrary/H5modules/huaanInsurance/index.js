var sessionID="";
$(function(){
	var huaanSwitch = common.queryParamList("SYSTEM","huaanActSwitch","")[0];
	if(huaanSwitch != undefined && huaanSwitch.pmco == "OFF"){
		location.href="/WeixinService/weixinLogin/register.shtml";
		return;
	}
	index.initPage();	
})

var index={
	initPage:function(){  //初始化页面
		this.pageAuthority() //页面签名验证
		this.queryDrainageUserInfo() //查询是否注册接口
		this.pageAction(); //页面行为事件绑定
	},
	formValid:function(){  //表单为空增加状态
		var userName=$("input[name=userName]").val();
		var telPhone=$("input[name=telPhone]").val();
		var passWord=$("input[name=passWord]").val();
		var repassWord=$("input[name=repassWord]").val();
		var captcha=$("input[name=captcha]").val();
		if(userName!=""&&telPhone!=""&&passWord!=""&&repassWord!=""&&captcha!=""){
			$("#getBtn").addClass("getBtn");
			$("#getBtn").attr("href","javascript:index.submitDate()")
		}else{
			$("#getBtn").removeClass("getBtn")
			$("#getBtn").attr("href","javascript:;")
		}
	},
	submitDate:function(){  //数据表单提交
		var userName=$("input[name=userName]").val();
		var telPhone=$("input[name=telPhone]").val();
		var passWord=$("input[name=passWord]").val();
		var repassWord=$("input[name=repassWord]").val();
		var captcha=$("input[name=captcha]").val();
		var appId=common.getUrlSearchParams("appId")
		var plateNo=common.getUrlSearchParams("plateNo")
		var idCard=common.getUrlSearchParams("idCard")
		var ownerName=common.getUrlSearchParams("ownerName")
		var brandName=common.getUrlSearchParams("brandName")
		var purchasePrice =common.getUrlSearchParams("purchasePrice")
		var sinoUserId=common.getUrlSearchParams("haUserId")
		var timestamp=common.getUrlSearchParams("timestamp")
		var sign=common.getUrlSearchParams("sign")
		var province=common.getUrlSearchParams("province")
		var _this=this;
		if(!common.isMobile(telPhone)){
			$(document).dialog({type : 'notice',infoText: '手机号码格式不正确',autoClose: 1500,position: 'center'});
		}else if(!common.passWord(passWord)){
			$(document).dialog({type : 'notice',infoText: '密码长度不正确',autoClose: 1500,position: 'center'});
		}else if(passWord!=repassWord){
			$(document).dialog({type : 'notice',infoText: '两次密码不一致',autoClose: 1500,position: 'center'});
		}else{
			var flag=$(".check i").hasClass("no")
			if(!flag){
				var toast=$(document).dialog({
			        type : 'toast',
			        infoIcon: '/WeixinWeb/WeixinWeb_Images/H5modules/huaanInsurance/loading.gif',
			        infoText: '注册中'
			    });
				$.ajax({
					async:true,
					url : "/WeixinService/setUp/registerWithDrainage.xhtml",
					data : {
						"userName":userName,
						"sessionID":sessionID,
						"mobile":telPhone,
						"passWord":passWord,
						"smsCode":captcha,
						"appId": appId,
			        	"plateNo":plateNo,
			        	"idCard":idCard,
			        	"ownerName":ownerName,
			        	"province":province,
			        	"brandName":brandName,
			        	"purchasePrice":purchasePrice,
			        	"sinoUserId":sinoUserId,
			        	"timestamp":timestamp,
			        	"sign":sign
					},
					dataType : "json",
					type:"POST",
					success : function(data){
						if(data.returnCode=="0000"){
							localStorage.setItem("mobile",telPhone);
							location.href="realName.html"
						}else if(data.returnCode=="9998"){
							$(document).dialog({type : 'notice',infoText: '请点击获取验证码',autoClose: 1500,position: 'center'});
						}else if(data.returnCode=="9997"){
							$(document).dialog({type : 'notice',infoText: '注册手机号码与短信验证码不一致',autoClose: 1500,position: 'center'});
						}else if(data.returnCode=="9996"){
							$(document).dialog({type : 'notice',infoText: '短信验证码验证失败',autoClose: 1500,position: 'center'});
						}else if(data.returnCode=="USR-A015"){
                            $(document).dialog({type : 'notice',infoText: '手机号码已被注册',autoClose: 1500,position: 'center'});
						}else if(data.returnCode=="9995"){
                            $(document).dialog({type : 'notice',infoText:data.returnMsg,autoClose: 1500,position: 'center'});
						}else{
							$(document).dialog({type : 'notice',infoText: data.returnMsg,autoClose: 1500,position: 'center'});
						}
						toast.close()
					},
					error:function(){
						$(document).dialog({type : 'notice',infoText: '服务器异常，请稍后再试',autoClose: 1500,position: 'center'});
					}
				});
			}else{
				$(document).dialog({type : 'notice',infoText: '请同意协议条款',autoClose: 1500,position: 'center'});
			}
		}
	},
	pageAction:function(){  //页面行为事件
		var _this=this;
		if(common.getUrlSearchParams("appId")!=""||common.getUrlSearchParams("plateNo")!=""){
			localStorage.setItem("APPID",common.getUrlSearchParams("appId"))
			localStorage.setItem("PLATENO",common.getUrlSearchParams("plateNo"))
			localStorage.setItem("IDCARD",common.getUrlSearchParams("idCard"))
			localStorage.setItem("OWNERNAME",common.getUrlSearchParams("ownerName"))
			localStorage.setItem("BRANDNAME",common.getUrlSearchParams("brandName"))
			localStorage.setItem("PURCHASWPRICE",common.getUrlSearchParams("purchasePrice"))
			localStorage.setItem("HAUSERID",common.getUrlSearchParams("haUserId"))
			localStorage.setItem("TIMESTAMP",common.getUrlSearchParams("timestamp"))
			localStorage.setItem("SIGN",common.getUrlSearchParams("sign"))
			localStorage.setItem("province",common.getUrlSearchParams("province"))
			$("input[name=userName]").val(common.getUrlSearchParams("ownerName"))
		}
		$(document).on("input","input",function(){
			if(_this.formValid()){
				$("#getBtn").addClass("getBtn");
			}
		})
		$("#getCaptcha").on("click",function(){
			if(!$(this).hasClass("disabled")){
				if($("input[name=telPhone]").val()==""){
					$(document).dialog({type : 'notice',infoText: '请输入手机号码',autoClose: 1500,position: 'center'});
				}else if(!common.isMobile($("input[name=telPhone]").val())){
					$(document).dialog({type : 'notice',infoText: '手机号码格式不正确',autoClose: 1500,position: 'center'});
				}else{
					_this.getCaptcha()
				}
			}
		})
		$(".check i").on("click",function(){
			$(this).hasClass("no")?$(this).removeClass("no"):$(this).addClass("no")
		})
		$(".check em").on("click",function(){
			location.href="protocol.html"
		})
	},
	getCaptcha:function(){  //获取验证码
		var toast=$(document).dialog({
	        type : 'notice',
	        content: '<img class="info-icon" src="/WeixinWeb/WeixinWeb_Images/H5modules/huaanInsurance/loading.gif" alt="" /><span class="info-text">正在发送中</span>',
	    });
		$.ajax({
			type:"post",
			async:false,
	        url: "/WeixinService/setUp/getMobileVerifyCodeForHABX.xhtml",
	        data: {"mobile": $("input[name=telPhone]").val()},
			success:function(res){
				toast.close();
				var data=JSON.parse(res)
				if(data.returnCode=="0000"){
					sessionID=data.sessionID;
					$(document).dialog({type : 'notice',infoText: '验证码发送成功',autoClose: 1500,position: 'center'});
					common.countDown({
				   	  elem:"#getCaptcha",
				   	  disable:"disabled",
				   	  time:60
				   })
				}else{
					$(document).dialog({type : 'notice',infoText: '验证码发送失败，请稍后再试',autoClose: 1500, position: 'center' });
				}
			},
			error:function(){
			    $(document).dialog({type : 'notice',infoText: '服务器异常',autoClose: 1500, position: 'center' });
			}
		});
	},
	pageAuthority:function(){  //页面权限验证
		
		var appId=common.getUrlSearchParams("appId")?common.getUrlSearchParams("appId"):localStorage.getItem("APPID")
		var plateNo=common.getUrlSearchParams("plateNo")?common.getUrlSearchParams("plateNo"):localStorage.getItem("PLATENO")
		var idCard=common.getUrlSearchParams("idCard")?common.getUrlSearchParams("idCard"):localStorage.getItem("IDCARD")
		var ownerName=common.getUrlSearchParams("ownerName")?common.getUrlSearchParams("ownerName"):localStorage.getItem("OWNERNAME")
		var brandName=common.getUrlSearchParams("brandName")?common.getUrlSearchParams("brandName"):localStorage.getItem("BRANDNAME")
		var purchasePrice=common.getUrlSearchParams("purchasePrice")?common.getUrlSearchParams("purchasePrice"):localStorage.getItem("PURCHASEPRICE")
		var sinoUserId=common.getUrlSearchParams("haUserId")?common.getUrlSearchParams("haUserId"):localStorage.getItem("HAUSERID")
		var timestamp=common.getUrlSearchParams("timestamp")?common.getUrlSearchParams("timestamp"):localStorage.getItem("TIMESTAMP")
		var sign=common.getUrlSearchParams("sign")?common.getUrlSearchParams("sign"):localStorage.getItem("SIGN")
		var province=common.getUrlSearchParams("province")?common.getUrlSearchParams("province"):localStorage.getItem("province")
		if(!appId||!plateNo||!idCard||!ownerName||!brandName||!purchasePrice||!sinoUserId||!timestamp||!sign||!province){
			  $(document).dialog({type : 'notice',infoText: '该用户不具有参与活动的资格',autoClose: 1500,position: 'center'});
		   	  setTimeout(function(){
		   	 	  location.href="/WeixinService/weixinLogin/register.shtml"
		   	  },2000)
		}else{
			$.ajax({
				type:"post",
				async:false,
		        url: "/WeixinService/setUp/verifySign.xhtml",
		        data: {
		        	"appId":decodeURI(decodeURI(appId)),
		        	"plateNo":decodeURI(decodeURI(plateNo)),
		        	"idCard":decodeURI(decodeURI(idCard)),
		        	"ownerName":decodeURI(decodeURI(ownerName)),
		        	"brandName":decodeURI(decodeURI(brandName)),
		        	"purchasePrice":decodeURI(decodeURI(purchasePrice)),
		        	"sinoUserId":decodeURI(decodeURI(sinoUserId)),
		        	"timestamp":decodeURI(decodeURI(timestamp)),
		        	"sign":decodeURI(decodeURI(sign))
		        },
				success:function(res){
				   if(res=="false"){
				   	  $(document).dialog({type : 'notice',infoText: '该用户不具有参与活动的资格',autoClose: 1500,position: 'center'});
				   	  setTimeout(function(){
				   	 	  location.href="/WeixinService/weixinLogin/register.shtml"
				   	  },2000)
				   }
				},
				error:function(){
				    $(document).dialog({type : 'notice',infoText: '服务器异常,请刷新页面再试',autoClose: 1500, position: 'center' });
				}
			});
		}
	},
	queryDrainageUserInfo:function(){ //判断用户是否注册跟实名
		var idCard=common.getUrlSearchParams("idCard")?common.getUrlSearchParams("idCard"):localStorage.getItem("IDCARD")
		var ownerName=common.getUrlSearchParams("ownerName")?common.getUrlSearchParams("ownerName"):localStorage.getItem("OWNERNAME")
		if(idCard||ownerName){
			$.ajax({
				type:"post",
				async:false,
				dataType:"json",
		        url: "/WeixinService/setUp/queryDrainageUserActivityPeriod.xhtml",
		        data: {
		        	"idCard":idCard,
		        	"ownerName":ownerName,
		        },
				success:function(res){
				   if(res.returnCode=="0000"){
				   	   if(res.period=="1"){
				   	   	   if(location.href.indexOf("register")<0){
				   	   	      location.href="/WeixinService/H5modules/huaanInsurance/register.html"
				   	   	   }
				   	   }else if(res.period=="2"){
				   	   	   if(location.href.indexOf("realName")<0){
				   	   	      location.href="/WeixinService/H5modules/huaanInsurance/realName.html"
				   	   	   }
				   	   }else if(res.period=="3"){
				   	       location.href="/WeixinService/H5modules/huaanInsurance/success.html"
				   	   }
				   }else{
				     	$(document).dialog({type : 'notice',infoText: '服务器异常,请刷新页面再试',autoClose: 1500, position: 'center' });
				   }
				},
				error:function(){
				    $(document).dialog({type : 'notice',infoText: '服务器异常,请刷新页面再试',autoClose: 1500, position: 'center' });
				}
			});
		}else{
			  $(document).dialog({type : 'notice',infoText: '该用户不具有参与活动的资格',autoClose: 1500,position: 'center'});
		   	  setTimeout(function(){
		   	 	  location.href="/WeixinService/weixinLogin/register.shtml"
		   	  },2000)
		}
		
	}
}