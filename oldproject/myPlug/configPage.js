var configPage={
	version:page_version?page_version:parseInt(new Date().getTime()/3600/1000),
	jsPageList:[
	   'js/....js'
	]
	init:function(){
		var _self=this;
		_self.jsPageList.forEach(function(src){
			document.write('<script type="text/javascript" src='+src+'?version='+_self.version+'></script>')
		})
	}
}
configPage.init()


//https://www.sino-life.com/SL_LEM/pay/weixin/showPage.do?version=20190121&goUrl=/grown/grown_extension_activity/2019/NewYearMoney/html/index.html