package com.cmwa.ecc.business.entity.fundinfo;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("fundStopVo")
public class FundStopVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String serialno;//流水号
	private String fundid;//基金代码
	private String apkind;//业务类型
	private String begindate;//开始日期
	private String enddate;//结束日期
	private String status;//状态
	private String opid;//操作员
	private String checkerid;//复核员
	private String checkflag;//复核标记
	private String checkdate;//复核日期
	private String remark;//备注
	
	private String[] fundids;//基金代码集
	private String[] apkinds;//业务类型集
	private String[] begindates;//开始日期集
	private String[] enddates;//结束日期集
	private String[] serialnos;//流水号集
	
	public String getSerialno() {
		return serialno;
	}
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}
	public String getFundid() {
		return fundid;
	}
	public void setFundid(String fundid) {
		this.fundid = fundid;
	}
	public String getApkind() {
		return apkind;
	}
	public void setApkind(String apkind) {
		this.apkind = apkind;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOpid() {
		return opid;
	}
	public void setOpid(String opid) {
		this.opid = opid;
	}
	public String getCheckerid() {
		return checkerid;
	}
	public void setCheckerid(String checkerid) {
		this.checkerid = checkerid;
	}
	public String getCheckflag() {
		return checkflag;
	}
	public void setCheckflag(String checkflag) {
		this.checkflag = checkflag;
	}
	public String getCheckdate() {
		return checkdate;
	}
	public void setCheckdate(String checkdate) {
		this.checkdate = checkdate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String[] getFundids() {
		return fundids;
	}
	public void setFundids(String[] fundids) {
		this.fundids = fundids;
	}
	public String[] getApkinds() {
		return apkinds;
	}
	public void setApkinds(String[] apkinds) {
		this.apkinds = apkinds;
	}
	public String[] getBegindates() {
		return begindates;
	}
	public void setBegindates(String[] begindates) {
		this.begindates = begindates;
	}
	public String[] getEnddates() {
		return enddates;
	}
	public void setEnddates(String[] enddates) {
		this.enddates = enddates;
	}
	public String[] getSerialnos() {
		return serialnos;
	}
	public void setSerialnos(String[] serialnos) {
		this.serialnos = serialnos;
	}
}