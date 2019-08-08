$(document).ready(function (e) {
    getUserRequest("pc_newsList_01");
    document.title = "新闻动态_招商财富 公司新闻、公司动态、新闻资讯、招商财富动态";
    $(".nav.fr ul li a").removeClass("current").eq(5).addClass("current");
    //noticeList();
    queryBondInvestorArticleNum("1");
    queryBondInvestorList()
    $(".information-left ul li a").removeClass("act");
    $(".information-left ul li.newnotice a").addClass("act");

});

/* 搜索 */
function searchByName() {
    $(".information-left ul li").each(function () {
        if ($(this).find("a").hasClass("act")) {
            var index = $(this).index();
            if (index == "0") {
                queryBondInvestorArticle("1");
            }
        }
    })
}

/* 信息公示/列表切换 */
function queryBondInvestorList() {
    getUserRequest("pc_publicityList_01");
    queryBondInvestorArticle("1");
}

/* 信息公示/列表查询 */
function queryBondInvestorArticleNum(pages) {
    var name = $("#name").val();
    var page = returnPage(pages);
    $.ajax({
        async: true,
        url: "/AppService/article/queryBondInvestorArticle.xhtml",
        data: {
            "page": page,
            "name": encodeURI(name)
        },
        dataType: "json",
        cache: false,
        type: "post",
        error: function (textStatus, errorThrown) {
        },
        success: function (data) {
            if (data.list == null || data.list.length <= 0) {
                return;
            }
        }
    });
}

/* 信息公示/列表查询 */
function queryBondInvestorArticle(pages) {
    var name = $("#name").val();
    var page = returnPage(pages);
    $.ajax({
        async: true,
        url: "/AppService/article/queryBondInvestorArticle.xhtml",
        data: {
            "page": page,
            "name": encodeURI(name)
        },
        dataType: "json",
        cache: false,
        type: "post",
        error: function (textStatus, errorThrown) {
        },
        success: function (data) {
            var htmls = "";
            var htmls2 = "";
            if (data.list != null && data.list.length > 0) {
                $.each(data.list, function (i, item) {
                    htmls += "<dl>";
                    htmls += "<h3 class='cursor' onclick='openToUrl(\"/AppService/article/newsDetail.shtml?articleId=" + item.articleId + "&type=publicity\")'>" + item.title + "</h3>";
                    htmls += "<dt>" + formatDate(item.publishDate) + "</dt>";
                    htmls += "<dd>" + item.brief + "</dd>";
                    htmls += "</dl>";
                })
                var page = data.currentPage;
                var count = data.count;
                var maxPages = data.maxPages;
                $("#page").val(page);
                $("#maxPages").val(maxPages);

                htmls2 += "<a href='javascript:queryBondInvestorArticle(\"" + (page - 1) + "\")' class='pre'></a>";
                if (page == 1) {
                    htmls2 += "<a class='act' href='javascript:queryBondInvestorArticle(\"1\")'>1</a>";
                } else {
                    htmls2 += "<a href='javascript:queryBondInvestorArticle(\"1\")'>1</a>";
                }

                if (page > 3) { /* 左边加...*/
                    htmls2 += "<a class='omit'></a>";
                }

                if ((page - 1) > 1) { /* 上一页*/
                    htmls2 += "<a href='javascript:queryBondInvestorArticle(\"" + (page - 1) + "\")'>" + (page - 1) + "</a>";
                }

                if (page != 1 && page != maxPages) { /* 当前页*/
                    htmls2 += "<a class='act' href='javascript:queryBondInvestorArticle(\"" + (page) + "\")'>" + (page) + "</a>";
                }

                if (page + 1 < maxPages) { /*下一页*/
                    htmls2 += "<a href='javascript:queryBondInvestorArticle(\"" + (page + 1) + "\")'>" + (page + 1) + "</a>";
                }

                if ((maxPages - page) > 2) { /* 右边加...*/
                    htmls2 += "<a class='omit'></a>";
                }

                if (page != 1) {
                    if (maxPages == page) {
                        htmls2 += "<a class='act' href='javascript:queryBondInvestorArticle(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
                    } else if (maxPages > page) {
                        htmls2 += "<a href='javascript:queryBondInvestorArticle(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
                    }
                } else {
                    if (maxPages == page) {

                    } else if (maxPages > page) {
                        htmls2 += "<a href='javascript:queryBondInvestorArticle(\"" + (maxPages) + "\")'>" + maxPages + "</a>";
                    }
                }
                htmls2 += "<a href='javascript:queryBondInvestorArticle(\"" + (page + 1) + "\")' class='next'></a>";
                htmls2 += "</div>";

                $("#list").html(htmls);
                $(".page").html(htmls2);
                $(".listNone").hide()
            } else {
                $("#list").html(htmls);
                $(".page").html(htmls2);
                $(".listNone").show()
                //				if($("#name").val()==""){
                //				   $(".newnotice").addClass("none");
                //				}else{
                //				   $("#name").val("")
                //				}
                //				noticeList()
                //				return;
            }

        }
    });
}

function returnPage(pages) {
    var page;
    var maxPages = $("#maxPages").val();

    if (parseInt(pages, 10) >= parseInt(maxPages, 10)) {
        page = maxPages;
    } else if (parseInt(pages, 10) <= 0) {
        page = "1";
    } else {
        page = pages;
    }
    return page;
}