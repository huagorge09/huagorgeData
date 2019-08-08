$(function() {
	
	//日期选择组件
	WASP_WIDGET.triggerDateStyleWithYMD("q-theDate");
    //注册清空事件
    WASP_WIDGET.registerResetClearEvent();
    $('#q-stat').select2({
        placeholder: '状态',
        minimumResultsForSearch: '-1'
    });
});

