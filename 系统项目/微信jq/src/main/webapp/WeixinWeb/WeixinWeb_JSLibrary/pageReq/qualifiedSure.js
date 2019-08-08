
function submitData(){
		if(qualified.isApply){
			var url="/WeixinService/business/modifyAccreditedInvestorInfoStatus.xhtml";
		}else{
			var url="/WeixinService/business/addAccreditedInvestorInfo.xhtml"
		}
		if($(".next").find("a").hasClass("cur")){
			var _self=this;
			var status=qualified.updateStatus;
			if(status=="F"){
				status="U"
			}else if(status=="U" || status == "R"){
				status="N"
			}
			$.ajax({
	            url  :url,
	            type : 'POST',
	            dataType : 'json',
	            data:{
	            	updateStatus:status
	            },
	            async:true,
	            success : function(data) {
	            	if(data.returnCode=="0000"){
	            		$(document).dialog({
	            			titleShow:false,
						    overlayClose: true,
						    content: '<h2>提交成功</h2><p>您的合规投资者证明已经上传</p><p>我们会在3天内完成审核并告知结果</p><p>有任何疑问请拨打4008878555</p>',
						    onClickConfirmBtn:function(){
						    	location.href="/WeixinService/business/user/queryAccount.shtml"
						    }
						}); 
	            	}else{
	            		$(document).dialog({
						    overlayClose: true,
						    content: '提交失败，请重新操作',
						}); 
	            	}
	            }
	        })
		}
	}