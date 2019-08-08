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
import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.ParameterDto;
import com.cmwa.ecc.business.entity.accountManger.AccountDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.service.accountManger.OpenAccountService;
import com.cmwa.ecc.business.service.bank.BankInfoService;
import com.cmwa.ecc.business.service.widget.WidgetService;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

/**
 * 增开交易账号
 * @author ex-chenbq
 *
 */
@Controller("addTradeAccountController")
@RequestMapping(value = "/service/tradeAccountManager")
public class TradeAccountController {

	@Autowired
	private WidgetService widgetService;
	
	@Autowired
	private BankInfoService bankInfoService;
	
	@Autowired
	private OpenAccountService accountService;
	
	/**
	 * 打开增开交易账号页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/addTradeAccountView")
	public ModelAndView openAccountView(ModelAndView model) throws IOException {
		//委托方式
		List<Map<String, Object>> trustTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_TRUSTTP);
		model.addObject("trustTypeArray", trustTypeArray);
		//证件类型
		List<Map<String, Object>> idtpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_IDTP);
		model.addObject("idtpArray", idtpArray);
		
		//银行卡类型 
		SearchParam bankSp = new SearchParam();
		List<BankBnkbaseVo> bankBaseList = bankInfoService.queryDsBankBase(bankSp);
		model.addObject("bankArray", bankBaseList);

		//获取省份集合
		List<Map<String, Object>> provinces = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_DS_PROVINCE);
		model.addObject("provinces", provinces);
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8002");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/tradeAccountMgr/addTradeAccount");
		return model;
	}
	
	
	/**
	 * 打开增开ECC交易账号页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/addECCTradeAcctView")
	public ModelAndView addECCTradeAcctView(ModelAndView model) throws IOException {
		//委托方式
		List<Map<String, Object>> trustTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_TRUSTTP);
		model.addObject("trustTypeArray", trustTypeArray);
		//证件类型
		List<Map<String, Object>> idtpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_IDTP);
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
		
		//银行卡类型 
		SearchParam bankSp = new SearchParam();
		List<BankBnkbaseVo> bankBaseList = bankInfoService.queryDsBankBase(bankSp);
		model.addObject("bankArray", bankBaseList);
		
		//获取省份集合
		List<Map<String, Object>> provinces = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_DS_PROVINCE);
		model.addObject("provinces", provinces);
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8002");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/tradeAccountMgr/addEccTradeAccount");
		return model;
	}
	


	/**
	 * 打开交易账号销户页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/deleteTradeAccountView")
	public ModelAndView deleteTradeAccountView(ModelAndView model) throws IOException {
		//委托方式
		List<Map<String, Object>> trustTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_TRUSTTP);
		model.addObject("trustTypeArray", trustTypeArray);
		//证件类型
		List<Map<String, Object>> idtpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_IDTP);
		model.addObject("idtpArray", idtpArray);
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8004");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/tradeAccountMgr/deleteTradeAccount");
		return model;
	}
	
	
	/**
	 * 客户资料信息查询
	 * @param fundAcc
	 * @param tradeAcc
	 * @return
	 */
	@RequestMapping(value = "/tradeAccoQuery",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public OpenAccountDto tradeAccoQuery(@RequestParam("fundAcc")String fundAcc , @RequestParam("tradeAcc")String tradeAcc){
		SearchParam param = new SearchParam();
		OpenAccountDto openAccountDto = new OpenAccountDto();
		try {
			param.getSp().put("custno", "");
			param.getSp().put("fundacco", fundAcc);
			param.getSp().put("tradeacco", tradeAcc);
			openAccountDto = accountService.tradeAccoQuery(param);
			
			if(!StringUtil.isEmpty(openAccountDto.getIdtp())){
				ParameterDto idtpDto = widgetService.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP, openAccountDto.getIdtp());
				openAccountDto.setIdtp(idtpDto.getPmnm());
			}
			
			if(!StringUtil.isEmpty(openAccountDto.getInvtp())){
				ParameterDto invtpDto = widgetService.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_INVTP, openAccountDto.getInvtp());
				openAccountDto.setInvtpnm(invtpDto.getPmnm());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return openAccountDto;
	}
	
	
	/**
	 * 帐户基本信息查询
	 * @param fundAcc
	 * @param tradeAcc
	 * @return
	 */
	@RequestMapping(value = "/getAccountInfo",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public AccountDto getAccountInfo(@RequestParam("idno")String idno , @RequestParam("fundacct")String fundacct){
		SearchParam param = new SearchParam();
		AccountDto accountDto = new AccountDto();
		try {
			param.getSp().put("idno", idno);
			param.getSp().put("fundacct", fundacct);
			accountDto = accountService.getAccountBycustNo(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountDto;
	}
	

	/**
	 * 增开交易账号
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping(value = "/addTradeAccount",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public OpenAccountDto addTradeAccount(@RequestParam("tradeInfo")String tradeInfo){
		OpenAccountDto dto = new OpenAccountDto();
		tradeInfo = StringEscapeUtils.unescapeHtml4(tradeInfo);
		try {
			JSONObject jsonObject = JSONObject.parseObject(tradeInfo);
			dto.setSerialno(Sequences.getPK());
			dto.setTrustType(jsonObject.getString("trustType"));
			dto.setPermissionId(jsonObject.getString("permissionId"));
			dto.setOperatorId(jsonObject.getString("operatorId"));
			dto.setOpenAddr(jsonObject.getString("openAddr"));
			dto.setOpenBankCity(jsonObject.getString("openbankcity"));
			dto.setCustno(jsonObject.getString("custNo"));
			dto.setOpenName(jsonObject.getString("openName"));
			dto.setBankAccoNm(jsonObject.getString("bankAccoNm"));
			dto.setBnkNo(jsonObject.getString("bnkNo"));
			dto.setBankAcco(jsonObject.getString("bankAcco"));
			dto.setTano(jsonObject.getString("tano"));
			dto = accountService.tradeAccountAdd(dto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg(e.getMessage());
		}
		return dto;
	}
	
	/**
	 * 交易账号销户
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping(value = "/deleteTradeAccount",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public OpenAccountDto deleteTradeAccount(@RequestParam("tradeInfo")String tradeInfo){
		OpenAccountDto dto = new OpenAccountDto();
		tradeInfo = StringEscapeUtils.unescapeHtml4(tradeInfo);
		try {
			JSONObject jsonObject = JSONObject.parseObject(tradeInfo);
			dto.setPermissionId(jsonObject.getString("permissionId"));
			dto.setOperatorId(jsonObject.getString("operatorId"));
			dto.setSerialno(Sequences.getPK());
			dto.setTrustType(jsonObject.getString("trustType"));
			dto.setTradeacco(jsonObject.getString("tradeacco"));
			dto.setTano(jsonObject.getString("tano"));
			dto = accountService.destroyTradeacco(dto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg(e.getMessage());
		}
		return dto;
	}
}
