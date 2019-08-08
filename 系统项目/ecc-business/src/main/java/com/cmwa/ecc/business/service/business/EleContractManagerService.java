package com.cmwa.ecc.business.service.business;

import com.cmwa.ecc.business.entity.trade.EleContractDto;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface EleContractManagerService {
	/**
	 * 获取产品电子合同列表
	 * @return
	 */
	public Page<EleContractDto> getEleContractList(SearchParam sp);
}
