package com.cmwa.ecc.business.controller.mail;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ecc.business.controller.BaseController;
import com.cmwa.ecc.business.entity.mail.MailAppend;
import com.cmwa.ecc.business.entity.mail.MailAppendDetail;
import com.cmwa.ecc.business.entity.mail.MailCategory;
import com.cmwa.ecc.business.entity.mail.MailConfPath;
import com.cmwa.ecc.business.entity.mail.MailConfig;
import com.cmwa.ecc.business.entity.mail.MailMessage;
import com.cmwa.ecc.business.entity.mail.SimpleAttachEntry;
import com.cmwa.ecc.business.mail.send.MailSender;
import com.cmwa.ecc.business.service.mail.MailService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

@Controller("mailManagerController")
@RequestMapping(value="/service/mailManager")
public class MailManagerController extends BaseController{
	/**
	 * 日志打印
	 */
	private static Logger logger = LoggerFactory.getLogger(MailManagerController.class);
	
	@Autowired
	private MailService mailService;
	
	
	/**
	 * 跳转至邮件配置管理页面
	 * @return
	 */
	@RequestMapping(value = "/goMailPage.xhtml")
	public String goMailPage(ModelMap model) {
		return "jsp/mail/mailConfList";
	};
	
	/**
	 * 查询所有邮件信息
	 * @param sp
	 * @return
	 */
	@RequestMapping("/msgConfList.do")
	@ResponseBody
	public Page<MailConfig> msgConfListPage(SearchParam sp){
		Page<MailConfig> msgConfVos = mailService.queryMailConfigListPage(sp);
		return msgConfVos;
	}
	
	/**
	 * 打开修改邮件配置页面
	 * @param msgConfId
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/msgConfUpdateView.xhtml")
	public String msgConfUpdateView(@RequestParam("msgConfId") String msgConfId,ModelMap model) throws Exception{
		MailConfig config = mailService.queryMailConfig(msgConfId);
		List<MailAppendDetail> appendToList = mailService.queryMailAppendList(config.getMcId(), MailCategory.TO.getVal());
		List<MailAppendDetail> appendCcList = mailService.queryMailAppendList(config.getMcId(), MailCategory.CC.getVal());
		List<MailAppendDetail> appendSccList = mailService.queryMailAppendList(config.getMcId(), MailCategory.SCC.getVal());
		String tos="";
		String tosNm = "";
		String ccs="";
		String ccsNm="";
		String sccs="";
		String sccsNm="";
		if (null != appendToList && appendToList.size() !=0) {
			for (int i = 0; i < appendToList.size(); i++) {
				MailAppendDetail detail = appendToList.get(i);
				tos += detail.getVal()+",";
				tosNm += detail.getValNm()+",";
			}
			tos=tos.substring(0, tos.length()-1);
			tosNm=tosNm.substring(0, tosNm.length()-1);
		}
		if (null != appendCcList && appendCcList.size() !=0) {
			for (int i = 0; i < appendCcList.size(); i++) {
				MailAppendDetail detail = appendCcList.get(i);
				ccs += detail.getVal()+",";
				ccsNm += detail.getValNm()+",";
			}
			ccs=ccs.substring(0, ccs.length()-1);
			ccsNm=ccsNm.substring(0, ccsNm.length()-1);
		}
		
		if (null != appendSccList && appendSccList.size() !=0) {
			for (int i = 0; i < appendSccList.size(); i++) {
				MailAppendDetail detail = appendSccList.get(i);
				sccs += detail.getVal()+",";
				sccsNm += detail.getValNm()+",";
			}
			sccs=sccs.substring(0, sccs.length()-1);
			sccsNm=sccsNm.substring(0, sccsNm.length()-1);
		}
		
		MailConfPath confPath =  mailService.queryMailPath(msgConfId);
		String mailCcPath = "";
		String mailSccPath = "";
		if (null != confPath) {
			mailCcPath = confPath.getMailCc();
			mailSccPath = confPath.getMailScc();
		}
		model.addAttribute("ccs1", mailCcPath);
		model.addAttribute("sccs1", mailSccPath);
		model.addAttribute("msgConf", config);
		model.addAttribute("tos", tos);
		model.addAttribute("tosNm", tosNm);
		model.addAttribute("ccs", ccs);
		model.addAttribute("ccsNm", ccsNm);		
		model.addAttribute("sccs", sccs);
		model.addAttribute("sccsNm", sccsNm);
		return "jsp/mail/msgConfUpdate";
	}
	
	/**
	 * 修改邮件配置
	 * @param mailConfig
	 * @param toList
	 * @param ccList
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "msgConfUpdate.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String mailConfigUpd(MailConfig mailConfig) throws Exception{
		MailConfig config = new MailConfig();
		config.setMcId(mailConfig.getMcId());
		config.setHead(mailConfig.getHead());
		config.setBody(StringEscapeUtils.unescapeHtml4(mailConfig.getBody()));
		config.setRemark(mailConfig.getRemark());
		
		List<MailAppend> appendList = new LinkedList<MailAppend>();
		if(StringUtils.isNotBlank(mailConfig.getToList())){
			for(String str : mailConfig.getToList().split(",")){
				MailAppend append = new MailAppend();
				append.setSourceId(str);
				append.setConfigId(mailConfig.getMcId());
				append.setCategory(MailCategory.TO.getVal());
				appendList.add(append);
			}
		}
		if(StringUtils.isNotBlank(mailConfig.getCcList())){
			for(String str : mailConfig.getCcList().split(",")){
				MailAppend append = new MailAppend();
				append.setSourceId(str);
				append.setConfigId(mailConfig.getMcId());
				append.setCategory(MailCategory.CC.getVal());
				appendList.add(append);
			}
		}
		//密送人
		if(StringUtils.isNotBlank(mailConfig.getSccList())){
			for(String str : mailConfig.getSccList().split(",")){
				MailAppend append = new MailAppend();
				append.setSourceId(str);
				append.setConfigId(mailConfig.getMcId());
				append.setCategory(MailCategory.SCC.getVal());
				appendList.add(append);
			}
		}
		
		//更新手动设置邮件址去数据库
		MailConfPath mailConfPath = new MailConfPath();
		mailConfPath.setConfId(mailConfig.getMcId());
		mailConfPath.setMailTo("");
		mailConfPath.setMailCc(mailConfig.getCcListAddr());
		mailConfPath.setMailScc(mailConfig.getSccListAddr());
		try {
			mailService.deleteMailPath(mailConfPath);
			if(!StringUtil.isEmpty(mailConfPath.getMailCc()) || !StringUtil.isEmpty(mailConfPath.getMailScc())){
				mailService.addMailPath(mailConfPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		try {
			mailService.updMailConfig(config, appendList);
			return redirectSuccess();
		} catch (Exception e) {
			logger.error("----MailManagerController-mailService.updMailConfig-Exception:",e);
			return redirectExecFaild();
		}
	}
	
	/**
	 * 打开邮件配置信息查看页面
	 * @param msgConfId
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/msgConfDetailView.xhtml")
	public String msgConfDetailView(@RequestParam("msgConfId") String msgConfId,ModelMap model) throws Exception{
		MailConfig config = mailService.queryMailConfig(msgConfId);
		List<MailAppendDetail> appendToList = mailService.queryMailAppendList(config.getMcId(), MailCategory.TO.getVal());
		List<MailAppendDetail> appendCcList = mailService.queryMailAppendList(config.getMcId(), MailCategory.CC.getVal());
		List<MailAppendDetail> appendSccList = mailService.queryMailAppendList(config.getMcId(), MailCategory.SCC.getVal());
		String tos="";
		String tosNm = "";
		String ccs="";
		String ccsNm="";
		String sccs="";
		String sccsNm="";
		
		if (null != appendToList && appendToList.size() !=0) {
			for (int i = 0; i < appendToList.size(); i++) {
				MailAppendDetail detail = appendToList.get(i);
				tos += detail.getVal()+"；";
				tosNm += detail.getValNm()+"；";
			}
			tos=tos.substring(0, tos.length()-1);
			tosNm=tosNm.substring(0, tosNm.length()-1);
		}
		if (null != appendCcList && appendCcList.size() !=0) {
			for (int i = 0; i < appendCcList.size(); i++) {
				MailAppendDetail detail = appendCcList.get(i);
				ccs += detail.getVal()+"；";
				ccsNm += detail.getValNm()+"；";
			}
			ccs=ccs.substring(0, ccs.length()-1);
			ccsNm=ccsNm.substring(0, ccsNm.length()-1);
		}
		
		
		if (null != appendSccList && appendSccList.size() !=0) {
			for (int i = 0; i < appendSccList.size(); i++) {
				MailAppendDetail detail = appendSccList.get(i);
				sccs += detail.getVal()+"；";
				sccsNm += detail.getValNm()+"；";
			}
			sccs=sccs.substring(0, sccs.length()-1);
			sccsNm=sccsNm.substring(0, sccsNm.length()-1);
		}
		
		MailConfPath confPath =  mailService.queryMailPath(msgConfId);
		String mailCcPath = "";
		String mailSccPath = "";
		if(null != confPath){
			mailCcPath = confPath.getMailCc();
			mailSccPath = confPath.getMailScc();
		}
		model.addAttribute("ccs1", mailCcPath);
		model.addAttribute("sccs1", mailSccPath);
		model.addAttribute("msgConf", config);
		model.addAttribute("tos", tos);
		model.addAttribute("tosNm", tosNm);
		model.addAttribute("ccs", ccs);
		model.addAttribute("ccsNm", ccsNm);
		model.addAttribute("sccs", sccs);
		model.addAttribute("sccsNm", sccsNm);
		return "jsp/mail/msgConfDetail";
	}
	
	/**
	 * 邮件发送测试
	 */
	@RequestMapping(value = "/sendMail.do")
	@ResponseBody
	public String sendMail(){
		MailMessage mailMessage =new  MailMessage();
		/*String[] tos ={"ex-chenbq@cmfchina.com"};
		mailMessage.setTo(tos);
		mailMessage.setCc(tos);*/
		mailMessage.setContent("这是一封测试邮件");
		mailMessage.setSubject("邮件发送测试");
		mailMessage.setConfId("1");
		List<SimpleAttachEntry> attrList = new ArrayList<SimpleAttachEntry>();
		SimpleAttachEntry attachEntry = new SimpleAttachEntry();
		attachEntry.setName("test.png");
		attachEntry.setPath("D:\\test.png");
		attrList.add(attachEntry);
		mailMessage.setAffixList(attrList);
		try {
			MailSender.getMailSender().add("1",mailMessage);
			logger.info("邮件已发送，稍后请查询结果！");
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("邮件发送失败："+e.toString());
			return "error";
		}
	}
	
	@RequestMapping(value = "/mailStatusOperate.do")
	@ResponseBody
	public JSONObject mailStatusOperate(MailConfig mailConfig){
		JSONObject jsonObject =  new JSONObject();
		jsonObject.put("resultCode", "1");
		jsonObject.put("resultMsg", "SUCCESS");
		if(StringUtils.isEmpty(mailConfig.getMcId())){
			jsonObject.put("resultCode", "-1");
			jsonObject.put("resultMsg", "参数错误");
			return jsonObject;
		}
		mailService.mailStatusOperate(mailConfig);
		return jsonObject;
	}
}
