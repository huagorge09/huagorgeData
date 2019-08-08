package com.cmwa.ecc.business.service.accountManger;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.UserTaxInfoDto;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;



/**
 * 账户资料接口类
 * 
 * @author ex-chenbq
 *
 */
public interface ModifyCustInfoService {
	
	/**
     * 账户类修改查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @param serialno 流水号
     * @param dsapkind 业务类型
     * @param checkst 复核状态
     * @param begindate 开始时间
     * @param enddate 结束时间
     * @param operatorId 操作员代码
     * @param operatorType 0，驳回修改查询；1，复核查询
     * @return List
     */
    public Page<OpenAccountDto> accountModifyQuery(SearchParam param);
    

	/**
	 * 个人/机构资料修改
	 * @param dto
	 */
	public OpenAccountDto modifyAccount(OpenAccountDto dto);
	
	/**
	 * 银行资料修改
	 * @param dto
	 * @return
	 */
	public OpenAccountDto modifyBankInfo(OpenAccountDto dto);
	

	/**
	 * 查询税收居民信息
	 * @param custno
	 * @return
	 */
	public List<UserTaxInfoDto> queryUserTaxInfoByList(String custno);
	

	/**
	 * 客户分类信息修改
	 * @param custno
	 * @return
	 */
	public OpenAccountDto modifyCategoryInfo(JSONObject categoryInfo);
	
	/**
	 * 客户综合资料修改
	 * @param custno
	 * @return
	 */
	public OpenAccountDto modifySyntheSizeInfo(JSONObject categoryInfo);
	
}
