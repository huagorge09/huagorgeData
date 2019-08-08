package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * 账户类复核查询实体类
 * @author ex-chenbq
 *
 */
@Alias("tradeVo")
public class TradeDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String accptmd;         //委托方式  2010-02-05
	
	private String operatorId; 		//操作员代码
	private String permissionId; 	//权限代码
	private String checkno; 		//授权主管编号
	
	private String serialno; 		//流水号
	private String oldserialno; 	//原申请编号
	private String custno; 			//客户号
	private String tradeacco; 		//交易账号
	private String fundacco; 		//基金账号
	private String trustType; 		//委托方式
	private String workdate; 		//申请日期
	private double subAmt;			//申请金额
	private double subQuty;			//申请份额
	private double availableBalance;//可用金额
	private String fundid;			//基金代码
	private String ofundid;			//对方基金代码
	private String currencytype;	//币种
	private String broker;			//经纪人
	private String largeflag;		//巨额赎回
	private String melonmd;			//分红方式
	private double melonpercent;	//分红比例
	private String dsapkind;		//业务类型
	private String dsapkindnm;		//业务类型
	
	private String vouchertype;		//票据类型
	private String voucherdt;		//票据日期
	private String openName; 		//开户银行
	private String bankAccoNm; 		//银行户名
	private String bnkNo; 			//银行编号
	private String bankAcco; 		//银行账号
	private String receivebnkAcco; 	//收款银行账号
	private String openAddr;        //开户地址（省）
	private String openBankCity;	//开户地址（市）
	
	private String contact; 		//经办人
	private String contidtp; 		//经办人证件类型
	private String contidtpnm; 		//经办人证件类型
	private String contidno; 		//经办人证件号码
	private String invnm; 			//客户姓名
	private String invtp; 			//客户类别
	private String invtpnm; 			//客户类别
	private String idtp; 			//证件类型
	private String idtpnm; 			//证件类型
	private String idno; 			//证件号码
	
	private String seatno; 			//销售机构代码
	private String netpoint; 		//网点号
	private String managedSwitchType; //转托管类别：1，单步；2，转出；3，转入；4，内部
	private String OTradeacco; 		//对方交易账号
	
	private String checkFlag;//复核标记
	private String checkFlagNM;//复核标记
	
	private String errcode;			//错误代码
	private String errmsg;			//错误信息
	
	private double fromzenbal; //冻结金额
	
	private String switchInShare;//转入份额
	private String tano;//TA代码
	private String applyst;//申请状态
	private String applystnm;//申请状态
	
	private String ncount;		//未复核数
	private String ycount;		//已复核数
	private String fcount;		//复核放弃数
	private String ccount;		//复核驳回数
	private String cancelcount;//撤单数
	private String totalcount;//总数
	private String excelrowid;//excel的行号
	private String isErrrow;//是否是错误行
	private String custname;
	private String fundname;
	private String dsapkindName;
	private String strSubAmt;
	private String strsubQuty;
	private String strlargeflag;
	
	
	private String contractsign;//合同签署
	private String isoriginal;//合同是否原件
    private String istradeform;//交易表单是否原件
	private String contractsignName;//合同签署
	private String isoriginalName;//合同是否原件
    private String istradeformName;//交易表单是否原件
    private String tradeIsNull;//判断交易账号是否为空
	
    
    
    private String voiceRecord;
    private String riskSign;
    
	public String getApplystnm() {
		return applystnm;
	}

	public void setApplystnm(String applystnm) {
		this.applystnm = applystnm;
	}

	public String getTradeIsNull() {
		return tradeIsNull;
	}

	public void setTradeIsNull(String tradeIsNull) {
		this.tradeIsNull = tradeIsNull;
	}

	public String getContractsignName() {
		return contractsignName;
	}

	public void setContractsignName(String contractsignName) {
		this.contractsignName = contractsignName;
	}

	public String getIsoriginalName() {
		return isoriginalName;
	}

	public void setIsoriginalName(String isoriginalName) {
		this.isoriginalName = isoriginalName;
	}

	public String getIstradeformName() {
		return istradeformName;
	}

	public void setIstradeformName(String istradeformName) {
		this.istradeformName = istradeformName;
	}

	public String getContractsign() {
		return contractsign;
	}

	public void setContractsign(String contractsign) {
		this.contractsign = contractsign;
	}

	public String getIsoriginal() {
		return isoriginal;
	}

	public void setIsoriginal(String isoriginal) {
		this.isoriginal = isoriginal;
	}

	public String getIstradeform() {
		return istradeform;
	}

	public void setIstradeform(String istradeform) {
		this.istradeform = istradeform;
	}

	public String getStrSubAmt() {
		return strSubAmt;
	}

	public void setStrSubAmt(String strSubAmt) {
		this.strSubAmt = strSubAmt;
	}

	public String getStrsubQuty() {
		return strsubQuty;
	}

	public void setStrsubQuty(String strsubQuty) {
		this.strsubQuty = strsubQuty;
	}

	public String getStrlargeflag() {
		return strlargeflag;
	}

	public void setStrlargeflag(String strlargeflag) {
		this.strlargeflag = strlargeflag;
	}

	public String getDsapkindName() {
		return dsapkindName;
	}

	public void setDsapkindName(String dsapkindName) {
		this.dsapkindName = dsapkindName;
	}

	public String getFundname() {
		return fundname;
	}

	public void setFundname(String fundname) {
		this.fundname = fundname;
	}

	public String getCustname() {
		return custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public String getIsErrrow() {
		return isErrrow;
	}

	public void setIsErrrow(String isErrrow) {
		this.isErrrow = isErrrow;
	}

	public String getExcelrowid() {
		return excelrowid;
	}

	public void setExcelrowid(String excelrowid) {
		this.excelrowid = excelrowid;
	}

	/**
	 * @return the fromzenbal
	 */
	public double getFromzenbal() {
		return fromzenbal;
	}

	/**
	 * @param fromzenbal the fromzenbal to set
	 */
	public void setFromzenbal(double fromzenbal) {
		this.fromzenbal = fromzenbal;
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

	public TradeDto(){
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
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

	public String getTrustType() {
		return trustType;
	}

	public void setTrustType(String trustType) {
		this.trustType = trustType;
	}

	public double getSubAmt() {
		return subAmt;
	}

	public void setSubAmt(double subAmt) {
		this.subAmt = subAmt;
	}

	public String getFundid() {
		return fundid;
	}

	public void setFundid(String fundid) {
		this.fundid = fundid;
	}

	public String getCurrencytype() {
		return currencytype;
	}

	public void setCurrencytype(String currencytype) {
		this.currencytype = currencytype;
	}

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

	public String getLargeflag() {
		return largeflag;
	}

	public void setLargeflag(String largeflag) {
		this.largeflag = largeflag;
	}

	public String getMelonmd() {
		return melonmd;
	}

	public void setMelonmd(String melonmd) {
		this.melonmd = melonmd;
	}

	public String getOldserialno() {
		return oldserialno;
	}

	public void setOldserialno(String oldserialno) {
		this.oldserialno = oldserialno;
	}

	public String getOfundid() {
		return ofundid;
	}

	public void setOfundid(String ofundid) {
		this.ofundid = ofundid;
	}

	public double getSubQuty() {
		return subQuty;
	}

	public void setSubQuty(double subQuty) {
		this.subQuty = subQuty;
	}

	public String getVouchertype() {
		return vouchertype;
	}

	public void setVouchertype(String vouchertype) {
		this.vouchertype = vouchertype;
	}

	public String getVoucherdt() {
		return voucherdt;
	}

	public void setVoucherdt(String voucherdt) {
		this.voucherdt = voucherdt;
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

	public String getReceivebnkAcco() {
		return receivebnkAcco;
	}

	public void setReceivebnkAcco(String receivebnkAcco) {
		this.receivebnkAcco = receivebnkAcco;
	}

	public String getCheckno() {
		return checkno;
	}

	public void setCheckno(String checkno) {
		this.checkno = checkno;
	}

	public String getDsapkind() {
		return dsapkind;
	}

	public void setDsapkind(String dsapkind) {
		this.dsapkind = dsapkind;
	}

	public String getFundacco() {
		return fundacco;
	}

	public void setFundacco(String fundacco) {
		this.fundacco = fundacco;
	}

	public String getWorkdate() {
		return workdate;
	}

	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	public String getInvnm() {
		return invnm;
	}

	public void setInvnm(String invnm) {
		this.invnm = invnm;
	}

	public String getInvtp() {
		return invtp;
	}

	public void setInvtp(String invtp) {
		this.invtp = invtp;
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

	public String getSeatno() {
		return seatno;
	}

	public void setSeatno(String seatno) {
		this.seatno = seatno;
	}

	public String getNetpoint() {
		return netpoint;
	}

	public void setNetpoint(String netpoint) {
		this.netpoint = netpoint;
	}

	public String getManagedSwitchType() {
		return managedSwitchType;
	}

	public void setManagedSwitchType(String managedSwitchType) {
		this.managedSwitchType = managedSwitchType;
	}

	public String getOTradeacco() {
		return OTradeacco;
	}

	public void setOTradeacco(String tradeacco) {
		OTradeacco = tradeacco;
	}
	/**
	 * @return the accptmd
	 */
	public String getAccptmd() {
		return accptmd;
	}

	/**
	 * @param accptmd the accptmd to set
	 */
	public void setAccptmd(String accptmd) {
		this.accptmd = accptmd;
	}

	public double getMelonpercent() {
		return melonpercent;
	}

	public void setMelonpercent(double melonpercent) {
		this.melonpercent = melonpercent;
	}

	public String getOpenAddr() {
		return openAddr;
	}

	public void setOpenAddr(String openAddr) {
		this.openAddr = openAddr;
	}

	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getOpenBankCity() {
		return openBankCity;
	}

	public void setOpenBankCity(String openBankCity) {
		this.openBankCity = openBankCity;
	}

	public String getSwitchInShare() {
		return switchInShare;
	}

	public void setSwitchInShare(String switchInShare) {
		this.switchInShare = switchInShare;
	}

	public String getTano() {
		return tano;
	}

	public void setTano(String tano) {
		this.tano = tano;
	}

	public String getNcount() {
		return ncount;
	}

	public void setNcount(String ncount) {
		this.ncount = ncount;
	}

	public String getYcount() {
		return ycount;
	}

	public void setYcount(String ycount) {
		this.ycount = ycount;
	}

	public String getFcount() {
		return fcount;
	}

	public void setFcount(String fcount) {
		this.fcount = fcount;
	}

	public String getCcount() {
		return ccount;
	}

	public void setCcount(String ccount) {
		this.ccount = ccount;
	}

	public String getApplyst() {
		return applyst;
	}

	public void setApplyst(String applyst) {
		this.applyst = applyst;
	}

	public String getCancelcount() {
		return cancelcount;
	}

	public void setCancelcount(String cancelcount) {
		this.cancelcount = cancelcount;
	}

	public String getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(String totalcount) {
		this.totalcount = totalcount;
	}

	public String getVoiceRecord() {
		return voiceRecord;
	}

	public void setVoiceRecord(String voiceRecord) {
		this.voiceRecord = voiceRecord;
	}

	public String getRiskSign() {
		return riskSign;
	}

	public void setRiskSign(String riskSign) {
		this.riskSign = riskSign;
	}

	public String getDsapkindnm() {
		return dsapkindnm;
	}

	public void setDsapkindnm(String dsapkindnm) {
		this.dsapkindnm = dsapkindnm;
	}

	public String getContidtpnm() {
		return contidtpnm;
	}

	public void setContidtpnm(String contidtpnm) {
		this.contidtpnm = contidtpnm;
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

	public String getCheckFlagNM() {
		return checkFlagNM;
	}

	public void setCheckFlagNM(String checkFlagNM) {
		this.checkFlagNM = checkFlagNM;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
