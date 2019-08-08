package com.cmwa.ec.weixin.dao;


import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.trade.facade.dto.fund.FundTradeDto;
import com.cmwa.ec.weixin.dto.RemindFailureRedeemDto;
import com.cmwa.ec.weixin.dto.TemplateMsgtoMECCDto;

public interface TemplateMsgtoMECCDao {

	void insertTemplateMsgtoMECC(@Param("dto")TemplateMsgtoMECCDto dto);
	
	/**
     * 查询失败赎回订单客户信息
     * @param list
     * @return
     */
    public RemindFailureRedeemDto queryRemindFailureRedeem(@Param("dto")FundTradeDto fundTradeDto);
}
