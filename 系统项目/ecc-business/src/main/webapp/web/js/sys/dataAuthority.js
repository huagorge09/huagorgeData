var PATH_PREFIX = "";

function setPathPrefix(path) {
	PATH_PREFIX = path;
}


var $dataAuthorityList = $('#dataAuthorityList');
$(function() {
	$dataAuthorityList.jqGrid({
				url : PATH_PREFIX + 'dataAuthorityListPage.do',
				caption:'数据权限列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',  
				datatype: "local",
				colNames:["用户名称","角色名称","资源名称"],        
				colModel:[  
							{name:'emName',index:'emName', resizable:true,align:'left'},  
							{name:'roleName',index:'roleName', resizable:true,align:'left'},  
							{name:'resourceName',index:'resourceName',resizable:true,align:'left', sortable:false}
						], 
				rowNum : 20,
				rowList : [ 20, 30, 50 ],
				rownumbers : true,
				rownumWidth : 50,
				prmNames : {
					search : "search",
					page : "pageNo", // 当前页
					rows : "limit" // 每页行数
				},
				height : 'auto',
				width : false,
				editurl : '',
				viewrecords : true,
				cellEdit : false,
				shrinkToFit : true,
				grouping : false,
				autowidth : true,
				jsonReader : {
					root : "items", // 结果集
					records : "total", // 总记录数
					total : "pageCount", // 总页数
					page : "pageNo", // 当前页
					repeatitems : false
				},
				multiselect : false,
				pager : "#dataAuthorityPage",
				viewrecords : true,
				hidegrid : false,
			});

	jQuery("#dataAuthorityList").navGrid('#dataAuthorityPage', {edit : false,add : false,del : false,search : false,refreshstate : 'current'});
	$dataAuthorityList.jqGrid('setFrozenColumns');
	jqGridResize($dataAuthorityList);
});

function queryByCondtion (flag){
	var resourceName = $("#q-resourceName").val();
	var roleType = $("#q-roleType").val();
	var empId = $("#q-empId").val();
 
	    var postData = $("#dataAuthorityList").jqGrid("getGridParam", "postData");
	$.extend(postData,{ 
					'sp[resourceName]' : resourceName,
					'sp[roleType]' : roleType,
					'sp[empId]' : empId
	               });
	if (flag) {
		$dataAuthorityList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    }
    else {
    	$dataAuthorityList.trigger("reloadGrid");//重新载入Grid表格
    }        
};

//对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function(){
    var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
    toggleFullScreen(document.documentElement);
    //全屏的时候将几个模态框放到下面去
    $('.modal[role="dialog"]').appendTo($wrapper);
});
