/**
 * 手机屏幕适配
 */
(function (doc, win) {
    var docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            var clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            docEl.style.fontSize = 100 * (clientWidth / 640) + 'px';
        };
    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);
/**
 * 获取滑动条高度
 */
var scroll = {
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
        if (Math.abs(isScrollBottom.getScrollTop() + isScrollBottom.getWindowHeight() - isScrollBottom.getScrollHeight()) < bottomPx) {
            return true;
        }
        return false;
    }
};
/**
 * 获取链接信息
 */
var getUrl = {
    protocol: location.protocol,
    host: location.host,
    origin: location.origin,
    href: location.href,
    getParam: function (key) {
        var reg = new RegExp('(^|&)' + key + '=([^&]*)(&|$)', 'i');
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return unescape(r[2]);
        }
        return '';
    }
};

/**
 * 验证
 */
var JXQ_validate = {
    //验证手机号
    isMobile: function (phone) {
        return /^0?(13[0-9]|15[012356789]|18[012356789]|14[57]|17[03678])[0-9]{8}$/.test(phone);
    },
    //验证身份证
    isIdCard: function (num) {
        num = num.toUpperCase();
        if (!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(num))) {
            return false;
        }
        var len, re, arrSplit, dtmBirth, bGoodDay, arrInt, arrCh, nTemp;
        len = num.length;
        if (len == 15) {
            re = new RegExp(/^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/);
            arrSplit = num.match(re);
            dtmBirth = new Date('19' + arrSplit[2] + '/' + arrSplit[3] + '/' + arrSplit[4]);
            bGoodDay = (dtmBirth.getYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
            if (!bGoodDay) {
                return false;
            } else {
                arrInt = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
                arrCh = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
                nTemp = 0;
                num = num.substr(0, 6) + '19' + num.substr(6, num.length - 6);
                for (var i = 0; i < 17; i++) {
                    nTemp += num.substr(i, 1) * arrInt[i];
                }
                num += arrCh[nTemp % 11];
                return true;
            }
        }
        if (len == 18) {
            re = new RegExp(/^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/);
            arrSplit = num.match(re);
            dtmBirth = new Date(arrSplit[2] + "/" + arrSplit[3] + "/" + arrSplit[4]);
            bGoodDay = (dtmBirth.getFullYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
            if (!bGoodDay) {
                return false;
            } else {
                var valnum;
                arrInt = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
                arrCh = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
                nTemp = 0;
                for (var i = 0; i < 17; i++) {
                    nTemp += num.substr(i, 1) * arrInt[i];
                }
                valnum = arrCh[nTemp % 11];
                if (valnum != num.substr(17, 1)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }
};
/**
 * @function sendCode            发送验证码
 * @param    codeId{string}      验证码按钮id
 * @param    activeClass{string} 验证码按钮禁止状态类
 * @param    sum{int}            验证码禁止时间
 */
function sendCode(codeId, activeClass, sum) {
    var codeBtn = $("#" + codeId);
    var text = codeBtn.text();
    sum = Number(sum);
    if (!codeBtn.hasClass(activeClass)) {
        codeBtn.addClass(activeClass);
        time();
        var delayed = setInterval(time, 1000);
    }
    function time() {
        codeBtn.text(sum + "s");
        sum--;
        if (sum < 0) {
            clearInterval(delayed);
            codeBtn.removeClass(activeClass).text(text);
        }
    }
}
var JXQ_msgTip = {
    activeBox: function (object) {
        object = object || {};
        var title = object.title || "提示";
        var contain = object.contain || "这是一个内容示例";
        var cancelText = object.cancelText || "取消";
        var confirmText = object.confirmText || "确定";
        var type = object.type ? object.type : 2;
        var actionBoxHtml = "";
        actionBoxHtml += '<div class="mask" id="mask"></div>' +
            '<div class="tipBox activeBox" id="activeBox">' +
            '<div class="activeBox-title">' + title + '</div>' +
            '<div class="activeBox-contain">' + contain + '</div>' +
            '<div class="activeBox-btnDiv">';
        if (type == 1) {
            actionBoxHtml += '<div id="activeBox_confirm">' + confirmText + '</div>'
        } else if (type == 2) {
            actionBoxHtml += '<div id="activeBox_cancel">' + cancelText + '</div>' + '<div id="activeBox_confirm">' + confirmText + '</div>'
        }
        actionBoxHtml += '</div></div>';
        if ($("#activeBox").length == 0) {
            $("body").append(actionBoxHtml);
            $("#mask").on("touchmove", function () {
                return false
            });
        }
        $("#activeBox_cancel").on("click", function () {
            $("#mask").off("touchmove").remove();
            $("#activeBox").remove();
            $.isFunction(object.cancel) && object.cancel();
        });
        $("#activeBox_confirm").on("click", function () {
            $("#mask").off("touchmove").remove();
            $("#activeBox").remove();
            $.isFunction(object.confirm) && object.confirm();
        })
    },
    autoBox: function (object) {
        object = object || {};
        var random = new Date().getTime();
        var contain = object.contain || "这是一个示例内容";
        var time = object.time || 1000;
        var autoBoxHtml = '<div class="tipBox autoBox" id="autoBox" data-id="autoBox' + random + '">' + contain + '</div>';
        if ($("#autoBox").length > 0) {
            $("#autoBox").remove();
        }
        $("body").append(autoBoxHtml);
        setTimeout(function () {
            $("[data-id=autoBox" + random + "]").css("opacity", 0);
        }, time);
        setTimeout(function () {
            $("[data-id=autoBox" + random + "]").remove();
        }, time + 500);
    }
};
var JXQ_select = {
    selectBox: function (selectBoxConfig) {
        this.selectBoxConfig = selectBoxConfig || {};
        var _this = this;
        var selectHtml =
            '<div class="mask" id="selectMask"></div>' +
            '<div id="selectBox">' +
            '<div class="swiper-container" id="select_swiper">' +
            '<div class="swiper-wrapper"></div>' +
            '<div class="select-line"></div>' +
            '</div>' +
            '<div class="select-btn clearFloat">' +
            '<div id="select_cancel">取消</div>' +
            '<div id="select_confirm">确定</div>' +
            '</div>' +
            '</div>';
        $(document).on("focus", "input[data-select-type]", function () {
            document.activeElement.blur();
        }).on("click", "input[data-select-type]", function () {
            var selectIpt = $(this);
            var selectType = selectIpt.attr("data-select-type");
            if ($("#selectMask").length == 0) {
                $("body").append(selectHtml);
                $("#selectMask").on("touchmove", function () {
                    return false
                });
                var slideHtml = "";
                if (_this.selectBoxConfig[selectType] && $.isArray(_this.selectBoxConfig[selectType])) {
                    for (var i = 0; i < _this.selectBoxConfig[selectType].length; i++) {
                        slideHtml += '<div class="swiper-slide">' + _this.selectBoxConfig[selectType][i] + '</div>';
                    }
                } else {
                    console.error("未配置" + selectType);
                }
                $("#selectBox .swiper-wrapper").html(slideHtml);
                var mySwiper = new Swiper('#select_swiper', {
                    direction: 'vertical',
                    centeredSlides: true,
                    slidesPerView: 5,
                    freeMode: true,
                    freeModeMomentumVelocityRatio: 1,
                    freeModeSticky: true
                });
                $("#selectBox").css("bottom", "0");
                $(".select-btn div").on("click", function () {
                    var id = $(this).attr("id");
                    $("#selectBox").css("bottom", "-4.5rem");
                    $("#selectMask").css("opacity", 0);
                    var value = $("#selectBox .swiper-slide-active").text();
                    var callback = {
                        selectType: selectType,
                        index: mySwiper.activeIndex,
                        value: value,
                        selectIpt: selectIpt
                    };
                    if (id == "select_cancel") {
                        $.isFunction(_this.selectBoxConfig.cancel) && _this.selectBoxConfig.cancel(callback);
                    } else if (id == "select_confirm") {
                        selectIpt.val(value);
                        $.isFunction(_this.selectBoxConfig.confirm) && _this.selectBoxConfig.confirm(callback);
                    }
                    setTimeout(function () {
                        $("#selectMask").remove();
                        $("#selectBox").remove();
                    }, 300);
                });
            }
        });
    },
    setSelectBox: function (object) {
        if (typeof object == "object") {
            for (var item in object) {
                this.selectBoxConfig[item] = object[item];
            }
        }
    }
};
(function ($, factory) {
    if ($) {
        factory($);
    } else {
        console.error("jQuery is Undefined");
    }
})(typeof jQuery == "undefined" ? false : jQuery, function ($) {
    var JXQ_slider = function (selector, context) {
        this.init(selector, context);
        this.compute("init", "");
        this.activeEvent();
    };
    JXQ_slider.prototype = {
        init: function (selector, context) {
            context = context || {};
            context.value = context.value || {};
            this.context = {
                value: {
                    min: context.value.min || 0,
                    max: context.value.max || 100,
                    current: context.value.current || context.value.min || 0
                },
                banSlider: false,
                banClick: false,
                getData: context.getData,
                sliderEnd: context.sliderEnd
            };
            this.selector = selector;
            this.width = $(this.selector).find(".slide-bar").width();
        },
        compute: function (type, value) {
            var keepValue = this.context.value.current;
            if (!this.context.banSlider && type == "slider") {
                this.left = value;
                this.context.value.current = Math.round(this.left / this.width * (this.context.value.max - this.context.value.min)) + this.context.value.min;
                this.per = (this.context.value.current - this.context.value.min) / (this.context.value.max - this.context.value.min);
            } else if (!this.context.banClick && type == "click") {
                if (value == "minus") {
                    this.context.value.current--;
                } else if (value == "plus") {
                    this.context.value.current++;
                }
                this.per = (this.context.value.current - this.context.value.min) / (this.context.value.max - this.context.value.min);
                this.left = this.per * this.width;
            } else if (type == "init") {
                this.per = (this.context.value.current - this.context.value.min) / (this.context.value.max - this.context.value.min);
                this.left = this.per * this.width;
            }
            if (Math.abs(this.context.value.current - keepValue) > 0 || type == "init") {
                $.isFunction(this.context.getData) && this.context.getData({
                    value: this.context.value.current,
                    per: this.per,
                    left: this.left,
                    type: type
                });
            }
            $(this.selector).find(".slide-button").css("left", this.left);
            $(this.selector).find(".slide-schedule").css("width", this.left);
        },
        eventType: (function () {
            if ("ontouchstart" in window) {
                return {
                    start: "touchstart",
                    move: "touchmove",
                    end: "touchend",
                    flag: true
                }
            } else {
                return {
                    start: "mousedown",
                    move: "mousemove",
                    end: "mouseup",
                    flag: false
                }
            }
        })(),
        activeEvent: function () {
            var _this = this;
            var keepLeft, startX, moveX;
            var isButton = false;
            $(_this.selector).on(_this.eventType.start, ".slide-button", function (start) {
                isButton = true;
                keepLeft = _this.left;
                startX = _this.eventType.flag ? start.originalEvent.targetTouches[0].pageX : start.originalEvent.pageX;
                var flag = true;
                $(document).on(_this.eventType.move, function move(move) {
                    moveX = _this.eventType.flag ? move.originalEvent.targetTouches[0].pageX : move.originalEvent.pageX;
                    var left = keepLeft + moveX - startX;
                    if (flag || left >= 0 && left <= _this.width) {
                        if (left <= 0) {
                            left = 0;
                            flag = false;
                        } else if (left >= _this.width) {
                            left = _this.width;
                            flag = false;
                        }
                        _this.compute("slider", left);
                    }
                });
            });
            $(document).on(_this.eventType.end, function () {
                if(isButton){
                    $(document).off(_this.eventType.move);
                    $.isFunction(_this.context.sliderEnd) && _this.context.sliderEnd({
                        value: _this.context.value.current,
                        per: _this.per,
                        left: _this.left
                    });
                }
            });
            $(_this.selector).on("click", ".slide-minus", function () {
                if (_this.context.value.current > _this.context.value.min) {
                    _this.compute("click", "minus");
                }
            });
            $(_this.selector).on("click", ".slide-plus", function () {
                if (_this.context.value.current < _this.context.value.max) {
                    _this.compute("click", "plus");
                }
            });
        }
    };
    window.JXQ_slider = JXQ_slider;
});
// 获取样色数值
var getStyleVal =function(el, attr){ var v=0; if(el.currentStyle){ v= el.currentStyle[attr] } else { v= getComputedStyle(el,false)[attr]; } return parseInt(v.replace("px","")) };

// class处理
var addClass =function(ele, className){
    if (!ele || !className || (ele.className && ele.className.search(new RegExp("\\b" + className + "\\b")) != -1)) return;
    ele.className += (ele.className ? " " : "") + className;
};

var removeClass = function(ele, className){
    if (!ele || !className || (ele.className && ele.className.search(new RegExp("\\b" + className + "\\b")) == -1)) return;
    ele.className = ele.className.replace(new RegExp("\\s*\\b" + className + "\\b", "g"), "");
};