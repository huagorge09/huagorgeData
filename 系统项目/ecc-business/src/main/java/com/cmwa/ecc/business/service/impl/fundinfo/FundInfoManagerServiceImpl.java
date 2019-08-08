package com.cmwa.ecc.business.service.impl.fundinfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.fundInfoManager.FundInfoManagerDao;
import com.cmwa.ecc.business.entity.fundinfo.FundBalanceVo;
import com.cmwa.ecc.business.entity.fundinfo.FundManagerVo;
import com.cmwa.ecc.business.entity.fundinfo.FundStopVo;
import com.cmwa.ecc.business.service.fundinfo.FundInfoManagerService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;

@Service
public class FundInfoManagerServiceImpl implements FundInfoManagerService {

	private static Logger logger = Logger
			.getLogger(FundInfoManagerServiceImpl.class.getName());

	@Autowired
	private FundInfoManagerDao fundInfoManagerDao;
	
	@Override
	public boolean addFundInfo(FundManagerVo vo) {
		try {
			fundInfoManagerDao.addFundInfo(vo);
			return true;
		} catch (Exception e) {
			logger.error("增加基金信息异常",e);
			return false;
		}
	}

	@Override
	public boolean delFundInfo(String fundId) {
		try {
			fundInfoManagerDao.delFundInfo(fundId);
			return true;
		} catch (Exception e) {
			logger.error("删除基金信息异常",e);
			return false;
		}
	}

	@Override
	public boolean modifyFundInfo(FundManagerVo vo) {
		try {
			fundInfoManagerDao.modifyFundInfo(vo);
			return true;
		} catch (Exception e) {
			logger.error("修改基金信息异常",e);
			return false;
		}
	}

	@Override
	public List<FundManagerVo> getAllFundInfo() {
		List<FundManagerVo> list = new ArrayList<FundManagerVo>();
		try {
			list = fundInfoManagerDao.getAllFundInfo();
		} catch (Exception e) {
			logger.error("获取所有的基金信息异常",e);
		}
		return list;
	}

	@Override
	public List<FundManagerVo> getSpecProFundInfo() {
		List<FundManagerVo> list = new ArrayList<FundManagerVo>();
		try {
			list = fundInfoManagerDao.getSpecProFundInfo();
		} catch (Exception e) {
			logger.error("获取专户产品的基金信息异常",e);
		}
		return list;
	}

	@Override
	public FundManagerVo getFundInfo(String fundId) {
		FundManagerVo fundInfoVo = new FundManagerVo();
		try {
			fundInfoVo = fundInfoManagerDao.getFundInfo(fundId);
		} catch (Exception e) {
			logger.error("获取指定基金代码的基金信息异常",e);
		}
		return fundInfoVo;
	}

	@Override
	public List<FundManagerVo> getCachedFunds() {
		List<FundManagerVo> list = new ArrayList<FundManagerVo>();
		try {
			list = fundInfoManagerDao.getCachedFunds();
		} catch (Exception e) {
			logger.error("获取所有基金异常",e);
		}
		return list;
	}

	@Override
	public String[] addFundStop(FundStopVo vo) {
		String[] result = new  String[2];
		try {
			fundInfoManagerDao.addFundStop(vo);
			result[0] = "0000";
			result[1] = "操作成功";
		} catch (Exception e) {
			logger.error("新增基金级暂停交易数据异常",e);
			result[0] = "9999";
			result[1] = "参数异常";
		}
		return result;
	}

	@Override
	public String[] checkFundStop(FundStopVo vo) {
		String[] result = new  String[2];
		try {
			fundInfoManagerDao.checkFundStop(vo);
			result[0] = "0000";
			result[1] = "操作成功";
		} catch (Exception e) {
			logger.error("基金级暂停交易复核异常",e);
			result[0] = "9999";
			result[1] = "参数异常";
		}
		return result;
	}

	@Override
	public String[] deleteFundStop(FundStopVo vo) {
		String[] result = new  String[2];
		try {
			fundInfoManagerDao.deleteFundStop(vo);
			result[0] = "0000";
			result[1] = "操作成功";
		} catch (Exception e) {
			logger.error("基金级暂停交易删除异常",e);
			result[0] = "9999";
			result[1] = "参数异常";
		}
		return result;
	}

	@Override
	public String[] updateFundStop(FundStopVo vo) {
		String[] result = new  String[2];
		try {
			fundInfoManagerDao.updateFundStop(vo);
			result[0] = "0000";
			result[1] = "操作成功";
		} catch (Exception e) {
			logger.error("基金级暂停交易修改异常",e);
			result[0] = "9999";
			result[1] = "参数异常";
		}
		return result;
	}

	@Override
	public List<FundManagerVo> getAllSubFundsArray(String fundst, String tano) {
		List<FundManagerVo> list = new ArrayList<FundManagerVo>();
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
			list = fundInfoManagerDao.getAllSubFundsArray(fundst, tano);
		} catch (Exception e) {
			logger.error("获取所有基金(包括限制基金)异常",e);
		}
		return list;
	}

	@Override
	public SearchParam syschonizeSetUpDate(String fundid) {
		SearchParam sp = new SearchParam();
		try {
			sp.getSp().put("fundid", fundid);
			fundInfoManagerDao.syschonizeSetUpDate(sp);
		} catch (Exception e) {
			logger.error("同步成立日期异常",e);
		}
		return sp;
	}

	@Override
	public boolean addChkFundInfo(FundManagerVo vo) {
		try {
			fundInfoManagerDao.addChkFundInfo(vo);
			return true;
		} catch (Exception e) {
			logger.error("增加未复核的基金信息异常",e);
			return false;
		}
	}

	@Override
	public boolean delChkFundInfo(String fundId, String processor) {
		try {
			fundInfoManagerDao.delChkFundInfo(fundId, processor);
			return true;
		} catch (Exception e) {
			logger.error("删除一个未复核的基金异常",e);
			return false;
		}
	}

	@Override
	public boolean modifyChkFundInfo(FundManagerVo vo) {
		try {
			fundInfoManagerDao.modifyChkFundInfo(vo);
			return true;
		} catch (Exception e) {
			logger.error("修改未复核的基金信息异常",e);
			return false;
		}
	}

	@Override
	public FundManagerVo getChkFundInfo(String fundId) {
		FundManagerVo fundInfoVo = new FundManagerVo();
		try {
			fundInfoVo = fundInfoManagerDao.getChkFundInfo(fundId);
		} catch (Exception e) {
			logger.error("获取指定基金代码的未复核的基金信息异常",e);
		}
		return fundInfoVo;
	}

	@Override
	public String[] checkFundInfo(String fundId, String checker,
			String checkstatus) {
		String[] result = new String[2];
		SearchParam sp = new SearchParam();
		sp.getSp().put("fundId", fundId);
		sp.getSp().put("checker", checker);
		sp.getSp().put("checkstatus", checkstatus);
		try {
			fundInfoManagerDao.checkFundInfo(sp);
			result[0] = sp.getSp().get("resultCode").toString();
			result[1] = sp.getSp().get("resultMessage").toString();
		} catch (Exception e) {
			logger.error("基金信息复核异常",e);
			result[0] = "9999";
			result[1] = "参数异常";
		}
		return result;
	}

	@Override
	public Page<FundManagerVo> getAllChkFundInfoListPage(SearchParam sp) {
		List<FundManagerVo> list = new ArrayList<FundManagerVo>();
		try {
			list = fundInfoManagerDao.getAllChkFundInfoListPage(sp);
		} catch (Exception e) {
			logger.error("获取所有待复核的基金信息异常",e);
		}
		return Page.create(list, sp.getStart(), sp.getLimit(), sp.getTotal() , false);
	}

	@Override
	public Page<FundManagerVo> getAllFundInfoListPage(SearchParam sp) {
		List<FundManagerVo> list = new ArrayList<FundManagerVo>();
		try {
			list = fundInfoManagerDao.getAllFundInfoListPage(sp);
		} catch (Exception e) {
			logger.error("获取所有待复核的基金信息异常",e);
		}
		return Page.create(list, sp.getStart(), sp.getLimit(), sp.getTotal() , false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FundBalanceVo> getFinanceBalance(String custno,
			String tradeAcco, String cycleenddt, String fundId, String quryType) {
		SearchParam sp = new SearchParam();
		sp.getSp().put("custno", custno);
		sp.getSp().put("tradeAcco", tradeAcco);
		sp.getSp().put("cycleenddt", cycleenddt);
		sp.getSp().put("fundId", fundId);
		sp.getSp().put("quryType", quryType);
		fundInfoManagerDao.getFinanceBalance(sp);
		List<FundBalanceVo> list = (List<FundBalanceVo>) sp.getSp().get("fundbalanceList");
		return list;
	}


}
