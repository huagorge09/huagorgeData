package com.cmwa.ecc.business.service.impl.fundinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmwa.ecc.business.dao.fundinfo.FundInfoDao;
import com.cmwa.ecc.business.entity.accountManger.FundBalanceDto;
import com.cmwa.ecc.business.entity.fundinfo.FundInfoVo;
import com.cmwa.ecc.business.entity.fundinfo.ProductInfoDto;
import com.cmwa.ecc.business.entity.fundinfo.QuestionDto;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.service.common.CommonService;
import com.cmwa.ecc.business.service.fundinfo.FundInfoService;
import com.cmwa.ecc.business.utils.DateUtils;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 产品-操作接口 实现
 * @author ex-liuy
 *
 */
@Service
public class FundInfoServiceImpl implements FundInfoService {
	private static Logger logger = LoggerFactory.getLogger(FundInfoServiceImpl.class);
	@Autowired
	private FundInfoDao fundInfoDao ;
	
	@Autowired
	private CommonService commonService ;
	
	@Override
	public List<FundInfoVo> queryMatchFundInfoList(SearchParam sp) {
		List<FundInfoVo> result = new ArrayList<FundInfoVo>();
		try{
			result = fundInfoDao.queryMatchFundInfoList(sp);
		}catch(Exception e){
			logger.error("----FundInfoServiceImpl-queryMatchFundInfoList-Exception:"+sp.toString(),e);
		}
		return result;
	}

	@Override
	public List<FundInfoVo> getAllSubFundsArray(String fundst, String tano) {
		List<FundInfoVo> list = new ArrayList<FundInfoVo>();
		if("S".equals(fundst)){
			fundst = "'" + ParameterConstant.PM_CO_SYSTEM$FUNDST$1 + "'";
		}else if("P".equals(fundst)){
			fundst = "'" + ParameterConstant.PM_CO_SYSTEM$FUNDST$0 + "'," +
		            "'" + ParameterConstant.PM_CO_SYSTEM$FUNDST$6 + "'," +
		            "'" + ParameterConstant.PM_CO_SYSTEM$FUNDST$7 + "'," +
		            "'" + ParameterConstant.PM_CO_SYSTEM$FUNDST$8 + "'," +
		            "'" + ParameterConstant.PM_CO_SYSTEM$FUNDST$b + "'" ;
		}
		try {
			list = fundInfoDao.getAllSubFundsArray(fundst, tano);
		} catch (Exception e) {
			logger.error("获取所有基金(包括限制基金)异常",e);
		}
		return list;
	}

	@Override
	public FundInfoVo getFundInfo(String fundId) {
		FundInfoVo fundInfoDto = new FundInfoVo();
		try {
			fundInfoDto = fundInfoDao.getFundInfo(fundId);
		} catch (Exception e) {
			logger.error("获取指定基金代码的基金信息异常",e);
		}
		return fundInfoDto;
	}
	
	@Override
	public List<FundBalanceDto> getFinanceBalance(String custno,
			String tradeAcco, String cycleenddt, String fundId, String quryType) {
		SearchParam sp = new SearchParam();
		sp.getSp().put("custno", custno);
		sp.getSp().put("tradeAcco", tradeAcco);
		sp.getSp().put("cycleenddt", cycleenddt);
		sp.getSp().put("fundId", fundId);
		sp.getSp().put("quryType", quryType);
		fundInfoDao.getFinanceBalance(sp);
		List<FundBalanceDto> list = (List<FundBalanceDto>) sp.getSp().get("fundbalanceList");
		return list;
	}

	@Override
	public List<FundInfoVo> getAllFundInfo() {
		List<FundInfoVo> list = new ArrayList<FundInfoVo>();
		try {
			list = fundInfoDao.getAllFundInfo();
		} catch (Exception e) {
			logger.error("获取所有的基金信息异常",e);
		}
		return list;
	}

	@Override
	public Page<ProductInfoDto> getOrderFundInfoList(SearchParam sp) {
		List<ProductInfoDto> productList = fundInfoDao.getOrderFundList(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(fundInfoDao.getOrderFundListTotal(sp));
		setFundParams(productList, new SearchParam(),null);
		return Page.create(productList, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}
	
	private void setFundParams(List<ProductInfoDto> product, SearchParam sps,List<QuestionDto> questionDto) {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		maps.put("pmky", ParameterConstant.PARAM_PMKY_FUNDRISKLEVEL);
		sps.setSp(maps);
		List<ParameterVo> fundRiskLevelList = commonService.getParameterListPage(sps);
		sps.getSp().put("pmky", "CMWAFUNDTYPE");
		List<ParameterVo> fundtypeList = commonService.getParameterListPage(sps);
		sps.getSp().put("pmky", "FUNDST");
		List<ParameterVo> fundStateList = commonService.getParameterListPage(sps);
		sps.getSp().put("pmst", ParameterConstant.PARAM_PMST_DS);
		sps.getSp().put("pmky", ParameterConstant.PARAM_PMKY_DSCHKFLAG);
		List<ParameterVo> checkStateList = commonService.getParameterListPage(sps);

		for (ProductInfoDto productInfoDto : product) {
			
			productInfoDto.setPrimaryKey(productInfoDto.getFuncode()+"/"+productInfoDto.getPeriod());
			for (ParameterVo ParameterVo : fundRiskLevelList) {
				if (ParameterVo.getPmco().equals(
						productInfoDto.getFundriskLevel())) {
					productInfoDto.setFundriskLevelPmnm(ParameterVo.getPmnm());
				}
			}
			for (ParameterVo ParameterVo : fundStateList) {
				if (ParameterVo.getPmco().equals(productInfoDto.getStates())) {
					productInfoDto.setStatesPmnm(ParameterVo.getPmnm());
				}
			}
			for (ParameterVo ParameterVo : fundtypeList) {
				if (ParameterVo.getPmco().equals(productInfoDto.getTypeId())) {
					productInfoDto.setTypeName(ParameterVo.getPmnm());
				}
			}
			for (ParameterVo ParameterVo : checkStateList) {
				if (ParameterVo.getPmco().equals(
						productInfoDto.getReview_status())) {
					productInfoDto.setReview_statusPmnm(ParameterVo.getPmnm());
				}
			}
			if ("N".equals(productInfoDto.getIsHot())) {
				productInfoDto.setIsHotName("否");
			} else if ("Y".equals(productInfoDto.getIsHot())) {
				productInfoDto.setIsHotName("是");
			}

			String appointdate = productInfoDto.getAppointdate();
			String appointEndDate = productInfoDto.getAppointEndDate();
			String currentWorkdate = productInfoDto.getCurrentWorkdate();
			String subdeadline = productInfoDto.getSubdeadline();
			String productStatus = "";
			String dateStep = "";
			if (currentWorkdate != null && appointdate != null
					&& appointEndDate != null && subdeadline != null) {
				if (DateUtils.getDaysBetween(currentWorkdate, appointdate) >= 0
						&& DateUtils.getDaysBetween(appointEndDate,
								currentWorkdate) >= 0) {
					productStatus = "预约期"; // 预约期
					dateStep = "N";
				} else if (DateUtils.getDaysBetween(currentWorkdate,
						appointEndDate) >= 0
						&& DateUtils.getDaysBetween(currentWorkdate,
								subdeadline) <= 0) {
					productStatus = "认购期"; // 认购期
					dateStep = "Y";
				} else {
					dateStep = "E";
					productStatus = "停止购买";
				}
			}
			productInfoDto.setDateStep(dateStep);
			productInfoDto.setFundState(productStatus);

		}
	}

	@Override
	public List<FundBalanceDto> getFinanceBalcons(String custno, String tradeAcco, String cycleenddt, String fundId, String quryType) {
		SearchParam sp = new SearchParam();
		sp.getSp().put("custno", custno);
		sp.getSp().put("tradeAcco", tradeAcco);
		sp.getSp().put("cycleenddt", cycleenddt);
		sp.getSp().put("fundId", fundId);
		sp.getSp().put("quryType", quryType);
		fundInfoDao.getFinanceBalcons(sp);
		List<FundBalanceDto> list = (List<FundBalanceDto>) sp.getSp().get("fundbalanceList");
		return list;
	}
}
