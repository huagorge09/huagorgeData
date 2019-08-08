
var custno = '';
var qualified = {
	isApply: false,  //该用户已经提交申请过记录
	updateStatus: "",
	imgMaxSize: queryList("IMGSIZE"),
	viedoMaxSize: queryList("VIDEOSIZE"),
	pdfMaxSize: queryList("PDFSIZE"),
	/**
     * 查看或下载当前文件
     * @param {Object} obj
     */
	queryCurrentFile: function (obj, type) {
		var key = $(obj).attr("data-key")
		if (type == "viedo") {
			var JSESSIONID = getCookie("JSESSIONID");
			var href = '/AppService/business/amazon/downloadVideo/' + key + '.xhtml?jsessionid=' + JSESSIONID
		} else {
			var href = '/AppService/business/amazon/download/' + key + '.xhtml'
		}
		window.open(href, "_blank");
	},
	queryUserAlreadyApply: function () {
		var _self = this;
		$.ajax({
			url: '/AppService/business/queryQualifiedUserInfoByIdno.xhtml',
			type: 'POST',
			async: false,
			dataType: 'json',
			success: function (data) {
				if (data.returnCode == "0000") {
					if (data.data && data.data.length > 0) {
						custno = data.data[0].custno;
						$('.custno').val(custno)
						_self.isApply = true;
						_self.updateStatus = data.data[0].statusRecord.status;
						var status = data.data[0].statusRecord.status
						var href1 = window.location.href;
						var page = getUrlParams(href1)
						if (page.thirdCatId != 'ACCOUNT_INFO') {
							if (status == "F" || status == "R") {
								if (location.href.indexOf("qualified.shtml") > -1) {
									$("#refuse").html(data.data[0].statusRecord.refuseReason);
									DJ.confirm(data.data[0].statusRecord.refuseReason, function (result) {
										if (result) {
											if (location.href.indexOf("qualified") < 0) {
												location.href = "/AppService/business/qualified/qualified.shtml";
											}
										}
									}, '合格投资者审核未通过');
								}
							} else if (status == "U") {
								DJ.alert('您的合格投资信息已由客户经理上传，请确认提交审核', '', function () {

								})
							} else if (status == "S") {
								DJ.alert('您的合格投资信息已审核通过,您已经是合格投资者', '', function () {
									location.href = "/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO";
								})
							} else if (status == "N") {
								DJ.alert('您的合格投资信息正在审核中，请耐心等待', '', function () {
									location.href = "/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO";
								})
							}
						}else{
							if (status == "N" || (status == "S" && diffDateTime())) { //审核中不修改
								$("#idCardImgSet,#idCardSet").siblings("a").remove();
							}
						}

					}

					$(".popClose").remove()

				} else {
					if (page.thirdCatId != 'ACCOUNT_INFO') {
						DJ.alert(data.returnMsg);
					}
				}
			}
		})
	},
	/**
	 * 判断文件的类型
	 * @param {Object} fileName
	 */
	judgeFile: function (filename) {
		var type = "";
		var typefile = ["pdf"];
		var imgFormatArr = ['jpg', 'png', 'bmp', 'jpeg'];
		var viedoFormatArr = ['mp4', 'rmvb', 'avi', '3gp', 'flv', 'mov']
		var fileName = filename.toLowerCase();
		var filenameFormat = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length);
		if (typefile.indexOf(filenameFormat) < 0) {
			if (imgFormatArr.indexOf(filenameFormat) < 0) {
				if (viedoFormatArr.indexOf(filenameFormat) < 0) {
					type = "other"
				} else {
					type = "viedo"
				}
			} else {
				type = "image"
			}
		} else {
			type = "pdf"
		}

		return type

	},
	/**
	 * 
	 * @param {Object} recordId 删除的recordId值
	 * @param {Object} type     页面删除类型
	 */
	deleteCurrentFile: function (recordId, type, key, fileType) {
		var _self = this;
		if (this.isApply) {
			var url = '/AppService/business/updateSubmittedInfo.xhtml'
		} else {
			var url = '/AppService/business/deleteFileUploadRecord.xhtml'
		}
		DJ.confirm('确定要删除?', function (result) {
			if (result) {
				var _self = this;
				var param = {
					recordId: recordId,
					operatorType: "delete",
					key: key,
					fileType: fileType
				}
				$.ajax({
					url: url,
					type: 'POST',
					data: param,
					dataType: 'json',
					success: function (data) {
						if (data.returnCode == "0000") {
							DJ.dialog.success("删除成功!");
							if (fileType == "idCardPortrait") {
								$('.icPersonHasUp').html('')
								$('#icPersonNone').attr('src', '/AppWeb/AppWeb_Images/images/qualified/idCardPerson.png')
								$('#deletPersonImg').hide()
							} else if (fileType == "idCardNationalEmblem") {
								$('.icCountryHasUp').html('')
								$('#icCountryNone').attr('src', '/AppWeb/AppWeb_Images/images/qualified/idCardPerson.png')
								$('#deleteCountryImg').hide()
							}
							if (type == "1") {
								qualifiedAdd.queryFileList()
							} else if (type == "2") {
								qualifiedSure.queryFileList()
							} else if (type == "3") {
								qualifiedAssets.queryFileList()
							} else if (type == "4") {
								qualifiedInvest.queryFileList()
							}
						} else {
							DJ.dialog.error("删除失败，请重新操作");
						}
					}
				})
			}
		}, '温馨提示');
	},
	/**
	 * @param {Object} tabT  点击对象
	 * @param {Object} tabC  选项卡对象
	 * @param {Object} type  控制对象
	 */
	tabChange: function (tabT, tabC, type) {
		$(tabT).click(function () {
			$(this).addClass(type).siblings().removeClass(type);
			$(tabC).eq($(this).index()).show().siblings().hide()
		})
	},
	/**
	 * @param {Object} obj  文件提交对象
	 * @param {Object} pro  进度条对象
	 * @param {Object} fileName 文件名
	 * @param {Object} fileEL 文件框
	 * @param {Object} type  要刷新的类型页面
	 */
	ajaxSubmit: function (obj, pro, fileName, fileEL, type) {
		var _self = this;
		$("#" + obj).ajaxSubmit({
			url: '/AppService/business/amazon/upload.xhtml', /*设置post提交到的页面*/
			type: "post", /*设置表单以post方法提交*/
			dataType: "json", /*设置返回值类型为文本*/
			success: function (data) {
				if (data.returnCode == "0000") {
					DJ.dialog.success("上传成功!");

					//该用户已经有申请记录
					if (_self.isApply) {
						$.ajax({
							url: '/AppService/business/updateSubmittedInfo.xhtml',
							type: 'POST',
							async: true,
							data: {
								key: data.key,
								custno: custno,
								operatorType: "insert",
								fileName: data.fileName,
								fileType: $("#" + obj).find("input[name=fileType]").val()
							},
							dataType: 'json',
							async: false,
							success: function (data) {
							}
						})
					}

					$("#" + fileEL).val("");
					if (type == "qualified") {
						qualifiedAdd.queryFileList();
					} else if (type == "qualifiedAssets") {
						qualifiedAssets.queryFileList()
					} else if (type == "qualifiedInvest") {
						qualifiedInvest.queryFileList()
					}
				} else {
					DJ.dialog.error("上传失败，请重新再试", "", "error");
				}
			},
			error: function (error) {
				DJ.dialog.error("服务器异常");
			},
			xhr: function () {
				var xhr = $.ajaxSettings.xhr();
				if (onprogress && xhr.upload) {
					xhr.upload.addEventListener("progress", onprogress, false);
					return xhr;
				}
			}
		});
		function onprogress(evt) {
			var loaded = evt.loaded;
			var tot = evt.total;
			var per = Math.floor(100 * loaded / tot);  //已经上传的百分比
			$("#" + pro).fadeIn()
			if (per == 100) {
				$("#" + fileName).text("");
				setTimeout(function () { $("#" + pro).fadeOut() }, 1000)
			}
			$("#" + pro).find("span").css("width", per + "%")
			$("#" + pro).find("span span").text(per + "%")
		}
	},
	/**
	 * @param {Object} obj  选择文件的对象
	 * @param {Object} fileNameRead  文本赋值对象
	 * @param {Object} fileType  文件类型父对象
	 */
	fileSizeJudge: function (obj, fileNameRead, fileType) {
		var files = obj.files[0];
		var name = files.name;
		var type = qualified.judgeFile(name);
		var size = files.size / 1024 / 1024;
		var imageLength = this.fileTypeNum(fileType, "image");
		var veidoLength = this.fileTypeNum(fileType, "viedo");
		if (type == "image") {
			if (size > parseInt(this.imgMaxSize)) {
				DJ.dialog.error('图片过大，请重新选择');
				$("#" + fileType).parents(".fileUpBox").find("input[name=file]").val("")
				return;
			} else if (imageLength >= 10) {
				DJ.dialog.error('图片数量已经超过10张');
				$("#" + fileType).parents(".fileUpBox").find("input[name=file]").val("")
				return;
			} else {
				$("#" + fileNameRead).text(name)
			}
		} else if (type == "viedo") {
			if (size > parseInt(this.viedoMaxSize)) {
				DJ.dialog.error('视频过大，请重新选择');
				$("#" + fileType).parents(".fileUpBox").find("input[name=file]").val("")
				return;
			} else if (veidoLength >= 10) {
				DJ.dialog.error('视频数量已经超过10个');
				$("#" + fileType).parents(".fileUpBox").find("input[name=file]").val("")
				return;
			} else {
				$("#" + fileNameRead).text(name)
			}
		} else if (type == "pdf") {
			if (size > parseInt(this.pdfMaxSize)) {
				DJ.dialog.error('pdf文件过大，请重新选择');
				$("#" + fileType).parents(".fileUpBox").find("input[name=file]").val("")
				return;
			} else {
				$("#" + fileNameRead).text(name)
			}
		}
	},
	/**
	 * 重新组装json数据
	 * @param {Object} obj
	 */
	dataReorganization: function (obj) {
		var dataReorganization = {};
		var financialCertificate = [];
		var investmentCertificate = [];
		var idCardPortraitificate = [];
		var idCardNationalEmblemcate = [];
		obj.forEach(function (e) {
			var qualified = {};
			qualified.filename = e.fileName;
			qualified.key = e.fileKey;
			qualified.recordId = e.recordId;
			if (e.fileType == "financialCertificate") {
				financialCertificate.push(qualified)
			} else if (e.fileType == "investCertificate") {
				investmentCertificate.push(qualified)
				// 身份证人像图
			} else if (e.fileType == "idCardPortrait") {
				idCardPortraitificate.push(qualified)
			} else if (e.fileType == "idCardNationalEmblem") {
				idCardNationalEmblemcate.push(qualified)
			}
		})
		dataReorganization.financialCertificate = financialCertificate
		dataReorganization.investmentCertificate = investmentCertificate
		dataReorganization.idCardPortraitificate = idCardPortraitificate
		dataReorganization.idCardNationalEmblemcate = idCardNationalEmblemcate
		return dataReorganization;
	},
	/**
     * 查询文件的数量
     * @param {Object} obj  父盒子对象
     * @param {Object} type  文件的类型
     * return 文件的数量
     */
	fileTypeNum: function (obj, type) {
		var len = 0
		$("#" + obj).find("li").each(function () {
			if ($(this).attr("data-type") == type) {
				len++
			}
		})
		return len;
	},
	/**
 *判断用户金融资产是否大于500万或者投资经历大于2年 
 */
	queryUserFinOrInvestMethod: function () {
		var _self = this;
		var queryUserFinOrInvest = "";
		$.ajax({
			url: '/AppService/business/queryAccreditedInvestorConditions.xhtml',
			type: 'POST',
			dataType: 'json',
			async: false,
			success: function (data) {
				if (data.returnCode == "0000") {
					if (data.data.financialCertificate && data.data.investCertificate) {
						DJ.alert("您已经是合格投资者", '', function () {
							location.href = "/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO"
						})
						$(".popClose").remove()
					}
					queryUserFinOrInvest = data.data;
				}
			}
		})
		return queryUserFinOrInvest;
	}
}
function queryList(key) {
	var param = queryParamComm("SYSTEM", "ACINVCONF", "");
	var pmnm = ""
	for (var i = 0; i < param.length; i++) {
		if (param[i].pmco == key) {
			pmnm = param[i].pmnm;
		}
	}
	return pmnm;
}

function getCookie(name) {
	var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
	if (arr = document.cookie.match(reg)) {
		return unescape(arr[2]);
	} else {
		return null;
	}
}

$(function () {
	qualified.queryUserAlreadyApply()

})

// 取通过URL传过来的参数 (格式如 ?Param1=Value1&Param2=Value2)
function getUrlParams() {
	var urlParams = new Object();
	var aParams = document.location.search.substr(1).split('&');
	for (i = 0; i < aParams.length; i++) {
		var aParam = aParams[i].split('=');
		urlParams[aParam[0]] = aParam[1];
	}
	return urlParams;
}


function diffDateTime() {
	var nowDateTime = Date.parse(new Date().toLocaleDateString());
	var idExpireDateTime = Date.parse(formatDateNum1(localStorage.getItem("idExpireDate")))
    if(nowDateTime>idExpireDateTime){
		return false
	}else{
		return true
	}

}

function formatDateNum1(date) {
	if (date == null || date == '') {
		return "";
	}
	date = unformat1(date);
	return parseInt(date.substr(0, 4), 10) + "/" + parseInt(date.substr(4, 2), 10) + "/" + parseInt(date.substr(6, 2), 10);
}

//  确定是否提交完整的身份证
function sureIsSubmitAll() {
	
    
	if (isUpPersonImg && isUpCountryImg) {
		$('#recommendPop').hide()
		$('.backLayer').hide()
	} else {
		show_tips('身份证上传不完整')
		return;
	}
	
	if(qualified.updateStatus=="S"&&!diffDateTime()){
        $.ajax({
			url: '/AppService/business/modifyAccreditedInvestorInfoStatus.xhtml',
			type: 'POST',
			dataType: 'json',
			data: {
				updateStatus: "N"
			},
			async: false,
			success: function (data) {
				if (data.returnCode == "0000") {
					queryUserinfoAccount()
				} 
			}
		})
	}


}