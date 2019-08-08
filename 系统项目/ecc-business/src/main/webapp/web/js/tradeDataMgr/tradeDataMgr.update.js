var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(document).ready(function(){
	  $('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
	  showdiv();
}); 


function showdiv(){
	var contractsign=document.getElementById("contractsign").value;
/*	document.getElementById("isoriginal").value="<%=isoriginal%>";
*/	 
	//纸质合同 展示
	if(contractsign=='1'){
		 document.getElementById("isoriginaldiv").style.display = "";
		 document.getElementById("isoriginal2").style.display = "";
		
	}else{
		 document.getElementById("isoriginaldiv").style.display = "none";
		 document.getElementById("isoriginal2").style.display = "none";
	}
}


function doSubmit()
{
	var serialno =  $("#serialno").val(); 
	var contractsign = $("#contractsign").val();        
	var contractdevolve = $("#contractdevolve").val();   
	var isoriginal = $("#isoriginal").val();
	var filed = $("#filed").val();
	var remark = $("#remark").val();
	var istradeform = $("#istradeform").val();
	
	if("1"==contractsign&&(""==isoriginal||isoriginal==null)){
		ctools.alert("请选择合同是否原件!",'','error');
	}else{
	  if(remark!=""&&remark.length>20){
		  ctools.alert("备注不要超过20个汉字!",'','error');
	  }else{
		  var data = {
				  serialno : serialno,
				  contractsign : contractsign,
				  contractdevolve : contractdevolve,
				  isoriginal : isoriginal,
				  filed : filed,
				  remark : remark,
				  istradeform : istradeform
		  };
		  $.ajax({
				type: 'POST',
			    url : "modifyTradeData.xhtml", 
				dataType:'json',
				data:data,
				contentType: "application/x-www-form-urlencoded",
				success: function(data){
					if(data.ResultCode=='0000'){
						window.location.href=BASE_PATH +  "service/jsp/hint/success.jsp";
					}else{
						swal('修改失败',data.ResultMsg,"error")
					}
				},
				error:function(xhr){
					switch(xhr.status){
						case 403:sweetAlert("对不起，您无此权限！","","error");break;
						case 404:sweetAlert("对不起，无此页面！", "","error");break;
						case 500:sweetAlert("内部错误，请联系管理员！","","error");break;  
						case 504:sweetAlert("超时，请联系管理员！", "","error");break;  
						case 417:sweetAlert("内部错误，请联系管理员！", "","error");break;  
					}
				}
			});	  
	  }
	}
}
