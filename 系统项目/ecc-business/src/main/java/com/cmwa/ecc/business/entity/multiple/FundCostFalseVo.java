package com.cmwa.ecc.business.entity.multiple;

import org.apache.ibatis.type.Alias;

@Alias("fundCostFalseVo")
public class FundCostFalseVo {
	private String banknm;
	private String respMsg;
	private String cnt;
	private String respCode;
	public String getBanknm() {
		return banknm;
	}
	public void setBanknm(String banknm) {
		this.banknm = banknm;
	}
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
	public String getCnt() {
		return cnt;
	}
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FundCostFalseVo other = (FundCostFalseVo) obj;
		if (banknm == null) {
			if (other.banknm != null)
				return false;
		} else if (!banknm.equals(other.banknm))
			return false;
		if (cnt == null) {
			if (other.cnt != null)
				return false;
		} else if (!cnt.equals(other.cnt))
			return false;
		if (respCode == null) {
			if (other.respCode != null)
				return false;
		} else if (!respCode.equals(other.respCode))
			return false;
		if (respMsg == null) {
			if (other.respMsg != null)
				return false;
		} else if (!respMsg.equals(other.respMsg))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FundCostFalseVo [banknm=" + banknm + ", respMsg=" + respMsg + ", cnt=" + cnt + ", respCode=" + respCode
				+ "]";
	}
}
