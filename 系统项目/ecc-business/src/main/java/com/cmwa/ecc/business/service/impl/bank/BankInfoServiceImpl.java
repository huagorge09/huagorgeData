package com.cmwa.ecc.business.service.impl.bank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.bank.BankBaseDao;
import com.cmwa.ecc.business.dao.bank.BankInfoDao;
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.service.bank.BankInfoService;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 银行信息管理接口
 * @author ex-liuy
 *
 */
@Service
public class BankInfoServiceImpl implements BankInfoService {
	@Autowired
	private BankInfoDao bankInfoDao;
	
	@Autowired
	private BankBaseDao bankBaseDao;
	
	@Override
	public List<BankBnkbaseVo> getBankBnkbaseList(SearchParam sp) {
		return bankInfoDao.getBankBnkbaseList(sp);
	}

	@Override
	public List<BankBnkbaseVo> queryDsBankBase(SearchParam sp) {
		return bankBaseDao.queryDsBankBase(sp);
	}
}
