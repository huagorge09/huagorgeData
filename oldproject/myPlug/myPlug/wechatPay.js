var oldTimeStamp;
var oldNonceStr;


function getTimeStamp() {
    var timestamp = new Date().getTime();
    var timestampstring = timestamp.toString();
    oldTimeStamp = timestampstring;
    return timestampstring
};


function getNonceStr() {
    var $chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    var maxPos = $chars.length;
    var noceStr = "";
    for (i = 0; i < 32; i++) {
        noceStr += $chars.charAt(Math.floor(Math.random() * maxPos))
    };
    oldNonceStr = noceStr;
    return noceStr
};
function getSignType() {
    return "SHA1"
};
function getPaySign(body, out_trade_no, total_fee) {
    out_trade_no = "" + out_trade_no;
    var timeStampVal = oldTimeStamp;
    var nonceStr = oldNonceStr;
    var params = "TRAN_CODE=EpayReq&TRAN_BODY=" + body + "&OUT_TRADE_NO=" + out_trade_no + "&TRAN_CREATEIP=127.0.0.1" + "&TRANS_AMOUNT=" + total_fee + "&NONCE_STR=" + nonceStr + "&TIME_STAMP=" + timeStampVal;
    var URL = "/SL_LEM/pay/weixin/getSignV3.do";
    /*alert(URL);*/
    var aDataSet;
    /*alert("params******************==="+params);*/
    $.ajax({
        type: "post",
        url: URL,
        data: params,
        async: false,
        success: function(data) {
            data = eval("(" + data + ")");
            aDataSet = data
        }
    });
    return aDataSet
};

//var wx_appId="wx4cf20eaf945b4f48";
function wechatPay(body, out_trade_no, total_fee, redirectUri,callback) {	
	
    var timeStampVal = getTimeStamp();
    //var nonceStr = getNonceStr();
    var backData ;
    var signFlag=true;
    //签名的如果失败自动重试3次
    for(var i=0;i<3;i++){    	
	    try{
	    	backData = getPaySign(body, out_trade_no, total_fee);
	    	if(backData.SING_FLAG=='N'){
	    		alert('23:57--24：00暂停支付，请其他时段再试');
	    		return false;
	    	}
	    }catch(e){	    	
	    	signFlag=false;
	    }
	    //如果签名成功，直接结束循环
	    if(backData.PAY_SIGN!=null&&backData.PAY_SIGN!='null'&&backData.PAY_SIGN!=''){
	    	signFlag=true;
	    	i=3;
	    }else{
	    	signFlag=false;
	    }
    }
    //判断签名是否成功，如果失败提示用户重试
    if(!signFlag){
    	LEM.page.hideOverlay();
    	alert('提交微信支付失败，请重试');
    	return;
    }
    var packageVal = backData.PACKAGE;
    var signVal = backData.PAY_SIGN;
    var appId =  backData.APPID;
    var timeStamp = backData.TIMESTAMP;
    var nonceStr = backData.NONCESTR;
    try{
    	
    	_subWxPay(appId,timeStamp,nonceStr,packageVal,signVal,redirectUri,callback); 
    	//定时10秒钟隐藏遮罩，防止支付控件没弹出，可以再次触发一次弹窗
		window.setTimeout(function(){
			LEM.page.hideOverlay();
			//判断当前时间和点击支付按钮的时间是否超过10s钟，如果超过则隐藏
			 	var timestamp = new Date().getTime();
			    var timestampstring = timestamp.toString();
			    var subTime=timestampstring-timeStampVal;			    
			    if(subTime >= 10000){			    	
			    	LEM.page.hideOverlay();
			    }			
			},10000); 
    
    }catch(e){
    	alert("微信支付出错了，重试下吧!");
    }
    return false
}
//弹窗微信支付
function _subWxPay(appId,timeStampVal,nonceStr,packageVal,signVal,redirectUri,callback){	
	wx.chooseWXPay({
		appId: appId,
	    timestamp: timeStampVal, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
	    nonceStr: nonceStr, // 支付签名随机串，不长于 32 位
	    package: packageVal, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
	    signType: "MD5", // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
	    paySign: signVal, // 支付签名
	    cancel:function(){
	    	
	    	LEM.page.hideOverlay();
	    },
	    fail:function(){
	    	
	    	LEM.page.hideOverlay();
	    	alert("微信支付出错了，重试下吧,亲");
	    },
	    success: function (res) {  
	    	if(null!=callback){
				if (typeof callback == 'string'){
					eval(callback+"()");
				}else{
					callback.call();
				}
				
			}
            if (! (typeof(redirectUri) == "undefined")&&redirectUri!='') {
                window.location.href = redirectUri;                
            }
	    },
	    complete:function(res){
	    	
	    }
	});
	
}