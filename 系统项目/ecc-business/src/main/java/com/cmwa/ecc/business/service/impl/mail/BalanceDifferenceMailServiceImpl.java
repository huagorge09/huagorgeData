package com.cmwa.ecc.business.service.impl.mail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.cmwa.ecc.business.entity.mail.SimpleAttachEntry;
import com.cmwa.ecc.business.entity.workdays.WorkdaysVo;
import com.cmwa.ecc.business.mail.send.MailSender;
import com.cmwa.ecc.business.service.mail.BalanceDifferenceMailService;
import com.cmwa.ecc.business.service.mail.MailService;
import com.cmwa.ecc.business.utils.SearchParam;

@Service
public class BalanceDifferenceMailServiceImpl   implements  BalanceDifferenceMailService{
	private Logger logger = LoggerFactory.getLogger(BalanceDifferenceMailServiceImpl.class);
	
	
	@Autowired
	private FundInfoDao  fundInfoDao;
	@Autowired
	private WorkdaysDao workdaysDao;	
	@Autowired 
	private MailService  mailService;
	@Override
	public void showBalanceDifference() throws Exception {
		logger.info("BalanceDifferenceMailServiceImpl类【showBalanceDifference】开始>>>timestamp:"+System.currentTimeMillis());	
		MailConfig mailConfig = new MailConfig();
		mailConfig.setMcId(MailConfigConstant.REMIND_0005);
		try {				
			//如果邮件是停止状态则不执行发送邮件		
			/*if(!checkMailConfigStatus(MailConfigConstant.REMIND_0005)){			
						return ;
			}		
			mailConfig.setRunStatus(MailConfigConstant.MAIL_CON_RUN);
			mailService.mailStatusOperate(mailConfig);
			Date  sysdate = new Date();
			String sysDateStr = DateUtils.formatDate(sysdate, "yyyyMMdd");
			if(!checkSendTime(sysDateStr)){
			    logger.info("BalanceDifferenceMailServiceImpl类【showBalanceDifference】当前时间不发送提醒邮件,当前时间是：" + DateUtils.formatDate(sysdate));
				return ;
			}			*/	
			List<Map>  result = fundInfoDao.queryBalanceDifference();
			
			//if(null != result &&result.size() > 0){
				MailMessage mailMessage = generateMessage(MailConfigConstant.REMIND_0005);
				if(null ==  mailMessage){
					logger.info("BalanceDifferenceMailServiceImpl类【showBalanceDifference】生成邮件消息失败。。。。");
					mailConfig.setRunStatus(MailConfigConstant.MAIL_CON_STOP);
					mailService.mailStatusOperate(mailConfig);
					return ;
				}
				String content = generateMessageContent(result);
				mailMessage.setContent(MessageFormat.format(mailMessage.getContent(), content));
				String filePath = generateExcell(result);
				if(StringUtils.isNotBlank(filePath)){
					mailMessage.setAffixList(Arrays.asList(new SimpleAttachEntry("直销柜台份额对账.xls", filePath)));
				}
				try {
					MailSender.getMailSender().add(mailMessage);			
				}catch(Exception e){
					logger.error("发送邮件异常："+e.getMessage());
				}
				
			//}
		} catch (Exception e) {
			logger.error("发送邮件异常："+e.getMessage());
		}finally{
			mailConfig.setRunStatus(MailConfigConstant.MAIL_CON_STOP);
			mailService.mailStatusOperate(mailConfig);
			logger.info("BalanceDifferenceMailServiceImpl类【showBalanceDifference】结束>>>timestamp:"+System.currentTimeMillis());
			
		}
		
		
	}
	
private String generateMessageContent(List<Map> result) {
		String msg = "";
		String content = "<!DOCTYPE html><html><head><meta charset='UTF-8'><meta http-equiv=content-type content='text/html;charset=utf-8'>"
				+"</head><body><table style='width: 600px;border-color: #666666;font-family: verdana,arial,sans-serif;font-size:15px;border-collapse: collapse;text-align: center;cellpadding='0' cellspacing='0'>"
				+"<tr style='background: #d3d3d3;'>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>基金账号</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>交易账号</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>基金代码</td>"	
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>直销份额</td>"	
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>TA份额</td></tr>";
				
				if(null != result && result.size() >0){
					
					for (Iterator iterator = result.iterator(); iterator.hasNext();) {
						 Map balanceInfo = (Map) iterator.next();
						 
						 content +=  " <tr>"				
					                + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+ balanceInfo.get("FUNDACCT") +"</td>"
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+ balanceInfo.get("TRADEACCO")+"</td>"
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+ balanceInfo.get("FUNDID")+"</td>"	
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+ balanceInfo.get("SALEBALANCE")+"</td>"		
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;text-align: right;'>"+ balanceInfo.get("TABALANCE")+"</td>"
							        + "</tr> ";
						
					}
				}else{
					msg = "<p style='color:red'>份额比对没有差异</p>";
				}	
				content += "</table>";
				if(StringUtils.isNotBlank(msg)){
					content += msg;
				}				
				content += "</body></html>";
		return content;
	}

	private MailMessage generateMessage(String configId) {		
		MailMessage mailMessage = new MailMessage();
		MailConfig mailConfig = null;
		String[] mailcc = new String[]{};
		String[] mailto = new String[]{};
		String[] mailscc = new String[]{};
		try {
			mailConfig = mailService.queryMailConfig(configId);
			MailConfPath mailPath = mailService.queryMailPath(configId);	
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
		    return null;
		}
		
		String[] tccs = this.queryMailToOrCcInfo(configId,"cc"); 
		String[] ttos = this.queryMailToOrCcInfo(configId,"to");
		String[] tsccs = this.queryMailToOrCcInfo(configId,"scc");  //密送人
		
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
			logger.info("BalanceDifferenceMailServiceImpl类【generateMessage】没有配置收件人信息。。。。。。");
			return null;
		}
		mailMessage.setTo(tos);
		mailMessage.setCc(ccs);
		mailMessage.setScc(sccs);
		mailMessage.setContent(mailConfig.getBody());		
		mailMessage.setSubject(mailConfig.getHead());
		mailMessage.setConfId(configId);
		return    mailMessage;

	}
	
	
	private String generateExcell(List<Map> result) {
		
		        if(result == null || result.size() == 0){
		        	return null;
		        }
		        String filePath = SpringUtil.getProperty("mail.path");
		        if(StringUtils.isBlank(filePath)){
		        	return null;
		        }
		// 第一步，创建一个webbook，对应一个Excel文件  
				@SuppressWarnings("resource")
				HSSFWorkbook wb = new HSSFWorkbook();
				// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
		        HSSFSheet sheet = wb.createSheet("直销柜台份额对账"); 
		        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
		        HSSFRow row = sheet.createRow(0);
		        row.setHeight((short) (20*25));
				String[] titles = { "基金账号", "交易账号" , "基金代码", "直销份额" , "TA份额"};
				HSSFCellStyle titleStyle = wb.createCellStyle();
				titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
				HSSFFont titleFont = wb.createFont(); // 创建字体
				titleFont.setFontName("黑体"); // 黑体
				titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 加粗
				titleStyle.setFont(titleFont);
				for (int i = 0; i < titles.length; i++) {
					sheet.setColumnWidth(i, 20*256);
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(titleStyle);
					cell.setCellValue(new HSSFRichTextString(titles[i]));
				}
				// 行数
				int rowCount = 1;
				// 流水号
				HSSFCellStyle contentStyle = wb.createCellStyle();
				contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
				for (int i = 0; i < result.size(); i++) {
					Map balanceInfo = result.get(i);
					HSSFRow rows = sheet.createRow(rowCount);
					rows.setHeight((short) (20*20));
					for (int j = 0; j < titles.length; j++) {
						sheet.setColumnWidth(j, 20*256);
						HSSFCell cell = rows.createCell(j);
						cell.setCellStyle(contentStyle);
						if (j == 0) {
							cell.setCellValue(balanceInfo.get("FUNDACCT") == null ? "" : balanceInfo.get("FUNDACCT").toString());
						} else if (j == 1) {
							cell.setCellValue(balanceInfo.get("TRADEACCO") == null ? "" : balanceInfo.get("TRADEACCO").toString());
						} else if (j == 2) {
							cell.setCellValue(balanceInfo.get("FUNDID") == null ? "" : balanceInfo.get("FUNDID").toString());
						} else if (j == 3) {
							cell.setCellValue(balanceInfo.get("SALEBALANCE") == null ? 0 : Double.valueOf(balanceInfo.get("SALEBALANCE").toString()));
						} else if (j == 4) {
							cell.setCellValue(balanceInfo.get("TABALANCE") == null ? 0 : Double.valueOf(balanceInfo.get("TABALANCE").toString()));
						}
					}
					rowCount = rowCount + 1;
				}
				rowCount=1;
				
				ByteArrayOutputStream os = new ByteArrayOutputStream();
		        try {
		        	wb.write(os);
		        } catch (IOException e) {
		            logger.error("生成excel文件异常",e);;
		        }

			    byte[] fileStream = os.toByteArray();
			    
			    
			 // 第六步，将文件存到指定位置  
		        try  
		        {  
		        	File tempDir = new File(filePath);
					if (!tempDir.exists()) {// 创建目录
						tempDir.mkdirs();
					}
					SimpleDateFormat adf = new SimpleDateFormat("yyyyMMddHHmmss");
					String date = adf.format(new Date());
					filePath = filePath+date+".xls";
		            FileOutputStream fout = new FileOutputStream(filePath);
		            
		            InputStream inputStream = new ByteArrayInputStream(fileStream);
		            
		            try {
		            	int ch = 0;
		                while ((ch = inputStream.read()) != -1) {
		                	fout.write(ch);
		                }
		            } catch (IOException e1) {
		                e1.printStackTrace();
		            } finally {
		            	fout.close();
		                inputStream.close();
		            }
		        }  
		        catch (Exception e)  
		        {  
		            e.printStackTrace();  
		        }
			    
				return filePath;
	}
	
	
	 private boolean checkMailConfigStatus(String mailConfigId) throws Exception {
			MailConfig	mailConfig = mailService.queryMailConfig(mailConfigId);
			if(null == mailConfig  ){	
				 logger.info("BalanceDifferenceMailServiceImpl类【checkMailConfigStatus】找不到郵件配置，mailConfigid:" + mailConfigId);
				return  false;
			}
			
			if(MailConfigConstant.MAIL_CON_RUN.equals(mailConfig.getRunStatus()) ){
				 logger.info("BalanceDifferenceMailServiceImpl类【checkMailConfigStatus】邮件发送执行中。。。。。，mailConfigid:" + mailConfigId);
				return false;
			}		
			return true;
		}
	 
	 
	 private boolean checkSendTime(String sysDateStr ) throws Exception {		
			//如果当前不是工作日就不发送邮件
			if(!sysDateStr.equals(getLatestWorkDate(sysDateStr))){		 
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
