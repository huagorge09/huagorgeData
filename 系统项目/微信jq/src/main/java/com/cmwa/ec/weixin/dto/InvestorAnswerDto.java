package com.cmwa.ec.weixin.dto;

import java.util.Date;

/**
 * 投资者教育活动
 * 用户答题信息
 * @author ex-wangz2
 *
 */
public class InvestorAnswerDto {

	private String openId;
	private Integer score;
	private Date firstDate;
	private Date lastDate;
	private Integer count;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Date getFirstDate() {
		return firstDate;
	}
	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "InvestorAnswerDto [openId=" + openId + ", score=" + score + ", firstDate=" + firstDate + ", lastDate="
				+ lastDate + ", count=" + count + "]";
	}
	
}
