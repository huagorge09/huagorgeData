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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.UserTaxInfoDto;
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.service.accountManger.AccountCheckService;
import com.cmwa.ecc.business.service.accountManger.ModifyCustInfoService;
import com.cmwa.ecc.business.service.accountManger.OpenAccountService;
import com.cmwa.ecc.business.service.bank.BankInfoService;
import com.cmwa.ecc.business.service.widget.WidgetService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

@Controller("ModifyCustInfoController")
@RequestMapping(value = "/service/custInfoManager")
public class ModifyCustInfoController {

	@Autowired
	private WidgetService widgetService;
	
	@Autowired
	private BankInfoService bankInfoService;
	
	@Autowired
	private OpenAccountService accountService;
	
	@Autowired
	private ModifyCustInfoService custInfoService;
	
	@Autowired
	private AccountCheckService accountCheckService;
	
	/**
	 * 打开客户基本信息列表查询页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/openBaseInfoView")
	public ModelAndView openCustInfoView(ModelAndView model) throws IOException {
		//查询客户分组信息
		List<Map<String, Object>> custFirst = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DSCUSTGROUP,ParameterConstant.PARAM_PMKY_FIRSTGROUP);
		model.addObject("custFirst", custFirst);
		model.addObject("type", "baseInfo");
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8001");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/custAccountInfoList");
		return model;
	}
	
	/**
	 * 打开客户信息列表查询页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/openCustBankInfoView")
	public ModelAndView openCustBankInfoView(ModelAndView model) throws IOException {
		//查询客户分组信息
		List<Map<String, Object>> custFirst = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DSCUSTGROUP,ParameterConstant.PARAM_PMKY_FIRSTGROUP);
		model.addObject("custFirst", custFirst);
		model.addObject("type", "bankInfo");
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8001");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/custAccountInfoList");
		return model;
	}
	
	/**
	 * 打开客户信息列表查询页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/openCustCategoryView")
	public ModelAndView openCustCategoryView(ModelAndView model) throws IOException {
		//查询客户分组信息
		List<Map<String, Object>> custFirst = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DSCUSTGROUP,ParameterConstant.PARAM_PMKY_FIRSTGROUP);
		model.addObject("custFirst", custFirst);
		model.addObject("type", "categoryInfo");
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8001");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/custAccountInfoList");
		return model;
	}
	
	/**
	 * 打开客户信息列表查询页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/openSyntheSizeInfoView")
	public ModelAndView openSyntheSizeInfoView(ModelAndView model) throws IOException {
		//查询客户分组信息
		List<Map<String, Object>> custFirst = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DSCUSTGROUP,ParameterConstant.PARAM_PMKY_FIRSTGROUP);
		model.addObject("custFirst", custFirst);
		model.addObject("type", "synthesizeInfo");
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8001");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/custAccountInfoList");
		return model;
	}
	
	/**
	 * 账户类修改查询
	 * @return
	 */
	@RequestMapping(value = "/accountModifyQuery",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public Page<OpenAccountDto> accountModifyQuery(SearchParam param){
		return custInfoService.accountModifyQuery(param);
	}
	

	/**
	 * 打开客户基本信息修改页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/modifyCustBaseInfoView")
	public ModelAndView modifyCustBaseInfoView(ModelAndView model,@RequestParam("custno")String custno,@RequestParam("invtp")String invtp) throws IOException {
		
		//委托方式
		List<Map<String, Object>> trustTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_TRUSTTP);
		model.addObject("trustTypeArray", trustTypeArray);
		
		//机构类型
		List<Map<String, Object>> organType = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,"INSTTYPE");
		model.addObject("organType", organType);
		
		//银行卡类型 
		SearchParam bankSp = new SearchParam();
		List<BankBnkbaseVo> bankArray = bankInfoService.queryDsBankBase(bankSp);
		model.addObject("bankArray", bankArray);
		
		//证件类型
		SearchParam idtpSp = new SearchParam();
		idtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		idtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		idtpSp.getSp().put("pmv1", "1");
		List<Map<String, Object>> idtpArray = widgetService.queryMatchApkindList(idtpSp);
		model.addObject("idtpArray", idtpArray);
		
		//证件类别或CMF证件类别
		SearchParam seatidtpSp = new SearchParam();
		seatidtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		seatidtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		seatidtpSp.getSp().put("pmv1", "0");
		List<Map<String, Object>> seatidtpArray = widgetService.queryMatchApkindList(seatidtpSp);
		model.addObject("seatidtpArray", seatidtpArray);
		
		//国籍代码
		List<Map<String, Object>> nationArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION);
		model.addObject("nationArray", nationArray);
		
		//风险等级
		List<Map<String, Object>> riskLevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_FUNDRISKLEVEL);
		model.addObject("riskLevelArray", riskLevelArray);
		
		// 投资者类别
		List<Map<String, Object>> invtpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_INVTP);
		model.addObject("invtpArray", invtpArray);
		
		//基金风险等级
		List<Map<String, Object>> docbusitpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_DOCBUSINESSTP);
		model.addObject("docbusitpArray", docbusitpArray);
		
		//性别
		List<Map<String, Object>> sexArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_SEX);
		model.addObject("sexArray", sexArray);
		
		//学历代码
		List<Map<String, Object>> edlevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_EDLEVEL);
		model.addObject("edlevelArray", edlevelArray);
		
		// 职业代码
		List<Map<String, Object>> vacodeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_VOCCODE);
		model.addObject("vacodeArray", vacodeArray);
		
		//年收入
		List<Map<String, Object>> incomeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_ANNUALINCOME);
		model.addObject("incomeArray", incomeArray);
		
		//客户风险承受能力
		List<Map<String, Object>> custrisklevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_CUSTRISKLEVEL);
		model.addObject("custrisklevelArray", custrisklevelArray);
		
		//经办人授权范围
		List<Map<String, Object>> bokerprivArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BROKERPRIV);
		model.addObject("bokerprivArray", bokerprivArray);
		
		model.addObject("custno", custno);
		model.addObject("invtp", invtp);
		
		String[] custnos = custno.split(",");
		
		OpenAccountDto openAccountDto = new OpenAccountDto();
		
		if (null != custnos && custnos.length > 0) {
			SearchParam param = new SearchParam();
			param .getSp().put("custno", custnos[0]);
			param.getSp().put("fundacco", "");
			param.getSp().put("tradeacco", "");
			openAccountDto = accountService.tradeAccoQuery(param);
		}
		openAccountDto.setFileno(accountCheckService.getCustFileInfo(openAccountDto.getCustno()));
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(openAccountDto);
		
		model.addObject("dto", jsonObject);
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8006");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/modifyBaseInfoDetail");
		return model;
	}

	/**
	 * 个人账户/机构资料修改
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping(value = "/modifyAccount",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public OpenAccountDto modifyAccount(@RequestParam("baseInfo")String baseInfo){
		OpenAccountDto dto = new OpenAccountDto();
		baseInfo = StringEscapeUtils.unescapeHtml4(baseInfo);
		try {
			dto = accountService.custModifyToBean(baseInfo);
			if (!dto.getErrcode().equals("0000")) {
				return dto;
			}
			dto = custInfoService.modifyAccount(dto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg(e.getMessage());
		}
		return dto;
	}
	

	/**
	 * 打开客户银行信息修改页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/modifyCustBankInfoView")
	public ModelAndView modifyCustBankInfoView(ModelAndView model,@RequestParam("custno")String custno,@RequestParam("tradeAcco")String tradeAcco,@RequestParam("invtp")String invtp) throws IOException {
		
		//委托方式
		List<Map<String, Object>> trustTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_TRUSTTP);
		model.addObject("trustTypeArray", trustTypeArray);
		
		//机构类型
		List<Map<String, Object>> organType = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,"INSTTYPE");
		model.addObject("organType", organType);

		//银行卡类型 
		SearchParam bankSp = new SearchParam();
		List<BankBnkbaseVo> bankArray = bankInfoService.queryDsBankBase(bankSp);
		model.addObject("bankArray", bankArray);
		
		//证件类型
		SearchParam idtpSp = new SearchParam();
		idtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		idtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		idtpSp.getSp().put("pmv1", "1");
		List<Map<String, Object>> idtpArray = widgetService.queryMatchApkindList(idtpSp);
		model.addObject("idtpArray", idtpArray);
		
		//证件类别或CMF证件类别
		SearchParam seatidtpSp = new SearchParam();
		seatidtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		seatidtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		seatidtpSp.getSp().put("pmv1", "0");
		List<Map<String, Object>> seatidtpArray = widgetService.queryMatchApkindList(seatidtpSp);
		model.addObject("seatidtpArray", seatidtpArray);
		
		//国籍代码
		List<Map<String, Object>> nationArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION);
		model.addObject("nationArray", nationArray);
		

		//获取省份集合
		List<Map<String, Object>> provinces = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_DS_PROVINCE);
		model.addObject("provinces", provinces);
		
		// 投资者类别
		List<Map<String, Object>> invtpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_INVTP);
		model.addObject("invtpArray", invtpArray);
		
		//基金风险等级
		List<Map<String, Object>> docbusitpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_DOCBUSINESSTP);
		model.addObject("docbusitpArray", docbusitpArray);
		
		model.addObject("custno", custno);
		model.addObject("tradeAcco", tradeAcco);
		model.addObject("invtp", invtp);
		
		String[] custnos = custno.split(",");
		
		OpenAccountDto openAccountDto = new OpenAccountDto();
		String isMultiple = "0";
		
		if (null != custnos && custnos.length > 0) {
			SearchParam param = new SearchParam();
			param .getSp().put("custno", custnos[0]);
			param.getSp().put("fundacco", "");
			param.getSp().put("tradeacco", tradeAcco);
			openAccountDto = accountService.tradeAccoQuery(param);
		}
		if (null != custnos && custnos.length > 1) {
			isMultiple = "1";
		}
		openAccountDto.setFileno(accountCheckService.getCustFileInfo(openAccountDto.getCustno()));
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(openAccountDto);
		
		model.addObject("isMultiple", isMultiple);
		model.addObject("dto", jsonObject);
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8007");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/modifyBankInfoDetail");
		return model;
	}
	
	/**
	 * 银行资料修改
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping(value = "/modifyBankInfo",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public OpenAccountDto modifyBankInfo(@RequestParam("baseInfo")String baseInfo){
		OpenAccountDto dto = new OpenAccountDto();
		baseInfo = StringEscapeUtils.unescapeHtml4(baseInfo);
		try {
			JSONObject jsonObject = null;
			try {
				jsonObject = JSONObject.parseObject(baseInfo);
			} catch (Exception e) {
				e.printStackTrace();
				dto.setErrcode("9999");
				dto.setErrmsg("客户资料修改数据信息转换异常");
				return dto;
			}
			String tradeaccos = "";
			String openName = "";
			String bankAccoNm = "";
			String openAddr = "";
			String openbankcity = "";
			String bnkNo = "";
			String bankAcco = "";

			String permissionId = "";
			String operatorId = "";
			String trustType = "";

			if (jsonObject != null) {
				tradeaccos = jsonObject.getString("tradeaccos");
				openName = jsonObject.getString("openName");
				bankAccoNm = jsonObject.getString("bankAccoNm");
				openAddr = jsonObject.getString("openAddr");
				openbankcity = jsonObject.getString("openbankcity");
				bnkNo = jsonObject.getString("bnkNo");
				bankAcco = jsonObject.getString("bankAcco");

				permissionId = jsonObject.getString("permissionId");
				operatorId = jsonObject.getString("operatorId");
				trustType = jsonObject.getString("trustType");
			}

			OpenAccountDto accoDto = new OpenAccountDto();
			if (tradeaccos != null && tradeaccos.length() > 0) {
				accoDto.setOpenName(openName);
				accoDto.setBankAccoNm(bankAccoNm);
				accoDto.setOpenAddr(openAddr);
				accoDto.setOpenBankCity(openbankcity);
				accoDto.setBnkNo(bnkNo);
				accoDto.setBankAcco(bankAcco);
				accoDto.setTradeacco(tradeaccos);

				accoDto.setPermissionId(permissionId);
				accoDto.setOperatorId(operatorId);
				accoDto.setTrustType(trustType);
			}

			// 资料信息
			accoDto.setDocbusinesstp(jsonObject.getString("docbusinesstp")); // 资料业务类型
			accoDto.setIfalldocument(jsonObject.getString("isalldoc")); // 资料是否齐全
			accoDto.setIfOriginal(jsonObject.getString("iforiginal")); // 资料是否原件
			accoDto.setIfsaved(jsonObject.getString("ifsaved")); // 资料是否归档
			accoDto.setIsupload(jsonObject.getString("isupload")); // 是否上传附件
			accoDto.setCustdocument(jsonObject.getString("document")); // 资料名称列表
			accoDto.setRemarkinfo(jsonObject.getString("remarkinfo")); // 资料信息备注
			accoDto.setIsscan(jsonObject.getString("isscan"));
			accoDto.setKeepaddress(jsonObject.getString("keepaddress"));
			accoDto.setSalesaccmanager(jsonObject.getString("salesaccmanager"));
			accoDto.setFileno(jsonObject.getString("fileno"));
			accoDto.setFlag("Y");
			dto = custInfoService.modifyBankInfo(accoDto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg(e.getMessage());
		}
		return dto;
	}
	

	
	/**
	 * 打开客户分类信息修改页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/modifyCategoryInfoView")
	public ModelAndView modifyCategoryInfoView(ModelAndView model,@RequestParam("custno")String custno,@RequestParam("invtp")String invtp) throws IOException {
		
		//业务类型
		List<Map<String, Object>> businesstpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BUSINESSTP);
		model.addObject("businesstpArray", businesstpArray);
		
		//公司
		List<Map<String, Object>> cmptpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_COMPANYTP);
		model.addObject("cmptpArray", cmptpArray);
		
		//区域代码
		List<Map<String, Object>> regiontpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_REGIONCODE);
		model.addObject("regiontpArray", regiontpArray);
		
		//基金风险等级
		List<Map<String, Object>> fxqArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_FUNDRISKLEVEL);
		model.addObject("fundrisklevlArray", fxqArray);
		
		//基金风险等级
		List<Map<String, Object>> docbusitpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_DOCBUSINESSTP);
		model.addObject("docbusitpArray", docbusitpArray);
		
		//委托方式
		List<Map<String, Object>> trustTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_TRUSTTP);
		model.addObject("trustTypeArray", trustTypeArray);
		
		//银行卡类型 
		SearchParam bankSp = new SearchParam();
		List<BankBnkbaseVo> bankArray = bankInfoService.queryDsBankBase(bankSp);
		model.addObject("bankArray", bankArray);
		
		//查询客户分组信息
		List<Map<String, Object>> custFirst = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DSCUSTGROUP,ParameterConstant.PARAM_PMKY_FIRSTGROUP);
		model.addObject("custFirst", custFirst);
		
		//证件类型
		SearchParam idtpSp = new SearchParam();
		idtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		idtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		idtpSp.getSp().put("pmv1", "1");
		List<Map<String, Object>> idtpArray = widgetService.queryMatchApkindList(idtpSp);
		model.addObject("idtpArray", idtpArray);
		
		//国籍代码
		List<Map<String, Object>> nationArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION);
		model.addObject("nationArray", nationArray);
		
		//风险等级
		List<Map<String, Object>> riskLevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_FUNDRISKLEVEL);
		model.addObject("riskLevelArray", riskLevelArray);
		
		// 投资者类别
		List<Map<String, Object>> invtpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_INVTP);
		model.addObject("invtpArray", invtpArray);
		
		//性别
		List<Map<String, Object>> sexArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_SEX);
		model.addObject("sexArray", sexArray);
		
		//学历代码
		List<Map<String, Object>> edlevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_EDLEVEL);
		model.addObject("edlevelArray", edlevelArray);
		
		// 职业代码
		List<Map<String, Object>> vacodeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_VOCCODE);
		model.addObject("vacodeArray", vacodeArray);
		
		//年收入
		List<Map<String, Object>> incomeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_ANNUALINCOME);
		model.addObject("incomeArray", incomeArray);
		
		//客户风险承受能力
		List<Map<String, Object>> custrisklevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_CUSTRISKLEVEL);
		model.addObject("custrisklevelArray", custrisklevelArray);
		
		//经办人授权范围
		List<Map<String, Object>> bokerprivArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BROKERPRIV);
		model.addObject("bokerprivArray", bokerprivArray);
		

		//近一年末净资产
		List<Map<String, Object>> oneYearEndNetAssetArray = widgetService.queryParamListByStAndKy("OTHERCORPORATE","NEARYEARNETASS");
		model.addObject("oneYearEndNetAssetArray", oneYearEndNetAssetArray);
		//近一年末金融资产
		List<Map<String, Object>> oneYearEndFinAssetArray = widgetService.queryParamListByStAndKy("OTHERCORPORATE","NEARYEARFINASS");
		model.addObject("oneYearEndFinAssetArray", oneYearEndFinAssetArray);
		//投资经历
		List<Map<String, Object>> investExperienceArray = widgetService.queryParamListByStAndKy("OTHERCORPORATE","INVESTEXP");
		model.addObject("investExperienceArray", investExperienceArray);
		//个人专业投资者
		//金融资产
		List<Map<String, Object>> financialAssetArray = widgetService.queryParamListByStAndKy("INDINVESTPRO","FINANCIALASSET");
		model.addObject("financialAssetArray", financialAssetArray);
		//近三年年均收入
		List<Map<String, Object>> threeAnnualIncomeArray = widgetService.queryParamListByStAndKy("INDINVESTPRO","NEARAVGINCOME");
		model.addObject("threeAnnualIncomeArray", threeAnnualIncomeArray);
		//投资经历
		List<Map<String, Object>> indInvExperienceArray = widgetService.queryParamListByStAndKy("INDINVESTPRO","INVESTEXP");
		model.addObject("indInvExperienceArray", indInvExperienceArray);
		//金融职业
		List<Map<String, Object>> finProfessionsArray = widgetService.queryParamListByStAndKy("INDINVESTPRO","FINPROFESSIONS");
		model.addObject("finProfessionsArray", finProfessionsArray);
		//相关工作经历
		List<Map<String, Object>> relatedWorkExpArray = widgetService.queryParamListByStAndKy("INDINVESTPRO","RELATEDWORKEXP");
		model.addObject("relatedWorkExpArray", relatedWorkExpArray);
		//税收居民身份
		List<Map<String, Object>> taxTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,"INVESTTAXTYPE");
		model.addObject("taxTypeArray", taxTypeArray);
		
		//证件类别或CMF证件类别
		SearchParam seatidtpSp = new SearchParam();
		seatidtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		seatidtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		seatidtpSp.getSp().put("pmv1", "0");
		List<Map<String, Object>> seatidtpArray = widgetService.queryMatchApkindList(seatidtpSp);
		model.addObject("seatidtpArray", seatidtpArray);
		
		//机构类型
		List<Map<String, Object>> investProInstArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,"INVPROINSTTYPE");
		model.addObject("investProInstArray", investProInstArray);
		
		//机构类型
		List<Map<String, Object>> organType = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,"INSTTYPE");
		model.addObject("organType", organType);
		
		model.addObject("invtp", invtp);
		
		String[] custnos = custno.split(",");
		
		OpenAccountDto openAccountDto = new OpenAccountDto();
		JSONArray jsonArray = new JSONArray();
		if (null != custnos && custnos.length > 0) {
			model.addObject("custno", custnos[0]);
			SearchParam param = new SearchParam();
			param .getSp().put("custno", custnos[0]);
			param.getSp().put("fundacco", "");
			param.getSp().put("tradeacco", "");
			openAccountDto = accountService.tradeAccoQuery(param);
			List<UserTaxInfoDto> list = custInfoService.queryUserTaxInfoByList(openAccountDto.getCustno());
			jsonArray.addAll(list);
		}
		openAccountDto.setFileno(accountCheckService.getCustFileInfo(openAccountDto.getCustno()));
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(openAccountDto);
		
		model.addObject("dto", jsonObject);
		model.addObject("userTaxArray", jsonArray.toString());
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8006");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/modifyCategoryInfoDetail");
		return model;
	}
	

	/**
	 * 分类信息修改
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping(value = "/modifyCategoryInfo",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public OpenAccountDto modifyCategoryInfo(@RequestParam("baseInfo")String baseInfo){
		OpenAccountDto dto = new OpenAccountDto();
		baseInfo = StringEscapeUtils.unescapeHtml4(baseInfo);
		try {
			JSONObject jsonObject = null;
			try {
				jsonObject = JSONObject.parseObject(baseInfo);
			} catch (Exception e) {
				e.printStackTrace();
				dto.setErrcode("9999");
				dto.setErrmsg("客户分类信息修改数据转换异常");
				return dto;
			}
			dto = custInfoService.modifyCategoryInfo(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg(e.getMessage());
		}
		return dto;
	}
	

	/**
	 * 打开客户综合资料修改页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/modifySyntheSizeInfoView")
	public ModelAndView modifySyntheSizeInfoView(ModelAndView model,@RequestParam("custno")String custno,@RequestParam("tradeAcco")String tradeAcco,@RequestParam("invtp")String invtp) throws IOException {
		//机构类型
		List<Map<String, Object>> investProInstArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,"INSTTYPE");
		model.addObject("investProInstArray", investProInstArray);
		
		//机构类型
		List<Map<String, Object>> organType = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,"INSTTYPE");
		model.addObject("organType", organType);
		
		//委托方式
		List<Map<String, Object>> trustTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_TRUSTTP);
		model.addObject("trustTypeArray", trustTypeArray);
		
		//银行卡类型 
		SearchParam bankSp = new SearchParam();
		List<BankBnkbaseVo> bankArray = bankInfoService.queryDsBankBase(bankSp);
		model.addObject("bankArray", bankArray);
		
		//证件类型
		SearchParam idtpSp = new SearchParam();
		idtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		idtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		idtpSp.getSp().put("pmv1", "1");
		List<Map<String, Object>> idtpArray = widgetService.queryMatchApkindList(idtpSp);
		model.addObject("idtpArray", idtpArray);
		
		//证件类别或CMF证件类别
		SearchParam seatidtpSp = new SearchParam();
		seatidtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		seatidtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		seatidtpSp.getSp().put("pmv1", "0");
		List<Map<String, Object>> seatidtpArray = widgetService.queryMatchApkindList(seatidtpSp);
		model.addObject("seatidtpArray", seatidtpArray);
		
		//国籍代码
		List<Map<String, Object>> nationArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION);
		model.addObject("nationArray", nationArray);
		
		//风险等级
		List<Map<String, Object>> riskLevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_FUNDRISKLEVEL);
		model.addObject("riskLevelArray", riskLevelArray);
		
		//客户风险承受能力
		List<Map<String, Object>> custrisklevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_CUSTRISKLEVEL);
		model.addObject("custrisklevelArray", custrisklevelArray);
		
		// 投资者类别
		List<Map<String, Object>> invtpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_INVTP);
		model.addObject("invtpArray", invtpArray);
		
		//业务类型
		List<Map<String, Object>> businesstpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BUSINESSTP);
		model.addObject("businesstpArray", businesstpArray);
		
		//基金风险等级
		List<Map<String, Object>> docbusitpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_DOCBUSINESSTP);
		model.addObject("docbusitpArray", docbusitpArray);
		
		//性别
		List<Map<String, Object>> sexArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_SEX);
		model.addObject("sexArray", sexArray);
		
		//学历代码
		List<Map<String, Object>> edlevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_EDLEVEL);
		model.addObject("edlevelArray", edlevelArray);
		
		// 职业代码
		List<Map<String, Object>> vacodeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_VOCCODE);
		model.addObject("vacodeArray", vacodeArray);
		
		//年收入
		List<Map<String, Object>> incomeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_ANNUALINCOME);
		model.addObject("incomeArray", incomeArray);
		
		//经办人授权范围
		List<Map<String, Object>> bokerprivArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BROKERPRIV);
		model.addObject("bokerprivArray", bokerprivArray);
		
		//查询客户分组信息
		List<Map<String, Object>> custFirst = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DSCUSTGROUP,ParameterConstant.PARAM_PMKY_FIRSTGROUP);
		model.addObject("custFirst", custFirst);
		
		//基金风险等级
		List<Map<String, Object>> fxqArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_FUNDRISKLEVEL);
		model.addObject("fundrisklevlArray", fxqArray);
		
		//获取省份集合
		List<Map<String, Object>> provinces = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_DS_PROVINCE);
		model.addObject("provinces", provinces);
		
		//公司
		List<Map<String, Object>> cmptpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_COMPANYTP);
		model.addObject("cmptpArray", cmptpArray);
		
		//区域代码
		List<Map<String, Object>> regiontpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_REGIONCODE);
		model.addObject("regiontpArray", regiontpArray);
		
		model.addObject("custno", custno);
		model.addObject("tradeAcco", tradeAcco);
		model.addObject("invtp", invtp);
		
		String[] custnos = custno.split(",");
		
		String[] tradeAccos = tradeAcco.split(",");
		
		String isMultiple = "0";
		
		if (null != custnos && custnos.length > 1) {
			isMultiple = "1";
		}
		
		model.addObject("isMultiple", isMultiple);
		
		OpenAccountDto openAccountDto = new OpenAccountDto();
		if (null != custnos && custnos.length > 0) {
			SearchParam param = new SearchParam();
			param .getSp().put("custno", custnos[0]);
			param.getSp().put("fundacco", "");
			param.getSp().put("tradeacco", tradeAccos.length > 1 ? "" : tradeAcco);
			openAccountDto = accountService.tradeAccoQuery(param);
		}
		openAccountDto.setFileno(accountCheckService.getCustFileInfo(openAccountDto.getCustno()));
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(openAccountDto);
		
		model.addObject("dto", jsonObject);
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8006");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/modifySyntheSizeInfoDetail");
		return model;
	}
	

	/**
	 * 客户综合资料修改
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping(value = "/modifySyntheSizeInfo",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public OpenAccountDto modifySyntheSizeInfo(@RequestParam("baseInfo")String baseInfo){
		OpenAccountDto dto = new OpenAccountDto();
		baseInfo = StringEscapeUtils.unescapeHtml4(baseInfo);
		try {
			JSONObject jsonObject = null;
			try {
				jsonObject = JSONObject.parseObject(baseInfo);
			} catch (Exception e) {
				e.printStackTrace();
				dto.setErrcode("9999");
				dto.setErrmsg("客户资料信息修改数据转换异常");
				return dto;
			}
			dto = custInfoService.modifySyntheSizeInfo(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg(e.getMessage());
		}
		return dto;
	}
}
