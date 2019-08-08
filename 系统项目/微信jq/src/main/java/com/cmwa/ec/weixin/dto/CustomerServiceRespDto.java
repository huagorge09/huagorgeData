package com.cmwa.ec.weixin.dto;

import java.util.Date;

/**
 * 图片格式为base64
 * @author ex-hezk
 *
 */
public class CustomerServiceRespDto {

	/**
	 * 顾问id
	 */
	private String custserId;
	
	/**
	 * 顾问名称
	 */
	private String custserName;
	
	/**
	 * 顾问地址
	 */
	private String custserAddress;
	
	/**
	 * 顾问手机号
	 */
	private String custserMobile;
	
	/**
	 * 顾问工作年限
	 */
	private String custserWorkYear;
	
	/**
	 * 顾问毕业院校
	 */
	private String custserGraduateSchool;
	
	/**
	 * 顾问公众号二维码
	 */
	private String custserPublicAccQrcode;
	
	/**
	 * 顾问个人二维码
	 */
	private String custserPersonalQrCode;

	/**
	 * 顾问个人照片
	 */
	private String custserPhoto;

	/**
	 * 是否为默认顾问
	 */
	private String custserDefault;
	
	/**
	 * 顾问微信昵称
	 */
	private String custserNickname;
	
	private String createUser;
	
	private Date createDate;
	
	private String updateUser;
	
	private Date updateDate;
	
	/**
	 * @return the custserId
	 */
	public String getCustserId() {
		return custserId;
	}

	/**
	 * @param custserId the custserId to set
	 */
	public void setCustserId(String custserId) {
		this.custserId = custserId;
	}

	/**
	 * @return the custserName
	 */
	public String getCustserName() {
		return custserName;
	}

	/**
	 * @param custserName the custserName to set
	 */
	public void setCustserName(String custserName) {
		this.custserName = custserName;
	}

	/**
	 * @return the custserMobile
	 */
	public String getCustserMobile() {
		return custserMobile;
	}

	/**
	 * @param custserMobile the custserMobile to set
	 */
	public void setCustserMobile(String custserMobile) {
		this.custserMobile = custserMobile;
	}

	/**
	 * @return the custserWorkYear
	 */
	public String getCustserWorkYear() {
		return custserWorkYear;
	}

	/**
	 * @param custserWorkYear the custserWorkYear to set
	 */
	public void setCustserWorkYear(String custserWorkYear) {
		this.custserWorkYear = custserWorkYear;
	}

	/**
	 * @return the custserGraduateSchool
	 */
	public String getCustserGraduateSchool() {
		return custserGraduateSchool;
	}

	/**
	 * @param custserGraduateSchool the custserGraduateSchool to set
	 */
	public void setCustserGraduateSchool(String custserGraduateSchool) {
		this.custserGraduateSchool = custserGraduateSchool;
	}

	/**
	 * @return the custserPublicAccQrcode
	 */
	public String getCustserPublicAccQrcode() {
		return custserPublicAccQrcode;
	}

	/**
	 * @param custserPublicAccQrcode the custserPublicAccQrcode to set
	 */
	public void setCustserPublicAccQrcode(String custserPublicAccQrcode) {
		this.custserPublicAccQrcode = custserPublicAccQrcode;
	}

	/**
	 * @return the custserPersonalQrCode
	 */
	public String getCustserPersonalQrCode() {
		return custserPersonalQrCode;
	}

	/**
	 * @param custserPersonalQrCode the custserPersonalQrCode to set
	 */
	public void setCustserPersonalQrCode(String custserPersonalQrCode) {
		this.custserPersonalQrCode = custserPersonalQrCode;
	}

	/**
	 * @return the custserAddress
	 */
	public String getCustserAddress() {
		return custserAddress;
	}

	/**
	 * @param custserAddress the custserAddress to set
	 */
	public void setCustserAddress(String custserAddress) {
		this.custserAddress = custserAddress;
	}

	/**
	 * @return the custserPhoto
	 */
	public String getCustserPhoto() {
		return custserPhoto;
	}

	/**
	 * @param custserPhoto the custserPhoto to set
	 */
	public void setCustserPhoto(String custserPhoto) {
		this.custserPhoto = custserPhoto;
	}

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the updateUser
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the custserDefault
	 */
	public String getCustserDefault() {
		return custserDefault;
	}

	/**
	 * @param custserDefault the custserDefault to set
	 */
	public void setCustserDefault(String custserDefault) {
		this.custserDefault = custserDefault;
	}

	/**
	 * @return the custserNickname
	 */
	public String getCustserNickname() {
		return custserNickname;
	}

	/**
	 * @param custserNickname the custserNickname to set
	 */
	public void setCustserNickname(String custserNickname) {
		this.custserNickname = custserNickname;
	}

}
