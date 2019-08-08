var title = ""; //标题
var desc = ""; //描述
var host = window.location.protocol + "//" + window.location.host;
var imgUrl = host + "/WeixinWeb/WeixinWeb_Images/mgm/share.jpg" //转发图标
var link = '';
var eventId = "";
var pageSource = ""
var userId = ""; //解密后的 用户id
var userid = getUrlSearchParams("userid") ? getUrlSearchParams("userid") : localStorage.getItem("userid"); //加密的用户id
var openId = "";
var apiHost = "" //生产为相对路径
var consultantMobile = ''; //顾问手机号
var consultant = ''; //顾问姓名
var referrer = ''; //当前用户名
var custserId = ''; //顾问id
var custName = ""; // 推荐人姓名
var pageFrom = ""; //页面跳转来源
var isRealName = ''; // 是否实名
var pageId = "wx_mgm_actPageId";
var unionid = '';

$(function() {
	silentAccredit()
	queryUserToRealName()
	queryUserInfo()
	resetCodeImg()

})

// 判断页面入口是短信还是微信
function isFromSms() {
	var mobile = getUrlSearchParams("mobile") //推荐人手机号
	if (isMobile(mobile) == ture) {
		pageFrom = 'sms'; //判断为短信链接
	}
}


// 正则校验姓名是否为中文
function isTrueName(name) {
	var regName = /^[\u4e00-\u9fa5]{2,4}$/;
	return regName.test(name);
}

/**
 * 验证手机号码
 * @param {*} str
 * @returns
 */
function isMobile(str) {
	return /^0?(13[0-9]|10[0123456789]|11[0123456789]|12[0123456789]|15[0123456789]|18[0123456789]|14[0123456789]|16[0123456789]|17[0123456789]|19[0123456789])[0-9]{8}$/
		.test(str);
}


// 点击推荐朋友给顾问按钮
function recommendSubmit() {
	if (isRealName == 'false') {
		$('#realNamePop').show()
		$('.backLayer').show()
	}
	var customerMobile = $('#customerMobile').val() //被推荐人手机号
	var customerName = $('#customerName').val() //被推荐人姓名
	var isValidateName = isTrueName(customerName);
	var isMob = isMobile(customerMobile)
	if (customerName == "") {
		$('#customerName').attr('placeholder', '姓名不能为空');
		$('#customerName').addClass("error")
	} else if (!isValidateName) {
		$('#customerName').val("")
		$('#customerName').attr('placeholder', '请输入正确的姓名');
		$('#customerName').addClass("error")
	} else if (customerMobile == "") {
		$('#customerMobile').attr('placeholder', '手机号不能为空');
		$('#customerMobile').addClass("error")
	} else if (!isMob) {
		$('#customerMobile').val("")
		$('#customerMobile').attr('placeholder', '手机号格式不正确');
		$('#customerMobile').addClass("error")
	} else if (isRealName == 'true') {
		$.ajax({
			url: '/WeixinService/business/integral/shareConsultant.xhtml',
			data: {
				'customerName': customerName,
				'customerMobile': customerMobile,
				'consultant': consultant,
				'consultantMobile': consultantMobile,
				'referrer': referrer
			},
			dataType: 'json', //服务器返回json格式数据
			type: 'post', //HTTP请求类型
			success: function(data) {
				var status = data.status; //是否成功推荐 0成功 1已推荐 2已经注册
				if (data.errcode == '0000') {
					if (status == '0' || data.status == '1') {
						$('.oneLine').html('感谢您的推荐')
						$('.threeLine').hide()
					} else if (status == '2') {
						$('.oneLine').html('您推荐的朋友已经是我们的客户')
						$('.threeLine').show()
					}
					$(".recommendPop").show();
					//调出蒙层
					$('.backLayer').show();
					dataRecord(customerName,customerMobile) //提交推荐埋点
				} else {
					console.log('请求异常')
				}

			},
			error: function(xhr, type, errorThrown) {
				console.log(type, errorThrown)
			}
		});

	}
}


/*静默授权 获取加密后的userid 分开保存 
 */

//  判断是否静默授权 微信需要授权
function silentAccredit() {
	if (!userid) { //判断用户有没有静默授权 wx
		addCookie("eventId", getUrlSearchParams("eventId"))
		addCookie("pageSource", getUrlSearchParams("pageSource"))
		//  跳转活动页面?
		location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host +
			"/WeixinService/business/mgm/mgmPromotionAct.shtml";
	} else {
		// 在localstorage里存userid
		eventId=getUrlSearchParams("eventId") ? getUrlSearchParams("eventId") : getCookie("eventId")
		pageSource=getUrlSearchParams("pageSource") ? getUrlSearchParams("pageSource") : getCookie("pageSource")
	}
}


// 判断用户是否绑定
function checkUserBind() {
	$.ajax({
		async: false,
		url: apiHost + "/api/userAutoLogin",
		type: "get",
		dataType: 'json',
		data: {
			"userId": userId
		},
		success: function(data) {
			if (data.success) {
				var type = localStorage.getItem("type");
			
				if (data.resp.returnCode == "N") { //如果用户未绑定,即未登录
					location.href = "/WeixinService/weixinLogin/login.shtml?pageSource=" + pageId;
					return;
				}else{
					//  如果用户未实名
					if (data.resp.userInfoExtend.realNameStatus == 'N') {
						$('#realNamePop').show()
						$('.backLayer').show()
					}
				}
				

			}

		},
		error: function() {
			errorRemark("接口获取用户信息服务器异常，请稍后再试。");
		}
	});
}

// 获取解密后的userid
function queryUserInfo() {
	if (!!userid) { //如果有userid
		$.ajax({
			url: apiHost + "/api/wxuserinfo",
			async: false,
			type: "get",
			data: {
				userId: userid
			},
			dataType: "json",
			success: function(res) {

				if (res.success) {
					userId = res.resp.userId;
					localStorage.setItem("userId", userId);
					localStorage.setItem("userid", userid);
					checkUserBind() //检测用户绑定
					queryRecommendInfo() //查询顾问信息
				} else {
					//errorRemark("解密失败");
					localStorage.removeItem("userId", userId);
					localStorage.removeItem("userid", userid);
					location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host +
						"/WeixinService/business/mgm/mgmPromotionAct.shtml";
				}
			},
			error: function() {
				console.log('请求用户信息异常')
			}
		})
	}

}

// 根据生产或者测试环境更改图片
function resetCodeImg(){
        var oneSrcDev="/WeixinWeb/WeixinWeb_Images/mgm/wxToRulePageCode.png";//测试环境短信二维码
        var onesrcPrd="/WeixinWeb/WeixinWeb_Images/mgm/wxCodePrd.png";//生产环境短信二维码
        if(host.indexOf("wxtest1")>-1){
            $("#codeImg").attr("src",oneSrcDev)
        }else{
            $("#codeImg").attr("src",onesrcPrd)
        } 

}

// 查询用户信息 【判断是否实名】
function queryUserToRealName() {
	var resultData = {};
	$.ajax({
		async: false,
		url: "/WeixinService/business/queryUserinfo.xhtml",
		data: "",
		dataType: "json",
		cache: false,
		type: "post",
		error: function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success: function(data) {

			if (data) {
				referrer = data.custName;
				if (data.userType != 30 || data.userType != '30') {
					isRealName = 'false';
				} else if (data.userType == "30" || data.userType == 30) {
					isRealName = 'true';
					custName = data.custName;
					//  修改分享样式
					sharePageStyle()
					localStorage.setItem('custName', custName)
				}
			}

		}
	});

}

/*
 * 获取链接参数
 */
function getUrlSearchParams(_name) {
	var name, value = '';
	var str = window.location.href;
	var num = str.indexOf("?");
	str = str.substr(num + 1);
	var arr = str.split("&");
	for (var i = 0; i < arr.length; i++) {
		num = arr[i].indexOf("=");
		if (num > 0) {
			name = arr[i].substring(0, num);
			if (name.replace(/^\s+|\s+$/g, "") == _name) {
				value = arr[i].substr(num + 1);
				break;
			}
		}
	}
	return decodeURI(value);
}

// 实名鉴权取消按钮
$('.firstLookBtn').click(function() {
	$('#realNamePop').hide()
	$('.backLayer').hide()
})

// 实名鉴权跳转
$('.toRealBtn').click(function() {
	window.location.href = '/WeixinService/business/bank/bankAuth.shtml';
})


/**
 * 添加cookies
 * @param {Object} name
 * @param {Object} value
 * @param {Object} path
 * @param {Object} expiresHours
 */
function addCookie(name, value, path, expiresHours) {
	var cookieString = name + "=" + escape(value);
	//判断是否设置过期时间
	if (expiresHours > 0) {
		var date = new Date();
		date.setTime(date.getTime() + expiresHours * 3600 * 1000);
		cookieString = cookieString + "; expires=" + date.toGMTString();
	}
	cookieString += "; path=" + (path ? path : "/");
	document.cookie = cookieString;
}

// 关闭推荐完成提示框
function closePop() {
	$('.recommendPop').hide()
	$('.backLayer').hide()
}


//  根据用户id[加密过]查询用户对应的顾问
function queryRecommendInfo() {
	$.ajax('/WeixinService/queryUserCustService.xhtml ', {
		data: {
			'userId': userid
		},
		dataType: 'json', //服务器返回json格式数据
		type: 'post', //HTTP请求类型
		success: function(data) {
			consultant = data.data.custserName;
			consultantMobile = data.data.custserMobile;
			custserId = data.data.custserId;
			localStorage.setItem('custserId', custserId)
			//  进入页面埋点
			pageDataRecord()

		},
		error: function(xhr, type, errorThrown) {
			console.log(errorThrown)
		}
	});

}

//  推荐完成数据埋点
function dataRecord(customerName,customerMobile) {
	//var eventId = 'wx_mgm_recommId';
	var eventId = pageSource='event_mgmTemplateMsg_modulMsgId'?'event_mgmPromoSubmit_modulMsgId':'wx_mgm_recommId';
	//  顾问id
	var userId = localStorage.getItem("userId");
	var data = customerName + ',' + customerMobile;
	var parm = {
		'event': eventId,
		'unionid': unionid,
		'pageSource': custserId,
		'pageGroup': pageId,
		'data': data,
		'userId': userId,
		'trackDate': getNowFormatDate(),
		'trackMillis': new Date().getTime(),
	}
	$.ajax({
		url: apiHost + "/api/wxtrack",
		data: JSON.stringify(parm),
		dataType: "json",
		contentType: "application/json",
		cache: false,
		type: "POST",
		success: function(n) {
		}
	});

}

//  进去页面数据埋点
function pageDataRecord() {
	//var eventId = 'wx_mgm_actId';
	//  顾问id存储在pageSource字段里
	unionid = new Date().getTime();
	var userId = localStorage.getItem("userId");
	var parm = {
		'event': eventId,
		'unionid': unionid,
		'pageSource': custserId,
		'page': pageId,
		'userId': userId,
		'trackDate': getNowFormatDate(),
		'trackMillis': new Date().getTime(),
	}

	$.ajax({
		url: apiHost + "/api/wxtrack",
		data: JSON.stringify(parm),
		dataType: "json",
		contentType: "application/json",
		cache: false,
		type: "POST",
		success: function(n) {

		}

	});

}

/*
 * 获取链接参数
 */
function getUrlSearchParams(_name) {
	var name, value = '';
	var str = window.location.href;
	var num = str.indexOf("?");
	str = str.substr(num + 1);
	var arr = str.split("&");
	for (var i = 0; i < arr.length; i++) {
		num = arr[i].indexOf("=");
		if (num > 0) {
			name = arr[i].substring(0, num);
			if (name.replace(/^\s+|\s+$/g, "") == _name) {
				value = arr[i].substr(num + 1);
				break;
			}
		}
	}
	return decodeURI(value);
}
// 修改分享链接样式
function sharePageStyle() {
	link = host + '/WeixinService/business/mgm/mgmPromotionAct.shtml';
	title = custName + "向您推荐招商财富"; //标题
	desc = "招商财富，值得信赖"; //描述
}
