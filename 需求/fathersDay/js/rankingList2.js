$(function () {
    // 页数
    var page = 0;
    // 每页展示5个
    var size = 10;

    var beginIdx = 0;
    var endIdx = page * size;
    // dropload
    $('.content').dropload({
        scrollArea: window,
        loadDownFn: function (me) {
            beginIdx = page * size
            endIdx = beginIdx + size;
            // 拼接HTML
            var result = '';
            $.ajax({
                type: 'GET',
                url: config.service.queryAllUserAwardInfo,
                dataType: 'json',
                data: {
                    activityId: activityId,
                    beginIdx: beginIdx,
                    endIdx: endIdx
                },
                success: function (data) {
                    page++
                    var arrLen = data.data.length;

                    if (arrLen > 0) {
                        for (var i = 0; i < arrLen; i++) {
                            result += '<tr>' +
                                '<td><span></span></td>' +
                                ' <td>' + data.data[i].nickname + '</td>' +
                                '<td>' + data.data[i].awardName + '</td>' +
                                '</tr>'
                        }
                        // 如果没有数据
                    } else {
                        // 锁定
                        me.lock();
                        // 无数据
                        me.noData();
                    }
                    // 为了测试，延迟1秒加载

                    // 插入数据到页面，放到最后面
                    $("table").append(result);
                    var len = $("table tr").length;
                    for (var i = 0; i < len; i++) {
                        $("table").find("tr").eq(1 + i).find("td").eq(0).find("span").html(i + 1)
                    }
                    // 每次数据插入，必须重置
                    me.resetload();

                },
                error: function (xhr, type) {
                    // alert('Ajax error!');
                    // 即使加载出错，也得重置
                    //me.resetload();
                }
            });
        }
    });


});
function helpList() {
    location.href = "rankingList1.html?toUserId=" + toUserId + "&subscribeChannel=" + subscribeChannel + "&pageSource=" + pageId + "&eventId=event_wx_fdReturnIndexFromAwardWinngList"
}
function returnIndex() {
    location.href = "draw.html?toUserId=" + toUserId + "&subscribeChannel=" + subscribeChannel + "&pageSource=" + pageId + "&eventId=event_wx_fdReturnIndexFromAwardWinngList"
}
var pageId = "wx_fatherday_AwardWinngList";
var eventId = "";
/**
 * 初始化数据埋点
 */
dataRecord()
function dataRecord() {
    if (getUrlSearchParams("pageSource")) {
        pageSource = getUrlSearchParams("pageSource")
    } else {
        pageSource = pageId;
    }
    if (getUrlSearchParams("eventId")) {
        eventId = getUrlSearchParams("eventId")
    }
    if (eventId && pageSource) {
        operatingRecord(pageSource, eventId, pageId, "", "", "", "")
    }
}
