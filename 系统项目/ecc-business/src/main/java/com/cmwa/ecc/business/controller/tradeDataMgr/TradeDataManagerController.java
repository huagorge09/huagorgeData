package com.cmwa.ecc.business.controller.tradeDataMgr;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ecc.business.entity.dsTrade.DsTradeDataDto;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.service.common.CommonService;
import com.cmwa.ecc.business.service.dsTrade.DsTradeService;
import com.cmwa.ecc.business.service.query.ExportDataService;
import com.cmwa.ecc.business.utils.DateUtils;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

@Controller
@RequestMapping("service/tradeDataManager")
public class TradeDataManagerController {
	
	@Autowired
	private DsTradeService dsTradeService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ExportDataService exportDataService;
	
	@RequestMapping(value = "/tradeDataMgr.do",method=RequestMethod.GET)
	public String goCustomerDataManager(ModelMap map) {
		Calendar cal = Calendar.getInstance();
		
		Date date = cal.getTime();
		String endDate  = DateUtils.formatDate(date,"yyyy-MM-dd");
		
		cal.add(Calendar.MONTH, -1);
		date = cal.getTime();
		String startDate = DateUtils.formatDate(date,"yyyy-MM-dd");
		
		
		map.put("startDate",startDate);
		map.put("endDate",endDate);
		return "jsp/tradeDataMgr/tradeDataManager";
	}
	
	@RequestMapping(value = "/queryDsTradeInfo.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<DsTradeDataDto> getRiskLevelInfoListPage(SearchParam sp) throws Exception{
		return dsTradeService.queryDsTradeDataListPage(sp);
	}
	
	@RequestMapping(value = "/queryContractParameter.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public List<ParameterVo> getTradeDataInfoList(SearchParam sp) throws Exception{
		return commonService.getParameterListPage(sp);
	}
	
	@RequestMapping(value ="/exportTradeData.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	public void exportTradeData(SearchParam sp,HttpServletResponse response) throws Exception {
		exportDataService.exportTradeData(sp, response);
	}
	
	@RequestMapping(value="/openEditDialog.xhtml",method={RequestMethod.GET})
	public ModelAndView openDialog(HttpServletRequest request){
		return dsTradeService.openDialog(request);
	}
	
	@RequestMapping(value="/modifyTradeData.xhtml",method={RequestMethod.POST})
	@ResponseBody
	public JSONObject modifyTradeData(HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		String serialno = request.getParameter("serialno");
		String contractsign = request.getParameter("contractsign");
		String isoriginal =  request.getParameter("isoriginal");
		String filed =  request.getParameter("filed");
		String remark = request.getParameter("remark");
		String istradeform = request.getParameter("istradeform");
		String contractdevolve =request.getParameter("contractdevolve");
		DsTradeDataDto dto  = new DsTradeDataDto();
		dto.setSerialno(serialno);
		dto.setContractsign(contractsign);
		dto.setContractdevolve(contractdevolve);
		dto.setIsoriginal(isoriginal);
		dto.setIstradeform(istradeform);
		dto.setFiled(filed);
		dto.setRemark(remark);
		try {
			dsTradeService.updateTradeDate(dto);
			jsonObject.put("ResultCode", "0000");
			jsonObject.put("ResultMsg", "修改成功");
			return jsonObject;
		} catch (Exception e) {
			jsonObject.put("ResultCode", "9999");
			jsonObject.put("ResultMsg", "修改失败");
			return jsonObject;
		}
	}
}	
