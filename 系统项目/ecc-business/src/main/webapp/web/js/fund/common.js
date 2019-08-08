

/**
 * �����ڴ�С�仯�ǣ��Զ�����jqgrid�Ŀ��
 * @param  {Object} $dom ʹ����jqgrid�Ķ���
 */
function jqGridResize($dom, $parents, timeout) {
    var $jqGridWrapper = $parents || $dom.parents('.jqGrid_wrapper');
    var width = $jqGridWrapper.width() - 2;
    $dom.jqGrid("setGridWidth", width);
    var timer = null;
    $(window).on('resize', function() {
        cancelAnimationFrame(timer);
        timer = requestAnimationFrame(function() {
            if (width != $jqGridWrapper.width()) {
                width = $jqGridWrapper.width() - 2;;
                $dom.jqGrid("setGridWidth", width);
            }
        });
    })
}

//ȫ���л�
function toggleFullScreen(de, cb) {
    if (!document.fullscreenElement && !document.mozFullScreenElement && !document.webkitFullscreenElement) {
        if (de.requestFullscreen) {
            de.requestFullscreen();
        } else if (de.mozRequestFullScreen) {
            de.mozRequestFullScreen();
        } else if (de.msRequestFullScreen) {
            de.msRequestFullScreen();
        } else if (de.webkitRequestFullscreen) {
            de.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
        }
    } else {
        if (document.cancelFullScreen) {
            document.cancelFullScreen();
        } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        } else if (document.msCancelFullScreen) {
            document.msCancelFullScreen();
        } else if (document.webkitCancelFullScreen) {
            document.webkitCancelFullScreen();
        }
    }
    cb && cb;
}



/**
 * �����´���
 * @author fangdb
 * @param actionUrl
 * @return
 */
var openDialog = function(actionUrl) {
    window.open(actionUrl, "", "height=550, width=950, top=50, left=250, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
}


/**
 * �����󴰿�
 */
var openBigDialog=function(actionUrl){
	window.open(actionUrl, "", "height=750, width=1150, top=50, left=250, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
}
/**
 * ����С�ʹ���
 */
var openSmallDialog = function(actionUrl) {
    window.open(actionUrl, "", "height=300, width=500, top=100, left=350, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
}


/**
 * ���validate ��select��radio�ȿؼ��������ֶ�����
 * @param id
 */
function triggerValidOnSelectChange(){
	var ids='';
	for(var i=0;i<arguments.length;i++){
		ids +='#'+arguments[i];
		if(i !=(arguments.length-1)){
			ids+=',';
		}
	};
	if(arguments.length>0){
		$(ids).change( function() {
			var $this=$(this);
			$this.valid && $this.valid();
		});
	}
}



//�����select2����ȫ������
if ($.fn.select2) {
    $.fn.select2.defaults.set("allow-clear", true);
    $.fn.select2.defaults.set("width", '100%');
    $.fn.select2.defaults.set("placeholder", '��ѡ��');
    $.fn.select2.defaults.set("language","zh-CN");
}

//�����Ƕ�validate�����ȫ������

if ($.validator) {
    $.validator.setDefaults({
    	ignore : [],
    	focusInvalid : false,
        errorPlacement: function(error, element) {
            var $element = $(element);
            var $parent = $(element).parents('.form-inner');
            //select����
            var domObject = $element[0].nodeName; //��jQuery�����еõ�ԭ����DOM����
            if (domObject=='SELECT') {
            	$parent.find('span span span').addClass("error");
			}
            if ($parent.length) {
                error.appendTo($parent);
                console.info(error);
            }else if(domObject=='TEXTAREA'){
            	error.appendTo($parent);
                console.info(error);
            } else {
                error.insertAfter($element);
                console.error(error);
            }
        }
    });
}


if (typeof toastr !== 'undefined') {
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "progressBar": true,
        "preventDuplicates": false,
        "positionClass": "toast-top-right",
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "7000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
}


/**
 * �л�����
 * @param xxje
 * @param amtNM
 * @returns {String}
 */
function capMoneyNoCheck(xxje,amtNM) {

	var low	;				
	var i,k,j, l_xx1; 			
	var cap = "", dxnr, xx1, unit, lastDigit = "", endUnit ="", lastUnit = "" ; 	
	
	var digits = "��Ҽ��������½��ƾ�"; 
	var units = "�ֽ�Ԫʰ��Ǫ��ʰ��Ǫ��ʰ��Ǫ"; 
		
	if(xxje == ""){
		return "";
	}
	if ( !xxje.match(/^(\d{1,}(|(\.{1}\d{0,2})))$|^(\.{1}\d{1,2})$/) )
	{
		return "<font color=red>������Ч��</font>"; 
	} else if(xxje.length>0 && xxje.substr(0,1) == 0){		//��һλΪ0
		return "<font color=red>������Ч��</font>"; 
	}
	low = parseFloat(xxje);	
	//if (isNaN(low)) return "������Ч"; 

	xx1 = Math.round(low * 100.0) + "" ;
	l_xx1 = xx1.length; 
	
	for (i=0; i<l_xx1; i++) { 
		j = l_xx1 -1 - i; 
		unit = units.substr(j, 1); 			
		k = parseInt(xx1.substr(i, 1)); 
		digit = digits.substr(k, 1);			
		cap = cap + digit + unit;
	}	
	
	cap = cap.replace( /���|���|��ʰ|���|��Ǫ/g, "��");
	cap = cap.replace( /��+/g, "��");
	cap = cap.replace( /����/g, "��");
	cap = cap.replace( /����/g, "��");
	cap = cap.replace( /��Ԫ/g, "Ԫ");
	cap = cap.replace( /����/g, "��");
	cap = cap.replace( /^Ҽʰ/, "ʰ");
	cap = cap.replace( /��$/, "��");
	
	if (cap == "��") cap = "��Ԫ��";
	
	cap="<font color=red>"+ cap +"</font>";
	return cap; 
}



/**
 * �ϴ�����IDs
 */
function serialAttId(containerId){
	var attIds="";
	$("#"+containerId).find(".uploaded").each(function(){
		var attId=$(this).attr("attId");
		if(attId!= undefined && attId !=''){
			attIds +=attId+",";
		}
	});
	return attIds;
}


var COMMON_INIT={
		initDateYMDComponent : function(dateId){
			$('#'+dateId).datepicker({
				format: "yyyy-mm-dd",
			    todayBtn: "linked",
			    language: "zh-CN",
			    autoclose: true,
			    todayHighlight: true
		    });
		}
}

/**
 * 4λС������
 * @param math
 * @returns {Boolean}
 */
function mathHandle(math){
	var number=math.value;
	if(number==null&math!=null)
		number=math
	if(number==null || number==''){
		return false;
	}
	var reg=new RegExp(/^([1-9]\d{0,15}|0)+(\.\d{1,4})+$/);
	if (!reg.test(number)) {
		var stringReg=new RegExp(/^([1-9]\d{0,15}|0)?$/);
		if(!stringReg.test(number)){
			var ss=number.split('.')[0];
			if(!stringReg.test(ss)){
				toastr.warning("����Ϊȫ����");
				return false
			}
			toastr.warning("ֻ�ܱ�����λС��");
			return false;
		}
		if(number==0||number=='0'){
			return true;
		}
		math.value=number+".0000";
	}
	var decimal=number.split('.')[1];
	if(decimal.length==3)
		math.value=number+"0"
	if(decimal.length==2)
		math.value=number+"00"
	if(decimal.length==1)
		math.value=number+"000"
	return true;
}

//���ظ���
function downloadAttach(attId){
	window.open("/mecc/ecc/attach/downloadAttach.xhtml?attId="+attId);
}


/**
 * ȥ����ѯ������ո�
 * @param str
 * @returns
 */
function trimString(str){ //ɾ���������˵Ŀո�
	if(str!=null&&str!='')
		return str.replace(/(^\s*)|(\s*$)/g, "");
	else
		return null;
}


var ctools={
	/**
	 * ����uuid�ķ���
	 * @author ex-weicb
	 * @param len ���ȣ�����Ĭ��36λuuid
	 * @param radix Դ�ַ�����
	 * @returns ���ɵ�uuid
	 */
	uuid:function(len,radix){
//		alert(0);
		var chars='0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
		var uuid=[],i;
		radix=radix||chars.length;
		if(len){
			for(i=0;i<len;i++){
				uuid[i]=chars[0|Manth.random()*radix];
			}
		}else{
			var r;
			uuid[8]=uuid[13]=uuid[18]=uuid[23]='-';
			uuid[14]='4';
			for(i=0;i<36;i++){
				if(!uuid[i]){
					r=0|Math.random()*16;
					uuid[i]=chars[(i==19) ? (r & 0x3)| 0x8 : r];
				}
			}
		}
		return uuid.join('');
	},
	
	/**
	 * alert��ʾ��-toastr���������ʾ��Ϣ������ʾ����ѡ��һ�����ݡ�
	 * @author ex-weicb modify by wangys
	 * @param msg ��ʾ��Ϣ ���мӴ�����
	 * @param title ��ʾ����� �ڶ���С���壬һ�㲻��Ҫ�Զ���
	 * @param type ���� success:�ɹ�[��ɫ]��warn:����[��ɫ]��Ĭ�ϣ���error������[��ɫ]
	 * 
	 */
	alert : function(msg, title, type) {
		if (!type) {
			console.error("���������͸����������warning��success��error");
			return;
		}
		type = type || "warning";
		title = title || "";// msg:���мӴ����� ,title:�ڶ���С����
		if (type == "success")
			toastr.success(title, msg);
		else if (type == "error")
			toastr.error(title, msg);
		else
			toastr.warning(title, msg);
	},
	/**
	 * confirm��ʾ���װ-sweetalert���֧��ͨ��confirm���Զ���confirm
	 * �������������Ͻ�ֱ���ڴ�����ֱ��ʹ��swal(),�������ģ̬��ͳһά��
	 * @author ex-weicb
	 * @param option
	 *            ���˲�Ϊ�ַ����������confirm����ʾ��Ϣ<br>
	 *            ��Ϊ������һ�����ڸ��Ի�confirm���������ȷ����ȡ����ť�����¼�;ȷ����ȡ����ť���ı���Ҫ���Ի������
	 * @param fn
	 *            ����һ������Ϊ�ַ�������˺�������ȷ����ť�Ļص�����<br>
	 *            ����һ������Ϊ������˺���Ϊ������ť�Ļص�����������һ���������Ͳ�����true�����û��������ȷ����ť��false�����û��������ȡ����ť<br>
	 * @description �ܶ���֮������һ���������ַ�����ʱ�򣬴���ͨ���÷�����������Ϊ��ʾ��Ϣ��ȷ����ť�ص���������ʾ��Ϣ˵������ɫС���壩<br>
	 *              ����һ�������Ƕ����ʱ�򣬴����Զ����÷�����������Ϊ�Զ���������󡢰�ť����Ļص�����
	 */
	confirm : function(option, fn) {
		var _confirm_option =function(option, callback) {// �����Զ���������õ�confirm��װ��sweetAlert���
			var default_option = {
				title : "��ʾ��Ϣ",
				text : "",
				type : "warning",
				html : false,
				showCancelButton : true,// ��ʾ�رհ�ť
				confirmButtonColor : "#fc6821",// ȷ����ť����ɫ
				confirmButtonText : "确定",// ȷ����ť���ı�
				cancelButtonText : "取消",// ȡ����ť���ı�
				closeOnConfirm : true,// ȷ����ť�Ƿ�ر�ģ̬��
				closeOnCancel : true// ȡ����ťʽ��ر�ģ̬��
			};
			$.extend(default_option, option);
			swal(default_option, function(isConfirm) {// �ص�����
				if (callback && typeof callback === 'function') {
					setTimeout(function() {// ����������޷��رյ�����
						callback(isConfirm);
					}, 350);
				}
			});
		};
		if (option && 'string' === typeof option) {
			_confirm_option({
				title : option,
				text : arguments[2] || ""
			}, function(isConfirm) {
				if (isConfirm && fn && typeof fn === 'function')
					fn();
			});
		} else if (option && 'object' === typeof option){
			_confirm_option(option, fn);
		} else {
			console.error("unsupported...");
		}
	},
	/**
	 * sweet����alert�����򣺵�Ƕ����ctools.confirm��ʱ���� 
	 * @author ex-weicb
	 * @param msg ��ʾ��Ϣ���Ӵִ�����
	 * @param type ���� success:�ɹ� ,warning:���棨Ĭ�ϣ���error������
	 * @param title ��ɫС����
	 * @param fn �رհ�ť�ص�����
	 */
	alert_sweet : function(msg, type, title, fn) {
		type = type || "warning";
		if (type != "success" && type != "warning" && type != "error")
			type = "warning";
		swal({
			title : msg,
			text : title || "",
			type : type,
			confirmButtonText : "关闭"
		}, function() {
			if (fn && typeof fn === 'function')
				fn();
		});
	},
	/**
	 * �ύ��̬��
	 * @param action  ��·��
	 * @param obj  ���ն������飬key��value�Ǳ���߱��ģ�����
	 *  var array=[];
		array.push({"key":"sp[basicDate]","value":basicDate});
		array.push({"key":"sp[strMonth]","value":strMonth});
		array.push({"key":"sp[prjserialId]","value":prjserialId});
		array.push({"key":"sp[status]","value":status});
		array.push({"key":"sp[courseStatus]",value:courseStatus});
		����array
	 * @param target ���ύĿ��  blank,self
	 */
	submitDynForm:function (action,obj,target) {
		var str=[];
		var id='dy'+new Date().getTime();
		str.push('<form id="'+id+'" method="post" target="'+target+'" action="'+action+'">');
		
		$(obj).each(function(key,obj) {
			var name=obj["key"];			
			var value=obj["value"];
			str.push('<input type="hidden" name="'+name+'" id="'+name+'" value="'+value+'">');
		});
		str.push('</form>');
		$(document.body).append(str.join(''));
		$("#"+id).submit();
		$("#"+id).remove();
	}

	
};




//�ֻ�������֤       
jQuery.validator.addMethod("isMobile", function(value, element) {       
    var length = value.length;  
   var mobile = /^((\(\d{2,3}\))|(\d{3}\-))?1[3,8,5]{1}\d{9}$/;   
  return this.optional(element) || (length == 11 && mobile.test(value));       
}, "����ȷ��д�ֻ�����");    
    
jQuery.validator.addMethod("isFloat", function(value, element) {       
   return this.optional(element) || /^[-\+]?\d+$/.test(value) || /^[-\+]?\d+(\.\d+)?$/.test(value);        
}, "ֻ�������֣�����С��");   


jQuery.validator.addMethod("isSymbol", function(value, element) {
	var ss=new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~��%����@#������&*�����D|{}��������������'��������]");
	  if(ss.test(value))
		  return false;
	  return true;       
	}, "���ܴ����������");   

/*$(function(){
	authority();
})
*/
//���м�Ȩ
/*function authority(){
	var transCode=$('#transCode').val();
	if(!transCode)
		return;
	var data={
			transCode:transCode
	}
	
	var actionUrl="/mecc/ecc/common/authority.xhtml";
	$.ajax({
		type: 'POST',
		url: actionUrl,
		dataType:'json',
		sync:true,
		data:data,
		success: function(data){
			if(data.ResultCode!='0000'){
				window.location.href='/ECCApp/ECCFramework/authorityError.jsp';
			}
		},
		error:function(xhr){
			switch(xhr.status){
				case 403:sweetAlert("�Բ������޴�Ȩ�ޣ�","","error");break;
				case 404:sweetAlert("�Բ����޴�ҳ�棡", "","error");break;
				case 500:sweetAlert("�ڲ���������ϵ����Ա��","","error");break;  
				case 504:sweetAlert("��ʱ������ϵ����Ա��", "","error");break;  
				case 417:sweetAlert("�ڲ���������ϵ����Ա��", "","error");break;  
			}
		}
	});
	
}

*/
//select��date change�¼�
function triggerValidOnSelectChange(){
	var ids='';
	for(var i=0;i<arguments.length;i++){
		ids +='#'+arguments[i];
		if(i !=(arguments.length-1)){
			ids+=',';
		}
	};
	if(arguments.length>0){
		$(ids).change( function() {
			var $this=$(this);
			var $parent = $this.parents('.form-inner');
			if($this.valid && $this.valid()){
				$parent.find('span span span').removeClass("error");
			}else{
				$parent.find('span span span').addClass("error");
			}
		});
	}
}

//select��date change�¼�
function triggerValidOnTextAreaChange(){
	var ids='';
	for(var i=0;i<arguments.length;i++){
		ids +='#'+arguments[i];
		if(i !=(arguments.length-1)){
			ids+=',';
		}
	};
	if(arguments.length>0){
		$(ids).change( function() {
			var $this=$(this);
			$this.valid && $this.valid();
			
			
		});
	}
}


//select��date change�¼�
function triggerOnChangeTextAreaChange(id){
		
}
function triggerValidOnDateChange(){
	var ids='';
	for(var i=0;i<arguments.length;i++){
		ids +='#'+arguments[i];
		if(i !=(arguments.length-1)){
			ids+=',';
		}
	};
	if(arguments.length>0){
		$(ids).change( function() {
			var $this=$(this);
			$this.valid && $this.valid();
		});
	}
}

/**
 *  ��̬�ǿ�У�鷽�����������ֶ���ӣ� ���ң���ӽ�����ʧ���¼���
 */
function dynamicCalibration(validId,content,relational){
	var $element=$("#"+validId);
	var $relational=$("#"+relational);
	var $parent = $element.parents('.form-inner');
	var errorText=content===null?"it must be not null":content;
	var validIderror=validId+"-error";
	$("#"+validIderror).remove();
	var domObject = $relational[0].nodeName; //��jQuery�����еõ�ԭ����DOM����
	var error='<label id="'+validIderror+'" class="error" for="title2">'+errorText+'</label>'
	var boolean = domObject=="TEXTAREA" ? $relational[0].value===null||$relational[0].value=='' : $relational.val()===null ||$relational.val()==''
	if (boolean) {
		$("#"+validIderror).remove();
		$element.unbind('focus')
		$element.unbind('blur')
		return;
	}
	$element.unbind('blur')
	$element.addClass("error");
	 domObject = $element[0].nodeName;
    if (domObject=='SELECT') {
    	$parent.find('span span span').addClass("error");
	}
    if ($parent.length) {
        $parent.append(error);
    }else if(domObject=='TEXTAREA'){
    	$parent.append(error);
    } else {
    	$parent.append(error);
    }
	
    $element.focus(function(){
    	$("#"+validIderror).remove();
    })
    $element.blur(function(){
    	var $this=$(this);
    	if($this.val()===null || $(this).val()=='')
    		dynamicCalibration(validId,content,relational)
   });	 
}

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) 
    	return unescape(r[2]); 
    return null;
}

/**
 * ���ύ������ǰ�˷�ֹ�ظ��ύ
 * @param buttonId
 * @param func
 * @param formId
 */
function SubmitByAjaxPreventSecond(buttonId,formId,actionUrl,func){
	var $button = $('#'+buttonId);
	$button.prop("disabled",true);
	$button.button('loading');
	//��һ�����ݴ�����
	if(func &&('function' == typeof func) ){
		if(!func()){
			setTimeout(function(){
				$button.button('reset');
			},1000);
			return;
		}
	}
	var $form=$('#'+formId);
	if(false === $form.valid()){
		toastr.warning('', '��������Ϣ');
		setTimeout(function(){
			$button.button('reset');
		},1000);
	    return ;
	} 
	var data = $form.serializeArray();
	if (data === null || !data) {
		toastr.warning('', '���ݴ���');
		setTimeout(function(){
			$button.button('reset');
		},1000);
	    return;
	}
	
	var url=actionUrl;
	$.ajax({
		type:"POST",
		url: actionUrl,
		dataType:'json',
		data:data,
		success: function(data){
			if(data.ResultCode=='0000'){
				window.location.href='/ECCApp/ECCFramework/success.jsp';
			}else{
				swal(data.ResultFail,data.ResultDesc,"error");
				$button.button('reset');
			}
		},
		error:function(xhr){
			switch(xhr.status){
				case 403:sweetAlert("�Բ������޴�Ȩ�ޣ�","","error");break;
				case 404:sweetAlert("�Բ����޴�ҳ�棡", "","error");break;
				case 500:sweetAlert("�ڲ���������ϵ����Ա��","","error");break;  
				case 504:sweetAlert("��ʱ������ϵ����Ա��", "","error");break;  
				case 417:sweetAlert("�ڲ���������ϵ����Ա��", "","error");break;  
			}
			
			$button.button('reset');
		}
	});
}


/**
 *  ��̬�ǿ�У�鷽�����������ֶ���ӣ� ���ң���ӽ�����ʧ���¼���
 */
/*
function dynamicCalibration(validId,content,relational){
	var $element=$("#"+validId);
	var $relational=$("#"+relational);
	var $parent = $element.parents('.form-inner');
	var errorText=content===null?"it must be not null":content;
	var validIderror=validId+"-error";
	$("#"+validIderror).remove();
	var domObject = $relational[0].nodeName; //��jQuery�����еõ�ԭ����DOM����
	var error='<label id="'+validIderror+'" class="error" for="title2">'+errorText+'</label>'
	var $error=$('#'+validIderror);
	var boolean = domObject=="TEXTAREA" ? $relational[0].value===null||$relational[0].value=='' : $relational.val()===null ||$relational.val()==''
	if (boolean) {
		$("#"+validIderror).remove();
		$element.unbind('focus')
		$element.unbind('blur')
		return;
	}
	$element.unbind('blur')
	$element.addClass("error");
	 domObject = $element[0].nodeName;
    if (domObject=='SELECT') {
    	$parent.find('span span span').addClass("error");
	}
    if ($parent.length) {
    	$error.appendTo($parent);
    }else if(domObject=='TEXTAREA'){
    	$error.appendTo($parent);
    } else {
    	$parent.append(error);
    }
	
    $element.focus(function(){
    	$("#"+validIderror).remove();
    })
    $element.blur(function(){
    	var $this=$(this);
    	if($this.val()===null || $(this).val()=='')
    		dynamicCalibration(validId,content,relational)
   });	 
}

/**
 * ��ȡurl����ֵ
 * @param name
 * @returns
 */
/*
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) 
    	return unescape(r[2]); 
    return null;
}*/