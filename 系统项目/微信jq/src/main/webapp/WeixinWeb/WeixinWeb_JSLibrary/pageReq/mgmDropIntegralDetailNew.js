$(function () {
    initIntegralList()
})

function initIntegralList() {
    // dropload
    var dropload = $('.recordBox').dropload({

        loadUpFn: function (me) {
            $.ajax({
                type: 'GET',
                url: '/WeixinService/business/integral/listIntegralDetail.xhtml',
                dataType: 'json',
                success: function (data) {
                    if(data.errcode=="0000"){
                        var result = '';
                        var integralChangeO;
                        var successContent; //操作结果
                        
                        for (var i = 0; i < data.data.length; i++) {
                            result += '<li>' +
                                '<span class="operationName">' + data.data[i].content + '</span>';
    
                            integralChangeO = parseInt(data.data[i].integralChange);
                            if (data.data[i].displayState == '1' || data.data[i].displayState == 1) {
                                if (data.data[i].operationid == '100008') {
                                    successContent = '成功购买';
                                    result += '<span class="successBuy">' + successContent + '</span>';
                                }
                            }
                            if (integralChangeO < 0 && integralChangeO != 0) {
                                result += '<span class="amount custRed">' + data.data[i].integralChange + '</span>';
                            }
    
                            if (integralChangeO > 0 && integralChangeO != 0) {
                                result += '<span class="amount ">+' + data.data[i].integralChange + '</span>';
                            }
    
                            result += '<p class="costDate">' + data.data[i].createTime.subString(0, 10) + '</p></li>';
    
                        };
                        $('.lists').html(result);
                    }
                },
                error: function (xhr, type) {
                    console.log('Ajax error!');
                    // 即使加载出错，也得重置
                    dropload.resetload();
                }
            });
        },
        loadDownFn: function (me) {
            $.ajax({
                type: 'GET',
                url: '/WeixinService/business/integral/listIntegralDetail.xhtml',
                dataType: 'json',
                success: function (data) {
                    if(data.errcode=="0000"){
                        var result = '';
                        var integralChangeO;
                        var successContent; //操作结果
                        if(data.data.length>0){
                            for (var i = 0; i < data.data.length; i++) {
                                result += '<li>' +
                                    '<span class="operationName">' + data.data[i].content + '</span>';
        
                                integralChangeO = parseInt(data.data[i].integralChange);
                                if (data.data[i].displayState == '1' || data.data[i].displayState == 1) {
                                    if (data.data[i].operationid == '100008') {
                                        successContent = '成功购买';
                                        result += '<span class="successBuy">' + successContent + '</span>';
                                    }
                                }
                                if (integralChangeO < 0 && integralChangeO != 0) {
                                    result += '<span class="amount custRed">' + data.data[i].integralChange + '</span>';
                                }
        
                                if (integralChangeO > 0 && integralChangeO != 0) {
                                    result += '<span class="amount">+' + data.data[i].integralChange + '</span>';
                                }
        
                                result += '<p class="costDate">' + data.data[i].createTime.subString(0, 10) + '</p></li>';
        
                            };
                        }else{
                           $(".noRecord").show();
                        }
                        $('#lists').html(result);
                    }
                },
                error: function (xhr, type) {
                    console.log('Ajax error!');
                    // 即使加载出错，也得重置
                    dropload.resetload();
                }
            });
        }
    });
}