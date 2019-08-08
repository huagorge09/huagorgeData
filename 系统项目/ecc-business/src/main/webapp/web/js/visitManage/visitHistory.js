/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";
var BACKLOG_PATH = "";

function setPath(path,basePath,backlogPath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
	BACKLOG_PATH = backlogPath;
}

var $visList = $('#visitHistoryList');

var visFunc = {
		visitHistoryAddView : function(method){
			var actionUrl =  PRIMARY_PATH + "/visitHistoryAddView.do?method=" + method;
			openDialog(actionUrl);
		},
		visDetailFunc : function(method,visId){
			var actionUrl =  PRIMARY_PATH + "/visitHistoryAddView.do?method="+method+"&visId="+visId;
			openDialog(actionUrl);
		},
		visUpdateFunc : function (method,visId){
			var actionUrl =  PRIMARY_PATH + "/visitHistoryAddView.do?method="+method+"&visId="+visId;
			openDialog(actionUrl);
		},
		visDelFunc : function(visId){
			var actionUrl = PRIMARY_PATH+"/deleteVisitHistory.do";
			ctools.confirm("您确认要删除该数据吗？",function(){
				$.ajax({
					url : actionUrl,
					type :	"post",
					data : {
						visId : visId
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
		
		},
		//发送邮件
		visSendFunc : function (method,visId){
			var actionUrl =  PRIMARY_PATH + "/sendvisitHistory.do";
			ctools.confirm("您确认要发送邮件吗？",function(){
				$.ajax({
					url : actionUrl,
					type :	"post",
					data : {
						visId : visId
					},
					dataType : "json",
					success : function(data) {
						if("1" == data.returnCode ){
							ctools.alert_sweet('发送成功！', "success", "");
						}
						queryByCondtion(true);
					}
				});
			});
		},
		
		
		
		visBacklogCompleteFunc : function(visId,todoId,isComplete){
			var isCompleteName = "未完成";
			if("Y" == isComplete){
				isCompleteName = "完成";
			}
			var actionUrl = BACKLOG_PATH+"/signTodoInfoIsComplete.do";
			ctools.confirm("确认标记待办事项为" +isCompleteName+ "？",function(){
				$.ajax({
					url : actionUrl,
					type :	"post",
					data : {
						todoId : todoId,
						visId : visId,
						isComplete : isComplete
					},
					dataType : "json",
					success : function(data) {
						if("1" == data.returnCode ){
							ctools.alert_sweet('操作成功！', "success", "");
						}
						queryByCondtion(true);
					}
				});
			});
		},
		visBacklogDetailFunc : function(method,todoId,visId){
			var actionUrl =  BACKLOG_PATH + "/todoInfoAddView.do?method="+method+"&todoId="+todoId+"&visId="+visId;
			openDialog(actionUrl);
		},
		visBacklogUpdateFunc :function(method,todoId,visId){
			var actionUrl =  BACKLOG_PATH + "/todoInfoAddView.do?method="+method+"&todoId="+todoId+"&visId="+visId;
			openDialog(actionUrl);
		},
		visBacklogDelFunc : function(todoId){
			var actionUrl = BACKLOG_PATH+"/delTodoListByTodoId.do";
			ctools.confirm("您确认要删除该数据吗？",function(){
				$.ajax({
					url : actionUrl,
					type :	"post",
					data : {
						todoId : todoId
					},
					dataType : "json",
					success : function(data) {
						if("1" == data.returnCode ){
							ctools.alert_sweet('删除成功！', "success", "");
						}else if("-1" == data.returnCode){
							ctools.alert_sweet('程序异常，请联系管理员！', "success", "");
						}
						queryByCondtion(true);
					}
				});
			});
		}
};

$(function(){
	//注册清空事件
    WASP_WIDGET.registerResetClearEvent();
    
    $('#qInstSName').select2();
    $('#qContacts').select2()
    $('#qEmpId').select2();
    
	WASP_WIDGET.triggerInstitutionSelect("qInstSName",false);
	
	WASP_WIDGET.triggerContactsByAuthSelect("qContacts",false);
	
	WASP_WIDGET.triggerEmployeeSelect("qEmpId",false);
	
	WASP_WIDGET.triggerDateRangeStyle("qVisDate");
	
	 $visList.jqGrid({
	        url: PRIMARY_PATH+'/visitHistoryListPage.do',
	        caption: '拜访历史列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
	        datatype: "json",
	        colNames: ["id","机构id","机构简称","联系人","记录人id","记录人","拜访人id","拜访人","拜访时间","拜访地点","操作"],
	        colModel: [
	            { name: 'visId', index: 'visId', width: 50, align:'left', resizable:true, hidden: true, key: true, sortable: false },
	            { name: 'instId', index: 'instId', width: 70, align:'left', resizable:true, hidden: true, key: false, sortable: false },
	            { name: 'instLName', index: 'instLName', width: 120, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'visContactsName', index: 'visContactsName', width: 100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'visEmpId', index: 'visContactsName', width: 100, align:'left', resizable:true, hidden: true, key: false, sortable: false },
	            { name: 'visEmpName', index: 'visContactsName', width: 50, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'visMeetId', index: 'visMeetId', width: 100, align:'left', resizable:true, hidden: true, key: false, sortable: false },
	            { name: 'visMeetName', index: 'visMeetName', width: 120, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'visDate', index: 'visDate', width: 150, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'visAddress', index: 'visAddress', width: 100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
	            { name: 'option', index: 'option', width: 120, align:'left', resizable:true, resizable: true, sortable: false }
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
	        pager: "#visitHistoryPage",
	        viewrecords: true,
	        hidegrid: false,
			subGrid: true,
			subGridRowExpanded: secondGridRowExpanded,
			subGridRowColapsed: function(subgrid_id, row_id) {
				$("#subGridTBId").val("");
			},
			ondblClickRow:function(visId){
				expandProject("visitHistoryList",visId);
			},
			gridComplete: function() {
				var ids = $visList.jqGrid('getDataIDs');
				for(var i=0;i < ids.length;i++){
					var id = ids[i];					 						    
					var rowData = $visList.jqGrid('getRowData', id);	
					var visId = rowData.visId;
					var visEmpId = rowData.visEmpId;
					//按钮置灰
					$(".permissionBtn a").attr("disabled", "disabled");
	                $(".permissionBtn a").removeAttr("onclick");
	                
					//初始化 拜访详情 修改 删除 发送邮件
	                
	                $("#visDetailBtn a").removeAttr("disabled");
                	$("#visDetailBtn a").attr("onClick", "visFunc.visDetailFunc('detail','"+visId+"');");
                	
                	$("#visUpdateBtn a").removeAttr("disabled");
                	$("#visUpdateBtn a").attr("onClick", "visFunc.visUpdateFunc('update','"+visId+"');");
                	/*if(visEmpId == "2171029" ){
                		$("#visUpdateBtn a").removeAttr("disabled");
                    	$("#visUpdateBtn a").attr("onClick", "visFunc.visUpdateFunc('update','"+visId+"');");
                	}*/
                	
                	$("#visDelBtn a").removeAttr("disabled");
                	$("#visDelBtn a").attr("onClick", "visFunc.visDelFunc('"+visId+"');");
                	
                	//发送邮件
                	$("#visSend a").removeAttr("disabled");
                	$("#visSend a").attr("onClick", "visFunc.visSendFunc('send','"+visId+"');");
                	
                	var vdb = $("#visDetailBtn").html();
                	var vub = $("#visUpdateBtn").html();
                	var vdelb = $("#visDelBtn").html();
                	var vsend=$("#visSend").html();//发送邮件
                	
                	$visList.jqGrid('setRowData',ids[i],{option: vdb + vub + vdelb + vsend});
				}
			}
	    });

	    $visList.navGrid('#visitHistoryPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
	    $visList.jqGrid('setFrozenColumns');
	    jqGridResize($visList);
});

function secondGridRowExpanded(subgrid_id, row_id){
	var rowData = $visList.jqGrid('getRowData', row_id);
	var subgrid_table_id = subgrid_id+"_t";
	var pager_id = "p_"+subgrid_table_id;
	$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
	$("#subGridTBId").val(subgrid_table_id);	
	var $subgrid=jQuery("#"+subgrid_table_id);
	$subgrid.jqGrid({
		caption: '待办事项列表',
		url:   BACKLOG_PATH+'/todoListPage.do',
		datatype: "json",
		postData:{
		 	'sp[visId]': rowData.visId
			},  
		colNames: ["id","拜访id","接收人员id","接收人员","待办内容","截止日期","是否完成","操作"],  
		colModel:  [
		            { name: 'todoId', index: 'todoId', width: 50, align:'left', resizable:true, hidden: true, key: true, sortable: false },
		            { name: 'visId', index: 'visId', width: 100, align:'left', resizable:true, hidden: true, key: false, sortable: false },
		            { name: 'todoReceiveId', index: 'todoReceiveId', width: 100, align:'left', resizable:true, hidden: true, key: false, sortable: false },
		            { name: 'todoReceiveName', index: 'todoReceiveName', width: 70, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'todoContent', index: 'todoContent', width: 150, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'todoEndDate', index: 'todoEndDate', width: 100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'isComplete', index: 'isComplete', width: 50, align:'left', resizable:true, hidden: false, key: false, sortable: false },
		            { name: 'option', index: 'option', width: 70, align:'left', resizable:true, resizable: true, sortable: false }
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
				var todoId = sellData.todoId;
				var visId = sellData.visId;
				var isComplete = sellData.isComplete;
				var isCompleteName = "否";
				//按钮置灰
				$(".permissionBtn a").attr("disabled", "disabled");
                $(".permissionBtn a").removeAttr("onclick");
				//初始化 标记完成 详情 修改 删除
                
                //标记 完成
                var vbc = "";
                //未完成时 可以 操作 完成
                if("N" == isComplete){
                	$("#visBacklogCompleteBtn a").removeAttr("disabled");
                	$("#visBacklogCompleteBtn a").attr("onClick", "visFunc.visBacklogCompleteFunc('"+visId+"','"+todoId+"','Y');");
                	vbc = $("#visBacklogCompleteBtn").html();
                }else if("Y" == isComplete){
                	isCompleteName = "是";
                	$("#visBacklogUnCompleteBtn a").removeAttr("disabled");
                	$("#visBacklogUnCompleteBtn a").attr("onClick", "visFunc.visBacklogCompleteFunc('"+visId+"','"+todoId+"','N');");
                	vbc = $("#visBacklogUnCompleteBtn").html();
                }
            	
            	$("#visBacklogDetailBtn a").removeAttr("disabled");
            	$("#visBacklogDetailBtn a").attr("onClick", "visFunc.visBacklogDetailFunc('detail','"+todoId+"','"+visId+"');");
            	
            	$("#visBacklogUpdateBtn a").removeAttr("disabled");
            	$("#visBacklogUpdateBtn a").attr("onClick", "visFunc.visBacklogUpdateFunc('update','"+todoId+"','"+visId+"');");
            	
            	$("#visBacklogDelBtn a").removeAttr("disabled");
            	$("#visBacklogDelBtn a").attr("onClick", "visFunc.visBacklogDelFunc('"+todoId+"');");
            	
            	
            	var vbd = $("#visBacklogDetailBtn").html();
            	var vbu = $("#visBacklogUpdateBtn").html();
            	var vbde = $("#visBacklogDelBtn").html();
            	
            	$subgrid.jqGrid('setRowData',id,{option: vbc+vbd+vbde  ,isComplete:isCompleteName});
			}
		}
	});
	
	$subgrid.navGrid('#'+pager_id,{edit:false,add:false,del:false,search:false});
} ;

function queryByCondtion(flag){
	var instId = $("#qInstSName").val();
	var contacts = $("#qContacts").val();
	var visDate = $("#qVisDate").val();
	var visEmpId = $("#qEmpId").val();
	
	var visDateSp = visDate.split("-");
	var visBeginDate = "";
	var visEndDate = "";
	
	if(!!visDate && visDateSp.length > 1){
		visBeginDate = visDateSp[0].trim();
		visEndDate  = visDateSp[1].trim();
	}
	
    var postData = $visList.jqGrid("getGridParam", "postData");
    $.extend(postData,{
    	'sp[instId]': instId,
    	'sp[visContacts]': contacts,
    	'sp[visBeginDate]': visBeginDate,
    	'sp[visEndDate]': visEndDate,
    	'sp[visEmpId]' : visEmpId
    });

    if (flag) {
    	$visList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	$visList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
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

