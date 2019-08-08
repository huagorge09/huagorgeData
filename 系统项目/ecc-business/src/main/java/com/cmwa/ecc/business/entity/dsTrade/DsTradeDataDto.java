package com.cmwa.ecc.business.entity.dsTrade;

import org.apache.ibatis.type.Alias;

@Alias("dsTradeDataDto")
public class DsTradeDataDto {
	private String serialno; //交易流水号
	private String apdt;//申请日期
	private String begindate;
	private String enddate;
	private String fundid; //基金代码
	private String fundname;//基金名称
	private String appserialno;
	private String isoriginal; //是否原件
	private String isoriginalName;
	private String contractsign; //合同签署
	private String contractdevolve; //是否移交
	private String contractdevolveName;
	private String filed;
	private String filedName;
	private String remark;
	private String subamt; //申请金额
    private String subquty; //申请份额
    private String invnm;//投资者名称
    private String fileno;
    private String custno;
    private String apkind;
    private String istradeform;//交易表单是否原件
    private String istradeformName;//交易表单是否原件
    private String voicerecord;
    private String risksign; 
    private String risksignName;
    //投资者类型  查询用
    private String invprtp;
    
	private String invprtpName;
    //扩展字段
    private String attr1;
	private String contractsignName;
	public String getIsoriginalName() {
		return isoriginalName;
	}

	public void setIsoriginalName(String isoriginalName) {
		this.isoriginalName = isoriginalName;
	}

	public String getContractdevolveName() {
		return contractdevolveName;
	}

	public void setContractdevolveName(String contractdevolveName) {
		this.contractdevolveName = contractdevolveName;
	}

	public String getFiledName() {
		return filedName;
	}

	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}

	public String getIstradeformName() {
		return istradeformName;
	}

	public void setIstradeformName(String istradeformName) {
		this.istradeformName = istradeformName;
	}

	public String getRisksignName() {
		return risksignName;
	}

	public void setRisksignName(String risksignName) {
		this.risksignName = risksignName;
	}

	public String getInvprtpName() {
		return invprtpName;
	}

	public void setInvprtpName(String invprtpName) {
		this.invprtpName = invprtpName;
	}


    public String getContractsignName() {
		return contractsignName;
	}

	public void setContractsignName(String contractsignName) {
		this.contractsignName = contractsignName;
	}
    
	public String getInvprtp() {
		return invprtp;
	}

	public void setInvprtp(String invprtp) {
		this.invprtp = invprtp;
	}

	public String getAttr1() {
		return attr1;
	}

	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}

	public String getIstradeform() {
		return istradeform;
	}

	public void setIstradeform(String istradeform) {
		this.istradeform = istradeform;
	}

	public String getApkind() {
		return apkind;
	}

	public void setApkind(String apkind) {
		this.apkind = apkind;
	}

	public String getCustno() {
		return custno;
	}

	public void setCustno(String custno) {
		this.custno = custno;
	}

	public String getFileno() {
		return fileno;
	}

	public void setFileno(String fileno) {
		this.fileno = fileno;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getApdt() {
		return apdt;
	}

	public void setApdt(String apdt) {
		this.apdt = apdt;
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

	public String getFundid() {
		return fundid;
	}

	public void setFundid(String fundid) {
		this.fundid = fundid;
	}

	public String getFundname() {
		return fundname;
	}

	public void setFundname(String fundname) {
		this.fundname = fundname;
	}

	public String getAppserialno() {
		return appserialno;
	}

	public void setAppserialno(String appserialno) {
		this.appserialno = appserialno;
	}

	public String getIsoriginal() {
		return isoriginal;
	}

	public void setIsoriginal(String isoriginal) {
		this.isoriginal = isoriginal;
	}

	public String getContractsign() {
		return contractsign;
	}

	public void setContractsign(String contractsign) {
		this.contractsign = contractsign;
	}

	public String getContractdevolve() {
		return contractdevolve;
	}

	public void setContractdevolve(String contractdevolve) {
		this.contractdevolve = contractdevolve;
	}

	public String getFiled() {
		return filed;
	}

	public void setFiled(String filed) {
		this.filed = filed;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getInvnm() {
		return invnm;
	}

	public void setInvnm(String invnm) {
		this.invnm = invnm;
	}

	public String getVoicerecord() {
		return voicerecord;
	}

	public void setVoicerecord(String voicerecord) {
		this.voicerecord = voicerecord;
	}

	public String getRisksign() {
		return risksign;
	}

	public void setRisksign(String risksign) {
		this.risksign = risksign;
	}

}
