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

import com.cmwa.ecc.business.entity.trade.TradeAckClearHisVo;
import com.cmwa.ecc.business.entity.trade.TradeAckClearVo;
import com.cmwa.ecc.business.entity.trade.TradeAckTodayVo;
import com.cmwa.ecc.business.entity.trade.TradeAppHisVo;
import com.cmwa.ecc.business.entity.trade.TradeAppTodayVo;
import com.cmwa.ecc.business.entity.trade.TradeCapitalBlotterVo;
import com.cmwa.ecc.business.entity.trade.TradeDividendDetailHisVo;
import com.cmwa.ecc.business.entity.trade.TradeDividendDetailVo;
import com.cmwa.ecc.business.service.dsReport.DsReportService;
import com.cmwa.ecc.business.service.dsquery.DSQueryService;
import com.cmwa.ecc.business.service.query.ExportDataService;
import com.cmwa.ecc.business.service.query.TradeQueryService;
import com.cmwa.ecc.business.utils.DateUtils;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

import net.sf.json.JSONObject;

/**
 * @author ex-wuh2
 *
 */
@Controller
@RequestMapping(value = "service/tradeSearch")
public class TradeSearchController {
	
	@Autowired
	private TradeQueryService tradeQueryService; 
	
	@Autowired
	private DsReportService dsReportService;
	
	@Autowired
	private ExportDataService exportDataService;
	
	@Autowired
	private DSQueryService dSQueryService;
	
	private static Logger logger = Logger.getLogger(TradeSearchController.class);	
	
	@RequestMapping(value = "/tradeSrh.do",method = RequestMethod.GET)
	public String goTradeSearchView(ModelMap map) {
		JSONObject jsonObject = dSQueryService.queryEccSystemInfo();
		String workdate = (String) jsonObject.get("WORKDATE");
		String startDate = workdate;
		String strDate = workdate;
		String endDate = workdate;
		String startEndDate = strDate + " - " + endDate;
		map.put("startDate",startDate);
		map.put("startEndDate", startEndDate);
		return "jsp/query/tradeSearch";
	}
	
	/**
	 *  跳转至账户类报表查询页面
	 */
	
	@RequestMapping(value = "/tradeInfoReport.do",method = RequestMethod.GET)
	public String goTradeInfoReportSearchView(ModelMap map) {		
		return "jsp/report/tradeinfo";
	}
	
	/* 当前账户查询  */
	@RequestMapping(value ="/queryCurrentTrade.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<TradeAppTodayVo> queryTradeAppTodayListPage(SearchParam sp) throws Exception {
		return 	tradeQueryService.queryTradeAppTodayListPage(sp);
	}
	
	/* 资金流水查询  */
	@RequestMapping(value ="/queryTradeCapitalBlotter.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<TradeCapitalBlotterVo> queryTradeCapitalBlotterListPage(SearchParam sp) throws Exception {
		return 	tradeQueryService.queryTradeCapitalBlotterListPage(sp);
	}
	
	/* 二级清算流水查询  */
	@RequestMapping(value ="/queryTradeAckClear.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<TradeAckClearVo> queryTradeAckClearListPage(SearchParam sp) throws Exception {
		return 	tradeQueryService.queryTradeAckClearListPage(sp);
	}
	
	/* TA交易流水查询  */
	@RequestMapping(value ="/queryTATrade.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<TradeAckTodayVo> queryTradeAckTodayListPage(SearchParam sp) throws Exception {
		return 	tradeQueryService.queryTradeAckTodayListPage(sp);
	}
	
	/* 当天分红流水查询  */
	@RequestMapping(value ="/queryTradeDividendDetail.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<TradeDividendDetailVo> queryTradeDividendDetailListPage(SearchParam sp) throws Exception {
		return 	tradeQueryService.queryTradeDividendDetailListPage(sp);
	}
	
	/* 历史分红流水查询  */
	@RequestMapping(value ="/queryHistoryMelonmd.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<TradeDividendDetailHisVo> queryTradeDividendDetailHisListPage(SearchParam sp) throws Exception {
		return 	tradeQueryService.queryTradeDividendDetailHisListPage(sp);
	}
	
	/* 历史交易流水查询  */
	@RequestMapping(value ="/queryTradeAppHis.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<TradeAppHisVo> queryTradeAppHisListPage(SearchParam sp) throws Exception {
		return 	tradeQueryService.queryTradeAppHisListPage(sp);
	}
	
	/* 历史二级交易流水查询  */
	@RequestMapping(value ="/queryTradeAckClearHis.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<TradeAckClearHisVo> queryTradeAckClearHisListPage(SearchParam sp) throws Exception {
		return 	tradeQueryService.queryTradeAckClearHisListPage(sp);
	}
	
	/*当前交易流水导出*/
	@RequestMapping(value = "/exportTradeAppToday.xhtml",method= {RequestMethod.POST})
	public void exportTradeAppToday(SearchParam sp,HttpServletResponse response) {
		exportDataService.exportTradeApp(sp, response);
	}
	
	/*历史交易流水导出*/
	@RequestMapping(value = "/exportTradeAppHistory.xhtml",method= {RequestMethod.POST})
	public void exportTradeAppHis(SearchParam sp,HttpServletResponse response) {
		exportDataService.exportTradeAppHis(sp, response);
	}
	
	/*历史二级交易流水导出*/
	@RequestMapping(value = "/exportTradeClearHis.xhtml",method= {RequestMethod.POST})
	public void exportTradeAckHis(SearchParam sp,HttpServletResponse response) {
		exportDataService.exportTradeAckHis(sp, response);
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
