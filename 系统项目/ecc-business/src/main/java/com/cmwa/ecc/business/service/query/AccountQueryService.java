/**
* @Title: AccountQueryService.java  
* @author ex-wuh2  
* @date 2018年6月7日  
* @version V1.0  
 */
package com.cmwa.ecc.business.service.query;

import com.cmwa.ecc.business.entity.query.AcctAckClearHisVo;
import com.cmwa.ecc.business.entity.query.AcctAckClearVo;
import com.cmwa.ecc.business.entity.query.AcctAckTodayVo;
import com.cmwa.ecc.business.entity.query.AcctAppHisVo;
import com.cmwa.ecc.business.entity.query.AcctAppModifyVo;
import com.cmwa.ecc.business.entity.query.AcctAppTodayVo;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * @author ex-wuh2
 *
 */
public interface AccountQueryService {
	
	

    /**
     * 查询当前账户申请流水
     */
    public  Page<AcctAppTodayVo> queryAcctAppTodayListPage(SearchParam sp) throws Exception;

    /**
     * 查询TA账户确认流水
     */
    public  Page<AcctAckTodayVo> queryAcctAckTodayListPage(SearchParam sp) throws Exception;

    /**
     * 查询二级清算账户确认流水
     */
    public  Page<AcctAckClearVo> queryAcctAckClearListPage(SearchParam sp) throws Exception;

    /**
     * 查询历史账户申请流水
     */
    public  Page<AcctAppHisVo> queryAcctAppHisListPage(SearchParam sp) throws Exception;

    /**
     * 查询历史二级清算账户确认流
     */
    public  Page<AcctAckClearHisVo> queryAcctAckClearHisListPage(SearchParam sp) throws Exception;
    /**
     * 客户账户资料修改流水查询
     */
    public  Page<AcctAppModifyVo> queryAcctModifyListPage(SearchParam sp)throws Exception;

	
	
}
