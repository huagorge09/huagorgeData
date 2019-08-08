/*****************************************************************
                  jQuery Validate扩展验证方法  (linjq)       
*****************************************************************/
$(function(){

	/**
	 * 重写validate中的required方法
	 */
	$.extend( $.validator.methods, {
		required: function( value, element, param ) {
			// Check if dependency is met
			if ( !this.depend( param, element ) ) {
				return "dependency-mismatch";
			}
			if ( element.nodeName.toLowerCase() === "select" ) {

				// Could be an array for select-multiple or a string, both are fine this way
				var val = $( element ).val();
				return val && val.length > 0;
			}
			if ( this.checkable( element ) ) {
				return this.getLength( value, element ) > 0;
			}
			return $.trim(value).length > 0 ;
		}
	});
    // 判断整数value是否大于0
    jQuery.validator.addMethod("isIntGtZero", function(value, element) { 
         value=parseInt(value);      
         return this.optional(element) || value>0;       
    }, "整数必须大于0"); 
    
    // 判断数值类型，包括整数和浮点数
    jQuery.validator.addMethod("isNumber", function(value, element) {       
         return this.optional(element) || /^[-\+]?\d+$/.test(value) || /^[-\+]?\d+(\.\d+)?$/.test(value);       
    }, "匹配数值类型，包括整数和浮点数");  
 
    /** 自定义函数的验证 add by weicb
     * @author ex-weicb
     * @param func，自定义校验函数，校验通过返回true，校验不通过返回false
     * @param optional，可选参数，是否是表单控件的值不为空的时候才触发校验，false代表是，true代表否，默认为是
     * 使用示例：
	 * rules:{
	 *		element: {
	 *		    validateByFunc: {func:function(value,element){
	 *		    			if(validatePass){//to implements some rules for yourself.
	 *		    				return true;
	 *		    			}
	 *		    			return false;
	 *				}[,optional:true]
	 *			}
	 *		}
	 *	}
     * 一般用于特别个性化的只针对一个业务的非通用校验
     */
    jQuery.validator.addMethod("validateByFunc", function(value, element,params) { 
    	if(!params.func){//自定义校验函数没有定义
    		return false;
    	}
    	//params.optional：是否是表单控件的不为空的时候才触发校验，false代表是，true代表否
    	//默认不为空的时候才触发校验
    	if(!params.optional){
    		return this.optional(element) || params.func(value,element);
    	}
    	return params.func(value,element);
		
    }, "不满足条件！"); 
    /**
     * 公共校验方法 不能等于
     * @author weicb
     */
    jQuery.validator.addMethod("notEquals", function(value, element,params) { 
    	return this.optional(element) || value!=params[0];
		
    }, "输入的值不能等于{0}！"); 
    /**
     * 公共校验方法 不能等于某个元素的值
     * @author weicb
     */
    jQuery.validator.addMethod("notEquals2Element", function(value, element,params) { 
    	return this.optional(element) || value!=$("#"+params[0]).val();
		
    }, "输入的值不能等于{0}元素的值！");
    jQuery.validator.addMethod("isFloatGtZero", function(value, element) { 
        value=parseFloat(value);      
        return this.optional(element) || value>0;       
   }, "小数必须大于0"); 
    // 正整数
    jQuery.validator.addMethod("isInt", function(value, element) { 
         var reg = /(^[0-9]\d*$)/;
         var value = $.trim(value);
         var flag=true;
         if (reg.exec(value) == null) {
             flag=false;
         }
         return this.optional(element) || flag;       
    }, "必须为正整数"); 
    // 正整数
    jQuery.validator.addMethod("isNoNegaNum", function(value, element) { 
    	var reg = /^(\d+)(\.\d+)?/;
    	var flag=true;
	    if (reg.exec(value) == null) {
	        flag=false;
	    }
    	return this.optional(element) || flag;       
    }, "必须为非负数"); 
    // 正整数
    jQuery.validator.addMethod("isNum", function(value, element) { 
    	 //var reg = /^\d+$/;
    	var reg = /^(-?\d+)(\.\d+)?/;
        value = $.trim(value.replace(/,/g, ""));
        var preRaseAmtNum = new Number(value);
        var flag=true;
        if (reg.exec(preRaseAmtNum) == null) {
            flag=false;
        }
    	return this.optional(element) || flag;       
    }, "必须为数字"); 
    //字符超长：一个汉字相当两个字符处理
    jQuery.validator.addMethod("maxlength4Byte", function(value, element,param) { 
   	 //var reg = /^\d+$/;
       var flag=true;
       value=value.replace(/ /ig,'a');//空格不需要处理为两个
       var length=value.replace(/[^x00-xff]/ig,'aa').length;
       if (length>param) {
    	   flag=false;
       }
   	return flag;
   }, "您输入的文本不能大于{0}个字符（一个汉字代表两个字符）"); 
    /**
     * 格式化数字number(15,2)形式的校验
     * @param value
     * @returns {Boolean}
     */
    jQuery.validator.addMethod("fmtNum152", function(value, element,param) { 
    	//var reg = /^\d+$/;
    	var flag=true;
    	if(value){
    		var preRaseAmt = value.replace(/,/g, "");
    		var preRaseAmtNum = new Number(preRaseAmt);
    		if (checkStrIsNum(preRaseAmtNum)) {
    			var amtArrs = preRaseAmt.split(".");
    			var regInt = new RegExp("^[0-9]{0,13}$");
    			var regDec = new RegExp("^([0-9]{0,2})?$");
    			if (!amtArrs[0].match(regInt) || (amtArrs[1] != null && !amtArrs[1].match(regDec))) {
    				flag=false;
    			} else {
    				flag=true;
    			}
    		}
    	}
    	return this.optional(element) || flag;
    }, "长度最多13位整数加2位小数"); 
    /**
     * 格式化数字number(13,8)形式的校验
     * @param id 文本框的id
     * promptInf  提示信息
     * @returns {Boolean}
     */
    jQuery.validator.addMethod("fmtNumerical138", function(value, element,param) { 
    	//var reg = /^\d+$/;
    	var flag=true;
    	if(value){
    		var preRaseAmt = value.replace(/,/g, "");
    		var preRaseAmtNum = new Number(preRaseAmt);
    		if (checkStrIsNum(preRaseAmtNum)) {
    			var amtArrs = preRaseAmt.split(".");
    	        var regInt = new RegExp("^[0-9]{0,5}$");
    	        var regDec = new RegExp("^([0-9]{0,8})?$");
    			if (!amtArrs[0].match(regInt) || (amtArrs[1] != null && !amtArrs[1].match(regDec))) {
    				flag=false;
    			} else {
    				flag=true;
    			}
    		}
    	}
    	return this.optional(element) || flag;
    }, "长度最多5位整数加8位小数");
    /**
     * 格式化数字number(6)形式的校验
     * @param id 文本框的id
     * promptInf  提示信息
     * @returns {Boolean}
     */
    jQuery.validator.addMethod("fmtNum6", function(value, element,param) { 
    	//var reg = /^\d+$/;
    	var flag=true;
    	if(value){
    		var preRaseAmt = value.replace(/,/g, "");
    		var preRaseAmtNum = new Number(preRaseAmt);
    		if (checkStrIsNum(preRaseAmtNum)) {
    	        var regInt = new RegExp("^[0-9]{0,6}$");
    			if (!preRaseAmt.match(regInt)) {
    				flag=false;
    			} else {
    				flag=true;
    			}
    		}
    	}
    	return this.optional(element) || flag;
    }, "整数长度最多6位"); 
    
    /**
     * 富文本编辑器非空校验，IE删除完内容后，还保留p、br标签
     */
    jQuery.validator.addMethod("kindEditorRequire", function(value, element, params) {
    	//去除<p> <br> 空白字符
    	if (value) value = value.replace(/<(\/*)p>/g, "").replace(/<br(\s\/*)>/g, "").replace(/\s*/g, "");
    	return value.length > 0;
    }, "不能为空");
    
    /*
     * Translated default messages for the jQuery validation plugin.
     * Locale: ZH (Chinese, 中文 (Zhōngwén), 汉语, 漢語)
     */
    $.extend($.validator.messages, {
    	required: "该字段为必填项",
    	remote: "请修正此字段",
    	email: "请输入有效的电子邮件地址",
    	url: "请输入有效的网址",
    	date: "请输入有效的日期",
    	dateISO: "请输入有效的日期 (YYYY-MM-DD)",
    	number: "请输入有效的数字",
    	digits: "只能输入数字",
    	creditcard: "请输入有效的信用卡号码",
    	equalTo: "你的输入不相同",
    	extension: "请输入有效的后缀",
    	maxlength: $.validator.format("最多可以输入 {0} 个字符"),
    	minlength: $.validator.format("最少要输入 {0} 个字符"),
    	rangelength: $.validator.format("请输入长度在 {0} 到 {1} 之间的字符串"),
    	range: $.validator.format("请输入范围在 {0} 到 {1} 之间的数值"),
    	max: $.validator.format("请输入不大于 {0} 的数值"),
    	min: $.validator.format("请输入不小于 {0} 的数值")
    });
    
    
    jQuery.validator.addMethod("maxValue", function(value, element,param) { 
        var max=param;
        var flag=true;
		if(value){
			value=parseFloat(value);      
			 if(value>=max){
				 if(value!=0)
					 flag=false
			 }
		 }
        return this.optional(element) || flag;       
   }, "不能超过最大长度");
    /**
     * 	判断是否为fundId
     */
    jQuery.validator.addMethod("isFundID", function(value, element) {
    	  return this.optional(element) || /^[0-9a-zA-Z]*$/g.test(value);       
    	}, "只能为字母或数字的组合");

});

