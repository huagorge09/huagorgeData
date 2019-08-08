var alreadyPurchase; //购买及未购买人数
var noPurchase;
var integral ;
$(function(){
	queryUserIntegral()
})

//  查询用户当前积分
function queryUserIntegral() {
	$.ajax({
		url: '/AppService/business/integral/getIntegralInfo.xhtml',
		data: {},
		dataType: 'json', //服务器返回json格式数据
		type: 'get', //HTTP请求类型
		success: function(data) {
			if (data) {
				 integral = data.integral;
				 alreadyPurchase=data.alreadyPurchase;
				 noPurchase=data.noPurchase;
				 initEchart()
			}
		},
		error: function(xhr, type, errorThrown) {
				console.log(type,errorThrown)
		}
	});
}

function initEchart(){
	var dom = document.getElementById("echartsBox");
	var myChart = echarts.init(dom);
	var app = {};
	 var option = null;
	app.title = '积分';

	option = {
	    tooltip: {
			show:true,
	        trigger: 'item',
	          formatter: "{a} <br/>{b}: {c} ({d}%)",
			 axisPointer:{
				type:'none'
			}
	    },
	    legend: {
	        orient:'orient',
	        x: 'right',
			y:'center',
			align:'left',
	        data:[
				{name:'推荐未购买',textStyle:{ color:'#99a6b9'}},
				{name:'推荐并购买',textStyle:{color:'#ff8448'}}
				],
				data:[
				{name:'推荐未购买',textStyle:{ color:'#99a6b9'}},
				{name:'推荐并购买',textStyle:{color:'#ff8448'}},
				],
	
			icon:'circle',
			itemWidth:10,
			itemHeight:10,
			itemGap:47,
			selectedMode:false,
			fontSize:16,
			
	    },
	    series: [
	        {
	            name:'',
	            type:'pie',
				clickable:false,
	            radius: ['50%', '70%'],
				hoverAnimation:false, //鼠标悬浮是否有区域弹出动画
	            avoidLabelOverlap: false,
				silent:true,
	            label: {
	                normal: {
	                    show: true,
	                    position: 'center',
						color:'#8493a8',
						fontSize:14.8,
						formatter:function(argument){
							var html;
							html='总计\n\n';
						return html;
						},
	                },
	                emphasis: {
	                    show: false,
	                    textStyle: {
							fontSize:'28.6px',
							color:'#8493a8'
	                    }
	                }
	            },
	            labelLine: {
	                normal: {
	                    show: false
	                }
	            },
	            data:[
	                {value:noPurchase, name:'推荐未购买'},
					{value:alreadyPurchase, name:'推荐并购买'}
	            ],
				color:['#fbc450','#ff7c49']
	        }
	    ]
	};
	
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
		
		
	}
}