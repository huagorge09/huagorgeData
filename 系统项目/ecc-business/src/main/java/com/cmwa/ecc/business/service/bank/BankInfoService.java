package com.cmwa.ecc.business.service.bank;

import java.util.List;
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 银行信息管理接口
 * @author ex-liuy
 *
 */
public interface BankInfoService {
	/**
	 * 查询所有对接 银行列表
	 * @param sp
	 * @return
	 */
	public List<BankBnkbaseVo> getBankBnkbaseList(SearchParam sp);
	
	/**
	 * 查询所有对接 银行列表
	 * @param sp
	 * @return
	 */
	public List<BankBnkbaseVo> queryDsBankBase(SearchParam sp);
}
