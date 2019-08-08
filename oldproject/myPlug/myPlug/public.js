var common={
   Pop : function(form,content,showtime,callback) {
		var object = object || {};
		object.contain = content;
		object.time = showtime || 1000 ;
		var random = new Date().getTime();
		var contain = object.contain || "000000";
		var time = object.time || 1000;
		var autoBoxHtml = '<div class="tipBox autoBox" id="autoBox" data-id="autoBox' + random + '">' + contain + '</div>';
		if ($("#autoBox").length > 0) {
		  $("#autoBox").remove();
		}
		$("body").append(autoBoxHtml); 
		setTimeout(function () {
			$("[data-id=autoBox" + random + "]").css("opacity", 0);
		}, time);
		setTimeout(function () {
			$("[data-id=autoBox" + random + "]").remove();
			  typeof object.callback == "function" && object.callback();
		}, time + 500);
	},
   isScrollBottom: {
        //滚动条在Y轴上的距离
        getScrollTop: function () {
            var scrollTop = 0,
                bodyScrollTop = 0,
                documentScrollTop = 0;
            if (document.body) {
                bodyScrollTop = document.body.scrollTop;
            }
            if (document.documentElement) {
                documentScrollTop = document.documentElement.scrollTop;
            }
            scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop : documentScrollTop;
            return scrollTop;
        },
        //文档的高度
        getScrollHeight: function () {
            var scrollHeight = 0,
                bodyScrollHeight = 0,
                documentScrollHeight = 0;
            if (document.body) {
                bodyScrollHeight = document.body.scrollHeight;
            }
            if (document.documentElement) {
                documentScrollHeight = document.documentElement.scrollHeight;
            }
            scrollHeight = (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight : documentScrollHeight;
            return scrollHeight;
        },
        //浏览器窗口的高度
        getWindowHeight: function () {
            var windowHeight = 0;
            if (document.compatMode == 'CSS1Compat') {
                windowHeight = document.documentElement.clientHeight;
            } else {
                windowHeight = document.body.clientHeight;
            }
            return windowHeight;
        },
        //判断滚动条是否到底部
        scrollBottom: function (bottomPx) {
            if (Math.abs(common.isScrollBottom.getScrollTop() + common.isScrollBottom.getWindowHeight() - common.isScrollBottom.getScrollHeight()) < bottomPx) {
                return true;
            }
            return false;
        }
    },
     /**
     * 格式化时间
     */
    sec_to_time : function(s) {
        var t = "";
        if(s > -1){
            var hour = Math.floor(s/3600);
            var min = Math.floor(s/60) % 60;
            var sec = s % 60;
            if(hour > 0){
                if(hour < 10) {
                    t = '0'+ hour + ":";
                } else {
                    t = hour + ":";
                }
            }
            if(min < 10){
                t += "0";
            }
            t += min + ":";
            if(sec < 10){t += "0";}
            t += sec;
        }
        return t;
    },
    /**
     * 返回两个时间的天数
     * @param {Object} sDate1 日期1
     * @param {Object} sDate2 日期2
     */
    getDateRegion: function(sDate1, sDate2) {
    	var aDate, oDate1, oDate2, iDays;
    	aDate = sDate1.split("-");
    	oDate1 = new Date(aDate[1] + '/' + aDate[2] + '/' + aDate[0]);
    	aDate = sDate2.split("-");
    	oDate2 = new Date(aDate[1] + '/' + aDate[2] + '/' + aDate[0]);
    	iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 / 24) + 1;
    	return iDays;
    },
     /**
      * 判断日期大小
      * @param {Object} date1 日期1
      * @param {Object} date2 日期2
      */
     compareTime:function (date1, date2) {
        var arr = date1.split("-");
        var starttime = new Date(arr[0], arr[1], arr[2]);
        var starttimes = starttime.getTime();
        var arrs = date2.split("-");
        var lktime = new Date(arrs[0], arrs[1], arrs[2]);
        var lktimes = lktime.getTime();
        if (starttimes > lktimes) {
            return false;
        }else{
            return true;
        }
    },
    /**
     * 
     * @param {Object} name
     * @param {Object} value
     * @param {Object} path
     * @param {Object} expiresHours
     */
    addCookie: function(name,value,path, expiresHours){
            var cookieString= name+"="+escape(value);
            //判断是否设置过期时间
            if(expiresHours>0){
                var date=new Date();
                date.setTime(date.getTime()+expiresHours*3600*1000);
                cookieString=cookieString+"; expires="+date.toGMTString();
            }

            cookieString +=  "; path=" +( path ? path : "/");
            document.cookie=cookieString;
    },
    /**
     * 
     * @param {Object} name
     */
    getCookie: function(name){
            var strCookie=document.cookie;
            var arrCookie=strCookie.split("; ");
            for(var i=0;i<arrCookie.length;i++){
                var arr=arrCookie[i].split("=");
                if(arr[0]==name)return arr[1];
            }
            return "";
    },
    /**
     * 
     * @param {Object} name
     */
    deleteCookie: function(name){
            var date=new Date();
            date.setTime(date.getTime()-10000);
            document.cookie=name+"=v; expires="+date.toGMTString();
    },
    //获取链接参数
    getUrlSearchParams:function(_name){
	    var name,value='';
	    var str=window.location.href;
	    var num=str.indexOf("?");
	    str=str.substr(num+1);
	    var arr=str.split("&");
	    for(var i=0;i < arr.length;i++){
	        num=arr[i].indexOf("=");
	        if(num>0){
	            name=arr[i].substring(0,num);
	            if(name.replace(/^\s+|\s+$/g,"") == _name){
	                value=arr[i].substr(num+1);
	                break;
	            }
	        }
	    }
	    return decodeURI(value);
	},
    /*
	身份证验证方法
	*/
 idCardNoUtil:function{
    provinceAndCitys: {11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",
        31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",
        45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",
        65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"},
    powers: ["7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2"],

    parityBit: ["1","0","X","9","8","7","6","5","4","3","2"],

    genders: {male:"男",female:"女"},

    checkAddressCode: function(addressCode){
        var check = /^[1-9]\d{5}$/.test(addressCode);
        if(!check) return false;
        if(idCardNoUtil.provinceAndCitys[parseInt(addressCode.substring(0,2))]){
            return true;
        }else{
            return false;
        }
    },

    checkBirthDayCode: function(birDayCode){
        var check = /^[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$/.test(birDayCode);
        if(!check) return false;
        var yyyy = parseInt(birDayCode.substring(0,4),10);
        var mm = parseInt(birDayCode.substring(4,6),10);
        var dd = parseInt(birDayCode.substring(6),10);
        var xdata = new Date(yyyy,mm-1,dd);
        if(xdata > new Date()){
            return false;//生日不能大于当前日期
        }else if ( ( xdata.getFullYear() == yyyy ) && ( xdata.getMonth () == mm - 1 ) && ( xdata.getDate() == dd ) ){
            return true;
        }else{
            return false;
        }
    },

    getParityBit: function(idCardNo){
        var id17 = idCardNo.substring(0,17);

        var power = 0;
        for(var i=0;i<17;i++){
            power += parseInt(id17.charAt(i),10) * parseInt(idCardNoUtil.powers[i]);
        }

        var mod = power % 11;
        return idCardNoUtil.parityBit[mod];
    },

    checkParityBit: function(idCardNo){
        var parityBit = idCardNo.charAt(17).toUpperCase();
        if(idCardNoUtil.getParityBit(idCardNo) == parityBit){
            return true;
        }else{
            return false;
        }
    },

    checkIdCardNo: function(idCardNo){
//15位和18位身份证号码的基本校验
        var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
        if(!check) return false;
//判断长度为15位或18位
        if(idCardNo.length==15){
            return idCardNoUtil.check15IdCardNo(idCardNo);
        }else if(idCardNo.length==18){
            return idCardNoUtil.check18IdCardNo(idCardNo);
        }else{
            return false;
        }
    },

//校验15位的身份证号码
    check15IdCardNo: function(idCardNo){
//15位身份证号码的基本校验
        var check = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/.test(idCardNo);
        if(!check) return false;
//校验地址码
        var addressCode = idCardNo.substring(0,6);
        check = idCardNoUtil.checkAddressCode(addressCode);
        if(!check) return false;
        var birDayCode = '19' + idCardNo.substring(6,12);
//校验日期码
        return idCardNoUtil.checkBirthDayCode(birDayCode);
    },

//校验18位的身份证号码
    check18IdCardNo: function(idCardNo){
//18位身份证号码的基本格式校验
        var check = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/.test(idCardNo);
        if(!check) return false;
//校验地址码
        var addressCode = idCardNo.substring(0,6);
        check = idCardNoUtil.checkAddressCode(addressCode);
        if(!check) return false;
//校验日期码
        var birDayCode = idCardNo.substring(6,14);
        check = idCardNoUtil.checkBirthDayCode(birDayCode);
        if(!check) return false;
//验证校检码
        return idCardNoUtil.checkParityBit(idCardNo);
    },

    formateDateCN: function(day){
        var yyyy =day.substring(0,4);
        var mm = day.substring(4,6);
        var dd = day.substring(6);
        return yyyy + '-' + mm +'-' + dd;
    },

   //获取信息
    getIdCardInfo: function(idCardNo){
        var idCardInfo = {
            gender:"", //性别
            birthday:"" // 出生日期(yyyy-mm-dd)
        };
        if(idCardNo.length==15){
            var aday = '19' + idCardNo.substring(6,12);
            idCardInfo.birthday=idCardNoUtil.formateDateCN(aday);
            if(parseInt(idCardNo.charAt(14))%2==0){
                idCardInfo.gender=idCardNoUtil.genders.female;
            }else{
                idCardInfo.gender=idCardNoUtil.genders.male;
            }
        }else if(idCardNo.length==18){
            var aday = idCardNo.substring(6,14);
            idCardInfo.birthday=idCardNoUtil.formateDateCN(aday);
            if(parseInt(idCardNo.charAt(16))%2==0){
                idCardInfo.gender=idCardNoUtil.genders.female;
            }else{
                idCardInfo.gender=idCardNoUtil.genders.male;
            }

        }
        return idCardInfo;
    },

    getId15:function(idCardNo){
        if(idCardNo.length==15){
            return idCardNo;
        }else if(idCardNo.length==18){
            return idCardNo.substring(0,6) + idCardNo.substring(8,17);
        }else{
            return null;
        }
    },

    getId18: function(idCardNo){
        if(idCardNo.length==15){
            var id17 = idCardNo.substring(0,6) + '19' + idCardNo.substring(6);
            var parityBit = idCardNoUtil.getParityBit(id17);
            return id17 + parityBit;
        }else if(idCardNo.length==18){
            return idCardNo;
        }else{
            return null;
        }
    },
    setBirthSex:function(idcard){//从身份证设置生日及性别
		var year,month,day,sex;
        var date=new Date();
		switch(idcard.length){
			case 18:
				year=idcard.substr(6,4);
            	month=idcard.substr(10,2);
            	day=idcard.substr(12,2);
            	sex=idcard.substr(16,1);
            	if(sex%2==0){
            		APPLY.input.genderCheck('girl');
	            }else{
	            	APPLY.input.genderCheck('boy');
	            }
            	$(":input[name='ip_birthday']").val(year+"-"+month+"-"+day);
	            $(".li-select, .li-birthday").addClass("disabled");
				break;
			case 15:
				year=idNum.substr(6,4);
	            month=idNum.substr(8,2);
	            day=idNum.substr(10,2);
	            sex=idNum.substr(13,1);
            	if(sex%2==0){
            		APPLY.input.genderCheck('girl');
	            }else{
	            	APPLY.input.genderCheck('boy');
	            }
            	$(":input[name='ip_birthday']").val(year+"-"+month+"-"+day);
	            $(".li-select, .li-birthday").addClass("disabled");
				break;
			default:
				break;
		}
	},
	 /**
	  * 功能说明：页面数据请求加载动画
	  */
	 Load:{
	 	/*
	 	 * 不带蒙层的弹窗
	 	 */
	    add1:function () {
	           if($(".load").length>0){
	              $(".load").remove();
	           }
	           $("body").append('<div class="load"><div class="loadIcon1 center"></div></div>');      
	    },
	    /**
	     * 带蒙层的弹窗
	     */
	    add2:function () {
	        $("body").append('<div class="load"><div class="mask"></div><div class="loadIcon2 center"></div> </div>');
	    },
	    /*
	     * 移除蒙层
	     */
	    remove:function () {
	        $(".load").remove();
	    }
	 },
    //请求回调
//	request:function(){
//		  request: function (Object) {
//		    wx.showLoading({
//		      title: "读取中..",
//		      mask: true
//		    });
//		    var data = Object.data || {};
//		    var header = Object.header || {};
//		    data.siteKey = data.siteKey || this.globalData.siteKey;//店铺名
//		    data.clientID = "9212AC9FFC804F1182834654EDDE0116";//客户端ID
//		    header.clientKey = "wonhero_wxapp"//客户端key
//		    wx.request({
//		      url: Object.url,
//		      data: data,
//		      header: header,
//		      method: Object.method,
//		      dataType: Object.dataType,
//		      success: function (res) {
//		        wx.hideLoading();
//		        typeof Object.success === "function" && Object.success(res);
//		      },
//		      fail: function (res) {
//		        wx.hideLoading();
//		        wx.showToast({
//		          title: Object.failTitle || "读取失败",
//		          icon: "loading"
//		        });
//		        typeof Object.fail === "function" && Object.fail(res);
//		      }
//		    })
//		  }
//	}
	request:function(object){
		$.ajax({
			type:object.type,
			url:object.url,
			dataType:"json",
			contentType:"appliaction/json",
			data:object.data,
			success:function(res){
				
			},
			error:function(){
				
			}
		})
	}
	
	
}
