var investAdviceByLevel = new Map();
investAdviceByLevel.set("1","低风险");
investAdviceByLevel.set("2","中低风险");
investAdviceByLevel.set("3","中风险");
investAdviceByLevel.set("4","中高风险");
investAdviceByLevel.set("5","高风险");

var SECTION_FIRST = "section_01";
var SECTION_LAST_TWO = "section_17";
var SECTION_LAST = "section_18";
var SECTION_RESULT ="section_result";

$(document).ready(function(e) {
	$(".header .top-a h2").html("风险评测");
	document.title="风险评测";
	getUserRequest("risk-level");/* 此处subPath为页面内行为 */


	$('.boxs-btn .pre_question').bind('click',function(){
		$('.title:visible').hide().prev().show();
		if($('.title:visible').attr('id')== SECTION_LAST_TWO){
				$('#question_tip').children().children().eq(2).css('margin-top',0.06*window.innerHeight)
				$('.boxs-btn span:eq(0)').hide();
			    $('.boxs-btn a:eq(3)').hide();
			    $('.boxs-btn a:eq(2)').hide();
				$('.boxs-btn a:eq(1)').show();
		}
		if($('.title:visible').attr('id')== SECTION_FIRST){
				$('.boxs-btn span:eq(0)').hide();
			    $('.boxs-btn a:eq(3)').hide();
			    $('.boxs-btn a:eq(2)').show();
				$('.boxs-btn a:eq(1)').hide();
		}
	});
	
	$('section ul li input').bind('click',function(event){
		event.stopPropagation();
	});
	
	$('section ul li input').bind('keypress',function(event){
		event = event || window.event;
		var target = $(event.target).parent();
		if(event.keyCode==13){
			target.click();
		}
	});
});
function toRisk() {
	$("#section_0").hide();
	$("#"+SECTION_RESULT).hide();
	$("#"+ SECTION_FIRST).show();
	$(".fund_cover-con .span").html("风险评测");
	document.title="风险评测";
	$('.boxs-btn a:eq(4)').hide();
	$('.boxs-btn span:eq(1)').hide();
	$(".boxs-btn .check-order").hide(); 
	$(".boxs-btn a:eq(2)").show();
	$('li.act').removeClass('act')
}
/* 第一题 返回到开始页面 */
function toIndex() {
	$("section section").hide();
	$("#section_0").show();
	$(".header .top-a h2").html("开始评测");
	document.title="开始评测";
	$("section ul li").removeClass("act");
	$(".boxs-btn .check-order").show();
	$(".boxs-btn a:eq(2)").hide();
	$('.boxs-btn a:eq(4)').hide();
	$('.boxs-btn span:eq(1)').hide();
	$(".boxs-btn .check-order").hide();
	$(".boxs-btn a:eq(0)").show();
}
/* 做题 选择答案自动下一题 */
$("section ul li").click(
		function() {
			$(this).siblings().removeClass("act");
			$(this).addClass("act");
			/* 最后一题 不需要跳转 */

			if (!($(this).parents(".title").attr("id") == SECTION_LAST)) {
				$('#question_tip').children().children().eq(2).css('margin-top',0.06*window.innerHeight)
				$('.boxs-btn span:eq(0)').hide();
			    $('.boxs-btn a:eq(3)').hide();
			    $('.boxs-btn a:eq(2)').hide();
				$('.boxs-btn a:eq(1)').show();
				window.setTimeout('next(\"'
						+ $(this).parents(".title").attr("id") + '\")', 200);
			}
			if($(this).parents(".title").attr("id") == SECTION_LAST_TWO){
				$('#question_tip').children().children().eq(2).css('margin-top',0.035*window.innerHeight)
				$('.boxs-btn a:eq(1)').hide();
			    $('.boxs- btna:eq(2)').hide();
			    $('.boxs-btn a:eq(3)').show();
			    $('.boxs-btn span:eq(0)').show();
			}
			
			if($(this).parents(".title").attr("id") == SECTION_RESULT){
				  $('.boxs-btn a:eq(3)').hide();
				  $('.boxs-btn span:eq(0)').hide();
				  
			}
			var section = $(this).parents(".title").attr("id");
			// 500 
			var windowHeight = window.innerHeight;
			if((section == "section_02" || section == "section_06" || section == "section_08")){
				$(".boxs-btn").css("marginTop","10px");
			}
			
			if(windowHeight <= 500 ){
				$(".boxs-btn").css("marginTop","10px");
			}
			
		});
function next(_id) {
	$("#" + _id).hide().next().show();
}
/* 重做上一题 */
$("a.pre_question").click(function() {
	$(this).parents(".title").hide().prev().show();
});


function restartRiskLevel(){
	$("#section_1").hide();
	$("#"+SECTION_RESULT).hide();
	$('.boxs-btn span:eq(0)').hide();
    $('.boxs-btn a:eq(3)').hide();
    $('.boxs-btn a:eq(2)').hide();
	$('.boxs-btn a:eq(1)').show();
	
	$('.boxs-btn span:eq(1)').hide();
	$('.boxs-btn a:eq(4)').hide();
}


/*传索引值查询对应选中的值--data-value*/
function getThisValDesc(index){
	var value= $("ul li.act").eq(index).attr("data-value");
	if(!!value){
		return  ":"+value;
	}
	return "";
}

/*传索引值查询对应输入的值-*/
function getThisInputVal(index){
	var value = $("ul li.act").eq(index).find("input").val();
	if(!!value){
		return  ":"+value;
	}
	return "";
}

/* 提交 */
function comit() {
	var len = $("ul li.act").length;
	if (len == null || len != 18) {
		errorRemark("还有未做题目");
		return;
	}
	var score = 0;
	var num = "";
	var temp = 0;
	for (var i = 0; i < len; i++) {
		var res = $("ul li.act").eq(i).attr("data-score");
		score = score + parseFloat($("ul li.act").eq(i).attr("data-score"));
		num = num != "" ? num + ',' + res + getThisValDesc(i) + getThisInputVal(i) : num + res + getThisValDesc(i) + getThisInputVal(i) ;
	}
	 //请问您是否具有完全民事行为能力
    var isCompAbility = $("ul li.act").eq(16).attr("data-value");
    //请问您是否没有风险容忍度或者不愿承受任何投资损失
    var isEndureLoss = $("ul li.act").eq(17).attr("data-value");
    
    if("N" == isCompAbility || "Y" == isEndureLoss){
    	score = 0;
    }
	console.log(score);
	console.log(num);
	$.ajax({
		async : true,
		url : "/WeixinService/business/riskRating.xhtml",
		data : {
			"score" : score,
			"num" : num
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			var returnCode = data.returnCode;
			
			if (returnCode != null && returnCode == "9001") {
				goToLogin();
			} else if (returnCode != null && returnCode == "9001") {
				goToLogin();
			} else if (returnCode != null && returnCode == "0000") {
				//获取评级数据需要 类型 承受能力 时间 建议等
				var riskLevel = data.riskLevel;
				var riskEvalDate = data.riskEvalDate;
				var riskAbility = "(" + investAdviceByLevel.get(riskLevel)+ "承受能力)";
				var riskLevelName = data.scoreName;
				var riskInvestAdvice = new Array();
				var riskLevelInt = parseInt(riskLevel);
				for(var i=1;i <= riskLevelInt; i++){
					riskInvestAdvice.push(investAdviceByLevel.get(i+"")+"等级");
				}
				
				$("#riskLevelName").html(riskLevelName);
				$("#riskAbility").html(riskAbility);
				$("#riskEvalDate").html(riskEvalDate);
				$("#riskInvestAdvice").html(riskInvestAdvice.join("、"));
				
				$('.boxs-btn a:eq(3)').hide();
				$('.boxs-btn span:eq(0)').hide();
				$('.boxs-btn a:eq(4)').show();
				$('.boxs-btn span:eq(1)').show();
				$("#"+SECTION_LAST).hide();
				$("#"+SECTION_RESULT).show();
				$(".header .top-a h2").html("风险评测");
				document.title="风险评测";
//				$("#scoreCount").html(data.scoreCount);
//				$("#scoreName").html("为" + data.scoreName + "客户");
			} else {
				errorRemark("风险评测失败<br>请稍后再试");
			}
		}
	});
}

function closeTip(){
	$('#question_tip').hide();
}
