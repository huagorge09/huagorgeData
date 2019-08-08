package com.cmwa.ecc.business.dao.datamanager;

import java.util.List;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.MagicMap;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 客户资料管理
 * 数据交互
 * @author ex-liuy
 *
 */
@MybatisDao
public interface CustDataManagerDao {
	/**
	 * 查询客户资料列表
	 * @param sp
	 * @return
	 */
	public List<OpenAccountDto> queryCustDataInfoListPage(SearchParam sp); 
	
	/**
	 * 查询客户资料 无分页
	 * @param sp
	 * @return
	 */
	public List<OpenAccountDto> queryCustDataInfoAllList(SearchParam sp); 
	
	/**
	 * 根据APPSERIALNO查询客户资料信息
	 * @param sp
	 * @return
	 */
	public OpenAccountDto queryCustDataInfoByCondtion(SearchParam sp);
	
	/**
	 * 更新客户资料
	 * @param sp
	 */
	public void updateCustDataManagerInfo(MagicMap sp);
}
