package com.cmwa.ec.weixin.dto;

/**
 * 系统通知dto类	
 *
 */
public class SystemNoticeDto {

	private String awardName;
	
	private String nickname;
	
	private String otherUserName;
	
	private String msgType;
	
	private String awardSource;

	public String getAwardName() {
		return awardName;
	}

	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getOtherUserName() {
		return otherUserName;
	}

	public void setOtherUserName(String otherUserName) {
		this.otherUserName = otherUserName;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	
	public String getAwardSource() {
		return awardSource;
	}

	public void setAwardSource(String awardSource) {
		this.awardSource = awardSource;
	}

	@Override
	public String toString() {
		return "SystemNoticeDto [awardName=" + awardName + ", nickname="
				+ nickname + ", otherUserName=" + otherUserName + ", msgType="
				+ msgType + ", awardSource=" + awardSource + "]";
	}
}
