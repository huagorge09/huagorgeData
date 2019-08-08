package com.cmwa.ecc.business.commonVo;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("scheduleVo")
public class ScheduleVo  implements Serializable {

	private String taskId;

	private String taskName;

	private String taskDesp;

	private String jobName;

	private String jobGroup;

	private String triggerName;

	private String triggerGroup;

	private String cronExp;

	private String state;

	private String classPath;

	private String createId;

	private String createTime;

	private String modifyTime;

	private String result;
	
	
	private String stateName;//状态名称

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDesp() {
		return taskDesp;
	}

	public void setTaskDesp(String taskDesp) {
		this.taskDesp = taskDesp;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	public String getCronExp() {
		return cronExp;
	}

	public void setCronExp(String cronExp) {
		this.cronExp = cronExp;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ScheduleVo [taskId=" + taskId + ", taskName=" + taskName + ", taskDesp=" + taskDesp + ", jobName=" + jobName + ", jobGroup=" + jobGroup + ", triggerName=" + triggerName + ", triggerGroup=" + triggerGroup + ", cronExp=" + cronExp + ", state=" + state + ", classPath=" + classPath + ", createId=" + createId + ", createTime=" + createTime + ", modifyTime=" + modifyTime + ", result="
				+ result + "]";
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	
}
