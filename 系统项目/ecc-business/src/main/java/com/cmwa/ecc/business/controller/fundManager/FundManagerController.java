package com.cmwa.ecc.business.controller.fundManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cmwa.ecc.business.entity.fundinfo.FundManagerVo;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.service.common.CommonService;
import com.cmwa.ecc.business.service.fundinfo.FundInfoManagerService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

@Controller
@RequestMapping("service/fundManager")
public class FundManagerController {

	
	@Autowired
	private FundInfoManagerService fundInfoManagerServiceImpl;
	
	@Autowired
	private CommonService commonServiceImpl;
	
	
	@RequestMapping("/setFundParamView.do")
	public String goSetFundParamView() {
		return "jsp/fundMgr/fundInfoList";
	}
	
	@RequestMapping("/checkFundView.do")
	public String goCheckFundView() {
		return "jsp/fundMgr/fundCheckInfoList";
	}
	
	@RequestMapping(value="/getAllFundInfoListPage.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Page<FundManagerVo> getAllFundInfoListPage(SearchParam sp) throws IOException{
		Page<FundManagerVo> list = new Page<FundManagerVo>();
		try {
			list = fundInfoManagerServiceImpl.getAllFundInfoListPage(sp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取所有未复核基金信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/getAllChkFundInfoListPage.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Page<FundManagerVo> getAllChkFundInfoListPage(SearchParam sp) throws IOException{
		Page<FundManagerVo> list = new Page<FundManagerVo>();
		try {
			list = fundInfoManagerServiceImpl.getAllChkFundInfoListPage(sp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 删除基金信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/deleteFundInfo.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject deleteFundInfo(@Param("fundId")String fundId,HttpServletRequest request) throws IOException{
		HttpSession session = request.getSession();
		String processor = 	SessionUtils.getEmployee().getID();

		JSONObject jsonObject = new JSONObject();
		try {
			fundInfoManagerServiceImpl.delChkFundInfo(fundId, processor);
			jsonObject.put("resultCode", "0000");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resultCode", "9999");
		}
		return jsonObject;
	}
	
	/**
	 * 打开修改基金信息页面
	 * @param fundId
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/addOrUpdateFundPage.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ModelAndView updateFundPage(@Param("fundId")String fundId,@Param("method")String method,@Param("status")String status,HttpServletRequest request,ModelAndView model) throws IOException{
		FundManagerVo fundManagerVo = null;
		if(method.equals("check")){
			fundManagerVo = fundInfoManagerServiceImpl.getChkFundInfo(fundId);
		}else{
			fundManagerVo = fundInfoManagerServiceImpl.getFundInfo(fundId);
		}
		if(fundManagerVo == null){
			fundManagerVo = new FundManagerVo();
		}
		model.addObject("fundManagerVo", fundManagerVo);
		model.addObject("method", method);
		model.addObject("status", status);
		model.setViewName("jsp/fundMgr/fundAddOrUpdate");
		return model;
	}
	
	/**
	 * 加载页面所需数据
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/loadData.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String,Object> loadData() throws IOException{
		Map<String,Object> jsonObject = new HashMap<String,Object>();
		//注册登记人信息
		List<ParameterVo> parmList = commonServiceImpl.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMST_TAINFO);
		jsonObject.put("parmList", parmList);
		//获取货币类型参数
		List<ParameterVo> currencyTypeList = commonServiceImpl.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_CURRENCYTYPE);
		jsonObject.put("currencyTypeList", currencyTypeList);
		//获取投资方向参数
		List<ParameterVo> inverstList = commonServiceImpl.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_INVESTTYPE);
		jsonObject.put("inverstList", inverstList);
		//获取基金类别参数Vo
		List<ParameterVo> fundTpList = commonServiceImpl.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_FUNDTP);
		jsonObject.put("fundTpList", fundTpList);
		//获取基金风险等级参数
		List<ParameterVo> fundRiskLevelList = commonServiceImpl.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_FUNDRISKLEVEL);
		jsonObject.put("fundRiskLevelList", fundRiskLevelList);
		//获取基金状态参数
		List<ParameterVo> fundStList = commonServiceImpl.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_FUNDST);
		jsonObject.put("fundStList", fundStList);
		//获取基金显示类别参数
		List<ParameterVo> fundDispTpList = commonServiceImpl.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_FUNDDISPTP);
		jsonObject.put("fundDispTpList", fundDispTpList);
		//基金净值小数处理方式
		List<ParameterVo> navFracModeList = commonServiceImpl.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_NAVFRACMODE);
		jsonObject.put("navFracModeList", navFracModeList);
		return jsonObject;
	}
	
	/**
	 * 增加或者修改基金信息
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/addOrUpdateFundInfo.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject addOrUpdateFundInfo(HttpServletRequest request) throws IOException{
		HttpSession session = request.getSession();
		JSONObject jsonObject = new JSONObject();
		try {
			String method = request.getParameter("method");//方法
			String CurrentUserId = 	SessionUtils.getEmployee().getID();
			String fundId = request.getParameter("txtBIFundId");//基金代码
			String fundNm = request.getParameter("txtBIFundNm");//基金名称
			String fundShortNm = request.getParameter("txtBIFundShortNm");//基金简称
			String englishNm = request.getParameter("txtBIFundElNm");//英文名称
			String taNo = request.getParameter("txtBITaNO");//注册登记代码
			String managerNm = request.getParameter("txtBIManagerNm");//管理人名称
			String trusteeNm = request.getParameter("txtBITrusteeNm");//托管人名称
			String fundDenomina = request.getParameter("txtBIFundDenomina");//基金面值
			String nav = request.getParameter("txtBINav");//基金单位净值(元)
			String currencyType = request.getParameter("selBICurrencyType");//货币类型
			String inverstDirect = request.getParameter("selBIInverstDirect");//投资方向
			String issueDate = request.getParameter("txtISIssueDate");//基金发行日
			String setUpDate = request.getParameter("txtISSetUpDate");//基金成立日期
			String issuePrice = request.getParameter("txtISIssuePrice");//发行价格
			String manageRates = request.getParameter("txtISManagerRates");//管理费率
			String navFracNum = request.getParameter("txtISNavFracNum");//基金净值小数位数
			String navFracMode = request.getParameter("selISNavFracMode");//基金净值小数处理方式 1-四舍五入,2-舍位处理
			String fundSt = request.getParameter("selISFundSt");//基金状态
			String fundType = request.getParameter("selISFundType");//基金类别
			String fundDispType = request.getParameter("selFundDispTp");//基金显示类别
			String parentFundid = request.getParameter("txtPFundid");//母基金代码
			String cycletp = request.getParameter("selCycletp");//周期类型
			String cyclelen = request.getParameter("txtCyclelen");//周期长度
			String isUnFund = request.getParameter("isUnFund");//参数限制
			if(!"5".equals(fundType)){
				cycletp = "";
				cyclelen = "0";
			}
			String fundRiskLevel = request.getParameter("selISFundRiskLevel");//基金风险等级
			String fundEvalDate = request.getParameter("txtISFundEvalDate");//基金评级日期
			String minSubAmt = request.getParameter("txtLSMinSubAmt");//最低认购金额
			String minPurAmt = request.getParameter("txtLSMinPurAmt");//最低申购金额
			String keepLimit = request.getParameter("txtKeepLimit");//最低持有份额
			String fundSize = request.getParameter("txtFundSize");//产品预订规模
			String ecmaxPurchase = request.getParameter("txtEcmaxPurchasest");//网上交易最高认申购限额
			String indiMaxPurchase = request.getParameter("txtIndiMaxPurchase");//最高认申购份额
			
			String minRedShare = request.getParameter("txtLSMinRedShare");//最低赎回份额
			String minConvShare = request.getParameter("txtLSMinConvShare");//最低转换份额
			String minRegAmt = request.getParameter("txtLSMinRegAmt");//最低定投金额
			String redeemToAcctDays = request.getParameter("txtLSRedeemToAcctDays");//赎回款到账日期
			String redMelonDays = request.getParameter("txtMelonDays");//分红划款日期
			String subDays = request.getParameter("txtSubDays");//认申购扣款划款日期
			String inconvertinbyinst = request.getParameter("txtInconvertinbyinst"); //最低转入限额 add by kouyd 20090828
			String nextIssueDate = request.getParameter("txtNextIssueDate"); //下一开放日 add by liaojj 20140421
			String feature = request.getParameter("txtFeature");//产品特点add by liaojj 20140421
			String productTimeLimit = request.getParameter("txtProductTimeLimit");//产品期限add by wanggang 20140715
			if(nextIssueDate != null && !"".equals(nextIssueDate)) {
				nextIssueDate = nextIssueDate.replaceAll("-", "");//nextIssueDate.substring(0,4) + nextIssueDate.substring(5,7) + nextIssueDate.substring(8,10);
			}
			FundManagerVo dto = new FundManagerVo();
			if(issuePrice != null && !"null".equals(issuePrice) && !"".equals(issuePrice)) {
			   dto.setIssuePrice(Double.parseDouble(issuePrice));
			}
			if(manageRates != null && !"null".equals(manageRates) && !"".equals(manageRates)) {
			   dto.setManagerRates(Double.parseDouble(manageRates));
			}
			if(redeemToAcctDays != null && !"null".equals(redeemToAcctDays) && !"".equals(redeemToAcctDays)) {
			   dto.setRedeemToAcctDays(Integer.parseInt(redeemToAcctDays));
			}
			if(redMelonDays != null && !"null".equals(redMelonDays) && !"".equals(redMelonDays)) {
			   dto.setRedMelonDays(Integer.parseInt(redMelonDays));
			}
			if(subDays != null && !"null".equals(subDays) && !"".equals(subDays)) {
			   dto.setSubDays(Integer.parseInt(subDays));
			}
			if(issueDate != null && !"null".equals(issueDate) && !"".equals(issueDate)){
				issueDate = issueDate.replaceAll("-", "");//issueDate.substring(0,4) + issueDate.substring(5,7) + issueDate.substring(8,10);
			}
			if(setUpDate != null && !"null".equals(setUpDate) && !"".equals(setUpDate)) {
				setUpDate = setUpDate.replaceAll("-", "");//setUpDate.substring(0,4) + setUpDate.substring(5,7) + setUpDate.substring(8,10);
			}
			if(productTimeLimit != null && !"null".equals(productTimeLimit) && !"".equals(productTimeLimit)) {
				dto.setProductTimeLimit(productTimeLimit);
			}
			
		    dto.setIssueDate(issueDate);
		    dto.setSetUpDate(setUpDate);
			dto.setNavFracNum(navFracNum);
			dto.setNavFracMode(navFracMode);
			
			dto.setNextIssueDate(nextIssueDate);
		    dto.setFeature(feature);
	
			if(minSubAmt != null && !"null".equals(minSubAmt) && !"".equals(minSubAmt)) {
				dto.setMinSubAmt(minSubAmt);
			}
			if(minPurAmt != null && !"null".equals(minPurAmt) && !"".equals(minPurAmt)) {
				dto.setMinBidAmt(minPurAmt);
			}
			if(keepLimit != null && !"null".equals(keepLimit) && !"".equals(keepLimit)) {
				dto.setKeepLimit(keepLimit);
			}
			if(fundSize != null && !"null".equals(fundSize) && !"".equals(fundSize)) {
				dto.setFundSize(fundSize);
			}
			if(indiMaxPurchase != null && !"null".equals(indiMaxPurchase) && !"".equals(indiMaxPurchase)) {
				dto.setIndiMaxPurchase(indiMaxPurchase);
			}
			if(ecmaxPurchase != null && !"null".equals(ecmaxPurchase) && !"".equals(ecmaxPurchase)){
		    	dto.setEcmaxPurchase(ecmaxPurchase);
		    }
			
		    if(minRedShare != null && !"null".equals(minRedShare) && !"".equals(minRedShare)) {
				dto.setMinRedAmt(minRedShare);
			}
		    if(minConvShare != null && !"null".equals(minConvShare) && !"".equals(minConvShare)) {
				dto.setMinConvAmt(minConvShare);
			}
		    if(minRegAmt != null && !"null".equals(minRegAmt) && !"".equals(minRegAmt)) {
				dto.setMinRspAmt(minRegAmt);
			}
		    dto.setFundId(fundId);
		    dto.setFundNm(fundNm);
			dto.setFundShortNm(fundShortNm);
		    dto.setCurrencyType(currencyType);
		    dto.setFundSt(fundSt);
		    dto.setTaNo(taNo);
		    dto.setTaNm(taNo);
		    //dto.setNavDate("20080229");
			if(nav != null && !"null".equals(nav) && !"".equals(nav)) {
				dto.setNav(Double.parseDouble(nav));
			}   
		    dto.setFundEnglishNm(englishNm);
		    dto.setInvestType(inverstDirect);
		    dto.setFundType(fundType);
			dto.setFundDispType(fundDispType);
			dto.setParentFundid(parentFundid);
			dto.setCycletp(cycletp);
			dto.setCyclelen(new Integer(cyclelen).intValue());
		    dto.setFundRiskLevel(fundRiskLevel);
		    dto.setIsUnFund(isUnFund);
	
			//基金评级日期
			if(fundEvalDate != null && !"".equals(fundEvalDate)) {
				fundEvalDate = fundEvalDate.replaceAll("-", "");//fundEvalDate.substring(0,4) + fundEvalDate.substring(5,7) + fundEvalDate.substring(8,10);
			}
			dto.setFundEvalDate(fundEvalDate);
			
			if(fundDenomina != null && !"null".equals(fundDenomina) && !"".equals(fundDenomina)) {
				dto.setFundDenomina(Double.parseDouble(fundDenomina));
			}
			//add kouyd by 20090828
			if(inconvertinbyinst != null && !"null".equals(inconvertinbyinst) && !"".equals(inconvertinbyinst)) {
				dto.setInconvertinbyinst(Double.parseDouble(inconvertinbyinst));
			}
			
		    dto.setManagerNm(managerNm);
		    dto.setTrusteeNm(trusteeNm);
		    
			dto.setProcessor(CurrentUserId);
			boolean flag = false;
			if(method.equals("add")){
				flag = fundInfoManagerServiceImpl.addChkFundInfo(dto);
			}else if(method.equals("update")){
				flag = fundInfoManagerServiceImpl.modifyChkFundInfo(dto);
			}
			if(flag){
				jsonObject.put("resultCode", "0000");
			}else{
				jsonObject.put("resultCode", "9999");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resultCode", "9999");
		}
		return jsonObject;
	}
	
	
	
	/**
	 * 验证基金代码是否存在
	 * @param request
	 * @param fundId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/getChkFundInfo.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject getChkFundInfo(HttpServletRequest request,@Param("fundId")String fundId) throws IOException{
		JSONObject jsonObject = new JSONObject();
		try {
			FundManagerVo FundManagerVo = fundInfoManagerServiceImpl.getChkFundInfo(fundId);
			if(FundManagerVo == null){
				jsonObject.put("resultCode", "0000");
			}else{
				jsonObject.put("resultCode", "9999");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resultCode", "9999");
		}
		return jsonObject;
	}
	
	
	/**
	 * 同步成立日期
	 * @param request
	 * @param fundId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/syschonizeSetupDate.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject syschonizeSetupDate(HttpServletRequest request) throws IOException{
		JSONObject jsonObject = new JSONObject();
		try {
			String fundId = request.getParameter("txtBIFundId");//基金代码
			SearchParam sp =  fundInfoManagerServiceImpl.syschonizeSetUpDate(fundId);
			jsonObject.put("resultCode", sp.getSp().get("resultCode"));
			jsonObject.put("resultMessage", sp.getSp().get("resultMessage"));
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resultCode", "9999");
		}
		return jsonObject;
	}
	
	/**
	 * 验证基金代码是否存在
	 * @param request
	 * @param fundId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/checkFundInfo.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject checkFundInfo(HttpServletRequest request,
										  @Param("fundId")String fundId,
										  @Param("status")String status) throws IOException{
		JSONObject jsonObject = new JSONObject();
		HttpSession session = request.getSession();
		String checker = SessionUtils.getEmployee().getID();

		try {
			String[] strs = fundInfoManagerServiceImpl.checkFundInfo(fundId, checker, status);
			jsonObject.put("resultCode", strs[0]);
			jsonObject.put("resultMessage", strs[1]);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resultCode", "9999");
		}
		return jsonObject;
	}
}
