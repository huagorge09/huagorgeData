package com.cmwa.ecc.business.controller.ratediscmgr;

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
import com.cmwa.ecc.business.entity.ratediscmgr.BankDiscountVo;
import com.cmwa.ecc.business.service.ratediscmgr.RateDiscountService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ResultConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

/**
 * 直销柜台-业务管理-费率折扣管理
 * 前端入口
 * @author ex-liuy
 *
 */
@Controller
@RequestMapping("service/rateDiscount")
public class RateDiscountController extends BaseController {
	@Autowired
	private RateDiscountService rateDiscountService;
	
	@RequestMapping("/rateDiscountMgrView.do")
	public String goRateDiscountMgrView(){
		return "jsp/businessMgr/rateDiscount/rateDiscountMgr";
	}
	
	@RequestMapping("/rateDiscountMgrAddView.do")
	public String goRateDiscountMgrAddView(){
		return "jsp/businessMgr/rateDiscount/rateDiscountMgrAdd";
	}
	@RequestMapping("/rateDiscountMgrCopyView.do")
	public String goRateDiscountMgrCopyView(){
		return "jsp/businessMgr/rateDiscount/rateDiscountMgrCopy";
	}
	
	@RequestMapping("/rateDiscountMgrUpdateView.do")
	public String goRateDiscountMgrUpdateView(ModelMap map,@RequestParam("bankNo")String bankNo,
			@RequestParam("productId")String productId,@RequestParam("apKind")String apKind ){
		SearchParam sp = new SearchParam();
		sp.getSp().put("bankNo",bankNo);
		sp.getSp().put("productId",productId);
		sp.getSp().put("apKind",apKind);
		List<BankDiscountVo> bankPage = rateDiscountService.rateDiscountListPage(sp).getItems();
		if(null != bankPage && bankPage.size() > 0){
			BankDiscountVo bankDiscountVo = bankPage.get(0);
			
			map.put("rateVo", bankDiscountVo);
		}
		return "jsp/businessMgr/rateDiscount/rateDiscountMgrUpdate";
	}
	
	
	/**
	 * 费率折扣管理 列表
	 * @param sp
	 * @return
	 */
	@RequestMapping("/rateDiscListPage.do")
	@ResponseBody
	public Page<BankDiscountVo> rateDiscListPage(SearchParam sp){
		return rateDiscountService.rateDiscountListPage(sp);
	}
	
	/**
	 * 删除 费率折扣 逻辑删除 status = D
	 * @param bankDiscountVo
	 * @return
	 */
	@RequestMapping("/delRateDisc.do")
	@ResponseBody
	public JSONObject delRateDiscountByParam(BankDiscountVo bankDiscountVo){
		JSONObject result = new JSONObject();
		result = rateDiscountService.delRateDiscountByParam(bankDiscountVo);
		return result;
	}
	
	/**
	 * 新增 费率折扣
	 * @param bankDiscountVo
	 * @return
	 */
	@RequestMapping("/addRateDisc.do")
	public String addRateDiscount(BankDiscountVo bankDiscountVo,HttpServletRequest request,ModelMap map){
		Employee emp =  SessionUtils.getEmployee(request);
		bankDiscountVo.setCreateId(emp.getID());
		JSONObject result = rateDiscountService.addRateDiscountByParam(bankDiscountVo);
		if(!ResultConstant.C_RESULT_SUCCESS.equals(result.get(ResultConstant.C_RESULT_CODE))){
			map.put("operatorId", emp.getID());
			map.put("exception", result.get(ResultConstant.C_RESULT_MSG));
			return redirectExecFaild();
		}
		return redirectSuccess();
	}
	
	/**
	 * 新增 费率折扣
	 * @param bankDiscountVo
	 * @return
	 */
	@RequestMapping("/updateRateDisc.do")
	public String updateRateDiscount(BankDiscountVo bankDiscountVo,HttpServletRequest request,ModelMap map){
		Employee emp =  SessionUtils.getEmployee(request);
		bankDiscountVo.setModifyId(emp.getID());
		JSONObject result = rateDiscountService.updateRateDiscountByParam(bankDiscountVo);
		if(!ResultConstant.C_RESULT_SUCCESS.equals(result.get(ResultConstant.C_RESULT_CODE))){
			map.put("operatorId", emp.getID());
			map.put("exception", result.get(ResultConstant.C_RESULT_MSG));
			return redirectExecFaild();
		}
		return redirectSuccess();
	}
	
	/**
	 * 新增 费率折扣
	 * @param bankDiscountVo
	 * @return
	 */
	@RequestMapping("/copyRateDisc.do")
	public String copyRateDiscount(HttpServletRequest request,ModelMap map,
			@RequestParam("souProductId")String souProductId,
			@RequestParam("tarProductId")String tarProductId
			){
		Employee emp =  SessionUtils.getEmployee(request);
		SearchParam sp = new SearchParam(); 
		sp.getSp().put("souProductId", souProductId);
		sp.getSp().put("tarProductId", tarProductId);
		sp.getSp().put("createId", emp.getID());
		JSONObject result = rateDiscountService.copyRateDiscountByParam(sp);
		if(!ResultConstant.C_RESULT_SUCCESS.equals(result.get(ResultConstant.C_RESULT_CODE))){
			map.put("operatorId", emp.getID());
			map.put("exception", result.get(ResultConstant.C_RESULT_MSG));
			return redirectExecFaild();
		}
		return redirectSuccess();
	}
}
