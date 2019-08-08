package com.cmwa.ecc.business.service.account;

import java.util.List;
import com.cmwa.ecc.business.entity.accountManger.AccountDto;
import com.cmwa.ecc.business.entity.accountManger.BakCustDto;
import com.cmwa.ecc.business.entity.accountManger.BrokerDto;
import com.cmwa.ecc.business.entity.accountManger.BrokerInfoDto;
import com.cmwa.ecc.business.entity.accountManger.CapitalCheckDto;
import com.cmwa.ecc.business.entity.accountManger.FundBalanceDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.RiskLevelDto;
import com.cmwa.ecc.business.entity.accountManger.SeatDto;
import com.cmwa.ecc.business.entity.accountManger.ValidateDto;
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.entity.fundinfo.FundInfoVo;
import com.cmwa.ecc.business.entity.trade.TradeCheckDto;
import com.cmwa.ecc.business.entity.trade.TradeDto;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * <p>Title:  直销柜台系统</p>
 * <p>Description:查询的Delegate层</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company:  </p>
 * @author zhangq
 * @version 1.0
 * @CreateDate: 2009-12-22
 * @UpdateDate:
 */
public interface QueryManager {

    /**
     * 通过客户号查询客户资料
     * @param custNo 客户号
     * @return OpenAccountDto
     */
    public OpenAccountDto queryAccoByCust(String custNo) ;

    /**
     * 分基金账号查询客户资料
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @param accountType 账户类型
     * @return OpenAccountDto
     */
    public OpenAccountDto fundaccoQry(String tradeacco, String fundacco, String accountType);
    
    /**
     * 个人/机构资料查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @param accountType 账户类型
     * @return OpenAccountDto
     */
	public OpenAccountDto accountQryByAcco(String tradeacco, String fundacco, String accountType);
    
    /**
     * 银行资料查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @param accountType 账户类型
     * @return OpenAccountDto
     */
    public OpenAccountDto bankQryByAcco(String tradeacco, String fundacco, String accountType);
    
    /**
     * 收款账户查询
     * @return List(map)
     */
    public List getReceiveAccoList();
    
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
    public Page<TradeCheckDto> tradeCheckQryListPage(SearchParam sp);
    
    
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
	public Page<TradeCheckDto> queryBatchListPage(SearchParam sp);
    
    /**
     * 交易类复核记录数查询
     * @param sp
     * @return
     * @throws Exception
     */
    public TradeCheckDto getTradeCheckCount() throws Exception;
    
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
    public TradeCheckDto tradeCheckDetailQry(String serialno);
    
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
    public List<CapitalCheckDto> capitalCheckQry(String tradeacco,String capitalno,String capitaltype,String checkst,
			String begindate,String enddate,String operatorId,String operatorType);
    
    /**
     * 资金类复核明细查询
     * @param capitalno 资金流水号
     * @return CapitalCheckDto
     */
    public CapitalCheckDto capitalCheckDetailQry(String capitalno);
    
    /**
     * 查询是否需要授权
     * @param permissionId,apkind
     * @return boolean
     */
    public SearchParam queryPermission(SearchParam sp);
    
    /**
     *授权校验
     * @param String checkno,String checkpwd
     * @return boolean
     */
    public SearchParam checkPermission(SearchParam sp);
    
    /**
     * 资金存入查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @param serialno 申请流水号
     * @param checkst 存入状态
     * @param apdt 申请时间
     * @return List
     */
    public List<CapitalCheckDto> storeQry(String tradeacco,String fundacco,String serialno,String checkst,String apdt);

    /**
     * 基金份额查询
     * @param custno 客户编号
     * @param tradeacco 交易账号
     * @param fundid 基金代码
     */
	public FundBalanceDto getFundBalanceInfo(FundBalanceDto dto);
	
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
    public List<ValidateDto> validateQry(String tradeacco,String fundacco,String idtp,String idno,String idnm,
    		String begindate,String enddate,String invtp,String operatorType);
    
    /**
     * 获取银行（直销柜台）
     * return List
     */	
	public List<BankBnkbaseVo> getDSBanksList();
	
	/**
     * 交易撤单查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     * 2010-02-04 xul 交易撤单、账户类撤销中，输入交易账号或基金账号后查询出的结果为当前操作员经办的交易流水
     */
    public Page<TradeDto> cancelQryListPage(SearchParam sp);

    /**
     * 客户经理修改查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     */
    public List<BrokerInfoDto> brokerModifyQry(String tradeacco,String fundacco,String custnm,String cltmno,String custno);

	/**
     * 交易撤单查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     */
	public List<TradeDto> cancelTradeDetailQry(String tradeacco, String fundacco);
	
	/**
     * 客户号查询客户经理
     * @param custno 客户号
     * @return List
     */
	public List<BrokerInfoDto> brokerQryByCustno(String custno);
	
	/**
     * 分红方式查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     */
    public Page<TradeDto> melonListQry(SearchParam sp);
    
    /**
     * 资金撤单查询（客户信息）
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     * 资金撤单中，输入交易账号或基金账号后查询出的结果为当前操作员经办的交易流水
     */
    public List<TradeDto> capitalCancelQry(String tradeacco,String fundacco,String opid,String firstCustGroup,String secondCustGroup);
    
    /**
     * 客户经理维护查询
     * @param brokerno 交易账号
     * @return List
     */
    public List<BrokerDto> brokerQry(String brokerno);
    
    /**
     * 客户风险等级查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     */
    public List<RiskLevelDto> risklevelQry(String tradeacco,String fundacco,String risklevel,String firstGroup,String secondGroup);
    
    /**
     * 客户经理复核查询
     * @param brokerno 客户经理编号,operatorId操作员编号
     * @return List
     */
    public List<BrokerDto> brokerCheckQry(String brokerno,String operatorId) ;
    
    //----------------------------------------------------------------------------------
    /**
     * 账户类撤单查询
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return List
     * 2010-02-04 xul 交易撤单、账户类撤销中，输入交易账号或基金账号后查询出的结果为当前操作员经办的交易流水
     */
    public List<TradeDto> accountCancelQry(String tradeacco,String fundacco,String operatorid);
    
    public TradeDto accountCancelDetailQry(String serialno,String operatorid);


    
    /**
     * 账户类复核查询
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
    public List<TradeDto> accountCheckQry(String tradeacco,String fundacco,String dsapkind,String checkst,String type,String operatorid);
    
    /**
     * 账户类复核明细查询
     * @param serialno 流水号
     * @return AccountCheckDto
     */
    public OpenAccountDto accountCheckDetailQry(String serialno);
    
    public List<OpenAccountDto> accountModifyQry(String custType,String fundacco,String option,String custsimpnm,String instrepcode);
    
    /**
     * 分交易账号查询客户资料
     * @param tradeacco 交易账号
     * @param fundacco 基金账号
     * @return OpenAccountDto
     */
    public OpenAccountDto tradeaccoQry(String custno,String tradeacco, String fundacco);
    public OpenAccountDto tradeaccoQry(String tradeacco, String fundacco);
    
    public List<BakCustDto> queryBakCust(String custNo,String role) ;
    
    public OpenAccountDto queryAccoByfundAcc(String fundAcc,String tradeAcc);
    /**
     * 获取账户复核记录数
     * @return List
     * @throws Exception
     */
    public TradeDto getTradeCount(String tradeacco, String fundacco,String dsapkind, String checkst);
    /**
     * 资金复核记录条数
     */
    public CapitalCheckDto getCapitalCount(String tradeacco,String capitalno,String capitaltype,String checkst,
			String begindate,String enddate,String operatorId,String operatorType);
    /**
     * 备案客户复核查询
     * 20101210 wangdw add
     */
    public List<BakCustDto> queryBakCustCheck(String custno, String optype,String checkflag);
    /**
     * 查询客户风险等级
     * @param invprtp 
     */
    public  List<RiskLevelDto> queryCustRiskLevel(String custno,String fundacco,String invnm,String invtp,
			String begindate,String enddate,String risklevel, String invprtp);
    /**
     * added by zengxy 20131029
     * 客户经理 渠道关系 修改查询
     * @param custnm 渠道名称
     * @param custno 渠道代码
     * @param cltmno 客户经理号
     * @return List
     *
     */
    public List<BrokerInfoDto> brokerChannelModifyQry(String custnm,String custno,String cltmno);
    
    /**
     * 查询账户信息
     * @param idno
     * @param fundacct
     * @return
     */
    public AccountDto queryCustInfo(String idno, 
    		String fundacct);
    
    /**
     * 查询折扣费率
     * @return
     */
    public float findDiscount(String tradeacco,String fundacco,String fundid,String apkind,String subAmt);
    
    /**
     * 直销系统获取所有基金列表
     * @param type
     * @param tano
     * @return
     */
    public List<FundInfoVo> getAllSubFundsArray(String type,String tano);
    
    /**
     * 查询所有销售机构
     * @return
     */
    public List<SeatDto> getSeats();
}
