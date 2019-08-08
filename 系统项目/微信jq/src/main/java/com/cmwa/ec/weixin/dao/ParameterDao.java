package com.cmwa.ec.weixin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.weixin.dto.ParameterDto;


/**
 * 业务推荐操作
 * @author zhangsk
 *
 */
public interface ParameterDao {
	
	public List<ParameterDto> list() ;
	
	public ParameterDto queryByAccessToken();
	
	public void updatePmco(@Param("dto") ParameterDto dto);
	
}
