package com.cmwa.ecc.business.dao.bakCustomer;

import com.cmwa.ecc.business.entity.accountManger.BakCustDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface BakCustomerDao {
	public BakCustDto manageBakCustomer(BakCustDto dto);
	
	public void queryBakCustomerCheck(SearchParam sp);
	
	public void checkBakCustomer(SearchParam sp);
}
