package com.cmwa.ecc.business.quartz;

import java.lang.reflect.Method;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmwa.ecc.business.commonVo.ScheduleVo;
import com.cmwa.ecc.business.service.impl.schedule.ScheduleServiceImpl;
import com.cmwa.ecc.business.utils.SpringApplicationContextUtil;

@DisallowConcurrentExecution
public class QuartzJobBasic implements Job {

	private static final Logger logger = LoggerFactory.getLogger(QuartzJobBasic.class);

	private static final String SUCCESS = "执行成功!";
	private static final String ERROR = "执行失败!";

	@Override
	public void execute(JobExecutionContext context) {
		String result = SUCCESS;
		String jobId = null;
		try {
			ScheduleVo scheduleJob = (ScheduleVo) context.getMergedJobDataMap().get(LoadRepositoryJob.JOB_DATA_MAP_KEY_DEFAULT);
			jobId = scheduleJob.getTaskId();
			String jobName = scheduleJob.getJobName();
			String classPath = scheduleJob.getClassPath();
			Class<?> clazz = Class.forName(classPath);
			Object obj = SpringApplicationContextUtil.getBean(clazz);
			Method m = clazz.getMethod(jobName, null);
			m.invoke(obj, null);
		} catch (Exception e) {
			result = ERROR;
			String errmsg=e.toString();
			if (errmsg !=null && errmsg.length() > 50) {
				result +=errmsg.substring(0,50);
			}else if(errmsg !=null ){
				result +=errmsg;
			}
			logger.error("QuartzJobBase error:", e);
			e.printStackTrace();
		} finally {
			try {
				SpringApplicationContextUtil.getBean(ScheduleServiceImpl.class).writeLastRunnig(jobId, result);
			} catch (Exception e) {
				logger.error("QuartzJobBase error:", e);
				e.printStackTrace();
			}

		}
	}

}
