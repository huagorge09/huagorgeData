var page = 1;
var totalAmount = -1;
var num = 0;/* 列表当前加载数量 */
var sum = 0;/* 列表总记录数 */
$(document).ready(function(){
	getUserRequest("pc_companyList");
	document.title = "机构用户信息披露_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	checkCompanyUser();
	queryUnReadForCompany();
	queryUserMessageList("1");
	$(".My-center").addClass("act");
	$(".header-nav-con .nav ul li a").removeClass("current");
})
/*查询用户是否已经登录*/
function checkCompanyUser(){
	$.ajax({
    	async:false,
        url: "/AppService/setUp/checkCompanyUser.xhtml",
        dataType: "json",
        type:"POST",
        data: {},
        cache: false,
        error : function(textStatus, errorThrown) {
        	show_tips("网络繁忙，请稍后再试。");
        }, 
        success : function (data) {
			if(data.returnCode != null && data.returnCode == "0000"){
				$("#loginYesPublic").show();
				$("#login_userinfo_public").html(data.fundAccoEncry).prop("href","/company/companyAccountInfo.shtml");
				$("#loginYes").hide();
				$("#loginNo").hide();
				$(".My-center").attr("href","/company/companyAccountInfo.shtml");
				if(data.type=="abs"){
					$(".money-overviews").hide()
				}
			}else{
				goToURL("/login/login.shtml");
			}
        }
    });
}
/* 查询信披列表 */
function queryUserMessageList(pages) {
	var page = returnPage(pages);
    $.ajax({
        async : true,
        url : "/AppService/setUp/queryUserMessageListForCompany.xhtml",
        data : {
            "page" : page
        },
        dataType : "json",
        cache : false,
        type : "post",
        error : function(textStatus, errorThrown) {
        },
        success : function(data) {
            if (data.returnCode != null && data.returnCode == "0000") {
                var list = data.fundReportsDtoList;
                if (list != null && list.length > 0) {
                    var htmls = "";
                    var msgId = "";
                    var msgType = "";
                    var cssName = "";
                    $.each(list, function(i, item) {
                    	cssName = item.readFlag == 1 ? 'read' : '';
                        htmls += "<dl><dt>";
                        htmls += "<input type='checkbox' class='checkbox fl'> <span class='new-icon fl "+cssName+"'></span> <span class='new-title fl' onclick='addReportReadRecord(\""+item.reportId+"\")' id='"+item.reportId+"' data-fundId='"+item.fundId+"'>" + item.fundLName + "</span> <span class='date fr'>"
                                + formatDate(item.date) + "</span></dt>";
                        msgId = item.reportId;
                        msgType = item.reportTextType;
                        htmls += "<dd id='rep"+item.reportId+"' class='none repCss'>";
                        htmls += "<p>" + item.content + "</p>";
                        if(msgType == '2' || msgType == '3' || msgType == '5'){/* pdf文件类型  */
                            if(item!=null){
                                htmls += "<div class='download-file'>";
                                htmls += "<a href='javascript:loadPdf(\"" + msgId + "\",\"" + msgType + "\")'>" + item.fundLName + "</a>";
                                htmls += "</div>";
                            }
                        }
                        htmls += "</dd>";
                        htmls +="</dl>";
                    });
                    $("#msgList").html(htmls);
                    var htmls2 = "";
                    var page = data.currentPage;
        			var count = data.count;
        			var maxPages = data.maxPage;
        			$("#page").val(page);
        			$("#maxPages").val(maxPages);
        			
        			htmls2 += "<a href='javascript:queryUserMessageList(\""+(page-1)+"\")' class='pre'></a>";
        			if(page == 1){
        				htmls2 += "<a class='act' href='javascript:queryUserMessageList(\"1\")'>1</a>";
        			}else{
        				htmls2 += "<a href='javascript:queryUserMessageList(\"1\")'>1</a>";
        			}
        			
        			if(page > 3){/* 左边加...*/
        				htmls2 += "<a class='omit'></a>";
        			}
        			
        			if((page-1) >1){/* 上一页*/
        				htmls2 += "<a href='javascript:queryUserMessageList(\""+(page-1)+"\")'>"+(page-1)+"</a>";
        			}
        			
        			if(page != 1 && page != maxPages){/* 当前页*/
        				htmls2 += "<a class='act' href='javascript:queryUserMessageList(\""+(page)+"\")'>"+(page)+"</a>";
        			}
        			
        			if(page+1 < maxPages){/*下一页*/
        				htmls2 += "<a href='javascript:queryUserMessageList(\""+(page+1)+"\")'>"+(page+1)+"</a>";
        			}
        			
        			if((maxPages - page) > 2){/* 右边加...*/
        				htmls2 += "<a class='omit'></a>";
        			}
        			
        			if(page != 1){
        				if(maxPages == page){
        					htmls2 += "<a class='act' href='javascript:queryUserMessageList(\""+(maxPages)+"\")'>"+maxPages+"</a>";
        				}else if(maxPages > page){
        					htmls2 += "<a href='javascript:queryUserMessageList(\""+(maxPages)+"\")'>"+maxPages+"</a>";
        				}
        			}else{
        				if(maxPages == page){
        					
        				}else if(maxPages > page){
        					htmls2 += "<a href='javascript:queryUserMessageList(\""+(maxPages)+"\")'>"+maxPages+"</a>";
        				}
        			}
        			htmls2 += "<a href='javascript:queryUserMessageList(\""+(page+1)+"\")' class='next'></a>";
        			htmls2 += "</div>";
        			$(".page").html(htmls2);
                } else{
                	$("#noMsg").show();
                	$("#msgList").hide();
                }
            } else{
            	$("#noMsg").show();
            	$("#msgList").hide();
            }
        }
    });
}
function returnPage(pages){
	var page;
	var maxPages = $("#maxPages").val();
	
	if(parseInt(pages,10) >= parseInt(maxPages,10)){
		page = maxPages;	
	}else if(parseInt(pages,10) <= 0){
		page = "1";
	}else{
		page = pages; 
	}
	return page;
}
/*信批已读保存*/ 
function addReportReadRecord(reportId) {
	if ($("#rep"+reportId).css("display")=="block"){
		$("#rep"+reportId).hide();
    } else {
    	$(".repCss").hide();
        $("#rep"+reportId).show();
    }
	var fundId = $("#" + reportId).attr("data-fundId");
	if(!$("#"+reportId+"").prev().hasClass("read")){
		$.ajax({
			async : true,
			url : "/AppService/setUp/addReportReadRecordForCompany.xhtml",
			data : {
				"fundId" : fundId,
				"reportId" : reportId
			},
			dataType : "json",
			cache : false,
			type : "post",
			error : function(textStatus, errorThrown) {
			},
			success : function(data) {
				if (data.returnCode != null && data.returnCode == '0000') {
					$("#"+reportId+"").prev().addClass("read");
					var readNum = $("#unRead").html().replace('(', '').replace(')', '');
					var unRead = readNum - 1;
					$("#unRead").html('(' + unRead + ')');
					if($(".mynews-tab-con dl dt .read").length==$(".mynews-tab-con dl dt").length){
						$("#btnUnRead").addClass("act");
						$("#btnUnRead").prop("onclick","");
					}
				}
			}
		});
	}
}
/*信批全部已读保存*/ 
function addReportReadRecordAll() {
	$.ajax({
		async : true,
		url : "/AppService/setUp/addReportReadRecordForCompany.xhtml",
		data : {
			"fundId" : "XXXXXX",
			"reportId" : "XXXXXX"
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			if (data.returnCode != null && data.returnCode == '0000') {
				window.location.reload();
			}
		}
	});
}
/*打开或者下载pdf文档*/ 
function loadPdf(msgId,msgType){
	window.location.href="/AppService/setUp/queryUserMessageByPDFForCompany.xhtml?msgId="+msgId+"&msgType="+msgType;
}
/* 查询未读信批条数 */
function queryUnReadForCompany(){
	$.ajax({
		async : true,
		url : "/AppService/setUp/queryUnReadForCompany.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			if(data!=null&&data.unRead=='0'){
				$("#btnUnRead").addClass("act");
				$("#btnUnRead").prop("onclick","");
			}else{
				$("#unRead").html("("+data.unRead+")");
			}
		}
	});
};
/* 退出登录 */
function checkOutLoginForCompany(){
	$.ajax({
		async : true,
		url : "/AppService/setUp/logoutForCompany.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			window.location.href = "/login/login.shtml";
		}
	});
};