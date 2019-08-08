package com.cmwa.ecc.business.service.customerDataManager;

import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.RiskLevelDto;

public interface CustTradeService {

	public OpenAccountDto modifyCategoryV2(OpenAccountDto dto) throws Exception ;
	
	public RiskLevelDto updateVoiceRecord(RiskLevelDto dto) throws Exception; 
	
	public RiskLevelDto auditCustRiskLevelV2(RiskLevelDto dto) throws Exception ;
}
