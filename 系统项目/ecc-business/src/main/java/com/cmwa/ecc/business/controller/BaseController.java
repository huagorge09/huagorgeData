package com.cmwa.ecc.business.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;


public abstract class BaseController {
	
	protected Map<String, String> getParams(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		try{
			Enumeration<String> keys = request.getParameterNames();
			if(keys==null){
				return params;
			}
			while(keys.hasMoreElements()){
				String key = keys.nextElement();
				String value = new String(((String)request.getParameter(key)).getBytes("ISO-8859-1"), "UTF-8");
				if(StringUtils.isNotEmpty(value)){
					params.put(key, value);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return params;
	}
	
	protected void paramModel(HttpServletRequest request,ModelMap model) {
		try{
			Enumeration<String> keys = request.getParameterNames();
			if(keys==null){
				return;
			}
			while(keys.hasMoreElements()){
				String key = keys.nextElement();
				String value = new String(((String)request.getParameter(key)).getBytes("ISO-8859-1"), "UTF-8");
				if(StringUtils.isNotEmpty(value)){
					model.addAttribute(key, value);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 重定向到成功页面
	 * @return
	 */
	protected String redirectSuccess() {
		return "jsp/hint/success";
	}
	
	/**
	 * 执行失败，跳转到失败页面
	 * @return
	 */
	protected String redirectExecFaild() {
		return "jsp/hint/validateFailed";
	}
	
	protected String redirectCustom() {
		return "custom";
	}
	
	protected String redirectLogin() {
		return "redirect:loginView.action";
	}
	
	public static InternetAddress[] _strToAdr(String[] arr) throws AddressException {
		if(arr == null || arr.length == 0){
			return new InternetAddress [0];
		}
		/*InternetAddress[] iasTo = new InternetAddress [arr.length];
		int index = 0, length = arr.length;
		for(; index < length; index++){
			iasTo[index] = new InternetAddress(arr[index]);
		}*/
		
		List<InternetAddress> iasToList = new ArrayList<InternetAddress>();
		for (int i = 0; i < arr.length; i++) {
			String intAdd = arr[i];
			if(!StringUtils.isEmpty(intAdd)){
				iasToList.add(new InternetAddress(intAdd));
			}
		}
		InternetAddress[] iasTo = new InternetAddress[iasToList.size()];
		try{
			iasTo = iasToList.toArray(iasTo);
		}catch(Exception e){
			System.out.println("----_strToAdr-Exception:"+ e.getMessage());
		}
		return null == iasTo ? new InternetAddress[0] : iasTo;
	}
}
