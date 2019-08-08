package com.cmwa.ec.weixin.dao.activity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.weixin.dto.SinoActAwardDto;

/**
 * 华安保险引流活动奖品dao.
 * @author ex-hezk
 *
 */
public interface SinoActAwardDao {

	/**
	 * 添加
	 * @param data
	 * @return
	 */
	public int insert(SinoActAwardDto data);
	
	
	/**
	 * 回写状态 
	 * @param dto
	 * @return
	 */
	public int rewriteStatus(SinoActAwardDto dto);
	
	
	/**
	 * 
	 * 回写错误状态
	 * @param dto
	 * @return
	 */
	public int rewriteStatusError(SinoActAwardDto dto);
	
	/**
	 * 根据身份证查询是否已经发放过奖品
	 * @param dto
	 * @return
	 */
	public int countAwardInfoByIdCard(String idCard);
	
	
	/**
	 * 查询所有异常的奖品信息
	 * @return
	 */
	public List<SinoActAwardDto> queryExceptionActAwardInfo();


	/**
	 * 回写新获取的信息到异常列内
	 * @param dto
	 * @param reqStreamId
	 * @return
	 */
	public int updateExceptionInfo(@Param("dto")SinoActAwardDto dto, @Param("oldReqStreamId")String reqStreamId);
	
	/**
	 * 查询用户是否已经发送短信
	 * @param dto
	 * @return
	 */
	public int querySendAwardInfoByIdCardAndUserName(@Param("dto") SinoActAwardDto dto);
}
