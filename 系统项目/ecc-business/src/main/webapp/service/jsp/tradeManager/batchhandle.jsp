<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<title>批量处理</title>
<style type="text/css">
.control-input{
	background: #fff;
    width: 250px;
    height: 38px;
    border: 1px solid #e7e7e7;
    padding: 0 10px;
}
.btn-primary{
	margin-left: 20px;
	width: 100px;
}
.input-group{
	line-height: 41px;
}
</style>
</head>
<body class="fixed-nav gray-bg">
<form action="/capitalService/server/importData.xhtml" method="post" id="submitFrom" name="submitFrom" enctype="multipart/form-data" >
<table class="table table-bordered" style="width: 99%;margin: 0 auto;">
	<colgroup>
        <col width="80%">
    </colgroup>
    <tbody>
    	<tr>
    		<td>
    			<h4>批量导入</h4>
    		</td>
    	</tr>
    </tbody>
    <tbody style="border-top: 0px;">
		<tr>
	  		<td>认购、申购、赎回交易批量导入</td>
  		</tr>
	  	<tr>
	  		<td>
				<div class="col-md-4 input-group" style="width: 600px;">
					<input id="errorFileName" type="hidden">
					<a id="errorFileNameDown" style="display: none" href="" target="_Blank">下载失败数据</a>
					<input id="lefile" type="file" name ="lefile" style="display: none">
					<span class="input-group-addon" onclick="$('input[id=lefile]').click();" style="cursor: pointer; background-color: #e7e7e7; max-height: 35px;line-height: 1px;">
					<i class="fa fa-folder-open"></i>选择文件:</span>
					<input id="photoCover" name="todo" readonly="readonly" onclick="$('input[id=lefile]').click();$('#importResult-BG').html('');" class="control-input" type="text">
					<button class="btn btn-primary" type="button" id="importData" onclick="importDataFun();">导&nbsp;&nbsp;入</button>
					<a id="downImportTemp" style="display: none" href="<%=context %>web/template/tradeDataImport/TransactionBatchImport.xls" target="_Blank">模板下载</a>
					<button class="btn btn-primary" type="button" onclick="downImportTempFun();">模板下载</button>
				</div>
			</td>
	  	</tr>
	  	<tr class="errorTr" style="display: none;">
	  		<td><span id="errorMsg"></span></td>
	  	</tr>
	  	<!-- <tr class="errorFile" style="display: none;">
	  		<td>
	  			<input value="" name="fileName" id="fileNmae" type="hidden"/>
	  			<input type="button" style="width: auto;margin: 0;" name="exportExcel" class="btn btn-primary bigbtn" id = "exportExcel" value="下载失败数据" onclick="doUpload();" />
	  		</td>
	  	</tr> -->
	</tbody>
</table>
</form>
<div id="importResult-BG">

</div>
<div id="importResultTemp" style="display:  none;">
	<table class="table table-bordered" style="width: 99%;margin: 0 auto;">
		<colgroup>
	        <col width="10%">
	        <col width="90%">
	    </colgroup>
		<tbody style="border-top: 0px;" >
			<tr id="downErrorFileNameTr" style="display:  none;">
		    	<td colspan="2">
		    		<button class="btn btn-primary" type="button" onclick="downErrorFileNameFun();">下载失败数据</button>
		    	</td>
		    </tr>
			<tr>
		  		<td colspan="2">
		  			<h4>导入结果：{resultMsg}</h4>
		  		</td>
	  		</tr>
			<tr>
		  		<td>总记录条数：</td>
		  		<td>{importTotal}</td>
	  		</tr>
	  		<tr>
		  		<td>成功条数：</td>
		  		<td>{successTotal}</td>
	  		</tr>
	  		<tr>
		  		<td>失败条数</td>
		  		<td>{errorTotal}</td>
	  		</tr>
		</tbody>
	</table>
</div>

<div id="importErrorInfoTemp" style="display:  none;">
	<table  class="table table-bordered" style="width: 99%;margin: 0 auto;">
		<colgroup>
	        <col width="10%">
	        <col width="90%">
	    </colgroup>
	    <tr>
	  		<td colspan="2">
	  			<h4>失败记录：</h4>
	  		</td>
	 	</tr>
	    <tr style="text-align: center;font-weight: bold;">
	  		<td>Excel行号</td>
	  		<td>错误信息</td>
		</tr>
		<tbody class="importErrorInfoText" style="border-top: 0px;">
		</tbody>
	</table>
</div>
</body>
<script type="text/javascript">
$('input[id=lefile]').change(function() {
	$('#photoCover').val($("#lefile").val().match(/[^\\]*$/)[0]);  
});

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
		/* toastr.warning('', '请选择要上传的文件！');
		return false; */
		$("#photoCover").click();
		return false;
	}
	var suffix = filename.substring(filename.lastIndexOf("."), filename.length).toLowerCase();
	if(suffix != ".xls"){	//判断选择要上传的文件是否正确
		toastr.warning('', '请选择正确的Excel文件，以.xls结尾！');
		return false;
	}
	$("#importData").text("请稍后...");
	$("#importData").attr("disabled",true);
	$("#submitFrom").attr("action","/capitalService/server/importData.xhtml");
	var form = new FormData();
	form.append("lefile",document.getElementById("lefile").files[0]);
	$.ajax({
		url: '<%=context%>capitalService/server/importData.xhtml',
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
			var targetDiv = $("#importResult-BG");
			var importResultText = $("#importResultTemp").html();//$("#importResultTemp").html();
			var importErrorInfoText = "";//$("#importErrorInfoTemp").html();
			if(!!data){
				$("#errorFileName").val(data.filePath);
				for(var field in data){
					var resultValue = data[field];
					if("resultMsg" == field && "0000"==data.resultCode){
						resultValue = "";
					}
					importResultText = importResultText.replace(new RegExp("\\{" + field + "\\}", "g"), resultValue); 
				}

				var showText = "";
				if(!!data.errorList && data.errorList.length > 0 ){
					importErrorInfoText = $("#importErrorInfoTemp").html();
					//如有导入失败信息
					for(var i=0;i<data.errorList.length;i++){
						var validErrorInfo = data.errorList[i];
						showText += "<tr>";
						showText += "<td>"+validErrorInfo.excelrowid+"</td>";
						showText +=	"<td>";
						showText += validErrorInfo.errmsg;
						showText +=	"</td></tr>";
					}
				}
				targetDiv.html(importResultText+importErrorInfoText);
				targetDiv.find("tbody[class=importErrorInfoText]").html(showText);
				
				if(!!data.errorTotal && parseInt(data.errorTotal) > 0){
					$("#downErrorFileNameTr").show();
				}
			}
		}
	});
}


function downErrorFileNameFun(){
	var errorFileName = $("#errorFileName").val();
	var url = "<%=context%>"+"capitalService/server/downloadErrorInfo.xhtml?fileName="+errorFileName;
	$("#errorFileNameDown").attr("href",url);
	$("#errorFileNameDown")[0].click();
}

//删除左右两端的空格
function trim(str){ 
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

function downImportTempFun(){
	$("#downImportTemp")[0].click();
}
</script>
</html>