/**
* @Title: AccountQueryDao.java  
* @author ex-wuh2  
* @date 2018年6月7日  
* @version V1.0  
 */
package com.cmwa.ecc.business.dao.query;

import java.util.List;

import com.cmwa.ecc.business.entity.query.AcctAckClearHisVo;
import com.cmwa.ecc.business.entity.query.AcctAckClearVo;
import com.cmwa.ecc.business.entity.query.AcctAckTodayVo;
import com.cmwa.ecc.business.entity.query.AcctAppHisVo;
import com.cmwa.ecc.business.entity.query.AcctAppModifyVo;
import com.cmwa.ecc.business.entity.query.AcctAppTodayVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * @author ex-wuh2
 *
 */
@MybatisDao
public interface AccountQueryDao {

    /**
     * 查询当前账户申请流水
     */
	public  List<AcctAppTodayVo> queryAcctAppTodayListPage(SearchParam sp) throws Exception;
   
	/*
	 * 当前账户导出方法
	 *  */
	public  List<AcctAppTodayVo> queryAcctAppTodayList(SearchParam sp) throws Exception;

	/*
	 * 查询当前账户申请流水总数
     */
    
    public  int queryCurrentAccListTotal(SearchParam sp);
    
    /**
     * 查询TA账户确认流水
     */
    public  List<AcctAckTodayVo> queryAcctAckTodayListPage(SearchParam sp) throws Exception;
    /**
   	 * 查询TA账户申请流水总数
     */
    public  int queryAcctAckTodayListTotal(SearchParam sp);

    /**
     * 查询二级清算账户确认流水
     */
    public  List<AcctAckClearVo> queryAcctAckClearListPage(SearchParam sp) throws Exception;
    /**
   	 * 查询二级清算账户申请流水总数
     */
  
    public  int queryAcctAckClearListTotal(SearchParam sp);
    /**
     * 查询历史账户申请流水
     */
    
    public  List<AcctAppHisVo> queryAcctAppHisListPage(SearchParam sp) throws Exception;

    /*
	 * 历史账户导出方法
	 *  */
    public  List<AcctAppHisVo> queryAcctAppHisList(SearchParam sp) throws Exception;

    /**
   	 * 查询历史账户申请流水总数
     */
    public  int queryAcctAppHisListTotal(SearchParam sp);
    
    /**
     * 查询历史二级清算账户确认流
     */
    public  List<AcctAckClearHisVo> queryAcctAckClearHisListPage(SearchParam sp) throws Exception;
    
    /*
	 * 历史二级账户导出方法
	 *  */
    public  List<AcctAckClearHisVo> queryAcctAckClearHisList(SearchParam sp) throws Exception;
   
    /**
   	 * 查询历史二级账户申请流水总数
     */
    public  int queryAcctAckClearHisListTotal(SearchParam sp);
    
    /**
     * 客户账户资料修改流水查询
     */
    public  List<AcctAppModifyVo> queryAcctModifyListPage(SearchParam sp)throws Exception;

    /*	
     * 查询客户账户修改流水总数
     * */
    public  int queryAcctModifyListTotal(SearchParam sp);

}
