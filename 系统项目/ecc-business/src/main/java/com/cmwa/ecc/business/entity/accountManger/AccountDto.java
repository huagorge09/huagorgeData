package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

/**
 * 
 * <p>
 * Title: 账户信息DTO
 * </p>
 * 
 * <p>
 * Description: 账户信息DTO，为开户，账户信息DTO帐户信息，修改帐户信息，修改密码，增加付款方式， 取消付款方式等业务提供DTO
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company: Legion Technology
 * </p>
 * 
 * @author xuhw
 * @version 1.0
 */
@Alias("accountDto")
public class AccountDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serialno; // 流水号
	private String custno; // 客户号
	private String tradeacco; // 交易账号
	private String invtp; // 客户类型
	private String invnm; // 用户真实姓名
	private String idtp; // 证件类型
	private String idno; // 证件号码
	private String mobileno; // 手机号码
	private String email; // 电子邮件
	private String logintime; // 登陆时间
	private String registertime; // 注册时间
	private String password; // 密码
	private String oldpwd; // 原密码
	private String newpwd; // 新密码
	private String cmfUserId; // 账户系统用户ID
	private InvestorInfoDto investorinfo; // 帐户投资人
	private List<TradeAccDto> tradeaccolist = new ArrayList<TradeAccDto>(); // 交易账号
	private List<FundAcctDto> fundaccolist = new ArrayList<FundAcctDto>(); // 基金账号
	private String status; // 账户状态
	private String riskLevel; // 账户风险等级
	private String errcode; // 业务返回代码
	private String errmsg; // 业务返回信息

	/**
	 * 构造函数，用于交易业务的参数值，查询业务的返回值
	 * 
	 * @param serialno
	 *            String 交易流水号，可为空
	 * @param custno
	 *            String 客户号，可为空
	 */
	public AccountDto(String serialno, String custno) {
		this.serialno = serialno;
		this.custno = custno;
	}

	public AccountDto() {
		super();
	}


	public String getTradeacco() {
		return tradeacco;
	}

	public String getCmfUserId() {
		return cmfUserId;
	}

	public void setCmfUserId(String cmfUserId) {
		this.cmfUserId = cmfUserId;
	}

	public void setTradeacco(String tradeacco) {
		this.tradeacco = tradeacco;
	}

	/**
	 * 设置交易流水号
	 * 
	 * @param serialno
	 *            String
	 */
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	/**
	 * 设置客户号
	 * 
	 * @param custno
	 *            String
	 */
	public void setCustno(String custno) {
		this.custno = custno;
	}

	/**
	 * 设置客户类型
	 * 
	 * @param invtp
	 *            String
	 */
	public void setInvtp(String invtp) {
		this.invtp = invtp;
	}

	/**
	 * 设置客户姓名
	 * 
	 * @param invnm
	 *            String
	 */
	public void setInvnm(String invnm) {
		this.invnm = invnm;
	}

	/**
	 * 设置证件类型
	 * 
	 * @param idtp
	 *            String
	 */
	public void setIdtp(String idtp) {
		this.idtp = idtp;
	}

	/**
	 * 设置证件号码
	 * 
	 * @param idno
	 *            String
	 */
	public void setIdno(String idno) {
		this.idno = idno;
	}

	/**
	 * 设置手机号码
	 * 
	 * @param mobileno
	 *            String
	 */
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	/**
	 * 设置电子邮件
	 * 
	 * @param email
	 *            String
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 设置登录时间
	 * 
	 * @param logintime
	 *            String
	 */
	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}

	/**
	 * 设置注册时间
	 * 
	 * @param registertime
	 *            String
	 */
	public void setRegistertime(String registertime) {
		this.registertime = registertime;
	}

	/**
	 * 设置密码
	 * 
	 * @param password
	 *            String
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 设置旧密码
	 * 
	 * @param oldpwd
	 *            String
	 */
	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}

	/**
	 * 设置新密码
	 * 
	 * @param newpwd
	 *            String
	 */
	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}

	/**
	 * 设置客户基本信息
	 * 
	 * @param investorinfo
	 *            InvestorInfoDto
	 */
	public void setInvestorinfo(InvestorInfoDto investorinfo) {
		this.investorinfo = investorinfo;
	}

	/**
	 * 设置状态
	 * 
	 * @param status
	 *            String
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 设置风险等级
	 * 
	 * @param riskLevel
	 *            String
	 */
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	/**
	 * 设置业务返回代码
	 * 
	 * @param errcode
	 *            String
	 */
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	/**
	 * 设置业务返回信息
	 * 
	 * @param errmsg
	 *            String
	 */
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getSerialno() {
		return serialno;
	}

	public String getCustno() {
		return custno;
	}

	public String getInvtp() {
		return invtp;
	}

	public String getInvnm() {
		return invnm;
	}

	public String getIdtp() {
		return idtp;
	}

	public String getIdno() {
		return idno;
	}

	public String getMobileno() {
		return mobileno;
	}

	public String getEmail() {
		return email;
	}

	public String getLogintime() {
		return logintime;
	}

	public String getRegistertime() {
		return registertime;
	}

	public String getPassword() {
		return password;
	}

	public String getOldpwd() {
		return oldpwd;
	}

	public String getNewpwd() {
		return newpwd;
	}

	/**
	 * 返回客户基本信息
	 * 
	 * @return InvestorInfoDto
	 */
	public InvestorInfoDto getInvestorinfo() {
		return investorinfo;
	}

	public List<TradeAccDto> getTradeaccolist() {
		return tradeaccolist;
	}

	public void setTradeaccolist(List<TradeAccDto> tradeaccolist) {
		this.tradeaccolist = tradeaccolist;
	}

	public List<FundAcctDto> getFundaccolist() {
		return fundaccolist;
	}

	public void setFundaccolist(List<FundAcctDto> fundaccolist) {
		this.fundaccolist = fundaccolist;
	}

	public String getStatus() {
		return status;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	/**
	 * 根据交易帐号返回交易账号相关信息（包括交易帐号、银行代码、银行提示账户号等）
	 * 
	 * @return List
	 */
	public TradeAccDto getTradeAccountDto(String tradeacco) {
		// return tradeaccolist;
		if ((tradeaccolist == null) || (tradeacco == null)) {
			return null;
		}
		if (tradeaccolist != null && tradeaccolist.size() > 0) {
			for (int i = 0; i < tradeaccolist.size(); i++) {
				TradeAccDto dto = (TradeAccDto) tradeaccolist.get(i);
				if (dto != null && tradeacco.equals(dto.getTradeAcco())) {
					return dto;
				}
			}
		}
		return null;
	}

	/**
	 * 根据银行代码返回交易账号相关信息（包括交易帐号、银行代码、银行提示账户号等） 此接口只支持一家银行只有一个交易账号的情况
	 * 
	 * @return List
	 */
	public TradeAccDto getTradeAccountDtoBankno(String bankno) {
		// return tradeaccolist;
		if ((tradeaccolist == null) || (bankno == null)) {
			return null;
		}
		if (tradeaccolist != null && tradeaccolist.size() > 0) {
			for (int i = 0; i < tradeaccolist.size(); i++) {
				TradeAccDto dto = (TradeAccDto) tradeaccolist.get(i);
				if (dto != null && bankno.equals(dto.getBankNo())
						&& "N".equals(dto.getStatus())) {
					return dto;
				}
			}
		}
		return null;
	}

	public String getErrcode() {
		return errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	/**
	 * 添加交易帐号
	 * 
	 * @param dto
	 *            TradeAccountDto
	 */
	public void addTradeacco(TradeAccDto dto) {
		tradeaccolist.add(dto);
	}

	/**
	 * 添加基金账号
	 * 
	 * @param dto
	 *            FundAccountDto
	 */
	public void addFundacco(FundAcctDto dto) {
		fundaccolist.add(dto);
	}

	public String toString() {
		StringBuffer strb = new StringBuffer();
		strb.append("custno: " + custno + "\n");
		strb.append("invtp: " + invtp + "\n");
		strb.append("invnm: " + invnm + "\n");
		strb.append("idtp: " + idtp + "\n");
		strb.append("idno: " + idno + "\n");
		strb.append("mobileno: " + mobileno + "\n");
		strb.append("email: " + email + "\n");
		strb.append("logintime: " + logintime + "\n");
		strb.append("registertime: " + registertime + "\n");
		strb.append("password: " + "******" + "\n");
		strb.append("oldpwd: " + "******" + "\n");
		strb.append("newpwd: " + "******" + "\n");
		if (investorinfo != null) {
			strb.append("investorinfo: " + investorinfo.toString() + "\n");
		} else {
			strb.append("investorinfo: IS NULL" + "\n");
		}
		if (tradeaccolist != null) {
			strb.append("tradeaccolist: " + tradeaccolist.toString() + "\n");
		} else {
			strb.append("tradeaccolist: IS NULL" + "\n");
		}
		if (fundaccolist != null) {
			strb.append("fundaccolist: " + fundaccolist.toString() + "\n");
		} else {
			strb.append("fundaccolist: IS NULL" + "\n");
		}
		strb.append("status: " + status + "\n");
		return strb.toString();
	}
}
