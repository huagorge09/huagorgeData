package com.cmwa.ecc.business.entity.workdate;

import org.apache.ibatis.type.Alias;

@Alias("workDateVo")
public class WorkDateVo {
	private String errCode;
	private String errMsg;
	
	private String lastDate; //上一工作日
	
	private String workDate; //当前工作日
	
	private String nextDate; //下一工作日
	
	private String newNextDate;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

	public String getNextDate() {
		return nextDate;
	}

	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}

	public String getNewNextDate() {
		return newNextDate;
	}

	public void setNewNextDate(String newNextDate) {
		this.newNextDate = newNextDate;
	}

	@Override
	public String toString() {
		return "WorkDateDto [errCode=" + errCode + ", errMsg=" + errMsg
				+ ", lastDate=" + lastDate + ", workDate=" + workDate
				+ ", nextDate=" + nextDate + ", newNextDate=" + newNextDate
				+ "]";
	} 
	
	
}
