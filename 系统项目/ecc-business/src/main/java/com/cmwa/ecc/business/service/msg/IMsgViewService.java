package com.cmwa.ecc.business.service.msg;

import java.util.List;

import com.cmwa.ecc.business.commonVo.MsgViewVo;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface IMsgViewService{
	
	/**
	 * @author: chenbq
	 * @date: 2016年9月20日 下午1:40:42
	 * @desc: 分页查询消息
	 */
	public Page<MsgViewVo> msgViewListPage(SearchParam sp);
	
	/**
	 * @author: chenbq
	 * @date: 2016年9月20日 下午2:05:05
	 * @desc: 查询最新的几条消息
	 */
	public Page<MsgViewVo> newestListPage(SearchParam sp);
	
	/**
	 * @author: chenbq
	 * @date: 2016年9月29日 下午1:49:33
	 * @desc: 查询未读消息数量
	 */
	public int unreadMsgCount();
	
	/**
	 * @author: chenbq
	 * @date: 2016年9月25日 下午2:11:18
	 * @desc: 查询有标志的几条消息
	 */
	public Page<MsgViewVo> flagListPage(SearchParam sp);
	
	/**
	 * @author: chenbq
	 * @date: 2016年9月20日 下午1:51:53
	 * @desc: 更新消息状态为已经读
	 */
	public void updateForView(String msgId);
	
	/**
	 * @author: chenbq
	 * @date: 2016年9月20日 下午1:55:18
	 * @desc: 为消息打标签,同时置为已读
	 */
	public void updateForFlag(String msgId, String flag);
	
	/**
	 * @author: chenbq
	 * @date: 2016年9月20日 下午1:57:47
	 * @desc: 删除消息
	 */
	public void deleteMsg(String msgId);
	
	/**
	 * @author: chenbq
	 * @date: 2016年9月20日 下午2:03:23
	 * @desc: 批量删除消息
	 */
	public void batchDeleteMsg(List<String> msgIdList);
	
	/**
	 * @author: chenbq
	 * @date: 2016年9月28日 上午10:46:39
	 * @desc: 增加消息
	 */
	public void addMsg(MsgViewVo msg);
	
	/**
	 * @author: chenbq
	 * @date: 2016年9月28日 上午10:48:01
	 * @desc: 批量增加消息
	 */
	public void batchAddMsg(List<MsgViewVo> msgList);
}
