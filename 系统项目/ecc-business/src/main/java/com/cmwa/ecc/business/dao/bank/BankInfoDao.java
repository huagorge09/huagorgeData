package com.cmwa.ecc.business.dao.bank;

import java.util.List;
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 
 * @author ex-liuy
 *
 */
@MybatisDao
public interface BankInfoDao {
	/**
	 * 查询所有对接 银行列表
	 * @param sp
	 * @return
	 */
	public List<BankBnkbaseVo> getBankBnkbaseList(SearchParam sp);
}
