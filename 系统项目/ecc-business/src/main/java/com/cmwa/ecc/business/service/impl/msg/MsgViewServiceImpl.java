package com.cmwa.ecc.business.service.impl.msg;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.MsgViewVo;
import com.cmwa.ecc.business.dao.msg.IMsgViewDao;
import com.cmwa.ecc.business.service.msg.IMsgViewService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

@Service
public class MsgViewServiceImpl  implements IMsgViewService{
	@Resource
	private IMsgViewDao msgViewDao;
	
	public Page<MsgViewVo> msgViewListPage(SearchParam sp){
		Employee employee = SessionUtils.getEmployee();// 当前的登录用户
		sp.getSp().put("empId", employee.getID());
		List<MsgViewVo> items = msgViewDao.msgViewListPage(sp);
		return Page.create(items, sp.getStart(), sp.getLimit(), sp.getTotal());
	}


	@Override
	public Page<MsgViewVo> newestListPage(SearchParam sp) {
		Employee employee = SessionUtils.getEmployee();// 当前的登录用户
		sp.getSp().put("empId", employee.getID());
		List<MsgViewVo> items = msgViewDao.newestListPage(sp);
		return Page.create(items, sp.getStart(), sp.getLimit(), sp.getTotal());
	}
	
	@Override
	public Page<MsgViewVo> flagListPage(SearchParam sp) {
		Employee employee = SessionUtils.getEmployee();// 当前的登录用户
		sp.getSp().put("empId", employee.getID());
		List<MsgViewVo> items = msgViewDao.flagListPage(sp);
		return Page.create(items, sp.getStart(), sp.getLimit(), sp.getTotal());
	}

	@Override
	public void updateForView(String msgId) {
		msgViewDao.updateForView(msgId);
	}

	@Override
	public void updateForFlag(String msgId, String flag) {
		msgViewDao.updateForFlag(msgId, flag);
		msgViewDao.updateForView(msgId);
	}

	@Override
	public void deleteMsg(String msgId) {
		msgViewDao.deleteMsg(msgId);
	}

	@Override
	public void batchDeleteMsg(List<String> msgIdList) {
		msgViewDao.batchDeleteMsg(msgIdList);
	}


	@Override
	public void addMsg(MsgViewVo msg) {
		msgViewDao.addMsg(msg);
	}


	@Override
	public void batchAddMsg(List<MsgViewVo> msgList) {
		msgViewDao.batchAddMsg(msgList);
	}


	@Override
	public int unreadMsgCount() {
		Employee employee = SessionUtils.getEmployee();// 当前的登录用户
		String empId = employee.getID();
		return msgViewDao.unreadMsgCount(empId);
	}

}
