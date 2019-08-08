package com.cmwa.ecc.business.dao.query;

import java.util.List;

import com.cmwa.ecc.business.entity.trade.TradeAckClearHisVo;
import com.cmwa.ecc.business.entity.trade.TradeAckClearVo;
import com.cmwa.ecc.business.entity.trade.TradeAckTodayVo;
import com.cmwa.ecc.business.entity.trade.TradeAppHisVo;
import com.cmwa.ecc.business.entity.trade.TradeAppTodayVo;
import com.cmwa.ecc.business.entity.trade.TradeCapitalBlotterVo;
import com.cmwa.ecc.business.entity.trade.TradeDividendDetailHisVo;
import com.cmwa.ecc.business.entity.trade.TradeDividendDetailVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * @author ex-wuh2
 *
 */
@MybatisDao
public interface TradeQueryDao {
	/**
     * 查询当前交易申请流水
     * @throws Exception
     */
	public List<TradeAppTodayVo> queryTradeAppTodayListPage(SearchParam sp) throws Exception;
    
	//查询当前交易总数
	public int queryTradeAppTodayListTotal(SearchParam sp) throws Exception;
	
	//当前交易导出
	public List<TradeAppTodayVo> queryTradeAppToday(SearchParam sp) throws Exception;

	
	/**
	 * 查询TA交易确认流水
	 * @throws Exception
	 */
	public List<TradeAckTodayVo> queryTradeAckTodayListPage(SearchParam sp) throws Exception;
    /**
     * 查询TA交易确认流水总数
     * @throws Exception
     */
    public int queryTradeAckTodayListTotal(SearchParam sp) throws Exception;

    /**
     * 查询二级清算交易确认流水
     * @throws Exception
     */
    public List<TradeAckClearVo> queryTradeAckClearListPage(SearchParam sp) throws Exception;
    /**
     * 查询二级清算交易确认流水总数
     * @throws Exception
     */
    public int queryTradeAckClearListTotal(SearchParam sp) throws Exception;

    /**
     * 查询历史交易申请流水
     * @throws Exception
     */
    public List<TradeAppHisVo> queryTradeAppHisListPage(SearchParam sp) throws Exception;
    /**
     * 查询历史交易导出
     * @throws Exception
     */
    public List<TradeAppHisVo> queryTradeAppHis(SearchParam sp) throws Exception;
    /**
     * 查询历史交易申请流水
     * @throws Exception
     */
    public int queryTradeAppHisListTotal(SearchParam sp) throws Exception;

    /**
     * 查询历史二级清算交易确认流水
     * @throws Exception
     */
    public List<TradeAckClearHisVo> queryTradeAckClearHisListPage(SearchParam sp) throws Exception;
    
    //历史二级交易总数 
    
    public int queryTradeAckClearHisListTotal(SearchParam sp) throws Exception;
    
    //历史二级交易导出
    
    public List<TradeAckClearHisVo> queryTradeAckClearHis(SearchParam sp) throws Exception;

    
    /**
     * 查询当天分红明细
     * @throws Exception
     */
    public List<TradeDividendDetailVo> queryTradeDividendDetailListPage(SearchParam sp) throws Exception;
    /**
     * 查询当天分红明总数
     * @throws Exception
     */
    public int queryTradeDividendDetailListTotal(SearchParam sp) throws Exception;

    /**
     * 查询历史分红明细
     * @throws Exception
     */
    public List<TradeDividendDetailHisVo> queryTradeDividendDetailHisListPage(SearchParam sp) throws Exception;
    
    /**
     * 查询历史分红总数
     * @throws Exception
     */
    public int queryTradeDividendDetailHisListTotal(SearchParam sp) throws Exception;

    /**
     * 查询资金流水
     * @throws Exception
     */
    public List<TradeCapitalBlotterVo> queryTradeCapitalBlotterListPage(SearchParam sp) throws Exception;
    
    //资金流水分页总数
    
    public int queryTradeCapitalBlotterListTotal(SearchParam sp) throws Exception;

}
