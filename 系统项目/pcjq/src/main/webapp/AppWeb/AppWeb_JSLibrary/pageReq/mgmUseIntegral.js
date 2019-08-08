var isOnceSubmit; //是否重复兑换
$(function () {
	recordIndexTab(); //导航条点击
	queryExThingInit();
	queryThisIntegral();

})
// 点击积分记录页面导航条选项
function recordIndexTab() {
	$('.top .tab li').click(function () {
		$(this).addClass('orangeFont').siblings().removeClass('orangeFont');
		// 获取li的index值
		var index = $(this).index();
		if (index == 0 || index == '0') {
			useIntegralBox()
		} else if (index == 1 || index == '1') {
			integralRecordBox()
		}
	})
}

//  初始化兑换物品的列表
function queryExThingInit() {
	//获取父节点
	var cardBox = $('#cardBox');
	$.ajax({
		url: '/AppService/business/integral/getIntegralGoods.xhtml',
		dataType: 'json',//服务器返回json格式数据
		type: 'get',//HTTP请求类型
		success: function (data) {
			if (data.errcode == '0000') {
				//动态加载兑换品列表
				var html = "";
				var type;
				$.each(data.data, function (i, e) {
					if (e.awardName == '京东卡') {
						type = 'jdIcon';
					} else if (e.awardName == '话费卡') {
						type = 'phoneRateIcon';
					}
					html += '<li>' +
						'<span class="prizeIcon ' + type + '"></span>' +
						'<span class="prizeName">' + e.awardName + '</span>' +
						'<span class="prizekAmount">' + e.awardDetail + '</span>' +
						'<span class="toExchange" onclick="toExchange(\'' + e.awardid + '\',\'' + e.awardName + '\',\'' + e.awardDetail + '\',\'' + (-e.integralChange) + '\')">兑换</span></li>'
				})
				$("#cardBox").append(html)
			}
		},
		error: function (xhr, type, errorThrown) {
			console.log("请求未成功")
		}
	});
}

//  兑换产品列表 兑换按钮
function toExchange(awardid, awardName, awardDetail, integralChange) {
	isOnceSubmit = false;
	queryThisIntegral(); //查询用户当前积分
	var useIntegral = $('#userIntegralNow').val()
	// 调出蒙层
	$('.backLayer').show();
	if (useIntegral <= 0) {
		var ze = '<div class="successExBox" >'
			+ '<p class="ob">您的积分余额不足，推荐好友即可获得积分喔</p></div>';
		$('#lastByContent').html(ze);
		$('#successExBox').show()
		resetExchangeBox()
	} else {
		$('.awardName').html(awardName)
		$('.awardDetail').html(awardDetail)
		$('#integralChange').html(integralChange)
		$('.integralChange').val(integralChange)
		$('#awardid').val(awardid)
		$("#integralExBox").show();
	}

}

//  兑换产品弹框 确认
function successExBtn() {
	//  获取兑换的物品信息
	var goodsId = $('#awardid').val()
	var integralChange = $('.integralChange').val()
	integralChange = '-' + integralChange;
	integralChangeX = parseInt(integralChange)
	var integralBeforeChange = $('#userIntegralNow').val();  //当前用户积分
	//  先判断用户积分是否为0
	if (integralBeforeChange <= 0) {
		var ze = '<div class="successExBox" >'
			+ '<p class="ob">您的积分余额不足，推荐好友即可获得积分喔</p></div>';
		$('#lastByContent').html(ze);
		$('#successExBox').show()
		resetExchangeBox()
	}
	if (isOnceSubmit == false) {
		isOnceSubmit = true;
		$.ajax({
			url: '/AppService/business/integral/goodsExchange.xhtml',
			data: {
				'goodsId': goodsId,
				'integralChange': integralChangeX,
				'integralBeforeChange': integralBeforeChange
			},
			dataType: 'json',//服务器返回json格式数据
			type: 'post',//HTTP请求类型
			success: function (data) {
				if (data) {
					if (data.errcode == '0000') {
						if (data.data == '0') {
							var ze = '<div class="successExBox" >'
								+ '<p class="w2">兑换失败</p></div>';
							$('#lastByContent').html(ze)
						} else if (data.data == '1') {
							$('#toExBox').html('兑换成功')
							$('#integralExBox').hide()
							var be = '<p class="successIcon"></p>'
								+ '<div class="successExBox" ><p class="w1"> 恭喜您成功兑换<i class="awardDetail"></i><i class="awardName"></i></p>'
								+ '<p class="w3">稍后将把兑换信息发送到您的<i id="exPhone"></i>手机上，</p>'
								+ '<p>请注意查收</p></div>';
							$('#lastByContent').html(be)
							$('#successExBox').show()
							resetExchangeBox()
							queryThisIntegral()
							// 重新加载下积分列表
							// queryIntegralList()
							$('.backLayer').show();
						}
					} else {
						var ze = '<div class="successExBox" >'
							+ '<p class="op">网络繁忙，请稍后再试</p></div>';
						$('#lastByContent').html(ze)
						$('#integralExBox').hide()
						$('#successExBox').show()
						$('.backLayer').show();
					}
				}

			},
			error: function (xhr, type, errorThrown) {
				console.log(type, errorThrown)
			}
		});

	}

}

//  还原兑换确认框
function resetExchangeBox() {
	var ze = '<p class="w1">你将兑换<i class="awardDetail"></i><i class="awardName"></i></p>' +
		'<p class="w2">消耗<i id="integralChange"></i>积分</p>';
	$('.toExBox').html(ze)
}

//  兑换产品弹框 取消
function cancelBtn() {
	exBox()
	resetExchangeBox()
}

function exBox() {
	$('#integralExBox').hide()
	$('.backLayer').hide()
}


//  兑换成功取消按钮
function cancelSuccessBtn() {
	$('#successExBox').hide();
	$('.backLayer').hide()
}

//  查询用户当前积分
function queryThisIntegral() {
	$.ajax({
		url: '/AppService/business/integral/getIntegralInfo.xhtml',
		data: {},
		dataType: 'json', //服务器返回json格式数据
		type: 'get', //HTTP请求类型
		success: function (data) {
			if (data) {
				var errcode = data.errcode;
				var mobile = data.mobile;
				var integral = data.integral;
				var alreadyPurchase = data.alreadyPurchase;
				var noPurchase = data.noPurchase;
				if (integral > 0) {
					$('#scoreNum').html(integral);
				} else {
					$('#scoreNum').html('推荐朋友');
				}
				$('#userIntegralNow').val(integral)
				$('#integralTotal').html(alreadyPurchase + noPurchase) //总推荐人数
				$('#alreadyPurchase').html(alreadyPurchase)
				$('#noPurchase').html(noPurchase)
				$('#hasbuyO').val(alreadyPurchase)
				$('#nobuyO').val(noPurchase)

			}
		},
		error: function (xhr, type, errorThrown) {
			console.log(type, errorThrown)
		}
	});
}

function closeEx() {
	$('#integralExBox').hide()
	resetExchangeBox()
	$('#successExBox').hide()
	$('.backLayer').hide()
}