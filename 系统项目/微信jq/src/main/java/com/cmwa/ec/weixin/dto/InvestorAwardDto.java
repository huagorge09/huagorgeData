package com.cmwa.ec.weixin.dto;

import java.util.Date;

/**
 * 投资者教育活动
 * 用户抽奖信息
 * @author ex-wangz2
 *
 */
public class InvestorAwardDto {

	private String openId;
	private Date createDate;
	private String status;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "InvestorAwardDto [openId=" + openId + ", createDate=" + createDate + ", status=" + status + "]";
	}
	
}
