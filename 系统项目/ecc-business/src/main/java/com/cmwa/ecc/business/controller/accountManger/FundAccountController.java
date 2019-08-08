package com.cmwa.ecc.business.controller.accountManger;

import java.io.IOException;
import java.util.ArrayList;
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
import com.cmwa.ecc.business.entity.accountManger.FundAcctDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.TaInfoDto;
import com.cmwa.ecc.business.service.accountManger.OpenAccountService;
import com.cmwa.ecc.business.service.bank.BankInfoService;
import com.cmwa.ecc.business.service.widget.WidgetService;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

/**
 * 增开基金账号
 * @author ex-chenbq
 *
 */
@Controller("addFundAccountController")
@RequestMapping(value = "/service/fundAccountManager")
public class FundAccountController {

	@Autowired
	private WidgetService widgetService;
	
	@Autowired
	private BankInfoService bankInfoService;
	
	@Autowired
	private OpenAccountService accountService;
	
	/**
	 * 打开增开基金账号页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/addFundAccountView")
	public ModelAndView openFundAccountView(ModelAndView model) throws IOException {
		//委托方式
		List<Map<String, Object>> trustTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_TRUSTTP);
		model.addObject("trustTypeArray", trustTypeArray);
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8003");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/fundAccountMgr/addFundAccount");
		return model;
	}
	
	

	/**
	 * 打开基金账号销户页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/deleteFundAccountView")
	public ModelAndView deleteFundAccountView(ModelAndView model) throws IOException {
		//委托方式
		List<Map<String, Object>> trustTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_TRUSTTP);
		model.addObject("trustTypeArray", trustTypeArray);
		//证件类型
		List<Map<String, Object>> idtpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_IDTP);
		model.addObject("idtpArray", idtpArray);
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8005");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/fundAccountMgr/deleteFundAccount");
		return model;
	}
	
	/**
	 * 帐户基本信息查询
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
			
			List<FundAcctDto> flist = openAccountDto.getFundAccoList();
			List<FundAcctDto> flist2 = new ArrayList<FundAcctDto>();
			for (int i = 0; i < flist.size(); i++) {
				FundAcctDto fundDto = (FundAcctDto) flist.get(i);
				List<TaInfoDto> taList = accountService.queryTaInfo(fundDto.getTano());
				if (null != taList && taList.size() > 0) {
					fundDto.setTanm(taList.get(0).getTanm());
				}
				flist2.add(fundDto);
			}
			openAccountDto.setFundAccoList(flist2);
			
			if(!StringUtil.isEmpty(openAccountDto.getIdtp())){
				ParameterDto idtpDto = widgetService.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP, openAccountDto.getIdtp());
				openAccountDto.setIdtp(idtpDto.getPmnm());
			}
			
			if(!StringUtil.isEmpty(openAccountDto.getContidtp())){
				ParameterDto contidtpDto = widgetService.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP, openAccountDto.getContidtp());
				openAccountDto.setContidtp(contidtpDto.getPmnm());
			}
			
			if(!StringUtil.isEmpty(openAccountDto.getPrincipalidtp())){
				ParameterDto principalidtpDto = widgetService.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP, openAccountDto.getPrincipalidtp());
				openAccountDto.setPrincipalidtp(principalidtpDto.getPmnm());
			}
			
			if(!StringUtil.isEmpty(openAccountDto.getInstrepidtp())){
				ParameterDto instrepidtpDto = widgetService.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP, openAccountDto.getInstrepidtp());
				openAccountDto.setInstrepidtp(instrepidtpDto.getPmnm());
			}
			
			if(!StringUtil.isEmpty(openAccountDto.getInstrepnation())){
				ParameterDto instrepnation = widgetService.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION,openAccountDto.getInstrepnation());
				openAccountDto.setInstrepnation(instrepnation.getPmnm());
			}
			
			if(!StringUtil.isEmpty(openAccountDto.getPrincipalnation())){
				ParameterDto principalnation = widgetService.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION,openAccountDto.getPrincipalnation());
				openAccountDto.setPrincipalnation(principalnation.getPmnm());
			}
			
			if(!StringUtil.isEmpty(openAccountDto.getContactnation())){
				ParameterDto contactnation = widgetService.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION,openAccountDto.getContactnation());
				openAccountDto.setContactnation(contactnation.getPmnm());
			}
		    
			//经办人授权范围
			if(!StringUtil.isEmpty(openAccountDto.getContactgrant())){
				ParameterDto contactgrant = widgetService.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BROKERPRIV,openAccountDto.getContactgrant());
				openAccountDto.setContactgrant(contactgrant.getPmnm());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return openAccountDto;
	}
	
	
	/**
	 * 增开基金账号
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping(value = "/addFundAccount",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public OpenAccountDto addFundAccount(@RequestParam("fundInfo")String fundInfo){
		OpenAccountDto dto = new OpenAccountDto();
		fundInfo = StringEscapeUtils.unescapeHtml4(fundInfo);
		try {
			JSONObject jsonObject = JSONObject.parseObject(fundInfo);
			dto.setSerialno(Sequences.getPK());
			dto.setPermissionId(jsonObject.getString("permissionId"));
			dto.setOperatorId(jsonObject.getString("operatorId"));
			dto.setTradeacco(jsonObject.getString("tradeacco"));
			dto.setTano(jsonObject.getString("tano"));
			dto.setTrustType(jsonObject.getString("trustType"));
			dto = accountService.openFundAccount(dto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg(e.getMessage());
		}
		return dto;
	}
	

	/**
	 * 基金账号销户
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping(value = "/deleteFundAccount",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public OpenAccountDto deleteFundAccount(@RequestParam("tradeInfo")String tradeInfo){
		OpenAccountDto dto = new OpenAccountDto();
		tradeInfo = StringEscapeUtils.unescapeHtml4(tradeInfo);
		try {
			JSONObject jsonObject = JSONObject.parseObject(tradeInfo);
			dto.setPermissionId(jsonObject.getString("permissionId"));
			dto.setOperatorId(jsonObject.getString("operatorId"));
			dto.setSerialno(Sequences.getPK());
			dto.setTrustType(jsonObject.getString("trustType"));
			dto.setFundacct(jsonObject.getString("tradeacco"));
			dto = accountService.destroyFundacco(dto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg(e.getMessage());
		}
		return dto;
	}
}
