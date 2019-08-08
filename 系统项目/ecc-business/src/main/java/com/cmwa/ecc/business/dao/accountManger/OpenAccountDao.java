package com.cmwa.ecc.business.dao.accountManger;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.entity.accountManger.AppR1BlotterDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountExcelDto;
import com.cmwa.ecc.business.entity.accountManger.UserTaxInfoDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 账户开户接口类
 * 
 * @author ex-chenbq
 *
 */
@MybatisDao
public interface OpenAccountDao {
	
	/**
	 * 个人账户/机构开户
	 * @param dto
	 * @return
	 */
	public OpenAccountDto openCustom(OpenAccountDto dto);
	
	/**
	 * 机构开户（批量）
	 * @param dto
	 * @return
	 */
	public OpenAccountExcelDto batchOpenAccountSave(OpenAccountExcelDto dto);
	
	/**
	 * 将 用户的 税收居民信息 备份至 历史表
	 * @param custno
	 */
	public void createUserTaxInfoByBackUp(@Param("custno")String custno);
	
	/**
	 * 删除用户的 税收居民信息
	 */
	public void delUserTaxInfo(@Param("custno")String custno);
	
	/**
	 * 删除涉税信息接口业务表信息
	 */
	public void delAppR1BlotterInfo(@Param("custno")String custno);
	
	/**
	 * 保存用户的 税收居民信息
	 * @param userTaxInfoDto
	 */
	public void createUserTaxInfo(UserTaxInfoDto userTaxInfoDto);
	
	/**
	 * 保存用户的涉税信息至接口业务表
	 * @param appR1BlotterDto
	 */
	public void insertAppR1BlotterInfo(AppR1BlotterDto appR1BlotterDto);
	
	/**
	 * 查询用户涉税所需信息
	 */
	public AppR1BlotterDto queryUserTaxInfo(@Param("custno")String custno);
	
	/**
	 * 增开交易账号
	 * @param dto
	 * @return
	 */
	public OpenAccountDto tradeAccountAdd(OpenAccountDto dto);
	

	/**
     * 注销交易账号
     * @param dto OpenAccountDto
     * @return OpenAccountDto
     */
    public OpenAccountDto destroyTradeacco(OpenAccountDto dto);
	
	/**
	 * 增开基金账号
	 * @param dto
	 * @return
	 */
	public OpenAccountDto openFundAccount(OpenAccountDto dto);
	

	/**
     * 注销基金账号
     * @param dto OpenAccountDto
     * @return OpenAccountDto
     */
	public OpenAccountDto destroyFundacco(OpenAccountDto dto);
	
	
	/**
	 * 反洗钱校验
	 * @param map
	 * @return
	 */
	public Map<String, String> antiMoneyLaunValid(SearchParam map);
}
