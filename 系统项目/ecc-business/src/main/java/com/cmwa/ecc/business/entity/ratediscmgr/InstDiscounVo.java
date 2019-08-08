package com.cmwa.ecc.business.entity.ratediscmgr;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.cmwa.ecc.business.commonVo.BaseVo;

/**
 * 机构费率设置 实体类
 * @author ex-liuy
 *
 */
@Alias("instDiscounVo")
public class InstDiscounVo extends BaseVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String serialNo;// 流水号
	private String instType;// 机构类型
	private String fundId;// 基金代码
	private String apkind;// 业务类型
	private String strAmt;// 起始金额(含)
	private String endAmt;// 结束金额(不含)
	private String strDate;// 起始日期
	private String endDate;// 结束日期
	private String discount;// 折扣
	private String status;// 状态,y：正常；n：禁用
	private String checkStatus;
	
	/**
	 * 查询用 扩展 字段 
	 */
	private String instTypeName;// 机构类型名称
	private String fundName;// 基金代码
	private String apkindName;// 业务类型
	private String statusName;// 状态,y：正常；n：禁用
	private String checkStatusName; //复核状态 名称
	private String strDateFormat; // 查询用 日期格式化
    private String endDateFormat;
    
	public InstDiscounVo() {
		super();
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getInstType() {
		return instType;
	}

	public void setInstType(String instType) {
		this.instType = instType;
	}

	public String getFundId() {
		return fundId;
	}

	public void setFundId(String fundId) {
		this.fundId = fundId;
	}

	public String getApkind() {
		return apkind;
	}

	public void setApkind(String apkind) {
		this.apkind = apkind;
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

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getInstTypeName() {
		return instTypeName;
	}

	public void setInstTypeName(String instTypeName) {
		this.instTypeName = instTypeName;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getApkindName() {
		return apkindName;
	}

	public void setApkindName(String apkindName) {
		this.apkindName = apkindName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getCheckStatusName() {
		return checkStatusName;
	}

	public void setCheckStatusName(String checkStatusName) {
		this.checkStatusName = checkStatusName;
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
	
	/*@Override  
    public Object clone() {  
        InstDiscounVo instDiscounVo = null;  
        try{  
        	instDiscounVo = (InstDiscounVo)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return instDiscounVo;  
    }  */
	 
	@Override
	public String toString() {
		return "InstDiscounVo [serialNo=" + serialNo + ", instType=" + instType
				+ ", fundId=" + fundId + ", apkind=" + apkind + ", strAmt="
				+ strAmt + ", endAmt=" + endAmt + ", strDate=" + strDate
				+ ", endDate=" + endDate + ", discount=" + discount
				+ ", status=" + status + ", checkStatus=" + checkStatus
				+ ", instTypeName=" + instTypeName + ", fundName=" + fundName
				+ ", apkindName=" + apkindName + ", statusName=" + statusName
				+ ", checkStatusName=" + checkStatusName + ", strDateFormat="
				+ strDateFormat + ", endDateFormat=" + endDateFormat + "]";
	}
}
