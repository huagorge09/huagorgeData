$(function() {
	 //注册清空事件
    WASP_WIDGET.registerResetClearEvent();
    bindselect2();

});

function bindselect2(){
	 $('#q-state').select2({
	        placeholder: '状态' 
	    });
}