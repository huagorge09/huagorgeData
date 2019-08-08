package com.cmwa.ecc.business.controller.directmanager;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.controller.BaseController;
import com.cmwa.ecc.business.entity.accountManger.ValidateDto;
import com.cmwa.ecc.business.service.accountManger.OpenAccountService;
import com.cmwa.ecc.business.service.directmanager.ValidateService;
import com.cmwa.ecc.business.utils.DateUtils;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ResultConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

/**
 * 直销管理-过期证件管理
 * @author ex-liuy
 *
 */
@Controller
@RequestMapping(value="service/dirctManager/validate")
public class ValidateController extends BaseController{
	@Autowired
	private ValidateService validateService;
	
	@Autowired
	private OpenAccountService openAccountService;
	
	@RequestMapping("/validateQryView.do")
	public String goValidateQryView(ModelMap map,HttpServletRequest request){
		Employee emp = SessionUtils.getEmployee(request);
		map.put("currEmpId", emp.getID());
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		String begindate = DateUtils.formatDate(date,"yyyy-MM-dd");
		
		cal.add(Calendar.MONTH, 1);
		date = cal.getTime();
		String enddate = DateUtils.formatDate(date,"yyyy-MM-dd");
		
		map.put("begindate",begindate);
		map.put("enddate",enddate);
		return "jsp/directManager/validateQry";
	}
	
	@RequestMapping("/queryValidateList.do")
	@ResponseBody
	public Page<ValidateDto> queryValidateList(SearchParam sp){
//		Page<ValidateDto> xx = new Page<ValidateDto>();
		return validateService.queryValidateList(sp);
	}
	
	@RequestMapping("/validateUpdateView.do")
	public String goValidateUpdateView(ModelMap map,HttpServletRequest request){
		SearchParam sp = new SearchParam();
		String operatorType = request.getParameter("operatorType");
		String idtp=request.getParameter("idtp");
		String idno=request.getParameter("idno");
		String begindate=request.getParameter("begindate");
		String enddate=request.getParameter("enddate");
		sp.getSp().put("operatorType", operatorType);
		sp.getSp().put("idtp", idtp);
		sp.getSp().put("idno", idno);
		sp.getSp().put("begindate", begindate);
		sp.getSp().put("enddate", enddate);
		ValidateDto validateDto = validateService.queryValidateDtoToUpdateView(sp);
		if(null == validateDto){
			validateDto = new ValidateDto();
		}
		validateDto.setOperatorType(operatorType);
		validateDto.setBegindate(begindate);
		validateDto.setEnddate(enddate);
		map.put("dto",validateDto);
		return "jsp/directManager/validateUpdate";
	}
	
	/**
	 * 证件有效期修改
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateValidate.do")
	public String updateValidate(ModelMap map,HttpServletRequest request,ValidateDto dto){
		JSONObject result = new JSONObject();
		Employee emp = SessionUtils.getEmployee(request);
		result = validateService.updateValidate(dto);
		if(!ResultConstant.C_RESULT_SUCCESS.equals(result.get(ResultConstant.C_RESULT_CODE))){
			map.put("operatorId", emp.getID());
			map.put("exception", result.get(ResultConstant.C_RESULT_MSG));
			return redirectExecFaild();
		}
		return redirectSuccess();
	}
}
