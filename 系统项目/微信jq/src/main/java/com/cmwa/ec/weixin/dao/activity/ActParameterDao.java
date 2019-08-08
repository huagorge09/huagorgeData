package com.cmwa.ec.weixin.dao.activity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.query.facade.dto.user.UserTermsDto;
import com.cmwa.ec.weixin.dto.CustomerServiceDto;
import com.cmwa.ec.weixin.dto.ParameterDto;

public interface ActParameterDao {
	
	public ParameterDto getParameterDto(@Param("dto")ParameterDto dto);
	
	public int updateParameter(@Param("dto")ParameterDto dto);
	
	/***
	 * 查询参数表
	 * @param dto
	 * @return
	 */
	public List<ParameterDto> getParameter(@Param("dto")ParameterDto dto);

	public void updatePmcoByPmky(@Param("dto") ParameterDto dto);
	
	/**
	 * 
	 * 查询微信顾问信息
	 * @param sp
	 * @return
	 */
	public List<CustomerServiceDto> queryCustServiceInfo(@Param("map") Map map);
	
	public void  deleteUserTerms(@Param("fundId")String fundId,@Param("period")String period,@Param("cmfUserId")String cmfUserId);
	
	public void insertUserTerms(List<UserTermsDto> list);
	
	public void insertUserOperateLog(@Param("map")Map map);
}
