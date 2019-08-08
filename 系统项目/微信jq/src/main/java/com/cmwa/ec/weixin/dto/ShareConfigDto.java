package com.cmwa.ec.weixin.dto;

import java.io.Serializable;

/**
 * 微信活动-分享配置信息
 * @author ex-chenhq
 *
 */
public class ShareConfigDto extends ActivityBaseDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1558182220562960810L;
	
	/**
	 * 分享配置Id
	 */
	private String configId;
	
	/**
	 * 活动ID
	 */
	private String activityId;
	
	/**
	 * 分享标题
	 */
	private String shareTitle;
	
	/**
	 * 分享图片地址
	 */
	private String shareImgUrl;
	
	/**
	 * 分享描述信息
	 */
	private String shareDesc;
	
	/**
	 * 分享链接
	 */
	private String shareLink;

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareImgUrl() {
		return shareImgUrl;
	}

	public void setShareImgUrl(String shareImgUrl) {
		this.shareImgUrl = shareImgUrl;
	}

	public String getShareDesc() {
		return shareDesc;
	}

	public void setShareDesc(String shareDesc) {
		this.shareDesc = shareDesc;
	}

	public String getShareLink() {
		return shareLink;
	}

	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

	@Override
	public String toString() {
		return "ShareConfigureDto [configId=" + configId
				+ ", activityId=" + activityId + ", shareTitle=" + shareTitle
				+ ", shareImgUrl=" + shareImgUrl + ", shareDesc=" + shareDesc
				+ ", shareLink=" + shareLink + "]";
	}
	

}
