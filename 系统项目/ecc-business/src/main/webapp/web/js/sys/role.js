/**
 * 角色管理JS类
 * @from role.js
 */
var PATH_PREFIX ="";

function setPathPrefix(path){
	PATH_PREFIX = path;
}

/**
 * 角色管理操作JS类
 * 
 */
var WASP_ROLE ={
		/*列表打开文件*/
		getViewHtml : function(text, options, rData){
	 		var roleId = rData["roleId"];
	 		return getHref(text, "WASP_ROLE.view('" + roleId+"')");
		},
		openEmp: function(){//打开选择员工的界面并设置层
			 OpenOrgSelect('EMPIDS','emp_nm','EMP','YES','NO','ORGorgid','NAMEX','loadlist()');
		},
		//详细界面
		view : function(roleId){ 
			var actionUrl = PATH_PREFIX+"roleDetailView.do?roleId=" + roleId;
			openDialog(actionUrl);
		},
		//新增界面
		add : function(){
			var actionUrl = PATH_PREFIX+"roleAddView.do";
			openDialog(actionUrl);
		},
		//更新
	   updateRole : function(roleId){
			var actionUrl = PATH_PREFIX+"roleUpdateView.do?roleId=" + roleId;
			openDialog(actionUrl);
	   },
	   //刷新缓存
	   refreshCached : function(){
			var actionUrl = "/cbp/utils/cached/shareCached/refreshShareCached.do";
			openDialog(actionUrl);
	   },
//	   deleteRole : function(Id){
//		    
//			if (!confirm("请确定要删除该角色吗"))
//			{
//				return;
//			}
//			
//			var actionUrl = "/CMFKMProject/WA_SpecialServer/Wasp/System/Role/RoleAction.jsp?method=deleteAction&ROLEEMPID=" +Id ;
//			openDialog(actionUrl);
//		},
//		 deleteAllRole : function(Id){
//			    
//				if (!confirm("请确定要删除该角色及该角色下所有人员配置吗"))
//				{
//					return;
//				}
//				
//				var actionUrl = "/CMFKMProject/WA_SpecialServer/Wasp/System/Role/RoleAction.jsp?method=deleteAllAction&ROLEID=" +Id ;
//				openDialog(actionUrl);
//			},
		showShareInfo: function(){
			var roleID = $("#roleId").val()||"";//当前记录ID，需要排除
			var authType = $("input[type='radio'][name='authorityType']:checked").val();//1:普通，2:特殊
			var roleType = $("#roleType").val();//角色类型，查询参数
			var actionUrl = PATH_PREFIX+"queryRole.do?roleType="+roleType;
			var container = $("#shareInfoContainer");//页面容器
			
			if('2' == authType){
				container.html('特殊权限不能分享！');//特殊权限不可分享给别人
				return;
			}
			
			//shareInfoIni 更新页面有，新建页面没有
			$.ajax({
	             url: actionUrl,
	             cache: false,
	             error : function(textStatus, errorThrown) {  
	                 ctools.alert("系统ajax交互错误: " + textStatus);  
	             }, 
	             success : function (data)
	             {
	            	 container.html('');
	             	 if(data){
	             		var datas=$.parseJSON(data);
	             		for(var i=0;i<datas.length;i++){
	            			 var shareItem = datas[i];
	            			 
	            			 if(roleID == shareItem['roleId']){continue;}//分享待选中排除自己
	            			 if('2' == shareItem['authorityType']){continue;}//权限类型为特殊的排除
	            			 
	            			 
	            			 var uiItem = $('<div>');
	            			 var uiChkbox = $('<input type="checkbox" name="shareRoleIds"/>');
	            			 
	            			 if(window.shareInfoIni && shareInfoIni[shareItem['roleId']] != null){
	            				 uiChkbox.attr('checked','checked');
	            			 }
	            			 uiChkbox.attr('id','shareRoleIds_'+shareItem['roleId']);
	            			 uiChkbox.val(shareItem['roleId']+'');//设置checkbox的值
	            			 uiItem.append(uiChkbox);
	            			 uiItem.append(' '+shareItem['roleName']);
	            			 container.append(uiItem);
	            			//绑定ICheck样式
	            			 WASP_WIDGET.triggerICheck();
	            		 }
	             	 }else{
	             		container.html("没有数据！");
	            	 }
	             }
	         });
			
		}
}

var $roleList=$("#roleList");
$(function(){
	$roleList.jqGrid({   
				url:PATH_PREFIX+'roleListPage.do',    
				caption:'角色维护列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',  
				datatype: "json",
				colNames:["角色ID","角色类型编码","员工ID","角色名称","角色类型","权限类型","状态", '操作'],        
				colModel:[  {name:'roleId',index:'ROLE_ID', hidden:true,key:true},
				            {name:'roleType',index:'ROLE_TYPE', hidden:true},
				            {name:'empId',index:'EMP_ID', hidden:true},
							{name:'roleName',index:'ROLE_NAME',width: 40, resizable:true,align:'left',formatter: WASP_ROLE.getViewHtml,sortable:false},  
							{name:'roleTypeName',index:'ROLE_TYPE_NAME',width: 30, resizable:true,align:'left',sortable:false},  
							{name:'authorityType',index:'AUTHORITY_TYPE',width: 20, resizable:true,align:'left',formatter:'select',
			                	  editoptions: { value: {1:'普通',2:'特殊'}},sortable:false},
							{name:'statName',index:'STATNAME', width: 20, resizable:true,align:'left', sortable:false},
							{name:'option',index:'option', width: 20, resizable:true,align:'left',sortable:false}
						],
				rowNum: 20,
		        rowList: [20, 30, 50],
		        rownumbers: true,
		        rownumWidth: 50,
		        prmNames: { search: "search", page: "pageNo", rows: "limit" },
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
				multiselect:false,
				pager: "#rolePage", 							
				viewrecords: true,
				hidegrid: false, 
				gridComplete: function(){
					var ids = $roleList.jqGrid('getDataIDs');
					for(var i=0;i < ids.length;i++){
						var role_ID = ids[i];
					 
						var rowData = $roleList.jqGrid('getRowData', role_ID); 
						var se = '<a id="lineEdit'+role_ID+'" href="#" class="btn btn-link btn-jqgrid" title="修改" onclick="WASP_ROLE.updateRole(\''+rowData.roleId+'\')"><i class="fa fa-pencil-square-o"></i></a>';
//						var se = "<span style='padding: 2px;'><img style='margin-top: 5px; cursor: pointer;' alt='修改' onclick=\"WASP_ROLE.updateRole('"+rowData.roleId+"')\""+ 
//									" src='/CMFKMProject/WA_SpecialServer/WaWeb/WaWeb_Images/FileUpload/editWasp.png'></span>"; 
						//+"<input style='width:40px;' class=\"button\" type='button' value='修改' onclick=\"WASP_ROLE.updateRole('"+rowData.ROLE_ID+"')\"  />&nbsp;";
						//var be = "<input style='width:40px;' class=\"button\" type='button' value='删除'+role_ID onclick=\"WASP_ROLE.deleteRole('"+rowData.ROLE_ID+"')\"  />&nbsp;"; 
						//var be = "<span style='padding: 2px;'><img style='margin-top: 5px; cursor: pointer;' alt='删除' onclick=\"WASP_ROLE.deleteAllRole('"+rowData.ROLE_ID+"')\""+
						//			" src='/CMFKMProject/WA_SpecialServer/WaWeb/WaWeb_Images/FileUpload/del.png'></span>";
						$roleList.jqGrid('setRowData',ids[i],{option:se});
					}	
				}
			});
		
		$roleList.navGrid('#rolePage',{edit:false,add:false,del:false,search:false,refreshstate:'current'});
		$roleList.jqGrid('setFrozenColumns');

//		$roleList.closest(".ui-jqgrid-bdiv").css({'overflow-y': 'scroll'});
		jqGridResize($roleList);
		
	});

/**
 * 查询
 * @param flag
 */
function queryByCondtion (flag){
	var roleName = $("#q-roleName").val();
	var roleType = $("#q-roleType").val();
	var empId = $("#q-empId").find("option:selected").val()||"";
 
    var postData = $roleList.jqGrid("getGridParam", "postData");
	$.extend(postData,{ 
		'sp[roleName]': roleName,
		'sp[roleType]': roleType,
		'sp[empId]': empId
	 });
	 if (flag){
		$roleList.trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
	}else{
		$roleList.trigger("reloadGrid");//重新载入Grid表格
	}       
};

	
//导出操作
//function openDialogExportExcel() {
//	window.location = "/CMFKMProject/WA_SpecialServer/Wasp/System/Role/ExpExcelAction.jsp";
//}
//对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function(){
    var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
    toggleFullScreen(document.documentElement);
    //全屏的时候将几个模态框放到下面去
    $('.modal[role="dialog"]').appendTo($wrapper);
})

function initRadios(){
    //单选框
	WASP_WIDGET.triggerIRadio("input[name='authorityType']",function(value){
		WASP_ROLE.showShareInfo();
	});
	
}

	
