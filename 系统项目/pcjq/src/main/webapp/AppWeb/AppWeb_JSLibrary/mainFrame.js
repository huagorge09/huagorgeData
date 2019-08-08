/**
 * @desc 招商财富网上交易平台 （原招钱宝首页js）
 * @author Liudh
 * @加载菜单指向的页面内容
 * @date 4/21/2014.
 */

var lastRequestLevel = 0 ;
function eContentFun(options,callback){
	var defaults = {etabsContentM:$("#cmfCenter"),url:null,data:{}};
	var opts = $.extend(defaults,options);
	
	WebpageLoader.load(opts.url, $("#cmfCenter"), {isOpenUnload:opts.data.isOpenUnload,reqData:opts.data, backFn:callback});
}
/**
 * @desc 招商财富网上交易平台 （原招钱宝首页js）
 * @author Liudh
 * @点击菜单加载指向的页面内容
 * @date 4/21/2014.
 */
$(function(){
	// 给左侧导航按钮加手势样式
//	$(".mod_sidebar li").css("cursor", "pointer");
    // 点击菜单切换内容及样式
    navLeftArr = $(".mod_sidebar li").click(function(){
		var _this = $(this).find("a");
		window.location.href="/AppService/applicationGroups.shtml?mainCatId="+_this.attr('parentId')+"&thirdCatId="+_this.attr('id');
		return false;
	});
    
    navLeftArr2 = $(".sub_nav li").click(function(){
    	var _this = $(this).find("a");
    	window.location.href="/AppService/applicationGroups.shtml?mainCatId="+_this.attr('id')+"&thirdCatId="+_this.attr('defaultId');
    	return false;
    });
});
 
 
 function changeClass(a_id,url){
		
		$(".mod_sidebar a").removeClass("selected").parent().removeClass("current");
			
		var _this = $('#'+ a_id);
		_this.addClass("selected");
		_this.parent().addClass("current");


		$(".mod_sidebar_item").each(function(){
			$(this).find(".mod_sidebar_item_tit").attr("class","mod_sidebar_item_tit");
		});
		_this.parents(".mod_sidebar_item").find(".mod_sidebar_item_tit").attr("class","mod_sidebar_item_tit mod_sidebar_item_current");

		eContentFun({"url":url},'');
		
 }
