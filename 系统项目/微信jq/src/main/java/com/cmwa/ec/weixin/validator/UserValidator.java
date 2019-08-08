package com.cmwa.ec.weixin.validator;

import org.apache.commons.lang.StringUtils;

/**
 * 用户相关信息校验器
 * @author caolp
 * 
 */
public class UserValidator {
	
	/**
	 * 校验当前登录账号手机号码与修改登录密码手机号是否一致
	 * @param currentLoginMobile 当前登录手机号 
	 * @param modifyMobile		 修改密码的手机号
	 * @return 一致返回true；否则返回false
	 */
	public static boolean isMatchMobileWithResetLoginPwd(String currentLoginMobile,String modifyMobile) {
		
		if (StringUtils.isNotBlank(currentLoginMobile)) {//用户已经登录
			return currentLoginMobile.equals(modifyMobile);
		}
		
		//用户未登录：则直接跳过该校验
		return true;
	}
	
	/**
	 * 提交修改密码的手机号是否为通过短信验证码的手机号 
	 * @param resetPwdMobile  重置密码手机号
	 * @param smsVerifyMobile 短信验证码验证手机号
	 * @return 一致返回true;否则返回false
	 */
	public static boolean isPassSmsVerify(String resetPwdMobile,String smsVerifyMobile) {
		if (StringUtils.isBlank(smsVerifyMobile)) {
			return false;
		}
		
		if (!smsVerifyMobile.equals(resetPwdMobile)) {
			return false;
		}
		
		return true;
	}
}
