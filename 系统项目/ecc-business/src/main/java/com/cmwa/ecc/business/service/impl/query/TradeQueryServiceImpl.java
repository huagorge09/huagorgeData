/**
* @Title: TradeQueryServiceImpl.java  
* @author ex-wuh2  
* @date 2018年6月13日  
* @version V1.0  
 */
package com.cmwa.ecc.business.service.impl.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.query.TradeQueryDao;
import com.cmwa.ecc.business.entity.trade.TradeAckClearHisVo;
import com.cmwa.ecc.business.entity.trade.TradeAckClearVo;
import com.cmwa.ecc.business.entity.trade.TradeAckTodayVo;
import com.cmwa.ecc.business.entity.trade.TradeAppHisVo;
import com.cmwa.ecc.business.entity.trade.TradeAppTodayVo;
import com.cmwa.ecc.business.entity.trade.TradeCapitalBlotterVo;
import com.cmwa.ecc.business.entity.trade.TradeDividendDetailHisVo;
import com.cmwa.ecc.business.entity.trade.TradeDividendDetailVo;
import com.cmwa.ecc.business.service.query.TradeQueryService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * @author ex-wuh2
 *
 */
@Service
public class TradeQueryServiceImpl implements TradeQueryService{

	@Autowired
	private TradeQueryDao tradeQueryDao;
		
	//当前交易分页
	@Override
	public Page<TradeAppTodayVo> queryTradeAppTodayListPage(SearchParam sp) throws Exception {
		String appDate = null;
		if(sp.getSp().get("appDate") != null) {
			appDate =  sp.getSp().get("appDate").toString().replace("-", "");
			sp.getSp().put("appDate", appDate);
		}
		List<TradeAppTodayVo> queryTradeAppTodayListPage =tradeQueryDao.queryTradeAppTodayListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(tradeQueryDao.queryTradeAppTodayListTotal(sp));
		return Page.create(queryTradeAppTodayListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}
	
	//TA
	@Override
	public Page<TradeAckTodayVo> queryTradeAckTodayListPage(SearchParam sp) throws Exception {
		List<TradeAckTodayVo> queryTradeAckTodayListPage =tradeQueryDao.queryTradeAckTodayListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(tradeQueryDao.queryTradeAckTodayListTotal(sp));
		return Page.create(queryTradeAckTodayListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}
	
	//二级清算
	@Override
	public Page<TradeAckClearVo> queryTradeAckClearListPage(SearchParam sp) throws Exception {
		List<TradeAckClearVo> queryTradeAckClearListPage =tradeQueryDao.queryTradeAckClearListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(tradeQueryDao.queryTradeAckClearListTotal(sp));
		return Page.create(queryTradeAckClearListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}
	
	//历史交易
	@Override
	public Page<TradeAppHisVo> queryTradeAppHisListPage(SearchParam sp) throws Exception {
		sp = changeDatePattern(sp);
		List<TradeAppHisVo> queryTradeAppHisListPage =tradeQueryDao.queryTradeAppHisListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(tradeQueryDao.queryTradeAppHisListTotal(sp));
		return Page.create(queryTradeAppHisListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}

	//历史二级
	@Override
	public Page<TradeAckClearHisVo> queryTradeAckClearHisListPage(SearchParam sp) throws Exception {
		sp = changeDatePattern(sp);
		List<TradeAckClearHisVo> queryTradeAckClearHisListPage =tradeQueryDao.queryTradeAckClearHisListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(tradeQueryDao.queryTradeAckClearHisListTotal(sp));
		return Page.create(queryTradeAckClearHisListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}
	
	//当天分红
	@Override
	public Page<TradeDividendDetailVo> queryTradeDividendDetailListPage(SearchParam sp) throws Exception {
		List<TradeDividendDetailVo> queryTradeDividendDetailListPage =tradeQueryDao.queryTradeDividendDetailListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(tradeQueryDao.queryTradeDividendDetailListTotal(sp));
		return Page.create(queryTradeDividendDetailListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}
	
	//历史分红
	@Override
	public Page<TradeDividendDetailHisVo> queryTradeDividendDetailHisListPage(SearchParam sp) throws Exception {
		sp = changeDatePattern(sp);
		List<TradeDividendDetailHisVo> queryTradeDividendDetailHisListPage =tradeQueryDao.queryTradeDividendDetailHisListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(tradeQueryDao.queryTradeDividendDetailHisListTotal(sp));
		return Page.create(queryTradeDividendDetailHisListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}

	//资金流水
	@Override
	public Page<TradeCapitalBlotterVo> queryTradeCapitalBlotterListPage(SearchParam sp) throws Exception {
		sp = changeDatePattern(sp);
		List<TradeCapitalBlotterVo> queryTradeCapitalBlotterListPage =tradeQueryDao.queryTradeCapitalBlotterListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(tradeQueryDao.queryTradeCapitalBlotterListTotal(sp));
		return Page.create(queryTradeCapitalBlotterListPage, sp.getStart(), sp.getLimit(),
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
}
