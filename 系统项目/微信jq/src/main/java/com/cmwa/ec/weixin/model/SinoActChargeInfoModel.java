package com.cmwa.ec.weixin.model;

public class SinoActChargeInfoModel {

	private String status;
	private String msg;
	private String order;
	private SinoActChargeInfoDataModel data;
	
	public SinoActChargeInfoModel() {
		this.data = new SinoActChargeInfoDataModel();
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
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}
	/**
	 * @return the data
	 */
	public SinoActChargeInfoDataModel getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(SinoActChargeInfoDataModel data) {
		this.data = data;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SinoActChargeInfoModel [status=" + status + ", msg=" + msg + ", order=" + order + ", data=" + data
				+ "]";
	}
	
}
