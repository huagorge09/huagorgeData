package com.cmwa.ecc.business.service.impl.accountManger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.accountManger.AccountCancelDao;
import com.cmwa.ecc.business.entity.accountManger.TradeDto;
import com.cmwa.ecc.business.service.accountManger.AccountCancelService;

/**
 * 账户复核及驳回修改业务层
 * @author ex-chenbq
 *
 */
@Service
public class AccountCancelServiceImpl implements AccountCancelService {

	private Logger logger = Logger.getLogger(AccountCancelServiceImpl.class.getName());
	
	@Autowired
	private AccountCancelDao accountCancelDao;

	@Override
	public TradeDto accountCancel(TradeDto dto) {
		try {
			accountCancelDao.accountCancel(dto);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("撤销账户申请异常：",e);
			dto.setErrcode("9999");
			dto.setErrmsg("撤销账户申请异常："+e.getMessage());
		}
		return dto;
	}
}