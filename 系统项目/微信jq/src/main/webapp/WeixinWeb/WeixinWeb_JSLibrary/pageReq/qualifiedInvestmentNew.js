var qualifiedInvest={
	queryUserFinOrInvest:qualified.queryUserFinOrInvestMethod(),
	initPage:function(){
		this.queryFileList();
		this.eventBind();
	},
	queryFileList:function(){
    	var _self=this;
		$.ajax({
            url  :'/WeixinService/business/queryFileUploadRecord.xhtml',
            type : 'GET',
            dataType : 'json',
            async:false,
            success : function(data) {
                if(data.returnCode=="0000"){
                	if(data.data.length>0){
	            		var extendInfo=qualified.dataReorganization(data.data);
	            		 if(extendInfo.investmentCertificate){
		                	 var investmentCertificate=extendInfo.investmentCertificate 
		                }
		                if(investmentCertificate.length>0){
		                	  $("#fileAwaitSecond").hide();
		                	  $("#fileItemSecond").show()
		                	 _self.renderUploadEL(investmentCertificate,"#fileItemSecond","investCertificate")
		                }
		               if(investmentCertificate==""){
		                	  $("#fileAwaitSecond").show();
		                	  $("#fileItemSecond").hide()
		                }
		                
	            	}else{
	            		  $("#fileAwaitSecond").show();
		                 $("#fileItemSecond").hide()
	            	}
	            	_self.maskDisable();
                }
            }
        })
	},
	eventBind:function(){
		$(".header h2").text("投资经历证明 ");
		$('#investFileUpload').unbind().change(function(){
            if(qualified.judgeFile(this.files[0].name)=="other"){
                 $(document).dialog({
				    overlayClose: true,
				    content: '文件格式不符合要求，请重新选择',
				}); 
            }else{
                qualified.fileSizeJudge(this,"investCertificate","qualifiedInvest","fileItemSecond")
            }
       })
	},
	investmentUpFile:function(){
		$("#investFileUpload").click();
	},
	//  渲染附件信息
	renderUploadEL:function(param,el,fileType){
		var html="";
		var lastChild="";
		var imageLength=qualified.fileTypeNum(fileType,"image");
        var veidoLength=qualified.fileTypeNum(fileType,"veido");
        var totalLength=imageLength+veidoLength;
        if(totalLength<10){
        	if(fileType=="financialCertificate"){
        		lastChild='<li id="filelaterFirst" onclick="qualifiedInvest.financialUpFile()"></li>';
        	}else{
        		lastChild='<li id="filelaterFirst" onclick="qualifiedInvest.investmentUpFile()"></li>';
        	}
        }
        var i=0;
		param.forEach(function(e){
		  var type=qualified.judgeFile(e.filename);
		  if(type=="viedo"){
		  	var JSESSIONID=getCookie("JSESSIONID")
		  	var src='/WeixinService/business/amazon/downloadVideo/'+e.key+'.xhtml?jsessionid='+JSESSIONID;
		  }else{
		  	var src='/WeixinService/business/amazon/download/'+e.key+'.xhtml';
		  }
	   	  if(type=="image"){
	   	  	i++;
	   	  	html+='<li data-type="image" data-index='+i+'><i onclick="qualified.deleteCurrentFile(\''+e.recordId+'\',\'4\',\''+e.key+'\',\''+fileType+'\')"></i><img src='+src+'></li>' 
	   	  }else if(type=="pdf"){
	   	  	html+='<li data-type="pdf"><i onclick="qualified.deleteCurrentFile(\''+e.recordId+'\',\'4\',\''+e.key+'\',\''+fileType+'\')"></i><a href='+src+'><img src="/WeixinWeb/WeixinWeb_Images/images/qualified/pdfDefault.jpg"></a></li>'  
	   	  }else if(type=="viedo"){
	   	  	html+='<li data-type="viedo"><i onclick="qualified.deleteCurrentFile(\''+e.recordId+'\',\'4\',\''+e.key+'\',\''+fileType+'\')"></i><a href="javascript:qualified.play(\''+src+'\')"><img src="/WeixinWeb/WeixinWeb_Images/images/qualified/veidoDefault.jpg"></a></li>'  
	   	  }else{
	   	  	html+='' 
	   	  }
		})
		$(el).html("").append(html+lastChild)
	},
	maskDisable:function(){
		 if(this.queryUserFinOrInvest.investCertificate){
		 	$("#investMask").show()
		 }else if(!this.queryUserFinOrInvest.investCertificate){
		 	$("#investMask").hide()
		 }
	}
}
qualifiedInvest.initPage()
