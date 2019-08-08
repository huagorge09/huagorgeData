/**
* @Title: ExportDataService.java  
* @author ex-wuh2  
* @date 2018年6月12日  
* @version V1.0  
 */
package com.cmwa.ecc.business.service.query;

import javax.servlet.http.HttpServletResponse;

import com.cmwa.ecc.business.utils.SearchParam;

/**
 * @author ex-wuh2
 */
public interface ExportDataService {
	
	//当前账户数据导出
	
	public void exportAccountApp(SearchParam sp,HttpServletResponse response);

	//当前交易数据导出
	
	public void exportTradeApp(SearchParam sp,HttpServletResponse response);

	//历史账户数据导出
	
	public void exportAccountAppHis(SearchParam sp,HttpServletResponse response);
	
	//历史交易数据导出
	
	public void exportTradeAppHis(SearchParam sp,HttpServletResponse response);
	
	//历史二级账户数据导出
	
	public void exportAccountAckHis(SearchParam sp,HttpServletResponse response);
	
	//历史二级交易数据导出
	
	public void exportTradeAckHis(SearchParam sp,HttpServletResponse response);
	
	//账户信息数据导出
	
	public void exportAccountInfo(SearchParam sp,HttpServletResponse response);
	
	//基金余额数据导出
	
	public void exportFundBalance(SearchParam sp,HttpServletResponse response);

	//客户评估数据导出
	
	public void exportCustomerData(SearchParam sp,HttpServletResponse response);
	
	//交易资料导出
	
	public void exportTradeData(SearchParam sp,HttpServletResponse response);
}
