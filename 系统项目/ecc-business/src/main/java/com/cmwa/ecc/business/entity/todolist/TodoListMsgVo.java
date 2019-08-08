package com.cmwa.ecc.business.entity.todolist;

import org.apache.ibatis.type.Alias;

import com.cmwa.ecc.business.commonVo.BaseVo;

@Alias("todoListMsgVo")
public class TodoListMsgVo extends BaseVo {

	public TodoListMsgVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private String msgId;
	private String todoId;
	private String expectDate;
	private String isAlert;
	private String alertDate;
	private String alertType;
	//保存数据 用
	private String visBeginDate;
	private String visEndDate;
	private String todoBeginDate;
	private String todoEndDate;
	
	public String getTodoBeginDate() {
		return todoBeginDate;
	}
	public void setTodoBeginDate(String todoBeginDate) {
		this.todoBeginDate = todoBeginDate;
	}
	public String getTodoEndDate() {
		return todoEndDate;
	}
	public void setTodoEndDate(String todoEndDate) {
		this.todoEndDate = todoEndDate;
	}
	public String getVisBeginDate() {
		return visBeginDate;
	}
	public void setVisBeginDate(String visBeginDate) {
		this.visBeginDate = visBeginDate;
	}
	public String getVisEndDate() {
		return visEndDate;
	}
	public void setVisEndDate(String visEndDate) {
		this.visEndDate = visEndDate;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getTodoId() {
		return todoId;
	}
	public void setTodoId(String todoId) {
		this.todoId = todoId;
	}
	public String getExpectDate() {
		return expectDate;
	}
	public void setExpectDate(String expectDate) {
		this.expectDate = expectDate;
	}
	public String getIsAlert() {
		return isAlert;
	}
	public void setIsAlert(String isAlert) {
		this.isAlert = isAlert;
	}
	public String getAlertDate() {
		return alertDate;
	}
	public void setAlertDate(String alertDate) {
		this.alertDate = alertDate;
	}
	
	
}
