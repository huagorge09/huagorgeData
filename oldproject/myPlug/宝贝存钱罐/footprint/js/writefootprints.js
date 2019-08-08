$(function() {
	page();
});

function page() {
	//微信端
	if(common.isWeiXin()){
		$("#tapebtn").addClass("disabled");
		$("#tapetips").show().addClass("tapetipsAnimation");
	}else {
		$("#tapebtn").on("click",function () {
			location.href = "tape.html"
		});
	}

	//设置默认日期
	var date = new Date();
	$("#date").text(date.getFullYear()+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日");

	writeFootprints();//写足迹

	previewPicture();//预览图片

    dateSelect();//日期选择

	songSelect();//主角选择

	privacySelect();//隐私选择

	topicSelect();//主题选择

	submit();//发表


	function writeFootprints() {
		//写足迹判断字符长度
		$("#textarea").on("input", function() {
			$("#subtips").hide();
			if($(this).val().length > 2000){
				$(".tips").show();
				$(this).val($(this).val().substring(0, 2000));
			}
			//设置输入框高度
			if($(this).val().length>0){
				$(this).css("height","1.85rem");
			}else {
				$(this).css("height","1.45rem");
			}
			//设置时间位置
			if(isHasLength()){
				$("#editdate").css("margin-top","0.15rem");
			}else {
				$("#editdate").css("margin-top","-0.25rem");
			}
			pass();
		});

		//选择图片
		document.getElementById("fileimg").addEventListener('change', function(e) {
			var length = 9-$("#imgList").find("li.imgWrap").length;
			var file = this.files;
			var len;
			//最多上传9张图片
			if(file.length > length){
				len = length;
				$('#shangChuanImg').hide();
			}else {
				len = file.length;
			}
			for(var i = 0; i < len; i++) {
				var reader = new FileReader();
				reader.readAsDataURL(file[i]);
				reader.onload = function(e) {
					var listhtml = "<li class='imgWrap'><img onload='common.pictureImgLoad(this)' src=" + this.result + "></li> ";
					$("#imgList .last").before(listhtml);
					//设置时间位置
					if(isHasLength()){
						$("#editdate").css("margin-top","0.15rem");
					}else {
						$("#editdate").css("margin-top","-0.25rem");
					}
				}
			}
		}, false);
	}

	function previewPicture() {
		//图片预览
		$("#imgList").on("click","img",function () {
			var currentUrl = $(this).attr("src");//当前图片
			var urlList=[];//预览图片列表
			$(this).parents("#imgList").find("img").each(function () {
				urlList.push({src:$(this).attr("src")});
			});
			common.previewPicture({
				currentUrl:currentUrl,
				urlList:urlList,
				hasRemove:true,
				callback:function(urlList){
					//删除图片处理
					if(urlList.length < 9){
						$('#shangChuanImg').show();
						$("#imgList").find(".imgWrap").remove();
						var listhtml = "";
						for(var i = 0; i < urlList.length; i++){
							listhtml += "<li class='imgWrap'><img onload='common.pictureImgLoad(this)' src=" + urlList[i] + "></li>";
						}
						$("#imgList .last").before(listhtml);
					}

					//设置时间位置
					if(isHasLength()){
						$("#editdate").css("margin-top","0.15rem");
					}else {
						$("#editdate").css("margin-top","-0.25rem");
					}
				}
			});
			return false
		});
	}

	function songSelect() {
		//默认选中上次选择的孩子
		$("#songList span").each(function (index) {
			var list = localStorage.getItem("songSelect") || [];
			if(list.indexOf(index) != -1){
				$(this).addClass("checked");
			}else{
				$(this).removeClass("checked");
			}
		});

		//足迹主角多选
		$(".checkbox span").click(function() {
			if(!$(this).hasClass("checked")) {
				$(this).addClass("checked");
			}else {
				$(this).removeClass("checked");
			}
		});
	}

	function topicSelect() {
		//设置内容展开
		$(".title p").click(function() {
			if(!$(this).find("em").hasClass("rotate")) {
				$(this).find("em").addClass("rotate");
				$(this).parents(".title").siblings(".setupitem").stop().slideDown();
			} else {
				$(this).find("em").removeClass("rotate");
				$(this).parents(".title").siblings(".setupitem").stop().slideUp();
			}
		});

		//主题选择
		$("#zhuTi li").click(function() {
			var text = $(this).text();
			$(this).addClass("cur").siblings().removeClass("cur");
			$(this).parents(".setupitem").siblings(".title").find("p span").text(text);
		});
	}

	function privacySelect() {
		//隐私选择
		$("#yinSi li").click(function() {
			var index = $(this).index();
			var text = $(this).text();
			$(this).addClass("cur").siblings().removeClass("cur");
			$(this).parents(".setupitem").siblings(".title").find("p span").text(text);
			if(index == 0){
				$("#xieYi").show();
			}else {
				$("#gouXuan").removeClass("no");
				$("#xieYi").hide();
			}
			pass();
		});

		//须知同意点击
		$("#gouXuan").click(function() {
			if($(this).hasClass("no")) {
				$(this).removeClass("no");
			} else {
				$(this).addClass("no");
			}
			pass();
		});


		//上传须知点击
		var scrollTop;
		$("#see").click(function() {
			scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
			$("body").append("<div class='mask' id='maskcontentMask'></div>");
			$(".page").css({
				"position" : "relative",
				"top" : -scrollTop + "px"
			});
			$("body,html").addClass("ban");
			$("#maskcontent").show();
		});

		//协议弹窗关闭
		$(".colse").click(function() {
			$("#maskcontentMask").remove();
			$("body,html").removeClass("ban");
			$("#maskcontent").hide();
			$(".page").css({
				"top" : 0
			});
			document.body.scrollTop = document.documentElement.scrollTop = scrollTop;
		});
	}

    //判断是否可以发表
	function pass() {
		if($("#textarea").val().length > 0 && !$("#gouXuan").hasClass("no")){
			$(".submitbox").find("input").removeClass("disabled");
		}else {
			$(".submitbox").find("input").addClass("disabled");
		}
	}

	//判断是否设置时间位置
	function isHasLength() {
		if($("#textarea").val().length > 0 || $("#imgList").find("img").length>4){
			return true;
		}else {
			return false
		}
	}

	function submit() {
		//发表
		$("#submit").on("click",function () {
			if($(this).hasClass("disabled")){
				if($(".textinput").val().length == 0){
					$("#subtips").show();
				}
			}else {
				var per = 0;//进度百分比[0-100]
				var scheduleBox = new ScheduleBox("发表中...");
				setInterval(function () {
					per++;
					if(per<100){
						scheduleBox.setSchedule(per);//设置进度
					}else {
						scheduleBox.finish();//进度100%，关闭进度弹框
						//缓存选择的孩子，下次进入默认选择
						var list = [];
						$("#songList span").each(function (index) {
							if($(this).hasClass("checked")){
								list.push(index);
							}
						});
						localStorage.setItem("songSelect",list);
						location.href = document.referrer;
					}
				},20);
			}
		});
	}

    //日期选择
    function dateSelect() {
        $("#date").mobiscroll().date({
            theme: "default",
            mode: "scroller",
            display: "bottom",
            animate: "slideup",
            defaultValue:new Date(),
            lang: "zh",
            dateFormat: 'yy/mm/dd', //返回结果格式化为年月格式  
            onSelect:function (valueText, inst) {
                var dateText = valueText.substring(0,4) + "年" + (valueText.substring(5,6) == '0' ? valueText.substring(6,7) : valueText.substring(5,7)) + "月" + (valueText.substring(8,9) == 0 ? valueText.substring(9,10) : valueText.substring(8,10)) + "日";
                $("#date").text(dateText);
            }
        });
    }
}


