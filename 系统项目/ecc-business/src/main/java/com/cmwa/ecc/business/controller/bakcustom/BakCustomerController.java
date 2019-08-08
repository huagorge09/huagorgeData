package com.cmwa.ecc.business.controller.bakcustom;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ecc.business.entity.accountManger.BakCustDto;
import com.cmwa.ecc.business.service.accountManger.OpenAccountService;
import com.cmwa.ecc.business.service.bakCustomer.BakCustomerService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.PageUtil;
import com.cmwa.ecc.business.utils.SearchParam;

@Controller
@RequestMapping("service/bakCustomerManager")
public class BakCustomerController {
	private Logger logger = Logger.getLogger(BakCustomerController.class.getName());
	
	@Autowired
	private OpenAccountService openAccountService;
	
	@Autowired
	private BakCustomerService bakCustomerService;
	
	@RequestMapping(value="/bakCustomerIndex.do")
	public String goBakCustomerView() {
		return "jsp/bakCustomer/bakCustomerIndex";
	}
	@RequestMapping(value="/bakCustomerCheck.do")
	public String goBakCustomerCheckView() {
		return "jsp/bakCustomer/bakCustomerCheck";
	}
	@RequestMapping(value="/bakCustDtoListPage")
	@ResponseBody
	public Page<BakCustDto> bakCustDtoListPage(SearchParam sp) {
		List<BakCustDto> bakCustDtoListPage = new ArrayList<BakCustDto>();
		PageUtil<BakCustDto> page = new PageUtil<BakCustDto>();
		List<BakCustDto> resultList = new ArrayList<BakCustDto>();
		try {
			bakCustDtoListPage = openAccountService.queryBakCust(sp);
			//list = (List<BakCustDto>) sp.getSp().get("tradeInfoList");
			resultList = page.getPageList(sp, bakCustDtoListPage);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("备案客户分页信息取值异常：",e);
		}
		return Page.create(resultList,  sp.getStart(), sp.getLimit(), bakCustDtoListPage.size());
	}
	
	@RequestMapping(value="/bakCustDtoCheckListPage")
	@ResponseBody
	public Page<BakCustDto> bakCustDtoCheckListPage(SearchParam sp) {
		List<BakCustDto> bakCustDtoCheckListPage = new ArrayList<BakCustDto>();
		PageUtil<BakCustDto> page = new PageUtil<BakCustDto>();
		List<BakCustDto> resultList = new ArrayList<BakCustDto>();
		try {
			bakCustDtoCheckListPage = bakCustomerService.checkBakCustomerListPage(sp);
			//list = (List<BakCustDto>) sp.getSp().get("tradeInfoList");
			resultList = page.getPageList(sp, bakCustDtoCheckListPage);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("备案客户复核分页信息取值异常：",e);
		}
		return Page.create(resultList,  sp.getStart(), sp.getLimit(), bakCustDtoCheckListPage.size());
	}
	
	@RequestMapping(value="/openDialog.xhtml",method={RequestMethod.GET})
	public ModelAndView openDialog(HttpServletRequest request){
		return bakCustomerService.openDialog(request);
	}
	
	@RequestMapping(value="/addBakCustomer.xhtml",method=RequestMethod.POST)
	@ResponseBody
	public BakCustDto addBakCustomer(@RequestParam("baseInfo") String baseInfo) {
		BakCustDto dto = new BakCustDto();
		baseInfo = StringEscapeUtils.unescapeHtml4(baseInfo);
	    try {
	    	
			dto = bakCustomerService.convertBean(baseInfo);
			if (!dto.getErrcode().equals("0000")) {
				return dto;
			}
			dto = bakCustomerService.addBakCustomer(dto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg(e.getMessage());
		}
		return dto;
	}
	
	@RequestMapping(value="/delBakCustomer.xhtml",method= {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public BakCustDto delBakCustomer(@RequestParam("baseInfo") String baseInfo) {
		BakCustDto dto = new BakCustDto();
		baseInfo = StringEscapeUtils.unescapeHtml4(baseInfo);
		try {
			
			dto = bakCustomerService.convertBean(baseInfo);
			if (!"0000".equals(dto.getErrcode())) {
				return dto;
			}
			dto = bakCustomerService.delBakCustomer(dto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg(e.getMessage());
		}
		return dto;
	}
	@RequestMapping(value="/updateBakCustomer.xhtml",method= {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public BakCustDto updateBakCustomer(@RequestParam("baseInfo") String baseInfo) {
		BakCustDto dto = new BakCustDto();
		baseInfo = StringEscapeUtils.unescapeHtml4(baseInfo);
	    try {
	    	
			dto = bakCustomerService.convertBean(baseInfo);
			if (!"0000".equals(dto.getErrcode())) {
				return dto;
			}
			dto = bakCustomerService.updateBakCustomer(dto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg(e.getMessage());
		}
		return dto;
	}
	
	@RequestMapping(value="/checkBakCustomer.xhtml",method= {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public BakCustDto checkBakCustomer(SearchParam sp) {
		BakCustDto dto = new BakCustDto();
	    try {
			dto = bakCustomerService.checkBakCustomer(sp);
		} catch (Exception e) {
			dto.setErrcode("9999");
			dto.setErrmsg("复核异常！");
		}
		return dto;
	}
	
}
