
var referrer; 

$(function() {
    queryMyRecommender();
	queryMyInfo()
    
})


// 正则校验姓名是否为中文
function isTrueName(name) {
	var regName = /^[\u4e00-\u9fa5]{2,4}$/;
	return regName.test(name);
}

// 点击推荐朋友给顾问按钮
function recommendSubmit() {
    var consultantMobile = $('#custserMobile').html(); //顾问手机号
    var customerMobile = $('#customerMobile').val() //被推荐人手机号
    var customerName = $('#customerName').val() //被推荐人姓名
    var consultant = $('#custserName').html() //顾问姓名
    var isValidateName = isTrueName(customerName);
    var isMob = isMobile(customerMobile)
    if(customerName==""){
        $('#customerName').attr('placeholder', '姓名不能为空');
        $('#customerName').addClass("error")
    }else if(!isValidateName){
        $('#customerName').val("")
        $('#customerName').attr('placeholder', '请输入正确的姓名');
        $('#customerName').addClass("error")
    }else if(customerMobile==""){
        $('#customerMobile').attr('placeholder', '手机号不能为空');
        $('#customerMobile').addClass("error")
    }else if(!isMob){
        $('#customerMobile').val("")
        $('#customerMobile').attr('placeholder', '手机号格式不正确');
        $('#customerMobile').addClass("error")
    }else{
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
                        $('.oneLine').show()
                        $('.twoLine').show()
                        $('.threeLine').show()
                    }
                    $(".recommendPop").show();
                    //调出蒙层
                    $('.backLayer').show();
                } else {
                    console.log('请求异常')
                }
            },
            error: function(xhr, type, errorThrown) {
                console.log(type,errorThrown)
            }
        });

    }
}


// 关闭叉叉
function closePop() {
    $(".recommendPop").hide();
    // 关闭蒙层
    $('.backLayer').hide();
}

// 弹出框 知道了
function knowCommit() {
    $(".recommendPop").hide();
    // 关闭蒙层
    $('.backLayer').hide();
}

//  返回上一级页面
function goback(){
    window.history.go(-1)
}
// 查询我的顾问信息
function queryMyRecommender() {
    $.ajax({
        url: '/WeixinService/business/getConsultantInfo.xhtml',
        data: {},
        dataType: 'json', //服务器返回json格式数据
        type: 'get', //HTTP请求类型
        timeout: 10000, //超时时间设置为10秒；
        success: function(data) {
            var custserAddress = data.consultantInfo.custserAddress; //顾问地址
            var custserGender = data.consultantInfo.custserGender; //性别
            var custserNickname = data.consultantInfo.custserNickname; //微信昵称
            var custserMobile = data.consultantInfo.custserMobile; //联系电话
            var custserName = data.consultantInfo.custserName; //顾问姓名
			var custserPhoto=data.consultantInfo.custserPhoto; //顾问头像
			var custserPersonalQrCode=data.consultantInfo.custserPersonalQrCode; //顾问二维码
            $('#custserNickname').html(custserNickname)
            $('#custserName').html(custserName)
            $('#custserMobile').html(custserMobile)
            $(".teleIcon").attr("href","tel:"+custserMobile)
            $('#custserPhoto').attr('src',custserPhoto)
			$('#qrCodeImg').attr('src',custserPersonalQrCode)
            if (custserGender == '男') {
                $('.custserGender').html('他')
            } else if (custserGender == '女') {
                $('.custserGender').html('她')
            }
        },
        error: function(xhr, type, errorThrown) {
            console.log("请求未成功", xhr, type, errorThrown)
        }
    });
    //  对手机号和姓名进行清空
    $('#customerName').val('')
    $('#customerMobile').val('')
}

//  查询当前用户信息,获取用户名 用户id
function queryMyInfo() {
	$.ajax({
		url: '/WeixinService/business/queryUserinfo.xhtml',
		data: {},
		dataType: 'json', //服务器返回json格式数据
		type: 'post', //HTTP请求类型
		success: function(data) {
		 referrer=data.custName;
		},
		error: function(xhr, type, errorThrown) {
			console.log(type, errorThrown)
		}
	});
}

//设置根元素字体
var win = window,
    doc = document;
function setFontSize() {
    var winWidth = $(window).width();
    //750这个数字是根据你的设计图的实际大小来的，所以值具体根据设计图的大小
    var size = (winWidth / 750) * 100;
    doc.documentElement.style.fontSize = (size < 100 ? size : 100) + 'px';
};
//这里我们给个定时器来实现页面加载完毕再进行字体设置
setTimeout(function() {
    //初始化
    setFontSize();
}, 100);