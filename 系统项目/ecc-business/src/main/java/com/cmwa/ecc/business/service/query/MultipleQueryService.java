package com.cmwa.ecc.business.service.query;

import com.cmwa.ecc.business.entity.multiple.FundBalanceSrhVo;
import com.cmwa.ecc.business.entity.multiple.FundCostFalseVo;
import com.cmwa.ecc.business.entity.multiple.FundCustInfoVo;
import com.cmwa.ecc.business.entity.multiple.FundNavVo;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface MultipleQueryService {
	
	//查询账户信息

	public Page<FundCustInfoVo> queryFundCustInfoListPage(SearchParam sp) throws Exception ;
	
	//查询基金净值
	
	public Page<FundNavVo> queryFundNavListPage(SearchParam sp) throws Exception ;
	
	//查询基金余额
	
	public Page<FundBalanceSrhVo> queryFundBalanceListPage(SearchParam sp) throws Exception;
	
	//查询定投扣款失败
	
	public Page<FundCostFalseVo> queryFundCostFasleListPage(SearchParam sp) throws Exception;
}
