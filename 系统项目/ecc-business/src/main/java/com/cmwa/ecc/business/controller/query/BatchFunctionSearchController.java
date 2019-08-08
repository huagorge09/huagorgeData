package com.cmwa.ecc.business.controller.query;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmwa.ecc.business.service.dsReport.DsReportService;
import com.cmwa.ecc.business.utils.SessionUtils;

@Controller
@RequestMapping("service/batchFunctionSearch")
public class BatchFunctionSearchController {
	private static Logger logger = Logger.getLogger(BatchFunctionSearchController.class);
	@Autowired
	private DsReportService dsReportService;

	/**
	 *  跳转至批量类报表查询页面
	 */
	
	@RequestMapping(value = "/batchFunctionReport.do",method = RequestMethod.GET)
	public String goCustInfoReportSearchView(ModelMap map) {		
		return "jsp/report/batchFunction";
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
		 	params.append("&PI_ACKSDT="+PI_ACKSDT);
		 }
		 if(PI_ACKEDT != null && PI_ACKEDT.length()>0){
		 	params.append("&PI_ACKEDT="+PI_ACKEDT);
		 }
		 if(PI_APSDT != null && PI_APSDT.length()>0){
		 	params.append("&PI_APSDT="+PI_APSDT);
		 }
		 if(PI_APEDT != null && PI_APEDT.length()>0){
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
