
var qualifiedAssets={
	queryUserFinOrInvest:qualified.queryUserFinOrInvestMethod(), 
	initPage:function(){
		qualified.tabChange(".tab li", ".improveText", "cur")
	    $("#upLoadFinancialBtn").click(function () {
	        if($("#financialNameRead").text()==""){
	           DJ.dialog.error("请选择文件后再上传");
	        }else{
	           qualified.ajaxSubmit("financialCertificate","financialPro","financialNameRead","financialFileUpload","qualifiedAssets");
	        }
        });
	    $('#financialFileUpload').unbind().change(function(){
            if(qualified.judgeFile(this.files[0].name)=="other"){
                DJ.dialog.error("文件格式不符合要求");
            }else{
                qualified.fileSizeJudge(this,"financialNameRead","financiList")
            }
        })
	},
	queryFileList:function(){
		var _self=this;
		$.ajax({
            url  :'/AppService/business/queryFileUploadRecord.xhtml',
            type : 'POST',
            dataType : 'json',
            async:false,
            success : function(data) {
            	if(data.returnCode=="0000"){
            	     if(data.data.length>0){
	            		var extendInfo=qualified.dataReorganization(data.data);
	            		if(extendInfo.financialCertificate.length>0){
	            			 var financialCertificate=extendInfo.financialCertificate;
	            			 _self.renderUploadEL(financialCertificate,"#financiList","financialCertificate");
	            			 $(".hasFile").show();
	            			 $(".noneFile").hide();
	            		}else{
	            			 $(".hasFile").hide();
	            			 $(".noneFile").show();
	            		}
	            	}
	            	_self.maskDisable()
            	}

            }
        })
	},
	financialUpFile:function(){
		$("#financialFileUpload").click();
	},
	/**
	 * 渲染附件元素
	 */
	renderUploadEL:function(param,el,filetype){
	   var _self=this;
	   $(el).html("").append(param.map(function(e){
	   	  var type=qualified.judgeFile(e.filename);
	   	  var el="";
	   	  if(type=="image"){
	   	  	fileEl='<li class="image" data-type="image"><em></em><b data-key='+e.key+' onclick="qualified.queryCurrentFile(this,\'image\')">'+e.filename+'</b><i onclick="qualified.deleteCurrentFile(\''+e.recordId+'\',\'3\',\''+e.key+'\',\''+filetype+'\')"></i></li>' 
	   	  }else if(type=="pdf"){
	   	  	fileEl='<li class="pdf" data-type="pdf"><em></em><b data-key='+e.key+' onclick="qualified.queryCurrentFile(this,\'pdf\')">'+e.filename+'</b><i onclick="qualified.deleteCurrentFile(\''+e.recordId+'\',\'3\',\''+e.key+'\',\''+filetype+'\')"></i></li>'  
	   	  }else if(type=="viedo"){
	   	  	fileEl='<li class="viedo"  data-type="viedo"><em></em><b data-key='+e.key+' onclick="qualified.queryCurrentFile(this,\'viedo\')">'+e.filename+'</b><i onclick="qualified.deleteCurrentFile(\''+e.recordId+'\',\'3\',\''+e.key+'\',\''+filetype+'\')"></i></li>'  
	   	  }else{
	   	  	fileEl='' 
	   	  }
	   	  return fileEl;
	    }).join(""))
	},
	maskDisable:function(){
		if(this.queryUserFinOrInvest.financialCertificate){
			$("#financialMask").show()
			$("#financialMask").siblings(".proUpload").hide()
		}
		else if(!this.queryUserFinOrInvest.financialCertificate){
		    $("#financialMask").hide();
		    $("#financialMask").siblings(".proUpload").show()
		}
	}
}
qualifiedAssets.initPage()
qualifiedAssets.queryFileList()
