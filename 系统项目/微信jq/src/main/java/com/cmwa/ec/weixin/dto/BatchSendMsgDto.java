package com.cmwa.ec.weixin.dto;


import org.springframework.web.multipart.MultipartFile;

/**
 * 类说明-批量发送短息dto
 * @author ex-chenhq
 *
 */
public class BatchSendMsgDto{

	
	/**
	 * 批量发送类型
	 */
	public String sendType;
	
	/**
	 * 需要发送的文件名
	 */
	private String sendFileName;
	
	/**
	 * 需要发送的文件
	 */
	private MultipartFile sendFile;

	/**
	 * 手动输入的号码
	 */
	private String otherMobile;
	
	/**
	 *	需要发送的内容
	 */
	private String content;
	
	private String userType;
	
	private String url;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendFileName() {
		return sendFileName;
	}

	public void setSendFileName(String sendFileName) {
		this.sendFileName = sendFileName;
	}

	public MultipartFile getSendFile() {
		return sendFile;
	}

	public void setSendFile(MultipartFile sendFile) {
		this.sendFile = sendFile;
	}


	public String getOtherMobile() {
		return otherMobile;
	}

	public void setOtherMobile(String otherMobile) {
		this.otherMobile = otherMobile;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
