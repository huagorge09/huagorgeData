package com.cmwa.ecc.business.service.impl.dsTrade;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ecc.business.dao.common.CommonDao;
import com.cmwa.ecc.business.dao.dsTrade.DsTradeDao;
import com.cmwa.ecc.business.entity.dsTrade.DsTradeDataDto;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.service.dsTrade.DsTradeService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

@Service
public class DsTradeServiceImpl implements DsTradeService{

	@Autowired
	private DsTradeDao dsTradeDao;
	
	@Autowired
	private CommonDao commonDao;
	@Override
	public Page<DsTradeDataDto> queryDsTradeDataListPage(SearchParam sp) {
		sp = changeDatePattern(sp);
		List<DsTradeDataDto> queryDsTradeDataListPage = dsTradeDao.queryDsTradeDataListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(dsTradeDao.queryDsTradeDataListTotal(sp));
		return Page.create(queryDsTradeDataListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}
	
	/* 起止日期更换格式 */
	public SearchParam  changeDatePattern(SearchParam sp) {
		//起止日期更换格式
		String startDate = null;
		String endDate = null;
		if(sp.getSp().get("startDate") != null) {
			startDate =  sp.getSp().get("startDate").toString().replace("-", "");
			sp.getSp().put("startDate", startDate);
		}
		if(sp.getSp().get("endDate") != null) {
			endDate =  sp.getSp().get("endDate").toString().replace("-", "");
			sp.getSp().put("endDate", endDate);
		}
		return sp;
	}

	@Override
	public ModelAndView openDialog(HttpServletRequest request) {
 		String serialno = request.getParameter("serialno");
		SearchParam sp = new SearchParam();
		sp.getSp().put("serialno",serialno);
		sp.getSp().put("pmst", "SYSTEM");
		sp.getSp().put("pmky", "TRADESIGN");
		List<ParameterVo> contractData = commonDao.getParameterListPage(sp);
		DsTradeDataDto dtdd = dsTradeDao.queryDsTradeDataList(sp).get(0);
		ModelAndView mav = new ModelAndView();
		mav.addObject("contractList",contractData);
		mav.addObject("dsTradeDto",dtdd);
		mav.setViewName("jsp/tradeDataMgr/updateDsTrade");
		return mav;
	}

	@Override
	public void updateTradeDate(DsTradeDataDto dto) {
		dsTradeDao.updateTradeData(dto);
	}
}
