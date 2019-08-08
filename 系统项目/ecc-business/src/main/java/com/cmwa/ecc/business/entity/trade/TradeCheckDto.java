package com.cmwa.ecc.business.entity.trade;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("tradeCheckDto")
public class TradeCheckDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String operatorId; 		//操作员代码
	private String operatorType; 	//操作类型
	private String permissionId; 	//权限代码
	
	private String serialno; 		//流水号
	private String custno; 			//客户号
	private String tradeacco; 		//交易账号
	private String fundacco; 		//基金账号
	private String invnm; 			//客户姓名
	private String bankacco;        //银行账号
	private String fundid; 			//基金代码
	private String fundnm; 			//基金代码
	private String invtp; 			//客户类别
	private String invtpnm; 			//客户类别
	private String dsapkind;		//业务类型
	private String dsapkindnm;		//业务类型
	private String checkst;			//复核状态
	private String trustType; 		//委托方式
	private String trustTypenm; 		//委托方式
	private String broker; 			//经纪人
	private String apdt; 			//申请日期
	private String largeflag; 		//巨额赎回标志
	private String largeflagnm; 		//巨额赎回标志
	private String ofundacco; 		//对方基金账号
	private String otradeacco; 		//对方交易账号
	private String ofundid; 		//对方基金代码
	private String ofundnm; 		//对方基金代码
	private String melonmd; 		//分红方式
	private String melonpercent; 	//分红比例
	private String oseatno; 		//对方销售机构代码
	private String oseatnm; 		//对方销售机构代码
	private String onetpoint; 		//对方网点号
	private String oldserialno; 	//原申请编号
	private String netpoint; 		//网点号
	private String netpointnm; 		//网点号
	private String remark; 			//备注
	private String checker; 		//复核员
	private String checkno; 		//复核员
	private String chkflag; 		//复核员
	private String chkflagnm; 		//复核员
	private String batchoperatorId; 		//复核员
	private String aviable;			//余额
	private String modifytype;		//修改类型
	private String melonmdnm;
	
	public String getMelonmdnm() {
		return melonmdnm;
	}

	public void setMelonmdnm(String melonmdnm) {
		this.melonmdnm = melonmdnm;
	}

	public String getChkflag() {
		return chkflag;
	}

	public void setChkflag(String chkflag) {
		this.chkflag = chkflag;
	}

	public String getBatchoperatorId() {
		return batchoperatorId;
	}

	public void setBatchoperatorId(String batchoperatorId) {
		this.batchoperatorId = batchoperatorId;
	}

	public String getFundnm() {
		return fundnm;
	}

	public void setFundnm(String fundnm) {
		this.fundnm = fundnm;
	}

	private String workdate;        //工作日
	private String subamt; 			//金额
	private String sumsubamt; 			//金额
	private String subamtnm; 			//金额
	private String subquty; 		//份额
	private String sumsubquty; 		//份额
	private String subqutynm; 		//份额
	private String applyst;			//申请状态
	private String applystnm;			//申请状态
	private String avsubquty;	//可用份额
	private String balance;		//实际余额
	private String abnmfrozen;	//冻结份额
	private String ncount;		//未复核数
	private String ycount;		//已复核数
	private String fcount;		//复核放弃数
	private String rccount;     //驳回修改
	private String rfcount;//驳回放弃
	private String ccount;		//复核驳回数
	private String cancelcount;//撤单数
	private String appno;		//申请单编号
	
	private String totalRow;//合计行数
	private String nopcount;//未处理数
	private String begindate;//未处理数
	private String enddate;//未处理数
	
	private String errcode;			//错误代码
	private String errmsg;			//错误信息 
	
	public String getNopcount() {
		return nopcount;
	}

	public void setNopcount(String nopcount) {
		this.nopcount = nopcount;
	}

	public String getRfcount() {
		return rfcount;
	}

	public void setRfcount(String rfcount) {
		this.rfcount = rfcount;
	}

	public String getRccount() {
		return rccount;
	}

	public void setRccount(String rccount) {
		this.rccount = rccount;
	}

	public String getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(String totalRow) {
		this.totalRow = totalRow;
	}

	public String getBankacco() {
		return bankacco;
	}

	public void setBankacco(String bankacco) {
		this.bankacco = bankacco;
	}

	public TradeCheckDto(){
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

	public String getFundacco() {
		return fundacco;
	}

	public void setFundacco(String fundacco) {
		this.fundacco = fundacco;
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

	public String getDsapkind() {
		return dsapkind;
	}

	public void setDsapkind(String dsapkind) {
		this.dsapkind = dsapkind;
	}

	public String getCheckst() {
		return checkst;
	}

	public void setCheckst(String checkst) {
		this.checkst = checkst;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
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

	public String getFundid() {
		return fundid;
	}

	public void setFundid(String fundid) {
		this.fundid = fundid;
	}


	public String getTrustType() {
		return trustType;
	}

	public void setTrustType(String trustType) {
		this.trustType = trustType;
	}

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

	public String getApdt() {
		return apdt;
	}

	public void setApdt(String apdt) {
		this.apdt = apdt;
	}

	public String getLargeflag() {
		return largeflag;
	}

	public void setLargeflag(String largeflag) {
		this.largeflag = largeflag;
	}

	public String getOfundacco() {
		return ofundacco;
	}

	public void setOfundacco(String ofundacco) {
		this.ofundacco = ofundacco;
	}

	public String getOtradeacco() {
		return otradeacco;
	}

	public void setOtradeacco(String otradeacco) {
		this.otradeacco = otradeacco;
	}

	public String getOseatno() {
		return oseatno;
	}

	public void setOseatno(String oseatno) {
		this.oseatno = oseatno;
	}

	public String getOnetpoint() {
		return onetpoint;
	}

	public void setOnetpoint(String onetpoint) {
		this.onetpoint = onetpoint;
	}

	public String getOldserialno() {
		return oldserialno;
	}

	public void setOldserialno(String oldserialno) {
		this.oldserialno = oldserialno;
	}

	public String getNetpoint() {
		return netpoint;
	}

	public void setNetpoint(String netpoint) {
		this.netpoint = netpoint;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOfundid() {
		return ofundid;
	}

	public void setOfundid(String ofundid) {
		this.ofundid = ofundid;
	}

	public String getMelonmd() {
		return melonmd;
	}

	public void setMelonmd(String melonmd) {
		this.melonmd = melonmd;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public String getCheckno() {
		return checkno;
	}

	public void setCheckno(String checkno) {
		this.checkno = checkno;
	}

	public String getWorkdate() {
		return workdate;
	}
	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}

	public String getMelonpercent() {
		return melonpercent;
	}

	public void setMelonpercent(String melonpercent) {
		this.melonpercent = melonpercent;
	}

	public String getApplyst() {
		return applyst;
	}

	public void setApplyst(String applyst) {
		this.applyst = applyst;
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

	public String getAppno() {
		return appno;
	}

	public void setAppno(String appno) {
		this.appno = appno;
	}

	public String getCancelcount() {
		return cancelcount;
	}

	public void setCancelcount(String cancelcount) {
		this.cancelcount = cancelcount;
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

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public String getApplystnm() {
		return applystnm;
	}

	public void setApplystnm(String applystnm) {
		this.applystnm = applystnm;
	}

	public String getDsapkindnm() {
		return dsapkindnm;
	}

	public void setDsapkindnm(String dsapkindnm) {
		this.dsapkindnm = dsapkindnm;
	}

	public String getSubamt() {
		return subamt;
	}

	public void setSubamt(String subamt) {
		this.subamt = subamt;
	}

	public String getSubquty() {
		return subquty;
	}

	public void setSubquty(String subquty) {
		this.subquty = subquty;
	}

	public String getAvsubquty() {
		return avsubquty;
	}

	public void setAvsubquty(String avsubquty) {
		this.avsubquty = avsubquty;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getAbnmfrozen() {
		return abnmfrozen;
	}

	public void setAbnmfrozen(String abnmfrozen) {
		this.abnmfrozen = abnmfrozen;
	}

	public String getTrustTypenm() {
		return trustTypenm;
	}

	public void setTrustTypenm(String trustTypenm) {
		this.trustTypenm = trustTypenm;
	}

	public String getLargeflagnm() {
		return largeflagnm;
	}

	public void setLargeflagnm(String largeflagnm) {
		this.largeflagnm = largeflagnm;
	}

	public String getOfundnm() {
		return ofundnm;
	}

	public void setOfundnm(String ofundnm) {
		this.ofundnm = ofundnm;
	}

	public String getAviable() {
		return aviable;
	}

	public void setAviable(String aviable) {
		this.aviable = aviable;
	}

	public String getOseatnm() {
		return oseatnm;
	}

	public void setOseatnm(String oseatnm) {
		this.oseatnm = oseatnm;
	}

	public String getNetpointnm() {
		return netpointnm;
	}

	public void setNetpointnm(String netpointnm) {
		this.netpointnm = netpointnm;
	}

	public String getInvtpnm() {
		return invtpnm;
	}

	public void setInvtpnm(String invtpnm) {
		this.invtpnm = invtpnm;
	}

	public String getSubamtnm() {
		return subamtnm;
	}

	public void setSubamtnm(String subamtnm) {
		this.subamtnm = subamtnm;
	}

	public String getSubqutynm() {
		return subqutynm;
	}

	public void setSubqutynm(String subqutynm) {
		this.subqutynm = subqutynm;
	}

	public String getChkflagnm() {
		return chkflagnm;
	}

	public void setChkflagnm(String chkflagnm) {
		this.chkflagnm = chkflagnm;
	}

	public String getSumsubamt() {
		return sumsubamt;
	}

	public void setSumsubamt(String sumsubamt) {
		this.sumsubamt = sumsubamt;
	}

	public String getSumsubquty() {
		return sumsubquty;
	}

	public void setSumsubquty(String sumsubquty) {
		this.sumsubquty = sumsubquty;
	}

	public String getModifytype() {
		return modifytype;
	}

	public void setModifytype(String modifytype) {
		this.modifytype = modifytype;
	}
}
