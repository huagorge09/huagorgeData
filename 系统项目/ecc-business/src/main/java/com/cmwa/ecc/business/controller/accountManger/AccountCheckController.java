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
import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.ParameterDto;
import com.cmwa.ecc.business.entity.accountManger.ContactInfoDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.TradeDto;
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

/**
 * 账户复核控制器
 * @author ex-chenbq
 *
 */
@Controller("accountCheckController")
@RequestMapping(value = "/service/accountCheckManager")
public class AccountCheckController {

	@Autowired
	private WidgetService widgetService;
	
	@Autowired
	private AccountCheckService accountCheckService;
	
	@Autowired
	private ModifyCustInfoService custInfoService;
	
	@Autowired
	private BankInfoService bankInfoService;
	
	@Autowired
	private OpenAccountService accountService;
	
	/**
	 * 打开账户复核列表页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/accountCheckListView")
	public ModelAndView accountCheckListView(ModelAndView model) throws IOException {
		//业务类型
		SearchParam idtpSp = new SearchParam();
		idtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		idtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_APKIND);
		idtpSp.getSp().put("pmv2", "A");
		List<Map<String, Object>> apkindArray = widgetService.queryMatchApkindList(idtpSp);
		model.addObject("apkindArray", apkindArray);
		
		//复核状态
		List<Map<String, Object>> chkflgArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_DSCHKFLAG);
		model.addObject("chkflgArray", chkflgArray);
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8009");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/checkAccountInfoList");
		return model;
	}
	
	
	/**
	 * 账户复核列表查询
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/accountCheckQuery")
	@ResponseBody
	public Page<TradeDto> accountCheckQuery(SearchParam param) throws IOException {
		return accountCheckService.accountCheckQry(param);
	}

	/**
	 * 获取账户复核记录数
	 * @param param
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/getTradeCount")
	@ResponseBody
	public TradeDto getTradeCount(){
		return accountCheckService.getTradeCount();
	}
	
	/**
	 * 打开账户复核页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/accountCheckView")
	public ModelAndView accountCheckView(ModelAndView model,@RequestParam("serialno")String serialno) throws IOException {
		model.addObject("serialno", serialno);
		
		//国籍代码
		List<Map<String, Object>> nationArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION);
		model.addObject("nationArray", nationArray);
		
		OpenAccountDto openAccountDto = new OpenAccountDto();
		JSONArray jsonArray = new JSONArray();
		
		SearchParam param = new SearchParam();
		param.getSp().put("serialno", serialno);
		model.addObject("serialno", serialno);
		openAccountDto = accountCheckService.accountCheckDetailQry(param);
		
		openAccountDto.setFileno(accountCheckService.getOpenFileNo(openAccountDto.getCustno()));
		
		openAccountDto.setDsapkindnm(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_APKIND, openAccountDto.getDsapkind()));
		
		//证件类型
		openAccountDto.setIdtpnm(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP,openAccountDto.getIdtp()));//客户证件类型
		openAccountDto.setPrincipalidtp(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP,openAccountDto.getPrincipalidtp()));//机构负责人证件类型
		openAccountDto.setContidtp(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP,openAccountDto.getContidtp()));//经办人证件类型
		openAccountDto.setInstrepidtp(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP,openAccountDto.getInstrepidtp()));//法人证件类型
		openAccountDto.setHoldingidtp(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP,openAccountDto.getHoldingidtp()));//控股股东证件类型
		openAccountDto.setInvprtpnm("0".equals(openAccountDto.getInvprtp())?"专业投资者":"普通投资者");
		
		openAccountDto.setVoccode(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_VOCCODE,openAccountDto.getVoccode()));
		
		openAccountDto.setCustrisklevlnm(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_CUSTRISKLEVEL, openAccountDto.getCustrisklevl()));
		
		openAccountDto.setFaxdelegate(openAccountDto.getFaxdelegate()!=null && openAccountDto.getFaxdelegate().equals("1") ? "是" : "否");
		
		openAccountDto.setSex(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_SEX, openAccountDto.getSex()));
		
		
		openAccountDto.setEdlevel(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_EDLEVEL, openAccountDto.getEdlevel()));
		
		openAccountDto.setIncome(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_ANNUALINCOME, openAccountDto.getIncome()));
		
		//国籍
		openAccountDto.setNationalitycode(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION,openAccountDto.getNationalitycode()));//客户国籍
		openAccountDto.setContactnation(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION,openAccountDto.getContactnation()));//经办人国籍
		openAccountDto.setPrincipalnation(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION,openAccountDto.getPrincipalnation()));//机构负责人国籍
		openAccountDto.setInstrepnation(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION,openAccountDto.getInstrepnation()));//法人国籍
		
		
		//银行卡类型 
		SearchParam bankSp = new SearchParam();
		List<BankBnkbaseVo> bankArray = bankInfoService.queryDsBankBase(bankSp);
		//开户银行
		if(bankArray != null && bankArray.size() > 0){
			for(int i=0;i<bankArray.size();i++){
				if(openAccountDto.getBnkNo()!=null && openAccountDto.getBnkNo().equals(bankArray.get(i).getBnkNo())){
					openAccountDto.setBnkNo(bankArray.get(i).getBnkNm());
					break;
				}
			}
		}
		
		openAccountDto.setContactgrant(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BROKERPRIV, openAccountDto.getContactgrant()));
		
		openAccountDto.setAmlrisktype(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_FUNDRISKLEVEL, openAccountDto.getAmlrisktype()));
		
		openAccountDto.setTaxType(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM,"INVESTTAXTYPE", openAccountDto.getTaxType()));
		openAccountDto.setTaxTypeDecl(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM,"INVESTTAXTYPE", openAccountDto.getTaxTypeDecl()));
		
		
		String pmst="",pmky="";
		if ("0".equals(openAccountDto.getInvestProInstType())){
			pmst = "INVPROINSTTYPE";
			pmky = "FINANCIALINST";
		} else if ("1".equals(openAccountDto.getInvestProInstType())) {
			pmst = "INVPROINSTTYPE";
			pmky = "FINPRODUCT";
		} else if ("2".equals(openAccountDto.getInvestProInstType())) {
			pmst = "INVPROINSTTYPE";
			pmky = "OTHERFUND";
		}
		
		openAccountDto.setInvestProInstType(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM,"INVPROINSTTYPE", openAccountDto.getInvestProInstType()));
		
		openAccountDto.setInvestProInstSecond(queryParameter(pmst, pmky, openAccountDto.getInvestProInstSecond()));
		
		openAccountDto.setOneYearEndNetAsset(queryParameter("OTHERCORPORATE","NEARYEARNETASS", openAccountDto.getOneYearEndNetAsset()));
		
		openAccountDto.setOneYearEndFinAsset(queryParameter("OTHERCORPORATE","NEARYEARFINASS",openAccountDto.getOneYearEndFinAsset()));
		
		openAccountDto.setInvestExperience(queryParameter("OTHERCORPORATE","INVESTEXP", openAccountDto.getInvestExperience()));
		
		openAccountDto.setFinancialAsset(queryParameter("INDINVESTPRO","FINANCIALASSET", openAccountDto.getFinancialAsset()));
		
		openAccountDto.setThreeAnnualIncome(queryParameter("INDINVESTPRO","NEARAVGINCOME", openAccountDto.getThreeAnnualIncome()));
		
		openAccountDto.setIndInvExperience(queryParameter("INDINVESTPRO","INVESTEXP", openAccountDto.getIndInvExperience()));
		
		openAccountDto.setFinProfessions(queryParameter("INDINVESTPRO","FINPROFESSIONS", openAccountDto.getFinProfessions()));
		
		openAccountDto.setRelatedWorkExp(queryParameter("INDINVESTPRO","RELATEDWORKEXP", openAccountDto.getRelatedWorkExp()));
		
		openAccountDto.setRegiontp(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_REGIONCODE, openAccountDto.getRegiontp()));
		
		openAccountDto.setCompanytp(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_COMPANYTP, openAccountDto.getCompanytp()));
		
		openAccountDto.setBusinesstp(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BUSINESSTP, openAccountDto.getBusinesstp()));
		
		openAccountDto.setDocbusinesstpnm(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_DOCBUSINESSTP, openAccountDto.getDocbusinesstp()));
		
		
		openAccountDto.setAcctabbr(queryParameter(ParameterConstant.PARAM_PMST_DSCUSTGROUP,ParameterConstant.PARAM_PMKY_FIRSTGROUP, openAccountDto.getAcctabbr()));
		
		openAccountDto.setInstrepcode(queryParameter(ParameterConstant.PARAM_PMST_DSCUSTGROUP,ParameterConstant.PARAM_PMKY_SECONDGROUP, openAccountDto.getInstrepcode()));
		
		openAccountDto.setOpenAddr(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_DS_PROVINCE, openAccountDto.getOpenAddr()));
		
		openAccountDto.setOpenBankCity(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_DS_CITYCODE, openAccountDto.getOpenBankCity()));
		
		
		openAccountDto.setOrganType(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM,"INSTTYPE", openAccountDto.getOrganType()));
		
		
		List<ContactInfoDto> infoDtos = openAccountDto.getOcontactlist();
		if (null != infoDtos && infoDtos.size() > 0) {
			for (int i = 0; i < infoDtos.size(); i++) {
				ContactInfoDto dto = infoDtos.get(i);
				dto.setContactnation(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION,dto.getContactnation()));//经办人国籍
				dto.setContidtp(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP,dto.getContidtp()));//经办人证件类型
				dto.setContactgrant(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BROKERPRIV, dto.getContactgrant()));
			}
		}
		
		
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(openAccountDto);
		
		model.addObject("dto", jsonObject);
		
		List<UserTaxInfoDto> list = custInfoService.queryUserTaxInfoByList(openAccountDto.getCustno());
		jsonArray.addAll(list);
		
		model.addObject("userTaxArray", jsonArray.toString());
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8009");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/checkAccountDetail");
		return model;
	}
	
	/**
	 * 账户类复核
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/accountInfoCheck",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public OpenAccountDto accountInfoCheck(SearchParam param){
		OpenAccountDto dto = new OpenAccountDto();
		try {
			dto = accountCheckService.accountInfoCheck(param);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg(e.getMessage());
		}
		return dto;
	}
	
	
	/**
	 * 账户驳回修改列表页面
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/accuuntRejectChangeListView")
	public ModelAndView accuuntRejectChangeListView(ModelAndView model) throws IOException {
		//业务类型
		SearchParam idtpSp = new SearchParam();
		idtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		idtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_APKIND);
		idtpSp.getSp().put("pmv2", "A");
		List<Map<String, Object>> apkindArray = widgetService.queryMatchApkindList(idtpSp);
		model.addObject("apkindArray", apkindArray);
		
		//复核状态
		List<Map<String, Object>> chkflgArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_DSCHKFLAG);
		model.addObject("chkflgArray", chkflgArray);
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8009");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/accountRejectChangeList");
		return model;
	}
	
	

	/**
	 * 打开驳回修改页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/accountRejectChangeView")
	public ModelAndView accountRejectChangeView(ModelAndView model,@RequestParam("serialno")String serialno) throws IOException {
		model.addObject("serialno", serialno);
		//委托方式
		List<Map<String, Object>> trustTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_TRUSTTP);
		model.addObject("trustTypeArray", trustTypeArray);
		//机构类型
		List<Map<String, Object>> organType = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,"INSTTYPE");
		model.addObject("organType", organType);
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
		
		//证件类别或CMF证件类别
		SearchParam seatidtpSp = new SearchParam();
		seatidtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		seatidtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		seatidtpSp.getSp().put("pmv1", "0");
		List<Map<String, Object>> seatidtpArray = widgetService.queryMatchApkindList(seatidtpSp);
		model.addObject("seatidtpArray", seatidtpArray);
		
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
		model.addObject("fxqArray", fxqArray);
		
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
		List<Map<String, Object>> contprivArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BROKERPRIV);
		model.addObject("contprivArray", contprivArray);
		
		//查询客户分组信息
		List<Map<String, Object>> custFirst = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DSCUSTGROUP,ParameterConstant.PARAM_PMKY_FIRSTGROUP);
		model.addObject("custFirst", custFirst);
		
		//银行卡类型 
		SearchParam bankSp = new SearchParam();
		List<BankBnkbaseVo> bankBaseList = bankInfoService.queryDsBankBase(bankSp);
		model.addObject("bankBaseList", bankBaseList);

		//获取省份集合
		List<Map<String, Object>> provinces = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_DS_PROVINCE);
		model.addObject("provinces", provinces);
		
		//机构类型
		List<Map<String, Object>> investProInstArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,"INVPROINSTTYPE");
		model.addObject("investProInstArray", investProInstArray);
		
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
		
		OpenAccountDto openAccountDto = new OpenAccountDto();
		JSONArray jsonArray = new JSONArray();
		
		SearchParam param = new SearchParam();
		param.getSp().put("serialno", serialno);
		model.addObject("serialno", serialno);
		openAccountDto = accountCheckService.accountCheckDetailQry(param);
		
		openAccountDto.setFileno(accountCheckService.getOpenFileNo(openAccountDto.getCustno()));
		
		openAccountDto.setDsapkindnm(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_APKIND, openAccountDto.getDsapkind()));
		
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(openAccountDto);
		
		model.addObject("dto", jsonObject);
		
		List<UserTaxInfoDto> list = custInfoService.queryUserTaxInfoByList(openAccountDto.getCustno());
		jsonArray.addAll(list);
		
		model.addObject("userTaxArray", jsonArray.toString());
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8009");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/accountRejectChange");
		return model;
	}
	
	

	/**
	 * 账户类驳回修改
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/accountRejectModify",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public OpenAccountDto accountRejectModify(@RequestParam("custInfo")String custInfo){
		OpenAccountDto dto = new OpenAccountDto();
		custInfo = StringEscapeUtils.unescapeHtml4(custInfo);
		try {
			dto = accountCheckService.convertBean(custInfo);
			if (!dto.getErrcode().equals("0000")) {
				return dto;
			}
			dto = accountCheckService.accountRejectModify(dto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("账户类驳回修改异常：" + e);
		}
		return dto;
	}
	
	public String queryParameter(String pmst, String pmky ,String pmco){
		String pmnm = "";
		if(!StringUtil.isEmpty(pmco)){
			ParameterDto parameterDto = widgetService.getParameterByPmstPmkyPmco(pmst, pmky , pmco);
			if(null != parameterDto){
				pmnm = parameterDto.getPmnm();
			}
		}
		return pmnm;
	}
}
