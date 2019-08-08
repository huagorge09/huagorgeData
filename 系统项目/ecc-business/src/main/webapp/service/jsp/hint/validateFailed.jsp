<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
	<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>INSPINIA | 200 Success</title>

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
    
    .ico-validate-err {
        font-size: 85px;
        color: rgb(248,172,89); 
    }

    .collapse-link {
        color: #489ae2;
    }
    </style>
</head>

<body class="gray-bg">
    
    <div class="main-box-container gray-bg full-width">
        <div class="text-center main-box white-bg ibox">
            <p class="m-md ico-validate-err">
              <!--   <span class="ico-validate-err"><span class="path1"></span><span class="path2"></span><span class="path3"></span></span> -->
           <i class="fa fa-exclamation-circle"></i>
            </p>
            <p class="f-14"><c:if test="${not empty operatorId}">操作员代码：${operatorId}<br></c:if>消息提示：${exception}</p>
            <p>
                <a href="javascript:closeWindow();" class="btn btn-outline btn-default btn-w-m">关 闭</a>
            </p>
        </div>
    </div>


</body>
<script type="text/javascript">
  	function closeWindow(){
  		if(window.opener != null){
			if(window.opener.document.btnRefresh != null){
				window.opener.btnRefresh.click();
				if(window.opener.queryByCondtion != null)
				{
				    window.opener.queryByCondtion(false);
			    }
			}
			else{
				window.close();
				if(window.opener.queryByCondtion != null)
				{
				    window.opener.queryByCondtion(false);
			    }
			}
		}
		window.close();
	}
  </script>
</html>
