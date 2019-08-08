/* 更改身份证有效期页面 
 */

$(document).ready(function (e) {
  queryICDateInfo();

  // 查询身份证有效期
  var calendar = new LCalendar();
  calendar.init({
    'trigger': '#idExpireDate', //标签id
    'type': 'date', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
    'minDate': '1998-1-1', //最小日期
    'maxDate': (new Date().getFullYear() + 100) + '-' + 12 + '-' + 31 //最大日期
  });

	$("input[name=foever]").click(function () {
		var flag = $(this).is(':checked');
		if (flag) {
			$("#idExpireDate").val("2099-12-31")
			$("#idExpireDate").attr("disabled", "disabled").css("background", "#fff")
		} else {
			$("#idExpireDate").removeAttr("disabled").css("background", "#fff");
			$("#idExpireDate").val("1998-12-31")
		}
	})



});

function queryICDateInfo() {
  $("#titleBack").click(function () {
    $('#titleBack').attr("href", "javascript:redirectUrl('/WeixinService/business/user/userInfo.shtml')");
  });
  $(".header .top-a h2").html("选择身份证有效期");
  var birthDate = GetQueryString("birthDate");
  $("#birthDate").val(birthDate);

  // 显示用户身份有效期
  var idExpireDate = localStorage.getItem('idExpireDate')
  idExpireDate = formatDate1(idExpireDate);
  $('#idExpireDate').val(idExpireDate)
};

/**
 * 获取地址栏传递参数
 * @param name
 * @returns
 */
function GetQueryString(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    var val = decodeURI(r[2]);
    return unescape(val);
  } else {
    return null;
  }
}

/**
 * 取消身份证有效期選擇事件
 * @returns
 */
function cancelICDateUpdate() {
  window.location.href = "/WeixinService/business/user/userInfo.shtml";
}

/**
 * 保存身份证有效期修改
 */
function saveIcDate() {
  var idExpireDate = $('#idExpireDate').val()
  var idExpireDate2 = idExpireDate;
  if (idExpireDate == '') {
    errorRemark('请选择身份有效期')
    return;
  }
  var nowDate = new Date();
  nowDate = Date.parse(nowDate.toLocaleDateString());
  idExpireDate = Date.parse(formatDateNum1(idExpireDate))
  if (idExpireDate > nowDate) {
    // 提交
    $.ajax({
      url: '/WeixinService/business/updateIdExpireDateByCustNo.xhtml',
      data: {
        'idExpireDate': idExpireDate2
      },
      dataType: 'json',//服务器返回json格式数据
      type: 'post',//HTTP请求类型
      success: function (data) {
        if (data.resultCode == '0000') {
          $(document).dialog({
            overlayClose: true,
            content: '修改成功',
          });
          setTimeout(function () {
            location.href = '/WeixinService/business/user/userInfo.shtml';
          }, 2500)
        }
      },
      error: function (xhr, type, errorThrown) {

      }
    });
  } else {
    errorRemark('身份证有效期须大于当前日期')
  }

}

/* 弹窗提示错误信息 */
function errorRemark(msg) {

  $("#errorRemark").html(msg);
  $("#errorDiv").show();
  $("#errorDiv").fadeOut(5000);
}

/**
 * 日期格式转换
 * @param date
 * @returns
 */
function formatDate1(date) {
  if (date == null || date == '') {
    return "";
  }
  date = unformat1(date);
  return date.substr(0, 4) + "-" + date.substr(4, 2) + "-" + date.substr(6, 2);
}

/**
 * 日期格式转换 '-'
 * @param date
 * @returns
 */
function formatDateNum1(date) {
  if (date == null || date == '') {
    return "";
  }
  date = unformat1(date);
  return parseInt(date.substr(0, 4), 10) + "/" + parseInt(date.substr(4, 2), 10) + "/" + parseInt(date.substr(6, 2), 10);
}
