<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <base href="<%=basePath%>">
<title>招商基金知识管理系统</title> 

<script type="text/javascript">
	function showdive(){
		document.getElementById("showbtn").style.display="none";
		document.getElementById("hiddenbtn").style.display="inline";
		document.getElementById("detaildiv").style.display="inline";
	}
	
	function hiddendive(){
		document.getElementById("showbtn").style.display="inline";
		document.getElementById("hiddenbtn").style.display="none";
		document.getElementById("detaildiv").style.display="none";
	}
</script>
    <style type="text/css"> 
        body { background-color: #fff; color: #666; text-align: center; font-family: arial, sans-serif; }
        div.dialog {
            width: 80%;
            padding: 1em 4em;
            margin: 4em auto 0 auto;
            border: 1px solid #ccc;
            border-right-color: #999;
            border-bottom-color: #999;
        }
        h1 { font-size: 200%;  line-height: 1.5em; }
    </style> 
</head> 
 
<body> 
  <div class="dialog"> 
     
    <c:if test="${custom!=null}">
	    
	    <c:if test="${custom.resultContent!=null}">
		    <h3>
		    	${custom.resultContent}
		    </h3>
	    </c:if>
	    
	    <c:if test="${custom.detailContent!=null}">
		    <h3>
		    	细节内容&nbsp;&nbsp;：&nbsp;&nbsp;
		    	<a id="showbtn" href="javascript:showdive()">[ 展 开 ]</a>
		    	<a id="hiddenbtn" style="display:none;" href="javascript:hiddendive()">[ 收 起 ]</a>
		    	<div id="detaildiv" style="display:none;">
		    		<c:if test="${custom.resultCode!=null}">
					    <h3>
					    	${custom.resultCode}
					    </h3>
			    	</c:if>
	    			<br/>
			    	${custom.detailContent}	
		    	</div>
		    </h3>
	    </c:if>
	    
    </c:if>
    
    <br/>
    
    <p>
    	<a href="" onclick="closeWindow()">关 闭</a> 
    </p> 
  </div>
  
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
</body> 
</html>
