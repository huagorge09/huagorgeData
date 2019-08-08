package com.cmwa.ecc.business.dao.account;

import java.util.List;

import com.cmwa.ecc.business.entity.accountManger.BankAccoInfoDto;
import com.cmwa.ecc.business.entity.accountManger.CustInfoDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface CustomerInfoDao{
	/**
	 * 线上用户
	 * @param sp
	 * @return
	 */
	public abstract List<CustInfoDto> onlineCustomerListPage(SearchParam sp);
	
	public abstract List<CustInfoDto> getOnlineUserInfo(SearchParam sp);
	/**
	 * 根据用户custno查询交易账号
	 * @param custno
	 * @return
	 * 			List
	 * @author maj
	 */
	public abstract List<BankAccoInfoDto> queryTradeAccoListPage(SearchParam sp) ;
}