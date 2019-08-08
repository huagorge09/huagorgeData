/**
 * 角色管理 页面脚本
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";
var PRIVILEGE_PATH = "";

function setPath(path,basePath,privilegePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
	PRIVILEGE_PATH = privilegePath;
}

var roleFunc = {
	getViewHtml : function(text, options, rData){
		var roleId = rData["roleId"];
		var html = "<a href=\"javascript:roleFunc.roleUpdateFunc('"+roleId+"','detail')\" class=\"showunderline\">" + text +"</a>";
 		return html;
	},
	roleUpdateFunc :function(roleId,method){
		var actionUrl = PRIMARY_PATH +"/roleOperationView.do?method="+method+"&roleId="+roleId;
		openDialog(actionUrl);
	},
	roleAddView : function(method){
		var actionUrl = PRIMARY_PATH +"/roleOperationView.do?method="+method;
		openDialog(actionUrl);
	},
	updatePrivilegeAuthRel : function(){
		var actionUrl = PRIVILEGE_PATH + "/updatePrivilegeAuthRel.do";
		$.ajax({
			 url: actionUrl,
			 type : "POST",
			 cache: false,
			 async:false,
			 success : function (data)
			 {
				 ctools.alert("更新成功","","success")
			 },
			 error : function(textStatus, errorThrown) {
				 ctools.alert("系统ajax交互错误: " + textStatus,"","error");  
			 }
		 });
	}
};

var $roleList = $('#roleList');

$(document).ready(function(){
	$('#q-empId').select2();
    WASP_WIDGET.triggerEmployeeSelect("q-empId",false);
    //注册清空事件
    WASP_WIDGET.registerResetClearEvent();
    
	$roleList.jqGrid({
        url: PRIMARY_PATH+'/roleListPage.do',
        caption: '角色维护列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
        datatype: "json",
        colNames: ["id","角色名称","创建人id","创建人","创建时间","操作"],
        colModel: [
            { name: 'roleId', index: 'roleId', width: 50, align:'left', resizable:true, hidden: true, key: true, sortable: false },
            { name: 'roleName', index: 'roleName', width: 100, align:'left', resizable:true, hidden: false, key: false, sortable: false,formatter:roleFunc.getViewHtml},
            { name: 'createId', index: 'createId', width: 50, align:'left', resizable:true, hidden: true, key: false, sortable: false },
            { name: 'createName', index: 'createName', width: 100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'createTime', index: 'createTime', width: 100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'option', index: 'option', width: 70, align:'left', resizable:true, resizable: true, sortable: false }
        ],
        rowNum: 20,
        rowList: [20, 30, 50],
        rownumbers: true,
        rownumWidth: 50,
        prmNames: {
        	        search: "search", 
        	        page: "pageNo",
        	        rows: "limit" 
        	       },
        height: 'auto',
        width: false,
        autowidth:true,
        shrinkToFit:true,
        editurl: '',
        viewrecords: true,
        cellEdit: false,
        shrinkToFit: true,
        grouping: false,
        jsonReader: {
            root: "items", //结果集
            records: "total", //总记录数 
            total: "pageCount", //总页数
            page: "pageNo", //当前页 
            repeatitems: false // (4) 
        },
        multiselect: false,
        pager: "#rolePage",
        viewrecords: true,
        hidegrid: false,
		subGrid: false,
		gridComplete: function() {
			var ids = $roleList.jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){
				var id = ids[i];					 						    
				var rowData = $roleList.jqGrid('getRowData', id);	
				var roleId = rowData.roleId;
				
				//按钮置灰
				$(".permissionBtn a").attr("disabled", "disabled");
                $(".permissionBtn a").removeAttr("onclick");
				//初始化 新增角色 修改 
                $("#roleUpdateBtn a").removeAttr("disabled");
            	$("#roleUpdateBtn a").attr("onClick", "roleFunc.roleUpdateFunc('"+roleId+"','update');");
            	
            	var rub = $("#roleUpdateBtn").html();
            	
            	$roleList.jqGrid('setRowData',ids[i],{option: rub });
			}
		}
    });

    $roleList.navGrid('#rolePage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
    $roleList.jqGrid('setFrozenColumns');
    jqGridResize($roleList);
    
});

function queryByCondtion(flag){
	var roleName = $("#q-roleName").val();
	var empId = $("#q-empId").val();
	
    var postData = $roleList.jqGrid("getGridParam", "postData");
    $.extend(postData,{
    	'sp[roleName]': roleName,
    	'sp[empId]': empId
    });
    if (flag) {
    	$roleList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	$roleList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

//对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function(){
    var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
    toggleFullScreen(document.documentElement);
    //全屏的时候将几个模态框放到下面去
    $('.modal[role="dialog"]').appendTo($wrapper);
});