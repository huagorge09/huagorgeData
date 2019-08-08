package com.cmwa.ec.weixin.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.weixin.client.UserServiceClient;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dto.CmwaWxUserInfoDto;
import com.cmwa.ec.weixin.dto.UserInfoDto;
import com.cmwa.ec.weixin.dto.UserInfoexDto;
import com.cmwa.ec.weixin.manager.business.UserInfoManager;
import com.cmwa.ec.weixin.manager.business.UserInfoexManager;
import com.cmwa.ec.weixin.util.ContextUtils;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.SpringContextUtil;
import com.cmwa.ec.weixin.util.WeixinUtil;

import net.sf.json.JSONObject;

public class VerifyUserInfoFilter implements Filter {

	private static Logger logger = Logger.getLogger(VerifyUserInfoFilter.class.getName());
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		
		String method = request.getMethod();
		
		HttpSession session = request.getSession(true);
		Object obj = session.getAttribute(SessionValue.SESSION_USERBASEINFO);
		
		if(obj==null){
			
			String requestPath = request.getRequestURI();//格式如http://59.37.11.115/WeixinService/query/saleList.jsp?item=all
    		String tempParam = request.getQueryString();
    		logger.info("请求param："+tempParam);
    		if(tempParam!=null && !"".equals(tempParam)){
    			requestPath += "?"+tempParam;
    		}
			if(!requestPath.contains("xhtml")){
    			//登录后跳转地址
    			session.setAttribute(SessionValue.SESSION_REQUESTPATH, requestPath);
    		}
			
			String channel = (String)session.getAttribute(SessionValue.SESSION_CHANNEL);
			//otherIE
			if(WXConstants.OTHER_CHANNEL.equals(channel)){
	    		//未登录调转到登录页面
				goToLogin(request, response);
			}else{
				//微信IE
				String openId = (String)session.getAttribute(SessionValue.SESSION_OPENID);
				
				//验证用户是否关注微信
				if (isSubscribe(openId)) {
					
					String cmfUserId = queryCmfUserIdByOpenId(openId);
					//判断用户是否绑定微信
					if(null!=cmfUserId && !"".equals(cmfUserId)){
						
						UserBaseInfoDto userBaseInfo = null;
						UserAccoRlaDto userAcco =  null;
						UserServiceMessage userServiceMessage = queryUserInfoByCmfUserId(cmfUserId, request);
						
						if(null!=userServiceMessage && "0000".equals(userServiceMessage.getReturnCode())){
							userBaseInfo = userServiceMessage.getUserBaseInfoDto();
							userAcco = userServiceMessage.getUserAccoRlaDto();
						}
						
						if(userBaseInfo!=null){
							request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfo);
							request.getSession(true).setAttribute(SessionValue.SESSION_CMFUSERID, userBaseInfo.getCmfUserId());
						}
						if(userAcco!=null){
							logger.info("VerifyUserInfoFilter,if{userAcco!=null},fffffffffffffk,根据cmfUserId获取userAcco,cmfUserId="+cmfUserId+",custNo="+userAcco.getEcCustNo());
							request.getSession(true).setAttribute(SessionValue.SESSION_USERACCORLA, userAcco);
						}else{
							logger.info("VerifyUserInfoFilter,if{userAcco=null},fffffffffffffk,根据cmfUserId获取的userAcco为空,cmfUserId="+cmfUserId);
						}
						
						chain.doFilter(request, res);
						return;
						
					}else{
						//未绑定微信，调转到微信登录页面
						JSONObject returnJsonObject = new JSONObject();
						returnJsonObject.put("cmfUserIdIsNull_F", "yes");
						returnJsonObject.put("channel_F", "WXIE");
						String url = WeixinUtil.getBindAccUrl();
						errorSkip(response, method, returnJsonObject, url);
						return ;
					}
				}else{
					//未关注微信，跳转到提示关注微信公众号页面
					logger.info("VerifyUserInfoFilter,微信浏览器发起请求，未关注微信，跳转到hintAttentionWeixin.shtml");
					JSONObject returnJsonObject = new JSONObject();
					returnJsonObject.put("noAttention_F", "yes");
					returnJsonObject.put("channel_F", "WXIE");
					String url = "/WeixinService/public/hintAttentionWeixin.shtml";
					errorSkip(response, method, returnJsonObject, url);
					return ;
				}
				
			}
			
		}else{
			chain.doFilter(request, res);
			return;
		}
		
	}

	
	/**
	 * 获取openId出错或openId获取为空， 将session中的channel改为OTHERIE，跳转到其他ie登陆页面
	 * @param request
	 * @param response
	 */
	private void goToLogin(HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession(true);
		String method = request.getMethod();
		session.setAttribute(SessionValue.SESSION_CHANNEL, WXConstants.OTHER_CHANNEL);
		try {
			//如果是POST过来，则是Ajax异步请求
			if (method.equalsIgnoreCase("POST")) {
				JSONObject returnJsonObject = new JSONObject();
				returnJsonObject.put("cmfUserIdIsNull_F", "yes");
				returnJsonObject.put("channel_F", "otherIE");
				response.getOutputStream().print(returnJsonObject.toString());
			}else{
				//网址输入或者页面url跳转 跳转到登陆页面 
				response.sendRedirect("/WeixinService/otherIELogin/otherIELogin.shtml");
			}
		} catch (Exception e) {
			logger.error("VerifyUserInfoFilter,goToLogin方法抛出异常");
		}
		
	}
	
	/**
	 * 说明：请求跳转判断，若是Ajax post请求，则根据返回相应的json数据，如果是get请求，则重定向到相应页面
	 * @param response
	 * @param method 请求方式    GET,POST	
	 * @param returnJsonObject Ajax（post）请求 返回的json值
	 * @param url get请求重定向的url
	 * @throws Exception
	 */
	private void errorSkip(HttpServletResponse response,String method,JSONObject returnJsonObject,String url){
		try {
			// 如果是POST过来，则是Ajax异步请求
			if (method.equalsIgnoreCase("POST")) {
				response.getOutputStream().print(returnJsonObject.toString());
			}else{//网址输入或者页面url跳转
				response.sendRedirect(url);
			}
		} catch (IOException e) {
			logger.error("VerifyUserInfoFilter,errorSkip方法抛出异常");
		}
	}
	
	//查询是否关注
	private boolean isSubscribe(String openid){
		UserInfoManager userInfoManager=(UserInfoManager)SpringContextUtil.getBean("userInfoManager");
		CmwaWxUserInfoDto dto = userInfoManager.queryIsSubscribeByOpenIdOnCmwaWxUserInfo(openid);
		if(dto != null) {
			if("1".equals(dto.getSubscribe())){
				return true;
			} else if ("0".equals(dto.getSubscribe())) {
				return false;
			} else {
				UserInfoDto userInfoDto = userInfoManager.queryByOpenIdAndStatus(openid, "1");
				if(userInfoDto != null) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			UserInfoDto userInfoDto = userInfoManager.queryByOpenIdAndStatus(openid, "1");
			if(userInfoDto!=null){
				return true;
			} 
			return false;
		}
	}
	 
	/**
	 * 通过openId获取cmfUserId 判断用户是否绑定微信（即是否注册）
	 * @param openId
	 * @return
	 */
	public String queryCmfUserIdByOpenId(String openId){
		
		UserInfoexManager userInfoexManager=(UserInfoexManager)SpringContextUtil.getBean("userInfoexManager");
		UserInfoexDto dto = userInfoexManager.queryUserinfoexQueryRelation(openId,WXConstants.USERINFOEX_BINDSTAT_R);
		String cmfUserId = null;
		if(dto!=null){
			cmfUserId = dto.getCmfuserid();		
		}
		
		return cmfUserId;
	}
	
	
	/**
	 * 通过cmfUserId获取用户基本信息
	 * @param cmfUserId
	 * @param request
	 * @return
	 */
	public UserServiceMessage queryUserInfoByCmfUserId(String cmfUserId,HttpServletRequest request){
		UserServiceClient userClient = (UserServiceClient)SpringContextUtil.getBean("userServiceClient");
		String seqId = request.getSession().getId();
		Context context=ContextUtils.setContext(WXConstants.USER_SERVICE_001,WXConstants.SERVICE_CHANNEL_WEIXIN,"", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		UserServiceMessage userServiceMessage = userClient.queryUserAndAccoRlaById(context, cmfUserId);
		return userServiceMessage;
	}
		
	/**
	 * 自动登录，用于不需要登录的页面，进入页面自动登录
	 * 仅限于微信端
	 * @param openId
	 * @param request
	 * 			void
	 * @author maj
	 */
	public void autoLogin(HttpServletRequest request) {
		// 获取访问的浏览器来源
		String channel = (String) request.getSession(true).getAttribute(SessionValue.SESSION_CHANNEL);
		
		// 如果不是其他浏览器    即微信浏览器   则去获取openid
		if (!WXConstants.OTHER_CHANNEL.equals(channel)) {

			String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
			if (openId != null && !openId.equals("")) {
				// 通过openid获取cmfUserId
				String cmfUserId = queryCmfUserIdByOpenId(openId);
				// 判断用户是否绑定微信
				if (null != cmfUserId && !"".equals(cmfUserId)) {

					UserBaseInfoDto userBaseInfo = null;
					UserAccoRlaDto userAcco = null;
					UserServiceMessage userServiceMessage = queryUserInfoByCmfUserId(cmfUserId, request);

					if (null != userServiceMessage && "0000".equals(userServiceMessage.getReturnCode())) {
						userBaseInfo = userServiceMessage.getUserBaseInfoDto();
						userAcco = userServiceMessage.getUserAccoRlaDto();
					}

					if (userBaseInfo != null) {
						request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfo);
						request.getSession(true).setAttribute(SessionValue.SESSION_CMFUSERID, userBaseInfo.getCmfUserId());
					}
					if (userAcco != null) {
						logger.info("VerifyUserInfoFilter,if{userAcco!=null},fffffffffffffk,根据cmfUserId获取userAcco,cmfUserId=" + cmfUserId + ",custNo=" + userAcco.getEcCustNo());
						request.getSession(true).setAttribute(SessionValue.SESSION_USERACCORLA, userAcco);
					} else {
						logger.info("VerifyUserInfoFilter,if{userAcco=null},fffffffffffffk,根据cmfUserId获取的userAcco为空,cmfUserId=" + cmfUserId);
					}
				}
			}
		}
	}
		
	@Override
	public void destroy() {

	}

}
