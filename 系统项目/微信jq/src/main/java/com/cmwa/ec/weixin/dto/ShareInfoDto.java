package com.cmwa.ec.weixin.dto;

import java.io.Serializable;

/**
 * 微信活动-用户分享关系纪录
 * @author ex-chenhq
 *
 */
public class ShareInfoDto extends ActivityBaseDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3837972508998377169L;
	
	/**
	 * 记录Id
	 */
	private String shareId;
	/**
	 * 活动编码
	 */
	private String activityId;
	/**
	 * 分享者openId
	 */
	private String fromOpenid;
	/**
	 * 被分享者openId
	 */
	private String openId;
	
	/**
	 * 分享者昵称
	 */
	private String nickName;
	
	/**
	 * 被分享者昵称
	 */
	private String fromNickName;
	
	public String getShareId() {
		return shareId;
	}
	public void setShareId(String shareId) {
		this.shareId = shareId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	public String getFromOpenid() {
		return fromOpenid;
	}
	public void setFromOpenid(String fromOpenid) {
		this.fromOpenid = fromOpenid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getFromNickName() {
		return fromNickName;
	}
	public void setFromNickName(String fromNickName) {
		this.fromNickName = fromNickName;
	}
	@Override
	public String toString() {
		return "ShareInfoDto [shareId=" + shareId + ", activityId="
				+ activityId + ", fromOpenid=" + fromOpenid + ", openId="
				+ openId + "]";
	}
	
}
