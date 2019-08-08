package com.cmwa.ecc.business.dao.bank;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.entity.bank.BankPayChannelVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface PayChannelDao {
	
	/**
	 * 获取支付渠道列表
	 * @param sp
	 * @return
	 */
	public List<BankPayChannelVo> queryPayChannelListPage(SearchParam sp);
	
	public int queryPayChannelListTotal(SearchParam sp);
	
	/**
	 * 新增
	 * @param payChannel
	 * @return
	 */
	public void insertPayChannel(BankPayChannelVo payChannel);
	
	/**
	 * 删除支付渠道
	 * @param delPayChannel
	 * @param bankNo
	 */
	public void delPayChannel(@Param("thirdChannel")String thirdChannel,@Param("bankNO")String bankNo);
	
	
	/**
	 * 修改支付渠道
	 * @param payChannelVo
	 */
	public void updatePayChannel(BankPayChannelVo payChannelVo);
}
