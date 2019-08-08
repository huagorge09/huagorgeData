﻿//判断是否是NetScape浏览器,true:是
ns4 = (document.layers)? true:false

//判断是否是IE,true:是
ie4 = (document.all)? true:false

function init()
{
    if (ns4) block = document.blockDiv
    if (ie4) block = blockDiv.style
    /*这里定义了一个函数，初始化对象block，在NS中，对CSS对象的表示方法是：
    document.blockdiv.propertyname,这里blockdiv是您可以任意定义的名称,
    在IE中， 表示方法是：blockdiv.style.propertyname。上面的代码是针对两
     种浏览器用不同的格式定义对象block,从而确保了在两种浏览器下都能正常显示
    */
}

/**
 * 日期格式转换
 * @param date
 * @returns
 */
function formatDate1(date){
	if(date==null || date==''){
		return "";
	}
	date = unformat1(date);
	return parseInt(date.substr(0,4),10)+"年"+parseInt(date.substr(4,2),10)+"月"+parseInt(date.substr(6,2),10)+"日";
}


// 取通过URL传过来的参数 (格式如 ?Param1=Value1&Param2=Value2)
function getUrlParams()
{
    var urlParams = new Object() ;
    var aParams = document.location.search.substr(1).split('&') ;
    for (i = 0; i < aParams.length; i++)
    {
        var aParam = aParams[i].split('=') ;
        urlParams[aParam[0]] = aParam[1];
    }
    return urlParams;
}

function paresUrlParams(url)
{
    var urlParams = new Object() ;
    var aParams = url.substr(url.indexOf('?')+1).split('&') ;
    for (i = 0; i < aParams.length; i++)
    {
        var aParam = aParams[i].split('=') ;
        urlParams[aParam[0]] = aParam[1];
    }
    return urlParams;
}



/**
  为 Array 类增加一个 max 方法
**/
Array.prototype.max = function()
{
    var i, max = this[0];

    for (i = 1; i < this.length; i++)
    {
        if (max < this[i])
            max = this[i];
    }

    return max;
}

/**
 为字符串增加trim方法
**/
String.prototype.trim = function()
{
    // 用正则表达式将前后空格用空字符串替代。
    return this.replace(/(^\s*)|(\s*$)/g, "");
}

/**
   执行正则表达式
**/
function executeExp(re, s)
{
    return re.test(s);
}

/**
  判断是否是字母、数字或者为空
**/
function isAlphaNumeric(strValue)
{
    // 只能是 A-Z a-z 0-9 之间的字母数字 或者为空
    return executeExp(/^\w*$/gi, strValue);
}

/**
  判断是否是正确的日期，格式为2003-12-12
**/
function isDate(strValue)
{
    if (isEmpty(strValue)) return true;

    if (!executeExp(/^\d{4}-[01]?\d-[0-3]?\d$/g, strValue)) return false;

    var arr = strValue.split("-");
    var year = arr[0];
    var month = arr[1];
    var day = arr[2];

    // 1 <= 月份 <= 12，1 <= 日期 <= 31
    if (!( ( 1 <= month ) && ( 12 >= month ) && ( 31 >= day ) && ( 1 <= day ) ))
        return false;

    // 润年检查
    if (!( ( year % 4 ) == 0 ) && ( month == 2) && ( day == 29 ))
        return false;

    // 7月以前的双月每月不超过30天
    if (( month <= 7 ) && ( ( month % 2 ) == 0 ) && ( day >= 31 ))
        return false;

    // 8月以后的单月每月不超过30天
    if (( month >= 8) && ( ( month % 2 ) == 1) && ( day >= 31 ))
        return false;

    // 2月最多29天
    if (( month == 2) && ( day >= 30 ))
        return false;

    return true;
}

/**
  判断是否是正确的Email
**/
function isEmail(strValue)
{
    if (isEmpty(strValue)) return true;

    var pattern = /^([a-zA-Z0-9_-])+[\.a-zA-Z0-9]+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
    return executeExp(pattern, strValue);

}

/**
  判断字符串是否为空
**/
function isEmpty(strValue)
{
    if (strValue == null || strValue == "")
        return true;
    else
        return false;
}


/**
  判断是否为数字
**/
function isNumeric(strValue)
{
    return executeExp(/^\d*$/g, strValue);
}


/**
  判断是否为浮点数（不带正负号）
**/
function isNumberFloat(strValue)
{
    if (isEmpty(strValue)) return true;
    return executeExp(/^\d+(\.\d+)?$/, strValue);
    //return (!isNaN(parseFloat(strValue))) ? true : false;
}


/**
  判断是否是货币
**/
function isMoney(strValue)
{
    if (isEmpty(strValue)) return true;
    return executeExp(/^[+-]?\d+(,\d{3})*(\.\d+)?$/g, strValue);
}


/**
  判断是否为手机号码
**/
function  isMobile(phone) {
    return /^0?(13[0-9]|15[0123456789]|18[0123456789]|14[0123456789]|17[0123456789])[0-9]{8}$/.test(phone);
}

/**
  判断是否为电话
**/
function isPhone(strValue)
{
    if (isEmpty(strValue)) return true;
    return executeExp(/(^\(\d{3,5}\)\d{6,8}(-\d{2,8})?$)|(^\d+-\d+$)|(^(130|131|132|133|134|135|136|137|138|139|150|151|152|153|154|155|156|157|158|159)\d{8}$)/g, strValue);
}

/**
  判断是否为邮政编码
**/
function isPostalCode(strValue)
{    if (isEmpty(strValue)) return true;
    return executeExp(/(^$)|(^\d{6}$)/gi, strValue)
}

/**
  判断是否为合法的URL
**/
function isURL(strValue)
{
    if (isEmpty(strValue)) return true;
    var pattern = /^(http|https|ftp):\/\/(\w+\.)+[a-z]{2,3}(\/\w+)*(\/\w+\.\w+)*(\?\w+=\w*(&\w+=\w*)*)*/gi;
    return executeExp(pattern, strValue);
}


//比较，compare(1,'<=10')将返回true，第一个参数为要比较的数字，第二个参数支持>、=、<、<=、>=
function compare(l, strParam)
{
    var ml;
    // 要判断的长度
    var co;
    // 比较符

    // 判断是否为<=、>=
    if (strParam.indexOf('<=') != -1 || strParam.indexOf('>=') != -1)
    {
        ml = parseInt(strParam.substr(2));
        cp = strParam.substr(0, 1);
    }
    else
    {
        ml = parseInt(strParam.substr(1));
        cp = strParam.charAt(0);
    }

    switch (cp)
            {
        case '<' :
            if (l >= ml) return false;
            break;
        case '=' :
            if (l != ml) return false;
            break;
        case '>' :
            if (l <= ml) return false;
            break;
        case '<=' :
            if (l > ml) return false;
            break;
        case '>=' :
            if (l < ml) return false;
            break;
        default :
            return false
    }

    return true;
}

//验证客户姓名是否带有特殊字符
function isValidateUserName(str){
  			var pattern = /^[\u4e00-\u9fa5-a-zA-Z-0-9]+$/;
  			return pattern.test(str);
}

//检查字符的长度
function checkStrLength(strValue, strParam)
{
    //if( isEmpty( strValue ) )	return true; // 此处注释掉，空字符串同样要检查

    // 参数形如：L<10, L=5, L>117, L<=10, L>=10
    if (strParam.charAt(0) != 'L')    return false;

    return compare(strValue.length, strParam.substr(1));
}


/**
  添加getBytesLength方法，用于得到字节数。中文为2个字节
**/
String.prototype.getBytesLength = function()
{
    var cArr = this.match(/[^\x00-\xff]/ig);
    return this.length + (cArr == null ? 0 : cArr.length);
}

//检查字符的长度，使用字节数来检测，即1个中文当作2个字节
function checkStrLengthOfBytes(strValue, strParam)
{
    //if( isEmpty( strValue ) )	return true; // 此处注释掉，空字符串同样要检查

    // 参数形如：L<10, L=5, L>117
    if (strParam.charAt(0) != 'L')    return false;

    return compare(strValue.getBytesLength(), strParam.substr(1));
}

/**
 * 检查文件扩展名
 * @param fileName 文件名
 * @param allowedName 允许的扩展名，以|分开，如"jpg|gif"代表允许.jpg和.gif文件。"*"和""（空字符串）代表不允许所有。如果该文件无扩展名，返回true
 */
function checkFileExtendName(fileName, allowedName)
{
    if (allowedName == null || allowedName == "" || allowedName == "*" ||
        fileName == null || fileName == "" || fileName.indexOf(".") == -1)
    {
        return true;
    }

    var realFileName = "";
    if (fileName.indexOf("\\") != -1)
    { // 如果包含路径名
        realFileName = fileName.substr(fileName.lastIndexOf("\\") + 1, fileName.length);
    }
    else if (fileName.indexOf("/") != -1)
    { // 如果包含路径名（for Unix）
        realFileName = fileName.substr(fileName.lastIndexOf("/") + 1, fileName.length);
    }
    else
    { // 无路径名
        realFileName = fileName;
    }

    if (realFileName.indexOf(".") == -1)
    {
        return true;
    }

    var extendName = realFileName.substr(realFileName.lastIndexOf(".") + 1, realFileName.length);

    var extendNames = allowedName.split("|");
    //alert("文件名：" + realFileName + " 扩展名：" + extendName + " " + extendNames);

    for (var i = 0; i < extendNames.length; i++)
    {
        if (extendName.toLowerCase() == extendNames[i].toLowerCase())
        {
            return true;
        }
    }

    return false;
}

/**
  检查字符的长度，使用字节数来检测，即1个中文当作2个字节
**/
function validStrLengthOfBytes(objName, strDescription, strParam)
{
    var strMsg = "";
    if (!isElementExist(objName))
    {
        strMsg = strDescription + " 对象不存在";
        window.alert(strMsg);
        return;
    }
    var strValue = getElement(objName).value.trim();

    if (!checkStrLengthOfBytes(strValue, strParam))
    {
        strMsg = '"' + strDescription + '" 长度不正确，必需为' + strParam + '（注意1个中文为2个字节长度）\n';
    }

    return strMsg;
}


/**
  判断各种类型的入口函数
**/
function checkValid(objName, strDescription, strType)
{
    var strMsg = "";
    if (isElementExist(objName))
    {
        var strValue = getElement(objName).value.trim();
        switch (strType)
                {
            case "Date" :    // 日期
                if (!isDate(strValue))
                    strMsg = '【' + strDescription + '】 格式错误，请输入正确的日期格式，如1970-1-1\n';
                break;

            case "AlphaNumeric" :    // 字母数字
                if (!isAlphaNumeric(strValue))
                    strMsg = '【' + strDescription + '】 格式错误，请输入字母或数字！\n';
                break;

            case "NotEmpty" :    // 不许空值
                if (isEmpty(strValue))
                    strMsg = '【' + strDescription + '】 不能为空！\n';
                break;

            case "Email" :    // 电子邮件
                if (!isEmail(strValue))
                    strMsg = '【' + strDescription + '】 格式错误，请输入正确的邮件格式\n';
                break;

            case "Money" :    //货币
                if (!isMoney(strValue))
                    strMsg = '【' + strDescription + '】 格式错误，请输入正确的货币格式\n';
                break;

            case "Numeric" :    //数字
                if (!isNumeric(strValue))
                    strMsg = '【' + strDescription + '】 格式错误，请输入数字！\n';
                break;

            case "NumberFloat" :    //浮点数
                if (!isNumberFloat(strValue))
                    strMsg = '【' + strDescription + '】 格式错误，请输入浮点数！\n';
                break;

            case "Mobile" :    // 手机号码
                if (!isMobile(strValue))
                    strMsg = '【' + strDescription + '】 格式错误，请输入正确的手机号码\n';
                break;

            case "Phone" :    // 电话
                if (!isPhone(strValue))
                    strMsg = '【' + strDescription + '】 格式错误，请输入正确的电话格式\n';
                break;

            case "PostalCode" :    // 邮政编码
                if (!isPostalCode(strValue))
                    strMsg = '【' + strDescription + '】 格式错误，请输入6位数字！\n';
                break;

            case "URL" :    // URL
                if (!isURL(strValue))
                    strMsg = '【' + strDescription + '】 格式错误，请输入正确的URL格式！\n';
                break;

            default :    // 其他
                strMsg = '错误的 【' + strDescription + '】 类型 "' + strType + '" 不能识别！\n';
                break;
        }
    }
    else
    {
        strMsg = '【' + strDescription + "】 对象不存在\n";
    }

    return strMsg;
}


/**
  链接转向
**/
function goToURL(url)
{
    window.location = url;
}

//判断用户是否选择了要操作的记录，如果是
//则自动提交表单
//form:表单名称
//action:要进行的操作
//listName:多选field字称
function listCheck(formName, action, listName, msg)
{
    var flag = 0;
    var form = document.all(formName);

    if (form == null)
        return false;

    if (action == '')
        return false;

    if (listName == '')
        return false;

    var field = document.all(listName);

    if (field.length == null)    //处理可能只有一条记录的Bug
    {
        if (field.checked == true)
        {
            flag = 1;
        }
    }
    else
    {
        for (i = 0; i < field.length; i++)
        {
            if (field[i].disabled != true)
            {
                if (field[i].checked == true)
                {
                    flag = 1;
                }
            }
        }
    }

    if (flag == 1)
    {
        document.all("function").value = action;
        if (window.confirm("你确定要[" + msg + "]吗？"))
        {
            form.submit();
            return true;
        }
        else
        {
            return false;
        }
    }
    else
    {
        alert("请选择记录！");
        return false;
    }

    return false;
}

/**
  弹出确认窗口
**/
function _confirm(msg)
{
    return window.confirm("你确定要" + msg + "吗？");
}

/**
  根据指定的名称，判断元素是否存在
**/
function isElementExist(name)
{
    var objArray = document.getElementsByName(name);
    if (objArray != null && objArray.length > 0)
    {
        return true;
    }
    return false;
}

/**
  根据指定的名称，返回特定的元素，若不存在，则返回null
**/
function getElement(name)
{
    var objArray = document.getElementsByName(name);
    if (objArray != null && objArray.length > 0)
    {
        return objArray[0];
    }
    return null;
}

/**
  根据指定的名称，返回特定的元素数组
**/
function getElements(name)
{
    var objArray = document.getElementsByName(name);
    return objArray;
}

/**
  根据指定的名称，判断特定的元素是否是数组
**/
function elementIsArray(name)
{
    var objArray = document.getElementsByName(name);
    if (objArray != null && objArray.length > 1)
    {
        return true;
    }
    return false;
}

/**
  字段是否为空
**/
function checkEmpty(field)
{
    if (isElementExist(field))
    {
        if (getElement(field).value.trim() == "")
            return true;
    }
    return false;
}

/**
  根据ID得到对应的对象
**/  
function getObject(id)
{
    return document.getElementById(id);
}
//屏蔽右键
oncontextmenu = "window.event.returnValue=false";

/**
  刷新当前窗口
**/
function refresh()
{
    document.location.reload();
}

/**
  打开新窗口
**/
function openWin(url, width, height)
{
    if (url == '')
        return;
    var winOption = "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=yes,left=50,top=50,width=" + width + ",height=" + height;
    window.open(url, '', winOption);
    return;
}

//根据select的名称和值，设置某一项选中
function setSelectSelected(selectName, value)
{
    if (!isElementExist(selectName))
        return;

    var selectObj = getElement(selectName);
    if (selectObj.tagName == "SELECT")
    {
        for (var i = 0; i < selectObj.options.length; i++)
        {
            var option = selectObj.options[i];
            if (option.value == value)
            {
                option.selected = true;
            }
        }
    }
}

/**
  打开新窗口,带滚动条
**/
function openWinWithScroll(url, width, height)
{
    if (url == '')
        return;
    var winOption = "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,left=50,top=50,width=" + width + ",height=" + height;
    window.open(url, '', winOption);
    return;
}

/**
 以最大化方式打开新窗口
**/
function openMaxWindow(url)
{
    if (url == '')
        return;
    var winOption = "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=yes,fullscreen=0";
    var win = window.open(url, '', winOption);
    win.moveTo(0, 0)
    win.resizeTo(screen.availWidth, screen.availHeight)
    return win;
}

/**
  以最大化方式打开新窗口，并且带滚动条
**/
function openMaxWindowWithScroll(url)
{
    if (url == '')
        return;
    var winOption = "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";
    var win = window.open(url, '', winOption);
    win.moveTo(0, 0)
    win.resizeTo(screen.availWidth, screen.availHeight)
    return win;
}

/**
  以最大化方式打开新窗口，并且带滚动条，还可以命名
**/
function openMaxNamedWindowWithScroll(url, windowName)
{
    if (url == '')
        return;
    var winOption = "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,fullscreen=0";
    var win = window.open(url, windowName, winOption);
    win.moveTo(0, 0)
    win.resizeTo(screen.availWidth, screen.availHeight)
    return win;
}

/**
  打开对话框
**/
function openDialog(url, width, height)
{
    return window.showModalDialog(url, window, 'dialogHeight: ' + height + 'px; dialogWidth: ' + width + 'px;edge: Raised; center: Yes; help: Yes;scroll:no; resizable: no; status: no;');
}

/**
  打开对话框，并且带滚动条
**/
function openDialogWithScroll(url, width, height)
{
    return window.showModalDialog(url, window, 'dialogHeight: ' + height + 'px; dialogWidth: ' + width + 'px;edge: Raised; center: Yes; help: Yes;scroll:auto; resizable: no; status: no;');
}

function openUploadFileDialog(fieldObj)
{
    if(fieldObj)
    {
        if(fieldObj.tagName == "INPUT" && fieldObj.type=="text")
        {
          return window.showModalDialog("upload/upload.jsp",fieldObj,'dialogHeight: 80px; dialogWidth: 350px;edge: Raised; center: Yes; help: no;scroll:no; resizable: no; status: no;')
        }
    }
    alert("接收上传文件路径的对象不是一个输入域");
}

function openDeleteFileDialog(fieldObj)
{
    if(fieldObj)
    {
        if(fieldObj.value.length == 0)
        {
            alert("您还没有上传文件");
            return;
        }
        var url = "upload/delete.jsp?filePath=" + fieldObj.value;
        return window.showModalDialog(url,fieldObj,'dialogHeight: 80px; dialogWidth: 350px;edge: Raised; center: Yes; help: no;scroll:no; resizable: no; status: no;')
    }
}

//键盘触发器
function isKeyTrigger(e,keyCode){
    var argv = isKeyTrigger.arguments;
    var argc = isKeyTrigger.arguments.length;
    var bCtrl = false;
    if(argc > 2){
        // 如果存在第3个传入参数,则作为是否判断按下ctrl键标志
        bCtrl = argv[2];
    }
    var bAlt = false;
    if(argc > 3){
        // 如果存在第4个传入参数,则作为是否判断按下ctrl键标志
        bAlt = argv[3];
    }

    var nav4 = window.Event ? true : false;

    if(typeof e == 'undefined') {
        e = event;
    }

    if( bCtrl &&
        !((typeof e.ctrlKey != 'undefined') ?
            e.ctrlKey : e.modifiers & Event.CONTROL_MASK > 0)){
        return false;
    }
    if( bAlt &&
        !((typeof e.altKey != 'undefined') ?
            e.altKey : e.modifiers & Event.ALT_MASK > 0)){
        return false;
    }
    var whichCode = 0;
    if (nav4) whichCode = e.which;
    else if (e.type == "keypress" || e.type == "keydown")
        whichCode = e.keyCode;
    else whichCode = e.button;

    return (whichCode == keyCode);
}

/**
*	描述:对用户输入的身份证号进行验证，判断如果返回结果不为"验证通过!"，
*	表示用户身份证输入错误！并将返回的错误信息打印出来;
*/
function checkIdcard(idcard)
{
	var Errors=new Array(
	"验证通过!",
	"身份证号码位数不对!",
	"身份证号码不存在!",
	"身份证号码校验错误!",
	"身份证地区非法!"
	);

	var idcard,Y,JYM;
	var S,M;
	var idcard_array = new Array();
	idcard_array = idcard.split("");
	if(idcard == "111111111111111" || idcard == "111111111111111111")
	{
		return Errors[2];
	}
	//地区检验
	if(area(parseInt(idcard.substr(0,2)))==null) return Errors[4];
	//身份号码位数及格式检验
	switch(idcard.length){
	case 15:
		if ( (parseInt(idcard.substr(6,2))+1900) % 4 == 0 || ((parseInt(idcard.substr(6,2))+1900) % 100 == 0 && (parseInt(idcard.substr(6,2))+1900) % 4 == 0 ))
		{
			ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//测试出生日期的合法性
		} 
		else 
		{
			ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//测试出生日期的合法性
		}
		if(ereg.test(idcard))
			return Errors[0];
		else 
			return Errors[2];
	break;
	case 18:
	//18位身份号码检测
	//出生日期的合法性检查 
	//闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
	//平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
	if ( parseInt(idcard.substr(6,4)) % 4 == 0 || (parseInt(idcard.substr(6,4)) % 100 == 0 && parseInt(idcard.substr(6,4))%4 == 0 )){
		ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
	} 
	else 
	{
		ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
	}
	if(ereg.test(idcard))
	{
		//测试出生日期的合法性
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
		if(M == idcard_array[17]) 
			return Errors[0];//检测ID的校验位
		else 
			return Errors[3];
	}
	else return Errors[2];
	break;
	default:
	return Errors[1];
	break;
	}
}

function area(num)
{
	switch(num)
	{
		case 11 : return "北京市"; break;
		case 12 : return "天津市"; break;
		case 13 : return "河北省"; break;
		case 14 : return "山西省"; break;
		case 15 : return "内蒙古自治区"; break;
		case 21 : return "辽宁省"; break;
		case 22 : return "吉林省"; break;
		case 23 : return "黑龙江省"; break;
		case 31 : return "上海市"; break;
		case 32 : return "江苏省"; break;
		case 33 : return "浙江省"; break;
		case 34 : return "安徽省"; break;
		case 35 : return "福建省"; break;
		case 36 : return "江西省"; break;
		case 37 : return "山东省"; break;
		case 41 : return "河南省"; break;
		case 42 : return "湖北省"; break;
		case 43 : return "湖南省"; break;
		case 44 : return "广东省"; break;
		case 45 : return "广西省"; break;
		case 46 : return "海南省"; break;
		case 50 : return "重庆市"; break;
		case 51 : return "四川省"; break;
		case 52 : return "贵州省"; break;
		case 53 : return "云南省"; break;
		case 54 : return "西藏自治区"; break;
		case 61 : return "陕西省"; break;
		case 62 : return "甘肃省"; break;
		case 63 : return "青海省"; break;
		case 64 : return "宁夏回族自治区"; break;
		case 65 : return "新疆维吾尔自治区"; break;
		case 71 : return "台湾特别行政区"; break;
		case 81 : return "香港特别行政区"; break;
		case 82 : return "澳门特别行政区"; break;
		case 91 : return "国外"; break;
		default : return "";  break;
	}
}
//电话号码验证
function CheckPhone(phone)
{
	//验证电话号码手机号码，包含153，159号段
	if (phone=="")
	{
		return false;
	}
	
	if (phone != "")
	{
		if(phone.length < 6 || phone.length >8) return false;
		var numberValidate = /^\d*$/g;
		if(!numberValidate.test(phone))return false;
		var p1 = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
		var me = false;
		if (p1.test(phone))
			me=true;
		if (!me)
		{
			phone='';
			return false;
		}
	}
	return true;
}

/**
  根据xml文档对象得到指定tag的文本值
*/
function getXmlTagText(xmlobj,tagName)
{  
    if(document.all)
    {
      if(xmlobj.getElementsByTagName(tagName)[0])
        return xmlobj.getElementsByTagName(tagName)[0].childNodes[0].text;
      else
        return "";
    }
    else
    {
      if(xmlobj.getElementsByTagName(tagName)[0])
        return xmlobj.getElementsByTagName(tagName)[0].childNodes[0].nodeValue;
      else
        return "";
    }
}

function checkArea(num)
{
	var numberValidate = /^\d*$/g;
	if(!numberValidate.test(num))return "区号必须为数字";
	var value = isArea(num);
	if(value == null)
		return "区号无效或不存在";
	else
		return "验证通过";
}

function isArea(num)
{
	switch(num)
	{
		case "010" : return "北京市"; break;
		case "020" : return "广州市"; break;
		case "021" : return "上海市"; break;
		case "022" : return "天津市"; break;
		case "023" : return "重庆市"; break;
		case "0660" : return "汕尾市"; break;
		case "0661" : return "潮阳市"; break;
		case "0662" : return "阳江市"; break;
		case "0663" : return "揭阳市"; break;
		case "0668" : return "茂名市"; break;
		case "0750" : return "江门市"; break;
		case "0751" : return "韶关市"; break;
		case "0752" : return "惠州市"; break;
		case "0753" : return "梅州市"; break;
		case "0754" : return "汕头市"; break;
		case "0755" : return "深圳市"; break;
		case "0756" : return "珠海市"; break;
		case "0757" : return "佛山市"; break;
		case "0758" : return "肇庆市"; break;
		case "0759" : return "湛江市"; break;
		case "0760" : return "中山市"; break;
		case "0762" : return "河源市"; break;
		case "0763" : return "清远市"; break;
		case "0765" : return "顺德市"; break;
		case "0766" : return "云浮市"; break;
		case "0768" : return "潮州市"; break;
		case "0769" : return "东莞市"; break;
		case "0570" : return "衢州市"; break;
		case "0571" : return "杭州市"; break;
		case "0572" : return "湖州市"; break;
		case "0573" : return "嘉兴市"; break;
		case "0574" : return "宁波市"; break;
		case "0575" : return "绍兴市"; break;
		case "0576" : return "台州市"; break;
		case "0577" : return "温州市"; break;
		case "0578" : return "丽水市"; break;
		case "0579" : return "金华市"; break;
		case "0580" : return "舟山市"; break;
		case "025" : return "南京市"; break;
		case "0510" : return "无锡市"; break;
		case "0511" : return "镇江市"; break;
		case "0512" : return "苏州市"; break;
		case "0513" : return "南通市"; break;
		case "0514" : return "扬州市"; break;
		case "0515" : return "盐城市"; break;
		case "0516" : return "徐州市"; break;
		case "0517" : return "淮安市"; break;
		case "0518" : return "连云港市"; break;
		case "0519" : return "常州市"; break;
		case "0523" : return "泰州市"; break;
		case "0527" : return "宿迁市"; break;
		case "0898" : return "海口市"; break;
		case "0591" : return "福州市"; break;
		case "0592" : return "厦门市"; break;
		case "0593" : return "宁德市"; break;
		case "0594" : return "莆田市"; break;
		case "0595" : return "泉州市"; break;
		case "0596" : return "漳州市"; break;
		case "0597" : return "龙岩市"; break;
		case "0598" : return "三明市"; break;
		case "0599" : return "南平市"; break;
		case "028" : return "成都市"; break;
		case "0812" : return "攀枝花市"; break;
		case "0813" : return "自贡市"; break;
		case "0816" : return "绵阳市"; break;
		case "0817" : return "南充市"; break;
		case "0818" : return "达州市"; break;
		case "0825" : return "达州市"; break;
		case "0826" : return "广安市"; break;
		case "0827" : return "巴中市"; break;
		case "0830" : return "泸州市"; break;
		case "0831" : return "宜宾市"; break;
		case "0832" : return "内江市"; break;
		case "0833" : return "眉山市"; break;
		case "0834" : return "凉山自治州"; break;
		case "0835" : return "雅安市"; break;
		case "0836" : return "甘孜自治州"; break;
		case "0837" : return "阿坝自治州"; break;
		case "0838" : return "德阳市"; break;
		case "0839" : return "广元市"; break;
		case "0730": return "岳阳市"; break;
		case "0731" : return "长沙市"; break;
		case "0732" : return "湘潭市"; break;
		case "0733" : return "株洲市"; break;
		case "0734" : return "衡阳市"; break;
		case "0735" : return "郴州市"; break;
		case "0736" : return "常德市"; break;
		case "0737" : return "益阳市"; break;
		case "0738" : return "娄底市"; break;
		case "0739" : return "邵阳市"; break;
		case "0743" : return "湘西自治州"; break;
		case "0744" : return "张家界市"; break;
		case "0745" : return "怀化市"; break;
		case "0746" : return "永州市"; break;
		case "027" : return "武汉市"; break;
		case "0710" : return "襄樊市"; break;
		case "0711" : return "鄂州市"; break;
		case "0712" : return "孝感市"; break;
		case "0713" : return "黄冈市"; break;
		case "0714" : return "黄石市"; break;
		case "0715" : return "咸宁市"; break;
		case "0716" : return "荆州市"; break;
		case "0717" : return "宜昌市"; break;
		case "0718" : return "恩施市"; break;
		case "0719" : return "神农架林区"; break;
		case "0722" : return "随州市"; break;
		case "0724" : return "荆门市"; break;
		case "0728" : return "天门市"; break;
		case "0530" : return "菏泽市"; break;
		case "0531" : return "济南市"; break;
		case "0532" : return "青岛市"; break;
		case "0533" : return "淄博市"; break;
		case "0534" : return "德州市"; break;
		case "0535" : return "烟台市"; break;
		case "0536" : return "潍坊市"; break;
		case "0537" : return "济宁市"; break;
		case "0538" : return "泰安市"; break;
		case "0539" : return "临沂市"; break;
		case "0543" : return "滨州市"; break;
		case "0546" : return "东营市"; break;
		case "0631" : return "威海市"; break;
		case "0632" : return "枣庄市"; break;
		case "0633" : return "日照市"; break;
		case "0634" : return "莱芜市"; break;
		case "0635" : return "聊城市"; break;
		case "0550" : return "滁州市"; break;
		case "0551" : return "合肥市"; break;
		case "0552" : return "蚌埠市"; break;
		case "0553" : return "芜湖市"; break;
		case "0554" : return "淮南市"; break;
		case "0555" : return "马鞍山市"; break;
		case "0556" : return "安庆市"; break;
		case "0557" : return "宿州市"; break;
		case "0558" : return "亳州市"; break;
		case "0558" : return "阜阳市"; break;
		case "0559" : return "黄山市"; break;
		case "0561" : return "淮北市"; break;
        case "0562" : return "铜陵市"; break;
		case "0563" : return "宣城市"; break;
		case "0564" : return "六安市"; break;
		case "0565" : return "巢湖市"; break;
		case "0566" : return "池州市"; break;
		case "0701" : return "鹰潭市"; break;
		case "0790" : return "新余市"; break;
		case "0792" : return "南昌市"; break;
		case "0631" : return "九江市"; break;
		case "0793" : return "上饶市"; break;
		case "0794" : return "抚州市"; break;
		case "0795" : return "宜春市"; break;
		case "0796" : return "吉安市"; break;
		case "0797" : return "赣州市"; break;
		case "0798" : return "景德镇市"; break;
		case "0799" : return "萍乡市"; break;
		case "0770" : return "防城港市"; break;
		case "0771" : return "南宁市"; break;
		case "0772" : return "柳州市"; break;
		case "0773" : return "桂林市"; break;
		case "0774" : return "梧州市"; break;
		case "0775" : return "玉林市"; break;
		case "0776" : return "百色地区"; break;
		case "0777" : return "钦州市"; break;
		case "0778" : return "河池地区"; break;
		case "0779" : return "北海市"; break;
		case "0370" : return "商丘市"; break;
		case "0371" : return "郑州市"; break;
		case "0372" : return "安阳市"; break;
		case "0373" : return "新乡市"; break;
		case "0374" : return "许昌市"; break;
		case "0375" : return "平顶山市"; break;
		case "0376" : return "信阳市"; break;
		case "0377" : return "南阳市"; break;
		case "0378" : return "开封市"; break;
		case "0379" : return "洛阳市"; break;
		case "0391" : return "焦作市"; break;
		case "0392" : return "鹤壁市"; break;
		case "0393" : return "濮阳市"; break;
		case "0394" : return "周口市"; break;
		case "0396" : return "驻马店市"; break;
		case "0397" : return "潢川市"; break;
		case "0398" : return "三门峡市"; break;
		case "0310" : return "邯郸市"; break;
		case "0311" : return "石家庄市"; break;
		case "0312" : return "保定市"; break;
		case "0313" : return "张家口市"; break;
		case "0314" : return "承德市"; break;
		case "0315" : return "唐山市"; break;
		case "0316" : return "廊坊市"; break;
		case "0317" : return "沧州市"; break;
		case "0318" : return "衡水市"; break;
		case "0319" : return "邢台市"; break;
		case "0335" : return "秦皇岛市"; break;
		case "0451" : return "哈尔滨市"; break;
		case "0452" : return "齐齐哈尔市"; break;
		case "0453" : return "牡丹江市"; break;
		case "0454" : return "佳木斯市"; break;
		case "0455" : return "绥化市"; break;
		case "0456" : return "黑河市"; break;
		case "0457" : return "大兴安岭地区"; break;
		case "0458" : return "伊春市"; break;
		case "0459" : return "大庆市"; break;
		case "0464" : return "七台河市"; break;
		case "0467" : return "鸡西市"; break;
		case "0468" : return "鹤岗市"; break;
		case "0469" : return "双鸭山市"; break;
		case "0431" : return "长春市"; break;
		case "0432" : return "吉林市"; break;
		case "0433" : return "延边自治州"; break;
		case "0434" : return "四平市"; break;
		case "0435" : return "通化市"; break;
		case "0436" : return "白城市"; break;
		case "0437" : return "辽源市"; break;
		case "0438" : return "松原市"; break;
		case "0439" : return "白山市"; break;
		case "0440" : return "珲春市"; break;
		case "0448" : return "梅河口市"; break;
		case "0951" : return "银川市"; break;
		case "0952" : return "石嘴山市"; break;
		case "0953" : return "吴忠市"; break;
		case "0954" : return "固原市"; break;
		case "0691" : return "西双版纳自治州"; break;
		case "0692" : return "德宏自治州"; break;
		case "0870" : return "昭通市"; break;
		case "0871" : return "昆明市"; break;
		case "0872" : return "大理自治州"; break;
		case "0873" : return "红河自治州"; break;
		case "0874" : return "曲靖市"; break;
		case "0875" : return "保山市"; break;
		case "0876" : return "文山自治州"; break;
		case "0877" : return "玉溪市"; break;
		case "0878" : return "楚雄自治州"; break;
		case "0879" : return "思茅地区"; break;
		case "0883" : return "临沧地区"; break;
		case "0886" : return "怒江自治州"; break;
		case "0888" : return "丽江地区"; break;
		case "0930" : return "临夏自治州"; break;
		case "0931" : return "兰州市"; break;
		case "0932" : return "定西地区"; break;
		case "0933" : return "平凉地区"; break;
		case "0934" : return "庆阳地区"; break;
		case "0935" : return "武威市"; break;
		case "0935" : return "金昌市"; break;
		case "0936" : return "张掖地区"; break;
		case "0937" : return "酒泉地区"; break;
		case "0938" : return "天水市"; break;
		case "0939" : return "陇南地区"; break;
		case "0941" : return "甘南自治州"; break;
		case "0943" : return "白银市"; break;
		case "024" : return "沈阳市"; break;
		case "0410" : return "铁岭市"; break;
		case "0411" : return "大连市"; break;
		case "0412" : return "鞍山市"; break;
		case "0413" : return "抚顺市"; break;
		case "0414" : return "本溪市"; break;
		case "0415" : return "丹东市"; break;
		case "0416" : return "锦州市"; break;
		case "0417" : return "营口市"; break;
		case "0418" : return "阜新市"; break;
		case "0419" : return "辽阳市"; break;
		case "0421" : return "朝阳市"; break;
		case "0427" : return "盘锦市"; break;
		case "0429" : return "葫芦岛市"; break;
		case "0851" : return "贵阳市"; break;
		case "0852" : return "遵义市"; break;
		case "0853" : return "安顺市"; break;
		case "0854" : return "黔南自治州"; break;
		case "0855" : return "黔东南自治州"; break;
		case "0856" : return "铜仁地区"; break;
		case "0857" : return "毕节地区"; break;
		case "0858" : return "六盘水市"; break;
		case "0859" : return "黔西南自治州"; break;
		case "0349" : return "朔州市"; break;
		case "0350" : return "忻州市"; break;
		case "0351" : return "太原市"; break;
		case "0352" : return "大同市"; break;
		case "0353" : return "阳泉市"; break;
		case "0354" : return "晋中市"; break;
		case "0355" : return "长治市"; break;
		case "0356" : return "晋城市"; break;
		case "0357" : return "临汾市"; break;
		case "0358" : return "吕梁地区"; break;
		case "0359" : return "运城市"; break;
		case "0970" : return "海北自治州"; break;
		case "0971" : return "西宁市"; break;
		case "0972" : return "海东地区"; break;
		case "0973" : return "黄南自治州"; break;
		case "0974" : return "海南自治州"; break;
		case "0975" : return "果洛自治州"; break;
		case "0976" : return "玉树自治州"; break;
		case "0977" : return "海西自治州"; break;
		case "0979" : return "格尔木市"; break;
		case "029" : return "西安市"; break;
		case "0910" : return "咸阳市"; break;
		case "0911" : return "延安市"; break;
		case "0912" : return "榆林市"; break;
		case "0913" : return "渭南市"; break;
		case "0914" : return "商洛市"; break;
		case "0915" : return "安康市"; break;
		case "0916" : return "汉中市"; break;
		case "0917" : return "宝鸡市"; break;
		case "0919" : return "铜川市"; break;
		case "0901" : return "塔城地区"; break;
		case "0902" : return "哈密地区"; break;
		case "0903" : return "和田地区"; break;
		case "0906" : return "阿勒泰地区"; break;
		case "0908" : return "克孜勒苏自治州"; break;
		case "0909" : return "博尔塔拉自治州"; break;
		case "0990" : return "克拉玛依市"; break;
		case "0991" : return "乌鲁木齐市"; break;
		case "0992" : return "奎屯市"; break;
		case "0993" : return "石河子市"; break;
		case "0994" : return "昌吉自治州"; break;
		case "0995" : return "吐鲁番地区"; break;
		case "0996" : return "巴音郭楞自治州"; break;
		case "0997" : return "阿克苏地区"; break;
		case "0998" : return "喀什地区"; break;
		case "0999" : return "伊犁自治州"; break;
		case "0891" : return "拉萨市"; break;
		case "0892" : return "日喀则地区"; break;
		case "0893" : return "山南地区"; break;
		case "0894" : return "林芝地区"; break;
		case "0895" : return "昌都地区"; break;
		case "0896" : return "那曲地区"; break;
		case "0897" : return "阿里地区"; break;
		case "0470" : return "呼伦贝尔市"; break;
		case "0471" : return "呼和浩特市"; break;
		case "0472" : return "包头市"; break;
		case "0472" : return "乌海市"; break;
		case "0474" : return "乌兰察布盟"; break;
		case "0475" : return "通辽市"; break;
		case "0476" : return "赤峰市"; break;
		case "0477" : return "鄂尔多斯市"; break;
		case "0478" : return "巴彦淖尔盟"; break;
		case "0479" : return "锡林郭勒盟"; break;
		case "0482" : return "兴安盟"; break;
		case "0483" : return "阿拉善盟"; break;
		//国际长途区号
		case "86" : return "中国"; break;
		case "93" : return "阿富汗"; break;
		case "355" : return "阿尔巴尼亚"; break;
		case "213" : return "阿尔及利亚"; break;
		case "376" : return "安道尔"; break;
		case "244" : return "安哥拉"; break;
		case "1264" : return "安圭拉岛(英)"; break;
		case "1268" : return "安提瓜和巴布达"; break;
		case "54" : return "阿根廷"; break;
		case "374" : return "亚美尼亚"; break;
		case "297" : return "阿鲁巴岛"; break;
		case "247" : return "阿森松(英)"; break;
		case "61" : return "澳大利亚"; break;
		case "43" : return "奥地利"; break;
		case "994" : return "阿塞拜疆"; break;
		case "1242" : return "巴哈马国"; break;
		case "973" : return "巴林"; break;
		case "880" : return "孟加拉国"; break;
		case "1246" : return "巴巴多斯"; break;
		case "375" : return "白俄罗斯"; break;
		case "32" : return "比利时"; break;
		case "501" : return "伯利兹"; break;
		case "229" : return "贝宁"; break;
		case "1441" : return "百慕大群岛(英)"; break;
		case "975" : return "不丹"; break;
		case "591" : return "玻利维亚"; break;
		case "591" : return "博茨瓦纳"; break;
		case "55" : return "巴西"; break;
		case "673" : return "文莱"; break;
		case "359" : return "保加利亚"; break;
		case "226" : return "布基纳法索"; break;
		case "257" : return "布隆迪"; break;
		case "237" : return "喀麦隆"; break;
		case "1" : return "加拿大"; break;
		case "34" : return "加那利群岛(西)"; break;
		case "238" : return "佛得角"; break;
		case "235" : return "乍得"; break;
		case "236" : return "中非"; break;
		case "56" : return "智利"; break;
		case "1345" : return "开曼群岛(英)"; break;
		case "619164" : return "圣诞岛"; break;
		case "619162" : return "科科斯岛"; break;
		case "57": return "哥伦比亚"; break;
		case "1767" : return "多米尼加联邦"; break;
		case "269" : return "科摩罗"; break;
		case "242" : return "科克群岛(新)"; break;
		case "506" : return "哥斯达黎加"; break;
		case "385" : return "克罗地亚"; break;
		case "53" : return "古巴"; break;
		case "357" : return "塞浦路斯"; break;
		case "420" : return "捷克"; break;
		case "45" : return "丹麦"; break;
		case "246" : return "迪戈加西亚"; break;
		case "298" : return "法罗群岛"; break;
		case "299" : return "格陵兰岛"; break;
		case "253" : return "吉布提"; break;
		case "1809" : return "多米尼加共和国"; break;
		case "593" : return "厄瓜多尔"; break;
		case "20" : return "埃及"; break;
		case "503" : return "萨尔瓦多"; break;
		case "240" : return "赤道几内亚"; break;
		case "372" : return "爱沙尼亚"; break;
		case "251" : return "埃塞俄比亚"; break;
		case "291" : return "厄立特里亚"; break;
		case "500" : return "福克兰群岛"; break;
		case "679" : return "斐济"; break;
		case "358" : return "芬兰"; break;
		case "33" : return "法国"; break;
		case "594" : return "法属圭亚那"; break;
		case "241" : return "加蓬"; break;
		case "220" : return "冈比亚"; break;
		case "995" : return "格鲁吉亚"; break;
		case "49" : return "德国"; break;
		case "233" : return "加纳"; break;
		case "350" : return "直布罗陀(英)"; break;
		case "30" : return "希腊"; break;
		case "1473" : return "格林纳达"; break;
		case "1671" : return "关岛(美)"; break;
		case "502" : return "危地马拉"; break;
		case "245" : return "几内亚比绍"; break;
		case "590" : return "瓜得罗普岛(法)"; break;
		case "224" : return "几内亚"; break;
		case "592" : return "圭亚那"; break;
		case "509" : return "海地"; break;
		case "504" : return "洪都拉斯"; break;
		case "36" : return "匈牙利"; break;
		case "354" : return "冰岛"; break;
		case "353" : return "爱尔兰"; break;
		case "91" : return "印度"; break;
		case "62" : return "印度尼西亚"; break;
		case "98" : return "伊朗"; break;
		case "964" : return "伊拉克"; break;
		case "972" : return "以色列"; break;
		case "39" : return "意大利"; break;
		case "225" : return "科特迪瓦"; break;
		case "1876" : return "牙买加"; break;
		case "81" : return "日本"; break;
		case "962" : return "约旦"; break;
		case "855" : return "柬埔寨"; break;
		case "7" : return "哈萨克斯坦"; break;
		case "254" : return "肯尼亚"; break;
		case "996" : return "吉尔吉斯斯坦"; break;
		case "686" : return "基里巴斯"; break;
		case "850" : return "朝鲜"; break;
		case "965" : return "科威特"; break;
		case "856" : return "老挝"; break;
		case "371" : return "拉脱维亚"; break;
		case "961" : return "黎巴嫩"; break;
		case "266" : return "莱索托"; break;
		case "231" : return "利比里亚"; break;
		case "218" : return "利比亚"; break;
		case "4175" : return "列支敦士登"; break;
		case "370" : return "立陶宛"; break;
		case "352" : return "卢森堡"; break;
		case "261" : return "马达加斯加"; break;
		case "265" : return "马拉维"; break;
		case "60" : return "马来西亚"; break;
		case "960" : return "马尔代夫"; break;
		case "223" : return "马里"; break;
		case "356" : return "马耳他"; break;
		case "1670" : return "马里亚纳群岛"; break;
		case "692" : return "马绍尔群岛"; break;
		case "596" : return "马提尼克(法)"; break;
		case "230" : return "毛里求斯"; break;
		case "269" : return "马约特岛"; break;
		case "222" : return "毛里塔尼亚"; break;
		case "691" : return "密克罗尼西亚"; break;
		case "52" : return "墨西哥"; break;
		case "1808" : return "中途岛(美)"; break;
		case "373" : return "摩尔多瓦"; break;
		case "377" : return "摩纳哥"; break;
		case "212" : return "摩洛哥"; break;
		case "258" : return "莫桑比克"; break;
		case "95" : return "缅甸"; break;
		case "389" : return "马其顿共和国"; break;
		case "976" : return "蒙古"; break;
		case "264" : return "纳米比亚"; break;
		case "674" : return "瑙鲁"; break;
		case "977" : return "尼泊尔"; break;
		case "31" : return "荷兰"; break;
		case "64" : return "新西兰"; break;
		case "505" : return "尼加拉瓜"; break;
		case "227" : return "尼日尔"; break;
		case "234" : return "尼日利亚"; break;
		case "683" : return "纽埃岛(新)"; break;
		case "672" : return "诺福克岛(澳)"; break;
		case "47" : return "挪威"; break;
		case "968" : return "阿曼"; break;
		case "92" : return "巴基斯坦"; break;
		case "680" : return "帕劳"; break;
		case "507" : return "巴拿马"; break;
		case "595" : return "巴拉圭"; break;
		case "51" : return "秘鲁"; break;
		case "63" : return "菲律宾"; break;
		case "48" : return "波兰"; break;
		case "351" : return "葡萄牙"; break;
		case "35196" : return "马德拉群岛(萄)"; break;
		case "35191" : return "亚速尔群岛(萄)"; break;
		case "1787" : return "波多黎各(美)"; break;
		case "974" : return "卡塔尔"; break;
		case "262" : return "留尼旺岛(法)"; break;
		case "40" : return "罗马尼亚"; break;
		case "250" : return "卢旺达"; break;
		case "684" : return "东萨摩亚(美)"; break;
		case "685" : return "西萨摩亚"; break;
		case "378" : return "圣马力诺"; break;
		case "966" : return "沙特阿拉伯"; break;
		case "221" : return "塞内加尔"; break;
		case "248" : return "塞舌尔"; break;
		case "232" : return "塞拉利昂"; break;
		case "65" : return "新加坡"; break;
		case "421" : return "斯洛伐克"; break;
		case "386" : return "斯洛文尼亚"; break;
		case "677" : return "所罗门群岛(英)"; break;
		case "27" : return "南非"; break;
		case "252" : return "索马里"; break;
		case "82" : return "韩国"; break;
		case "34" : return "西班牙"; break;
		case "94" : return "斯里兰卡"; break;
		case "1784" : return "圣文森特岛(英)"; break;
		case "290" : return "圣赫勒拿"; break;
		case "1758" : return "圣卢西亚"; break;
		case "1784" : return "圣文森特岛(英)"; break;
		case "249" : return "苏丹"; break;
		case "597" : return "苏里南"; break;
		case "268" : return "斯威士兰"; break;
		case "46" : return "瑞典"; break;
		case "41" : return "瑞士"; break;
		case "963" : return "叙利亚"; break;
		case "255" : return "坦桑尼亚"; break;
		case "66" : return "泰国"; break;
		case "228" : return "多哥"; break;
		case "690" : return "托克劳群岛(新)"; break;
		case "676" : return "汤加"; break;
		case "216" : return "突尼斯"; break;
		case "90" : return "土耳其"; break;
		case "993" : return "土库曼斯坦"; break;
		case "688" : return "图瓦卢"; break;
		case "256" : return "乌干达"; break;
		case "44" : return "英国"; break;
		case "380" : return "乌克兰"; break;
		case "598" : return "乌拉圭"; break;
		case "1808" : return "夏威夷"; break;
		case "907" : return "阿拉斯加"; break;
		case "998" : return "乌兹别克斯坦"; break;
		case "678" : return "瓦努阿图"; break;
		case "3906698" : return "梵蒂冈"; break;
		case "58" : return "委内瑞拉"; break;
		case "84" : return "越南"; break;
		case "1284" : return "维尔京群岛(英)"; break;
		case "1340" : return "维京京群岛(美)"; break;
		case "1808": return "威克岛(美)"; break;
		case "967" : return "也门"; break;
		case "381" : return "南斯拉夫"; break;
		case "243" : return "扎伊尔"; break;
		case "260" : return "赞比亚"; break;
		case "263" : return "津巴布韦"; break;
		case "259" : return "桑给巴尔"; break;
		case "969" : return "原民主也门地区"; break;
		case "689" : return "法属波里尼西亚"; break;
		case "675" : return "巴布亚新几内亚"; break;
		case "1681" : return "瓦里斯和富士那群岛"; break;
		case "852" : return "香港"; break;
		case "853" : return "澳门"; break;
		case "239" : return "圣多美和普林西比"; break;
		case "306" : return "(马尔维纳斯群岛)"; break;
		case "971" : return "阿拉伯联合酋长国"; break;
		case "689" : return "波利尼西亚"; break;
		case "967" : return "原阿拉伯也门地区"; break;
		case "387" : return "波斯尼亚和黑塞哥维那"; break;
		case "1649" : return "特克斯和凯科斯群岛"; break;
		case "1868" : return "特立尼达和多巴哥"; break;
		case "508" : return "圣皮埃尔岛及密克隆岛"; break;
		case "64672" : return "南极"; break;
		case "599" : return "荷属安的列斯群岛"; break;
		case "687" : return "新喀里多尼亚群岛(法)"; break;
		case "1664" : return "蒙特塞拉特岛(英)"; break;
		case "1869" : return "圣克里斯托弗和尼维斯"; break;
		default : return "";  break;
	}
}



/**
 * 获取url链接中的指定参数
 * name 为要获取的key
 */
function getUrlParameter(name){
	var myUrl = location.href;//获取url参数
	var par = myUrl.split("?")[1];//获取url中“？”后面的参数
	var returnVal = "";
	if( typeof(par) != 'undefined' && par != null && par != "" ){
		var tempList = par.split("&");
		for(var i = 0;i < tempList.length;i++){
			if((tempList[i].split("=")[0]) == name){
				returnVal = tempList[i].split("=")[1];
			}
		}
	}
	if(returnVal == undefined || returnVal == null || returnVal == ""){
		returnVal = "";
	}
	return returnVal;
}
/** 
* 除法运算，避免数据相除小数点后产生多位数和计算精度损失。 
* 
* @param num1被除数 | num2除数 
*/ 
function numDiv(num1, num2) { 
	var baseNum1 = 0, baseNum2 = 0; 
	var baseNum3, baseNum4; 
	try { 
		baseNum1 = num1.toString().split(".")[1].length; 
	} catch (e) { 
		baseNum1 = 0; 
	} 
	try { 
		baseNum2 = num2.toString().split(".")[1].length; 
	} catch (e) { 
		baseNum2 = 0; 
	} 
	with (Math) { 
		baseNum3 = Number(num1.toString().replace(".", "")); 
		baseNum4 = Number(num2.toString().replace(".", "")); 
		return numMulti((baseNum3 / baseNum4) , pow(10, baseNum2 - baseNum1)); 
	} 
};

/** 
* 乘法运算，避免数据相乘小数点后产生多位数和计算精度损失。 
* 
* @param num1被乘数 | num2乘数 
*/ 
function numMulti(num1, num2) {
	var baseNum = 0; 
	try { 
		baseNum += num1.toString().split(".")[1].length; 
	} catch (e) {} 
	try { 
		baseNum += num2.toString().split(".")[1].length; 
	} catch (e) {} 
	return Number(num1.toString().replace(".", "")) * Number(num2.toString().replace(".", "")) / Math.pow(10, baseNum); 
}; 

/**
  * 日期比较
  *
  * @param dateOne | dateTwo
  * 
  * return:
  * dateOne早 cha < 0
  * 相等 cha = 0
  * dateOne晚 cha < 0
  */
function daysBetween(dateOne,dateTwo){
	/**
	 * 下面一段代码是为了将数字进行处理，将传入的值改为'yyyy-mm-dd'
	 * 原因：
	 * 传入到本方法的值可能是'yyyy-mm-dd'也可能是'yyyymmdd'，
	 * 首先将传入的值去掉符号的字符，只保留8位数字，即：'yyyymmdd'
	 * 然后将'yyyymmdd'转为'yyyy-mm-dd'进行比较
	 */
	dateOne = unformat1(dateOne);
	dateTwo = unformat1(dateTwo);
	dateOne = dateOne.substring(0,4)+"-"+dateOne.substring(4,6)+"-"+dateOne.substring(6,8);
	dateTwo = dateTwo.substring(0,4)+"-"+dateTwo.substring(4,6)+"-"+dateTwo.substring(6,8);

	var oneMonth = dateOne.substring(5,dateOne.lastIndexOf('-'));
	var oneDay = dateOne.substring(dateOne.length,dateOne.lastIndexOf('-')+1);
	var oneYear = dateOne.substring(0,dateOne.indexOf('-'));
	

	var twoMonth = dateTwo.substring(5,dateTwo.lastIndexOf('-'));
	var twoDay = dateTwo.substring(dateTwo.length,dateTwo.lastIndexOf('-')+1);
	var twoYear = dateTwo.substring(0,dateTwo.indexOf('-'));
	
	var cha = (Date.parse(oneMonth+'/'+oneDay+'/'+oneYear)-Date.parse(twoMonth+'/'+twoDay+'/'+twoYear))/86400000;
	return cha;
}
/**
 * js 实现千位分隔符
 * @param n 需要分隔的值 | j 分隔符
 */
function formatNumber(n, j) {
	n = n + "";
	n = unformat(n);
	var markPre = "";
	if(!isNaN(n) && n < 0){
		markPre = "-";
		n = Math.abs(n);
	}
    var s = n + "";
    var numList = s.split(".");
    var l = numList[0].length;
    var m = l % 3;
    if (m==l){
    	if(numList[1] == undefined){
		    return markPre+numList[0];
    	}else{
    		return markPre+numList[0]+"."+numList[1];
    	}
    }
    else if(m==0){
    	if(numList[1] == undefined){
		    return markPre+(numList[0].substring(m).match(/\d{3}/g)).join(j);
    	}else{
		    return markPre+(numList[0].substring(m).match(/\d{3}/g)).join(j)+"."+numList[1];
    	}
    }
	else{
    	if(numList[1] == undefined){
			return markPre+([numList[0].substr(0,m)].concat(numList[0].substring(m).match(/\d{3}/g)).join(j));
    	}else{
			return markPre+([numList[0].substr(0,m)].concat(numList[0].substring(m).match(/\d{3}/g)).join(j))+"."+numList[1];
    	}
	}
}
/**
 * 保留两位小数，没有小数不做处理
 * @param num
 * @returns
 */
function unformatNumber(num){
	if(parseInt(num)==num){
		return parseInt(num);
	}else{
		return parseFloat(num).toFixed(2);
	}
}
/**
 * 千分位分隔符以“,”号分隔，四舍五入保留2位小数
 * 如：100,000,000.05
 * @param num
 * @returns
 */
function format (num) {
    return (num.toFixed(2) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
}
function unformat (num)
{
	return (num+"").replace(eval('/,/gi'),'');
}
/**
 * 字符替换为*号，如电话号码和身份证号码部分改为*(升级版本)
 * str为要转换的字符串
 * num1为截取字符串的开始索引(前面保留的位数)
 * num2为截取字符串的后面保留的位数
 */
function replaceStr1(str,num1,num2){
	str = str + "";
	var rep = "  ";
	for(var i = 0;i < str.length-num1-num2;i++){
		rep += "*";
		if((i+1)%4==0){
			rep += "  ";
		}
	}
	var strPre = str.substr(0,num1);
	var strSuff = str.substr((str.length-num1),num2);
	return strPre+rep+strSuff;
}

/**
 * 字符替换为*号，身份证号码 6210 8888 8888 8888 5462 ->6210 **** **** 5462
 * str为要转换的字符串
 * num1为截取字符串的开始索引(前面保留的位数)
 * num2为截取字符串的后面保留的位数
 */
function replaceStr2(str,num1,num2){
	
	var strPre = str.substr(0,num1);
	var strSuff = str.substr((str.length-num1),num2);
	return strPre+"  ****  ****  "+strSuff;
}

/**
 * 去掉字符串中的‘-’
 * @param num
 * @returns
 */
function unformat1(num)
{	
	return (num+"").replace(eval('/-/gi'),'');
}

/**
 * 入参格式YYYYMMDD   出参格式：YYYY年M月D日 
 * @param date
 */
function dateformat(date){
	if(date==null || date==''){
		return null;
	}
	date = unformat1(date);
	var year = date.substr(0,4);
	var month = date.substr(4,2);
	var day = date.substr(6,2);
	
	var str = year+"年"+parseInt(month)+"月"+parseInt(day)+"日";
	return str;
}

//20150808 --> 2015-08-08
function dataFormat1(date){
	
	if(date.length!=8 && isNaN(date)){
		return date;
	}
	
	var year = date.substring(0,4);
	var m = date.substring(4,6);
	var d = date.substring(6,8);
	var temp = year+"-"+m+"-"+d;
	return temp;
}

/* 检验姓名 */
function checkName(name) {
	if (name == null || $.trim(name) == "") {
		errorRemark("真实姓名不能为空");
		return false;
	}

	if (name.length < 2 || name.length > 20) {
		errorRemark("姓名长度有误，请重新输入");
		return false;
	}

	if (!Validater.isName(name)) {
		errorRemark("姓名格式必须为汉字、英文或点号");
		return false;
	}

	return true;

}

/* 校验证件号码 */
function checkIdNo(idno) {
	if (idno == null || $.trim(idno) == "") {
		errorRemark("身份证号码不能为空");
		return false;
	} else {
		var result = legion.regValidater.isIdCardNumber4ZSJJ(idno);
		if (result != true) {
			errorRemark("请输入正确的身份证号");
			return false;
		}
	}
	return true;
}

/**
 * 对onkeyup属性进行优化，部分手机onkeyup删除或者输入中文的时候无法触发。
 * @param _idList
 * @param Func
 */
function cmwaOnkeyup(_idList,Func){
	//先判断浏览器是不是万恶的IE，没办法，写的东西也有IE使用者
	var bind_name = 'input';
	if (navigator.userAgent.indexOf("MSIE") != -1){
	    bind_name = 'propertychange';
	}
	Func = Func || function (){};
	var list = _idList.split(";");
	if(list != null && list.length > 0){
		$.each(list, function(i, item) {
			$('#'+item).bind(bind_name, function(){
				Func();
			})
		});
	}
	
}
/*对参数中最后一个参数的#号进行处理，因为一些页面参数带#会出错*/
function removeSpecialStr(str){
    if(str.substr(str.length-1) =='#'){
        return str.substring(0,str.length-1);
    }else{
        return str;
    }
}
/**
 * 日期格式字符转换
 * @param date 格式"20150101"
 * @returns 格式"2015-01-01"
 */
function formatDate(date){

	if(date == null || date == ""){
		return "";
	}
	date = date + "";
	date = unformat1(date);
	return date.substr(0,4)+"-"+date.substr(4,2)+"-"+date.substr(6,2);
}




function numAdd(num1, num2) {
	var r1,r2,m,c; 
	try { 
		r1 = num1.toString().split(".")[1].length; 
	} catch (e) {} 
	try { 
		r2 = num2.toString().split(".")[1].length; 
	} catch (e) {} 
	
	c=Math.abs(r1-r2);
	m=Math.pow(10,Math.max(r1,r2))
	if (c>0) {
		var cm=Math.pow(r1,r2);
		if (r1>r2) {
			num1=Number(num1.toString().replace(".", ""));
			num2=Number(num2.toString().replace(".", ""))*cm;
		}else{
			num1=Number(num1.toString().replace(".", ""))*cm;
			num2=Number(num2.toString().replace(".", ""));
		}
	}else{
		num1=Number(num1.toString().replace(".", ""));
		num2=Number(num2.toString().replace(".", ""));
	}
	return (num1+num2)/m; 
};

/**
 * 埋点
 * @param option
 */
// function recordOperation(option){
// 	var _option = {groupId:null};
// 	$.extend(true, _option, option); 
// 	if(_option.pageSource){
// 		$.ajax({
// 			url:'/WeixinService/buriedData.xhtml',
// 			type:'post',
// 			data:_option,
// 			success:function(data){
//				
// 			}
// 		});
// 	}
// }



function recordOperation(option) {
    var _option = {groupId:null};
    $.extend(true, _option, option);
    var parm = {
        'data': "",
        'event': _option.eventId,
        'group': "",
        'openid': "",
        'pageSource': _option.pageSource,
        'page': _option.pageId,
        'trackDate': getNowFormatDate(),
        'trackMillis': new Date().getTime(),
        'unionid': "",
        'userId': localStorage.getItem("userId")
    }
    $.ajax({
        url: "/api/wxtrack",
        data: JSON.stringify(parm),
        dataType: "json",
        contentType: "application/json",
        cache: false,
        type: "POST",
        success: function (n) {
        }

    });
}
/**
 * 获取系统当前时间
 */
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + date.getHours() + seperator2 + date.getMinutes()
        + seperator2 + date.getSeconds();
    return currentdate;
}

/**
 * 插入用户操作记录
 * @param registmark 登录渠道
 * @param type 用户操作类型
 */
function pageEventData(registmark,type){
	var param = {
	"loginChannel":registmark,
	"type":type
	};
	$.ajax({
		async : false,
		url : "/WeixinService/business/insertUserOperateLog.xhtml",
		data : JSON.stringify(param),
		dataType : "json",
		contentType:"application/json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
		}
	});
}


/**
 * 查询字典数据
 * @param paramType
 * @param paramKey
 * @param pmValueOne
 * @returns
 */
function queryParamList(paramType,paramKey,pmValueOne){
    var data = null;
    $.ajax({
        async: !1,
        url: "/WeixinService/business/queryParamList.xhtml",
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


/**
 * 获取cookie
 * @param name
 * @returns
 */
function getCookie(name)
{
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg)){
		return unescape(arr[2]);
	}else{
		return null;
	}
} 
/**
 * 设
 * @param key
 * @param val
 * @param expires
 * @returns
 */
function setCookie(key,val,expires){
	if(!expires){
		expires = getExpiresOneDay();
	}
	document.cookie=key+'='+val+';expires='+expires;
}
/**
 * 获取cookie一天有效时间
 * @returns
 */
function getCookieExpiresOneDay(){
	//设置有效期
	var curDate = new Date();  
    //当前时间戳  
    var curTamp = curDate.getTime();  
    //当日凌晨的时间戳,减去一毫秒是为了防止后续得到的时间不会达到00:00:00的状态  
    var curWeeHours = new Date(curDate.toLocaleDateString()).getTime() - 1;  
    //当日已经过去的时间（毫秒）  
    var passedTamp = curTamp - curWeeHours;  
    //当日剩余时间  
    var leftTamp = 24 * 60 * 60 * 1000 - passedTamp;  
    var leftTime = new Date();  
    leftTime.setTime(leftTamp + curTamp);  
    return leftTime.toString();
}




function queryUserinfo(){
	var resultData = {};
	$.ajax({
    	async:false,
        url: "/WeixinService/business/queryUserinfo.xhtml",
        data: "",
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
        	errorRemark("网络繁忙，请稍后再试。");  
        }, 
        success : function (data){
        	
        	if(!!data && (data.userType == null || data.userType != "30")){
				isRealName = false;
        	}
        	
			if(data.returnCode != null && data.returnCode == "0000"){
				var flag = false;
				if(!data.nation || !data.vocCode || !data.taxResidentType || !data.birthDate){
					flag = true;
				}
				resultData.nation = data.nation;
				resultData.nationNM=data.nationNM;
				resultData.province=data.province;
				resultData.provinceNM=data.provinceNM;
				resultData.city=data.city;
				resultData.cityNM=data.cityNM;
				resultData.vocCode=data.vocCode;
				resultData.vocName=data.vocCodeNM;
				resultData.flag=flag;
				resultData.addr=data.addr;
				resultData.taxResidentType = data.taxResidentType;
				resultData.taxResidentTypeNm = data.taxResidentTypeNm;
				resultData.birthDate = data.birthDate;
				resultData.syncInvprtpAlert = data.syncInvprtpAlert;
				resultData.cmfUserId = data.cmfUserId;
			}
        }
    });
	
	return resultData;
}


function getNowTime(){
	var nowDate = new Date();
	var time = nowDate.getFullYear() + "" +((nowDate.getMonth()+1)<10?"0":"")+(nowDate.getMonth()+1)+""+(nowDate.getDate()<10?"0":"")+nowDate.getDate();
	return time;
}



/**
 * 检查是否登录
 * @returns
 */
function checkIsLogin() {
	var urlVal = "/WeixinService/setUp/checkUserIsLogin.xhtml";
	var isLogin;
	$.ajax({
		async : false,
		url : urlVal,
		type : "post",
		dataType : 'json',
		data : {},
		error : function() {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data, textStatus) {
			if(data.returnCode != null && data.returnCode == "0000"){
				isLogin = true;
			}else{
				isLogin  = false;
    		}
		}
	});
	return isLogin;
}

/**
 * 添加cookies
 * @param {Object} name
 * @param {Object} value
 * @param {Object} path
 * @param {Object} expiresHours
 */
function addCookie(name,value,path, expiresHours){
        var cookieString= name+"="+escape(value);
        //判断是否设置过期时间
        if(expiresHours>0){
            var date=new Date();
            date.setTime(date.getTime()+expiresHours*3600*1000);
            cookieString=cookieString+"; expires="+date.toGMTString();
        }

        cookieString +=  "; path=" +( path ? path : "/");
        document.cookie=cookieString;
}
