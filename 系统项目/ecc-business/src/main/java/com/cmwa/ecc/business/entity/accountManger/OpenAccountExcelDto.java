package com.cmwa.ecc.business.entity.accountManger;

import com.cmwa.ecc.business.utils.excel.ExcelField;

/**
 * 批量开户excel导入反射实体类
 * @author ex-chenbq
 *
 */
public class OpenAccountExcelDto {

	private String transcd = "8001"; //操作代码
	private String serialno; //流水号
	private String trusttp = "3"; //委托方式
	private String tano = "17"; //TA代码 17或98
	private String invTp = "0"; //投资人类型
	@ExcelField(fieldName="投资者名称")
	private String invnm; //开户真实姓名
	@ExcelField(fieldName="证件类型ID")
	private String idtp; //开户证件类型
	@ExcelField(fieldName="注册登记证件类型")
	private String idtpnm; //开户证件类型中文描述
	@ExcelField(fieldName="注册登记证件号码")
	private String idno; //开户证件号码
	@ExcelField(fieldName="注册登记证件有效期")
	private String idnolimit; //开户证件有效期
	private String accountabbr; //投资人简称
	private String passwd; //密码
	@ExcelField(fieldName="开户银行ID")
	private String bankno; //开户银行编号
	@ExcelField(fieldName="开户银行")
	private String banknonm; //开户银行编号
	@ExcelField(fieldName="预留银行全称")
	private String banklongname; //银行全称
	@ExcelField(fieldName="预留银行户名")
	private String bankacnm; //银行账户名
	@ExcelField(fieldName="预留银行账号")
	private String bankacco; //银行账户号
	@ExcelField(fieldName="预留银行开户地ID")
	private String bankaddr; //银行开户地
	@ExcelField(fieldName="预留银行开户地")
	private String bankaddrnm; //银行开户地
	@ExcelField(fieldName="银行市所在地")
	private String bankaddrcity; //银行所在地(市)
	private String provincecode; //所在省份
	private String cityname; //所在城市
	@ExcelField(fieldName="办公地址")
	private String addr; //办公地址
	@ExcelField(fieldName="邮政编码")
	private String postcode; //邮政编码
	private String mobileno; //手机号
	private String email; //e-mail
	private String telno; //固定电话
	private String hometel; //家庭电话
	private String officetel; //办公电话
	private String nation; //国籍
	private String faxno; //传真号码
	private String voccode; //职业代码
	private String edlevel; //学历
	private String income; //年收入
	private String sex; //性别
	private String marriage; //婚姻
	private String birthday; //生日
	private String invest; //证券投资经历
	private String deliverway; //对账单寄送方式
	private String corpropertiy; //企业性质
	private String registcapital; //注册资本
	private String industrytype; //行业类型
	private String instreprname; //法人姓名
	private String instrepridtp; //法人证件类型
	private String instrepridno; //法人证件号码
	private String instrepdate; //法人证件有效期
	private String instreprnation; //法人国籍
	@ExcelField(fieldName="法定代表人姓名")
	private String principalname; //负责人名称
	private String principaltype = "0"; //负责人证件类型
	@ExcelField(fieldName="法定代表人证件号码")
	private String principalno; //负责人证件号码
	@ExcelField(fieldName="法定代表人证件有效期")
	private String principalvalidate; //负责人证件有效期
	private String principalnation = "156"; //负责人国籍
	
	
	@ExcelField(fieldName="经办人姓名")
	private String contact; 		//经办人
	private String contactnation = "156"; //经办人国籍
	private String contidtp = "0"; 		//经办人证件类型
	@ExcelField(fieldName="经办人证件号码")
	private String contidno; 		//经办人证件号码
	@ExcelField(fieldName="经办人证件有效期")
	private String contvalidate; 	//经办人证件有效期
	@ExcelField(fieldName="经办人办公电话")
	private String contphone; 		//经办人电话
	@ExcelField(fieldName="经办人传真号码")
	private String contfax; 		//经办人传真
	@ExcelField(fieldName="经办人手机号")
	private String contmobile; 		//经办人手机
	@ExcelField(fieldName="经办人电子邮件")
	private String contemail; 		//经办人Email
	@ExcelField(fieldName="经办人通讯地址")
	private String contAddr;		//经办人联系地址
	@ExcelField(fieldName="经办人邮政编码")
	private String contPostcode;	//经办人邮编
	
	
	private String tellertrust = "3"; //柜台委托
	private String callcenter; //电话委托
	private String internet; //网上委托
	private String faxtrust; //传真委托
	private String mobiletrust = "3"; //手机委托
	private String othertrust; //其他委托
	private String holdingname; //控股股东名称/账户实际控制人
	private String holdingidtype = "11"; //控股股东证件类型
	private String holdingidno; //控股股东证件号码
	private String holdingidvalidate; //控股股东证件有效期
	@ExcelField(fieldName="基金投资受益人名称")
	private String beneficiary; //基金投资受益人名称
	private String custabbrcode; //客户简称
	private String custinstreprcode; //法人代码
	@ExcelField(fieldName="业务类型ID")
	private String businesstp; //业务类型
	@ExcelField(fieldName="业务类型")
	private String businesstpnm; //业务类型
	private String companytp = "C"; //公司类型
	private String regioncode = "0755"; //地域类型
	private String risklevel = "1"; //风险承受能力
	private String riskscore; //风险评测得分
	private String amlrisktype = "1"; //反洗钱风险等级
	private String opid; //操作员代码
	private String aptype = "AP_02"; //资料信息业务类型
	private String custinfostat = "01||,0||;02||,0||;03||,0||;04||,0||;06||,0"; //资料信息字符串
	private String broke; //经办人信息字符串
	private String otherid; //证件号码信息字符串
	private String isfull = "0"; //是否完整
	private String original = "0"; //是否原件
	private String filed = "0"; //是否归档
	private String amlrisknote; //反洗钱备注
	private String attach = "0"; //是否上传附件
	private String szsecacc; //深交所股东账号
	private String shsecacc; //上交所股东账号
	private String remark; //资料信息备注字段
	private String fundacct; //基金帐号
	private String apkind = "001"; //业务类型
	@ExcelField(fieldName="机构类型ID")
	private String insttype; //机构类型
	@ExcelField(fieldName="机构类型")
	private String insttypenm; //机构类型
	private String isscan = "0"; //是否扫描
	private String keepaddress; //存档位置
	private String salesaccmanager; //所属客户经理
	private String fileno; //文件编号
	@ExcelField(fieldName="投资者类型")
	private String invprtp; //投资者类型
	private String taxtype = "1"; //税收居民身份
	private String taxtypedecl; //税收居民身份声明
	private String investproinsttype = "1"; //专业投资者-机构类型
	@ExcelField(fieldName="机构类型(专业)ID")
	private String investproinstsecond; //专业投资者-机构类型子集
	@ExcelField(fieldName="机构类型(专业)")
	private String investproinstsecondnm; //专业投资者-机构类型子集
	private String oneyearendnetasset; //近1年末净资产
	private String oneyearendfinasset; //近1年末金融资产
	private String investexperience; //机构专业-投资经历
	private String negativenotfinainst; //消极非金融机构
	private String controlpertaxdecl; //控制人税收居民身份声明
	private String threeannualincome; //近三年年均收入
	private String financialasset; //金融资产
	private String indinvexperience; //个人专业-投资经历
	private String relatedworkexp; //相关工作经历
	private String finprofessions; //金融职业
	private String specrisklevel ; //特殊用户风险等级
	private String offinvserialno; //委托人流水号
	
	/**
	 * 返回结果
	 */
	private String outCustNo; //客户号
	private String errCode; //状态码
	private String errMsg;	//状态描述
	
	/**
	 * GET SET
	 * @return
	 */
	public String getTranscd() {
		return transcd;
	}
	public void setTranscd(String transcd) {
		this.transcd = transcd;
	}
	public String getSerialno() {
		return serialno;
	}
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}
	public String getTrusttp() {
		return trusttp;
	}
	public void setTrusttp(String trusttp) {
		this.trusttp = trusttp;
	}
	public String getTano() {
		return tano;
	}
	public void setTano(String tano) {
		this.tano = tano;
	}
	public String getInvTp() {
		return invTp;
	}
	public void setInvTp(String invTp) {
		this.invTp = invTp;
	}
	public String getInvnm() {
		return invnm;
	}
	public void setInvnm(String invnm) {
		this.invnm = invnm;
	}
	public String getIdtp() {
		return idtp;
	}
	public void setIdtp(String idtp) {
		this.idtp = idtp;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getAccountabbr() {
		return accountabbr;
	}
	public void setAccountabbr(String accountabbr) {
		this.accountabbr = accountabbr;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getBankno() {
		return bankno;
	}
	public void setBankno(String bankno) {
		this.bankno = bankno;
	}
	public String getBanklongname() {
		return banklongname;
	}
	public void setBanklongname(String banklongname) {
		this.banklongname = banklongname;
	}
	public String getBankacnm() {
		return bankacnm;
	}
	public void setBankacnm(String bankacnm) {
		this.bankacnm = bankacnm;
	}
	public String getBankacco() {
		return bankacco;
	}
	public void setBankacco(String bankacco) {
		this.bankacco = bankacco;
	}
	public String getBankaddr() {
		return bankaddr;
	}
	public void setBankaddr(String bankaddr) {
		this.bankaddr = bankaddr;
	}
	public String getProvincecode() {
		return provincecode;
	}
	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelno() {
		return telno;
	}
	public void setTelno(String telno) {
		this.telno = telno;
	}
	public String getHometel() {
		return hometel;
	}
	public void setHometel(String hometel) {
		this.hometel = hometel;
	}
	public String getOfficetel() {
		return officetel;
	}
	public void setOfficetel(String officetel) {
		this.officetel = officetel;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getFaxno() {
		return faxno;
	}
	public void setFaxno(String faxno) {
		this.faxno = faxno;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getInvest() {
		return invest;
	}
	public void setInvest(String invest) {
		this.invest = invest;
	}
	public String getIdnolimit() {
		return idnolimit;
	}
	public void setIdnolimit(String idnolimit) {
		this.idnolimit = idnolimit;
	}
	public String getDeliverway() {
		return deliverway;
	}
	public void setDeliverway(String deliverway) {
		this.deliverway = deliverway;
	}
	public String getCorpropertiy() {
		return corpropertiy;
	}
	public void setCorpropertiy(String corpropertiy) {
		this.corpropertiy = corpropertiy;
	}
	public String getRegistcapital() {
		return registcapital;
	}
	public void setRegistcapital(String registcapital) {
		this.registcapital = registcapital;
	}
	public String getIndustrytype() {
		return industrytype;
	}
	public void setIndustrytype(String industrytype) {
		this.industrytype = industrytype;
	}
	public String getInstreprname() {
		return instreprname;
	}
	public void setInstreprname(String instreprname) {
		this.instreprname = instreprname;
	}
	public String getInstrepridtp() {
		return instrepridtp;
	}
	public void setInstrepridtp(String instrepridtp) {
		this.instrepridtp = instrepridtp;
	}
	public String getInstrepridno() {
		return instrepridno;
	}
	public void setInstrepridno(String instrepridno) {
		this.instrepridno = instrepridno;
	}
	public String getInstrepdate() {
		return instrepdate;
	}
	public void setInstrepdate(String instrepdate) {
		this.instrepdate = instrepdate;
	}
	public String getInstreprnation() {
		return instreprnation;
	}
	public void setInstreprnation(String instreprnation) {
		this.instreprnation = instreprnation;
	}
	public String getPrincipalname() {
		return principalname;
	}
	public void setPrincipalname(String principalname) {
		this.principalname = principalname;
	}
	public String getPrincipaltype() {
		return principaltype;
	}
	public void setPrincipaltype(String principaltype) {
		this.principaltype = principaltype;
	}
	public String getPrincipalno() {
		return principalno;
	}
	public void setPrincipalno(String principalno) {
		this.principalno = principalno;
	}
	public String getPrincipalvalidate() {
		return principalvalidate;
	}
	public void setPrincipalvalidate(String principalvalidate) {
		this.principalvalidate = principalvalidate;
	}
	public String getPrincipalnation() {
		return principalnation;
	}
	public void setPrincipalnation(String principalnation) {
		this.principalnation = principalnation;
	}
	public String getTellertrust() {
		return tellertrust;
	}
	public void setTellertrust(String tellertrust) {
		this.tellertrust = tellertrust;
	}
	public String getCallcenter() {
		return callcenter;
	}
	public void setCallcenter(String callcenter) {
		this.callcenter = callcenter;
	}
	public String getInternet() {
		return internet;
	}
	public void setInternet(String internet) {
		this.internet = internet;
	}
	public String getFaxtrust() {
		return faxtrust;
	}
	public void setFaxtrust(String faxtrust) {
		this.faxtrust = faxtrust;
	}
	public String getMobiletrust() {
		return mobiletrust;
	}
	public void setMobiletrust(String mobiletrust) {
		this.mobiletrust = mobiletrust;
	}
	public String getOthertrust() {
		return othertrust;
	}
	public void setOthertrust(String othertrust) {
		this.othertrust = othertrust;
	}
	public String getHoldingname() {
		return holdingname;
	}
	public void setHoldingname(String holdingname) {
		this.holdingname = holdingname;
	}
	public String getHoldingidtype() {
		return holdingidtype;
	}
	public void setHoldingidtype(String holdingidtype) {
		this.holdingidtype = holdingidtype;
	}
	public String getHoldingidno() {
		return holdingidno;
	}
	public void setHoldingidno(String holdingidno) {
		this.holdingidno = holdingidno;
	}
	public String getHoldingidvalidate() {
		return holdingidvalidate;
	}
	public void setHoldingidvalidate(String holdingidvalidate) {
		this.holdingidvalidate = holdingidvalidate;
	}
	public String getBeneficiary() {
		return beneficiary;
	}
	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
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
	public String getBusinesstp() {
		return businesstp;
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
	public String getRegioncode() {
		return regioncode;
	}
	public void setRegioncode(String regioncode) {
		this.regioncode = regioncode;
	}
	public String getRisklevel() {
		return risklevel;
	}
	public void setRisklevel(String risklevel) {
		this.risklevel = risklevel;
	}
	public String getRiskscore() {
		return riskscore;
	}
	public void setRiskscore(String riskscore) {
		this.riskscore = riskscore;
	}
	public String getAmlrisktype() {
		return amlrisktype;
	}
	public void setAmlrisktype(String amlrisktype) {
		this.amlrisktype = amlrisktype;
	}
	public String getOpid() {
		return opid;
	}
	public void setOpid(String opid) {
		this.opid = opid;
	}
	public String getAptype() {
		return aptype;
	}
	public void setAptype(String aptype) {
		this.aptype = aptype;
	}
	public String getCustinfostat() {
		return custinfostat;
	}
	public void setCustinfostat(String custinfostat) {
		this.custinfostat = custinfostat;
	}
	public String getBroke() {
		return broke;
	}
	public void setBroke(String broke) {
		this.broke = broke;
	}
	public String getOtherid() {
		return otherid;
	}
	public void setOtherid(String otherid) {
		this.otherid = otherid;
	}
	public String getIsfull() {
		return isfull;
	}
	public void setIsfull(String isfull) {
		this.isfull = isfull;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	public String getFiled() {
		return filed;
	}
	public void setFiled(String filed) {
		this.filed = filed;
	}
	public String getAmlrisknote() {
		return amlrisknote;
	}
	public void setAmlrisknote(String amlrisknote) {
		this.amlrisknote = amlrisknote;
	}
	public String getBankaddrcity() {
		return bankaddrcity;
	}
	public void setBankaddrcity(String bankaddrcity) {
		this.bankaddrcity = bankaddrcity;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFundacct() {
		return fundacct;
	}
	public void setFundacct(String fundacct) {
		this.fundacct = fundacct;
	}
	public String getApkind() {
		return apkind;
	}
	public void setApkind(String apkind) {
		this.apkind = apkind;
	}
	public String getInsttype() {
		return insttype;
	}
	public void setInsttype(String insttype) {
		this.insttype = insttype;
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
	public String getInvprtp() {
		return invprtp;
	}
	public void setInvprtp(String invprtp) {
		this.invprtp = invprtp;
	}
	public String getTaxtype() {
		return taxtype;
	}
	public void setTaxtype(String taxtype) {
		this.taxtype = taxtype;
	}
	public String getTaxtypedecl() {
		return taxtypedecl;
	}
	public void setTaxtypedecl(String taxtypedecl) {
		this.taxtypedecl = taxtypedecl;
	}
	public String getInvestproinsttype() {
		return investproinsttype;
	}
	public void setInvestproinsttype(String investproinsttype) {
		this.investproinsttype = investproinsttype;
	}
	public String getInvestproinstsecond() {
		return investproinstsecond;
	}
	public void setInvestproinstsecond(String investproinstsecond) {
		this.investproinstsecond = investproinstsecond;
	}
	public String getOneyearendnetasset() {
		return oneyearendnetasset;
	}
	public void setOneyearendnetasset(String oneyearendnetasset) {
		this.oneyearendnetasset = oneyearendnetasset;
	}
	public String getOneyearendfinasset() {
		return oneyearendfinasset;
	}
	public void setOneyearendfinasset(String oneyearendfinasset) {
		this.oneyearendfinasset = oneyearendfinasset;
	}
	public String getInvestexperience() {
		return investexperience;
	}
	public void setInvestexperience(String investexperience) {
		this.investexperience = investexperience;
	}
	public String getNegativenotfinainst() {
		return negativenotfinainst;
	}
	public void setNegativenotfinainst(String negativenotfinainst) {
		this.negativenotfinainst = negativenotfinainst;
	}
	public String getControlpertaxdecl() {
		return controlpertaxdecl;
	}
	public void setControlpertaxdecl(String controlpertaxdecl) {
		this.controlpertaxdecl = controlpertaxdecl;
	}
	public String getThreeannualincome() {
		return threeannualincome;
	}
	public void setThreeannualincome(String threeannualincome) {
		this.threeannualincome = threeannualincome;
	}
	public String getFinancialasset() {
		return financialasset;
	}
	public void setFinancialasset(String financialasset) {
		this.financialasset = financialasset;
	}
	public String getIndinvexperience() {
		return indinvexperience;
	}
	public void setIndinvexperience(String indinvexperience) {
		this.indinvexperience = indinvexperience;
	}
	public String getRelatedworkexp() {
		return relatedworkexp;
	}
	public void setRelatedworkexp(String relatedworkexp) {
		this.relatedworkexp = relatedworkexp;
	}
	public String getFinprofessions() {
		return finprofessions;
	}
	public void setFinprofessions(String finprofessions) {
		this.finprofessions = finprofessions;
	}
	public String getSpecrisklevel() {
		return specrisklevel;
	}
	public void setSpecrisklevel(String specrisklevel) {
		this.specrisklevel = specrisklevel;
	}
	public String getOffinvserialno() {
		return offinvserialno;
	}
	public void setOffinvserialno(String offinvserialno) {
		this.offinvserialno = offinvserialno;
	}
	public String getOutCustNo() {
		return outCustNo;
	}
	public void setOutCustNo(String outCustNo) {
		this.outCustNo = outCustNo;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
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
	public String getIdtpnm() {
		return idtpnm;
	}
	public void setIdtpnm(String idtpnm) {
		this.idtpnm = idtpnm;
	}
	public String getBanknonm() {
		return banknonm;
	}
	public void setBanknonm(String banknonm) {
		this.banknonm = banknonm;
	}
	public String getBankaddrnm() {
		return bankaddrnm;
	}
	public void setBankaddrnm(String bankaddrnm) {
		this.bankaddrnm = bankaddrnm;
	}
	public String getBusinesstpnm() {
		return businesstpnm;
	}
	public void setBusinesstpnm(String businesstpnm) {
		this.businesstpnm = businesstpnm;
	}
	public String getInsttypenm() {
		return insttypenm;
	}
	public void setInsttypenm(String insttypenm) {
		this.insttypenm = insttypenm;
	}
	public String getInvestproinstsecondnm() {
		return investproinstsecondnm;
	}
	public void setInvestproinstsecondnm(String investproinstsecondnm) {
		this.investproinstsecondnm = investproinstsecondnm;
	}
}
