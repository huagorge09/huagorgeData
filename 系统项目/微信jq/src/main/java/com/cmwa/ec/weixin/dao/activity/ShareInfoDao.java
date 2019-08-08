package com.cmwa.ec.weixin.dao.activity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.weixin.dto.ShareInfoDto;

/**
 * 微信活动-用户分享关系
 * @author ex-chenhq
 *
 */
public interface ShareInfoDao {

	public List<Map<String,Object>> queryUserRelation(@Param("openId") String openid);
	
	public List<Map<String,Object>> queryUserRelationLimit(@Param("openId") String openid);
	
	/**
	 * 获取用户助力信息列表
	 * 分页
	 * @param map
	 * @return
	 */
	public List<ShareInfoDto> queryShareInfoList(@Param("map") Map map);
	
}
