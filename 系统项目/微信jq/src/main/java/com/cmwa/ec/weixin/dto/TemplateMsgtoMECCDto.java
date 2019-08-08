package com.cmwa.ec.weixin.dto;

import java.io.Serializable;
/**
 * 微信端推送模板消息给mecc
 * @author ex-tuoy
 *
 */
public class TemplateMsgtoMECCDto implements Serializable {
	
	private static final long serialVersionUID = 6116418546345874220L;
	String messageId;
	String toUserId;
	String messageType;
	String template_id;
	String url;
	String content;
	String state;
	String insertTime;
	String updateTime;
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "TemplateMsgtoMECCDto [messageId=" + messageId + ", toUserId="
				+ toUserId + ", messageType=" + messageType + ", template_id="
				+ template_id + ", url=" + url + ", content=" + content
				+ ", state=" + state + ", insertTime=" + insertTime
				+ ", updateTime=" + updateTime + "]";
	}
	public TemplateMsgtoMECCDto(String messageId, String toUserId,
			String messageType, String template_id, String url, String content,
			String state, String insertTime, String updateTime) {
		super();
		this.messageId = messageId;
		this.toUserId = toUserId;
		this.messageType = messageType;
		this.template_id = template_id;
		this.url = url;
		this.content = content;
		this.state = state;
		this.insertTime = insertTime;
		this.updateTime = updateTime;
	}
	public TemplateMsgtoMECCDto() {
		super();
	}
	
	
}
