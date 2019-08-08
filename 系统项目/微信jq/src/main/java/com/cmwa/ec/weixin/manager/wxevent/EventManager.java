package com.cmwa.ec.weixin.manager.wxevent;

import javax.servlet.http.HttpServletRequest;


/**
 * 微信请求处理接口
 * @author liury
 *
 */
public interface EventManager {

	/**
	 * 处理微信请求
	 * @param request
	 * @return
	 */
	public String handlerRequest(HttpServletRequest request);
	
}
