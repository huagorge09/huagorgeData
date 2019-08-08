package com.cmwa.ec.weixin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.weixin.dto.CustomerServiceDto;
import com.cmwa.ec.weixin.dto.UserBaseInfoDto;

/**
 * @author ex-hezk
 *
 */
public interface CustServiceInfoDao {

	public CustomerServiceDto queryServiceInfoById(@Param("custserId") String custserId);
	
	
	public CustomerServiceDto queryDefaultCustServiceInfo();
	
	public String queryCustServiceByCmfUserId(@Param("cmfuserid") String cmfuserid);
	
	public int updateCustServiceByCmfUserId(@Param("cmfuserid") String cmfuserid,@Param("custserId") String custserId);
	
	public String queryMobileNoByCmfUserId(@Param("cmfuserid") String cmfuserid);
	
	public List<UserBaseInfoDto> queryUserBaseList(@Param("userType") String userType);
}
