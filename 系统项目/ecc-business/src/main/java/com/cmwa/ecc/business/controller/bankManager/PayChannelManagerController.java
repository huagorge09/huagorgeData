package com.cmwa.ecc.business.controller.bankManager;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ecc.business.dao.bank.BankBaseDao;
import com.cmwa.ecc.business.entity.bank.BankPayChannelVo;
import com.cmwa.ecc.business.service.bank.PayChannelManagerService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

@Controller
@RequestMapping(value = "service/payChannelManager" )
public class PayChannelManagerController {
	
	private static Logger logger = Logger.getLogger(PayChannelManagerController.class);	
	
	@Autowired
	private PayChannelManagerService payChannelService;
	
	@Autowired
	private BankBaseDao bankBaseDao;
	
	
	@RequestMapping(value = "/payChannelMgr.do",method=RequestMethod.GET)
	public String goPayChannelView() {
		return "jsp/bankMgr/pcIndex";
	}
	
	@RequestMapping(value = "paychannel.xhtml" ,method={RequestMethod.GET})
	@ResponseBody
	public Page<BankPayChannelVo> queryBankBaseInfoListPage(SearchParam sp){
		return payChannelService.queryPayChannelListPage(sp);
	}
	
	
	@RequestMapping(value="/openDialog.xhtml",method={RequestMethod.GET})
	public ModelAndView openDialog(HttpServletRequest request){
		return payChannelService.openDialog(request);
	}
	
	@RequestMapping(value="/add.xhtml",method={RequestMethod.POST})
	public void addPayChannel(BankPayChannelVo payChannelVo,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String opertor=SessionUtils.getEmployee().getID();
		payChannelVo.setCreateOpid(opertor);
		payChannelVo.setModifyOpid(opertor);
		JSONObject result=payChannelService.addPayChannel(payChannelVo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(result.toString());
	}
	
	
	@RequestMapping(value="/del.xhtml",method={RequestMethod.POST})
	@ResponseBody
	public void delPayChannel(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=payChannelService.delPayChannel(request);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(result.toString());
	}
	
	@RequestMapping(value="/update.xhtml",method={RequestMethod.POST})
	public void updatePayChannel(BankPayChannelVo payChannelVo,HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=payChannelService.updatePayChannel(payChannelVo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(result.toString());
	}
	
}
	