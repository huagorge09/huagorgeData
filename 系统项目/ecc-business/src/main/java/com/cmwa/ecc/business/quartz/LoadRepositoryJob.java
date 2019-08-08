package com.cmwa.ecc.business.quartz;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.cmwa.ecc.business.commonVo.ScheduleVo;
import com.cmwa.ecc.business.service.schedule.ScheduleService;
import com.cmwa.ecc.business.utils.SysConf;

/**
 * 
 * 加载所有的任务 {@link SysScheduleEntity#getValid()} 为 <B>0</B> 就不加载任务 </br>
 * 
 * @author chenbq@cmfchina.com
 * @see QuartzJobBasic
 */
public class LoadRepositoryJob {

	static final String JOB_DATA_MAP_KEY_DEFAULT = "cmpScheduleJob";

	static final String RUN_SCHEDULE_OFF = "OFF";

	private static final String STATE_N = "0";

	private static final Log log = LogFactory.getLog(LoadRepositoryJob.class);

	@Autowired(required = false)
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private ScheduleService scheduleService;

	public void init() {
		// 如果配置为OFF，则不做初始化
		if (RUN_SCHEDULE_OFF.equals(SysConf.get("RUN_SCHEDULE")))
			return;
		Scheduler scheduler = schedulerFactoryBean.getScheduler();

		List<ScheduleVo> jobList = scheduleService.queryAllSchedule();

		for (ScheduleVo job : jobList) {
			if (STATE_N.equals(job.getState())) {
				continue;
			}
			try {
				TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getTriggerGroup());

				// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
				CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

				// 不存在，创建一个
				if (null == trigger) {
					JobDetail jobDetail = JobBuilder.newJob(QuartzJobBasic.class).withIdentity(job.getJobName(), job.getJobGroup()).build();
					jobDetail.getJobDataMap().put(JOB_DATA_MAP_KEY_DEFAULT, job);

					// 表达式调度构建器
					CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExp()).withMisfireHandlingInstructionDoNothing();

					// 按新的cronExpression表达式构建一个新的trigger
					trigger = TriggerBuilder.newTrigger().withIdentity(job.getTriggerName(), job.getTriggerGroup()).withSchedule(scheduleBuilder).build();
					scheduler.scheduleJob(jobDetail, trigger);

				} else {
					// Trigger已存在，那么更新相应的定时设置
					// 表达式调度构建器
					CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExp()).withMisfireHandlingInstructionDoNothing();
					// 按新的cronExpression表达式重新构建trigger
					trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
					// 按新的trigger重新设置job执行
					scheduler.rescheduleJob(triggerKey, trigger);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("quartz加载失败:" + job, e);
			}
		}

	}
}
