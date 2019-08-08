package com.cmwa.ecc.business.dao.msg;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.commonVo.MsgViewVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao // 注解为mybatis的业务接口
public interface IMsgViewDao{
	
	public List<MsgViewVo> msgViewListPage(SearchParam sp);
	
	public List<MsgViewVo> newestListPage(SearchParam sp);
	
	public List<MsgViewVo> flagListPage(SearchParam sp);
	
	public void updateForView(@Param("msgId") String msgId);
	
	public void updateForFlag(@Param("msgId") String msgId, @Param("flag") String flag);
	
	public void deleteMsg(@Param("msgId") String msgId);
	
	public void batchDeleteMsg(@Param("msgIdList") List<String> msgIdList);
	
	public void batchAddMsg(@Param("msgList") List<MsgViewVo> msgList);
	
	public void addMsg(@Param("msg") MsgViewVo msg);
	
	public int unreadMsgCount(@Param("empId") String empId);
}
