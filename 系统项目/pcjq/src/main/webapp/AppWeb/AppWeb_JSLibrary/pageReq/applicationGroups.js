var unReadNum=0;
var isTip = false;

$(document).ready(function(){
	$(".header .header-nav .My-center").addClass("act");
	$(".header .header-nav .My-center img").addClass("none").eq(1).removeClass("none");
	$(".nav.fr ul li a").removeClass("current");
	document.title = "我的财富_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	queryUnRead();
	getMenu();
	queryUser();

    //合格投资者认证开关
	openAualified()
    //mgm积分功能开关
    integralOpenOrClose()
})
/*查询未读信批条数*/
function queryUnRead(){
	$.ajax({
		async : false,
		url : "/AppService/business/queryUnReadMsg.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			unReadNum=data.unRead;
		}
	});
};
/*查询用户消息*/
function queryUser(){
	$.ajax({
		async : false,
		url : "/AppService/setUp/headerInfo.xhtml",
		dataType : "json",
		type : "POST",
		data : {},
		cache : false,
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			$("#custName").html(data.name);
		}
	});
}
/*获取菜单*/
function getMenu(){
	var mainCatId = getUrlParameter("mainCatId");
	var thirdCatId = getUrlParameter("thirdCatId");
	thirdCatId = removeSpecialStr(thirdCatId);
	$.ajax({
		async : false,
		url : "/AppService/setUp/getMenu.xhtml",
		data : {
			"mainCatId" : mainCatId,
			"thirdCatId" : thirdCatId
		},
		type : 'post',
		dataType : "json",
		cache : false,
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			$.each(data.defaultMainCatId, function(i, item) {
				var cssAct = data.currentThirdCatId == item.id ? 'act' : '';
				if (item.name == "我的消息" && unReadNum != 0) {
					var html = "<li class='"+item.param + " " + cssAct + "'>" +
							"<img src='/AppWeb/AppWeb_Images/images/new.png' class='rotate' style='position: absolute;display:none;width: auto;height: auto;top: 4px;'>" +
							"<a parentId='" + item.parentId + "' id='" + item.id + "' href='/AppService/applicationGroups.shtml?mainCatId="
							+ item.parentId + "&thirdCatId=" + item.id + "' url='" + item.url + "'>" + item.name + "</a><em>" + unReadNum + "</em></li>";
				}else if(item.name == "我的消息"){
					var html = "<li class='"+item.param + " " + cssAct + "'>" +
							"<img src='/AppWeb/AppWeb_Images/images/new.png' class='rotate'  style='position: absolute;display:none;width: auto;height: auto;top: 4px;'>" +
							"<a parentId='" + item.parentId + "' id='" + item.id + "' href='/AppService/applicationGroups.shtml?mainCatId="
							+ item.parentId + "&thirdCatId=" + item.id + "' url='" + item.url + "'>" + item.name + "</a></li>";
				}else if(item.name=="投资者认证"){
					var html = "<li class='"+item.param + "'>" +
							"<img src='/AppWeb/AppWeb_Images/images/new.png' class='rotate'  style='position: absolute;display:none;width: auto;height: auto;top: 4px;'>" +
							"<a parentId='" + item.parentId + "' id='" + item.id + "' href='javascript:qualified1();'>" + item.name + "</a></li>";
				}
				else {
					var html = "<li class='"+item.param + " " + cssAct + "'><a parentId='" + item.parentId + "' id='" + item.id + "' href='/AppService/applicationGroups.shtml?mainCatId="
							+ item.parentId + "&thirdCatId=" + item.id + "' url='" + item.url + "'>" + item.name + "</a></li>";
				}
				$("#menu").append(html);
			});
			var url = data.currentThirdCatUrl;
			eContentFun({
				"url" : url
			});
		}
	});
	myMessage();
}

/*
 * 根据条件查询数据字典数据
 */
function queryParamList(paramType,paramKey,pmValueOne){
    var data = null;
    $.ajax({
        async: !1,
        url: "/AppService/business/queryParamList.xhtml",
        data: {
            paramType :paramType,
            paramKey : paramKey,
            pmValueOne : pmValueOne
        },
        dataType: "json",
        cache: !1,
        type: "POST",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {
            if(n != null && n.resultCode =="0000"){
                data = n.data;
            }
        }
    });
    return data;
}

/**
 * 在一定的时间段内我的消息按钮上出现new小图标
 */
function myMessage(){
	var nowDate = new Date();
	var time = nowDate.getFullYear() + "" +((nowDate.getMonth()+1)<10?"0":"")+(nowDate.getMonth()+1)+""+(nowDate.getDate()<10?"0":"")+nowDate.getDate();
	var planDate = queryParamList("SYSTEM","QUARTERDATE","");
	var planMsgKey = queryParamList("SYSTEM","PLANMSGKEY","");
	var title = planMsgKey[0].pmco;//关键字
	if(time <= planDate[0].pmco){
		var data = queryMsgLikeTitle(title);
		if(data.isShowTips == "Y"){
			$(".mynews img").css('display','block');
			isTip = true;
		}else{
			$(".mynews img").css('display','none');
		}
	}
}

function state1(){
	//$(".mynews img").css("transform","rotate(-5deg)");
	$(".mynews a").css({fontSize:"16px",fontWeight: "bold"});
	setTimeout(state2,250);
}
function state2(){	   
	//$(".mynews img").css("transform","rotate(0deg)");
	$(".mynews a").css({fontSize:"14px",fontWeight: "100"});
	setTimeout(state1,250);
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
}



/**
 * 合格投资者认证
 */



function qualified1(){
    var _self=this;
	$.ajax({
        url  :'/AppService/business/queryQualifiedUserInfoByIdno.xhtml',
        type : 'POST',
        async:false,
        dataType : 'json',
        success : function(data) {
    	  if(data.returnCode=="0000"){
    	  	   if(data.data&&data.data.length>0){
    	  	   	   var status=data.data[0].statusRecord.status
    	  	   	   if(status=="F"){
    	  	   	   	    $.ajax({
    			            url  :'/AppService/business/modifyAccreditedInvestorInfoStatus.xhtml',
    			            type : 'POST',
    			            data : {"updateStatus":"R"},
    			            async:false,
    			            dataType : 'json',
    			            success : function(data) {
    			        	  if(data.returnCode=="0000"){
    			        		  location.href="/AppService/business/qualified/qualified.shtml";
    			        	  } else{
    			        		  DJ.alert(data.returnMsg);
    			        	  }
    			           }
				       })
    	  	   	   }else if(status=="U"){
    	  	   	   	   DJ.confirm("您的合格投资信息已由客户经理上传，请确认提交审核",function(result){
    	  	   	   	    	if(result){
    	  	   	   	    	   location.href="/AppService/business/qualified/qualified.shtml";
    	  	   	   	    	}
    	  	   	   	    },'温馨提示');
    	  	   	   }else if(status=="S"){
    	  	   	      	DJ.alert('您的合格投资信息已审核通过,您已经是合格投资者')  
    	  	   	   }else if(status=="N"){
    	  	   	      	DJ.alert('您的合格投资信息正在审核中，请耐心等待')  
    	  	   	   }else if(status=="R"){
    	  	   	   	     location.href="/AppService/business/qualified/qualified.shtml";
    	  	   	   }
  	  	   	   }else{
      	  	   	   	$.ajax({
			            url  :'/AppService/business/queryAccreditedInvestorConditions.xhtml',
			            type : 'POST',
			            async:false,
			            dataType : 'json',
			            success : function(data) {
			        	  if(data.returnCode=="0000"){
			        	  	   if(data.data.financialCertificate&&data.data.investCertificate){
			        	  	   	    DJ.alert('您已经是合格投资者');
			        	  	   }else{
				        	  	   	DJ.confirm("根据监管要求，需要您完成合格投资者认证",function(result){
		        	  	   	   	    	if(result){
		        	  	   	   	    	   location.href="/AppService/business/qualified/qualified.shtml";
		        	  	   	   	    	}
		        	  	   	   	    },'温馨提示');
		        	  	   	   	    $(".popbotton1").html("线上认证")
		        	  	   	   	    $(".popbotton2").html("线下认证")
		        	  	   	   	    $(".buttonPop").find("a:nth-child(1)").addClass("popbotton2").removeClass("popbotton1").css("background","#fff");
							        $(".buttonPop").find("a:nth-child(2)").addClass("popbotton1").removeClass("popbotton2");
							        $(".popClose").remove() 
			        	  	   }
			        	  }
			           }
				       })
      	  	   	   }
    	  	 }else if(data.returnCode=="9005"){
		      	 DJ.alert(data.returnMsg,'',function(){
		      	 	location.href="/AppService/business/bank/realName.shtml"
		      	 })
		      	 $(".popClose").remove()
        	 }else{
        	 	 DJ.alert(data.returnMsg);
        	 }
       }
   })
}
// 合格投资者功能开关
function openAualified() {
    var param= queryParamComm("SYSTEM","ACINVCONF","");
    var pmnm="";
    for(var i=0;i<param.length;i++){
        if(param[i].pmco=="MAIN"){
            pmnm=param[i].pmnm;
        }
    }
    if(pmnm=="0"){
        $(".qualified").remove()
    }
}

// 积分功能开关
function integralOpenOrClose() {
    var param= queryParamComm("SYSTEM","enableIntegral","")[0].pmco
    if(param=="0"){
        $('#menu .myIntegral').remove()
    }
}