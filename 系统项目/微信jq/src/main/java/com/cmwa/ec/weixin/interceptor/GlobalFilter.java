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

import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.util.MeccReqUrlList;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.StringUtils;

import net.sf.json.JSONObject;

/**
 * 全局过滤器，将请求渠道放入request请求中
 *
 * @author liury 20160326
 *
 */
public class GlobalFilter implements Filter {
	
	private static Logger logger = Logger.getLogger(GlobalFilter.class.getName());

	@Override
	public void destroy() {
		logger.debug("GlobalFilter destroy");
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws ServletException, IOException {
		HttpServletRequest request=(HttpServletRequest)req;
		HttpServletResponse response=(HttpServletResponse)res;
		HttpSession session = request.getSession(true);
		
		
		//验证是否是微信浏览器发起请求
		{
			String ieType = request.getHeader("user-agent").toLowerCase();
			String channel = WXConstants.WEIXIN_CHANNEL;//默认为微信浏览器浏览
			if(ieType.indexOf("micromessenger")<0){//其他浏览器
				channel = WXConstants.OTHER_CHANNEL;
			}
			logger.info("GlobalFilter--浏览渠道：channel="+channel);
			session.setAttribute(SessionValue.SESSION_CHANNEL, channel);
		}
		
		
		
		
		//通过mecc登录的，验证访问权限
		{
			String reqChannel = (String)request.getSession(true).getAttribute(SessionValue.SESSION_REQCHANNEL);
			if(StringUtils.isEmptyString(reqChannel)){
				logger.info("GlobalFilter--登录渠道：reqChannel = null ");
			}
			if(reqChannel!=null && reqChannel.equals("MECC")){
				boolean bool = meccReq(request, response, chain);
				if(!bool){
					return ;
				}
			}
		}
		
		chain.doFilter(request, response);
	}
	
	
	/**
	 * mecc请求处理，验证当前请求页面是否是MECC不可访问页面
	 */
	public boolean meccReq(HttpServletRequest request,HttpServletResponse response,FilterChain chain)
			 throws ServletException, IOException {
		
		String method = request.getMethod();
		String uri = request.getRequestURI();
		
		if(MeccReqUrlList.getList().contains(uri)){
			
			logger.info("登录来源：mecc，页面访问权限不足，请求地址："+request.getRequestURI());

			JSONObject returnJsonObject = new JSONObject();
			returnJsonObject.put("signError_F", "yes");
			returnJsonObject.put("channel_F", "MECC");
			String url = "/WeixinService/public/signError.shtml";
			
			errorSkip(response, method, returnJsonObject, url);
			return false;
		}else{
			for (String str : MeccReqUrlList.getList()) {
				if(uri.contains(str)){
					return false;
				}
			}
		}
		return true;
		
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
			logger.error(">>GlobalFilter,errorSkip方法抛出异常",e);
		}
	}
	
	

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.debug("GlobalFilter init");
	}

}
