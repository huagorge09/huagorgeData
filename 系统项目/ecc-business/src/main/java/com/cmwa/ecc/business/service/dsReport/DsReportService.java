package com.cmwa.ecc.business.service.dsReport;

/**
 * @author ex-wuh2
 *
 */
public interface DsReportService {
	/**
	 * 根据操作代码取URL
	 */
	public String getUrlByCode(String opCode);
	/**
	 * 获取SSOKey
	 */
	public String getSSOKey();
	/**
	 * 取操作员ID
	 */
	public String getCustId(String opid);
	
	/*获取加密后的url */
	public String getUrlAndEncrypt(String opCode,String loginid,String src,String curtime,String SSOKey);

}
