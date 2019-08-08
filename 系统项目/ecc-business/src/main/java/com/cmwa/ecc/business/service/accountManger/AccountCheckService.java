package com.cmwa.ecc.business.service.accountManger;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.TradeDto;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;



/**
 * 账户复核及驳回修改接口类
 * 
 * @author ex-chenbq
 *
 */
public interface AccountCheckService {
	
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
	public Page<TradeDto> accountCheckQry(SearchParam param);
	

	/**
	 * 账户类复核明细查询
	 * 
	 * @param serialno
	 *            流水号
	 * @return AccountCheckDto
	 */
	public OpenAccountDto accountCheckDetailQry(SearchParam param);
	

	/**
	 * 账户类复核
	 * @param dto
	 * @return
	 */
    public OpenAccountDto accountInfoCheck(SearchParam param);
	
	
	/**
	 * 账户类驳回修改
	 * @param dto
	 * @return
	 */
    public OpenAccountDto accountRejectModify(OpenAccountDto dto);
    
    /**
     * 账户类驳回修改JSON字符串转实体类
     * @param str
     * @return
     */
    public OpenAccountDto convertBean(String jsonStr);
    
    /**
     * 获取账户复核记录数
     * @return List
     * @throws Exception
     */
    public TradeDto getTradeCount();
    

	/***
	 * 查询开户文件编号
	 * @return
	 */
	public String getOpenFileNo(String custno);
	

	/***
	 * 查询客户的文件编号
	 * @return
	 */
	public String getCustFileInfo(@Param("custno")String custno );
}
