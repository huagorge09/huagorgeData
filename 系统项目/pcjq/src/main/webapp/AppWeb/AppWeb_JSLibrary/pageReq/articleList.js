var page = 1;
var totalAmount = -1;
var num = 0;/* 列表当前加载数量 */
var sum = 0;/* 列表总记录数 */

//当前页面pageId
var pageId = "";
//来源页面ID
var pageSourceId = "";
//事件Id
var eventId = "";

$(document).ready(function(e) {
	getUserRequest("pc_applicationGroups_articleList_01");
    $(".nav.fr ul li a").removeClass("current");
    document.title = "我的消息_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
    
  //当前页面pageId
	pageId = $("#pageId").val();
	//来源页面ID
	pageSourceId = getUrlParameter("pageSourceId");
	if(null == pageSourceId || pageSourceId == ""){
		pageSourceId = pageId;
	}
	eventId = getUrlParameter("eventId");
	if(null == eventId || eventId == ""){
		eventId = "event_myMessageId";
	}
	if(!!pageSourceId && !!eventId){
		operatingRecord(pageSourceId,pageId,eventId,"");
	}
    queryUnRead();
    queryUserMessageList("1");
    var flag = queryUserinfo();
    if(flag){
    	isShowPlanTips();
    }
    
});
/* 查询未读信批条数 */
function queryUnRead(){
    $.ajax({
        async : true,
        url : "/AppService/business/queryUnReadMsg.xhtml",
        data : "",
        dataType : "json",
        cache : false,
        type : "post",
        error : function(textStatus, errorThrown) {
        },
        success : function(data) {
            if(data!=null&&data.unRead=='0'){
                $("#btnUnRead").addClass("act");
            }else{
                $("#unRead").html("("+data.unRead+")");
            }
        }
    });
};
/* 查询信息列表 */
function queryUserMessageList(pages) {
    var page = returnPage(pages);
    $.ajax({
        async : false,
        url : "/AppService/business/queryUserMessageList.xhtml",
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
                var list = data.list;
                if (list != null && list.length > 0) {
                    var htmls = "";
                    var msgId = "";
                    var msgType = "";
                    var cssName = "";
                    $.each(list, function(i, item) {
                        cssName = item.readFlag == 0 ? '' : 'read';
                        
                        var ss =new Array(item.reportTypeName,item.reportType,item.fundId,item.fundSName);
                        htmls += "<dl><dt>";
                        htmls += "<input type='checkbox' class='checkbox fl'> <span class='new-icon fl "+cssName+"'></span> <span class='new-title fl' onclick='addReportReadRecord(\""+item.reportId+"\",\""+ss+"\")' id='"+item.reportId+"' data-fundId='"+item.fundId+"'>" + item.fundLName + "</span> <span class='date fr'>"
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
                    var maxPages = data.maxPages;
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
/*打开或者下载pdf文档*/ 
function loadPdf(msgId,msgType){
    window.location.href="/AppService/business/queryUserMessageByPDF.xhtml?msgId="+msgId+"&msgType="+msgType;
}
/*信批已读保存*/ 
function addReportReadRecord(reportId,obj) {
    obj=obj.split(',');
    console.info(obj[2])
    if(obj[2]=="17D116"||obj[2]=="17D115"||obj[2]=='178031'){
        saveStatis(obj);
    }
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
            url : "/AppService/business/addReportReadRecord.xhtml",
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
                    $("#" + reportId + "").prev().addClass("read");
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
        url : "/AppService/business/addReportReadRecord.xhtml",
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

//信批访问埋点
function saveStatis(obj){
    var obj={
            reportTypeName: obj[0],
            reportType:obj[1],
            fundId:obj[2],
            fundSName:obj[3]
    }
    var channel = "01";
    obj.channel=channel;
    $.ajax({
        async:true,
        url: "/AppService/log/save.xhtml",
        dataType: "json",
        type:"POST",
        data:obj,
        cache: false,
        error : function(textStatus, errorThrown) {  
            
        }, 
        success : function (data){

        }
    }); 
}

function queryByPage(queryPage){
    page = queryPage;
    queryUserMessageList();
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

function queryUserinfo(){
	var flag = true;
	$.ajax({
		async : false,
		url : "/AppService/business/queryUserinfo.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) { 
			if (data.returnCode != null && data.returnCode == "0000") {
				//是否评级
				var riskLevel = data.riskLevel; 
				if(riskLevel == 0 || riskLevel == '0'){
					if(pageSourceId != "myMessageId"){
						$("#noOperation .risk .noOperation_text").html("年报");
					}else{
						$("#noOperation .risk .noOperation_text").html("信披");
			    	}
					$("#noOperation").show();
					$("#noOperation .realName").hide();
					$("#noOperation .anewRisk").hide();
					$("#noOperation .risk").show();
					disable_scroll();
					$("#noOperation .risk .btn").unbind("click");
					$("#noOperation .risk .btn").click(function(){
						window.location.href="/AppService/business/fund/fundList.shtml?pageSourceId="+pageSourceId+"&eventId=event_myMessageId"; 
					});
					flag = false;
					return;
				};
				//评级是否过有效期
				var resultData = checkIsRiskLevel();
				if(resultData){
					var date = new Date(resultData.riskEvalDate); 
					var nowDate = new Date();
				    var dataDiff = (nowDate - date) / 86400000;
				    //时间已过 则 提示过期
				    if(dataDiff >= 365){
				    	$("#noOperation").show();
						$("#noOperation .risk").hide();
						$("#noOperation .anewRisk").show();
						$("#noOperation .realName").hide();
						disable_scroll();
						$("#noOperation .anewRisk .btn").unbind("click");
						$("#noOperation .anewRisk .btn").click(function(){
							$("#noOperation").hide();
							$("#noOperation .risk").hide();
							$("#noOperation .anewRisk").hide();
							$("#noOperation .realName").hide();
							addCookie('riskUrl',location.href);
							window.location.href="/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO&operation=toRiskLevel&pageSourceId="+pageSourceId+"&eventId=event_myMessage_expiredId";
						});
						flag = false;
				    	return;
				    }
				}
				//是否实名
				var userType = data.userType;
				if(userType == null || userType == '10'){
					if(pageSourceId != "myMessageId"){
						$("#noOperation .realName .noOperation_text").html("年报");
					}else{
						$("#noOperation .realName .noOperation_text").html("信披");
					}
					$("#noOperation").show();
					$("#noOperation .risk").hide();
					$("#noOperation .anewRisk").hide();
					$("#noOperation .realName").show();
					disable_scroll();
					$("#noOperation .realName .btn").unbind("click");
					$("#noOperation .realName .btn").click(function(){
						window.location.href="/AppService/business/bank/realName.shtml?pageSourceId="+pageSourceId+"&eventId=event_myMessageAfter_realName";
					});
					flag = false;
			    	return;
				};
			}
		}
	});
	return flag;
}

/**
 * 未风险测评则弹出风险测评弹框
 * @returns
 */
function checkIsRiskLevel(){
	var result = "";
	var urlVal="/AppService/business/queryIsNeedTest.xhtml";
    $.ajax({
    	async:false,
		url:urlVal,
		type:"post",
		dataType:'json',
		data:{},
		error:function(){
			show_tips("网络繁忙，请稍后再试。"); 
		},
		success:function(data, textStatus){
			/*riskLevel = data.riskLevel;
			riskEvalDate = data.riskEvalDate;*/
			result =  data;
		}
	});
    return result;
};

$('.btn-meta').on('click', function (e) {
	var $this = $(e.currentTarget);
	$this.toggleClass('active');
	//$this.next().toggleClass('active');
	if($(".meta").is(":hidden")){
		$(".meta").show();
	}else{
		$(".meta").hide();
	}
	
});


/**
 * 验证是否显示信披弹框
 */
function isShowPlanTips(){
	var flag = true;
	var userId = queryUserId();
	$(".gudieBox .btn").click(function(){
		$(".gudieBox").hide();
		$(".nextGuide").show();
	});
	$(".nextGuide .know").click(function(){
		$("#guide").hide();
		$(".nextGuide").hide();
	});
	var cookieStr = getCookie(userId+"isShowPlanTips");
	if(cookieStr == null){
		var curDate = new Date();  
        //当前时间戳  
        var curTamp = curDate.getTime();  
        //当日凌晨的时间戳,减去一毫秒是为了防止后续得到的时间不会达到00:00:00的状态  
        var curWeeHours = new Date(curDate.toLocaleDateString()).getTime() - 1;  
        //当日已经过去的时间（毫秒）  
        var passedTamp = curTamp - curWeeHours;  
        //当日剩余时间  
        var leftTamp = 24 * 60 * 60 * 1000 - passedTamp;  
        var leftTime = new Date();  
        leftTime.setTime(leftTamp + curTamp);  
		document.cookie=userId+'isShowPlanTips=QUARTERDATE;expires='+leftTime.toGMTString();
		
		var nowDate = new Date();
		var time = nowDate.getFullYear() + "" +((nowDate.getMonth()+1)<10?"0":"")+(nowDate.getMonth()+1)+""+(nowDate.getDate()<10?"0":"")+nowDate.getDate();
		var planDate = queryParamList("SYSTEM","QUARTERDATE","");
		if(time <= planDate[0].pmco ){
			if(!isTip){
		    	$("#guide").show();
		    	disable_scroll();
		    	flag = false;
			}else{
				$("#guide").hide();
				enable_scroll();
			}
		}
	}
	return flag;
}


/**
 * /**
 * 页面操作记录
 * @param buried_PageSource	来源页面Id
 * @param buried_PageId		页面Id
 * @param buried_EventId	事件Id
 * @param buried_GroupId	页面分组Id 默认传空
 */
function operatingRecord(buried_PageSource,buried_PageId,buried_EventId,buried_GroupId){
	$.ajax({
        async: true,
        url: "/AppService/buriedData.xhtml",
        data: {
        	'pageSource' : buried_PageSource,
        	'pageId' : buried_PageId,
            'eventId' :buried_EventId,
            'groupId' : buried_GroupId
        },
        dataType: "json",
        cache: false,
        type: "POST",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {}
    });
};