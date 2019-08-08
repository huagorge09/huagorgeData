package com.cmwa.ec.weixin.model;

import java.util.Map;

public class MobileMessagePushModel {

	private String content;
	
	private String mobile;
	
	private Map<String,String> replaceParam;

	private String idcard;
	
	private String openid;
	
	private String awardType;
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the replaceParam
	 */
	public Map<String, String> getReplaceParam() {
		return replaceParam;
	}

	/**
	 * @param replaceParam the replaceParam to set
	 */
	public void setReplaceParam(Map<String, String> replaceParam) {
		this.replaceParam = replaceParam;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MobileMessagePushModel [content=" + content + ", mobile="
				+ mobile + ", replaceParam=" + replaceParam + "]";
	}

	public MobileMessagePushModel(String idcard,String openid,String content, String mobile,
			Map<String, String> replaceParam, String awardType) {
		super();
		this.content = content;
		this.mobile = mobile;
		this.replaceParam = replaceParam;
		this.idcard = idcard;
		this.openid = openid;
		this.awardType = awardType;
	}
	
	public MobileMessagePushModel(){}

	/**
	 * @return the idcard
	 */
	public String getIdcard() {
		return idcard;
	}

	/**
	 * @param idcard the idcard to set
	 */
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	/**
	 * @return the openid
	 */
	public String getOpenid() {
		return openid;
	}

	/**
	 * @param openid the openid to set
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}

	/**
	 * @return the awardType
	 */
	public String getAwardType() {
		return awardType;
	}

	/**
	 * @param awardType the awardType to set
	 */
	public void setAwardType(String awardType) {
		this.awardType = awardType;
	}
	
}
