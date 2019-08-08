/*系统公共的控件、组件 调用的JS*/
var WASP_WIDGET = {
	// 初始化select的值
	initializeSelectVal : function(selId, selVal, selText) {
		var multiple = $('#' + selId).attr("multiple");
		selVal = selVal || $('#' + selId).attr("val");
		selText = selText || $('#' + selId).attr("text");
		if ((selVal != undefined && selVal != "") && (selText != undefined && selText != "")) {
			if (multiple) {// 多选
				var vals = selVal.split(",");
				var txts = selText.split(",");
				if (vals.length != txts.length) {
					return;
				}
				for (var i = 0; i < vals.length; i++) {
					if(fiterRepeat(vals[i])){
						var option = $('<option selected="selected" value="' + vals[i] + '" >' + txts[i] + '</option>');
						$('#' + selId).append(option).trigger('change');
					}
				}
			} else {
				var option = $('<option selected="selected" value="' + selVal + '" >' + selText + '</option>');
				$('#' + selId).append(option).trigger('change');
			}
		}
		//判断是否存在重复的值
		function fiterRepeat(val){
			var result =true;
			if($('#' + selId).find("option").length != 0){
				$('#' + selId).find("option").each(function(){
					if($(this).val()===val)
						result = false;
				});
			}
			return result;
		}
	},
	
	triggerSelectOnBankBase : function(id){
		$('#' + id).select2({
			language : "zh-CN",
			multiple : false,
			ajax : {
				url :  WIDGET_PATH_PREFIX + "/bank/getMatchBankBaseInfo.xhtml",
				type : "POST",
				data : function(params) {
					var query;
					if (params.term == null || params.term == '' || params.term == 'undefind') {
						query = {
								bankName : ""
						};
					} else {
						query = {
								bankName : params.term
						};
					}
					return query;
				},
				dataType : "json",
				processResults : function(data) {
					var results = $.map(data, function(obj) {
						return {
							id : obj.bnkNo,
							text : obj.bnkNm
						}
					});
					return {
						results : results
					};
				}
			},
			cache : true,
			delay : 3000,
			templateSelection : function(obj) {
				console.info(obj);
				return $('<span data-html="true" >' + obj.text + '</span>').tooltip();
			}
		});
	},
	
	triggerSelectOnDsBankBase : function(id){
		$('#' + id).select2({
			language : "zh-CN",
			multiple : false,
			ajax : {
				url :  WIDGET_PATH_PREFIX + "/bank/queryDsBankBase.xhtml",
				type : "POST",
				data : function(params) {
					var query;
					if (params.term == null || params.term == '' || params.term == 'undefind') {
						query = {
								bankName : ""
						};
					} else {
						query = {
								bankName : params.term
						};
					}
					return query;
				},
				dataType : "json",
				processResults : function(data) {
					var results = $.map(data, function(obj) {
						return {
							id : obj.bnkNo,
							text : obj.bnkNm
						}
					});
					return {
						results : results
					};
				}
			},
			cache : true,
			delay : 3000,
			templateSelection : function(obj) {
				console.info(obj);
				return $('<span data-html="true" >' + obj.text + '</span>').tooltip();
			}
		});
	},
	
	
	triggerSelectOnMenu : function(menuId) {
		var _placeholder = $('#' + menuId).attr("placeholder");
		if (_placeholder == null || _placeholder == '' || _placeholder == 'undefind') {
			_placeholder = "菜单";
		}
		$('#' + menuId).select2({
			language:"zh-CN",
			placeholder : _placeholder,
			multiple : false,
			ajax : {
				url : WIDGET_PATH_PREFIX + "queryMatchMenu.do",
				type : "POST",
				data : function(params) {
					var query;
					if (params.term == null || params.term == '' || params.term == 'undefind') {
						query = {
							menuNM : ""
						};
					} else {
						query = {
							menuNM : params.term
						};
					}
					return query;
				},
				dataType : "json",
				processResults : function(data) {
					var results = $.map(data, function(obj) {
						return {
							id : obj.menuId,
							text : obj.name
						}
					});
					return {
						results : results
					};
				}
			},
			cache : true,
			delay : 3000,
			templateSelection : function(obj) {
				console.info(obj);
				return $('<span data-html="true" >' + obj.text + '</span>').tooltip();
			}
		});
	},
	// 项目经理 返回无样式封装
	// modify add weicb :支持参数决定是否多选
	triggerSelectOnManagerIdNonStyle : function(managerId, option) {
		var _placeholder = $('#' + managerId).attr("placeholder");
		if (_placeholder == null || _placeholder == '' || _placeholder == 'undefind') {
			_placeholder = "项目经理";
		}
		var _multiple = true;
		if (option) {
			if (null != option.multiple && _placeholder != '' && option.multiple != 'undefind') {
				_multiple = option.multiple;
			}
		}
		$('#' + managerId).select2({
			language:"zh-CN",
			placeholder : _placeholder,
			multiple : _multiple,
			ajax : {
				url : WIDGET_PATH_PREFIX + "queryMatchUserInfoList.do",
				type : "POST",
				delay : 400,
				data : function(params) {
					var query;
					if (params.term == null || params.term == '' || params.term == 'undefind') {
						query = {
							assistantNM : "",
							duplicate : true
						};
					} else {
						query = {
							assistantNM : params.term,
							duplicate : true
						};
					}
					return query;
				},
				dataType : "json",
				processResults : function(data) {
					var results = $.map(data, function(obj) {
						return {
							id : obj.empID,
							text : obj.empName
						}
					});
					return {
						results : results
					};
				}
			},
			cache : true
		});
	},
	// 拓展文本域placeholder，绑定Summernote控件 modify by anzy 2016-05-18
	triggerSummernoteStyle : function(descId,option) {
		var default_option = {
				allowFileManager : false,
				minWidth:670,
//				filterMode:false,
//				readonlyMode:true, //只读 
				afterCreate : function() {
					this.sync();
				},
				afterChange:function(){
					this.sync();
				},
				afterBlur:function(){
					this.sync();
					if(option && 'object' === typeof option && option.isValidate)
						$('#' + descId).valid &&  $('#' + descId).valid();
					
				},
				cssPath : '/web/js/plugins/code/prettify.css',
				fileManagerJson : '/web/js/jsp/file_manager_json.jsp',
				uploadJson : '/service/jsp/upload_json.jsp'
		};//默认参数
		if(option && 'object' === typeof option)
			$.extend(default_option, option);//默认参数和自定义参数处理
		KindEditor.ready(function(K) {
			LzEditor = K.create("#"+descId, default_option);
		});
	},
	// 初始化文本域，绑定Summernote控件  modify dingxq 扩展placeholder
	triggerSummernote : function(descId,option) {
		var default_option = {
				allowFileManager : false,
				minWidth:670,
//				filterMode:false,
//				readonlyMode:true, //只读 
				afterCreate : function() {
					this.sync();
				},
				afterChange:function(){
					this.sync();
				},
				afterBlur:function(){
					this.sync();
					if(option && 'object' === typeof option && option.isValidate)
						$('#' + descId).valid &&  $('#' + descId).valid();
				},
				cssPath : '/web/js/plugins/code/prettify.css',
				fileManagerJson : '/service/jsp/file_manager_json.jsp',
				uploadJson : '/service/jsp/upload_json.jsp'
		};//默认参数
		if(option &&'object' === typeof option)
			$.extend(default_option, option);//默认参数和自定义参数处理
		KindEditor.ready(function(K) {
			LzEditor = K.create("#"+descId, default_option);
		});
	},
	triggerKindEditor : function(descId,isvalid) {
		KindEditor.ready(function (K) {
			var editor = K.create('#'+descId, {
				allowFileManager : false,
				//autoHeightMode : true,
				minWidth:670,
				afterCreate : function() {
					this.sync();
				},
				afterChange:function(){
					this.sync();
					var $this = $('#'+descId);
					if(isvalid){
						$this.valid && $this.valid();
					}
				},
				afterBlur:function(){
					this.sync();
					var $this = $('#'+descId);
					if(isvalid){
						$this.valid && $this.valid();
				    }
				},
				fileManagerJson : '/service/jsp/file_manager_json.jsp',
				uploadJson : '/service/jsp/upload_json.jsp'
			});
			
		});
	},
	// 绑定ICheck样式 modify by weicb：支持jquery选择器个性化绑定ifChecked、ifUnchecked事件
	//  modify by weicb：2016-06-13增加参数noValid 是否不需要校验 不需要表单校验传入true
	triggerICheck : function(selector,funcChecked,funcUnchecked,noValid) {
		if (selector && typeof selector==="string") {//支持jquery选择器个性化绑定
			$(selector).iCheck({
				checkboxClass : 'icheckbox_square-green',
			}).on('ifChecked', function() {
				if(funcChecked && typeof funcChecked==="function")
					funcChecked(this.value);
				if(!noValid)
					$(this).valid && $(this).valid();
			}).on('ifUnchecked', function() {
				if(funcUnchecked && typeof funcUnchecked==="function")
					funcUnchecked(this.value);
				if(!noValid)
					$(this).valid && $(this).valid();
			});
		}else{//不传参数默认绑定页面所有的checkbox
			$('[type=checkbox]').iCheck({
				checkboxClass : 'icheckbox_square-green',
			}).on('ifChecked', function() {
				if(!noValid)
					$(this).valid && $(this).valid();
			}).on('ifUnchecked', function() {
				if(!noValid)	
					$(this).valid && $(this).valid();
			});
		}
	},
	// 绑定IRadio样式 modify by weicb：支持jquery选择器个性化绑定ifChecked、ifUnchecked事件
	//  modify by weicb：2016-06-13增加参数noValid 是否不需要校验 不需要表单校验传入true
	triggerIRadio : function(selector,funcChecked,funcUnchecked,noValid) {
		if (selector && typeof selector==="string") {//jquery选择器个性化
			$(selector).iCheck({
				radioClass : 'iradio_square-green',
			}).on('ifChecked', function() {
				if(funcChecked && typeof funcChecked==="function")
					funcChecked(this.value);
				if(!noValid)
					$(this).valid && $(this).valid();
			}).on('ifUnchecked', function() {
				if(funcUnchecked && typeof funcUnchecked==="function")
					funcUnchecked(this.value);
				if(!noValid)
					$(this).valid && $(this).valid();
			});
		}else{//不传参数默认绑定页面所有的radio
			$('[type=radio]').iCheck({
				radioClass : 'iradio_square-green',
			}).on('ifChecked', function() {
				if(!noValid)
					$(this).valid && $(this).valid();
			}).on('ifUnchecked', function() {
				if(!noValid)
					$(this).valid && $(this).valid();
			});
		}
	},
	// 绑定日期控件 YYYY-MM-DD样式
	triggerDateStyleWithYMD : function(dateId) {
		$('#' + dateId).datepicker({
			weekStart: 1,
			format: "yyyy-mm-dd",
		    todayBtn: "linked",
		    clearBtn: true,
		    language: "zh-CN",
		    autoclose: true,
		    todayHighlight: true
	    });
	},
	// 绑定日期控件 YYYY-MM-DD样式
	triggerDateStyleWithYMDAndSlash : function(dateId) {
		$('#' + dateId).datepicker({
			weekStart: 1,
			format: "yyyy/mm/dd",
		    todayBtn: "linked",
		    clearBtn: true,
		    language: "zh-CN",
		    autoclose: true,
		    todayHighlight: true
	    });
	},
	// 绑定日期控件 YYYY-MM-DD样式
	triggerDateStyleWithYMD_YMD : function(dateId) {
		$('#' + dateId).datepicker({
			weekStart: 1,
			format: "yyyy/mm/dd - yyyy/mm/dd",
		    todayBtn: "linked",
		    clearBtn: true,
		    language: "zh-CN",
		    autoclose: true,
		    todayHighlight: true
	    });
	},
	// 绑定日期控件 YYYY-MM-DD样式
	triggerYMDDateStyle : function(selector, option) {
		var todayBtn = "linked";
		if (option && option.todayBtn !== null) {
			todayBtn = option.todayBtn;
		}
		var todayHighlight = true;
		if (option && option.todayHighlight !== null) {
			todayHighlight = option.todayHighlight;
		}
		
		$(selector).datepicker({
			weekStart: 1,
			format: option.format,
			forceParse: option.forceParse,
			todayBtn: todayBtn,
			clearBtn: true,
			language: "zh-CN",
			autoclose: true,
			todayHighlight: todayHighlight,
		    daysOfWeekDisabled: option.daysOfWeekDisabled || ""
		});
	},
	// 绑定日期控件 YYYY-MM样式
	triggerDateStyleWithYM : function(dateId) {
		$('#' + dateId).datepicker({
			minViewMode: 1,
	        format: "yyyy-mm",
	        todayBtn: "linked",
	        clearBtn: true,
	        language: "zh-CN",
	        autoclose: true,
	        todayHighlight: true  	
	    });
	},
	// 绑定日期控件 YYYY样式
	triggerDateStyleWithY : function(dateId) {
		$('#' + dateId).datepicker({
			minViewMode: 1,
	        format: "yyyy",
	        todayBtn: "linked",
	        clearBtn: true,
	        language: "zh-CN",
	        autoclose: true,
	        todayHighlight: true  	
	    });
	},
	// 绑定日期控件 YYYY样式
	triggerDateStyleWithYYYY : function(dateId) {
		$('#' + dateId).datepicker({
			startDate: '1990',
			minViewMode: 2,
			startView: 2,
	        format: "yyyy",
	        todayBtn: "linked",
	        clearBtn: true,
	        language: "zh-CN",
	        autoclose: true,
	        todayHighlight: true  	
	    });
	},
	
	// 绑定日期时间控件
	triggerDateTimeStyle : function(dateId) {
		$('#' + dateId).datetimepicker({
			weekStart: 1,
			format: "yyyy-mm-dd hh:ii:ss",
		    todayBtn: "linked",
		    clearBtn: true,
		    language: "zh-CN",
		    autoclose: true,
		    todayHighlight: true,
		    fontAwesome: true
	    });
	},
	
	// 绑定日期范围控件
	triggerDateRangeStyle : function(dateId) {
		$('#' + dateId).daterangepicker({
			autoUpdateInput : false,
			alwaysShowCalendars : true,
			ranges : {
				'今天' : [ moment(), moment() ],
				'昨天' : [ moment().subtract(1, 'days'), moment().subtract(1, 'days') ],
				'前一周' : [ moment().subtract(6, 'days'), moment() ],
				'当月' : [ moment().startOf('month'), moment().endOf('month') ],
				'截止今天' : [ moment('1990/01/01', "YYYY/MM/DD"), moment() ],
				'从今天起' : [ moment(), moment('2200/01/01', "YYYY/MM/DD") ]
			},
			locale : {
				format : 'YYYY/MM/DD',
				separator : ' - ',
				applyLabel : '确定',  
				cancelLabel : '取消',
				customRangeLabel : '自定义'
			}
		});
		$('#' + dateId).on('apply.daterangepicker', function(ev, picker) {
			$(this).val(picker.startDate.format('YYYY/MM/DD') + ' - ' + picker.endDate.format('YYYY/MM/DD'));
		});
		$('#' + dateId).on('cancel.daterangepicker', function(ev, picker) {
			$(this).val('');
		});
	},
	
	// 注册清空事件，必须是button[type=reset]
	registerResetClearEvent : function() {
		$('button[type=reset]').on('click', function() {
			var $parent = $(this).closest('form, .form-col-panel, .form-multi-col-panel');
			var $select = $parent.find('.use-select2').val(null);
			$parent.find('input, textarea, select').val(null);
			setTimeout(function() {
				$select.trigger('change.select2');
			}, 0);
		});

	},

	//通用上传附件
	triggerSelectOnCommonDicts : function(element,option) {
		var _placeholder=element.attr("placeholder");
		if(_placeholder== null || _placeholder== ''
			|| _placeholder == 'undefind'){
			_placeholder="数据字典";
		}
		element.select2(
				{
					language : "zh-CN",
					placeholder : _placeholder,
					multiple : false,
					ajax : {
						url : WIDGET_PATH_PREFIX + "queryMatchCommonDictList.do",
						type : "POST",
						data : function(params) {
							var query={};
							if(option){//自定义参数条件
								query=option;
								if(option.txt_dctFathType){
									query.dctFathType=$("#"+option.txt_dctFathType).val();
								}
							}
							if (params.term == null || params.term == ''
								|| params.terdictm == 'undefind') {//用户输入的查询条件
								query.dctName = "";
							} else {
								query.dctName = params.term;
							}
							return query;
						},
						dataType : "json",
						processResults : function(data) {
							var results = $.map(data, function(obj) {
								return {
									id : obj.dctValue,
									text : obj.dctName
								}
							});
							return {
								results : results
							};
						}
					},
					cache : true,
					delay : 3000
					
				});
	},
	/**
	 * 查询所有对接银行列表
	 */
	triggerBnkBaseListSelect : function(elementId, multiple, placeholder) {
		$("#" + elementId).select2({
			language : "zh-CN",
			placeholder: placeholder || '请选择',
			multiple : multiple || false,
			ajax : {
				url : WIDGET_PATH_PREFIX + "queryMatchBankBaseInfoList.do",
				delay : 250,
				type : "POST",
				data : function(params) {
					var query = {};
					query.limit = 20;
					if(!!params.term){
						query.bankName = params.term;
					}
					return query;
				},
				dataType : "json",
				processResults : function(data) {
					var results = $.map(data, function(obj) {
						return {
							id : obj.bnkNo,
							text : obj.bnkNm
						};
					});
					return {
						results : results
					};
				}
			},
			cache : false,
			templateSelection : function(obj) {
				return $('<span data-html="true" >' + obj.text + '</span>').tooltip();
			},
			templateResult : function(obj) {
				return $('<span data-html="true" >' + obj.text + '</span>').tooltip();
			}
		});
	},
	/**
	 * 查询所有产品基金列表
	 */
	triggerFundInfoListSelect : function(elementId, multiple, placeholder) {
		$("#" + elementId).select2({
			language : "zh-CN",
			placeholder: placeholder || '请选择',
			multiple : multiple || false,
			ajax : {
				url : WIDGET_PATH_PREFIX + "queryMatchFundInfoList.do",
				delay : 250,
				type : "POST",
				data : function(params) {
					var query = {};
					query.limit = 20;
					if (params.term != null || params.term != '' || params.term != 'undefind') {
						query.fundNm = params.term
					}	
					return query;
				},
				dataType : "json",
				processResults : function(data) {
					var results = $.map(data, function(obj) {
						return {
							id : obj.fundId,
							text : obj.fundNm
						};
					});
					return {
						results : results
					};
				}
			},
			cache : false,
			templateSelection : function(obj) {
				return $('<span data-html="true" >' + obj.text + '</span>').tooltip();
			},
			templateResult : function(obj) {
				return $('<span data-html="true" >' + obj.text + '</span>').tooltip();
			}
		});
	},
	/**
	 * 查询所有产品基金列表
	 */
	triggerParamListSelect : function(elementId, multiple, placeholder) {
		$("#" + elementId).select2({
			language : "zh-CN",
			placeholder: placeholder || '请选择',
			multiple : multiple || false,
			ajax : {
				url : WIDGET_PATH_PREFIX + "queryMatchParamList.do",
				delay : 250,
				type : "POST",
				data : function(params) {
					var query = {};
					var param = $(this).attr("param");
					if(!!param){
						query = JSON.parse(param);
					}
					query.limit = 20;
					if (params.term != null || params.term != '' || params.term != 'undefind') {
						query.pmnm = params.term
					}	
					return query;
				},
				dataType : "json",
				processResults : function(data) {
					var results = $.map(data, function(obj) {
						return {
							id : obj.PMCO,
							text : obj.PMNM
						};
					});
					return {
						results : results
					};
				}
			},
			cache : false,
			templateSelection : function(obj) {
				return $('<span data-html="true" >' + obj.text + '</span>').tooltip();
			},
			templateResult : function(obj) {
				return $('<span data-html="true" >' + obj.text + '</span>').tooltip();
			}
		});
	},
	/**
	 * 查询参数表
	 * 展示值和名称
	 * value name
	 */
	triggerParamListSelectMultName : function(elementId, multiple, placeholder) {
		$("#" + elementId).select2({
			language : "zh-CN",
			placeholder: placeholder || '请选择',
			multiple : multiple || false,
			ajax : {
				url : WIDGET_PATH_PREFIX + "queryMatchParamList.do",
				delay : 250,
				type : "POST",
				data : function(params) {
					var query = {};
					var param = $(this).attr("param");
					if(!!param){
						query = JSON.parse(param);
					}
					query.limit = 20;
					if (params.term != null || params.term != '' || params.term != 'undefind') {
						query.pmnm = params.term
					}	
					return query;
				},
				dataType : "json",
				processResults : function(data) {
					var results = $.map(data, function(obj) {
						return {
							id : obj.PMCO,
							text : obj.PMNM
						};
					});
					return {
						results : results
					};
				}
			},
			cache : false,
			templateSelection : function(obj) {
				return $('<span data-html="true" >'+ obj.id +' ' + obj.text + '</span>').tooltip();
			},
			templateResult : function(obj) {
				return $('<span data-html="true" >'+ obj.id +' ' + obj.text + '</span>').tooltip();
			}
		});
	},
	/**
	 * KM 所有的在职star员工
	 * elementId: select id
	 * multiple: boolean 是否可多选
	 */
	triggerEmployeeSelect : function(elementId, multiple, placeholder) {
		$("#" + elementId).select2({
			language : "zh-CN",
			placeholder: placeholder || '请选择',
			multiple : multiple || false,
			ajax : {
				url : WIDGET_PATH_PREFIX + "queryMatchKMEmployeeInfos.do",
				delay : 250,
				type : "POST",
				data : function(params) {
					var query;
					if (params.term == null || params.term == '' || params.term == 'undefind') {
						query = {
							limit : 20
						};
					} else {
						query = {
							empName : params.term
						};
					}
					return query;
				},
				dataType : "json",
				processResults : function(data) {
					var results = $.map(data, function(obj) {
						return {
							id : obj.empID,
							text : obj.empName
						};
					});
					return {
						results : results
					};
				}
			},
			cache : true,
			templateSelection : function(obj) {
				return $('<span data-html="true" >' + obj.text + '</span>').tooltip();
			},
			templateResult : function(obj) {
				return $('<span data-html="true" >' + obj.text + '</span>').tooltip();
			}
		});
	}
};

$('.btn-collapse').click(function() {
    var button = $(this).find('i');
    button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
});

/**
 * 获取日期范围控件值数组，数组第一个值为开始日期，数组第二个值为结束日期
 * @param dateId
 */
$.fn.getDateRangeValue = function() {
	var local = {
		rangeseparator : ' - ',// 日期范围中两个日期的分割符
		dateseparator : '-'// 取值后单个日期各元素之间的分隔符
	};
	var dateVal = $(this).val();
	var data = [];
	if (dateVal) {
		var dates = dateVal.split(local.rangeseparator);
		data[0] = $.trim(dates[0].replace(/\//g, local.dateseparator));// 开始日期
		if (dates.length == 2) {
			data[1] = $.trim(dates[1].replace(/\//g, local.dateseparator));// 结束日期
		} else {
			data[1] = "";
		}
	} else {
		data[0] = "";
		data[1] = "";
	}
	return data;
};
var extendsAttr = new Map();
function loadExtendsAttr(key,checked){
	if (!checked) {
		checked = '';
	}
	var attrs;
	if (extendsAttr.isEmpty()) {
		$.ajax({
			url:WIDGET_PATH_PREFIX+"queryBusinessExtendsAttr.xhtml",
			dataType:'json',
			type:'get',
			async:false,
			success:function(data){
				if (data) {
					$.each(data,function(k,v){
						s = data; 
						extendsAttr.put(k, v);
					});
				}
			}
		});
	}
	attrs = extendsAttr.get(key);
	
	if (attrs) {
		var selectHtml = '<select style="width: 25%;float: left;" class="form-control use-select2 extendsAttr">';
		$.each(attrs,function(k,v){
			if (checked.indexOf((k+',')) == -1) {
				selectHtml += '<option value="'+k+'">'+v+'</option>';
			}
			/*else{
				selectHtml += '<option style="display:none;" value="'+k+'">'+v+'</option>';
			}*/
		});
		selectHtml +='</select>';
		
		return selectHtml;
	}
}

