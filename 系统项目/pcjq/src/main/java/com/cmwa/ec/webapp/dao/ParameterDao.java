package com.cmwa.ec.webapp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.base.util.MagicMap;
import com.cmwa.ec.query.facade.dto.user.UserTermsDto;
import com.cmwa.ec.webapp.dto.ParameterDto;

/**
 * 业务推荐操作
 * @author zhangsk
 *
 */
public interface ParameterDao {
	
	public List<ParameterDto> list() ;
	public ParameterDto queryByAccessToken();
	
	public void insertLog(MagicMap map);
	
	public void  deleteUserTerms(@Param("fundId")String fundId,@Param("period")String period,@Param("cmfUserId")String cmfUserId);
	
	public void insertUserTerms(List<UserTermsDto> list);
	
	public void insertUserOperateLog(@Param("map")Map map);
}
