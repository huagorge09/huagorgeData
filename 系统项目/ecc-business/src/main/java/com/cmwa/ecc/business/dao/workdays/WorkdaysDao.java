package com.cmwa.ecc.business.dao.workdays;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.entity.workdays.WorkdaysVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;


/**
 * <p>Title: BankIdtpDao</p>
 * <p>Description: 银行管理工作日自动生成Dao接口.</p>
 * <p>Copyright: Copyright (c) 2004-2008</p>
 * <p>Company: Legion Technology</p>
 * @author 张彦军(rainer)
 * @version 1.0
 * @Create date: 2008/07/03
 * @Update date:
 * @todo:
 */
@MybatisDao
public interface WorkdaysDao {
    /**
     * 新增工作日
     * @param WorkdaysVo 工作日
     * @return WorkdaysVo
     * @throws Exception
     */
    public void create(WorkdaysVo workdaysVo) throws Exception;

    /**
     * 修改工作日
     * @param bankIdtpVo 工作日
     * @return WorkdaysVo
     * @throws Exception
     */
    public void update(WorkdaysVo workdaysVo) throws Exception;

    /**
     * 删除工作日
     * @param findid workdate
     * @return WorkdaysVo
     * @throws Exception
     */
    public void delete(@Param("operator")String operator, @Param("fundid")String fundid,
    		@Param("workdate")String workdate) throws Exception;

    /**
     * 获取单个工作日信息
     * @param findid workdate
     * @return WorkdaysVo 工作日
     * @throws Exception
     */
    public WorkdaysVo getWorkDate(@Param("fundid")String fundid, @Param("workdate")String workdate) throws
            Exception;

    /**
     * 获取所有工作日信息
     * @return List
     * @throws Exception
     */
    public List<WorkdaysVo> getWorkDateListPage(SearchParam sp) throws Exception;
    
    /**
     * 取当前工作日
     * @param apkind
     * @return
     * @throws Exception
     * WorkdaysVo
     */
    public void getWorkDateNow(SearchParam sp) throws Exception;
    
    /**
     * 根据当前日期取工作日
     * @param apkind
     * @param date
     * @param time
     * @return
     * @throws Exception
     * WorkdaysVo
     */
    public WorkdaysVo getWorkDateByDate(@Param("apkind")String apkind,@Param("date")String date, @Param("time")String time ) throws Exception;
    
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
    public void getOtherWorkDate(SearchParam sp) throws Exception;
    
    /**
     * 根据当前日期取工作日
     * @param date
     * @return
     * @throws Exception
     * WorkdaysDto
     */
    public WorkdaysVo getWorkDateByDT(SearchParam sp) throws Exception;
}
