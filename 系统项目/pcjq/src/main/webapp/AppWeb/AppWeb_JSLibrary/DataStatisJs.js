function getUserRequest(subPath){
	var curTime = getNowFmtDate();
	var curTimeStamp = Date.parse(new Date());
	var path = location.pathname;
	var subPath = subPath;
	var urlParameter = "";
	var userId = "";
	var channel = "01";
	var type = "11";
	var others = "";
	var reqSource = document.referrer;
	
	if(location.href.split("?").length > 1){
		urlParameter = location.href.split("?")[1];
	}
	
	$.ajax({
		async:true,
		url: "/AppService/log/getUserRequest.stat",
		dataType: "json",
        type:"POST",
		data: {
			"curTime":curTime,
			"curTimeStamp":curTimeStamp,
			"path":path,
			"subPath":subPath,
			"urlParameter":urlParameter,
			"userId":userId,
			"channel":channel,
			"type":type,
			"others":others,
			"reqSource":reqSource
		},
		cache: false,
		error : function(textStatus, errorThrown) {  
 			
		}, 
		success : function (data){

		}
	}); 
	
}


/**
 * 获取当前的时间并返回
 * 
 * @returns {String} YYYYMMDD-HHMISS
 */
function getNowFmtDate(){
	var d = new Date();
	var month = d.getMonth()+1;
	var date = d.getDate();
	var hours = d.getHours();
	var minutes = d.getMinutes();
	var second = d.getSeconds();
	
	if(parseInt(month) >= 0 && parseInt(month) <= 9){
		month = "0"+month;
	}
	if(parseInt(date) >= 0 && parseInt(date) <= 9){
		date = "0"+date;
	}
	
	if(parseInt(hours) >= 1 && parseInt(hours) <= 9){
		hours = "0"+hours;
	}else if(parseInt(hours) == 0){
		hours = "00";
	}
	if(parseInt(minutes) >= 1 && parseInt(minutes) <= 9){
		minutes = "0"+minutes;
	}else if(parseInt(minutes) == 0){
		minutes = "00";
	}
	if(parseInt(second) >= 1 && parseInt(second) <= 9){
		second = "0"+second;
	}else if(parseInt(second) == 0){
		second = "00";
	}
	
	var currDateTime = ""+d.getFullYear()+month+date+"-"+hours+minutes+second;
	
	return currDateTime;
}