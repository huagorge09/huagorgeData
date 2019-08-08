package com.cmwa.ecc.business.controller.instfeemana;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.controller.BaseController;
import com.cmwa.ecc.business.entity.institution.InstitutionVo;
import com.cmwa.ecc.business.entity.ratediscmgr.BankDiscountVo;
import com.cmwa.ecc.business.entity.ratediscmgr.InstDiscounVo;
import com.cmwa.ecc.business.service.instfeemana.InstFeeSetService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ResultConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

/**
 * 财富柜台费率设置 前后端 接口
 * @author ex-liuy
 *
 */
@Controller
@RequestMapping("service/instFeeSet")
public class InstFeeSetController extends BaseController{
	@Autowired
	private InstFeeSetService instFeeSetService ;
	
	@RequestMapping("/instFeeSetIndexView.do")
	public String goInstFeeSetIndexView(ModelMap map,HttpServletRequest request){
		Employee emp = SessionUtils.getEmployee(request);
		map.put("currEmpId", emp.getID());
		return "jsp/businessMgr/instFeeMana/InstFeeSetIndex";
	}
	
	@RequestMapping("/instFeeSetAddView.do")
	public String goInstFeeSetAddView(){
		return "jsp/businessMgr/instFeeMana/instFeeSetAdd";
	}
	
	@RequestMapping("/instFeeSetDetailView.do")
	public String goInstFeeSetDetailView(ModelMap map,HttpServletRequest request,@RequestParam("serialNo")String serialNo){
		Employee emp = SessionUtils.getEmployee(request);
		SearchParam sp = new SearchParam();
		sp.getSp().put("serialNo", serialNo);
		List<InstDiscounVo> instDiscounVos = instFeeSetService.queryInstDiscountAllList(sp);
		if(instDiscounVos.size() > 0){
			map.put("instFeeVo", instDiscounVos.get(0));
		}else{
			map.put("operatorId", emp.getID());
			map.put("exception", "参数错误");
			return redirectExecFaild();
		}
		return "jsp/businessMgr/instFeeMana/instFeeSetDetail";
	}
	
	@RequestMapping("/instFeeSetUpdateView.do")
	public String goInstFeeSetUpdateView(ModelMap map,HttpServletRequest request,@RequestParam("serialNo")String serialNo){
		Employee emp = SessionUtils.getEmployee(request);
		SearchParam sp = new SearchParam();
		sp.getSp().put("serialNo", serialNo);
		List<InstDiscounVo> instDiscounVos = instFeeSetService.queryInstDiscountAllList(sp);
		if(instDiscounVos.size() > 0){
			map.put("instFeeVo", instDiscounVos.get(0));
		}else{
			map.put("operatorId", emp.getID());
			map.put("exception", "参数错误");
			return redirectExecFaild();
		}
		return "jsp/businessMgr/instFeeMana/instFeeSetUpdate";
	}
	
	@RequestMapping("/instFeeSetCheckView.do")
	public String goInstFeeSetCheckView(ModelMap map,HttpServletRequest request,@RequestParam("serialNo")String serialNo){
		Employee emp = SessionUtils.getEmployee(request);
		map.put("currEmpId", emp.getID());
		SearchParam sp = new SearchParam();
		sp.getSp().put("serialNo", serialNo);
		List<InstDiscounVo> instDiscounVos = instFeeSetService.queryInstDiscountAllList(sp);
		if(instDiscounVos.size() > 0){
			map.put("instFeeVo", instDiscounVos.get(0));
		}else{
			map.put("operatorId", emp.getID());
			map.put("exception", "参数错误");
			return redirectExecFaild();
		}
		return "jsp/businessMgr/instFeeMana/instFeeSetCheck";
	}
	
	
	/**
	 * 费率折扣管理 列表
	 * @param sp
	 * @return
	 */
	@RequestMapping("/instFeeSetListPage.do")
	@ResponseBody
	public Page<InstDiscounVo> instFeeSetListPage(SearchParam sp){
		return instFeeSetService.queryInstDiscountListPage(sp);
	}
	
	/**
	 * 新增 柜台费率折扣
	 * @param bankDiscountVo
	 * @return
	 */
	@RequestMapping("/addInstFeeSetInfo.do")
	public String addInstFeeSetInfo(InstDiscounVo instDiscounVo,HttpServletRequest request,ModelMap map){
		Employee emp =  SessionUtils.getEmployee(request);
		instDiscounVo.setCreateId(emp.getID());
		instDiscounVo.setModifyId(emp.getID());
		JSONObject result = instFeeSetService.addInstFeeSetInfo(instDiscounVo);
		if(!ResultConstant.C_RESULT_SUCCESS.equals(result.get(ResultConstant.C_RESULT_CODE))){
			map.put("operatorId", emp.getID());
			map.put("exception", result.get(ResultConstant.C_RESULT_MSG));
			return redirectExecFaild();
		}
		return redirectSuccess();
	}
	
	/**
	 * 复核 柜台费率设置
	 * @param 
	 */
	@RequestMapping("/checkInstFeeSetInfo.do")
	public String checkInstFeeSetInfo(InstDiscounVo instDiscounVo,HttpServletRequest request,ModelMap map){
		Employee emp =  SessionUtils.getEmployee(request);
		instDiscounVo.setCheckId((emp.getID()));
		JSONObject result = instFeeSetService.checkInstFeeSetInfo(instDiscounVo);
		if(!ResultConstant.C_RESULT_SUCCESS.equals(result.get(ResultConstant.C_RESULT_CODE))){
			map.put("operatorId", emp.getID());
			map.put("exception", result.get(ResultConstant.C_RESULT_MSG));
			return redirectExecFaild();
		}
		return redirectSuccess();
	};
	
	/**
	 * 修改 柜台费率设置
	 * @param 
	 */
	@RequestMapping("/updateInstFeeSetInfo.do")
	public String updateInstFeeSetInfo(InstDiscounVo instDiscounVo,HttpServletRequest request,ModelMap map){
		Employee emp =  SessionUtils.getEmployee(request);
		instDiscounVo.setModifyId(emp.getID());
		JSONObject result = instFeeSetService.updateInstFeeSetInfo(instDiscounVo);
		if(!ResultConstant.C_RESULT_SUCCESS.equals(result.get(ResultConstant.C_RESULT_CODE))){
			map.put("operatorId", emp.getID());
			map.put("exception", result.get(ResultConstant.C_RESULT_MSG));
			return redirectExecFaild();
		}
		return redirectSuccess();
	};
	
	/**
	 * 删除 柜台费率设置
	 * @param 
	 */
	@RequestMapping("/delInstFeeSetInfo.do")
	@ResponseBody
	public JSONObject delInstFeeSetInfo(InstDiscounVo instDiscounVo,HttpServletRequest request,ModelMap map){
		Employee emp =  SessionUtils.getEmployee(request);
		instDiscounVo.setModifyId(emp.getID());
		JSONObject result = instFeeSetService.delInstFeeSetInfo(instDiscounVo);
		return result;
	};
}
