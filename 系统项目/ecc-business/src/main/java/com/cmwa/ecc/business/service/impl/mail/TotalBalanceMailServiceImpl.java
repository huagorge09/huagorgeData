package com.cmwa.ecc.business.service.impl.mail;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ecc.business.constant.MailConfigConstant;
import com.cmwa.ecc.business.dao.fundinfo.FundInfoDao;
import com.cmwa.ecc.business.dao.workdays.WorkdaysDao;
import com.cmwa.ecc.business.entity.mail.MailAppendDetail;
import com.cmwa.ecc.business.entity.mail.MailConfPath;
import com.cmwa.ecc.business.entity.mail.MailConfig;
import com.cmwa.ecc.business.entity.mail.MailMessage;
import com.cmwa.ecc.business.entity.workdays.WorkdaysVo;
import com.cmwa.ecc.business.mail.send.MailSender;
import com.cmwa.ecc.business.service.mail.MailService;
import com.cmwa.ecc.business.service.mail.TotalBalanceMailService;
import com.cmwa.ecc.business.utils.DateUtils;
import com.cmwa.ecc.business.utils.NumberToCN;
import com.cmwa.ecc.business.utils.SearchParam;

import net.sf.json.JSONObject;

@Service
public class TotalBalanceMailServiceImpl   implements  TotalBalanceMailService{
	
	@Autowired
	private FundInfoDao  fundInfoDao;
	
	@Autowired
	private WorkdaysDao workdaysDao;
	
	@Autowired 
	private MailService  mailService;
	
	private Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void remindAllBalconsSendMail() {
		logger.info("TotalBalanceMailServiceImpl类【remindAllBalconsSendMail】开始>>>timestamp:"+System.currentTimeMillis());	
		MailConfig mailConfig = new MailConfig();
		mailConfig.setMcId(MailConfigConstant.REMIND_0004);
		try{
		//如果邮件是停止状态则不执行发送邮件		
		if(!checkMailConfigStatus(MailConfigConstant.REMIND_0004)){			
			return ;
		}		
		mailConfig.setRunStatus(MailConfigConstant.MAIL_CON_RUN);
		mailService.mailStatusOperate(mailConfig);
		Date  sysdate = new Date();
		String sysDateStr = DateUtils.formatDate(sysdate, "yyyyMMdd");
		if(!checkSendTime(sysDateStr)){
		    logger.info("MailManagerImpl类【remindAllBalconsSendMail】当前时间不发送提醒邮件,当前时间是：" + sysDateStr);	
			return ;
		}	
//		//当前日前一个工作日
//		String lastWorkDate = getLastWorkDate(sysDateStr);
//		//计算T+7
//		String  next7Dt="";
//		 int diff = (int) ((DateUtils.parseString(sysDateStr, "yyyyMMdd").getTime() - DateUtils.parseString(lastWorkDate, "yyyyMMdd").getTime()) / (1000*3600*24));
//		 String restDate =  lastWorkDate;
//		 while(diff >= 1){
//			next7Dt = getLatestWorkDate(DateUtils.formatDate(new Date(DateUtils.parseString(restDate, "yyyyMMdd").getTime() +  (long)7 * 24 * 60 * 60 * 1000),"yyyyMMdd"));
//			if(next7Dt.equals(DateUtils.addDay(restDate, "yyyyMMdd", 7))){				
//				Map	paramMap = new HashMap();
//				paramMap.put("allowedRedeemDt", next7Dt);
//				List<Map>    result = fundInfoDao.queryAllBalconsByDate(paramMap);		
//				if(null != result && result.size() >0){
//					 logger.info("TotalBalanceMailServiceImpl【remindAllBalconsSendMail】发送邮件，日期：" + next7Dt);
//					 sendBalanceMail( result,next7Dt);					
//				}
//			}	
//			
//			restDate = DateUtils.addDay(restDate, "yyyyMMdd", 1);	
//			diff = (int) ((DateUtils.parseString(sysDateStr, "yyyyMMdd").getTime() - DateUtils.parseString(restDate, "yyyyMMdd").getTime()) / (1000*3600*24));
//			 
//		 }
		 
		  String allowedRedeemDt = getAllowedRedeemDt();
		  Map	paramMap = new HashMap();
		  paramMap.put("allowedRedeemDt", allowedRedeemDt);
		  List<Map>    result = fundInfoDao.queryAllBalconsByDate(paramMap);	
		  if(null != result && result.size() >0){				 
				 sendBalanceMail( result,allowedRedeemDt);					
			}
		 
         }catch (Exception e) {        	 
        	 logger.info("TotalBalanceMailServiceImpl【remindAllBalconsSendMail】发送邮件异常。。。。。。" , e);
			
		}finally{		 
		 mailConfig.setRunStatus(MailConfigConstant.MAIL_CON_STOP);
		 mailService.mailStatusOperate(mailConfig);
		 logger.info("TotalBalanceMailServiceImpl类【remindAllBalconsSendMail】结束>>>timestamp:"+System.currentTimeMillis());
		}
	}
	
	
	private String getAllowedRedeemDt() throws Exception {
		String result = "";
    	String dt = DateUtils.formatDate(new Date(),"yyyyMMdd");
		int week = DateUtils.getDayOfWeek(dt);
		result = DateUtils.formatDate(new Date(DateUtils.parseString(dt, "yyyyMMdd").getTime() +  (long)6 * 24 * 60 * 60 * 1000),"yyyyMMdd");
		if(week == 1){
			result = DateUtils.formatDate(new Date(DateUtils.parseString(dt, "yyyyMMdd").getTime() +  (long)4 * 24 * 60 * 60 * 1000),"yyyyMMdd");
		}		
		//取最近工作日
		return getLatestWorkDate(result);
	}


	protected  JSONObject  sendBalanceMail(List<Map> balcons,String redeemDt){
		JSONObject returnJsonObject = new JSONObject();
		MailMessage mailMessage = new MailMessage();
		MailConfig mailConfig = null;
		String[] mailcc = new String[]{};
		String[] mailto = new String[]{};
		String[] mailscc = new String[]{};
		try {
			mailConfig = mailService.queryMailConfig("4");
			MailConfPath mailPath = mailService.queryMailPath("4");	
			if(null != mailPath){
		    mailcc = StringUtils.isBlank(mailPath.getMailCc())?new String[]{} : mailPath.getMailCc().split(",");
		    mailto = StringUtils.isBlank(mailPath.getMailTo())?new String[]{} : mailPath.getMailTo().split(",");	
		    mailscc = StringUtils.isBlank(mailPath.getMailScc())?new String[]{} : mailPath.getMailScc().split(",");
			logger.info("查询邮件配置==============" + new Date()+":"+mailConfig.toString() + ";mailPath:" + mailPath.getMailCc());
			}
		} catch (Exception e1) {
			logger.error("查询邮件配置 Exception.......................", e1);
		}
		if(null == mailConfig){
			logger.info("查询邮件配置结果为空" + new Date());
			returnJsonObject.put("returnCode", "9999");
		    return returnJsonObject;
		}
		
		String[] tccs = queryMailToOrCcInfo("4","cc"); 
		String[] ttos = queryMailToOrCcInfo("4","to");
		String[] tsccs = this.queryMailToOrCcInfo(MailConfigConstant.REMIND_0004,"scc");  
		
		String[] ccs = new String[tccs.length + mailcc.length];
		String[] tos = new String[ttos.length + mailto.length];
		String[] sccs = new String[tsccs.length + mailscc.length];
		
		System.arraycopy(mailcc, 0, ccs, 0, mailcc.length);
		System.arraycopy(tccs, 0, ccs, mailcc.length, tccs.length);
		
		System.arraycopy(mailto, 0, tos, 0, mailto.length);
		System.arraycopy(ttos, 0, tos, mailto.length, ttos.length);
		
		System.arraycopy(mailscc, 0, sccs, 0, mailscc.length);
		System.arraycopy(tsccs, 0, sccs, mailscc.length, tsccs.length);
		
		if(tos.length == 0 && ccs.length == 0 && sccs.length == 0  ){
			logger.info("TotalBalanceMailServiceImpl类【sendBalanceMail】没有配置收件人信息。。。。。。");
			return null;
		}
		mailMessage.setTo(tos);
		mailMessage.setCc(ccs);
		mailMessage.setScc(sccs);
		
		String content = "<!DOCTYPE html><html><head><meta charset='UTF-8'><meta http-equiv=content-type content='text/html;charset=utf-8'>"
				+"</head><body><table style='width: 600px;border-color: #666666;font-family: verdana,arial,sans-serif;font-size:15px;border-collapse: collapse;text-align: center;cellpadding='0' cellspacing='0'>"
				+"<tr style='background: #d3d3d3;'>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>最大赎回规模对应日期</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>基金代码</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>基金名称</td>"		
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>最大赎回规模(万份)</td></tr>";
				
				if(null != balcons && balcons.size() >0){
					
					for (Iterator iterator = balcons.iterator(); iterator.hasNext();) {
						 Map balconInfo = (Map) iterator.next();
						 
						 double share = Double.parseDouble( balconInfo.get("BALANCE")==null?"0":balconInfo.get("BALANCE")+"")/10000;
						 content +=  " <tr>"				
					                + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+redeemDt+ "("+ DateUtils.dateToWeek(redeemDt) + ")" +"</td>"
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+balconInfo.get("FUNDID")+"</td>"
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+ balconInfo.get("FUNDCHINESENM")+"</td>"		        
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;text-align: right;'>"+NumberToCN.parseNumber("###,###.0",new BigDecimal(share))+"</td>"
							        + "</tr> ";
						
					}
				}	
				content += "</table></body></html>";
				
		mailMessage.setContent(MessageFormat.format(mailConfig.getBody(), redeemDt, DateUtils.dateToWeek(redeemDt),DateUtils.formatDate(new Date(), "yyyyMMdd"),content));		
		mailMessage.setSubject(String.format(mailConfig.getHead(),redeemDt, DateUtils.dateToWeek(redeemDt)));
		mailMessage.setConfId("4");
		mailMessage.setPrefix("custom2");  

		
		
		try {
			MailSender.getMailSender().add(mailMessage);
			returnJsonObject.put("returnCode", "0000");			
		} catch (Exception e) {
			logger.error("发送邮件异常："+e.getMessage());
			returnJsonObject.put("returnCode", "9999");		
		}		
		return returnJsonObject;
		
	}
	
	
	private boolean checkSendTime(String sysDateStr ) throws Exception {		
		//如果当前不是工作日就不发送邮件
//		if(!sysDateStr.equals(getLatestWorkDate(sysDateStr))){		 
//		   return false;		   
//		}
		//只有周六和周日不发
		int week = DateUtils.getDayOfWeek(sysDateStr);
		if(week == 6 || week == 0){
			return false;
		}
		return true;
	}
	
    private boolean checkMailConfigStatus(String mailConfigId) throws Exception {
			MailConfig	mailConfig = mailService.queryMailConfig(mailConfigId);
			if(null == mailConfig  ){	
				 logger.info("TotalBalanceMailServiceImpl类【checkMailConfigStatus】找不到郵件配置，mailConfigid:" + mailConfigId);
				return  false;
			}
			
			if(MailConfigConstant.MAIL_CON_RUN.equals(mailConfig.getRunStatus()) ){
				 logger.info("TotalBalanceMailServiceImpl类【checkMailConfigStatus】邮件发送执行中。。。。。，mailConfigid:" + mailConfigId);
				return false;
			}		
			return true;
		}
    
    
       private String getLatestWorkDate(String date) throws Exception{
    	   String result =  date;    	  
    	   SearchParam sp = new SearchParam();
		   Map map = new HashMap();
    	   map.put("cycleendt", date);			
		   sp.setSp(map);	
		   WorkdaysVo workdaysDto;		
		   workdaysDto = workdaysDao.getWorkDateByDT(sp);
		   result = workdaysDto.getWorkdate();			
		   return result;
       }
       
       private String getLastWorkDate(String date)  throws Exception{
    	   String result =  date;    	  
    	   SearchParam sp = new SearchParam();
		   Map map = new HashMap();
    	   map.put("cycleendt", date);
		   map.put("nextn",-1);
		   sp.setSp(map);	
		   WorkdaysVo workdaysDto =  workdaysDao.getWorkDateByDT(sp);
		   result = workdaysDto.getWorkdate();		
		   return result;
    	   
       }
		
//		@SuppressWarnings({ "unchecked", "rawtypes" })
//		protected String getWorkDate(String currentWorkDate,String num,String type,String flag) throws Exception {
//			SearchParam sp = new SearchParam();
//			Map map = new HashMap();
//			String result =  currentWorkDate;
//			if("1".equals(flag)){		
//			map.put("now", currentWorkDate);
//			map.put("datenum", StringUtils.isBlank(num)? "0" : num);
//			map.put("type", type);
//			map.put("fundid", "");
//			sp.setSp(map);
//			workdaysDao.getOtherWorkDate(sp);
//			if (sp.getSp().get("resultCode").equals("0000")) {
//				result = sp.getSp().get("workdate").toString();
//			}
//			}else if("2".equals(flag)){
//				map.put("cycleendt", currentWorkDate);			
//				sp.setSp(map);	
//				WorkdaysVo workdaysDto =  workdaysDao.getWorkDateByDT(sp);
//				result = workdaysDto.getWorkdate();			
//			}else if("3".equals(flag)){	
//				map.put("apkind", "");
//				sp.setSp(map);	
//				workdaysDao.getWorkDateNow(sp);
//				if (sp.getSp().get("resultCode").equals("0000")) {
//					result = sp.getSp().get("workdate").toString();
//				}
//			}else if("4".equals(flag)){
//				map.put("cycleendt", currentWorkDate);
//				map.put("nextn",-1);
//				sp.setSp(map);	
//				WorkdaysVo workdaysDto =  workdaysDao.getWorkDateByDT(sp);
//				result = workdaysDto.getWorkdate();		
//				
//			}
//			return result;
//		}
		
		
		
		private String[] queryMailToOrCcInfo(String configId,
				String type) {
			List<MailAppendDetail> mailPathList = null;
			try {
			   mailPathList = mailService.queryMailAppendList(configId, type);
			} catch (Exception e) {
				logger.error("查询收件和和抄送人异常",e);
			}
			String[] mailPath = new String [mailPathList.size()];
			for(int i = 0; i < mailPathList.size(); i++){
				mailPath[i] = mailPathList.get(i).getMailPath();
			}
			return mailPath;
		}

	

}
