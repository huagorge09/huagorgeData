var PATH_PREFIX ='/cbp/sys/menu/';

var WASP_MENU = {
		menuUpdateView: function(menuId) {
		    /** 更新菜单 */
			 var actionUrl = PATH_PREFIX+"menuUpdateView.do?menuId="+menuId;
			 openDialog(actionUrl);
		},
		deleteMenu: function(menuId){
			/**删除菜单**/
			ctools.confirm({title : "删除菜单"},function(isConfirm){
				var actionUrl = PATH_PREFIX+"deleteMenu.do?menuId=" + menuId;
				if(isConfirm){
					$.ajax({
						type: 'POST',
						url: actionUrl,
						async: false,
						success: function(data){
							if(data.result=='success'){
								swal("删除成功!", "", "success");
								refresh();
							}else if(data.result=='reject'){
								swal("拒绝删除!", "存在关联的子节点，请先删除子节点再操作", "warning");
							};
						},
						error:function(xhr){
							swal("删除出错!", "", "error");
						}
					});
				}
				
			});
		}	
		
};

/**
 * 刷新父页面
 */
function refresh(){
	try{
		window.parent.location.reload();
	}catch(e){
		try{
			window.opener.location.reload();
		}catch(e){}
	}
}




var $buttonList = $('#buttonList');
$(function() {
	var menuId = $('#menuId').val();
    $buttonList.jqGrid({
        url: PATH_PREFIX+'buttonListPage.do',
        datatype: "json",
        colNames: ["菜单主键", "按钮代码","按钮描述","按钮名称", "菜单链接", "菜单序号", "状态",'操作'],
        colModel: [
            { name: 'menuId', index: 'menuId', width: 20, hidden: false, key: true, sortable: true },
            { name: 'code', index: 'code', width: 20, hidden: false, resizable: true, sortable: false },
            { name: 'desc', index: 'desc', width: 20, hidden: false, resizable: true, sortable: false },
            { name: 'name', index: 'name', width: 20, hidden: false, resizable: true, sortable: false },
            { name: 'url', index: 'url', width: 30, resizable: true, sortable: false },
            { name: 'sequence', index: 'sequence', width: 25, resizable: true, sortable: false },
            { name: 'status', index: 'status', hidden: true },
            { name: 'option', index: 'option', width: 20, resizable: true, sortable: false }
        ],
        rowNum: 50,
        rowList: [50],
        rownumbers: true,
        rownumWidth: 50,
        postData:{
		 	'sp[menuId]': menuId
			},  
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
        viewrecords: true,
        hidegrid: false,
		gridComplete: function() {
			var ids = $buttonList.jqGrid('getDataIDs');
			for (var i = 0; i < ids.length; i++) {
                var menuId = ids[i];
                var rowData = $buttonList.jqGrid('getRowData', menuId);
                var  be = '<a  id="delete-' + menuId + '" href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="删除" onclick="WASP_MENU.deleteMenu(\''+menuId+'\');" ><i class="fa fa-trash-o"></i></a>';
	            var  se = '<a  id="modifi-' + menuId + '" href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="修改" onclick="WASP_MENU.menuUpdateView(\''+menuId+'\');" data-target="#modal-edit"><i class="fa fa-pencil-square-o"></i></a>';

                $buttonList.jqGrid('setRowData', ids[i], { option: be + se });
			}
		}
    });


    //$buttonList.navGrid('#buttonListPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
    $buttonList.jqGrid('setFrozenColumns');
    jqGridResize($buttonList);
    
});