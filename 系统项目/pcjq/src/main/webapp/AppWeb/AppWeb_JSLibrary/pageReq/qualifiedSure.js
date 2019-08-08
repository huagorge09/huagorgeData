var qualifiedSure = {
	queryUserFinOrInvest: qualified.queryUserFinOrInvestMethod(),
	/**
	 * 查询金融资产证明及投资经历
	 */
	queryFileList: function () {
		var _self = this;
		$.ajax({
			url: '/AppService/business/queryFileUploadRecord.xhtml',
			type: 'POST',
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
							financialCertificate = extendInfo.financialCertificate;
							_self.renderUploadEL(financialCertificate, "#financiList", "financialCertificate");
						}
						if (extendInfo.investmentCertificate) {
							investmentCertificate = extendInfo.investmentCertificate;
							_self.renderUploadEL(investmentCertificate, "#assetsList", "investCertificate")
						}
						// 人像图片数据
						if (extendInfo.idCardPortraitificate) {
							idCardPortraitificate = extendInfo.idCardPortraitificate
						}
						//  国徽图片数据
						if (extendInfo.idCardNationalEmblemcate) {
							idCardNationalEmblemcate = extendInfo.idCardNationalEmblemcate
						}
						var serverFilePath = '/AppService/business/amazon/download/';
						var icPersonLast = parseInt(idCardPortraitificate.length) - 1;
						var icCountryLast = parseInt(idCardNationalEmblemcate.length) - 1;
						if (idCardPortraitificate.length > 0) {
							var icPersonImg = idCardPortraitificate[icPersonLast].key + '.xhtml'; //身份证人像面路径
							var e = idCardPortraitificate[icPersonLast]; //最新的人像图片信息对象
							$('#deletPersonImg').show()
							isUpPersonImg = true;
							$('#icPersonNone').attr('src', '' + serverFilePath + icPersonImg + '')
							$('#deletPersonImg').attr('onclick', 'qualified.deleteCurrentFile(\'' + e.recordId + '\',\'2\',\'' + e.key + '\',\'' + 'idCardPortrait' + '\')')
						}
						if (idCardNationalEmblemcate.length > 0) {
							var icCountryImg = idCardNationalEmblemcate[icCountryLast].key + '.xhtml'; //身份证国徽面路径
							var a = idCardNationalEmblemcate[icCountryLast];//最新国徽图片信息对象
							$('#icCountryNone').attr('src', '' + serverFilePath + icCountryImg + '')
							isUpCountryImg = true;
							$('#deleteCountryImg').show()
							$('#deleteCountryImg').attr('onclick', 'qualified.deleteCurrentFile(\'' + a.recordId + '\',\'2\',\'' + a.key + '\',\'' + 'idCardNationalEmblem' + '\')')
						}
						//  如果身份证 图片为空
						if (idCardPortraitificate == "") {
							$('#icPersonNone').attr('src', '/AppWeb/AppWeb_Images/images/qualified/idCardPerson.png')
							$('#deletPersonImg').hide()
							isUpPersonImg = false;
						}
						if (idCardNationalEmblemcate == "") {
							$('#icCountryNone').attr('src', '/AppWeb/AppWeb_Images/images/qualified/idCardPerson.png')
							$('#deleteCountryImg').hide()
							isUpCountryImg = false;
						}

					}
					var flag1 = financialCertificate.length > 0 && investmentCertificate.length > 0;
					var flag2 = _self.queryUserFinOrInvest.financialCertificate && investmentCertificate.length > 0;
					var flag3 = _self.queryUserFinOrInvest.investCertificate && financialCertificate.length > 0;
					var flag4 = _self.queryUserFinOrInvest.investCertificate && _self.queryUserFinOrInvest.financialCertificate;
					var flag5 = idCardPortraitificate.length > 0 && idCardNationalEmblemcate.length > 0;
					if ((flag1 || flag2 || flag3 || flag4)&&flag5) {
						$(".btnBox").find("a").eq(0).removeClass("disable").attr("href", "javascript:qualifiedSure.submitData()")
					} else if ((!flag1 && !flag2 && !flag3 && !flag4)||!flag5) {
						$(".btnBox").find("a").eq(0).addClass("disable").attr("href", "javascript:qualifiedSure.submitTips();")
					}
				}
			}
		})
		$("#returnBtn").attr("href", "/AppService/business/qualified/qualified.shtml")
	},
	submitTips: function () {
		DJ.dialog.error("资料未齐全，请返回添加修改");
	},
	/**
	 * 提交合格投资者信息
	 */
	submitData: function () {
		if (qualified.isApply) {
			var url = "/AppService/business/modifyAccreditedInvestorInfoStatus.xhtml";
		} else {
			var url = "/AppService/business/addAccreditedInvestorInfo.xhtml"
		}
		if (!$(".btnBox").find("a").eq(0).hasClass("disable")) {
			var _self = this;
			var status = qualified.updateStatus;
			if (status == "F") {
				status = "U"
			} else if (status == "U" || status == "R") {
				status = "N"
			}
			$.ajax({
				url: url,
				type: 'POST',
				dataType: 'json',
				data: {
					updateStatus: status
				},
				async: false,
				success: function (data) {
					if (data.returnCode == "0000") {
						DJ.alert(data.returnMsg, '', function () {
							location.href = "/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO"
						})

					} else {
						DJ.dialog.error("提交失败，请重新操作");
					}
				}
			})
		}
	},
	/**
	* 渲染附件信息元素
	*/
	renderUploadEL: function (param, el, filetype) {
		$(el).html("").append(param.map(function (e) {
			var type = qualified.judgeFile(e.filename);
			var el = "";
			if (type == "image") {
				fileEl = '<li><div class="upload_file img" data-key=' + e.key + ' onclick="qualified.queryCurrentFile(this,\'image\')">' + e.filename + '</div><i onclick="qualified.deleteCurrentFile(\'' + e.recordId + '\',\'2\',\'' + e.key + '\',\'' + filetype + '\')"></i></li>'
			} else if (type == "pdf") {
				fileEl = '<li><div class="upload_file pdf" data-key=' + e.key + ' onclick="qualified.queryCurrentFile(this,\'pdf\')">' + e.filename + '</div><i onclick="qualified.deleteCurrentFile(\'' + e.recordId + '\',\'2\',\'' + e.key + '\',\'' + filetype + '\')"></i></li>'
			} else if (type == "viedo") {
				fileEl = '<li><div class="upload_file viedo" data-key=' + e.key + ' onclick="qualified.queryCurrentFile(this,\'viedo\')">' + e.filename + '</div><i onclick="qualified.deleteCurrentFile(\'' + e.recordId + '\',\'2\',\'' + e.key + '\',\'' + filetype + '\')"></i></li>'
			}
			return fileEl;
		}).join(""))
	}
}
qualifiedSure.queryFileList()
