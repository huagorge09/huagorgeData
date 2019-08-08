package com.cmwa.ecc.business.controller.bankManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ecc.business.entity.bank.DsBankBnkbaseVo;
import com.cmwa.ecc.business.service.bank.DsBankBnkBaseService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

@Controller
@RequestMapping("service/dsBankBnkBaseManager")
public class DsBankBnkBaseController {
	
	private static Logger logger = LoggerFactory.getLogger(DsBankBnkBaseController.class);

	@Autowired
	private DsBankBnkBaseService dsBankBnkBaseService;
	
	@RequestMapping(value = "/dsBankBnkBaseMgr.do", method = RequestMethod.GET)
	public String goCapitalManagerView() {
		return "jsp/bankMgr/dsBankBnkbaseIndex";
	}
	
	@RequestMapping(value = "/dsBankBnkBaseInfo.xhtml" , method =RequestMethod.GET)
	@ResponseBody
	public Page<DsBankBnkbaseVo> queryBankBnkbaseListPage(SearchParam sp) throws Exception {
		return dsBankBnkBaseService.queryBankBnkbaseListPage(sp);
	}
	
	@RequestMapping(value="/openDialog.xhtml",method={RequestMethod.GET})
	public ModelAndView openDialog(HttpServletRequest request) throws Exception{
		return dsBankBnkBaseService.openDialog(request);
	}
	
	@RequestMapping(value="/del.xhtml",method={RequestMethod.POST})
	@ResponseBody
	public void deleteDsBankBaseInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject result=dsBankBnkBaseService.deleteDsBankBaseInfo(request);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(result.toString());
	}
	
	@RequestMapping(value="/add.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	public void addDsBankBaseInfo(DsBankBnkbaseVo dsBankBnkbaseVo,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String opertor=	SessionUtils.getEmployee().getID();
		dsBankBnkbaseVo.setCMan(opertor);
		JSONObject result=dsBankBnkBaseService.addDsBankBaseInfo(dsBankBnkbaseVo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(result.toString());
	}
	
	@RequestMapping(value="/mod.xhtml",method={RequestMethod.POST})
	public void updateDsBankBaseInfo(DsBankBnkbaseVo dsBankBnkbaseVo,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String opertor=SessionUtils.getEmployee().getID();
		dsBankBnkbaseVo.setEMan(opertor);
		JSONObject result=dsBankBnkBaseService.updateDsBankBaseInfo(dsBankBnkbaseVo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(result.toString());
	}
}
