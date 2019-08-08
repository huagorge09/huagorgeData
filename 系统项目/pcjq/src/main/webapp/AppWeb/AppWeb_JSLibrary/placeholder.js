
    /**
     * @name placeHolder
     * @class 璺ㄦ祻瑙堝櫒placeHolder,瀵逛簬涓嶆敮鎸佸師鐢焢laceHolder鐨勬祻瑙堝櫒锛岄�杩噕alue鎴栨彃鍏pan鍏冪礌涓ょ鏂规妯℃嫙
     * @param {Object} obj 瑕佸簲鐢╬laceHolder鐨勮〃鍗曞厓绱犲璞�
     * @param {Boolean} span 鏄惁閲囩敤鎮诞鐨剆pan鍏冪礌鏂瑰紡鏉ユā鎷焢laceHolder锛岄粯璁ゅ�false,榛樿浣跨敤value鏂瑰紡妯℃嫙
     */
    //鎵ц
	jQuery(function(){
		jQuery(':input[placeholder]').each(function(index, element) {
            placeHolder(element,true);
        });
		jQuery(':input[placeholder]').on('blur', function() {
            if ($(this).val() == '') {
                $(this).parent().find("span.wrap-placeholder").css('display', 'block');
            };
        });
		jQuery(':input[placeholder]').on('keyup', function() {
            if ($(this).val() != '') {
                $(this).parent().find("span.wrap-placeholder").css('display', 'none');
            }else{
                $(this).parent().find("span.wrap-placeholder").css('display', 'block');
            };
        });
		jQuery(':input[placeholder]').on('paste', function() {
            $(this).parent().find("span.wrap-placeholder").css('display', 'none');
        });
		jQuery(':input[placeholder]').on('change', function() {
            if ($(this).val() == '') {
                $(this).parent().find("span.wrap-placeholder").css('display', 'block');
            }else{
                $(this).parent().find("span.wrap-placeholder").css('display', 'none');
            };
        });
    });
	  

      function placeHolder(obj, span) {
        if (!obj.getAttribute('placeholder')) return;
        var imitateMode = span===true?true:false;
        var supportPlaceholder = 'placeholder' in document.createElement('input');
        if (!supportPlaceholder) {
            var defaultValue = obj.getAttribute('placeholder');
            var type = obj.getAttribute('type');
            if (!imitateMode) {
                obj.onfocus = function () {
                    (obj.value == defaultValue) && (obj.value = '');
                    obj.style.color = '';
                }
                obj.onblur = function () {
                    if (obj.value == defaultValue) {
                        obj.style.color = '';
                    } else if (obj.value == '') {
                        obj.value = defaultValue;
                        obj.style.color = '#ACA899';
                    }
                }
                obj.onblur();
            } else {
                var placeHolderCont = document.createTextNode(defaultValue);
               
                var oWrapper = document.createElement('span');
                oWrapper.style.cssText = 'position:absolute; color:#ACA899; display:inline-block; overflow:hidden;';
                oWrapper.className = 'wrap-placeholder';
                oWrapper.style.fontFamily = getStyle(obj, 'fontFamily');
                oWrapper.style.fontSize = getStyle(obj, 'fontSize');
                oWrapper.style.marginLeft = parseInt(getStyle(obj, 'marginLeft')) ? parseInt(getStyle(obj, 'marginLeft')) + 3 + 'px' : 3 + 'px';
                
                if ((window.location.href).indexOf('orderDetail.shtml')>-1) 
                	oWrapper.style.marginTop = 3 + 'px';
				else
					oWrapper.style.marginTop = parseInt(getStyle(obj, 'marginTop'))!=0 ? getStyle(obj, 'marginTop'): 0 + 'px';
                oWrapper.style.paddingLeft = getStyle(obj, 'paddingLeft');
//                oWrapper.style.width = ((obj.offsetWidth - parseInt((getStyle(obj, 'marginLeft')=="auto"?0:(getStyle(obj, 'marginLeft')))))==0?100:(obj.offsetWidth - parseInt((getStyle(obj, 'marginLeft')=="auto"?0:(getStyle(obj, 'marginLeft')))))<0?100:(obj.offsetWidth - parseInt((getStyle(obj, 'marginLeft')=="auto"?0:(getStyle(obj, 'marginLeft')))))==0?100:(obj.offsetWidth - parseInt((getStyle(obj, 'marginLeft')=="auto"?0:(getStyle(obj, 'marginLeft')))))) + 'px';
                oWrapper.style.height = obj.offsetHeight==0?34:obj.offsetHeight + 'px';
                oWrapper.style.lineHeight = obj.nodeName.toLowerCase()=='textarea'? '':(obj.offsetHeight==0?34:obj.offsetHeight) + 'px';
                oWrapper.appendChild(placeHolderCont);
                obj.parentNode.insertBefore(oWrapper, obj);
                oWrapper.onclick = function () {
                    obj.focus();
                };
				//缁戝畾input鎴杘npropertychange浜嬩欢,ie9涓垹闄ゆ椂鏃犳硶瑙﹀彂姝や簨浠�
                if (typeof(obj.oninput)=='object') {
                    obj.addEventListener("input", changeHandler, false);
                    obj.onpropertychange = changeHandler;
                    obj.onkeyup = delHandler;
                } else {
                    obj.onpropertychange = changeHandler;
                    obj.onkeyup = delHandler;
                }
                function changeHandler() {
                    oWrapper.style.display = obj.value != '' ? 'none' : 'inline-block';
                }
            	function delHandler(e){//鐩戝惉del銆乥ackspace銆乧trl+x
            		var e = e || window.event;
            		if(e.keyCode == 8 || e.keyCode == 46 || (event.ctrlKey&&e.keyCode == 88)){
            			oWrapper.style.display = obj.value != '' ? 'none' : 'inline-block';
            		}
            	}
                /**
                 * @name getStyle
                 * @class 鑾峰彇鏍峰紡
                 * @param {Object} obj 瑕佽幏鍙栨牱寮忕殑瀵硅薄
                 * @param {String} styleName 瑕佽幏鍙栫殑鏍峰紡鍚�
                 */
                function getStyle(obj, styleName) {
                    var oStyle = null;
                    if (obj.currentStyle)
                        oStyle = obj.currentStyle[styleName];
                    else if (window.getComputedStyle)
                        oStyle = window.getComputedStyle(obj, null)[styleName];
                    return oStyle;
                }
            }
        }
    }