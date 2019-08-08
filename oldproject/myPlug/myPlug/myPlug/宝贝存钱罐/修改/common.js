var common = {
    /**
     * 足迹内容超出处理
     */
    dayItemLoad:function () {
        $(".dayItem").each(function () {
            var text = $(this).find(".wrap").children(".text");
            if(text.get(0)){
                if(text.get(0).scrollHeight > text.height()){
                    text.after("<span class='text-spread'>展开</span>")
                }
            }
        });
        $(".text-spread").on("click",function () {
            if($(this).hasClass("on")){
                $(this).removeClass("on").text("展开").siblings(".text").css({
                    "display":"-webkit-box",
                    "max-height":"2.4rem"
                });
            }else {
                $(this).addClass("on").text("收起").siblings(".text").css({
                    "display":"block",
                    "max-height":"none"
                });
            }
            return false
        });
    },
    /**
     * 图片尺寸处理，preview下图片内容更新后需再次调用此方法
     */
    pictureImgLoad:function(that){
        if(that.width >= that.height){
            $(that).css({
                "width": "auto",
                "height": "100%"
            });
        }
    },
    /**
     * 图片预览
     * @param Object[{currentUrl:"当前选择的图片链接",urlList:"需要预览的图片链接列表",hasRemove:"是否有删除按钮",callback:function}]
     * Object.callback回调返回删除后的图片链接数组
     */
    previewPicture: function (Object) {
        var currentUrl = Object.currentUrl;
        var urlList = Object.urlList;
        var hasRemove = Object.hasRemove;
        var index = urlList.indexOf(currentUrl) == -1 ? 0 : urlList.indexOf(currentUrl);
        //添加swiper元素
        var html = '<div class="previewPicture" id="previewPicture"><div class="swiper-container previewPicture-swiper-wrapper"><div class="swiper-wrapper">';
        for (var i = 0; i < urlList.length; i++) {
            html += '<div class="swiper-slide"><img src="' + urlList[i] + '"></div>';
        }
        html += '</div><div class="swiper-pagination" id="previewPicture-swiper-pagination"></div></div>';
        //是否添加删除按钮
        if (hasRemove) {
            html += "<div class='delete' id='previewPicture-delete'>删除</div>"
        }
        html += '</div>';
        $("body").append(html);
        var previewPicture = new Swiper(".previewPicture-swiper-wrapper", {
            initialSlide: index,
            pagination: "#previewPicture-swiper-pagination"
        });
        $("#previewPicture").on("click", function (e) {
            $.isFunction(Object.callback) && Object.callback(urlList);
            $(this).remove();
        });
        //预览图片删除
        $("#previewPicture-delete").on("click", function (e) {
            e.stopPropagation();
            common.affirmHintBox({
                content: "真的要删除吗？",
                callback: function (res) {
                    if (res === "confirm") {
                        urlList.splice(previewPicture.activeIndex, 1);
                        previewPicture.removeSlide(previewPicture.activeIndex);
                        previewPicture.updateSlidesSize();
                        if (!($("#previewPicture").find(".swiper-slide").length > 0)) {
                            $.isFunction(Object.callback) && Object.callback(urlList);
                            $("#previewPicture").remove();
                        }
                    }
                }
            });
        });
    },
    /**
     * 自动消失提示弹窗
     * @param object[{content:"提示内容"，icon:"图标链接"，time:"显示时间"}]
     */
    autoHintBox: function (object) {
        object = object || {};
        var random = new Date().getTime();
        var content = object.content || "示例内容";
        var time = object.time || 1000;
        var html = '<div class="hintBox centerHintBox autoHintBox" id="autoHintBox" data-id="autoHintBox' + random + '">';
        if(object.title){
            html += '<div class="title">'+object.title+'</div>';
        }
        if (object.icon) {
            html += '<img class="icon" src="' + object.icon + '">';
        }
        html += '<div class="context">' + content + '</div></div>';
        if ($("#autoHintBox").length > 0) {
            $("#autoHintBox").remove();
        }
        $("body").append(html);
        setTimeout(function () {
            $("[data-id=autoHintBox" + random + "]").css("opacity", 0);
        }, time);
        setTimeout(function () {
            $("[data-id=autoHintBox" + random + "]").remove();
            typeof object.callback == "function" && object.callback();
        }, time + 300);
    },
    /**
     * 确认弹窗
     * @param object[{content:"提示内容"，callback:function}]
     * object.callback回调返回点击了哪个按钮
     */
    affirmHintBox: function (object) {
        var content = object.content || "示例内容";
        var confirmText = object.confirm || "确定";
        var cancelText = object.cancel || "取消";
        var html = '<div class="hintBox bottomHintBox affirmHintBox" id="affirmHintBox" style="display: none">' +
            '<div class="content">' + content + '</div>' +
            '<div class="confirm button" id="confirm">' + confirmText + '</div>' +
            '<div class="cancel button" id="cancel">' + cancelText + '</div>' +
            '</div><div class="mask" id="affirmHintBoxMask"></div>';
        $("body").append(html);
        $("#affirmHintBox").slideDown(200);
        $("#affirmHintBox .button").on("click", function () {
            $("#affirmHintBox").remove();
            $("#affirmHintBoxMask").remove();
            typeof object.callback == "function" && object.callback($(this).attr("id"));
        });
    },
    /**
     * 评论弹窗
     * @param object[{placeholder:"提示文字"，callback:function}]
     * 点击发送时object.callback回调函数返回输入内容
     */
    commentBox:function (object) {
        var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
        var height = window.innerHeight;
        var html =
            '<div class="hintBox bottomHintBox commentBox" id="commentBox">\n' +
            '    <textarea placeholder="'+object.placeholder+'" id="commentBoxTextarea"></textarea>\n' +
            '    <div class="confirm" id="commentBoxConfirm">发送</div>\n' +
            '</div><div class="mask" id="commentBoxMask"></div>';
        $("body").append(html);
        var boxH = $("#commentBox").height();
        //解决键盘弹出时出现的一些问题
        $("html,body").addClass("ban");
        $("#commentBoxTextarea").trigger("click").focus();
        setTimeout(function () {
            // $(".page").css({
            //     "position" : "relative",
            //     "top" : -scrollTop + "px"
            // });
        },500);
        $("#commentBoxTextarea").on("focus",function () {
            // setTimeout(function () {
            //     $(".page").css({
            //         "position" : "relative",
            //         "top" : -scrollTop + "px"
            //     });
            // },500);
        });
        //输入框高度自适应
        var textarea = $("#commentBoxTextarea").height();
        $("#commentBoxTextarea").on("input",function () {
            if(this.scrollHeight > 4*textarea){
                $(this).height(4*textarea);
            }else {
                $(this).height(this.scrollHeight);
            }
        });
        //评论完成
        $("#commentBoxMask").on("click",function () {
            recovery();
        }).on("touchmove",function () {
            return false
        });
        $("#commentBoxConfirm").on("click",function () {
            typeof object.callback == "function" && object.callback($("#commentBoxTextarea").val());
            recovery();
        });
        function recovery() {
            $("#commentBoxTextarea").val("");
            $("#commentBox").remove();
            $("#commentBoxMask").remove();
            $("html,body").removeClass("ban");
            $(".page").css({
                "top" : 0
            });
            document.body.scrollTop = document.documentElement.scrollTop = scrollTop;
        }
    },
    /**
     * 分享弹窗
     */
    shareBox:function (text) {
        var html = '<div class="hintBox shareBox" id="hintBox"><div class="shareTip"><p>点击右上角，</p><p>'+text+'</p></div></div>';
        $("body").append(html);
        $("#hintBox").on("click",function () {
            $(this).remove();
        });
    },
    /**
     * 是否是微信
     * @returns {boolean}
     */
    isWeiXin:function () {
        return window.navigator.userAgent.toLowerCase().match(/MicroMessenger/i) == 'micromessenger'
    },
    /**
     * 获取参数链接
     * @param _name[key]
     * @returns {string}[value]
     */
    getUrlSearchParams:function (_name){
        var name,value='';
        var str=window.location.href;
        var num=str.indexOf("?");
        str=str.substr(num+1);
        var arr=str.split("&");
        for(var i=0;i < arr.length;i++){
            num=arr[i].indexOf("=");
            if(num>0){
                name=arr[i].substring(0,num);
                if(name.replace(/^\s+|\s+$/g,"") == _name){
                    value=arr[i].substr(num+1);
                    break;
                }
            }
        }
        return decodeURI(value);
    },
    /**
     * 是否滑动到了底部
     */
    isScrollBottom: {
        //滚动条在Y轴上的距离
        getScrollTop: function () {
            var scrollTop = 0,
                bodyScrollTop = 0,
                documentScrollTop = 0;
            if (document.body) {
                bodyScrollTop = document.body.scrollTop;
            }
            if (document.documentElement) {
                documentScrollTop = document.documentElement.scrollTop;
            }
            scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop : documentScrollTop;
            return scrollTop;
        },
        //文档的高度
        getScrollHeight: function () {
            var scrollHeight = 0,
                bodyScrollHeight = 0,
                documentScrollHeight = 0;
            if (document.body) {
                bodyScrollHeight = document.body.scrollHeight;
            }
            if (document.documentElement) {
                documentScrollHeight = document.documentElement.scrollHeight;
            }
            scrollHeight = (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight : documentScrollHeight;
            return scrollHeight;
        },
        //浏览器窗口的高度
        getWindowHeight: function () {
            var windowHeight = 0;
            if (document.compatMode == 'CSS1Compat') {
                windowHeight = document.documentElement.clientHeight;
            } else {
                windowHeight = document.body.clientHeight;
            }
            return windowHeight;
        },
        //判断滚动条是否到底部
        scrollBottom: function (bottomPx) {
            if (Math.abs(common.isScrollBottom.getScrollTop() + common.isScrollBottom.getWindowHeight() - common.isScrollBottom.getScrollHeight()) < bottomPx) {
                return true;
            }
            return false;
        }
    },
    /**
     *输入金额处理
     */
    moneyHandle:function (string) {
        var v = string;
        if(/^0+\d+\.?\d*.*$/.test(v)){
            v = v.replace(/^0+(\d+\.?\d*).*$/, '$1');
        }else if(!/^\d+\.\d{2}$/.test(v)){
            if(/^\d+\.\d{2}.+/.test(v)){
                v = v.replace(/^(\d+\.\d{2}).*$/, '$1');
            }else if(/^[^\d]+\d+\.?\d*$/.test(v)){
                v = v.replace(/^[^\d]+(\d+\.?\d*)$/, '$1');
            }else if(/\d+/.test(v)){
                v = v.replace(/^[^\d]*(\d+\.?\d*).*$/, '$1');
            }else if(/^0+\d+\.?\d*$/.test(v)){
                v = v.replace(/^0+(\d+\.?\d*)$/, '$1');
            }
        }
        return v;
    },
    /**
     * 格式化时间
     */
    sec_to_time : function(s) {
        var t = "";
        if(s > -1){
            var hour = Math.floor(s/3600);
            var min = Math.floor(s/60) % 60;
            var sec = s % 60;
            if(hour > 0){
                if(hour < 10) {
                    t = '0'+ hour + ":";
                } else {
                    t = hour + ":";
                }
            }
            if(min < 10){
                t += "0";
            }
            t += min + ":";
            if(sec < 10){t += "0";}
            t += sec;
        }
        return t;
    },
    /**
     * 获取二维码图片事数据
     */
    getCodeImageBase64:function (src,w) {
        var makeQCCode=new QRCode("",{
            width:w,
            height:w
        });
        makeQCCode.makeCode(src);
        return makeQCCode._oDrawing.getData();
    },
    /**
     *
     */
    addLoad:{
        haveInHand:function () {
            if(!($("#loadAnimation").length>0)){
                $("body").append('<div class="loadAnimation" id="loadAnimation"><span class="icon"></span>努力加载中...</div>');
            }
        },
        end:function () {
            $("#loadAnimation").remove();
        },
        noMore:function () {
            if($("#loadAnimation").length>0){
                common.addLoad.end();
            }
            $("body").append('<div class="loadAnimation" id="loadAnimation">已经没有更多了</div>');
        }
    }
};

/**
 * 进度条弹窗
 */
(function (window) {
    var ScheduleBox = function (text) {
        var content = text || "加载中...";
        var html = "";
        html += '<div class="mask" id="scheduleBoxMask"></div>' +
            '<div class="hintBox centerHintBox scheduleBox" id="scheduleBox">' +
            '<div class="content">' + content + '</div>' +
            '<div class="scheduleBar"><div class="schedule">' +
            '</div></div></div>';
        $("body").append(html);
        $("#scheduleBox").find("scheduleBar").css("width", "0%");
    };
    ScheduleBox.prototype = {
        //设置进度
        setSchedule: function (per) {
            var Per = Number(per) >= 0 ? (Number(per) >= 100 ? 100 : Number(per)) : 0;
            $("#scheduleBox").find(".schedule").css("width", Per + "%");
        },
        //结束
        finish: function () {
            $("#scheduleBoxMask").remove();
            $("#scheduleBox").remove();
        }
    };
    window.ScheduleBox = ScheduleBox;
})(window);
/**
 * 图片移动
 */
(function (window) {
    function PictureMove(ele) {
        var that = this;
        var sx,sy,mx,my,imgW,imgH,wrap,wrapW,wrapH,imgL,imgT;
        ele.on("touchstart",function (e) {
            sx = e.changedTouches[0].pageX;
            sy = e.changedTouches[0].pageY;
            imgW = $(this).innerWidth();
            imgH = $(this).innerHeight();
            wrap = $(this).parent();
            wrapW = wrap.innerWidth();
            wrapH = wrap.innerHeight();
            imgL = $(this).position().left;
            imgT = $(this).position().top;
            e.preventDefault();
        });
        ele.on("touchmove",function (e) {
            mx = e.changedTouches[0].pageX;
            my = e.changedTouches[0].pageY;
            that.handle($(this),mx-sx,my-sy);
            e.preventDefault();
        });
        this.handle = function (that,x,y) {
            var dx = Math.abs(imgW - wrapW);
            var dy = Math.abs(imgH - wrapH);
            var left = imgL + x;
            var top = imgT + y;
            if(left < -dx){
                left = -dx;
            }else if(left > 0){
                left = 0;
            }
            if(top < -dy){
                top = -dy
            }else if(top > 0){
                top = 0
            }
            that.css({
                "left" : left,
                "top" : top
            });
        };
    }
    window.PictureMove = PictureMove;
})(window);
/**
 * 生成相片
 */
(function () {
    var Ratio = 750/window.innerWidth;
    var footer = {
        height: 119
    };
    function CompositePicture(hasPicture,callback) {
        var that = this;
        this.callback = callback;
        this.cvs = document.createElement("canvas");
        this.cvs.id = "compositePictureCanvas";
        this.ctx = this.cvs.getContext("2d");
        this.flag = hasPicture;
        if(hasPicture === true){
            this.contain = $("#hasPicture");
            this.setCanvas(this.cvs,this.contain);
            this.drawHasPicture();
        }else if(hasPicture === false){
            this.contain = $("#notPicture");
            this.setCanvas(this.cvs,this.contain);
            this.drawNotPicture();
        }else {
            alert("是否有图片 ? true : false");
        }
    }
    CompositePicture.prototype = {
        setCanvas : function (cvs,contain) {
            cvs.width = contain.innerWidth()*Ratio;
            cvs.height = contain.innerHeight()*Ratio+footer.height;
        },
        drawHasPicture : function () {
            var that = this;
            var pictureList = this.contain.find(".pictureList").find("img");
            for(var i = 0; i < pictureList.length; i++){
                var img = new Image();
                img.src = pictureList.eq(i).attr("src");
                img.pl = pictureList.eq(i).parent().offset().left*Ratio;
                img.pt = pictureList.eq(i).parent().offset().top*Ratio;
                img.tw = pictureList.eq(i).parent().innerWidth()*Ratio;
                img.th = pictureList.eq(i).parent().innerHeight()*Ratio;
                img.w = pictureList.eq(i).innerWidth()*Ratio;
                img.cl = pictureList.eq(i).position().left*Ratio;
                img.ct = pictureList.eq(i).position().top*Ratio;
                img.onload = function () {
                    var per = this.width/this.w;
                    var sl = -this.cl*per;
                    var st = -this.ct*per;
                    that.ctx.drawImage(this,sl,st,this.tw*per,this.th*per,this.pl,this.pt,this.tw,this.th);
                }
            }
            this.drawJar();
            this.drawParagraph(3);
            this.drawCode();
            this.drawFooter();
            this.callback(this.cvs);
        },
        drawNotPicture : function () {
            this.drawCode();
            this.drawJar();
            this.drawParagraph(9);
            this.drawFooter();
            this.callback(this.cvs);
        },
        drawJar : function () {
            var jarList = this.contain.find(".jar").find("span");
            for(var i =0; i < jarList.length; i++){
                var pl = jarList.eq(i).offset().left*Ratio;
                var pt = jarList.eq(i).offset().top*Ratio;
                var text = jarList.eq(i).text();
                var _fontSize = jarList.eq(i).css("font-size");
                var fontSize = Number(_fontSize.substring(0,_fontSize.length-2))*Ratio;
                var fontFamily = jarList.eq(i).css("font-family");
                var w = jarList.eq(i).outerWidth()*Ratio;
                var h = jarList.eq(i).outerHeight()*Ratio;
                var x = pl+w/2;
                var y = pt+h/2;
                var color = jarList.eq(i).css("color");
                this.ctx.beginPath();
                this.ctx.moveTo(pl + h/2, pt);
                this.ctx.lineTo(pl + w - h/2,pt);
                this.ctx.strokeStyle = color;
                this.ctx.arc(pl + w - h/2,pt + h/2,h/2,-0.5*Math.PI,0.5*Math.PI);
                this.ctx.lineTo(pl + h/2, pt + h);
                this.ctx.arc(pl + h/2,pt + h/2,h/2,0.5*Math.PI,1.5*Math.PI);
                this.ctx.stroke();
                this.ctx.beginPath();
                this.ctx.font = fontSize + "px " + fontFamily;
                this.ctx.textAlign="center";
                this.ctx.textBaseline="middle";
                this.ctx.fillStyle = color;
                this.ctx.fillText(text,x,y);
            }
        },
        drawCode : function () {
            var codeImage = this.contain.find(".codeImage");
            var pl = codeImage.offset().left*Ratio;
            var pt = codeImage.offset().top*Ratio;
            var tw = codeImage.innerWidth()*Ratio;
            var th = codeImage.innerHeight()*Ratio;
            this.ctx.drawImage(codeImage.get(0),pl,pt,tw,th);
            var fontSize,x = pl + tw/2,y;
            if(this.flag){
                fontSize = 18;
                y = pt + th + 20;
            }else {
                fontSize = 30;
                y = pt + th +25;
            }
            this.ctx.font = fontSize + "px Adobe";
            this.ctx.textAlign="center";
            this.ctx.textBaseline="middle";
            this.ctx.fillText("扫码查看完整足迹",x,y);
        },
        drawParagraph : function (line) {
            var textEle = this.contain.find(".text");
            var text = textEle.text();
            var w = textEle.innerWidth()*Ratio;
            var _fontSize = textEle.css("font-size");
            var fontSize = Number(_fontSize.substring(0,_fontSize.length-2))*Ratio;
            var lineHeight = Number(textEle.css("line-height").substring(0,textEle.css("line-height").length-2))*Ratio;
            var color = textEle.css("color");
            var pl = textEle.offset().left*Ratio;
            var pt = textEle.offset().top*Ratio;
            var fontFamily = textEle.css("font-family");
            this.ctx.font = fontSize + "px " + fontFamily;
            var textArray = [];
            var sIndex = 0;
            if(this.ctx.measureText(text).width < w){
                textArray.push(text);
            }else {
                for(var i = 0; i < text.length; i++){
                    if(this.ctx.measureText(text.substring(sIndex,i)).width > w){
                        i = i-1;
                        textArray.push(text.substring(sIndex,i));
                        sIndex = i;
                    }
                }
            }
            this.ctx.beginPath();
            this.ctx.font = fontSize + "px " + fontFamily;
            this.ctx.textAlign="start";
            this.ctx.textBaseline="middle";
            this.ctx.fillStyle = color;
            for(var j = 0;j < textArray.length;j++){
                var top = pt + j*lineHeight + lineHeight/2;
                if(line && line < textArray.length){
                    if(j < line-1){
                        console.log("1");
                        this.ctx.fillText(textArray[j],pl,top);
                    }else if(j == line-1){
                        console.log("2");
                        this.ctx.fillText(textArray[j].substring(0,textArray[j].length-1)+"...",pl,top);
                    }else {
                        break;
                    }
                }else {
                    this.ctx.fillText(textArray[j],pl,top);
                }
            }
        },
        drawFooter:function () {
            var that = this;
            this.ctx.beginPath();
            this.ctx.fillStyle = "#ebebeb";
            this.ctx.rect(0,this.cvs.height- footer.height,this.cvs.width,footer.height);
            this.ctx.fill();
            var image = new Image();
            image.src = "image/icon/public/logo.png";
            image.onload = function () {
                that.ctx.drawImage(image,165,that.cvs.height-96,87,72);
                that.ctx.font = "30px Adobe";
                that.ctx.fillStyle = "#333";
                that.ctx.textAlign="start";
                that.ctx.textBaseline="middle";
                that.ctx.fillText("有趣有爱的互联网教育金",267,that.cvs.height - footer.height/2);
            }
        }
    };
    window.CompositePicture = CompositePicture;
})();