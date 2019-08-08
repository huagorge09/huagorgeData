package com.cmwa.ec.weixin.dao.activity;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.weixin.dto.ShortUrlDto;

public interface ShortUrlDao {

	/**
	 * 通过键查询key
	 * @param keys
	 * @return
	 */
	public ShortUrlDto queryShortUrlByPrimaryKey(@Param("keys") String keys);
	
	/**
	 * 添加短连接映射
	 * @param dto
	 * @return
	 */
	public int insertShortUrl(@Param("param") ShortUrlDto dto);
}
