package com.cmwa.ecc.business.controller.account;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.base.util.Sequences;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.entity.trade.TradeDto;
import com.cmwa.ecc.business.service.account.QueryManager;
import com.cmwa.ecc.business.service.business.EleContractManagerService;
import com.cmwa.ecc.business.service.common.CommonService;
import com.cmwa.ecc.business.service.fundinfo.FundInfoService;
import com.cmwa.ecc.business.service.trade.TradeManagerService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

/**
 * 设置分红方式控制器
 * @author ex-chenbq
 *
 */
@Controller
@RequestMapping(value="/capitalService/server")
public class MelonManagerController {
		
	@Autowired
	private CommonService commonService;
	@Autowired
	private FundInfoService fundInfoService;
	@Autowired
	private QueryManager queryManager;
	/*@Autowired
	private ECCProductService productService;*/
	@Autowired
	private EleContractManagerService eleService;
	@Autowired
	private TradeManagerService tradeManagerService;
	
	/**
	 * 客户一级分组信息
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/loadCustFirstInfo.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> loadCustFirstInfo(HttpServletRequest request) throws IOException{
		//客户一级分组信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ParameterVo> custFirst = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_DSCUSTGROUP, ParameterConstant.PARAM_PMKY_FIRSTGROUP);
		resultMap.put("custFirst", custFirst);
		//委托方式
		List<ParameterVo> trustTypeArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_ACCPTMD);
		resultMap.put("trustTypeArray", trustTypeArray);
		Employee emp = SessionUtils.getEmployee();
		String operatorId =emp.getID();
		resultMap.put("operatorId", operatorId);
		
		List<ParameterVo> managerArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_MANAGERID);
		resultMap.put("managerArray", managerArray);
		
		return resultMap;
	}
	
	/**
	 * 客户二级分组信息
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/loadSecondCustGroup.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> loadSecondCustGroup(@RequestParam("pmco")String pmco) throws IOException{
		//客户二级分组信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		SearchParam sp = new SearchParam();
		sp.getSp().put("pmst", ParameterConstant.PARAM_PMST_DSCUSTGROUP);
		sp.getSp().put("allPmky", ParameterConstant.PARAM_PMKY_SECONDGROUP);
		sp.getSp().put("pmv1", pmco);
		List<ParameterVo> secondCust = commonService.getParameterListPage(sp);
		resultMap.put("secondCust", secondCust);
		return resultMap;
	}
	
	
	/**
	 * 获取所有分红方式数据
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/melonListQry.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Page<TradeDto> melonListQry(SearchParam sp) throws IOException{
		Page<TradeDto> list = new Page<TradeDto>();
		try {
			list = queryManager.melonListQry(sp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 提交设置分红方式数据
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/setMelonmd.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject setMelonmd(HttpServletRequest request) throws IOException{
		JSONObject jsonObject = new JSONObject();
		try {
			String permissionId = request.getParameter("permissionId");	//页面权限
			Employee emp = SessionUtils.getEmployee();
			String operatorId = emp.getID();
			String trustType = request.getParameter("trustType"); // 委托方式
			String tradeacco = request.getParameter("hidtradeacco"); // 交易账号
			String fundid = request.getParameter("hidfundid"); // 基金代码
			String melonmd = request.getParameter("melonmd");	//分红方式
			String melonmdpercent = request.getParameter("melonmdpercent");	//分红方式
			String checkno = request.getParameter("checkno"); // 主管编号
			String serialNo = Sequences.getPK();// 流水号
			
			TradeDto dto = new TradeDto();
			TradeDto returnDto = new TradeDto();
			
			dto.setOperatorId(operatorId);
			dto.setPermissionId(permissionId);
			dto.setSerialno(serialNo);
			dto.setTrustType(trustType);
			dto.setTradeacco(tradeacco);
			dto.setFundid(fundid);
			dto.setMelonpercent(melonmdpercent);
			dto.setMelonmd(melonmd);
			dto.setCheckno(checkno);
			
			//基金转换
			tradeManagerService.setMelonmd(dto);
			
			returnDto = dto;
			
			if(returnDto != null){
				jsonObject.put("serialno", returnDto.getSerialno());
				jsonObject.put("errCode", returnDto.getErrcode());
				jsonObject.put("errMsg", returnDto.getErrmsg());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errCode", "9999");
			jsonObject.put("errMsg", e.toString());
		}
		return jsonObject;
	}
}