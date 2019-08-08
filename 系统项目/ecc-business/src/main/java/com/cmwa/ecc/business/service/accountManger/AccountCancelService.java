package com.cmwa.ecc.business.service.accountManger;

import com.cmwa.ecc.business.entity.accountManger.TradeDto;



/**
 * 账户复核及驳回修改接口类
 * 
 * @author ex-chenbq
 *
 */
public interface AccountCancelService {

	/**
     * 撤销账户申请
     * @param dto TradeDto
     * @return TradeDto
     */
    public TradeDto accountCancel(TradeDto dto);
}
