/**
 * 产品发起公共操作JS类
 * @author ex-huangch 2016。5.5
 */
var PATH_PREFIX ="";

function setPathPrefix(privilegePath){
	PATH_PREFIX = privilegePath;
}

var WASP_PRIVILEGE = {
	    //修改权限
		privilegeUpdateView: function(roleId) {
			var actionUrl = PATH_PREFIX+"privilegeUpdateView.do?roleId="+roleId;
	    	openDialog(actionUrl);
		},
		//查询角色权限    
	    getViewLink: function(text, options, rData){
			return '<a href="javascript(0);" class="showunderline" data-toggle="modal" data-target="#modal-info" onclick="WASP_PRIVILEGE.showViewPage(\''
			+ rData.roleId  + '\')\">' + text + '</a>';
		},
		showViewPage : function(roleId) {
			var url = PATH_PREFIX+'privilegeDetailView.do?roleId='+roleId;
			openDialog(url);
		}
	};

/**
 *角色维护列
 **/
var $privilegeList = $('#privilegeList');
$(function() {
    $privilegeList.jqGrid({
        url: PATH_PREFIX+'privilegeListPage.do',
        caption: '角色维护列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
        datatype: "json",        
		colNames:["角色ID","员工ID","角色名称","角色类型","权限类型","状态", '操作',''],        
		colModel:[  {name:'roleId',index:'roleId',key:true, hidden:true},
		            {name:'empId',index:'empId', hidden:true},
					{name:'roleName',index:'roleName',resizable:true,align:'left',formatter: WASP_PRIVILEGE.getViewLink},  
					{name:'roleTypeName',index:'roleTypeName',resizable:true,align:'left'},  
					{name:'authorityType',index:'authorityType', resizable:true,align:'left',formatter:'select',
	                	  editoptions: { value: {1:'普通',2:'特殊'}}},
					{name:'statName',index:'statName', resizable:true,align:'left', sortable:false},
					{name:'option',index:'option',  resizable:true,align:'left',sortable:false},
					{name:'roleType',index:'roleType', hidden:true},
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
        pager: "#privilegePage",
        viewrecords: true,
        hidegrid: false,
		gridComplete: function(){
			var ids = $privilegeList.jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){
				var roleId = ids[i];
				var rowData = $privilegeList.jqGrid('getRowData', roleId);
				var se = '<a date-roleid="' + roleId + '" href="#" class="btn btn-link btn-jqgrid" title="修改" data-toggle="modal" onclick="WASP_PRIVILEGE.privilegeUpdateView(\''+roleId+'\');" data-target="#modal-edit"><i class="fa fa-pencil-square-o"></i></a>';
				//角色类型的权限不允许挂资源
				if(rowData.roleType == '1'){
					se="";
				}
				$privilegeList.jqGrid('setRowData',ids[i],{option:se});
			}	
		}
		});
    $privilegeList.navGrid('#privilegePage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
    $privilegeList.jqGrid('setFrozenColumns');
    jqGridResize($privilegeList);
});

/**
 * 通过条件进行搜索
 */
function queryByCondtion(flag) {
	//获取角色名称
	var roleName = $("#q-roleName").val();
	//获取角色类型
	var roleType = $("#q-roleType").val();
	//获取员工姓名
	var empId = $("#q-empName").val();
    var postData = $privilegeList.jqGrid("getGridParam", "postData");
    $.extend(postData, {
        'sp[roleName]':roleName,
        'sp[roleType]':roleType,
        'sp[empId]':empId,
    });
    if (flag) {
    	$privilegeList.trigger("reloadGrid", [{ page: 1 }]); //重新载入Grid表格
    } else {
    	$privilegeList.trigger("reloadGrid"); //重新载入Grid表格
    }
};


//刷新权限控制表
function updateLimit () {
	ctools.confirm("确认刷新权限控制表？",function(){
	var actionUrl  = "/cbp/privilege/privilegeUpdate/executeUpdateLimit.do";
	$.ajax({
		dataType: "json",
		cache: false,
		url:actionUrl, 				
		success: function(data){
                ctools.alert_sweet(data.STATUS,"success","");
		},
		error: function(jqXHR, textStatus, errorThrown){
			ctools.alert_sweet("操作失败 " + textStatus,"error","");
		}
	});	
	},"");
}


//对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function() {
	var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
	toggleFullScreen(document.documentElement);
	// 全屏的时候将几个模态框放到下面去
	$('.modal[role="dialog"]').appendTo($wrapper);
})

