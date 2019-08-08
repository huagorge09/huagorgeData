var aCity = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外" }
/**
 * 调用方式(Invoke Way):
 * 1、legion.regValidater.方法名(需验证的值);，此方法返回true或false。
 * 2、legion.regValidater.is(legion.regEx.表达式名, 需验证的值);，此方法返回true或false。
 * 3、legion.regEx.表达式名.test(需验证的值);，此方法返回true或false。
 */
var legion = {
	//正则表达式
	regEx : {
		//是否是中文，验证规则：1.只能是UniCode编码中的汉字范围[\u2E80-\u9FFF](注意：。《》、这四个中文符号以及中文空格也在其中)，2.长度为1位或以上
		isChinese : /^((?!\s|。|《|》|、)[\u2E80-\u9FFF])+$/,
		//是否有中文，验证规则：1.有1个和多个UniCode编码中的汉字范围
		hasChinese : /^(([^\u2E80-\u9FFF]|\s|。|《|》|、)*((?!\s|。|《|》|、)[\u2E80-\u9FFF])+([^\u2E80-\u9FFF]|\s|。|《|》|、)*)+$/,
		//是否是特殊字符，验证规则：1.只能是特殊字符(包括空字符、全角字符与数字)，2.长度为1位或以上
		isSpacialChar : /^([^\u2E80-\u9FFFa-zA-Z0-9]|\s|。|《|》|、)+$/,
		//是否有特殊字符，验证规则：1.有1个和多个特殊字符(包括空字符、全角字符与数字)
		hasSpacialChar : /^(((?!\s|。|《|》|、)[\u2E80-\u9FFFa-zA-Z0-9])*([^\u2E80-\u9FFFa-zA-Z0-9]|\s|。|《|》|、)+((?!\s|。|《|》|、)[\u2E80-\u9FFFa-zA-Z0-9])*)+$/,
		//是否为姓名，验证规则：1.前后不能有空字符，2.不能有数字和特殊字符，3.首字符为中文，4.非首字符可为字母，5.长度为2~10位
		isName : /^((?!\s|。|《|》|、)[\u2E80-\u9FFF])((?!\s|。|《|》|、)[\u2E80-\u9FFFa-zA-Z]){1,9}$/,
		//是否为手机号码，验证规则：1.前后不能有空字符，2.只能是数字，3.首字符为1，4.第二位字符为2~8之中的一个，5.长度为11位
		isMobilePhoneNumber : /^1[3-8]\d{9}$/,
		//是否为短信验证码，验证规则：1.前后不能有空字符，2.只能是数字，3.长度为6位
		isMsgValidateCode : /^\d{6}$/,
		//是否为15位身份证号码，验证规则：1.前后不能有空字符，2.只能是数字，3.长度为15位
		isIdCardNumber_15 : /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/,
		//是否为18位身份证号码，验证规则：1.前后不能有空字符，2.前17位只能是数字，3.末位只能是数字或大写字母，4.长度为18位
		isIdCardNumber_18 : /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[A-Z])$/,
		//是否为其它证件号码，验证规则：1.前后不能有空字符，2.只能是字母或数字，3.长度为4~30位
		isOtherCertificateNumber : /^[a-zA-Z0-9]{4,30}$/,
		//是否为邮政编码，验证规则：由6位数字组成
		isPostCode : /^\d{6}$/,
		//是否数字
		hasNumber : /^(\D*\d+\D*)+$/
	},
	//正则验证器
	regValidater : {
		//通用验证，参数列表[regEx:是legion.regEx.*, val:是需验证的值]
		is : function(regEx, val) {
			if (regEx.test(val)) return true; else return false;
		},
		//去除前后空格后，是否是空，参数val是需验证的值
		isNull4Trim : function(val) {
			if (null == val || "null" == $.trim(val) || "NULL" == $.trim(val) || "Null" == $.trim(val) || "" == $.trim(val)) return true; else return false;
		},
		//是否是中文，参数val是需验证的值
		isChinese : function(val) {
			if (legion.regEx.isChinese.test(val)) return true; else return false;
		},
		//是否有中文，参数val是需验证的值
		hasChinese : function(val) {
			if (legion.regEx.hasChinese.test(val)) return true; else return false;
		},
		//是否是特殊字符，参数val是需验证的值
		isSpacialChar : function(val) {
			if (legion.regEx.isSpacialChar.test(val)) return true; else return false;
		},
		//是否有特殊字符，参数val是需验证的值
		hasSpacialChar : function(val) {
			if (legion.regEx.hasSpacialChar.test(val)) return true; else return false;
		},
		//是否为姓名，参数val是需验证的值
		isName : function(val) {
			if (legion.regEx.isName.test(val)) return true; else return false;
		},
		//是否为手机号码，参数val是需验证的值
		isMobilePhoneNumber : function(val) {
			if (legion.regEx.isMobilePhoneNumber.test(val)) return true; else return false;
		},
		//是否为短信验证码，参数val是需验证的值
		isMsgValidateCode : function(val) {
			if (legion.regEx.isMsgValidateCode.test(val)) return true; else return false;
		},
		//是否为15位身份证号码，参数val是需验证的值
		isIdCardNumber_15 : function(val) {
			if (legion.regEx.isIdCardNumber_15.test(val)) return true; else return false;
		},
		//是否为18位身份证号码，参数val是需验证的值
		isIdCardNumber_18 : function(val) {
			if (legion.regEx.isIdCardNumber_18.test(val)) return true; else return false;
		},
		//是否为其它证件号码，参数val是需验证的值
		isOtherCertificateNumber : function(val) {
			if (legion.regEx.isOtherCertificateNumber.test(val)) return true; else return false;
		},
		//是否为身份证号码（包括15位、18位身份证号码），参数val是需验证的值
		isIdCardNumber : function(val) {
			if (legion.regEx.isIdCardNumber_15.test(val) || legion.regEx.isIdCardNumber_18.test(val)) return true; else return false;
		},
		//是否为证件号码（包括15位、18位身份证号码和其它证件号码），参数val是需验证的值
		isCertificateNumber : function(val) {
			if (legion.regEx.isIdCardNumber_15.test(val) || legion.regEx.isIdCardNumber_18.test(val) || legion.regEx.isOtherCertificateNumber.test(val)) return true; else return false;
		},
		//是否为身份证号码，招商基金自带的身份证校验函数，参数val是需验证的值
		isIdCardNumber4ZSJJ : function(val) {
			var iSum = 0;
			var info = "";
			if (!/^\d{17}(\d|x)$/i.test(val))
				return "证件号码填写错误，请重新核对填写";
			val = val.replace(/x$/i, "a");
			if (aCity[parseInt(val.substr(0, 2))] == null)
				return "你的身份证地区非法";
			sBirthday = val.substr(6, 4) + "-" + Number(val.substr(10, 2)) + "-"
					+ Number(val.substr(12, 2));
			var d = new Date(sBirthday.replace(/-/g, "/"));
			if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d
					.getDate()))
				return "身份证上的出生日期非法";
			for ( var i = 17; i >= 0; i--)
				iSum += (Math.pow(2, i) % 11) * parseInt(val.charAt(17 - i), 11);
			if (iSum % 11 != 1)
				return "你输入的身份证号非法";
			return true;
		},
		//是否为邮政编码，参数val是需验证的值
		isPostCode : function(val) {
			if (legion.regEx.isPostCode.test(val)) return true; else return false;
		},
		hasNumber : function(val) {
			if (legion.regEx.hasNumber.test(val)) return true; else return false;
		} 
	},
	//自动格式化
	autoFormat : {
		mobile : function(jQueryObj) {
			jQueryObj.bind({
				focus : function() {
					$(this).val($(this).val().replace(/\s/g, ""));
					$(this).select();
				},
				blur : function() {
					$(this).val($(this).val().replace(/\s/g, "").replace(/\D/g, "").replace(/(\d{3})(\d*)/g, "$1 $2").replace(/(\d{4})(\d*)/g, "$1 $2")); 
				}
			});
		},
		bankCardNumber : function(jQueryObj) {
			jQueryObj.bind({
				focus : function() {
					$(this).val($(this).val().replace(/\s/g, ""));
					$(this).select();
				},
				blur : function() {
					$(this).val($(this).val().replace(/\s/g, "").replace(/\D/g, "").replace(/(\d{4})/g, "$1 "));
				}/*,
				keyup : function(even) {
					if (even.which === 37 || even.which === 39) {//忽略左右键
						return; 
					}
					$(this).val($(this).val().replace(/\s/g, "").replace(/\D/g, "").replace(/(\d{4})/g, "$1 "));
				}*/
			});
		}
	},
	//获取自动格式化输入框的除空格值，参数列表：obj是绑定了自动格式化事件的输入框jQuery对象
	getParsedVal4AutoFormat : function(obj) {
		return obj.val().replace(/\s/g, '').replace(/\D/g, "");
	}
}