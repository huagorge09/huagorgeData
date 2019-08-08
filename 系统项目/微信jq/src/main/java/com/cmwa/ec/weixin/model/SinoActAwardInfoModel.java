package com.cmwa.ec.weixin.model;

import java.util.List;

public class SinoActAwardInfoModel {

	/**
	 * ¶©µ¥ºÅ
	 */
	private String reqStreamId;
	
	/**
	 * Æ½Ì¨Á÷Ë®ºÅ
	 */
	private String orderNo;
	
	/**
	 * ×´Ì¬Âë
	 */
	private String status;
	
	/**
	 * ×´Ì¬ÃèÊö
	 */
	private String msg;
	
	private List<SinoActAwardInfoDataModel> data;
	
	/**
	 * @return the reqStreamId
	 */
	public String getReqStreamId() {
		return reqStreamId;
	}

	/**
	 * @param reqStreamId the reqStreamId to set
	 */
	public void setReqStreamId(String reqStreamId) {
		this.reqStreamId = reqStreamId;
	}

	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}


	/**
	 * @return the data
	 */
	public List<SinoActAwardInfoDataModel> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<SinoActAwardInfoDataModel> data) {
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SinoActAwardInfoModel [reqStreamId=" + reqStreamId
				+ ", orderNo=" + orderNo + ", status=" + status + ", msg="
				+ msg + ", data=" + data + "]";
	}
	

}

