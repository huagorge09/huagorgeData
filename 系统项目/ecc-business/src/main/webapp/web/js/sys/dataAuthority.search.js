$(function() {
	//绑定select2 项目经理 控件
	WASP_WIDGET.triggerSelectOnManagerIdNonStyle("q-empId",{multiple : false});
	
	$('#q-roleType').select2({placeholder: '资源类型'});
    
    //注册清空事件
    WASP_WIDGET.registerResetClearEvent();


});