$(function(){
    var itemIndex = 0;
    var tab1LoadEnd = false;
    var tab2LoadEnd = false;
    var pageTab1=0;
    var pageTab2=0;
    // tab
    $('.tabInfo div').on('click',function(){
        var $this = $(this);
        itemIndex = $this.index();
        $this.addClass('current').siblings('div').removeClass('current');
        pageTab1=0;
        pageTab2=0
        // 如果选中菜单一
        if(itemIndex == '0'){
            // 如果数据没有加载完
            if(!tab1LoadEnd){
                // 解锁
                dropload.unlock();
                dropload.noData(false);
            }else{
                // 锁定
                dropload.lock('down');
                dropload.noData();
            }
              $('.lists').eq(0).find("ul").html("");
              $('.lists').eq(0).show();
              $('.lists').eq(1).hide();
              $(".info1").find("img").attr("src","images/zhuicon1.png")
              $(".info2").find("img").attr("src","images/zhuicon2no.png")
              operatingRecord(pageSource,"event_wx_fdHelpListOnMe",pageId,"","","","") 
        // 如果选中菜单二
        }else if(itemIndex == '1'){
            if(!tab2LoadEnd){
                // 解锁
                dropload.unlock();
                dropload.noData(false);
            }else{
                // 锁定
                dropload.lock('down');
                dropload.noData();
            }
              $('.lists').eq(1).find("ul").html("");
              $('.lists').eq(1).show();
              $('.lists').eq(0).hide();
              $(".info2").find("img").attr("src","images/zhuicon2.png")
              $(".info1").find("img").attr("src","images/zhuicon1no.png")
              operatingRecord(pageSource,"event_wx_fdHelpListOnFriend",pageId,"","","","") 
        }
        // 重置
        dropload.resetload();
    });


    // dropload
    var dropload = $('.content').dropload({
        scrollArea : window,
        loadDownFn : function(me){
            // 加载菜单一的数据
            if(itemIndex == '0'){
               pageTab1++
               $.ajax({
	                type: 'GET',
	                url:config.service.obtainMessageList,
	                dataType: 'json',
	                data:{
                    	userId:userId,
                    	activityId:activityId,
                    	pageIndex:pageTab1,
                    	type:"1"
                    },
	                success: function(data){
	                    var arrLen = data.data.length;  
	                    var result = '';
	                    if(arrLen > 0){
	                       
                    		for (var i=0;i<data.data.length;i++) {
                    			
                    				result+='<li >'+
                    				      '<div>我为<span>'+data.data[i].nickName+'</span>助力一张碎片</div>'
                    			
                    			result+='<p>'+data.data[i].date+'</p></li>'
                    		}
	                    // 如果没有数据
	                    }else{
	                        // 锁定
	                        me.lock();
	                        // 无数据
	                        me.noData();
	                    }
	                    // 为了测试，延迟1秒加载
	                    
	                        // 插入数据到页面，放到最后面
	                       $('.lists').eq(0).find("ul").append(result);
	                        // 每次数据插入，必须重置
	                       me.resetload();
	                 
	                },
	                error: function(xhr, type){
	                   // alert('Ajax error!');
	                    // 即使加载出错，也得重置
	                    me.resetload();
	                }
	            });
            // 加载菜单二的数据
            }else if(itemIndex == '1'){
               pageTab2++
               $.ajax({
	                type: 'GET',
	                url:config.service.obtainMessageList,
	                dataType: 'json',
	                data:{
                    	userId:userId,
                    	activityId:activityId,
                    	pageIndex:pageTab2,
                    	type:"0"
                    },
	                success: function(data){
	                    var arrLen = data.data.length; 
	                     var result = '';
	                    if(arrLen > 0){
	                        
                    		for (var i=0;i<data.data.length;i++) {
                    			   //好友为你助力
                    		    result+='<li >'+
                    				      '<div>您的好友<span>'+data.data[i].nickName+'</span>为您助力一张碎片</div>'
                    			
                    			result+='<p>'+data.data[i].date+'</p></li>'
                    		}
	                    // 如果没有数据
	                    }else{
	                        // 锁定
	                        me.lock();
	                        // 无数据
	                        me.noData();
	                    }
	                    // 为了测试，延迟1秒加载
	                   
	                        // 插入数据到页面，放到最后面
	                        $('.lists').eq(1).find("ul").append(result);
	                        // 每次数据插入，必须重置
	                        me.resetload();
	                  
	                },
	                error: function(xhr, type){
	                   // alert('Ajax error!');
	                    // 即使加载出错，也得重置
	                    //me.resetload();
	                }
	            });
            }
        }
    });
});
	
function helpPrize(){
	location.href="rankingList2.html?toUserId="+toUserId+"&subscribeChannel="+subscribeChannel+"&pageSource="+pageId+"&eventId=event_wx_fdtAwardWinngListFromHelpList"
}
function returnIndex(){
	location.href="draw.html?toUserId="+toUserId+"&subscribeChannel="+subscribeChannel+"&pageSource="+pageId+"&eventId=event_wx_fdReturnIndexFromHelpList"
}
var pageId="wx_fatherday_helpList";
var eventId="";
/**
 * 初始化数据埋点
 */
dataRecord() 
function dataRecord() {
	if(getUrlSearchParams("pageSource")) {
		pageSource = getUrlSearchParams("pageSource")
	} else {
		pageSource = pageId;
	}
	if(getUrlSearchParams("eventId")) {
		eventId = getUrlSearchParams("eventId")
	}
	if(eventId && pageSource) {
		operatingRecord(pageSource,eventId,pageId,"","","","")
	}
}
