var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
$(function(){
	var colNames=['主键','基金代码','工作日日期','工作日日期','工作日名称',"操作"];
	COMMON_INIT.initDateYMDComponent("workdate_add");
	initGrid(colNames);
});

var queryDetailList=$('#workDaysList');

function initGrid(colNames){
	queryDetailList.jqGrid({
		url : PRIMARY_PATH +  '/getWorkDateList.xhtml',
		caption : '<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
		datatype : "json",
		colNames : colNames,
		colModel : [ 
		             {name : 'workdayId',index : 'workdayId',width : 100,hidden : true,key : true,sortable : false},
		             {name : 'fundid',index : 'fundid',width : 100,hidden : true,key : false,sortable : false},
		             {name : 'workdate',index : 'workdate',width : 100,hidden : true,key : false,sortable : false},
		             {name : 'workdateFmt',index : 'workdateFmt',hidden : false, key : false,sortable : false}, 
		             {name : 'workflagnm',index : 'workflagnm',hidden : false, key : false,sortable : false},
		             {name: 'option', index: 'option', width: 80, resizable:true, resizable: true, sortable: false }
		           ],
		rowNum : 20,
		rowList : [ 20, 30, 50 ],
		rownumbers : true,
		rownumWidth : 50,
		prmNames : {
			search : "search",
			page : "pageNo",
			rows : "limit"
		},
		height : 'auto',
		width: false,
        autowidth:true,
        shrinkToFit:true,
        autoScroll : true,
        editurl: '',
        viewrecords: true,
        cellEdit: false,
        grouping: false,
		jsonReader : {
			root : "items", // 结果集
			records : "total", // 总记录数
			total : "pageCount", // 总页数
			page : "pageNo", // 当前页
			repeatitems : false// (4)
		},loadError : function(xhr, status, error) {
			switch (status) {
			case 403:
				sweetAlert("对不起，您无此权限！", "", "error");
				break;
			case 404:
				sweetAlert("对不起，无此页面！", "", "error");
				break;
			case 500:
				sweetAlert("内部错误，请联系管理员！", "", "error");
				break;
			case 504:
				sweetAlert("超时，请联系管理员！", "", "error");
				break;
			}
		},
		pager : "#workDaysPage",
		multiselect: false,
        viewrecords: true,
        hidegrid: false,
		subGrid: false,
		gridComplete : function() {
		   var ids = queryDetailList.jqGrid('getDataIDs');
           for (var i = 0; i < ids.length; i++) {
        	   var id = ids[i];					 						    
        	   var rowData = queryDetailList.jqGrid('getRowData', id);	
			   var fundid = rowData.fundid;
			   var workdate = rowData.workdate;//\'"+fundid+"\',\'"+workdate+"\'
        	   var se = '<a href="javascript:void(0)" class="btn btn-link btn-jqgrid" onclick="updateWorkDayPage(\''+fundid+'\',\''+workdate+'\')" title="修改"><i class="fa fa-pencil-square-o"></i></a>';
        	   var be = '<a href="javascript:void(0)" class="btn btn-link btn-jqgrid" onclick="deleteWorkDay(\''+fundid+'\',\''+workdate+'\')" title="删除"><i class="fa fa-trash-o"></i></a>'; 
        	   queryDetailList.jqGrid('setRowData',ids[i],{option: se+be });
           }
		}
	});
	queryDetailList.navGrid('#workDaysPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	queryDetailList.jqGrid('setFrozenColumns');
	jqGridResize(queryDetailList);
}

/**
 * 查询工作日列表数据
 * @param flag
 */
function queryByCondtion(flag){
    var postData = queryDetailList.jqGrid("getGridParam", "postData");
    $.extend(postData,{});
    if (flag) {
    	queryDetailList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	queryDetailList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

/**
 * 打开修改工作日页面
 * @param fundid
 * @param workdate
 */
function updateWorkDayPage(fundid,workdate){
	var actionUrl = PRIMARY_PATH +  "/goUpdatePage.xhtml?fundid="+fundid+"&workdate="+workdate;
	openDialog(actionUrl);
}

/**
 * 修改工作日
 * @param fundid
 * @param workdate
 */
function updateWorkDay(fundid,workdate){
	var fundid = $("input[name=fundid]").val();
	var workdate = $("input[name=workdate]").val();
	var workflag = $("#workflagnm").val();
	$.ajax({
		url:PRIMARY_PATH +   '/updateWorkday.xhtml',   
		dataType: "json",
		type: "POST",
		data:{
			'fundid':fundid,
			'workdate':workdate,
			'workflag':workflag
			},
		cache: false,
		async: true,
		success: function(data) {
			if(data.resultCode=='0000'){
				window.location.href=BASE_PATH +  "service/jsp/hint/success.jsp";
			}else{
				ctools.alert_sweet('修改失败！', "error", "");
			}
		}
	});
}

/**
 * 删除工作日
 * @param fundid
 * @param workdate
 */
function deleteWorkDay(fundid,workdate){
	ctools.confirm("您确认要删除该数据吗？",function(){
		$.ajax({
			url: PRIMARY_PATH + '/deleteWorkday.xhtml',   
			dataType: "json",
			type: "POST",
			data:{
				'fundid':fundid,
				'workdate':workdate
				},
			cache: false,
			async: true,
			success: function(data) {
				if(data.resultCode=='0000'){
					ctools.alert_sweet('删除成功！', "success", "");
					queryDetailList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
				}else{
					ctools.alert_sweet('删除失败！', "error", "");
				}
			}
		});
	});
}

/**
 * 打开生成工作日页面
 */
function openAddPage(){
	var actionUrl = PRIMARY_PATH +  "/goAddPage.xhtml";
	openDialog(actionUrl);
}

/**
 * 增加工作日
 * @param fundid
 * @param workdate
 */
function saveWorkDay(fundid,workdate){
	var fundid = $("#fundid_add").val();
	var workdate = $("#workdate_add").val();
	if(workdate == "" || workdate == null){
		toastr.warning('', '请填写工作日日期');
	}else{
		$.ajax({
			url:PRIMARY_PATH + '/createWorkday.xhtml',   
			dataType: "json",
			type: "POST",
			data:{
				'fundid':fundid,
				'workdate':workdate,
				'workflag':'Y'
			},
			cache: false,
			async: true,
			success: function(data) {
				if(data.resultCode=='0000'){
					window.location.href=BASE_PATH +  "service/jsp/hint/success.jsp";
				}else{
					ctools.alert_sweet('增加失败！', "error", "");
				}
			}
		});
	}
}

/**
 * 打开自动生成工作日页面
 */
function openAutoAddPage(){
	var actionUrl = PRIMARY_PATH +  "/goAutoAddPage.xhtml";
	openDialog(actionUrl);
}

function loadYears(){
	var html = "";
	$.ajax({
		url: PRIMARY_PATH + '/getYearsList.xhtml',   
		dataType: "json",
		type: "POST",
		cache: false,
		async: true,
		success: function(data) {
			for (var i = 0; i < data.length; i++) {
				var temp = data[i];
				html+="<option value="+temp+">"+temp+"</option>";
			}
			$("#years").html(html);
			$('#years').select2({allowClear: false,minimumResultsForSearch:Infinity});
		}
	});
}

/**
 * 获取所有基金信息
 */
function loadFundAllInfo(){
	var html = "";
	$.ajax({
		url:PRIMARY_PATH + '/getFundAllInfo.xhtml',   
		dataType: "json",
		type: "POST",
		cache: false,
		async: true,
		success: function(data) {
			for (var i = 0; i < data.length; i++) {
				var temp = data[i];
				html+="<option value="+temp.fundId+">"+temp.fundShortNm+"</option>";
			}
			$("#fundid_add").html('<option value="******">全部</option>'+html);
			$('#fundid_add').select2({allowClear: false,minimumResultsForSearch:Infinity});
		}
	});
}


function pMonth(m){
	ret = "";
	ret += "<option value='--'>--</option>";
	for(i=1; i<13; i++){
		sel = "";
		if(m==i) sel = "selected";
		ret += '<option value="'+i+'" '+sel+'>'+i+'</option>';
	}
	return ret;
}

function pDay(selDay){
	ret = "";
	ret += "<option value='--'>--</option>";
	for (i=1; i<32; i++){	
		sel = "";
		if(selDay==i) sel = "selected";
		ret += '<option value="'+i+'" '+sel+'>'+i+'</option>';
	}
	return ret;
}

var fieldsetCount = 0;

function addForm(des,sMon,sDay,eMon,eDay){ 
	var workDaysTable = $("#workDaysTable");
	var html = buildHtml(des,sMon,sDay,eMon,eDay);
	workDaysTable.append(html);
	fieldsetCount++;
}

function buildHtml(des,sMon,sDay,eMon,eDay){
	var ret = "";
	ret+='<tr>';
	ret+='<td></td>';
	ret+='<td>节日区间：</td>';
	ret+='<td>';
	ret+='<table style="height: 140px;">';
	ret+='<tbody>';
	ret+='<tr>';
	ret+='<td class="formCaptionText">时间区间：</td>';
	ret+='<td class="spanText">从</td>';
	ret+='<td class="selectWid">';
	ret+='<select name="startMonth" class="select2">';
	ret+=pMonth(sMon);
	ret+='</select>';
	ret+='</td>';
	ret+='<td class="spanText">月</td>';
	ret+='<td class="selectWid">';
	ret+='<select name="startDay" class="select2">';
	ret+=pDay(sDay)
	ret+='</select>';
	ret+='</td>';
	ret+='<td class="spanText" style="width: 40px;">日 到</td>';
	ret+='<td class="selectWid">';
	ret+='<select name="endMonth" class="select2">';
	ret+=pMonth(eMon);
	ret+='</select>';
	ret+='</td>';
	ret+='<td class="spanText">月</td>';
	ret+='<td class="selectWid">';
	ret+='<select name="endDay" class="select2">';
	ret+=pDay(eDay);
	ret+='</select>';
	ret+='</td>';
	ret+='<td class="spanText">日</td>';
	ret+='</tr>';
	ret+='<tr>';
	ret+='<td class="formCaptionText">节日描述：</td>';
	ret+='<td colspan="4"><input name="desc" class="form-control"type="text" value="'+des+'"></td>';
	ret+='</tr>';
	ret+='<tr>';
	ret+='<td class="formCaptionText">是否应用：</td>';
	ret+='<td colspan="4"><input name="applyChk" style="width: 20px;" type="checkbox" checked value="Y">是<input name="apply" type="hidden" value=""></td>';
	ret+='</tr>';
	ret+='</tbody>';
	ret+='</table>';
	ret+='</td>';
	ret+='<td></td>';
	ret+='</tr>';
	return ret;
}

/**
 * 自动增加工作日
 * @param fundid
 * @param workdate
 */
function autoSaveWorkDay(){
	var chklen = $("input[name=applyChk]").length;
	var chkElm = $("input[name=applyChk]");
	var applyElm = $("input[name=apply]");
	for (i = 0; i < chklen ; i++){
		elm = eval(chkElm[i]);
		elmApp = eval(applyElm[i]);
		if(elm.checked){
			elmApp.value='Y';
		}else{
			elmApp.value='N';
		}
	}
	$.ajax({
		url:PRIMARY_PATH + '/autoCreateWorkday.xhtml',   
		dataType: "json",
		type: "POST",
		data:$('#workDayForm').serialize(),// 你的formid
		cache: false,
		async: false,
		success: function(data) {
			if(data.resultCode=='0000'){
				window.location.href=BASE_PATH + "service/jsp/hint/success.jsp";
			}else{
				ctools.alert_sweet('增加失败！', "error", "");
			}
		}
	});
}