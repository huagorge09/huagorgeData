var PATH_PREFIX = "";
function setPathPrefix(path) {
	PATH_PREFIX = path;
}
$(function(){
	var $msgConfList = $('#msgConfList');
	$msgConfList.jqGrid({
		caption: '主消息配置列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
		url:  PATH_PREFIX+'msgConfList.do',
		datatype: "json",
		colNames: ['邮件编号', '标题' , '修改时间','运行状态id','运行状态','运行结果' ,'操作'],  
		colModel: [
				  {name:'mcId',width:100,index:'mcId', align:'left',key:true},
				  {name:'category',width:100,index:'category',align:'left',sortable:false},
				  {name:'modifiDate',width:100,index:'modifiDate',align:'left',sortable:false},
				  {name:'runStatus',width:100,index:'runStatus',align:'left',sortable:false,hidden:true},
				  {name:'runStatusNM',width:100,index:'runStatusNM',align:'left',sortable:false},
				  {name:'result',index:'result',align:'left',sortable:false},
				  {name:'option',index:'option',  resizable:true, align:'left', sortable:false}
				  ],
		rowNum:10,        
		rowList:[10, 20,30,50],
		rownumbers : true,
		rownumWidth : 50,
		prmNames: {
			search: "search",
			page: "pageNo",     //当前页
			rows: "limit"         //每页行数
		},  
		height: 'auto',
		autowidth : true,
		width: false,
		editurl : '',
		viewrecords : true,
		cellEdit : false,
		shrinkToFit : true,
		grouping : false,
		autowidth : true,
		jsonReader: {  
			root: "items",       //结果集
			records: "total", //总记录数 
			total: "pageCount",	  //总页数
			page: "pageNo",	  //当前页 
			repeatitems : false       // (4)  
		},
		pager: "#msgConfPage", 
		viewrecords: true,
		hidegrid : false,
		multiselect:false,
		gridComplete: function(){
			var ids = $msgConfList.jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){
				var id = ids[i];
				var rowData = $msgConfList.jqGrid('getRowData',id);
				var runStatus = rowData.runStatus;
				var mp = "";
				
				if("R" != runStatus ){
					mp= '<a href="#" class="btn btn-link btn-jqgrid" title="运行" onclick="mailStatusOperate(\''+id+'\',\'R\');"  ><i class="fa fa-check-circle-o"></i></a>';
				}else{
					mp='<a href="#" class="btn btn-link btn-jqgrid" title="停止" onclick="mailStatusOperate(\''+id+'\',\'S\');"  ><i class="fa fa-ban"></i></a>';
				}
				
				var ve = '<a href="#" class="btn btn-link btn-jqgrid" title="详情" onclick="viewMsgConf(\''+id+'\');"  ><i class="fa fa-file-text-o"></i></a>';
				var update = '<a date-id="' + id + '" href="#" class="btn btn-link btn-jqgrid" title="修改" data-toggle="modal" onclick="updateMsgConf(\''+id+'\');" data-target="#modal-edit"><i class="fa fa-pencil-square-o"></i></a>';
				var send = '<a href="#" class="btn btn-link btn-jqgrid" title="立即发送" onclick="runMsgConf();"><i class="fa fa-share-square-o"></i></a>';
				jQuery("#msgConfList").jqGrid('setRowData',ids[i],{option:mp+ve+update+send});
			}
		}
		
	});
	$msgConfList.navGrid('#msgConfPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	$msgConfList.jqGrid('setFrozenColumns');
	jqGridResize($msgConfList);	
});

function updateMsgConf(id){
	var actionUrl =  PATH_PREFIX+"msgConfUpdateView.xhtml?msgConfId=" + id;
	openDialog(actionUrl);
}
function viewMsgConf(id){
	var actionUrl =  PATH_PREFIX+"msgConfDetailView.xhtml?msgConfId=" + id;
	openDialog(actionUrl);
}

function runMsgConf(){
	var actionUrl =  PATH_PREFIX+"sendMail.do";
	ctools.confirm("您确定要发送邮件吗？",function(){
		$.ajax({
			//任务生效
			url: actionUrl,  
			dataType: "text",
			type: "POST",
			cache: false,
			async: false,
			success: function(data) {
				if(data=="success"){
					ctools.alert("邮件已发送，稍后请查询结果！","","info");
					queryByCondtion(false);
				}else{
					ctools.alert("邮件发送失败！","","error");
				}
			}
		});
	});
}

function mailStatusOperate(mailConfigId,status){
	var actionUrl =  PATH_PREFIX+"mailStatusOperate.do";
	ctools.confirm("S"== status ?"您确认停止执行此消息配置？":"您确认启动执行此消息配置？",function(){
		$.ajax({
			//任务生效
			url: actionUrl,  
			dataType: "json",
			data :{
				mcId : mailConfigId,
				runStatus :status
			},
			type: "POST",
			cache: false,
			async: false,
			success: function(data) {
				if(!!data){
					if("1" == data.resultCode){
						ctools.alert("操作成功！","","success");
					}else{
						ctools.alert(data.resultMsg,"","error");
					}
				}
				queryByCondtion(false);
			}
		});
	});
}

function queryByCondtion (flag)
{
	var title = $("#q-title").val();
    var postData = $("#msgConfList").jqGrid("getGridParam", "postData");
    //将filters参数串加入postData选项  
    $.extend(postData,{
    	'sp[title]':title
    });
    if (flag){
    	$("#msgConfList").trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    }else{
    	$("#msgConfList").trigger("reloadGrid");//重新载入Grid表格
	}
}

// 对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function() {
	var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
	toggleFullScreen(document.documentElement);
	// 全屏的时候将几个模态框放到下面去
	$('.modal[role="dialog"]').appendTo($wrapper);
})