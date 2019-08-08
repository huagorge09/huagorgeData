var _data = {
    codeSrc:"http://www.baidu.com",//二维码链接
    pictureList:[
        "image/picture/img4.png",
        "image/picture/img4.png",
        "image/picture/img4.png",
        "image/picture/img4.png",
        "image/picture/img3.png",
        "image/picture/img3.png",
        "image/picture/img3.png"
    ],//图片列表
    jarList:[
        {
            name:"大宝宝",
            date:"4岁2个月20天"
        },
        {
            name:"大宝宝",
            date:"4岁2个月20天"
        },
        {
            name:"大宝宝",
            date:"4岁2个月20天"
        }
    ],//罐子列表
    //内容
    content:"足迹正文最多只显示3行字，剩余未显示的的用省略号足迹正文最多只显示3行字，剩余未显示的的用省略号足足迹正文最多只显示3行字，剩余未显示的的用省略号足迹正文最多只显示3行字，剩余未显示的的用省略号足足迹正文最多只显示3行字，剩余未显示的的用省略号足迹正文最多只显示3行字，剩余未显示的的用省略号足足迹正文最多只显示3行字，剩余未显示的的用省略号足迹正文最多只显示3行字，剩余未显示的的用省略号足足迹正文最多只显示3行字，剩余未显示的的用省略号足迹正文最多只显示3行字，剩余未显示的的用省略号足足迹正文最多只显示3行字，剩余未显示的的用省略号足迹正文最多只显示3行字，剩余未显示的的用省略号足"
};
$(function () {
    page();
});
var page = function () {
    setDate();
    //设置元素数据
    function setDate() {
        var codeImage = common.getCodeImageBase64(_data.codeSrc, 136);//获取二维码base64数据
        $(".codeImage").attr("src", codeImage);//设置二维码图片
        $(".contain .text").text(_data.content);//设置内容
        $(".contain .jar").html(getJarHtml(_data.jarList));
        //是否有图片
        if (_data.pictureList && _data.pictureList.length > 0) {
            $("#picture").html(getPictureHtml(_data.pictureList));//设置图片html
            $("#hasPicture").show();
            $("#notPicture").remove();
            $("#buttonWrap").show().addClass("hasPicture-buttonWrap");
        } else {
            $("#hasPicture").remove();
            $("#notPicture").show();
            $("#buttonWrap").show().addClass("notPicture-buttonWrap");
        }
        new PictureMove($("#picture img"));
        compositePicture();
    }

    //获取图片列表html
    function getPictureHtml(pictureList) {
        var len = pictureList.length;
        var html = '<div class="pictureList pictureList' + len + '">';
        for (var i = 0; i < len; i++) {
            html += '<div class="imgWrap"><img src="' + pictureList[i] + '" onload="common.pictureImgLoad(this)"></div>';
        }
        html += "</div>";
        return html;
    }

    //获取罐子列表
    function getJarHtml(Jar) {
        var html = "";
        for(var i = 0; i < Jar.length; i++){
            html += "<span>"+Jar[i].name+"&nbsp;&nbsp;&nbsp;&nbsp;"+Jar[i].date+"</span>";
        }
        return html;
    }
    //合成图片
    function compositePicture() {
        //编辑
        $("#edit").on("click", function () {
            location.href = "writefootprints.html";
        });
        //确定
        $("#confirm").on("click", function () {
            var isHasPicture;
            if (_data.pictureList && _data.pictureList.length > 0) {
                isHasPicture = true;
            } else {
                isHasPicture = false
            }
            new CompositePicture(isHasPicture, function (canvas) {
                var img = new Image();
                try {
                    img.src = canvas.toDataURL();
                    img.id = "compositePictureCanvas";
                    img.onload = function () {
                        $("#contain").html(img);
                        $("#buttonWrap").hide();
                        $(".tip").show();
                    };
                }catch (res){
                    $("body").append("<div class='mask' id='generatePhotosFailBox_mask'></div>");
                    $("#generatePhotosFailBox").show();
                    $("#generatePhotosFailBox .button").on("click",function () {
                        $("#generatePhotosFailBox_mask").remove();
                        $("#generatePhotosFailBox").hide();
                    });
                }
            });
        });
    }

    //移动弹窗
    moveBox();
    function moveBox() {
        var html =
            '<div class="mask" id="moveBox">' +
            '    <div class="hintBox moveBox">' +
            '         <img src="image/icon/public/move.png">' +
            '         <p>选中要移动的相片，上下左右滑动至合适位置</p>' +
            '    </div>' +
            '</div>';
        $("body").append(html);
        $("#moveBox").on("click",function () {
            $(this).remove();
        });
        setTimeout(function () {
            $("#moveBox").remove();
        },3000)
    }
};
