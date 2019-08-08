var $sideMenu = $('#side-menu'),
$pageTabs = $('.page-tabs'),
$navTabs = $('.nav-page-tabs'),
$navContainer = $('.nav-page-container');

(function(window, $, undefined) {
    $(function() {
            //默认页
            /*
             * 流程指引暂时隐藏起来
             * defaultPageInfo = {
                url: '#!/cbp/analysis/flow/index.do',
                title: '流程指引'
            };*/
            defaultPageInfo = {
                /*url: '#!/otcWebapp/resources/modules/wasp/defaultPage.jsp',
                title: 'defaultPage'*/
            };

        
        // 菜单搜索
        $('#top-search').keypress(function(e){
        	if(e.keyCode!=13) return;
        	
        	$title = $(this).val();
        	$ele = $sideMenu.find('[data-title*="' + $title + '"]');
        	if($ele.length!=0) {
        		setTimeout(function() {
                    $sideMenu
                        .find('.in')
                        .removeClass('in')
                        .end()
                        .find('.active')
                        .removeClass('active');

                    $ele.parents('li')
                        .addClass('active')
                        .end()
                        .parents('ul')
                        .removeAttr('style')
                        .addClass('in');
                }, $ele.parents('.nav > li.active').length ? 0 : 10);
        		
        		 /* 通过搜索自动开启菜单注释掉
        		 var $activeTab = $pageTabs.find('[data-title="' + $title + '"]');
                 if ($activeTab.length) {
                     $activeTab
                         .addClass('active')
                         .siblings()
                         .removeClass('active');
                 } else {
                     //新增nav-page-tabs
                     createPageTabs($ele.attr('data-url'), $title);
                 }*/
        	}
        	$(this).val('');
        });
        
        //绑定侧边栏的事件，点击后生成右边的iframe
        //一般是二级菜单
        $sideMenu.on('click', '.final-menu a', function(e) {
            e.preventDefault();
            //记录菜单点击
            //切换页签
            changePageTabs(getDomInfo($(this)));
        });

        //删除页签
        $pageTabs.on('click', '.nav-page-close', function(e) {
            var $header = $(this).parents('.nav-page-item-header');
            deletePageTabs(getDomInfo($(this)).url, $header.hasClass('active'), $header.siblings().length);
            return false;
        })

        //非活动页签被点击
        .on('click', '.nav-page-item-header:not(.active)', function(e) {
            e.preventDefault();
            //切换页签
            changePageTabs(getDomInfo($(this)));
        });
        
      //点击进入我的消息页面
        $("#btn-my-msg").click(function(){
        	var msgInfo = {
        			url: '#!'+msgPath+'list.do',
                    title: '我的通知'
        	};
        	changePageTabs(msgInfo);
	    });
        
        window.openTab = function(url){
        	url = "#!" + url;
        	$ele = $sideMenu.find('[data-url="' + url + '"]');
	    	if($ele.length!=0) {
	    		setTimeout(function() {
	                $sideMenu
	                    .find('.in')
	                    .removeClass('in')
	                    .end()
	                    .find('.active')
	                    .removeClass('active');
	
	                $ele.parents('li')
	                    .addClass('active')
	                    .end()
	                    .parents('ul')
	                    .removeAttr('style')
	                    .addClass('in');
	            }, $ele.parents('.nav > li.active').length ? 0 : 10);
	    	}
		 	var $activeTab = $pageTabs.find('[data-url="' + url + '"]');
	        if ($activeTab.length) {
	            $activeTab
	                .addClass('active')
	                .siblings()
	                .removeClass('active');
	        } else {
	            //新增nav-page-tabs
	            createPageTabs($ele.attr('data-url'), $ele.attr('data-title'));
	        }
        };

        //跳转到默认页
        changePageTabs(defaultPageInfo);
    });
})(window, $, undefined);

//获取dom上保存的信息
function getDomInfo($dom) {
    return {
        url: $dom.attr('data-url') || false,
        title: $dom.attr('data-title') || false
    }
}

//切换页签
function changePageTabs(info) {
    var url, title, $ele;
    if (!info.url) {
        return false;
    }

    url = info.url;
    title = info.title;
    $ele = $sideMenu.find('[data-url="' + url + '"]');
    //更新左面的nav-tabs
    setTimeout(function() {
        $sideMenu
            .find('.in')
            .removeClass('in')
            .end()
            .find('.active')
            .removeClass('active');

        $ele.parents('li')
            .addClass('active')
            .end()
            .parents('ul')
            .removeAttr('style')
            .addClass('in');
    }, $ele.parents('.nav > li.active').length ? 0 : 10);

    //判断是否已经有了这个tab
    var $activeTab = $pageTabs.find('[data-url="' + url + '"]');

    //看看有没有，没有的话只能跳转了
    if ($activeTab.length) {
        $activeTab
            .addClass('active')
            .siblings()
            .removeClass('active');
    } else {
        //新增nav-page-tabs
        createPageTabs(url, title);
    }
}

//删除页签，并将最后一个置为活动状态
//如果是活动页签，就要让最后一个置为活动状态
//如果这是最后一个页签，那么就要显示默认的页签
function deletePageTabs(url, isActive, isNotLast) {
    $pageTabs.find('[data-url="' + url + '"]').remove();

    if (isActive) {
        $pageTabs.find('.nav-page-tabs')
            .find('.nav-page-item-header')
            .last()
            .trigger('click');
    }

    if (!isNotLast) {
        changePageTabs(defaultPageInfo);
    }
}

//新增页签
function createPageTabs(url, title) {
    //realUrl是去掉#!之后的url
    var realUrl = url.slice(2);
    if (vaildatePageTabs(realUrl)) {
       var headerNum =$navTabs.find('.nav-page-item-header').length + 1;
       if(title != 'defaultPage') {
    	   if (title == '流程指引') {
    	  	 $('<li class="active nav-page-item-header nav-page-flow-guide" data-url="' + url + '" data-title="' + title + '"><i class="fa fa-cogs"></i>' + title + '<i  data-url="' + url + '" class="fa fa-times-circle nav-page-close"></i></li>')
           .appendTo($navTabs)
           .siblings()
           .removeClass('active'); 
    	   }else {
    	  	 $('<li class="active nav-page-item-header" data-url="' + url + '" data-title="' + title + '">' + title + '<i  data-url="' + url + '" class="fa fa-times-circle nav-page-close"></i></li>')
           .appendTo($navTabs)
           .siblings()
           .removeClass('active');
    	   }
       }
        //新增page-container            
        $('<div class="nav-page-item-body active" data-url="' + url + '" data-title="' + title + '">\
            <iframe src="' + realUrl + '" id="index-frame'+headerNum+'" frameborder="0" allowfullscreen mozallowfullscreen webkitallowfullscreen>\
            </iframe>\
        </div>')
            .appendTo($navContainer)
            .siblings()
            .removeClass('active');
    }
    //监听iframe中的点击事件,用来关闭首页的消息列表
    $($("iframe[src='"+realUrl+"']")[0].contentWindow).click(function(){
    	$("body").trigger("click");
    });
}

function vaildatePageTabs(url) {
    if ($navTabs.find('.nav-page-item-header').length < 10) {
        return true;
    } else {
        toastr.error('打开页面超过10个，请关闭后再打开。');
        return false;
    }
}

$(function(){
	pubsub.subscribe("goPageTab",function(id,param){
		debugger;
//		var paramObj = JSON.parse(param);
		/*var info = {
			url: '#!/cmwa-ecc-business/service/rateDiscount/rateDiscountMgrView.do',
            title: '费率折扣管理'
		};*/
		changePageTabs(param);
	});
	queryEccSystemInfo();
});

function showInvestManager(ele){
	$(ele).siblings().show();
}

function setManagerInfo(ele){
	var transActorId = $('#transActorInfoId').val(),
	investManagerId = $('#investManagerId').val();
	if (transActorId && investManagerId) {
			$.ajax({
				url:'/otcWebapp/investManager/setSysManagerInfo.xhtml',
				dataType:'text',
				type:'post',
				data:{
					id:investManagerId,
					transActorInfoId:transActorId
				},
				success:function(data){
					if (data && data == 'success') {
						$(ele).closest('ul').find('li:gt(0)').hide();
					}
				}
			});
	}
}

function queryEccSystemInfo (){
	$.ajax({
		async:true,
		url:BASE_PATH+'service/system/dsquery/queryEccSystemInfo.xhtml',
		type:"post",
		dataType:'json',
		success:function(data, textStatus){
			var eccSysStatusName = $("#eccSysStatusName");
			var eccSysWorkDate = $("#eccSysWorkDate");
			if(!!data){
				var sysstatus = data.SYSSTATUS;
				var sysStatusName = data.SYSSTATUSNAME;
				var workDate = data.WORKDATE;
				eccSysStatusName.html(sysStatusName);
				eccSysWorkDate.html(workDate);
				if("1" == sysstatus || "2" == sysstatus){//1 收市
					$("#eccSystemInfo").addClass("hughCity");
				}
			}
		}
	});
};