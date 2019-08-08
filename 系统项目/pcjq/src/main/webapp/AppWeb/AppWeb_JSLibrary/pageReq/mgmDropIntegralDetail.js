
$(function(){
	queryIntegralList()
})
	
//  我的积分使用记录查询

function queryIntegralList(){
	// dropload
	var dropload = $('.inner').dropload({

		loadUpFn: function(me) {
			$.ajax({
				type: 'GET',
				url: '/AppService/business/integral/listIntegralDetail.xhtml',
				dataType: 'json',
				success: function(data) {
                    var result = '';
                    var integralChangeO;
                    var successContent; //操作结果
					if (data){
                        for (var i = 0; i < data.data.length; i++) {
                            result += '<li class="item opacity" >' +
                                '<h3>' + data.data[i].content + '</h3>' +
                                '<span class="date">' + data.data[i].createTime.subString(0,10) + '</span>';

                            integralChangeO = parseInt(data.data[i].integralChange);
                            if(data.data[i].displayState=='1'||data.data[i].displayState==1){
                                if(data.data[i].operationid=='100008'){
                                    successContent='成功购买';
                                    result+='<span class="successBuy">' +successContent +'</span>';
                                }
                            }
                            if (integralChangeO <0 && integralChangeO!=0) {
                                result += '<span class="costIntegral costRed">' + data.data[i].integralChange + '</span></li>'
                            }

                            if(integralChangeO>0 && integralChangeO!=0) {
                                result += '<span class="costIntegral">' +'+'+ data.data[i].integralChange + '</span></li>'
                            }
                        }
                        $('.lists').html(result);
					}else {
						$('#noRecord').show()
					}
				},
				error: function(xhr, type) {
					console.log('Ajax error!');
				}
			});
		},
		loadDownFn: function(me) {
            $.ajax({
                type: 'GET',
                url: '/AppService/business/integral/listIntegralDetail.xhtml',
                dataType: 'json',
                success: function(data) {
                    var result = '';
                    var integralChangeO;
                    var successContent; //操作结果
                    if (data){
                        for (var i = 0; i < data.data.length; i++) {
                            result += '<li class="item opacity" >' +
                                '<h3>' + data.data[i].content + '</h3>' +
                                '<span class="date">' + data.data[i].createTime.subString(0,10) + '</span>';

                            integralChangeO = parseInt(data.data[i].integralChange);
                            if(data.data[i].displayState=='1'||data.data[i].displayState==1){
                                if(data.data[i].operationid=='100008'){
                                    successContent='成功购买';
                                    result+='<span class="successBuy">' +successContent +'</span>';
                                }
                            }
                            if (integralChangeO <0 && integralChangeO!=0) {
                                result += '<span class="costIntegral costRed">' + data.data[i].integralChange + '</span></li>'
                            }

                            if(integralChangeO>0 && integralChangeO!=0) {
                                result += '<span class="costIntegral">' +'+'+ data.data[i].integralChange + '</span></li>'
                            }
                        }
                        $('.lists').html(result);
                    }else {
                        $('#noRecord').show()
                    }
                },
                error: function(xhr, type) {
                    console.log('Ajax error!');
                }
            });
		}
	});
}
