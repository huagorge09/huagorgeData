package com.cmwa.ec.webapp.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.base.util.MagicMap;
import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.mail.MailMessage;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.webapp.client.MessageServiceClient;
import com.cmwa.ec.webapp.client.QueryServiceClient;
import com.cmwa.ec.webapp.dao.ParameterDao;
import com.cmwa.ec.webapp.util.ContextUtils;
import com.cmwa.ec.webapp.util.ECConstants;

/**
 * @author niedc
 * 2014-12-18
 */

@Controller("TestController")
@RequestMapping(value = "/AppService")
public class TestController {
	
	@Autowired
	private ParameterDao parameterDao;
	
	@Autowired
	private QueryServiceClient queryServiceClient;
	
	@Autowired
	private MessageServiceClient messageServiceClient;
	
	
	@RequestMapping(value = "/log/save.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public void save(HttpServletResponse response, HttpServletRequest request) {
		Long l = System.currentTimeMillis();
		String curTimeStamp = l.toString();
		String reportName = request.getParameter("reportTypeName");
		String reportType = request.getParameter("reportType");
		String fundId = request.getParameter("fundId");
		String fundSName = request.getParameter("fundSName");
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String channel = request.getParameter("channel");
		Object obj = request.getSession(true).getAttribute("cmfUserId");
		if(obj != null){
			userId = (String)obj;
		}
		Object name = request.getSession(true).getAttribute("cmfUserName");
		if(name != null){
			userName = (String)name;
		}
		String ipAttr = getRemoteAddress(request);
		StringBuffer urlS = request.getRequestURL();
		String url = "";
		if(urlS == null || urlS.equals("")){
			url = "";
		}else {
			url = urlS.toString();
		}
		String uri = request.getRequestURI(); 
		
		MagicMap map = new MagicMap(new Object[][]{{"CURTIMESTAMP",curTimeStamp},{"REPORTNAME",reportName},{"REPORTTYPE",reportType},
				{"FUNDID",fundId},{"FUNDSNAME",fundSName},{"USERID",userId},{"USERNAME",userName},{"CHANNEl",channel},{"IPATTR",ipAttr}});
		parameterDao.insertLog(map);
	}

	@RequestMapping(value = "/test/test.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public QueryMessageDto test(HttpServletResponse response, HttpServletRequest request) {
		return queryServiceClient.queryHomeAddressIsWordWithLinkage(new Context(), "DS", "", "", "");
	}
	
	@RequestMapping(value = "/test/sendMail.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public MsgServiceMessageDto sendMail(HttpServletResponse response, HttpServletRequest request) {
		MsgServiceMessageDto dto = new MsgServiceMessageDto();
		//日志信息
	    Context context=ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP,"", System.currentTimeMillis(), System.currentTimeMillis(), "", "");
		MailMessage mailMessage = new MailMessage();
		/*String[] tos ={"chengt@cmwachina.com","tianmh@cmwachina.com","tongj@cmwachina.com"};
		String[] cc ={"ex-longyh@cmfchina.com"};*/
		String toMailPath = SpringUtil.getProperty("toMailPath");
		String[] tos = {};
		if(!StringUtil.isEmpty(toMailPath)){
			tos = toMailPath.split(",");
		}
		String ccMailPath = SpringUtil.getProperty("ccMailPath");
		String[] ccs = {};
		if(!StringUtil.isEmpty(ccMailPath)){
			ccs = ccMailPath.split(",");
		}
		mailMessage.setTo(tos);
		mailMessage.setCc(ccs);
		mailMessage.setContent("<!DOCTYPE html><html><head><meta charset='UTF-8'><meta http-equiv=content-type content='text/html;charset=utf-8'>"
				+"</head><body><table style='width: 600px;border-color: #666666;font-family: verdana,arial,sans-serif;font-size:15px;border-collapse: collapse;text-align: center;cellpadding='0' cellspacing='0'>"
				+"<tr style='background: #d3d3d3;'>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>姓名</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>电话</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>事项</td></tr>"
				+"<tr>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>张思思</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>13888888888</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>专业投资者申请</td></tr>"
				+"</table></body></html>");
		mailMessage.setSubject("电商客户张思思专业投资者申请");
		mailMessage.setConfId("1");
		try {
			dto = messageServiceClient.sendMail(context, mailMessage );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dto;
	}
	
	
	/***
	 * 获取IP
	 * @param request
	 * @return
	 */
	public String getRemoteAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");  
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
			ip = request.getHeader("Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
			ip = request.getHeader("WL-Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
		    ip = request.getRemoteAddr();  
		}  
		return ip;  
	}   
}
