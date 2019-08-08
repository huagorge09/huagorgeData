package com.cmwa.ecc.business.service.workdays;

import com.cmwa.ecc.business.entity.workdays.WorkdaysVo;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * <p>Title: BankIdtpDao</p>
 * <p>Description: 银行管理工作日自动生成Service接口.</p>
 * <p>Copyright: Copyright (c) 2004-2008</p>
 * <p>Company: Legion Technology</p>
 * @version 1.0
 * @Create date: 2008/07/08
 * @Update date:
 * @todo:
 */
public interface WorkdaysService {
	 /**
     * 新增工作日
     * @param WorkdaysVo 工作日
     * @return WorkdaysVo
     * @throws Exception
     */
    public WorkdaysVo create(String operator, WorkdaysVo workdaysVo) throws Exception;

    /**
     * 修改工作日
     * @param bankIdtpVo 工作日
     * @return WorkdaysVo
     * @throws Exception
     */
    public WorkdaysVo update(String operator, WorkdaysVo workdaysVo) throws Exception;

    /**
     * 删除工作日
     * @param findid workdate
     * @return WorkdaysVo
     * @throws Exception
     */
    public WorkdaysVo delete(String operator, String fundid, String workdate) throws Exception;

    /**
     * 获取单个工作日信息
     * @param findid workdate
     * @return WorkdaysVo 工作日
     * @throws Exception
     */
    public WorkdaysVo getWorkDate(String fundid, String workdate) throws
            Exception;

    /**
     * 获取所有工作日信息
     * @return List
     * @throws Exception
     */
    public Page<WorkdaysVo> getWorkDateListPage(SearchParam sp) throws Exception;
    
    
    /**
     * 取当前工作日
     * @param apkind
     * @return
     * @throws Exception
     * WorkdaysVo
     */
    public WorkdaysVo getWorkDateNow( String apkind ) throws Exception;
    
    /**
     * 根据当前日期取工作日
     * @param apkind
     * @param date
     * @param time
     * @return
     * @throws Exception
     * WorkdaysVo
     */
    public WorkdaysVo getWorkDateByDate( String apkind,String date, String time ) throws Exception;
    
    /**
     * 取工作日
     * @param now		当前时间
     * @param datenum	数量（正整数）
     * @param type		-1，往前；0,当前；1,往后
     * @param fundid	基金代码
     * @return
     * @throws Exception
     * WorkdaysVo
     */
    public WorkdaysVo getOtherWorkDate( String now,String datenum, String type, String fundid ) throws Exception;
    
}
