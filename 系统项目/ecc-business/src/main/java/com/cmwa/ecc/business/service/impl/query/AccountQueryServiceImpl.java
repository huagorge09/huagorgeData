package com.cmwa.ecc.business.service.impl.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.query.AccountQueryDao;
import com.cmwa.ecc.business.entity.query.AcctAckClearHisVo;
import com.cmwa.ecc.business.entity.query.AcctAckClearVo;
import com.cmwa.ecc.business.entity.query.AcctAckTodayVo;
import com.cmwa.ecc.business.entity.query.AcctAppHisVo;
import com.cmwa.ecc.business.entity.query.AcctAppModifyVo;
import com.cmwa.ecc.business.entity.query.AcctAppTodayVo;
import com.cmwa.ecc.business.service.query.AccountQueryService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

import oracle.net.aso.q;

/**
 * @author ex-wuh2
 */
@Service
public class AccountQueryServiceImpl implements AccountQueryService {
	
	@Autowired
	private AccountQueryDao accountQueryDao;
	
	/* 当前账户查询  */

	@Override
	public Page<AcctAppTodayVo> queryAcctAppTodayListPage(SearchParam sp) throws Exception {
		String appDate = null;
		if(sp.getSp().get("appDate") != null) {
			appDate =  sp.getSp().get("appDate").toString().replace("-", "");
			sp.getSp().put("appDate", appDate);
		}
		List<AcctAppTodayVo> queryAcctAppTodayListPage =accountQueryDao.queryAcctAppTodayListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(accountQueryDao.queryCurrentAccListTotal(sp));
		return Page.create(queryAcctAppTodayListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}
	
	/* TA账户查询 */

	@Override
	public Page<AcctAckTodayVo> queryAcctAckTodayListPage(SearchParam sp) throws Exception {
		List<AcctAckTodayVo> queryAcctAckTodayListPage =accountQueryDao.queryAcctAckTodayListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(accountQueryDao.queryAcctAckTodayListTotal(sp));
		return Page.create(queryAcctAckTodayListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}
	
	/*二级清算账户查询*/
	
	@Override
	public Page<AcctAckClearVo> queryAcctAckClearListPage(SearchParam sp) throws Exception {
		List<AcctAckClearVo> queryAcctAckClearListPage =accountQueryDao.queryAcctAckClearListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(accountQueryDao.queryAcctAckClearListTotal(sp));
		return Page.create(queryAcctAckClearListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}

	/*历史账户查询*/
	
	@Override
	public Page<AcctAppHisVo> queryAcctAppHisListPage(SearchParam sp) throws Exception {
		sp = changeDatePattern(sp);
		List<AcctAppHisVo> queryAcctAppHisListPage =accountQueryDao.queryAcctAppHisListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(accountQueryDao.queryAcctAppHisListTotal(sp));
		return Page.create(queryAcctAppHisListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}
	
	/*历史二级账户查询*/
	
	@Override
	public Page<AcctAckClearHisVo> queryAcctAckClearHisListPage(SearchParam sp) throws Exception {
		sp = changeDatePattern(sp);
		List<AcctAckClearHisVo> queryAcctAckClearHisListPage =accountQueryDao.queryAcctAckClearHisListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(accountQueryDao.queryAcctAckClearHisListTotal(sp));
		return Page.create(queryAcctAckClearHisListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}

	/*客户账户修改查询*/
	
	@Override
	public Page<AcctAppModifyVo> queryAcctModifyListPage(SearchParam sp) throws Exception {
		sp = changeDatePattern(sp);
		List<AcctAppModifyVo> queryAcctModifyListPage =accountQueryDao.queryAcctModifyListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(accountQueryDao.queryAcctModifyListTotal(sp));
		return Page.create(queryAcctModifyListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}
	
	/* 起止日期更换格式 */
	public SearchParam  changeDatePattern(SearchParam sp) {
		//起止日期更换格式
		String startDate = null;
		String endDate = null;
		if(sp.getSp().get("startDate") != null) {
			startDate =  sp.getSp().get("startDate").toString().replace("-", "");
			sp.getSp().put("startDate", startDate);
		}
		if(sp.getSp().get("endDate") != null) {
			endDate =  sp.getSp().get("endDate").toString().replace("-", "");
			sp.getSp().put("endDate", endDate);
		}
		return sp;
	}

}
