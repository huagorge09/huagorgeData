package com.cmwa.ecc.business.controller.schedule;

import java.text.ParseException;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ecc.business.commonVo.ScheduleVo;
import com.cmwa.ecc.business.controller.BaseController;
import com.cmwa.ecc.business.service.schedule.ScheduleService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

@Controller("scheduleController") // 注解为Controller组件
@RequestMapping("/service/schedule") // 注解一级请求路径为base
public class ScheduleController extends BaseController{
	private static final Log log = LogFactory.getLog(ScheduleController.class);
	
	@Resource
	private ScheduleService scheduleService;
	
	/**
	 * 定时任务列表页面
	 * 
	 * @return
	 */
	@RequestMapping("/scheduleListView")
	public String scheduleListView(ModelMap model) {
		return "jsp/schedule/scheduleList";
	}
	
	/**
	 * 定时任务列表分页数据
	 * 
	 * @param sp
	 * @return
	 */
	@RequestMapping("/scheduleListPage")
	@ResponseBody
	public Page<ScheduleVo> scheduleListPage(SearchParam sp) {
		Page<ScheduleVo> list = scheduleService.scheduleListPage(sp);
		return list;
	}
	
	/**
	 * 定时任务状态改变
	 * 
	 * @return
	 */
	@RequestMapping("/updateScheduleStatus")
	@ResponseBody
	public String updateScheduleStatus(ScheduleVo schedule) {
		try {
			scheduleService.updateScheduleStatus(schedule);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			log.error("######定时任务状态改变异常######"+e);
			return e.toString();
		}
		
	}
	
	@RequestMapping("updateCron")
	@ResponseBody
	public String updateCron(String taskId , String jobName,String jobGroup,String cronExp){
		try {
			scheduleService.updateCron(taskId, jobName, jobGroup, cronExp);
			return "success";
		} catch (SchedulerException e) {
			log.error("######updateCron任务修改运行周期异常######"+e);
			e.printStackTrace();
			return e.toString();
		}
	}
	
	/**
	 * 立即运行
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	@RequestMapping("runSchedule")
	@ResponseBody
	public String runSchedule(String jobName,String jobGroup){
		try {
			scheduleService.runSchedule(jobName, jobGroup);
			return "success";
		} catch (SchedulerException e) {
			log.error("######updateCron任务立即执行异常######"+e);
			e.printStackTrace();
			return e.toString();
		}
	}
	
	/**
	 * 验证任务配置时间
	 * @param cronExp
	 * @return
	 */
	@RequestMapping("valiDateCronExp")
	@ResponseBody
	public String valiDateCronExp(String cronExp){
		String res="";
			try {
				new CronExpression(cronExp);
				res="correct";
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return res;
	}
	
}
