var isUpPersonImg = false;
var isUpCountryImg = false;
var qualifiedAssets = {
	queryUserFinOrInvest: qualified.queryUserFinOrInvestMethod(),
	initPage: function () {
		this.queryFileList();
		this.eventBind();
	},
	queryFileList: function () {
		var _self = this;
		$.ajax({
			url: '/WeixinService/business/queryFileUploadRecord.xhtml',
			type: 'GET',
			dataType: 'json',
			async: false,
			success: function (data) {
				if (data.returnCode == "0000") {
					var idCardPortraitificate = [];
					var idCardNationalEmblemcate = [];
					if (data.data.length > 0) {
                              
						var extendInfo = qualified.dataReorganization(data.data);
						if (extendInfo.financialCertificate) {
							var financialCertificate = extendInfo.financialCertificate
						}
						if (financialCertificate.length > 0) {
							$("#fileAwaitFirst").hide();
							$("#fileItemFirst").show()
							_self.renderUploadEL(financialCertificate, "#fileItemFirst", "financialCertificate");
						}
						if (financialCertificate == "") {
							$("#fileAwaitFirst").show();
							$("#fileItemFirst").hide()
						}
						//  人像数据
						// 人像图片数据
						if (extendInfo.idCardPortraitificate) {
							idCardPortraitificate = extendInfo.idCardPortraitificate
						}
						//  国徽图片数据
						if (extendInfo.idCardNationalEmblemcate) {
							idCardNationalEmblemcate = extendInfo.idCardNationalEmblemcate
						}
						// if(idCardPortraitificate && idCardNationalEmblemcate){
						// 	localStorage.setItem('isSubmitAllIc',true);
						// }else{
						// 	localStorage.setItem('isSubmitAllIc',false);
						// }
						var serverFilePath = '/WeixinService/business/amazon/download/';
						if (idCardPortraitificate.length > 0) {
							var icPersonImg = idCardPortraitificate[0].key + '.xhtml'; //身份证人像面路径
							var e = idCardPortraitificate[0]; //最新的人像图片信息对象
							isUpPersonImg = true;
							$('#icPersonImg').attr('src', '' + serverFilePath + icPersonImg + '')
							$('#deletePersonImg').show()
							$('#deletePersonImg').attr('onclick', 'qualified.deleteCurrentFile(\'' + e.recordId + '\',\'3\',\'' + e.key + '\',\'' + 'idCardPortrait' + '\')')
						} else {
                                                      
							$('#icPersonImg').attr("src","1")
							isUpPersonImg = false;
							$('#deletePersonImg').hide()
						}

						if (idCardNationalEmblemcate.length > 0) {
							var icCountryImg = idCardNationalEmblemcate[0].key + '.xhtml'; //身份证国徽面路径
							var a = idCardNationalEmblemcate[0];//最新国徽图片信息对象
							$('#icCountryImg').attr('src', '' + serverFilePath + icCountryImg + '')
							isUpCountryImg = true;
							$('#deleteCountryImg').show()
							$('#deleteCountryImg').attr('onclick', 'qualified.deleteCurrentFile(\'' + a.recordId + '\',\'3\',\'' + a.key + '\',\'' + 'idCardNationalEmblem' + '\')')

						} else {
							$('#icCountryImg').attr("src","1")
							isUpCountryImg = false;
							$('#deleteCountryImg').hide()
						}
						if(idCardNationalEmblemcate.length>0 && idCardPortraitificate.length>0){
							$('#IdCardTo').addClass('act')
							$('#IdCardTo').html('已设置')
						}else{
							$('#IdCardTo').removeClass('act')
							$('#IdCardTo').html('未设置')
						}
				
					} else {
						$("#fileAwaitFirst").show();
						$("#fileItemFirst").hide();
						$('#icPersonImg').attr('src', '')
						$('#icCountryImg').attr('src', '')
						$('#deleteCountryImg').hide()
						$('#deletePersonImg').hide()
					}
					_self.maskDisable();
				}
			}
		})
	},
	eventBind: function () {
		$(".header h2").text("收入证明 ");
		$('#financialFileUpload').unbind().change(function () {
			if (qualified.judgeFile(this.files[0].name) == "other") {
				$(document).dialog({
					overlayClose: true,
					content: '文件格式不符合要求，请重新选择',
				});
			} else {
				qualified.fileSizeJudge(this, "financialCertificate", "qualifiedAssets", "fileItemFirst")
			}
		})
		//  上传身份证正面
		$('#idCardPersonFileUpload').unbind().change(function () {
			if (qualified.judgeFile(this.files[0].name) != "image") {
				$(document).dialog({
					overlayClose: true,
					content: '文件格式不符合要求，请重新选择',
				});
			} else {
				qualified.fileSizeJudge(this, "uploadICPersonForm", "qualifiedAssets", "idCardPortrait")
			}
		})
		//  上传身份证反面
		$('#idCardCountryFileUpload').unbind().change(function () {
			if (qualified.judgeFile(this.files[0].name) != "image") {
				$(document).dialog({
					overlayClose: true,
					content: '文件格式不符合要求，请重新选择',
				});
			} else {
				qualified.fileSizeJudge(this, "uploadICCountryForm", "qualifiedAssets", "idCardNationalEmblem")
			}
		})

		$(".downLoad").click(function () {
			$(".sendMail").show()
		})


	},
	financialUpFile: function () {
		$("#financialFileUpload").click();
	},
	//  上传人像面
	idCardPersonUpFile: function () {
		if (isUpPersonImg == false) {
			$('#idCardPersonFileUpload').click()
		}
	},
	//  上传国徽面
	idCardCountryUpFile: function () {
		if (isUpCountryImg == false) {
			$('#idCardCountryFileUpload').click()
		}
	},
	//发邮件模板
	sendEmail: function () {
		var email = $("input[name=email]").val();
		var re = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/;
		if (email == "") {
			$(document).dialog({
				overlayClose: true,
				content: '邮箱不能为空',
			});
		} else if (!re.test(email)) {
			$(document).dialog({
				overlayClose: true,
				content: '邮箱格式不正确',
			});
		} else {
			$.ajax({
				url: '/WeixinService/business/sendFileTemplateMail.xhtml',
				type: 'GET',
				dataType: 'json',
				data: {
					mail: email
				},
				async: false,
				success: function (data) {
					if (data.returnCode == "0000") {
						$(".sendMail").hide();
						$("input[name=email]").val("")
						$(document).dialog({
							content: '发送成功',
						});
					} else {
						$(document).dialog({
							content: '提交失败，请稍后再试',
						});
					}
				},
				error: function () {
					$(document).dialog({
						content: '系统异常',
					});
				}
			})
		}
	},
	//  渲染附件信息
	renderUploadEL: function (param, el, fileType) {
		var html = "";
		var lastChild = "";
		var imageLength = qualified.fileTypeNum(fileType, "image");
		var veidoLength = qualified.fileTypeNum(fileType, "veido");
		var totalLength = imageLength + veidoLength;
		if (totalLength < 10) {
			if (fileType == "financialCertificate") {
				lastChild = '<li id="filelaterFirst" onclick="qualifiedAssets.financialUpFile()"></li>';
			} else {
				lastChild = '<li id="filelaterFirst" onclick="qualifiedAssets.investmentUpFile()"></li>';
			}
		}
		var i = 0;
		param.forEach(function (e) {
			var type = qualified.judgeFile(e.filename);
			if (type == "viedo") {
				var JSESSIONID = getCookie("JSESSIONID")
				var src = '/WeixinService/business/amazon/downloadVideo/' + e.key + '.xhtml?jsessionid=' + JSESSIONID;
			} else {
				var src = '/WeixinService/business/amazon/download/' + e.key + '.xhtml';
			}
			if (type == "image") {
				i++;
				html += '<li data-type="image" data-index=' + i + '><i onclick="qualified.deleteCurrentFile(\'' + e.recordId + '\',\'3\',\'' + e.key + '\',\'' + fileType + '\')"></i><img src=' + src + '></li>'
			} else if (type == "pdf") {
				html += '<li data-type="pdf"><i onclick="qualified.deleteCurrentFile(\'' + e.recordId + '\',\'3\',\'' + e.key + '\',\'' + fileType + '\')"></i><a href=' + src + '><img src="/WeixinWeb/WeixinWeb_Images/images/qualified/pdfDefault.jpg"></a></li>'
			} else if (type == "viedo") {
				html += '<li data-type="viedo"><i onclick="qualified.deleteCurrentFile(\'' + e.recordId + '\',\'3\',\'' + e.key + '\',\'' + fileType + '\')"></i><a href="javascript:qualified.play(\'' + src + '\')"><img src="/WeixinWeb/WeixinWeb_Images/images/qualified/veidoDefault.jpg"></a></li>'
			} else {
				html += ''
			}
		})
		$(el).html("").append(html + lastChild)
	},
	maskDisable: function () {
		if (this.queryUserFinOrInvest.financialCertificate) {
			$("#financialMask").show()
		} else if (!this.queryUserFinOrInvest.financialCertificate) {
			$("#financialMask").hide()
		}
	}
}
qualifiedAssets.initPage()


//提交用户职业
function submitProfession() {
	var profession = $('#professionInput').val()
	// // 校验职业是否为中文
	// if (!isOnlyChinese(profession)) {
	// 	errorRemark('请输入正确的职业')
	// 	return;
	// }
	if (profession == '') {
		errorRemark('请输入您的职业')
		return;
	}
	$.ajax({
		async: false,
		url: "/WeixinService/business/updateCmfUserBaseInfo.xhtml",
		data: {
			voccode: '15',
			otherVocation: profession
		},
		dataType: "json",
		cache: false,
		type: "post",
		error: function () {
			errorRemark('网络繁忙，请稍后再试。')
		},
		success: function (n) {
			if (n != null && n.resultCode == "0000") {
				isSucc = true;
				$(document).dialog({
					content: '提交成功',
				});
			} else {
				$(document).dialog({
					content: n.resultMsg,
				});
			}
		}
	});

}

// 正则校验 是否为中文
function isOnlyChinese(s) {
	// 正则表达式对象
	var re = new RegExp("^[\\u4e00-\\u9fa5]+$", "");
	// 验证是否刚好匹配
	var yesorno = re.test(s);
	if (yesorno) {
		return true;
	}
	else {
		return false;
	}
}

$(function(){
	queryUserInfo()
})
function queryUserInfo(){
	$.ajax({
		async: false,
		url: "/WeixinService/business/queryUserinfo.xhtml",
		data: "",
		dataType: "json",
		cache: false,
		type: "post",
		error: function (textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success: function (data) {
			if(data){
				if(data.vocCode==15){
					$('#userProfessionBox').show()
					$('#professionInput').val(data.otherVocation)
				}
			}
		}
	})

}
$(document).on("click", "img", e => { 
   e.preventDefault(); 
}) 