package com.cmwa.ecc.business.service.schedule;

import java.util.List;

import org.quartz.SchedulerException;

import com.cmwa.ecc.business.commonVo.ScheduleVo;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 
 * 定时任务操作相关
 * 
 * @author ex-luoxy@cmfchina.com
 * 
 */

public interface ScheduleService {
	/**
	 * stop Schedule
	 * 
	 * @param jobName
	 *            Job Name
	 * @param jobGroup
	 *            Job Group
	 * @throws SchedulerException
	 */
	void stopSchedule(String jobName, String jobGroup) throws SchedulerException;

	/**
	 * restart Schedule
	 * 
	 * @param jobName
	 *            Job Name
	 * @param jobGroup
	 *            Job Group
	 * @throws SchedulerException
	 */
	void restartSchedule(String jobName, String jobGroup) throws SchedulerException;

	/**
	 * run Schedule once right now
	 * 
	 * @param jobName
	 *            Job Name
	 * @param jobGroup
	 *            Job Group
	 * @throws SchedulerException
	 */
	void runSchedule(String jobName, String jobGroup) throws SchedulerException;

	/**
	 * update job cron,the job will run once right now
	 * 
	 * @param jobName
	 *            Job Name
	 * @param jobGroup
	 *            Job Group
	 * @param cron
	 * @throws SchedulerException
	 */
	void updateCron(String taskId , String jobName, String jobGroup, String cron) throws SchedulerException;

	/**
	 * record job's last running, write the result
	 * 
	 * @param jobId
	 *            the job's id
	 * @param result
	 *            last running result
	 */
	void writeLastRunnig(String jobId, String result);

	/**
	 * query all Schedule
	 * 
	 * @return
	 */
	List<ScheduleVo> queryAllSchedule();

	/**
	 * 定时任务数据分页
	 * @author ex-weicb
	 * @createDate 2016年6月27日 下午12:14:19
	 * @param sp
	 * @return
	 */
	public Page<ScheduleVo> scheduleListPage(SearchParam sp);

	/**
	 * 修改定时器状态
	 * @author ex-weicb
	 * @createDate 2016年6月27日 下午1:19:15
	 * @param schedule
	 */
	public void updateScheduleStatus(ScheduleVo schedule);
}
