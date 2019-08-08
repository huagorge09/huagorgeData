package com.cmwa.ecc.business.service.impl.mail;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.constant.MailConfigConstant;
import com.cmwa.ecc.business.dao.fundinfo.FundInfoDao;
import com.cmwa.ecc.business.dao.workdays.WorkdaysDao;
import com.cmwa.ecc.business.entity.mail.MailAppendDetail;
import com.cmwa.ecc.business.entity.mail.MailConfPath;
import com.cmwa.ecc.business.entity.mail.MailConfig;
import com.cmwa.ecc.business.entity.mail.MailMessage;
import com.cmwa.ecc.business.entity.workdays.WorkdaysVo;
import com.cmwa.ecc.business.mail.send.MailSender;
import com.cmwa.ecc.business.service.mail.AllowedBalanceMailService;
import com.cmwa.ecc.business.service.mail.MailService;
import com.cmwa.ecc.business.utils.DateUtils;
import com.cmwa.ecc.business.utils.NumberToCN;
import com.cmwa.ecc.business.utils.SearchParam;

import net.sf.json.JSONObject;
@Service
public class AllowedBalanceMailServiceImpl      implements  AllowedBalanceMailService {
	private Logger logger = LoggerFactory.getLogger(AllowedBalanceMailServiceImpl.class);
	@Autowired
	private FundInfoDao  fundInfoDao;
	@Autowired
	private WorkdaysDao workdaysDao;
	
	@Autowired 
	private MailService  mailService;
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void remindRedeemSendMail()  {
		logger.info("AllowedBalanceMailServiceImpl类【remindRedeemSendMail】开始>>>timestamp:"+System.currentTimeMillis());
		MailConfig mailConfig = new MailConfig();
		mailConfig.setMcId(MailConfigConstant.REMIND_0003);
		
		try {			
		
		//如果邮件是停止状态则不执行发送邮件		
			if(!checkMailConfigStatus(MailConfigConstant.REMIND_0003)){			
				return ;
			}
			
			mailConfig.setRunStatus(MailConfigConstant.MAIL_CON_RUN);
			mailService.mailStatusOperate(mailConfig);
			
			//查询当前工作日
			WorkdaysVo workdaysDto = new WorkdaysVo();
			SearchParam sp = new SearchParam();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("apkind", "");
			sp.setSp(map);	
			workdaysDao.getWorkDateNow(sp);
			if (sp.getSp().get("resultCode").equals("0000")) {
				workdaysDto.setResultCode(sp.getSp().get("resultCode").toString());
				workdaysDto.setWorkdate(sp.getSp().get("workdate").toString());    //过了当天15点取的时下一个工作
			}
			Date  sysdate = new Date();
			String sysDateStr = DateUtils.formatDate(sysdate, "yyyyMMdd");
			//当前系统时间跟工作日不是同一天不发邮
			if(!sysDateStr.equals(workdaysDto.getWorkdate())){
			   logger.info("AllowedBalanceMailServiceImpl类【remindRedeemSendMail】当前时间不发送提醒邮件");
			   return;
			}
			//推算后面两个工作日日期
			map.clear();
			map.put("now", sysDateStr);
			map.put("datenum", "2");
			map.put("type", "1");
			map.put("fundid", "");
			sp.setSp(map);
			workdaysDao.getOtherWorkDate(sp);
			if (sp.getSp().get("resultCode").equals("0000")) {
				workdaysDto.setResultCode(sp.getSp().get("resultCode").toString());
				workdaysDto.setWorkdate(sp.getSp().get("workdate").toString());
			}
			Map paramMap = new HashMap();
			paramMap.put("allowedRedeemDt", workdaysDto.getWorkdate());
			
			List<Map> result = fundInfoDao.queryAllowedBalcons(paramMap);
			
			//根据客户号归组	
			Map<String,List<Map>>  balconsInfo = new HashMap<String,List<Map>>();		
			if(null !=result &&  result.size() > 0){
				for (Iterator iterator = result.iterator(); iterator.hasNext();) {
					Map bancon = (Map) iterator.next();
					if (balconsInfo.containsKey(bancon.get("CUSTNO").toString())){
						balconsInfo.get(bancon.get("CUSTNO").toString()).add(bancon);
					}else{
						List<Map> balcons = new ArrayList<Map>();
						balcons.add(bancon);	
						balconsInfo.put(bancon.get("CUSTNO").toString(), balcons);
					}				
				}
			}
			
			//发送信息
			
			 for (Entry<String, List<Map>> entry : balconsInfo.entrySet()) {
				 List<Map> balcons =   entry.getValue();
				 if(null !=balcons && balcons.size() >0 ){				 
					 sendBalconsMail(balcons,workdaysDto.getWorkdate());
				 }        
		     }
			 
		} catch (Exception e) {
			logger.error("AllowedBalanceMailServiceImpl类【remindRedeemSendMail】当前时间不发送提醒邮件",e);
		}finally{
			 mailConfig.setRunStatus(MailConfigConstant.MAIL_CON_STOP);
			 mailService.mailStatusOperate(mailConfig);
			 logger.info("AllowedBalanceMailServiceImpl类【expireProductSendMail】结束>>>timestamp:"+System.currentTimeMillis());
		}
		 
		
		
	}
	
	
	private  JSONObject  sendBalconsMail(List<Map> balcons,String allowedRedeemDt){
		JSONObject returnJsonObject = new JSONObject();		
		MailMessage mailMessage = new MailMessage();
		MailConfig mailConfig = null;
		String[] mailcc = new String[]{};
		String[] mailto = new String[]{};
		String[] mailscc = new String[]{};
		try {
			mailConfig = mailService.queryMailConfig(MailConfigConstant.REMIND_0003);
			if(null == mailConfig){
				logger.info("查询邮件配置结果为空" + new Date());
				returnJsonObject.put("returnCode", "9999");
			    return returnJsonObject;
			}
			
		    MailConfPath mailPath = mailService.queryMailPath(MailConfigConstant.REMIND_0003);	
			if(null != mailPath){
			    mailcc = StringUtils.isBlank(mailPath.getMailCc())?new String[]{} : mailPath.getMailCc().split(",");
			    mailto = StringUtils.isBlank(mailPath.getMailTo())?new String[]{} : mailPath.getMailTo().split(",");
			    mailscc = StringUtils.isBlank(mailPath.getMailScc())?new String[]{} : mailPath.getMailScc().split(",");	   
			
			}
			
			 
			
			logger.info("查询邮件配置==============" + new Date()+":"+mailConfig.toString());
		} catch (Exception e1) {			
			logger.error("查询邮件配置 Exception.......................", e1);
			returnJsonObject.put("returnCode", "9999");
		    return returnJsonObject;
		}			
		
		String to = null;	
		String content = "<!DOCTYPE html><html><head><meta charset='UTF-8'><meta http-equiv=content-type content='text/html;charset=utf-8'>"
				+"</head><body><table style='width: 600px;border-color: #666666;font-family: verdana,arial,sans-serif;font-size:15px;border-collapse: collapse;text-align: center;cellpadding='0' cellspacing='0'>"
				+"<tr style='background: #d3d3d3;'>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>基金代码</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>基金名称</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>交易账号</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>可赎回日期</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>份额</td></tr>";
		
		        if(null !=balcons && balcons.size() >0){
		        	
		        	for (Iterator iterator = balcons.iterator(); iterator.hasNext();) {
						Map balcon = (Map) iterator.next();						
						content+= "<tr>"
						        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+balcon.get("FUNDID")+"</td>"
						        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+balcon.get("FUNDCHINESENM")+"</td>"
						        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+balcon.get("TRADEACCO")+"</td>"
						        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+allowedRedeemDt+ "("+ DateUtils.dateToWeek(allowedRedeemDt) + ")"+"</td>"
						        + "<td style='height: 40px;border: 1px solid #a5a5a5;text-align: right;'>"+NumberToCN.parseNumber("###,###.00", new BigDecimal(balcon.get("BALANCE")+""))+"</td>"
						        + "</tr>";
						to = (String) balcon.get("EMAIL");
					}
		        }
		        content+= "</table></body></html>";
		        
		String[] tccs = this.queryMailToOrCcInfo(MailConfigConstant.REMIND_0003,"cc");
		String[] tsccs = this.queryMailToOrCcInfo(MailConfigConstant.REMIND_0003,"scc");  
		
		
		String[] ccs = new String[tccs.length + mailcc.length];
		String[] sccs = new String[tsccs.length + mailscc.length];
		
		System.arraycopy(mailcc, 0, ccs, 0, mailcc.length);		
		System.arraycopy(tccs, 0, ccs, mailcc.length, tccs.length);
		
		System.arraycopy(mailscc, 0, sccs, 0, mailscc.length);
		System.arraycopy(tsccs, 0, sccs, mailscc.length, tsccs.length);
		        
		String[] tos =  new String[]{to};
	    mailMessage.setTo(tos);	
	    mailMessage.setCc(ccs);
	    mailMessage.setScc(sccs);
		mailMessage.setContent(MessageFormat.format(mailConfig.getBody(),allowedRedeemDt, DateUtils.dateToWeek(allowedRedeemDt), content));
		mailMessage.setSubject(MessageFormat.format(mailConfig.getHead(),allowedRedeemDt, DateUtils.dateToWeek(allowedRedeemDt))); 
		mailMessage.setConfId(MailConfigConstant.REMIND_0003);
		mailMessage.setPrefix("custom1");
		
		try {			
			MailSender.getMailSender().add(mailMessage);
			returnJsonObject.put("returnCode", "0000");	
		} catch (Exception e) {
			logger.error("发送邮件异常："+e.getMessage());
			returnJsonObject.put("returnCode", "9999");	
		}		
		return returnJsonObject;
		   
	}
	
	
	 private boolean checkMailConfigStatus(String mailConfigId) throws Exception {
			MailConfig	mailConfig = mailService.queryMailConfig(mailConfigId);
			if(null == mailConfig  ){	
				 logger.info("AllowedBalanceMailServiceImpl类【checkMailConfigStatus】找不到郵件配置，mailConfigid:" + mailConfigId);
				return  false;
			}
			
			if(MailConfigConstant.MAIL_CON_RUN.equals(mailConfig.getRunStatus()) ){
				 logger.info("AllowedBalanceMailServiceImpl类【checkMailConfigStatus】邮件发送执行中。。。。。，mailConfigid:" + mailConfigId);
				return false;
			}		
			return true;
		}
	 
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
