package com.cmwa.ecc.business.dao.accountManger;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.entity.accountManger.CbpOffLineOTOInvestDto;
import com.cmwa.ecc.business.entity.accountManger.FundAcctDto;
import com.cmwa.ecc.business.entity.accountManger.FundBalanceDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.TaInfoDto;
import com.cmwa.ecc.business.entity.accountManger.UserTaxInfoDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 公共信息查询DAO
 * 
 * @author ex-chenbq
 *
 */
@MybatisDao
public interface CommonQueryDao {
	
	/**
	 * 查询最大的文件编号
	 * @return
	 */
	public String getMaxFileNo();
	
	/**
	 * 查询最大的存档位置
	 * @return
	 */
	public String getMaxKeepAddress();
	
	/**
	 * 根据文件编号查询文件数量
	 * @param fileNo
	 * @return
	 */
	public int getFileNo(@Param("fileNo")String fileNo);
	
	/***
	 * 查询开户文件编号
	 * @return
	 */
	public String getOpenFileNo(@Param("custno")String custno );
	
	/***
	 * 查询客户的文件编号
	 * @return
	 */
	public String getCustFileInfo(@Param("custno")String custno );
	
	/**
	 * 备案客户信息查询
	 * @param param
	 * @return
	 */
	public void queryBakCust(SearchParam param);
	
	/**
	 * 开户信息查询
	 * @param param
	 * @return
	 */
	public void queryAccoByfundAcc(SearchParam param);
	
	/***
	 * 查询综合业务平台 银行信息
	 * @return
	 */
	public List<OpenAccountDto> queryWaspUserBankInfo(@Param("accName")String accName);
	
	/***
	 * 查询客户信息
	 * @return
	 */
	public List<OpenAccountDto> queryOpenUserInfo(SearchParam param);
	
	/***
	 * 查询线下一对一 客户资料信息
	 * @return
	 */
	public List<CbpOffLineOTOInvestDto> queryCbpOffLineOTOInvInfo(SearchParam param);
	
	/***
	 * 基金余额查询
	 * @return
	 */
	public List<FundBalanceDto> queryFundBalance(SearchParam param);
	

	/***
	 * 客户信息查询
	 * @return
	 */
	public void tradeaccoQry(SearchParam param);
	
	/**
     * 通过基金账号，证件号查询客户号
     * @param idno
     * @param fundacct
     * @return
     */
    public String getCustnoByIdnoAndFundAcco(@Param("idno")String idno, @Param("fundacct")String fundacct);
    
    /**
     * 帐户基本信息查询
     * @param custno
     * @return AccountDto
     */
    public void getAccountBycustNo(SearchParam param);
    
    /**
     * 基金账号查询
     * @param custno
     * @return
     */
	public List<FundAcctDto> getFundAccount(@Param("custno")String custno);
	
	/**
	 * 查询Ta类型信息
	 * @param tano
	 * @return
	 */
	public List<TaInfoDto> queryTaInfo(@Param("tano")String tano);
	
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
    public List<OpenAccountDto> accountModifyQuery(SearchParam param);
    

	/**
	 * 查询税收居民信息
	 * @param custno
	 * @return
	 */
	public List<UserTaxInfoDto> queryUserTaxInfoByList(@Param("custno")String custno);
}
