package com.cmwa.ecc.business.controller.query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.entity.query.AcctAckClearHisVo;
import com.cmwa.ecc.business.entity.query.AcctAckClearVo;
import com.cmwa.ecc.business.entity.query.AcctAckTodayVo;
import com.cmwa.ecc.business.entity.query.AcctAppHisVo;
import com.cmwa.ecc.business.entity.query.AcctAppModifyVo;
import com.cmwa.ecc.business.entity.query.AcctAppTodayVo;
import com.cmwa.ecc.business.service.common.CommonService;
import com.cmwa.ecc.business.service.dsReport.DsReportService;
import com.cmwa.ecc.business.service.dsquery.DSQueryService;
import com.cmwa.ecc.business.service.query.AccountQueryService;
import com.cmwa.ecc.business.service.query.ExportDataService;
import com.cmwa.ecc.business.utils.DateUtils;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

@Controller
@RequestMapping("service/accountSearch")
public class AccountSearchController {
	
	private static Logger logger = Logger.getLogger(AccountSearchController.class);	

	@Autowired
	private AccountQueryService accountQueryService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ExportDataService exportDataService;
	
	@Autowired
	private DsReportService dsReportService;
	
	@Autowired
	private DSQueryService dSQueryService;
	/**
	 *  跳转至账户类查询页面
	 */
	
	@RequestMapping(value = "/accountSrh.do",method = RequestMethod.GET)
	public String goAccountSearchView(ModelMap map) {
		JSONObject jsonObject = dSQueryService.queryEccSystemInfo();
		String workdate = (String) jsonObject.get("WORKDATE");
		String startDate = workdate;
		String strDate = workdate;
		String endDate = workdate;
		String startEndDate = strDate + " - " + endDate;
		map.put("startDate",startDate);
		map.put("startEndDate", startEndDate);
		return "jsp/query/accountSearch";
	}
	
	
	/**
	 *  跳转至账户类报表查询页面
	 */
	
	@RequestMapping(value = "/custInfoReport.do",method = RequestMethod.GET)
	public String goCustInfoReportSearchView(ModelMap map) {		
		return "jsp/report/custinfo";
	}
	
	/* 当前账户查询  */
	
	@RequestMapping(value ="/queryCurrentAcc.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<AcctAppTodayVo> queryCurrentAccListPage(SearchParam sp) throws Exception {
		return 	accountQueryService.queryAcctAppTodayListPage(sp);
	}
	
	/* TA账户查询 */
	
	@RequestMapping(value ="/queryTAAcc.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<AcctAckTodayVo> queryTAAccListPage(SearchParam sp) throws Exception {
		return 	accountQueryService.queryAcctAckTodayListPage(sp);
	}
	
	/*二级清算账户查询 */
	
	@RequestMapping(value ="/queryAckClearAcc.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<AcctAckClearVo> queryAcctAckClearListPage(SearchParam sp) throws Exception {
		return 	accountQueryService.queryAcctAckClearListPage(sp);
	}
	
	/*历史账户查询*/
	
	@RequestMapping(value ="/queryAppHisAcc.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<AcctAppHisVo> queryAcctAppHisListPage(SearchParam sp) throws Exception {
		return 	accountQueryService.queryAcctAppHisListPage(sp);
	}
	
	/*历史二级清算账户查询*/
	
	@RequestMapping(value ="/queryAppClearHisAcc.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<AcctAckClearHisVo> queryAcctAckClearHisListPage(SearchParam sp) throws Exception {
		return 	accountQueryService.queryAcctAckClearHisListPage(sp);
	}
	
	/*客户账户修改查询*/
	
	@RequestMapping(value ="/queryAppModifyAcc.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<AcctAppModifyVo> queryAcctModifyListPage(SearchParam sp) throws Exception {
		return 	accountQueryService.queryAcctModifyListPage(sp);
	}
	/*获取业务类型*/
	@RequestMapping(value ="/queryApkindParameter.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public List<ParameterVo> queryApkindParameter(SearchParam sp) throws Exception {
		return 	commonService.getParameterListPage(sp);
	}
	/*当前账户导出*/
	@RequestMapping(value = "/exportAcctAppToday.xhtml",method= {RequestMethod.POST})
	public void exportAcctAppToday(SearchParam sp,HttpServletResponse response) {
		exportDataService.exportAccountApp(sp, response);
	}
	/*历史账户导出*/
	@RequestMapping(value = "/exportAcctAppHistory.xhtml",method= {RequestMethod.POST})
	public void exportAcctAppHistory(SearchParam sp,HttpServletResponse response) {
		exportDataService.exportAccountAppHis(sp, response);
	}
	/*历史二级清算账户导出 */
	@RequestMapping(value = "/exportAcctClearHistory.xhtml",method= {RequestMethod.POST})
	public void exportAcctClearHistory(SearchParam sp,HttpServletResponse response) {
		exportDataService.exportAccountAckHis(sp, response);
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
