package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

/**
 * 非居民涉税信息申请实体类
 * @author ex-chenbq
 *
 */
public class AppR1BlotterDto implements Serializable{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	private String appsheetserialno;    //	申请单编号
	private String branchcode;			//	网点号码
	private String originalappsheetno;	//	原申请单编号
	private String transactiondate;  	//	交易发生日期
	private String transactiontime;  	//	交易发生时间
	private String individualorinstitution;	//	个人/机构标志
	private String distributorcode;  	//	销售人代码
	private String sex;					//	投资人性别
	private String taaccountid;      	//	投资人基金帐号
	private String specification;    	//	摘要/说明
	private String originalappdate;  	//	原申请日期
	private String addflag;   			//	增删标志
	private String reservedfield1;   	//	预留字段1
	private String reservedfield2;   	//	预留字段2
	private String reservedfield3;   	//	预留字段3
	private String reservedfield4;   	//	预留字段4
	private String reservedfield5;   	//	预留字段5
	private String regregioncode;    	//	注册地国家代码
	private String surveymethod;     	//	调查规则
	private String getinvestcerflag; 	//	取得投资人声明标识
	private String nonresiflag;      	//	非居民标识
	private String chinesename;      	//	中文姓名
	private String englishfirstname2;	//	英文名2
	private String englishfamliyname2;	//	英文姓2
	private String englishname;      	//	英文全称
	private String livingcountry;    	//	现居国家
	private String livingaddress;    	//	现居地址
	private String livingaddress2;   	//	现居地址2
	private String livingaddress3;   	//	现居地址3
	private String livingaddress4;   	//	现居地址4
	private String birthdate; 			//	出生日期
	private String birthcountry;     	//	出生国家
	private String birthcountryengname;	//	出生国英文名称
	private String birthcity; 			//	出生城市
	private String addresstype;      	//	地址类型
	private String taxcountry;			//	税收居民国
	private String taxid;     			//	纳税人识别号
	private String passivenonfinflag;	//	消极非金融机构标识
	private String havenonresconflag;	//	存在非居民控制人标识
	private String chinesename2;     	//	中文姓名2
	private String englishfirstname3;	//	英文名3
	private String englishfamliyname3;  //	英文姓3
	private String controllertype;   	//	控制人类型
	private String connonresiflag;   	//	控制人非居民标识
	private String conshareratio;    	//	控制人持股比例
	private String livingcountry2;   	//	现居国家2
	private String livingaddress5;   	//	现居地址5
	private String livingaddress6;   	//	现居地址6
	private String livingaddress7;   	//	现居地址7
	private String livingaddress8;   	//	现居地址8
	private String regregioncode2;   	//	国籍2
	private String birthdate2;			//	出生日期2
	private String birthcountry2;    	//	出生国家2
	private String birthcouengname2; 	//	出生国英文名称2
	private String birthcity2;			//	出生城市2
	private String taxcountry2;      	//	税收居民国2
	private String taxid2;    			//	纳税人识别号2
	private String specificationCode;   //	摘要/说明2
	private String specification2;   	//	摘要/说明2
	private String reservedfield6;   	//	预留字段6
	private String tano;      			//	TA代码
	private String cmfuserid; 			//	CMF用户ID
	private String custno;    			//	客户号
	private String applyst;   			//	申请状态
	private String transst;   			//	上传状态
	private String transtm; 			//	上传时间
	
	/**
	 * GET SET
	 * @return
	 */
	public String getAppsheetserialno() {
		return appsheetserialno;
	}
	public void setAppsheetserialno(String appsheetserialno) {
		this.appsheetserialno = appsheetserialno;
	}
	public String getBranchcode() {
		return branchcode;
	}
	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}
	public String getOriginalappsheetno() {
		return originalappsheetno;
	}
	public void setOriginalappsheetno(String originalappsheetno) {
		this.originalappsheetno = originalappsheetno;
	}
	public String getTransactiondate() {
		return transactiondate;
	}
	public void setTransactiondate(String transactiondate) {
		this.transactiondate = transactiondate;
	}
	public String getTransactiontime() {
		return transactiontime;
	}
	public void setTransactiontime(String transactiontime) {
		this.transactiontime = transactiontime;
	}
	public String getIndividualorinstitution() {
		return individualorinstitution;
	}
	public void setIndividualorinstitution(String individualorinstitution) {
		this.individualorinstitution = individualorinstitution;
	}
	public String getDistributorcode() {
		return distributorcode;
	}
	public void setDistributorcode(String distributorcode) {
		this.distributorcode = distributorcode;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getTaaccountid() {
		return taaccountid;
	}
	public void setTaaccountid(String taaccountid) {
		this.taaccountid = taaccountid;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getOriginalappdate() {
		return originalappdate;
	}
	public void setOriginalappdate(String originalappdate) {
		this.originalappdate = originalappdate;
	}
	public String getAddflag() {
		return addflag;
	}
	public void setAddflag(String addflag) {
		this.addflag = addflag;
	}
	public String getReservedfield1() {
		return reservedfield1;
	}
	public void setReservedfield1(String reservedfield1) {
		this.reservedfield1 = reservedfield1;
	}
	public String getReservedfield2() {
		return reservedfield2;
	}
	public void setReservedfield2(String reservedfield2) {
		this.reservedfield2 = reservedfield2;
	}
	public String getReservedfield3() {
		return reservedfield3;
	}
	public void setReservedfield3(String reservedfield3) {
		this.reservedfield3 = reservedfield3;
	}
	public String getReservedfield4() {
		return reservedfield4;
	}
	public void setReservedfield4(String reservedfield4) {
		this.reservedfield4 = reservedfield4;
	}
	public String getReservedfield5() {
		return reservedfield5;
	}
	public void setReservedfield5(String reservedfield5) {
		this.reservedfield5 = reservedfield5;
	}
	public String getRegregioncode() {
		return regregioncode;
	}
	public void setRegregioncode(String regregioncode) {
		this.regregioncode = regregioncode;
	}
	public String getSurveymethod() {
		return surveymethod;
	}
	public void setSurveymethod(String surveymethod) {
		this.surveymethod = surveymethod;
	}
	public String getGetinvestcerflag() {
		return getinvestcerflag;
	}
	public void setGetinvestcerflag(String getinvestcerflag) {
		this.getinvestcerflag = getinvestcerflag;
	}
	public String getNonresiflag() {
		return nonresiflag;
	}
	public void setNonresiflag(String nonresiflag) {
		this.nonresiflag = nonresiflag;
	}
	public String getChinesename() {
		return chinesename;
	}
	public void setChinesename(String chinesename) {
		this.chinesename = chinesename;
	}
	public String getEnglishfirstname2() {
		return englishfirstname2;
	}
	public void setEnglishfirstname2(String englishfirstname2) {
		this.englishfirstname2 = englishfirstname2;
	}
	public String getEnglishfamliyname2() {
		return englishfamliyname2;
	}
	public void setEnglishfamliyname2(String englishfamliyname2) {
		this.englishfamliyname2 = englishfamliyname2;
	}
	public String getEnglishname() {
		return englishname;
	}
	public void setEnglishname(String englishname) {
		this.englishname = englishname;
	}
	public String getLivingcountry() {
		return livingcountry;
	}
	public void setLivingcountry(String livingcountry) {
		this.livingcountry = livingcountry;
	}
	public String getLivingaddress() {
		return livingaddress;
	}
	public void setLivingaddress(String livingaddress) {
		this.livingaddress = livingaddress;
	}
	public String getLivingaddress2() {
		return livingaddress2;
	}
	public void setLivingaddress2(String livingaddress2) {
		this.livingaddress2 = livingaddress2;
	}
	public String getLivingaddress3() {
		return livingaddress3;
	}
	public void setLivingaddress3(String livingaddress3) {
		this.livingaddress3 = livingaddress3;
	}
	public String getLivingaddress4() {
		return livingaddress4;
	}
	public void setLivingaddress4(String livingaddress4) {
		this.livingaddress4 = livingaddress4;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getBirthcountry() {
		return birthcountry;
	}
	public void setBirthcountry(String birthcountry) {
		this.birthcountry = birthcountry;
	}
	public String getBirthcountryengname() {
		return birthcountryengname;
	}
	public void setBirthcountryengname(String birthcountryengname) {
		this.birthcountryengname = birthcountryengname;
	}
	public String getBirthcity() {
		return birthcity;
	}
	public void setBirthcity(String birthcity) {
		this.birthcity = birthcity;
	}
	public String getAddresstype() {
		return addresstype;
	}
	public void setAddresstype(String addresstype) {
		this.addresstype = addresstype;
	}
	public String getTaxcountry() {
		return taxcountry;
	}
	public void setTaxcountry(String taxcountry) {
		this.taxcountry = taxcountry;
	}
	public String getTaxid() {
		return taxid;
	}
	public void setTaxid(String taxid) {
		this.taxid = taxid;
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
	public String getChinesename2() {
		return chinesename2;
	}
	public void setChinesename2(String chinesename2) {
		this.chinesename2 = chinesename2;
	}
	public String getEnglishfirstname3() {
		return englishfirstname3;
	}
	public void setEnglishfirstname3(String englishfirstname3) {
		this.englishfirstname3 = englishfirstname3;
	}
	public String getEnglishfamliyname3() {
		return englishfamliyname3;
	}
	public void setEnglishfamliyname3(String englishfamliyname3) {
		this.englishfamliyname3 = englishfamliyname3;
	}
	public String getControllertype() {
		return controllertype;
	}
	public void setControllertype(String controllertype) {
		this.controllertype = controllertype;
	}
	public String getConnonresiflag() {
		return connonresiflag;
	}
	public void setConnonresiflag(String connonresiflag) {
		this.connonresiflag = connonresiflag;
	}
	public String getConshareratio() {
		return conshareratio;
	}
	public void setConshareratio(String conshareratio) {
		this.conshareratio = conshareratio;
	}
	public String getLivingcountry2() {
		return livingcountry2;
	}
	public void setLivingcountry2(String livingcountry2) {
		this.livingcountry2 = livingcountry2;
	}
	public String getLivingaddress5() {
		return livingaddress5;
	}
	public void setLivingaddress5(String livingaddress5) {
		this.livingaddress5 = livingaddress5;
	}
	public String getLivingaddress6() {
		return livingaddress6;
	}
	public void setLivingaddress6(String livingaddress6) {
		this.livingaddress6 = livingaddress6;
	}
	public String getLivingaddress7() {
		return livingaddress7;
	}
	public void setLivingaddress7(String livingaddress7) {
		this.livingaddress7 = livingaddress7;
	}
	public String getLivingaddress8() {
		return livingaddress8;
	}
	public void setLivingaddress8(String livingaddress8) {
		this.livingaddress8 = livingaddress8;
	}
	public String getRegregioncode2() {
		return regregioncode2;
	}
	public void setRegregioncode2(String regregioncode2) {
		this.regregioncode2 = regregioncode2;
	}
	public String getBirthdate2() {
		return birthdate2;
	}
	public void setBirthdate2(String birthdate2) {
		this.birthdate2 = birthdate2;
	}
	public String getBirthcountry2() {
		return birthcountry2;
	}
	public void setBirthcountry2(String birthcountry2) {
		this.birthcountry2 = birthcountry2;
	}
	public String getBirthcouengname2() {
		return birthcouengname2;
	}
	public void setBirthcouengname2(String birthcouengname2) {
		this.birthcouengname2 = birthcouengname2;
	}
	public String getBirthcity2() {
		return birthcity2;
	}
	public void setBirthcity2(String birthcity2) {
		this.birthcity2 = birthcity2;
	}
	public String getTaxcountry2() {
		return taxcountry2;
	}
	public void setTaxcountry2(String taxcountry2) {
		this.taxcountry2 = taxcountry2;
	}
	public String getTaxid2() {
		return taxid2;
	}
	public void setTaxid2(String taxid2) {
		this.taxid2 = taxid2;
	}
	public String getSpecification2() {
		return specification2;
	}
	public void setSpecification2(String specification2) {
		this.specification2 = specification2;
	}
	public String getReservedfield6() {
		return reservedfield6;
	}
	public void setReservedfield6(String reservedfield6) {
		this.reservedfield6 = reservedfield6;
	}
	public String getTano() {
		return tano;
	}
	public void setTano(String tano) {
		this.tano = tano;
	}
	public String getCmfuserid() {
		return cmfuserid;
	}
	public void setCmfuserid(String cmfuserid) {
		this.cmfuserid = cmfuserid;
	}
	public String getCustno() {
		return custno;
	}
	public void setCustno(String custno) {
		this.custno = custno;
	}
	public String getApplyst() {
		return applyst;
	}
	public void setApplyst(String applyst) {
		this.applyst = applyst;
	}
	public String getTransst() {
		return transst;
	}
	public void setTransst(String transst) {
		this.transst = transst;
	}
	public String getTranstm() {
		return transtm;
	}
	public void setTranstm(String transtm) {
		this.transtm = transtm;
	}
	public String getSpecificationCode() {
		return specificationCode;
	}
	public void setSpecificationCode(String specificationCode) {
		this.specificationCode = specificationCode;
	}
}
