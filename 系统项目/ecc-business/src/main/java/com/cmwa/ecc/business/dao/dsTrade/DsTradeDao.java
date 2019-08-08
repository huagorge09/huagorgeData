package com.cmwa.ecc.business.dao.dsTrade;

import java.util.List;

import com.cmwa.ecc.business.entity.dsTrade.DsTradeDataDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;
@MybatisDao
public interface DsTradeDao {
	/***
	 * 交易资料信息查询
	 * @return
	 */
	public List<DsTradeDataDto> queryDsTradeDataListPage(SearchParam sp);
	
	public List<DsTradeDataDto> queryDsTradeDataList(SearchParam sp);
	
	public int queryDsTradeDataListTotal(SearchParam sp);
	
	public void updateTradeData(DsTradeDataDto dto);
}
