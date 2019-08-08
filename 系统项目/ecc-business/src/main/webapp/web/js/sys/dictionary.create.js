//提交新增
function submitDictionaryAddForm() {
	
	SubmitAndPreventSecond("dictionaryAddForm",null);

}
//更新数据
function submitDictionaryUpdateForm() {

	SubmitAndPreventSecond("dictionaryUpdateForm",null);

}


$(function() {
	
	//初始化select2控件
	$('#dctIsLeaf').select2({ placeholder: '是否叶子'});
	$('#dctStat').select2({ placeholder: '是否生效'});
	//初始化表单校验事件
    initValidateAndSubmit();
	//对select控件绑定验证事件
	triggerValidOnSelectChange('dctIsLeaf','dctStat');

});

//表单校验
function initValidateAndSubmit() {
	$("#dictionaryAddForm,#dictionaryUpdateForm").validate({
		focusCleanup : true,
		rules : {
			dctRootType : {
				required : true,// 非空判断
			},
			dctFathType : {
				required : true,// 非空判断
			},
			dctLeftType : {
				required : true,// 非空判断
			},
			dctName : {
				required : true,// 非空判断
			},
			dctValue : {
				required : true,// 非空判断
			},
			dctSortNo : {
				digits:true //数字判断
			},
			dctIsLeaf : {
				required : true,// 非空判断
			},
			dctStat : {
				required : true,
			},
		},
		messages : {
			dctRootType : {
				required : "请填写字典根类型",
			},
			dctFathType : {
				required : "请填写字典父类型",
			},
			dctLeftType : {
				required : "请填写字典子类型",
			},
			dctName : {
				required : "请填写字典名",
			},
			dctValue : {
				required : "请填写字典值",
			},
			dctSortNo : {
				digits : "请输入整数",
			},
			dctIsLeaf : {
				required : "请选择是否叶子",
			},
			dctStat : {
				required : '请选择是否生效',
			}
		}
	});
	
}


