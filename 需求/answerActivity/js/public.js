//时间格式化
function format(d) {
	d = new Date(d);
	return d.getFullYear() + "/" + (d.getMonth() + 1) + "/" + d.getDate();
}
//验证中文姓名
function ischinese(name){
    return /^[\u2E80-\u9FFF]{2,10}$/.test(name)
}  
//验证手机号
function  isMobile(phone) {
        return /^0?(13[0-9]|15[012356789]|18[012356789]|14[57]|17[03678])[0-9]{8}$/.test(phone);
}

//运动事件监听
if (window.DeviceMotionEvent) {
     window.addEventListener('devicemotion',deviceMotionHandler,false);
}
 /*
  * 获取链接参数
  */
function getUrlSearchParams(_name){
	var name,value='';
	    var str=window.location.href;
	    var num=str.indexOf("?");
	    str=str.substr(num+1);
	    var arr=str.split("&");
	    for(var i=0;i < arr.length;i++){
	        num=arr[i].indexOf("=");
	        if(num>0){
	            name=arr[i].substring(0,num);
	            if(name.replace(/^\s+|\s+$/g,"") == _name){
	                value=arr[i].substr(num+1);
	                break;
	            }
	        }
	    }
	    return decodeURI(value);
}


//获取加速度信息
//通过监听上一步获取到的x, y, z 值在一定时间范围内的变化率，进行设备是否有进行晃动的判断。
//而为了防止正常移动的误判，需要给该变化率设置一个合适的临界值。
var SHAKE_THRESHOLD = 4000;
var last_update = 0;
var x, y, z, last_x = 0, last_y = 0, last_z = 0;
function deviceMotionHandler(eventData) {
        var acceleration =eventData.accelerationIncludingGravity;
        var curTime = new Date().getTime();
        if ((curTime-last_update)> 10) {
            var diffTime = curTime -last_update;
            last_update = curTime;
            x = acceleration.x;
            y = acceleration.y;
            z = acceleration.z;
            var speed = Math.abs(x +y + z - last_x - last_y - last_z) / diffTime * 10000;
            if (speed > SHAKE_THRESHOLD) {
            	var flag=$(".draw").is(":visible");
				if(flag){
					document.getElementById("bgm").play();
					alert("你摇奖了");
					$.ajax({
						async : false,
						url:config.service.isFollow,
						type:"get",
						data:{
							"openId":openId,
						},
						dataType:"json",
						success:function(res){
							if(res.returnCode=="0"){
								if(res.prizeLevel=="0"){
									
								}
							}
						},
						error:function(){
							alert("摇奖了，但是服务器异常了")
						}
					})
				}
            }
            last_x = x;
            last_y = y;
            last_z = z;
        }
}


$(function(){
	$(".reserves input").keyup(function(){
		if($("input[name=userName]").val()!==""&&$("input[name=mobilePhone]").val()!==""){
			$("button").removeClass("disabled");
			checkAppoInfoParam()
		}
    });
    //输入框光标事件
	$(".reserves input").focus(function(){
		$(this).parents("div").addClass("foucs");
		$(this).parents("li").find("p").hide().text("");
		checkAppoInfoParam()
	});
	$(".reserves input").blur(function(){
		$(this).parents("div").removeClass("foucs");
		checkAppoInfoParam()
	});
	$(".reservesSuc .close,.reserves .close").click(function(){
		$("input[name=userName]").val('');
		$("input[name=mobilePhone]").val('');
        $(".reserves li p").hide();
        $(".reserves,.reservesSuc").hide();
        $("#saveBtn").addClass("disabled").attr("onclick","");
        index.queryMyPrize();
	})
})
/* 验证输入框的有效性  */
function checkAppoInfoParam(){
	var userName = $("#userName").val();
	var mobilePhone = $("#mobilePhone").val();
	if(userName == null || userName == ""){
		$("#saveBtn").addClass("disabled").attr("onclick","");
		return;
	}else if(mobilePhone == null || mobilePhone == ""){
		$("#saveBtn").addClass("disabled").attr("onclick","");
		return;
	}else{
		$("#saveBtn").removeClass("disabled").attr("onclick","saveAppointInfo()");
	}
}
//点击信息提交
function saveAppointInfo(){
		var userName=$.trim($("input[name=userName]").val());
		var mobilePhone=$.trim($("input[name=mobilePhone]").val());
		var flag=true;
		if(userName==""||!ischinese(userName)){
			$("input[name=userName]").parents("li").find("p").show().text("请输入2-10字中文姓名");
			flag=false;
		}	
		if (!isMobile(mobilePhone)) {
			$("input[name=mobilePhone]").parents("li").find("p").show().text("请输入正确的手机号码");
			flag=false;
		}
		if(flag){
			userDateSumbit(userName,mobilePhone);
		}
}
//用户信息提交
function userDateSumbit(userName,mobilePhone){
	$.ajax({
			async : false,
			url:config.service.userGetAward,
			type : "post",
			dataType :'json',
			data : {
				"userName":userName,
				"userTel":mobilePhone,
				"prizeId":prizeId,
				"openId":openId
			},
			success : function(data) {
				if(data.returnCode=="0"){
					$(".reserves").hide();
					$(".reservesSuc").show();
					$("input[name=userName]").val('');
					$("input[name=mobilePhone]").val('');
					$("#saveBtn").addClass("disabled").attr("onclick","");
					
				}else{
					alert("系统异常")
				}
			}
		});
}