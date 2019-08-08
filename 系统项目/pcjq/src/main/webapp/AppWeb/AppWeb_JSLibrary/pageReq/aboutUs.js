$(document).ready(function(e) {
	getUserRequest("pc_aboutUs_01");
	$(".nav.fr ul li a").removeClass("current").eq(3).addClass("current");
	querySocialRecruitment("1","","");
	console.log("")
//	document.title = "财富故事";
});
/* 锚链接添加选中样式act */
$(".about-nav ul li a").click(function() {
	$(".about-nav ul li a").removeClass("act");
	$(this).addClass("act");
});
/* 社会招聘 */
function querySocialRecruitment(pages,positionId,placeId) {
	getUserRequest("pc_aboutUs_02_01");
	$(".table .nav-table ul li").removeClass("act");
	$(".table .nav-table ul li:eq(0)").addClass("act");
	var page = parseInt(returnPage(pages), 10);

	$.ajax({
		async : true,
		url : "/AppService/article/querySocialRecruitment.xhtml",
		data : {
			"positionId" : positionId,
			"placeId" : placeId,
			"page" : page
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			var htmls = "";
			var htmls2 = "";/* 分页 */

			var placeDtoList = data.placeDtoList;/* 工作地点 */
			var positionTypeDtoList = data.positionTypeDtoList;/* 职位类别 */
			var recruitmentDtoList = data.recruitmentDtoList;/* 招聘信息 */
			var currentPositionId = data.currentPositionId;/* 当前职位类型ID */
			var currentPlaceId = data.currentPlaceId;/* 当前工作地点ID */
			var currentPage = data.currentPage;/* 当前页数 */
			var count = data.count;/* 招聘信息记录数 */
			var maxPages = data.maxPages;/* 最大页数 */

			$("#page").val(currentPage);
			$("#maxPages").val(maxPages);
			if (count == null || count == "" || count == 0) {
				$("#infos").html("<span class='table-con-none'>暂无此类招聘信息</span>");
				$(".page").html("");
				return;
			}

			htmls += "<div class='joinUs-title'>";
            htmls += "<span>职称名称</span>";
            htmls += "<div class='about-select workstyle'>";
            htmls += "<h4 class='title'>职称类别</h4>";
            htmls += "<div class='about-select-list none'>";
            htmls += "<ul>";
            if (positionTypeDtoList != null && positionTypeDtoList.length > 0) {
                $.each(positionTypeDtoList, function(i, item) {
                    if (currentPositionId != null && currentPositionId == item.positionId) {
                        htmls +=  "<li class='act'><a href='javascript:querySocialRecruitment(\"1\",\""+item.positionId+"\",\"\");'>"+item.positionName+"</a></li>";
                    } else {
                        htmls +=  "<li><a href='javascript:querySocialRecruitment(\"1\",\""+item.positionId+"\",\"\");'>"+item.positionName+"</a></li>";
                    }
                });
            }

            htmls += "</ul>";
            htmls += "</div>";
            htmls += "</div>";
            htmls += "<span>招聘人数</span>";
            htmls += "<div class='about-select workaddress'>";
            htmls += "<h4 class='title'>工作地点</h4>";
            htmls += "<div class='about-select-list none'>";
            htmls += "<ul>";
            if (placeDtoList != null && placeDtoList.length > 0) {
                $.each(placeDtoList, function(i, item) {
                    if (currentPlaceId != null && currentPlaceId == item.placeId) {
                        htmls += "<li class='act'><a href='javascript:querySocialRecruitment(\"1\",\"\",\""+item.placeId+"\");'>"+item.placeName+"</a></li>";
                    } else {
                        htmls += "<li><a href='javascript:querySocialRecruitment(\"1\",\"\",\""+item.placeId+"\");'>"+item.placeName+"</a></li>";
                    }
                });
            }
            htmls += "</div>";
            htmls += "</div>";
            htmls += "<span>发布时间</span>";
            htmls += "</div>";
			htmls += "<table>";

			if (recruitmentDtoList != null && recruitmentDtoList.length > 0) {
				$.each(recruitmentDtoList, function(i, item) {
					if (i % 2 == 0) {
						htmls += "<tr class='even'>";
					} else {
						htmls += "<tr class='odd'>";
					}
					htmls += "<td class='begin'><a href='/AppService/article/recruitmentDetail.shtml?informationId=" + item.informationId + "'>" + item.informationName + "</a></td>";
					htmls += "<td class='second'>" + item.positionName + "</td>";
					htmls += "<td class='third'>" + item.informationNumber + "</td>";
					htmls += "<td class='fourth'>" + item.placeName + "</td>";
					htmls += "<td class='end'>" + formatDate(item.updateDate || "") + "</td>";
					htmls += "</tr>";
				});
			}

			htmls += "</table>";

			$("#infos").html(htmls);
			$(".table-con .joinUs-title h4.title").click(function() {
        		$(this).siblings(".about-select-list").toggleClass("none");
        		$(this).parent().siblings().find(".about-select-list").addClass("none");
        	});
        	$(".about-select-list a").click(function() {
        		/*var selectValue = $(this).text();
        		$(this).parents(".about-select-list").siblings(".table-con .joinUs-title h4.title").text(selectValue);*/
        		$(this).parents(".about-select-list").addClass("none");
        	});
        	$(document).bind("click", function(e) {
        		var target = $(e.target);
        		if (target.closest(".table-con .joinUs-title h4.title,.about-select-list").length == 0) {
        			$(".about-select-list").addClass("none");
        		}
        	});
			htmls2 += "<a href='javascript:querySocialRecruitment(\"" + (page - 1) + "\")' class='pre'></a>";
			if (page == 1) {
				htmls2 += "<a class='act' href='javascript:querySocialRecruitment(\"1\")'>1</a>";
			} else {
				htmls2 += "<a href='javascript:querySocialRecruitment(\"1\")'>1</a>";
			}

			if (page > 3) {/* 左边加... */
				htmls2 += "<a class='omit'>...</a>";
			}

			if ((page - 1) > 1) {/* 上一页 */
				htmls2 += "<a href='javascript:querySocialRecruitment(\"" + (page - 1) + "\")'>" + (page - 1) + "</a>";
			}

			if (page != 1 && page != maxPages) {/* 当前页 */
				htmls2 += "<a class='act' href='javascript:querySocialRecruitment(\"" + (page) + "\")'>" + (page) + "</a>";
			}

			if (page + 1 < maxPages) {/* 下一页 */
				htmls2 += "<a href='javascript:querySocialRecruitment(\"" + (page + 1) + "\")'>" + (page + 1) + "</a>";
			}

			if ((maxPages - page) > 2) {/* 右边加... */
				htmls2 += "<a class='omit'>...</a>";
			}

			if (page != 1) {
				if (maxPages == page) {
					htmls2 += "<a class='act' href='javascript:querySocialRecruitment(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
				} else if (maxPages > page) {
					htmls2 += "<a href='javascript:querySocialRecruitment(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
				}
			} else {
				if (maxPages == page) {

				} else if (maxPages > page) {
					htmls2 += "<a href='javascript:querySocialRecruitment(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
				}
			}
			htmls2 += "<a href='javascript:querySocialRecruitment(\"" + (page + 1) + "\")' class='next'></a>";
			htmls2 += "</div>";
			$(".page").html(htmls2);
		}
	});
}
/* 校园招聘 */
function queryCampusRecruitment(pages,positionId,placeId) {
	getUserRequest("pc_aboutUs_02_02");
	$(".table .nav-table ul li").removeClass("act");
	$(".table .nav-table ul li:eq(1)").addClass("act");
	var page = parseInt(returnPage(pages), 10);

	$.ajax({
		async : true,
		url : "/AppService/article/queryCampusRecruitment.xhtml",
		data : {
			"positionId" : positionId,
			"placeId" : placeId,
			"page" : page
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			var htmls = "";
			var htmls2 = "";/* 分页 */

			var placeDtoList = data.placeDtoList;/* 工作地点 */
			var positionTypeDtoList = data.positionTypeDtoList;/* 职位类别 */
			var recruitmentDtoList = data.recruitmentDtoList;/* 招聘信息 */
			var currentPositionId = data.currentPositionId;/* 当前职位类型ID */
			var currentPlaceId = data.currentPlaceId;/* 当前工作地点ID */
			var currentPage = data.currentPage;/* 当前页数 */
			var count = data.count;/* 招聘信息记录数 */
			var maxPages = data.maxPages;/* 最大页数 */

			$("#page").val(currentPage);
			$("#maxPages").val(maxPages);

			if (count == null || count == "" || count == 0) {
				$("#infos").html("<span class='table-con-none'>暂无此类招聘信息</span>");
				$(".page").html("");
				return;
			}

			htmls += "<div class='joinUs-title'>";
            htmls += "<span>职称名称</span>";
            htmls += "<div class='about-select workstyle'>";
            htmls += "<h4 class='title'>职称类别</h4>";
            htmls += "<div class='about-select-list none'>";
            htmls += "<ul>";
            if (positionTypeDtoList != null && positionTypeDtoList.length > 0) {
                $.each(positionTypeDtoList, function(i, item) {
                    if (currentPositionId != null && currentPositionId == item.positionId) {
                        htmls +=  "<li class='act'><a href='javascript:queryCampusRecruitment(\"1\",\""+item.positionId+"\",\"\");'>"+item.positionName+"</a></li>";
                    } else {
                        htmls +=  "<li><a href='javascript:queryCampusRecruitment(\"1\",\""+item.positionId+"\",\"\");'>"+item.positionName+"</a></li>";
                    }
                });
            }

            htmls += "</ul>";
            htmls += "</div>";
            htmls += "</div>";
            htmls += "<span>招聘人数</span>";
            htmls += "<div class='about-select workaddress'>";
            htmls += "<h4 class='title'>工作地点</h4>";
            htmls += "<div class='about-select-list none'>";
            htmls += "<ul>";
            if (placeDtoList != null && placeDtoList.length > 0) {
                $.each(placeDtoList, function(i, item) {
                    if (currentPlaceId != null && currentPlaceId == item.placeId) {
                        htmls += "<li class='act'><a href='javascript:queryCampusRecruitment(\"1\",\"\",\""+item.placeId+"\");' data-value='"+item.placeId+"'>"+item.placeName+"</a></li>";
                    } else {
                        htmls += "<li><a href='javascript:queryCampusRecruitment(\"1\",\"\",\""+item.placeId+"\");' data-value='"+item.placeId+"'>"+item.placeName+"</a></li>";
                    }
                });
            }
            htmls += "</div>";
            htmls += "</div>";
            htmls += "<span>发布时间</span>";
            htmls += "</div>";
			htmls += "<table>";

			if (recruitmentDtoList != null && recruitmentDtoList.length > 0) {
				$.each(recruitmentDtoList, function(i, item) {
					if (i % 2 == 0) {
						htmls += "<tr class='even'>";
					} else {
						htmls += "<tr class='odd'>";
					}
					htmls += "<td class='begin'><a href='/AppService/article/recruitmentDetail.shtml?informationId=" + item.informationId + "'>" + item.informationName + "</a></td>";
					htmls += "<td class='second'>" + item.positionName + "</td>";
					htmls += "<td class='third'>" + item.informationNumber + "</td>";
					htmls += "<td class='fourth'>" + item.placeName + "</td>";
					htmls += "<td class='end'>" + formatDate(item.updateDate || "") + "</td>";
					htmls += "</tr>";
				});
			}

			htmls += "</table>";

			$("#infos").html(htmls);
			 $(".table-con .joinUs-title h4.title").click(function() {
	        		$(this).siblings(".about-select-list").toggleClass("none");
	        		$(this).parent().siblings().find(".about-select-list").addClass("none");
	        	});
	        	$(".about-select-list a").click(function() {
	        		/*var selectValue = $(this).text();
	        		$(this).parents(".about-select-list").siblings(".table-con .joinUs-title h4.title").text(selectValue);*/
	        		$(this).parents(".about-select-list").addClass("none");
	        	});
	        	$(document).bind("click", function(e) {
	        		var target = $(e.target);
	        		if (target.closest(".table-con .joinUs-title h4.title,.about-select-list").length == 0) {
	        			$(".about-select-list").addClass("none");
	        		}
	        	});
			htmls2 += "<a href='javascript:queryCampusRecruitment(\"" + (page - 1) + "\")' class='pre'></a>";
			if (page == 1) {
				htmls2 += "<a class='act' href='javascript:queryCampusRecruitment(\"1\")'>1</a>";
			} else {
				htmls2 += "<a href='javascript:queryCampusRecruitment(\"1\")'>1</a>";
			}

			if (page > 3) {/* 左边加... */
				htmls2 += "<a class='omit'>...</a>";
			}

			if ((page - 1) > 1) {/* 上一页 */
				htmls2 += "<a href='javascript:queryCampusRecruitment(\"" + (page - 1) + "\")'>" + (page - 1) + "</a>";
			}

			if (page != 1 && page != maxPages) {/* 当前页 */
				htmls2 += "<a class='act' href='javascript:queryCampusRecruitment(\"" + (page) + "\")'>" + (page) + "</a>";
			}

			if (page + 1 < maxPages) {/* 下一页 */
				htmls2 += "<a href='javascript:queryCampusRecruitment(\"" + (page + 1) + "\")'>" + (page + 1) + "</a>";
			}

			if ((maxPages - page) > 2) {/* 右边加... */
				htmls2 += "<a class='omit'>...</a>";
			}

			if (page != 1) {
				if (maxPages == page) {
					htmls2 += "<a class='act' href='javascript:queryCampusRecruitment(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
				} else if (maxPages > page) {
					htmls2 += "<a href='javascript:queryCampusRecruitment(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
				}
			} else {
				if (maxPages == page) {

				} else if (maxPages > page) {
					htmls2 += "<a href='javascript:queryCampusRecruitment(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
				}
			}
			htmls2 += "<a href='javascript:queryCampusRecruitment(\"" + (page + 1) + "\")' class='next'></a>";
			htmls2 += "</div>";
			$(".page").html(htmls2);
		}
	});
}
/* 实习生招聘 */
function queryTraineeRecruitment(pages,positionId,placeId) {
	getUserRequest("pc_aboutUs_02_03");
	$(".table .nav-table ul li").removeClass("act");
	$(".table .nav-table ul li:eq(2)").addClass("act");
	var page = parseInt(returnPage(pages), 10);

	$.ajax({
		async : true,
		url : "/AppService/article/queryTraineeRecruitment.xhtml",
		data : {
			"positionId" : positionId,
			"placeId" : placeId,
			"page" : page
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			var htmls = "";
			var htmls2 = "";/* 分页 */

			var placeDtoList = data.placeDtoList;/* 工作地点 */
			var positionTypeDtoList = data.positionTypeDtoList;/* 职位类别 */
			var recruitmentDtoList = data.recruitmentDtoList;/* 招聘信息 */
			var currentPositionId = data.currentPositionId;/* 当前职位类型ID */
			var currentPlaceId = data.currentPlaceId;/* 当前工作地点ID */
			var currentPage = data.currentPage;/* 当前页数 */
			var count = data.count;/* 招聘信息记录数 */
			var maxPages = data.maxPages;/* 最大页数 */

			$("#page").val(currentPage);
			$("#maxPages").val(maxPages);

			if (count == null || count == "" || count == 0) {
				$("#infos").html("<span class='table-con-none'>暂无此类招聘信息</span>");
				$(".page").html("");
				return;
			}

			htmls += "<div class='joinUs-title'>";
            htmls += "<span>职称名称</span>";
            htmls += "<div class='about-select workstyle'>";
            htmls += "<h4 class='title'>职称类别</h4>";
            htmls += "<div class='about-select-list none'>";
            htmls += "<ul>";
            if (positionTypeDtoList != null && positionTypeDtoList.length > 0) {
                $.each(positionTypeDtoList, function(i, item) {
                    if (currentPositionId != null && currentPositionId == item.positionId) {
                        htmls +=  "<li class='act'><a href='javascript:queryTraineeRecruitment(\"1\",\""+item.positionId+"\",\"\");'>"+item.positionName+"</a></li>";
                    } else {
                        htmls +=  "<li><a href='javascript:queryTraineeRecruitment(\"1\",\""+item.positionId+"\",\"\");'>"+item.positionName+"</a></li>";
                    }
                });
            }

            htmls += "</ul>";
            htmls += "</div>";
            htmls += "</div>";
            htmls += "<span>招聘人数</span>";
            htmls += "<div class='about-select workaddress'>";
            htmls += "<h4 class='title'>工作地点</h4>";
            htmls += "<div class='about-select-list none'>";
            htmls += "<ul>";
            if (placeDtoList != null && placeDtoList.length > 0) {
                $.each(placeDtoList, function(i, item) {
                    if (currentPlaceId != null && currentPlaceId == item.placeId) {
                        htmls += "<li class='act'><a href='javascript:queryTraineeRecruitment(\"1\",\"\",\""+item.placeId+"\");'>"+item.placeName+"</a></li>";
                    } else {
                        htmls += "<li><a href='javascript:queryTraineeRecruitment(\"1\",\"\",\""+item.placeId+"\");'>"+item.placeName+"</a></li>";
                    }
                });
            }
            htmls += "</div>";
            htmls += "</div>";
            htmls += "<span>发布时间</span>";
            htmls += "</div>";
			htmls += "<table>";

			if (recruitmentDtoList != null && recruitmentDtoList.length > 0) {
				$.each(recruitmentDtoList, function(i, item) {
					if (i % 2 == 0) {
						htmls += "<tr class='even'>";
					} else {
						htmls += "<tr class='odd'>";
					}
					htmls += "<td class='begin'><a href='/AppService/article/recruitmentDetail.shtml?informationId=" + item.informationId + "'>" + item.informationName + "</a></td>";
					htmls += "<td class='second'>" + item.positionName + "</td>";
					htmls += "<td class='third'>" + item.informationNumber + "</td>";
					htmls += "<td class='fourth'>" + item.placeName + "</td>";
					htmls += "<td class='end'>" + formatDate(item.updateDate || "") + "</td>";
					htmls += "</tr>";
				});
			}

			htmls += "</table>";

			$("#infos").html(htmls);
			$(".table-con .joinUs-title h4.title").click(function() {
        		$(this).siblings(".about-select-list").toggleClass("none");
        		$(this).parent().siblings().find(".about-select-list").addClass("none");
        	});
        	$(".about-select-list a").click(function() {
        		/*var selectValue = $(this).text();
        		$(this).parents(".about-select-list").siblings(".table-con .joinUs-title h4.title").text(selectValue);*/
        		$(this).parents(".about-select-list").addClass("none");
        	});
        	$(document).bind("click", function(e) {
        		var target = $(e.target);
        		if (target.closest(".table-con .joinUs-title h4.title,.about-select-list").length == 0) {
        			$(".about-select-list").addClass("none");
        		}
        	});
			htmls2 += "<a href='javascript:queryTraineeRecruitment(\"" + (page - 1) + "\")' class='pre'></a>";
			if (page == 1) {
				htmls2 += "<a class='act' href='javascript:queryTraineeRecruitment(\"1\")'>1</a>";
			} else {
				htmls2 += "<a href='javascript:queryTraineeRecruitment(\"1\")'>1</a>";
			}

			if (page > 3) {/* 左边加... */
				htmls2 += "<a class='omit'>...</a>";
			}

			if ((page - 1) > 1) {/* 上一页 */
				htmls2 += "<a href='javascript:queryTraineeRecruitment(\"" + (page - 1) + "\")'>" + (page - 1) + "</a>";
			}

			if (page != 1 && page != maxPages) {/* 当前页 */
				htmls2 += "<a class='act' href='javascript:queryTraineeRecruitment(\"" + (page) + "\")'>" + (page) + "</a>";
			}

			if (page + 1 < maxPages) {/* 下一页 */
				htmls2 += "<a href='javascript:queryTraineeRecruitment(\"" + (page + 1) + "\")'>" + (page + 1) + "</a>";
			}

			if ((maxPages - page) > 2) {/* 右边加... */
				htmls2 += "<a class='omit'>...</a>";
			}

			if (page != 1) {
				if (maxPages == page) {
					htmls2 += "<a class='act' href='javascript:queryTraineeRecruitment(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
				} else if (maxPages > page) {
					htmls2 += "<a href='javascript:queryTraineeRecruitment(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
				}
			} else {
				if (maxPages == page) {

				} else if (maxPages > page) {
					htmls2 += "<a href='javascript:queryTraineeRecruitment(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
				}
			}
			htmls2 += "<a href='javascript:queryTraineeRecruitment(\"" + (page + 1) + "\")' class='next'></a>";
			htmls2 += "</div>";
			$(".page").html(htmls2);
		}
	});
}
/* 校园宣讲 */
function queryCampusTalk(pages, campusId) {
	getUserRequest("pc_aboutUs_02_04");
	$(".table .nav-table ul li").removeClass("act");
	$(".table .nav-table ul li:eq(3)").addClass("act");

	var page = parseInt(returnPage(pages), 10);

	$.ajax({
		async : true,
		url : "/AppService/article/queryCampusTalk.xhtml",
		data : {
			"campusName" : campusId,
			"page" : page
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			var htmls = "";
			var htmls2 = "";/* 分页 */

			var schoolDtoList = data.schoolDtoList;/* 校园列表 */
			var campusTalkDtoList = data.campusTalkDtoList;/* 校园宣讲列表 */
			var campusId = data.currentCampusId;/* 当前校园ID */
			var currentPage = data.currentPage;/* 当前页数 */
			var count = data.count;/* 招聘信息记录数 */
			var maxPages = data.maxPages;/* 最大页数 */

			$("#page").val(currentPage);
			$("#maxPages").val(maxPages);

			if (count == null || count == "" || count == 0) {
				$("#infos").html("<span class='table-con-none'>暂无此类招聘信息</span>");
				$(".page").html("");
				return;
			}

			htmls += "<div class='joinUs-title'>";
			htmls += "<ul class='school'>";
			if (campusId == null || campusId == "") {
				htmls += "<li class='act'><a href='javascript:queryCampusTalk(\"1\",\"\")'>全部</a></li>";
			} else {
				htmls += "<li><a href='javascript:queryCampusTalk(\"1\",\"\")'>全部</a></li>";
			}
			if (schoolDtoList != null && schoolDtoList.length > 0) {
				$.each(schoolDtoList, function(i, item) {
					if (campusId != null && campusId == item.schoolId) {
						htmls += "<li class='act'><a href='javascript:queryCampusTalk(\"" + currentPage + "\",\"" + item.schoolId + "\")'>" + item.schoolName + "</a></li>";
					} else {
						htmls += "<li><a href='javascript:queryCampusTalk(\"" + currentPage + "\",\"" + item.schoolId + "\")'>" + item.schoolName + "</a></li>";
					}
				});
			}

			htmls += "</ul>";
			htmls += "</div>";
			htmls += "<div class='school-con clear'>";
			htmls += "<ul>";

			if (campusTalkDtoList != null && campusTalkDtoList.length > 0) {
				$.each(campusTalkDtoList, function(i, item) {
					htmls += "<li>";
					htmls += "<dl>";
					htmls += "<dt>" + item.campusData.substr(0, 4) + "-" + parseInt(item.campusData.substr(5, 2), 10) + "</dt>";
					htmls += "<dd>" + parseInt(item.campusData.substr(8, 2), 10) + "</dd>";
					htmls += "</dl>";
					htmls += "<div class='detail'>";
					htmls += "<span class='note'>";
					htmls += "<em>" + item.schoolName + "</em>";
					htmls += "<i>" + item.campusTime + "</i>";
					htmls += "<b>" + item.campusPlace + "</b>";
					htmls += "</span>";
					htmls += "<span class='text'>" + item.reserve1 + "</span>";
					htmls += "</div>";
					htmls += "<a href='" + item.campusContent + "'><img src='/AppWeb/AppWeb_Images/images/zhaoPing_icon_download.png' height='48' width='48' class='fr loading'></a>";
					htmls += "</li>";
				});
			}

			htmls += "</ul>";
			htmls += "</div>";

			$("#infos").html(htmls);

			htmls2 += "<a href='javascript:queryCampusTalk(\"" + (page - 1) + "\",\"" + campusId + "\")' class='pre'></a>";
			if (page == 1) {
				htmls2 += "<a class='act' href='javascript:queryCampusTalk(\"1\",\"" + campusId + "\")'>1</a>";
			} else {
				htmls2 += "<a href='javascript:queryCampusTalk(\"1\",\"" + campusId + "\")'>1</a>";
			}

			if (page > 3) {/* 左边加... */
				htmls2 += "<a class='omit'>...</a>";
			}

			if ((page - 1) > 1) {/* 上一页 */
				htmls2 += "<a href='javascript:queryCampusTalk(\"" + (page - 1) + "\",\"" + campusId + "\")'>" + (page - 1) + "</a>";
			}

			if (page != 1 && page != maxPages) {/* 当前页 */
				htmls2 += "<a class='act' href='javascript:queryCampusTalk(\"" + (page) + "\",\"" + campusId + "\")'>" + (page) + "</a>";
			}

			if (page + 1 < maxPages) {/* 下一页 */
				htmls2 += "<a href='javascript:queryCampusTalk(\"" + (page + 1) + "\",\"" + campusId + "\")'>" + (page + 1) + "</a>";
			}

			if ((maxPages - page) > 2) {/* 右边加... */
				htmls2 += "<a class='omit'>...</a>";
			}

			if (page != 1) {
				if (maxPages == page) {
					htmls2 += "<a class='act' href='javascript:queryCampusTalk(\"" + (maxPages) + "\",\"" + campusId + "\")'>" + maxPages + "</a>";
				} else if (maxPages > page) {
					htmls2 += "<a href='javascript:queryCampusTalk(\"" + (maxPages) + "\",\"" + campusId + "\")'>" + maxPages + "</a>";
				}
			} else {
				if (maxPages == page) {

				} else if (maxPages > page) {
					htmls2 += "<a href='javascript:queryCampusTalk(\"" + (maxPages) + "\",\"" + campusId + "\")'>" + maxPages + "</a>";
				}
			}
			htmls2 += "<a href='javascript:queryCampusTalk(\"" + (page + 1) + "\",\"" + campusId + "\")' class='next'></a>";
			htmls2 += "</div>";
			$(".page").html(htmls2);
		}
	})

}
function returnPage(pages) {
	var page;
	var maxPages = $("#maxPages").val();

	if (parseInt(pages,10) >= parseInt(maxPages,10)) {
		page = maxPages;
	} else if (parseInt(pages,10) <= 0) {
		page = "1";
	} else {
		page = pages;
	}
	return page;
}
function changeRequest(index){
	getUserRequest("pc_aboutUs_0"+index);
}