<%@page import="com.cmwa.ec.log.filter.PCWXSkipFilter"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ page import = "com.cmwa.ec.user.facade.UserService"%>
<%@ page import = "com.cmwa.ec.user.facade.dto.UserServiceMessage"%>
<%@ page import = "org.apache.log4j.Logger"%>
<%@ page import = "com.cmwa.ec.webapp.util.ECConstants"%>
<%@ page import = "com.cmwa.ec.user.facade.dto.UserBaseInfoDto"%>
<%@ page import = "com.cmwa.ec.webapp.util.MD5"%>
<%@ page import = "com.cmwa.ec.base.dto.Context"%>
<%@ page import = "com.cmwa.ec.webapp.util.ContextUtils"%>
<%@ page import = "com.cmwa.ec.webapp.client.UserServiceClient"%>
<%@ page import = "org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import = "org.springframework.context.ApplicationContext"%>
<%@ page import = "org.apache.log4j.Logger"%>

 <%
 	Logger  logger = Logger.getLogger(UserServiceClient.class.getName());
 	String  LoginUserId=request.getParameter("LoginUserId");//mecc登陆名
	String  LonginAccunt=request.getParameter("LonginAccunt");//登陆账号
	String  LoginCode=request.getParameter("LoginCode");//随机code
	String  seqId = request.getSession().getId();
	String  channel=request.getParameter("channel");//渠道
	try {
		 //获取bean
		 ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());    
		 UserServiceClient client = (UserServiceClient)ctx.getBean("userServiceClient");  
		 //输出日志
		logger.info("LonginAccunt"+LonginAccunt+"seqId"+seqId);
		//记录日志
		Context context=ContextUtils.setContext(ECConstants.USER_SERVICE_001, channel,
				"", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		 //发送请求
		UserServiceMessage userServiceMsg = client.queryMeccLogin(context, LonginAccunt,LoginCode,ECConstants.LOGIN_CHANEL_90 , ECConstants.LOGIN_NMARK_03,LoginUserId);
		//返回的信息
		String returnCode="";
		String returnMsg="";
			if(userServiceMsg!=null){
				returnCode=userServiceMsg.getReturnCode();
				if(ECConstants.COMMON_SUCCESS.equals(returnCode)){
					//更新session中的用户信息
					UserBaseInfoDto userBaseInfo = userServiceMsg.getUserBaseInfoDto();
					String isSetTradePassword = "1"; //已设置或修改初始的支付密码
					if(userBaseInfo.getLPassword().equals(userBaseInfo.getTPassword())){
						isSetTradePassword = "0";//未设置或修改初始的支付密码
					}
					
					request.getSession(true).setAttribute("reqChannel", "MECC");
					request.getSession(true).setAttribute("isSetTradePassword", isSetTradePassword);
					request.getSession(true).setAttribute("userBaseInfo", userBaseInfo);
					request.getSession(true).setAttribute("userAccoRla", userServiceMsg.getUserAccoRlaDto());
					
					//如果不是微信端请求，则不推送绑定成功消息
					String cmfUserId = userServiceMsg.getUserBaseInfoDto().getCmfUserId();
					request.getSession(true).setAttribute("cmfUserId", cmfUserId);
					request.getSession(true).setAttribute("secId", (new MD5()).getMD5ofStr(cmfUserId==null?"":cmfUserId));
					logger.info("cmfUserId>>>>"+cmfUserId+",userBaseInfo.getMobile>>>"+userBaseInfo.getMobile()+",userBaseInfo>>>"+userBaseInfo+",userAccoRla>>>>"+userServiceMsg.getUserAccoRlaDto());
				}
			}
			logger.info("userService【queryMeccLogin】结束>>>returnCode:"+returnCode+">>>returnMsg:"+returnMsg
					+">>>timestamp:"+System.currentTimeMillis());
			%>
				<script type="text/javascript">
			<%
			if("0000".equals(returnCode)){
			%>
			     alert("登录成功");
			<%
		    }else if("USR-B004".equals(returnCode)){//登陆账号为空
			%>
			 	alert("登录账号为空");
			<%
		   }else if("USR-A005".equals(returnCode)){//已被锁定
			%>
			  	 alert("用户已被锁定");
			 <%
		   }else if("USR-A007".equals(returnCode)){//未注册
		 	%>
			   alert("对不起，该账号未注册");
 	   		<%
		   }else{
			   %>
			   alert("网络繁忙，请稍后再试!");    
			   <%
		   }
			%>
				window.location.href="/AppService/applicationGroups.shtml";
				</script>
			<%
		 } catch (Exception e) {
			 logger.error("userService【queryMeccLogin】出错了"+e);
			 e.printStackTrace();
		} 
 %>
 

 
 

