package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("capitalCheckDto")
public class CapitalCheckDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String operatorId; 		//操作员代码
	private String operatorType; 	//操作类型
	private String permissionId; 	//权限代码
	
	private String capitalno; 		//资金流水号
	private String serialno;		//申请编号
	private String custno; 			//客户号
	private String tradeacco; 		//交易账号
	private String fundacco; 		//基金账号
	private String invnm; 			//客户名称
	private String invtp; 			//客户类型
	private String trustType; 		//委托方式
	private double subamt;			//申请金额
	private String currencytype;	//币种
	private String dsapkind;		//业务类型
	private String capitaltype;		//资金类型
	private String checkst;			//复核状态
	private String checkno;			//主管编号
	private String apdt;			//申请日期
	private String aptm;			//申请时间
	private String netpoint;		//网点
	private String remark;			//备注
	private String fundid;			//基金代码
	private String payst;			//付款状态
	
	private String vouchertype;		//票据类型
	private String voucherdt;		//票据日期
	private String openName; 		//开户银行
	private String bankAccoNm; 		//银行户名
	private String bnkNo; 			//银行编号
	private String bankAcco; 		//银行账号
	private String receivebnkAcco; 	//收款银行账号
	
	private String workdate;        //工作日
	private String errcode;			//错误代码
	private String errmsg;			//错误信息
	
	private String cchkFlag;	//资金复核状态
	private String idtp;//客户证件类型
	private String idno;//客户证件号码
	
	private String ncount;		//未复核数
	private String ycount;		//已复核数
	private String fcount;		//复核放弃数
	private String ccount;		//复核驳回数
	private String cancelcount;//撤单数
	private String totalcount;//总数
	
	private String begindate;//未处理数
	private String enddate;//未处理数
	
	public CapitalCheckDto() {
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

	public String getCapitalno() {
		return capitalno;
	}

	public void setCapitalno(String capitalno) {
		this.capitalno = capitalno;
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

	public String getTrustType() {
		return trustType;
	}

	public void setTrustType(String trustType) {
		this.trustType = trustType;
	}

	public double getSubamt() {
		return subamt;
	}

	public void setSubamt(double subamt) {
		this.subamt = subamt;
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

	public String getCustno() {
		return custno;
	}

	public void setCustno(String custno) {
		this.custno = custno;
	}

	public String getCapitaltype() {
		return capitaltype;
	}

	public void setCapitaltype(String capitaltype) {
		this.capitaltype = capitaltype;
	}

	public String getInvtp() {
		return invtp;
	}

	public void setInvtp(String invtp) {
		this.invtp = invtp;
	}

	public String getCheckno() {
		return checkno;
	}

	public void setCheckno(String checkno) {
		this.checkno = checkno;
	}

	public String getApdt() {
		return apdt;
	}

	public void setApdt(String apdt) {
		this.apdt = apdt;
	}

	public String getAptm() {
		return aptm;
	}

	public void setAptm(String aptm) {
		this.aptm = aptm;
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

	public String getFundid() {
		return fundid;
	}

	public void setFundid(String fundid) {
		this.fundid = fundid;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getCurrencytype() {
		return currencytype;
	}

	public void setCurrencytype(String currencytype) {
		this.currencytype = currencytype;
	}

	public String getPayst() {
		return payst;
	}

	public void setPayst(String payst) {
		this.payst = payst;
	}
	public String getWorkdate() {
		return workdate;
	}
	public void setWorkdate(String workdate) {
		this.workdate = workdate;
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

	public String getCchkFlag() {
		return cchkFlag;
	}

	public void setCchkFlag(String cchkFlag) {
		this.cchkFlag = cchkFlag;
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

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
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

	@Override
	public String toString() {
		return "CapitalCheckDto [operatorId=" + operatorId + ", operatorType="
				+ operatorType + ", permissionId=" + permissionId
				+ ", capitalno=" + capitalno + ", serialno=" + serialno
				+ ", custno=" + custno + ", tradeacco=" + tradeacco
				+ ", fundacco=" + fundacco + ", invnm=" + invnm + ", invtp="
				+ invtp + ", trustType=" + trustType + ", subamt=" + subamt
				+ ", currencytype=" + currencytype + ", dsapkind=" + dsapkind
				+ ", capitaltype=" + capitaltype + ", checkst=" + checkst
				+ ", checkno=" + checkno + ", apdt=" + apdt + ", aptm=" + aptm
				+ ", netpoint=" + netpoint + ", remark=" + remark + ", fundid="
				+ fundid + ", payst=" + payst + ", vouchertype=" + vouchertype
				+ ", voucherdt=" + voucherdt + ", openName=" + openName
				+ ", bankAccoNm=" + bankAccoNm + ", bnkNo=" + bnkNo
				+ ", bankAcco=" + bankAcco + ", receivebnkAcco="
				+ receivebnkAcco + ", workdate=" + workdate + ", errcode="
				+ errcode + ", errmsg=" + errmsg + ", cchkFlag=" + cchkFlag
				+ ", idtp=" + idtp + ", idno=" + idno + ", ncount=" + ncount
				+ ", ycount=" + ycount + ", fcount=" + fcount + ", ccount="
				+ ccount + ", cancelcount=" + cancelcount + ", totalcount="
				+ totalcount + ", begindate=" + begindate + ", enddate="
				+ enddate + "]";
	}
	
}
