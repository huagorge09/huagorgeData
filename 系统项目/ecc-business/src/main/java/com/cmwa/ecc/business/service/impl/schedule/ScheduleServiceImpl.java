package com.cmwa.ecc.business.service.impl.schedule;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.commonVo.ScheduleVo;
import com.cmwa.ecc.business.dao.schedule.ScheduleDao;
import com.cmwa.ecc.business.quartz.QuartzJobBasic;
import com.cmwa.ecc.business.service.schedule.ScheduleService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private ScheduleDao scheduleDao;
	
	private static Logger log = LoggerFactory.getLogger(ScheduleServiceImpl.class.getName());

	@Override
	public void stopSchedule(String jobName, String jobGroup) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		scheduler.pauseJob(jobKey);
	}

	@Override
	public void restartSchedule(String jobName, String jobGroup) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		scheduler.resumeJob(jobKey);
	}

	@Override
	public void runSchedule(String jobName, String jobGroup) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		scheduler.triggerJob(jobKey);
	}

	@Override
	public void updateCron(String taskId , String jobName, String jobGroup, String cron) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cron);
		trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(schedBuilder).build();
		scheduler.rescheduleJob(triggerKey, trigger);
		ScheduleVo schedule=new ScheduleVo();
		schedule.setTaskId(taskId);
		schedule.setCronExp(cron);
		scheduleDao.updateCron(schedule);
	}

	@Override
	public void writeLastRunnig(String jobId, String result) {
		scheduleDao.updateRunningResult(jobId, result);
	}

	@Override
	public List<ScheduleVo> queryAllSchedule() {
		return scheduleDao.queryAllSchedule();
	}
	
	/**
	 * 定时任务数据分页
	 * @author ex-weicb
	 * @createDate 2016年6月27日 下午12:14:19
	 * @param sp
	 * @return
	 */
	public Page<ScheduleVo> scheduleListPage(SearchParam param){
		//分页查询数据
		List<ScheduleVo> items =scheduleDao.scheduleListPage(param);
		return Page.create(items, param.getStart(), param.getLimit(), param.getTotal());
	}
	
	static final String JOB_DATA_MAP_KEY_DEFAULT = "cmpScheduleJob";
	
	/**
	 * 修改定时器状态
	 * @author ex-weicb
	 * @createDate 2016年6月27日 下午1:19:15
	 * @param schedule
	 */
	public void updateScheduleStatus(ScheduleVo schedule){
		String state=schedule.getState();//要修改的目标状态
		String jName=schedule.getJobName();
		String jGroup=schedule.getJobGroup();
		String tName=schedule.getTriggerName();
		String tGroup=schedule.getTriggerGroup();
		String cronExp=schedule.getCronExp();
		String classPath=schedule.getClassPath();
		Scheduler sched = schedulerFactoryBean.getScheduler();
    	JobKey jobKey = JobKey.jobKey(jName, jGroup);
    	TriggerKey triggerKey = TriggerKey.triggerKey(tName, tGroup);
		if ("0".equalsIgnoreCase(state)) {//失效
	        try {  
	        	sched.pauseJob(jobKey);
	        	sched.pauseTrigger(triggerKey);//停止触发器  
	        } catch (SchedulerException e) {   
	            throw new RuntimeException(e);   
	        }   
		}
		if ("1".equalsIgnoreCase(state)) {//生效
	        try {   
	        	if (null == sched.getJobDetail(jobKey)) {//任务不存在，就新增一个任务
					try {
//						JobDetail job = new JobDetail(jName, jGroup, Class.forName(classPath));
//						CronTrigger trigger = new CronTrigger(tName, tGroup, cronExp);
						
						// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
						CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
						// 不存在，创建一个
						if (null == trigger) {
							JobDetail jobDetail = JobBuilder.newJob(QuartzJobBasic.class).withIdentity(jName, jGroup).build();
							jobDetail.getJobDataMap().put(JOB_DATA_MAP_KEY_DEFAULT, schedule);

							// 表达式调度构建器
							CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp);

							// 按新的cronExpression表达式构建一个新的trigger
							trigger = TriggerBuilder.newTrigger().withIdentity(tName, tGroup).withSchedule(scheduleBuilder).build();
							sched.scheduleJob(jobDetail, trigger);
						} else {
							// Trigger已存在，那么更新相应的定时设置
							// 表达式调度构建器
							CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp);
							// 按新的cronExpression表达式重新构建trigger
							trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
							// 按新的trigger重新设置job执行
							sched.rescheduleJob(triggerKey, trigger);
						}
					} catch (Exception e) {
						log.error("启动定时任务失败   job名称为：" + tName + ", class路径为：" + classPath, e);
						e.printStackTrace();
					}
	        	} else {
	        		sched.resumeJob(jobKey);
	        		sched.resumeTrigger(triggerKey);//重启触发器   
	        	}
	        } catch (SchedulerException e) {   
	            throw new RuntimeException(e);   
	        }   
		}
		scheduleDao.changeState(schedule);//修改数据库该该定时任务的状态
	}

}
