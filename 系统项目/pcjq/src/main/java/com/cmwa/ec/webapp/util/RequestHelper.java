package com.cmwa.ec.webapp.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;

import java.io.IOException;
import java.util.Enumeration;

/**
 * <p>Title: RequestHelper</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004-2008</p>
 * <p>Company: 览众科技</p>
 * @author 毛盾
 * @version 1.0
 * @CreateTime 2014-03-03 17:03
 *
 */
public final class RequestHelper {
	/**
	 * 从表单中提取字符串
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param fieldName
	 *            表单字段名称
	 * @return 字符串，如果为NULL返回空字符
	 */
	public static String getString(HttpServletRequest request, String fieldName) {
		String value = request.getParameter(fieldName);
		if (value != null && value.length() > 0) {
			value = StringHelper.getFilterStr(value);
		}
		return value == null ? "" : value;
	}

	/**
	 * 从表单中提取字符串
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param fieldName
	 *            表单字段名称
	 * @param defaultValue
	 *            缺省值
	 * @return
	 */
	public static String getString(HttpServletRequest request,
			String fieldName, String defaultValue) {
		String value = request.getParameter(fieldName);
		if (value != null && value.length() > 0) {
			value = StringHelper.getFilterStr(value);
		}
		return value == null ? defaultValue : value;
	}

	/**
	 * 从表单是提取数值，如果不存在，返回0
	 * 
	 * @param request
	 * @param fieldName
	 *            表单名称
	 * @return 数值，如果不存在，返回0
	 */
	public static int getInt(HttpServletRequest request, String fieldName) {
		String value = getString(request, fieldName);
		if (StringHelper.isEmpty(value)) {
			return 0;
		}
		try {
			return new Integer(value).intValue();
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 从表单是提取数值，如果不存在，返回0
	 * 
	 * @param request
	 * @param fieldName
	 *            表单名称
	 * @param defaultValue
	 *            缺省值
	 * @return 数值，如果不存在，返回缺省值
	 */
	public static int getInt(HttpServletRequest request, String fieldName,
			int defaultValue) {
		int value = getInt(request, fieldName);
		if (value == 0) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * 从表单是提取数值，如果不存在，返回0
	 * 
	 * @param request
	 * @param fieldName
	 *            表单名称
	 * @return 数值，如果不存在，返回0
	 */
	public static long getLong(HttpServletRequest request, String fieldName) {
		String value = getString(request, fieldName);
		if (StringHelper.isEmpty(value)) {
			return 0;
		}
		try {
			return new Long(value).longValue();
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 从表单是提取数值，如果不存在，返回0
	 * 
	 * @param request
	 * @param fieldName
	 *            表单名称
	 * @param defaultValue
	 *            缺省值
	 * @return 数值，如果不存在，返回缺省值
	 */
	public static long getLong(HttpServletRequest request, String fieldName,
			long defaultValue) {
		long value = getLong(request, fieldName);
		if (value == 0) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * 从HttpServletRequest中提取属性值
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param attributeName
	 *            属性名称
	 * @return
	 */
	public static String getStrAttribute(HttpServletRequest request,
			String attributeName) {
		String value = (String) request.getAttribute(attributeName);
		return value == null ? "" : value;
	}

	/**
	 * 从HttpServletRequest中提取属性值
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param attributeName
	 *            属性名称
	 * @param defaultValue
	 *            缺省值
	 * @return
	 */
	public static String getStrAttribute(HttpServletRequest request,
			String attributeName, String defaultValue) {
		String value = (String) request.getAttribute(attributeName);
		return value == null ? defaultValue : value;
	}

	/**
	 * 从HttpServletRequest中提取属性值
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param attributeName
	 *            属性名称
	 * @return
	 */
	public static int getIntAttribute(HttpServletRequest request,
			String attributeName) {
		String value = getStrAttribute(request, attributeName);
		if (StringHelper.isEmpty(value)) {
			return 0;
		}
		try {
			return new Integer(value).intValue();
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 从HttpServletRequest中提取属性值
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param attributeName
	 *            属性名称
	 * @param defaultValue
	 *            缺省值
	 * @return
	 */
	public static int getIntAttribute(HttpServletRequest request,
			String attributeName, int defaultValue) {
		int value = getIntAttribute(request, attributeName);
		if (value == 0) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * 向HttpServletRequest中设置属性值
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param attributeName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public static void setIntAttribute(HttpServletRequest request,
			String attributeName, int value) {
		request.setAttribute(attributeName, new Integer(value));
	}

	/**
	 * 向HttpServletRequest中设置属性值
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param attributeName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public static void setStrAttribute(HttpServletRequest request,
			String attributeName, String value) {
		request.setAttribute(attributeName, value);
	}

	/**
	 * 提取字符串数组
	 * 
	 * @param request
	 * @param fieldName
	 *            字段名称
	 * @return 字符串数组
	 */
	public static String[] getStringArray(HttpServletRequest request,
			String fieldName) {
		String[] valueArray = request.getParameterValues(fieldName);
		if (valueArray != null && valueArray.length > 0) {
			for (int i = 0; i < valueArray.length; i++) {
				String value = valueArray[i];
				if (value != null && value.length() > 0) {
					valueArray[i] = StringHelper.getFilterStr(value);
				}
			}
		}
		return valueArray;
	}

	/**
	 * 提取数字数组
	 * 
	 * @param request
	 * @param fieldName
	 *            字段名
	 * @return 数字数组
	 */
	public static int[] getIntArray(HttpServletRequest request, String fieldName) {
		String[] array = RequestHelper.getStringArray(request, fieldName);
		if (array == null || array.length == 0) {
			return null;
		}
		int[] value = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			value[i] = Integer.parseInt(array[i]);
		}
		return value;
	}

	/**
	 * 转发请求.
	 * 
	 * @param request
	 *            HTTP请求.
	 * @param response
	 *            HTTP响应.
	 * @param url
	 *            需转发到的URL.
	 */
	public static void dispatchRequest(HttpServletRequest request,
			HttpServletResponse response, String url) throws IOException,
			ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response);
		rd = null;
	}

	/**
	 * 判断是否是提交回来
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isPostBack(HttpServletRequest request) {
		String method = request.getMethod();
		if ("POST".equalsIgnoreCase(method)) {
			return true;
		}
		return false;
	}

	/**
	 * 删除request中的所有attribute值
	 * 
	 * @param request
	 */
	public static void removeAllAttribute(HttpServletRequest request) {
		Enumeration enumeration = request.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			request.removeAttribute(name);
		}
	}

	public static Object getAttribute(HttpServletRequest request, String attributeName) {
		Object value = (Object) request.getAttribute(attributeName);
		return value == null ? null : value;
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
	
	/**
	 * 获取session中的属性
	 * session == null  return ""
	 * 
	 * @param request
	 * @param str
	 * @return
	 * 			String
	 * @author maj
	 */
	public static String getSessionAttr(HttpServletRequest request,String str){
		String value = "";
		if(request.getSession(false) == null || request.getSession(false).getAttribute(str) == null){
			value = "";
		}else{
			value = (String) request.getSession(false).getAttribute(str);
		}
		return value;
	}
	
	/**
	 * 获取session中的对象并返回为Object
	 * @param request
	 * @param str sessionName
	 * @return Object
	 * @author maj
	 */
	public static Object getSessionAttribute(HttpServletRequest request,String str){
		Object obj = request.getSession(true).getAttribute(str);
		return obj == null ? null : obj;
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
	 * 根据用户信息判断用户是否设置支付密码
	 * 
	 * @param userInfo
	 * @return 未设置N   已设置Y
	 */
	public static String getTPsdStatus(UserBaseInfoDto userInfo) {
		String lPassword = userInfo.getLPassword() == null ? "" : userInfo.getLPassword();
		String tPassword = userInfo.getTPassword() == null ? "" : userInfo.getTPassword();
		String status = "N";
		if (!"".equals(lPassword) && !lPassword.equals(tPassword)) {
			status = "Y";
		}
		return status;
	}
	/*
	 * 格式化身份证
	 */
	public static String getEncryptIdNo(String idNo){
		String str="";
		if(idNo!=null && !"".equals(idNo) && idNo.length() > 8){
			str = idNo.replace(idNo.substring(4,idNo.length()-4), "**********");
		}
		return str;
	}
	/*
	 * 格式化银行卡
	 */
	public static String getEncryptBankCard(String bankCard){
		String str="";
		if(bankCard!=null && !"".equals(bankCard) && bankCard.length() > 8){
			str = bankCard.trim().replace(bankCard.substring(4,bankCard.length()-4), " **** **** ");
		}
		return str;
	}
	/*
	 * 格式化手机号
	 */
	public static String getEncryptMobile(String mobile){
		String str="";
		if(mobile!=null && !"".equals(mobile) && mobile.length() > 7){
			str = mobile.trim().replace(mobile.substring(3,mobile.length()-4), "****");
		}
		return str;
	}
	/*
	 * 格式化用户名
	 */
	public static String getEncryptUserName(String name){
		String str="";
		String replaceStr ="";
		if(name!=null && !"".equals(name)){
			for (int i = 1; i < name.length(); i++) {
				replaceStr += "*";
			}
			str = name.trim().replace(name.substring(1,name.length()), replaceStr);
		}
		return str;
	}
	/*
	 * 格式化机构用户名称
	 */
	public static String getEncryptPublicUserName(String name){
		String str="";
		String replaceStr ="***";
		if(name!=null && !"".equals(name)){
			if(name.length() > 5) {
				str = name.trim().replace(name.substring(4,name.length()), replaceStr);
			} else {
				str = name.trim().replace(name.substring(2,name.length()), replaceStr);
			}
		}
		return str;
	}
}
