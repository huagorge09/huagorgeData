package com.cmwa.ec.weixin.dto;

import java.io.Serializable;

public class CmwaWxUserInfoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String openid;
	
	private String subscribe;
	
	//¸ñÊ½Îª yyyy-MM-dd HH:mm:ss
	private String subscribeTime;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public String getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(String subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	
	public CmwaWxUserInfoDto() {
		
	}

	public CmwaWxUserInfoDto(String openid, String subscribe,
			String subscribeTime) {
		super();
		this.openid = openid;
		this.subscribe = subscribe;
		this.subscribeTime = subscribeTime;
	} 

}
