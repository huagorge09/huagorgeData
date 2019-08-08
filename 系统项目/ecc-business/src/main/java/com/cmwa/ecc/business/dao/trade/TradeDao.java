package com.cmwa.ecc.business.dao.trade;

import com.cmwa.ecc.business.entity.accountManger.ContractSignDto;
import com.cmwa.ecc.business.entity.trade.TradeCheckDto;
import com.cmwa.ecc.business.entity.trade.TradeDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;

@MybatisDao
public interface TradeDao {
	 public TradeDto simpSwitch(TradeDto dto);
	 public TradeDto tradeCheck(TradeCheckDto dto);
	 /**
	  * 电子合同签署
	  * @param dto
	  * @return
	  */
	 public ContractSignDto contractSign(ContractSignDto dto);
	 
	 /**
	  * 新增交易资料
	  * @param dto
	  */
	 public void insertTradeData(TradeDto dto);
	 
	 /**
	  * 认购
	  * @param dto
	  * @return
	  */
	 public void subscribe(TradeDto dto);
	 
	 /**
	  * 申购
	  * @param dto
	  * @return
	  */
	 public void purchase(TradeDto dto);
	 
	 /**
	  * 赎回
	  * @param dto
	  * @return
	  */
	 public void redeem(TradeDto dto);

	 /**
	  * 基金转换
	  * @param dto
	  * @return
	  */
	 public void convert(TradeDto dto);
	 
	 /**
	  * 设置分红方式
	  * @param dto
	  * @return
	  */
	 public void setMelonmd(TradeDto dto);
	 
	 /**
	 * 转托管转出
	 * @param dto
	 * @return
	 */
	public TradeDto managedSwitchOut(TradeDto dto);
	
	/**
	 * 转托管转入
	 * @param dto
	 * @return
	 */
	public TradeDto managedSwitchIn(TradeDto dto);
	 
	/**
    * 交易类驳回修改
    * @param TradeCheckDto dto,String modifytype
    * @return TradeCheckDto
    */
   public TradeCheckDto tradeModify(TradeCheckDto dto);
	
	/**
	 * 撤销交易申请
	 * @param dto
	 * @return
	 */
	public TradeDto cancel(TradeDto dto);
}
