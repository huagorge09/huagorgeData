package com.cmwa.ecc.business.controller.query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ecc.business.entity.multiple.FundBalanceSrhVo;
import com.cmwa.ecc.business.entity.multiple.FundCustInfoVo;
import com.cmwa.ecc.business.entity.multiple.FundNavVo;
import com.cmwa.ecc.business.service.dsReport.DsReportService;
import com.cmwa.ecc.business.service.query.ExportDataService;
import com.cmwa.ecc.business.service.query.MultipleQueryService;
import com.cmwa.ecc.business.utils.DateUtils;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

/**
 * @author ex-wuh2
 */
@Controller
@RequestMapping(value = "service/multipleSearch")
public class MultipleSearchController {
	
	@Autowired
	private MultipleQueryService multipleQueryService;
	
	@Autowired
	private ExportDataService exportDataService;
	
	@Autowired
	private DsReportService dsReportService;
	
	private static Logger logger = Logger.getLogger(MultipleSearchController.class);	
	
	
	//跳转至综合查询页面
	 
	@RequestMapping(value = "/multipleSrh.do",method = RequestMethod.GET)
	public String goAccountSearchView(ModelMap map) {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		date = cal.getTime();
		String startDate = DateUtils.formatDate(date,"yyyy-MM-dd");
		//起始时间的date
		String endDate = DateUtils.formatDate(date,"yyyy/MM/dd");
		cal.add(Calendar.DATE, -7);
		Date date2 = cal.getTime();
		String strDate = DateUtils.formatDate(date2,"yyyy/MM/dd");
		String startEndDate = strDate + " - " + endDate;
		map.put("startDate",startDate);
		map.put("startEndDate", startEndDate);
		return "jsp/query/multipleSearch";
	}
	
	/* 账户信息查询  */
	
	@RequestMapping(value ="/queryFundCustInfo.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<FundCustInfoVo> queryFundCustInfoListPage(SearchParam sp) throws Exception {
		return 	multipleQueryService.queryFundCustInfoListPage(sp);
	}
	
	/*基金净值  */
	
	@RequestMapping(value ="/queryFundNav.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<FundNavVo> queryFundNavListPage(SearchParam sp) throws Exception {
		return 	multipleQueryService.queryFundNavListPage(sp);
	}
	
	/*基金余额  */
	
	@RequestMapping(value ="/queryFundBalance.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<FundBalanceSrhVo> queryFundBalanceListPage(SearchParam sp) throws Exception {
		return 	multipleQueryService.queryFundBalanceListPage(sp);
	}
	
	/*账户信息导出  */
	
	@RequestMapping(value ="/exportAccountInfo.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	public void exportAccountInfo(HttpServletResponse response,SearchParam sp) throws Exception {
		exportDataService.exportAccountInfo(sp,response);
	}
	
	/*基金余额导出  */
	
	@RequestMapping(value ="/exportFundBalance.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	public void exportFundBalance(HttpServletResponse response,SearchParam sp) throws Exception {
		exportDataService.exportFundBalance(sp,response);
	}
	
	/**
	 *  跳转至综合类类报表查询页面
	 */
	
	@RequestMapping(value = "/colligateReport.do",method = RequestMethod.GET)
	public String goColligateReportSearchView(ModelMap map) {		
		return "jsp/report/colligate";
	}
	
	/*获取报表链接 */
	@RequestMapping(value = "/getDsReportLink.xhtml",method= {RequestMethod.GET})
	public void getDsReportLink(HttpServletRequest request,HttpServletResponse response) throws Exception {
	     String operator = SessionUtils.getEmployee().getID();//操作员
	     String permissionId = request.getParameter("permissionId");//获取操作代码
		 String PI_APPNO = request.getParameter("PI_APPNO");//申请单编号
		 String PI_APSDT = request.getParameter("PI_APSDT");//申请开始日期
		 String PI_APEDT = request.getParameter("PI_APEDT");//申请结束日期
		 String PI_ACKNO = request.getParameter("PI_ACKNO");//确认单编号
		 String PI_ACKSDT = request.getParameter("PI_ACKSDT");//确认开始日期
		 String PI_ACKEDT = request.getParameter("PI_ACKEDT");//确认结束日期
		 StringBuffer params = new StringBuffer();
		 if(PI_APPNO != null && PI_APPNO.length()>0){
		 	params.append("&PI_APPNO="+PI_APPNO);
		 }
		 if(PI_ACKNO != null && PI_ACKNO.length()>0){
		 	params.append("&PI_ACKNO="+PI_ACKNO);
		 }
		 if(PI_ACKSDT != null && PI_ACKSDT.length()>0){
			PI_ACKSDT = PI_ACKSDT.replace("-", "").trim();
		 	params.append("&PI_ACKSDT="+PI_ACKSDT);
		 }
		 if(PI_ACKEDT != null && PI_ACKEDT.length()>0){
			PI_ACKEDT = PI_ACKEDT.replace("-", "").trim();
		 	params.append("&PI_ACKEDT="+PI_ACKEDT);
		 }
		 if(PI_APSDT != null && PI_APSDT.length()>0){
			PI_APSDT = PI_APSDT.replace("-", "").trim();
		 	params.append("&PI_APSDT="+PI_APSDT);
		 }
		 if(PI_APEDT != null && PI_APEDT.length()>0){
			PI_APEDT = PI_APEDT.replace("-", "").trim();
		 	params.append("&PI_APEDT="+PI_APEDT);
		 }
		String src = "ECC";
		String loginid = dsReportService.getCustId(operator);
		String SSOKey = dsReportService.getSSOKey();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMyyHHmmss");
		String curtime = sdf.format(new Date());
		String url = dsReportService.getUrlAndEncrypt(permissionId,loginid,src,curtime,SSOKey);
		response.sendRedirect(url+params.toString());
	}
}
