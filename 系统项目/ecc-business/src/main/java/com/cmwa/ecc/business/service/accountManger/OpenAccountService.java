package com.cmwa.ecc.business.service.accountManger;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.entity.accountManger.AccountDto;
import com.cmwa.ecc.business.entity.accountManger.BakCustDto;
import com.cmwa.ecc.business.entity.accountManger.CbpOffLineOTOInvestDto;
import com.cmwa.ecc.business.entity.accountManger.FundBalanceDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountExcelDto;
import com.cmwa.ecc.business.entity.accountManger.TaInfoDto;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;



/**
 * 账户开户接口类
 * 
 * @author ex-chenbq
 *
 */
public interface OpenAccountService {
	
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
	public int getFileNo(String fileNo);
	
	/**
	 * 备案客户信息查询
	 * @param param
	 * @return
	 */
	public List<BakCustDto> queryBakCust(SearchParam param);
	
	/**
	 * 开户信息查询
	 * @param param
	 * @return
	 */
	public OpenAccountDto queryAccoByfundAcc(SearchParam param);
	
	/***
	 * 查询综合业务平台 银行信息
	 * @return
	 */
	public List<OpenAccountDto> queryWaspUserBankInfo(String accName);
	
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
	 * 开户Json字符串数据转换成实体
	 * @param str
	 * @return
	 */
	public OpenAccountDto convertBean(String str);
	
	/**
	 * 客户资料修改Json字符串数据转换成实体
	 * @param str
	 * @return
	 */
	public OpenAccountDto custModifyToBean(String str);
	

	/***
	 * 基金余额查询
	 * @return
	 */
	public Page<FundBalanceDto> queryFundBalance(SearchParam param);
	

	/***
	 * 客户信息查询
	 * @return
	 */
	public OpenAccountDto tradeAccoQuery(SearchParam param);
	
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
    public AccountDto getAccountBycustNo(SearchParam param);
    

	/**
	 * 查询Ta类型信息
	 * @param tano
	 * @return
	 */
	public List<TaInfoDto> queryTaInfo(String tano);
	
	
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
	 * 保存税收居民信息
	 * @param custNo
	 * @param dto
	 * @param taxResidentData
	 * @return
	 */
	public void saveUserTaxInfo(String custNo, OpenAccountDto dto , String taxResidentData);
	
	//备份并删除税收信息
	public void deleteTaxInfo(String custno);
	
	/**
	 * 反洗钱校验
	 * @param map
	 * @return
	 */
	public Map<String, String> antiMoneyLaunValid(SearchParam map);
}
