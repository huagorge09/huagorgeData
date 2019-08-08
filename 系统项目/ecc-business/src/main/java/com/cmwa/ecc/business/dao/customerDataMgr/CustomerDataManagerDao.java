package com.cmwa.ecc.business.dao.customerDataMgr;

import java.util.List;

import com.cmwa.ecc.business.entity.accountManger.RiskLevelDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface CustomerDataManagerDao {
	
	public List<RiskLevelDto> getRiskLevelInfoListPage(SearchParam sp) throws Exception;
	
	public List<RiskLevelDto> getRiskLevelInfoList(SearchParam sp) throws Exception;
	
	public int getRiskLevelInfoListTotal(SearchParam sp) throws Exception;

	public RiskLevelDto getRiskLevelInfoByCustNo(String custNo);
}
