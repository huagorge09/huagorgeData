$(function(){
	realName.initPage();
   realName.formValid()
})
var channelNo="";
var bankNo="";
var realName={
	initPage:function(){  //初始化页面
		this.pageAction();
		this.queryDrainageUserInfo();
	},
	formValid:function(){  //表单为空增加状态
		var userName=$("input[name=userName]").val();
		var bankCard=$("input[name=bankCard]").val();
		var bankName=$(".bankName").text();
		var idCard=$("input[name=idCard]").val();
		var telPhone=$("input[name=telPhone]").val();
		localStorage.setItem("userName",userName)
		localStorage.setItem("bankCard",bankCard)
		localStorage.setItem("idCard",idCard)
		localStorage.setItem("telPhone",telPhone)
		if(userName!=""&&bankCard!=""&&bankName!=""&&idCard!=""&&telPhone!=""){
			$("#getBtn").addClass("getBtn");
			$("#getBtn").attr("href","javascript:realName.submitDate()")
		}else{
			$("#getBtn").removeClass("getBtn")
			$("#getBtn").attr("href","javascript:;")
		}
	},
	submitDate:function(){  //数据表单提交
		var userName=$("input[name=userName]").val();
		var bankCard=$("input[name=bankCard]").val();
		var bankName=$(".bankName").text();
		var idCard=$("input[name=idCard]").val();
		var telPhone=$("input[name=telPhone]").val();

		//获取隐藏域的手机号
		var RePhone=$("#IsSure").val();
		var _this=this;
		if(!_this.validIdCard(bankCard)){
		}else if(!common.isIdCard(idCard)){
			$(document).dialog({type : 'notice',infoText: '身份证号号码不正确',autoClose: 1500,position: 'center'});
		}else if(!common.isMobile(telPhone)){
			$(document).dialog({type : 'notice',infoText: '手机号码格式不正确',autoClose: 1500,position: 'center'});
		}else{

            if($(".prizeType em:nth-child(1)").hasClass("cur")){
                var awardType="jdCard";
                var assignMobile="";
			}else{
            	var awardType="phoneCard";
            	var assignMobile=$("#topUpTel").text();
			}
            // 将 充值类型保存到 localStorage
            localStorage.setItem("awardType",awardType);
			var toast=$(document).dialog({
		        type : 'toast',
		        infoIcon: '/WeixinWeb/WeixinWeb_Images/H5modules/huaanInsurance/loading.gif',
		        infoText: '请求中'
			});
			$.ajax({
				async:true,
				url : "/WeixinService/setUp/openAccountForActivity.xhtml",
				data : {
					"mobile":telPhone,
					"name":userName,
					"idNo":idCard,
					"idType":"0",
					"bankNumber":bankCard,
					"bankName":bankName,
					"channelNo":channelNo,
					"bankNo":bankNo,
                    "awardType":awardType,
					"assignMobile":assignMobile
				},
				dataType : "json",
				type:"POST",
				success : function(data){
					if(data.returnCode=="0000"){
                        localStorage.removeItem("idCard");
                        localStorage.removeItem("telPhone");
                        localStorage.removeItem("bankName");
                        localStorage.removeItem("bankCard");
                        localStorage.removeItem("topUpTel");
                        localStorage.removeItem("userAwardType");
						location.href="success.html?";
					//	+awardType
					}else{
						toast.close()
						$(document).dialog({type : 'notice',infoText: data.returnMsg,autoClose: 1500,position: 'center'});
					}
				},
				error:function(){
					toast.close()
					$(document).dialog({type : 'notice',infoText: '服务器异常，请稍后再试',autoClose: 1500,position: 'center'});
				}
			});
		}
	},
	pageAction:function(){  //页面行为事件
		var _this=this;
		$(document).on("input","input",function(){
			if(_this.formValid()){
				$("#getBtn").addClass("getBtn");
			}
		})
		$(".check i").on("click",function(){
			$(this).hasClass("no")?$(this).removeClass("no"):$(this).addClass("no")
		})
		$(".bankName").text(localStorage.getItem("bankName")?localStorage.getItem("bankName"):"")
		$("input[name=userName]").val(localStorage.getItem("OWNERNAME"));
		$("input[name=bankCard]").val(localStorage.getItem("bankCard"));
		$("input[name=idCard]").val(localStorage.getItem("IDCARD"));
		$("input[name=telPhone]").val(localStorage.getItem("telPhone"));
		$(".bankName").on("click",function(){
			location.href="/WeixinService/H5modules/huaanInsurance/selectBank.html"
		})
		_this.initInput()
        var status=localStorage.getItem("userAwardType");
		if(status=="1"){
			$(".prizeType em").eq(0).addClass("cur").siblings().removeClass("cur")
		}else if(status=="2"){
            $(".prizeType em").eq(1).addClass("cur").siblings().removeClass("cur")
            $(".telEdit").show()
			$("#topUpTel").text( localStorage.getItem("topUpTel"))
		}

        $(".prizeType em").click(function () {
			$(this).addClass("cur").siblings().removeClass("cur");
			var index=$(this).index();
			if(index==1){
				if($("#topUpTel").text()==""){
                    $(".formInput").show()
				}else {
                    $(".telEdit").show();
                    localStorage.setItem("userAwardType","2");
                }
			}else{
                $(".telEdit").hide();
                localStorage.setItem("userAwardType","1");
			}
        })
		$(".formInput i,.cancel").click(function () {

             $(".formInput").hide();
             if($("#topUpTel").text()==""){
                 $(".prizeType em").eq(0).addClass("cur").siblings().removeClass("cur");
			 }

        })

        $(".telEdit div").click(function () {
            $(".formInput").show()
			$("input[name=topTel],input[name=reTopTel]").val($("#topUpTel").text())
        })
	},
	getCaptcha:function(){  //获取验证码
		$.ajax({
			type:"post",
			async:false,
	        url: "/WeixinService/setUp/getMobileVerifyCode.xhtml",
	        data: {"mobile": $("input[name=telPhone]").val()},
			success:function(res){
				if(res=="0000"){
					$(document).dialog({type : 'notice',infoText: '验证码发送成功',autoClose: 1500,position: 'center'});
					common.countDown({
				   	  elem:"#getCaptcha",
				   	  disable:"disabled",
				   	  time:60
				   })
				}else{
					$(document).dialog({type : 'notice',infoText: '验证码发送失败',autoClose: 1500, position: 'center' });
				}
			},
			error:function(){
			    $(document).dialog({type : 'notice',infoText: '服务器异常',autoClose: 1500, position: 'center' });
			}
		});
	},
	initInput:function(){
		var flag=true;
		$("input").each(function(){
			if($(this).val()==""||$(".bankName").text()==""){
				flag=false
			}
		})
		if(flag){
			$("#getBtn").addClass("getBtn");
			$("#getBtn").attr("href","javascript:realName.submitDate()")
		}
	},
	validIdCard:function(str){  //银行卡校验
		var flag=false;
		$.ajax({
			async : false,
			url : "/WeixinService/setUp/queryBankInfoByBankNumber.xhtml",
			data : {
				bankNumber:str
			},
			dataType : "json",
			cache : false,
			type : 'post',
			success : function(data) {
				if (data.returnCode == "0000") {
					var returnMsg = data.returnMsg;
					if (data.listIsNull == "no") {
						if (data.status == "Y") {
							var bankName=$(".bankName").text();
							if(data.bankInfoDto.bankName.indexOf(bankName)>-1){
								channelNo=data.bankInfoDto.channelNo;
								bankNo=data.bankInfoDto.bankNo;
								flag = true;
							}else{
								$(document).dialog({type : 'notice',infoText:"银行名称与银行卡号不匹配",autoClose: 1500, position: 'center' });
							    flag = false;
							}
						} else {
							$(document).dialog({type : 'notice',infoText:data.returnMsg,autoClose: 1500, position: 'center' });
							flag = false;
						}
					} else {
						$(document).dialog({type : 'notice',infoText:data.returnMsg,autoClose: 1500, position: 'center' });
						flag = false;
					}
				} else if (data.returnCode == "9999") {
					$(document).dialog({type : 'notice',infoText: '信息验证未通过，请检查输入信息是否有误',autoClose: 1500, position: 'center' });
					flag = false;
				} else {
					$(document).dialog({type : 'notice',infoText: '服务器异常，请稍后再试',autoClose: 1500, position: 'center' });
					flag = false;
				}
			},
			error : function(textStatus, errorThrown) {
				$(document).dialog({type : 'notice',infoText: '服务器异常，请稍后再试',autoClose: 1500, position: 'center' });
			},
		});
		return flag;
	},
	queryDrainageUserInfo:function(){ //判断用户是否注册跟实名
		var idCard=localStorage.getItem("IDCARD");
		var ownerName=localStorage.getItem("OWNERNAME");
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
			   }else if(res.returnCode=="9999"){
			     	location.href="/WeixinService/weixinLogin/register.shtml"
			   }else{
			     	$(document).dialog({type : 'notice',infoText: '服务器异常,请刷新页面再试',autoClose: 1500, position: 'center' });
			   }
			},
			error:function(){
			    $(document).dialog({type : 'notice',infoText: '服务器异常,请刷新页面再试',autoClose: 1500, position: 'center' });
			}
		});
	},
	updateMobile:function () {
		var topTel=$("input[name=topTel]").val();
		var reTopTel=$("input[name=reTopTel]").val();
		if(topTel==""||topTel==null){
            $(document).dialog({type : 'notice',infoText: '请输入手机号码',autoClose: 1500, position: 'center' });
		}else if(!common.isMobile(topTel)){
            $(document).dialog({type : 'notice',infoText: '手机号码格式不正确',autoClose: 1500,position: 'center'});
		}else if( reTopTel=="" || reTopTel==null){
            $(document).dialog({type : 'notice',infoText: '请输入确认手机号码',autoClose: 1500, position: 'center' });
		}

		else if(topTel!=reTopTel){
            $(document).dialog({type : 'notice',infoText: '两次输入的手机号码不一致',autoClose: 1500,position: 'center'});
		}else{
            $(".formInput").hide();
            $(".telEdit").show();
            $("#topUpTel").text(topTel);
           var rePhone= $("input[name=reTopTel]").val();
           localStorage.setItem("topUpTel",rePhone)
           $("#IsSure").val(rePhone);
            localStorage.setItem("userAwardType","2");

		}



    }
}

