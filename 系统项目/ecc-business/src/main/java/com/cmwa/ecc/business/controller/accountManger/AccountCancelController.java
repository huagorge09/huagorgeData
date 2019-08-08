package com.cmwa.ecc.business.controller.accountManger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ec.base.util.Sequences;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.entity.accountManger.TradeDto;
import com.cmwa.ecc.business.service.accountManger.AccountCancelService;
import com.cmwa.ecc.business.service.accountManger.AccountCheckService;
import com.cmwa.ecc.business.service.widget.WidgetService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

/**
 * 账户撤单控制器
 * @author ex-chenbq
 *
 */
@Controller("accountCancelController")
@RequestMapping(value = "/service/accountCancelManager")
public class AccountCancelController {

	@Autowired
	private WidgetService widgetService;
	
	@Autowired
	private AccountCheckService accountCheckService;
	
	@Autowired
	private AccountCancelService accountCancelService;
	
	/**
	 * 打开账户复核列表页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/accountCancelListView")
	public ModelAndView accountCancelListView(ModelAndView model) throws IOException {
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8011");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/accountCancelList");
		return model;
	}
	
	/**
	 * 账户撤单列表查询
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/accountCancelQuery")
	@ResponseBody
	public Page<TradeDto> accountCancelQuery(SearchParam param) throws IOException {
		return accountCheckService.accountCheckQry(param);
	}
	

	/**
	 * 打开账户复核列表页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/accountCancelView")
	public ModelAndView accountCancelView(ModelAndView model,@RequestParam("serialno")String serialno,@RequestParam("operatorId")String operatorId) throws IOException {
		Employee employee = SessionUtils.getEmployee();
		
		//委托方式
		List<Map<String, Object>> trustTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_TRUSTTP);
		model.addObject("trustTypeArray", trustTypeArray);
		
		SearchParam param = new SearchParam();
		param.getSp().put("custno", "");
		param.getSp().put("operatorId", operatorId);
		param.getSp().put("operatorType", "");
		param.getSp().put("tradeAcco", "");
		param.getSp().put("fundAcco", "");
		param.getSp().put("appType", "");
		param.getSp().put("checkStatus", "");
		param.getSp().put("type", "2");
		param.getSp().put("serialno", serialno);
		
		Page<TradeDto> pageList = new Page<TradeDto>();
		TradeDto tradeDto = new TradeDto();
		pageList = accountCheckService.accountCheckQry(param);
		if (null != pageList) {
			tradeDto = pageList.getItems().get(0);
		}
		
		model.addObject("serialno", serialno);
		model.addObject("permissionId", "8011");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.addObject("dto", tradeDto);
		model.setViewName("jsp/accountManger/custAccountMgr/accountCancelDetail");
		return model;
	}
	

	/**
	 * 撤销账户申请
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/accountCancel",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public TradeDto accountCancel(@RequestParam("tradeInfo") String tradeInfo){
		TradeDto dto = new TradeDto();
		tradeInfo = StringEscapeUtils.unescapeHtml4(tradeInfo);
		JSONObject jsonObject = JSONObject.parseObject(tradeInfo);
		//初始化参数   
		if (jsonObject != null) {
			String oserialno = jsonObject.getString("oserialno");
			String tradeacco = jsonObject.getString("tradeacco");
			String trustType = jsonObject.getString("trustType");
			String grantid = jsonObject.getString("grantid");
			String permissionId = jsonObject.getString("permissionId");
			String operatorId = jsonObject.getString("operatorId");
			// 主要业务
			dto.setSerialno(Sequences.getPK());
			dto.setOldserialno(oserialno);
			dto.setTradeacco(tradeacco);
			dto.setTrustType(trustType);
			dto.setCheckno(grantid);
			dto.setPermissionId(permissionId);
			dto.setOperatorId(operatorId);
		}
		return accountCancelService.accountCancel(dto);
	}
}
