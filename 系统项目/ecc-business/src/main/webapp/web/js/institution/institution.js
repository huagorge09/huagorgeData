/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";
var CONTACTS_PATH = "";
function setPath(path,basePath,contactsPath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
	CONTACTS_PATH=contactsPath;
}

var $instList = $('#institutionList');

var instFunc = {
		institutionAddView : function(method){
			var actionUrl =  PRIMARY_PATH + "/institutionAddView.do?method=" + method;
			openDialog(actionUrl);
		},
		instDetailFunc : function(instId){
			var actionUrl =  PRIMARY_PATH + "/instContactsDetailView.do?instId=" + instId;
			openDialog(actionUrl);
		},
		instContactAddFunc : function(instId){
			var actionUrl =  PRIMARY_PATH + "/instContactsAddView.do?instId=" + instId;
			openDialog(actionUrl);
		},
		instUpdateFunc : function(instId,method){
			var actionUrl =  PRIMARY_PATH + "/institutionAddView.do?method="+method+"&instId="+instId;
			openDialog(actionUrl);
		},
		instContactsDelFunc : function(instId,conId){
			var actionUrl = PRIMARY_PATH+"/deleteInstContacts.do";
			ctools.confirm("您确认要删除该数据吗？",function(){
				$.ajax({
					url : actionUrl,
					type :	"post",
					data : {
						conId : conId,
						instId : instId
					},
					dataType : "json",
					success : function(data) {
						if("1" == data.returnCode ){
							ctools.alert_sweet('删除成功！', "success", "");
						}
						queryByCondtion(true);
					}
				});
			});
		}
};

$(function(){
	 $instList.jqGrid({
	        url: PRIMARY_PATH+'/institutionListPage.do',
	        caption: '机构信息列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
	        datatype: "json",
	        colNames: ["机构ID","机构简称","机构全称","机构类型编码","机构类型","地址","营业执照编码","操作"],
	        colModel: [
	            { name: 'instId', index: 'instId', width: 50, align:'left', resizable:true, hidden: false, key: true, sortable: false },
	            { name: 'instSName', index: 'instSName', width: 70, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'instLName', index: 'instLName', width: 100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'instType', index: 'instType', width: 50, align:'left', resizable:true, hidden: true, key: false, sortable: false },
	            { name: 'instTypeNM', index: 'instTypeNM', width: 70, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'instAddress', index: 'instAddress', width: 100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'businessLicenceCode', index: 'businessLicenceCode', width: 50, align:'left', resizable:true, hidden: false, key: false, sortable: false },
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
	        autoScroll : true,
	        editurl: '',
	        viewrecords: true,
	        cellEdit: false,
	        grouping: false,
	        jsonReader: {
	            root: "items", //结果集
	            records: "total", //总记录数 
	            total: "pageCount", //总页数
	            page: "pageNo", //当前页 
	            repeatitems: false // (4) 
	        },
	        multiselect: false,
	        pager: "#institutionPage",
	        viewrecords: true,
	        hidegrid: false,
			subGrid: true,
			subGridRowExpanded: secondGridRowExpanded,
			subGridRowColapsed: function(subgrid_id, row_id) {
				$("#subGridTBId").val("");
			},
			ondblClickRow:function(instId){
				expandProject("institutionList",instId);
			},
			gridComplete: function() {
				var ids = $instList.jqGrid('getDataIDs');
				for(var i=0;i < ids.length;i++){
					var id = ids[i];					 						    
					var rowData = $instList.jqGrid('getRowData', id);	
					var instId = rowData.instId;
					
					
					//按钮置灰
					$(".permissionBtn a").attr("disabled", "disabled");
	                $(".permissionBtn a").removeAttr("onclick");
					//初始化 新增联系人 修改机构  机构详情 按钮
	                $("#instContactsAddBtn a").removeAttr("disabled");
                	$("#instContactsAddBtn a").attr("onClick", "instFunc.instContactAddFunc('"+instId+"');");
                	
                	$("#instUpdateBtn a").removeAttr("disabled");
                	$("#instUpdateBtn a").attr("onClick", "instFunc.instUpdateFunc('"+instId+"','update');");
                	
                	$("#instDetailBtn a").removeAttr("disabled");
                	$("#instDetailBtn a").attr("onClick", "instFunc.instDetailFunc('"+instId+"');");
                	
                	var icab = $("#instContactsAddBtn").html();
                	var iub = $("#instUpdateBtn").html();
                	var idb = $("#instDetailBtn").html();
                	
                	$instList.jqGrid('setRowData',ids[i],{option: idb+icab+iub });
				}
			}
	    });

	    $instList.navGrid('#institutionPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
	    $instList.jqGrid('setFrozenColumns');
	    jqGridResize($instList);
		
	    $('#q-instId').select2();
	    $('#q-contacts').select2();
	    
	    WASP_WIDGET.triggerInstitutionSelect("q-instId",false);
	    WASP_WIDGET.triggerContactsByAuthSelect("q-contacts",false);
	    //注册清空事件
	    WASP_WIDGET.registerResetClearEvent();

	
});

function secondGridRowExpanded(subgrid_id, row_id){
	var rowData = $instList.jqGrid('getRowData', row_id);
	var subgrid_table_id = subgrid_id+"_t";
	var pager_id = "p_"+subgrid_table_id;
	$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
	$("#subGridTBId").val(subgrid_table_id);	
	var $subgrid=jQuery("#"+subgrid_table_id);
	$subgrid.jqGrid({
		caption: '联系人信息列表',
		url:   CONTACTS_PATH+'/instContactsListPage.do',
		datatype: "json",
		postData:{
		 	'sp[instId]': rowData.instId
			},  
		colNames: ["联系人主键", "联系人", "部门", "职务", "手机", "固定电话", "QQ或微信","邮箱","操作"],    
		colModel: [
		            { name: 'conId', index: 'conId', width: 50, align:'left', resizable:true, hidden: true, key: true, sortable: false },
		            { name: 'conName', index: 'conName', width: 70, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'conDept', index: 'conDept', width: 80, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'conPost', index: 'conPost', width: 50, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'conPhone', index: 'conPhone', width: 80, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'conTel', index: 'conTel', width: 80, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'conWeChat', index: 'conWeChat', width: 80, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'conEmail', index: 'conEmail', width: 100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'option', index: 'option', width: 70, resizable: true, sortable: false }
				  ],
		rowNum:20,        
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
        autoScroll : true,
        grouping: false,
        jsonReader: {
            root: "items", //结果集
            records: "total", //总记录数 
            total: "pageCount", //总页数
            page: "pageNo", //当前页 
            repeatitems: false // (4) 
        },
		pager: "#" + pager_id, 
		viewrecords: true,
		hidegrid: false, 
		subGrid: false,
		gridComplete: function(){
			var ids = $subgrid.jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){
				var id = ids[i];					 						    
				var sellData = $subgrid.jqGrid('getRowData', id);	
				var instId = rowData.instId;
				var conId = sellData.conId;
				
				//按钮置灰
				$(".permissionBtn a").attr("disabled", "disabled");
                $(".permissionBtn a").removeAttr("onclick");
				//初始化 删除联系人
                $("#instContactsDelBtn a").removeAttr("disabled");
            	$("#instContactsDelBtn a").attr("onClick", "instFunc.instContactsDelFunc('"+instId+"','"+conId+"');");
            	var icdb = $("#instContactsDelBtn").html();
            	$subgrid.jqGrid('setRowData',id,{option: icdb });
			}
		}
	});
	$subgrid.navGrid('#'+pager_id,{edit:false,add:false,del:false,search:false});
} ;

function queryByCondtion(flag){
	var instId = $("#q-instId").val();
	var contacts = $("#q-contacts").val();
	
    var postData = $instList.jqGrid("getGridParam", "postData");
    $.extend(postData,{
    	'sp[instId]': instId,
    	'sp[contacts]': contacts
    });

    if (flag) {
    	$instList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	$instList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

//双击子产品记录打开子列表		
function expandProject(gridId,rowid) {
	var ids = jQuery("#"+gridId).jqGrid('getDataIDs');
	for(var i=0;i < ids.length;i++){
		var id = ids[i];
		if(id == rowid){
			jQuery("#"+gridId).jqGrid('toggleSubGridRow', id);
		}else{
			jQuery("#"+gridId).jqGrid('collapseSubGridRow', id);
		}
	}
}

//对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function(){
    var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
    toggleFullScreen(document.documentElement);
    //全屏的时候将几个模态框放到下面去
    $('.modal[role="dialog"]').appendTo($wrapper);
});

