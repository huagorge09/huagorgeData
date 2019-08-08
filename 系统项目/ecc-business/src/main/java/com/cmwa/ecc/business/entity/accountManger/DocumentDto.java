package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.ibatis.type.Alias;

import com.cmwa.ecc.business.commonVo.BaseVo;
/**
 * 客户资料
 * 上传附件
 * @author ex-liuy
 *
 */
@Alias("documentDto")
public class DocumentDto extends BaseVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int storageid; // 附件流水号
	private String catalog; // 主业务表的表名
	private String subkeyid; // 主业务表的主键ID
	private String filename; // 文件名
	private long filesize; // 文件大小
	private String securitylevel; // 权限级别，0表示低，1中，2高，可以不填写
	private String fundacco; // 权限级别，0表示低，1中，2高，可以不填写
	private byte[] filecontent;
	private String errcode;
	private String errmsg;
	
	public DocumentDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getStorageid() {
		return storageid;
	}
	public void setStorageid(int storageid) {
		this.storageid = storageid;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getSubkeyid() {
		return subkeyid;
	}
	public void setSubkeyid(String subkeyid) {
		this.subkeyid = subkeyid;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getFilesize() {
		return filesize;
	}
	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}
	public String getSecuritylevel() {
		return securitylevel;
	}
	public void setSecuritylevel(String securitylevel) {
		this.securitylevel = securitylevel;
	}
	public String getFundacco() {
		return fundacco;
	}
	public void setFundacco(String fundacco) {
		this.fundacco = fundacco;
	}
	public byte[] getFilecontent() {
		return filecontent;
	}
	public void setFilecontent(byte[] filecontent) {
		this.filecontent = filecontent;
	}
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "DocumentDto [storageid=" + storageid + ", catalog=" + catalog
				+ ", subkeyid=" + subkeyid + ", filename=" + filename
				+ ", filesize=" + filesize + ", securitylevel=" + securitylevel
				+ ", fundacco=" + fundacco + ", filecontent="
				+ Arrays.toString(filecontent) + ", errcode=" + errcode
				+ ", errmsg=" + errmsg + "]";
	}
}
