/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";
var custDataDocList = $('#custDataDocList');
function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	$('input[id=lefile]').change(function() {
		$('#photoCover').val($("#lefile").val().match(/[^\\]*$/)[0]);  
	});
	
	initGrid();
});



function initGrid(){
	custDataDocList.jqGrid({
		url: PRIMARY_PATH+'/queryCustDataDocAllList.do',
        caption: '已上传附件<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
        datatype: "json",
        postData :queryPostData(),
        colNames: ["附件id","附件大小","附件名称","操作"],
        colModel: [
            { name: 'storageid', index: 'storageid',  hidden: true, key: true, sortable: false},
            { name: 'filesize', index: 'filesize',  hidden: true, key: false, sortable: false},
            { name: 'filename', index: 'filename', formatter: formatFileName , width:150, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'option', index:'option',width:100, align:'left', resizable:true, resizable: true, sortable: false }
        ],
        rowNum: 10,
        rowList: [10, 20, 30, 50],
        rownumbers: true,
        rownumWidth: 50,
        prmNames: {search:"search",page:"pageNo",rows:"limit"},
        height: 'auto',
        width: false,
        autowidth:true,
        shrinkToFit: true,
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
        pager: "#custDataDocPage",
        viewrecords: true,
        hidegrid: false,
		subGrid: false,
		gridComplete: function() {
			var ids = custDataDocList.jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){
				var id = ids[i];
				var rowData = custDataDocList.jqGrid('getRowData', id);
				var storageid = rowData.storageid;
				//按钮置灰
				$(".permissionBtn a").attr("disabled", "disabled");
                $(".permissionBtn a").removeAttr("onclick");
                
                //初始化修改按钮
                $("#custDataDocDelBtn a").removeAttr("disabled");
            	$("#custDataDocDelBtn a").attr("onClick", "custDataDocDelFunc('"+storageid+"');");
				
            	var cub = $("#custDataDocDelBtn").html();
            	
            	custDataDocList.jqGrid('setRowData',ids[i],{option:cub});
			}
		}
    });
	custDataDocList.navGrid('#custDataDocPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
	custDataDocList.jqGrid('setFrozenColumns');
    jqGridResize(custDataDocList);
}

function queryPostData(){
	var postData={};
	postData.sp={};
	postData.sp.fundacct = $("input[name=fundacct]").val();
	return postData;
}

/**
 * 查询按钮
 * 根据条件查询数据
 * @param flag
 */
function queryByCondtion(flag){
    var postData = custDataDocList.jqGrid("getGridParam", "postData");
    $.extend(postData,queryPostData());
    if (flag) {
    	custDataDocList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	custDataDocList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

function formatFileName(text, options, rData){
	var storageid = rData["storageid"];
    return '<a href="javascript(0);"  class="showunderline" data-toggle="modal" data-target="#modal-info"  onclick="custDataDocDownloadFunc('+storageid+')" >' + text + '</a>';
}

function custDataDocDelFunc(storageid){
	ctools.confirm({title : "您确认执行该操作吗？"},function(isConfirm){
		var actionUrl = PRIMARY_PATH +"/custDataDocDel.do";
		if(isConfirm){
			$.ajax({
				type: 'POST',
				url: actionUrl,
				data :{
					"storageid" : storageid
				},
				async: false,
				success: function(data){
					if(!!data){
						var resultCode = data.resultCode ;
						var resultMsg = data.resultMsg;
						if("0000" == resultCode ){
							ctools.alert("删除成功","","success");
							queryByCondtion(true);
						}else{
							ctools.alert(resultMsg,"删除失败","warning");
						}
					}else{
						ctools.alert("未知返回，请刷新后重试！","","warning");
					}
				},
				error:function(xhr){
					swal("删除失败!", "", "error");
				}
			});
		}
		
	});
}

function custDataDocDownloadFunc(storageid){
	var url = PRIMARY_PATH + "/custDataDocDownload.do?storageid="+storageid;
	window.open(url,"_self");
}

/**
 * 导入数据
 */
function importDataFun(){
	$("#importResult-BG").html("");
	$("#errorMsg").html("");
	$("#fileNmae").val("");
	$(".errorTr").hide();
	$(".errorFile").hide();
	var filename = $('#photoCover').val();
	if(trim(filename) == ""){
		toastr.warning('', '请选择要上传的文件！');
		return false;
	}
	/*var suffix = filename.substring(filename.lastIndexOf("."), filename.length).toLowerCase();
	if(suffix != ".xls"){	//判断选择要上传的文件是否正确
		toastr.warning('', '请选择正确的Excel文件，以.xls结尾！');
		return false;
	}*/
	$("#importData").text("请稍后...");
	$("#importData").attr("disabled",true);
	$("#submitFrom").attr("action",PRIMARY_PATH+"/uploadCustDataDoc.do");
	var form = new FormData();
	form.append("lefile",document.getElementById("lefile").files[0]);
	form.append("appserialno",$("input[name=appserialno]").val());
	form.append("fundacct",$("input[name=fundacct]").val());
	form.append("custno",$("input[name=custno]").val());
	$.ajax({
		url:  PRIMARY_PATH +'/uploadCustDataDoc.do',
		dataType: "json",
		type: "POST",
		data: form,
		cache: false,
		async: true,
		processData:false,
		contentType:false,
		success: function(data) {
			$("#importData").text("导　入");
			$("#importData").attr("disabled",false);
			if(!!data){
				var resultCode = data.resultCode ;
				var resultMsg = data.resultMsg;
				if("0000" == resultCode ){
					ctools.alert("上传成功","","success");
					queryByCondtion(true);
				}else{
					ctools.alert(resultMsg,"上传错误","warning");
				}
			}else{
				ctools.alert("未知返回，请刷新后重试！","","warning");
			}
		}
	});
};

//删除左右两端的空格
function trim(str){ 
	return str.replace(/(^\s*)|(\s*$)/g, "");
};