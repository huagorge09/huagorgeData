package com.cmwa.ecc.business.controller.dsquery;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cmwa.ecc.business.service.dsquery.DSQueryService;

@Controller("DSQueryController")
@RequestMapping(value = "service/system/dsquery" )
public class DSQueryController {
	private static Logger logger = Logger.getLogger(DSQueryController.class);
	
	@Autowired
	DSQueryService dsQueryService;
	
	@RequestMapping(value="/sendRedirectUrl.xhtml",method={RequestMethod.GET})
	public void sendRedirectUrl(HttpServletRequest request,HttpServletResponse response) throws IOException{
		 
		String permissionId = request.getParameter("permissionId");//获取操作代码/交易代码
		 String PI_APPNO = request.getParameter("PI_APPNO");//申请单编号
		 String PI_APSDT = request.getParameter("PI_APSDT");//申请开始日期
		 String PI_APEDT = request.getParameter("PI_APEDT");//申请结束日期
		 String PI_ACKNO = request.getParameter("PI_ACKNO");//确认单编号
		 String PI_ACKSDT = request.getParameter("PI_ACKSDT");//确认开始日期
		 String PI_ACKEDT = request.getParameter("PI_ACKEDT");//确认结束日期
		 String opId = "";//操作员代码(String) request.getSession().getAttribute(ECCConstant.OPERATORID)
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
		 logger.info("DSQueryController.permissionId"+permissionId);
		 logger.info("DSQueryController.opId"+opId);
		 String url = dsQueryService.getUrlAndEncrypt(permissionId, opId);
		 logger.info("DSQueryController.url"+url);
		 logger.info("DSQueryController.params"+params.toString());
		response.sendRedirect(url+params.toString());
	}
	
	@RequestMapping(value = "/queryEccSystemInfo.xhtml", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject queryEccSystemInfo(HttpServletResponse response,HttpServletRequest request) throws Exception  {
		JSONObject  result = new JSONObject();
		result = dsQueryService.queryEccSystemInfo();
		return result;
	}
}
