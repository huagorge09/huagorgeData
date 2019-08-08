var isRealName; //是否实名

var wechatImg;
$(function() {
	queryMyIntegral()
	queryMyInfo()
	realNameTest()
	input()
})

// 返回【积分首页】
function goIntegralIndex() {
	$('#div_02').hide();
	// 进行操作后重新查询客户积分信息
	$('#div_01').show();
}

//  使用积分页面切换
function useIntegralBox() {
	$('#div_01').hide();
	$('.useIntegral_tab').addClass('orangeFont').siblings().removeClass('orangeFont');
	$('.integralRecordDetail').hide()
	$('.useIntegralDetail').show()
	$('#div_02').show();
}

// 积分记录页面切换
function integralRecordBox() {
	$('#div_01').hide();
	$('.useIntegral_tab').removeClass('orangeFont').siblings().addClass('orangeFont');
	$('.useIntegralDetail').hide();
	$('.integralRecordDetail').show();
	$('#div_02').show();
}

// 【积分首页】 使用积分按钮
function useIntegral() {
	if (isRealName == true) {
		// 积分使用盒子
		useIntegralBox()
	}else {
		$('#realNameBox').show()
		$('.backLayer').show()
	}

}

// 【积分首页】 积分记录按钮
function integralRecordButton() {
	if (isRealName == true) {
		// 积分记录盒子
		integralRecordBox()
	}else {
		$('#realNameBox').show()
		$('.backLayer').show()
	}
	
}


function close() {
	$('#recommendReturn').hide()
	$('.backLayer').hide()
	
}
//  关闭我的顾问按钮【清除数据】
function closeIcon() {
	$('#myRecommenderPop').hide();
	$('#custserNickname').html('')
	$('#custserName').html('')
	$('#custserMobile').html('')
	$('.backLayer').hide()
	$('#friend_phone').val('');
	$('#friend_phone').attr('placeholder', '请输入朋友手机号!');
	$('#friend_name').val('');
	$('#friend_name').attr('placeholder', '请输入朋友姓名!');
	$('#name_error').hide()
    $('#phone_error').hide()

}

// 我的顾问按钮 [打开我的顾问弹框]
function myRecommenderBtn() {

	$('#friend_name').val('')
	$('#friend_phone').val('')
	if (isRealName == true) {
		$.ajax({
			url: '/AppService/business/getConsultantInfo.xhtml',
			data: {},
			dataType: 'json', //服务器返回json格式数据
			type: 'get', //HTTP请求类型
			success: function(data) {
				var custserAddress = data.consultantInfo.custserAddress; //顾问地址
				var custserGender = data.consultantInfo.custserGender; //性别
				var custserNickname = data.consultantInfo.custserNickname; //微信昵称
				var custserPhoto = data.consultantInfo.custserPhoto; //头像文件地址
				var custserMobile = data.consultantInfo.custserMobile; //联系电话
				var custserName = data.consultantInfo.custserName; //顾问姓名
				// var 
				$('#custserNickname').html(custserNickname)
				$('#custserName').html(custserName)
				$('#custserMobile').html(custserMobile)
				$('#custserPhoto').attr('src', custserPhoto);
				var sex;
				if (custserGender == '男') {
					sex = '他';
				} else if (custserGender == '女') {
					sex = '她';
				}
				$('.recommendSex').html(sex)
				// 二维码
				wechatImg = data.consultantInfo.custserPersonalQrCode;
			},
			error: function(xhr, type, errorThrown) {
				console.log("请求未成功", xhr, type, errorThrown)
			}
		});
		$('#myRecommenderPop').show();
		$('.backLayer').show()
	}else {
		$('#realNameBox').show()
		$('.backLayer').show()
	}
}

//  【我的顾问弹出框】提交按钮
function inventBtn() {
	var customerName = trimString($('#friend_name').val())
	var customerMobile = trimString($('#friend_phone').val())
	var consultant = $('#custserName').html() //顾问名称
	var consultantMobile = $('#custserMobile').html() //顾问手机号
	var phoneLen=$('#friend_phone').val().length; //手机号长度
	var referrer = $('#MycustName').val(); //推荐人姓名
	var isValidateName = isTrueName(customerName);
	var isMob = isMobile(customerMobile)
    if(customerName==""){
        $('#name_error').html("姓名不能为空")
        $('#name_error').show()
    }else if(isValidateName == false) {
        $('#name_error').html("请输入正确的姓名")
        $('#name_error').show()
    }else if(customerMobile==""){
        $('#phone_error').html("手机号不能为空")
        $('#phone_error').show()
    }else if(isMob== false) {
        $('#phone_error').html("手机号格式不正确")
        $('#phone_error').show()
    }

	if (customerMobile != null && customerMobile != '' && customerName != null && customerName != '' && isValidateName ==
		true && isMob == true && isRealName == true) {
		//  对手机号进行正则验证
		$.ajax({
			url: '/AppService/business/integral/shareConsultant.xhtml',
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
					if (status == 0 || status == 1) {
                        $('#thankWord').hide()
						$('#successWord').show()
					} else if (status == 2) {
						$('#thankWord').show()
                        $('#successWord').hide()
					}
					$('#myRecommenderPop').hide()
                    $('#recommendReturn').show();
				}else {
					var ze='<p class="tipWord2">网络繁忙，请稍后再试</p>';
                    $('#successWord').html(ze)
				}
			},
			error: function(xhr, type, errorThrown) {
				console.log(type, errorThrown)
			}
		});

	}
}

function clearRecommenderPop() {
	$('#successWord').hide()
	$('#thankWord').hide()
	$('#friend_name').html('')
	$('#friend_phone').html('')
}

//  还原输入款 placeholder
function resetPlaceHolder() {
	$('#friend_name').attr('placeholder', '请输入朋友姓名');
	$('#friend_phone').attr('placeholder', '请输入朋友手机号');
}

//  查询用户积分信息【*总积分和手机号】
function queryMyIntegral() {
	$.ajax({
		url: '/AppService/business/integral/getIntegralInfo.xhtml',
		data: {},
		dataType: 'json', //服务器返回json格式数据
		type: 'get', //HTTP请求类型
		success: function(data) {
			if (data) {
				var errcode = data.errcode;
				var mobile = data.mobile;
				var integral = data.integral;
				
				$('#integralCount').val(integral)
				if (integral <= 0) {
					$('#scoreNum').html('推荐朋友');
				}else{
					$('#scoreNum').html(integral);
				}
			}
		},
		error: function(xhr, type, errorThrown) {

		}
	});
}

//  查询当前用户信息,获取用户名 用户id
function queryMyInfo() {
	$.ajax({
		url: '/AppService/business/queryUserinfo.xhtml',
		data: {},
		dataType: 'json', //服务器返回json格式数据
		type: 'post', //HTTP请求类型
		success: function(data) {
			$('#MycustName').val(data.custName)
			$('#MycustId').val(data.cmfUserId)
		},
		error: function(xhr, type, errorThrown) {
			console.log(type, errorThrown)
		}
	});
}


// 关闭我的顾问回调弹框

$(".closeRe,.exSure").click(function() {
	$("#recommendReturn, .backLayer").hide();
});


// 正则校验姓名是否为中文
function isTrueName(name) {
	var regName = /^[\u4e00-\u9fa5]{2,4}$/;
	return regName.test(name);
}


// 实名鉴权
function realNameTest() {
	$.ajax({
		url: '/AppService/business/queryOriginalUserinfo.xhtml',
		data: {},
		dataType: 'json', //服务器返回json格式数据
		type: 'post', //HTTP请求类型
		success: function(data) {
			if (data) {
				if (data.userType == '30' || data.userType == 30) {
					isRealName = true;
				} else {
					isRealName = false;
				}
			}
		},
		error: function(xhr, type, errorThrown) {

		}
	});
}

// 查询并打开活动规则弹框
function showActRules() {
	$('#actRuleBox').show();
	$('.backLayer').show()
}

//关闭活动规则弹框
function closeActRule() {
	$('#actRuleBox').hide();
	$('.backLayer').hide()
}

//  确定实名鉴权
function closeRealNameBox() {
	$('#realNameBox').hide()
	$('.backLayer').hide()
	setTimeout(function() {
		window.location.href = '/AppService/business/bank/realName.shtml';
	})
}

// 取消实名鉴权
function cancelRealNameBtn() {
	$('#realNameBox').hide()
	$('.backLayer').hide()
}


$('.moreIcon').click(function() {
	$('.wechat-box').show();
	$('#myRecommenderPop').css("z-index", "0");
	$('#wechat-img').attr('src', wechatImg);

});
$('.box-p').click(function() {
	$('.wechat-box').hide();
	$('#myRecommenderPop').css("z-index", "99");
});



function input() {
    $('#friend_name ').on('input',function () {
		$('#name_error').hide()
    })
	$('#friend_phone').on('input',function () {
		$('#phone_error').hide()
    })

}