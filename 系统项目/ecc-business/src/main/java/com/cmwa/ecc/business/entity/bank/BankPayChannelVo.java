package com.cmwa.ecc.business.entity.bank;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;
@Alias("bankPayChannelVo")
public class BankPayChannelVo implements Serializable{
	private static final long serialVersionUID = -9169400483142163927L;
	
	private String thirdChannel;//第三方支付渠道代码
	private String thirdChannelName;//第三方支付渠道银行名称
	private String bankNO;      //支持银行代码,填写第三方支付渠道支持的银行
	private String oldBankNo;   //原支持银行代码
	private String status;      //状态, Y:有效,N:无效
	private String statusName;  //状态转换值
	private String createDate;  //创建日期
	private String createTime;  //创建时间
	private String createOpid;  //创建人
	private String modifyDate;  //修改日期
	private String modifyTime;  //修改时间
	private String modifyOpid;  //修改人
	private String bankName;    //银行名称
	private String thirdBankNO;//第三方支付银行代码
	private String openDemo;    //开户演示链接
	private String openObseRev; //开户注意事项链接
	private String payMode;     //扣款方式 0:B2B委托代扣，1:B2C网银支付
	private String payModeName; //扣款方式转换值
	private String createOpName; //创建人名称
	private String modifyOpName; //修改人名称
	private String mipFlag;      //是否支持定投
	private String mipFlagName;  //是否支持定投转换
	
	//added by zengxy 20131120 新增字段：是否推荐
	private String recommend;  //是否推荐
	
	//added by liaojj 20140707新增字段：
	private String avalsource; //可进行交易的终端渠道(A:所有终端渠道；N：不支持任何终端渠道；|EC|WEIXIN|:基金易和微信)
	
	private boolean isTradeaVailable;//是否支持某个终端渠道

	
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public String getOldBankNo() {
		return oldBankNo;
	}
	public void setOldBankNo(String oldBankNo) {
		this.oldBankNo = oldBankNo;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getMipFlag() {
		return mipFlag;
	}
	public void setMipFlag(String mipFlag) {
		this.mipFlag = mipFlag;
	}
	public String getMipFlagName() {
		return mipFlagName;
	}
	public void setMipFlagName(String mipFlag) {
		if("Y".equals(mipFlag)){
			this.mipFlagName = "是";
		}else if("N".equals(mipFlag)){
			this.mipFlagName = "否";
		}else{
			this.mipFlagName = "";
		}
	}
	public String getCreateOpName() {
		return createOpName;
	}
	public void setCreateOpName(String createOpName) {
		this.createOpName = createOpName;
	}
	public String getModifyOpName() {
		return modifyOpName;
	}
	public void setModifyOpName(String modifyOpName) {
		this.modifyOpName = modifyOpName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String status) {
		if("Y".equals(status)){
			this.statusName = "有效";
		}else if("N".equals(status)){
			this.statusName = "无效";
		}else{
			this.statusName = "";
		}
	}
	public String getPayModeName() {
		return payModeName;
	}
	public void setPayModeName(String payModeID) {
		if("0".equals(payModeID)){
			this.payModeName = "B2B委托代扣";
		}else if("1".equals(payModeID)){
			this.payModeName = "B2C网银支付";
		}else{
			this.payModeName = "";
		}
	}
	public String getThirdChannel() {
		return thirdChannel;
	}
	public void setThirdChannel(String thirdChannel) {
		this.thirdChannel = thirdChannel;
	}
	public String getBankNO() {
		return bankNO;
	}
	public void setBankNO(String bankNO) {
		this.bankNO = bankNO;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateOpid() {
		return createOpid;
	}
	public void setCreateOpid(String createOpid) {
		this.createOpid = createOpid;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyOpid() {
		return modifyOpid;
	}
	public void setModifyOpid(String modifyOpid) {
		this.modifyOpid = modifyOpid;
	}
	
	public String getOpenDemo() {
		return openDemo;
	}
	public void setOpenDemo(String openDemo) {
		this.openDemo = openDemo;
	}
	public String getOpenObseRev() {
		return openObseRev;
	}
	public void setOpenObseRev(String openObseRev) {
		this.openObseRev = openObseRev;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public String getAvalsource() {
		return avalsource;
	}
	public void setAvalsource(String avalsource) {
		this.avalsource = avalsource;
	}
	public boolean isTradeaVailable() {
		return isTradeaVailable;
	}
	public void setTradeaVailable(boolean isTradeaVailable) {
		this.isTradeaVailable = isTradeaVailable;
	}
	public String getBankName() {
		return bankName;
	}
	public String getThirdBankNO() {
		return thirdBankNO;
	}
	public void setThirdBankNO(String thirdBankNO) {
		this.thirdBankNO = thirdBankNO;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	@Override
	public String toString() {
		return "BankPayChannelDto [thirdChannel=" + thirdChannel + ", bankNO="
				+ bankNO + ", oldBankNo=" + oldBankNo + ", status=" + status
				+ ", statusName=" + statusName + ", createDate=" + createDate
				+ ", createTime=" + createTime + ", createOpid=" + createOpid
				+ ", modifyDate=" + modifyDate + ", modifyTime=" + modifyTime
				+ ", modifyOpid=" + modifyOpid + ", bankName=" + bankName
				+ ", thirdBankNO=" + thirdBankNO + ", openDemo=" + openDemo
				+ ", openObseRev=" + openObseRev + ", payMode=" + payMode
				+ ", payModeName=" + payModeName + ", createOpName="
				+ createOpName + ", modifyOpName=" + modifyOpName
				+ ", mipFlag=" + mipFlag + ", mipFlagName=" + mipFlagName
				+ ", recommend=" + recommend + ", avalsource=" + avalsource
				+ ", isTradeaVailable=" + isTradeaVailable + "]";
	}
	public String getThirdChannelName() {
		return thirdChannelName;
	}
	public void setThirdChannelName(String thirdChannelName) {
		this.thirdChannelName = thirdChannelName;
	}
	
}
