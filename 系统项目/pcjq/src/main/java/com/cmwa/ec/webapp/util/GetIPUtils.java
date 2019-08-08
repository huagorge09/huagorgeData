package com.cmwa.ec.webapp.util;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Title: GetIPUtils
 * </p>
 * <p>
 * Description: IP地址通用工具.
 * </p>
 * @author niedc
 * @version 1.0
 * @Create date: 2014-12-23
 * 
 */

public class GetIPUtils {

	
	/***
	 * 获取IP
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		/***********************************************************************
		 * 获取客户端地址 开始
		 **********************************************************************/
//		String serverName = request.getServerName();
//		String serverPort = "";
//		String serverNamePort = "";
//
//		String chalid = "";
//
//		int iServerPort = request.getServerPort();
//
//		if (serverName != null){
//			serverName = serverName.toLowerCase();
//		}
//
//		serverPort = String.valueOf(iServerPort);
//
//		serverNamePort = serverName + ":" + serverPort;
//
//		/**
//		 * 以下为获取http头中的ip信息 Added by zhanxb 2007-4-6
//		 */
//		StringBuffer sbWlClientIp = new StringBuffer(); // WL-Proxy-Client-IP
//		// StringBuffer sbClientIp = new StringBuffer(); // Proxy-Client-IP
//		// StringBuffer sbXForwardedFor = new StringBuffer(); // X-Forwarded-For
//		// StringBuffer sbRemoteAddr = new StringBuffer(); // getRemoteAddr
//		StringBuffer sbClientContent = new StringBuffer();
//
//		sbWlClientIp.append(request.getHeader("WL-Proxy-Client-IP")); // WL-Proxy-Client-IP
//
//		sbClientContent.append(sbWlClientIp);
//
//		chalid = sbClientContent.toString();
//		return chalid==null?"":chalid;
		String ip = request.getHeader("x-forwarded-for");  
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
			ip = request.getHeader("Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
			ip = request.getHeader("WL-Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
		    ip = request.getRemoteAddr();  
		}  
		return ip;
	}
	
}
