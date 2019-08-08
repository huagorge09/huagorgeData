package com.cmwa.ecc.business.dao.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.entity.accountManger.BakCustDto;
import com.cmwa.ecc.business.entity.accountManger.BrokerDto;
import com.cmwa.ecc.business.entity.accountManger.BrokerInfoDto;
import com.cmwa.ecc.business.entity.accountManger.CapitalCheckDto;
import com.cmwa.ecc.business.entity.accountManger.FundBalanceDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.OpenListDto;
import com.cmwa.ecc.business.entity.accountManger.RiskLevelDto;
import com.cmwa.ecc.business.entity.accountManger.SeatDto;
import com.cmwa.ecc.business.entity.accountManger.ValidateDto;
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.entity.trade.TradeCheckDto;
import com.cmwa.ecc.business.entity.trade.TradeDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface QueryDao {

	/**
     * 通过客户号查询客户资料
     * @param custNo 客户号
     * @return OpenAccountDto
     */
	public OpenAccountDto queryAccoByCust(@Param("custNo")String custNo) throws Exception;
    /**
     * 获取所有经济人列表
     * @return List
     * @throws Exception
     */
    public List<OpenListDto> getBrokers() throws Exception;
    /**
     * 分基金账号查询客户资料
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @param accountType 账户类型
     * @return OpenAccountDto
     */
    public OpenAccountDto fundaccoQry(OpenAccountDto dto) throws Exception;
    
    /**
     * 个人/机构资料查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @param accountType 账户类型
     * @return OpenAccountDto
     */
    public OpenAccountDto accountQryByAcco(OpenAccountDto dto) throws Exception;
    
    /**
     * 银行资料查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @param accountType 账户类型
     * @return OpenAccountDto
     */
    public OpenAccountDto bankQryByAcco(OpenAccountDto dto) throws Exception;
    
    /**
     * 收款账户查询
     * @return List(map)
     */
    public List getReceiveAccoList() throws Exception;
    
    /**
     * 交易类复核查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @param serialno 流水号
     * @param dsapkind 业务类型
     * @param checkst 复核状态
     * @param begindate 开始时间
     * @param enddate 结束时间
     * @param opid 操作员代码
     * @param checkno 主管编码
     * @param trustType 委托方式
     * @param fundid 基金代码
     * @param operatorId 操作员代码
     * @param operatorType 0，驳回修改查询；1，复核查询
     * @return List
     */
    public List<TradeCheckDto> tradeCheckQryListPage(SearchParam sp) throws Exception;
    
    /**
     * 交易类复核记录数查询
     * @param sp
     * @return
     * @throws Exception
     */
    public TradeCheckDto getTradeCheckCount(SearchParam sp) throws Exception;
    
    /**
     * 批量-交易类复核记录数查询
     * @param sp
     * @return
     * @throws Exception
     */
    public TradeCheckDto getBatchTradeCheckCount(SearchParam sp) throws Exception;
    
    /**
     * 交易类复核明细查询
     * @param serialno 流水号
     * @return TradeCheckDto
     */
    public TradeCheckDto tradeCheckDetailQry(@Param("serialno")String serialno) throws Exception;
    
    /**
     * 资金类复核查询
     * @param tradeacco 交易账号
     * @param capitalno 资金流水号
     * @param capitaltype 资金类型
     * @param checkst 复核状态
     * @param begindate 开始时间
     * @param enddate 结束时间
     * @param operatorId 操作员代码
     * @param operatorType 0，驳回修改查询；1，复核查询
     * @return List
     */
    public List<CapitalCheckDto> capitalCheckQry(CapitalCheckDto dto) throws Exception;
    
    /**
     * 资金类复核明细查询
     * @param capitalno 资金流水号
     * @return CapitalCheckDto
     */
    public CapitalCheckDto capitalCheckDetailQry(@Param("capitalno")String capitalno) throws Exception;
    
    /**
     * 查询是否需要授权
     * @param permissionId,apkind
     * @return boolean
     */
    public SearchParam queryPermission(SearchParam sp) throws Exception;
    
    /**
     * 授权校验
     * @param String checkno,String checkpwd
     * @return boolean
     */
    public SearchParam checkPermission(SearchParam sp) throws Exception;
    
    /**
     * 资金存入查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @param serialno 申请流水号
     * @param checkst 存入状态
     * @param apdt 申请时间
     * @return List
     */
    public List<CapitalCheckDto> storeQry(CapitalCheckDto dto) throws Exception;

    /**
     * 基金份额查询
     * @param custno 客户编号
     * @param tradeacco 交易账号
     * @param fundid 基金代码
     */
	public FundBalanceDto getFundBalanceInfo(FundBalanceDto dto) throws Exception;
	
	/**
     * 证件有效期查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @param idtp 证件类型
     * @param idno 证件号码
     * @param idnm 证件持有人姓名
     * @param begindate 查询时间起
     * @param enddate 查询时间止
     * @param invtp 客户类别
     * @param operatorType 查询类别
     * @return List
     */
	public List<ValidateDto> validateQry(ValidateDto dto) throws Exception;
	
	/**
     * 获取银行（直销柜台）
     * return List
     */	
	public List<BankBnkbaseVo> getDSBanksList() throws Exception;
	
	/**
     * 交易撤单查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     * 2010-02-04 xul 交易撤单、账户类撤销中，输入交易账号或基金账号后查询出的结果为当前操作员经办的交易流水
     */
	public List<TradeDto> cancelQryListPage(SearchParam sp) throws Exception;

    /**
     * 客户经理修改查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     */
    public List<BrokerInfoDto> brokerModifyQry(BrokerInfoDto dto) throws Exception;

	/**
     * 交易撤单查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     */
	public List<TradeDto> cancelTradeDetailQry(@Param("tradeacco")String tradeacco,@Param("fundacco") String fundacco) throws Exception;
	
	/**
     * 客户号查询客户经理
     * @param custno 客户号
     * @return List
     */
	public List<BrokerInfoDto> brokerQryByCustno(@Param("custno")String custno) throws Exception;
	
	/**
     * 分红方式查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     */
    public SearchParam melonListQry(SearchParam sp) throws Exception;
    
    /**
     * 资金撤单查询（客户信息）
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     * 资金撤单中，输入交易账号或基金账号后查询出的结果为当前操作员经办的交易流水
     */
    public List<TradeDto> capitalCancelQry(TradeDto dto) throws Exception;
    
    /**
     * 客户经理维护查询
     * @param brokerno 交易账号
     * @return List
     */
    public List<BrokerDto> brokerQry(@Param("brokerno")String brokerno) throws Exception;
    
    /**
     * 客户风险等级查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     */
    public List<RiskLevelDto> risklevelQry(RiskLevelDto dto) throws Exception;
    
    /**
     * 客户经理复核查询
     * @param brokerno 客户经理编号,operatorId操作员编号
     * @return List
     */
    public List<BrokerDto> brokerCheckQry(@Param("brokerno")String brokerno,@Param("operatorId")String operatorId) throws Exception;
    
	/********************************************************************************
     * 账户类撤单查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     * 2010-02-04 xul 交易撤单、账户类撤销中，输入交易账号或基金账号后查询出的结果为当前操作员经办的交易流水
     */
    public List<TradeDto> accountCancelQry(@Param("tradeacco")String tradeacco,@Param("fundacco")String fundacco,@Param("operatorid")String operatorid) throws Exception;
    
    
    public TradeDto accountCancelDetailQry(@Param("serialno")String serialno,@Param("operatorid")String operatorid) throws Exception;

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
    public List<TradeDto> accountCheckQry(TradeDto dto) throws Exception;
    
    /**
	 * 账户类复核明细查询
	 * 
	 * @param serialno
	 *            流水号
	 * @return AccountCheckDto
	 */
    public OpenAccountDto accountCheckDetailQry(@Param("serialno")String serialno) throws Exception;
    
    
    public List<OpenAccountDto> accountModifyQry(OpenAccountDto dto) throws Exception;
    
    
    public OpenAccountDto tradeaccoQry(@Param("custno")String custno,@Param("tradeacco")String tradeacco, @Param("fundacco")String fundacco) throws Exception;
    
    /**
     * 备案客户查询
     * @param custno
     * @param role
     * @return
     * @throws Exception
     */
    public List<BakCustDto> queryBakCust(@Param("custno")String custno,@Param("role")String role) throws Exception;
    
    /**
     * 
     * @param fundAcc
     * @param tradeAcc
     * @return
     * @throws Exception
     */
    public OpenAccountDto queryAccoByfundAcc(@Param("fundAcc")String fundAcc,@Param("tradeAcc")String tradeAcc) throws Exception;
    
    /**
     * 分交易账号查询客户资料
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return OpenAccountDto
     */
    public SearchParam tradeaccoQry(SearchParam sp) throws Exception;
    
    /**
     * 获取账户复核记录数
     * @return List
     * @throws Exception
     */
    public TradeDto getTradeCount(TradeDto dto) throws Exception;
    /**
     * 资金复核记录条数
     */
    public CapitalCheckDto getCapitalCount(CapitalCheckDto dto)throws Exception;
    /**
     * 备案客户复核查询
     * 20101210 wangdw add
     */
    public List<BakCustDto> queryBakCustCheck(@Param("custno")String custno, @Param("optype")String optype,@Param("checkflag")String checkflag) throws Exception;
    /**
     * 查询客户风险等级
     * @param invprtp 
     */
    public List<RiskLevelDto> queryCustRiskLevel(RiskLevelDto dto)throws Exception;
    /**
     * added by zengxy 20131029
     * 客户经理 渠道关系 修改查询
     * @param custnm 渠道名称
     * @param custno 渠道代码
     * @param cltmno 客户经理号
     * @return List
     *
     */
    public List<BrokerInfoDto> brokerChannelModifyQry(@Param("custnm")String custnm,@Param("custno")String custno,@Param("cltmno")String cltmno) throws Exception;
    
    /**
     * 通过基金账号，证件号查询客户号
     * @param idno
     * @param fundacct
     * @return
     */
    public String getCustnoByIdnoAndFundAcco(@Param("idno")String idno, @Param("fundacct")String fundacct);
    
    /**
     * 查询折扣费率
     * @return
     */
    public float findDiscount(@Param("tradeacco")String tradeacco,@Param("fundacco")String fundacco,
    						  @Param("fundid")String fundid,@Param("apkind")String apkind,@Param("subAmt")String subAmt);
    
    /**
     * 查询所有销售机构
     * @return
     */
    public List<SeatDto> getSeats();
    
    /**
	 * 查询批量处理列表
	 * @param fundid
	 * @param begindate
	 * @param enddate
	 * @param checkst
	 * @param dsapkind
	 * @param opid
	 * @return
	 * @throws Exception
	 */
	public List<TradeCheckDto> queryBatchListPage(SearchParam sp) throws Exception;
}
