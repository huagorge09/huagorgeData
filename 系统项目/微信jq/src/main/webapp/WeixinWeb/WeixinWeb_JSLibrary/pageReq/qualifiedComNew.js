var qualified = {
	isApply: false, //该用户已经提交申请过记录
	updateStatus: "",
	imgMaxSize: queryList("IMGSIZE"),
	viedoMaxSize: queryList("VIDEOSIZE"),
	pdfMaxSize: queryList("PDFSIZE"),
	queryUserAlreadyApply: function () {
		var _self = this;
		var href1 = window.location.href;
		$.ajax({
			url: '/WeixinService/business/queryQualifiedUserInfoByIdno.xhtml',
			type: 'POST',
			async: false,
			dataType: 'json',
			success: function (data) {
				if (data.returnCode == "0000") {
					if (data.data && data.data.length > 0) {
						_self.isApply = true;
						_self.updateStatus = data.data[0].statusRecord.status;
						var status = data.data[0].statusRecord.status
						
						// 判断是否是用户信息页面
						if (href1.indexOf('userInfo') == -1) {
							if (status == "F" || status == "R") {
								if (location.href.indexOf("qualifiedNew.shtml") > -1) {
									$(document).dialog({
										type: 'confirm',
										titleText: "合格投资者审核未通过",
										closeBtnShow: false,
										content: data.data[0].statusRecord.refuseReason,
										onClickConfirmBtn: function () {
											if (location.href.indexOf("qualified") < 0) {
												location.href = "/WeixinService/business/qualified/qualifiedNew.shtml";
											}
										}
									});
								}
							} else if (status == "U") {
								$(document).dialog({
									closeBtnShow: false,
									overlayClose: false,
									content: '您的合格投资信息已由客户经理上传，请确认提交审核',
									onClickConfirmBtn: function () {

									}
								});
							} else if (status == "S") {
								$('#IdCardTo').attr('disabled', 'disabled')
								$(document).dialog({
									content: '您的合格投资信息已审核通过,您已经是合格投资者',
									onClickConfirmBtn: function () {
										location.href = "/#/myAccount";
									}
								});
							} else if (status == "N") {
								$(document).dialog({
									content: '您的合格投资信息正在审核中，请耐心等待',
									onClickConfirmBtn: function () {
										location.href = "/#/myAccount";
									}
								});
							}
						} else {
							if (status == "N" || (status == "S" && diffDateTime())) { //审核中不修改
								$("#idImgPoint").unbind("click");
								$('#idTImePoint').attr("href", "javascript:;")
							}
						}
					}
				} else {
					if (href1.indexOf('userInfo') == -1) {
						$(document).dialog({
							content: data.returnMsg,
						});
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
	 * @param {Object} obj  选择文件的对象
	 * @param {Object} formObj  文件提交对象
	 * @param {Object} page  页面更新类型
	 */
	fileSizeJudge: function (obj, formObj, pageType, fileType) {
		var _self = this;
		var files = obj.files[0];
		var name = files.name;
		var type = qualified.judgeFile(name);
		var size = files.size / 1024 / 1024;
		var imageLength = _self.fileTypeNum(fileType, "image");
		var veidoLength = _self.fileTypeNum(fileType, "viedo");
		if (type == "image") {
			if (size > parseInt(_self.imgMaxSize)) {
				$(document).dialog({
					overlayClose: true,
					content: '图片过大，请重新选择',
				});
				return;
			} else if (imageLength >= 10) {
				$(document).dialog({
					overlayClose: true,
					content: '图片数量已经超过10张',
				});
				return;
			}
		} else if (type == "viedo") {
			if (size > parseInt(_self.viedoMaxSize)) {
				$(document).dialog({
					overlayClose: true,
					content: '视频过大，请重新选择',
				});
				return;
			} else if (veidoLength >= 10) {
				$(document).dialog({
					overlayClose: true,
					content: '视频数量已经超过10个',
				});
				return;
			}
		} else if (type == "pdf") {
			if (size > parseInt(_self.pdfMaxSize)) {
				$(document).dialog({
					overlayClose: true,
					content: 'pdf文件过大，请重新选择',
				});
				return;
			}
		}
		_self.ajaxSubmit(formObj, pageType, obj)
		$("#" + formObj).find("input[name=file]").val("")
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
	 * @param {Object} formObj  文件提交对象
	 * @param {Object} page  页面更新类型
	 * @param {Object} fileObj  文件对象
	 */
	ajaxSubmit: function (formObj, pageType, fileObj) {

		var _self = this;
		$("#" + formObj).ajaxSubmit({
			url: '/WeixinService/business/amazon/upload.xhtml',
			/*设置post提交到的页面*/
			type: "post",
			/*设置表单以post方法提交*/
			dataType: "json",
			/*设置返回值类型为文本*/
			success: function (data) {
				if (data.returnCode == "0000") {

					$(document).dialog({
						overlayClose: true,
						content: '上传成功',
					});

					//该用户已经有申请记录
					if (_self.isApply) {
						$.ajax({
							url: '/WeixinService/business/updateSubmittedInfo.xhtml',
							type: 'POST',
							async: true,
							data: {
								key: data.key,
								operatorType: "insert",
								fileName: data.fileName,
								fileType: $("#" + formObj).find("input[name=fileType]").val()
							},
							dataType: 'json',
							async: false,
							success: function (data) {}
						})
					}
					if (pageType == "qualified") {
						qualifiedAdd.queryFileList();
					} else if (pageType == "qualifiedAssets") {
						qualifiedAssets.queryFileList()
					} else if (pageType == "qualifiedInvest") {
						qualifiedInvest.queryFileList()
					}
				} else {
					$(document).dialog({
						overlayClose: true,
						content: '上传失败，请重新再试',
					});
				}

			},
			error: function (error) {
				$(document).dialog({
					overlayClose: true,
					content: '服务器异常',
				});
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
			var per = Math.floor(100 * loaded / tot); //已经上传的百分比
			$("#scheduleBox").show()
			if (per == 100) {
				$("#scheduleBox").hide()
			}
			$("#scheduleBox").find(".currentBar").css("width", per + "%")
			$("#scheduleBox").find(".percent").text(per + "%")
		}
	},
	/**
	 * 
	 * @param {Object} recordId 删除的recordId值
	 * @param {Object} type     页面删除类型
	 */
	deleteCurrentFile: function (recordId, type, key, fileType) {
		var _self = this;
		if (this.isApply) {
			var url = '/WeixinService/business/updateSubmittedInfo.xhtml'
		} else {
			var url = '/WeixinService/business/deleteFileUploadRecord.xhtml'
		}
		$(document).dialog({
			type: 'confirm',
			closeBtnShow: true,
			content: '是否删除该文件',
			onClickConfirmBtn: function () {
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
							$(document).dialog({
								overlayClose: true,
								content: '删除成功！',
							});
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
							$(document).dialog({
								overlayClose: true,
								content: '删除失败，请重新操作！',
							});
						}
					}
				})
			}
		});
	},
	/**
	 *判断用户金融资产是否大于500万或者投资经历大于2年 
	 */
	queryUserFinOrInvestMethod: function () {
		var queryUserFinOrInvest = "";
		var _self = this;
		$.ajax({
			url: '/WeixinService/business/queryAccreditedInvestorConditions.xhtml',
			type: 'POST',
			dataType: 'json',
			async: false,
			success: function (data) {
				if (data.returnCode == "0000") {
					if (data.data.financialCertificate && data.data.investCertificate) {
						$(document).dialog({
							closeBtnShow: false,
							content: '请进行合格投资者认证',
							onClickConfirmBtn: function () {
								location.href = "/#/myAccount";
							}
						});
					}
					queryUserFinOrInvest = data.data;
				}
			}
		})
		return queryUserFinOrInvest;
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
	tabChange: function () {
		$(".tab li").click(function () {
			var index = $(this).index()
			$(this).addClass("cur").siblings().removeClass("cur")
			$(".tabContent>div").eq(index).show().siblings().hide()
		})
	},
	maskHide: function (obj) {
		$("." + obj).hide()
	},
	maskShow: function (type) {
		$("." + type).show()
	},
	closeVideo: function (type) {
		$("." + type).hide()
		document.getElementById("video").pause()
		$("." + type).find("source").attr("src", "");
	},
	/**
	 * 图片预览
	 * @param {Object} imgList
	 */
	previewImg: function (imgList, index) {
		var html = "";
		html += '<div class="previewPicture" id="previewPicture"><div class="swiper-container previewPicture-swiper-wrapper"><div class="swiper-wrapper">';
		for (var i = 0; i < imgList.length; i++) {
			html += '<div class="swiper-slide"><img src="' + imgList[i] + '"></div>';
		}
		html += '</div><div class="swiper-pagination" id="previewPicture-swiper-pagination"></div></div>';
		html += '</div>';
		$("body").append(html);
		var previewPicture = new Swiper(".previewPicture-swiper-wrapper", {
			initialSlide: index - 1,
			pagination: "#previewPicture-swiper-pagination"
		});
	},
	play: function (videoSrc) {
		$(".tipsMaskVideo").show();
		document.getElementById("video").pause();
		document.getElementById("video").src = videoSrc;
		document.getElementById("video").play();
	}
}

function queryList(key) {
	var param = queryParamList("SYSTEM", "ACINVCONF", "");
	var pmnm = ""
	for (var i = 0; i < param.length; i++) {
		if (param[i].pmco == key) {
			pmnm = param[i].pmnm;
		}
	}
	return pmnm;
}
$(function () {
	qualified.tabChange()
	qualified.queryUserAlreadyApply()
	$(document).on("click", "#previewPicture", function () {
		$(this).remove();
	})

})
$(document).on("click", ".fileItem li img", function () {
	var imgList = [];
	var index = ""
	if (!!!$(this).parents("li").attr("id") && $(this).parents("li").attr("data-type") != "viedo" && $(this).parents("li").attr("data-type") != "pdf") {
		index = $(this).parents("li").attr("data-index");
		$(this).parents(".fileItem").find("li").each(function () {
			if ($(this).attr("data-type") == "image") {
				imgList.push($(this).find("img").attr("src"))
			}
		})
		qualified.previewImg(imgList, index)
	}
})



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
function sureIsSubmitAll(){
	if(isUpPersonImg&& isUpCountryImg){
		$('#recommendPop').hide()
		$('.backLayer').hide()
	}else{
		$(document).dialog({
			titleText: '提示',
			content: '身份证上传不完整',
		});
		return;
	}

 if(qualified.updateStatus=="S"&&!diffDateTime()){
	$.ajax({
		url: "/WeixinService/business/modifyAccreditedInvestorInfoStatus.xhtml",
		type: 'POST',
		dataType: 'json',
		data: {
			updateStatus: "N"
		},
		async: false,
		success: function (data) {
			if (data.returnCode == "0000") {
          location.reload()
			}
		}
	})
 }
}