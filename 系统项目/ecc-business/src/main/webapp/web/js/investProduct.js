var grid_data = [
    {id : "00001",type : "退货出库",pay : "1000",name : "abc",text : "ccc"},
	{id : "00002",type : "退货出库",pay : "1000",name : "abc",text : "aaa"},
	{id : "00003",type : "退货出库",pay : "1040.06",name : "abc",text : "ddd"}];
var grid_selector = "#investProductList";
var pager_selector = "#grid-pager";
$(document).ready(function() {
	//注册清空事件
    WASP_WIDGET.registerResetClearEvent();
	WASP_WIDGET.triggerDateStyleWithYMD("createTime");
	$("#investProductList").jqGrid({
		data : grid_data, // 当 datatype 为"local" 时需填写
		datatype : "local", // 数据来源，本地数据（local，json,jsonp,xml等）
		height : 150, // 高度，表格高度。可为数值、百分比或'auto'
		colNames : [ '出库单号', '出库类型', '总金额', '申请人（单位）', '备注' ],
		colModel : [
		            { name : 'id',index : 'id',key : true,width : 100,sortable : false},
		            { name : 'type',index : 'type',width : 200,sortable : false },
		            { name : 'pay', index : 'pay', width : 60,sortable : false},
		            { name : 'name',index : 'name',width : 150,sortable : false},
		            { name : 'text',index : 'text',width : 250,sortable : false}
		           ],
		caption : "测试列表", // 表名
		autowidth : true	// 自动宽
	});
	$.ajax({
        url : path + "returnList.xhtml",
        type : "POST",
        dataType : "json",
        success : function(data) {
        	for (var i = 0; i < data.length; i++) {
				//alert(data[i]);
			}
        }
    });
	
	$.ajax({
        url : path + "returnJson.xhtml",
        type : "POST",
        dataType : "json",
        success : function(data) {
        	$("#productName").val(data.name);
        }
    });
});
