package com.cmwa.ecc.business.dao.business;

import java.util.List;
import com.cmwa.ecc.business.entity.trade.EleContractDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface EleContractDao {
	/**
	 * 获取产品电子合同列表
	 * @return
	 */
	public List<EleContractDto> queryContractListPage(SearchParam sp);
}
