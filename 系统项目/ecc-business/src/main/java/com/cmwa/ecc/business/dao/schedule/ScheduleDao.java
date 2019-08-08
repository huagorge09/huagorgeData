package com.cmwa.ecc.business.dao.schedule;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.commonVo.ScheduleVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface ScheduleDao {

	ScheduleVo findById(String taskId);

	/**
	 * 记录job运行状态
	 * 
	 * @param taskId
	 *            jobId
	 * @param result
	 *            运行结果
	 */
	void updateRunningResult(@Param("taskId") String taskId, @Param("result") String result);

	List<ScheduleVo> queryAllSchedule();
	
	/**
	 * 定时任务数据分页
	 * @author ex-weicb
	 * @createDate 2016年6月27日 下午12:16:34
	 * @param param
	 * @return
	 */
	public List<ScheduleVo> scheduleListPage(SearchParam param);

	/**
	 * 更新定时任务状态
	 * @author ex-weicb
	 * @createDate 2016年6月27日 下午1:38:29
	 * @param schedule
	 * @return
	 */
	public void changeState(ScheduleVo schedule);
	
	 /** 修改运行周期
	 * 
	 * @param schedule
	 * @return
	 */
	void updateCron(ScheduleVo schedule);

}
