var isUpPersonImg = false;
var isUpCountryImg = false;
var isSucc = ''; //提交职业信息 
var isUploadAllFile = false; //是否上传全部文件
var qualifiedAdd = {
	queryUserFinOrInvest: qualified.queryUserFinOrInvestMethod(),
	initPage: function () {
		this.queryFileList();
		this.eventBind();
	},
	queryFileList: function () {
		var _self = this;
		$.ajax({
			url: '/AppService/business/queryFileUploadRecord.xhtml',
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
						// 人像图片数据
						if (extendInfo.idCardPortraitificate) {
							idCardPortraitificate = extendInfo.idCardPortraitificate
						}
						//  国徽图片数据
						if (extendInfo.idCardNationalEmblemcate) {
							idCardNationalEmblemcate = extendInfo.idCardNationalEmblemcate
						}
						if (financialCertificate.length > 0) {
							$("#financialNone").hide();
							$("#financialHas").show()
							_self.renderUploadEL(financialCertificate, "#finanList", "financialCertificate");
						}
						if (investmentCertificate.length > 0) {
							$("#investNone").hide();
							$("#investHas").show()
							_self.renderUploadEL(investmentCertificate, "#investList", "investCertificate")
						}
						var serverFilePath = '/AppService/business/amazon/download/';
						var icPersonLast = parseInt(idCardPortraitificate.length) - 1;
						var icCountryLast = parseInt(idCardNationalEmblemcate.length) - 1;
						if (idCardPortraitificate.length > 0) {
							var icPersonImg = idCardPortraitificate[icPersonLast].key + '.xhtml'; //身份证人像面路径
							var e = idCardPortraitificate[icPersonLast]; //最新的人像图片信息对象
							$('#deletPersonImg').show()
							$('.icPersonHasUp').html('身份证肖像面已上传')
							isUpPersonImg = true;
							$('#icPersonNone').attr('src', '' + serverFilePath + icPersonImg + '')
							$('#deletPersonImg').attr('onclick', 'qualified.deleteCurrentFile(\'' + e.recordId + '\',\'1\',\'' + e.key + '\',\'' + 'idCardPortrait' + '\')')
						}

						if (idCardNationalEmblemcate.length > 0) {
							var icCountryImg = idCardNationalEmblemcate[icCountryLast].key + '.xhtml'; //身份证国徽面路径
							var a = idCardNationalEmblemcate[icCountryLast];//最新国徽图片信息对象
							$('#icCountryNone').attr('src', '' + serverFilePath + icCountryImg + '')
							$('.icCountryHasUp').html('身份证国徽面已上传')
							isUpCountryImg = true;
							$('#deleteCountryImg').show()
							$('#deleteCountryImg').attr('onclick', 'qualified.deleteCurrentFile(\'' + a.recordId + '\',\'1\',\'' + a.key + '\',\'' + 'idCardNationalEmblem' + '\')')
						}
						//  个人信息页面 设置状态显示
						if (idCardPortraitificate.length>0 && idCardNationalEmblemcate.length>0) {
							$('#idCardImgSet').html('已上传');
							$('#idCardImgSet').addClass('act');
							$('#idCardImgSet').next().html("重新上传");
						}else {
							$('#idCardImgSet').html('未上传');
							$('#idCardImgSet').removeClass('act');
							$('#idCardImgSet').next().html("去上传");
						}
						if (financialCertificate == "") {
							$("#financialNone").show();
							$("#financialHas").hide()
						}
						if (investmentCertificate == "") {
							$("#investNone").show();
							$("#investHas").hide()
						}
						//  如果身份证 图片为空
						if (idCardPortraitificate == "") {
							$('#icPersonNone').attr('src', '/AppWeb/AppWeb_Images/images/qualified/idCardPerson.png')
							$('#deletPersonImg').hide()
							isUpPersonImg = false;
						}
						if (idCardNationalEmblemcate == "") {
							$('#icCountryNone').attr('src', '/AppWeb/AppWeb_Images/images/qualified/idCardCountry.png')
							$('#deleteCountryImg').hide()
							isUpCountryImg = false;
						}
					} else {
						$("#financialNone").show();
						$("#financialHas").hide()
						$("#investNone").show();
						$("#investHas").hide()
					}
					var flag1 = financialCertificate.length > 0 && investmentCertificate.length > 0;
					// var flag2 = _self.queryUserFinOrInvest.financialCertificate && investmentCertificate.length > 0;
					// var flag3 = _self.queryUserFinOrInvest.investCertificate && financialCertificate.length > 0;
					// var flag4 = _self.queryUserFinOrInvest.investCertificate && _self.queryUserFinOrInvest.financialCertificate;
					var flag5 = idCardPortraitificate.length > 0 && idCardNationalEmblemcate.length > 0;
					if (flag1 && flag5 ) {
						if($("#userProfessionBox").is(":hidden")&&$("#icTimeBox").is(":hidden")){
							isUploadAllFile = true;
							var href = "/AppService/business/qualified/qualifiedSure.shtml"
							$("#next").attr("href", href).removeClass("disable")
						}
					} else if (!flag1 || !flag5 ) {
						isUploadAllFile = false;
						$("#next").attr("href", "javascript:;").addClass("disable")
					}


					_self.maskDisable()
				}
			}
		})
	},
	eventBind: function () {
		//金融资产上传点击
		$("#upLoadFinancialBtn").click(function () {
			if ($("#financialNameRead").text() == "") {
				DJ.dialog.error('请选择文件后再上传')
			} else {
				qualified.ajaxSubmit("financialCertificate", "financialPro", "financialNameRead", "financialFileUpload", "qualified");
			}
		});
		$('#financialFileUpload').unbind().change(function () {
			if (qualified.judgeFile(this.files[0].name) == "other") {
				DJ.dialog.error("文件格式不符合要求，请重新选择");
			} else {
				qualified.fileSizeJudge(this, "financialNameRead", "finanList")
			}
		})
		//投资经历上传点击
		$("#upLoadAssetsBtn").click(function () {
			if ($("#investNameRead").text() == "") {
				DJ.dialog.error('请选择文件后再上传')
			} else {
				qualified.ajaxSubmit("investCertificate", "investPro", "investNameRead", "investFileUpload", "qualified");
				//investCertificate form的 id值 
				/* investNameRead 文件名保存span 
				investFileUpload  文件上传点击 input
				 */
			}
		});
		$('#investFileUpload').unbind().change(function () {
			if (qualified.judgeFile(this.files[0].name) == "other") {
				DJ.dialog.error("文件格式不符合要求，请重新选择");
			} else {
				qualified.fileSizeJudge(this, "investNameRead", "investList")
			}
		});

		var fileTypePerson; //人像面文件格式
		var filetypeCountry; //国徽面文件格式
		// 身份证人像面上传
		$('#idCardPersonFileUpload').unbind().change(function () {
			fileTypePerson = qualified.judgeFile(this.files[0].name);
			// if (this.files.length > 0) {
			// 	$('.icPersonHasUp').html('身份证肖像面已选择')
			// }
			if (fileTypePerson != "image") {
				DJ.dialog.error("文件须为图片格式,请重新选择");
				return;
			} else {
				qualified.fileSizeJudge(this, "icPersonFileName", "idCardPortrait")
			}
			if ($('#icPersonFileName').text() == "") {
				DJ.dialog.error('请选择文件后再上传')
				$('.icPersonHasUp').html('')
				return;
			} else {
				qualified.ajaxSubmit("uploadICPersonForm", "", "icPersonFileName", "idCardPersonFileUpload", "qualified")
			}
		});

		//   身份证国徽面上传
		$('#idCardCountryFileUpload').unbind().change(function () {
			filetypeCountry = qualified.judgeFile(this.files[0].name);
			// if (this.files.length > 0) {
			// 	$('.icCountryHasUp').html('身份证国徽面已选择')
			// }
			if (filetypeCountry != "image") {
				DJ.dialog.error("文件须为图片格式,请重新选择");
				return;
			} else {
				qualified.fileSizeJudge(this, "icCountryFileName", "idCardNationalEmblem")
			}
			if ($('#icCountryFileName').text() == "") {
				DJ.dialog.error('请选择文件后再上传')
				$('.icCountryHasUp').html('')
				return;
			} else {
				qualified.ajaxSubmit("uploadICCountryForm", "", "icCountryFileName", "idCardCountryFileUpload", "qualified");
			}

		});

	},
	financialUpFile: function () {
		$("#financialFileUpload").click();
	},
	investmentUpFile: function () {
		$("#investFileUpload").click();
	},
	// 点击按钮 展示文件上传选择框
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
	/**
	 * 渲染附件元素
	 */
	renderUploadEL: function (param, el, filetype) {
		var _self = this;
		$(el).html("").append(param.map(function (e) {
			var type = qualified.judgeFile(e.filename);
			var el = "";
			if (type == "image") {
				fileEl = '<li class="image" data-type="image"><em></em><b data-key=' + e.key + ' onclick="qualified.queryCurrentFile(this,\'image\')">' + e.filename + '</b><i onclick="qualified.deleteCurrentFile(\'' + e.recordId + '\',\'1\',\'' + e.key + '\',\'' + filetype + '\')"></i></li>'
			} else if (type == "pdf") {
				fileEl = '<li class="pdf" data-type="pdf"><em></em><b data-key=' + e.key + ' onclick="qualified.queryCurrentFile(this,\'pdf\')">' + e.filename + '</b><i onclick="qualified.deleteCurrentFile(\'' + e.recordId + '\',\'1\',\'' + e.key + '\',\'' + filetype + '\')"></i></li>'
			} else if (type == "viedo") {
				fileEl = '<li class="viedo" data-type="viedo"><em></em><b data-key=' + e.key + ' onclick="qualified.queryCurrentFile(this,\'viedo\')">' + e.filename + '</b><i onclick="qualified.deleteCurrentFile(\'' + e.recordId + '\',\'1\',\'' + e.key + '\',\'' + filetype + '\')"></i></li>'
			} else {
				fileEl = ''
			}
			return fileEl;
		}).join(""))
	},
	maskDisable: function () {
		if (this.queryUserFinOrInvest.financialCertificate) {
			$("#financialMask").show()
			$("#financialMask").siblings(".proUpload").hide()
		}
		if (!this.queryUserFinOrInvest.financialCertificate) {
			$("#financialMask").hide();
			$("#financialMask").siblings(".proUpload").show()
		}
		if (this.queryUserFinOrInvest.investCertificate) {
			$("#investMask").show()
			$("#investMask").siblings(".proUpload").hide()
		}
		if (!this.queryUserFinOrInvest.investCertificate) {
			$("#investMask").hide()
			$("#investMask").siblings(".proUpload").show()
		}
	}

}

$(function () {
	queryUserinfo()
	qualifiedAdd.initPage()
	$("input[name=foever]").click(function () {
		var flag = $(this).is(':checked');
		if (flag) {
			$("#idCardIndate").val("2099-12-31")
			$("#idCardIndate").attr("disabled", "disabled")
		} else {
			isTrueIdExpireDate = false;
			$("#idCardIndate").removeAttr("disabled")
			$("#idCardIndate").val("")
		}
	})
})
// 查询用户信息
function queryUserinfo() {
	$.ajax({
		async: false,
		url: "/AppService/business/queryUserinfo.xhtml",
		data: "",
		dataType: "json",
		cache: false,
		type: "post",
		error: function (textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success: function (data) {
			if (data) {
				if (data.idExpireDate == '') {
					$('#icTimeBox').show()
				} else {
					$('#icTimeBox').hide()
				}
				if (data.vocCode == 15 && data.otherVocation == '') {
					$('#userProfessionBox').show()
				} else {
					$('#userProfessionBox').hide()
				}

			}
		}
	});
}

//提交用户职业
function submitProfession() {
	var profession = $('#professionInput').val()
	// // 校验职业是否为中文
	// if (!isOnlyChinese(profession)) {
	// 	show_tips('请输入正确的职业')
	// 	return;
	// }
	if (profession == '') {
		show_tips('请输入您的职业')
		return;
	}
	$.ajax({
		async: false,
		url: "/AppService/business/updateCmfUserBaseInfo.xhtml",
		data: {
			voccode: '15',
			otherVocation: profession
		},
		dataType: "json",
		cache: false,
		type: "post",
		error: function () {
			show_tips("网络繁忙，请稍后再试。");
		},
		success: function (n) {
			if (n != null && n.resultCode == "0000") {
				$('#userProfessionBox').hide()
				qualifiedAdd.queryFileList()
				show_tips('提交成功')
				
			} else {
				show_tips(n.resultMsg);
			}
			queryUserinfo()
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

/* 提交身份证有效期 */
function submitIcTime() {
	var idExpireDate = $.trim($('#idCardIndate').val());
	if (idExpireDate == '') {
		show_tips('请选择身份证有效期')
		return;
	}
	var nowDateTime = Date.parse(new Date().toLocaleDateString());
	var idExpireDateTime = Date.parse(formatDateNum1(idExpireDate));
	if (idExpireDateTime > nowDateTime) {
		$.ajax({
			url: '/AppService/business/updateIdExpireDateByCustNo.xhtml',
			data: {
				'idExpireDate': idExpireDate
			},
			dataType: 'json',//服务器返回json格式数据
			type: 'post',//HTTP请求类型
			success: function (data) {
				// 重新查询用户信息
				queryUserinfo()
				if (data.resultCode == '9999') {
					show_tips(data.resultMsg)
					setTimeout(function () {
						$('#idCard-select-pop').hide()
					}, 1500)
					// 修改成功
				} else if (data.resultCode == '0000') {
					$('#idCard-select-pop').hide()
					show_tips("修改成功");
					$('#idCardSet').html('已设置')
					$('#idCardSet').addClass('act')
					$('#icTimeBox').hide()
					queryUserinfo()
					qualifiedAdd.queryFileList()
				}

			},
			error: function () {
				show_tips("网络繁忙，请稍后再试。");
			}
		});
	} else {
		show_tips('身份证有效期须大于当前日期')
		return;
	}
}

function click() {
	$('#next').click(function () {
		submitProfession()
	})
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

//展示身份证示例 
function showIcExam() {
	$('#icExampleBox').show()
	$('.backLayer').show()
}

function closeExample() {
	$('.closeIcon').parent().parent().hide()
	$('.backLayer').hide()

}



openQualified()
// 是否开启合格投资者功能
function openQualified(){
    var param=queryParamComm("SYSTEM","ACINVCONF","");
	var pmnm=""
	for(var i=0;i<param.length;i++){
		if(param[i].pmco=="MAIN"){
			pmnm=param[i].pmnm;
		}
    }
	if(pmnm=="0"){  //为0时不显示菜单
		$("#idCardImg").hide()
	}else{
		$("#idCardImg").show()
	}
}