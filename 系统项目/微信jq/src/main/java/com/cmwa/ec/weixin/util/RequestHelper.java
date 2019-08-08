package com.cmwa.ec.weixin.util;

import javax.servlet.http.HttpServletRequest;

import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.weixin.constants.WXConstants;

public class RequestHelper {
	
	/**
	 * 说明：判断请来自于哪个浏览器  
	 * </br>
	 * 来自于微信请求：WEIXIN_CHANNEL ,  其他请求：OTHER_CHANNEL
	 * @param request
	 * @return
	 */
	public static String verfiyIEChannel(HttpServletRequest request){
		String ieType = request.getHeader("user-agent").toLowerCase();
		String channel = null;
		if(ieType.indexOf("micromessenger")<0){//其他浏览器
			channel = WXConstants.OTHER_CHANNEL;
		}else{
			channel = WXConstants.WEIXIN_CHANNEL;
		}
		
		return channel;
	}
	
	/***
	 * 获取IP 新的获取ip方法，以后使用这个
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		/***********************************************************************
		 * 获取客户端地址 开始
		 **********************************************************************/
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
		ip = ip == null?"":ip;
		return ip;
	}
	
	/***
	 * 获取IP 
	 * 原有获取ip地址的方法，但有些时候无法获取到ip值，用新方法
	 * 仅作为备份使用
	 * @param request
	 * @return
	 */
	public static String getIpAddr_old(HttpServletRequest request) {
		/***********************************************************************
		 * 获取客户端地址 开始
		 **********************************************************************/
		String serverName = request.getServerName();
		String serverPort = "";
		String serverNamePort = "";

		String chalid = "";

		int iServerPort = request.getServerPort();

		if (serverName != null)
			serverName = serverName.toLowerCase();

		serverPort = String.valueOf(iServerPort);

		serverNamePort = serverName + ":" + serverPort;

		/**
		 * 以下为获取http头中的ip信息 Added by zhanxb 2007-4-6
		 */
		StringBuffer sbWlClientIp = new StringBuffer(); // WL-Proxy-Client-IP
		StringBuffer sbClientContent = new StringBuffer();

		sbWlClientIp.append(request.getHeader("WL-Proxy-Client-IP")); // WL-Proxy-Client-IP

		sbClientContent.append(sbWlClientIp);

		chalid = sbClientContent.toString();
		if(chalid == null || chalid.equals("null")){
			chalid = "";
		}
		
		return chalid;
	}
	
	/**
	 * 判断是否设置支付密码
	 * @param request
	 * @return
	 * 			hasSetTradePassword：Y-已设置支付密码；N-未设置支付密码
	 * @author maj
	 */
	public static String checkHasSetTPsw(HttpServletRequest request){
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		String lPassWord = "";
		String tPassWord = "";
		if(userBaseInfoDto != null){
			lPassWord = userBaseInfoDto.getLPassword();
			tPassWord = userBaseInfoDto.getTPassword();
		}
		String hasSetTradePassword = "N";
		if(lPassWord != null && tPassWord != null && !lPassWord.equals(tPassWord)){
			hasSetTradePassword = "Y";
		}
		return hasSetTradePassword;
	}
	/***
	 * 获取IP
	 * @param request
	 * @return
	 */
	public static String getRemoteAddress(HttpServletRequest request) {
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
	/**
	 * 获取保存在session中Userbaseinfo的cmfUserId
	 * @return
	 * 			String
	 * @author maj
	 */
	public static String getSessionCmfUserId(HttpServletRequest request){
		Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		return obj != null ? ((UserBaseInfoDto) obj).getCmfUserId() : "";
	}

	/**
	 * 获取保存在session中Userbaseinfo
	 * 
	 * @return String
	 * @author luos
	 */
	public static UserBaseInfoDto getSessionUserBaseInfo(HttpServletRequest request) {
		Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		return obj != null ? (UserBaseInfoDto) obj : null;
	}
	/**
	 * 获取保存在session中UserAccoRla
	 * 
	 * @return String
	 * @author luos
	 */
	public static UserAccoRlaDto getSessionUserAccoRla(HttpServletRequest request) {
		Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		return obj != null ? (UserAccoRlaDto) obj : null;
	}
	/**
	 * 获取保存在session中UserAccoRla的ecCustNo
	 * 
	 * @return String
	 * @author luos
	 */
	public static String getSessionCustNo(HttpServletRequest request) {
		Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		return obj != null ? ((UserAccoRlaDto) obj).getEcCustNo() : "";
	}
	/**
	 * 获取sessionId
	 * if(session == null) 获取当前时间戳
	 * else 获取sessionId
	 * @param request
	 * @return
	 * 			String
	 * @author maj
	 */
	public static String getSeqId(HttpServletRequest request){
		String seqId = "";
		if(request.getSession(false) == null){
			seqId = Long.toString(System.currentTimeMillis());
		}else{
			seqId = request.getSession(false).getId();
		}
		return seqId;
	}
}
