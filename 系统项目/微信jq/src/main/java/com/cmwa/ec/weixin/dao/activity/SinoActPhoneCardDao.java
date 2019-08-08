package com.cmwa.ec.weixin.dao.activity;

import java.util.List;

import com.cmwa.ec.weixin.dto.SinoActPhoneCardDto;

public interface SinoActPhoneCardDao {

	public int countByIdCard(String idCard);
	
	public void insertRecord(SinoActPhoneCardDto dto);
	
	public List<SinoActPhoneCardDto> queryInProcess();
	
	public void updateQueryResult(SinoActPhoneCardDto dto);
	
}
