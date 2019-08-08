package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias("openAccountDto")
public class OpenAccountDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String invprtp; //投资者
	private String invprtpnm; //投资者
	
	private String operatorId; 		//操作员代码
	private String permissionId; 	//权限代码
	
	private String tradeCount;
	private List<TradeAccDto> tradeAccoList = new ArrayList<TradeAccDto>();
	private List<FundAcctDto> fundAccoList = new ArrayList<FundAcctDto>();
	
	private String serialno; 		//流水号
	private String oserialno; 		//原流水号
	private String trustType; 		//委托方式
	private String invnm; 			//客户姓名
	private String invtp; 			//客户类别
	private String invtpnm; 			//客户类别
	private String sex; 			//性别
	private String idtp; 			//证件类型
	private String idtpnm; 			//证件类型
	private String nationalitycode; //国籍
	private String idno; 			//证件号码
	private String tano; 			//TA代码
	private String idvalidate; 		//证件有效期
	private String password; 		//交易密码
	private String oldpassword; 	//原交易密码
	private String openName; 		//开户银行
	private String openAddr; 	    //开户银行地址(省）
	private String openBankCity;	//开户银行市
	private String bankAccoNm; 		//银行户名
	private String bnkNo; 			//银行编号
	private String bankAcco; 		//银行账号
	private String changeAmt; 		//划转资金
	private String postcode; 		//邮政编码
	private String tel; 			//联系电话
	private String housetel; 		//家庭电话
	private String fax; 			//传真
	private String faxdelegate;     //传真委托
	private String mobile; 			//手机
	private String addr; 			//地址
	private String email; 			//email
	private String cast; 			//折扣率
	private String melonmd; 		//分红方式
	private String capitalmode; 	//资金模式
	private String sendingroute; 	//对账单寄送方式
	private String postmode; 		//对账单寄送途径
	private String trustType1; 		//委托方式--电话委托
	private String trustType2; 		//委托方式--网上委托
	private String trustType3; 		//委托方式--传真委托
	private String trustType4; 		//委托方式--自助委托
	private String trustType5; 		//委托方式--经纪人委托
	private String trustType6; 		//委托方式--其他委托
	private String birthday; 		//生日
	private String voccode; 		//职业
	private String edlevel; 		//学历
	private String income; 			//年收入
	private String marriedflag; 	//婚姻
	private String szsecacc; 		//深交所账号
	private String shsecacc; 		//上交所账号
	private String citycode; 		//城市代码
	private String provincecode;    //省份（2010-01-30）
	private String broker; 			//经纪人
	private String percent; 		//经纪人分成比例
	private String seller; 			//业务员
	private String invest; 			//账户备注
	private String explain; 		//备注
	private List<ContactDto> contList = new ArrayList<ContactDto>(); //经办人信息列表
	private List<ContactDto> oidList = new ArrayList<ContactDto>(); //经办人信息列表
	private String checkno; 		//主管工号
	private String checkpwd; 		//主管密码
	private String custrisklevl; 	//客户风险等级
	private String custrisklevlnm; 	//客户风险等级
	private String specriskLevel;	//特殊用户风险评级
	private String custriskscore; 	//客户风险等级评分
	
	private String custriskdate;   //测评日期 自然日，YYYYMMDD added 20130517
	private String custrisktime;   //测评时间 时分秒，HHMMSS added 20130517
	
	


	private String acctabbr; 		//客户简称
	private String instrepcode;     //法人代码
	private String instrepnm; 		//法人姓名
	private String instrepidtp; 	//法人证件类型
	private String instrepidno; 	//法人证件号码
	private String industryType; 	//行业类型
	private String corpropertiy; 	//企业性质
	private String registCapital; 	//注册资本
	private String instrepvalidate; //法人证件有效期
	private String instrepnation; //法人国籍
	
	
	private double availableBalance;//可用金额
	private double fromzenbal; //冻结金额
	
	private String dsapkind; 		//业务类型代码
	private String dsapkindnm; 		//业务类型代码
	private String netpoint; 		//网点
	
	private String fundacct; 		//基金账号
	private String custno; 			//客户号
	private String tradeacco; 		//交易账号
	
	private List<FundBalanceDto> fundbalanceList;	//份额列表
	
	//复核驳回使用
	private String checkst; 		//复核状态
	private String checker; 		//复核员
	
	//反洗钱信息
	private String warrantyvalidate;//授权书有效期
	private String amlrisktype;		//反洗钱风险等级
	private String organizationno;	//组织机构代码
	private String ornovalidate;	//组织机构代码证有效期
	private String taxno;			//税务登记证代码
	private String qualificationtp;	//资格证类型
	private String qualificationno;	//资格证号码
	private String qlfvalidate;		//资格证有效期
	
	private String holdingname;		//控股股东名称/账户实际控制人
	private String holdingidtp;		//控股股东/账户实际控制人证件类型
	private String holdingidno;		//控股股东/账户实际控制人证件号码
	private String holdingvalidate;	//控股股东/账户实际控制人证件有效期
	private String holdingorno;		//控股股东组织机构代码
	private String holdingtaxno;	//控股股东税务登记证代码
	private String recipientsname;	//账单收件人
	private String recipientsidtp;	//账单收件人证件类型
	private String recipientsidno;	//账单收件人证件号码
	private String recipientsvalidt;//账单收件人证件有效期
	
	private String principalname;	//负责人名称
	private String principalidtp;	//负责人证件类型
	private String principalidno;	//负责人证件号码
	private String principalvalidt;	//负责人证件有效期
	private String principalnation;	//负责人国籍
	
	private String beneficiary;		//账户实际受益人
	private String businessrange;	//经营范围
	private String fxqremark;		//反洗钱备注
	
	private String businesstp; 		//业务类型
	private String docbusinesstp; 		//资料业务类型
	private String docbusinesstpnm; 		//资料业务类型
	private String companytp;        
	private String regiontp;
	
	
	private String contactgrant; 		//经办人授权
	private String contact; 		//经办人
	private String contactnation; 		//经办人国籍
	private String contidtp; 		//经办人证件类型
	private String contidtpnm; 		//经办人证件类型
	private String contidno; 		//经办人证件号码
	private String contvalidate; 	//经办人证件有效期		
	private String contphone; 		//经办人电话
	private String contfax; 		//经办人传真
	private String contmobile; 		//经办人手机
	private String contemail; 		//经办人Email
	private String contAddr;		//经办人联系地址
	private String contPostcode;	//经办人邮编
	
	private String ocontactgrant; 		//其他经办人授权
	private String ocontact; 		//其他经办人
	private String ocontactnation; 		//其他经办人国籍
	private String ocontidtp; 		//其他经办人证件类型
	private String ocontidno; 		//其他经办人证件号码
	private String ocontvalidate; 	//其他经办人证件有效期		
	private String ocontphone; 		//其他经办人电话
	private String ocontfax; 		//其他经办人传真
	private String ocontmobile; 		//其他经办人手机
	private String ocontemail; 		//其他经办人Email
	private String ocontAddr;		//其他经办人联系地址
	private String ocontPostcode;	//其他经办人邮编

	private String oidtp; 			//其他证件类型	
	private String oidno; 			//其他证件号码	
	private String oidvalidate;     //其他证件有效期	
	
	
	private String errcode; 		//错误代码
	private String errmsg; 			//错误信息
	private String custabbrcode; 		//一级
	private String custinstreprcode; 			//二级

	//资料信息
	private String ifsaved;      //是否归档
	private String ifOriginal;    //是否原件
	private String ifalldocument;   //资料是否齐全
	private String isupload;//是否上传附件
	private String remarkinfo;//资料信息备注
	private String custdocument;     //客户资料
	private String existsflag;	//客户资料是否存在
	private String isscan;//是否扫描
	private String keepaddress;//存档位置
	private String salesaccmanager;//所属客户经理
	private String fileno;//文件编号
	
	private String opentype;//开户类型 新开户001，已有基金账号开户008
	private String fundacc;//已有基金账号开户时用
	private List<OpenAccountDto> documentlist;//客户资料信息列表
	
	private List<IDInfoDto> oidlist;
	private List<ContactInfoDto> ocontactlist;
	
	private String modifylist;
	
	private String organType;//机构类型 hufj 2012-02-14添加
	private String flag;//
	private String apdt;//申请日期
	private String begindate;
	private String enddate;
	private String custinfoid;
	private String aptype;
	private String appserialno;
	/**  	投资者适当性信息   		*/
	private String taxType;
	private String taxTypeDecl;
	private String investProInstType;
	private String investProInstSecond;
	private String oneYearEndNetAsset;
	private String oneYearEndFinAsset;
	private String investExperience;
	private String negativeNotFinaInst;
	private String controlPerTaxDecl;
	private String threeAnnualIncome;
	private String financialAsset;
	private String indInvExperience;
	private String relatedWorkExp;
	private String finProfessions;
	//录音文件编号 查询用
	private String voiceRecord;
	//预留扩展字段
	private String attr1;
	private String attr2;
	private String attr3;
	
	//资料信息字符串
	private String newDocStr;
	
	//其他经办人信息字符串
	private String newOcontStr;
	
	//其他证件信息字符串
	private String newOidStr;
	
	//税收居民信息字符串
	private String taxresident;
	
	//开户数据字符串
	private String baseInfo;
	
	private String accountType; //账户类型
	private String custType;
	private String fundacco;
	private String option;
	private String custsimpnm;
	/**		查询用 	*/
	private String apkind;
	private String apkindName;
	private String aptypename;
	//插入过程用
	private String nullStr;
	private String valueOfA;
	private String newCustno;
	//短期理财产品
	private List<FundBalanceDto> fundbalconsList = new ArrayList<FundBalanceDto>();	//份额列表
	private String fundType;
	private String currentWorkDay;
	
	private String preRisklevel;
	
	private String preInvprtp;
	
	public String getPreInvprtp() {
		return preInvprtp;
	}

	public void setPreInvprtp(String preInvprtp) {
		this.preInvprtp = preInvprtp;
	}

	public String getPreRisklevel() {
		return preRisklevel;
	}

	public void setPreRisklevel(String preRisklevel) {
		this.preRisklevel = preRisklevel;
	}

	public String getCurrentWorkDay() {
		return currentWorkDay;
	}

	public void setCurrentWorkDay(String currentWorkDay) {
		this.currentWorkDay = currentWorkDay;
	}

	public String getFundType() {
		return fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	public List<FundBalanceDto> getFundbalconsList() {
		return fundbalconsList;
	}

	public void setFundbalconsList(List<FundBalanceDto> fundbalconsList) {
		this.fundbalconsList = fundbalconsList;
	}

	public String getNewCustno() {
		return newCustno;
	}

	public void setNewCustno(String newCustno) {
		this.newCustno = newCustno;
	}

	public String getNullStr() {
		return nullStr;
	}

	public void setNullStr(String nullStr) {
		this.nullStr = nullStr;
	}

	public String getValueOfA() {
		return valueOfA;
	}

	public void setValueOfA(String valueOfA) {
		this.valueOfA = valueOfA;
	}

	public String getAptypename() {
		return aptypename;
	}

	public void setAptypename(String aptypename) {
		this.aptypename = aptypename;
	}

	public String getApkind() {
		return apkind;
	}

	public void setApkind(String apkind) {
		this.apkind = apkind;
	}

	public String getApkindName() {
		return apkindName;
	}

	public void setApkindName(String apkindName) {
		this.apkindName = apkindName;
	}

	public String getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(String baseInfo) {
		this.baseInfo = baseInfo;
	}

	public String getVoiceRecord() {
		return voiceRecord;
	}

	public void setVoiceRecord(String voiceRecord) {
		this.voiceRecord = voiceRecord;
	}

	public String getAttr1() {
		return attr1;
	}

	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}

	public String getAttr2() {
		return attr2;
	}

	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}

	public String getAttr3() {
		return attr3;
	}

	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getTaxTypeDecl() {
		return taxTypeDecl;
	}

	public void setTaxTypeDecl(String taxTypeDecl) {
		this.taxTypeDecl = taxTypeDecl;
	}

	public String getInvestProInstType() {
		return investProInstType;
	}

	public void setInvestProInstType(String investProInstType) {
		this.investProInstType = investProInstType;
	}

	public String getInvestProInstSecond() {
		return investProInstSecond;
	}

	public void setInvestProInstSecond(String investProInstSecond) {
		this.investProInstSecond = investProInstSecond;
	}

	public String getOneYearEndNetAsset() {
		return oneYearEndNetAsset;
	}

	public void setOneYearEndNetAsset(String oneYearEndNetAsset) {
		this.oneYearEndNetAsset = oneYearEndNetAsset;
	}

	public String getOneYearEndFinAsset() {
		return oneYearEndFinAsset;
	}

	public void setOneYearEndFinAsset(String oneYearEndFinAsset) {
		this.oneYearEndFinAsset = oneYearEndFinAsset;
	}

	public String getInvestExperience() {
		return investExperience;
	}

	public void setInvestExperience(String investExperience) {
		this.investExperience = investExperience;
	}

	public String getNegativeNotFinaInst() {
		return negativeNotFinaInst;
	}

	public void setNegativeNotFinaInst(String negativeNotFinaInst) {
		this.negativeNotFinaInst = negativeNotFinaInst;
	}

	public String getControlPerTaxDecl() {
		return controlPerTaxDecl;
	}

	public void setControlPerTaxDecl(String controlPerTaxDecl) {
		this.controlPerTaxDecl = controlPerTaxDecl;
	}

	public String getThreeAnnualIncome() {
		return threeAnnualIncome;
	}

	public void setThreeAnnualIncome(String threeAnnualIncome) {
		this.threeAnnualIncome = threeAnnualIncome;
	}

	public String getFinancialAsset() {
		return financialAsset;
	}

	public void setFinancialAsset(String financialAsset) {
		this.financialAsset = financialAsset;
	}

	public String getIndInvExperience() {
		return indInvExperience;
	}

	public void setIndInvExperience(String indInvExperience) {
		this.indInvExperience = indInvExperience;
	}

	public String getRelatedWorkExp() {
		return relatedWorkExp;
	}

	public void setRelatedWorkExp(String relatedWorkExp) {
		this.relatedWorkExp = relatedWorkExp;
	}

	public String getFinProfessions() {
		return finProfessions;
	}

	public void setFinProfessions(String finProfessions) {
		this.finProfessions = finProfessions;
	}

	public String getAppserialno() {
		return appserialno;
	}

	public void setAppserialno(String appserialno) {
		this.appserialno = appserialno;
	}

	public String getAptype() {
		return aptype;
	}

	public void setAptype(String aptype) {
		this.aptype = aptype;
	}

	public String getCustinfoid() {
		return custinfoid;
	}

	public void setCustinfoid(String custinfoid) {
		this.custinfoid = custinfoid;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getApdt() {
		return apdt;
	}

	public void setApdt(String apdt) {
		this.apdt = apdt;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCustriskdate() {
		return custriskdate;
	}

	public void setCustriskdate(String custriskdate) {
		this.custriskdate = custriskdate;
	}

	public String getCustrisktime() {
		return custrisktime;
	}

	public void setCustrisktime(String custrisktime) {
		this.custrisktime = custrisktime;
	}
	public String getModifylist() {
		return modifylist;
	}
	
	public String getOrganType() {
		return organType;
	}


	public void setOrganType(String organType) {
		this.organType = organType;
	}

	public void setModifylist(String modifylist) {
		this.modifylist = modifylist;
	}

	
	public String getIfsaved() {
		return ifsaved;
	}


	public String getIsscan() {
		return isscan;
	}

	public void setIsscan(String isscan) {
		this.isscan = isscan;
	}

	public String getKeepaddress() {
		return keepaddress;
	}

	public void setKeepaddress(String keepaddress) {
		this.keepaddress = keepaddress;
	}

	public String getSalesaccmanager() {
		return salesaccmanager;
	}

	public void setSalesaccmanager(String salesaccmanager) {
		this.salesaccmanager = salesaccmanager;
	}

	public String getFileno() {
		return fileno;
	}

	public void setFileno(String fileno) {
		this.fileno = fileno;
	}

	public void setIfsaved(String ifsaved) {
		this.ifsaved = ifsaved;
	}


	public String getIfOriginal() {
		return ifOriginal;
	}


	public void setIfOriginal(String ifOriginal) {
		this.ifOriginal = ifOriginal;
	}


	public String getIfalldocument() {
		return ifalldocument;
	}


	public void setIfalldocument(String ifalldocument) {
		this.ifalldocument = ifalldocument;
	}


	public String getCustdocument() {
		return custdocument;
	}


	public void setCustdocument(String custdocument) {
		this.custdocument = custdocument;
	}


	public String getErrmsg() {
		return errmsg;
	}


	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}


	/**
     * 构造函数
     * brokerno String 经济人代码
     */
    public OpenAccountDto() {
    }
    
    
    public String getInvtp() {
		return invtp;
	}


	public void setInvtp(String invtp) {
		this.invtp = invtp;
	}


	public String getCustno() {
		return custno;
	}

	public void setCustno(String custno) {
		this.custno = custno;
	}

	public String getTradeacco() {
		return tradeacco;
	}

	public void setTradeacco(String tradeacco) {
		this.tradeacco = tradeacco;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getTrustType() {
		return trustType;
	}

	public void setTrustType(String trustType) {
		this.trustType = trustType;
	}

	public String getInvnm() {
		return invnm;
	}

	public void setInvnm(String invnm) {
		this.invnm = invnm;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getContidtpnm() {
		return contidtpnm;
	}

	public void setContidtpnm(String contidtpnm) {
		this.contidtpnm = contidtpnm;
	}

	public String getIdtp() {
		return idtp;
	}

	public void setIdtp(String idtp) {
		this.idtp = idtp;
	}

	public String getNationalitycode() {
		return nationalitycode;
	}

	public void setNationalitycode(String nationalitycode) {
		this.nationalitycode = nationalitycode;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getTano() {
		return tano;
	}

	public void setTano(String tano) {
		this.tano = tano;
	}

	public String getIdvalidate() {
		return idvalidate;
	}

	public void setIdvalidate(String idvalidate) {
		this.idvalidate = idvalidate;
	}

	public String getOpenName() {
		return openName;
	}

	public void setOpenName(String openName) {
		this.openName = openName;
	}

	public String getBankAccoNm() {
		return bankAccoNm;
	}

	public void setBankAccoNm(String bankAccoNm) {
		this.bankAccoNm = bankAccoNm;
	}

	public String getBnkNo() {
		return bnkNo;
	}

	public void setBnkNo(String bnkNo) {
		this.bnkNo = bnkNo;
	}

	public String getBankAcco() {
		return bankAcco;
	}

	public void setBankAcco(String bankAcco) {
		this.bankAcco = bankAcco;
	}

	public String getChangeAmt() {
		return changeAmt;
	}

	public void setChangeAmt(String changeAmt) {
		this.changeAmt = changeAmt;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public String getMelonmd() {
		return melonmd;
	}

	public void setMelonmd(String melonmd) {
		this.melonmd = melonmd;
	}

	public String getCapitalmode() {
		return capitalmode;
	}

	public void setCapitalmode(String capitalmode) {
		this.capitalmode = capitalmode;
	}

	public String getSendingroute() {
		return sendingroute;
	}

	public void setSendingroute(String sendingroute) {
		this.sendingroute = sendingroute;
	}

	public String getPostmode() {
		return postmode;
	}

	public void setPostmode(String postmode) {
		this.postmode = postmode;
	}

	public String getTrustType1() {
		return trustType1;
	}

	public void setTrustType1(String trustType1) {
		this.trustType1 = trustType1;
	}

	public String getTrustType2() {
		return trustType2;
	}

	public void setTrustType2(String trustType2) {
		this.trustType2 = trustType2;
	}

	public String getTrustType3() {
		return trustType3;
	}

	public void setTrustType3(String trustType3) {
		this.trustType3 = trustType3;
	}

	public String getTrustType4() {
		return trustType4;
	}

	public void setTrustType4(String trustType4) {
		this.trustType4 = trustType4;
	}

	public String getTrustType5() {
		return trustType5;
	}

	public void setTrustType5(String trustType5) {
		this.trustType5 = trustType5;
	}

	public String getTrustType6() {
		return trustType6;
	}

	public void setTrustType6(String trustType6) {
		this.trustType6 = trustType6;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getVoccode() {
		return voccode;
	}

	public void setVoccode(String voccode) {
		this.voccode = voccode;
	}

	public String getEdlevel() {
		return edlevel;
	}

	public void setEdlevel(String edlevel) {
		this.edlevel = edlevel;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getMarriedflag() {
		return marriedflag;
	}

	public void setMarriedflag(String marriedflag) {
		this.marriedflag = marriedflag;
	}

	public String getSzsecacc() {
		return szsecacc;
	}

	public void setSzsecacc(String szsecacc) {
		this.szsecacc = szsecacc;
	}

	public String getShsecacc() {
		return shsecacc;
	}

	public void setShsecacc(String shsecacc) {
		this.shsecacc = shsecacc;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getInvest() {
		return invest;
	}

	public void setInvest(String invest) {
		this.invest = invest;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getCheckno() {
		return checkno;
	}

	public void setCheckno(String checkno) {
		this.checkno = checkno;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}


	public String getCheckpwd() {
		return checkpwd;
	}


	public void setCheckpwd(String checkpwd) {
		this.checkpwd = checkpwd;
	}


	public String getOperatorId() {
		return operatorId;
	}


	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}


	public String getAcctabbr() {
		return acctabbr;
	}


	public void setAcctabbr(String acctabbr) {
		this.acctabbr = acctabbr;
	}


	public String getInstrepnm() {
		return instrepnm;
	}


	public void setInstrepnm(String instrepnm) {
		this.instrepnm = instrepnm;
	}


	public String getInstrepidtp() {
		return instrepidtp;
	}


	public void setInstrepidtp(String instrepidtp) {
		this.instrepidtp = instrepidtp;
	}


	public String getInstrepidno() {
		return instrepidno;
	}


	public void setInstrepidno(String instrepidno) {
		this.instrepidno = instrepidno;
	}


	public String getIndustryType() {
		return industryType;
	}


	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}


	public String getCorpropertiy() {
		return corpropertiy;
	}


	public void setCorpropertiy(String corpropertiy) {
		this.corpropertiy = corpropertiy;
	}


	public String getRegistCapital() {
		return registCapital;
	}


	public void setRegistCapital(String registCapital) {
		this.registCapital = registCapital;
	}


	public String getInstrepvalidate() {
		return instrepvalidate;
	}


	public void setInstrepvalidate(String instrepvalidate) {
		this.instrepvalidate = instrepvalidate;
	}


	public String getFundacct() {
		return fundacct;
	}


	public void setFundacct(String fundacct) {
		this.fundacct = fundacct;
	}


	public double getAvailableBalance() {
		return availableBalance;
	}


	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}


	public String getDsapkind() {
		return dsapkind;
	}


	public void setDsapkind(String dsapkind) {
		this.dsapkind = dsapkind;
	}


	public String getNetpoint() {
		return netpoint;
	}


	public void setNetpoint(String netpoint) {
		this.netpoint = netpoint;
	}


	public String getCheckst() {
		return checkst;
	}


	public void setCheckst(String checkst) {
		this.checkst = checkst;
	}


	public String getChecker() {
		return checker;
	}


	public void setChecker(String checker) {
		this.checker = checker;
	}


	public String getOldpassword() {
		return oldpassword;
	}


	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}


	public String getWarrantyvalidate() {
		return warrantyvalidate;
	}


	public void setWarrantyvalidate(String warrantyvalidate) {
		this.warrantyvalidate = warrantyvalidate;
	}


	public String getAmlrisktype() {
		return amlrisktype;
	}


	public void setAmlrisktype(String amlrisktype) {
		this.amlrisktype = amlrisktype;
	}


	public String getOrganizationno() {
		return organizationno;
	}


	public void setOrganizationno(String organizationno) {
		this.organizationno = organizationno;
	}


	public String getOrnovalidate() {
		return ornovalidate;
	}


	public void setOrnovalidate(String ornovalidate) {
		this.ornovalidate = ornovalidate;
	}


	public String getTaxno() {
		return taxno;
	}


	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}


	public String getQualificationtp() {
		return qualificationtp;
	}


	public void setQualificationtp(String qualificationtp) {
		this.qualificationtp = qualificationtp;
	}


	public String getQualificationno() {
		return qualificationno;
	}


	public void setQualificationno(String qualificationno) {
		this.qualificationno = qualificationno;
	}


	public String getQlfvalidate() {
		return qlfvalidate;
	}


	public void setQlfvalidate(String qlfvalidate) {
		this.qlfvalidate = qlfvalidate;
	}


	public String getHoldingname() {
		return holdingname;
	}


	public void setHoldingname(String holdingname) {
		this.holdingname = holdingname;
	}


	public String getHoldingidtp() {
		return holdingidtp;
	}


	public void setHoldingidtp(String holdingidtp) {
		this.holdingidtp = holdingidtp;
	}


	public String getHoldingidno() {
		return holdingidno;
	}


	public void setHoldingidno(String holdingidno) {
		this.holdingidno = holdingidno;
	}


	public String getHoldingvalidate() {
		return holdingvalidate;
	}


	public void setHoldingvalidate(String holdingvalidate) {
		this.holdingvalidate = holdingvalidate;
	}


	public String getHoldingorno() {
		return holdingorno;
	}


	public void setHoldingorno(String holdingorno) {
		this.holdingorno = holdingorno;
	}


	public String getHoldingtaxno() {
		return holdingtaxno;
	}


	public void setHoldingtaxno(String holdingtaxno) {
		this.holdingtaxno = holdingtaxno;
	}


	public String getRecipientsname() {
		return recipientsname;
	}


	public void setRecipientsname(String recipientsname) {
		this.recipientsname = recipientsname;
	}


	public String getRecipientsidtp() {
		return recipientsidtp;
	}


	public void setRecipientsidtp(String recipientsidtp) {
		this.recipientsidtp = recipientsidtp;
	}


	public String getRecipientsidno() {
		return recipientsidno;
	}


	public void setRecipientsidno(String recipientsidno) {
		this.recipientsidno = recipientsidno;
	}


	public String getRecipientsvalidt() {
		return recipientsvalidt;
	}


	public void setRecipientsvalidt(String recipientsvalidt) {
		this.recipientsvalidt = recipientsvalidt;
	}


	public String getPrincipalname() {
		return principalname;
	}


	public void setPrincipalname(String principalname) {
		this.principalname = principalname;
	}


	public String getPrincipalidtp() {
		return principalidtp;
	}


	public void setPrincipalidtp(String principalidtp) {
		this.principalidtp = principalidtp;
	}


	public String getPrincipalidno() {
		return principalidno;
	}


	public void setPrincipalidno(String principalidno) {
		this.principalidno = principalidno;
	}


	public String getPrincipalvalidt() {
		return principalvalidt;
	}


	public void setPrincipalvalidt(String principalvalidt) {
		this.principalvalidt = principalvalidt;
	}


	public String getBeneficiary() {
		return beneficiary;
	}


	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
	}


	public String getBusinessrange() {
		return businessrange;
	}


	public void setBusinessrange(String businessrange) {
		this.businessrange = businessrange;
	}


	public String getFxqremark() {
		return fxqremark;
	}


	public void setFxqremark(String fxqremark) {
		this.fxqremark = fxqremark;
	}


	public String getCustrisklevl() {
		return custrisklevl;
	}


	public void setCustrisklevl(String custrisklevl) {
		this.custrisklevl = custrisklevl;
	}


	public String getCustriskscore() {
		return custriskscore;
	}


	public void setCustriskscore(String custriskscore) {
		this.custriskscore = custriskscore;
	}

	public String getProvincecode() {
		return provincecode;
	}
	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}


	public double getFromzenbal() {
		return fromzenbal;
	}


	public void setFromzenbal(double fromzenbal) {
		this.fromzenbal = fromzenbal;
	}



	public String getOpenAddr() {
		return openAddr;
	}


	public void setOpenAddr(String openAddr) {
		this.openAddr = openAddr;
	}


	public String getTradeCount() {
		return tradeCount;
	}


	public void setTradeCount(String tradeCount) {
		this.tradeCount = tradeCount;
	}



	public void setBusinesstp(String businesstp) {
		this.businesstp = businesstp;
	}


	public String getCompanytp() {
		return companytp;
	}


	public void setCompanytp(String companytp) {
		this.companytp = companytp;
	}


	public String getRegiontp() {
		return regiontp;
	}


	public void setRegiontp(String regiontp) {
		this.regiontp = regiontp;
	}

	public String getContactgrant() {
		return contactgrant;
	}


	public void setContactgrant(String contactgrant) {
		this.contactgrant = contactgrant;
	}


	public String getContact() {
		return contact;
	}


	public void setContact(String contact) {
		this.contact = contact;
	}


	public String getContactnation() {
		return contactnation;
	}


	public void setContactnation(String contactnation) {
		this.contactnation = contactnation;
	}


	public String getContidtp() {
		return contidtp;
	}


	public void setContidtp(String contidtp) {
		this.contidtp = contidtp;
	}


	public String getContidno() {
		return contidno;
	}


	public void setContidno(String contidno) {
		this.contidno = contidno;
	}


	public String getContvalidate() {
		return contvalidate;
	}


	public void setContvalidate(String contvalidate) {
		this.contvalidate = contvalidate;
	}


	public String getContphone() {
		return contphone;
	}


	public void setContphone(String contphone) {
		this.contphone = contphone;
	}


	public String getContfax() {
		return contfax;
	}


	public void setContfax(String contfax) {
		this.contfax = contfax;
	}


	public String getContmobile() {
		return contmobile;
	}


	public void setContmobile(String contmobile) {
		this.contmobile = contmobile;
	}


	public String getContemail() {
		return contemail;
	}


	public void setContemail(String contemail) {
		this.contemail = contemail;
	}


	public String getOcontactgrant() {
		return ocontactgrant;
	}


	public void setOcontactgrant(String ocontactgrant) {
		this.ocontactgrant = ocontactgrant;
	}


	public String getOcontact() {
		return ocontact;
	}


	public void setOcontact(String ocontact) {
		this.ocontact = ocontact;
	}


	public String getOcontactnation() {
		return ocontactnation;
	}


	public void setOcontactnation(String ocontactnation) {
		this.ocontactnation = ocontactnation;
	}


	public String getOcontidtp() {
		return ocontidtp;
	}


	public void setOcontidtp(String ocontidtp) {
		this.ocontidtp = ocontidtp;
	}


	public String getOcontidno() {
		return ocontidno;
	}


	public void setOcontidno(String ocontidno) {
		this.ocontidno = ocontidno;
	}


	public String getOcontvalidate() {
		return ocontvalidate;
	}


	public void setOcontvalidate(String ocontvalidate) {
		this.ocontvalidate = ocontvalidate;
	}


	public String getOcontphone() {
		return ocontphone;
	}


	public void setOcontphone(String ocontphone) {
		this.ocontphone = ocontphone;
	}


	public String getOcontfax() {
		return ocontfax;
	}


	public void setOcontfax(String ocontfax) {
		this.ocontfax = ocontfax;
	}


	public String getOcontmobile() {
		return ocontmobile;
	}


	public void setOcontmobile(String ocontmobile) {
		this.ocontmobile = ocontmobile;
	}


	public String getOcontemail() {
		return ocontemail;
	}


	public void setOcontemail(String ocontemail) {
		this.ocontemail = ocontemail;
	}


	public String getOidtp() {
		return oidtp;
	}


	public void setOidtp(String oidtp) {
		this.oidtp = oidtp;
	}


	public String getOidno() {
		return oidno;
	}


	public void setOidno(String oidno) {
		this.oidno = oidno;
	}


	public String getOidvalidate() {
		return oidvalidate;
	}


	public void setOidvalidate(String oidvalidate) {
		this.oidvalidate = oidvalidate;
	}


	public String getInstrepnation() {
		return instrepnation;
	}


	public void setInstrepnation(String instrepnation) {
		this.instrepnation = instrepnation;
	}


	public String getPrincipalnation() {
		return principalnation;
	}


	public void setPrincipalnation(String principalnation) {
		this.principalnation = principalnation;
	}


	public String getHousetel() {
		return housetel;
	}


	public void setHousetel(String housetel) {
		this.housetel = housetel;
	}


	public String getFaxdelegate() {
		return faxdelegate;
	}


	public void setFaxdelegate(String faxdelegate) {
		this.faxdelegate = faxdelegate;
	}


	public String getDocbusinesstp() {
		return docbusinesstp;
	}


	public void setDocbusinesstp(String docbusinesstp) {
		this.docbusinesstp = docbusinesstp;
	}


	public String getInstrepcode() {
		return instrepcode;
	}


	public void setInstrepcode(String instrepcode) {
		this.instrepcode = instrepcode;
	}


	public String getCustabbrcode() {
		return custabbrcode;
	}


	public void setCustabbrcode(String custabbrcode) {
		this.custabbrcode = custabbrcode;
	}


	public String getCustinstreprcode() {
		return custinstreprcode;
	}


	public void setCustinstreprcode(String custinstreprcode) {
		this.custinstreprcode = custinstreprcode;
	}



	public String getContAddr() {
		return contAddr;
	}


	public void setContAddr(String contAddr) {
		this.contAddr = contAddr;
	}


	public String getContPostcode() {
		return contPostcode;
	}


	public void setContPostcode(String contPostcode) {
		this.contPostcode = contPostcode;
	}


	public String getOcontAddr() {
		return ocontAddr;
	}


	public void setOcontAddr(String ocontAddr) {
		this.ocontAddr = ocontAddr;
	}


	public String getOcontPostcode() {
		return ocontPostcode;
	}


	public void setOcontPostcode(String ocontPostcode) {
		this.ocontPostcode = ocontPostcode;
	}


	public String getOpenBankCity() {
		return openBankCity;
	}


	public void setOpenBankCity(String openBankCity) {
		this.openBankCity = openBankCity;
	}


	public String getIsupload() {
		return isupload;
	}


	public void setIsupload(String isupload) {
		this.isupload = isupload;
	}


	public String getRemarkinfo() {
		return remarkinfo;
	}


	public void setRemarkinfo(String remarkinfo) {
		this.remarkinfo = remarkinfo;
	}


	public String getOpentype() {
		return opentype;
	}


	public void setOpentype(String opentype) {
		this.opentype = opentype;
	}


	public String getFundacc() {
		return fundacc;
	}


	public void setFundacc(String fundacc) {
		this.fundacc = fundacc;
	}

	public List<TradeAccDto> getTradeAccoList() {
		return tradeAccoList;
	}

	public void setTradeAccoList(List<TradeAccDto> tradeAccoList) {
		this.tradeAccoList = tradeAccoList;
	}

	public List<FundAcctDto> getFundAccoList() {
		return fundAccoList;
	}

	public void setFundAccoList(List<FundAcctDto> fundAccoList) {
		this.fundAccoList = fundAccoList;
	}

	public String getOserialno() {
		return oserialno;
	}

	public void setOserialno(String oserialno) {
		this.oserialno = oserialno;
	}

	public List<ContactDto> getContList() {
		return contList;
	}

	public void setContList(List<ContactDto> contList) {
		this.contList = contList;
	}

	public List<ContactDto> getOidList() {
		return oidList;
	}

	public void setOidList(List<ContactDto> oidList) {
		this.oidList = oidList;
	}

	public List<FundBalanceDto> getFundbalanceList() {
		return fundbalanceList;
	}

	public void setFundbalanceList(List<FundBalanceDto> fundbalanceList) {
		this.fundbalanceList = fundbalanceList;
	}

	public List<OpenAccountDto> getDocumentlist() {
		return documentlist;
	}

	public void setDocumentlist(List<OpenAccountDto> documentlist) {
		this.documentlist = documentlist;
	}

	public List<IDInfoDto> getOidlist() {
		return oidlist;
	}

	public void setOidlist(List<IDInfoDto> oidlist) {
		this.oidlist = oidlist;
	}

	public List<ContactInfoDto> getOcontactlist() {
		return ocontactlist;
	}

	public void setOcontactlist(List<ContactInfoDto> ocontactlist) {
		this.ocontactlist = ocontactlist;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBusinesstp() {
		return businesstp;
	}

	public String getExistsflag() {
		return existsflag;
	}


	public void setExistsflag(String existsflag) {
		this.existsflag = existsflag;
	}

	public String getInvprtp() {
		return invprtp;
	}

	public void setInvprtp(String invprtp) {
		this.invprtp = invprtp;
	}

	public String getSpecriskLevel() {
		return specriskLevel;
	}

	public void setSpecriskLevel(String specriskLevel) {
		this.specriskLevel = specriskLevel;
	}

	public String getNewDocStr() {
		return newDocStr;
	}

	public void setNewDocStr(String newDocStr) {
		this.newDocStr = newDocStr;
	}

	public String getNewOcontStr() {
		return newOcontStr;
	}

	public void setNewOcontStr(String newOcontStr) {
		this.newOcontStr = newOcontStr;
	}

	public String getNewOidStr() {
		return newOidStr;
	}

	public void setNewOidStr(String newOidStr) {
		this.newOidStr = newOidStr;
	}

	public String getTaxresident() {
		return taxresident;
	}

	public void setTaxresident(String taxresident) {
		this.taxresident = taxresident;
	}
	public String getInvtpnm() {
		return invtpnm;
	}

	public void setInvtpnm(String invtpnm) {
		this.invtpnm = invtpnm;
	}
	public String getIdtpnm() {
		return idtpnm;
	}

	public void setIdtpnm(String idtpnm) {
		this.idtpnm = idtpnm;
	}

	public String getDsapkindnm() {
		return dsapkindnm;
	}

	public void setDsapkindnm(String dsapkindnm) {
		this.dsapkindnm = dsapkindnm;
	}

	public String getDocbusinesstpnm() {
		return docbusinesstpnm;
	}

	public void setDocbusinesstpnm(String docbusinesstpnm) {
		this.docbusinesstpnm = docbusinesstpnm;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public String getFundacco() {
		return fundacco;
	}

	public void setFundacco(String fundacco) {
		this.fundacco = fundacco;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getCustsimpnm() {
		return custsimpnm;
	}

	public void setCustsimpnm(String custsimpnm) {
		this.custsimpnm = custsimpnm;
	}

	public String getInvprtpnm() {
		return invprtpnm;
	}

	public void setInvprtpnm(String invprtpnm) {
		this.invprtpnm = invprtpnm;
	}

	public String getCustrisklevlnm() {
		return custrisklevlnm;
	}

	public void setCustrisklevlnm(String custrisklevlnm) {
		this.custrisklevlnm = custrisklevlnm;
	}

	@Override
	public String toString() {
		return "OpenAccountDto [invprtp=" + invprtp + ", operatorId="
				+ operatorId + ", permissionId=" + permissionId
				+ ", tradeCount=" + tradeCount + ", tradeAccoList="
				+ tradeAccoList + ", fundAccoList=" + fundAccoList
				+ ", serialno=" + serialno + ", oserialno=" + oserialno
				+ ", trustType=" + trustType + ", invnm=" + invnm + ", invtp="
				+ invtp + ", invtpnm=" + invtpnm + ", sex=" + sex + ", idtp="
				+ idtp + ", idtpnm=" + idtpnm + ", nationalitycode="
				+ nationalitycode + ", idno=" + idno + ", tano=" + tano
				+ ", idvalidate=" + idvalidate + ", password=" + password
				+ ", oldpassword=" + oldpassword + ", openName=" + openName
				+ ", openAddr=" + openAddr + ", openBankCity=" + openBankCity
				+ ", bankAccoNm=" + bankAccoNm + ", bnkNo=" + bnkNo
				+ ", bankAcco=" + bankAcco + ", changeAmt=" + changeAmt
				+ ", postcode=" + postcode + ", tel=" + tel + ", housetel="
				+ housetel + ", fax=" + fax + ", faxdelegate=" + faxdelegate
				+ ", mobile=" + mobile + ", addr=" + addr + ", email=" + email
				+ ", cast=" + cast + ", melonmd=" + melonmd + ", capitalmode="
				+ capitalmode + ", sendingroute=" + sendingroute
				+ ", postmode=" + postmode + ", trustType1=" + trustType1
				+ ", trustType2=" + trustType2 + ", trustType3=" + trustType3
				+ ", trustType4=" + trustType4 + ", trustType5=" + trustType5
				+ ", trustType6=" + trustType6 + ", birthday=" + birthday
				+ ", voccode=" + voccode + ", edlevel=" + edlevel + ", income="
				+ income + ", marriedflag=" + marriedflag + ", szsecacc="
				+ szsecacc + ", shsecacc=" + shsecacc + ", citycode="
				+ citycode + ", provincecode=" + provincecode + ", broker="
				+ broker + ", percent=" + percent + ", seller=" + seller
				+ ", invest=" + invest + ", explain=" + explain + ", contList="
				+ contList + ", oidList=" + oidList + ", checkno=" + checkno
				+ ", checkpwd=" + checkpwd + ", custrisklevl=" + custrisklevl
				+ ", specriskLevel=" + specriskLevel + ", custriskscore="
				+ custriskscore + ", custriskdate=" + custriskdate
				+ ", custrisktime=" + custrisktime + ", acctabbr=" + acctabbr
				+ ", instrepcode=" + instrepcode + ", instrepnm=" + instrepnm
				+ ", instrepidtp=" + instrepidtp + ", instrepidno="
				+ instrepidno + ", industryType=" + industryType
				+ ", corpropertiy=" + corpropertiy + ", registCapital="
				+ registCapital + ", instrepvalidate=" + instrepvalidate
				+ ", instrepnation=" + instrepnation + ", availableBalance="
				+ availableBalance + ", fromzenbal=" + fromzenbal
				+ ", dsapkind=" + dsapkind + ", netpoint=" + netpoint
				+ ", fundacct=" + fundacct + ", custno=" + custno
				+ ", tradeacco=" + tradeacco + ", fundbalanceList="
				+ fundbalanceList + ", checkst=" + checkst + ", checker="
				+ checker + ", warrantyvalidate=" + warrantyvalidate
				+ ", amlrisktype=" + amlrisktype + ", organizationno="
				+ organizationno + ", ornovalidate=" + ornovalidate
				+ ", taxno=" + taxno + ", qualificationtp=" + qualificationtp
				+ ", qualificationno=" + qualificationno + ", qlfvalidate="
				+ qlfvalidate + ", holdingname=" + holdingname
				+ ", holdingidtp=" + holdingidtp + ", holdingidno="
				+ holdingidno + ", holdingvalidate=" + holdingvalidate
				+ ", holdingorno=" + holdingorno + ", holdingtaxno="
				+ holdingtaxno + ", recipientsname=" + recipientsname
				+ ", recipientsidtp=" + recipientsidtp + ", recipientsidno="
				+ recipientsidno + ", recipientsvalidt=" + recipientsvalidt
				+ ", principalname=" + principalname + ", principalidtp="
				+ principalidtp + ", principalidno=" + principalidno
				+ ", principalvalidt=" + principalvalidt + ", principalnation="
				+ principalnation + ", beneficiary=" + beneficiary
				+ ", businessrange=" + businessrange + ", fxqremark="
				+ fxqremark + ", businesstp=" + businesstp + ", docbusinesstp="
				+ docbusinesstp + ", companytp=" + companytp + ", regiontp="
				+ regiontp + ", contactgrant=" + contactgrant + ", contact="
				+ contact + ", contactnation=" + contactnation + ", contidtp="
				+ contidtp + ", contidtpnm=" + contidtpnm + ", contidno="
				+ contidno + ", contvalidate=" + contvalidate + ", contphone="
				+ contphone + ", contfax=" + contfax + ", contmobile="
				+ contmobile + ", contemail=" + contemail + ", contAddr="
				+ contAddr + ", contPostcode=" + contPostcode
				+ ", ocontactgrant=" + ocontactgrant + ", ocontact=" + ocontact
				+ ", ocontactnation=" + ocontactnation + ", ocontidtp="
				+ ocontidtp + ", ocontidno=" + ocontidno + ", ocontvalidate="
				+ ocontvalidate + ", ocontphone=" + ocontphone + ", ocontfax="
				+ ocontfax + ", ocontmobile=" + ocontmobile + ", ocontemail="
				+ ocontemail + ", ocontAddr=" + ocontAddr + ", ocontPostcode="
				+ ocontPostcode + ", oidtp=" + oidtp + ", oidno=" + oidno
				+ ", oidvalidate=" + oidvalidate + ", errcode=" + errcode
				+ ", errmsg=" + errmsg + ", custabbrcode=" + custabbrcode
				+ ", custinstreprcode=" + custinstreprcode + ", ifsaved="
				+ ifsaved + ", ifOriginal=" + ifOriginal + ", ifalldocument="
				+ ifalldocument + ", isupload=" + isupload + ", remarkinfo="
				+ remarkinfo + ", custdocument=" + custdocument
				+ ", existsflag=" + existsflag + ", isscan=" + isscan
				+ ", keepaddress=" + keepaddress + ", salesaccmanager="
				+ salesaccmanager + ", fileno=" + fileno + ", opentype="
				+ opentype + ", fundacc=" + fundacc + ", documentlist="
				+ documentlist + ", oidlist=" + oidlist + ", ocontactlist="
				+ ocontactlist + ", modifylist=" + modifylist + ", organType="
				+ organType + ", flag=" + flag + ", apdt=" + apdt
				+ ", begindate=" + begindate + ", enddate=" + enddate
				+ ", custinfoid=" + custinfoid + ", aptype=" + aptype
				+ ", appserialno=" + appserialno + ", taxType=" + taxType
				+ ", taxTypeDecl=" + taxTypeDecl + ", investProInstType="
				+ investProInstType + ", investProInstSecond="
				+ investProInstSecond + ", oneYearEndNetAsset="
				+ oneYearEndNetAsset + ", oneYearEndFinAsset="
				+ oneYearEndFinAsset + ", investExperience=" + investExperience
				+ ", negativeNotFinaInst=" + negativeNotFinaInst
				+ ", controlPerTaxDecl=" + controlPerTaxDecl
				+ ", threeAnnualIncome=" + threeAnnualIncome
				+ ", financialAsset=" + financialAsset + ", indInvExperience="
				+ indInvExperience + ", relatedWorkExp=" + relatedWorkExp
				+ ", finProfessions=" + finProfessions + ", voiceRecord="
				+ voiceRecord + ", attr1=" + attr1 + ", attr2=" + attr2
				+ ", attr3=" + attr3 + ", newDocStr=" + newDocStr
				+ ", newOcontStr=" + newOcontStr + ", newOidStr=" + newOidStr
				+ ", taxresident=" + taxresident + ", baseInfo=" + baseInfo
				+ ", accountType=" + accountType + ", custType=" + custType
				+ ", fundacco=" + fundacco + ", option=" + option
				+ ", custsimpnm=" + custsimpnm + "]";
	}


}
