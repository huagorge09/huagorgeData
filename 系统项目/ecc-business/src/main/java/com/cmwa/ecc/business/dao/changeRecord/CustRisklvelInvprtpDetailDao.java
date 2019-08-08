package com.cmwa.ecc.business.dao.changeRecord;

import java.util.List;

import com.cmwa.ecc.business.entity.changeRecord.CustRisklvelInvprtpDetail;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface CustRisklvelInvprtpDetailDao {

	/**
	 * 		根据custno查询变动明细
	 * @param custNo
	 * @return
	 */
	public List<CustRisklvelInvprtpDetail> queryChangeRecordByCustNo(SearchParam sp);
	
	
}
