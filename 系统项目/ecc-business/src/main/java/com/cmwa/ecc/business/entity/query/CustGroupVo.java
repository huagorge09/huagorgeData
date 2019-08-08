
package com.cmwa.ecc.business.entity.query;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ex-wuh2
 */
public class CustGroupVo implements Serializable{

	private static final long serialVersionUID = 1L;
		private String serialno;
		private String pmst;
	    private String pmky;
	    private String pmco;
	    private String pmnm;
	    private String pmv1;
	    private String pmv2;
	    private String pmv3;
	    private String pmv4;
	    private String pmv5;
	    private Date inserttimestamp;
	    private Date updatetimestamp;
	    private String errcode = "";
	    private String errmsg = "";
	    private String resultCode;
	    private String resultMessage;
	    public String getSerialno() {
			return serialno;
		}

		public void setSerialno(String serialno) {
			this.serialno = serialno;
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

	    public String getPmky() {
	        return pmky;
	    }

	    public void setPmky(String pmky) {
	        this.pmky = pmky;
	    }

	    public String getPmco() {
	        return pmco;
	    }

	    public void setPmco(String pmco) {
	        this.pmco = pmco;
	    }

	    public String getPmnm() {
	        return pmnm;
	    }

	    public void setPmnm(String pmnm) {
	        this.pmnm = pmnm;
	    }

	    public String getPmv1() {
	        return pmv1;
	    }

	    public void setPmv1(String pmv1) {
	        this.pmv1 = pmv1;
	    }

	    public String getPmv2() {
	        return pmv2;
	    }

	    public void setPmv2(String pmv2) {
	        this.pmv2 = pmv2;
	    }

	    public String getPmv3() {
	        return pmv3;
	    }

	    public void setPmv3(String pmv3) {
	        this.pmv3 = pmv3;
	    }

	    public String getPmv4() {
	        return pmv4;
	    }

	    public void setPmv4(String pmv4) {
	        this.pmv4 = pmv4;
	    }

	    public String getPmv5() {
	        return pmv5;
	    }

	    public void setPmv5(String pmv5) {
	        this.pmv5 = pmv5;
	    }

	    public Date getInserttimestamp() {
	        return inserttimestamp;
	    }

	    public void setInserttimestamp(Date inserttimestamp) {
	        this.inserttimestamp = inserttimestamp;
	    }

	    public Date getUpdatetimestamp() {
	        return updatetimestamp;
	    }

	    public void setUpdatetimestamp(Date updatetimestamp) {
	        this.updatetimestamp = updatetimestamp;
	    }

	    public String getPmst() {
	        return pmst;
	    }

	    public void setPmst(String pmst) {
	        this.pmst = pmst;
	    }

	    /**
	     * 取值返回代码
	     * @return resultCode
	     */
	    public String getResultCode() {
	        return this.resultCode;
	    }

	    /**
	     * 取值返回消息
	     * @return resultMessage
	     */
	    public String getResultMessage() {
	        return this.resultMessage;
	    }

	    /**
	     * 赋值返回代码
	     * @param resultCode 返回代码
	     */
	    public void setResultCode(String resultCode) {
	        this.resultCode = resultCode;
	    }

	    /**
	     * 赋值返回消息
	     * @param resultMessage 返回消息
	     */
	    public void setResultMessage(String resultMessage) {
	        this.resultMessage = resultMessage;
	    }

	    /**
	     * Vo转换成String
	     * @return String
	     */
	    public String toString() {
	        String returnString = "";
	        returnString += "[PMST]:" + pmst;
	        returnString += ", " + "[PMKY]:" + pmky;
	        returnString += "[PMCO]:" + pmco;
	        returnString += ", " + "[PMNM]:" + pmnm;
	        returnString += ", " + "[PMV1]:" + pmv1;
	        returnString += ", " + "[PMV2]:" + pmv2;
	        returnString += ", " + "[PMV3]:" + pmv3;
	        returnString += ", " + "[PMV4]:" + pmv4;
	        returnString += ", " + "[PMV5]:" + pmv5;
	        returnString += ", " + "[INSERTTIMESTAMP]:" + inserttimestamp;
	        returnString += ", " + "[UPDATETIMESTAMP]:" + updatetimestamp;
	        return returnString;
	    }
}
