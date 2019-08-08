var PROJEC_TPATH = "";

function setPath(projectPath,path){
	PROJEC_TPATH = projectPath;
}


$('input[id=lefile]').change(function() {
	$('#photoCover').val($("#lefile").val().match(/[^\\]*$/)[0]);  
});

function importExcel() {
	$("#accountData").val("");
	$("#accountInfoList").jqGrid("clearGridData");
	$("#msgDiv").html("");
	var filename = $('#photoCover').val();
	if ($.trim(filename) == "") {
		toastr.warning('', '请先选择文件！！');
		return false;
	}
	var suffix = filename.substring(filename.lastIndexOf("."), filename.length).toLowerCase();
	if (suffix != ".xlsx") { // 判断选择要上传的文件是否正确
		toastr.warning('', '请选择正确的Excel文件，以.xlsx结尾！');
		return false;
	}
	
	$("#importData").text("请稍后...");
	$("#importData").attr("disabled",true);
	$("#submitFrom").attr("action",PROJEC_TPATH + "service/accountManager/batchOpenAccount.xhtml");
	var form = new FormData();
	form.append("lefile",document.getElementById("lefile").files[0]);
	$.ajax({
		url: PROJEC_TPATH + "service/accountManager/batchOpenAccount.xhtml",
		dataType: "json",
		type: "POST",
		data: form,
		cache: false,
		async: true,
		processData:false,
		contentType:false,
		success: function(data) {
			$("#importData").text("上　传");
			$("#importData").attr("disabled",false);
			if(data.resultCode == "0000"){
				if(data.resultList.length > 0){
					var rowText = "";
					for (var i = 0; i < data.resultList.length; i++) {
						rowText += '<p>'+data.resultList[i]+'</p>';
					}
					$("#msgDiv").html(rowText);
					$("#mymodal-data").modal({backdrop: 'static' ,keyboard: "false"});
				}else if(data.data.length > 0){
					$("#accountData").val(JSON.stringify(data.data));
					for (var i = 0; i < data.data.length; i++) {
						$("#accountInfoList").jqGrid("addRowData", i,data.data[i]);
					}
				}
			}else{
				toastr.warning('', "开户文件导入异常："+data.resultMsg);
				return false;
			}
		},
		error: function(e){
			$("#importData").text("上　传");
			$("#importData").attr("disabled",false);
			$("#msgDiv").html("上传文件异常！");
			$("#mymodal-data").modal({backdrop: 'static' ,keyboard: "false"});
		}
	});
}
$(function(){
	initGrid();
	$(".ui-jqgrid-bdiv").css("max-height","612px");
	$("#mymodal-data").on('shown.bs.modal', function(){
	      var $this = $(this);
	      var $modal_dialog = $this.find('.modal-dialog');
	      var m_top = ( $(window).height() - $modal_dialog.height() )/2;
	      $modal_dialog.css({'margin': m_top + 'px auto'});
	});
});

function initGrid(){
	$("#accountInfoList").jqGrid({
		caption: '开户内容列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
        datatype: "local",
        colNames: ["投资者名称","注册登记证件类型","注册登记证件号码","注册登记证件有效期","开户银行","预留银行全称","预留银行开户地","预留银行账号","预留银行户名","法定代表人姓名","法定代表人证件号码","法定代表人证件有效期",
                   "经办人姓名","经办人证件号码","经办人证件有效期","经办人办公电话","经办人传真号码","经办人手机号","经办人电子邮件","经办人通讯地址","经办人邮政编码","办公地址","邮政编码",
                   "机构类型","投资者类型","基金投资受益人名称","业务类型","机构类型(专业)"],
        colModel: [
            { name: 'invnm', index: 'invnm', resizable:true, width: 200, hidden: false, key: false, sortable: false },
            { name: 'idtpnm', index: 'idtpnm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'idno', index: 'idno', resizable:true, width: 150, hidden: false, key: false, sortable: false },
            { name: 'idnolimit', index: 'idnolimit', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'banknonm', index: 'banknonm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'banklongname', index: 'banklongname', width: 200, resizable:true, hidden: false, key: false, sortable: false },
            { name: 'bankaddrnm', index: 'bankaddrnm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'bankacco', index: 'bankacco', resizable:true,width: 200, hidden: false, key: false, sortable: false },
            { name: 'bankacnm', index: 'bankacnm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'principalname', index: 'principalname', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'principalno', index: 'principalno', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'principalvalidate', index: 'principalvalidate', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'contact', index: 'contact', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'contidno', index: 'contidno', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'contvalidate', index: 'contvalidate', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'contphone', index: 'contphone', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'contfax', index: 'contfax', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'contmobile', index: 'contmobile', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'contemail', index: 'contemail', resizable:true, width: 200, hidden: false, key: false, sortable: false },
            { name: 'contAddr', index: 'contAddr', resizable:true, width: 250, hidden: false, key: false, sortable: false },
            { name: 'contPostcode', index: 'contPostcode', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'addr', index: 'addr', resizable:true, width: 250, hidden: false, key: false, sortable: false },
            { name: 'postcode', index: 'postcode', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'insttypenm', index: 'insttypenm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'invprtp', index: 'invprtp', resizable:true, hidden: false, key: false, sortable: false ,
            	formatter:function(cellvalue, options, rowObject){
            		if(cellvalue == "0"){
            			return "专业投资者";
            		}else{
            			return "普通投资者";
            		}
            	}
            },
            { name: 'beneficiary', index: 'beneficiary', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'businesstpnm', index: 'businesstpnm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'investproinstsecondnm', index: 'investproinstsecondnm', resizable:true, hidden: false, key: false, sortable: false }
        ],
        rowNum: 15,
        rowList: [15, 30, 50, 100],
        rownumWidth: 60,
        rownumbers: true,
        prmNames: {search:"search",page:"pageNo",rows:"limit"},
        //height: 'auto',
        height: $(window).height()-235,
        width: false,
        autowidth : true,
        shrinkToFit : false,
        viewrecords : true
    });
	$("#accountInfoList").navGrid('#accountInfoPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
    $("#accountInfoList").jqGrid('setFrozenColumns');
    $(".ui-jqgrid-titlebar-close").remove();
    jqGridResize($("#accountInfoList"));
}

function submitData(){
	var accountData = $("#accountData").val();
	if(accountData == "" || accountData == "[]"){
		toastr.warning('', '请先上传文件！！');
		return false;
	}
	ctools.confirm("您确定要提交开户信息吗？",function(){
		//提交
		$.ajax({
	         url:PROJEC_TPATH + "service/accountManager/batchOpenAccountSave.xhtml",
	         type:"post",
	         async: true,
	         data: {
	        	 	'accountData' : accountData
	        	   },
	         dataType:"json",
	         success:function(data){
	        	 if(data.resultCode == "0000"){
	        		 ctools.alert_sweet(data.resultMsg, "success", "" , function(){
	        			 window.location.reload();
					 });
	        	 } else if(data.resultCode == "0001"){
	        		 if(data.errorList.length > 0){
	 					var rowText = "";
	 					for (var i = 0; i < data.errorList.length; i++) {
	 						rowText += '<p>'+data.errorList[i]+'</p>';
	 					}
	 					$("#msgDiv").html(rowText);
	 					$("#mymodal-data").modal({backdrop: 'static' ,keyboard: "false"});
	 				}
	 			}else{
	 				$("#msgDiv").html(data.resultMsg);
 					$("#mymodal-data").modal({backdrop: 'static' ,keyboard: "false"});
	 				return false;
	 			}
	         }
	      });
	});
}

/**
 * 批量开户模板下载
 */
function downLoadOrderTemp(){
	window.open(PROJEC_TPATH + "service/accountManager/downLoadTemp.xhtml");
}