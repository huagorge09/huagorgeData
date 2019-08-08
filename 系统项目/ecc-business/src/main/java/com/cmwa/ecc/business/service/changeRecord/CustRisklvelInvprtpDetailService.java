package com.cmwa.ecc.business.service.changeRecord;

import com.cmwa.ecc.business.entity.changeRecord.CustRisklvelInvprtpDetail;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface CustRisklvelInvprtpDetailService {
	
	/**
	 * 		客户评估详情页调用 根据custno查询变动记录
	 * @param custNo
	 * @return
	 */
	public Page<CustRisklvelInvprtpDetail> queryChangeRecord(SearchParam sp);
	
}
