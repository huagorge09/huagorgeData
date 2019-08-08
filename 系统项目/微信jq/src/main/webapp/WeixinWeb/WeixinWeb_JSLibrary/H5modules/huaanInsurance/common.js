var common={
	countDown:function(object){  //验证码倒计时
	   var elem=object.elem;
   	   var disable=object.disable;
   	   var time=parseInt(object.time);
   	   var txt=$(elem).text();
   	   if(!$(elem).hasClass(disable)){
   	   	  $(elem).addClass(disable);
   	   	  timeDowm()
   	   	  var delay=setInterval(timeDowm,1000);
   	   	  function timeDowm(){
   	   	  	 time--;
   	   	  	 $(elem).text(time + "s后重新发送");
   	   	  	 if(time<1){
   	   	  	 	$(elem).text(txt);
   	   	  	 	$(elem).removeClass(disable);
   	   	  	 	clearInterval(delay);
   	   	  	 }
   	   	  }
   	   }
	},
	isMobile:function(str){
		return /^0?(13[0-9]|10[0123456789]|11[0123456789]|12[0123456789]|15[0123456789]|18[0123456789]|14[0123456789]|16[0123456789]|17[0123456789]|19[0123456789])[0-9]{8}$/.test(str);
	},
	passWord:function(str){
		return /^[\d]{6,16}$/.test(str);
	},
	getUrlSearchParams:function(_name){
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
	},
	isIdCard: function (num) {
        num = num.toUpperCase();
        if (!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(num))) {
            return false;
        }
        var len, re, arrSplit, dtmBirth, bGoodDay, arrInt, arrCh, nTemp;
        len = num.length;
        if (len == 15) {
            re = new RegExp(/^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/);
            arrSplit = num.match(re);
            dtmBirth = new Date('19' + arrSplit[2] + '/' + arrSplit[3] + '/' + arrSplit[4]);
            bGoodDay = (dtmBirth.getYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
            if (!bGoodDay) {
                return false;
            } else {
                arrInt = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
                arrCh = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
                nTemp = 0;
                num = num.substr(0, 6) + '19' + num.substr(6, num.length - 6);
                for (var i = 0; i < 17; i++) {
                    nTemp += num.substr(i, 1) * arrInt[i];
                }
                num += arrCh[nTemp % 11];
                return true;
            }
        }
        if (len == 18) {
            re = new RegExp(/^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/);
            arrSplit = num.match(re);
            dtmBirth = new Date(arrSplit[2] + "/" + arrSplit[3] + "/" + arrSplit[4]);
            bGoodDay = (dtmBirth.getFullYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
            if (!bGoodDay) {
                return false;
            } else {
                var valnum;
                arrInt = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
                arrCh = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
                nTemp = 0;
                for (var i = 0; i < 17; i++) {
                    nTemp += num.substr(i, 1) * arrInt[i];
                }
                valnum = arrCh[nTemp % 11];
                if (valnum != num.substr(17, 1)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    },
    queryParamList : function(paramType,paramKey,pmValueOne){
        var data = null;
        $.ajax({
            async: !1,
            url: "/WeixinService/setUp/queryParamList.xhtml",
            data: {
                paramType :paramType,
                paramKey : paramKey,
                pmValueOne : pmValueOne
            },
            dataType: "json",
            cache: !1,
            type: "POST",
            error: function() {
                errorRemark("网络繁忙，请稍后再试。");
            },
            success: function(n) {
                if(n != null && n.resultCode =="0000"){
                    data = n.data;
                }
            }
        });
        return data;
    }

}
