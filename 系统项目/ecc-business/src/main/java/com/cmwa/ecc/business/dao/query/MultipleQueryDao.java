package com.cmwa.ecc.business.dao.query;

import java.util.List;

import com.cmwa.ecc.business.entity.multiple.FundBalanceSrhVo;
import com.cmwa.ecc.business.entity.multiple.FundCostFalseVo;
import com.cmwa.ecc.business.entity.multiple.FundCustInfoVo;
import com.cmwa.ecc.business.entity.multiple.FundNavVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface MultipleQueryDao {
	
	//查询账户信息
	
	public List<FundCustInfoVo> queryFundCustInfoListPage(SearchParam sp) throws Exception ;
	
	//账户信息总数
	public int queryFundCustInfoListTotal(SearchParam sp) throws Exception ;
	
	//账户信息导出
	public List<FundCustInfoVo> queryFundCustInfoList(SearchParam sp) throws Exception ;
	
	//查询基金净值
	
	public List<FundNavVo> queryFundNavListPage(SearchParam sp) throws Exception ;
	
	//基金净值总数
	
	public int queryFundNavListTotal(SearchParam sp) throws Exception ;
	
	//查询基金余额
	
	public List<FundBalanceSrhVo> queryFundBalanceListPage(SearchParam sp) throws Exception;
	
	//基金余额总数
	
	public int queryFundBalanceListTotal(SearchParam sp) throws Exception;
	
	//基金余额导出
	
	public List<FundBalanceSrhVo> queryFundBalanceList(SearchParam sp) throws Exception;
	
	//查询定投扣款失败
	
	public List<FundCostFalseVo> queryFundCostFasleListPage(SearchParam sp) throws Exception;
	
	//定投扣款失败总数
	
	public int queryFundCostFasleList(SearchParam sp) throws Exception;

}
