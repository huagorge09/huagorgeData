package com.cmwa.ec.weixin.dto;

/**
 * 微信用户基本信息
 * @author cmfchina
 *
 */
public class UserInfoexDto {

	private String inserttime;//录入时间
	private String updatetime;//最后更新时间
	
    /**
     *  用户的标识，对当前公众号唯一
     */
    private String openid;
    
    /**
     * 基金易用户ID
     */
    private String cmfuserid;
    
    /**
     * CRM客户号
     */
    private String crmCustno;
    
    /**
     * 基金易客户号
     */
    private String ecCustno;
    
    /**
     * 绑定状态
     */
    private String bindstat;
    
    /**
     * 绑定时间
     */
    private String bindtime;
    
    /**
     * 默认银行ID
     */
    private String defaultbankno;
    
    private String errcode;
    private String errmsg;

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

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getInserttime() {
		return inserttime;
	}
	
	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}
	
	public String getUpdatetime() {
		return updatetime;
	}
	
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getCmfuserid() {
		return cmfuserid;
	}

	public void setCmfuserid(String cmfuserid) {
		this.cmfuserid = cmfuserid;
	}

	public String getCrmCustno() {
		return crmCustno;
	}

	public void setCrmCustno(String crmCustno) {
		this.crmCustno = crmCustno;
	}

	public String getEcCustno() {
		return ecCustno;
	}

	public void setEcCustno(String ecCustno) {
		this.ecCustno = ecCustno;
	}

	public String getBindstat() {
		return bindstat;
	}

	public void setBindstat(String bindstat) {
		this.bindstat = bindstat;
	}

	public String getBindtime() {
		return bindtime;
	}

	public void setBindtime(String bindtime) {
		this.bindtime = bindtime;
	}

	public String getDefaultbankno() {
		return defaultbankno;
	}

	public void setDefaultbankno(String defaultbankno) {
		this.defaultbankno = defaultbankno;
	}
	
	public String toString(){
		return "UserInfoexDto={defaultbankno="+this.defaultbankno+"," +
				"bindtime="+this.bindtime+"," +
				"bindstat="+this.bindstat+"," +
				"ecCustno="+this.ecCustno+"," +
				"crmCustno="+this.crmCustno+"," +
				"cmfuserid="+this.cmfuserid+"," +
				"updatetime="+this.updatetime+"," +
				"openid="+this.openid+"," +
				"inserttime="+this.inserttime+"," +
				"errcode="+this.errcode+"," +
				"errmsg="+this.errmsg+"}";
	}
	 
	
}
