/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var $contactsList = $('#contactsList');

var conFunc = {
		contactsAddView : function(method){
			var actionUrl =  PRIMARY_PATH + "/contactsInfoAddView.do?method=" + method;
			openDialog(actionUrl);
		},
		contactsUpdateFunc : function(conId,method){
			var actionUrl =  PRIMARY_PATH + "/contactsInfoAddView.do?method=" + method + "&conId=" + conId;
			openDialog(actionUrl);
		},
		contactsDelFunc : function(conId){
			var actionUrl = PRIMARY_PATH+"/deleteContactsInfo.do";
			ctools.confirm("您确认要删除该数据吗？",function(){
				$.ajax({
					url : actionUrl,
					type :	"post",
					data : {
						conId : conId
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
	 $contactsList.jqGrid({
	        url: PRIMARY_PATH+'/contactsListPage.do',
	        caption: '联系人信息列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
	        datatype: "json",
	        colNames: ["ID","联系人","公司id","公司","部门","职务","手机","固定电话","QQ或微信","邮箱","创建人","创建时间","修改人","修改时间","操作"],
	        colModel: [
	            { name: 'conId', index: 'conId', width:50, align:'left',  hidden: true, key: true, sortable: false },
	            { name: 'conName', index: 'conName', width:60, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'conCompany', index: 'conCompany', width:150, align:'left', resizable:true, hidden: true, key: false, sortable: false },
	            { name: 'conCompanyName', index: 'conCompanyName', width:200, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'conDept', index: 'conDept', width:60, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'conPost', index: 'conPost', width:60, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'conPhone', index: 'conPhone', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'conTel', index: 'conTel', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'conWeChat', index: 'conWeChat', width:140, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'conEmail', index: 'conEmail', width:140, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'createName', index: 'createName', width:70, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'createTime', index: 'createTime', width:140,align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'modifyName', index: 'modifyName', width:70, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'modifyTime', index: 'modifyTime', width:140, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'option', index:'option',width:80, align:'left', resizable:true, resizable: true, sortable: false }
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
	        width: 'auto',
	        scroll :true,
	        autowidth:false,
	        shrinkToFit:false,
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
	        pager: "#contactsPage",
	        viewrecords: true,
	        hidegrid: false,
			subGrid: false,/*
			subGridRowExpanded: secondGridRowExpanded,
			subGridRowColapsed: function(subgrid_id, row_id) {
				$("#subGridTBId").val("");
			},*/
			gridComplete: function() {
				var ids = $contactsList.jqGrid('getDataIDs');
				for(var i=0;i < ids.length;i++){
					var id = ids[i];					 						    
					var rowData = $contactsList.jqGrid('getRowData', id);	
					var conId = rowData.conId;
					
					//按钮置灰
					$(".permissionBtn a").attr("disabled", "disabled");
	                $(".permissionBtn a").removeAttr("onclick");
					//初始化 新增联系人 修改 删除
	                $("#contactsUpdateBtn a").removeAttr("disabled");
                	$("#contactsUpdateBtn a").attr("onClick", "conFunc.contactsUpdateFunc('"+conId+"','update');");
                	
                	$("#contactsDelBtn a").removeAttr("disabled");
                	$("#contactsDelBtn a").attr("onClick", "conFunc.contactsDelFunc('"+conId+"');");
                	
                	var cub = $("#contactsUpdateBtn").html();
                	var cdb = $("#contactsDelBtn").html();
                	
                	$contactsList.jqGrid('setRowData',ids[i],{option: cub+cdb });
				}
			}
	    });

	    $contactsList.navGrid('#contactsPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
	    $contactsList.jqGrid('setFrozenColumns');
	    jqGridResize($contactsList);
	    $('#q-conId').select2();
	    WASP_WIDGET.triggerContactsByAuthSelect("q-conId",false);
	    //注册清空事件
	    WASP_WIDGET.registerResetClearEvent();

	
});

function queryByCondtion(flag){
	var contacts = $("#q-conId").val();
	
    var postData = $contactsList.jqGrid("getGridParam", "postData");
    $.extend(postData,{
    	'sp[conId]': contacts
    });

    if (flag) {
    	$contactsList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	$contactsList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

//对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function(){
    var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
    toggleFullScreen(document.documentElement);
    //全屏的时候将几个模态框放到下面去
    $('.modal[role="dialog"]').appendTo($wrapper);
});

