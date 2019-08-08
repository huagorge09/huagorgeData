/**
 * 段鹏2014-05-07
 * 放置一些公共js
 * **/
;LEM={}
//页面内部页面处理类
LEM.page={
		showPage:function(pageId,callback){
			$('div[data-role="sf-page" ]').hide();
	    	$("#"+pageId).show();
	    	if(null!=callback){
				if (typeof callback == 'string'){
					eval(callback+"()");
				}else{
					callback.call();
				}
			}
		},
		//自动赋值到自定义显示json标签数据
		autoShowPageJsonView : function(jsonData){
			$("span[data-rule='jsonView']").each(function(){
				var objName=$(this).attr("forName");
				var names=objName.split(".");
				var val="";
				if(names.length==1){
					 val=jsonData[objName];
					 $(this).html(val);
				}else if(names.length>1){
					var obj=null;
					for(var i=0,size=names.length;i<size;i++){
						var name=names[i];
						if(i==0){
							obj=jsonData[name];
							if(obj==null){
								return ;
							}
						}else{
							obj=obj[name];
						}

					}
					$(this).html(obj);
				}


			});
		},
		showOverlay : function(){
            $('body').append('<div class="super_loading" style="position: absolute;left: 50%;top: 50%;margin-top: -14px;margin-left: -14px;z-index: 9999999;"><img src="/SL_LEM/grown/grown_resource/icon/preloader.gif"></div><div class="super_overlay" style="background: #fff;position: fixed;left: 0;top: 0;opacity: 0;width: 100%;height: 100%;z-index: 9998;display: none;"></div>')
            $('.super_overlay').show();
        },
        hideOverlay : function (){
            $('body').find('.super_loading').remove();
            $('.super_overlay').hide();
            $('body').find('.super_overlay').remove();
        },
	    go_guidev:function(goUrl){
	    	if($.cookie('cookie_add_guidev2') != '1'){
	    		$.cookie('cookie_add_guidev2', '1', { expires: 60 , path: '/' });
	    		goUrl = ResouresPath + "/SL_LEM/grown/add_insurance/add_guidev2.shtml";
	    	}
	    	return goUrl;
	    }
}
LEM.loadHtml={
		//功能说明:加载指定url对应的页面到指定的地方显示
		//参数说明：url:请求的地址;loadingMessage:加载页面的提示消息;disObject:显示结果的目标对象ID;callback:加载完后回调的函数
		loadUrlToObject:function(url,loadingMessage,params,disObjectId,callback){
			var disObj=$("#"+disObjectId).html($('<div class="loading"></div>').html(loadingMessage));
				jQuery.ajax({
					url: url,
					type: "post",
					dataType: "html",
					contentType:"application/x-www-form-urlencoded; charset=UTF-8",
					data: params,
					complete: function( res, status ) {
						if ( status === "success" || status === "notmodified" ) {
							disObj.html( res.responseText );
							//$.parser.parse(disObj);
							if(null!=callback){
								if (typeof callback == 'string'){
									eval(callback+"()");
								}else{
									callback.call();
								}

								}
						}
					}
			});
		},
		//功能说明:提交表单加载页面到指定的地方显示
		//参数说明：formId:表单ID;loadingMessage:加载页面的提示消息;disObject:显示结果的目标对象ID
		submitFormToObject:function(formId,loadingMessage,disObjectID){
			var form=$("#"+formId);
			var params=form.serialize();
			UW.loadHtml.loadUrlToObject(form.attr("action"),loadingMessage,params,disObjectID);
		}

}
//功能说明:ie会缓存请求，通过加一个随机数字来解决此问题
//参数说明:url请求地址
//checkIdcard:
LEM.util={
  addRandomTermToUrl : function(url){
	var date=new Date();
	if(url.indexOf('?')==-1){
		url=url+"?urlsendtime="+date.getTime();
	}else{
		url=url+"&urlsendtime="+date.getTime();
	}
	return url;
	},
	//获取浏览器的查询参数对应的值 eg http://UNAN_AN1.html?itemId=1111 获取itemId值 用getUrlParam("itemId")
	getUrlParam:function (name)
	{
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
		var r = window.location.search.substr(1).match(reg);  //匹配目标参数
		if (r!=null) return decodeURIComponent(r[2]);
		return ""; //返回参数值
	},
	//获取APP类型是com.sinolife.app 安卓，com.sinolife.iphone 苹果
	//参数 method 函数名称，例如：提示错误信息函数 showToastReq。
	//param 为提示内容，根据不同的method需要扩展可封装对象传入， 例如：var param = {aa:"",bb:""}。
	//callback 回调函数，不需要回调时可以传入null
	getIsAppType : function (method,param,callback){
		var ua = navigator.userAgent.toLowerCase();
		
		//com.sinolife.app 安卓，com.sinolife.iphone 苹果
	    if(ua.indexOf("com.sinolife.app")!=-1) {
		    //alert("method:"+method+"param:"+param);
	    	if(method == "weChatPayReq"){
	    		window.androidAppJsObj.weChatPayReq(param.appId,
								    				param.partnerId,
								    				param.prepayId,
								    				param.nonceStr,
								    				param.timeStamp,
								    				param.sign,
								    				param.methodName);
	    	}else if(method == "getPayContract"){
	    		var strPara = '{'+
				'"appid":"'+param.appid+'",' +
				'"contract_code":"'+ param.contract_code+'",'+
				'"contract_display_account":"'+param.contract_display_account+'",'+
				'"mch_id":"'+param.mch_id+'",'+
				'"notify_url":"'+param.notify_url +'",'+
				'"plan_id":"'+param.plan_id+'",'+
				'"request_serial":"'+ new String(param.request_serial)+'",'+
				'"timestamp":"'+param.timestamp +'",'+
				'"version":"'+ param.version+'",'+
				'"sign":"'+ param.sign + '",'+
				'"return_app":"'+param.return_app+'"}';
		    	window.androidAppJsObj.getPayContract(strPara);
		    }else if(method == "checkWeChatIsInstalledReq"){
	    		window.androidAppJsObj.checkWeChatIsInstalledReq();
	    	}else if(method == "getNetStateReq"){
	    		window.androidAppJsObj.getNetStateReq();
	    	}else if(method == "goMainActivityReq"){
	    		window.androidAppJsObj.goMainActivityReq(param);
	    	}else if(method == "showWaitReq"){
	    		window.androidAppJsObj.showWaitReq();
	    	}else if(method == "closeWaitReq"){
	    		window.androidAppJsObj.closeWaitReq();
	    	}else if(method == "callReq"){
	    		window.androidAppJsObj.callReq(param);
	    	}else if(method == "getSmsCodeReq"){
	    		//alert("method:"+method+"\n"+'mobileNo:'+param.mobileNo+"\n"+"time:"+param.time+"\n"+"marryString:"+param.marryString+"\n"+"callBack:"+param.callBack);
	    		window.androidAppJsObj.getSmsCodeReq(param.mobileNo,
	    		    	param.time,
	    		    	param.marryString,
	    		    	param.callBack);
	    	}else if(method == "quickPayReq"){
	    		window.androidAppJsObj.quickPayReq(param.viewId,
							    				param.sceneId,
							    				param.bizType,
							    				param.bizkey,
							    				param.value,
							    				param.minPayPrem,
							    				param.isPercentage,
							    				param.methodName);
	    	}else if(method == "shareDataReq"){
	    		
	    		if(LEM.util.isAndroidAppVesion()){
	    			window.androidAppJsObj.shareDataReq(param.link,
		    				param.imgUrl,
		    				param.desc,
		    				param.title,
		    				param.shareType,
		    				param.bizType,
		    				param.bizKey,
		    				param.eventId,
		    				param.allowShareChannel,
		    				param.succCallBack
		    				);
	    		}else{
	    			window.androidAppJsObj.shareDataReqNew(param.link,
		    				param.imgUrl,
		    				param.desc,
		    				param.title,
		    				param.shareType,
		    				param.bizType,
		    				param.bizKey,
		    				param.eventId,
		    				param.timeLineTitle == undefined ? null:param.timeLineTitle,
		    				param.timeLineDesc == undefined ? null:param.timeLineDesc,
				    		param.allowShareChannel,
				    		param.succCallBack);
	    		}

	    	} else if (method == "gobackHistoryPhotos") {
	    		window.androidAppJsObj.gobackHistoryPhotos();
	    	} else if (method == "goBackReq") {
	    		window.androidAppJsObj.goBackReq();
	    	}else if(method == "getCurrViewIdReq"){
	    		return window.androidAppJsObj.getCurrViewIdReq();
	    	}else if(method == "gotoAppActivityReq"){
	    		window.androidAppJsObj.gotoAppActivityReq(JSON.stringify(param.android),
	    				"");
	    	}else if(method == "refreshUserInfo"){
	    		window.androidAppJsObj.refreshUserInfo();
	    	}else if(method == "shareImageReq"){
	    		window.androidAppJsObj.shareImageReq(param.smallImageUrl,
	    				param.bigImageUrl,
	    				param.shareType,
	    				param.bizType,
	    				param.bizKey,
	    				param.eventId,
	    				param.allowShareChannel,
	    				param.succCallBack);
	        }else if(method == "getCardInfo"){
	    		window.androidAppJsObj.getCardInfo(param.methodName);
	        }else if(method == "saveToAlbum"){
	    		window.androidAppJsObj.saveToAlbum(param.imaeBase64,param.callBack);
	        }else if(method == "shareImageReqWithBase64"){
	    		window.androidAppJsObj.shareImageReqWithBase64(param.smallImageUrl,
	    				param.bigImageUrl,
	    				param.shareType,
	    				param.bizType,
	    				param.bizKey,
	    				param.eventId,
	    				param.allowShareChannel,
	    				param.succCallBack);
	        }else if(method == "getRecording"){
	    		window.androidAppJsObj.getRecording(param.guide,param.meaning,param.recordingTimeLimit,param.succCallBack);
	        }else {
	    		window.androidAppJsObj.showToastReq(param);
	    	}
	    }else if(ua.indexOf("com.sinolife.iphone")!=-1) {
	    	 callWebViewHandler(method,param,callback);
	    }else{
	    	  return null;
	    }
	},
	/**
	 * 判断安卓版本
	 */
	isAndroidAppVesion:function(){
		var str = "";
		var a = 0;
		var b = 0;
		var c = 0;
		var app = navigator.userAgent.toLowerCase();
		var vesionStr = app.split(';');
		for (var i = 0;i<= vesionStr.length;i++){
			if(vesionStr[i].indexOf('versionname') != -1){
				str = vesionStr[i].substr(11,vesionStr[i].length-1);
				break;
			}
		}
		var vesion = str.split('.');
		a = parseInt(vesion[0]);
		b = parseInt(vesion[1]);
		c = parseInt(vesion[2]);
		var sfVesion = AndroidAppVesion.split('.');
		d = parseInt(sfVesion[0]);
		e = parseInt(sfVesion[1]);
		f = parseInt(sfVesion[2]);

		if (a < d){
			return true;
		}
		if (b < e){
			return true;
		}
		if (c < f){
			return true;
		}
		return false;
	},
	/**
	 * 判断IOS版本
	 */
	isIosAppVesion:function(){
		var str = "";
		var a = 0;
		var b = 0;
		var c = 0;
		var app = navigator.userAgent.toLowerCase();
		var vesionStr = app.split(';');
		for (var i = 0;i<= vesionStr.length;i++){
			if(vesionStr[i].indexOf('versionname') != -1){
				str = vesionStr[i].substr(11,vesionStr[i].length-1);
				break;
			}
		}
		var vesion = str.split('.');
		a = parseInt(vesion[0]); 
		b = parseInt(vesion[1]); 
		c = parseInt(vesion[2]); 
		var sfVesion = IOSAppVesion.split('.');
		d = parseInt(sfVesion[0]);
		e = parseInt(sfVesion[1]);
		f = parseInt(sfVesion[2]);
		if (a < d){
			return true;
		}
		if (b < e){
			return true;
		}
		if (c < f){
			return true;
		}
		return false;
	}
}
LEM.date={
		//获取当天的日期格式
		getCurrDateStr:function(){
			var date = new Date();
			return LEM.date.dateToStr(date);
		},
		//功能说明： Date类型转换成字符串
		//参数说明:date,js日期对象
		 dateToStr:function(date){
			var year = date.getFullYear();
			var month = date.getMonth()+1;
			var dateVal = date.getDate();
			return year + '-' + (month > 9 ? month : '0' + month) + '-' + (dateVal > 9 ? dateVal : '0' + dateVal);
		},
		 objToDateStr : function(value){
				if(value!=null){
			        var date =new Date();
			       	date.setTime(value.time);
					return LEM.date.dateToStr(date);
			    }else{
					return "";
			    }
		}
}

LEM.check={
		checkIdcard:function(idcard){
		var Errors=new Array(
				"1",
				"身份证号码位数不对!",
				"身份证号码出生日期超出范围或含有非法字符!",
				"身份证号码校验错误!",
				"身份证地区非法!"
				);
				var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}


				var idcard,Y,JYM;
				var S,M;
				var idcard_array = new Array();
				idcard_array = idcard.split("");
				//地区检验
				if(area[parseInt(idcard.substr(0,2))]==null) return Errors[4];
				//身份号码位数及格式检验
				switch(idcard.length){
				case 15:
				if ( (parseInt(idcard.substr(6,2))+1900) % 4 == 0 || ((parseInt(idcard.substr(6,2))+1900) % 100 == 0 && (parseInt(idcard.substr(6,2))+1900) % 4 == 0 )){
				ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//测试出生日期的合法性
				} else {
				ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//测试出生日期的合法性
				}
				if(ereg.test(idcard)) return Errors[0];
				else return Errors[2];
				break;
				case 18:
				//18位身份号码检测
				//出生日期的合法性检查
				//闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
				//平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
				if ( parseInt(idcard.substr(6,4)) % 4 == 0 || (parseInt(idcard.substr(6,4)) % 100 == 0 && parseInt(idcard.substr(6,4))%4 == 0 )){
				ereg=/^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
				} else {
				ereg=/^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
				}
				if(ereg.test(idcard)){//测试出生日期的合法性
				//计算校验位
				S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
				+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
				+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
				+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
				+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
				+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
				+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
				+ parseInt(idcard_array[7]) * 1
				+ parseInt(idcard_array[8]) * 6
				+ parseInt(idcard_array[9]) * 3 ;
				Y = S % 11;
				M = "F";
				JYM = "10X98765432";
				M = JYM.substr(Y,1);//判断校验位
				if(M == idcard_array[17]) return Errors[0]; //检测ID的校验位
				else return Errors[3];
				}
				else return Errors[2];
				break;
				default:
				return Errors[1];
				break;
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
	isRelName:function(v) {//姓名含有中英文
		var re = new RegExp("^[A-Za-z\\u4e00-\\u9fa5]+$");
		if (re.test(v)) return true;
		return false;
	},
	/**
	* 检测姓名格式方法
	* 姓名前后不能有空格
	* 姓名前后不能有符号“·”
	* 中文姓名可包含符号“·”
	* 英文姓与名之间可以包含空格
	*/
	isRelNameNew: function(name) {
		if (name.length < 2) return false;
	    var arr = name.split(''), length = arr.length, i = 0, flag = true;
	    if (arr[0] == ' ' || arr[length - 1] == ' ') {
	        flag = false;
	    } else if (arr[0] == '·' || arr[length - 1] == '·') {
	        flag = false;
	    } else {
	        for (var j = 0; j < arr.length; j++) {
	            if (arr[j] == '·' || /[\u4E00-\u9FA5]/g.test(arr[j])) {
	                i += 2;
	            } else if (/[a-zA-Z\s]/g.test(arr[j])) {
	                i++;
	            } else {
	                i -= 10;
	            }
	        }
	    }

	    // 中文和英文名字都能通过验证
	    if (flag && !(i == length || i == length * 2)) {
	        flag = false;
	    }
	    // 仅中文名字能通过验证
	    /*if (flag == 0 && i != length * 2) {
	        flag = false;
	    }*/

	    // 过滤同时输入两个或以上符号“·”或空格
	    if (flag && /[·]{2,}|[\s]{2,}/.test(name)) {
	        flag = false;
	    }

	    return flag;
	},

	checkBirthAndSex:function(birth,sex,idCard_1){//校验身份证号码和页面上面的性别和生日是否相符
		var Errors=new Array(
				"1",
				"您的出生年月/性别与身份证不符，请重新输入"
				);
		var year,month,day,sex;//身份证号码截取的年月日
	    var sexNo;
	    var idcard = "";
	    idcard=idCard_1;
		if(idcard.length==18){
				year=idcard.substr(6,4);
	        	month=idcard.substr(10,2);
	        	day=idcard.substr(12,2);
	        	card_sex=idcard.substr(16,1);
	        	if(card_sex%2==0){//偶数为女性
	        		if(sex!='女'){//如果传过来的性别不是为‘女’，就报错
	        			LEM.util.getIsAppType("showToastReq","您的出生年月/性别与身份证不符，请重新输入。",null);
	        			return Errors[1];
	        		}
	            }else{//奇数为男性
	            	if(sex!='男'){//如果传过来的性别不是为‘男’，就报错
	        			LEM.util.getIsAppType("showToastReq","您的出生年月/性别与身份证不符，请重新输入。",null);
	        			return Errors[1];
	        		}
	            }
		}else if(idcard.length==15){
				year=idNum.substr(6,4);
	            month=idNum.substr(8,2);
	            day=idNum.substr(10,2);
	            card_sex=idNum.substr(13,1);
	            if(card_sex%2==0){//偶数为女性
	        		if(sex!='女'){//如果传过来的性别不是为‘女’，就报错
	        			LEM.util.getIsAppType("showToastReq","您的出生年月/性别与身份证不符，请重新输入。",null);
	        			return Errors[1];
	        		}
	            }else{//奇数为男性
	            	if(sex!='男'){//如果传过来的性别不是为‘男’，就报错
	        			LEM.util.getIsAppType("showToastReq","您的出生年月/性别与身份证不符，请重新输入。",null);
	        			return Errors[1];
	        		}
	            }
		}
		card_birth = year+"-"+month+"-"+day;
		if(birth != card_birth){
			LEM.util.getIsAppType("showToastReq","您的出生年月/性别与身份证不符，请重新输入。",null);
			return Errors[1];
		}
		return Errors[0];
	}
}
/**
 * 放置输入控制相关函数
 */
LEM.input={
		//只能输入数字
		numberInput:function(input){
				var val=event.keyCode;
				if(val>=48&&val<=57){
					window.event.returnValue = true;
				}else{
					window.event.returnValue = false;
				}
		},//只能输入电话号码
		phoneInput:function (input){
			var val=event.keyCode;
			if(val>=48&&val<=57||val==45){
				window.event.returnValue = true;
			}else{
				window.event.returnValue = false;
			}
	},//自动剔除输入框的左右空格
	trimInputValue:function(input){
		var inputObj=$(input);
		var inputVal=inputObj.val();
		if(inputVal!=null){
			inputObj.val($.trim(inputVal));
		}
	},//checkbox全选按钮checkObj:控件本身,objName:需要选中的checkbox name名称
	 checkBoxAll:function(checkObj,objName){
		if(checkObj.checked){
			$(":input[name='"+objName+"']").attr("checked","checked");
		}else{
			$(":input[name='"+objName+"']").attr("checked","");
		}
	},//证件类型选择idTypeSleId证件类型选择Id,idNoId,bridayId,sexName
	idNoInput:function(idTypeSleId,idNoId,bridayId,sexName){
		$("#"+idTypeSleId).bind("change",function(){
			var idType=$("#"+idTypeSleId).val();
			var $idNo=$("#"+idNoId);
			var $briday=$("#"+bridayId);
			var $sex=$(":input[name='"+sexName+"']");
			//如果是身份证再进行验证
			if(idType=='01'){
				//$briday.attr("readOnly","readOnly");
				//$sex.attr("readOnly","readOnly");
			}else{
				$("input[name='app.birthday']").prop("disabled",false);
	            		$("input:radio[name='app.gender']").prop("disabled",false);
				//$briday.removeAttr("readOnly");
				//$sex.removeAttr("readOnly");
			}
		});
	},
	initRadioValue:function(inputName,value){
		$(":input[name='"+inputName+"']").each(function(i){
			if($(this).val()==value){
				$(this).attr("checked","checked");
			}else{
				$(this).removeAttr("checked");
			}
		});
	}
}
/**
 * 表单验证相关函数
 */
LEM.form={
		//objId 验证id下所有验证控件
		validate:function(objId){
			var canFalg=false;
			var obj=$("#"+objId);
			obj.find(".sfValidatebox-text:not(:disabled)").each(function(index){
				$(this).sfValidatebox("validate");
			});
			var invalidbox = obj.find('.sfValidatebox-invalid');
			var t=invalidbox.filter(':not(:disabled):first');

			if(invalidbox.length>0){
				canFalg= false;
				if(t){
					t.focus();
				}
			}else{
				 canFalg=true;
			}
			return canFalg;
	}

}
function showLock(lockId,divId){
	var divs=document.getElementById(divId);
	divs.style.display="block";
	var v_left=(document.body.clientWidth-divs.clientWidth)/2 + $(document).scrollLeft();
	var v_top=(document.body.clientHeight-divs.clientHeight+$(document).scrollTop())/2;
	divs.style.left=v_left+'px';
	divs.style.top=v_top+'px';
	$("#"+lockId).css("height",screen.height + $(document).scrollTop());
	$("#"+lockId).css("width",screen.width + $(document).scrollLeft());
	$("#"+lockId).show();
	$(document.body).css("overflow","hidden");
}

/**
 * 建立IOS webView与js的通信桥梁
 * @author dengzhiwei.wb
 * @param callback
 * @returns
 */
function connectWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) { return callback(WebViewJavascriptBridge); }
    if (window.WVJBCallbacks) { return window.WVJBCallbacks.push(callback); }
    window.WVJBCallbacks = [callback];
    var WVJBIframe = document.createElement('iframe');
    WVJBIframe.style.display = 'none';
    WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
    document.documentElement.appendChild(WVJBIframe);
    setTimeout(function() { document.documentElement.removeChild(WVJBIframe) }, 0)
}
/**
 * 建立IOS webView与js的通信桥梁发起函数
 * @author dengzhiwei.wb
 * @param method
 * @param params
 * @param callback
 */
function callWebViewHandler(method,params,callback){
	connectWebViewJavascriptBridge(function(bridge) {
		if(method == "getPayContract"){
			params.notify_url = encodeURI(params.notify_url);
		}
		bridge.callHandler(method, params, callback);
	})
}

/* 增加友盟监控统计 2016-12-22 */
function _UM() {
	if (!window.umengID) return;
	var a = navigator.userAgent.toLocaleLowerCase(), b = document, c = b.body;
	if (a.indexOf('com.sinolife.iphone') == -1 && a.indexOf('com.sinolife.app') == -1) return;
	if (!c) {setTimeout(_UM, 200);return;}
	var d = b.createElement('div'), e = b.createElement('script');
	d.style.display = 'none';
	c.appendChild(d);
	e.src = 'https://s11.cnzz.com/z_stat.php?id='+umengID+'&web_id='+umengID;
	d.appendChild(e);
};
_UM();
