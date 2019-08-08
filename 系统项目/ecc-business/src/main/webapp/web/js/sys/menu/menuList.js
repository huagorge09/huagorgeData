var WASP_MENU = {
    menuAddView: function() {
        /** 新建立项 */
    	 var actionUrl = PATH_PREFIX+"menuAddView.do";
		 openDialog(actionUrl);
    },
    menuAuthManageView: function() {
        /**菜单权限管理页面 */
    	 var actionUrl = MENU_AUTH_PATH_PREFIX+"menuAuthManageView.do";
		 openDialog(actionUrl);
    },
    menuUpdateView: function(menuId) {
	    /** 更新菜单 */
		 var actionUrl = PATH_PREFIX+"menuUpdateView.do?menuId="+menuId;
		 openDialog(actionUrl);
	},
	formatViewHtml: function(text, options, rData) {
        var menuId = rData["menuId"];
        return '<a href="javascript(0);"  class="showunderline" data-toggle="modal" data-target="#modal-info"  onclick="WASP_MENU.menuDetailView(\''+menuId+'\');" >' + text + '</a>';
    },
	menuDetailView: function(menuId){
		 var actionUrl = PATH_PREFIX+"menuDetailView.do?menuId="+menuId;
		 openDialog(actionUrl);
	},
	refreshMenuCache: function(){
		 var actionUrl = PATH_PREFIX+"refreshMenuCache.do";
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
							queryByCondtion(false);
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
	},
    /**
	 * 子列表数据加载
	 */
	secondGridRowExpanded:function (subgrid_id, row_id) {
		var rowData = $menuList.jqGrid('getRowData', row_id);
		var subgrid_table_id = subgrid_id+"_t";
		//var pager_id = "p_"+subgrid_table_id;
		$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table></div>");
		$("#subGridTBId").val(subgrid_table_id);	
		var $subgrid=jQuery("#"+subgrid_table_id);
		$subgrid.jqGrid({
			caption: '二级菜单列表',
			url:   PATH_PREFIX+'menuListPage.do',
			datatype: "json",
			postData:{
			 	'sp[parentId]': rowData.menuId
				},  
			colNames: ["菜单主键", "菜单名称", "菜单链接", "菜单序号", "状态",'操作'],    
			colModel: [
			            { name: 'menuId', index: 'menuId', width: 30, hidden: false, key: true, sortable: true },
			            { name: 'nameConvert', index: 'nameConvert', width: 60, hidden: false, resizable: true, sortable: false,formatter: WASP_MENU.formatViewHtml },
			            { name: 'url', index: 'url', width: 90, resizable: true, sortable: false },
			            { name: 'sequence', index: 'sequence', width: 25, resizable: true, sortable: false },
			            { name: 'status', index: 'status', hidden: true },
			            { name: 'option', index: 'option', width: 20, resizable: true, sortable: false }
					  ],
			rowNum:9999,        
			rowList:[10,20,30],
			rownumbers: true,
			rownumWidth: 50,
			prmNames: { search: "search", page: "pageNo", rows: "limit" }, 
	        height: 'auto',
	        width: false,
	        autowidth:true, 
	        editurl: '',
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
			//pager: "#" + pager_id, 
			viewrecords: true,
			hidegrid: false, 
			subGrid: true,
			subGridRowExpanded: WASP_MENU.thirdGridRowExpanded,
			subGridRowColapsed: function(subgrid_id, row_id) {
				$("#subGridTBId").val("");
			},
			ondblClickRow: function(prjCode){
			},
			gridComplete: function(){
				var ids = $subgrid.jqGrid('getDataIDs');
				for (var i = 0; i < ids.length; i++) {
	                var menuId = ids[i];
	                var rowData = $subgrid.jqGrid('getRowData', menuId);
			        var  be = '<a  id="delete-' + menuId + '" href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="删除" onclick="WASP_MENU.deleteMenu(\''+menuId+'\');" ><i class="fa fa-trash-o"></i></a>';
		            var  se = '<a  id="modifi-' + menuId + '" href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="修改" onclick="WASP_MENU.menuUpdateView(\''+menuId+'\');" data-target="#modal-edit"><i class="fa fa-pencil-square-o"></i></a>';

		            $subgrid.jqGrid('setRowData', ids[i], { option: be + se });
				}
			}
		});
		//jQuery("#"+subgrid_table_id).navGrid('#'+pager_id,{edit:false,add:false,del:false,search:false, refreshstate: 'current'});
	},
	 /**
	 * 子列表数据加载
	 */
	thirdGridRowExpanded:function (subgrid_id, row_id) {
		var parentGridId='#'+subgrid_id.replace('_'+row_id,'');
		var rowData = $(parentGridId).jqGrid('getRowData', row_id);
		var subgrid_table_id = subgrid_id+"_tt";
		var pager_id = "p_"+subgrid_table_id;
		$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
		var $subgrid=jQuery("#"+subgrid_table_id);
		$subgrid.jqGrid({
			caption: '三级菜单列表',
			url:   PATH_PREFIX+'menuListPage.do',
			datatype: "json",
			postData:{
			 	'sp[parentId]': rowData.menuId
				},  
			colNames: ["菜单主键", "菜单名称", "菜单链接", "菜单序号", "状态",'操作'],    
			colModel: [
			            { name: 'menuId', index: 'menuId', width: 50, hidden: false, key: true, sortable: true },
			            { name: 'nameConvert', index: 'nameConvert', width: 60, hidden: false, resizable: true, sortable: false,formatter: WASP_MENU.formatViewHtml },
			            { name: 'url', index: 'url', width: 90, resizable: true, sortable: false },
			            { name: 'sequence', index: 'sequence', width: 25, resizable: true, sortable: false },
			            { name: 'status', index: 'status', hidden: true },
			            { name: 'option', index: 'option', width: 20, resizable: true, sortable: false }
					  ],
			rowNum:9999,        
			rowList:[10,20,30],
			rownumbers: true,
			rownumWidth: 50,
			prmNames: { search: "search", page: "pageNo", rows: "limit" }, 
	        height: 'auto',
	        width: false,
	        autowidth:true, 
	        editurl: '',
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
			//pager: "#" + pager_id, 
			viewrecords: true,
			hidegrid: false, 
			subGrid: true,
			subGridRowExpanded: WASP_MENU.fourGridRowExpanded,
			subGridRowColapsed: function(subgrid_id, row_id) {
				$("#subGridTBId").val("");
			},
			gridComplete: function(){
				var ids = $subgrid.jqGrid('getDataIDs');
				for (var i = 0; i < ids.length; i++) {
	                var menuId = ids[i];
	                var rowData = $subgrid.jqGrid('getRowData', menuId);
	                var  be = '<a  id="delete-' + menuId + '" href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="删除" onclick="WASP_MENU.deleteMenu(\''+menuId+'\');" ><i class="fa fa-trash-o"></i></a>';
		            var  se = '<a  id="modifi-' + menuId + '" href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="修改" onclick="WASP_MENU.menuUpdateView(\''+menuId+'\');" data-target="#modal-edit"><i class="fa fa-pencil-square-o"></i></a>';

		            $subgrid.jqGrid('setRowData', ids[i], { option: be + se });
				}
			}
		});
	},
	/**
	 * 子列表数据加载
	 */
	fourGridRowExpanded:function (subgrid_id, row_id) {
		var parentGridId='#'+subgrid_id.replace('_'+row_id,'');
		var rowData = $(parentGridId).jqGrid('getRowData', row_id);
		var subgrid_table_id = subgrid_id+"_ttt";
		var pager_id = "pp_"+subgrid_table_id;
		$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
		var $subgrid=jQuery("#"+subgrid_table_id);
		$subgrid.jqGrid({
			caption: '四级菜单列表',
			url:   PATH_PREFIX+'menuListPage.do',
			datatype: "json",
			postData:{
			 	'sp[parentId]': rowData.menuId
				},  
			colNames: ["菜单主键", "菜单名称", "菜单链接", "菜单序号", "状态",'操作'],    
			colModel: [
			            { name: 'menuId', index: 'menuId', width: 50, hidden: false, key: true, sortable: true },
			            { name: 'nameConvert', index: 'nameConvert', width: 60, hidden: false, resizable: true, sortable: false,formatter: WASP_MENU.formatViewHtml },
			            { name: 'url', index: 'url', width: 90, resizable: true, sortable: false },
			            { name: 'sequence', index: 'sequence', width: 25, resizable: true, sortable: false },
			            { name: 'status', index: 'status', hidden: true },
			            { name: 'option', index: 'option', width: 20, resizable: true, sortable: false }
					  ],
			rowNum:9999,        
			//rowList:[10,20,30],
			rownumbers: true,
			rownumWidth: 50,
			prmNames: { search: "search", page: "pageNo", rows: "limit" }, 
	        height: 'auto',
	        width: false,
	        autowidth:true, 
	        editurl: '',
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
			//pager: "#" + pager_id, 
			viewrecords: true,
			hidegrid: false, 
			gridComplete: function(){
				var ids = $subgrid.jqGrid('getDataIDs');
				for (var i = 0; i < ids.length; i++) {
	                var menuId = ids[i];
	                var rowData = $subgrid.jqGrid('getRowData', menuId);
	                var  be = '<a  id="delete-' + menuId + '" href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="删除" onclick="WASP_MENU.deleteMenu(\''+menuId+'\');" ><i class="fa fa-trash-o"></i></a>';
		            var  se = '<a  id="modifi-' + menuId + '" href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="修改" onclick="WASP_MENU.menuUpdateView(\''+menuId+'\');" data-target="#modal-edit"><i class="fa fa-pencil-square-o"></i></a>';

		            $subgrid.jqGrid('setRowData', ids[i], { option: be + se });
				}
			}
		});
	}
};


var $menuList = $('#menuList');
$(function() {
    $menuList.jqGrid({
        url: PATH_PREFIX+'menuListPage.do',
        caption: '系统菜单列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
        datatype: "json",
        colNames: ["菜单主键", "菜单名称", "菜单链接", "菜单序号", "状态",'操作'],
        colModel: [
            { name: 'menuId', index: 'menuId', width: 50, hidden: false, key: true, sortable: true },
            { name: 'nameConvert', index: 'nameConvert', width: 60, hidden: false, resizable: true, sortable: false,formatter: WASP_MENU.formatViewHtml},
            { name: 'url', index: 'url', width: 30, resizable: true, sortable: false },
            { name: 'sequence', index: 'sequence', width: 25, resizable: true, sortable: false },
            { name: 'status', index: 'status', hidden: true },
            { name: 'option', index: 'option', width: 20, resizable: true, sortable: false }
        ],
        rowNum: 10,
        rowList: [10, 20, 30, 50],
        rownumbers: true,
        rownumWidth: 50,
        postData:{
		 	'sp[parentId]':'0'
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
        pager: "#menuPage",
        viewrecords: true,
        hidegrid: false,
		subGrid: true,
		subGridRowExpanded: WASP_MENU.secondGridRowExpanded,
		subGridRowColapsed: function(subgrid_id, row_id) {
			$("#subGridTBId").val("");
		},
		gridComplete: function() {
			var ids = $menuList.jqGrid('getDataIDs');
			for (var i = 0; i < ids.length; i++) {
                var menuId = ids[i];
                var rowData = $menuList.jqGrid('getRowData', menuId);
                var  be = '<a  id="delete-' + menuId + '" href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="删除" onclick="WASP_MENU.deleteMenu(\''+menuId+'\');" ><i class="fa fa-trash-o"></i></a>';
	            var  se = '<a  id="modifi-' + menuId + '" href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="修改" onclick="WASP_MENU.menuUpdateView(\''+menuId+'\');" data-target="#modal-edit"><i class="fa fa-pencil-square-o"></i></a>';

                $menuList.jqGrid('setRowData', ids[i], { option: be + se });
			}
		}
    });


    $menuList.navGrid('#menuPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
    $menuList.jqGrid('setFrozenColumns');
    jqGridResize($menuList);
    
    
    //注册清空事件
    WASP_WIDGET.registerResetClearEvent();
    
});


/**
 * 通过条件进行搜索
 */
function queryByCondtion(flag) {
    var name = $("#q-name").val();
    var postData = $menuList.jqGrid("getGridParam", "postData");
    var parentId='';
    if($.trim(name) != ''){
    	parentId='';
    }else{
    	parentId='0';
    }
	$.extend(postData, {
		'sp[name]':name,
		'sp[parentId]':parentId
	});
    if (flag) {
    	$menuList.trigger("reloadGrid", [{ page: 1 }]); //重新载入Grid表格
    } else {
    	$menuList.trigger("reloadGrid"); //重新载入Grid表格
    }
};
