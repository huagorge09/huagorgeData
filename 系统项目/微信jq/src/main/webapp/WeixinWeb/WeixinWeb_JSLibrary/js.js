$(document).ready(function(){
    $(".highchart_nav ul li").click(function(){
        $(this).addClass("act").siblings().removeClass("act");
        var nav_index=$(this).index();
        var nhighchart=nav_index+1;
        $(".highchart").attr("id","highchart"+nhighchart);
        if(nhighchart==1){
            showHighchart1();
        }else if(nhighchart==2){
            showHighchart2();
        }else if(nhighchart==3){
            showHighchart3();
        }else if(nhighchart==4){
            showHighchart4();
        }else{

        }
        
   })
})