$(document).ready(function(e) {
    getUserRequest("pc_index");
    $('.section').css({
        'height' : $(window).height()
    });
    $(".wrap .top-weixin").hover(function() {
        $(".weixin_img").show();
    }, function() {
        $(".weixin_img").hide();
    });
    $(".nav.fr ul li a").removeClass("current").eq(0).addClass("current");




    var app = {
        init: function(data){
            this.$scrollBox = $('#scrollBox');
            this.$visibleArea = $('#amBg0');
            this.defWid = $(window).width();
            this.$leftBtn = $('#leftBtn');
            this.$rightBtn = $('#rightBtn');    
            this.rollFlag = 1;                  /*判断鼠标是否悬停在大图上，1是，0否*/
            this.needScroll = 1;                /*如果数据有多条则为1，开启自滚动以及按钮事件绑定，否则为0，不做任何操作*/

            this.prepare(data);
            this.bind();
        },
        bind: function(){
            if (this.needScroll == 1) {
                this.$visibleArea.on('mouseover', $.proxy(this.notRoll, this));
                this.$visibleArea.on('mouseleave', $.proxy(this.canRoll, this));
                this.$leftBtn.on('click', $.proxy(this.move, this));
                this.$rightBtn.on('click', $.proxy(this.move, this));
            };
            $(window).on('resize', $.proxy(this.onResize, this));
        },
        /*页面尺寸变化时事件*/
        onResize: function(){
            this.$scrollBox.finish();
            this.defWid = $(window).width();
            var index = $('.scroll-unit.active').index();
            if(index > -1){
            	var left = - index * this.defWid;
                this.$scrollBox.css('left',left);	
            }
            $('.scroll-unit').width(this.defWid);
        },
        /*数据读取*/
        dataLoad: function(){
            // $.ajax({
            //     url: '/path/to/file',
            //     type: 'default GET (Other values: POST)',
            //     dataType: 'default: Intelligent Guess (Other values: xml, json, script, or html)',
            //     data: {param1: 'value1'},
            //     success: function(data){
                     
            //     }
            // });
            var data = queryBanner();
            this.init(data);
        },
        /*首次加载预处理*/
        prepare: function(data){
            this.$scrollBox.html(data);
            var boxHtml = '<div class="main-content">'
                        + '<div class="layMid">'
                        + '<div class="main-slogan"></div>'
                        + '<div class="user-bt" id="loginNoBt">'
                        + '<a href="/login/login.shtml" class="bt-login" javascript:"void(0)"">登录</a>'
                        + '<a href="/login/register.shtml" class="bt-signin">注册</a>'
                        + '</div>'
                        + '<div class="user-bt none" id="loginYesBt">'
                        + '<a href="/AppService/applicationGroups.shtml" class="bt-login">我的财富</a>'
                        + '<a href="javascript:checkOutLogin();" class="bt-signin">退出</a>'
                        + '</div>'
                        + '</div>';
            this.$scrollBox.find('li').eq(0).append("");            
            var html = this.$scrollBox.html();
            this.$scrollBox.append(html);
            var len = this.$scrollBox.children('li').length;
            if (len == 2) {
                this.needScroll = 0;
                this.$leftBtn.remove();
                this.$rightBtn.remove();
                return;
            };
            var width = len * this.defWid;
            this.$scrollBox.width(width);
            this.$scrollBox.children('li:first').addClass('active');
            $('.scroll-unit').width(this.defWid);
            setTimeout(this.autoRoll, 5000);
        },
        /*滚动逻辑*/
        roll: function(flag){
            if (app.rollFlag == 0 && flag == undefined) {
                return;
            };
            if (flag == undefined) {
                flag = 'right';
            };
            var len = app.$scrollBox.children('li').length;
            var first = len / 2;
            var now = app.$scrollBox.children('li.active').index();
            app.$scrollBox.children('li').removeClass('active');
            if (flag == 'right') {
                if (now == first) {
                    app.$scrollBox.css('left', '0');
                    app.$scrollBox.children('li').eq(1).addClass('active');
                }else{
                    app.$scrollBox.children('li').eq(now + 1).addClass('active');
                };
            }else {
                if (now == 0) {
                    var left = - first * app.defWid; 
                    app.$scrollBox.css('left', left);
                    app.$scrollBox.children('li').eq(first -1).addClass('active');
                }else{
                    app.$scrollBox.children('li').eq(now - 1).addClass('active');
                };
            };
            var boxLeft = parseInt(app.$scrollBox.css('left'));
            if (flag == 'right') {
                var leftValue = boxLeft - app.defWid;
            }else {
                var leftValue = boxLeft + app.defWid;
            };
            app.$scrollBox.animate({
                left: leftValue + 'px'},
                1000
            );
        },
        /*鼠标悬停时禁止滚动*/
        notRoll: function(){
            this.rollFlag = 0;
        },
        /*鼠标离开后恢复滚动*/
        canRoll: function(){
            this.rollFlag = 1;
        },
        /*左右滚动键点击处理*/
        move: function(e){
            this.$scrollBox.finish();
            var $this = $(e.currentTarget);
            var flag = $this.data('move');
            this.roll(flag);
        },
        /*自动滚动设置*/
        autoRoll: function(){
            app.roll();
            setTimeout(app.autoRoll, 5000);
        }
    };

    app.dataLoad();


    headerInfo();

});
function headerInfo() {
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
            if(data.returnCode=='0000'){
                $("#loginYes").show();
                $("#loginNo").hide();
                $("#userId").val(data.cmfUserId);
                $("#login_userinfo").html(data.name);
                $("#loginYesBt").show();
                $("#loginNoBt").hide();
            }else{
                $("#loginNo").show();
                $("#loginYes").hide();
                $("#loginNoBt").show();
                $("#loginYesBt").hide();
            }
        }
    });
}
function checkOutLogin() {
    $.ajax({
        async : false,
        url : "/AppService/setUp/checkOutLogin.xhtml",
        dataType : "json",
        type : "POST",
        data : {},
        cache : false,
        error : function(textStatus, errorThrown) {
            show_tips("网络繁忙，请稍后再试。")
        },
        success : function(data) {
            window.location.href = "/login/login.shtml";
        }
    });
}
function showVideo(){
    var browser = navigator.appName;
    var b_version = navigator.appVersion;
    var version = b_version.split(";");
    var trim_Version = "";
    if (browser == "Microsoft Internet Explorer") {
        if (trim_Version != null && version.length >= 2) {
            trim_Version = version[1].replace(/[ ]/g, "");
        }
        if (trim_Version == "MSIE6.0" || trim_Version == "MSIE7.0" || trim_Version == "MSIE8.0") {
            $(".logo_con .point").show();
            $(".video_bg video").hide();
            $(".video_bg").removeClass("none");
            $(".videoClose").click(function() {
                $(".video_bg").addClass("none");
                $(".logo_con .point").hide();
            });
        } else {
            $(".video_bg").show();
            document.getElementById("video").play();
            $(".video_bg .logo .logo_con span.point").hide();

            $(".videoClose").click(function() {
                document.getElementById("video").pause();
                $(".video_bg").hide();
            });

            $(".logo_close").click(function() {
                close_bg();
            });
        }

    } else {
        $(".video_bg").show();
        document.getElementById("video").play();
        $(".video_bg .logo .logo_con span.point").hide();

        $(".videoClose").click(function() {
            document.getElementById("video").pause();
            $(".video_bg").hide();
        });

        $(".logo_close").click(function() {
            close_bg();
        });
    }
}
//未知调用处
function close_bg(){
    $(".logo_bg").hide();
    $(".other_bg").hide();
}

//未知调用处
function Jqqdshow() {
    if (index == 1) {
        $("#jqqd").show();
        index = 2;
    } else if (index == 2) {
        $("#jqqd").hide();
        index = 1;
    }
}
//未知调用处
function Jqqdhide() {
    if (index == 1) {
        $("#jqqd").show();
        index = 2;
    } else if (index == 2) {
        $("#jqqd").hide();
        index = 1;
    }
}
//未知调用处
function getNowFmtDate() {
    var d = new Date();
    var month = d.getMonth() + 1;
    var date = d.getDate();
    var hours = d.getHours();
    var minutes = d.getMinutes();
    var second = d.getSeconds();

    if (parseInt(month) >= 0 && parseInt(month) <= 9) {
        month = "0" + month;
    }
    if (parseInt(date) >= 0 && parseInt(date) <= 9) {
        date = "0" + date;
    }

    if (parseInt(hours) >= 1 && parseInt(hours) <= 9) {
        hours = "0" + hours;
    } else if (parseInt(hours) == 0) {
        hours = "00";
    }
    if (parseInt(minutes) >= 1 && parseInt(minutes) <= 9) {
        minutes = "0" + minutes;
    } else if (parseInt(minutes) == 0) {
        minutes = "00";
    }
    if (parseInt(second) >= 1 && parseInt(second) <= 9) {
        second = "0" + second;
    } else if (parseInt(second) == 0) {
        second = "00";
    }

    var currDateTime = "" + d.getFullYear() + month + date + "-" + hours + minutes + second;

    return currDateTime;
}
/*查询首页banner*/
function queryBanner(){
	var returnHtml = "";
	$.ajax({
    	async:false,
		url:"/AppService/setUp/queryIndexBanner.xhtml",
		type:"post",
		dataType:'json',
		data:{
			type:"33"
		},
		error:function(){
//			show_tips("网络繁忙，请稍后再试。"); 
		},
		success:function(data, textStatus){
			if(data.advertDtoList == null || data.advertDtoList.length == 0){
				return returnHtml;
			}
			$.each(data.advertDtoList,function(i, item){
				if(item.state != null && item.state == '1'){
					returnHtml += '<li class="scroll-unit cursor"><img onclick="openDialog(\''+item.url+'\')" src="'+item.picture+'"></li>';
				}else{
					returnHtml += '<li class="scroll-unit"><img src="'+item.picture+'"></li>';
				}
				
			});
		}
	});
	return returnHtml;
}

function openDialog(url){
	if(url == null || url == ""){
		return;
	}
	window.open(url);
}