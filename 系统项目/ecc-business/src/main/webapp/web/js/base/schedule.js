/**
 * 定时任务JS类
 * @from schedule.js
 */
var PATH_PREFIX ="";

function setPathPrefix(path){
	PATH_PREFIX = path;
}

$(document).ready(function(){
		$("#scheduleList").jqGrid(
			{   
				url:PATH_PREFIX+'scheduleListPage.do',   
				caption:'定时任务列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',  
				datatype: "json",
				colNames:["任务编号", "任务名称", "任务描述", "job名称", "job所在组", "trigger名称", "trigger所在组", "CLASS路径", "运行周期", "上次运行时间", "运行结果", "原始状态", "状态", "创建人", "创建时间", "操作"],        
				colModel:[
							{name:'taskId',index:'TASK_ID',align:'left',hidden:true, key:true,sortable:false },
							{name:'taskName',index:'TASK_NAME',align:'left', width: 20,hidden:false, resizable:true,sortable:false},             
							{name:'taskDesp',index:'TASK_DESP',align:'left',width: 20, resizable:true,sortable:false},
							{name:'jobName',index:'JOB_NAME', width: 20, resizable:true,align:'left', sortable:false},
							{name:'jobGroup',index:'JOB_GROUP', width: 20, resizable:true,align:'left', sortable:false},
							{name:'triggerName',index:'TRIGGER_NAME', width: 20, hidden:true, resizable:true,align:'left', sortable:false},
							{name:'triggerGroup',index:'TRIGGER_GROUP', width: 20, hidden:true, resizable:true,align:'left', sortable:false},
							{name:'classPath',index:'CLASS_PATH', width: 20, hidden:true, resizable:true,align:'left', sortable:false},
							{name:'cronExp',index:'CRON_EXP', width: 20, resizable:true,align:'left', sortable:false},
							{name:'modifyTime',index:'MODIFY_TIME', width: 30, resizable:true,align:'left', sortable:false},
							{name:'result',index:'RESULT', width: 16, resizable:true,align:'left', sortable:false},
							{name:'state',index:'STATUS',width: 10, hidden:true, resizable:true, align:'left', sortable:false},
							{name:'stateName',index:'STATE',width: 12, resizable:true,align:'left', sortable:false},
							{name:'createId',index:'CREATE_ID', width: 15, resizable:true,align:'left', sortable:false},
							{name:'createTime',index:'CREATE_TIME', width: 25, resizable:true,align:'left', sortable:false},
							{name:'option',index:'option', width: 20, resizable:true,align:'left',sortable:false}
						],        
				rowNum: 10,
		        rowList: [10, 20, 30, 50],
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
				viewrecords: true,
				hidegrid: false, 
				pager: "#schedulePage", 							
				gridComplete: function(){
					var ids = jQuery("#scheduleList").jqGrid('getDataIDs');
					for(var i=0;i < ids.length;i++){
						var taskId = ids[i];
						var rowData = jQuery("#scheduleList").jqGrid('getRowData', taskId);
						var jName = rowData.jobName;
						var jGroup = rowData.jobGroup;
						var tName = rowData.triggerName;
						var tGroup = rowData.triggerGroup;
						var classPath = rowData.classPath;
						var cronExp = rowData.cronExp;
						var status = rowData.state;
						
						var be = "";
						var se = "";
						var xe = "";
						if (status == '1') { //激活状态
							be = '<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="生效" data-toggle="modal" disabled><i class="fa fa-check-circle-o"></i></a>'; 
							se = '<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="失效" data-toggle="modal" onclick="disable(\''+taskId+'\',\''+jName+'\',\''+jGroup+'\',\''+tName+'\',\''+tGroup+'\')"><i class="fa fa-ban"></i></a>';
							xe = '<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="修改运行周期" data-toggle="modal" onclick="updateCron(\''+taskId+'\',\''+tName+'\',\''+tGroup+'\',\''+cronExp+'\')"><i class="fa fa-pencil-square-o"></i></a>';
							ze = '<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="立即执行" data-toggle="modal" onclick="runSchedule(\''+jName+'\',\''+jGroup+'\')"><i class="fa fa-share-square-o"></i></a>';
						} else {   //未激活状态
							be = '<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="生效" data-toggle="modal" onclick="enable(\''+taskId+'\',\''+jName+'\',\''+jGroup+'\',\''+tName+'\',\''+tGroup+'\',\''+classPath+'\',\''+cronExp+'\')"><i class="fa fa-check-circle-o"></i></a>'; 
							se = '<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="失效" data-toggle="modal" disabled><i class="fa fa-ban"></i></a>';
							xe = '<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="修改运行周期" disabled="disabled"><i class="fa fa-pencil-square-o"></i></a>';
							ze = '<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="立即执行" data-toggle="modal" disabled><i class="fa fa-share-square-o"></i></a>';
						}
						jQuery("#scheduleList").jqGrid('setRowData',ids[i],{option:be+se+xe+ze});
					}	
				}
			});
		
		jQuery("#scheduleList").navGrid('#schedulePage',{edit:false,add:false,del:false,search:false,refreshstate:'current'});
		jQuery("#scheduleList").jqGrid('setFrozenColumns');

		jqGridResize(jQuery("#scheduleList"));
	});

function enable(taskId, jName, jGroup, tName, tGroup, classPath, cronExp) {
	ctools.confirm("您确定要使这个任务生效吗？",function(){
		$.ajax({
			//任务生效
			url: PATH_PREFIX+"updateScheduleStatus.do?state=1&taskId=" + taskId + "&jobName=" + jName + "&jobGroup=" + jGroup + "&triggerName=" + tName + "&triggerGroup=" + tGroup + "&classPath=" + classPath + "&cronExp=" + cronExp,  
			dataType: "text",
			type: "POST",
			cache: false,
			async: false,
			success: function(data) {
				if(data=="success"){
					ctools.alert("任务生效成功！","","success");
					checkSalesCancel();
					queryByCondtion(false);
				}else{
					ctools.alert("任务生效失败！","","error");
				}
			}
		});
	});
};

function disable(taskId, jName, jGroup, tName, tGroup) {
	ctools.confirm("您确定要使这个任务失效吗？",function(){
		$.ajax({
			//任务生效
			url: PATH_PREFIX+"updateScheduleStatus.do?state=0&taskId=" + taskId + "&jobName=" + jName + "&jobGroup=" + jGroup + "&triggerName=" + tName + "&triggerGroup=" + tGroup,  
			dataType: "text",
			type: "POST",
			cache: false,
			async: false,
			success: function(data) {
				if(data=="success"){
					ctools.alert("任务失效成功！","","success");
					checkSalesCancel();
					queryByCondtion(false);
				}else{
					ctools.alert("任务失效失败！","","error");
				}
			}
		});
	});
};

/**
 * 修改运行周期
 */
function updateCron(taskId,jobName,jobGroup,cronExp)
{
	$("#taskId").val(taskId);
	$("#jobName").val(jobName);
	$("#jobGroup").val(jobGroup);
	$("#cronExp").val(cronExp);
	$("#updateCronExpPanel").show();
};


function checkSalesCancel(){
	$("#taskId").val("");
	$("#jobName").val("");
	$("#jobGroup").val("");
	$("#cronExp").val("");
	$("#updateCronExpPanel").hide();
}
/**
 * 确认提交修改运行周期
 */
function submitRealTime(){
	var taskId = $("#taskId").val();
	var jobName=$("#jobName").val();
	var jobGroup=$("#jobGroup").val();
	var cronExp=$("#cronExp").val();
	if(cronExp != null && cronExp != ""){
		$.ajax({
			//删除附件与系列产品关系表
			url: PATH_PREFIX+"valiDateCronExp.do?cronExp="+cronExp,   
			dataType: "text",
			type: "POST",
			cache: false,
			async: false,
			success: function(data) {
				if(data=="correct"){
					$.ajax({
						//删除附件与系列产品关系表
						url: PATH_PREFIX+"updateCron.do?taskId="+taskId+"&jobName="+jobName+"&jobGroup="+jobGroup+"&cronExp="+cronExp,  
						dataType: "text",
						type: "POST",
						cache: false,
						async: false,
						success: function(data) {
							if(data=="success"){
								ctools.alert("运行周期修改成功！","","success");
								checkSalesCancel();
								queryByCondtion(false);
							}else{
								ctools.alert("运行周期修改失败！","","error");
							}
						}
					});	
				}else{
					ctools.alert("运行周期不正确！","","warning");
				}
			}
		});	
	}else{
		ctools.alert("运行周期不能为空！","","warning");
	}
}

/**
 * 立即执行
 * @param tName
 * @param tGroup
 */
function runSchedule(tName, tGroup) {
	ctools.confirm("您确定要立即执行这个任务吗？",function(){
		$.ajax({
			//任务立即执行
			url: PATH_PREFIX+"runSchedule.do?jobName="+tName+"&jobGroup="+tGroup,    
			dataType: "text",
			type: "POST",
			cache: false,
			async: false,
			success: function(data) {
				if(data=="success"){
					ctools.alert("任务执行成功！","","success");
					checkSalesCancel();
					queryByCondtion(false);
				}else{
					ctools.alert("任务执行失败！","","error");
				}
			}
		});
	});
};

function queryByCondtion(flag) {
	var txtName = $("#q-name").val();
	var state = $("#q-state").val();

	var postData = $("#scheduleList").jqGrid("getGridParam", "postData");
	$.extend(postData, {
		"sp[name]" : txtName,
		"sp[state]" : state
	});
	if (flag) {
		$("#scheduleList").trigger("reloadGrid", [ {
			page : 1
		} ]);// 重新载入Grid表格
	} else {
		$("#scheduleList").trigger("reloadGrid");// 重新载入Grid表格
	}
};
	
//对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function(){
    var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
    toggleFullScreen(document.documentElement);
    //全屏的时候将几个模态框放到下面去
    $('.modal[role="dialog"]').appendTo($wrapper);
})

