package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;
@Alias("MsgViewVo")
public class MsgViewVo {
	private String id;
	private String msgConfId;
	private String empId;
	private String title;
	private String content;
	private String stat;
	private String createId;
	private String createDate;
	private String viewerId;
	private String viewDate;
	private String targetId1;
	private String targetId2;
	private String targetId3;
	private String targetId4;
	private String sourceId;
	private String standardDate;
	private String flag = "0";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMsgConfId() {
		return msgConfId;
	}
	public void setMsgConfId(String msgConfId) {
		this.msgConfId = msgConfId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getViewerId() {
		return viewerId;
	}
	public void setViewerId(String viewerId) {
		this.viewerId = viewerId;
	}
	public String getViewDate() {
		return viewDate;
	}
	public void setViewDate(String viewDate) {
		this.viewDate = viewDate;
	}
	public String getTargetId1() {
		return targetId1;
	}
	public void setTargetId1(String targetId1) {
		this.targetId1 = targetId1;
	}
	public String getTargetId2() {
		return targetId2;
	}
	public void setTargetId2(String targetId2) {
		this.targetId2 = targetId2;
	}
	public String getTargetId3() {
		return targetId3;
	}
	public void setTargetId3(String targetId3) {
		this.targetId3 = targetId3;
	}
	public String getTargetId4() {
		return targetId4;
	}
	public void setTargetId4(String targetId4) {
		this.targetId4 = targetId4;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getStandardDate() {
		return standardDate;
	}
	public void setStandardDate(String standardDate) {
		this.standardDate = standardDate;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
