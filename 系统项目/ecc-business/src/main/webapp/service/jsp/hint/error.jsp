<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>INSPINIA | 404 Error</title>
    <style>
    .main-box-container {
        padding: 20px 20px 0 20px;
        height: 100%;
    }
    
    .main-box {
        padding-top: 40px;
        height: 100%;
    }

    .main-box .ibox-content {
        width: 500px;
        height: 300px;
        overflow: auto;
        text-align: left;
        margin: 0 auto;
        border: 1px solid #e2e2e2;
    }
    .collapse-link {
        color: #489ae2;
    }
    .ico-error {
        width: 100px;
        color: #f50000;
        font-size: 85px;
    }
    </style>
</head>

<body class="gray-bg">
    
    <div class="main-box-container gray-bg full-width">
        <div class="text-center main-box white-bg ibox">
            <p class="m-md">
                <i class="ico-error"></i>
            </p>
              <p class="f-18">抱歉！您访问的页面出现异常，请重试或联系管理员</p>
            <p>
                <a   href="javascript:history.back(-1);" class="btn btn-outline btn-default btn-w-m">返回</a>
                <a  href="javascript:showErr();" class="btn btn-link collapse-link btn-w-m">
                 	   显示详情 <i class="fa fa-chevron-down"></i></span>
                </a>
            </p>
            <textarea class="form-control ibox-content" style="display: none;">${exception}</textarea>
        </div>
    </div>
   <!--  <div class="footer">
        Copyright &coyp; 2016 Powered By Zhangsan Version 1.0.0
    </div> -->
    <!-- Mainly scripts -->

<script type="text/javascript">
    $(function() {
        // Collapse ibox function
        $('.collapse-link').click(function() {
            var ibox = $(this).closest('div.ibox');
            var button = $(this).find('i');
            var content = ibox.find('.ibox-content');
            content.slideToggle(200);
            button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
            ibox.toggleClass('').toggleClass('border-bottom');
        });
    })
  function showErr(){
  	document.getElementById("err").style.display = "";
  }
    </script>
</body>

</html>
