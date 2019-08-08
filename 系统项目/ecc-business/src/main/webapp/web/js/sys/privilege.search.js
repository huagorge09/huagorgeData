$(function() {
    //这里是检索部分
	WASP_WIDGET.triggerSelectOnManagerIdNonStyle("q-empName",{multiple:false});//员工姓名
	
	 //注册清空事件
    WASP_WIDGET.registerResetClearEvent();
   
    $('#q-roleType').select2({ placeholder: '角色类型'});

});

