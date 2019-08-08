package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * 客户税收居民信息
 * @author ex-chenbq
 *
 */
@Alias("userTaxInfoDto")
public class UserTaxInfoDto implements Serializable{
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 3741420519332065630L;
	
	/**
	 * 税收居民类型
	 */
	private String taxResidentType;
	
	/**
	 * 客户编号
	 */
	private String custNo;
	
	/**
	 * 性别
	 */
	 private String sex;
	 
	 /**
	  * 出生日期
	  */
	 private String birthDate;
	
	/**
	 * 客户名称
	 */
	private String custName;
	
	/**
	 * 用户ID
	 */
	private String cmfUserId;
	/**
	 * 税收居民国
	 */
	private String taxNationality;
	/**
	 * 税收居民国 描述
	 */
	private String taxNationalityNM;
	/**
	 * 税收居民地区
	 */
	private String taxArea;
	/**
	 * 税收居民地区 描述
	 */
	private String taxAreaNM;
	/**
	 * 纳税人识别号
	 */
	private String taxPayerCode;
	/**
	 * 无纳税识别号原因
	 */
	private String taxNotCodeCause;
	/**
	 * 无纳税识别号原因 描述
	 */
	private String taxNotCodeCauseNM;
	/**
	 * 未取得纳税人识别号的原因
	 */
	private String taxnotGetCause;
	
	/**
	 * 出生地国籍
	 */
	private String taxBirthNation;
	/**
	 * 出生地国籍 描述
	 */
	private String taxBirthNationNM;
	
	/**
	 * 出生地地区
	 */
	private String taxBirthRegion;
	/**
	 * 出生地地区 描述
	 */
	private String taxBirthRegionNM;
	
	/**
	 * 出生地详细信息
	 */
	private String taxBirthAddress;
	/**
	 * 排序号
	 */
	private String sortNo;
	/**
	 * 插入时间
	 */
	private String insertTime;
	/**
	 * 更新时间
	 */
	private String updateTime;
	
	/**
	 * 现居地址国家
	 */
	private String taxResideNation;
	private String taxResideNationNM;
	/**
	 * 现居地地区
	 */
	private String taxResideRegion;
	private String taxResideRegionNM;
	/**
	 * 现居地地址
	 */
	private String taxResideAddress;
	/**
	 * 现居地英文地址
	 */
	private String taxResideAddressEnglish;
	
	/**
	 * 英文姓
	 */
	private String firstName;
	
	/**
	 * 英文姓名
	 */
	private String englishName;
	private String passivenonfinflag;	//	消极非金融机构标识
	private String havenonresconflag;	//	存在非居民控制人标识
	
	//控制人中文姓名
	private String chineseName2;

	//控制人英文姓
	private String englishFamliyName3;
	//控制人英文名
	private String englishFirstName3;
	//控制人类型
	private String controllerType;
	//控制人非居民标识
	private String conNonResiFlag;
	//控制人持股比例【填写小数，比如0.6】
	private String conShareRatio;
	//控制人国籍
	private String regRegionCode2;
	//控制人国籍代码
	private String regRegionCode2Code;
	//控制人现居国家
	private String livingCountry2;
	//控制人现居国家代码
	private String livingCountry2Code;
	//控制人现居地址
	private String livingAddress5;
	//控制人现居地址[英文地址]
	private String livingAddress7;
	//控制人出生期
	private String birthDate2;
	//控制人出生国家
	private String birthCountry2;
	//控制人出生国家代码
	private String birthCountry2Code;
	//控制人出生城市
	private String birthCity2;
	//控制人税收居民国
	private String taxCountry2;
	private String taxCountry2Code;
	//控制人纳税人识别号
	private String taxID2;
	private String specificationCode;   //	摘要/说明2
	//控制人纳税识别号说明【若无识别号或者无法获得时填写】
	private String specification2;
	
	
	public String getTaxNationalityNM() {
		return taxNationalityNM;
	}

	public void setTaxNationalityNM(String taxNationalityNM) {
		this.taxNationalityNM = taxNationalityNM;
	}

	public String getTaxAreaNM() {
		return taxAreaNM;
	}

	public void setTaxAreaNM(String taxAreaNM) {
		this.taxAreaNM = taxAreaNM;
	}

	public String getTaxNotCodeCauseNM() {
		return taxNotCodeCauseNM;
	}

	public void setTaxNotCodeCauseNM(String taxNotCodeCauseNM) {
		this.taxNotCodeCauseNM = taxNotCodeCauseNM;
	}

	public String getTaxBirthNationNM() {
		return taxBirthNationNM;
	}

	public void setTaxBirthNationNM(String taxBirthNationNM) {
		this.taxBirthNationNM = taxBirthNationNM;
	}

	public String getTaxBirthRegionNM() {
		return taxBirthRegionNM;
	}

	public void setTaxBirthRegionNM(String taxBirthRegionNM) {
		this.taxBirthRegionNM = taxBirthRegionNM;
	}

	public String getTaxArea() {
		return taxArea;
	}

	public void setTaxArea(String taxArea) {
		this.taxArea = taxArea;
	}

	public UserTaxInfoDto() {
		super();
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getCmfUserId() {
		return cmfUserId;
	}

	public void setCmfUserId(String cmfUserId) {
		this.cmfUserId = cmfUserId;
	}

	public String getTaxNationality() {
		return taxNationality;
	}

	public void setTaxNationality(String taxNationality) {
		this.taxNationality = taxNationality;
	}

	public String getTaxPayerCode() {
		return taxPayerCode;
	}

	public void setTaxPayerCode(String taxPayerCode) {
		this.taxPayerCode = taxPayerCode;
	}

	public String getTaxNotCodeCause() {
		return taxNotCodeCause;
	}

	public void setTaxNotCodeCause(String taxNotCodeCause) {
		this.taxNotCodeCause = taxNotCodeCause;
	}

	public String getTaxnotGetCause() {
		return taxnotGetCause;
	}

	public void setTaxnotGetCause(String taxnotGetCause) {
		this.taxnotGetCause = taxnotGetCause;
	}

	public String getSortNo() {
		return sortNo;
	}

	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
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

	public String getTaxBirthNation() {
		return taxBirthNation;
	}

	public void setTaxBirthNation(String taxBirthNation) {
		this.taxBirthNation = taxBirthNation;
	}

	public String getTaxBirthRegion() {
		return taxBirthRegion;
	}

	public void setTaxBirthRegion(String taxBirthRegion) {
		this.taxBirthRegion = taxBirthRegion;
	}

	public String getTaxBirthAddress() {
		return taxBirthAddress;
	}

	public void setTaxBirthAddress(String taxBirthAddress) {
		this.taxBirthAddress = taxBirthAddress;
	}

	public String getTaxResideNation() {
		return taxResideNation;
	}

	public void setTaxResideNation(String taxResideNation) {
		this.taxResideNation = taxResideNation;
	}

	public String getTaxResideNationNM() {
		return taxResideNationNM;
	}

	public void setTaxResideNationNM(String taxResideNationNM) {
		this.taxResideNationNM = taxResideNationNM;
	}

	public String getTaxResideRegion() {
		return taxResideRegion;
	}

	public void setTaxResideRegion(String taxResideRegion) {
		this.taxResideRegion = taxResideRegion;
	}

	public String getTaxResideRegionNM() {
		return taxResideRegionNM;
	}

	public void setTaxResideRegionNM(String taxResideRegionNM) {
		this.taxResideRegionNM = taxResideRegionNM;
	}

	public String getTaxResideAddress() {
		return taxResideAddress;
	}

	public void setTaxResideAddress(String taxResideAddress) {
		this.taxResideAddress = taxResideAddress;
	}

	public String getTaxResideAddressEnglish() {
		return taxResideAddressEnglish;
	}

	public void setTaxResideAddressEnglish(String taxResideAddressEnglish) {
		this.taxResideAddressEnglish = taxResideAddressEnglish;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getTaxResidentType() {
		return taxResidentType;
	}

	public void setTaxResidentType(String taxResidentType) {
		this.taxResidentType = taxResidentType;
	}

	public String getChineseName2() {
		return chineseName2;
	}

	public void setChineseName2(String chineseName2) {
		this.chineseName2 = chineseName2;
	}

	public String getEnglishFamliyName3() {
		return englishFamliyName3;
	}

	public void setEnglishFamliyName3(String englishFamliyName3) {
		this.englishFamliyName3 = englishFamliyName3;
	}

	public String getEnglishFirstName3() {
		return englishFirstName3;
	}

	public void setEnglishFirstName3(String englishFirstName3) {
		this.englishFirstName3 = englishFirstName3;
	}

	public String getControllerType() {
		return controllerType;
	}

	public void setControllerType(String controllerType) {
		this.controllerType = controllerType;
	}

	public String getConNonResiFlag() {
		return conNonResiFlag;
	}

	public void setConNonResiFlag(String conNonResiFlag) {
		this.conNonResiFlag = conNonResiFlag;
	}

	public String getConShareRatio() {
		return conShareRatio;
	}

	public void setConShareRatio(String conShareRatio) {
		this.conShareRatio = conShareRatio;
	}

	public String getLivingCountry2() {
		return livingCountry2;
	}

	public void setLivingCountry2(String livingCountry2) {
		this.livingCountry2 = livingCountry2;
	}

	public String getLivingAddress5() {
		return livingAddress5;
	}

	public void setLivingAddress5(String livingAddress5) {
		this.livingAddress5 = livingAddress5;
	}

	public String getLivingAddress7() {
		return livingAddress7;
	}

	public void setLivingAddress7(String livingAddress7) {
		this.livingAddress7 = livingAddress7;
	}

	public String getBirthDate2() {
		return birthDate2;
	}

	public void setBirthDate2(String birthDate2) {
		this.birthDate2 = birthDate2;
	}

	public String getBirthCountry2() {
		return birthCountry2;
	}

	public void setBirthCountry2(String birthCountry2) {
		this.birthCountry2 = birthCountry2;
	}

	public String getBirthCity2() {
		return birthCity2;
	}

	public void setBirthCity2(String birthCity2) {
		this.birthCity2 = birthCity2;
	}

	public String getTaxCountry2() {
		return taxCountry2;
	}

	public void setTaxCountry2(String taxCountry2) {
		this.taxCountry2 = taxCountry2;
	}

	public String getTaxID2() {
		return taxID2;
	}

	public void setTaxID2(String taxID2) {
		this.taxID2 = taxID2;
	}

	public String getSpecification2() {
		return specification2;
	}

	public void setSpecification2(String specification2) {
		this.specification2 = specification2;
	}

	public String getRegRegionCode2() {
		return regRegionCode2;
	}

	public void setRegRegionCode2(String regRegionCode2) {
		this.regRegionCode2 = regRegionCode2;
	}

	public String getSpecificationCode() {
		return specificationCode;
	}

	public void setSpecificationCode(String specificationCode) {
		this.specificationCode = specificationCode;
	}

	public String getPassivenonfinflag() {
		return passivenonfinflag;
	}

	public void setPassivenonfinflag(String passivenonfinflag) {
		this.passivenonfinflag = passivenonfinflag;
	}

	public String getHavenonresconflag() {
		return havenonresconflag;
	}

	public void setHavenonresconflag(String havenonresconflag) {
		this.havenonresconflag = havenonresconflag;
	}

	public String getRegRegionCode2Code() {
		return regRegionCode2Code;
	}

	public void setRegRegionCode2Code(String regRegionCode2Code) {
		this.regRegionCode2Code = regRegionCode2Code;
	}

	public String getLivingCountry2Code() {
		return livingCountry2Code;
	}

	public void setLivingCountry2Code(String livingCountry2Code) {
		this.livingCountry2Code = livingCountry2Code;
	}

	public String getBirthCountry2Code() {
		return birthCountry2Code;
	}

	public void setBirthCountry2Code(String birthCountry2Code) {
		this.birthCountry2Code = birthCountry2Code;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getTaxCountry2Code() {
		return taxCountry2Code;
	}

	public void setTaxCountry2Code(String taxCountry2Code) {
		this.taxCountry2Code = taxCountry2Code;
	}
}
