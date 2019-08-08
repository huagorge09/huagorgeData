var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}		

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
			//初始化单选框选中
			/*if( invprtp == '1' ) {
				if(invtp == '0'){
					initOrgAnswer();
				}else if(invtp == '1'){
					initPersonAnswer();
				}
			}*/
			$('#riskResetButton').click(function(e){  
				var radio=$('input:radio');
			    $.each(radio, function(i,v){  
			    	var name = v.name;
			    	$('input:radio[name="'+name+'"]').attr("checked",false);
			   });
	        });  
		});
		//初始化单选框选中
		/*function initOrgAnswer(){
			var ss = hidAnswer;  
			var bb = ss.split('-');   
			for(var i = 0; i <bb.length; i++){
						var j = i+1;
						 if(i==10){
							var zz=bb[10].split('&');  
							for(var z = 0;z<zz.length;z++){
								 if(zz[z] == 1){
								 	$('input:radio[name="oq11_5"]').attr("checked",true);
								 }else if(zz[z]==2){
								 	$('input:radio[name="oq11_4"]').attr("checked",true);
								 }else if(zz[z]==3){
								 	$('input:radio[name="oq11_3"]').attr("checked",true);
								 }else if(zz[z]==4){
								 	$('input:radio[name="oq11_2"]').attr("checked",true);
								 }else if(zz[z]==5){
								 	$('input:radio[name="oq11_1"]').attr("checked",true);
								 }
							}
						}else{ } 
						$('input:radio[name="oq'+j+'"][value="'+ bb[i] + '"]').attr("checked",true);
						
						
			}
		}
		function initPersonAnswer(){
			var ss = hidAnswer;  
			var bb = ss.split('-');  
			for(var i = 0; i <bb.length; i++){
				var j = i+1;
				$('input:radio[name="pq'+j+'"][value="'+ bb[i] + '"]').attr("checked",true);
			}
		}*/
		
		
		function getPersonAnswer(){
			score=0;
			answer=0;
			specriskLevel = 0;
			for(var i = 1; i < 15 ;i++){
				var value =  $('input:radio[name="pq'+i+'"]:checked').val();
				var dataValue =  $('input:radio[name="pq'+i+'"]:checked').attr("data-value");
				dataValue = !dataValue ? "" : dataValue; 
				answer = answer != "" ? answer + ',' + parseInt(value,10)+":"+dataValue :parseInt(value,10)+":"+dataValue;
				score = parseInt(value,10) + parseInt(score,10);
			}
			//请问您是否具有完全民事行为能力
			var isCompAbility = $('input:radio[name="pq13"]:checked').attr("data-value");
			//请问您是否没有风险容忍度或者不愿承受任何投资损失
			var isEndureLoss = $('input:radio[name="pq14"]:checked').attr("data-value");
			if("Y" == isCompAbility || "Y" == isEndureLoss){
				score = 0;
				specriskLevel = 1;
			}
			$('#evalScope').val(score);
			$('#evalAnswer').val(answer);
			$('#specriskLevel').val(specriskLevel);
		}
		
		function getOrganizatioAnswer(){
			score=0;
			answer=0;
			for(var i = 1; i <15 ;i++){
				 if(i==11){
					var value =  0;
					var ss=0;
					for(var j = 1; j < 6;j++){
						var zz =  $('input:radio[name="oq11_'+j+'"]:checked').val();
						if(zz==null)
							zz=0;
						if(j==1 && zz!=null){
							value=zz;
						}
						else if(zz>value){
							value = zz;
						}
						if(zz!=null && zz!=0){
							 ss = ss != '0' ?ss+"&"+parseInt(zz,10):parseInt(zz,10);
						}
					}
					answer =  answer + ',' + ss;
				}else{ } 
				var value =  $('input:radio[name="oq'+i+'"]:checked').val();
				answer = answer != "" ? answer + ',' + parseInt(value,10) : parseInt(value,10);
				score = parseInt(value,10) + parseInt(score,10);
				$('#specriskLevel').val(specriskLevel);
			}	
			
			$('#evalScope').val(score);
			$('#evalAnswer').val(answer);
		}
		
		
		function checkPersonAnswer(){
			for(var i = 1; i <15 ;i++){
			var value =  $('input:radio[name="pq'+i+'"]:checked').val();
				if(value == null){
					ctools.alert('个人投资者风险评估答案不能为空',"","error");
					return false;
				}
			}	
			return true;
	}

	function checkOrganizatioAnswer(){
			for(var i = 1; i <15 ;i++){
				var value =  $('input:radio[name="oq'+i+'"]:checked').val();
				if(value == null){
					ctools.alert('机构投资者风险评估答案不能为空',"","error");
					return false;
				}
		}	
			return true;
	}
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
	

	
	function appendmsg(alertmsg, addmsg) {
		if (alertmsg == '') {
			alertmsg = addmsg;
		} else {
			alertmsg = alertmsg + '\n' + addmsg;
		}
		return alertmsg;
	}

	function checkform() {
		var alermsg = '';
		var hidinvprtp = $("#hidinvprtp").val();
		var hidfileno = $("#hidfileno").val();
		var invprtp = $("#invprtp").val();
		
		var hidregioncode = $("#hidregioncode").val();
		var hidapptp = $("#hidapptp").val();
		var hidappst = $("#hidappst").val();
		
		
		if (hidregioncode != null && hidregioncode != ''
				&& (hidapptp != '0' || hidappst != 'N')) {
			if (invprtp != hidinvprtp)
				alermsg = "网上客户未进行申请，不能直接修改投资者类型";
			if(invprtp == hidinvprtp && invprtp == 1){
				alermsg = "网上客户未进行申请，不能直接修改投资者类型";
			}
		}

		if (hidinvprtp == '0' && (hidfileno == null || hidfileno.length == 0)) {
			alermsg = "录音编号不能为空";
		}
		if (alermsg != '') {
			ctools.alert(alermsg,"","error");
			return false;
		}
		return true;
	}
	function doSubmit() {
		if (checkform()) {
			var invtp= $("#hidinvtp").val();
			var hidinvprtp = $("#hidinvprtp").val();
			var regioncode = $("#hidregioncode").val();
			if(regioncode == null  || regioncode == ''){
				if(hidinvprtp =='1'){
				if(invtp == '0'){
					if(!checkOrganizatioAnswer()){
						return false;
					}
					getOrganizatioAnswer();
					
					 if(score >= 14 && score <=23){
				        	riskLevel = "1";
				        }else if(score >= 24 && score <=33){
				        	riskLevel = "2";
				        }else if(score >= 34 && score <=43){
				        	riskLevel = "3";
				        }else if(score >= 44 && score <=56){
				        	riskLevel = "4";
				        }else if(score >=58){
				        	riskLevel = "5";
				        }else{
				        	riskLevel = "1";
				        }
				}else if(invtp =='1'){
					if(!checkPersonAnswer()){
						return false;
					}
					getPersonAnswer();
					
					 if(score >= 24 && score <=34){
				        	riskLevel = "1";
				        }else if(score >= 35 && score <=48){
				        	riskLevel = "2";
				        }else if(score >= 49 && score <=62){
				        	riskLevel = "3";
				        }else if(score >= 63 && score <=80){
				        	riskLevel = "4";
				        }else if(score > 81){
				        	riskLevel = "5";
				        }else{
				        	riskLevel = "1";
				        }
				}
				  $('#hidrisklevel').val(riskLevel);
				}
			}
			/*if (!window.confirm("是否确认修改？")) {
				return false;
			}*/
			/*document.getElementById("dopass").disabled = true;
			brokerModifyForm.action = "custRiskAction.jsp";
			brokerModifyForm.submit();
			*/
			
			ctools.confirm({title:'是否确认修改?',text:''},function(confirm){
				if(confirm){
					var hidinvtp = $("#hidinvtp").val();
					var hidregioncode = $("#hidregioncode").val();
					var hidrisklevel = $("#hidrisklevel").val();
					var specriskLevel = $("#specriskLevel").val();
					var evalAnswer = $("#evalAnswer").val();
					var evalScope = $("#evalScope").val();
					var operatorId = $("#operatorId").val();
					var hidcustno = $("#hidcustno").val();
					var invnm = $("#invnm").val();
					var invtp = $("#invtp").val();
					var hidfileno = $("#hidfileno").val();
					var hidinvprtp = $("#hidinvprtp").val();
					var invprtp = $("#invprtp").val();
					var fileno = $("#fileno").val();
					var data = $("#brokerModifyForm").serializeArray();
					/*var data = {
							"hidinvtp" : hidinvtp,
							"hidregioncode" : hidregioncode,
							"hidrisklevel" : hidrisklevel,
							"specriskLevel" : specriskLevel,
							"evalAnswer" : evalAnswer,
							"evalScope" : evalScope,
							"operatorId" : operatorId,
							"hidcustno" : hidcustno,
							"invnm" : invnm,
							"invtp" : invtp,
							"hidfileno" : hidfileno,
							"hidinvprtp" : hidinvprtp,
							"invprtp" : invprtp,
							"fileno" : fileno
					}*/
					var actionUrl= PRIMARY_PATH + "/updateCustData.xhtml";
					$.ajax({
						type: 'POST',
						url: actionUrl,
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
			})
			
		}
	}