package com.cmwa.ecc.business.service.query;

import java.util.List;

import com.cmwa.ecc.business.entity.trade.TradeAckClearHisVo;
import com.cmwa.ecc.business.entity.trade.TradeAckClearVo;
import com.cmwa.ecc.business.entity.trade.TradeAckTodayVo;
import com.cmwa.ecc.business.entity.trade.TradeAppHisVo;
import com.cmwa.ecc.business.entity.trade.TradeAppTodayVo;
import com.cmwa.ecc.business.entity.trade.TradeCapitalBlotterVo;
import com.cmwa.ecc.business.entity.trade.TradeDividendDetailHisVo;
import com.cmwa.ecc.business.entity.trade.TradeDividendDetailVo;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * @author ex-wuh2
 *
 */
public interface TradeQueryService {
	/**
     * 查询当前交易申请流水
     * @return List
     * @throws Exception
     */
	public Page<TradeAppTodayVo> queryTradeAppTodayListPage(SearchParam sp) throws Exception;
    
	
    /**
     * 查询TA交易确认流水
     * @return List
     * @throws Exception
     */
    public Page<TradeAckTodayVo> queryTradeAckTodayListPage(SearchParam sp) throws Exception;

    /**
     * 查询二级清算交易确认流水
     * @return List
     * @throws Exception
     */
    public Page<TradeAckClearVo> queryTradeAckClearListPage(SearchParam sp) throws Exception;

    /**
     * 查询历史交易申请流水
     * @param b_date
     * @param e_date
     * @return List
     * @throws Exception
     */
    public Page<TradeAppHisVo> queryTradeAppHisListPage(SearchParam sp) throws Exception;

    /**
     * 查询历史二级清算交易确认流水
     * @param b_date
     * @param e_date
     * @return List
     * @throws Exception
     */
    public Page<TradeAckClearHisVo> queryTradeAckClearHisListPage(SearchParam sp) throws Exception;

    /**
     * 查询当天分红明细
     * @return List
     * @throws Exception
     */
    public Page<TradeDividendDetailVo> queryTradeDividendDetailListPage(SearchParam sp) throws Exception;

    /**
     * 查询历史分红明细
     * @param b_date
     * @param e_date
     * @return List
     * @throws Exception
     */
    public Page<TradeDividendDetailHisVo> queryTradeDividendDetailHisListPage(SearchParam sp) throws Exception;

    /**
     * 查询资金流水
     * @param b_date
     * @param e_date
     * @return List
     * @throws Exception
     */
    public Page<TradeCapitalBlotterVo> queryTradeCapitalBlotterListPage(SearchParam sp) throws Exception;
}
