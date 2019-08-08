/**
 * Created by  on 2017/9/29.
 */

var popFn = {
    /*************************************
     *  信息提示弹窗
     *
     *  @param {Object} obj 信息内容
     *  obj = {
     *      msg : "信息提示内容"，
     *      time : 2000   //可选参数，信息停留时间
     *  }
     * **********************************/
    message : function (obj) {
        if($(".pop-msg").length == 0){
            $("body").append('<section class="pop-   op-msg"><div class="msg"></div></section>');
        }
        $(".pop-msg").show().find(".msg").html(obj.msg);
        setTimeout(function () {
            $(".pop-msg").hide();
        }, obj.time ? obj.time : 2000);
    },
    /*************************************
     *  信息提示弹窗(带确认按钮)
     *
     *  @param {Object} obj 信息内容
     *  obj = {
     *      msg : "信息提示内容"，
     *      btnMsg : "按钮上文案",      //可选参数，默认按钮文案为“我知道了”
     *      callBack : function(){}    //回调函数，可选参数
     *  }
     * **********************************/
    alert : function (obj) {
        if($(".pop-alert").length == 0){
            $("body").append('<section class="pop-public pop-alert"><div class="box"><i class="close"></i><p class="message"></p><div class="btn-confirm">我知道了</div></div></section>');
        }
        $(".pop-alert").find(".message").html(obj.msg);
        obj.btnMsg && $(".pop-alert").find(".btn-confirm").html(obj.btnMsg);


        $(".pop-alert,.pop-alert .close").unbind().on("click",function(){
            popFn.close();
        });
        $(".pop-alert .btn-confirm").unbind().on("click",function(e){
            e.preventDefault();
            return false;
        });


        //点击按钮
        $(".btn-confirm").unbind().on("click",function(){
            popFn.close();
            obj.callBack && obj.callBack();
            
        });
        $(".pop-alert").show();
    },

    /********************************************************************************
     * loading提示弹出层（移动端，没有按钮）
     * *****************************************************************************/
    loading : function() {
        if($(".pop-load").length == 0){
            $("body").append("<div class='pop-public pop-load'><div class='box'><p class='loading'></p></div></div>");
        }
        $(".pop-load").show();
    },


    /********************************************************************************
     * 关闭弹窗（通用弹窗）
     * *****************************************************************************/
    close : function(){
        $(".pop-public").hide();
    }
};