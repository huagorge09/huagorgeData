package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;

/**
 * 用户登录记录实体类
 * @author ex-chenbq
 *
 */
@Alias("loginErrorVo")
public class LoginErrorVo {

	/**
	 * 主键序列号
	 */
	private String serialNo;
	
	/**
	 * 登录ID
	 */
	private String loginId;
	
	/**
	 * 最后登录时间
	 */
	private String loginDate;
	
	/**
	 * 登录错误次数
	 */
	private int loginErrorCount;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	public int getLoginErrorCount() {
		return loginErrorCount;
	}

	public void setLoginErrorCount(int loginErrorCount) {
		this.loginErrorCount = loginErrorCount;
	}
}
