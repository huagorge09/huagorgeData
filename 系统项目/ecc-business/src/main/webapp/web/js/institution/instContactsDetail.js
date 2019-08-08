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

var $instContactsList = $('#instContactsList');

$(document).ready(function(){
	
	$instContactsList.jqGrid({
		caption: '联系人信息列表',
		url:   CONTACTS_PATH+'/instContactsListPage.do',
		datatype: "json",
		postData:{
		 	'sp[instId]': $("input[name=instId]:eq(0)").val()
			},  
		colNames: ["联系人主键", "名称", "部门", "职务", "手机", "固定电话", "QQ或微信","邮箱"],    
		colModel: [
		            { name: 'conId', index: 'conId', width: 50, align:'left', resizable:true, hidden: true, key: true, sortable: false },
		            { name: 'conName', index: 'conName', width: 100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'conDept', index: 'conDept', width: 200, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'conPost', index: 'conPost', width: 100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'conPhone', index: 'conPhone', width: 150, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'conTel', index: 'conTel', width: 150, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'conWeChat', index: 'conWeChat', width: 150, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'conEmail', index: 'conEmail', width: 200, align:'left', resizable:true, hidden: false, key: false, sortable: false },
				  ],
				  rowNum: 20,
			        rowList: [20, 30, 50],
			        rownumbers: true,
			        rownumWidth: 70,
			        prmNames: {
			        	        search: "search", 
			        	        page: "pageNo",
			        	        rows: "limit" 
			        	       },
			        height: '300',
			        width: "auto",
			        autowidth:true,
			        editurl: '',
			        viewrecords: true,
			        cellEdit: false,
			        autoScroll: true,
//			        shrinkToFit: true,
			        grouping: false,
			        jsonReader: {
			            root: "items", //结果集
			            records: "total", //总记录数 
			            total: "pageCount", //总页数
			            page: "pageNo", //当前页 
			            repeatitems: false // (4) 
			        },
			        multiselect: false,
			        pager: "#instContactsPage",
			        hidegrid: false,
					subGrid: false
	});
		$instContactsList.navGrid('#instContactsPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
		$instContactsList.jqGrid('setFrozenColumns');
	    jqGridResize($instContactsList);
	    
});