package com.cmwa.ecc.business.entity.ratediscmgr;

import java.io.Serializable;
import java.sql.Date;
import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

import com.cmwa.ecc.business.commonVo.BaseVo;

@Alias("bankDiscountVo")
public class BankDiscountVo extends BaseVo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String bnkNo;
	private String bnkName;
    private String apkind;
    private String apkindName;
    private String productId;
    private String discount;
    private String cTime;
    private String cMan;
    private String cManName;
    private String eTime;
    private String eMan;
    private String eManName;
    private String strAmt;
    private String endAmt;
    private String strDate;
    private String endDate;
    private String status;
    private String resultCode; //返回代码
    private String resultMessage; //返回信息
    private String productName;
    private String statusName;
    /**
     * 查询用 日期格式化
     */
    private String strDateFormat;
    private String endDateFormat;
    
	public BankDiscountVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getStrDateFormat() {
		return strDateFormat;
	}

	public void setStrDateFormat(String strDateFormat) {
		this.strDateFormat = strDateFormat;
	}

	public String getEndDateFormat() {
		return endDateFormat;
	}

	public void setEndDateFormat(String endDateFormat) {
		this.endDateFormat = endDateFormat;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getcTime() {
		return cTime;
	}

	public void setcTime(String cTime) {
		this.cTime = cTime;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getStrAmt() {
		return strAmt;
	}

	public void setStrAmt(String strAmt) {
		this.strAmt = strAmt;
	}

	public String getEndAmt() {
		return endAmt;
	}

	public void setEndAmt(String endAmt) {
		this.endAmt = endAmt;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBnkName() {
		return bnkName;
	}

	public void setBnkName(String bnkName) {
		this.bnkName = bnkName;
	}

	public String getApkindName() {
		return apkindName;
	}

	public void setApkindName(String apkindName) {
		this.apkindName = apkindName;
	}

	public String getcManName() {
		return cManName;
	}

	public void setcManName(String cManName) {
		this.cManName = cManName;
	}

	public String geteManName() {
		return eManName;
	}

	public void seteManName(String eManName) {
		this.eManName = eManName;
	}

	public String getBnkNo() {
		return bnkNo;
	}

	public void setBnkNo(String bnkNo) {
		this.bnkNo = bnkNo;
	}

	public String getApkind() {
		return apkind;
	}

	public void setApkind(String apkind) {
		this.apkind = apkind;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getcMan() {
		return cMan;
	}

	public void setcMan(String cMan) {
		this.cMan = cMan;
	}

	public String geteMan() {
		return eMan;
	}

	public void seteMan(String eMan) {
		this.eMan = eMan;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	@Override
	public String toString() {
		return "BankDiscountDto [bnkNo=" + bnkNo + ", apkind=" + apkind
				+ ", productId=" + productId + ", discount=" + discount
				+ ", cTime=" + cTime + ", cMan=" + cMan + ", eTime=" + eTime
				+ ", eMan=" + eMan + ", strAmt=" + strAmt + ", endAmt="
				+ endAmt + ", strDate=" + strDate + ", endDate=" + endDate
				+ ", status=" + status + ", resultCode=" + resultCode
				+ ", resultMessage=" + resultMessage + "]";
	}
}
