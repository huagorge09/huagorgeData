package com.cmwa.ecc.business.entity.common;

import java.util.Arrays;

import org.apache.ibatis.type.Alias;

@Alias("attachVo")
public class AttachVo {
	
	private String attId; //附件ID
	private String attName; // 名称

	private String attStoreType; // 附件存储类型(ATT_SAV_TYP)

	private byte[] attBlob; // 存放到Blob
	private String stat; // 状态

	private String isOK;// 记录上传是否成功标志

	private String errorMessage;// 记录上传是错误的信息
	
	private String createTime; // 上传时间

	public String getAttId() {
		return attId;
	}

	public void setAttId(String attId) {
		this.attId = attId;
	}

	public String getAttName() {
		return attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

	public String getAttStoreType() {
		return attStoreType;
	}

	public void setAttStoreType(String attStoreType) {
		this.attStoreType = attStoreType;
	}

	public byte[] getAttBlob() {
		return attBlob;
	}

	public void setAttBlob(byte[] attBlob) {
		this.attBlob = attBlob;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getIsOK() {
		return isOK;
	}

	public void setIsOK(String isOK) {
		this.isOK = isOK;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "AttachVo [attId=" + attId + ", attName=" + attName
				+ ", attStoreType=" + attStoreType + ", attBlob="
				+ Arrays.toString(attBlob) + ", stat=" + stat + ", isOK="
				+ isOK + ", errorMessage=" + errorMessage + ", createTime="
				+ createTime + "]";
	}
	
	
	
}
