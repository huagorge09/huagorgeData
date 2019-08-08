package com.cmwa.ec.weixin.manager.business;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.weixin.dto.ParameterDto;

public interface ParameterManager {

	public List list() ;
	
	
	public ParameterDto queryAccessToken();
	
	public void updateJdCardStock(@Param("dto") ParameterDto dto);
}
