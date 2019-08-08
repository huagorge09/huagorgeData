/**
* @author ex-wuh2  
* @date 2018年6月13日  
* @version V1.0  
 */
package com.cmwa.ecc.business.dao.dsReport;

import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;

@MybatisDao
public interface DsReportDao {
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
}
