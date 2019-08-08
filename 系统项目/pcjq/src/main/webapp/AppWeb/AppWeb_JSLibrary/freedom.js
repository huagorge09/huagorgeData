/**
 * 数组集合
 */
function ArrayList() {
	//元素集
	var elements = [];
	//元素个数
	var size = 0;
	/**
	 * 获取元素个数
	 * @return JS.Number
	 */
	this.size = function () {
		return size;
	}
	/**
	 * 是否为空
	 * @return JS.Boolean
	 */
	this.isEmpty = function () {
		return size == 0;
	}
	/**
	 * 根据下标获取元素
	 * @param _index 下标
	 * @return 该下标所指向的元素
	 */
	this.get = function (_index) {
		if (!isInRange(_index)) return null;
		return elements[_index];
	}
	/**
	 * 获取所有元素
	 * @return JS.Array
	 */
	this.getAll = function () {
		return elements;
	}
	/**
	 * 根据下标设置元素
	 * @param _index 下标
	 * @param _element 新元素
	 * @return 老元素
	 */
	this.set = function (_index, _element) {
		if (!isInRange(_index)) return null;
		var oldValue = elements[_index];
		elements[_index] = _element;
		return oldValue;
	}
	/**
	 * 设置所有元素
	 * @param _collection 新元素集
	 * @return 老元素集
	 */
	this.setAll = function (_collection) {
		if ($.type(_collection) != "array") return null;
		var oldElements = elements;
		elements = _collection;
		size = _collection.length;
		return oldElements;
	}
	/**
	 * 添加元素
	 * @param _obj 需要添加的元素
	 * @return JS.Boolean
	 */
	this.add = function (_obj) {
		elements[size++] = _obj;
		return true;
	}
	/**
	 * 添加一个元素集，暂仅支持：JS数组、ArrayList
	 * @param _collection 需要添加的元素集
	 * @return JS.Boolean - 当增加的个数大于0时返回true，否则返回false
	 */
	this.addAll = function (_collection) {
		var thiz = this;
		var addCount = 0;
		if ($.type(_collection) == "array") {
			$.each(_collection, function (_i, _v) {
				thiz.add(_v);
				addCount ++;
			});
		} else if (_collection instanceof ArrayList) {
			$.each(_collection.getAll(), function (_i, _v) {
				thiz.add(_v);
				addCount ++;
			});
		}
		return addCount != 0;
	}
	/**
	 * 根据下标删除元素
	 * @param _index 下标
	 * @return 被删除的元素
	 */
	this.remove = function (_index) {
		if (!isInRange(_index)) return null;
		var oldValue = elements[_index];
		elements = $.grep(elements, function(_v, _i) {
			return _i == _index;
		}, true);
		size--;
		return oldValue;
	}
	/**
	 * 删除所有元素
	 * @return 被删除的元素集
	 */
	this.removeAll = function () {
		return this.setAll([]);
	}
	/**
	 * 当前元素集打印
	 * @return JS.String
	 */
	this.toString = function () {
		if (this.isEmpty()) return "[]";
		var str = "[";
		$.each(elements, function (_i, _v) {
			if (_v instanceof ArrayList) {
				str += _v.toString();//要加下标请用(_i + ":" + _v.toString());
			} else if (Validater.isBooleanObj(_v) || Validater.isNumberObj(_v) || Validater.isStringObj(_v) || Validater.isDateObj(_v) || Validater.isRegExpObj(_v) || Validater.isFunctionObj(_v)) {
				str += _v;//要加下标请用(_i + ":" + _v);
			} else if (Validater.isArrayObj(_v)) {
				var arrayList = new ArrayList();
				arrayList.addAll(_v);
				str += arrayList.toString();//要加下标请用(_i + ":" + array.toString());
			} else if (Validater.isJsonObj(_v)) {
				str += JsonHelper.toString(_v);//要加下标请用(_i + ":" + JsonHelper.toString(_v));
			}
			str += ", ";
		});
		//去掉最后的逗号和空格
		str = str.substring(0, str.length-2);
		str += "]";
		return str;
	}
	/**
	 * 是否在范围里
	 * @param _index 下标
	 * @return JS.Boolean
	 */
	var isInRange = function (_index) {
		return rangeCheck(_index);
	}
	/**
	 * 范围检查
	 * @param _index 下标
	 * @return JS.Boolean
	 */
	var rangeCheck = function (_index) {
		var isInRange = true;
		if (_index >= size) {
			isInRange = false;
			ExceptionHelper.printStack("数组下标越界(Index: " + _index + ", Size: " + size + ")");
		}
		return isInRange;
	}
}
function JQMap() {
	
}

/**
 * JSON键值对
 * 补充说明：键(key)有非法转换处理，即非命名规范键处理(支持以数字开头(包括纯数字)key、半角特殊字符key、支持中文key)
 */
function JsonMap() {
	//键值对集
	var map = {};
	//元素个数
	var size = 0;
	/**
	 * 获取元素个数
	 * @return JS.Number
	 */
	this.size = function () {
		return size;
	}
	/**
	 * 是否为空
	 * @return JS.Boolean
	 */
	this.isEmpty = function () {
		return size == 0;
	}
	/**
	 * 是否包含键
	 * @param _key 键
	 * @return JS.Boolean
	 */
	this.containsKey = function (_key) {
		if (!isValidKey(_key)) return ;
Printer.info("JsonMap.containsKey.before._key : " + _key);
		_key = _keyTransition(_key);
Printer.info("JsonMap.containsKey.after._key : " + _key);
		var isHas = false;
		$.each(map, function (_k, _v) {
			if (_k == _key) isHas = true;
		});
		return isHas;
	}
	/**
	 * 是否包含值
	 * @param _value 值
	 * @return JS.Boolean
	 */
	this.containsValue = function (_value) {
		var isHas = false;
		$.each(map, function (_k, _v) {
			if (_v == _value) isHas = true;
		});
		return isHas;
	}
	/**
	 * 添加键值对
	 * @param _key 键
	 * @param _value 值
	 */
	this.put = function (_key, _value) {
		if (!isValidKey(_key)) return ;
Printer.info("JsonMap.put.before._key : " + _key);
		_key = _keyTransition(_key);
Printer.info("JsonMap.put.after._key : " + _key);
		var duplicate = Freedom.makeDuplicate(_value);
		eval("map." + _key + " = duplicate;");
		size ++;
	}
	/**
	 * 添加键值对集
	 * @param _map 键值对集
	 */
	this.putAll = function (_map) {
		var thiz = this;
		if (_map instanceof JsonMap) {
			$.each(_map.getAll(), function (_k, _v) {
				thiz.put(_k, _v);
			});
		} else if (Validater.isJsonObj(_map)) {
			$.each(_map, function (_k, _v) {
				thiz.put(_k, _v);
			});
		} else {
			ExceptionHelper.printStack("键值对集类型错误，仅支持JS.JsonMap和JSON对象");
		}
	}
	/**
	 * 根据键获取对应的值
	 * @param _key 键
	 * @return 该键对应的值
	 */
	this.get = function (_key) {
		if (!isValidKey(_key)) return ;
Printer.info("JsonMap.get.before._key : " + _key);
		_key = _keyTransition(_key);
Printer.info("JsonMap.get.after._key : " + _key);
		return eval("map." + _key);
	}
	/**
	 * 获取所有
	 */
	this.getAll = function () {
		return map;
	}
	/**
	 * 根据键移除元素
	 * @param _key 键
	 * @return 被移除的值
	 */
	this.remove = function (_key) {
		if (!isValidKey(_key)) return ;
Printer.info("JsonMap.remove.before._key : " + _key);
		_key = _keyTransition(_key);
Printer.info("JsonMap.remove.after._key : " + _key);
		var oldValue = this.get(_key);
		eval("delete map." + _key);
		size --;
		return oldValue;
	}
	/**
	 * 移除所有
	 */
	this.removeAll = function () {
		map = {};
		size = 0;
	}
	/**
	 * 获取键集
	 * @return JS.ArrayList
	 */
	this.keys = function () {
		var keys = new ArrayList();
		$.each(map, function (_k, _v) {
			keys.add(_k);
		});
		return keys;
	}
	/**
	 * 获取值集
	 * @return JS.ArrayList
	 */
	this.values = function () {
		var values = new ArrayList();
		$.each(map, function (_k, _v) {
			values.add(_v);
		});
		return values;
	}
	/**
	 * 打印
	 */
	this.toString = function () {
		return JsonHelper.toString(map);
	}
	/**
	 * 是否为有效键
	 * @param _key 键
	 * @return JS.Boolean
	 */
	var isValidKey = function (_key) {
		var isValid = true;
		if (!Validater.isStringObj(_key)) {
			isValid = false;
			ExceptionHelper.printStack("键类型错误，仅支持JS.String");
		} else if (Validater.isNull4trim(_key)) {
			isValid = false;
			ExceptionHelper.printStack("键非法，不支持空键");
		} else if (Validater.hasQuanJiaoSpacialChar(_key)) {
			isValid = false;
			ExceptionHelper.printStack("键非法，不支持含有全角特殊字符的键");
		}
		return isValid;
	}
	/**
	 * 键转换
	 * @param _key 键
	 * @return 转换过后的键
	 * 补充说明：
	 * 1.键前后空格将被去除
	 * 2.首字符为数字的键最前将加"JsonMapStartWithNumberKey_"，最后将加上"_yeKrebmuNhtiWtratSpaMnosJ"
	 * 3.键中的所有半角特殊字符(除下划线以外)，都将被替换为对应的标识字符串
	 * 4.JSON本身就支持中文key
	 */
	var _keyTransition = function (_key) {
		_key = _key.trim();
		if (Validater.isPureNumber(_key.charAt(0))) _key = ("JsonMapStartWithNumberKey_" + _key + "_yeKrebmuNhtiWtratSpaMnosJ");
		_key = _key.replace(/[\~]/g, "BJTSZF_0");
		_key = _key.replace(/[\`]/g, "BJTSZF_1");
		_key = _key.replace(/[\!]/g, "BJTSZF_2");
		_key = _key.replace(/[\@]/g, "BJTSZF_3");
		_key = _key.replace(/[\#]/g, "BJTSZF_4");
		_key = _key.replace(/[\$]/g, "BJTSZF_5");
		_key = _key.replace(/[\%]/g, "BJTSZF_6");
		_key = _key.replace(/[\^]/g, "BJTSZF_7");
		_key = _key.replace(/[\&]/g, "BJTSZF_8");
		_key = _key.replace(/[\*]/g, "BJTSZF_9");
		_key = _key.replace(/[\(]/g, "BJTSZF_10");
		_key = _key.replace(/[\)]/g, "BJTSZF_11");
		_key = _key.replace(/[\-]/g, "BJTSZF_13");
		_key = _key.replace(/[\+]/g, "BJTSZF_14");
		_key = _key.replace(/[\=]/g, "BJTSZF_15");
		_key = _key.replace(/[\{]/g, "BJTSZF_16");
		_key = _key.replace(/[\[]/g, "BJTSZF_17");
		_key = _key.replace(/[\}]/g, "BJTSZF_18");
		_key = _key.replace(/[\]]/g, "BJTSZF_19");
		_key = _key.replace(/[\|]/g, "BJTSZF_20");
		_key = _key.replace(/[\\]/g, "BJTSZF_21");
		_key = _key.replace(/[\:]/g, "BJTSZF_22");
		_key = _key.replace(/[\;]/g, "BJTSZF_23");
		_key = _key.replace(/[\"]/g, "BJTSZF_24");
		_key = _key.replace(/[\']/g, "BJTSZF_25");
		_key = _key.replace(/[\<]/g, "BJTSZF_26");
		_key = _key.replace(/[\,]/g, "BJTSZF_27");
		_key = _key.replace(/[\>]/g, "BJTSZF_28");
		_key = _key.replace(/[\.]/g, "BJTSZF_29");
		_key = _key.replace(/[\?]/g, "BJTSZF_30");
		_key = _key.replace(/[\/]/g, "BJTSZF_31");
		return _key;
	}
}
/**
 * 异常帮助者
 */
function ExceptionHelper() {}
/**
 * 打印追溯栈
 * @param _errorMsg 错误消息
 */
ExceptionHelper.printStack = function (_errorMsg) {
	try {
		_errorMsg = (Validater.isUndefined(_errorMsg) ? "" : _errorMsg);
		throw new Error(_errorMsg);
	} catch (e) {
Printer.error(e.stack);
	}
}
/**
 * 控制台打印器
 */
function Printer() {}
//打印开关
Printer.isOpen = {info:false, log:true, error:true};
/**
 * 控制台打印info消息函数
 * @param _msg 消息
 */
Printer.info = function (_msg) {
	try {
		if (Printer.isOpen.info) console.info(_msg);
	} catch (e) {}
}
/**
 * 控制台打印log消息函数
 * @param _msg 消息
 */
Printer.log = function (_msg) {
	try {
		if (Printer.isOpen.log) console.log(_msg);
	} catch (e) {}
}
/**
 * 控制台打印error消息函数
 * @param _msg 消息
 */
Printer.error = function (_msg) {
	try {
		if (Printer.isOpen.error) console.error(_msg);
	} catch (e) {}
}
/**
 * 正则表达式
 */
// 是否为空字符
RegExp.isNullCharacter = /^\s+$/;
// 是否有空字符
RegExp.hasNullCharacter = /^(\S*\s+\S*)+$/;
//是否是纯数字
RegExp.isPureNumber = /^\d+$/;
//是否是整数
RegExp.isInt = /^[-|+]{0,1}\d+$/;
//是否有整数
RegExp.hasInt = /^(\D*\d+\D*)+$/;
//是否是数字，支持_val=.12与_val=12.
RegExp.isNumber = /^(([-|+]{0,1}\d*\.{0,1}\d+)|([-|+]{0,1}\d+\.{0,1}\d*))$/;
//是否有数字
RegExp.hasNumber = /^(\D*\d+\D*)+$/;
//是否是字母
RegExp.isLetter = /^[a-zA-Z]+$/;
//是否有字母
RegExp.hasLetter = /^([^a-zA-Z]*[a-zA-Z]+[^a-zA-Z]*)+$/;
//是否是小写字母
RegExp.isLowerLetter = /^[a-z]+$/;
//是否有小写字母
RegExp.hasLowerLetter = /^([^a-z]*[a-z]+[^a-z]*)+$/;
//是否是大写字母
RegExp.isUpperLetter = /^[A-Z]+$/;
//是否有大写字母
RegExp.hasUpperLetter = /^([^A-Z]*[A-Z]+[^A-Z]*)+$/;
//是否是中文，验证规则：1.只能是UniCode编码中的汉字范围[\u2E80-\u9FFF](注意：。《》、这四个中文符号以及中文空格也在其中)，2.长度为1位或以上
RegExp.isChinese = /^((?!\s|。|《|》|、)[\u2E80-\u9FFF])+$/;
//是否有中文，验证规则：1.至少有1个和多个UniCode编码中的汉字范围
RegExp.hasChinese = /^(([^\u2E80-\u9FFF]|\s|。|《|》|、)*((?!\s|。|《|》|、)[\u2E80-\u9FFF])+([^\u2E80-\u9FFF]|\s|。|《|》|、)*)+$/;
//是否是特殊字符，(包括空字符、全角字符与数字)
RegExp.isSpacialChar = /^([^\u2E80-\u9FFFa-zA-Z0-9]|\s|。|《|》|、)+$/;
//是否有特殊字符，(包括空字符、全角字符与数字)
RegExp.hasSpacialChar = /^(((?!\s|。|《|》|、)[\u2E80-\u9FFFa-zA-Z0-9])*([^\u2E80-\u9FFFa-zA-Z0-9]|\s|。|《|》|、)+((?!\s|。|《|》|、)[\u2E80-\u9FFFa-zA-Z0-9])*)+$/;
//是否是半角特殊字符，未包含空格
RegExp.isBanJiaoSpacialChar = /^[\~\`\!\@\#\$\%\^\&\*\(\)\_\-\+\=\{\[\}\]\|\\\:\;\"\'\<\,\>\.\?\/]+$/;
//是否有半角特殊字符，未包含空格
RegExp.hasBanJiaoSpacialChar = /^([^\~\`\!\@\#\$\%\^\&\*\(\)\_\-\+\=\{\[\}\]\|\\\:\;\"\'\<\,\>\.\?\/]*[\~\`\!\@\#\$\%\^\&\*\(\)\_\-\+\=\{\[\}\]\|\\\:\;\"\'\<\,\>\.\?\/]+[^\~\`\!\@\#\$\%\^\&\*\(\)\_\-\+\=\{\[\}\]\|\\\:\;\"\'\<\,\>\.\?\/]*)+$/;
//是否是全角特殊字符，未包含空格
RegExp.isQuanJiaoSpacialChar = /^[\～\｀\！\＠\＃\＄\％\＾\＆\＊\（\）\＿\－\＋\＝\｛\［\｝\］\｜\＼\：\；\＂\＇\＜\，\＞\．\？\／]+$/;
//是否有全角特殊字符，未包含空格
RegExp.hasQuanJiaoSpacialChar = /^([^\～\｀\！\＠\＃\＄\％\＾\＆\＊\（\）\＿\－\＋\＝\｛\［\｝\］\｜\＼\：\；\＂\＇\＜\，\＞\．\？\／]*[\～\｀\！\＠\＃\＄\％\＾\＆\＊\（\）\＿\－\＋\＝\｛\［\｝\］\｜\＼\：\；\＂\＇\＜\，\＞\．\？\／]+[^\～\｀\！\＠\＃\＄\％\＾\＆\＊\（\）\＿\－\＋\＝\｛\［\｝\］\｜\＼\：\；\＂\＇\＜\，\＞\．\？\／]*)+$/;
//是否为姓名，验证规则：1.前后不能有空字符，2.不能有数字和特殊字符，3.首字符为中文，4.非首字符可为字母，5.长度为2~10位
//RegExp.isName = /^((?!\s|。|《|》|、)[\u2E80-\u9FFF])((?!\s|。|《|》|、)[\u2E80-\u9FFFa-zA-Z]){1,9}$/;
//是否为手机号码，验证规则：1.前后不能有空字符，2.只能是数字，3.首字符为1，4.第二位字符为2~8之中的一个，5.长度为11位
RegExp.isMobilePhoneNumber = /^1[3-8]\d{9}$/;
//是否为短信验证码，验证规则：1.前后不能有空字符，2.只能是数字，3.长度为6位
RegExp.isMsgValidateCode = /^\d{6}$/;
//是否为15位身份证号码，验证规则：1.前后不能有空字符，2.只能是数字，3.长度为15位
RegExp.isIdCardNumber_15 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
//是否为18位身份证号码，验证规则：1.前后不能有空字符，2.前17位只能是数字，3.末位只能是数字或大写字母，4.长度为18位
RegExp.isIdCardNumber_18 = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[A-Z])$/;
//是否为其它证件号码，验证规则：1.前后不能有空字符，2.只能是字母或数字，3.长度为4~30位
RegExp.isOtherCertificateNumber = /^[a-zA-Z0-9]{4,30}$/;
//是否为邮政编码，验证规则：由6位数字组成
RegExp.isPostCode = /^\d{6}$/;
//是否为登录密码，验证规则：由6~8位数字组成
RegExp.isLoginPassword = /^[\d]{6,8}$/;
//是否为电子邮箱
RegExp.isEmail = /^[a-zA-Z0-9_\.\-]+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})$/; 
/**
 * 验证器
 */
function Validater() {}
/**
 * 是否为布尔对象
 * @param _obj 被验证对象
 * @return JS.Boolean
 */
Validater.isBooleanObj = function (_obj) {
	return $.type(_obj) == "boolean";
}
/**
 * 是否为数字对象
 * @param _obj 被验证对象
 * @return JS.Boolean
 */
Validater.isNumberObj = function (_obj) {
	return $.type(_obj) == "number";
}
/**
 * 是否为字符串对象
 * @param _obj 被验证对象
 * @return JS.Boolean
 */
Validater.isStringObj = function (_obj) {
	return $.type(_obj) == "string";
}
/**
 * 是否为日期对象
 * @param _obj 被验证对象
 * @return JS.Boolean
 */
Validater.isDateObj = function (_obj) {
	return $.type(_obj) == "date";
}
/**
 * 是否为正则表达式对象
 * @param _obj 被验证对象
 * @return JS.Boolean
 */
Validater.isRegExpObj = function (_obj) {
	return $.type(_obj) == "regexp";
}
/**
 * 是否为函数对象
 * @param _obj 被验证对象
 * @return JS.Boolean
 */
Validater.isFunctionObj = function (_obj) {
	return $.type(_obj) == "function";
}
/**
 * 是否为JSON对象
 * @param _obj 被验证对象
 * @return JS.Boolean
 */
Validater.isJsonObj = function (_obj) {
	return (
			$.type(_obj) == "object"
			&& !(_obj instanceof ArrayList)
			&& !(_obj instanceof JsonMap)
			&& !(_obj instanceof ExceptionHelper)
			&& !(_obj instanceof Printer)
			&& !(_obj instanceof Validater)
			&& !(_obj instanceof Transverter)
			&& !(_obj instanceof StyleHelper)
			&& !(_obj instanceof TimingTask)
			&& !(_obj instanceof AjaxHelper)
			&& !(_obj instanceof JavaScriptLoader)
			&& !(_obj instanceof WebpageLoader)
			&& !(_obj instanceof IFrame)
			&& !(_obj instanceof Formater)
			&& !(_obj instanceof Mouse)
			&& !(_obj instanceof JsonHelper)
			&& !(_obj instanceof Cursor)
			&& !(_obj instanceof DatumHelper)
			&& !(_obj instanceof HtmlLabelHelper)
			&& !(_obj instanceof AnimateHelper)
			&& !(_obj instanceof OverrideTemplete)
			&& !(_obj instanceof OverrideTemplete)
			&& !(_obj instanceof Freedom)
		);
}
/**
 * 是否为数组对象
 * @param _obj 被验证对象
 * @return JS.Boolean
 */
Validater.isArrayObj = function (_obj) {
	return $.type(_obj) == "array";
}
/**
 * 是否为JS对象
 * @param _obj 被验证对象
 * @return JS.Boolean
 */
Validater.isJSObj = function (_obj) {
	return (Validater.isBooleanObj(_obj) || Validater.isNumberObj(_obj) || Validater.isStringObj(_obj) || Validater.isDateObj(_obj) || Validater.isRegExpObj(_obj) || Validater.isFunctionObj(_obj) || Validater.isJsonObj(_obj) || Validater.isArrayObj(_obj));
}
/**
 * 是否为jQuery对象
 * @param _obj 被验证对象
 * @return JS.Boolean
 */
Validater.isJQueryObj = function (_obj) {
	return _obj instanceof $;
}
/**
 * 去除前后空格后，是否是空
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isNull4trim = function (_val) {
	return "" == $.trim(_val);
	//注释部分代码与上一行代码是等价的
	//return (null == _val || "" == $.trim(_val));
}
/**
 * 是否未定义
 * @param _obj 被验证对象
 * @return JS.Boolean
 * 补充说明：能匹配null和undefined
 */
Validater.isUndefined = function (_obj) {
	if (Validater.isJQueryObj(_obj)) {
		if (_obj.html() == "") return false;
		return !_obj.html();
	}
	if (Validater.isJSObj(_obj)) return false;
	return true;
}
/**
 * 是否为空字符
 * @param _val 被验证值
 * @return JS.Boolean - true:是, false:否
 */
Validater.isNullCharacter = function (_val) {
	return RegExp.isNullCharacter.test(_val);
}
/**
 * 是否有空字符
 * @param _val 被验证值
 * @return JS.Boolean - true:是, false:否
 */
Validater.hasNullCharacter = function (_val) {
	return RegExp.hasNullCharacter.test(_val);
}
/**
 * 是否是纯数字
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isPureNumber = function (_val) {
	return RegExp.isPureNumber.test(_val);
}
/**
 * 是否是整数
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isInt = function (_val) {
	return RegExp.isInt.test(_val);
}
/**
 * 是否有整数
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.hasInt = function (_val) {
	return RegExp.hasInt.test(_val);
}
/**
 * 是否是数字
 * @param _val 被验证值
 * @return JS.Boolean
 * 补充说明：
 * 1.包括正负整数、正负小数
 * 2.支持_val=.12与_val=12.
 */
Validater.isNumber = function (_val) {
	return RegExp.isNumber.test(_val);
}
/**
 * 是否有数字
 * @param _val 被验证值
 * @return JS.Boolean
 * 补充说明：包括正负整数、正负小数
 */
Validater.hasNumber = function (_val) {
	return RegExp.hasNumber.test(_val);
}
/**
 * 是否是字母
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isLetter = function (_val) {
	return RegExp.isLetter.test(_val);
}
/**
 * 是否有字母
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.hasLetter = function (_val) {
	return RegExp.hasLetter.test(_val);
}
/**
 * 是否是小写字母
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isLowerLetter = function (_val) {
	return RegExp.isLowerLetter.test(_val);
}
/**
 * 是否有小写字母
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.hasLowerLetter = function (_val) {
	return RegExp.hasLowerLetter.test(_val);
}
/**
 * 是否是大写字母
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isUpperLetter = function (_val) {
	return RegExp.isUpperLetter.test(_val);
}
/**
 * 是否有大写字母
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.hasUpperLetter = function (_val) {
	return RegExp.hasUpperLetter.test(_val);
}
/**
 * 是否是中文
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isChinese = function (_val) {
	return RegExp.isChinese.test(_val);
}
/**
 * 是否有中文
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.hasChinese = function (_val) {
	return RegExp.hasChinese.test(_val);
}
/**
 * 是否是特殊字符
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isSpacialChar = function (_val) {
	return RegExp.isSpacialChar.test(_val);
}
/**
 * 是否有特殊字符
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.hasSpacialChar = function (_val) {
	return RegExp.hasSpacialChar.test(_val);
}
/**
 * 是否是半角特殊字符
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isBanJiaoSpacialChar = function (_val) {
	return RegExp.isBanJiaoSpacialChar.test(_val);
}
/**
 * 是否有半角特殊字符
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.hasBanJiaoSpacialChar = function (_val) {
	return RegExp.hasBanJiaoSpacialChar.test(_val);
}
/**
 * 是否是全角特殊字符
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isQuanJiaoSpacialChar = function (_val) {
	return RegExp.isQuanJiaoSpacialChar.test(_val);
}
/**
 * 是否有全角特殊字符
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.hasQuanJiaoSpacialChar = function (_val) {
	return RegExp.hasQuanJiaoSpacialChar.test(_val);
}
/**
 * 是否为姓名
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isName = function (_val) {
	_val = _val.trim();
	var length = _val.length;
	
	// B 长度检查
	if (length < 2 || length > 20) return false;// 长度小于2或者大于20
	if (Validater.isChinese(_val) && length > 10) return false;// 是中文且长度大于10
	// E 长度检查

	// B 数字与特殊字符检查
	if (_val.replace(/[^\.]+/g, '').length > 1) return false;// 点号个数大于1
	if (Validater.hasNumber(_val)) return false;// 有数字
	if (Validater.isChinese(_val.replace(/[\s]+/g, '')) && _val.indexOf(' ') >= 0) return false;// 是中文且有空字符
	if (Validater.hasSpacialChar(_val.replace(/[\.|\s]+/g, ''))) return false;// 有除了点号和空字符外的特殊字符
	// E 数字与特殊字符检查
	
	return true;
}
/**
 * 是否为手机号码
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isMobilePhoneNumber = function (_val) {
	return RegExp.isMobilePhoneNumber.test(_val);
}
/**
 * 是否为短信验证码
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isMsgValidateCode = function (_val) {
	return RegExp.isMsgValidateCode.test(_val);
}
/**
 * 是否为15位身份证号码
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isIdCardNumber_15 = function (_val) {
	return RegExp.isIdCardNumber_15.test(_val);
}
/**
 * 是否为18位身份证号码
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isIdCardNumber_18 = function (_val) {
	return RegExp.isIdCardNumber_18.test(_val);
}
/**
 * 是否为其它证件号码(除15位、18位身份证号码以外的)
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isOtherCertificateNumber = function (_val) {
	return RegExp.isOtherCertificateNumber.test(_val);
}
/**
 * 是否为身份证号码(包括15位、18位身份证号码)
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isIdCardNumber = function (_val) {
	return (RegExp.isIdCardNumber_15.test(_val) || RegExp.isIdCardNumber_18.test(_val));
}
/**
 * 是否为证件号码(包括15位、18位身份证号码、其它证件号码)
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isCertificateNumber = function (_val) {
	return (RegExp.isIdCardNumber_15.test(_val) || RegExp.isIdCardNumber_18.test(_val) || RegExp.isOtherCertificateNumber.test(_val));
}
/**
 * 是否为身份证号码，招商基金自带的身份证校验函数
 * @param _val 被验证值
 * @return JS.Boolean
 * 补充说明：推荐使用该方法来验证身份证号码
 */
Validater.isIdCardNumber4ZSJJ = function (_val) {
	var citys = {11:"北京", 12:"天津", 13:"河北", 14:"山西", 15:"内蒙古", 21:"辽宁", 22:"吉林", 23:"黑龙江", 31:"上海", 32:"江苏", 33:"浙江", 34:"安徽", 35:"福建", 36:"江西", 37:"山东", 41:"河南", 42:"湖北", 43:"湖南", 44:"广东", 45:"广西", 46:"海南", 50:"重庆", 51:"四川", 52:"贵州", 53:"云南", 54:"西藏", 61:"陕西", 62:"甘肃", 63:"青海", 64:"宁夏", 65:"新疆", 71:"台湾", 81:"香港", 82:"澳门", 91:"国外"}
	var iSum = 0;
	var info = "";
	if (!/^\d{17}(\d|x)$/i.test(_val)) return "证件号码填写错误，请重新核对填写";
	_val = _val.replace(/x$/i, "a");
	if (citys[parseInt(_val.substr(0, 2))] == null) return "你的身份证地区非法";
	var sBirthday = _val.substr(6, 4) + "-" + Number(_val.substr(10, 2)) + "-" + Number(_val.substr(12, 2));
	var d = new Date(sBirthday.replace(/-/g, "/"));
	if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate())) return "身份证上的出生日期非法";
	for ( var i = 17; i >= 0; i--) iSum += (Math.pow(2, i) % 11) * parseInt(_val.charAt(17 - i), 11);
	if (iSum % 11 != 1) return "你输入的身份证号非法";
	return true;
}
/**
 * 是否为邮政编码
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isPostCode = function (_val) {
	return RegExp.isPostCode.test(_val);
}
/**
 * 是否为登录密码
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isLoginPassword = function (_val) {
	return RegExp.isLoginPassword.test(_val);
}
/**
 * 是否为安全码
 * @param _val 被验证值
 * @return JS.Boolean
 * 验证规则：
 * 1.长度6~16位
 * 2.由数字、字母、半角特殊字符组成
 * 3.不能全是数字、或全是字母、或全是半角特殊字符
 */
Validater.isTradePassword = function (_val) {
	/*return (/^[\da-zA-Z\~\!\@\#\$\%\^\&\*\(\)\_\+\{\}\|\:\"\<\>\?\-\=\[\]\\\;\'\,\.\/]{8,16}$/.test(_val)//长度6~16位
			&& ((Validater.hasNumber(_val) && Validater.hasLetter(_val))//有数字和字母
					|| (Validater.hasNumber(_val) && Validater.hasBanJiaoSpacialChar(_val))//有数字和半角特殊字符
					|| (Validater.hasLetter(_val) && Validater.hasBanJiaoSpacialChar(_val))));//有字母和半角特殊字符
*/	
	return (/^[\da-zA-Z]{8,16}$/.test(_val)//长度8~16位
			&& (Validater.hasNumber(_val) && Validater.hasLetter(_val))
			&& !Validater.hasBanJiaoSpacialChar(_val) && !Validater.hasQuanJiaoSpacialChar(_val));//不包括半角和全角特殊符号
}
/**
 * 是否为电子邮箱
 * @param _val 被验证值
 * @return JS.Boolean
 */
Validater.isEmail = function (_val) {
	return RegExp.isEmail.test(_val);
}
/**
 * 安全码强度检查
 * @param _val 安全码
 * @return JS.String(L:低, M:中, H:高)
 */
Validater.strengthCheck4tradePassword = function (_val) {
	if (_val.length < 6)//长度小于6位
		return "L";
	if (Validater.hasNumber(_val) && Validater.hasLetter(_val) && Validater.hasBanJiaoSpacialChar(_val))//有数字和字符和半角字符
		return "H";
	if ((Validater.hasNumber(_val) && Validater.hasLetter(_val))//有数字和字母
			|| (Validater.hasNumber(_val) && Validater.hasBanJiaoSpacialChar(_val))//有数字和半角特殊字符
			|| (Validater.hasLetter(_val) && Validater.hasBanJiaoSpacialChar(_val))//有字母和半角特殊字符
			|| (Validater.hasLowerLetter(_val) && Validater.hasUpperLetter(_val)))//有小写字符和大写字符
		return "M";
	if (Validater.isPureNumber(_val)//是纯数字
			|| Validater.isLowerLetter(_val)//是小写字母
			|| Validater.isUpperLetter(_val)//是大写字母
			|| Validater.isBanJiaoSpacialChar(_val))//是半角特殊字符
		return "L";
	return "L";
}
/**
 * 转换器
 */
function Transverter() {}
/**
 * 字符串
 * @param _val 需要转换的值
 * @return JS.Number
 */
Transverter.toStr = function (_val) {
	return (Validater.isUndefined(_val) ? "" : (_val + ""));
}
/**
 * 整数
 * @param _val 需要转换的值
 * @return JS.Number
 * 补充说明：包括正负整数
 */
Transverter.toInt = function (_val) {
	if (!Validater.isInt($.trim(_val))) return 0;
	return new Number(_val);
}
/**
 * 数字
 * @param _val 需要转换的值
 * @return JS.Number
 * 补充说明：
 * 1.不是数字，返回0
 * 2.支持正负整数、正负小数
 * 3.支持.12(转换后=0.12)与12.(转换后=12)
 */
Transverter.toNumber = function (_val) {
	if (!Validater.isNumber(Transverter.toStr(_val).trim())) return 0;
	return new Number(_val);
}
/**
 * 大写金额(不支持负数)
 * @param _val 金额
 * @return JS.String
 */
Transverter.toUpperMoney = function (_val) {
	_val = Transverter.toStr(_val);
	var low	;
	var i, k, j, l_xx1;
	var cap = "", xx1 = "", unit;
	var digits = "零壹贰叁肆伍陆柒捌玖";
	var units = "分角元拾佰仟万拾佰仟亿拾佰仟";
	if (!_val.match(/^(\d{1,}(|(\.{1}\d{0,})))$|^(\.{1}\d{1,})$/)) {
		return "<font color=\"red\"><b>输入无效</b></font>";
	}
	low = parseFloat(_val);
	if (low == 0 || low >= 1) {
		xx1 = Transverter.toNumber(Formater.decimal(low, 2, true));
		xx1 = Math.round(xx1 * 100.0) + "";
	} else if (low > 0 && low < 1) {
		xx1 = Formater.decimal(low, 2, true);
	}
	l_xx1 = xx1.length;
	if (Transverter.toNumber(_val) >= 2100000000) {
		return "<font color=\"red\"><b>金额过大</b></font>";
	}
	for (i = 0; i < l_xx1; i++) {
		j = l_xx1 -1 - i;
		unit = units.substr(j, 1);
		k = parseInt(xx1.substr(i, 1));
		var digit = digits.substr(k, 1);
		cap = cap + digit + unit;
	}
	cap = cap.replace( /零分|零角|零拾|零佰|零仟/g, "零");
	cap = cap.replace( /零+/g, "零");
	cap = cap.replace( /零亿/g, "亿");
	cap = cap.replace( /零万/g, "万");
	cap = cap.replace( /零元/g, "元");
	cap = cap.replace( /亿万/g, "亿");
	cap = cap.replace( /^壹拾/, "拾");
	cap = cap.replace( /零$/, "整");
	if (cap == "整") cap = "零元整";
	var fontSize = (cap.length >= 15 ? 12 : 14);
	cap = ("<font color=\"#FF6900\"><b style=\"font-size:" + fontSize + "px;\">" + cap + "</b></font>");
	return cap;
}
/**
 * 大写份额
 * @param _val 份额(不支持负数)
 * @return JS.String
 */
Transverter.toUpperShare = function (_val) {
	_val = Transverter.toStr(_val);
	var low	;
	var i, k, j, l_xx1;
	var cap = "", xx1 = "", unit;
	var digits = "零壹贰叁肆伍陆柒捌玖"; 
	var units = "分角份拾佰仟万拾佰仟亿拾佰仟"; 
	if (!_val.match(/^(\d{1,}(|(\.{1}\d{0,})))$|^(\.{1}\d{1,})$/)) {
		return "<font color=\"red\"><b>输入无效</b></font>";
	}
	low = parseFloat(_val);
	if (low >= 1) {
		xx1 = Transverter.toNumber(Formater.decimal(low, 2, true));
		xx1 = Math.round(xx1 * 100.0) + "";
	} else if (low > 0 && low < 1) {
		xx1 = Formater.decimal(low, 2, true);
	}
	l_xx1 = xx1.length; 
	if (Transverter.toNumber(_val) >= 2100000000) {
		return "<font color=\"red\"><b>份额过大</b></font>";
	}
	for (i = 0; i < l_xx1; i++) {
		j = l_xx1 - 1 - i;
		unit = units.substr(j, 1);
		k = parseInt(xx1.substr(i, 1));
		var digit = digits.substr(k, 1);
		cap = cap + digit + unit;
	}
	cap = cap.replace(/零分|零角|零拾|零佰|零仟/g, "零");
	cap = cap.replace(/零+/g, "零");
	cap = cap.replace(/零亿/g, "亿");
	cap = cap.replace(/零万/g, "万");
	cap = cap.replace(/零元/g, "份");
	cap = cap.replace(/亿万/g, "亿");
	cap = cap.replace(/^壹拾/, "拾");
	cap = cap.replace(/零$/, "");
	cap = cap.replace(/分+/g, "");
	cap = cap.replace(/角+/g, "");
	cap = cap.replace(/零份/g, "份");
	if (cap == "份" || cap == "") cap = "零份";
	for (i = 0; i < cap.length; i++) {
		if (cap.charAt(i) == '份' && i < cap.length - 1) {
			cap = cap.replace(/份+/g, "点");
			cap = cap + "份"
		}
	}
	if (Transverter.toNumber(xx1) == 0) cap = ("零份");
	else if (low > 0 && low < 1) cap = ("零" + cap + "");
	var fontSize = (cap.length >= 15 ? 12 : 14);
	cap = "<font color=\"#FF6900\"><b style=\"font-size:" + fontSize + "px;\">" + cap + "</b></font>";
	return cap;
}
/**
 * 大写数字
 * 补充说明：
 * 1.支持负数、小数
 * 2.整数部分最多支持千万亿
 */
Transverter.toUpperNumber = function (_val) {
	_val = Transverter.toStr(Transverter.toNumber(_val));
	var upperMap = new JsonMap();
	upperMap.putAll({"0":"零", "1":"壹", "2":"贰", "3":"叁", "4":"肆", "5":"伍", "6":"陆", "7":"柒", "8":"捌", "9":"玖"});
	//小数转换者
	var decimalTransverter = function (_val, _decimalIndex) {
		var intVal = _val.subString(0, _decimalIndex);
		var decimalVal = _val.subString(_decimalIndex + 1);
		var intValUpper = eval("_" + intVal.length + "weiTransverter(intVal);");
		var result = "";
		result = (intValUpper + "点");
		for (var index = 0; index < decimalVal.length; index ++) result += upperMap.get(decimalVal.charAt(index));
		return result;
	}
	//万位转换者
	var wanweiIntTransverter = function (_val, _wei) {
		_val = Transverter.toNumber(_val);
		_val = Transverter.toStr(_val);
		if (_val.length != _wei) return ("零" + eval("_" + _val.length + "weiTransverter(_val);"));
		var result = "";
		var wanwei = eval("_" + (_wei - 4) + "weiTransverter(_val.subString(0, " + (_wei - 4) + "));");
		var qianResult = _4weiTransverter(_val.subString(_wei - 4));
		result = (wanwei + "万" + (qianResult == "零零" ? "" : qianResult));
		return result;
	}
	//亿位转换者
	var yiweiIntTransverter = function (_val, _wei) {
		_val = Transverter.toNumber(_val);
		_val = Transverter.toStr(_val);
		if (_val.length != _wei) return ("零" + eval("_" + _val.length + "weiTransverter(_val);"));
		var result = "";
		var yiwei = eval("_" + (_wei - 8) + "weiTransverter(_val.subString(0, " + (_wei - 8) + "));");
		var qianwanResult = _8weiTransverter(_val.subString(_wei - 8));
		result = (yiwei + "亿" + (qianwanResult == "零零" ? "" : qianwanResult));
		return result;
	}
	//个
	var _1weiTransverter = function (_val) {
		return upperMap.get(_val);
	}
	//拾
	var _2weiTransverter = function (_val) {
		var gewei = upperMap.get(_val.charAt(1));
		var shiwei = upperMap.get(_val.charAt(0));
		return (((shiwei == "零" || shiwei == "壹") ? "" : shiwei) + "拾" + (gewei == "零" ? "" : gewei));
	}
	//佰
	var _3weiTransverter = function (_val) {
		_val = Transverter.toNumber(_val);
		_val = Transverter.toStr(_val);
		if (_val.length != 3) return ("零" + eval("_" + _val.length + "weiTransverter(_val);"));
		var result = "";
		var gewei = upperMap.get(_val.charAt(2));
		var shiwei = upperMap.get(_val.charAt(1));
		var baiwei = upperMap.get(_val.charAt(0));
		result = (baiwei + "佰" + (shiwei == "零" ? "" : shiwei) + (shiwei == "零" ? "" : "拾") + (gewei == "零" ? "" : gewei));
		result = ((shiwei == "零" && gewei != "零") ? (result.subString(0, 2) + "零" + result.subString(2)) : result);
		return result
	}
	//仟
	var _4weiTransverter = function (_val) {
		_val = Transverter.toNumber(_val);
		_val = Transverter.toStr(_val);
		if (_val.length != 4) return ("零" + eval("_" + _val.length + "weiTransverter(_val);"));
		var result = "";
		var qianwei = upperMap.get(_val.charAt(0));
		var baiResult = _3weiTransverter(_val.subString(1));
		result = (qianwei + "仟" + (baiResult == "零零" ? "" : baiResult));
		return result;
	}
	//万
	var _5weiTransverter = function (_val) {
		return wanweiIntTransverter(_val, 5);
	}
	//拾万
	var _6weiTransverter = function (_val) {
		return wanweiIntTransverter(_val, 6);
	}
	//佰万
	var _7weiTransverter = function (_val) {
		return wanweiIntTransverter(_val, 7);
	}
	//仟万
	var _8weiTransverter = function (_val) {
		return wanweiIntTransverter(_val, 8);
	}
	//亿
	var _9weiTransverter = function (_val) {
		return yiweiIntTransverter(_val, 9);
	}
	//拾亿
	var _10weiTransverter = function (_val) {
		return yiweiIntTransverter(_val, 10);
	}
	//佰亿
	var _11weiTransverter = function (_val) {
		return yiweiIntTransverter(_val, 11);
	}
	//仟亿
	var _12weiTransverter = function (_val) {
		return yiweiIntTransverter(_val, 12);
	}
	//万亿
	var _13weiTransverter = function (_val) {
		return yiweiIntTransverter(_val, 13);
	}
	//拾万亿
	var _14weiTransverter = function (_val) {
		return yiweiIntTransverter(_val, 14);
	}
	//佰万亿
	var _15weiTransverter = function (_val) {
		return yiweiIntTransverter(_val, 15);
	}
	//仟万亿
	var _16weiTransverter = function (_val) {
		return yiweiIntTransverter(_val, 16);
	}
	
	var result = "";
	if (_val.charAt(0) == "-") {
		result = "负";
		_val = _val.subString(1);
	}
	var decimalIndex = _val.indexOf(".");
	if (decimalIndex >= 0) result += decimalTransverter(_val, decimalIndex);
	else result += eval("_" + _val.length + "weiTransverter(_val);");
	return result;
}
/**
 * 日期字符串
 * @param _tarDate 目标日期(JS Date对象)
 * @param _fomatStr 转换格式(暂仅支持两种格式：yyyy-MM-dd、yyyy-MM-dd HH:mm:ss)
 * @return JS.String
 */
Transverter.toDateStr = function (_tarDate, _fomatStr) {
	if ($.type(_tarDate) != "date")  _tarDate = new Date();
	var date = new Date(_tarDate);
	var year = Transverter.toNumber(date.getFullYear());
	var month = Transverter.toNumber(date.getMonth()) + 1;
	var day = Transverter.toNumber(date.getDate());
	var hours = Transverter.toNumber(date.getHours());
	var minutes = Transverter.toNumber(date.getMinutes());
	var seconds = Transverter.toNumber(date.getSeconds());
	if ("yyyy-MM-dd" == _fomatStr) {
		return year + "-" + ("0" + month).slice(-2) + "-" + ("0" + day).slice(-2);
	}
	return year + "-" + ("0" + month).slice(-2) + "-" + ("0" + day).slice(-2) + " " + ("0" + hours).slice(-2) + ":" + ("0" + minutes).slice(-2) + ":" + ("0" + seconds).slice(-2);
}
/**
 * 样式帮助者
 */
function StyleHelper() {}
/**
 * 获取屏幕显示窗口宽度值
 * @return JS.Number
 */
StyleHelper.getWindowWidth = function () {
	return Transverter.toNumber(document.documentElement.clientWidth);
}
/**
 * 获取屏幕显示窗口高度值
 * @return JS.Number
 */
StyleHelper.getWindowHeight = function () {
	return Transverter.toNumber(document.documentElement.clientHeight);
}
/**
 * 定时任务
 * @param _name 名称
 */
function TimingTask(_name) {
	//初始化定时任务名字
	var name = ("TimingTask." + _name);
Printer.info("Success create a timing task object, name:" + name + ", current timing methods size(once http forword):" + TimingTask.timingMethods.size());
	//初始化定时方法下标
	var timingMethodIndex = TimingTask.timingMethods.size();
	//定时器集
	var timers = new ArrayList();
	//已执行次数
	var executedTimes = 0;
	/**
	 * 定时方法
	 * @param _currentTimes 当前次数
	 * @param _times 次数(传入0，表示无限循环，直到调用this.stop();才停止)
	 * @param _step 步长
	 * @param _circulateMethod 循环方法(执行时，会回传2个参数[1.当前执行次数、2.总执行次数])
	 * @param _finishMethod 结束方法(执行时，会回传2个参数[1.当前执行次数、2.总执行次数])
	 */
	var timingMethod = function (_currentTimes, _times, _step, _circulateMethod, _finishMethod) {
//Printer.info(name + ", size:" + timers.size() + ", current timers:" + timers.toString());
		executedTimes ++;
		//删除并清除当前执行的定时器及其之前未执行的定时器
		for (var index = 0; index <= (_currentTimes - executedTimes); index ++) {
			window.clearTimeout(timers.remove(0));
		}
		executedTimes = _currentTimes;
		if (_times == 0) {
			_circulateMethod(_currentTimes, _times);
			timers.add(window.setTimeout(TimingTask.timingMethods.get(timingMethodIndex), (_step * 1000), (executedTimes + 1), 0, _step, _circulateMethod, _finishMethod));
		} else if (_currentTimes == _times) _finishMethod(_currentTimes, _times);
		else _circulateMethod(_currentTimes, _times);
	};
	//加入定时方法集
	TimingTask.timingMethods.add(timingMethod);
	/**
	 * 启动
	 * @param _step 步长(单位：秒)
	 * @param _times 次数(传入0表示无限循环，直到调用this.stop();才停止)
	 * @param _circulateMethod 循环方法(执行时，会回传2个参数[1.当前执行次数、2.总执行次数])
	 * @param _finishMethod 任务结束方法(执行时，会回传2个参数[1.当前执行次数、2.总执行次数])
	 */
	this.start = function (_step, _times, _circulateMethod, _finishMethod) {
		//启动前先停止尚未执行的
		this.stop();
		var step = Transverter.toNumber(_step);
		var times = Transverter.toNumber(_times);
		if (step <= 0 || times < 0 || !Validater.isFunctionObj(_circulateMethod) || !Validater.isFunctionObj(_finishMethod)) {
Printer.error("freedom timing task start failure, please check your params!");
			return ;
		}
		if (times == 0) {
			timers.add(window.setTimeout("TimingTask.timingMethods.get(" + timingMethodIndex + ")(" + 1 + ", " + 0 + ", " + step + ", " + _circulateMethod + ", " + _finishMethod + ")", (step * 1000)));
		} else {
			for (var currentTimes = 1; currentTimes <= times; currentTimes++) {
				/**
				 * 说明：
				 * 1.此方法为原生JS自带函数
				 * 2.它没有阻塞效果
				 * 3.可以理解为新开executeTimes条线程，且它们会分别在(currentTimes * step * 1000)毫秒后调用timingMethod方法，并且依次传入参数：(currentTimes, times, step, circulateMethod, finishMethod)
				 */
				timers.add(window.setTimeout("TimingTask.timingMethods.get(" + timingMethodIndex + ")(" + currentTimes + ", " + times + ", " + step + ", " + _circulateMethod + ", " + _finishMethod + ")", (currentTimes * step * 1000)));
			}
		}
	}
	/**
	 * 停止 
	 */
	this.stop = function () {
		//停止当前所有定时器
		$.each(timers.getAll(), function (_i, _v) {
			window.clearTimeout(_v);
		});
Printer.info("TimingTask - stop:" + timers.size());
		//初始化定时器集
		timers.removeAll();
		//初始化已执行次数
		executedTimes = 0;
	}
}
//定时方法集
TimingTask.timingMethods = new ArrayList();
/**
 * 获取单例对象
 * @return JS.TimingTask
 */
TimingTask.getSingleton = function () {
	if (TimingTask[19910625] == null) {
		TimingTask[19910625] = new TimingTask("singleton");
	}
	return TimingTask[19910625];
}
/**
 * 异步帮助者
 */
function AjaxHelper() {}
/**
 * 创建一个jQuery.Ajax请求状态对象
 * @return JSON对象
 */
AjaxHelper.newReqStateObj = function () {
	return {
		isSend : false,//是否发送
		isAbort : false,//是否中止
		isSuccess : false,//是否成功
		isError : false,//是否错误
		isComplete : false,//是否完成
		respData : {}//响应数据
	};
}
/**
 * JS载入者
 */
function JavaScriptLoader() {}
/**
 * 载入多个JS脚本
 * @param _urls JS脚本的URL集(仅支持JS.ArrayList、JS.数组(Array、[]))
 */
JavaScriptLoader.loads = function (_urls) {
	if (_urls instanceof ArrayList) {
		$.each(_urls.getAll(), function (_i, _v) {
			$.getScript(_v);
		});
	} else if (Validater.isArrayObj(_urls)) {
		$.each(_urls, function (_i, _v) {
			$.getScript(_v);
		});
	} else ExceptionHelper.printStack("请求URL集类型错误，仅支持JS.ArrayList、JS.数组(Array、[])");
}
/**
 * 网页加载器，采用的是Ajax方式
 */
function WebpageLoader() {}
//是否开启停止加载
WebpageLoader.isOpenUnload = true;
//当前请求的个数
WebpageLoader.currentReqCount = 0;
//最新的响应HTML
WebpageLoader.newestRespHtml = "";
//最新的请求路径
WebpageLoader.newestReqUrl = "";
//最新的装入对象
WebpageLoader.newestLoadObj = null;
//最新的可选项
WebpageLoader.newestOptions = null;
//手动回调函数
WebpageLoader.manualBackFn = function () {}
//手动回调函数参数列表(多个参数推荐使用JSON)
WebpageLoader.manualBackFnParams = null;
//请求集
WebpageLoader.reqs = new ArrayList();
/**
 * 网页加载
 * @param _reqUrl 请求URL
 * @param _loadObj 装入对象(响应HTML要装入的目的地)
 * @param _options 可选项集(JSON) {
 * 		isOpenWaitCursor : 是否开始等待手势(默认false),
 * 		isOpenUnload : 是否开启停止加载(默认false),
 *		isAppend : 是否追加(默认false),
 *		reqData : 请求数据(默认null),
 *		reqType : 请求类型(默认post),
 *		isAsync : 是否异步(默认true),
 *		isCache : 是否缓存(默认false),
 *		backFn : 回调函数(默认function () {}),
 *		backFnParams : 回调函数参数列表(多个参数推荐使用JSON),
 *		manualBackFn : 手动回调函数(默认function() {}),
 *		manualBackFnParams : 手动回调函数参数列表(多个参数推荐使用JSON)
 * }
 */
WebpageLoader.load = function (_reqUrl, _loadObj, _options) {
	//保留装入对象中老的Html
	var oldHtml = _loadObj.html();
	//loding图片的信息
	var lodingImgInfo = {
		//absolutePath : "/ECApp/ECPublic/ECWeb/ECWeb_Images/loding_2.gif",//freedom包中该图片所在相对路径：img/loding_2.gif
		absolutePath : "#",//freedom包中该图片所在相对路径：img/loding_2.gif
		width : "16px",
		height : "16px"
	};
	/*reqType    ajax请求静态页面（shtml），POST提交方式会报405错误，改为get*/
	var defaultOptions = {isOpenWaitCursor:false, isIFrame:false, isOpenUnload:false, isAppend:false, reqData:null, reqType:"get", isAsync:true, isCache:false, backFn:function () {}, backFnParams:null, manualBackFn:function () {}, manualBackFnParams:null};
	var finalOptions = JsonHelper.extend(defaultOptions, _options);
	if (finalOptions.isIFrame) IFrame.isOpenUnload = finalOptions.isOpenUnload;
	else WebpageLoader.isOpenUnload = finalOptions.isOpenUnload;
	finalOptions.backFn = (Validater.isFunctionObj(finalOptions.backFn) ? finalOptions.backFn : function () {});
	if (finalOptions.isIFrame && Validater.isFunctionObj(finalOptions.manualBackFn)) {
		//如果是iFrame请求过来的，且可选项中的手动回调函数是一个函数，就赋值给IFrame.manualBackFn
		IFrame.manualBackFn = finalOptions.manualBackFn;
		IFrame.manualBackFnParams = finalOptions.manualBackFnParams;
	} else if (Validater.isFunctionObj(finalOptions.manualBackFn)) {
		//否则如果可选项中的手动回调函数是一个函数，就赋值给WebpageLoader.manualBackFn 
		WebpageLoader.manualBackFn = finalOptions.manualBackFn;
		WebpageLoader.manualBackFnParams = finalOptions.manualBackFnParams;
	}
	var ajaxReqStateObj = AjaxHelper.newReqStateObj();
	var ajaxReqObj = $.ajax({
		url : _reqUrl,
		data : finalOptions.reqData,
		type : finalOptions.reqType,
		async : finalOptions.isAsync,
		cache : finalOptions.isCache,
		beforeSend : function () {
			if (finalOptions.isIFrame) {
				IFrame.currentReqCount ++;
				IFrame.newestReqUrl = _reqUrl;
				IFrame.newestLoadObj = _loadObj;
				IFrame.newestOptions = _options;
			} else {
				WebpageLoader.currentReqCount ++;
				WebpageLoader.newestReqUrl = _reqUrl;
				WebpageLoader.newestLoadObj = _loadObj;
				WebpageLoader.newestOptions = _options;
			}
			var reqs = (finalOptions.isIFrame ? IFrame.reqs : WebpageLoader.reqs);
			var hasThisUrl = false;
			$.each(reqs.getAll(), function(_i, _v) {
				if (_v.reqUrl == _reqUrl) hasThisUrl = true;
			});
			//停止加载Html
			var unloadHtml = "";
			//是否开启停止加载判断
			if (finalOptions.isOpenUnload) {
				unloadHtml = "[<a href=\"javascript:Freedom.nothing();\" onclick=\"javascript:" + (finalOptions.isIFrame ? "IFrame" : "WebpageLoader") + ".unload();\" style=\"text-decoration:none; color:#999999;\" onmouseover=\"javascript:$(this).css({'text-decoration':'underline', 'color':'#FF6900'});\" onmouseout=\"javascript:$(this).css({'text-decoration':'none', 'color':'#999999'});\">停止加载</a>]";
			}
			_loadObj.html("<span style=\"color:#999999; position:relative; left:" + (_loadObj.innerWidth() / 2 - 20) + "px; top:" + (_loadObj.innerHeight() * 2 / 7) + "px; font-size:12px; background-image:url(" + lodingImgInfo.absolutePath + "); background-repeat:no-repeat; background-position:left; padding-left:" + lodingImgInfo.width + "; height:" + lodingImgInfo.height + "; line-height:" + lodingImgInfo.height + "; display:block; \">&nbsp;加载中, 请稍候..." + unloadHtml + "</span>");
			//如果当前请求集中有本次请求的Url，那么就取消本次请求
			if (hasThisUrl) {
Printer.info("has this request url(" + _reqUrl + "), so cancel send.");
				return false;
			}
			ajaxReqStateObj.isSend = true;
			if (finalOptions.isOpenWaitCursor) _loadObj.css("cursor", "progress");
		},
		success : function(_respHtml) {
			if (finalOptions.isIFrame)  IFrame.newestRespHtml = _respHtml;
			else WebpageLoader.newestRespHtml = _respHtml;
			ajaxReqStateObj.respData = _respHtml;
			ajaxReqStateObj.isSuccess = true;
		},
		error : function(_respData) {
			if (_respData.readyState == 0) ajaxReqStateObj.isAbort = true;
			else alert("load failure!");
			ajaxReqStateObj.isError = true;
		},
		complete : function() {
			var newestReqUrl = (finalOptions.isIFrame ? IFrame.newestReqUrl : WebpageLoader.newestReqUrl);
			var newestRespHtml = (finalOptions.isIFrame ? IFrame.newestRespHtml : WebpageLoader.newestRespHtml);
			var reqs = (finalOptions.isIFrame ? IFrame.reqs : WebpageLoader.reqs);
Printer.info("Ajax is complete," + (newestReqUrl != _reqUrl ? " itsn't newest" : " it's newest") + (finalOptions.isIFrame ? " iFrame " : " ") + "request url(" + _reqUrl + ")" + (ajaxReqStateObj.isError ? (ajaxReqStateObj.isAbort ? ", it's manul breaked" : ", it's error") : "") + ", total request count=" + (finalOptions.isIFrame ? IFrame.currentReqCount : WebpageLoader.currentReqCount));
Printer.info(" ");
			//如果不是手动中止的(因为手动中止时，是一次性全部中止，所以也被一次性全部删除了)，就删除当前请求
			if (!ajaxReqStateObj.isAbort) {
				$.each(reqs.getAll(), function(_i, _v) {
					if (_v.reqUrl == _reqUrl) reqs.remove(_i);
				});
			}
			//过滤非最新请求
			if (newestReqUrl != _reqUrl) return false;
			//过滤请求错误的情况
			if (ajaxReqStateObj.isError) {
				if (finalOptions.isIFrame) {
					if (oldHtml != "") {
						IFrame.resetStyle(IFrame.lastStyle);
						_loadObj.html(newestRespHtml);
					} else IFrame.close();
				} else _loadObj.html(newestRespHtml);
				return false;
			}
			//保存IFrame上次的样式
			if (finalOptions.isIFrame) IFrame.lastStyle = finalOptions;
			//执行回调函数
			if (finalOptions.backFnParams == null) finalOptions.backFn();
			else finalOptions.backFn(finalOptions.backFnParams);
			if (finalOptions.isOpenWaitCursor) _loadObj.css("cursor", "default");
			//载入内容
			if (finalOptions.isAppend) {
				_loadObj.empty();
				_loadObj.append(oldHtml);
				_loadObj.append(newestRespHtml);
			} else _loadObj.html(newestRespHtml);
		}
	});
	var reqs = (finalOptions.isIFrame ? IFrame.reqs : WebpageLoader.reqs);
	var hasThisReqUrl = false;
	$.each(reqs.getAll(), function(_i, _v) {
		if (_v.reqUrl == _reqUrl) hasThisReqUrl = true;
	});
	if (!hasThisReqUrl) reqs.add({reqUrl:_reqUrl, loadObj:_loadObj, options:_options, ajaxReqObj:ajaxReqObj});
Printer.info("--------current" + (finalOptions.isIFrame ? " iframe " : " ") + "total request url list, size=" + reqs.size() + "-------");
	$.each(reqs.getAll(), function(_i, _v) {
Printer.info(_i + ", " + _v.reqUrl);
	});
Printer.info(" ");
}
/**
 * 停止加载
 * 补充说明：该方法并不能中止已发出的Http请求，只是单纯的取消本次加载响应等待
 */
WebpageLoader.unload = function () {
	var isIFrame = (arguments[0] == "isIFrame");
	var reqs = (isIFrame ? IFrame.reqs : WebpageLoader.reqs);
	if ((WebpageLoader.isOpenUnload && !isIFrame) || (IFrame.isOpenUnload && isIFrame)) {
Printer.info("--------manual break" + (isIFrame ? " iframe " : " ") + "list, size=" + reqs.size() + "-------");
		$.each(reqs.getAll(), function(_i, _v) {
Printer.info(_i + ", " + _v.reqUrl);
		});
Printer.info(" ");
	}
	$.each(reqs.getAll(), function(_i, _v) {
		if ((WebpageLoader.isOpenUnload && !isIFrame) || (IFrame.isOpenUnload && isIFrame)) _v.ajaxReqObj.abort();
	});
	reqs.removeAll();
}
/**
 * 执行手动回调函数
 * @param _backParams 回调参数(多个参数推荐使用JSON)
 * 补充说明：
 * 1.若调用时传入了参数，则该参数将作为手动回调函数的参数传入
 * 2.若调用时未传入参数，则会把WebpageLoader.load时的可选项中的manualBackFnParams作为手动回调函数的参数传入
 */
WebpageLoader.executeManualBackFn = function (_backParams) {
	var backParams = (_backParams == null ? {} : _backParams);
	var manualBackFn = (backParams.isIFrame ? IFrame.manualBackFn : WebpageLoader.manualBackFn);
	backParams = (backParams.isIFrame ? (backParams.backParams == null ? IFrame.manualBackFnParams : backParams.backParams) : (_backParams == null ? WebpageLoader.manualBackFnParams : _backParams));
	if (backParams == null) manualBackFn();
	else manualBackFn(backParams);
}
/**
 * 浮动层
 */
function IFrame() {}
//该变量用来保存上次的样式
IFrame.lastStyle = null;
//是否开启停止加载
IFrame.isOpenUnload = true;
//当前请求的个数
IFrame.currentReqCount = 0;
//最新的响应HTML
IFrame.newestRespHtml = "";
//最新的请求路径
IFrame.newestReqUrl = "";
//最新的装入对象
IFrame.newestLoadObj = null;
//最新的可选项
IFrame.newestOptions = null;
//手动回调函数
IFrame.manualBackFn = function () {}
//手动回调函数参数列表(多个参数推荐使用JSON)
IFrame.manualBackFnParams = null;
//关闭回调函数
IFrame.closeBackFn = function () {}
//关闭回调函数参数列表(多个参数推荐使用JSON)
IFrame.closeBackFnParams = null;
//请求集
IFrame.reqs = new ArrayList();
/**
 * 重置样式
 * @param _options 可选项集(JSON) {
 * 		title : 标题,
 * 		width : 宽,
 * 		height : 高,
 * 		isDraggable : 是否可拖动(默认false),
 * 		xScrollStyle : 横向滚动条样式,
 * 		yScrollStyle : 纵向滚动条样式
 * }
 */
IFrame.resetStyle = function (_options) {
	if (Validater.isUndefined($("#maodIFrameWindow"))) return ;
	//可选项默认值
	var defaultOptions = {isIFrame:true, title:"", width:450, height:500, isDraggable:false, xScrollStyle:"auto", yScrollStyle:"auto"}
	var finalOptions = JsonHelper.extend(defaultOptions, _options);
	var xScrollStyle = $.trim(finalOptions.xScrollStyle);
	var yScrollStyle = $.trim(finalOptions.yScrollStyle);
	if (xScrollStyle != "auto" && xScrollStyle != "hidden" && xScrollStyle != "scroll" && xScrollStyle != "visible" && xScrollStyle != "inherit") xScrollStyle = "auto";
	if (yScrollStyle != "auto" && yScrollStyle != "hidden" && yScrollStyle != "scroll" && yScrollStyle != "visible" && yScrollStyle != "inherit") yScrollStyle = "auto";
	var width = Transverter.toNumber(finalOptions.width);
	var height = Transverter.toNumber(finalOptions.height);
	//浏览器窗口宽度
	var windowWidth = StyleHelper.getWindowWidth();//browser valid window width
	//浏览器窗口高度
	var windowHeight = StyleHelper.getWindowHeight();//browser valid window height
	//最小宽度100像素
	if (width < 100) width = 100;//min width is 100px
	//最小高度100像素
	if (height < 100) height = 100;//min height is 100px
	//距左
	var marginLeftPx = (windowWidth - width) / 2;//left
	//距顶
	var marginTopPx = (windowHeight - height) / 3;//top
	//若IFrame宽度大于浏览器可视宽度，就把距左重置为0，并将IFrame设置为绝对定位，可供滚动垂直滚动条来看到IFrame被遮住的区域
	if (width > windowWidth) {
		$("#maodIFrameWindow>div[class='maodIFrameMain']").css("position", "absolute");
		marginLeftPx = 0;
	}
	//若IFrame高度大于浏览器可视高度，就把距顶重置为0，并将IFrame设置为absolute定位，可供滚动水平滚动条来看到IFrame被遮住的区域
	if (height > windowHeight) {
		$("#maodIFrameWindow>div[class='maodIFrameMain']").css("position", "absolute");
		marginTopPx = 0;
	}
	//若IFrame宽度小于或等于浏览器可视宽度，并且IFrame高度小于或等于浏览器可视高度，就将IFrame设置为fixed定位
	if (width <= windowWidth && height <= windowHeight) {
		$("#maodIFrameWindow>div[class='maodIFrameMain']").css("position", "fixed");
	}
	$("#maodIFrameWindow>div[class='maodIFrameMain']").css("width", width + "px");
	$("#maodIFrameWindow>div[class='maodIFrameMain']").css("height", height + "px");
	
	// B 重新定位处理
	$("#maodIFrameWindow>div[class='maodIFrameMain']").css("left", marginLeftPx + "px");
	$("#maodIFrameWindow>div[class='maodIFrameMain']").css("top", marginTopPx + "px");
	// E 重新定位处理
	
/* 
	// B 未销毁的弹层，再次弹出时，位置不变处理
	// B 阻止弹层超出浏览器窗口处理
	var mainIFrame = $("#maodIFrameWindow>div.maodIFrameMain");
	var mainIFrameWidth = mainIFrame.width();//弹层宽度
	var mainIFrameHeight = mainIFrame.height();//弹层高度
	var mainIFrameLeftPX = Formater.getNumberVal(mainIFrame.css("left"));//弹层距左像素
	var mainIFrameTopPX = Formater.getNumberVal(mainIFrame.css("top"));//弹层距顶像素
	if (mainIFrameLeftPX > (windowWidth - mainIFrameWidth)) {
		$("#maodIFrameWindow>div[class='maodIFrameMain']").css("left", (windowWidth - mainIFrameWidth) + "px");
	}
	if (mainIFrameTopPX > (windowHeight - mainIFrameHeight)) {
		$("#maodIFrameWindow>div[class='maodIFrameMain']").css("top", (windowHeight - mainIFrameHeight) + "px");
	}
	// E 阻止弹层超出浏览器窗口处理
	// E 未销毁的弹层，再次弹出时，位置不变处理
*/
	
	$("#maodIFrameWindow>div[class='maodIFrameMain']>div[class='maodIFrameHead']").css("width", (width - 10) + "px");
	$("#maodIFrameWindow>div[class='maodIFrameMain']>div[class='maodIFrameHead']>b").html(finalOptions.title);
	$("#maodIFrameWindow>div[class='maodIFrameMain']>div[class='maodIFrameContent']").css("width", (width - 12) + "px");
	$("#maodIFrameWindow>div[class='maodIFrameMain']>div[class='maodIFrameContent']").css("height", (height - 45) + "px");
	$("#maodIFrameWindow>div[class='maodIFrameMain']>div[class='maodIFrameContent']").css("overflow-x", xScrollStyle);
	$("#maodIFrameWindow>div[class='maodIFrameMain']>div[class='maodIFrameContent']").css("overflow-y", yScrollStyle);
	if (finalOptions.isDraggable) $("#maodIFrameHead").css("cursor", "move");
	else {
		$("#maodIFrameHead").css("cursor", "default");
		$("#maodIFrameHead").unbind("mousedown");
	}
}
/**
 * 打开
 * @param _contentUrl 内容URL
 * @param _options 可选项集(JSON) {
 * 		title : 标题,
 * 		width : 宽,
 * 		height : 高,
 * 		xScrollStyle : 横向滚动条样式(默认auto),
 * 		yScrollStyle : 纵向滚动条样式(默认auto),
 * 		isDraggable : 是否可拖动(默认false),
 * 		isOpenUnload : 是否开启停止加载(默认false),
 * 		isAppend : 是否追加(默认false),
 * 		reqData : 请求数据(默认null),
 * 		reqType : 请求类型(默认post),
 * 		isAsync : 是否异步(默认true),
 * 		isCache : 是否缓存(默认false),
 * 		backFn : 回调函数(默认function() {}),
 * 		backFnParams : 回调函数参数列表(多个参数推荐使用JSON),
 * 		closeBackFn : 关闭回调函数(默认function() {}),
 * 		closeBackFnParams : 关闭回调函数参数列表(多个参数推荐使用JSON),
 *		manualBackFn : 手动回调函数(默认function() {}),
 *		manualBackFnParams : 手动回调函数参数列表(多个参数推荐使用JSON)
 * }
 */
IFrame.open = function (_contentUrl, _options) {
	//图片绝对路径
	var imgAbsolutePath = {
		close:"/ECApp/ECPublic/ECWeb/ECWeb_Images/shutup.png"//freedom包中该图片所在相对路径：img/close_1.png
	}
	//可选项默认值
	var defaultOptions = {isIFrame:true, title:"", width:450, height:500, xScrollStyle:"auto", yScrollStyle:"auto", isDraggable:false, isOpenUnload:false, closeBackFn:function () {}, closeBackFnParams:null};
	var finalOptions = JsonHelper.extend(defaultOptions, _options);
	
	//如果可选项中的关闭回调函数不是一个函数，就初始化为function () {}
	finalOptions.closeBackFn = (Validater.isFunctionObj(finalOptions.closeBackFn) ? finalOptions.closeBackFn : function () {});
	IFrame.closeBackFn = finalOptions.closeBackFn;
	IFrame.closeBackFnParams = finalOptions.closeBackFnParams;
	
	if (Validater.isUndefined($("#maodIFrameWindow"))) {
		var xScrollStyle = $.trim(finalOptions.xScrollStyle);
		var yScrollStyle = $.trim(finalOptions.yScrollStyle);
		if (xScrollStyle != "auto" && xScrollStyle != "hidden" && xScrollStyle != "scroll" && xScrollStyle != "visible" && xScrollStyle != "inherit") xScrollStyle = "auto";
		if (yScrollStyle != "auto" && yScrollStyle != "hidden" && yScrollStyle != "scroll" && yScrollStyle != "visible" && yScrollStyle != "inherit") yScrollStyle = "auto";
		var IFrame_position = "fixed";
		var width = Transverter.toNumber(finalOptions.width);
		var height = Transverter.toNumber(finalOptions.height);
		var windowWidth = StyleHelper.getWindowWidth();
		var windowHeight = StyleHelper.getWindowHeight();
		if (width < 100) width = 100;
		if (height < 100) height = 100;
		var marginLeftPx = (windowWidth - width) / 2;
		var marginTopPx = (windowHeight - height) / 3;
		if (width > windowWidth) {
			IFrame_position = "absolute";
			marginLeftPx = 0;
		}
		if (height > windowHeight) {
			IFrame_position = "absolute";
			marginTopPx = 0;
		}
		var style = "";
		style += "<style>";
			style += "#maodIFrameWindow {z-index:999999;}";
			style += ".maodIFrameShade {z-index:999999; width:100%; height:100%; display:block; position:fixed; background-color:#000; top:0px; left:0px; filter:alpha(opacity=70); opacity:0.7;}";
			style += (".maodIFrameMain {z-index:999999; width:" + width + "px; height:" + height + "px; display:block; position:" + IFrame_position + "; background-color:#FFF; top:" + marginTopPx + "px; left:" + marginLeftPx + "px; box-shadow:0px 0px 5px 5px rgba(0, 0, 0, 0.1);}");
			style += (".maodIFrameHead {z-index:999999; width:" + (width - 20) + "px; height:20px; line-height:20px; padding-top:10px; padding-left:20px;}");
			style += (".maodIFrameHead a {width:15px; height:15px; display:inline-block; position:absolute; top:12px; right:15px; background:url(\"" + imgAbsolutePath.close + "\") no-repeat scroll -18px 0px transparent;}");
			style += (".maodIFrameHead a:hover {background: url(\"" + imgAbsolutePath.close + "\") no-repeat scroll 0px 0px transparent;}");
			style += (".maodIFrameContent {z-index:999999; width:" + (width - 12) + "px; height:" + (height - 45) + "px; padding:5px; overflow-x:" + xScrollStyle + "; overflow-y:" + yScrollStyle + "; white-space:nowrap;}");
		style += "</style>";
		$("body").prepend("<div id=\"maodIFrameWindow\"></div>");
		$("#maodIFrameWindow").append(style);
		$("#maodIFrameWindow").append("<div class=\"maodIFrameShade\"></div>");
		$("#maodIFrameWindow").append("<div class=\"maodIFrameMain\"></div>");
		$("#maodIFrameWindow>div:last").append("<div id=\"maodIFrameHead\" class=\"maodIFrameHead\"><b style=\"font-size:14px;\">" + finalOptions.title + "</b><a href=\"javascript:Freedom.nothing();\" onclick=\"javascript:IFrame.close();\"></a></div>");
		$("#maodIFrameWindow>div:last").append("<div class=\"maodIFrameContent\"></div>");
		WebpageLoader.load(_contentUrl, $("#maodIFrameWindow>div:last>div:eq(1)"), finalOptions);
		if (finalOptions.isDraggable) $("#maodIFrameWindow>div.maodIFrameMain>div.maodIFrameHead").css("cursor", "move");
	} else {
		IFrame.resetStyle(finalOptions);
		$("#maodIFrameWindow").show();
		WebpageLoader.load(_contentUrl, $("#maodIFrameWindow>div:last>div:eq(1)"), finalOptions);
	}
	//拖动开关
	if (finalOptions.isDraggable) {
		Mouse.activate();
		$("#maodIFrameHead").mousedown(function (_e) {
			if (_e.target.nodeName != "A") {
				var mainIFrame = $("#maodIFrameWindow>div.maodIFrameMain");
				$("#maodIFrameHead").css("cursor", "move");
				var mainIFrameWidth = mainIFrame.width();//弹层宽度
				var mainIFrameHeight = mainIFrame.height();//弹层高度
				var windowWidth = 0;//浏览器窗口有效区域宽度
				var windowHeight = 0;//浏览器窗口有效区域高度
				var leftPX = 0;//弹层距左像素(根据鼠标当前横坐标算出)
				var topPX = 0;//弹层距顶像素(根据鼠标当前纵坐标算出)
				var mainIFrameLeftPX = Formater.getNumberVal(mainIFrame.css("left"));//弹层距左像素
				var mainIFrameTopPX = Formater.getNumberVal(mainIFrame.css("top"));//弹层距顶像素
				var mouseDownX = Mouse.x;//鼠标按下时的横坐标
				var mouseDownY = Mouse.y;//鼠标按下时的纵坐标
				//拖动处理函数
				var dragHandleFn = function () {
					windowWidth = StyleHelper.getWindowWidth();
					windowHeight = StyleHelper.getWindowHeight();
					leftPX = (Mouse.x - (mouseDownX - mainIFrameLeftPX));
					topPX = (Mouse.y - (mouseDownY - mainIFrameTopPX));
					leftPX = (leftPX < 0 ? 0 : (leftPX > (windowWidth - mainIFrameWidth) ? (windowWidth - mainIFrameWidth) : leftPX));//阻止弹层超出窗口左右边
					topPX = (topPX < 0 ? 0 : (topPX > (windowHeight - mainIFrameHeight) ? (windowHeight - mainIFrameHeight) : topPX));//阻止弹层超出窗口上下边
					mainIFrame.css("left", (leftPX + "px"));
					mainIFrame.css("top", (topPX + "px"));
				}
				var oldDragHandleFn = $(this).data("IFrame_open_dragHandleFn");
				//若旧的拖动处理函数非未定义的，就将其解绑
				if (!Validater.isUndefined(oldDragHandleFn)) $(document).unbind("mousemove", oldDragHandleFn);
				$(this).data("IFrame_open_dragHandleFn", dragHandleFn);
				$(document).mousemove($(this).data("IFrame_open_dragHandleFn"));
			}
		});
		$(document).mouseup(function () {
			var dragHandleFn = $("#maodIFrameHead").data("IFrame_open_dragHandleFn");
			if (Validater.isUndefined(dragHandleFn)) return ;
			$(document).unbind("mousemove", dragHandleFn);
		});
	}
}
/**
 * 关闭
 */
IFrame.close = function () {
	//为了避免DOM元素冲突，隐藏前先清空内容Div的Html
	$("#maodIFrameWindow>div:last>div:eq(1)").empty();
	$("#maodIFrameWindow").hide();
	IFrame.unload();
	$("#maodIFrameHead").unbind("mousedown");
	if (IFrame.closeBackFnParams == null) IFrame.closeBackFn();
	else IFrame.closeBackFn(IFrame.closeBackFnParams);
}
/**
 * 销毁
 */
IFrame.destroy = function () {
	$("#maodIFrameWindow").remove();
	WebpageLoader.unload("isIFrame");
	if (IFrame.closeBackFnParams == null) IFrame.closeBackFn();
	else IFrame.closeBackFn(IFrame.closeBackFnParams);
}
/**
 * 执行手动回调函数
 * @param _backParams 回调参数(多个参数推荐使用JSON)
 * 补充说明：
 * 1.若调用时传入了参数，则该参数将作为手动回调函数的参数传入
 * 2.若调用时未传入参数，则会把IFrame.open时的可选项中的manualBackFnParams作为手动回调函数的参数传入
 */
IFrame.executeManualBackFn = function (_backParams) {
	WebpageLoader.executeManualBackFn({backParams:_backParams, isIFrame:true});
}
/**
 * 停止加载
 * 补充说明：该方法并不能中止已发出的Http请求，只是单纯的取消本次加载响应等待
 */
IFrame.unload = function () {
	WebpageLoader.unload("isIFrame");
}
/**
 * 向下滚动固定
 * @param _tarJQueryObj 目标jQuery对象
 * @param _startFiexdScrollPx 滚动多少像素开始固定
 * @param _marginTopPx4body 距body顶部多少像素
 */
IFrame.downScrollFixed = function (_tarJQueryObj, _startFiexdScrollPx, _marginTopPx4body) {
	_marginTopPx4body -= Formater.getNumberVal(_tarJQueryObj.css("margin-top"));
	$(window).bind("scroll", function () {
		if ($(document).scrollTop() > Transverter.toNumber(_startFiexdScrollPx)) {
			_tarJQueryObj.css({'position':'fixed', "top":(Transverter.toNumber(_marginTopPx4body) + "px")});
		} else {
			_tarJQueryObj.css({'position':'static'});
		}
	});
}
/**
 * 跟随鼠标弹出层
 * @param _tarJQueryObj 目标jQuery对象
 * @param _popJQueryObj 弹出jQuery对象
 * @param _popEvent 弹出事件，支持click、mouseover(默认)
 * 补充说明：
 * 1.IE8及其以下兼容性不是很理想
 * 2.同一个目标jQuery对象仅支持一个弹出jQuery对象，之后绑定的弹出jQuery对象将覆盖之前的
 * 3.弹出对象暂仅支持div标签
 */
IFrame.pop4mouse = function (_tarJQueryObj, _popJQueryObj, _popEvent) {
	try {
		if (!_popJQueryObj.is("div")) {
			ExceptionHelper.printStack("弹出对象暂仅支持div标签，请用div标签包裹想要弹出的内容");
			return ;
		}
		//图片绝对路径
		var imgAbsolutePath = {
				arrows:"/ECApp/ECPublic/ECWeb/ECWeb_Images/jiao01.gif"//freedom包中该图片所在相对路径：img/arrows_2.png
		}
		_popJQueryObj.data("IFrame_pop4mouse_valid", true);
		//初始化弹出层装载对象
		if (Validater.isUndefined($("#IFrame_pop4mouse_popObjs"))) {
			$("body").append("<span id=\"IFrame_pop4mouse_popObjs\"></span>");
			var style = "";
			style += "<style>";
				style += (".maodArrows {background:url(\"" + imgAbsolutePath.arrows + "\") no-repeat; width:11px; height:6px; line-height:0px; font-size:0px; overflow:hidden; position:absolute; left:" + (_popJQueryObj.width() / 4 - 6) + "px; top:-6px;}");
			style += "</style>";
			$("#IFrame_pop4mouse_popObjs").prepend(style);
			$("#IFrame_pop4mouse_popObjs").data("popObjIds", new ArrayList());
		}
		if ("click" == _popEvent) _tarJQueryObj.css("cursor", "pointer");
		//若弹出对象没有id，则生成随机id赋值
		if (Validater.isUndefined(_popJQueryObj.attr("id"))) _popJQueryObj.attr("id", "_Freedom_id_" + Freedom.randomInt(8) + "_di_modeerF_");
		var hasThisPopObj = false;
		var popObjId = _popJQueryObj.attr("id");
		$.each($("#IFrame_pop4mouse_popObjs").data("popObjIds").getAll(), function (_i, _v) {
			if (_v == popObjId) hasThisPopObj = true;
		});
		if (!hasThisPopObj) {
			$("#IFrame_pop4mouse_popObjs").append(_popJQueryObj);
			$("#IFrame_pop4mouse_popObjs").data("popObjIds").add(popObjId);
			_popJQueryObj.prepend("<i class=\"maodArrows\"/>");
			_popJQueryObj.css("display", "none");
			_popJQueryObj.css("position", "absolute");
			_popJQueryObj.css("z-index", "100000");
			_popJQueryObj.css("box-shadow", "4px 4px 10px #000");
		}
		Mouse.activate();
		var eventHandler = function () {
			var hasShowed = false;
			$.each($("#IFrame_pop4mouse_popObjs").data("popObjIds").getAll(), function (_i, _v) {
				if ($("#" + _v).isShowed()) hasShowed = true;
			});
			if (_popJQueryObj.isShowed() || !_popJQueryObj.data("IFrame_pop4mouse_valid") || hasShowed) return ;
			//设置左边距，将鼠标定位到弹出层宽度1/4的位置
			var left = (Mouse.x - _popJQueryObj.width() / 4);
			var top = Mouse.y;
			//如果左边距为负数，重置为0，这将避免弹出层左侧1/4部分有可能隐藏至窗口左侧
			if (left < 0) left = 0;
			_popJQueryObj.css("left", left + "px");
			_popJQueryObj.css("top", top + "px");
			_popJQueryObj.show();
			//超出区域处理函数
			var outAreaHandler = function () {
				//超出偏移，也就是说，超出边框15像素才隐藏
				var outOffset = 15;
				//若鼠标超出弹出对象outOffset像素，就将弹出对象隐藏并解绑mousemove事件的outAreaHandler处理函数
				if (Mouse.isOverflow(_popJQueryObj, outOffset)) {//判断超出目标对象：&& Mouse.isOverflow(_tarJQueryObj, outOffset)
					_popJQueryObj.hide();
					$(document).unbind("mousemove", outAreaHandler);
				}
			}
			$(document).mousemove(outAreaHandler);
		}
		var oldPopJQueryObj = _tarJQueryObj.data("IFrame_pop4mouse_popJQueryObj");
		//若存在旧的弹出对象，则使其失效
		if (!Validater.isUndefined(oldPopJQueryObj) && _popJQueryObj.attr("id") != oldPopJQueryObj.attr("id")) oldPopJQueryObj.data("IFrame_pop4mouse_valid", false);
		_tarJQueryObj.data("IFrame_pop4mouse_popJQueryObj", _popJQueryObj);
		var oldEventHandler = _tarJQueryObj.data("IFrame_pop4mouse_eventHandler");
		if (Validater.isUndefined(oldEventHandler)) {
			_tarJQueryObj.data("IFrame_pop4mouse_eventHandler", eventHandler);
			if ("click" == _popEvent) _tarJQueryObj.click(_tarJQueryObj.data("IFrame_pop4mouse_eventHandler"));
			else _tarJQueryObj.mouseover(_tarJQueryObj.data("IFrame_pop4mouse_eventHandler"));
		}
	} catch (e) {
		ExceptionHelper.printStack();
	}
}
/**
 * 格式化器
 */
function Formater() {}
/**
 * 提取数字
 * @param _val 被提取值
 * @return JS.String
 * 补充说明：该方法只是单纯的从头至尾提取数字
 */
Formater.getNumber = function (_val) {
	return _val.replace(/\s/g, '').replace(/\D/g, "");
}
/**
 * 提取数值(包括正负整数、正负小数)
 * @param _val 被提取值
 * @return JS.Number
 * 补充说明：
 * 1.若_val中没有数字，则返回空字符串
 * 2.若有'.'字符，则取第一个'.'作为小数分割点
 * 3.若_val中有'-'字符，则首先会截取第一个'-'作为负号，并截取其后的字符作为被提取值，之前的字符将被丢弃
 */
Formater.getNumberVal = function (_val) {
	_val = Transverter.toStr(_val);
	if (!Validater.hasNumber(_val)) return "";
	var returnVal = 0;
	//负号下标
	var fhIndex = _val.indexOf("-");
	//过滤负号前面的字符
	if (fhIndex >= 0) {
		_val = _val.substring(fhIndex, _val.length);
	}
	//小数点下标
	var decimalIndex = _val.indexOf(".");
	var intVal = 0;
	var decimalVal = 0;
	if (decimalIndex >= 0) {
		intVal = Formater.getNumber(_val.subString(0, decimalIndex));
		decimalVal = Formater.getNumber(_val.subString(decimalIndex, _val.length));
		if (fhIndex >= 0) {
			returnVal = Transverter.toNumber("-" + intVal + "." + decimalVal);//.toFixed(decimalVal.length);
		} else {
			returnVal = Transverter.toNumber(intVal + "." + decimalVal);//.toFixed(decimalVal.length);
		}
	} else {
		intVal = Formater.getNumber(_val);
		if (fhIndex >= 0) {
			returnVal = Transverter.toInt("-" + intVal);
		} else {
			returnVal = Transverter.toInt(intVal);
		}
	}
	return returnVal;
}
/**
 * 手机号码
 * @param _val 需要格式化的值
 * @return JS.String
 * 补充说明：返回格式形如 - 186 7071 7327
 */
Formater.mobilePhone = function (_val) {
	return $.trim(_val.replace(/\s/g, "").replace(/\D/g, "").replace(/(\d{3})(\d*)/g, "$1 $2").replace(/(\d{4})(\d*)/g, "$1 $2"));
}
/**
 * 银行卡号码
 * @param _val 需要格式化的值
 * @return JS.String
 * 补充说明：返回格式形如 - 6226 6226 0397 3344
 */
Formater.bankCardNumber = function (_val) {
	return $.trim(_val.replace(/\s/g, "").replace(/\D/g, "").replace(/(\d{4})/g, "$1 "));
}
/**
 * 金额
 * @param _val 需要格式化的值
 * @param _returnMode 返回模式[1:返回格式形如 - 1,000,000.00, 其他:返回格式形如 - 1000000.00]
 * @param _isRound 是否四舍五入[true:是, 其他:否]
 * @return JS.String
 * 补充说明：返回格式形如 - 1000000.00
 */
Formater.money = function (_val, _returnMode, _isRound) {
	if (!Validater.hasNumber(_val)) return "";
	_isRound = (_isRound == true ? true : false);
	var val = Formater.decimal(_val, 2, _isRound);
	var isFuShu = false;
	if (val.charAt(0) == "-") {
		isFuShu = true;
		val = val.subString(1);
	}
	var dicimalIndex = val.indexOf(".");
	var intVal = val.subString(0, dicimalIndex);
	var decimalVal = val.subString(dicimalIndex + 1);
	val = intVal.getReversedStr().replace(/(\d{3})/g, "$1,");
	val = val.getReversedStr();
	val = val.charAt(0) == "," ? val.subString(1) : val;
	if (_returnMode == 1) {
		//返回格式形如 - 1,000,000.00
		return (isFuShu ? ("-" + val + "." + decimalVal) : (val + "." + decimalVal));
	}
	//返回格式形如 - 1000000.00
	return (isFuShu ? ("-" + val + "." + decimalVal).replace(/\,/g, "") : (val + "." + decimalVal).replace(/\,/g, ""));
	
}
/**
 * 小数
 * @param _val 需要格式化的值
 * @param _keepLen 保留长度(默认全部，最多保留20位小数)
 * @param _isRound 是否四舍五入(默认false)[true:是, 其他:否]
 * @return JS.String
 */
Formater.decimal = function (_val, _keepLen, _isRound) {
	if (Validater.isUndefined(_keepLen) || !Validater.isNumberObj(_keepLen) || _keepLen < 0) return _val;
	_keepLen = (_keepLen > 20 ? 20 : _keepLen);
	_val = Transverter.toStr(_val);
	if (!Validater.hasNumber(_val)) return "";
	if (_isRound == true || _val.indexOf(".") < 0) return Formater.getNumberVal(_val).toFixed(_keepLen);
	if (_val.subString(_val.indexOf(".") + 1).length <= _keepLen) return Formater.getNumberVal(_val).toFixed(_keepLen);
	return Formater.getNumberVal(_val.subString(0, _val.indexOf(".") + _keepLen + 1)).toFixed(_keepLen);
}
/**
 * 鼠标
 */
function Mouse () {}
//横坐标
Mouse.x = 0;
//纵坐标
Mouse.y = 0;
//是否刷新
Mouse.isRefresh = true;
//可停止的
Mouse.stoppable = false;
//是否打印坐标
Mouse.isPrintCoordinate = false;
/**
 * 刷新坐标
 * @param _e 事件信息
 */
Mouse.refreshCoordinate = function (_e) {
	Mouse.x = _e.pageX;
	Mouse.y = _e.pageY;
	if (Mouse.isPrintCoordinate) Printer.info("当前坐标：（X:" + Mouse.x + ", Y:" + Mouse.y + "）");
}
/**
 * 激活
 */
Mouse.activate = function () {
	if (!Validater.isUndefined(Mouse.moveHandleFn)) return ;
Printer.info("mouse activated");
	Mouse.isRefresh = true;
	//鼠标移动处理函数
	Mouse.moveHandleFn = function (_e) {
		if (!Mouse.isRefresh) {
			$(document).unbind("mousemove", Mouse.moveHandleFn);
			Mouse.moveHandleFn = null;
			return ;
		}
		Mouse.refreshCoordinate(_e);
	}
	//给文档绑定鼠标移动处理事件
	$(document).mousemove(Mouse.moveHandleFn);
}
/**
 * 停止
 */
Mouse.stop = function () {
	if (Mouse.stoppable) Mouse.isRefresh = false;
}
/**
 * 鼠标是否超出区域
 * @param _tarJQueryObj 目标对象
 * @param _outOffset 超出偏移，单位(像素)
 * @return JS.Boolean
 */
Mouse.isOverflow = function (_tarJQueryObj, _outOffset) {
	if (!Validater.isJQueryObj(_tarJQueryObj)) return ;
	//距窗口左边框偏移
	var xOffset = _tarJQueryObj.offset().left;
	//距窗口顶边框偏移
	var yOffset = _tarJQueryObj.offset().top;
	var width = _tarJQueryObj.width();
	var height = _tarJQueryObj.height();
	//超出偏移，也就是说，鼠标超出边框outOffset像素才隐藏
	var outOffset = Transverter.toNumber(_outOffset);
	//若小于0，则初始化为0
	if (outOffset < 0) outOffset = 0;
	return (Mouse.x < (xOffset - outOffset) || Mouse.x > (xOffset + width + outOffset)|| Mouse.y < (yOffset - outOffset) || Mouse.y > (yOffset + height + outOffset));
}
/**
 * JSON帮助器
 */
function JsonHelper() {}
/**
 * 继承
 * @param _parentJsonObj 父JSON对象
 * @param _sonJsonObj 子JSON对象
 * @return 继承后的JSON对象
 */
JsonHelper.extend = function (_parentJsonObj, _sonJsonObj) {
	return $.extend(_parentJsonObj, _sonJsonObj);
}
/**
 * JSON转换成字符串
 * @param _jsonObj JSON对象
 * 补充说明：该字符串不能直接通过JsonHelper.toJson转换成Json对象
 */
JsonHelper.toString = function (_jsonObj) {
	if (JsonHelper.isEmptyObj(_jsonObj)) return "{}";
	var str = "";
	if (!Validater.isJsonObj(_jsonObj)) return str;
	str += "{";
	$.each(_jsonObj, function (_i, _v) {
		if (_v instanceof JsonMap) {
			str += (_i + ":" + _v.toString());//JsonHelper.toString(_v.getAll()));
		} else if (Validater.isJsonObj(_v)) {
			str += (_i + ":" + JsonHelper.toString(_v));
		} else if (_v instanceof ArrayList) {
			str += (_i + ":" + _v.toString());
		} else if (Validater.isArrayObj(_v)) {
			var arrayList = new ArrayList();
			arrayList.addAll(_v);
			str += (_i + ":" + arrayList.toString());
		} else {
			str += (_i + ":" + _v);
		}
		str += ", "
	});
	//去掉最后的逗号和空格
	str = str.substring(0, str.length-2);
	str += "}";
	return str;
}
/**
 * Json字符串转换成Json
 * @param _jsonStr 需要转换的JSON字符串
 * @return JSON对象
 * 补充说明：
 * 1.格式须为：{"name":"zhang3", "sex":"男", "age":"22", "array":[1, 2, 3, "zhangsan"]...}
 * 2.格式中的"不可用'替代
 * 3.仅支持数字、字符串、以[]方式包裹起来的数组
 * 4.传入一个畸形的JSON字符串会抛出一个异常，比如下面的都是畸形的JSON字符串：
 * 		{test:1} (test 没有包围双引号)
 * 		{'test':1} (使用了单引号而不是双引号)
 */
JsonHelper.toJson = function (_jsonStr) {
	return $.parseJSON(_jsonStr);
}
/**
 * 是否为空对象
 * @param _jsonObj JSON对象
 * @return JS.Boolean
 * 补充说明：可以匹配 - null, "", {}, []
 */
JsonHelper.isEmptyObj = function (_jsonObj) {
	return $.isEmptyObject(_jsonObj);
}
/**
 * 光标
 */
function Cursor() {}
/**
 * 得到选择的文本
 * @return JS.String
 */
Cursor.getSelectedText = function () {
	if ($.browser != null && !$.browser.msie) return ;// 不是IE则返回，因为非IE浏览器不支持此方法以下代码从而导致JS异常
	return $.trim(document.selection.createRange().text);
}
/**
 * 检查参数
 * @param methodName 被检查方法名
 * @param _id DOM.id
 * @return JS.Boolean
 */
Cursor.checkParams = function (methodName, _id) {
	if ($.browser != null && !$.browser.msie) return ;// 不是IE则返回，因为非IE浏览器不支持此方法以下代码从而导致JS异常
	var element = document.getElementById(_id);
	if (Validater.isUndefined(element)) {
Printer.error("Cursor." + methodName + "(_id):找不到id为" + _id + "的对象");
		return false;
	}
	if (!$(element).is("input[type='text']") && !$(element).is("textarea")) {
Printer.error("Cursor." + methodName + "(_id):id为" + _id + "的对象，不是文本输入框或文本域");
		return false;
	}
	return true;
}
/**
 * 得到当前位置，暂未实现
 */
Cursor.getCurrentPosition = function (_id) {
	if ($.browser != null && !$.browser.msie) return ;// 不是IE则返回，因为非IE浏览器不支持此方法以下代码从而导致JS异常
	//待实现...
}
/**
 * 置前
 * @param _id DOM.id
 */
Cursor.toFirst = function (_id) {
	if ($.browser != null && !$.browser.msie) return ;// 不是IE则返回，因为非IE浏览器不支持此方法以下代码从而导致JS异常
	if (!Cursor.checkParams("toFirst", _id)) return ;
	var textRange = document.getElementById(_id).createTextRange();
	textRange.collapse(true);
	textRange.select();
}
/**
 * 置后
 * @param _id DOM.id
 */
Cursor.toLast = function (_id) {
	if ($.browser != null && !$.browser.msie) return ;// 不是IE则返回，因为非IE浏览器不支持此方法以下代码从而导致JS异常
	if (!Cursor.checkParams("toLast", _id)) return ;
	var textRange = document.getElementById(_id).createTextRange();
	textRange.collapse(false);
	textRange.select();
}
/**
 * 移动
 * @param _id DOM.id
 * @param _step 步长
 * 补充说明：_step > 0 时(右移)，_step < 0 时(左移)
 */
Cursor.move = function (_id, _step) {
	if ($.browser != null && !$.browser.msie) return ;// 不是IE则返回，因为非IE浏览器不支持此方法以下代码从而导致JS异常
	if (!Cursor.checkParams("move", _id)) return ;
	var jsObj = document.getElementById(_id);
	jsObj.focus();
	var range = document.selection.createRange();
	range.collapse(false);
	range.move("character", Transverter.toInt(_step));
	range.select();
}
/**
 * 左移
 * @param _id DOM.id
 * @param _step 步长
 */
Cursor.leftMove = function (_id, _step) {
	if ($.browser != null && !$.browser.msie) return ;// 不是IE则返回，因为非IE浏览器不支持此方法以下代码从而导致JS异常
	if (!Cursor.checkParams("leftMove", _id)) return ;
	var step = Transverter.toInt(_step);
	if (step < 0) step = 0;
	Cursor.move(_id, -step);
}
/**
 * 右移
 * @param _id DOM.id
 * @param _step 步长
 */
Cursor.rightMove = function (_id, _step) {
	if ($.browser != null && !$.browser.msie) return ;// 不是IE则返回，因为非IE浏览器不支持此方法以下代码从而导致JS异常
	if (!Cursor.checkParams("rightMove", _id)) return ;
	var step = Transverter.toInt(_step);
	if (step < 0) step = 0;
	Cursor.move(_id, step);
}
/**
 * 数据帮助者
 */
function DatumHelper () {}
/**
 * 获取身份证号码中的出生日期
 * @param _idCardNo 身份证号码
 * 补充说明：支持15、18位身份证
 */
DatumHelper.getBirthday4idCardNo = function (_idCardNo) {
	_idCardNo = Formater.getNumber(_idCardNo);
	if (_idCardNo.length == 18) return _idCardNo.subString(6, 14);
	if (_idCardNo.length == 15) return "19" + _idCardNo.subString(6, 12);
	return "";
}
/**
 * HTML标签帮助者
 */
function HtmlLabelHelper () {}
/**
 * 获取radio group当前选中项的值
 * @param _jQueryObj jQuery对象
 * @return JS.String or undefined
 */
HtmlLabelHelper.getRadioGroupCheckedVal = function (_jQueryObj) {
	if (!_jQueryObj.is("input:radio")) return ;
	return $("input[name=" + _jQueryObj.attr("name") + "]:checked").val();
}
/**
 * 获取select当前选中项的展示文本或值
 * @param _jQueryObj jQuery对象
 * @param _textOrValue 展示文本或值
 * @return JS.String or undefined
 */
HtmlLabelHelper.getSelectedTextOrValue = function (_jQueryObj, _textOrValue) {
	if (!_jQueryObj.is("select")) return ;
	return (_textOrValue == "text" ? _jQueryObj.find("option:selected").text() : _jQueryObj.val());
}
/**
 * 是否选中
 * @param _jQueryObj jQuery对象
 * @return JS.Boolean
 */
HtmlLabelHelper.isChecked = function (_jQueryObj) {
	//待完成...
}
/**
 * 动画帮助者
 * @param _tarJQueryObj 目标jQuery对象
 * 补充说明：
 * 1、若构造对象时传入的参数不匹配，则控制台会打印错误日志：AnimateHelper构造参数异常
 * 2、若构造之后，目标jQuery对象的宽高有变动，需要重新构造，否则，动画还是采用之前构造时的宽高
 */
function AnimateHelper (_tarJQueryObj) {
	if (arguments.length != 1 || !Validater.isJQueryObj(_tarJQueryObj)) {
Printer.error("错误：AnimateHelper构造参数异常");
		return ;
	}
	//目标jQuery对象
	var tarJQueryObj = _tarJQueryObj;
	
	// B 往jQuery的数据存储域存储初始数据
	//原始宽度
	if (Validater.isUndefined(tarJQueryObj.data("AnimateHelper_original_width"))) tarJQueryObj.data("AnimateHelper_original_width", tarJQueryObj.width());
	//原始高度
	if (Validater.isUndefined(tarJQueryObj.data("AnimateHelper_original_height"))) tarJQueryObj.data("AnimateHelper_original_height", tarJQueryObj.height());
	//当前的动画动作
	if (Validater.isUndefined(tarJQueryObj.data("AnimateHelper_currentAnimateAction"))) tarJQueryObj.data("AnimateHelper_currentAnimateAction", "");
	// E 往jQuery的数据存储域存储初始数据
	
	/**
	 * 向上滑动隐藏
	 * @param _animateTime 动画时间(单位：毫秒)
	 * @param _backFn 回调函数
	 */
	this.upSlide = function (_animateTime, _backFn) {
Printer.info("currentAnimateAction : " + tarJQueryObj.data("AnimateHelper_currentAnimateAction"));
		if (tarJQueryObj.data("AnimateHelper_currentAnimateAction") == "upSlide") return ;
		tarJQueryObj.stop(true, false);
		tarJQueryObj.css("outline", "");
		tarJQueryObj.data("AnimateHelper_currentAnimateAction", "upSlide");//currentAnimateAction = "upSlide";
Printer.info("up-original_height:" + tarJQueryObj.data("AnimateHelper_original_height"));
		tarJQueryObj.animate({"height":"0px"}, _animateTime, function () {
			$(this).hide();//animate不支持display动画，所以就放到这里隐藏
			if (Validater.isFunctionObj(_backFn)) _backFn();
		});
	}
	/**
	 * 向下滑动显示
	 * @param _animateTime 动画时间(单位：毫秒)
	 * @param _backFn 回调函数
	 */
	this.downSlide = function (_animateTime, _backFn) {
Printer.info("currentAnimateAction : " + tarJQueryObj.data("AnimateHelper_currentAnimateAction"));
		if (tarJQueryObj.data("AnimateHelper_currentAnimateAction") == "downSlide") return ;
		tarJQueryObj.stop(true, false);
		tarJQueryObj.css("outline", "");
		tarJQueryObj.data("AnimateHelper_currentAnimateAction", "downSlide");//currentAnimateAction = "downSlide";
		if (!tarJQueryObj.isShowed()) tarJQueryObj.css("height", "0px");//这行代码是为了保证高度不为0px且隐藏时，同样有下滑动画效果
		tarJQueryObj.show();//animate不支持display动画，所以就放到这里显示
Printer.info("down-original_height:" + tarJQueryObj.data("AnimateHelper_original_height"));
		tarJQueryObj.animate({"height":(tarJQueryObj.data("AnimateHelper_original_height") + "px")}, _animateTime, function () {
			if (Validater.isFunctionObj(_backFn)) _backFn();
		});
	}
	/**
	 * 向左滑动隐藏
	 * @param _animateTime 动画时间(单位：毫秒)
	 * @param _backFn 回调函数
	 */
	this.leftSlide = function (_animateTime, _backFn) {
Printer.info("currentAnimateAction : " + tarJQueryObj.data("AnimateHelper_currentAnimateAction"));
		if (tarJQueryObj.data("AnimateHelper_currentAnimateAction") == "leftSlide") return ;
		tarJQueryObj.stop(true, false);
		tarJQueryObj.css("outline", "");
		tarJQueryObj.data("AnimateHelper_currentAnimateAction", "leftSlide");//currentAnimateAction = "leftSlide";
Printer.info("left-original_width:" + tarJQueryObj.data("AnimateHelper_original_width"));
		tarJQueryObj.animate({"width":"0px"}, _animateTime, function () {
			$(this).hide();//animate不支持display动画，所以就放到这里隐藏
			if (Validater.isFunctionObj(_backFn)) _backFn();
		});
	}
	/**
	 * 向右滑动显示
	 * @param _animateTime 动画时间(单位：毫秒)
	 * @param _backFn 回调函数
	 */
	this.rightSlide = function (_animateTime, _backFn) {
Printer.info("currentAnimateAction : " + tarJQueryObj.data("AnimateHelper_currentAnimateAction"));
		if (tarJQueryObj.data("AnimateHelper_currentAnimateAction") == "rightSlide") return ;
		tarJQueryObj.stop(true, false);
		tarJQueryObj.css("outline", "");
		tarJQueryObj.data("AnimateHelper_currentAnimateAction", "rightSlide");//currentAnimateAction = "rightSlide";
		if (!tarJQueryObj.isShowed()) tarJQueryObj.css("width", "0px");//这行代码是为了保证宽度不为0px且隐藏时，同样有右滑动画效果
		tarJQueryObj.show();//animate不支持display动画，所以就放到这里显示
Printer.info("right-original_width:" + tarJQueryObj.data("AnimateHelper_original_width"));
		tarJQueryObj.animate({"width":(tarJQueryObj.data("AnimateHelper_original_width") + "px")}, _animateTime, function () {
			if (Validater.isFunctionObj(_backFn)) _backFn();
		});
	}
	/**
	 * 闪烁
	 * @param _options 可选项 {
	 * 		width : 宽度(默认2px),
	 * 		style : 样式(默认solid),
	 * 		color : 颜色(默认red),
	 * 		times : 次数(默认3次)
	 * }
	 * 补充说明：不支持IE7及其以下浏览器版本
	 */
	this.flicker = function (_options) {
Printer.info("currentAnimateAction : " + tarJQueryObj.data("AnimateHelper_currentAnimateAction"));
		var defaultOptions = {width:"2px", style:"solid", color:"red", times:3};
		var finalOptions = JsonHelper.extend(defaultOptions, _options);
/*
		var flickerHandler = function () {
			// B 动画回调方式，缺点：不可动态指定闪烁次数，必须写死
			tarJQueryObj.animate({"":""}, 200, function () {
				$(this).css("outline", finalOptions.width + " " + finalOptions.style + " " + finalOptions.color);
				$(this).animate({"":""}, 100, function () {
					$(this).css("outline", "");
					$(this).animate({"":""}, 100, function () {
						$(this).css("outline", finalOptions.width + " " + finalOptions.style + " " + finalOptions.color);
						$(this).animate({"":""}, 100, function () {
							$(this).css("outline", "");
							$(this).animate({"":""}, 100, function () {
								$(this).css("outline", finalOptions.width + " " + finalOptions.style + " " + finalOptions.color);
								$(this).animate({"":""}, 100, function () {
									$(this).css("outline", "");
									$(this).animate({"":""}, 100, function () {
										$(this).css("outline", finalOptions.width + " " + finalOptions.style + " " + finalOptions.color);
										$(this).animate({"":""}, 200, function () {
											$(this).css("outline", "");
										});
									});
								});
							});
						});
					});
				});
			});
			// E 动画回调方式，缺点：不可动态指定闪烁次数
		}
*/
		var flickerHandler = function () {
			// B 动画队列方式
			for (var index = 0; index < (finalOptions.times * 2); index ++) {
				//第一次
				if (index == 0) {
					tarJQueryObj.animate({"width":(tarJQueryObj.width() + "px")}, 0, function () {
						$(this).css("outline", finalOptions.width + " " + finalOptions.style + " " + finalOptions.color);
					});
					continue;
				}
				//最后一次
				if (index == (finalOptions.times * 2) - 1) {
					tarJQueryObj.animate({"width":(tarJQueryObj.width() + "px")}, 200, function () {
						$(this).css("outline", "");
					});
					continue;
				}
				//中间的次数切换
				if (index % 2 == 0) {
					tarJQueryObj.animate({"width":(tarJQueryObj.width() + "px")}, 100, function () {
						$(this).css("outline", finalOptions.width + " " + finalOptions.style + " " + finalOptions.color);
					});
				} else {
					tarJQueryObj.animate({"width":(tarJQueryObj.width() + "px")}, 100, function () {
						$(this).css("outline", "");
					});
				}
			}
			// E 动画队列方式
		}
		
		tarJQueryObj.stop(true, false);
		if (tarJQueryObj.data("AnimateHelper_currentAnimateAction") == "upSlide") {
			tarJQueryObj.data("AnimateHelper_currentAnimateAction", "flicker");
			this.upSlide(100, function () {flickerHandler();});
		} else if (tarJQueryObj.data("AnimateHelper_currentAnimateAction") == "downSlide") {
			tarJQueryObj.data("AnimateHelper_currentAnimateAction", "flicker");
			this.downSlide(100, function () {flickerHandler();});
		} else if (tarJQueryObj.data("AnimateHelper_currentAnimateAction") == "leftSlide") {
			tarJQueryObj.data("AnimateHelper_currentAnimateAction", "flicker");
			this.leftSlide(100, function () {flickerHandler();});
		} else if (tarJQueryObj.data("AnimateHelper_currentAnimateAction") == "rightSlide") {
			tarJQueryObj.data("AnimateHelper_currentAnimateAction", "flicker");
			this.rightSlide(100, function () {flickerHandler();});
		} else {
			tarJQueryObj.data("AnimateHelper_currentAnimateAction", "flicker");
			flickerHandler();
		}
	}
}
/**
 * keyup事件帮助者
 */
function KeyUpEventHelper() {}
/**
 * 回车
 * @param _tarJQueryObj 目标jQuery对象
 * @param _handleFn 处理函数
 * 补充说明：
 * 1.同一jQuery对象重复绑定会自动清除上次绑定的事件
 * 2.若_handleFn有参数，可以把参数追加至_handleFn参数后，第三个及其以后的参数都将视为_handleFn的参数依次传入
 */
KeyUpEventHelper.enter = function (_tarJQueryObj, _handleFn) {
	var _arguments = arguments;
Printer.info("KeyUpEventHelper.enter.arguments.length : " + _arguments.length);
	_handleFn = (Validater.isFunctionObj(_handleFn) ? _handleFn : function () {});
	var handler = function (_e) {
		if (_e.keyCode == 13) {
			if (_arguments.length <= 2) _handleFn();
			else {
				var preExecuteCodeStr = "_handleFn(";
				for (var index = 2; index < _arguments.length; index ++) {
					preExecuteCodeStr += ("_arguments[" + index + "], ");
				}
				preExecuteCodeStr = preExecuteCodeStr.substring(0, preExecuteCodeStr.length - 2);
				preExecuteCodeStr += ");";
Printer.info("preExecuteCodeStr : " + preExecuteCodeStr);
				eval(preExecuteCodeStr);
			}
		}
	};
	var oldHanlder = _tarJQueryObj.data("KeyUpEventHelper_enter_handler");
	if (!Validater.isUndefined(oldHanlder)) _tarJQueryObj.unbind("keyup", oldHanlder);
	_tarJQueryObj.data("KeyUpEventHelper_enter_handler", handler);
	_tarJQueryObj.keyup(handler);
}
/**
 * 单例模板(这里仅支持一次HTTP跳转(Ajax请求除外)单例，并不能实现与Java相同的单例)
 */
function SingletonTemplete() {
	this.age = 0;
	this.name = "Freedom.20140625";
}
SingletonTemplete.singleton = null;
SingletonTemplete.getInstance = function () {
	if (SingletonTemplete.singleton == null) SingletonTemplete.singleton = new SingletonTemplete();
	return SingletonTemplete.singleton;
}
/**
 * 重载模板
 */
function OverrideTemplete() {}
OverrideTemplete.overrideMethodName = function () {
	if (arguments.length == 1) OverrideTemplete.overrideMethodName_override1(arguments[0]);
	else if (arguments.length == 2) OverrideTemplete.overrideMethodName_override2(arguments[0], arguments[1]);
	else if (arguments.length == 3) OverrideTemplete.overrideMethodName_override3(arguments[0], arguments[1], arguments[2]);
	//else if...circle
}
OverrideTemplete.overrideMethodName_override1 = function (param1) {}
OverrideTemplete.overrideMethodName_override2 = function (param1, param2) {}
OverrideTemplete.overrideMethodName_override3 = function (param1, param2, param3) {}
//Override.overrideMethodName_override4 ... circle
/**
 * Freedom
 */
function Freedom() {}
/**
 * 代理，可用来改变函数内this的作用域
 * @param _proxyObj 代理对象
 * @param _proxyedObj 被代理对象或函数
 * @return JS.function
 */
Freedom.proxy = function (_proxyObj, _proxyedObj) {
	//jQuery.proxy(被代理对象, 代理对象);
	return $.proxy(_proxyedObj, _proxyObj);
}
/**
 * 增加jQuery插件
 * @param _fnsJson JSON格式的function集合
 * 补充说明：若传入{a:function () {}, b:function (param1, param2...) {}, ...}，扩展后，jQuery对象就可以这么来调用：jQueryObj.a();、jQueryObj.b(param1, param2...);、...
 */
Freedom.addJQueryPlug = function (_fnsJson) {
	//扩展jQuery的方法
	$.fn.extend(_fnsJson);
	//一下注释的代码是增加单个jQuery方法的方式
	//$.fn.fnName = function () {}
}
/**
 * 暗示(采用HTML5属性实现)
 * @param _tarJQueryObj 目标jQuery对象
 * @param _displayText 展示文本
 */
Freedom.placeHolder = function (_tarJQueryObj, _displayText) {
	_tarJQueryObj.attr("placeholder", _displayText);
}
/**
 * 暗示(暂仅支持的HTML标签：text、password、textarea)
 * @param _tarJQueryObj 目标jQuery对象
 * @param _hintText 示意文本
 * @param _mode 模式["clear":获焦会消除暗示文本, "noClear":获焦不会消除暗示文本(默认)]
 * 补充说明：clear模式下密码输入框不支持IE9以下版本
 */
Freedom.hint = function (_tarJQueryObj, _hintText, _mode) {
	if ("clear" == _mode) Freedom.hint4clear(_tarJQueryObj, _hintText);
	else Freedom.hint4noClear(_tarJQueryObj, _hintText);
}
/**
 * 暗示(暂仅支持的HTML标签：text、password、textarea)
 * @param _tarJQueryObj 目标jQuery对象
 * @param _hintText 示意文本
 * 补充说明：
 * 1.密码输入框不支持IE9以下版本
 * 2.获焦会消除暗示文本
 */
Freedom.hint4clear = function (_tarJQueryObj, _hintText) {
	if (!Validater.isJQueryObj(_tarJQueryObj)) {
		ExceptionHelper.printStack("参数异常，_tarJQueryObj不是jQuery对象");
		return ;
	}
	if (!_tarJQueryObj.is("input[type='text']") && !_tarJQueryObj.is("input[type='password']") && !_tarJQueryObj.is("textarea")) {
		ExceptionHelper.printStack("参数异常，_tarJQueryObj不是文本输入框");
		return ;
	}
	var oldHintMode = _tarJQueryObj.data("currentHintMode");
	if (oldHintMode == "noClear" && _tarJQueryObj.is("input[type='password']"))
		_tarJQueryObj.next("input[name='_Freedom_hint_password_bak_']").data("currentHintMode", "clear");
	_tarJQueryObj.data("currentHintMode", "clear");
	
	var oldHintText = _tarJQueryObj.data("hintText4clear");//旧暗示文本
	_tarJQueryObj.data("hintText4clear", _hintText);
	if (oldHintMode == "noClear") _tarJQueryObj.attr("_Freedom_hint_password_toggle_", "open");
	
	if (_tarJQueryObj.val().trim() == "" || _tarJQueryObj.val().trim() == oldHintText
			|| (oldHintMode == "noClear" && _tarJQueryObj.val().trim() == _tarJQueryObj.data("hintText4noClear"))) {
		if (_tarJQueryObj.is("input[type='password']")) {
			_tarJQueryObj.attr({"type":"text", "_Freedom_hint_password_toggle_":"close"});
			_tarJQueryObj.next("input[name='_Freedom_hint_password_bak_']").hide();
			_tarJQueryObj.show();
		}
		_tarJQueryObj.val(_tarJQueryObj.data("hintText4clear"));
		_tarJQueryObj.css("color", "#999999");
	}
	if (!Validater.isUndefined(oldHintText)) return ;
	
	var focusHandle4clear = function () {
		if (_tarJQueryObj.data("currentHintMode") != "clear") return ;
		if ($(this).is("input[type='text']") && $(this).attr("_Freedom_hint_password_toggle_") == "close") {
			$(this).attr({"type":"password", "_Freedom_hint_password_toggle_":"open"});
		}
		if ($(this).val() == $(this).data("hintText4clear")) {
			$(this).val("");
		}
		$(this).css("color", "#000000");
	};
	_tarJQueryObj.data("focusHandle4clear", focusHandle4clear);
	var blurHandle4clear = function () {
		if (_tarJQueryObj.data("currentHintMode") != "clear") return ;
		if ($.trim($(this).val()) == "" && !$(this).is("input[type='password']")) {
			$(this).val($(this).data("hintText4clear"));
			$(this).css("color", "#999999");
		} else if ($(this).val() == "" && $(this).is("input[type='password']")) {
			$(this).val($(this).data("hintText4clear"));
			$(this).css("color", "#999999");
			if ($(this).attr("_Freedom_hint_password_toggle_") == "open") {
				$(this).attr({"type":"text", "_Freedom_hint_password_toggle_":"close"});
			}
		}
	};
	_tarJQueryObj.data("blurHandle4clear", blurHandle4clear);
	_tarJQueryObj.bind({
		focus : focusHandle4clear,
		blur : blurHandle4clear
	});
}
/**
 * 暗示(暂仅支持的HTML标签：text、password、textarea)
 * @param _tarJQueryObj 目标jQuery对象
 * @param _hintText 示意文本
 * 补充说明：获焦不会消除暗示文本
 */
Freedom.hint4noClear = function (_tarJQueryObj, _hintText) {
	if (!Validater.isJQueryObj(_tarJQueryObj)) {
		ExceptionHelper.printStack("参数异常，_tarJQueryObj不是jQuery对象");
		return ;
	}
	if (!_tarJQueryObj.is("input[type='text']") && !_tarJQueryObj.is("input[type='password']") && !_tarJQueryObj.is("textarea")) {
		ExceptionHelper.printStack("参数异常，_tarJQueryObj不是文本输入框");
		return ;
	}
	var oldHintMode = _tarJQueryObj.data("currentHintMode");
	_tarJQueryObj.data("currentHintMode", "noClear");
	
	var oldHintText = _tarJQueryObj.data("hintText4noClear");//旧暗示文本
	_tarJQueryObj.data("hintText4noClear", _hintText);
	
	if (oldHintMode == "clear" && _tarJQueryObj.attr("_freedom_hint_password_toggle_") == "close") {
		_tarJQueryObj.attr("type", "password");
		_tarJQueryObj.next("input[name='_Freedom_hint_password_bak_']").data("currentHintMode", "noClear");
		if (_tarJQueryObj.val().trim() == "" || _tarJQueryObj.val().trim() == _tarJQueryObj.data("hintText4clear")) {
			_tarJQueryObj.hide();
			_tarJQueryObj.next("input[name='_Freedom_hint_password_bak_']").show();
			_tarJQueryObj.val("");
			_tarJQueryObj.css("color", "#000000");
		}
	}
	
	if (_tarJQueryObj.is("input[type='password']")) {
		var bakJQueryObj = _tarJQueryObj.next("input[name='_Freedom_hint_password_bak_']");
		bakJQueryObj.data("hintText4noClear", _hintText);
		if (bakJQueryObj.isShowed()) bakJQueryObj.val(_hintText);
	} else if (_tarJQueryObj.val() == oldHintText
			|| (oldHintMode == "clear" && _tarJQueryObj.val().trim() == _tarJQueryObj.data("hintText4clear")))
		_tarJQueryObj.val(_hintText);

	if (!Validater.isUndefined(oldHintText)) return ;
	if (_tarJQueryObj.is("input[type='password']")) {
		_tarJQueryObj.after("<input name='_Freedom_hint_password_bak_' type='text'/>");
		_tarJQueryObj.next("input[name='_Freedom_hint_password_bak_']").attr("class", _tarJQueryObj.attr("class"));
		_tarJQueryObj.next("input[name='_Freedom_hint_password_bak_']").attr("style", _tarJQueryObj.attr("style"));
		_tarJQueryObj.next("input[name='_Freedom_hint_password_bak_']").css("font-size", "14px");
		_tarJQueryObj.next("input[name='_Freedom_hint_password_bak_']").hint(_tarJQueryObj.data("hintText4noClear"));
		_tarJQueryObj.next("input[name='_Freedom_hint_password_bak_']").hide();
	}
	if ($.trim(_tarJQueryObj.val()) == "" || $.trim(_tarJQueryObj.val()) == oldHintText) {
		if (_tarJQueryObj.is("input[type='password']")) {
			_tarJQueryObj.hide();
			_tarJQueryObj.next("input[name='_Freedom_hint_password_bak_']").show();
		} else {
			_tarJQueryObj.val(_tarJQueryObj.data("hintText4noClear"));
			_tarJQueryObj.css("color", "#999999");
		}
	}
	
	var focusHandle4noClear = function () {
		if (_tarJQueryObj.data("currentHintMode") != "noClear") return ;
		if ($(this).val() == $(this).data("hintText4noClear")) {
			$(this).css("color", "#999999");
			if (Validater.isUndefined($(this).attr("id"))) {
				var randomId = Freedom.randomInt(8);
				$(this).attr("id", "_Freedom_random_id_" + randomId + "_");
			}
			Cursor.toFirst($(this).attr("id"));
		}
	};
	_tarJQueryObj.data("focusHandle4noClear", focusHandle4noClear);
	
	var blurHandle4noClear = function () {
		if (_tarJQueryObj.data("currentHintMode") != "noClear") return ;
		if (($.trim($(this).val()) == "" || $(this).val() == $(this).data("hintText4noClear")) && !$(this).is("input[type='password']")) {
			$(this).val($(this).data("hintText4noClear"));
			$(this).css("color", "#999999");
		} else if ($(this).val() == "" && $(this).is("input[type='password']")) {
			$(this).hide();
			$(this).next("input[name='_Freedom_hint_password_bak_']").show();
			$(this).next("input[name='_Freedom_hint_password_bak_']").val($(this).data("hintText4noClear"));
		}
	};
	_tarJQueryObj.data("blurHandle4noClear", blurHandle4noClear);
	
	var keydownHandle4noClear = function () {
		if (_tarJQueryObj.data("currentHintMode") != "noClear") return ;
		if ($(this).val() == $(this).data("hintText4noClear")) {
			if ($(this).attr("name") == "_Freedom_hint_password_bak_") {
				//$(this).prev("input[type='password']").attr("text", "password");
				$(this).hide();
				$(this).prev("input[type='password']").show();
				$(this).prev("input[type='password']").focus();
			} else {
				$(this).css("color", "#000000");
				$(this).val("");
			}
		}
	};
	_tarJQueryObj.data("keydownHandle4noClear", keydownHandle4noClear);
	
	_tarJQueryObj.bind({
		focus : focusHandle4noClear,
		blur : blurHandle4noClear,
		keydown : keydownHandle4noClear
	});
}
/**
 * 获取认/申购手续费集
 * @param _buyMoney 购买金额
 * @param _rate 费率
 * @param _discount 折扣
 * @return JS.JsonMap[ysxf:原手续费, xsxf:现手续费, ssxf:省手续费]
 */
Freedom.getBuyHandlingCharges = function (_buyMoney, _rate, _discount) {
	if (arguments.length != 3) {ExceptionHelper.printStack("参数错误"); return ;}
Printer.info("购买金额:" + _buyMoney + ", 费率:" + _rate + ", 折扣:" + _discount);
	var _buyMoney = Transverter.toNumber(_buyMoney);
	var _discount = Transverter.toNumber(_discount);
	//原手续费
	var ysxf = Formater.money((_buyMoney * _rate * 0.01) / (1 + _rate * 0.01));
	//现手续费
	var xsxf = Formater.money((_buyMoney * Transverter.toNumber(_rate * _discount * 0.1) * 0.01) / (1 + Transverter.toNumber(_rate * _discount * 0.1) * 0.01));
	//省手续费
	var ssxf = Formater.money(Transverter.toNumber(ysxf) - Transverter.toNumber(xsxf));
	var buyHandlingCharges = new JsonMap();
	buyHandlingCharges.put("ysxf", ysxf);
	buyHandlingCharges.put("xsxf", xsxf);
	buyHandlingCharges.put("ssxf", ssxf);
Printer.info("手续费集:" + buyHandlingCharges.toString());
	return buyHandlingCharges;
}
/**
 * 此方法用于替换void()方法，因为介于不同浏览器，有时JS默认的void();会报错，所以用此方法替换
 */
Freedom.nothing = function () {}
/**
 * 制造副本
 * @param _resObj 源对象
 * @return 副本对象
 */
Freedom.makeDuplicate = function (_resObj) {
	var duplicate = _resObj;
	if (_resObj instanceof JsonMap) {
		duplicate = new JsonMap();
		duplicate.putAll(_resObj);
	} else if (Validater.isJsonObj(_resObj)) {
		var executeStr = "duplicate = {";
		$.each(_resObj, function (_k, _v) {
			if (Validater.isStringObj(_v)) _v = ("\"" + _v + "\"");
			executeStr += ("\"" + _k + "\":" + _v + ", ");
		});
		executeStr = executeStr.subString(0, executeStr.length - 2);
		executeStr += "};";
Printer.info("Freedom.makeDuplicate.JsonObj.executeStr : " + executeStr);
		eval(executeStr);
	} else if (_resObj instanceof ArrayList) {
		duplicate = new ArrayList();
		duplicate.addAll(_resObj);
	} else if (Validater.isArrayObj(_resObj)) {
		var executeStr = "duplicate = [";
		$.each(_resObj, function (_i, _v) {
			executeStr += (_v + ", ");
		});
		executeStr = executeStr.subString(0, executeStr.length - 2);
		executeStr += "];";
Printer.info("Freedom.makeDuplicate.ArrayObj.executeStr : " + executeStr);
		eval(executeStr);
	}
	return duplicate;
}
/**
 * 正在提交控制
 * @param _tarJQueryObj 目标jQuery对象
 * @param _isSubmitting 是否正在提交[true:是, false:否]
 * @param _text 提交文本
 */
Freedom.submittingHandle = function (_tarJQueryObj, _isSubmitting, _text) {
	var span_submitText = $("#Freedom_submittingHandle_span_submitText");
	if (_isSubmitting) {
		if (!Validater.isUndefined(span_submitText)) span_submitText.show();
		else _tarJQueryObj.after("<span id='Freedom_submittingHandle_span_submitText' class='loading_button'>" + _text + "</span>");
		_tarJQueryObj.removeClass("jijin_btn2").addClass("jijin_btn2_gray");
	} else {
		if (!Validater.isUndefined(span_submitText)) span_submitText.hide();
		_tarJQueryObj.removeClass("jijin_btn2_gray").addClass("jijin_btn2");
	}
}
/**
 * 生成一个随机整数
 * @param _places 位数
 * @return JS.Number
 */
Freedom.randomInt = function (_places) {
	var beiShu = 1;
	for (var index = 1; index <= _places; index ++) {
		beiShu *= 10;
	}
	var randomNumber = Math.random();
	var result = (randomNumber *  beiShu).getIntVal();
	var chaJiWei = _places - Transverter.toStr(result).length;
	beiShu = 1;
	for (var index = 1; index <= chaJiWei; index ++) {
		beiShu *= 10;
	}
	return result * beiShu;
}
/**
 * 重写JS.String.trim()
 * @return JS.String
 */
/*String.prototype.trim = function () {
	return $.trim(this);
}*/
/**
 * 给JS的String类添加subString方法
 * @param _startIndex 开始下标(包含)
 * @param _endIndex 结束下标(不包含)
 * @return JS.String
 */
String.prototype.subString = function (_startIndex, _endIndex) {
	return this.substring(_startIndex, _endIndex);
}
/**
 * 给JS的String类添加获取逆序字符串方法
 * @return JS.String
 */
String.prototype.getReversedStr = function () {
	var val = "";
	for (var index = this.length - 1; index >= 0; index--) {
		val += this.charAt(index);
	}
	return val;
}
/**
 * 给JS的String类添加startsWith方法
 * @return JS.Boolean
 */
String.prototype.startsWith = function () {}
/**
 * 给JS的Number类添加获取整数值方法
 * @return JS.Number
 */
Number.prototype.getIntVal = function () {
	var thisStr = this + "";
	var decimalIndex = thisStr.indexOf(".");
	if (decimalIndex >= 0)
		return new Number(thisStr.substring(0, decimalIndex));
	else return this;
}
/**
 *  给JS的Number类添加获取小数值方法
 * @return JS.Number - 0 or 0.*****
 */
Number.prototype.getDecimalVal = function () {
	var thisStr = this + "";
	var decimalIndex = thisStr.indexOf(".");
	if (decimalIndex >= 0)
		return (Transverter.toNumber("0" + thisStr.substring(decimalIndex)) - 1 + 1);
	else return 0;
}
/**
 * 给JS的String类添加endsWith方法
 * @return JS.JS.Boolean
 */
String.prototype.endsWith = function () {}
//添加jQuery插件
Freedom.addJQueryPlug({
	/**
	 * 暗示文本
	 * @param _text 暗示文本
	 * @param _mode 模式["clear":获焦会消除暗示文本, "noClear":获焦不会消除暗示文本(默认)]
	 * 补充说明：clear模式下密码输入框不支持IE9以下版本
	 */
	hint : function (_text, _mode) {
		Freedom.hint(this, _text, _mode);
	},
	/**
	 * 是否显示的
	 * @return JS.Boolean
	 */
	isShowed : function () {
		return this.css("display") != "none";
	},
	/**
	 * 向上滑动隐藏
	 * @param _animateTime 动画时间(单位：毫秒)
	 * @param _backFn 回调函数
	 */
	upSlide : function (_animateTime, _backFn) {
		new AnimateHelper(this).upSlide(_animateTime, _backFn);
	},
	/**
	 * 向下滑动显示
	 * @param _animateTime 动画时间(单位：毫秒)
	 * @param _backFn 回调函数
	 */
	downSlide : function (_animateTime, _backFn) {
		new AnimateHelper(this).downSlide(_animateTime, _backFn);
	},
	/**
	 * 向左滑动隐藏
	 * @param _animateTime 动画时间(单位：毫秒)
	 * @param _backFn 回调函数
	 */
	leftSlide : function (_animateTime, _backFn) {
		new AnimateHelper(this).leftSlide(_animateTime, _backFn);
	},
	/**
	 * 向右滑动显示
	 * @param _animateTime 动画时间(单位：毫秒)
	 * @param _backFn 回调函数
	 */
	rightSlide : function (_animateTime, _backFn) {
		new AnimateHelper(this).rightSlide(_animateTime, _backFn);
	},
	/**
	 * 闪烁
	 * @param _options 可选项 {
	 * 		width : 宽度(默认1px),
	 * 		style : 样式(默认dashed),
	 * 		color : 颜色(默认red),
	 * 		times : 次数(默认3次)
	 * }
	 * 补充说明：不支持IE8及其以下浏览器版本
	 */
	flicker : function (_options) {
		new AnimateHelper(this).flicker(_options);
	},
	/**
	 * 载入网页
	 * @param _url 请求URL
	 * @param _options 可选项集(JSON) {
	 * 		isOpenUnload : 是否开启停止加载(默认true),
	 *		isAppend : 是否追加(默认false),
	 *		reqData : 请求数据(默认null),
	 *		reqType : 请求类型(默认post),
	 *		isAsync : 是否异步(默认true),
	 *		isCache : 是否缓存(默认false),
	 *		backFn : 回调函数(默认function () {}),
	 *		backFnParams : 回调函数参数列表(多个参数推荐使用JSON),
	 *		manualBackFn : 手动回调函数(默认function() {}),
	 *		manualBackFnParams : 手动回调函数参数列表(多个参数推荐使用JSON)
	 * }
	 */
	loadWebpage : function (_url, _options) {
		WebpageLoader.load(_url, this, _options);
	},
	/**
	 * 禁用
	 */
	disable : function () {
		this.attr("disabled", "disabled");
	},
	/**
	 * 取消禁用
	 */
	undisable : function () {
		this.removeAttr("disabled");
	},
	/**
	 * 选中
	 */
	checked : function () {
		for (var index = 0; index < this.length; index ++) {
			this[index].checked = true;
			this[index].checked = "checked";
		}
	},
	/**
	 * 取消选中
	 */
	unchecked : function () {
		for (var index = 0; index < this.length; index ++) {
			this[index].checked = false;
			this[index].checked = "";
		}
	},
	/**
	 * 跟随鼠标弹出
	 * @param _popJQueryObj 弹出jQuery对象
	 * @param _popEvent 弹出事件，支持click、mouseover(默认)
	 * 补充说明：
	 * 1.IE8及其以下兼容性不是很理想
	 * 2.同一个目标jQuery对象仅支持一个弹出jQuery对象，之后绑定的弹出jQuery对象将覆盖之前的
	 */
	popIFrame4mouse : function (_popJQueryObj, _popEvent) {
		IFrame.pop4mouse(this, _popJQueryObj, _popEvent);
	},
	/**
	 * 正在提交处理
	 * @param _text 提交文本
	 */
	submittingHandle : function (_text) {
		_text = (Validater.isUndefined(_text) ? "正在提交，请稍候..." : _text);
		Freedom.submittingHandle(this, true, _text);
	},
	/**
	 * 禁止正在提交处理
	 */
	unSubmittingHandle : function () {
		Freedom.submittingHandle(this, false);
	},
	/**
	 * 获取select当前选中项的展示文本
	 * @param _jQueryObj jQuery对象
	 * @return JS.String or undefined
	 */
	getSelectedText : function () {
		return HtmlLabelHelper.getSelectedTextOrValue(this, "text");
	},
	/**
	 * 获取select当前选中项的展示文本
	 * @param _jQueryObj jQuery对象
	 * @return JS.String or undefined
	 */
	getSelectedValue : function () {
		return HtmlLabelHelper.getSelectedTextOrValue(this, "text");
	},
	/**
	 * 获取radio group当前选中项的值
	 * @param _jQueryObj jQuery对象
	 * @return JS.String or undefined
	 */
	getRadioGroupCheckedVal : function () {
		return HtmlLabelHelper.getRadioGroupCheckedVal(this);
	},
	/**
	 * 回车keyup事件
	 * @param _handleFn 处理函数
	 * 补充说明：
	 * 1.同一jQuery对象重复绑定会自动清除上次绑定的事件
	 * 2.若_handleFn有参数，可以把参数追加至_handleFn参数后，第二个及其以后的参数都将视为_handleFn的参数依次传入
	 */
	enterKeyUp : function (_handleFn) {
		if (arguments.length <= 1) KeyUpEventHelper.enter(this, _handleFn);
		else {
			var preExecuteCodeStr = "KeyUpEventHelper.enter(this, _handleFn, ";
			for (var index = 1; index < arguments.length; index ++) {
				preExecuteCodeStr += ("arguments[" + index + "], ");
			}
			preExecuteCodeStr = preExecuteCodeStr.substring(0, preExecuteCodeStr.length - 2);
			preExecuteCodeStr += ");";
Printer.info("preExecuteCodeStr : " + preExecuteCodeStr);
			eval(preExecuteCodeStr);
		}
	},
	/**
	 * 向下滚动固定
	 * @param _startFiexdScrollPx 滚动多少像素开始固定
	 * @param _marginTopPx4body 距body顶部多少像素
	 */
	downScrollFixed : function (_startFiexdScrollPx, _marginTopPx4body) {
		IFrame.downScrollFixed(this, _startFiexdScrollPx, _marginTopPx4body);
	}
});