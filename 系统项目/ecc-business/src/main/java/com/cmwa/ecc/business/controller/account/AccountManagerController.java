package com.cmwa.ecc.business.controller.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ecc.business.entity.accountManger.ContactDto;
import com.cmwa.ecc.business.entity.accountManger.FundBalanceDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.fundinfo.FundInfoVo;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.entity.workdays.WorkdaysVo;
import com.cmwa.ecc.business.service.account.QueryManager;
import com.cmwa.ecc.business.service.business.EleContractManagerService;
import com.cmwa.ecc.business.service.common.CommonService;
import com.cmwa.ecc.business.service.fundinfo.FundInfoService;
import com.cmwa.ecc.business.service.trade.TradeManagerService;
import com.cmwa.ecc.business.service.workdays.WorkdaysService;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 交易管理
 * @author 
 *
 */
@Controller
@RequestMapping(value="/capitalService/server")
public class AccountManagerController {
	
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
	
	@Autowired
	private WorkdaysService  workdaysService;
	
	/**
	 * 加载页面所需数据
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/loadAccountData.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> loadData() throws IOException{
		//委托方式
//		JSONObject jsonObject = new JSONObject();
		Map<String, Object> result = new HashMap<String, Object>();
		List<ParameterVo> trustTypeArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_ACCPTMD);
		result.put("trustTypeArray", trustTypeArray);
		//客户类型
		List<ParameterVo> invtpArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_INVTP);
		result.put("invtpArray", invtpArray);
		//证件类型
		List<ParameterVo> idtpArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_IDTP);
		result.put("idtpArray",idtpArray);
		//合同签署类型
		List<ParameterVo> contractSignArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_TRADESIGN);
		result.put("contractSignArray", contractSignArray);	
		//巨额赎回标志
		List<ParameterVo> largeflagArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_LARGEREDEEMFLAG);
		result.put("largeflagArray", largeflagArray);	
		
		return result;
	}
	
	/**
	 * 取认购并且对应该客户TANO的基金信息
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/getAllSubFunds.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public List<FundInfoVo> getAllSubFunds(@RequestParam("fundst")String fundst, @RequestParam("tano")String tano) throws IOException{
		List<FundInfoVo> list = new ArrayList<FundInfoVo>();
		try {
			list = fundInfoService.getAllSubFundsArray(fundst, tano);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 查询机构折扣率
	 * @param tradeacco
	 * @param fundacco
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/findDiscount.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject findDiscount(@RequestParam("fundid")String fundid, @RequestParam("tradeacco")String tradeacco
									, @RequestParam("fundacco")String fundacco, @RequestParam("subAmt")String subAmt,@RequestParam(value="apkind")String apkind) throws IOException{
		JSONObject jsonObject = new JSONObject();
		try {
			float disCount = queryManager.findDiscount(tradeacco, fundacco, fundid, apkind, subAmt);
			jsonObject.put("disCount", disCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 查询单个基金信息
	 * @param tradeacco
	 * @param fundacco
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/getFundInfo.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public FundInfoVo getFundInfo(@RequestParam("fundId")String fundId) throws IOException{
		FundInfoVo FundInfoVo = new FundInfoVo();
		try {
			FundInfoVo = fundInfoService.getFundInfo(fundId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FundInfoVo;
	}
	
	/**
	 * 根据交易账号或者基金账号查询客户资料
	 * @param tradeacco
	 * @param fundacco
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/tradeaccoQry.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public OpenAccountDto tradeaccoQry(@RequestParam("tradeacco")String tradeacco, @RequestParam("fundacco")String fundacco) throws IOException{
		OpenAccountDto dto = new OpenAccountDto();
		try {
			dto = queryManager.tradeaccoQry(tradeacco, fundacco);
			//返回null 或者 非 成功标识
			if(null == dto ||(null != dto && !"0000".equals(dto.getErrcode()))){
				return dto;
			}
			
			dto.setInvtpnm(dto.getInvtp());
			//投资者类别
			SearchParam invtpSp = new SearchParam();
			invtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
			invtpSp.getSp().put("allPmky", ParameterConstant.PARAM_PMKY_INVTP);
			invtpSp.getSp().put("Qpmco", dto.getInvtp());
			List<ParameterVo> invtpArray = commonService.getParameterListPage(invtpSp);
			dto.setInvtp(invtpArray.get(0).getPmnm());
			//证件类别或CMF证件类别
			SearchParam sp = new SearchParam();
			sp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
			sp.getSp().put("allPmky", ParameterConstant.PARAM_PMKY_IDTP);
			sp.getSp().put("Qpmco", dto.getIdtp());
			List<ParameterVo> idtpArray = commonService.getParameterListPage(sp);
			dto.setIdtp(idtpArray.get(0).getPmnm());
			List<FundBalanceDto> banlanceList = new ArrayList<FundBalanceDto>();
			
			List<FundInfoVo> fundNmArray = fundInfoService.getAllSubFundsArray("", dto.getTano());
			@SuppressWarnings("unchecked")
			List<FundBalanceDto> fundBalanceList = dto.getFundbalanceList();
			for (int i = 0; i < fundBalanceList.size(); i++) {
				FundBalanceDto fundBalanceDto = fundBalanceList.get(i);
				
				String fundid = fundBalanceDto.getFundid();
				String fundst = fundBalanceDto.getFundst();
				
				//正常、暂停申购、权益登记、红利发放可赎回
   				if(ParameterConstant.PM_CO_SYSTEM$FUNDST$0.equals(fundst) || 
        			ParameterConstant.PM_CO_SYSTEM$FUNDST$5.equals(fundst) ||
        			ParameterConstant.PM_CO_SYSTEM$FUNDST$7.equals(fundst) ||
        			ParameterConstant.PM_CO_SYSTEM$FUNDST$8.equals(fundst) ||
        			ParameterConstant.PM_CO_SYSTEM$FUNDST$b.equals(fundst) ){
        			if(fundNmArray != null && fundNmArray.size()>0){
        				for(int f=0;f<fundNmArray.size();f++){
        					FundInfoVo FundInfoVo = fundNmArray.get(f);
        					if(FundInfoVo.getFundId().equals(fundid)){
        						fundBalanceDto.setFundnm(FundInfoVo.getFundShortNm());
        						banlanceList.add(fundBalanceDto);
   							}
   						}
   					}		
   				}
			}
			dto.setFundbalanceList(banlanceList);
			List<ContactDto> list = dto.getContList();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					ContactDto contactDto = list.get(i);
					if(null == contactDto){
						continue;
					}
					SearchParam spParam = new SearchParam();
					spParam.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
					spParam.getSp().put("allPmky", ParameterConstant.PARAM_PMKY_IDTP);
					spParam.getSp().put("Qpmco", contactDto.getContidtp());
					List<ParameterVo> idtpArrays = commonService.getParameterListPage(spParam);
					contactDto.setContidtpName(contactDto.getContidtp());
					contactDto.setContidtp(idtpArrays.get(0).getPmnm());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	/**
	 * 授权校验
	 * @param checkno
	 * @param checkpwd
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/checkPermission.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject checkPermission(@RequestParam("checkno")String checkno,@RequestParam("checkpwd")String checkpwd) throws IOException{
		JSONObject jsonObject = new JSONObject();
		try {
			SearchParam sp = new SearchParam();
			sp.getSp().put("checkno", checkno);
			sp.getSp().put("checkpwd", checkpwd);
			queryManager.checkPermission(sp);
			jsonObject.put("result", sp.getSp().get("result"));
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("result", "9999");
		}
		return jsonObject;
	}
	
	/**
	 * 获取当前基金可用份额
	 * @param tradeacco
	 * @param custno
	 * @param fundacco
	 * @param fundidForQry
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/queryAvailable.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public OpenAccountDto queryAvailable(@RequestParam("tradeacco")String tradeacco, @RequestParam("custno")String custno,@RequestParam("fundacco")String fundacco, @RequestParam("fundidForQry")String fundidForQry) throws IOException{
		OpenAccountDto dto = new OpenAccountDto();
		try {
			dto = queryManager.tradeaccoQry(tradeacco, fundacco);
			String fundidDis = "";
			String tradeaccoDis = "";
			String fundtype = "";
			double avaliableDis = 0.0;
			double available = 0.0;
			@SuppressWarnings("unchecked")
			List<FundBalanceDto> fundBalanceList = dto.getFundbalanceList();
			List<FundBalanceDto> finaceFundList = new ArrayList<FundBalanceDto>();
			if (fundBalanceList != null && fundBalanceList.size()>0) {
				for(int i=0;i<fundBalanceList.size();i++){
					FundBalanceDto fundDto = (FundBalanceDto)fundBalanceList.get(i);
				    fundidDis = fundDto.getFundid();
				    if(fundidDis != null && fundidDis.equals(fundidForQry)){
				    	tradeaccoDis = fundDto.getTradeacco();
					    if(tradeaccoDis == null){
					       tradeaccoDis = "";
					    }
					    avaliableDis = fundDto.getAvailable();

						//added by wangxl 20120921 短期理财处理 start
						FundInfoVo fundDto1 = fundInfoService.getFundInfo(fundidDis);
						if(fundDto1 != null){
							fundtype = fundDto1.getFundType(); //基金类型
						}
						if(fundDto1!=null && "5".equals(fundtype)){
							finaceFundList = fundInfoService.getFinanceBalcons(custno,tradeaccoDis,"",fundidDis,"2");
							dto.setFundbalanceList(finaceFundList);
							dto.setFundType(fundtype);
//							if(finaceFundList != null){
//								for(int j=0; j<finaceFundList.size(); j++){
//									FundBalanceDto finaceBalanceDto = (FundBalanceDto) finaceFundList.get(j);
//									double marketValue = finaceBalanceDto.getAvailable();
//									available += marketValue;
//								}
//								avaliableDis = available;
//							}
						}else{
							dto.setFundbalanceList(finaceFundList);
						}
						//added by wangxl 20120921 短期理财处理 end
						WorkdaysVo  currentWorkday = workdaysService.getWorkDateNow("");
						dto.setCurrentWorkDay(currentWorkday.getWorkdate());
					    available = avaliableDis;
					    dto.setAvailableBalance(available);
					   
				    } else {
				    	fundidDis = "";
				    }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	/**
	 * 根据交易账号或者基金账号查询客户资料【基金转换页面调用使用】
	 * @param tradeacco
	 * @param fundacco
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/tradeaccoQryToConvert.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public OpenAccountDto tradeaccoQryToConvert(@RequestParam("tradeacco")String tradeacco, @RequestParam("fundacco")String fundacco) throws IOException{
		OpenAccountDto dto = new OpenAccountDto();
		try {
			dto = queryManager.tradeaccoQry(tradeacco, fundacco);
			dto.setInvtpnm(dto.getInvtp());
			//证件类型
			SearchParam invtpSp = new SearchParam();
			invtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
			invtpSp.getSp().put("allPmky", ParameterConstant.PARAM_PMKY_INVTP);
			invtpSp.getSp().put("Qpmco", dto.getInvtp());
			List<ParameterVo> invtpArray = commonService.getParameterAllList(invtpSp);
			dto.setInvtp(invtpArray.get(0).getPmnm());
			//证件类型
			SearchParam sp = new SearchParam();
			sp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
			sp.getSp().put("allPmky", ParameterConstant.PARAM_PMKY_IDTP);
			sp.getSp().put("Qpmco", dto.getIdtp());
			List<ParameterVo> idtpArray = commonService.getParameterAllList(sp);
			dto.setIdtp(idtpArray.get(0).getPmnm());
			List<FundBalanceDto> banlanceList = new ArrayList<FundBalanceDto>();
			
			List<FundInfoVo> fundNmArray = fundInfoService.getAllSubFundsArray("", dto.getTano());
			List<ParameterVo> convertFunds =commonService.getParameterAllList(ParameterConstant.PARAM_PMST_SYSTEM, "TRADE036");//取可转换的基金
			List<FundBalanceDto> fundBalanceList = dto.getFundbalanceList();
			for (int i = 0; i < fundBalanceList.size(); i++) {
				FundBalanceDto fundBalanceDto = fundBalanceList.get(i);
				
				String fundid = fundBalanceDto.getFundid();
				String fundst = fundBalanceDto.getFundst();
				
				//正常、暂停申购、权益登记、红利发放可赎回
				if(ParameterConstant.PM_CO_SYSTEM$FUNDST$0.equals(fundst) || 
						ParameterConstant.PM_CO_SYSTEM$FUNDST$5.equals(fundst) ||
						ParameterConstant.PM_CO_SYSTEM$FUNDST$7.equals(fundst) ||
						ParameterConstant.PM_CO_SYSTEM$FUNDST$8.equals(fundst) ||
						ParameterConstant.PM_CO_SYSTEM$FUNDST$b.equals(fundst) ){
					if(fundNmArray != null && fundNmArray.size()>0){
						for(int f=0;f<fundNmArray.size();f++){
							FundInfoVo FundInfoVo = fundNmArray.get(f);
							if(FundInfoVo.getFundId().equals(fundid)){
								if(convertFunds != null && convertFunds.size() > 0){
									for (int j = 0; j < convertFunds.size(); j++) {
										ParameterVo ParameterVo = convertFunds.get(j);
										if(ParameterVo.getPmco().equals(fundid)){
											fundBalanceDto.setFundnm(FundInfoVo.getFundShortNm());
											banlanceList.add(fundBalanceDto);
										}
									}
								}
							}
						}
					}		
				}
			}
			dto.setFundbalanceList(banlanceList);
			List<ContactDto> list = dto.getContList();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					ContactDto contactDto = list.get(i);
					if(null == contactDto){
						continue;
					}
					SearchParam spParam = new SearchParam();
					spParam.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
					spParam.getSp().put("allPmky", ParameterConstant.PARAM_PMKY_IDTP);
					spParam.getSp().put("Qpmco", contactDto.getContidtp());
					List<ParameterVo> idtpArrays = commonService.getParameterAllList(spParam);
					contactDto.setContidtpName(contactDto.getContidtp());
					contactDto.setContidtp(idtpArrays.get(0).getPmnm());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	/**
	 * 获取对方基金名称
	 * @param tradeacco
	 * @param custno
	 * @param fundacco
	 * @param fundidForQry
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/queryOtherFundInfo.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public List<FundInfoVo> queryOtherFundInfo(@RequestParam("fundId")String fundId,@RequestParam("tano")String tano) throws IOException{
		
		List<FundInfoVo> otherFundList = new ArrayList<FundInfoVo>();
		
		List<FundInfoVo> fundNmArray = fundInfoService.getAllSubFundsArray("", tano);
		
		//可转换的基金列表
		List<FundInfoVo> convfunds = new ArrayList<FundInfoVo>();
		List<ParameterVo> ParameterVos = commonService.getParameterAllList(ParameterConstant.PARAM_PMST_SYSTEM, "TRADE036");//取可转换的基金
        if (ParameterVos != null && ParameterVos.size() > 0) {
            for (int i=0; i<ParameterVos.size(); i++ ) {
                FundInfoVo dto = new FundInfoVo();
                ParameterVo ParameterVo = ParameterVos.get(i);
                dto.setFundId(ParameterVo.getPmco());
                dto.setFundNm(ParameterVo.getPmnm());
                if (fundId != null && !fundId.equals(dto.getFundId())) {
                	convfunds.add(dto);
                    dto = null;
				}
            }
        }
        List<ParameterVo> convlimitDtos = commonService.getParameterAllList(ParameterConstant.PARAM_PMST_SYSTEM, "CONVLIMIT");//取可转换的基金
        List<FundInfoVo> convfundsLimit = new ArrayList<FundInfoVo>();//重新生成list
		for (int j = 0; j < convfunds.size(); j++) {
			FundInfoVo dto = convfunds.get(j);
			convfundsLimit.add(dto);
		}
		
		
		//查出不能转转的基金列表 并remove
        if(convfundsLimit != null && convfundsLimit.size()>0){
	        for (int i = 0; i < convlimitDtos.size(); i++) {
	        	ParameterVo ParameterVo = ParameterVos.get(i);
				if(fundId.equals(ParameterVo.getPmv1())){
					for (int j=0;j< convfundsLimit.size(); j++) { //不能用iterator 遍历 会产生并发Exception
						FundInfoVo dto = (FundInfoVo) convfundsLimit.get(j);
						if(dto.getFundId().equals(ParameterVo.getPmv2())){
			        		System.out.println(fundId+"不能转换的的基金ID"+dto.getFundId());
			        		convfundsLimit.remove(dto);
			        	}
					}
				}
			}
        }
        
        if(convfundsLimit != null && convfundsLimit.size() > 0){
        	for (int k = 0; k < convfundsLimit.size(); k++) {
        		FundInfoVo infoDto = convfundsLimit.get(k);
        		FundInfoVo dto = fundInfoService.getFundInfo(infoDto.getFundId());
        		if(dto == null){
        			continue;
        		}
        		infoDto.setFundId(dto.getFundId());
        		infoDto.setFundNm(dto.getFundNm());
        		infoDto.setFundSt(dto.getFundSt());
        		String fundSt = dto.getFundSt();
        		if(fundSt != null && 
						(ParameterConstant.PM_CO_SYSTEM$FUNDST$0.equals(fundSt) || 
								 ParameterConstant.PM_CO_SYSTEM$FUNDST$6.equals(fundSt) ||
								 ParameterConstant.PM_CO_SYSTEM$FUNDST$7.equals(fundSt) || 
								 ParameterConstant.PM_CO_SYSTEM$FUNDST$8.equals(fundSt) ||
								 ParameterConstant.PM_CO_SYSTEM$FUNDST$b.equals(fundSt))){
        			if(fundNmArray != null && fundNmArray.size()>0){//对应TANO的基金
        				for(int f=0;f<fundNmArray.size();f++){
        					FundInfoVo FundInfoVo = fundNmArray.get(f);
        					if(FundInfoVo.getFundId().equals(dto.getFundId())){
        						otherFundList.add(infoDto);
							}
						}
					}
        		}
			}
        }
		return otherFundList;
	}
}