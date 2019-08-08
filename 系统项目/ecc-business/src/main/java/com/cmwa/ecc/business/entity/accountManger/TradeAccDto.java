package com.cmwa.ecc.business.entity.accountManger;

import org.apache.ibatis.type.Alias;


/**
 * 交易账号信息实体类
 * @author ex-chenbq
 *
 */
@Alias("tradeAccDto")
public class TradeAccDto {
	 
	private String tradeAcco; //交易账号
	private String fundAcco; //基金账号
    private String bankNo; //银行代码   （第三方渠道代码）
    private String bankNm; //银行全称    （第三方渠道名称：如快捷，通联）
    private String bankBranchNm; //分支名称
    private String bankAcco; //银行账户   （第三方渠道关联账号）
    private String bankAcNm; //银行账户名
    private String bankaccoDisplay_last; //银行卡后四位
    private String province; //省份
    private String city; //城市
    private String idtp;//证件类型
    private String idnm;//证件名称
    private String idno;//证件号码
    private String openName; 		//开户银行
	private String openAddr; 	    //开户银行地址(省）
	private String openAddrNm; 	    //开户银行地址(省）
	private String openBankCity;	//开户银行市
	private String openBankCityNm;	//开户银行市
	private String tano;	//开户银行市
    private String protocol; //协议编号
    private String areaco; //区域编码
    private String areanm; //区域名称
    private String bankaccodisplay; //银行提示账户号
    private String primaryflg; //是否为主交易账号，Y--是的，N--不是
    private String modifyflg; //是否可修改标志，Y--可修改，N--不可修改
    private String createtime; //创建时间
    private String status; //交易账号状态
    private String isunconfirm; //用于标志该交易账户是否处于009交易在途状态  add by wangxl
    private String mobile;   //手机号码
    private String opendt;   //交易账号开通日期
    
    private String realbankno;//真实银行代码，添加第三方支付渠道后原来的bankno存放第三方渠道代码
    private String realBankName; //真实银行名称
    private String payMode;    //支付方式      0：B2B    1：B2C
    
	private String isOpenTA;//是否已开17基金号  0 已开17户，1 重新开17户，2交易账号重新关联到17
	private String isMip;//该银行是否可以做定投  Y：是，N:否
	private String isOpenLof;//该交易账号是否已在中登开户 1：是，0：否
	
	
	private int timesPerDay ;				//每天限制次数
	private double limitAmountOneTime;		//每次限制金额
	private double limitAmountOneDay;		//每天限制金额
	private String balance;					//余额

    private String discount; //费率折扣
    private String discountDisplay; //费率折扣显示
    
    private boolean isRemitPay; //是否允许线下汇款
    private String remitPayDiscount; //线下汇款折扣
    private String remitPayDiscountDisplay;   //线下汇款折扣显示
    
    private double bankLimitAmountOneTime; //银行单笔限额
    private double bankLimitAmountOneDay;  //银行单日限额
    
    private String isSupportThisThirdPayChannelToTrade;// 是否支持这个第三方支付渠道进行交易 - [Y:支持, N:不支持]
    
    private String gcount;//存续中的总条数
    private String onpassagecount;//在途交易的总条数
    private String newcardcount;//新的银行卡换卡记录数
    private String oldcardcount;//旧的银行卡换卡记录数
    
    
    private String remark;
 
    
	private String errcode; //业务返回代码
    private String errmsg; //业务返回信息
	
	public String getTradeAcco() {
		return tradeAcco;
	}
	public void setTradeAcco(String tradeAcco) {
		this.tradeAcco = tradeAcco;
	}
	public String getFundAcco() {
		return fundAcco;
	}
	public void setFundAcco(String fundAcco) {
		this.fundAcco = fundAcco;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getBankNm() {
		return bankNm;
	}
	public void setBankNm(String bankNm) {
		this.bankNm = bankNm;
	}
	public String getBankBranchNm() {
		return bankBranchNm;
	}
	public void setBankBranchNm(String bankBranchNm) {
		this.bankBranchNm = bankBranchNm;
	}
	public String getBankAcco() {
		return bankAcco;
	}
	public void setBankAcco(String bankAcco) {
		this.bankAcco = bankAcco;
	}
	public String getBankAcNm() {
		return bankAcNm;
	}
	public void setBankAcNm(String bankAcNm) {
		this.bankAcNm = bankAcNm;
	}
	public String getBankaccoDisplay_last() {
		return bankaccoDisplay_last;
	}
	public void setBankaccoDisplay_last(String bankaccoDisplay_last) {
		this.bankaccoDisplay_last = bankaccoDisplay_last;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOpenAddrNm() {
		return openAddrNm;
	}
	public void setOpenAddrNm(String openAddrNm) {
		this.openAddrNm = openAddrNm;
	}
	public String getOpenBankCityNm() {
		return openBankCityNm;
	}
	public void setOpenBankCityNm(String openBankCityNm) {
		this.openBankCityNm = openBankCityNm;
	}
	public String getIdtp() {
		return idtp;
	}
	public void setIdtp(String idtp) {
		this.idtp = idtp;
	}
	public String getIdnm() {
		return idnm;
	}
	public void setIdnm(String idnm) {
		this.idnm = idnm;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getAreaco() {
		return areaco;
	}
	public void setAreaco(String areaco) {
		this.areaco = areaco;
	}
	public String getAreanm() {
		return areanm;
	}
	public void setAreanm(String areanm) {
		this.areanm = areanm;
	}
	public String getBankaccodisplay() {
		return bankaccodisplay;
	}
	public void setBankaccodisplay(String bankaccodisplay) {
		this.bankaccodisplay = bankaccodisplay;
	}
	public String getPrimaryflg() {
		return primaryflg;
	}
	public void setPrimaryflg(String primaryflg) {
		this.primaryflg = primaryflg;
	}
	public String getModifyflg() {
		return modifyflg;
	}
	public void setModifyflg(String modifyflg) {
		this.modifyflg = modifyflg;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsunconfirm() {
		return isunconfirm;
	}
	public void setIsunconfirm(String isunconfirm) {
		this.isunconfirm = isunconfirm;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOpendt() {
		return opendt;
	}
	public void setOpendt(String opendt) {
		this.opendt = opendt;
	}
	public String getRealbankno() {
		return realbankno;
	}
	public void setRealbankno(String realbankno) {
		this.realbankno = realbankno;
	}
	public String getRealBankName() {
		return realBankName;
	}
	public void setRealBankName(String realBankName) {
		this.realBankName = realBankName;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public String getIsOpenTA() {
		return isOpenTA;
	}
	public void setIsOpenTA(String isOpenTA) {
		this.isOpenTA = isOpenTA;
	}
	public String getIsMip() {
		return isMip;
	}
	public void setIsMip(String isMip) {
		this.isMip = isMip;
	}
	public String getIsOpenLof() {
		return isOpenLof;
	}
	public void setIsOpenLof(String isOpenLof) {
		this.isOpenLof = isOpenLof;
	}
	public int getTimesPerDay() {
		return timesPerDay;
	}
	public void setTimesPerDay(int timesPerDay) {
		this.timesPerDay = timesPerDay;
	}
	public double getLimitAmountOneTime() {
		return limitAmountOneTime;
	}
	public void setLimitAmountOneTime(double limitAmountOneTime) {
		this.limitAmountOneTime = limitAmountOneTime;
	}
	public double getLimitAmountOneDay() {
		return limitAmountOneDay;
	}
	public void setLimitAmountOneDay(double limitAmountOneDay) {
		this.limitAmountOneDay = limitAmountOneDay;
	}
	 
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getDiscountDisplay() {
		return discountDisplay;
	}
	public void setDiscountDisplay(String discountDisplay) {
		this.discountDisplay = discountDisplay;
	}
	public boolean isRemitPay() {
		return isRemitPay;
	}
	public void setRemitPay(boolean isRemitPay) {
		this.isRemitPay = isRemitPay;
	}
	public String getRemitPayDiscount() {
		return remitPayDiscount;
	}
	public void setRemitPayDiscount(String remitPayDiscount) {
		this.remitPayDiscount = remitPayDiscount;
	}
	public String getRemitPayDiscountDisplay() {
		return remitPayDiscountDisplay;
	}
	public void setRemitPayDiscountDisplay(String remitPayDiscountDisplay) {
		this.remitPayDiscountDisplay = remitPayDiscountDisplay;
	}
	public double getBankLimitAmountOneTime() {
		return bankLimitAmountOneTime;
	}
	public void setBankLimitAmountOneTime(double bankLimitAmountOneTime) {
		this.bankLimitAmountOneTime = bankLimitAmountOneTime;
	}
	public double getBankLimitAmountOneDay() {
		return bankLimitAmountOneDay;
	}
	public void setBankLimitAmountOneDay(double bankLimitAmountOneDay) {
		this.bankLimitAmountOneDay = bankLimitAmountOneDay;
	}
	public String getIsSupportThisThirdPayChannelToTrade() {
		return isSupportThisThirdPayChannelToTrade;
	}
	public void setIsSupportThisThirdPayChannelToTrade(
			String isSupportThisThirdPayChannelToTrade) {
		this.isSupportThisThirdPayChannelToTrade = isSupportThisThirdPayChannelToTrade;
	}
	public String getGcount() {
		return gcount;
	}
	public void setGcount(String gcount) {
		this.gcount = gcount;
	}
	public String getOnpassagecount() {
		return onpassagecount;
	}
	public void setOnpassagecount(String onpassagecount) {
		this.onpassagecount = onpassagecount;
	}
	public String getNewcardcount() {
		return newcardcount;
	}
	public void setNewcardcount(String newcardcount) {
		this.newcardcount = newcardcount;
	}
	public String getOldcardcount() {
		return oldcardcount;
	}
	public void setOldcardcount(String oldcardcount) {
		this.oldcardcount = oldcardcount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getOpenName() {
		return openName;
	}
	public void setOpenName(String openName) {
		this.openName = openName;
	}
	public String getOpenAddr() {
		return openAddr;
	}
	public void setOpenAddr(String openAddr) {
		this.openAddr = openAddr;
	}
	public String getOpenBankCity() {
		return openBankCity;
	}
	public void setOpenBankCity(String openBankCity) {
		this.openBankCity = openBankCity;
	}
	public String getTano() {
		return tano;
	}
	public void setTano(String tano) {
		this.tano = tano;
	}
}
