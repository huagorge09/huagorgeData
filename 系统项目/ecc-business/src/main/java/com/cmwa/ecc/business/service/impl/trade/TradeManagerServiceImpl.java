package com.cmwa.ecc.business.service.impl.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.trade.TradeDao;
import com.cmwa.ecc.business.entity.accountManger.ContractSignDto;
import com.cmwa.ecc.business.entity.trade.TradeCheckDto;
import com.cmwa.ecc.business.entity.trade.TradeDto;
import com.cmwa.ecc.business.service.trade.TradeManagerService;

/**
 * 
 * @author ex-liuy
 *
 */
@Service
public class TradeManagerServiceImpl implements TradeManagerService{
	
	@Autowired
	private TradeDao tradeDao;
	
	@Override
	public TradeDto simpSwitch(TradeDto dto) {
		tradeDao.simpSwitch(dto);
		return dto;
	}

	@Override
	public TradeCheckDto tradeCheck(TradeCheckDto returnChkDto) {
		tradeDao.tradeCheck(returnChkDto);
		return returnChkDto;
	}

	@Override
	public ContractSignDto contractSign(ContractSignDto dto) {
		ContractSignDto signDto = tradeDao.contractSign(dto);
		return signDto;
	}

	@Override
	public void insertTradeData(TradeDto dto) {
		tradeDao.insertTradeData(dto);
	}

	@Override
	public void subscribe(TradeDto dto) {
		tradeDao.subscribe(dto);
	}

	@Override
	public void purchase(TradeDto dto) {
		tradeDao.purchase(dto);
	}

	@Override
	public void redeem(TradeDto dto) {
		tradeDao.redeem(dto);
	}

	@Override
	public void convert(TradeDto dto) {
		tradeDao.convert(dto);
	}

	@Override
	public void setMelonmd(TradeDto dto) {
		tradeDao.setMelonmd(dto);
	}

	@Override
	public TradeDto managedSwitchOut(TradeDto dto) {
		return tradeDao.managedSwitchOut(dto);
	}

	@Override
	public TradeDto managedSwitchIn(TradeDto dto) {
		return tradeDao.managedSwitchIn(dto);
	}

	@Override
	public TradeCheckDto tradeModify(TradeCheckDto dto) {
		try {
			tradeDao.tradeModify(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public TradeDto cancel(TradeDto dto) {
		try {
			tradeDao.cancel(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
}
