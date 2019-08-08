
/*****授权*****/
var urlParams = getUrlParams();
var userId="";
var userid=urlParams['userid']?urlParams['userid']:localStorage.getItem("userid");
var openId="";
var host=window.location.protocol+"//"+window.location.host;
var apiHost="" //生产为相对路径
initPageAuth();
function initPageAuth(){
      
	if(!userid){  //判断用户有没有静默授权
		localStorage.setItem("eventId",getUrlParameter("eventId"))
		localStorage.setItem("pageSource",getUrlParameter("pageSource"))
		location.href=host+"/auth/proxy-silent.html?scope=snsapi_base&target_url="+host+"/WeixinService/adviser.shtml";
	}
        queryAdviser()
}

/*****授权end*****/

function queryAdviser(){
    $.ajax({
        url : "/WeixinService/queryUserCustService.xhtml",
        data :{
			userId:userid
		},
        dataType : "json",
        cache : false,
        type : "post",
        success : function(data) {
        	if(data.returnCode=="0000"){
        		var data=data.data;
        		$("#custserPhoto").attr("src",data.custserPhoto)
        		$("#custserName").html(data.custserName)
        		$("#custserMobile").html(data.custserMobile)
                $("#custserMobile,#custserMobile2").attr("href","tel:"+data.custserMobile)
                $("#custserNickname").html(data.custserNickname)
                $("#wxImg").attr("data-src",data.custserPersonalQrCode)
                $("#custserGraduateSchool").html(data.custserGraduateSchool)
                $("#custserWorkYear").html(data.custserWorkYear)
        	}else if(data.returnCode=="1"){
			location.href=host+"/auth/proxy-silent.html?scope=snsapi_base&target_url="+host+"/WeixinService/adviser.shtml";
		}
        }
    });
}
/**
 * 显示微信二维码
 */
function addWetChat(obj){
    var src=$(obj).attr("data-src");
    $(".qrcode img").attr("src",src)
    $("#hintDiv").show();
}
/**
 * 显示二维码
 */
function closeQrcode(){
    $("#hintDiv").hide();
}