$(function() {
    //这里是检索部分
	WASP_WIDGET.triggerSelectOnManagerIdNonStyle("q-empId",{multiple:false});//产品简称
	
	 //注册清空事件
    WASP_WIDGET.registerResetClearEvent();
    bindRoleType();

});

function bindRoleType(){
	 $('#q-roleType').select2({
	        placeholder: '角色类型' 
	    });
}