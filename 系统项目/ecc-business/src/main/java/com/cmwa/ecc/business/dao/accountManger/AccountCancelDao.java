package com.cmwa.ecc.business.dao.accountManger;

import com.cmwa.ecc.business.entity.accountManger.TradeDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;

/**
 * 账户复核及驳回修改接口类
 * 
 * @author ex-chenbq
 *
 */
@MybatisDao
public interface AccountCancelDao {
	
	/**
     * 撤销账户申请
     * @param dto TradeDto
     * @return TradeDto
     */
    public void accountCancel(TradeDto dto);
}
