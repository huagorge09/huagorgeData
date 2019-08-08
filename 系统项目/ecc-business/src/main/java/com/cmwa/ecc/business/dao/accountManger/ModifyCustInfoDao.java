package com.cmwa.ecc.business.dao.accountManger;

import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;

/**
 * 账户资料管理接口类
 * 
 * @author ex-chenbq
 *
 */
@MybatisDao
public interface ModifyCustInfoDao {

	/**
	 * 个人/机构资料修改
	 * @param dto
	 */
	public void modifyAccount(OpenAccountDto dto);
	
	/**
	 * 银行资料修改
	 * @param dto
	 */
	public void modifyBankInfo(OpenAccountDto dto);
	

	/**
	 * 分类信息修改
	 * @param dto
	 */
	public void modifyCategoryInfo(OpenAccountDto dto);
}
