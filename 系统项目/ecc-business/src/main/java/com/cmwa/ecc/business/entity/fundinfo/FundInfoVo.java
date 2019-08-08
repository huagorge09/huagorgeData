package com.cmwa.ecc.business.entity.fundinfo;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * 基金-产品 实体
 * @author ex-liuy
 *
 */
@Alias("fundInfoVo")
public class FundInfoVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fundId; // 基金代码
	private String fundNm; // 基金名称
	private String fundShortNm; // 基金简称 //Added on 2008-07-09
	private String fundEnglishNm; // 基金英文名称
	private String taNo; // 注册登记机构代码
	private String taNm; // 注册登记机构名称
	private String managerId; // 管理人代码
	private String trusteeId; // 托管人代码
	private String sponsorId; // 发起人代码
	private String managerNm; // 管理人名称
	private String trusteeNm; // 托管人名称
	private String sponsorNm; // 发起人名称
	private String inverstDirect; // 投资方向
	private String motherFundId; // 母基金代码
	private String inMotherFundRate; // 占母基金比例
	private String currencyType; // 货币类型
	private String nav; // 基金单位净值(最新净值)
	private String navDate; // 净值日期
	private String fundSt; // 基金状态
	private String changeToSign; // 转换选择标志
	private String fundTotalSum; // 基金总份数
	private String fundType; // 基金类别
	private String fundDispType; // 基金显示类别
	private String parentFundid; // 母基金代码
	private String fundRiskLevel; // 基金风险等级 //Added By Lijh on 2008/06/06
	private String fundEvalDate; // 基金评级日期 //Added By Lijh on 2008/09/29
	private String fundPreScale; // 基金预定规模
	private String fundDenomina; // 基金面值
	private String insertTime; // 记录生成时间
	private String updateTime; // 记录更新时间
	private String issueSet; // 基金发行设置
	private String cycletp; // 周期类型
	private String cyclelen; // 周期长度
	private String fundLimit; // 基金限额
	private double minSubAmt; // 最低认购金额
	private double minBidAmt; // 最低申购金额
	private double minRedAmt; // 最低赎回份额

	private String minConvAmt; // 最低转换份额
	private String minRspAmt; // 最低定投金额
	
	private String keepLimit; //最低持有份额 //Added By liaojj on 2014/01/14
	private String fundSize;//产品预订规模 Added By Liaojj on 2014/01/24
	private String ecmaxPurchase;//网上交易最高认申购限额 Added By liaojj on 2014/03/29
	
	private String chargeType; // 收费方式
	private String managerRatio; // 管理费率
	private String indiMaxPurchase; //最大申购额度
	private String navFracNum; // 基金净值小数位数
	private String navFracMode; // 基金净值小数处理方式 1-四舍五入,2-舍位处理
	
	// 下一开放日 add by liaojj 20140421
	private String nextIssueDate;
	// 产品特点add by liaojj 20140421
	private String feature;
	
	private String processor;       //经办人
	private String checktime;    //复核时间
	private String checker;         //复核人
	private String checkstatus;     //复核状态
	
	private String fundStNm;        //基金状态名称
	private String fundDispTypeNm;  //基金显示类别名称
	private String fundRiskLevelNm; //基金分险级别名称

	private String isUnFund;//参数限制 added by pengl 20140807
	
	private String errcode; // 执行存储过程返回代码
	private String errmsg; // 执行存储过程返回信息
	private String displayOrder; //展示顺序号
	private String issuePrice;
	private String issueDate;
	private String investType;
	private String currencyTypeNm;
	private String managerRates;
	private String fundTypeNm;
	private String setUpDate;
	private String redeemToAcctDays;
	private String redMelonDays;
	private String subDays;
	private String inconvertinbyinst;
	private String productTimeLimit;
	
	public FundInfoVo() {
		super();
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getInvestType() {
		return investType;
	}

	public void setInvestType(String investType) {
		this.investType = investType;
	}

	public String getCurrencyTypeNm() {
		return currencyTypeNm;
	}

	public void setCurrencyTypeNm(String currencyTypeNm) {
		this.currencyTypeNm = currencyTypeNm;
	}

	public String getManagerRates() {
		return managerRates;
	}

	public void setManagerRates(String managerRates) {
		this.managerRates = managerRates;
	}

	public String getFundTypeNm() {
		return fundTypeNm;
	}

	public void setFundTypeNm(String fundTypeNm) {
		this.fundTypeNm = fundTypeNm;
	}

	public String getSetUpDate() {
		return setUpDate;
	}

	public void setSetUpDate(String setUpDate) {
		this.setUpDate = setUpDate;
	}

	public String getRedeemToAcctDays() {
		return redeemToAcctDays;
	}

	public void setRedeemToAcctDays(String redeemToAcctDays) {
		this.redeemToAcctDays = redeemToAcctDays;
	}

	public String getRedMelonDays() {
		return redMelonDays;
	}

	public void setRedMelonDays(String redMelonDays) {
		this.redMelonDays = redMelonDays;
	}

	public String getSubDays() {
		return subDays;
	}

	public void setSubDays(String subDays) {
		this.subDays = subDays;
	}

	public String getInconvertinbyinst() {
		return inconvertinbyinst;
	}

	public void setInconvertinbyinst(String inconvertinbyinst) {
		this.inconvertinbyinst = inconvertinbyinst;
	}

	public String getProductTimeLimit() {
		return productTimeLimit;
	}

	public void setProductTimeLimit(String productTimeLimit) {
		this.productTimeLimit = productTimeLimit;
	}

	public String getIssuePrice() {
		return issuePrice;
	}

	public void setIssuePrice(String issuePrice) {
		this.issuePrice = issuePrice;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getFundId() {
		return fundId;
	}

	public void setFundId(String fundId) {
		this.fundId = fundId;
	}

	public String getFundNm() {
		return fundNm;
	}

	public void setFundNm(String fundNm) {
		this.fundNm = fundNm;
	}

	public String getFundShortNm() {
		return fundShortNm;
	}

	public void setFundShortNm(String fundShortNm) {
		this.fundShortNm = fundShortNm;
	}

	public String getFundEnglishNm() {
		return fundEnglishNm;
	}

	public void setFundEnglishNm(String fundEnglishNm) {
		this.fundEnglishNm = fundEnglishNm;
	}

	public String getTaNo() {
		return taNo;
	}

	public void setTaNo(String taNo) {
		this.taNo = taNo;
	}

	public String getTaNm() {
		return taNm;
	}

	public void setTaNm(String taNm) {
		this.taNm = taNm;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getTrusteeId() {
		return trusteeId;
	}

	public void setTrusteeId(String trusteeId) {
		this.trusteeId = trusteeId;
	}

	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	public String getManagerNm() {
		return managerNm;
	}

	public void setManagerNm(String managerNm) {
		this.managerNm = managerNm;
	}

	public String getTrusteeNm() {
		return trusteeNm;
	}

	public void setTrusteeNm(String trusteeNm) {
		this.trusteeNm = trusteeNm;
	}

	public String getSponsorNm() {
		return sponsorNm;
	}

	public void setSponsorNm(String sponsorNm) {
		this.sponsorNm = sponsorNm;
	}

	public String getInverstDirect() {
		return inverstDirect;
	}

	public void setInverstDirect(String inverstDirect) {
		this.inverstDirect = inverstDirect;
	}

	public String getMotherFundId() {
		return motherFundId;
	}

	public void setMotherFundId(String motherFundId) {
		this.motherFundId = motherFundId;
	}

	public String getInMotherFundRate() {
		return inMotherFundRate;
	}

	public void setInMotherFundRate(String inMotherFundRate) {
		this.inMotherFundRate = inMotherFundRate;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getNav() {
		return nav;
	}

	public void setNav(String nav) {
		this.nav = nav;
	}

	public String getNavDate() {
		return navDate;
	}

	public void setNavDate(String navDate) {
		this.navDate = navDate;
	}

	public String getFundSt() {
		return fundSt;
	}

	public void setFundSt(String fundSt) {
		this.fundSt = fundSt;
	}

	public String getChangeToSign() {
		return changeToSign;
	}

	public void setChangeToSign(String changeToSign) {
		this.changeToSign = changeToSign;
	}

	public String getFundTotalSum() {
		return fundTotalSum;
	}

	public void setFundTotalSum(String fundTotalSum) {
		this.fundTotalSum = fundTotalSum;
	}

	public String getFundType() {
		return fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	public String getFundDispType() {
		return fundDispType;
	}

	public void setFundDispType(String fundDispType) {
		this.fundDispType = fundDispType;
	}

	public String getParentFundid() {
		return parentFundid;
	}

	public void setParentFundid(String parentFundid) {
		this.parentFundid = parentFundid;
	}

	public String getFundRiskLevel() {
		return fundRiskLevel;
	}

	public void setFundRiskLevel(String fundRiskLevel) {
		this.fundRiskLevel = fundRiskLevel;
	}

	public String getFundEvalDate() {
		return fundEvalDate;
	}

	public void setFundEvalDate(String fundEvalDate) {
		this.fundEvalDate = fundEvalDate;
	}

	public String getFundPreScale() {
		return fundPreScale;
	}

	public void setFundPreScale(String fundPreScale) {
		this.fundPreScale = fundPreScale;
	}

	public String getFundDenomina() {
		return fundDenomina;
	}

	public void setFundDenomina(String fundDenomina) {
		this.fundDenomina = fundDenomina;
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

	public String getIssueSet() {
		return issueSet;
	}

	public void setIssueSet(String issueSet) {
		this.issueSet = issueSet;
	}

	public String getCycletp() {
		return cycletp;
	}

	public void setCycletp(String cycletp) {
		this.cycletp = cycletp;
	}

	public String getCyclelen() {
		return cyclelen;
	}

	public void setCyclelen(String cyclelen) {
		this.cyclelen = cyclelen;
	}

	public String getFundLimit() {
		return fundLimit;
	}

	public void setFundLimit(String fundLimit) {
		this.fundLimit = fundLimit;
	}

	public String getMinConvAmt() {
		return minConvAmt;
	}

	public void setMinConvAmt(String minConvAmt) {
		this.minConvAmt = minConvAmt;
	}

	public String getMinRspAmt() {
		return minRspAmt;
	}

	public void setMinRspAmt(String minRspAmt) {
		this.minRspAmt = minRspAmt;
	}

	public String getKeepLimit() {
		return keepLimit;
	}

	public void setKeepLimit(String keepLimit) {
		this.keepLimit = keepLimit;
	}

	public String getFundSize() {
		return fundSize;
	}

	public void setFundSize(String fundSize) {
		this.fundSize = fundSize;
	}

	public String getEcmaxPurchase() {
		return ecmaxPurchase;
	}

	public void setEcmaxPurchase(String ecmaxPurchase) {
		this.ecmaxPurchase = ecmaxPurchase;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getManagerRatio() {
		return managerRatio;
	}

	public void setManagerRatio(String managerRatio) {
		this.managerRatio = managerRatio;
	}

	public String getIndiMaxPurchase() {
		return indiMaxPurchase;
	}

	public void setIndiMaxPurchase(String indiMaxPurchase) {
		this.indiMaxPurchase = indiMaxPurchase;
	}

	public String getNavFracNum() {
		return navFracNum;
	}

	public void setNavFracNum(String navFracNum) {
		this.navFracNum = navFracNum;
	}

	public String getNavFracMode() {
		return navFracMode;
	}

	public void setNavFracMode(String navFracMode) {
		this.navFracMode = navFracMode;
	}

	public String getNextIssueDate() {
		return nextIssueDate;
	}

	public void setNextIssueDate(String nextIssueDate) {
		this.nextIssueDate = nextIssueDate;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getChecktime() {
		return checktime;
	}

	public void setChecktime(String checktime) {
		this.checktime = checktime;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public String getCheckstatus() {
		return checkstatus;
	}

	public void setCheckstatus(String checkstatus) {
		this.checkstatus = checkstatus;
	}

	public String getFundStNm() {
		return fundStNm;
	}

	public void setFundStNm(String fundStNm) {
		this.fundStNm = fundStNm;
	}

	public String getFundDispTypeNm() {
		return fundDispTypeNm;
	}

	public void setFundDispTypeNm(String fundDispTypeNm) {
		this.fundDispTypeNm = fundDispTypeNm;
	}

	public String getFundRiskLevelNm() {
		return fundRiskLevelNm;
	}

	public void setFundRiskLevelNm(String fundRiskLevelNm) {
		this.fundRiskLevelNm = fundRiskLevelNm;
	}

	public String getIsUnFund() {
		return isUnFund;
	}

	public void setIsUnFund(String isUnFund) {
		this.isUnFund = isUnFund;
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

	public double getMinSubAmt() {
		return minSubAmt;
	}

	public void setMinSubAmt(double minSubAmt) {
		this.minSubAmt = minSubAmt;
	}

	public double getMinBidAmt() {
		return minBidAmt;
	}

	public void setMinBidAmt(double minBidAmt) {
		this.minBidAmt = minBidAmt;
	}

	public double getMinRedAmt() {
		return minRedAmt;
	}

	public void setMinRedAmt(double minRedAmt) {
		this.minRedAmt = minRedAmt;
	}

	@Override
	public String toString() {
		return "FundInfoVo [fundId=" + fundId + ", fundNm=" + fundNm
				+ ", fundShortNm=" + fundShortNm + ", fundEnglishNm="
				+ fundEnglishNm + ", taNo=" + taNo + ", taNm=" + taNm
				+ ", managerId=" + managerId + ", trusteeId=" + trusteeId
				+ ", sponsorId=" + sponsorId + ", managerNm=" + managerNm
				+ ", trusteeNm=" + trusteeNm + ", sponsorNm=" + sponsorNm
				+ ", inverstDirect=" + inverstDirect + ", motherFundId="
				+ motherFundId + ", inMotherFundRate=" + inMotherFundRate
				+ ", currencyType=" + currencyType + ", nav=" + nav
				+ ", navDate=" + navDate + ", fundSt=" + fundSt
				+ ", changeToSign=" + changeToSign + ", fundTotalSum="
				+ fundTotalSum + ", fundType=" + fundType + ", fundDispType="
				+ fundDispType + ", parentFundid=" + parentFundid
				+ ", fundRiskLevel=" + fundRiskLevel + ", fundEvalDate="
				+ fundEvalDate + ", fundPreScale=" + fundPreScale
				+ ", fundDenomina=" + fundDenomina + ", insertTime="
				+ insertTime + ", updateTime=" + updateTime + ", issueSet="
				+ issueSet + ", cycletp=" + cycletp + ", cyclelen=" + cyclelen
				+ ", fundLimit=" + fundLimit + ", minSubAmt=" + minSubAmt
				+ ", minBidAmt=" + minBidAmt + ", minRedAmt=" + minRedAmt
				+ ", minConvAmt=" + minConvAmt + ", minRspAmt=" + minRspAmt
				+ ", keepLimit=" + keepLimit + ", fundSize=" + fundSize
				+ ", ecmaxPurchase=" + ecmaxPurchase + ", chargeType="
				+ chargeType + ", managerRatio=" + managerRatio
				+ ", indiMaxPurchase=" + indiMaxPurchase + ", navFracNum="
				+ navFracNum + ", navFracMode=" + navFracMode
				+ ", nextIssueDate=" + nextIssueDate + ", feature=" + feature
				+ ", processor=" + processor + ", checktime=" + checktime
				+ ", checker=" + checker + ", checkstatus=" + checkstatus
				+ ", fundStNm=" + fundStNm + ", fundDispTypeNm="
				+ fundDispTypeNm + ", fundRiskLevelNm=" + fundRiskLevelNm
				+ ", isUnFund=" + isUnFund + ", errcode=" + errcode
				+ ", errmsg=" + errmsg + ", displayOrder=" + displayOrder
				+ ", issuePrice=" + issuePrice + ", issueDate=" + issueDate
				+ ", investType=" + investType + ", currencyTypeNm="
				+ currencyTypeNm + ", managerRates=" + managerRates
				+ ", fundTypeNm=" + fundTypeNm + ", setUpDate=" + setUpDate
				+ ", redeemToAcctDays=" + redeemToAcctDays + ", redMelonDays="
				+ redMelonDays + ", subDays=" + subDays
				+ ", inconvertinbyinst=" + inconvertinbyinst
				+ ", productTimeLimit=" + productTimeLimit + "]";
	}
}
