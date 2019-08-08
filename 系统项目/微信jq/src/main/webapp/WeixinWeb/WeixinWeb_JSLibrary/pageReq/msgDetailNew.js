$(document).ready(function(e) {
	$(".header .top-a h2").html("我的消息");
	document.title="我的消息";
	myScroll = new IScroll('#wrapper', {
		scrollbars: true,
		mouseWheel: true,
		interactiveScrollbars: true,
		shrinkScrollbars: 'scale',
		fadeScrollbars: true
	});
	queryUserMessage();
	getUserRequest("msg-detail");/* 此处subPath为页面内行为 */
});
document.addEventListener('touchmove', function(e) {e.preventDefault();}, false);
function queryUserMessage(){
	var msgId = getUrlParameter("msgId");
	var msgType = getUrlParameter("msgType");
	$.ajax({
    	async:true,
        url: "/WeixinService/business/queryUserMessage.xhtml",
        data: {
        	"msgId":msgId,
        	"msgType":msgType
        },
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
        }, 
        success : function (data){
        	if(msgType != null && msgType == "1"){/* html格式 */
        		$("#title").html(data.fundReportsDto.fundLName);
        		$("#date").html(data.fundReportsDto.date);
        		$("#content").html("<p>"+data.fundReportsDto.content+"</p>");
        	}else if(msgType == '2' || msgType == '3' || msgType == '5'){/* pdf文件类型  */
        		$("#title").html(data.fundReportsPDFDto.fundLName);
        		$("#date").html(data.fundReportsPDFDto.date);
        		$("#content").html("<p id='contentLink'>相关下载：<a href='javascript:loadPdf(\""+msgId+"\",\""+msgType+"\")'>"+data.fundReportsPDFDto.fileName+"</a></p>");
        	}else{
        		redirectUrl("/WeixinService/business/query/msgListNew.shtml");
        	}
        	myScroll.refresh();
        }
    });
}
/* 打开或者下载pdf文档 */
function loadPdf(msgId,msgType){
	var u = navigator.userAgent.toLowerCase();  
    if(u.match(/MicroMessenger/i)=="micromessenger") {/* 微信端 */
    	
    	if (u.indexOf('android') > -1 || u.indexOf('linux') > -1) {/* 安卓手机 */
        		errorRemark("该公告为pdf文档，请将本页在浏览器中打开");
    	
		} else if (u.indexOf('iphone') > -1) {/* 苹果手机 */
			window.location.href="/WeixinService/business/queryUserMessageByPDF.xhtml?msgId="+msgId+"&msgType="+msgType;
			
		} else if (u.indexOf('windows phone') > -1) {/* winphone手机 */
    		errorRemark("该公告为pdf文档，请将本页在浏览器中打开");
		}else {
    		errorRemark("该公告为pdf文档，请将本页在浏览器中打开");
		}
    
    } else {/* 其他浏览器端   */
    	redirectUrl("/WeixinService/business/queryUserMessageByPDF.xhtml?msgId="+msgId+"&msgType="+msgType)
    } 
}