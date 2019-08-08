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
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.service.bank.BankBaseInfoManagerService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

/**
 * 银行管理 - 基本信息管理接口
 * @author ex-huangbl
 *
 */

@Controller
@RequestMapping(value = "service/bankManager" )
public class BaseInfoManagerController {
	
	private static Logger logger = Logger.getLogger(BaseInfoManagerController.class);	
	
	@Autowired
	private BankBaseInfoManagerService bankBaseService;
	
	@RequestMapping(value = "/baseInfoMgr.do", method=RequestMethod.GET)
	public String goBaseInfoView() {
		return "jsp/bankMgr/bnkBaseIndex";
	}
	
	@RequestMapping(value = "/baseinfo.xhtml" ,method={RequestMethod.GET})
	@ResponseBody
	public Page<BankBnkbaseVo> queryBankBaseInfoList(SearchParam sp){
		return bankBaseService.queryBankBaseInfoListPage(sp);
	}
	
	@RequestMapping(value="/del.xhtml",method={RequestMethod.POST})
	@ResponseBody
	public void delBankBase(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		JSONObject result=bankBaseService.delBankBase(request);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(result.toString());
	}
	
	
	@RequestMapping(value="/openDialog.xhtml",method={RequestMethod.GET})
	public ModelAndView openDialog(HttpServletRequest request){
		return bankBaseService.openDialog(request);
	}
	
	
	@RequestMapping(value="/add.xhtml",method={RequestMethod.POST})
	public void addBankBaseInfo(BankBnkbaseVo bankBaseInfo,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String opertor=	SessionUtils.getEmployee().getID();
		bankBaseInfo.setCMan(opertor);
		JSONObject result=bankBaseService.addBankBaseInfo(bankBaseInfo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(result.toString());
	}
	@RequestMapping(value="/mod.xhtml",method={RequestMethod.POST})
	public void modBankBaseInfo(BankBnkbaseVo bankBaseInfo,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String opertor=SessionUtils.getEmployee().getID();
		bankBaseInfo.setEMan(opertor);
		JSONObject result=bankBaseService.modBankBaseInfo(bankBaseInfo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(result.toString());
	}
}