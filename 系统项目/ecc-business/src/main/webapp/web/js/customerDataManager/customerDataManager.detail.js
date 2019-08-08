var PRIMARY_PATH = "";
var BASE_PATH = "";

var $riskChangeLogList = $("#riskChangeLogList");

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}		

$(function(){
	$riskChangeLogList.jqGrid({
        url: PRIMARY_PATH+'/queryChangeRecord.do',
        caption: '变动历史记录<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
        datatype: "json",
        postData : queryPostData(),
        colNames: ["id","变动项目","变动明细","变动方式","变动时间"],
        colModel: [
            { name: 'changeId', index: 'changeId', width: 50, align:'left', resizable:true, hidden: true, key: true, sortable: false },
            { name: 'changeProject', index: 'changeProject', width: 150, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'changeDesc', index: 'changeDesc', width: 150, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'changeWay', index: 'changeWay', width: 150, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'updateTime', index: 'updateTime', width: 150, align:'left', resizable:true, hidden: false, key: false, sortable: false }
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
        height: '350',
        width: 'auto',
        autowidth:true,
//        shrinkToFit:false,
        editurl: '',
        viewrecords: true,
        autoScroll: true,
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
        pager: "#riskChangeLogListPage",
        hidegrid: false,
		subGrid: false,
		gridComplete: function() {
			var ids = $riskChangeLogList.jqGrid('getDataIDs');
			for (var i = 0; i < ids.length; i++) {
				var id = ids[i];
				var rowData = $riskChangeLogList.jqGrid('getRowData', id);
				var changeProject = rowData.changeProject;
		      	 if(changeProject == '0'){
		      		changeProject = "风险等级";
		      	 }else if(changeProject == '1'){
		      		changeProject = "专业类型";
		      	 }
		      	$riskChangeLogList.jqGrid('setRowData', id, {
					changeProject : changeProject
				});
			}
		}
    });
	$riskChangeLogList.navGrid('#riskChangeLogListPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
	$riskChangeLogList.jqGrid('setFrozenColumns');
    jqGridResize($riskChangeLogList);
});
	var score=0;	
	var answer=0;
	var specriskLevel = 0;
	var risklevel = 1;
	var hidAnswer = "";
	$(function(){
		$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
		showPageInfo();
	});		
	
	function showPageInfo(){
		var invtp = $("#hidinvtp").val();
		var invprtp=$("#invprtp").val();
		var regioncode = $("#hidregioncode").val();
		var orgQuestion = $("#orgUser");
		var personQuestion = $("#personUser");
		var riskResetButton = $("#riskResetButton"); //重置按鈕
		var riskTd = $("#riskTd");//风险承受能力
		var riskSelect = $("#riskSelect"); //风险承受能力文本
		var fileTd = $("#fileTd");		//录音文件
		var fileTdx = $("#fileTdx");		//录音文件文本域
		//默认隐藏
		personQuestion.hide();
		orgQuestion.hide();
		var dtoAnswer = $("#dtoAnswer").val();
		if(dtoAnswer == null){
			dtoAnswer = "";
		}else{
			dtoAnswer = dtoAnswer.replace(",","-");
		}
		if(invprtp == null && invprtp.length == 0){
			fileTd.hide();
			fileTdx.hide();
			riskTd.show();
			riskSelect.show();
		}else if(invprtp == '1'){
			fileTd.hide();
			fileTdx.hide();
			riskTd.show();
			riskSelect.show();
		}else if(invprtp == '0'){
			fileTd.show();
			fileTdx.show();
			riskTd.hide();
			riskResetButton.hide();
			riskSelect.hide();
		}
		if(regioncode != "" || regioncode.length >0){
			riskTd.hide();
			riskResetButton.hide();
			riskSelect.hide();
		}
		if(invprtp == '1' ){
			if(invtp == '1' && (regioncode == "" || regioncode.length == 0)){
				personQuestion.show();
				hidAnswer = dtoAnswer.replace('A', '2').replace('B', '4').replace('C', '6').replace('D', '8').replace(new RegExp("E","g"), "10");
			}else if(invtp == '0' && (regioncode == "" || regioncode.length == 0)){
				orgQuestion.show();
				hidAnswer = dtoAnswer.replace('A', '5').replace('B', '4').replace('C', '3').replace('D', '2').replace('E', '1');
			}
		}
	}
	$(document).ready(function() {
		var invtp=$("#hidinvtp").val();
		var invprtp=$("#invprtp").val();
		$("#hidinvprtp").change(function() {
			extInfo();
		});
		disbaledInputs();
	});

function extInfo() {
	var invtp=$("#hidinvtp").val();
	if ($("#hidinvprtp").val() == 1) {
		$('#fileTd').hide();
		$('#fileTdx').hide();
		if($('#hidregioncode').val()==null || $('#hidregioncode').val() == ''){
			if(invtp == '0'){
				//initOrgAnswer();
				$('#orgUser').show();
			}else if(invtp == '1'){
				//initPersonAnswer();
				$('#personUser').show();
			}
			$('#riskTd').show();
			$('#riskSelect').show();
			$('#riskResetButton').show();
		}
	} else {
		$('#fileTd').show();
		$('#fileTdx').show();
		$('#orgUser').hide();
		$('#riskResetButton').hide();
		$('#personUser').hide();
		if($('#hidregioncode').val()==null || $('#hidregioncode').val() == ''){
			$('#riskTd').hide();
			$('#riskSelect').hide();
		}
		if ($('#fileno').val() == null || $('#fileno').val() == '') {
			$('#hidfileno').val("");
		}
	}
}

function disbaledInputs(){
	//input组件禁用
	$("input[name=hidfileno]:eq(0)").attr("disabled","disabled").attr("readonly","readonly");
	//select组件禁用
	$('#hidinvprtp').select2("destroy");
	//$('#hidinvprtp').removeClass('select2');
	$("#hidinvprtp").attr("disabled","disabled").attr("readonly","readonly");
}

function queryPostData(){
	
	var postData = {};
	var custNo = $("#hidcustno").val();
	postData.sp={};
	postData.sp.custNo = custNo;
	return postData;
}
	
