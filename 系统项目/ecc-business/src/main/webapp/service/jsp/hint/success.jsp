<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    
    .ico-success {
        font-size: 85px;
         color: #79a64a;
    }

    .collapse-link {
        color: #489ae2;
    }
    </style>
</head>

<body class="gray-bg">
    
    <div class="main-box-container gray-bg full-width">
        <div class="text-center main-box white-bg ibox">
            <p class="m-md ico-success">
               <!--  <span class="ico-success"><span class="path1"></span><span class="path2"></span><span class="path3"></span></span> -->
                <i class="fa fa-check-circle"></i>
            </p>
            <p class="f-18">操作成功<c:if test="${not empty successInfo}">，${successInfo}<br></c:if></p>
            <p>
                <a href="javascript:closeWindow();" class="btn btn-outline btn-default btn-w-m">关 闭</a>
            </p>
        </div>
    </div>
    <!-- <div class="footer">
        Copyright &coyp; 2016 Powered By Zhangsan Version 1.0.0
    </div>
 -->

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
				if(window.opener.queryByCondition != null)
				{
					if(getQueryString("sub")!=null && getQueryString("sub")=='true'){
						var subgrid=getQueryString("sub_gridid");
						if(subgrid)
						    window.opener.querySubByCondtion(false,subgrid);
							
					};
				    window.opener.queryByCondtion(false);
			    }
			}
		}
		window.close();
	}
  </script>
</html>
