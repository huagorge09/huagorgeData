package com.cmwa.ecc.business.dao.accountManger;

import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.TradeDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 账户复核及驳回修改接口类
 * 
 * @author ex-chenbq
 *
 */
@MybatisDao
public interface AccountCheckDao {

	/**
	 * 账户类复核查询
	 * 
	 * @param tradeacco
	 *            交易账号
	 * @param fundacco
	 *            基金账号
	 * @param serialno
	 *            流水号
	 * @param dsapkind
	 *            业务类型
	 * @param checkst
	 *            复核状态
	 * @param begindate
	 *            开始时间
	 * @param enddate
	 *            结束时间
	 * @param operatorId
	 *            操作员代码
	 * @param operatorType
	 *            0，驳回修改查询；1，复核查询
	 * @return List
	 */
	public void accountCheckQry(SearchParam param);
	
	/**
	 * 账户类复核明细查询
	 * 
	 * @param serialno
	 *            流水号
	 * @return AccountCheckDto
	 */
	public void accountCheckDetailQry(SearchParam param);
	

	/**
     * 账户类复核
     * @param serialno,checkst,operatorId,permissionId
     * 
     */
    public void accountInfoCheck(SearchParam param);
    

	/**
	 * 账户类驳回修改
	 * @param dto
	 * @return
	 */
    public void accountRejectModify(OpenAccountDto dto);
    
    
    /**
     * 获取账户复核记录数
     * @return List
     * @throws Exception
     */
    public TradeDto getTradeCount();
}
