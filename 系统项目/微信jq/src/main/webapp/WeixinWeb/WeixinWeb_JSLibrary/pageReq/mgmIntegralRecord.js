$(function(){
    queryIntegralDeatail()
})

//  返回上一级页面
function goback() {
    window.history.go(-1)
}
//  查询积分详情信息
function queryIntegralDeatail(){
    $.ajax({
        url:'/WeixinService/business/integral/getIntegralInfo.xhtml',
        data:{},
        dataType:'json',//服务器返回json格式数据
        type:'get',//HTTP请求类型
        success:function(data){
            $('#total').html(data.integral)
        },
        error:function(xhr,type,errorThrown){

        }
    });

}
 