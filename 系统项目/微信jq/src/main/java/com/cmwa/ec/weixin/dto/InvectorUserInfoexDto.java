package com.cmwa.ec.weixin.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 *  投资者教育活动
 *  用户扩展信息
 * @author ex-tuoy
 *
 */
public class InvectorUserInfoexDto {

	private String subscribe;
	private String headImgurl; 
	private String openId;
	private String nickName;
	private String subscribeChannel;
	private String custserid;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
	private Date subscribeTime;
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}
	public String getHeadImgurl() {
		return headImgurl;
	}
	public void setHeadImgurl(String headImgurl) {
		this.headImgurl = headImgurl;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getSubscribeChannel() {
		return subscribeChannel;
	}
	public void setSubscribeChannel(String subscribeChannel) {
		this.subscribeChannel = subscribeChannel;
	}
	public Date getSubscribeTime() {
		return subscribeTime;
	}
	public void setSubscribeTime(Date subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	
	public String getCustserid() {
		return custserid;
	}
	public void setCustserid(String custserid) {
		this.custserid = custserid;
	}
	@Override
	public String toString() {
		return "InvectorUserInfoexDto [subscribe=" + subscribe
				+ ", headImgurl=" + headImgurl + ", openId=" + openId
				+ ", nickName=" + nickName + ", subscribeChannel="
				+ subscribeChannel + ", custserid=" + custserid
				+ ", subscribeTime=" + subscribeTime + "]";
	}
}
