package com.cmwa.ecc.business.dao.customerDataMgr;

import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.RiskLevelDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;

@MybatisDao
public interface CustTradeDao {
	
	public void modifyCategoryV2(OpenAccountDto dto) throws Exception ;
	
	public RiskLevelDto updateVoiceRecord(RiskLevelDto dto) throws Exception; 
	
	public void auditCustRiskLevelV2(RiskLevelDto dto) throws Exception ;
}
