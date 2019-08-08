package com.cmwa.ecc.business.dao.directmanager;

import java.util.List;

import com.cmwa.ecc.business.entity.accountManger.ValidateDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 过期证件管理 数据交互 
 * @author ex-liuy
 *
 */
@MybatisDao
public interface ValidateDao {
	/**
	 * 查询过期证件客户信息
	 * @param sp
	 * @return
	 */
	public List<ValidateDto> queryValidateList(SearchParam sp);
	
	/**
	 * 证件有效期修改
	 * @param dto
	 * @return
	 */
	public void updateValidate(SearchParam sp);
}
