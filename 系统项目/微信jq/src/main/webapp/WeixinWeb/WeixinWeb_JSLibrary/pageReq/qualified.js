var isUpPersonImg = false;
var isUpCountryImg = false;
queryUserInfo()
var qualifiedAdd = {
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
					var financialCertificate = [];
					var investmentCertificate = [];
					var idCardPortraitificate = [];
					var idCardNationalEmblemcate = [];
					if (data.data.length > 0) {
						var extendInfo = qualified.dataReorganization(data.data);
						if (extendInfo.financialCertificate) {
							financialCertificate = extendInfo.financialCertificate
						}
						if (extendInfo.investmentCertificate) {
							investmentCertificate = extendInfo.investmentCertificate
						}
						if (financialCertificate.length > 0) {
							$("#fileAwaitFirst").hide();
							$("#fileItemFirst").show()
							_self.renderUploadEL(financialCertificate, "#fileItemFirst", "financialCertificate");
						}
						if (investmentCertificate.length > 0) {
							$("#fileAwaitSecond").hide();
							$("#fileItemSecond").show()
							_self.renderUploadEL(investmentCertificate, "#fileItemSecond", "investCertificate")
						}
						if (financialCertificate == "") {
							$("#fileAwaitFirst").show();
							$("#fileItemFirst").hide()
						}
						if (investmentCertificate == "") {
							$("#fileAwaitSecond").show();
							$("#fileItemSecond").hide()
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

						var serverFilePath = '/WeixinService/business/amazon/download/';
						if (idCardPortraitificate.length > 0) {
							var icPersonImg = idCardPortraitificate[0].key + '.xhtml'; //身份证人像面路径
							var e = idCardPortraitificate[0]; //最新的人像图片信息对象
							isUpPersonImg = true;
							$('#icPersonImg').attr('src', '' + serverFilePath + icPersonImg + '')
							$('#deletePersonImg').show()
							$('#deletePersonImg').attr('onclick', 'qualified.deleteCurrentFile(\'' + e.recordId + '\',\'1\',\'' + e.key + '\',\'' + 'idCardPortrait' + '\')')
						} else {
							$('#icPersonImg').attr('src', '')
							isUpPersonImg = false;
							$('#deletePersonImg').hide()
						}

						if (idCardNationalEmblemcate.length > 0) {
							var icCountryImg = idCardNationalEmblemcate[0].key + '.xhtml'; //身份证国徽面路径
							var a = idCardNationalEmblemcate[0]; //最新国徽图片信息对象
							$('#icCountryImg').attr('src', '' + serverFilePath + icCountryImg + '')
							isUpCountryImg = true;
							$('#deleteCountryImg').show()
							$('#deleteCountryImg').attr('onclick', 'qualified.deleteCurrentFile(\'' + a.recordId + '\',\'1\',\'' + a.key + '\',\'' + 'idCardNationalEmblem' + '\')')

						} else {
							$('#icCountryImg').attr('src', '')
							isUpCountryImg = false;
							$('#deleteCountryImg').hide()
						}
						// 用户信息页面
						if (isUpPersonImg == true && isUpCountryImg == true) {
							$('#IdCardTo').addClass('act')
							$('#IdCardTo').html('已设置')
						}
						var flag1 = financialCertificate.length > 0 && investmentCertificate.length > 0;
						// var flag2 = _self.queryUserFinOrInvest.financialCertificate && investmentCertificate.length > 0;
						// var flag3 = _self.queryUserFinOrInvest.investCertificate && financialCertificate.length > 0;
						// var flag4 = _self.queryUserFinOrInvest.investCertificate && _self.queryUserFinOrInvest.financialCertificate;
						var flag5 = idCardPortraitificate.length > 0 && idCardNationalEmblemcate.length > 0;

						if (flag1 && flag5) {
							var href = "/WeixinService/business/qualified/qualifiedSure.shtml"
							if (location.href.indexOf("qualifiedSure") > -1) {
								$(".next a").attr("href", "javascript:submitData()")
							}else{
								$(".next a").attr("href", href)
							}
							$(".next a").addClass("cur")
						}
						if (!flag1 || !flag5 || !$("#userProfessionBox").is(":hidden") || !$("#userIcTimeBox").is(":hidden")) {
							$(".next a").attr("href", "javascript:;").removeClass("cur")
						}


					} else {
						$("#fileAwaitFirst").show();
						$("#fileItemFirst").hide()
						$("#fileAwaitSecond").show();
						$("#fileItemSecond").hide()

						$('#icPersonImg').attr('src', '/WeixinWeb/WeixinWeb_Images/images/qualified/idCardPerson.png')
						$('#icCountryImg').attr('src', '/WeixinWeb/WeixinWeb_Images/images/qualified/idCardCountry.png')
						$('#deleteCountryImg').hide()
						$('#deletePersonImg').hide()

					}

					_self.maskDisable();

				}
			}
		})
	},
	//  渲染附件信息
	renderUploadEL: function (param, el, fileType) {
		var html = "";
		var lastChild = "";
		var imageLength = qualified.fileTypeNum(fileType, "image");
		var veidoLength = qualified.fileTypeNum(fileType, "veido");
		var totalLength = imageLength + veidoLength;
		if (totalLength < 20) {
			if (fileType == "financialCertificate") {
				lastChild = '<li id="filelaterFirst" onclick="qualifiedAdd.financialUpFile()"></li>';
			} else {
				lastChild = '<li id="filelaterFirst" onclick="qualifiedAdd.investmentUpFile()"></li>';
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
				html += '<li data-type="image" data-index=' + i + '><i onclick="qualified.deleteCurrentFile(\'' + e.recordId + '\',\'1\',\'' + e.key + '\',\'' + fileType + '\')"></i><img src=' + src + '></li>'
			} else if (type == "pdf") {
				html += '<li data-type="pdf"><i onclick="qualified.deleteCurrentFile(\'' + e.recordId + '\',\'1\',\'' + e.key + '\',\'' + fileType + '\')"></i><a href=' + src + '><img src="/WeixinWeb/WeixinWeb_Images/images/qualified/pdfDefault.jpg"></a></li>'
			} else if (type == "viedo") {
				html += '<li data-type="viedo"><i onclick="qualified.deleteCurrentFile(\'' + e.recordId + '\',\'1\',\'' + e.key + '\',\'' + fileType + '\')"></i><a href="javascript:qualified.play(\'' + src + '\')"><img src="/WeixinWeb/WeixinWeb_Images/images/qualified/veidoDefault.jpg"></a></li>'
			} else {
				html += ''
			}
		})
		$(el).html("").append(html + lastChild);

	},
	eventBind: function () {
		$(".header h2").text("合格投资者证明");
		$('#financialFileUpload').unbind().change(function () {
			if (qualified.judgeFile(this.files[0].name) == "other") {
				$(document).dialog({
					overlayClose: true,
					content: '文件格式不符合要求，请重新选择',
				});
			} else {
				qualified.fileSizeJudge(this, "financialCertificate", "qualified", "fileItemFirst")
			}
		})
		$('#investFileUpload').unbind().change(function () {
			if (qualified.judgeFile(this.files[0].name) == "other") {
				$(document).dialog({
					overlayClose: true,
					content: '文件格式不符合要求，请重新选择',
				});
			} else {
				qualified.fileSizeJudge(this, "investCertificate", "qualified", "fileItemSecond")
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
				qualified.fileSizeJudge(this, "uploadICPersonForm", "qualified", "idCardPortrait")
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
				qualified.fileSizeJudge(this, "uploadICCountryForm", "qualified", "idCardNationalEmblem")
			}
		})
	},
	// 上传文件点击框
	financialUpFile: function () {
		$("#financialFileUpload").click();
	},
	investmentUpFile: function () {
		$("#investFileUpload").click();
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
	maskDisable: function () {
		if (this.queryUserFinOrInvest.financialCertificate) {
			$("#financialMask").show()
		}
		if (!this.queryUserFinOrInvest.financialCertificate) {
			$("#financialMask").hide()
		}
		if (this.queryUserFinOrInvest.investCertificate) {
			$("#investMask").show()
		}
		if (!this.queryUserFinOrInvest.investCertificate) {
			$("#investMask").hide()
		}
	}

}
qualifiedAdd.initPage()


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
				$("#userProfessionBox").hide();
				qualifiedAdd.queryFileList()
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
	} else {
		return false;
	}
}

function timePicker() {
	var calendar = new LCalendar();
	calendar.init({
		'trigger': '#idExpireDate', //标签id
		'type': 'date', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
		'minDate': '1980-1-1', //最小日期
		'maxDate': (new Date().getFullYear() + 100) + '-' + 12 + '-' + 31 //最大日期
	});
	$("input[name=foever]").click(function () {
		var flag = $(this).is(':checked');
		if (flag) {
			$("#idExpireDate").val("2099-12-31")
			$("#idExpireDate").attr("disabled", "disabled").css("background", "#fff")
		} else {
			$("#idExpireDate").removeAttr("disabled").css("background", "#fff");
			$("#idExpireDate").val("1998-12-31")
		}
	})
}

$(function () {
	
	timePicker()
	click()
})

function queryUserInfo() {
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
			if (data.returnCode == "0000") {

				var isQualified = location.href.indexOf("qualifiedSure") < 0
				if (isQualified) {
					if (data.idExpireDate == '') {
						$('#userIcTimeBox').show()
					} else {
						$('#userIcTimeBox').hide()
					}
					if (data.vocCode == 15 && data.otherVocation == '') {
						$('#userProfessionBox').show()
					} else {
						$('#userProfessionBox').hide()
					}
				}

			}
		}
	})

}

/**
 * 保存身份证有效期修改
 */
function saveIcDate() {
	var idExpireDate = $('#idExpireDate').val()
	var idExpireDate2 = idExpireDate;
	if (idExpireDate == '') {
		errorRemark('请选择身份有效期')
		return;
	}
	var nowDate = new Date();
	nowDate = Date.parse(nowDate.toLocaleDateString());
	idExpireDate = Date.parse(formatDateNum1(idExpireDate))
	if (idExpireDate > nowDate) {
		// 提交
		$.ajax({
			url: '/WeixinService/business/updateIdExpireDateByCustNo.xhtml',
			data: {
				'idExpireDate': idExpireDate2
			},
			dataType: 'json', //服务器返回json格式数据
			type: 'post', //HTTP请求类型
			success: function (data) {
				if (data.resultCode == '0000') {
					$(document).dialog({
						overlayClose: true,
						content: '提交成功',
					});
					$('#userIcTimeBox').hide();
					qualifiedAdd.queryFileList()
				}
			},
			error: function (xhr, type, errorThrown) {

			}
		});
	} else {
		errorRemark('身份证有效期须大于当前日期')
	}

}

/**
 * 日期格式转换
 * @param date
 * @returns
 */
function formatDate1(date) {
	if (date == null || date == '') {
		return "";
	}
	date = unformat1(date);
	return date.substr(0, 4) + "-" + date.substr(4, 2) + "-" + date.substr(6, 2);
}

/**
 * 日期格式转换 '-'
 * @param date
 * @returns
 */
function formatDateNum1(date) {
	if (date == null || date == '') {
		return "";
	}
	date = unformat1(date);
	return parseInt(date.substr(0, 4), 10) + "/" + parseInt(date.substr(4, 2), 10) + "/" + parseInt(date.substr(6, 2), 10);
}

//  关闭身份证示例弹框
function closeExample() {
	$('#icExampleBox').hide()
	$('.backLayer').hide()
}

function click() {
	//  打开示例框
	$('#icCardExShow').click(function () {
		$('#icExampleBox').show()
		$('.backLayer').show()
	})
}